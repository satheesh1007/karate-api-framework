package Common;


import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public static Results runTest(String featurePath, String... tags){
        System.out.println("Running feature from path: classpath:" + featurePath);

        Results results = Runner.path("classpath:" + featurePath)
                .tags(tags)
                .outputCucumberJson(true)
                .parallel(1);

        System.out.println("Total: " + results.getScenariosTotal());
        System.out.println("Passed: " + results.getScenariosPassed());
        System.out.println("Failed: " + results.getFailCount());

        //generateReport(results.getReportDir());
        moveKarateJsonFiles(results.getReportDir(),"target/karate-json-reports");
        assertEquals(0,results.getFailCount(),results.getErrorMessages());
        return results;
    }
    public static void generateReport(String karateOutPath){
        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutPath),new String[] {"json"},true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
        Configuration config = new Configuration(new File("target"),"karate-framework-api");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths,config);
        reportBuilder.generateReports();
    }
    public static void moveKarateJsonFiles(String source_folder, String destination_folder){
        File sourceDirectory = new File(source_folder);
        File destinationDirectory = new File(destination_folder);

        try{
            FileUtils.copyDirectory(sourceDirectory,destinationDirectory,file -> file.getName().toLowerCase().endsWith(".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipFolder(String sourceFolderPath, String zipFilePath) throws IOException {
        Path sourceFolder = Paths.get(sourceFolderPath);
        Path zipFile = Paths.get(zipFilePath);

        if (!Files.exists(sourceFolder)) {
            System.err.println("❌ Source folder does not exist: " + sourceFolderPath);
            return;
        }

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Files.walk(sourceFolder)
                    .filter(path -> {
                        try {
                            return Files.isRegularFile(path); // ✅ skip directories
                        } catch (Exception e) {
                            System.err.println("⚠️ Skipping (access denied): " + path);
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            ZipEntry zipEntry = new ZipEntry(sourceFolder.relativize(path).toString().replace("\\", "/"));
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.err.println("⚠️ Could not add file: " + path + " -> " + e.getMessage());
                        }
                    });

            System.out.println("✅ Zipped report folder to: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("❌ Failed to zip folder: " + e.getMessage());
        }
    }

    public static void convertHtmlToPdf(String htmlPath, String pdfPath) throws Exception {
        try (OutputStream os = new FileOutputStream(pdfPath)) {
            File input = new File(htmlPath);
            Document jsoupDoc = Jsoup.parse(input, "UTF-8");

            // Clean up the HTML to be XHTML-compliant for PDF rendering
            jsoupDoc.outputSettings()
                    .syntax(Document.OutputSettings.Syntax.xml) // make it XHTML
                    .escapeMode(Entities.EscapeMode.xhtml)
                    .prettyPrint(true);

            String cleanedHtml = jsoupDoc.html();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(cleanedHtml, input.getParentFile().toURI().toString());
            builder.toStream(os);
            builder.run();

            System.out.println("✅ Cleaned HTML converted to PDF.");
        } catch (Exception e) {
            System.err.println("❌ Failed to convert cleaned HTML to PDF: " + e.getMessage());
            throw e;
        }

    }
}


