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

public class TestInvoiceApi extends TestBase
{

    @TestVariables(testrailData = "11:C9375", automatedBy = QA.Van)
    @Test(description = "To verify that admin can create, get, update and delete invoice counterparty", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testInvoiceCounterParty(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails"}, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 55);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to get invoice counterparty and verify invoice counterparty data get successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetInvoiceCounterParty, SaasJsonDetails.GetInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to update invoice counterparty and verify invoice counterparty data updated successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdateInvoiceCounterParty, SaasJsonDetails.UpdateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.UpdateInvoiceCounterParty, SaasJsonDetails.UpdateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

    @TestVariables(testrailData = "11:C9378", automatedBy = QA.Van)
    @Test(description = "To verify that finance transfer only user can create, get, update and delete invoice counterparty", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testInvoiceCounterPartyWithFinanceTransferOnly(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails"}, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 42);
        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to get invoice counterparty and verify invoice counterparty data get successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetInvoiceCounterParty, SaasJsonDetails.GetInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to update invoice counterparty and verify invoice counterparty data updated successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdateInvoiceCounterParty, SaasJsonDetails.UpdateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.UpdateInvoiceCounterParty, SaasJsonDetails.UpdateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

    @TestVariables(testrailData = "11:C9392", automatedBy = QA.Van)
    @Test(description = "To verify that admin can create invoice", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminCanCreateInvoice(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails", "InvoiceDetails"}, 3, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 43);
        testConfig.logStep("Pre-Condition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Pre-Condition: Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoices to create invoice and verify invoice data created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceResponse);

        testConfig.logStep("Post-Condition: Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

    @TestVariables(testrailData = "11:C9361", automatedBy = QA.Van)
    @Test(description = "Verify user preview invoice successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testUserCanPreviewInvoice(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails", "InvoiceDetails"}, 3, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 43);
        testConfig.logStep("Pre-Condition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Pre-Condition: Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoices to create invoice and verify invoice data created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceResponse);

        testConfig.logStep("Execute Api v1/invoices/{uuid}/payment/preview to preview invoice and verify preview data");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.PreviewInvoice, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.PreviewInvoice, SaasJsonDetails.PreviewInvoiceResponse);

        testConfig.logStep("Post-Condition: Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

    @TestVariables(testrailData = "11:C9382", automatedBy = QA.Van)
    @Test(description = "Get list of invoices with Admin successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminCanGetInvoiceList(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails", "InvoiceDetails"}, 4, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 44);
        testConfig.logStep("Pre-Condition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Pre-Condition: Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoices to create invoice and verify invoice data created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceResponse);

        testConfig.logStep("Execute Api /v1/invoices to get invoice list and verify invoice data");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetInvoiceList, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetInvoiceList, SaasJsonDetails.GetInvoiceListResponse);

        testConfig.logStep("Post-Condition: Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

    @TestVariables(testrailData = "11:C10000", automatedBy = QA.Van)
    @Test(description = "To verify that admin can Update by adding invoice item", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminCanUpdateInvoiceAddingInvoiceItem(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails", "InvoiceDetails", "InvoiceItemDetails"}, 3, 1, 2);
        AccessHelper accessHelper = new AccessHelper(testConfig, 43);
        testConfig.logStep("Pre-Condition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Pre-Condition: Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoices to create invoice and verify invoice data created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceResponse);

        testConfig.logStep("Execute Api /v1/invoice-items to create invoice item and verify invoice item created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceItem, SaasJsonDetails.CreateInvoiceItemRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceItem, SaasJsonDetails.CreateInvoiceItemResponse);

        testConfig.logStep("Post-Condition: Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

    @TestVariables(testrailData = "11:C10001", automatedBy = QA.Van)
    @Test(description = "To verify that admin can delete invoice", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminCanDeleteInvoice(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails", "InvoiceDetails", "InvoiceItemDetails"}, 3, 1, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 43);
        testConfig.logStep("Pre-Condition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Pre-Condition: Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoices to create invoice and verify invoice data created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceResponse);

        testConfig.logStep("Execute Api /v1/invoice-items to create invoice item and verify invoice item created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceItem, SaasJsonDetails.CreateInvoiceItemRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceItem, SaasJsonDetails.CreateInvoiceItemResponse);

        testConfig.logStep("Execute Api /v1/invoices/{$invoiceUuid}/submit to submit invoice and verify invoice data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoice, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoice, null);
    }

    @TestVariables(testrailData = "11:C9409", automatedBy = QA.Van)
    @Test(description = "To verify that admin can get invoice insights", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminGetInvoiceInsight(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"InvoiceCounterPartyDetails", "InvoiceDetails", "InvoiceItemDetails"}, 3, 1, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, 43);
        testConfig.logStep("Pre-Condition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Pre-Condition: Execute Api /v1/invoice-counterparties to create invoice counterparty and verify invoice counterparty data created successfully");
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceCounterParty, SaasJsonDetails.CreateInvoiceCounterPartyResponse);

        testConfig.logStep("Execute Api /v1/invoices to create invoice and verify invoice data created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoice, SaasJsonDetails.CreateInvoiceResponse);

        testConfig.logStep("Execute Api /v1/invoice-items to create invoice item and verify invoice item created successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateInvoiceItem, SaasJsonDetails.CreateInvoiceItemRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.CreateInvoiceItem, SaasJsonDetails.CreateInvoiceItemResponse);

        testConfig.logStep("Execute Api /v1/invoice-insights?source_uuid=99172997-05e4-4169-8a4a-d9f19bf52d38&source_type=business to get invoice insight and verify insight data");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetInvoiceMiniInsight, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.GetInvoiceMiniInsight, SaasJsonDetails.GetInvoiceMiniInsightResponse);

        testConfig.logStep("Post-Condition: Execute Api /v1/invoice-counterparties to delete invoice counterparty and verify invoice counterparty data deleted successfully");
        response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeleteInvoiceCounterParty, null);
        saasHelper.verifyApiResponse(response, SaasApiDetails.DeleteInvoiceCounterParty, null);
    }

}
