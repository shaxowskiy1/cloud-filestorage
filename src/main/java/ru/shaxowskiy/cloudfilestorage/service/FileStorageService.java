package ru.shaxowskiy.cloudfilestorage.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.InputStream;

public interface FileStorageService {
    void uploadFile(String objectName, MultipartFile multipartFile);

    StreamingResponseBody downloadFile(String objectName);

    void deleteFile(String objectName);

}
