package code.reportGenerator;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;


public class TestObject {
    long time;
    File TestObject;

    File signature;

    File publickey;

    public TestObject(File testObject) {
        this.time = new Date().getTime();
        TestObject = testObject;
    }

    public long getTime() {
        return time;
    }

    public File getTestObject() {
        return TestObject;
    }

    public File getSignature() {
        return signature;
    }

    public void setSignature(File signature) {
        this.signature = signature;
    }

    public File getPublickey() {
        return publickey;
    }

    public void setPublickey(File publickey) {
        this.publickey = publickey;
    }
}
