package code.reportGenerator;

import code.signature.Signature;

import java.io.File;

import static code.signature.Verification.verifySignature;
import static code.util.FileUtil.getHashValueOfFileContent;

public class TestVerification {
    public static void main(String[] args) {
        testSignAndCheck();
    }
    public static void testSignAndCheck(){
        TestObject testReport = new TestObject(TestExecutor.initAndExecuteTestReport());
        File parentPath = testReport.getTestObject().getParentFile();
        Signature.signFile(testReport.getTestObject());

        String testReportFileName = testReport.getTestObject().getName();
        String substring = testReportFileName.substring(0, testReportFileName.length() - 4);

        testReport.setSignature(setKeyPairPath(parentPath, substring, "signature"));
        testReport.setPublickey(setKeyPairPath(parentPath, substring, "publickey"));
        if(verifySignature(testReport.getTestObject(), testReport.getPublickey(), testReport.getSignature())){
            System.out.println("Signature verified");
        }
    }

    public static File setKeyPairPath(File parentPath, String filename, String type){
        return new File(parentPath + File.separator + "signatures" + File.separator + filename +"_" + type );
    }
}
