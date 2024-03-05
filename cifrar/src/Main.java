import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey= keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance("Aes");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] encrypted = cipher.doFinal("Texto a cifrar".getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] original = cipher.doFinal(encrypted);
        String originalString = new String(original,StandardCharsets.UTF_8);
        System.out.println(originalString);
    }
}