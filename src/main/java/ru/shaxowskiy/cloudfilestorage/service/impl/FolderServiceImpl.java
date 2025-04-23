package ru.shaxowskiy.cloudfilestorage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.cloudfilestorage.service.FolderStorageService;

import java.io.FileInputStream;

@Service
public class FolderServiceImpl implements FolderStorageService {

    private MinioServiceImpl minioService;

    @Autowired
    public FolderServiceImpl(MinioServiceImpl minioService) {
        this.minioService = minioService;
    }


    @Override
    public void createFolder(String objectName) {
        minioService.createFolder(objectName);
    }

    @Override
    public FileInputStream downloadFolder() {
        return null;
    }

    @Override
    public void deleteFolder() {

    }
}
