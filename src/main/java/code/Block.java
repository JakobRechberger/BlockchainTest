package code;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;


public class Block {
    long timestamp;
    String nonce;
    String proofOfWork;
    String data;
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public void setProofOfWork(String proofOfWork) {
        this.proofOfWork = proofOfWork;
    }
    public void setData(String data){														//Data String addiert alle Strings für Wert des Blocks
        String s = "";
        s+= timestamp + data + nonce + proofOfWork;
        this.data= s;

    }
    @Override
    public String toString() {													//neue toString Methode m Format"timestamp|nonce|pow
        String s ="";
        s += timestamp+"|"+data+"|"+nonce+"|"+proofOfWork;
        return s;
    }

}

