package Automation.Utils;

import Automation.Utils.Enums.ProjectName;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Config
{

    public static String browserName;
    public static String browserVersion;
    public static String resultsDirectory;
    public static String mobileAppName;
    public static String locale;
    public static String appLanguage;
    public static boolean isMobileViewExecution = false;
    public static boolean isMobileAppExecution = false;
    public static boolean isRemoteExecution = true;
    public static boolean isDebugMode = false;
    public static String osName = System.getProperty("os.name");
    public static HashMap<String, TestDataReader> testDataReaderHashMap = new HashMap<String, TestDataReader>();
    public static String googleAuthToken = null;
    public static String projectName = null;
    public static boolean useSingleBrowser = false;
    public static WebDriver staticDriver = null;
    protected static String environment = null;
    public WebDriver driver = null;
    public AppiumDriver appiumDriver = null;
    public AppiumDriverLocalService appiumServer = null;
    public boolean endExecutionOnfailure = false;
    public String testcaseName;
    public String testcaseClass;
    public HashMap<String, String> testData = new HashMap<>();
    String testLog = "";
    String testEndTime;
    String testStartTime;
    SoftAssert softAssert = null;
    Properties runTimeProperties = null;
    int testcasesRemaining = 0;
    boolean enableScreenshot = true;
    boolean testResult = true;
    boolean retry = true;

    @SuppressWarnings("resource")
    public Config()
    {
        softAssert = new SoftAssert();
        runTimeProperties = new Properties();
        Properties properties = null;
        // Code to read .properties file and put key value pairs into RunTime Property file
        try
        {
            InputStream inputStream = null;
            String parametersPath = System.getProperty("user.dir") + File.separator + "Parameters" + File.separator;

            /*
             * Using config.properties file as a resource in thanos jar
             * so need to read the file as resource stream instead of passing the relative path
             * as path would be different in thanos jar and thanos repo
             */
            try
            {
                //First read from local
                inputStream = new FileInputStream(parametersPath + "config.properties");
            } catch (FileNotFoundException e)
            {
                //If not found then read from jar file
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            }
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();

            // Override the mobileAppExecution value if passed through TestNG.xml
            isMobileAppExecution = isMobileAppExecution || (properties.getProperty("MobileAppExecution") != null && properties.getProperty("MobileAppExecution").equalsIgnoreCase("true"));
            if (isMobileAppExecution)
            {
                if (!StringUtils.isEmpty(mobileAppName))
                {
                    properties.put("MobileAppName", mobileAppName);
                }
                FileInputStream fileInputStream = new FileInputStream(parametersPath + properties.getProperty("MobileAppName") + ".properties");
                properties.load(fileInputStream);
                fileInputStream.close();
            }

            // Override the environment value if passed through TestNG.xml
            if (!StringUtils.isEmpty(environment))
            {
                properties.put("Environment", environment.toLowerCase());
            }
            if (!StringUtils.isEmpty(locale))
            {
                properties.put("Locale", locale.toLowerCase());
            }
            if (!StringUtils.isEmpty(appLanguage))
            {
                properties.put("AppLanguage", appLanguage.toLowerCase());
            }

            /*
             * Using environment.properties file as a resource in thanos jar
             * so need to read the file as resource stream instead of passing the relative path
             * as path would be different in thanos jar and thanos repo
             */
            if (properties.get("Environment") == null)
            {
                properties.put("Environment", "staging");
            }
            if (properties.get("Locale") == null || ((String) properties.get("Locale")).equalsIgnoreCase("sg"))
            {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(properties.get("Environment") + ".properties");
                logComment("Running on '" + properties.get("Environment") + "' environment");
            } else
            {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(properties.get("Environment") + "-" + properties.get("Locale") + ".properties");
                logComment("Running on '" + properties.get("Environment") + "-" + properties.get("Locale") + "' environment");
            }

            properties.load(inputStream);
            inputStream.close();

            try
            {
                /*
                 * Using system.properties file as a resource in thanos jar
                 * so need to read the file as resource stream instead of passing the relative path
                 * as path would be different in thanos jar and thanos repo
                 */
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties");
                Properties systemProperties = new Properties();
                systemProperties.load(inputStream);
                inputStream.close();
                Enumeration<Object> enumeration = systemProperties.keys();
                while (enumeration.hasMoreElements())
                {
                    String str = (String) enumeration.nextElement();
                    System.setProperty(str, (String) systemProperties.get(str));
                }
            } catch (FileNotFoundException e)
            {
                logWarning("system.properties file not found.");
            }
        } catch (Exception e)
        {
            logComment("Exception while reading config.properties file...");
            e.printStackTrace();
        }

        Enumeration<Object> enumeration = properties.keys();
        while (enumeration.hasMoreElements())
        {
            String str = (String) enumeration.nextElement();
            putRunTimeProperty(str, (String) properties.get(str));
        }

        // override param values if passed through TestNG.xml
        if (!StringUtils.isEmpty(resultsDirectory))
        {
            putRunTimeProperty("ResultsDirectory", resultsDirectory);
        } else
        {
            resultsDirectory = System.getProperty("user.dir") + File.separator + "test-output";
            putRunTimeProperty("ResultsDirectory", resultsDirectory);
        }
        if (!StringUtils.isEmpty(projectName))
        {
            putRunTimeProperty("ProjectName", projectName);
        }
        if (!StringUtils.isEmpty(browserName))
        {
            putRunTimeProperty("Browser", browserName);
        }
        if (!StringUtils.isEmpty(browserVersion))
        {
            putRunTimeProperty("BrowserVersion", browserVersion);
        }

        // Putting values into variables from RunTime properties
        endExecutionOnfailure = endExecutionOnfailure || (getRunTimeProperty("EndExecutionOnFailure") != null && getRunTimeProperty("EndExecutionOnFailure").equalsIgnoreCase("true"));
        isMobileViewExecution = isMobileViewExecution || (getRunTimeProperty("MobileViewExecution") != null && getRunTimeProperty("MobileViewExecution").equalsIgnoreCase("true"));
        isMobileAppExecution = isMobileAppExecution || (getRunTimeProperty("MobileAppExecution") != null && getRunTimeProperty("MobileAppExecution").equalsIgnoreCase("true"));
        isRemoteExecution = isRemoteExecution || (getRunTimeProperty("RemoteExecution") != null && getRunTimeProperty("RemoteExecution").equalsIgnoreCase("true"));
        isDebugMode = isDebugMode || (getRunTimeProperty("debugMode") != null && getRunTimeProperty("debugMode").equalsIgnoreCase("true"));
        environment = getRunTimeProperty("Environment");
        // To handle case of prod canary
        if (getRunTimeProperty("Environment").equalsIgnoreCase("prodcanary"))
        {
            putRunTimeProperty("Environment", "Production");
        }
        projectName = getRunTimeProperty("ProjectName");
        mobileAppName = getRunTimeProperty("MobileAppName");
        locale = getRunTimeProperty("locale");
        appLanguage = getRunTimeProperty("appLanguage");
    }


    /**
     * Add the given key value pair in the Run Time Properties
     *
     * @param keyName - string key
     * @param value   - string value
     */
    public void putRunTimeProperty(String keyName, String value)
    {
        logCommentForDebugging("Putting run time key-" + keyName + " value:-'" + value + "'");
        runTimeProperties.put(keyName.toLowerCase(), value);
    }

    /**
     * Add the given key value pair in the Run Time Properties
     *
     * @param keyName - string key
     * @param value   - string value
     */
    public void putRunTimeProperty(String keyName, Object value)
    {
        logCommentForDebugging("Putting run time key-" + keyName.toLowerCase() + " value:-'" + value + "'");
        runTimeProperties.put(keyName.toLowerCase(), value);
    }

    /**
     * Add the given key value pair in the Run Time Properties
     *
     * @param hashMap - HashMap
     */
    public void putRunTimeProperty(HashMap<String, Integer> hashMap)
    {
        Map map = hashMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(), entry -> entry.getValue()));
        runTimeProperties.putAll(map);
    }

    /**
     * Add the given key value pair in the Run Time Properties
     *
     * @param hashMap - HashMap
     */
    public void putHashmapStringTypeAsRunTimeProperty(HashMap<String, String> hashMap)
    {
        Map map = hashMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(), entry -> entry.getValue()));
        runTimeProperties.putAll(map);
    }

    /**
     * Get the value of any key which is either saved in TestData or RunTime properties
     *
     * @param keyName name whose value is needed
     * @return value of the specified key
     */
    public String getRunTimeProperty(String keyName)
    {
        String value = getRunTimePropertyInternal(keyName);
        if (StringUtils.isEmpty(value) && testData != null)
        {
            value = testData.get(keyName);
            value = StringUtils.isEmpty(value) ? testData.get(keyName.toLowerCase()) : value;
        }
        value = StringUtils.isEmpty(value) ? null : value;

        if (value != null)
        {
            logCommentForDebugging("Value of test data -'" + keyName + "' is -'" + value + "'");
        } else
        {
            logCommentForDebugging("'" + keyName + "' not found in test data");
        }

        return value;
    }

    /**
     * Get the Run Time Property value
     *
     * @param keyName whose value is needed
     * @return value of the specified key
     */
    private String getRunTimePropertyInternal(String keyName)
    {
        String value = null;
        try
        {
            value = runTimeProperties.get(keyName.toLowerCase()).toString();
            logCommentForDebugging("Read RunTime Property -'" + keyName + "' as -'" + value + "'");
        } catch (Exception e)
        {
            logCommentForDebugging("'" + keyName + "' not found in run time properties");
            return null;
        }
        return value;
    }

    /**
     * Replaces the arguments like {$someArg} present in input string with its value from RuntimeProperties
     *
     * @param input string in which some Argument is present
     * @return replaced string
     */
    public String replaceArgumentsWithRunTimeProperties(String input)
    {
        if (input.contains("{$"))
        {
            int index = input.indexOf("{$");
            input.length();
            input.indexOf("}", index + 2);
            String key = input.substring(index + 2, input.indexOf("}", index + 2));
            String value = getRunTimeProperty(key);

            value = StringUtils.isEmpty(value) ? "null" : value;
            if (value.equalsIgnoreCase("null"))
            {
                input = input.replace("\"{$" + key + "}\"", value);
            }

            value = value.equalsIgnoreCase("{skip}") ? "" : value;
            input = input.replace("{$" + key + "}", value);
            return replaceArgumentsWithRunTimeProperties(input);
        }
        return input;
    }

    public void logComment(String message)
    {
        Log.comment(this, message);
    }

    public void logCommentJson(String initialMessage, String fullMessage, String color)
    {
        Log.commentJson(this, initialMessage, fullMessage, color);
    }

    public void logColorfulComment(String message, String color)
    {
        Log.comment(this, message, color);
    }


    public <T> void logStep(String message)
    {
        Log.step(this, message);
    }

    public void logCommentForDebugging(String message)
    {
        if (isDebugMode)
        {
            Log.comment(this, message);
        }
    }

    public void logWarning(String message)
    {
        Log.warning(this, message);
    }

    public void logWarning(String message, boolean pageCapture)
    {
        Log.warning(this, message, pageCapture);
    }

    public void logWarning(String what, String expected, String actual)
    {
        String message = "Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'";
        Log.warning(this, message);
    }

    public void logFail(String message)
    {
        testResult = false;
        Log.fail(this, message);
    }

    public void logFailToEndExecution(String message)
    {
        Browser.takeScreenshot(this);
        enableScreenshot = false;
        retry = false;
        endExecutionOnfailure = true;
        testResult = false;
        Log.fail(this, message);
    }

    public <T> void logFail(String what, T expected, T actual)
    {
        testResult = false;
        String message = "Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'";
        Log.fail(this, message);
    }

    public void logPass(String message)
    {
        Log.pass(this, message);
    }

    public <T> void logPass(String what, T actual)
    {
        String message = "Verified '" + what + "' as :-'" + actual + "'";
        Log.pass(this, message);
    }

    public void logExceptionAndFail(Throwable e)
    {
        logExceptionAndFail("", e);
    }

    public void logExceptionToEndExecution(String message, Throwable e)
    {
        endExecutionOnfailure = true;
        testResult = false;
        String fullStackTrace = ExceptionUtils.getStackTrace(e);
        Log.fail(this, message + "\nException Message:- " + fullStackTrace);
    }

    public void logExceptionAndFail(String message, Throwable e)
    {
        testResult = false;
        String fullStackTrace = ExceptionUtils.getStackTrace(e);
        Log.fail(this, message + "\nException Message:- " + fullStackTrace);
    }

    public void logException(String message, Throwable e)
    {
        if (e.getMessage() == null)
        {
            logWarning(message);
            if (isDebugMode)
            {
                String fullStackTrace = ExceptionUtils.getStackTrace(e);
                Log.warning(this, " \nFull Exception Stacktrace:- \n" + fullStackTrace);
            }
        } else
        {
            logWarning(message + ". \nException Message:- " + e.getMessage());
        }
        if (isDebugMode)
        {
            String fullStackTrace = ExceptionUtils.getStackTrace(e);
            Log.warning(this, " \nFull Exception Stacktrace:- \n" + fullStackTrace);
        }
    }

    /**
     * End Test
     *
     * @param result - ITestResult
     */
    protected void endTest(ITestResult result)
    {
        if (getRunTimeProperty("LogBrowserConsoleErrors") != null && getRunTimeProperty("LogBrowserConsoleErrors").equalsIgnoreCase("true") && driver != null && appiumDriver == null)
        {
            logComment("Printing all Console Error Logs...");
            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
            for (LogEntry logEntry : logEntries)
            {
                logWarning("[" + logEntry.getLevel() + "] " + logEntry.getMessage());
            }
        }
        testEndTime = DataGenerator.getCurrentDateTime("dd-MM-yyyy HH:mm:ss");
        endExecutionOnfailure = false;
        enableScreenshot = false;
        List<String> reporterOutput = Reporter.getOutput(result);
        if (testResult)
        {
            if (!CommonUtilities.listContainsString(reporterOutput, "<B>Failure occured in test '" + testcaseName + "' Ended on '"))
            {
                logPass("<B>Test Passed '" + testcaseName + "' of Class '" + testcaseClass + "' Ended on '" + testEndTime + "'</B>");
            }
        } else
        {
            if (!CommonUtilities.listContainsString(reporterOutput, "<B>Test Passed '" + testcaseName + "' Ended on '"))
            {
                result.setAttribute("reporterLog", reporterOutput);// For PDF Report
                logFail("<B>Failure occured in test '" + testcaseName + "' of Class '" + testcaseClass + "' Ended on '" + testEndTime + "'</B>");
            }
        }

        if (testStartTime != null)
        {
            long minutes = 0;
            long seconds = 0;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String minuteOrMinutes = " ";
            String secondOrSeconds = "";
            long timeinMillis = 0;
            try
            {
                timeinMillis = (dateFormat.parse(testEndTime).getTime() - dateFormat.parse(testStartTime).getTime()) / 1000;
                minutes = timeinMillis / 60;
                seconds = timeinMillis % 60;
                if (minutes > 1)
                {
                    minuteOrMinutes = "s ";
                }
                if (seconds > 1)
                {
                    secondOrSeconds = "s";
                }
            } catch (Exception e)
            {
            }
            if (!CommonUtilities.listContainsString(reporterOutput, "<font color='Blue'><B>Total time taken by Test '" + testcaseName + "' : '"))
            {
                logComment("<font color='Blue'><B>Total time taken by Test '" + testcaseName + "' of Class '" + testcaseClass + "' : '" + minutes + " minute" + minuteOrMinutes + seconds + " second" + secondOrSeconds + "' </B></font>");
            }
            System.out.println("Finished test - '" + testcaseName + "' from class - '" + testcaseClass + "' in " + timeinMillis + " seconds");
        }

        if (GenerateTestngXmlAndRun.isDebugMode && GenerateTestngXmlAndRun.testClassAndMethodDetails != null)
        {
            for (String classes : GenerateTestngXmlAndRun.testClassAndMethodDetails.keySet())
            {
                ArrayList<String> methods = GenerateTestngXmlAndRun.testClassAndMethodDetails.get(classes);
                if (methods.remove(testcaseName))
                {
                    GenerateTestngXmlAndRun.testClassAndMethodDetails.put(classes, methods);
                }
            }
        }
    }

    /**
     * This function will read the Sheet from the excel file specific to particular project
     *
     * @param sheetName - Particular sheet name inside project specific excel file
     * @return - object of TestDataReader
     */
    public TestDataReader getExcelSheet(String sheetName)
    {
        return getExcelSheet(null, sheetName);
    }

    /**
     * This function will read the Sheet from the excel file passed in as testDataSheetToBeUsed parameter
     *
     * @param testDataSheetToBeUsed
     * @param sheetName
     * @return
     */
    protected TestDataReader getExcelSheet(String testDataSheetToBeUsed, String sheetName)
    {
        TestDataReader testDataReader = null;
        if (testDataSheetToBeUsed == null)
        {
            testDataSheetToBeUsed = getTestDataSheetToBeUsed();
        }

        String csvFile = System.getProperty("user.dir") + File.separator + "TestData" + File.separator + testDataSheetToBeUsed.split("TestData")[0] + File.separator + "CsvFiles" + File.separator + this.getRunTimeProperty("Environment").toLowerCase() + File.separator + sheetName + ".csv";
        synchronized (Config.class)
        {
            testDataReader = testDataReaderHashMap.get(csvFile);
            if (testDataReader == null)
            {
                testDataReader = new TestDataReader(this, sheetName, csvFile);
                testDataReaderHashMap.put(csvFile + sheetName, testDataReader);
            }
        }
        return testDataReader;
    }

    private String getTestDataSheetToBeUsed()
    {
        ProjectName projectName = null;
        String lastClass = null;
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++)
        {
            StackTraceElement ste = stElements[i];
            if (ste.getClassName().contains("Automation"))
            {
                lastClass = ste.getClassName();
            }
        }

        String[] allNames = lastClass.split("\\.");
        for (int i = 1; i < allNames.length; i++)
        {
            for (ProjectName project : ProjectName.values())
            {
                if (project.toString().equalsIgnoreCase(allNames[i]))
                {
                    projectName = project;
                    break;
                }
            }
            if (projectName != null)
            {
                break;
            }
        }
        if (projectName == null)
        {
            projectName = ProjectName.CustomerFrontend;
        }

        putRunTimeProperty("ProjectName", projectName.toString());
        logCommentForDebugging("Excel being used ==>> " + projectName.testDataSheetToBeUsed);
        return projectName.testDataSheetToBeUsed;
    }
}