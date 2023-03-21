package org.skin.consultation.management.gui.cryptography.files;

import org.skin.consultation.management.gui.cryptography.CryptoUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class EncDecFile {
    private static final String password = "p5XtOrgFlTtgtSH1U3NV3bX14Dfb8FLh6U0oT1U5n/o=";

    public static byte[] encrypt(byte[] pText, String password) throws Exception {

        // 16 bytes salt
        byte[] salt = CryptoUtils.getRandomNonce(16);

        // GCM recommended 12 bytes iv
        byte[] iv = CryptoUtils.getRandomNonce(12);

        // secret key from password
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // ASE-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(128, iv));

        byte[] cipherText = cipher.doFinal(pText);

        return ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();
    }

    // we need the same password, salt and iv to decrypt it
    private static byte[] decrypt(byte[] cText, String password) throws Exception {

        // get back the iv and salt that was prefixed in the cipher text
        ByteBuffer byteBuffer = ByteBuffer.wrap(cText);

        byte[] iv = new byte[12];
        byteBuffer.get(iv);

        byte[] salt = new byte[16];
        byteBuffer.get(salt);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        // get back the aes key from the same password and salt
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(128, iv));

        return cipher.doFinal(cipherText);
    }

    public static void encryptFile(String fromFile, String toFile) throws Exception {

        // read a normal txt file
        byte[] fileContent = Files.readAllBytes(Paths.get(fromFile));

        // encrypt with a password
        byte[] encryptedText = encrypt(fileContent, password);

        // save a file
        Path path = Paths.get(toFile);

        Files.write(path, encryptedText);
    }

    public static void decryptFile(String fromEncryptedFile, String toFile) throws Exception {

        // read a file
        byte[] fileContent = Files.readAllBytes(Paths.get(fromEncryptedFile));

        byte[] decryptedData = decrypt(fileContent, password);

        int length = fromEncryptedFile.length();

        // Get characters
        String extension = fromEncryptedFile.substring(length - 7).replace(".skf", "");

        String selectedExtension;
        switch (extension) {
            case "jpg" -> selectedExtension = "jpg";
            case "png" -> selectedExtension = "png";

            default -> selectedExtension = "jpeg";
        }

        // save a file
        Path path = Paths.get(toFile);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedData);
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
        ImageIO.write(bufferedImage, selectedExtension, new File(path.toString()));
    }
}