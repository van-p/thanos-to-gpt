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

public class TestDashSubscriptionAddNewModuleApis extends TestBase
{

    @TestVariables(testrailData = "9:C13455", automatedBy = QA.Quan)
    @Test(description = "Verify user is able to create, update and delete a module", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testModuleCreation(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);

        testConfig.logStep("Execute Api /dashboard/auth/login-password with valid email and get the X-Auth-Session");
        dashAccessHelper.doLogin();

        testConfig.logStep("Execute Api POST dashboard/premium-modules to create a new module");
        Response moduleResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostNewModule, DashAccessJsonDetails.PostNewModuleSchema);
        dashAccessHelper.verifyApiResponse(moduleResponse, DashAccessApiDetails.PostNewModule, DashAccessJsonDetails.PostNewModuleSchemaResponse);

        testConfig.logStep("Execute Api GET dashboard/premium-modules/moduleUuid to get and verify module detail");
        moduleResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.GetModuleUuid, null);
        dashAccessHelper.verifyApiResponse(moduleResponse, DashAccessApiDetails.GetModuleUuid, DashAccessJsonDetails.GetNewModuleSchemaResponse);

        testConfig.logStep("Execute Api PUT dashboard/premium-modules/moduleUuid to update module detail");
        moduleResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PutModule, DashAccessJsonDetails.PutModuleSchema);
        dashAccessHelper.verifyApiResponse(moduleResponse, DashAccessApiDetails.PutModule, DashAccessJsonDetails.PutModuleSchemaResponse);

        testConfig.logStep("Execute Api GET dashboard/premium-modules/moduleUuid to get and verify updated module detail");
        moduleResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.GetModuleUuid, null);
        dashAccessHelper.verifyApiResponse(moduleResponse, DashAccessApiDetails.GetModuleUuid, DashAccessJsonDetails.GetNewModuleSchemaResponse);

        testConfig.logStep("Execute Api DELETE dashboard/premium-modules/moduleUuid to delete a module");
        moduleResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.DeleteModule, null);
        dashAccessHelper.verifyApiResponse(moduleResponse, DashAccessApiDetails.DeleteModule, null);
    }
}