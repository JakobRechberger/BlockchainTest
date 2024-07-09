package code;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static code.Verification.verify_signature;

public class TestVerification {
    public static void main(String[] args) throws InterruptedException {
        TestObject object1 = new TestObject(new File("hello.txt"));
        object1.setPublickey(new File("publickey"));
        object1.setSignature(new File("signature"));
        TimeUnit.SECONDS.sleep(5);
        long localtime = new Date().getTime();
        //execute test
        long time_difference = (localtime - object1.getTime()) / 1000;
        System.out.println(time_difference);
        if (verify_signature(object1.getTestObject(), object1.getPublickey(), object1.getSignature()) && time_difference <= 10){
            System.out.println("Test not corrupted");
        }
        else {
            System.out.println("Test took too long to upload");
        }
    }
}
