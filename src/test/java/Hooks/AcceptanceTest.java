package Hooks;

import Common.TestConstants;
import Common.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import mail.DailyEmailReport;
import report.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;


import static Common.TestUtils.runTest;

public class AcceptanceTest {
    @BeforeAll
    public static void setup() throws Exception {
        System.out.println(TestConstants.env);
    }
    @Test
    void test() throws Exception {
        runTest("features/budgets");
    }

    @AfterAll
    public static void teardown() throws Exception{
        System.out.println("Executed Successfully");

        try {
            // 1. Generate Cucumber report
            TestUtils.generateReport("target/karate-reports");

            // 2. Flush extent report
            ExtentTest summary = ExtentReportManager.createTest("Post-test Summary");
            summary.pass("All tests completed successfully.");
            ExtentReportManager.flush();

            // 3. Convert HTML to PDF
            try {
                TestUtils.convertHtmlToPdf("target/extent-report.html", "target/extent-report.pdf");
                System.out.println("📄 PDF report generated successfully.");
            } catch (Exception e) {
                System.err.println("❌ Error generating PDF report: " + e.getMessage());
            }

            // 4. Send Email with PDF
            DailyEmailReport.sendEmailReport();

            System.out.println("✅ Report generated and email sent.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
