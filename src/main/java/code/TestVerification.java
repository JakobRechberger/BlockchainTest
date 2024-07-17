package code;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static code.Verification.verify_signature;

public class TestVerification {
    public static void main(String[] args) {
        testSignAndCheck();
    }
    public static void signFile(){
        Signature.signFile(new File("TEST-TestReport.pdf"));
    }
    /**public static void verifyFile() throws InterruptedException {
        TestObject object1 = new TestObject(new File("hello.txt"));
        object1.setPublickey(new File("publickey"));
        object1.setSignature(new File("signature"));
        TimeUnit.SECONDS.sleep(5);
        long localtime = new Date().getTime();
        long time_difference = (localtime - object1.getTime()) / 1000;
        System.out.println(time_difference);
        if (verify_signature(object1.getTestObject(), object1.getPublickey(), object1.getSignature()) && time_difference <= 10){
            System.out.println("Test not corrupted");
        }
        else {
            System.out.println("Test took too long to upload");
        }
    }*/
    public static void testSignAndCheck(){
        TestObject testReport = new TestObject(TestExecutor.initAndExecuteTestReport());
        System.out.println(fileToHash(testReport.getTestObject()));
        Signature.signFile(testReport.getTestObject());
        String testReportFileName = testReport.getTestObject().getName();
        String substring = testReportFileName.substring(0, testReportFileName.length() - 4);
        testReport.setSignature(new File("temp/signatures/"+ substring +"_signature" ));
        testReport.setPublickey(new File("temp/signatures/"+ substring +"_publickey" ));
        if(verify_signature(testReport.getTestObject(), testReport.getPublickey(), testReport.getSignature())){
            System.out.println("Signature verified");
        }
    }
    public static String fileToHash(File file){
        return getString(file);
    }

    static String getString(File file) {
        try {
            byte[] buffer = new byte[8192];
            int count;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
            bis.close();
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            System.out.println("Hash-Value of File: "+hexString);
            return hexString.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "Error could not convert to hash";
    }
}
