package code;
import code.converter.POMChecker;
import code.converter.XmlToPdf;
import org.apache.maven.shared.invoker.*;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestExecutor {

    public static void main(String[] args) {
        try {
            File file = readFileFromConsole();
            if (file != null){
                String contentRootPath= filterContentRoot(file);
                String filename = filterFilenameFromPath(file.getAbsolutePath());
                File testReport=executeTestsAndProcessReport(filename, contentRootPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static File initAndExecuteTestReport(){
        try {
            File file = readFileFromConsole();
            if (file != null){
                String filename = filterFilenameFromPath(file.getAbsolutePath());
                return executeTestsAndProcessReport(filename, filterContentRoot(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public static File executeTestsAndProcessReport(String filename, String contentRoot) throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        if(POMChecker.checkDependenciesOfPom(contentRoot + "pom.xml")){
            request.setPomFile(new File(contentRoot + "pom.xml"));
            request.setGoals(Arrays.asList("clean", "site"));
            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
            InvocationResult result = invoker.execute(request);
            if (result.getExitCode() != 0) {
                throw new IllegalStateException("Build failed.");
            }
            try {
                Map<String, String> testCaseMap = decompileClassFile(contentRoot + "target/test-classes/"+ filename + ".class");
                return XmlToPdf.convertXmlToPdf(new File(contentRoot + "target/surefire-reports/TEST-" + filename + ".xml"), contentRoot, testCaseMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Dependencies not found please refer to the docs and check the required dependenicies");
        }
        return new File("");
    }

    public static Map<String, String> decompileClassFile(String classFilePath) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            List<String> args = Collections.singletonList(classFilePath);
            Map<String, String> options = Collections.emptyMap();
            CfrDriver driver = new CfrDriver.Builder()
                    .withOptions(options)
                    .withOutputSink(new OutputSinkFactory() {
                        @Override
                        public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                            return Collections.singletonList(SinkClass.STRING);
                        }

                        @Override
                        public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                            return sinkable -> {
                                if (sinkType == SinkType.JAVA && sinkClass == SinkClass.STRING) {
                                    byteArrayOutputStream.writeBytes(sinkable.toString().getBytes());
                                }
                            };
                        }
                    }).build();
            driver.analyse(args);

            String decompiledCode = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
            return extractTestCaseMethods(decompiledCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> extractTestCaseMethods(String decompiledCode) {
        Map<String, String> testCaseMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(@Test\\s+)?(public|protected|private|static|\\s) +[\\w\\[\\]]+ +(\\w+) *\\([^\\)]*\\) *(\\{(?:[^\\{\\}]|\\{(?:[^\\{\\}]|\\{[^\\{\\}]*\\})*\\})*\\})");
        Matcher matcher = pattern.matcher(decompiledCode);

        while (matcher.find()) {
            String annotation = matcher.group(1);
            String methodSignature = matcher.group();
            String methodName = matcher.group(3);
            if (annotation.startsWith("@Test")) {
                testCaseMap.put(methodName, methodSignature);
            }
        }
        return testCaseMap;
    }


}
