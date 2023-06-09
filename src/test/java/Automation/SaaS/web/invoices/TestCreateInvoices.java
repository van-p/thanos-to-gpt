package Automation.SaaS.web.invoices;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.SubMenuItem;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.SaaS.customer.helpers.SaasEnums.InvoiceState;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.InvoiceDetailPage;
import Automation.SaaS.customer.web.InvoiceSentPage;
import Automation.SaaS.customer.web.InvoicesPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestDataReader;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestCreateInvoices extends TestBase
{

    @TestVariables(testrailData = "11:C10857", automatedBy = QA.NgaVu)
    @Test(description = "To verify Invoice created without uploaded file successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testInvoiceCreatedWithoutUploadedFile(Config testConfig)
    {
        int customerDetailsRowNum = 59;
        int invoiceDetailsRowNum = 4;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "InvoiceDetails"}, customerDetailsRowNum, invoiceDetailsRowNum);

        ArrayList<HashMap> invoiceItems = new ArrayList();
        TestDataReader data = testConfig.getExcelSheet("InvoiceItemDetails");
        invoiceItems.add(data.getTestData(testConfig, 3));

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate Invoices page by clicking on menu");
        saasHelper.invoicesPage = (InvoicesPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Invoices, AfterNavigationPage.InvoicesPage);

        testConfig.logStep("Create Invoice without upload file and verify summary info");
        saasHelper.addNewInvoicePage = saasHelper.invoicesPage.openNewInvoicePage();
        saasHelper.selectInvoiceCustomerPage = saasHelper.addNewInvoicePage.createNewInvoiceWithoutPdf();

        saasHelper.addNewInvoiceItemsPage = saasHelper.selectInvoiceCustomerPage.selectCustomer(testConfig.testData.get("customerEmail"));
        saasHelper.addNewInvoiceItemsPage.addInvoiceItems(invoiceItems);
        saasHelper.addNewInvoiceItemsPage.validateTotalAmountInSummaryInfo();

        testConfig.logStep("Send Invoice without upload file and verify if invoice sent");
        InvoiceSentPage invoiceSentPage = saasHelper.addNewInvoiceItemsPage.sendInvoice();
        invoiceSentPage.verifyInvoiceSent(testConfig.testData.get("customerName"));
    }

    @TestVariables(testrailData = "11:C9562", automatedBy = QA.NgaVu)
    @Test(description = "Verify SGD Invoice is created with uploaded file successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testInvoiceCreatedWithUploadedFile(Config testConfig)
    {
        int customerDetailsRowNum = 59;
        int invoiceDetailsRowNum = 4;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "InvoiceDetails"}, customerDetailsRowNum, invoiceDetailsRowNum);

        ArrayList<HashMap> invoiceItems = new ArrayList();
        TestDataReader data = testConfig.getExcelSheet("InvoiceItemDetails");
        invoiceItems.add(data.getTestData(testConfig, 3));

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate Bill page by clicking on menu");
        saasHelper.invoicesPage = (InvoicesPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Invoices, AfterNavigationPage.InvoicesPage);

        testConfig.logStep("Create Invoice with upload file and verify if invoice sent");
        saasHelper.addNewInvoicePage = saasHelper.invoicesPage.openNewInvoicePage();
        saasHelper.selectInvoiceCustomerPage = saasHelper.addNewInvoicePage.createNewInvoiceWithPdf("Invoice#05.png");
        saasHelper.addNewInvoiceItemsPage = saasHelper.selectInvoiceCustomerPage.selectCustomer(testConfig.testData.get("customerEmail"));
        InvoiceSentPage invoiceSentPage = saasHelper.addNewInvoiceItemsPage.sendInvoiceWithUploadFileAndRequireField(invoiceItems);
        invoiceSentPage.verifyInvoiceSent(testConfig.testData.get("customerName"));
    }

    @TestVariables(testrailData = "11:C9577", automatedBy = QA.NgaVu)
    @Test(description = "Verify invoice is Marked as Paid successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testInvoiceMarkedPaid(Config testConfig)
    {
        int customerDetailsRowNum = 59;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate Bill page by clicking on menu");
        saasHelper.invoicesPage = (InvoicesPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Invoices, AfterNavigationPage.InvoicesPage);

        testConfig.logStep("Create Invoice with upload file and verify if invoice sent");
        saasHelper.invoicesPage.filterState(InvoiceState.Due.getState());
        InvoiceDetailPage invoicesDetailPage = saasHelper.invoicesPage.openFirstInvoiceDetailPage();
        invoicesDetailPage.markInvoicePaid();
        invoicesDetailPage.validateInvoiceMarkedPaid();
    }
}
