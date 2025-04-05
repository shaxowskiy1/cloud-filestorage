package ru.shaxowskiy.cloudfilestorage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shaxowskiy.cloudfilestorage.service.MinioService;

@RestController
@RequestMapping("/resource")
@Slf4j
public class FileController {

    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam("file") MultipartFile multipartFile){
        log.info("Upload file with name: {}", multipartFile.getOriginalFilename());
        minioService.uploadFile(multipartFile.getOriginalFilename(), multipartFile);
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
