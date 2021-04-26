package ru.netology.cloud_storage;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import ru.netology.cloud_storage.model.FileStorageData;
import ru.netology.cloud_storage.model.Role;
import ru.netology.cloud_storage.model.UserEntity;
import ru.netology.cloud_storage.repository.FileStorageRepository;
import ru.netology.cloud_storage.repository.UserRepository;
import ru.netology.cloud_storage.service.app.FileService;
import ru.netology.cloud_storage.service.app.StorageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileServiceUnitTest {
    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_NEW_FILE_NAME = "testNewFile.txt";
    private static final String TEST_FILE_CONTENT = "abc";
    private static final String USER_LOGIN = "user1";
    private static final String USER_PASSWORD = "password1";
    private static final String USER_ROLE = "ROLE_USER";
    private static final String SORT_ELEMENT_NAME = "filename";

    // this may be different!!
    private static final Path ROOT_LOCATION = Paths.get("/Users/Uploads");
    private static final Path ABSOLUTE_PATH = Paths.get("D:/Users/Uploads");

    @Autowired
    private FileService fileService;

    @MockBean
    private StorageService storageService;

    @MockBean
    private FileStorageRepository fileStorageRepository;

    @MockBean
    private UserRepository userRepository;

    @WithMockUser(username = USER_LOGIN)
    @Test
    void postFile() {
        // creating test data
        MockMultipartFile testFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT.getBytes());
        FileStorageData testFileStorageData = getTestFileStorageData(testFile);

        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(getTestUserEntity()));
        when(fileStorageRepository.saveAndFlush(testFileStorageData)).thenReturn(null);
        doNothing().when(storageService).store(testFile, TEST_FILE_NAME, getTestRootLocation());

        fileService.postFile(TEST_FILE_NAME, testFile);

        InOrder inOrder = Mockito.inOrder(fileStorageRepository, storageService);
        inOrder.verify(fileStorageRepository).saveAndFlush(testFileStorageData);
        inOrder.verify(storageService).store(testFile, TEST_FILE_NAME, getTestRootLocation());
    }

    @WithMockUser(username = USER_LOGIN)
    @Test
    public void deleteFile() {
        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(getTestUserEntity()));
        doNothing().when(fileStorageRepository).removeFileDataByFilename(TEST_FILE_NAME);
        doNothing().when(storageService).deleteFile(getTestRootLocation());

        fileService.deleteFile(TEST_FILE_NAME);

        InOrder inOrder = Mockito.inOrder(fileStorageRepository, storageService);
        inOrder.verify(fileStorageRepository).removeFileDataByFilename(TEST_FILE_NAME);
        inOrder.verify(storageService).deleteFile(getTestRootLocation());
    }

    @WithMockUser(username = USER_LOGIN)
    @Test
    public void getFile() {
        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(getTestUserEntity()));

        when(storageService.loadAsResource(TEST_FILE_NAME, getTestRootLocation())).thenReturn(null);

        fileService.getFile(TEST_FILE_NAME);

        verify(storageService).loadAsResource(TEST_FILE_NAME, getTestRootLocation());
    }

    @WithMockUser(username = USER_LOGIN)
    @Test
    public void getList() {
        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(getTestUserEntity()));

        fileService.getList();

        verify(fileStorageRepository).findAllByUser_Login(USER_LOGIN, Sort.by(SORT_ELEMENT_NAME));
    }

    @WithMockUser(username = USER_LOGIN)
    @Test
    public void putFile() {
        // creating test data
        MockMultipartFile testFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT.getBytes());
        FileStorageData testFileStorageData = getTestFileStorageData(testFile);
        FileStorageData newTestFileStorageData = getTestFileStorageData(testFile);
        newTestFileStorageData.setFilename(TEST_NEW_FILE_NAME);

        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(getTestUserEntity()));
        when(fileStorageRepository.findByFilenameAndUser(TEST_FILE_NAME, getTestUserEntity())).thenReturn(testFileStorageData);
        when(fileStorageRepository.saveAndFlush(newTestFileStorageData)).thenReturn(null);
        doNothing().when(storageService).renameFile(getTestRootLocation(), TEST_NEW_FILE_NAME);

        fileService.putFile(TEST_FILE_NAME, TEST_NEW_FILE_NAME);

        InOrder inOrder = Mockito.inOrder(fileStorageRepository, storageService);
        inOrder.verify(fileStorageRepository).findByFilenameAndUser(TEST_FILE_NAME, getTestUserEntity());
        inOrder.verify(fileStorageRepository).saveAndFlush(newTestFileStorageData);
        inOrder.verify(storageService).renameFile(getTestRootLocation(), TEST_NEW_FILE_NAME);
    }

    private FileStorageData getTestFileStorageData(MockMultipartFile tempFile) {
        return new FileStorageData(TEST_FILE_NAME,
                ABSOLUTE_PATH.resolve(USER_LOGIN).resolve(TEST_FILE_NAME).toString(),
                tempFile.getSize(),
                getTestUserEntity());
    }

    private UserEntity getTestUserEntity() {
       return new UserEntity(USER_LOGIN,USER_PASSWORD,new Role(1L, USER_ROLE));
    }

    private Path getTestRootLocation() {
       return ROOT_LOCATION.resolve(USER_LOGIN).resolve(TEST_FILE_NAME);
    }
}
