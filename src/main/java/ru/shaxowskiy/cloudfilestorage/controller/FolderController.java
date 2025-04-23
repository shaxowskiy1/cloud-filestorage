package ru.shaxowskiy.cloudfilestorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.shaxowskiy.cloudfilestorage.service.impl.FolderServiceImpl;

@RestController
@RequestMapping("/directory")
public class FolderController {

    private FolderServiceImpl folderService;

    @Autowired
    public FolderController(FolderServiceImpl folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createFolder(@RequestParam("objectName") String objectName){
        folderService.createFolder(objectName);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
