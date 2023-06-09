package Automation.Payments.web.sgd;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.customer.web.ReviewTransferPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

import static Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum.SGD;

public class TestMakerCheckerFlow_LocalTransfer extends TestBase
{

    @TestVariables(testrailData = "41:C12503", automatedBy = QA.Akankshi)
    @Test(description = "Maker submits sgd local instant transfer and delete it from pending tab", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void makerSubmitLocalInstantTransferAndDeleteFromPendingTab(Config testConfig)
    {
        int localTransferInfoRowNum = 2;
        int userDetailDataRow = 7;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailDataRow, localTransferInfoRowNum);

        testConfig.logStep("Login to Customer FrontEnd as Finance Submit, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the SGD Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfig.testData.get("transferType")));

        testConfig.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickSubmitATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(SGD);

        testConfig.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelper.reviewTransferPage = (ReviewTransferPage) paymentHelper.transferPage.inputInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);

        testConfig.logStep("Click on Submit button and then validate Local transfer info on Transfer success overview screen");
        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);

        testConfig.logStep("Go to pending tab of Transactions and delete the transaction created above");
        paymentHelper.transactionsPage = accessHelper.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelper.transactionsPage.goToPendingTab();
        paymentHelper.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelper.transactionsPage.checkPendingTabCheckbox();
        paymentHelper.transactionsPage.deletePendingTransferOnPendingTab();
    }

    @TestVariables(testrailData = "41:C12494", automatedBy = QA.Akankshi)
    @Test(description = "Verify Admin can successfully approve local instant transfer from Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminApproveLocalInstantTransferFromPendingTab(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 2;
        int userDetailsMaker = 7;
        int userDetailsChecker = 6;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the SGD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(SGD);

        testConfigMaker.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelperMaker.reviewTransferPage = (ReviewTransferPage) paymentHelperMaker.transferPage.inputInstantLocalTransferInfo(false);
        paymentHelperMaker.reviewTransferPage.validateInstantLocalTransferInfo(false);

        testConfigMaker.logStep("Click on Submit button and then validate Local transfer info on Transfer success overview screen");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        paymentHelperMaker.transferSuccessOverviewPage.validateLocalTransferInfo(testConfigMaker.testData);

        //Checker steps
        testConfigChecker.logStep("Login to Customer FrontEnd as Admin, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(false);
        paymentHelperChecker.transactionsPage.validateApproveSuccessMessage();
    }

    @TestVariables(testrailData = "41:C12495", automatedBy = QA.Akankshi)
    @Test(description = "Verify Admin can bulk Approve pending local instant transfers", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminBulkApprovePendingLocalInstantTransfers(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 2;
        int userDetailsMaker = 7;
        int userDetailsChecker = 6;
        int transfersToApprove = 2;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the SGD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(SGD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.reviewTransferPage = (ReviewTransferPage) paymentHelperMaker.transferPage.inputInstantLocalTransferInfo(false);

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        accessHelperMaker.dashBoardPage = paymentHelperMaker.transferSuccessOverviewPage.clickGoToHomeButton();

        testConfigMaker.logStep("Select the SGD Debit account again");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(SGD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.reviewTransferPage = (ReviewTransferPage) paymentHelperMaker.transferPage.inputInstantLocalTransferInfo(false);

        testConfigMaker.logStep("Click on Submit button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        //Checker steps
        testConfigChecker.logStep("Login to Customer FrontEnd as Admin, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation, click on Pending tab, search for the transactions created above using reference code and then approve the transfers");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
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

    @TestVariables(testrailData = "41:C12512", automatedBy = QA.Akankshi)
    @Test(description = "Verify Finance Transfer can bulk Approve pending local instant transfers", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void financeTransferBulkApprovePendingLocalInstantTransfers(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 2;
        int userDetailsMaker = 7;
        int userDetailsChecker = 9;
        int transfersToApprove = 2;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the SGD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(SGD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.reviewTransferPage = (ReviewTransferPage) paymentHelperMaker.transferPage.inputInstantLocalTransferInfo(false);

        testConfigMaker.logStep("Click on Submit button and then click on Go to Home button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        accessHelperMaker.dashBoardPage = paymentHelperMaker.transferSuccessOverviewPage.clickGoToHomeButton();

        testConfigMaker.logStep("Select the SGD Debit account again");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(SGD);

        testConfigMaker.logStep("Input Local transfer info & click Next button");
        paymentHelperMaker.reviewTransferPage = (ReviewTransferPage) paymentHelperMaker.transferPage.inputInstantLocalTransferInfo(false);

        testConfigMaker.logStep("Click on Submit button");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        //Checker steps
        testConfigChecker.logStep("Login to Customer FrontEnd as Finance Transfer, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation, click on Pending tab, search for the transactions created above using reference code and then approve the transfers");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
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

    @TestVariables(testrailData = "41:C13465", automatedBy = QA.Akankshi)
    @Test(description = "Verify Admin can successfully modify local instant pending transfer", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminModifyLocalInstantTransferFromPendingTab(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNumMaker = 2;
        int localTransferInfoRowNumChecker = 3;
        int userDetailsMaker = 7;
        int userDetailsChecker = 6;
        String amount = "333";
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsMaker, localTransferInfoRowNumMaker);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNumChecker);

        //Maker steps
        testConfigMaker.logStep("Login to Customer FrontEnd as Finance Submit, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the SGD Debit account");
        paymentHelperMaker.debitDetailPage = accessHelperMaker.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfigMaker.testData.get("transferType")));

        testConfigMaker.logStep("Click on 'Submit a transfer' CTA button and select a random recipient in the list");
        paymentHelperMaker.recipientsPage = paymentHelperMaker.debitDetailPage.clickSubmitATransferButton();
        paymentHelperMaker.transferPage = paymentHelperMaker.recipientsPage.selectARecipient(SGD);

        testConfigMaker.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelperMaker.reviewTransferPage = (ReviewTransferPage) paymentHelperMaker.transferPage.inputInstantLocalTransferInfo(false);
        paymentHelperMaker.reviewTransferPage.validateInstantLocalTransferInfo(false);

        testConfigMaker.logStep("Click on Submit button and then validate Local transfer info on Transfer success overview screen");
        paymentHelperMaker.transferSuccessOverviewPage = paymentHelperMaker.reviewTransferPage.submitTransfer(TransferType.valueOf(testConfigMaker.testData.get("transferType")));
        paymentHelperMaker.transferSuccessOverviewPage.validateLocalTransferInfo(testConfigMaker.testData);

        //Checker steps
        testConfigChecker.putRunTimeProperty("recipientCurrency", SGD.name());
        testConfigChecker.logStep("Login to Customer Frontend as Admin, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation and then click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfigChecker.logStep("Search for the transaction created above, click on it and then open Modify screen from Transfer Details side navigation screen");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.clickPendingTransfer(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.clickMoreButtonOnTransferDetailsScreen();
        paymentHelperChecker.modifyPendingTransferPage = paymentHelperChecker.transactionsPage.clickModifyButtonOnTransferDetailsScreen();

        testConfigChecker.logStep("Modify transfer amount and then confirm transfer");
        paymentHelperChecker.modifyPendingTransferPage.modifyAmount(amount);
        paymentHelperChecker.reviewTransferPage = paymentHelperChecker.modifyPendingTransferPage.clickNextButton();
        paymentHelperChecker.reviewTransferPage.validateModifiedAmount(amount);
        paymentHelperChecker.transferSuccessOverviewPage = paymentHelperChecker.reviewTransferPage.confirmTransfer(testConfigChecker.testData.get("otp"), TransferType.valueOf(testConfigChecker.testData.get("transferType")));

        testConfigChecker.logStep("validate amount is successfully modified and transfer is initiated");
        paymentHelperChecker.transferSuccessOverviewPage.validateModifiedLocalTransferInfo(amount, testConfigChecker.testData);
    }
}