package ru.shaxowskiy.cloudfilestorage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.cloudfilestorage.dto.ResourceInfoDTO;
import ru.shaxowskiy.cloudfilestorage.service.FolderStorageService;

import java.io.FileInputStream;

@Service
public class FolderServiceImpl implements FolderStorageService {

    private FileServiceImpl fileService;
    private MinioServiceImpl minioService;

    @Autowired
    public FolderServiceImpl(FileServiceImpl fileService, MinioServiceImpl minioService) {
        this.fileService = fileService;
        this.minioService = minioService;
    }


    @Override
    public ResourceInfoDTO createFolder(String path) {
        minioService.createFolder(path);
        return fileService.getInfoAboutResource(path);
    }

    @Override
    public FileInputStream downloadFolder() {
        return null;
    }

    @Override
    public void deleteFolder() {

    }
}
