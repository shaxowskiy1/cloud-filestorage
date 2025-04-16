package ru.shaxowskiy.cloudfilestorage.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.shaxowskiy.cloudfilestorage.models.User;
import ru.shaxowskiy.cloudfilestorage.service.MinioService;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private final String BUCKET_NAME;

    @Autowired
    public MinioServiceImpl(MinioClient minioClient, @Value("${minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        BUCKET_NAME = bucketName;
    }

    public void createBucket(User user) {
        try {
            boolean found = isBucketExist(BUCKET_NAME);
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException |
                 IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isBucketExist(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().
                    bucket(bucketName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void uploadFile(String objectName, MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build());
        } catch (Exception e) {
            log.error("Unsuccessful upload file");
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("Unsuccessful download file");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("Failed to delete file");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFolder(String objectName) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName + "/")
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build());
        } catch (Exception e) {
            log.error("Create folder error {}", e.getMessage());
        }
    }

    @Override
    public FileInputStream downloadFolder() {
        return null;
    }

    @Override
    public void deleteFolder() {

    }

    @SneakyThrows
    public StatObjectResponse statObject(String path) {
        return minioClient.statObject(StatObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(path)
                .build());
    }

    public Iterable<Result<Item>> listObjects() {
        return minioClient.listObjects(ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .recursive(true)
                .build());
    }
}