package ru.shaxowskiy.cloudfilestorage.service;

import io.minio.errors.*;
import ru.shaxowskiy.cloudfilestorage.models.User;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface BucketStorageService {
    void createBucket(User user);
}
