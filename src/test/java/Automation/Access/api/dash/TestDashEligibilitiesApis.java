package Automation.Access.api.dash;

import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.api.DashAccessJsonDetails;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestDashEligibilitiesApis extends TestBase
{

    @TestVariables(testrailData = "9:C13454", automatedBy = QA.Loc)
    @Test(description = "Verify that admin user is able to get /dashboard/eligibilities api", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testDashboardGetEligibilities(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 3);

        testConfig.logStep("Execute Api /dashboard/auth/login-password with valid email and get acccess token");
        dashAccessHelper.doLogin();

        testConfig.logStep("Execute Api /dashboard/eligibilities to get list of eligibilities");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.GetEligibilitiesList, null);

        testConfig.logStep("Verify that response is getting correct eligibilities");
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.GetEligibilitiesList, DashAccessJsonDetails.EligibilitiesListSuccessfulResponse);
    }
}