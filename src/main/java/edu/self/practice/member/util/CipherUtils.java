package edu.self.practice.member.util;

import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class CipherUtils {
    private static final SecureRandom RANDOM_NUMBER = new SecureRandom();
    private static final String AES = "AES";
    private static final String KEY_256 = "aeskey12345678987654321asekey987";
    private static final String PBKDF2_WIT_HHMAC_SHA256 = "PBKDF2WithHmacSHA256"; // Password-Based Key Derivation Function
    private static final String PBE_SALT = "'~7&amp;03~/.";
    private static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int IV_LENGTH = 16;

    private static final SecretKey KEY;
    private static final IvParameterSpec IV;

    static {
        KEY = generateKey();
        IV = generateIv();
    }

    public SecretKey generateKey(int length) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(length);
        return keyGenerator.generateKey();
    }

    public SecretKey generateFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WIT_HHMAC_SHA256);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), PBE_SALT.getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, KEY_LENGTH);
        byte[] encodedKey = factory.generateSecret(spec)
                                   .getEncoded();
        return new SecretKeySpec(encodedKey, AES);
    }

    private static SecretKey generateKey() {
        return new SecretKeySpec(KEY_256.getBytes(StandardCharsets.UTF_8), AES);
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        RANDOM_NUMBER.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        cipher.init(Cipher.ENCRYPT_MODE, KEY, IV);
        byte[] encryptionBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder()
                     .encodeToString(encryptionBytes);
    }

    public String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, KEY, IV);
        byte[] decryptionBytes = Base64.getDecoder()
                                       .decode(cipherText);
        return new String(cipher.doFinal(decryptionBytes), StandardCharsets.UTF_8);
    }

    public String encrypt(String plainText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptionBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder()
                     .encodeToString(encryptionBytes);
    }

    public String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptionBytes = Base64.getDecoder()
                                       .decode(cipherText);
        return new String(cipher.doFinal(decryptionBytes), StandardCharsets.UTF_8);
    }

    public IvParameterSpec generateIv(int n) {
        byte[] iv = new byte[n];
        RANDOM_NUMBER.nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
