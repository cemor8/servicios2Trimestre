package com.example.librosserver;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class Decryptor {
    public String decrypt(byte[] cipherText, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher decrypter = Cipher.getInstance("RSA");
        decrypter.init(Cipher.DECRYPT_MODE,privateKey);
        return new String(decrypter.doFinal(cipherText));
    }
}
