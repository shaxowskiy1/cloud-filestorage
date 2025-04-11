package ru.shaxowskiy.cloudfilestorage;

import java.nio.file.Paths;

public class test {
    public static void main(String[] args) {
        System.out.println(Paths.get("/folder1/folder2/Docker.pdf").getFileName().toString());
    }
}
