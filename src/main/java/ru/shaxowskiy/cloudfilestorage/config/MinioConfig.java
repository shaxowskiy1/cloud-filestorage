package ru.shaxowskiy.cloudfilestorage.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {

    @Value("${minio.user}")
    private String minioUsername;
    @Value("${minio.password}")
    private String minioPassword;
    @Value("${minio.url}")
    private String minioUrl;

    @Bean
    public MinioClient minioClient() throws IOException {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(minioUrl)
                        .credentials(minioUsername, minioPassword)
                        .build();

        return minioClient;
    }
}
