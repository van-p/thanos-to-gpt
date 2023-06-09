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

public class TestDashBusinessesApis extends TestBase
{

    @TestVariables(testrailData = "9:C13998", automatedBy = QA.Loc)
    @Test(description = "Create new business then verify business is created in correct status with the response", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testCreateNewBusiness(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 4);

        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();

        testConfig.logStep("Create business and verify business uuid exist in the response");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostNewBusinesses, DashAccessJsonDetails.PostNewBusinessSchema);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.PostNewBusinesses, null);

        testConfig.logStep("Get business detail and verify businesses state");
        response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.GetBusinessDetails, null);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.GetBusinessDetails, DashAccessJsonDetails.GetBusinessDetailsCreatedFromDashSuccessfulResponse);
        AssertHelper.compareEquals(testConfig, "Business State", StateCode.NewBusiness.getCode(), response.jsonPath().getString("state.reference_code"));
    }

    @TestVariables(testrailData = "9:C14610", automatedBy = QA.Loc)
    @Test(description = "Fulfill Kyb requirement to create Kyb, Verify Kyb is created in correct status", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "dash"})
    public void testFulfillRequirementsToCreateKyb(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 4);

        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();

        testConfig.logStep("Create business and verify business uuid exist in the response");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostNewBusinesses, DashAccessJsonDetails.PostNewBusinessSchema);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.PostNewBusinesses, null);

        testConfig.logStep("Fulfill requirements to create Kyb, verify Kyb is created with correct status");
        dashAccessHelper.fulfillCreateKybRequirements();
        response = dashAccessHelper.waitingForEntityStateChange(EntityType.Business, StateCode.NewBusiness);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.GetBusinessDetails, DashAccessJsonDetails.GetBusinessDetailsCreatedFromDashSuccessfulResponse);
        AssertHelper.compareEquals(testConfig, "Business State", StateCode.PendingBusiness.getCode(), response.jsonPath().getString("state.reference_code"));
        AssertHelper.compareEquals(testConfig, "Kyb State", StateCode.EligibilityNotStarted.getCode(), response.jsonPath().getString("eligibility.state.reference_code"));
    }
}