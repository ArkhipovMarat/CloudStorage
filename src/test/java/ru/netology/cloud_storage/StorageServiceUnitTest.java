package ru.netology.cloud_storage;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import ru.netology.cloud_storage.service.app.StorageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StorageServiceUnitTest {
    @TempDir
    File tempDir;

    @Autowired
    StorageService storageService;

    private static final String TEMP_FILE_NAME = "tempFile.txt";
    private static final String TEMP_NEW_FILE_NAME = "tempNewFile.txt";
    private static final String TEMP_FILE_CONTENT = "abc";
    private static final List<String> TEST_FILE_CONTENT = List.of("abc");

    @Test
    void tempDirShouldBeADirectory() {
        Assertions.assertTrue(this.tempDir.isDirectory(), "should be a directory");
    }


    @Test
    void whenStoreFile_thenStoreIsSuccess_andContentIsCorrect() throws IOException {
        // creating test file
        MockMultipartFile tempFile = new MockMultipartFile(TEMP_FILE_NAME, TEMP_FILE_CONTENT.getBytes());

        storageService.store(tempFile, TEMP_FILE_NAME, tempDir.toPath().resolve(TEMP_FILE_NAME));

        assertAll(
                () -> assertTrue(Files.exists(tempDir.toPath().resolve(TEMP_FILE_NAME))),
                () -> assertLinesMatch(TEST_FILE_CONTENT, Files.readAllLines(tempDir.toPath().resolve(TEMP_FILE_NAME)))
        );
    }

    @Test
    void whenDeleteFile_thenDeleteIsSuccess() throws IOException {
        // creating test file
        File tempFile = createTempFile();

        storageService.deleteFile(tempFile.toPath());

        assertFalse(Files.exists(tempFile.toPath()));
    }

    @Test
    void whenRenameFile_thenRenameIsSuccess() throws IOException {
        // creating test file
        File tempFile = createTempFile();

        storageService.renameFile(tempFile.toPath(), TEMP_NEW_FILE_NAME);

        assertTrue(Files.exists(tempDir.toPath().resolve(TEMP_NEW_FILE_NAME)));
    }

    @Test
    void whenLoadAsResource_thenLoadIsSuccess_andLoadIsCorrect_andContentIsCorrect() throws IOException {
        // creating test file
        File tempFile = createTempFile();

        Resource resource = storageService.loadAsResource(TEMP_FILE_NAME, tempFile.toPath());

        assertAll(
                () -> assertTrue(resource.exists()),
                () -> assertTrue(resource.isReadable()),
                () -> assertTrue(resource.isFile()),
                () -> assertLinesMatch(TEST_FILE_CONTENT, Files.readAllLines(resource.getFile().toPath()))
        );
    }

    private File createTempFile() throws IOException {
        File tempFile = new File(tempDir, TEMP_FILE_NAME);
        Files.write(tempFile.toPath(), TEMP_FILE_CONTENT.getBytes());
        return tempFile;
    }
}
