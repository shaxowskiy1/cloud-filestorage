package ru.shaxowskiy.cloudfilestorage.service;

import java.io.FileInputStream;

public interface FolderStorageService {
    void createFolder();

    FileInputStream downloadFolder();

    void deleteFolder();
}
