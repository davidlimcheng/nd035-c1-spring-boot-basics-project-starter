package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.data.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.data.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public Credential[] getAllUserCredentials(String username) {
        User user = userMapper.getUser(username);
        Credential[] userCredentials = credentialMapper.getCredentials(user.getUserId());

        for (Credential credential : userCredentials) {
            String decryptedPassword = getDecryptedPassword(credential);
            credential.setDecryptedPassword(decryptedPassword);
        }

        return userCredentials;
    }

    public int addCredential(CredentialForm credentialData, String currentUsername) {
        User currentUser = userMapper.getUser(currentUsername);

        String url = credentialData.getUrl();
        String username = credentialData.getUsername();
        String password = credentialData.getPassword();

        String encodedKey = getEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        Credential newCredential = new Credential();
        newCredential.setUrl(url);
        newCredential.setUsername(username);
        newCredential.setKey(encodedKey);
        newCredential.setPassword(encryptedPassword);
        newCredential.setUserId(currentUser.getUserId());
        return credentialMapper.insert(newCredential);
    }

    public int deleteCredential(int credentialId, String username) {
        User user = userMapper.getUser(username);

        return credentialMapper.deleteCredential(user.getUserId(), credentialId);
    }

    public int editCredential(CredentialForm credentialData, String currentUsername) {
        User currentUser = userMapper.getUser(currentUsername);
        Credential credential = credentialMapper.getCredential(currentUser.getUserId(), Integer.parseInt(credentialData.getStringId()));

        String url = credentialData.getUrl();
        String username = credentialData.getUsername();
        String password = credentialData.getPassword();

        String encodedKey = getEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        credential.setUrl(url);
        credential.setUsername(username);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        return credentialMapper.updateCredential(credential);
    }

    private String getDecryptedPassword(Credential credential) {
        String encryptedPassword = credential.getPassword();
        String key = credential.getKey();

        return encryptionService.decryptValue(encryptedPassword, key);
    }

    private String getEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }


}
