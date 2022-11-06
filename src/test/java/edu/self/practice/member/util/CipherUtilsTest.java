package edu.self.practice.member.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("암호화, 복호화 유틸")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CipherUtilsTest {
    @Autowired
    private CipherUtils cipherUtils;

    @DisplayName("KeyGenerator: Secret Key의 길이는 128, 192, 256이어야 한다.")
    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("provideKeyLengths")
    void generateKey(int length, int size, String message) throws NoSuchAlgorithmException {
        // given
        SecretKey secretKey = cipherUtils.generateKey(length);

        // when
        byte[] encodedKey = secretKey.getEncoded();

        // then
        assertThat(encodedKey).hasSize(size);
    }

//    @DisplayName("SecretKeySpec: Secret Key의 길이는 256이어야 한다.")
//    @Test
//    void generateKey() {
//        // given
//        SecretKey secretKey = cipherUtils.generateKey();
//
//        // when
//        byte[] encodedKey = secretKey.getEncoded();
//
//        // then
//        assertThat(encodedKey).hasSize(32);
//    }

    @DisplayName("SecretKeyFactory: Secret Key의 길이는 256이어야 한다.")
    @Test
    void generateFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // given
        SecretKey secretKey = cipherUtils.generateFromPassword("password");

        // when
        byte[] encodedKey = secretKey.getEncoded();

        // then
        assertThat(encodedKey).hasSize(32);
    }

    @DisplayName("AES-256으로 암호화한 데이터는 복호화 했을 때 원래 데이터와 일치해야 한다.")
    @Test
    void encryptAndDecrypt() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // given
        String plainText = "helloooooo";

        // when
        String encrypt = cipherUtils.encrypt(plainText);
        String decrypt = cipherUtils.decrypt(encrypt);

        // then
        assertThat(plainText).isEqualTo(decrypt);
    }

    @DisplayName("")
    @Test
    void encrypt() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // given
        String plainText = "helloooooo";
        SecretKey key = cipherUtils.generateFromPassword("password");
        IvParameterSpec iv = cipherUtils.generateIv(16);

        // when
        String encrypt = cipherUtils.encrypt(plainText, key, iv);
        String decrypt = cipherUtils.decrypt(encrypt, key, iv);

        // then
        assertThat(plainText).isEqualTo(decrypt);
    }

    private static Stream<Arguments> provideKeyLengths() { // argument source method
        return Stream.of(
                Arguments.of(128, 16, "128비트는 16바이트이다."),
                Arguments.of(192, 24, "192비트는 24바이트이다."),
                Arguments.of(256, 32, "256비트는 32바이트이다.")
        );
    }
}
