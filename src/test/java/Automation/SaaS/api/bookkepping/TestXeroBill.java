package Automation.SaaS.api.bookkepping;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestXeroBill extends TestBase
{

    @TestVariables(testrailData = "12:C7698", automatedBy = QA.NgaVu)
    @Test(description = "Verify move bill from pending review to pending sync", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAddXeroBillToPendingSync(Config testConfig)
    {
        int customerDetailsRowNum = 22;

        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);

        testConfig.logStep("Execute API GET /oauth/token - Authenticate as an Admin user");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/auth - to get business account uuid with with 'Bookkeeping' business");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        testConfig.putRunTimeProperty("business_uuid", authResponse.jsonPath().getString("businesses.find { it.name == '" + testConfig.testData.get("businessName") + "'}.uuid"));

        testConfig.logStep("Execute Api /v1/bill-pay-submissions/{$bill_uuid}/approve - to create bill with line items and approve");
        saasHelper.createBillWithLineItemAndApprove(CurrencyEnum.SGD);

        testConfig.logStep("Execute Api /v1/accounting/bills/sync - to sync bill to Xero");
        saasHelper.addBillToPendingSyncXero();

        testConfig.logStep("Execute Api /v1/accounting/bills/{$bill_uuid} - to verify bill added to pending sync with line item");
        Response xeroBillDetail = saasHelper.executeRequestAndGetResponse(SaasApiDetails.GetDetailXeroBill, null);
        saasHelper.verifyApiResponse(xeroBillDetail, SaasApiDetails.GetDetailXeroBill, null);
    }
}
