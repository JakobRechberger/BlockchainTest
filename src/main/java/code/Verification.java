package code;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class Verification {
    public static boolean verify_signature(File file){
        try {
            byte[] publicKeyEncoded = Files.readAllBytes(Paths.get("publickey"));
            byte[] digitalSignature = Files.readAllBytes(Paths.get("signature"));

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");

            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
            signature.initVerify(publicKey);

            byte[] bytes = Files.readAllBytes(Paths.get(String.valueOf(file)));
            signature.update(bytes);

            return signature.verify(digitalSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
