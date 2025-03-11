package com.example.moattravel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.example.moattravel.entity.House;
import com.example.moattravel.form.HouseEditForm;
import com.example.moattravel.form.HouseRegisterForm;
import com.example.moattravel.repository.HouseRepository;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @InjectMocks
    private HouseService houseService;  // テスト対象のクラス

    @Mock
    private HouseRepository houseRepository;  // モック化

    @Spy
    private HouseService spyHouseService = new HouseService(houseRepository);  // Spyを使ってメソッドの呼び出しを確認

    @Test
    void testCreateHouse_WithImage() throws IOException {
        // ダミーフォーム作成
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setDescription("Test Description");
        form.setPrice(10000);
        form.setCapacity(4);
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("090-1234-5678");

        // MockのMultipartFileを作成
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("image.jpg");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        form.setImageFile(mockFile);

        // 実行
        houseService.create(form);

        // houseRepository.save() が1回呼ばれたか確認
        verify(houseRepository, times(1)).save(any(House.class));

        // 画像がある場合に copyImageFile() が呼ばれたか確認
        verify(spyHouseService, times(1)).copyImageFile(eq(mockFile), any(Path.class));
    }

    @Test
    void testUpdateHouse_WithImage() throws IOException {
        // モックの House インスタンスを作成
        House mockHouse = new House();
        mockHouse.setId((int) 1L);
        mockHouse.setName("Old House");

        // リポジトリのモック設定
        when(houseRepository.getReferenceById((int) 1L)).thenReturn(mockHouse);

        // フォームデータを作成
        HouseEditForm form = new HouseEditForm();
        form.setId((int) 1L);
        form.setName("Updated House");
        form.setDescription("Updated Description");
        form.setPrice(12000);
        form.setCapacity(5);
        form.setPostalCode("987-6543");
        form.setAddress("Updated Address");
        form.setPhoneNumber("080-9876-5432");

        // MockのMultipartFileを作成
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("new_image.jpg");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        form.setImageFile(mockFile);

        // 実行
        houseService.update(form);

        // 更新後のデータが正しくセットされたか確認
        assertEquals("Updated House", mockHouse.getName());
        assertEquals("Updated Description", mockHouse.getDescription());
        assertEquals(12000, mockHouse.getPrice());

        // houseRepository.save() が1回呼ばれたか確認
        verify(houseRepository, times(1)).save(mockHouse);

        // 画像がある場合に copyImageFile() が呼ばれたか確認
        verify(spyHouseService, times(1)).copyImageFile(eq(mockFile), any(Path.class));
    }

    @Test
    void testGenerateNewFileName() {
        String originalName = "test.jpg";
        String generatedName = houseService.generateNewFileName(originalName);

        // ファイル名が変更されているか（UUIDが含まれるか）
        assertNotEquals(originalName, generatedName);
        assertTrue(generatedName.endsWith(".jpg"));
    }
}
