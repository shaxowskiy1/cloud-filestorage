package ru.shaxowskiy.cloudfilestorage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.shaxowskiy.cloudfilestorage.service.MinioService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/resource")
@Slf4j
//TODO: вынести логику обработки в FileService. 1 - добавить методы обрбаотки папок.
// Добавить path в виде requestParam до файла и парсинг пути
public class FileController {

    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        log.info("Upload file with name: {}", multipartFile.getOriginalFilename());
        minioService.uploadFile(multipartFile.getOriginalFilename(), multipartFile);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("objectName") String objectName) throws UnsupportedEncodingException {
        log.info("Download file with name: {}", objectName);
        InputStream inputStream = minioService.downloadFile(objectName);
        final StreamingResponseBody out =
                outputStream -> {
                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        outputStream.write(data, 0, nRead);
                    }
                };
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Content-Disposition", "attachment; filename=" + objectName);
        headers.add("Pragma", "no-cache");
        headers.add("Cache-Control", "no-cache");
        return ResponseEntity.ok().headers(headers).body(out);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteFile(@RequestParam("objectName") String objectName) {
        log.info("Delete file with name: {}", objectName);
        minioService.deleteFile(objectName);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
