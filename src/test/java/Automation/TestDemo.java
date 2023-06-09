package Automation;

import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import Automation.Utils.GenerateTestngXmlAndRun;
import Automation.Utils.TestBase;
import org.testng.annotations.Test;

public class TestDemo extends TestBase
{

    @Test(timeOut = DEFAULT_TEST_TIMEOUT, dataProvider = "getTestConfig", description = "This Demo")
    public void testDemo(Config testConfig)
    {
        String projectName = "CustomerFrontend";
        String environment = "Staging";
        String browserName = "Chrome";
        String sendEmailTo = "mukes@example.com";
        String jobBuildTag = DataGenerator.generateRandomAlphaNumericString(10);
        String groupNames = "regression";
        String sendReportOnSlack = "false";
        String mobileViewExecution = "false";
        String branchName = "main";
        String debugMode = "false";
        String rerunFailures = "false";
        String locale = "SG";
        String appLanguage = "EN";
        GenerateTestngXmlAndRun.remoteExecution = false;

        String[] args = {projectName, environment, browserName, sendEmailTo, jobBuildTag, groupNames, sendReportOnSlack, mobileViewExecution, branchName, debugMode, rerunFailures, locale, appLanguage};
        GenerateTestngXmlAndRun.main(args);
    }

    @Test(timeOut = DEFAULT_TEST_TIMEOUT, dataProvider = "getTestConfig", description = "This is demo testcase 1", groups = {"dummyGroup"})
    public void exampleTestcase1(Config testConfig)
    {
        testConfig.logPass("This testcases is PASSED intentionally to show the example fo successful case");

    }

    @Test(timeOut = DEFAULT_TEST_TIMEOUT, dataProvider = "getTestConfig", description = "This is demo testcase 2", groups = {"dummyGroup"})
    public void exampleTestcase2(Config testConfig)
    {
        testConfig.logFail("This testcases is FAILED intentionally to show the example fo failed case");
    }
}