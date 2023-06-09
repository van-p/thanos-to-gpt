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

public class TestGetUserAccessApis extends TestBase
{

    @TestVariables(testrailData = "6:C8117", automatedBy = QA.Mukesh)
    @Test(description = "Verify get all the User accesses - GET /user-access - 200 Ok ", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1UserAccesses(Config testConfig)
    {
        int userDetailsRowNum = 3;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/user-access with valid headers and verify that response is accurate");
        Response userAccessesResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetUserAccesses, null);
        accessHelper.verifyApiResponse(userAccessesResponse, AccessApiDetails.GetUserAccesses, AccessJsonDetails.GetUserAccessesSuccessfulResponse);
    }
}
