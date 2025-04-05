package ru.shaxowskiy.cloudfilestorage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

public interface FileStorageService {
    void uploadFile(String objectName, MultipartFile multipartFile);

    FileInputStream downloadFile();

    void deleteFile();

}
