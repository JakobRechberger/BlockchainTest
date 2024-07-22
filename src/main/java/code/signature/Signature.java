package code.signature;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class Signature {
    public static void signFile(File file) {
        String filepath = file.getAbsolutePath();
        String filename = file.getName().substring(0,file.getName().length()-4);
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();

            java.security.Signature signature = java.security.Signature.getInstance("SHA1withDSA", "SUN");
            signature.initSign(privateKey);

            byte[] bytes = Files.readAllBytes(Paths.get(filepath).toAbsolutePath());
            signature.update(bytes);
            byte[] digitalSignature = signature.sign();
            Path directoryPath = Paths.get("temp/signatures");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path signaturePath = directoryPath.resolve(filename + "_signature");
            Path publicKeyPath = directoryPath.resolve(filename + "_publickey");

            Files.write(signaturePath, digitalSignature);
            Files.write(publicKeyPath, keyPair.getPublic().getEncoded());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
