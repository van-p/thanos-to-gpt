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

public class TestUpdateBusinessDetailsApis extends TestBase
{

    @TestVariables(testrailData = "6:C8826", automatedBy = QA.Priya)
    @Test(description = "To verify user can update Business Headquarter", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1UpdateBusinessHeadquarter(Config testConfig)
    {
        int userDetailsRowNum = 23;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute authLogin API to get authentication token to be used in the header of /v1/businesses/{$businessUuid}/properties API");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute /v1/businesses/{$businessUuid}/properties API to Update Business Headquarter Country in Business details and then validate that response is accurate and same country is updated");
        Response updateBusinessHeadquarterResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutBusinessProperties, AccessJsonDetails.PutBusinessHeadquarterRequestSchema);
        accessHelper.verifyApiResponse(updateBusinessHeadquarterResponse, AccessApiDetails.PutBusinessProperties, AccessJsonDetails.PutBusinessHeadquarterSuccessResponse);
    }

    @TestVariables(testrailData = "6:C8827", automatedBy = QA.Priya)
    @Test(description = "To verify user can update Number of Employees in Business", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1UpdateNumberOfEmployee(Config testConfig)
    {
        int userDetailsRowNum = 24;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute authLogin API to get authentication token to be used in the header of /v1/businesses/{$businessUuid}/properties API");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute /v1/businesses/{$businessUuid}/properties API to Update Number of Employees in Business details and then validate that response is accurate and same Number of Employees is updated");
        Response updateBusinessHeadquarterResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutBusinessProperties, AccessJsonDetails.PutNumberOfEmployeeRequestSchema);
        accessHelper.verifyApiResponse(updateBusinessHeadquarterResponse, AccessApiDetails.PutBusinessProperties, AccessJsonDetails.PutNumberOfEmployeeSuccessResponse);
    }
}
