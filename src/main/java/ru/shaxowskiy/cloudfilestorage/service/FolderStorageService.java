package ru.shaxowskiy.cloudfilestorage.service;

import ru.shaxowskiy.cloudfilestorage.dto.ResourceInfoDTO;

import java.io.FileInputStream;

public interface FolderStorageService {
    ResourceInfoDTO createFolder(String objectName);

    FileInputStream downloadFolder();

    void deleteFolder();
}
