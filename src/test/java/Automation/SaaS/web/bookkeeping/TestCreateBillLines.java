package Automation.SaaS.web.bookkeeping;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.SubMenuItem;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.web.AccountsListPage;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.BillPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestDataReader;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestCreateBillLines extends TestBase
{

    @TestVariables(testrailData = "12:C9871", automatedBy = QA.NgaVu)
    @Test(description = "To verify the bill line items saved successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testCreateBillLineItemWithoutOcr(Config testConfig)
    {
        int customerDetailsRowNum = 22;
        int invoiceAmount = 50;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);

        ArrayList<HashMap> lineItems = new ArrayList();
        TestDataReader data = testConfig.getExcelSheet("LineItemDetails");
        lineItems.add(data.getTestData(testConfig, 1));
        lineItems.add(data.getTestData(testConfig, 2));

        testConfig.logStep("Login to CF as admin user");
        accessHelper.accountsListPage = (AccountsListPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.AccountsListPage);

        testConfig.logStep("Choose the business you would like to manage");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.accountsListPage.clickToChooseBusiness(testConfig.testData.get("businessName"), AfterNavigationPage.DashBoardPage);

        testConfig.logStep("Navigate Bill page by clicking on menu");
        saasHelper.billPage = (BillPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Bills, AfterNavigationPage.BillPage);

        testConfig.logStep("Create Bill with matched total line items and verify if total items shown correctly");
        saasHelper.addNewBillPage = saasHelper.billPage.openAddNewBillPageSkipUploadFile();
        saasHelper.addNewBillPage.addLineItem(lineItems, invoiceAmount);
        saasHelper.addNewBillPage.validateTotalLineItem(lineItems.size());
        saasHelper.addNewBillPage.validateLineItemInfo(lineItems);
    }

    @TestVariables(testrailData = "12:C9878", automatedBy = QA.NgaVu)
    @Test(description = "To verify show error message if total line items not matched invoice amount", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testCreateLineNotMatchedInvoiceAmount(Config testConfig)
    {
        int customerDetailsRowNum = 22;
        int invoiceAmount = 50;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);

        ArrayList<HashMap> lineItems = new ArrayList();
        TestDataReader data = testConfig.getExcelSheet("LineItemDetails");
        lineItems.add(data.getTestData(testConfig, 3));
        lineItems.add(data.getTestData(testConfig, 4));

        testConfig.logStep("Login to CF as admin user");
        accessHelper.accountsListPage = (AccountsListPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.AccountsListPage);

        testConfig.logStep("Choose the business you would like to manage");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.accountsListPage.clickToChooseBusiness(testConfig.testData.get("businessName"), AfterNavigationPage.DashBoardPage);

        testConfig.logStep("Navigate Bill page by clicking on menu");
        saasHelper.billPage = (BillPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Bills, AfterNavigationPage.BillPage);

        testConfig.logStep("Create Bill with not matched total line items and verify error message shown correctly");
        saasHelper.addNewBillPage = saasHelper.billPage.openAddNewBillPageSkipUploadFile();
        saasHelper.addNewBillPage.addLineItem(lineItems, invoiceAmount);
        saasHelper.addNewBillPage.validateLineItemErrorMessage();
    }

    @TestVariables(testrailData = "12:C9894", automatedBy = QA.Alex)
    @Test(description = "To verify the bill line item updated successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testUpdateBillLineItems(Config testConfig)
    {
        int customerDetailsRowNum = 22;
        int invoiceAmount = 50;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);

        TestDataReader data = testConfig.getExcelSheet("LineItemDetails");
        ArrayList<HashMap> lineItems = new ArrayList();
        lineItems.add(data.getTestData(testConfig, 1));
        lineItems.add(data.getTestData(testConfig, 2));

        ArrayList<HashMap> lineItemsEdit = new ArrayList();
        lineItemsEdit.add(data.getTestData(testConfig, 1));
        lineItemsEdit.add(data.getTestData(testConfig, 2));

        testConfig.logStep("Login to CF as admin user");
        accessHelper.accountsListPage = (AccountsListPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.AccountsListPage);

        testConfig.logStep("Choose the business you would like to manage");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.accountsListPage.clickToChooseBusiness(testConfig.testData.get("businessName"), AfterNavigationPage.DashBoardPage);

        testConfig.logStep("Navigate Bill page by clicking on menu");
        saasHelper.billPage = (BillPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Bills, AfterNavigationPage.BillPage);

        testConfig.logStep("Edit Bill with matched total line items and verify if items saved correctly");
        saasHelper.addNewBillPage = saasHelper.billPage.openAddNewBillPageSkipUploadFile();
        saasHelper.addNewBillPage.addLineItem(lineItems, invoiceAmount);
        saasHelper.addNewBillPage.editLineItems(lineItemsEdit, invoiceAmount);
        saasHelper.addNewBillPage.validateLineItemInfo(lineItemsEdit);
    }

    @TestVariables(testrailData = "12:C9899", automatedBy = QA.NgaVu)
    @Test(description = "To verify that Ocr can be captured values", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testOcrCapturedBillLineItemValues(Config testConfig)
    {
        int customerDetailsRowNum = 22;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);

        ArrayList<HashMap> lineItems = new ArrayList();
        TestDataReader data = testConfig.getExcelSheet("LineItemDetails");
        lineItems.add(data.getTestData(testConfig, 5));

        testConfig.logStep("Login to CF as admin user");
        accessHelper.accountsListPage = (AccountsListPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.AccountsListPage);

        testConfig.logStep("Choose the business you would like to manage");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.accountsListPage.clickToChooseBusiness(testConfig.testData.get("businessName"), AfterNavigationPage.DashBoardPage);

        testConfig.logStep("Navigate Bill page by clicking on menu");
        saasHelper.billPage = (BillPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Bills, AfterNavigationPage.BillPage);

        testConfig.logStep("Upload Bill line items and verify that ocr can be captured values");
        saasHelper.addNewBillPage = saasHelper.billPage.openAddNewBillPageAndUploadFile("Invoice#05.png");
        saasHelper.addNewBillPage.validateLineItemInfo(lineItems);
        saasHelper.addNewBillPage.validateLineItemMessage(SaasHelper.saasStaticDataBase.getBillLineItemOcrMessage());
    }
}
