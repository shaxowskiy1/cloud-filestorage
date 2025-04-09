package ru.shaxowskiy.cloudfilestorage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

public interface FileStorageService {
    void uploadFile(String objectName, MultipartFile multipartFile);

    InputStream downloadFile(String objectName);

    void deleteFile(String objectName);

}
