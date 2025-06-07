package ru.shaxowskiy.cloudfilestorage.exceptions;

public class FileNotExistException extends RuntimeException{
    public FileNotExistException(String message) {
        super(message);
    }
}
