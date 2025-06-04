package ru.shaxowskiy.cloudfilestorage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.shaxowskiy.cloudfilestorage.dto.ResourceInfoDTO;
import ru.shaxowskiy.cloudfilestorage.service.impl.FileServiceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/resource")
@Slf4j
public class FileController {

    private final FileServiceImpl fileServiceImpl;

    public FileController(FileServiceImpl fileServiceImpl) {
        this.fileServiceImpl = fileServiceImpl;
    }

    @GetMapping
    public ResponseEntity<ResourceInfoDTO> infoResource(@RequestParam("path") String path){
        log.info("Get info about resource with path: {}", path);
        ResourceInfoDTO infoAboutResource = fileServiceImpl.getInfoAboutResource(path);
        return ResponseEntity.ok().body(infoAboutResource);
    }


    @PostMapping
    public ResponseEntity<ResourceInfoDTO> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                                 @RequestParam("path") String path) {
        log.info("Upload file with name: {}", multipartFile.getOriginalFilename());
        ResourceInfoDTO resource = fileServiceImpl.uploadFile(multipartFile.getOriginalFilename(), multipartFile, path);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("path") String path) throws UnsupportedEncodingException {
        String encodeObjectName = URLEncoder.encode(path, StandardCharsets.UTF_8);
        log.info("Download file with name: {}", path);
        StreamingResponseBody downloadedFile = fileServiceImpl.downloadFile(path);
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
        fileServiceImpl.deleteFile(objectName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResourceInfoDTO>> search(@RequestParam("query") String query){
        List<ResourceInfoDTO> resourceInfoDTOS = fileServiceImpl.searchResources(query);
        return ResponseEntity.ok().body(resourceInfoDTOS);
    }

    @GetMapping("/move")
    public ResponseEntity<ResourceInfoDTO> renameOrRebaseResource(@RequestParam("from") String from,
                                                                  @RequestParam("to") String to){
        ResourceInfoDTO movedResource = fileServiceImpl.copyObject(from, to);
        return ResponseEntity.ok().body(movedResource);
    }
}
