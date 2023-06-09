package Automation.Utils;

import Automation.Utils.Enums.ProjectName;
import Automation.Utils.Enums.QA;
import Automation.Utils.Mobile.App;
import Automation.Utils.Mobile.Emulator;
import org.eclipse.aether.util.StringUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.testng.internal.TestResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Listeners(Automation.Utils.TestListener.class)
public class TestBase
{

    protected final static long DEFAULT_TEST_TIMEOUT = 600000;
    protected static ThreadLocal<Config[]> threadLocalConfig = new ThreadLocal<Config[]>();
    protected static Config beforeSuiteConfig = null;
    protected static List<TestrailData> testrailData = new ArrayList<>();
    // Added a flag to reset browser after every TC execution even while using data provider
    protected boolean resetBrowserOrAppAfterMethod = false;

    @DataProvider(name = "getTestConfig")
    public Object[][] getTestConfig(Method method)
    {
        Config testConfig = new Config();
        testConfig.testcaseName = method.getName();
        testConfig.testcaseClass = method.getDeclaringClass().getName();
        testConfig.testStartTime = DataGenerator.getCurrentDateTime("dd-MM-yyyy HH:mm:ss");
        System.out.println("Running test - '" + testConfig.testcaseName + "' from class - '" + testConfig.testcaseClass + "'...");
        threadLocalConfig.set(new Config[]{testConfig});
        return new Object[][]{{testConfig}};
    }

    @DataProvider(name = "GetTwoTestConfig")
    public Object[][] GetTwoTestConfig(Method method)
    {
        Config testConfig = new Config();
        Config secondaryConfig = new Config();
        testConfig.testcaseName = method.getName();
        testConfig.testcaseClass = method.getDeclaringClass().getName();
        testConfig.testStartTime = DataGenerator.getCurrentDateTime("dd-MM-yyyy HH:mm:ss");
        System.out.println("Running test - '" + testConfig.testcaseName + "' from class - '" + testConfig.testcaseClass + "'...");
        threadLocalConfig.set(new Config[]{testConfig, secondaryConfig});
        return new Object[][]{{testConfig, secondaryConfig}};
    }

    // TODO: fix this function for data driven test cases that are using retry analyser.

    /**
     * This method is used to implement data driven approach to read complete excel data sheet for a test method. Each excel sheet row represents one test case. Herein, the workbook name and sheet name to be used are passed as testDataSheetToBeUsed and dataSheetName from the testcase as @TestVariables Annotation
     *
     * @param method - testNG's method object
     * @return config instance and file data
     */
    @DataProvider(name = "GetExcelData")
    public Object[][] getExcelData(Method method)
    {
        Object[][] object = null;
        Config testConfig = (Config) getTestConfig(method)[0][0];
        // Create a object of annotation
        Annotation annotation = method.getAnnotation(TestVariables.class);
        TestVariables annotationObj = (TestVariables) annotation;
        TestDataReader readerData = testConfig.getExcelSheet(annotationObj.testDataSheetToBeUsed().toString(), annotationObj.dataSheetName());
        // get total data Rows and columns
        int totalRowLen = readerData.getRecordsNum();
        int columnLen = readerData.getColumnNum();
        if (annotationObj.dataSheetRowNum().length >= 1 & annotationObj.dataSheetRowNum()[0] != 0)
        {
            // read data from mentioned rows only
            int rowLen = annotationObj.dataSheetRowNum().length;
            object = new Object[rowLen][columnLen];
            int i = 0;
            for (int row : annotationObj.dataSheetRowNum())
            {
                // get data from each mentioned row number
                object[i][0] = testConfig;
                for (int col = 1; col < columnLen; col++)
                {
                    object[i][col] = readerData.getData(testConfig, row, readerData.getHeaderData(col));
                }
                i++;
                testConfig.testcasesRemaining = rowLen;
            }
        } else
        {
            // read data from all the rows present in the sheet
            object = new Object[totalRowLen - 1][columnLen];
            for (int row = 1; row < totalRowLen; row++)
            {
                // testconfig object
                object[row - 1][0] = testConfig;
                for (int col = 1; col < columnLen; col++)
                {
                    object[row - 1][col] = readerData.getData(testConfig, row, readerData.getHeaderData(col));
                }
                testConfig.testcasesRemaining = totalRowLen;
            }
        }
        return object;
    }

    /**
     * This method is used to implement data driven approach to read complete excel data sheet for a test method. Each excel sheet row represents one test case. Herein, the workbook name and sheet name to be used are passed as testDataSheetToBeUsed and dataSheetName from the testcase as @TestVariables Annotation This method also reset the browser after every execution of test case.
     *
     * @param method - testNG's method object
     * @return config instance and file data
     */
    @DataProvider(name = "GetExcelDataWithBrowserOrAppReset")
    public Object[][] getExcelDataWithBrowserOrAppReset(Method method)
    {
        Object[][] object = null;
        Config testConfig = (Config) getTestConfig(method)[0][0];
        // Create a object of annotation
        Annotation annotation = method.getAnnotation(TestVariables.class);
        TestVariables annotationObj = (TestVariables) annotation;
        TestDataReader readerData = testConfig.getExcelSheet(annotationObj.testDataSheetToBeUsed().toString(), annotationObj.dataSheetName());
        // get total data Rows and columns
        int totalRowLen = readerData.getRecordsNum();
        int columnLen = readerData.getColumnNum();
        if (annotationObj.dataSheetRowNum().length >= 1 & annotationObj.dataSheetRowNum()[0] != 0)
        {
            // read data from mentioned rows only
            int rowLen = annotationObj.dataSheetRowNum().length;
            object = new Object[rowLen][columnLen];
            int i = 0;
            for (int row : annotationObj.dataSheetRowNum())
            {
                // get data from each mentioned row number
                object[i][0] = testConfig;
                for (int col = 1; col < columnLen; col++)
                {
                    object[i][col] = readerData.getData(testConfig, row, readerData.getHeaderData(col));
                }
                i++;
                testConfig.testcasesRemaining = rowLen;
            }
        } else
        {
            // read data from all the rows present in the sheet
            object = new Object[totalRowLen - 1][columnLen];
            for (int row = 1; row < totalRowLen; row++)
            {
                // testconfig object
                object[row - 1][0] = testConfig;
                for (int col = 1; col < columnLen; col++)
                {
                    object[row - 1][col] = readerData.getData(testConfig, row, readerData.getHeaderData(col));
                }
                testConfig.testcasesRemaining = totalRowLen;
            }
        }
        resetBrowserOrAppAfterMethod = true;
        return object;
    }

    /**
     * This method is used to implement data driven approach to read only specific row in excel data sheet for a test method. Each excel sheet row represents one test case. Herein, the workbook name and sheet name to be used are passed as testDataSheetToBeUsed and dataSheetName from the testcase as @TestVariables Annotation This method also reset the browser after every execution of test case.
     *
     * @param method - testNG's method object
     * @return config instance and file data
     */
    @DataProvider(name = "GetExcelRows")
    public Object[][] getExcelRows(Method method)
    {
        Object[][] object = null;
        Config testConfig = (Config) getTestConfig(method)[0][0];
        // Create a object of annotation
        Annotation annotation = method.getAnnotation(TestVariables.class);
        TestVariables annotationObj = (TestVariables) annotation;
        TestDataReader readerData = testConfig.getExcelSheet(annotationObj.testDataSheetToBeUsed().toString(), annotationObj.dataSheetName());
        // get total data Rows and columns
        int totalRowLen = readerData.getRecordsNum();
        if (annotationObj.dataSheetRowNum().length >= 1 & annotationObj.dataSheetRowNum()[0] != 0)
        {
            // read data from mentioned rows only
            int rowLen = annotationObj.dataSheetRowNum().length;
            object = new Object[rowLen][2];
            int i = 0;
            for (int row : annotationObj.dataSheetRowNum())
            {
                // get data from each mentioned row number
                object[i][0] = testConfig;
                object[i][1] = row;
                i++;
                testConfig.testcasesRemaining = rowLen;
            }
        } else
        {
            // read data from all the rows present in the sheet
            object = new Object[totalRowLen - 1][2];
            for (int row = 1; row < totalRowLen; row++)
            {
                // testconfig object
                object[row - 1][0] = testConfig;
                object[row - 1][1] = row;
                testConfig.testcasesRemaining = totalRowLen;
            }
        }
        return object;
    }

    @SuppressWarnings("static-access")
    @BeforeSuite(alwaysRun = true)
    @Parameters({"projectName", "environment", "browserName", "browserVersion", "resultsDirectory", "mobileViewExecution", "mobileAppExecution", "remoteExecution", "debugMode", "mobileAppName", "locale", "appLanguage"})
    public void beforeSuiteExecution(@Optional String projectName, @Optional String environment, @Optional String browserName, @Optional String browserVersion, @Optional String resultsDirectory, @Optional String mobileViewExecution, @Optional String mobileAppExecution, @Optional String remoteExecution, @Optional String debugMode, @Optional String mobileAppName, @Optional String locale, @Optional String appLanguage)
    {
        Config.projectName = projectName;
        Config.environment = environment;
        Config.browserName = browserName;
        Config.browserVersion = browserVersion;
        Config.resultsDirectory = resultsDirectory;
        Config.isMobileViewExecution = mobileViewExecution != null && mobileViewExecution.equalsIgnoreCase("true");
        Config.isMobileAppExecution = mobileAppExecution != null && mobileAppExecution.equalsIgnoreCase("true");
        Config.isRemoteExecution = remoteExecution != null && remoteExecution.equalsIgnoreCase("true");
        Config.mobileAppName = mobileAppName;
        Config.isDebugMode = debugMode != null && debugMode.equalsIgnoreCase("true");
        Config.locale = locale;
        Config.appLanguage = appLanguage;

        beforeSuiteConfig = new Config();
        if (Config.isMobileAppExecution)
        {
            if (beforeSuiteConfig.getRunTimeProperty("useOptimusCloud").equalsIgnoreCase("true"))
            {

            }
        }

        // Before suite configs from different projects
        if (Config.projectName != null && Config.isRemoteExecution)
        {
            ProjectName project = ProjectName.valueOf(Config.projectName);
            if (project == ProjectName.App)
            {
                try
                {
                    App.downloadPaymentSdkApk(beforeSuiteConfig, resultsDirectory.split("/")[5]);
                } catch (Exception e)
                {
                    beforeSuiteConfig.logExceptionAndFail("Unable to download the apk", e);
                }
            }
        }
    }

    @SuppressWarnings("static-access")
    @AfterMethod(alwaysRun = true)
    public void afterMethodExecution(ITestResult result)
    {
        Boolean clearConfig = true;
        Config[] testConfigs = threadLocalConfig.get();
        if (testConfigs != null)
        {
            for (Config testConfig : testConfigs)
            {
                if (testConfig != null)
                {
                    if (testConfig.testcasesRemaining > 1)
                    {
                        clearConfig = false;
                        if (result.getStatus() != TestResult.SUCCESS || (resetBrowserOrAppAfterMethod))
                        {
                            if (!(testConfig.useSingleBrowser && testConfig.staticDriver != null))
                            {
                                Browser.closeBrowser(testConfig);
                                App.closeApplication(testConfig);
                                testConfig.driver = null;
                                testConfig.appiumDriver = null;
                            }
                        }
                    }

                    if (clearConfig)
                    {
                        if (!(testConfig.useSingleBrowser && testConfig.staticDriver != null))
                        {
                            Browser.closeBrowser(testConfig);
                            App.closeApplication(testConfig);
                            testConfig.runTimeProperties.clear();
                            testConfig = null;
                        }
                    } else
                    {
                        // reset config data so that old failure data is not passed to next test case in data driven testcases
                        testConfig.softAssert = new SoftAssert();
                        testConfig.testResult = true;
                        testConfig.enableScreenshot = true;
                        testConfig.testcasesRemaining--;
                    }
                } else
                {
                    System.out.println("testConfig object not found");
                }
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuiteExecution()
    {
        if (Config.isMobileAppExecution)
        {
            if (beforeSuiteConfig.getRunTimeProperty("useOptimusCloud").equalsIgnoreCase("true"))
            {

            } else
            {
                if (Config.isRemoteExecution)
                {
                    if (beforeSuiteConfig.getRunTimeProperty("useRealDevice") == null || beforeSuiteConfig.getRunTimeProperty("useRealDevice").equalsIgnoreCase("false"))
                    {
                        Emulator.closeEmulator(beforeSuiteConfig);
                    }
                }
            }
        }
    }

    public static class TestrailData
    {
        private final String methodName;
        private String suiteId;
        private String testcaseIds;
        private String emailId;


        public TestrailData(String methodName, String testRailData, QA automatedBy)
        {
            this.methodName = methodName;
            if (!StringUtils.isEmpty(testRailData))
            {
                this.suiteId = testRailData.split(":")[0];
                this.testcaseIds = testRailData.split(":")[1];
                this.emailId = automatedBy.getEmail();
            }
        }

        public String getMethodName()
        {
            return methodName;
        }

        public String getSuiteId()
        {
            return suiteId;
        }

        public String getTestcaseIds()
        {
            return testcaseIds;
        }

        public String getEmailId()
        {
            return emailId;
        }
    }
}
