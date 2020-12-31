package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.data.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.data.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;
import java.util.Objects;

@Service
public class FileService {
    private final long MAXIMUM_FILE_SIZE = 10485760;
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    @Autowired
    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public int addFile(String username, MultipartFile file)  {
        User user = userMapper.getUser(username);
        int userId = user.getUserId();
        try {
            File newFile = new File();
            newFile.setFileName(file.getOriginalFilename());
            newFile.setContentType(file.getContentType());
            newFile.setFileSize(file.getSize());
            newFile.setFileData(file.getBytes());
            newFile.setUserId(userId);

            return fileMapper.insert(newFile);
        } catch (IOException e) {
            return 0;
        }
    }

    public File[] getAllUserFiles(String username) {
        User user = userMapper.getUser(username);
        return fileMapper.getFiles(user.getUserId());
    }

    public File getFile(String fileName) {
        return fileMapper.getFile(fileName);
    }

    public int deleteFile(int fileId, String username) {
        User currentUser = userMapper.getUser(username);
        return fileMapper.deleteFile(fileId, currentUser.getUserId());
    }

    public boolean isFileNameAvailable(String fileName) {
        return fileMapper.getFile(fileName) == null;
    }

    public boolean fileSizeExceedsMaximum(MultipartFile file) {
        return file.getSize() > MAXIMUM_FILE_SIZE;
    }

    public boolean isFileEmpty(MultipartFile file) { return Objects.requireNonNull(file.getOriginalFilename()).isEmpty(); }
}
