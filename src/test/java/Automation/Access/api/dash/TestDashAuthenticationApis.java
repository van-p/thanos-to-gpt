package Automation.Access.api.dash;

import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestDashAuthenticationApis extends TestBase
{

    @TestVariables(testrailData = "9:C13455", automatedBy = QA.Loc)
    @Test(description = "Verify that admin user is able to do successful login using /dashboard/auth/login-password api", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testSuccessfulLoginLogout(Config testConfig) {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 5);
    
        testConfig.logStep("Execute Api /dashboard/auth/login-password with valid email and get the X-Auth-Session");
        dashAccessHelper.doLogin();

        testConfig.logStep("Execute Api /dashboard/auth/logout");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.DeleteAuthLogout, null);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.DeleteAuthLogout, null);
    }
}