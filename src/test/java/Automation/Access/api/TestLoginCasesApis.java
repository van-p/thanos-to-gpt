package Automation.Access.api;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestLoginCasesApis extends TestBase
{

    @TestVariables(testrailData = "9:C14123", automatedBy = QA.Loc)
    @Test(description = "Test login to Customer Frontend with account is being requested to provide more information. Verify that KYC/B state is missing_info", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testLoginWithMissingInfoAccount(Config testConfig)
    {
        int userDetailsRowNum = 50;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token");
        Response tokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(tokenResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);

        testConfig.logStep("Execute API /v1/auth/login and verify response return missing info is True");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthLogin, AccessJsonDetails.AuthLoginRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.PostAuthLogin, AccessJsonDetails.MissingInfoAuthLoginSingleBusinessSuccessfulResponse);
    }

    @TestVariables(testrailData = "9:C14124", automatedBy = QA.Loc)
    @Test(description = "Test login to Customer Frontend with account belong to multiple businesses. Verify that response include a list of businesses", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testLoginToAccountHasMultipleBusinesses(Config testConfig)
    {
        int userDetailsRowNum = 51;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token");
        Response tokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(tokenResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);

        testConfig.logStep("Execute API /v1/auth/login and verify that response a list of businesses");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthLogin, AccessJsonDetails.AuthLoginRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.PostAuthLogin, AccessJsonDetails.AuthLoginMultipleBusinessesSuccessfulResponse);
    }
}
