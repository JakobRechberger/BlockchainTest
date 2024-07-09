package code;
import code.converter.XmlToPdf;
import org.apache.maven.shared.invoker.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import java.awt.event.TextListener;
import java.io.*;
import java.security.MessageDigest;
import java.util.Arrays;

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
            XmlToPdf.convertXmlToPdf(new File("target/surefire-reports/TEST-TestReport.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
