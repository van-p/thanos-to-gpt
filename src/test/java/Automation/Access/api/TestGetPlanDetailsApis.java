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

public class TestGetPlanDetailsApis extends TestBase
{

    @TestVariables(testrailData = "6:C8126", automatedBy = QA.Priya)
    @Test(description = "Verify get all the Plan Details  - GET /plan-details-and-extra-charges - 200 Ok ", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1PlanDetails(Config testConfig)
    {
        int userDetailsRowNum = 5;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token with valid email and set X-Auth-Session for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/plan-details-and-extra-charges with valid headers and verify that response is accurate with 'is_active' key value as 'true', 'plan_key' key value as 'standard'");
        Response getPlanDetailsResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetPlanDetails, null);
        accessHelper.verifyApiResponse(getPlanDetailsResponse, AccessApiDetails.GetPlanDetails, AccessJsonDetails.GetPlanDetailsSuccessfulResponse);
    }

}
