package code;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

public class Signature {
    public static void signFile(File file) {
        String filepath = file.getAbsolutePath();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();

            java.security.Signature signature = java.security.Signature.getInstance("SHA1withDSA", "SUN");
            signature.initSign(privateKey);


            // Supply the data to be signed to the Signature object
            // using the update() method and generate the digital
            // signature.
            byte[] bytes = Files.readAllBytes(Paths.get(filepath).toAbsolutePath());
            signature.update(bytes);
            byte[] digitalSignature = signature.sign();

            // Save digital signature and the public key to a file.
            Files.write(Paths.get("signature"), digitalSignature);
            Files.write(Paths.get("publickey"), keyPair.getPublic().getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
