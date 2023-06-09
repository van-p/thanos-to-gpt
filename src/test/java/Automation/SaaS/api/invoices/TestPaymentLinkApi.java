package Automation.SaaS.api.invoices;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestPaymentLinkApi extends TestBase
{

    @TestVariables(testrailData = "11:C9370", automatedBy = QA.Van)
    @Test(description = "To verify that admin can create and delete payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void createAndDeletePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 3);
        AccessHelper accessHelper = new AccessHelper(testConfig, 19);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links to delete payment link and verify payment link deleted successfully");
        Response deletePaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeletePaymentLink, null);
        saasHelper.verifyApiResponse(deletePaymentLinkResponse, SaasApiDetails.DeletePaymentLink, null);
    }

    @TestVariables(testrailData = "11:C10108", automatedBy = QA.Van)
    @Test(description = "To verify that admin can get payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void getPaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails", "PaymentLinkGetDetails"}, 3, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 24);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to get payment link and verify payment link data returned");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPaymentLink, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetPaymentLink, SaasJsonDetails.GetPaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9639", automatedBy = QA.Van)
    @Test(description = "Verify admin can create payment links notifiers successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void adminCreatePaymentLinkNotifier(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails", "PaymentLinkNotifierMessage", "PaymentLinkGetDetails"}, 3, 1, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 24);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to get payment link and verify payment link data returned");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPaymentLink, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetPaymentLink, SaasJsonDetails.GetPaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links/notifiers to create payment link notifier and verify payment link notifier created successfully");
        Response createPaymentLinkNotifierResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkNotifierResponse, SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierResponse);
    }

    @TestVariables(testrailData = "11:C9648", automatedBy = QA.Van)
    @Test(description = "To verify that finance transfer can create payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeTransferCreatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 3);
        AccessHelper accessHelper = new AccessHelper(testConfig, 29);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9645", automatedBy = QA.Van)
    @Test(description = "To verify that finance submit can create payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeSubmitCreatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 3);
        AccessHelper accessHelper = new AccessHelper(testConfig, 30);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9650", automatedBy = QA.Van)
    @Test(description = "To verify that finance no transfer no submit can create payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeNoTransferNoSubmitCreatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 3);
        AccessHelper accessHelper = new AccessHelper(testConfig, 28);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9644", automatedBy = QA.Van)
    @Test(description = "Verify finance no transfer no submit can create payment links notifiers successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeNoTransferNoSubmitCreatePaymentLinkNotifier(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails", "PaymentLinkNotifierMessage", "PaymentLinkGetDetails"}, 3, 2, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 31);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to get payment link and verify payment link data returned");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPaymentLink, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetPaymentLink, SaasJsonDetails.GetPaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links/notifiers to create payment link notifier and verify payment link notifier created successfully");
        Response createPaymentLinkNotifierResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkNotifierResponse, SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierResponse);
    }

    @TestVariables(testrailData = "11:C9646", automatedBy = QA.Van)
    @Test(description = "Verify finance submit can create payment links notifiers successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeSubmitCreatePaymentLinkNotifier(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails", "PaymentLinkNotifierMessage", "PaymentLinkGetDetails"}, 3, 1, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 32);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to get payment link and verify payment link data returned");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPaymentLink, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetPaymentLink, SaasJsonDetails.GetPaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links/notifiers to create payment link notifier and verify payment link notifier created successfully");
        Response createPaymentLinkNotifierResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkNotifierResponse, SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierResponse);
    }

    @TestVariables(testrailData = "11:C9649", automatedBy = QA.Van)
    @Test(description = "Verify finance transfer can create payment links notifiers successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeTransferCreatePaymentLinkNotifier(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails", "PaymentLinkNotifierMessage", "PaymentLinkGetDetails"}, 3, 3, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 33);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup infon for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to get payment link and verify payment link data returned");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPaymentLink, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetPaymentLink, SaasJsonDetails.GetPaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links/notifiers to create payment link notifier and verify payment link notifier created successfully");
        Response createPaymentLinkNotifierResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkNotifierResponse, SaasApiDetails.CreatePaymentLinkNotifier, SaasJsonDetails.CreatePaymentLinkNotifierResponse);
    }

    @TestVariables(testrailData = "11:C9651", automatedBy = QA.Van)
    @Test(description = "To verify that admin can update payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void adminUpdatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 3);
        AccessHelper accessHelper = new AccessHelper(testConfig, 19);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links to update payment link and verify payment link updated successfully");
        Response updatePaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(updatePaymentLinkResponse, SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9657", automatedBy = QA.Van)
    @Test(description = "To verify that finance transfer can update payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeTransferUpdatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 6);
        AccessHelper accessHelper = new AccessHelper(testConfig, 27);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links to update payment link and verify payment link updated successfully");
        Response updatePaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(updatePaymentLinkResponse, SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9656", automatedBy = QA.Van)
    @Test(description = "To verify that finance submit can update payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeSubmitUpdatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 5);
        AccessHelper accessHelper = new AccessHelper(testConfig, 26);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links to update payment link and verify payment link updated successfully");
        Response updatePaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(updatePaymentLinkResponse, SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C9658", automatedBy = QA.Van)
    @Test(description = "To verify that finance no transfer no submit can update payment link", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void financeNoTransferNoSubmitUpdatePaymentLink(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 4);
        AccessHelper accessHelper = new AccessHelper(testConfig, 25);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to create payment link and verify payment link created successfully");
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponse);

        testConfig.logStep("Execute Api /v1/payment-links to update payment link and verify payment link updated successfully");
        Response updatePaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(updatePaymentLinkResponse, SaasApiDetails.UpdatePaymentLink, SaasJsonDetails.UpdatePaymentLinkResponse);
    }

    @TestVariables(testrailData = "11:C13985", automatedBy = QA.Van)
    @Test(description = "Verify Finance Transfer User successfully get list of payment links ", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void getPaymentLinkList(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails", "PaymentLinkGetDetails"}, 3, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 33);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/payment-links to get payment link and verify payment link data returned");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPaymentLink, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetPaymentLink, SaasJsonDetails.GetPaymentLinkResponse);
    }
}
