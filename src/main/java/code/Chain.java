package code;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static code.Verification.verify_signature;

public class Chain {
    LinkedList<Block> blocks = new LinkedList<>();

    public void addBlock(File file) {
        String fileAsHash = fileToHash(file);
        if (verify_signature(file)){
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
    public String generateNonce() {																//berechnet zufällige Nonce
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";		//alle Characters die zu verwenden sind
        SecureRandom r = new SecureRandom();
        StringBuilder s= new StringBuilder();
        for(int i = 0; i < 8; i++) {															//erstelle neuen STring zu dem acht mal ein zufälliger Buchstaber/Zahl hinzugefügt wird
            int index = r.nextInt(letters.length());
            //char b = (char) ('a' + r.nextInt(26));
            char c = letters.charAt(index);
            s.append(c);
        }
        return s.toString();
    }
    public String fileToHash(File file){
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

//7befd7e189716dda9eb88e6f0bf2a0bb1eb5499ef3e7f8bb55fe930ea201295f
//7f29ce27f081292726b525bbe67d4a87b6fe98c5df57e287dfe5c2f21428686e
//7f29ce27f081292726b525bbe67d4a87b6fe98c5df57e287dfe5c2f21428686e
//b64a066b50b6beb2377dc268e5ead6d5d8ba37d7c043d12fea836965cc6be498
