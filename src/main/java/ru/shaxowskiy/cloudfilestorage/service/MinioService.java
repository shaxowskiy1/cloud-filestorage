package ru.shaxowskiy.cloudfilestorage.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.shaxowskiy.cloudfilestorage.models.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService implements FileStorageService, BucketStorageService, FolderStorageService {

    private final MinioClient minioClient;
    private final String BUCKET_NAME;

    @Autowired
    public MinioService(MinioClient minioClient, @Value("${minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        BUCKET_NAME = bucketName;
    }


    @Override
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

    private boolean isBucketExist(String bucketName) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileInputStream downloadFile() {
        return null;
    }

    @Override
    public void deleteFile() {

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
}