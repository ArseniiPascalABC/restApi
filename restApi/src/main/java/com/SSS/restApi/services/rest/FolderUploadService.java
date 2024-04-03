package com.sss.restapi.services.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class FolderUploadService {
    public Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("tempFolder");
    }
    public void copyFolderContents(Path sourceDir, Path targetDir) throws IOException {
        try (Stream<Path> walk = Files.walk(sourceDir)) {
            walk.filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        Path relativePath = sourceDir.relativize(path);
                        Path targetFile = targetDir.resolve(relativePath);
                        try {
                            Files.createDirectories(targetFile.getParent());
                            Files.copy(path, targetFile);
                        } catch (IOException e) {
                            log.error("Ошибка при копировании файлов: " + e.getMessage());
                        }
                    });
        }
    }
    public void deleteTempDirectory(Path tempDir) throws IOException {
        FileSystemUtils.deleteRecursively(tempDir);
    }
}
