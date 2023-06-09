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

public class TestNavigationItemsApis extends TestBase
{

    @TestVariables(testrailData = "6:C8885", automatedBy = QA.Priya)
    @Test(description = "Verify get all the Navigation Items Details  - GET /navigation-items/business-person/all - 200 Ok ", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1AllNavigationItems(Config testConfig)
    {
        int userDetailsRowNum = 8;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/navigation-items/business-person/all with valid headers and verify that status and response value for 'uuid','title','key','position' and 'icon_url' keys are same as expected");
        Response getAllNavigationItemResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAllNavigationItems, null);
        accessHelper.verifyApiResponse(getAllNavigationItemResponse, AccessApiDetails.GetAllNavigationItems, AccessJsonDetails.GetAllNavigationItemSuccessfulResponse);
    }

    @TestVariables(testrailData = "6:C8891", automatedBy = QA.Priya)
    @Test(description = "Verify get available the Navigation Items Details  - GET /v1/navigation-items/business-person/available - 200 Ok ", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1AvailableNavigationItems(Config testConfig)
    {
        int userDetailsRowNum = 20;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/navigation-items/business-person/available with valid headers ");
        Response getAvailableNavigationItemResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAvailableNavigationItems, null);

        testConfig.logStep("Verify that status and response value for 'uuid','title','key','position','badge','web_destination',is_locked,'type' and 'icon_url' keys are same as expected");
        accessHelper.verifyApiResponse(getAvailableNavigationItemResponse, AccessApiDetails.GetAvailableNavigationItems, AccessJsonDetails.GetAvailableNavigationItemSuccessfulResponse);
    }
}