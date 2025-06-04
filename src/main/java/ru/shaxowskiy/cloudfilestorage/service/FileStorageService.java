package ru.shaxowskiy.cloudfilestorage.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.shaxowskiy.cloudfilestorage.dto.ResourceInfoDTO;

import java.io.FileInputStream;
import java.io.InputStream;

public interface FileStorageService {
    ResourceInfoDTO uploadFile(String objectName, MultipartFile multipartFile, String path);

    StreamingResponseBody downloadFile(String objectName);

    void deleteFile(String objectName);

}
