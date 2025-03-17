package code.util;

import java.io.*;
import java.security.MessageDigest;

public class FileUtil {
    public static String getHashValueOfFileContent(File file) {
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
            return hexString.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "Error could not convert to hash";
    }
    public static File readFileFromConsole() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Enter a pathname for your JUnit Test class");
        while (true) {
            String filepath = reader.readLine();
            File file = new File(filepath);
            if (file.exists()){
                reader.close();
                System.out.println("Creating Test Report. This can take a moment...");
                return file;
            } else if (filepath.equalsIgnoreCase("x")) {
                System.out.println("aborting...");
                break;
            } else{
                System.out.println("FileNotFound Exception: Please enter a valid filepath or type 'x' to abort");
            }
        }
        reader.close();
        return null;
    }
    public static String filterFilenameFromPath(String path){
        File file = new File(path);
        if (path.endsWith(".java")){
            return file.getName().substring(0, file.getName().length() - 5);
        }
        else {
            return file.getName();
        }
    }
    public static String filterContentRoot(File file){
        String path = file.getAbsolutePath();
        if(path.contains("src")){
            path = path.substring(0, path.indexOf("src"));
        }
        return path;
    }
}
