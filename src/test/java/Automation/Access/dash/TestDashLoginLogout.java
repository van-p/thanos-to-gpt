package Automation.Access.dash;

import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Access.dash.web.DashHomePage;
import Automation.Access.dash.web.DashNavigationPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestDashLoginLogout extends TestBase
{

    @TestVariables(testrailData = "452:C13781", automatedBy = QA.Loc)
    @Test(description = "Login to Admin Dashboard successful", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testLoginToAdminDashboardThenLogout(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);

        testConfig.logStep("Login to admin dashboard as admin user");
        DashNavigationPage dashNavigationPage = dashAccessHelper.loginToAdminDashboard();

        testConfig.logStep("Verify Home Page is loaded");
        dashAccessHelper.dashHomePage = new DashHomePage(testConfig);
        dashAccessHelper.dashHomePage.verifyDashHomePage();

        testConfig.logStep("Logout from Dashboard");
        dashNavigationPage.logoutFromDash();
    }
}