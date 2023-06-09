package Automation.Payments.web.usd;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

import static Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum.USD;

public class TestMakerCheckerUsdLocalTransfer extends TestBase
{

    String swiftRecipient = "USD swift";

    @TestVariables(testrailData = "43:C13005", automatedBy = QA.Natali)
    @Test(description = "Verify Maker can submit a USD local transfer and delete it from Pending Transaction list", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testMakerSubmitAUsdLocalTransfer(Config testConfig)
    {
        int localTransferInfoRowNum = 4;
        int userDetailDataRow = 11;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailDataRow, localTransferInfoRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Login CF as maker account role");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the USD Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfig.testData.get("transferType")));

        testConfig.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickSubmitATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfig.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelper.transferPage.inputUsdInstantLocalTransferInfo(testConfig.testData, USD);
        paymentHelper.reviewTransferPage = paymentHelper.transferPage.clickNextButton();
        paymentHelper.reviewTransferPage.validateMcaInstantLocalTransferInfo(USD);

        testConfig.logStep("Click on Submit button and then validate Local transfer info on Transfer success overview screen");
        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSubmittedDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButtonFromSubmitTransfer();
        paymentHelper.transferSubmittedDetailPage.validateInstantLocalTransferInfo();

        testConfig.logStep("Go to pending tab of Transactions and delete the transaction created above");
        paymentHelper.transactionsPage = accessHelper.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelper.transactionsPage.goToPendingTab();
        paymentHelper.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelper.transactionsPage.checkPendingTabCheckbox();
        paymentHelper.transactionsPage.deletePendingTransferOnPendingTab();
    }

    @TestVariables(testrailData = "43:C12999", automatedBy = QA.Natali)
    @Test(description = "Verify Finance Transfer can Approve USD Local Instant Transfer from Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void testFinanceTransferUserApproveAPendingUsdLocalTransfer(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 4;
        int userDetailDataRowMaker = 11;
        int userDetailDataRowChecker = 12;

        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailDataRowMaker, localTransferInfoRowNum);
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailDataRowMaker);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailDataRowChecker, localTransferInfoRowNum);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailDataRowChecker);

        testConfigChecker.logStep("Login As maker account role");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();
        paymentHelperMaker.reviewTransferPage.validateMcaInstantLocalTransferInfo(USD);

        testConfigMaker.logStep("Click on Submit button and then validate Local transfer info on Transfer success overview screen");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        paymentHelperMaker.transferSuccessOverviewPage.validateLocalTransferInfo(testConfigMaker.testData);

        testConfigMaker.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelperMaker.transferSubmittedDetailPage = paymentHelperMaker.transferSuccessOverviewPage.clickViewTransferDetailsButtonFromSubmitTransfer();
        paymentHelperMaker.transferSubmittedDetailPage.validateInstantLocalTransferInfo();

        testConfigChecker.logStep("Login As Checker account role");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Go to pending tab of Transactions and delete the transaction created above");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(false);
    }

    @TestVariables(testrailData = "43:C12990", automatedBy = QA.Natali)
    @Test(description = "Verify Admin can bulk approve pending Instant Local USD transfers", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void testAdminBulkApprovePendingLocalInstantTransfers(Config testConfigMaker, Config testConfigAdmin)
    {
        int userDetailsMaker = 11;
        int userDetailsAdmin = 21;
        int transfersToApprove = 2;
        int localTransferInfoRowNum = 4;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperAdmin = new AccessHelper(testConfigAdmin, userDetailsAdmin);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperAdmin = new PaymentHelper(testConfigAdmin, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsAdmin, localTransferInfoRowNum);

        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        accessHelperMaker.dashBoardPage = paymentHelperMaker.transferSuccessOverviewPage.clickGoToHomeButton();

        testConfigMaker.logStep("Select the USD Debit account again");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();
        paymentHelperMaker.reviewTransferPage.validateMcaInstantLocalTransferInfo(USD);

        testConfigMaker.logStep("Click on Submit button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigAdmin.logStep("Login to Customer FrontEnd as Admin");
        accessHelperAdmin.dashBoardPage = (DashBoardPage) accessHelperAdmin.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigAdmin.logStep("Go to pending tab of Transactions and bulk approve the transaction created above");
        paymentHelperAdmin.transactionsPage = accessHelperAdmin.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperAdmin.transactionsPage.goToPendingTab();
        paymentHelperAdmin.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperAdmin.transactionsPage.checkSelectAllCheckBox();
        paymentHelperAdmin.transactionsPage.approveTransferOnPendingTab(false);

        testConfigAdmin.logStep("Validate bulk approve success message display and approved transactions are present in the Past tab");
        paymentHelperAdmin.transactionsPage.validateBulkApproveSuccessMessage();
        paymentHelperAdmin.transactionsPage.goToPastTab();
        paymentHelperAdmin.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperAdmin.transactionsPage.validateApprovedTransactionsPresentInPastTab(transfersToApprove);
    }

    @TestVariables(testrailData = "43:C12991", automatedBy = QA.Natali)
    @Test(description = "Verify Admin can bulk reject pending Instant Local USD transfers", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void testAdminBulkRejectPendingLocalInstantTransfers(Config testConfigMaker, Config testConfigAdmin)
    {
        int userDetailsMaker = 11;
        int userDetailsAdmin = 21;
        int localTransferInfoRowNum = 4;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperAdmin = new AccessHelper(testConfigAdmin, userDetailsAdmin);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperAdmin = new PaymentHelper(testConfigAdmin, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsAdmin, localTransferInfoRowNum);

        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        accessHelperMaker.dashBoardPage = paymentHelperMaker.transferSuccessOverviewPage.clickGoToHomeButton();

        testConfigMaker.logStep("Select the USD Debit account again");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigAdmin.logStep("Login to Customer FrontEnd as Admin");
        accessHelperAdmin.dashBoardPage = (DashBoardPage) accessHelperAdmin.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigAdmin.logStep("Go to pending tab of Transactions and delete the transaction created above");
        paymentHelperAdmin.transactionsPage = accessHelperAdmin.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();

        testConfigAdmin.logStep("Go to pending tab of Transactions and delete the transaction created above");
        paymentHelperAdmin.transactionsPage.goToPendingTab();
        paymentHelperAdmin.transactionsPage.enterTextInSearchBox(testConfigMaker.getRunTimeProperty("referenceValue"));
        paymentHelperAdmin.transactionsPage.checkSelectAllCheckBox();

        testConfigAdmin.logStep("Reject and Validate bulk reject success message display");
        paymentHelperAdmin.transactionsPage.rejectTransferOnPendingTab();
        paymentHelperAdmin.transactionsPage.validateNotFoundMessageOnPendingTab(testConfigMaker.getRunTimeProperty("referenceValue"));

        testConfigAdmin.logStep("Go to Past tab. Search for the transactions created above using reference code and validate");
        paymentHelperAdmin.transactionsPage.goToPastTab();
        paymentHelperAdmin.transactionsPage.enterTextInSearchBox(testConfigMaker.getRunTimeProperty("referenceValue"));
        paymentHelperAdmin.transactionsPage.validateNotFoundMessageOnPastTab(testConfigMaker.getRunTimeProperty("referenceValue"));
    }

    @TestVariables(testrailData = "43:C13012", automatedBy = QA.Natali)
    @Test(description = "Verify FinanceTransfer can bulk approve pending mca transfers submitted", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void testFinanceTransferBulkApprovePendingLocalUsdTransfers(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 4;
        int userDetailsMaker = 11;
        int userDetailsChecker = 12;
        int transfersToApprove = 2;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        accessHelperMaker.dashBoardPage = paymentHelperMaker.transferSuccessOverviewPage.clickGoToHomeButton();

        testConfigMaker.logStep("Select the USD Debit account again");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigChecker.logStep("Login to Customer FrontEnd as Finance Transfer");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Go to Transactions page");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();

        testConfigChecker.logStep("Go to pending tab of Transactions and bulk approve the transaction created above");
        paymentHelperChecker.transactionsPage.goToPendingTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkSelectAllCheckBox();
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(false);

        testConfigChecker.logStep("Validate bulk approve success message display and approved transactions are present in the Past tab");
        paymentHelperChecker.transactionsPage.validateBulkApproveSuccessMessage();
        paymentHelperChecker.transactionsPage.goToPastTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validateApprovedTransactionsPresentInPastTab(transfersToApprove);
    }

    @TestVariables(testrailData = "43:C13000", automatedBy = QA.Natali)
    @Test(description = "Verify Finance Transfer can bulk reject pending Local USD transfers", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void testFinanceTransferBulkRejectPendingLocalTransfers(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 4;
        int userDetailsMaker = 11;
        int userDetailsChecker = 12;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        accessHelperMaker.dashBoardPage = paymentHelperMaker.transferSuccessOverviewPage.clickGoToHomeButton();

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random USD recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigChecker.logStep("Login to Customer FrontEnd as Finance Transfer");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Go to Transactions page");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();

        testConfigChecker.logStep("Go to pending tab of Transactions, search the created transaction  above");
        paymentHelperChecker.transactionsPage.goToPendingTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.getRunTimeProperty("referenceValue"));
        paymentHelperChecker.transactionsPage.checkSelectAllCheckBox();

        testConfigChecker.logStep("Bulk reject transactions in the Past tab and validate not found message displays");
        paymentHelperChecker.transactionsPage.rejectTransferOnPendingTab();
        paymentHelperChecker.transactionsPage.validateNotFoundMessageOnPendingTab(testConfigMaker.getRunTimeProperty("referenceValue"));

        testConfigChecker.logStep("Go to Past tab. Search for the transactions created above using reference code and validate");
        paymentHelperChecker.transactionsPage.goToPastTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.getRunTimeProperty("referenceValue"));
        paymentHelperChecker.transactionsPage.validateNotFoundMessageOnPastTab(testConfigMaker.getRunTimeProperty("referenceValue"));
    }

    @TestVariables(testrailData = "43:C12993", automatedBy = QA.Natali)
    @Test(description = "Verify Admin can reject pending transfer in Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void testAdminRejectAPendingLocalTransfer(Config testConfigMaker, Config testConfigAdmin)
    {
        int localTransferInfoRowNum = 4;
        int userDetailsMaker = 11;
        int userDetailsChecker = 21;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperAdmin = new AccessHelper(testConfigAdmin, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperAdmin = new PaymentHelper(testConfigAdmin, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the USD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(swiftRecipient, USD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.transferPage.inputUsdInstantLocalTransferInfo(testConfigMaker.testData, USD);
        paymentHelperMaker.reviewTransferPage = paymentHelperMaker.transferPage.clickNextButton();

        testConfigMaker.logStep("Click on Submit button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigAdmin.logStep("Login to Customer FrontEnd as Finance Transfer");
        accessHelperAdmin.dashBoardPage = (DashBoardPage) accessHelperAdmin.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigAdmin.logStep("Go to Transactions page");
        paymentHelperAdmin.transactionsPage = accessHelperAdmin.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();

        testConfigAdmin.logStep("Go to pending tab of Transactions and delete the transaction created above");
        paymentHelperAdmin.transactionsPage.goToPendingTab();
        paymentHelperAdmin.transactionsPage.enterTextInSearchBox(testConfigMaker.getRunTimeProperty("referenceValue"));
        paymentHelperAdmin.transactionsPage.checkSelectAllCheckBox();

        testConfigAdmin.logStep("Reject a transaction in the Pending tab");
        paymentHelperAdmin.transactionsPage.rejectTransferOnPendingTab();
        paymentHelperAdmin.transactionsPage.validateNotFoundMessageOnPendingTab(testConfigMaker.getRunTimeProperty("referenceValue"));

        testConfigAdmin.logStep("Go to Past tab. Search for the transactions created above using reference code and validate");
        paymentHelperAdmin.transactionsPage.goToPastTab();
        paymentHelperAdmin.transactionsPage.enterTextInSearchBox(testConfigMaker.getRunTimeProperty("referenceValue"));
        paymentHelperAdmin.transactionsPage.validateNotFoundMessageOnPastTab(testConfigMaker.getRunTimeProperty("referenceValue"));
    }
}