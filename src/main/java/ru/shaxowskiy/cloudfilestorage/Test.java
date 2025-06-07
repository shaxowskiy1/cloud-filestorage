package ru.shaxowskiy.cloudfilestorage;

import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) {
        String path = "/folders/myPath1/myPath2/";
        System.out.println(Paths.get(path).getFileName());

    }
}
