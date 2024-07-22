package code.blockchain;

import java.io.File;
import java.security.SecureRandom;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static code.signature.Verification.verify_signature;
import static code.util.FileUtil.getHashValueOfFileContent;

public class Chain {
    LinkedList<Block> blocks = new LinkedList<>();

    public void addBlock(File file) {
        String fileAsHash = getHashValueOfFileContent(file);
        if (verify_signature(file, new File("publickey"), new File("signature"))){
            if(blocks.isEmpty()) {
                Block genesis = new Block();
                genesis.setTimestamp(System.currentTimeMillis());
                genesis.setNonce(generateNonce());
                genesis.setProofOfWork("0");
                genesis.setData(fileAsHash);
                blocks.add(genesis);
            }
            else {
                Block pBlock = blocks.getLast();
                Block nBlock = new Block();
                nBlock.setTimestamp(System.currentTimeMillis());
                nBlock.setNonce(generateNonce());
                nBlock.setProofOfWork(PoW(pBlock, fileAsHash));
                nBlock.setData(fileAsHash);
                blocks.add(nBlock);
            }
        }
        else{
            System.out.println("Could not verify file Signature.");
        }

    }
    public String PoW(Block block, String fileAsHash) {
        String erg = null;
        String startSeq = "1";
        while(!startSeq.equals("000")) {
            block.setNonce(generateNonce());
            block.setData(fileAsHash);
            erg = hash(block);
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < 3; i++) {
                s.append(erg.charAt(i));
            }
            startSeq = s.toString();
        }
        return erg;
    }
    public String hash(Block block) {
        String s = "";
        byte[] hash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(block.data.getBytes());
            hash = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            s= hexString.toString();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return s;
    }
    public String generateNonce() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom r = new SecureRandom();
        StringBuilder s= new StringBuilder();
        for(int i = 0; i < 8; i++) {
            int index = r.nextInt(letters.length());
            //char b = (char) ('a' + r.nextInt(26));
            char c = letters.charAt(index);
            s.append(c);
        }
        return s.toString();
    }
    public static void main(String[] args) {
        Chain c = new Chain();
        // System.out.println(fileToHash(new File("hello.txt")));
        for(int i = 0; i < 3; i++){
            c.addBlock(new File("hello.txt"));
        }
        for(Block block : c.blocks){
            System.out.println(block.toString());
        }
    }
}

