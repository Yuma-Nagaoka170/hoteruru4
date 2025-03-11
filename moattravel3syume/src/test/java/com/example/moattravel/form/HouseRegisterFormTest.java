package com.example.moattravel.form;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class HouseRegisterFormTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // バリデータのセットアップ
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidForm() {
        // 正常なデータを持つフォームを作成
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setImageFile(new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]));
        form.setDescription("This is a test description.");
        form.setPrice(10000);
        form.setCapacity(4);
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("090-1234-5678");

        // バリデーション実行
        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        // バリデーションエラーがないことを確認
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidForm_MissingFields() {
        // 不正なデータを持つフォームを作成（すべて空）
        HouseRegisterForm form = new HouseRegisterForm();

        // バリデーション実行
        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        // バリデーションエラーが発生していることを確認
        assertEquals(7, violations.size()); // 必須フィールドが6つあるため6件のエラーが期待される
    }

    @Test
    void testInvalidPrice() {
        // 宿泊料金が不正（0円以下）
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setImageFile(new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]));
        form.setDescription("This is a test description.");
        form.setPrice(0); // 不正な値（1以上でない）
        form.setCapacity(4);
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("090-1234-5678");

        // バリデーション実行
        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        // バリデーションエラーが1件出ることを確認（priceフィールド）
        assertEquals(1, violations.size());
        
        ConstraintViolation<HouseRegisterForm> violation = violations.iterator().next();
        assertEquals("宿泊料金は1円以上に設定してください。", violation.getMessage());
    }

    @Test
    void testInvalidCapacity() {
        // 定員が不正（0人以下）
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setImageFile(new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]));
        form.setDescription("This is a test description.");
        form.setPrice(10000);
        form.setCapacity(0); // 不正な値（1以上でない）
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("090-1234-5678");

        // バリデーション実行
        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        // バリデーションエラーが1件出ることを確認（capacityフィールド）
        assertEquals(1, violations.size());
        
        ConstraintViolation<HouseRegisterForm> violation = violations.iterator().next();
        assertEquals("定員は1人以上に設定してください。", violation.getMessage());
    }

    @Test
    void testInvalidPostalCode() {
        // 郵便番号が空の場合
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setImageFile(new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]));
        form.setDescription("This is a test description.");
        form.setPrice(10000);
        form.setCapacity(4);
        form.setPostalCode(""); // 空文字列（不正）
        form.setAddress("Test Address");
        form.setPhoneNumber("090-1234-5678");

        // バリデーション実行
        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        // バリデーションエラーが1件出ることを確認（postalCodeフィールド）
        assertEquals(1, violations.size());
        
        ConstraintViolation<HouseRegisterForm> violation = violations.iterator().next();
        assertEquals("郵便番号を入力してください。", violation.getMessage());
    }
    
    @Test
    void testInvalidPhoneNumber_TooShort() {
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setDescription("Test Description");
        form.setPrice(10000);
        form.setCapacity(4);
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("09012345"); // 桁数不足

        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        assertEquals(1, violations.size());
        ConstraintViolation<HouseRegisterForm> violation = violations.iterator().next();
        assertEquals("有効な電話番号を入力してください。", violation.getMessage());
    }

    @Test
    void testInvalidPhoneNumber_TooLong() {
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setDescription("Test Description");
        form.setPrice(10000);
        form.setCapacity(4);
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("0901234567890"); // 桁数超過

        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        assertEquals(1, violations.size());
        ConstraintViolation<HouseRegisterForm> violation = violations.iterator().next();
        assertEquals("有効な電話番号を入力してください。", violation.getMessage());
    }

    @Test
    void testInvalidPhoneNumber_ContainsLetters() {
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("Test House");
        form.setDescription("Test Description");
        form.setPrice(10000);
        form.setCapacity(4);
        form.setPostalCode("123-4567");
        form.setAddress("Test Address");
        form.setPhoneNumber("0901234ABCD"); // 不正な文字含み

        Set<ConstraintViolation<HouseRegisterForm>> violations = validator.validate(form);

        assertEquals(1, violations.size());
        ConstraintViolation<HouseRegisterForm> violation = violations.iterator().next();
        assertEquals("有効な電話番号を入力してください。", violation.getMessage());
    }

}
