package code.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.*;

public class CompressProject {
    public static void main(String[] args) {
        initZipProject("C:\\Users\\*****\\IdeaProjects\\BlockchainTest");

    }
    public static void initZipProject(String projectPath){
        String[] parts = projectPath.split("[\\\\/]");
        StringBuilder zipFilePath = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++){
            zipFilePath.append(parts[i]).append("/");
        }
        zipFilePath.append(parts[parts.length - 1]).append(".zip");
        try{
            zipDirectory(Paths.get(projectPath), Paths.get(String.valueOf(zipFilePath)));
            System.out.println("Directory successfully zipped.");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void zipDirectory(Path sourceDirPath, Path zipFilePath) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath));
             Stream<Path> paths = Files.walk(sourceDirPath)) {
            paths
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
