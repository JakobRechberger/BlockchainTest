package code;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;


public class SignatureTest {
    public static void main(String[] args) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            System.out.println(privateKey);
            System.out.println(publicKey);

            String test = "Hallo hier bin ich";
            System.out.println("Original message:"+ test);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

