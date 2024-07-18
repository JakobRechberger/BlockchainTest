## Blockchain Test Project for Bachelor Thesis
This repo contains multiple features for research and testing of Bachelor Thesis approach.

Project is written in Java with Maven
  
#### Test Report Creation
- Surefire Plugin to gather test data and test results (JUnit)
- automatically create test report from test directory 
- includes decompiler (cfr) to add code snippet of test to report
- output in XML, HTML and PDF format
  
#### Signature and Blockchain
- Signs input file with digital signature and appends hash value as Block to basic Blockchain
- Signature is verified before appended to Block

#### Tutorial Create Test Report for Test Suite in other local Maven Repo:
1. Make sure you have add the following dependencies to your pom.xml in your other repo (you may have to do some refactoring):


    <details>
    <summary>pom.xml</summary>
    <br>
      
    ```
   <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>18</maven.compiler.source>
        <maven.compiler.target>18</maven.compiler.target>
        <maven.surefire.version>2.22.0</maven.surefire.version>
        <maven.surefire.report.plugin.version>3.3.0</maven.surefire.report.plugin.version>
        <org.junit.platform.version>1.2.0</org.junit.platform.version>
        <openhtml.version>1.0.10</openhtml.version>
        <commons.codec.version>1.16.0</commons.codec.version>
        <org.apache.maven.shared.version>3.3.0</org.apache.maven.shared.version>
        <org.apache.xmlgraphics.version>2.6</org.apache.xmlgraphics.version>
        <com.openhtmltopdf.version>1.0.10</com.openhtmltopdf.version>
        <org.benf.version>0.152</org.benf.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
            <version>${commons.codec.version}</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>RELEASE</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.maven.shared</groupId>
            <artifactId>maven-invoker</artifactId>
            <version>${org.apache.maven.shared.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>fop</artifactId>
            <version>${org.apache.xmlgraphics.version}</version>
        </dependency>
        <dependency>
            <groupId>com.openhtmltopdf</groupId>
            <artifactId>openhtmltopdf-pdfbox</artifactId>
            <version>${com.openhtmltopdf.version}</version>
        </dependency>
        <dependency>
            <groupId>org.benf</groupId>
            <artifactId>cfr</artifactId>
            <version>${org.benf.version}</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven.surefire.version}</version>
                <dependencies>
                    <dependency>
                        <groupId>org.junit.platform</groupId>
                        <artifactId>junit-platform-surefire-provider</artifactId>
                        <version>${org.junit.platform.version}</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <additionalClasspathElements>
                        <additionalClasspathElement>src/test/java/</additionalClasspathElement>
                    </additionalClasspathElements>
                    <testFailureIgnore>true</testFailureIgnore>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-report-plugin</artifactId>
                <version>${maven.surefire.report.plugin.version}</version>
            </plugin>
        </plugins>
    </reporting>
    
   ```
    
  </details>

2. run the TestExecutor class and specify the absolute(!) filepath to your test class
   ![image](https://github.com/user-attachments/assets/cf45a399-c0c2-4987-bf44-e2b3fc4f9176)


3.  after successful creation you have a TEST-\<NAME-OF-THE-TEST-CLASS\>.pdf file in your root folder as well as a .xml and .html file of the test report in your target/surefire-reports folder

#### Test Report Snapshot
![image](https://github.com/user-attachments/assets/a4e05182-7ecc-47ca-8cbf-e37a539632ae)



