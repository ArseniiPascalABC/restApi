package com.sss.restapi.controllers.rest;

import com.sss.restapi.services.rest.FolderUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rest/folders")
public class FolderUploadController {
    private final FolderUploadService folderUploadService;
    @PostMapping("/uploadFolder")
    public String handleFolderUpload(@RequestParam("folder") MultipartFile folder) {
        if (folder.isEmpty()) {
            return "Пожалуйста, выберите файл для загрузки.";
        }

        try {
            Path tempDir = folderUploadService.createTempDirectory();
            File tempFolder = tempDir.toFile();
            folder.transferTo(tempFolder);
            Path targetDir = Paths.get("restApi/src/main/resources/static/files/" + folder.getOriginalFilename());
            folderUploadService.copyFolderContents(tempDir, targetDir);
            folderUploadService.deleteTempDirectory(tempDir);
            return "Папка успешно загружена и сохранена в проекте.";
        } catch (IOException e) {
            log.error("Ошибка загрузки и сохранения папки: " + e.getMessage());
            return "Ошибка загрузки и сохранения папки";
        }
    }

}
