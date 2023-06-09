package Automation.Access.api;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestFilterUsersApis extends TestBase
{

    @TestVariables(testrailData = "6:C8129", automatedBy = QA.Sanjeevan)
    @Test(description = "Verify get filters for admin user - GET /v1/filters -200 Ok", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1FilterUsers(Config testConfig)
    {
        int userDetailsRowNum = 4;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/filters with valid headers & query params");
        Response userAccessesResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetFilterUsers, null);

        testConfig.logStep("Verify that response is accurate with 'name' key value as 'Search' and 'visible' key value as 'true'");
        accessHelper.verifyApiResponse(userAccessesResponse, AccessApiDetails.GetFilterUsers, AccessJsonDetails.GetFilterUsersSuccessfulResponse);
    }
}