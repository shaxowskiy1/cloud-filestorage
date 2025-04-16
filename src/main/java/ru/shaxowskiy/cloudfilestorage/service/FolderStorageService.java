package ru.shaxowskiy.cloudfilestorage.service;

import java.io.FileInputStream;

public interface FolderStorageService {
    void createFolder(String objectName);

    FileInputStream downloadFolder();

    void deleteFolder();
}
