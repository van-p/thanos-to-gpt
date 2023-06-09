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

public class TestUpdatePersonalDetailsApis extends TestBase
{

    @TestVariables(testrailData = "6:C8563", automatedBy = QA.Quan)
    @Test(description = "To verify user can update Preferred Name", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1UpdatePreferredName(Config testConfig)
    {
        int userDetailsRowNum = 40;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Update Preferred Name in personal details");
        Response updatePreferredNameResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutUpdatePreferredName, null);
        accessHelper.verifyApiResponse(updatePreferredNameResponse, AccessApiDetails.PutUpdatePreferredName, AccessJsonDetails.UpdatePreferredNameSuccessfulResponse);
    }

    @TestVariables(testrailData = "6:C8563", automatedBy = QA.Priya)
    @Test(description = "Verify update phone number API", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testV1UpdatePhoneNumber(Config testConfig)
    {
        int userDetailsRowNum = 22;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute authLogin API to get authentication token to be used in the header of /v1/phones API ");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute peoples API with valid credentials to get the People uuid which will be used as Source uuid in the request body of /v1/phones API ");
        Response peopleResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelper.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);

        testConfig.logStep("Execute /v1/phones API to update the Phone Number of the Person and then validate that response is accurate");
        Response updatePhoneNumberResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostUpdatePhoneNumber, AccessJsonDetails.UpdatePhoneNumberRequestSchema);
        accessHelper.verifyApiResponse(updatePhoneNumberResponse, AccessApiDetails.PostUpdatePhoneNumber, AccessJsonDetails.UpdatePhoneNumberSuccessfulResponse);
    }
}
