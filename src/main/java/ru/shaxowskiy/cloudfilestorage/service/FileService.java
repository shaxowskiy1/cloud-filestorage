package ru.shaxowskiy.cloudfilestorage.service;

import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        return outputStream -> {
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
    }

    @Override
    public void deleteFile(String objectName) {
        minioService.deleteFile(objectName);
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
        boolean isDirectory = path.endsWith("/");
        if(isDirectory){
            return ResourceInfoDTO.builder()
                    .path(path)
                    .name(extractFileNameFromPath(path))
                    .type(ResourseType.DIRECTORY)
                    .build();
        }

        StatObjectResponse resourceInfo = minioService.statObject(path);

        return ResourceInfoDTO.builder()
                .path(path)
                .size(resourceInfo.size())
                .name(extractFileNameFromPath(path))
                .type(ResourseType.FILE)
                .build();
    }

    private String extractFileNameFromPath(String path) {
        return Paths.get(path).getFileName().toString();
    }


    public List<ResourceInfoDTO> searchResources(String query) {
        Iterable<Result<Item>> results = minioService.listObjects();
        ArrayList<ResourceInfoDTO> objects = new ArrayList<>();
        for(Result<Item> result : results){
            try {
                String fileName = result.get().objectName();
                if(fileName.contains(query))
                {
                    log.info("Searching object is {}", result.get().objectName());
                    StatObjectResponse metaInfoAboutObject = minioService.statObject(fileName);
                    objects.add(getInfoAboutResource(metaInfoAboutObject.object()));
                }
            } catch (Exception e) {
                log.error("Failed the search: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return objects;
    }
}
