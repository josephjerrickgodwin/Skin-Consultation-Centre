package org.skin.consultation.management.gui.cryptography.data;

import org.skin.consultation.management.gui.cryptography.CryptoUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class EncDecString {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String PASSWORD = "94&zd36S^Ju!6dz2iqJ6*#7@UFcwe#pHzyOHHCkoK*79^9!wV$6q7&%hLSHM^cP5OeH1Nt!IOExaYFpc1U";

    // Encrypt the text input
    public static String encryptText(String pText) throws Exception {

        // 16 bytes salt
        byte[] salt = CryptoUtils.getRandomNonce(16);

        // GCM recommended 12 bytes iv?
        byte[] iv = CryptoUtils.getRandomNonce(12);

        // secret key from password
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(PASSWORD.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // ASE-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(128, iv));

        byte[] cipherText = cipher.doFinal(pText.getBytes(UTF_8));

        // string representation, base64, send this string to other for decryption.
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array());
    }

    // Decrypt the encrypted data
    public static String decryptText(String cText) throws Exception {

        // get back the iv and salt from the cipher text
        ByteBuffer bb = ByteBuffer.wrap(Base64.getDecoder().decode(cText.getBytes(UTF_8)));

        byte[] iv = new byte[12];
        bb.get(iv);

        byte[] salt = new byte[16];
        bb.get(salt);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        // get back the aes key from the same password and salt
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(PASSWORD.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(128, iv));

        return new String(cipher.doFinal(cipherText), UTF_8);
    }
}