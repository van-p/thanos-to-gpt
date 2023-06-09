package Automation.Payments.dash;

import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.api.DashAccessJsonDetails;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Payments.dash.api.DashPaymentApiDetails;
import Automation.Payments.dash.api.DashPaymentJsonDetails;
import Automation.Payments.dash.helpers.DashPaymentEnums;
import Automation.Payments.dash.helpers.DashPaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestLiveTinkering extends TestBase
{

    @TestVariables(testrailData = "41:C14093", automatedBy = QA.Mukesh)
    @Test(description = "Verify that Admin can execute command with live tinkering", dataProvider = "getTestConfig", groups = {"dash", "regression", "apiCases"})
    public void testExecuteCommand(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        DashPaymentHelper dashPaymentHelper = new DashPaymentHelper(testConfig, 1);

        testConfig.logStep("Execute API POST /dashboard/auth/login-password - Get Dash Login Session");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostAuthLogin, DashAccessJsonDetails.LoginRequestSchema);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.PostAuthLogin, DashAccessJsonDetails.AuthSuccessfulResponse);

        testConfig.logStep("Execute API POST /oauth/token - Get Dash access token and verify token");
        Response authResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostOauthToken, DashAccessJsonDetails.OauthTokenRequestSchema);
        dashAccessHelper.verifyApiResponse(authResponse, DashAccessApiDetails.PostOauthToken, DashAccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Execute API POST /dashboard/commands-on-fly with valid command and verify that response is accurate");
        dashPaymentHelper.setCommandLine(DashPaymentEnums.CommandLine.InvoicingCheckInvoiceStates);
        Response tinkeringResponse = dashPaymentHelper.sendRequestAndGetResponse(DashPaymentApiDetails.LiveTinkering, DashPaymentJsonDetails.CommandOnFlyRequestSchema);
        dashPaymentHelper.verifyApiResponse(tinkeringResponse, DashPaymentApiDetails.LiveTinkering, DashPaymentJsonDetails.CommandOnFlyResponse);
    }

}
