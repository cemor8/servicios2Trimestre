import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Asimetrico {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        KeyPairGeneratorExample keyPairGenerator = new KeyPairGeneratorExample();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Encryptor encryptor = new Encryptor();
        byte[] cipherText = encryptor.encrypt("Hola",keyPair.getPublic());

        Decryptor decryptor = new Decryptor();
        String decryptedText = decryptor.decrypt(cipherText,keyPair.getPrivate());
        System.out.println(decryptedText);
    }
}
