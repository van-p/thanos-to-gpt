package Automation.Access.web;

import Automation.Access.customer.helpers.AccessEnums.LoginType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.LoginPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestLoginStepsLockedAttempts extends TestBase
{

    @TestVariables(testrailData = "8:C108", automatedBy = QA.TanHo)
    @Test(description = "Test Login with 3 incorrect password attempts", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases", "login"})
    public void testLoginWith3IncorrectPasswordAttempts(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 6);

        testConfig.logStep("Login with 3 incorrect password attempts");
        accessHelper.loginPage = (LoginPage) accessHelper.doLoginWithIncorrectCredentials(LoginType.IncorrectPassword3Times);
        testConfig.logStep("Validate locked attempts message should be displayed and correct");
        accessHelper.loginPage.validateLockedAttemptsMessage(3);
    }

    @TestVariables(testrailData = "8:C109", automatedBy = QA.TanHo)
    @Test(description = "Test Login with 3 incorrect OTP attempts", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases", "login"})
    public void testLoginWith3IncorrectOtpAttempts(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 7);

        testConfig.logStep("Login with 3 incorrect OTP attempts");
        accessHelper.loginPage = (LoginPage) accessHelper.doLoginWithIncorrectCredentials(LoginType.IncorrectOtp3Times);
        testConfig.logStep("Validate locked attempts message should be displayed and correct");
        accessHelper.loginPage.validateLockedAttemptsMessage(3);
    }
}
