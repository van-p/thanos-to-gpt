package Automation.SaaS.web.bookkeeping;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.ExportDataPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestExportData extends TestBase
{

    @TestVariables(testrailData = "12:C10146", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Account statements ", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllAccountStatementData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to ExportData page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Account statements module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.AccountStatements);
        testConfig.logStep("Select Account source of funds filter as SGD");
        saasHelper.exportDataPage.selectAccountSourceOfFund(SaasEnums.SourceOfFund.SgdDebitAccount);
        testConfig.logStep("Select Date range filter as All");
        saasHelper.exportDataPage.selectDateRange(SaasEnums.ExportDateRange.All);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.AccountStatements);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.AccountStatements);
        testConfig.logStep("Select export format as Default and file type as CSV");
        saasHelper.exportDataPage.selectExportFormat(SaasEnums.ExportFormat.Default);
        saasHelper.exportDataPage.selectExportFileType(SaasEnums.FileType.CSV);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.AccountStatements);
    }

    @TestVariables(testrailData = "12:C10147", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Money Out", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllMoneyOutData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Export Data page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Money Out module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.MoneyOut);
        testConfig.logStep("Select Account source of funds filter as SGD");
        saasHelper.exportDataPage.selectAccountSourceOfFund(SaasEnums.SourceOfFund.SgdDebitAccount);
        testConfig.logStep("Select Date range filter as All");
        saasHelper.exportDataPage.selectDateRange(SaasEnums.ExportDateRange.All);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.MoneyOut);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.MoneyOut);
        testConfig.logStep("Select export format as Default");
        saasHelper.exportDataPage.selectExportFormat(SaasEnums.ExportFormat.Default);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.MoneyOut);
    }

    @TestVariables(testrailData = "12:C10148", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Bill", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllBillData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Export Data page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Bill module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.Bills);
        testConfig.logStep("Select Date range filter as All");
        saasHelper.exportDataPage.selectDueDateRange(SaasEnums.ExportDateRange.All);
        testConfig.logStep("Select all Bill status");
        saasHelper.exportDataPage.selectBillStatusFilter(SaasEnums.BillStatus.All);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.Bills);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.Bills);
        testConfig.logStep("Select export format as Default");
        saasHelper.exportDataPage.selectExportFormat(SaasEnums.ExportFormat.Default);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.Bills);
    }

    @TestVariables(testrailData = "12:C10149", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Claim", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllClaimData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Export Data page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Claim module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.Claims);
        testConfig.logStep("Select all Claim status");
        saasHelper.exportDataPage.selectClaimStatusFilter(SaasEnums.ClaimStatus.All);
        testConfig.logStep("Select Date range filter as All");
        saasHelper.exportDataPage.selectDateRange(SaasEnums.ExportDateRange.All);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.Claims);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.Claims);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.Claims);
    }

    @TestVariables(testrailData = "12:C10150", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllBudgetData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Export Data page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Budget module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.Budgets);
        testConfig.logStep("Select Account source of funds filter as SGD and remove Expiring Soon filter");
        saasHelper.exportDataPage.filterExpiringSoonBudgets(false);
        saasHelper.exportDataPage.selectAccountSourceOfFund(SaasEnums.SourceOfFund.SgdDebitAccount);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.Budgets);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.Budgets);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.Budgets);
    }

    @TestVariables(testrailData = "12:C10151", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Card", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllCardData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Export Data page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Cards module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.Cards);
        testConfig.logStep("Select Account source of funds filter as SGD");
        saasHelper.exportDataPage.selectAccountSourceOfFund(SaasEnums.SourceOfFund.SgdDebitAccount);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.Cards);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.Cards);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.Cards);
    }

    @TestVariables(testrailData = "12:C10152", automatedBy = QA.Van)
    @Test(description = "To verify Admin user is able to export for all data Invoices", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminCanExportAllInvoicesData(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 60);
        SaasHelper saasHelper = new SaasHelper(testConfig, 60);
        testConfig.logStep("Login to CF as admin user and navigate to Dashboard page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Export Data page");
        saasHelper.exportDataPage = (ExportDataPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.ExportData, AccessEnums.AfterNavigationPage.ExportDataPage);
        testConfig.logStep("Select Invoices module");
        saasHelper.exportDataPage.selectModule(SaasEnums.ExportModule.Invoices);
        testConfig.logStep("Preview the transactions list and validate preview modal");
        saasHelper.previewRecordPage = saasHelper.exportDataPage.previewRecord(SaasEnums.ExportModule.Invoices);
        saasHelper.exportDataPage = saasHelper.previewRecordPage.validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule.Invoices);
        testConfig.logStep("Export and validate exported file downloaded successfully.");
        saasHelper.exportDataPage.exportDataAndValidateExportedFile(SaasEnums.FileType.CSV, SaasEnums.ExportModule.Invoices);
    }
}
