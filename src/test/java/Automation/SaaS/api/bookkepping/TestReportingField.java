package Automation.SaaS.api.bookkepping;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Api.ApiDetails.Headers;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestReportingField extends TestBase
{

    @TestVariables(testrailData = "77:C11495", automatedBy = QA.Alex)
    @Test(description = "To verify the reporting field saved successfully, the reporting fields will shown in all transfer pages", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testCreateReportingField(Config testConfig)
    {
        int customerDetailsRowNum = 22;

        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);

        testConfig.logStep("Execute API GET /oauth/token - Get access token and verify token");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Using Bookkeeping business - third business in account");
        testConfig.putRunTimeProperty(Headers.xexampleBusinessUuid.getValue(), accessHelper.testConfig.testData.get("accountUuid"));

        testConfig.logStep("Execute Api /v1/accounting/reporting-fields and verify reporting field added");
        Response reportingFieldAdded = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddReportingField, SaasJsonDetails.AddReportingFieldRequestSchema);
        saasHelper.verifyApiResponse(reportingFieldAdded, SaasApiDetails.AddReportingField, null);
    }

    @TestVariables(testrailData = "77:C11506", automatedBy = QA.Alex)
    @Test(description = "To verify the reporting field deactivated successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testDeactivateReportingField(Config testConfig)
    {
        int customerDetailsRowNum = 22;

        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);

        testConfig.logStep("Execute API GET /oauth/token - Get access token and verify token");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Using Bookkeeping business - third business in account");
        testConfig.putRunTimeProperty(Headers.xexampleBusinessUuid.getValue(), accessHelper.testConfig.testData.get("accountUuid"));

        testConfig.logStep("Execute Api /v1/accounting/reporting-fields and verify reporting field added");
        Response reportingFieldAdded = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddReportingField, SaasJsonDetails.AddReportingFieldRequestSchema);
        saasHelper.verifyApiResponse(reportingFieldAdded, SaasApiDetails.AddReportingField, null);

        testConfig.logStep("Execute Api /v1/accounting/reporting-fields and verify reporting field deactivated");
        Response reportingFieldDeactivate = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeactivateReportingField, SaasJsonDetails.DeactivateReportingFieldRequestSchema);
        AssertHelper.compareEquals(testConfig, "Status Code", 200, reportingFieldDeactivate.statusCode());
        Response reportingFieldDeactivated = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetDetailReportingField, null);
        saasHelper.verifyApiResponse(reportingFieldDeactivated, SaasApiDetails.GetDetailReportingField, null);
    }
}
