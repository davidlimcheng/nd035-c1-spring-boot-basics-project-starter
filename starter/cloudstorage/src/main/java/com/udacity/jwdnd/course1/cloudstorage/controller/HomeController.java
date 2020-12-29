package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final CredentialService credentialService;
    private final FileService fileService;
    private final NoteService noteService;

    public HomeController(CredentialService credentialService, FileService fileService, NoteService noteService) {
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.noteService = noteService;
    }

    @GetMapping()
    public String homeView(Authentication auth, Model model) {
        String currentUsername = auth.getName();
        addFilesToModel(model, currentUsername);

        return "/home";
    }

    // Files

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "filename") String fileName) {
        File file = fileService.getFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getFileSize())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }

    @PostMapping("/file/upload")
    public String handleFileUpload(Authentication auth, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) {
        String currentUsername = auth.getName();

        if (!fileService.isFileNameAvailable(fileUpload.getOriginalFilename())) {
            model.addAttribute("fileErrorMessage", "You already have a file of that name. Please rename file before uploading.");
            addFilesToModel(model, currentUsername);
            return "/home";
        }

        int rowsAdded = fileService.addFile(currentUsername, fileUpload);

        if (rowsAdded < 1) {
            model.addAttribute("fileErrorMessage", "Could not successfully add file. Please try again.");
        }

        addFilesToModel(model, currentUsername);
        return "/home";
    }

    @PostMapping("/file/delete/{fileId}")
    public String handleFileDelete(Authentication auth, @PathVariable("fileId") int fileId, Model model) {
        String currentUsername = auth.getName();
        int rowsDeleted = fileService.deleteFile(fileId, currentUsername);

        if (rowsDeleted < 0) {
            model.addAttribute("fileErrorMessage", "Could not successfully delete file. Please try again.");
        }

        addFilesToModel(model, currentUsername);
        return "/home";
    }

    // Notes

    @PostMapping("/note")
    public String handleAddNote(Authentication auth, @ModelAttribute("noteData") NoteForm noteData, Model model) {
        String currentUsername = auth.getName();

        // if there is no id, create a new note
        if (noteData.getStringId().length() == 0) {
            int rowsAdded = noteService.addNote(noteData, currentUsername);

            if (rowsAdded < 1) {
                model.addAttribute("noteErrorMessage", "Could not successfully add note. Please try again.");
            }
        } else {
            // otherwise, modify the existing note in database
            int rowsChanged = noteService.editNote(noteData, currentUsername);

            if (rowsChanged < 1) {
                model.addAttribute("noteErrorMessage", "Could not successfully update note. Please try again.");
            }
        }

        addFilesToModel(model, currentUsername);
        return "/home";
    }

    @PostMapping("/note/delete/{noteId}")
    public String handleNoteDelete(Authentication auth, @PathVariable("noteId") int noteId, Model model) {
        String currentUsername = auth.getName();
        int rowsDeleted = noteService.deleteNote(noteId, currentUsername);

        if (rowsDeleted < 1) {
            model.addAttribute("noteErrorMessage", "Could not successfully delete note. Please try again.");
        }

        addFilesToModel(model, currentUsername);
        return "/home";
    }

    @ModelAttribute("noteData")
    public NoteForm noteForm() {
        return new NoteForm();
    }

    // Credentials

    @PostMapping("/credential")
    public String handleAddCredential(Authentication auth, @ModelAttribute("credentialData") CredentialForm credentialData, Model model) {
        String currentUsername = auth.getName();

        // if there is no id, create a new credential
        if (credentialData.getStringId().length() == 0) {
            int rowsAdded = credentialService.addCredential(credentialData, currentUsername);

            if (rowsAdded < 1) {
                model.addAttribute("credentialErrorMessage", "Could not successfully add credential. Please try again.");
            }
        } else {
            int rowsChanged = credentialService.editCredential(credentialData, currentUsername);

            if (rowsChanged < 1) {
                model.addAttribute("credentialErrorMessage", "Could not successfully update credential. Please try again.");
            }
        }

        addFilesToModel(model, currentUsername);
        return "/home";
    }

    @PostMapping("/credential/delete/{credentialId}")
    public String handleCredentialDelete(Authentication auth, @PathVariable("credentialId") int credentialId, Model model) {
        String currentUsername = auth.getName();
        int rowsDeleted = credentialService.deleteCredential(credentialId, currentUsername);

        if (rowsDeleted < 1) {
            model.addAttribute("credentialErrorMessage", "Could not successfully delete credential. Please try again.");
        }

        addFilesToModel(model, currentUsername);
        return "/home";
    }

    @ModelAttribute("credentialData")
    public CredentialForm credentialForm() {
        return new CredentialForm();
    }

    private void addFilesToModel(Model model, String username) {
        Credential[] userCredentials = credentialService.getAllUserCredentials(username);
        File[] userFiles = fileService.getAllUserFiles(username);
        Note[] userNotes = noteService.getAllUserNotes(username);

        model.addAttribute("files", userFiles);
        model.addAttribute("notes", userNotes);
        model.addAttribute("credentials", userCredentials);
    }
}
