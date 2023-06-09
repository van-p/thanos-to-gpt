package Automation.Utils;

import com.opencsv.CSVWriter;
import org.testng.Reporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QaDashboardHelper
{

    static final String bucketName = "qa-thanos-results";
    static String commonPathForFileUpload = System.getProperty("user.dir") + File.separator + "build" + File.separator;
    static boolean enableConsoleLogs = true;

    public static void uploadAutomationResultsToThanosBucket(String pdgName, String createdAt, String projectName, String environment, String groupName, String duration, String percentage, String totalCases, String passedCases, String failedCases, String buildTag, String resultLink, boolean debugMode)
    {
        logCommentForDebugging("Uploading Automation Results to Thanos Bucket...");
        QaDashboardHelper.enableConsoleLogs = debugMode;
        Config.isDebugMode = false;
        Config testConfig = new Config();
        String csvFileName = createAutomationResultsCsvFile(testConfig, pdgName.replaceAll(" ", ""), createdAt, projectName, environment, groupName, duration, percentage, totalCases, passedCases, failedCases, buildTag, resultLink);
        GcpHelper.uploadFileInGcpBucket(testConfig, null, bucketName, commonPathForFileUpload + csvFileName, csvFileName);
        Reporter.log("File " + csvFileName + " uploaded successfully to GCP bucket", true);
    }

    private static String createAutomationResultsCsvFile(Config testConfig, String pdgName, String createdAt, String projectName, String environment, String groupName, String duration, String percentage, String totalCases, String passedCases, String failedCases, String buildTag, String qaDashboardResultsLink)
    {
        Writer writer = null;
        String csvFileName = pdgName + "_TestResults_" + buildTag + ".csv";
        CommonUtilities.createFolder(testConfig, commonPathForFileUpload);
        try
        {
            writer = Files.newBufferedWriter(Paths.get(commonPathForFileUpload + csvFileName));
            logCommentForDebugging("writer initialised: " + commonPathForFileUpload + csvFileName);
        } catch (Exception e)
        {
            logCommentForDebugging("Exception thrown while initialising writer");
            Reporter.log(e.getMessage(), true);
        }
        CSVWriter csvWriter = new CSVWriter(writer, ',', '\u0000', '"', "\n");
        logCommentForDebugging("csvWriter initialised");
        csvWriter.writeNext(new String[]{"createdAt", "projectName", "environment", "groupName", "duration", "percentage", "totalCases", "passedCases", "failedCases", "buildTag", "resultLink"});
        logCommentForDebugging("Headers added");

        csvWriter.writeNext(new String[]{createdAt, projectName, environment, groupName, duration, percentage, totalCases, passedCases, failedCases, buildTag, qaDashboardResultsLink});
        logCommentForDebugging("Data added to csv file");

        String line = "";
        BufferedReader csvFile;
        try
        {
            writer.close();
            // Code to print contents
            csvFile = new BufferedReader(new FileReader(commonPathForFileUpload + csvFileName));
            while ((line = csvFile.readLine()) != null)
            {
                Reporter.log(line, true);
            }
            csvWriter.close();
        } catch (Exception e)
        {
            Reporter.log(e.getMessage(), true);
        }
        return csvFileName;
    }

    public static void logCommentForDebugging(String message)
    {
        Reporter.log(message, enableConsoleLogs);
    }
}