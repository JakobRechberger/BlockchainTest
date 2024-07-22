package code.reportGenerator;

import code.signature.Signature;

import java.io.File;

import static code.signature.Verification.verify_signature;
import static code.util.FileUtil.getHashValueOfFileContent;

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
        System.out.println(getHashValueOfFileContent(testReport.getTestObject()));
        Signature.signFile(testReport.getTestObject());
        String testReportFileName = testReport.getTestObject().getName();
        String substring = testReportFileName.substring(0, testReportFileName.length() - 4);
        testReport.setSignature(new File("temp/signatures/"+ substring +"_signature" ));
        testReport.setPublickey(new File("temp/signatures/"+ substring +"_publickey" ));
        if(verify_signature(testReport.getTestObject(), testReport.getPublickey(), testReport.getSignature())){
            System.out.println("Signature verified");
        }
    }
}
