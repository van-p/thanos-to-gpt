package Automation.Access.api.dash;

import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.api.DashAccessJsonDetails;
import Automation.Access.dash.helpers.DashAccessEnums.EntityType;
import Automation.Access.dash.helpers.DashAccessEnums.StateCode;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestDashPeopleApis extends TestBase
{

    @TestVariables(testrailData = "9:C13998", automatedBy = QA.Loc)
    @Test(description = "Create new people. Verify people is created in correct status with the response", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testCreateNewPeople(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 2);

        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();

        testConfig.logStep("Create new people, get response and verify people uuid exist");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostNewPeople, DashAccessJsonDetails.PostNewPeopleSchema);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.PostNewPeople, null);

        testConfig.logStep("Get people detail response and verify people state");
        response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.GetPeopleDetails, null);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.GetPeopleDetails, DashAccessJsonDetails.GetPeopleDetailsCreatedFromDashSuccessfulResponse);
        AssertHelper.compareEquals(testConfig, "People State", StateCode.DraftPeople.getCode(), response.jsonPath().getString("state.reference_code"));
    }

    @TestVariables(testrailData = "9:C14517", automatedBy = QA.Loc)
    @Test(description = "Fulfill requirement to create Kyc. Verify Kyc is created with correct status", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testFulfillRequirementsToCreateKyc(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 2);

        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();

        testConfig.logStep("Create new people, get response and verify people uuid exist");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostNewPeople, DashAccessJsonDetails.PostNewPeopleSchema);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.PostNewPeople, null);

        testConfig.logStep("Fulfill requirements to create Kyc, verify Kyc is created with correct status");
        dashAccessHelper.fulfillCreateKycRequirements();
        response = dashAccessHelper.waitingForEntityStateChange(EntityType.Person, StateCode.DraftPeople);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.GetPeopleDetails, DashAccessJsonDetails.GetPeopleDetailsCreatedFromDashSuccessfulResponse);
        AssertHelper.compareEquals(testConfig, "People State", StateCode.PendingPeople.getCode(), response.jsonPath().getString("state.reference_code"));
        AssertHelper.compareEquals(testConfig, "Kyc State", StateCode.EligibilityNotStarted.getCode(), response.jsonPath().getString("eligibility.state.reference_code"));
    }
}