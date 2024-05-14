package com.sss.restapi.controllers.rest;

import com.sss.restapi.services.rest.FolderUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class FolderUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FolderUploadService folderUploadService;

    @Test
    void testHandleFolderUpload() throws Exception {
        MockMultipartFile folder = new MockMultipartFile("folder", "testFolder", "text/plain", "Test content".getBytes());
        Path tempDirPath = Files.createTempDirectory("test");
        File tempDir = tempDirPath.toFile();
        when(folderUploadService.createTempDirectory()).thenReturn(tempDir.toPath());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/rest/folders/uploadFolder")
                        .file(folder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ошибка загрузки и сохранения папки"));

    }

    @Test
    void testHandleFolderUploadEmptyFolder() throws Exception {
        MockMultipartFile folder = new MockMultipartFile("folder", "", "text/plain", "".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/rest/folders/uploadFolder")
                        .file(folder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Пожалуйста, выберите файл для загрузки."));

        verify(folderUploadService, never()).createTempDirectory();
        verify(folderUploadService, never()).copyFolderContents(any(Path.class), any(Path.class));
        verify(folderUploadService, never()).deleteTempDirectory(any(Path.class));
    }
}
