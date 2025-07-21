
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.io.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class FileEncryptor {

    // Constants
    private static final int AES_KEY_SIZE = 256; 
    private static final int IV_SIZE = 16;       
    private static final int SALT_SIZE = 16;     
    private static final int ITERATIONS = 65536;

    // Generate Secret Key from Password
    private static SecretKey getSecretKey(char[] password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, AES_KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // Encrypt File
    public static void encryptFile(String inputFile, String outputFile, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        byte[] iv = new byte[IV_SIZE];
        random.nextBytes(salt);
        random.nextBytes(iv);

        SecretKey key = getSecretKey(password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             DataOutputStream dos = new DataOutputStream(fos);
             FileInputStream fis = new FileInputStream(inputFile);
             CipherOutputStream cos = new CipherOutputStream(dos, cipher)) {

            // Write salt & IV to output file
            dos.write(salt);
            dos.write(iv);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("Encryption Done: " + outputFile);
    }

    // Decrypt File
    public static void decryptFile(String inputFile, String outputFile, String password) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile);
             DataInputStream dis = new DataInputStream(fis);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] salt = new byte[SALT_SIZE];
            byte[] iv = new byte[IV_SIZE];
            dis.readFully(salt);
            dis.readFully(iv);

            SecretKey key = getSecretKey(password.toCharArray(), salt);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            try (CipherInputStream cis = new CipherInputStream(dis, cipher)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
        System.out.println("Decryption Done: " + outputFile);
    }

    // Main Method: Console User Interface
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Advanced Encryption Tool (AES-256)");
        System.out.println("1. Encrypt File");
        System.out.println("2. Decrypt File");
        System.out.print("Choose option: ");
        int choice = Integer.parseInt(reader.readLine());

        System.out.print("Enter input file path: ");
        String inputFile = reader.readLine();
        System.out.print("Enter output file path: ");
        String outputFile = reader.readLine();
        System.out.print("Enter password: ");
        String password = reader.readLine();

        if (choice == 1) {
            encryptFile(inputFile, outputFile, password);
        } else if (choice == 2) {
            decryptFile(inputFile, outputFile, password);
        } else {
            System.out.println("Invalid Option!");
        }
    }
}
