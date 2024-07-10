package code;
import code.converter.XmlToPdf;
import org.apache.maven.shared.invoker.*;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import java.awt.event.TextListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestExecutor {
    public static void main(String[] args) {
        try {
            executeTestsAndProcessReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void executeTestsAndProcessReport() throws MavenInvocationException {
        // Set up Maven invoker
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("pom.xml"));
        request.setGoals(Arrays.asList("clean", "site"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        InvocationResult result = invoker.execute(request);
        if (result.getExitCode() != 0) {
            throw new IllegalStateException("Build failed.");
        }
        try {
            Map<String, String> testCaseMap = decompileClassFile("target/test-classes/TestReport.class");
            //decompileCode(new File("target/test-classes/TestReport.class"));
            XmlToPdf.convertXmlToPdf(new File("target/surefire-reports/TEST-TestReport.xml"), testCaseMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        // Regex pattern to match method signatures and bodies in the decompiled code
        Pattern pattern = Pattern.compile("(@Test\\s+)?(public|protected|private|static|\\s) +[\\w\\[\\]]+ +(\\w+) *\\([^\\)]*\\) *(\\{(?:[^\\{\\}]|\\{(?:[^\\{\\}]|\\{[^\\{\\}]*\\})*\\})*\\})");
        Matcher matcher = pattern.matcher(decompiledCode);

        while (matcher.find()) {
            String annotation = matcher.group(1); // This captures the @Test annotation if present
            String methodSignature = matcher.group();
            String methodName = matcher.group(3); // This captures the method name
            if (annotation.startsWith("@Test")) {
                testCaseMap.put(methodName, methodSignature);
            }
        }

        return testCaseMap;
    }


}
