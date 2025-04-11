package ru.shaxowskiy.cloudfilestorage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.shaxowskiy.cloudfilestorage.dto.ResourceInfoDTO;
import ru.shaxowskiy.cloudfilestorage.service.FileService;
import ru.shaxowskiy.cloudfilestorage.service.MinioServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/resource")
@Slf4j

public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<ResourceInfoDTO> infoResource(@RequestParam("path") String path){
        log.info("Get info about resource with path: {}", path);
        ResourceInfoDTO infoAboutResource = fileService.getInfoAboutResource(path);
        return ResponseEntity.ok().body(infoAboutResource);
    }


    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        log.info("Upload file with name: {}", multipartFile.getOriginalFilename());
        fileService.uploadFile(multipartFile.getOriginalFilename(), multipartFile);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("objectName") String objectName) throws UnsupportedEncodingException {
        String encodeObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8);
        log.info("Download file with name: {}", objectName);
        StreamingResponseBody downloadedFile = fileService.downloadFile(objectName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Content-Disposition", "attachment; filename=" + encodeObjectName);
        headers.add("Pragma", "no-cache");
        headers.add("Cache-Control", "no-cache");
        return ResponseEntity.ok().headers(headers).body(downloadedFile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@RequestParam("objectName") String objectName) {
        log.info("Delete file with name: {}", objectName);
        fileService.deleteFile(objectName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResourceInfoDTO>> search(@RequestParam("query") String query){
        List<ResourceInfoDTO> resourceInfoDTOS = fileService.searchResources(query);
        return ResponseEntity.ok().body(resourceInfoDTOS);
    }
}
