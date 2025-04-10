package ru.shaxowskiy.cloudfilestorage.service;

import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.shaxowskiy.cloudfilestorage.dto.ResourceInfoDTO;
import ru.shaxowskiy.cloudfilestorage.models.ResourseType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class FileService implements FileStorageService, FolderStorageService {

    private final Integer BUFFER_SIZE = 1024 * 1024;
    private final MinioServiceImpl minioService;

    @Autowired
    public FileService(MinioServiceImpl minioService) {
        this.minioService = minioService;
    }

    @Override
    public void uploadFile(String objectName, MultipartFile multipartFile) {
        minioService.uploadFile(objectName, multipartFile);
    }

    @Override
    public StreamingResponseBody downloadFile(String objectName) {
        final StreamingResponseBody fileDownloadStream = outputStream -> {
            try (InputStream inputStream = minioService.downloadFile(objectName)) {

                int nRead;
                byte[] data = new byte[BUFFER_SIZE];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                }
            } catch (IOException e) {
                log.error("Failed downloading file: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        };
        return fileDownloadStream;
    }

    @Override
    public void deleteFile(String objectName) {

    }

    @Override
    public void createFolder() {

    }

    @Override
    public FileInputStream downloadFolder() {
        return null;
    }

    @Override
    public void deleteFolder() {

    }

    public ResourceInfoDTO getInfoAboutResource(String path) {
        StatObjectResponse resourceInfo = minioService.statObject(path);
        if (path.charAt(path.length() - 1) == '/') {
            return ResourceInfoDTO.builder()
                    .name(null)
                    .size(resourceInfo.size())
                    .type(ResourseType.DIRECTORY)
                    .path(path)
                    .build();
        }
        return ResourceInfoDTO.builder()
                .name(null)
                .size(resourceInfo.size())
                .type(ResourseType.FILE)
                .path(path)
                .build();
    }
}
