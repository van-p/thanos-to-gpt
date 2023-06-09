package Automation.Payments.web.advance;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestMakerCheckerAdvanceTransfer extends TestBase
{

    @TestVariables(testrailData = "44:C13263", automatedBy = QA.Himanshu)
    @Test(description = "Verify Admin can successfully approve local advance SGD transfer from Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminApproveLocalAdvanceSGDTransferFromPendingTab(Config testConfigMaker, Config testConfigChecker)
    {
        int userDetailsMaker = 13;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 2;
        int userDetailsChecker = 8;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo"}, userDetailsMaker, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the Advance Limit section");
        paymentHelperMaker.advanceLimitPage = accessHelperMaker.dashBoardPage.clickAdvanceButtonOnLeftSideNavigation();

        paymentHelperMaker.createUiAdvanceTransfer(testConfigMaker, CurrencyEnum.SGD);

        //Checker steps
        testConfigChecker.logStep("Navigate to Customer FrontEnd, Fill details and do login by checker");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfigChecker.logStep("Search for transaction in pending tab with reference id");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();

        testConfigChecker.logStep("Approve the searched transaction");
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(true);

        testConfigChecker.logStep("Validate approve message");
        paymentHelperChecker.transactionsPage.validateApproveSuccessMessage();

        testConfigChecker.logStep("Check the transaction in past tab");
        paymentHelperChecker.transactionsPage.goToPastTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validateApprovedTransactionsPresentInPastTab(1);
        paymentHelperChecker.transactionsPage.validateApprovedTransactionAmountInPastTab(paymentHelperMaker.testConfig.testData.get("amount"));
    }

    @TestVariables(testrailData = "44:C13264", automatedBy = QA.Trung)
    @Test(description = "Verify Admin can successfully reject local advance SGD transfer from Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminRejectLocalAdvanceSGDTransferFromPendingTab(Config testConfigMaker, Config testConfigChecker)
    {
        int userDetailsMaker = 15;
        int userDetailsChecker = 14;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 2;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo"}, userDetailsMaker, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the Advance Limit section");
        paymentHelperMaker.advanceLimitPage = accessHelperMaker.dashBoardPage.clickAdvanceButtonOnLeftSideNavigation();

        paymentHelperMaker.createUiAdvanceTransfer(testConfigMaker, CurrencyEnum.SGD);

        //Checker steps
        testConfigChecker.logStep("Navigate to Customer FrontEnd, Fill details and do login by checker");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfigChecker.logStep("Search for transaction in pending tab with reference id");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();

        testConfigChecker.logStep("Reject the searched transaction");
        paymentHelperChecker.transactionsPage.rejectTransferOnPendingTab();

        testConfigChecker.logStep("Validate approve message");
        paymentHelperChecker.transactionsPage.validateNotFoundMessageOnPendingTab(testConfigMaker.testData.get("reference"));
    }

    @TestVariables(testrailData = "44:C13267", automatedBy = QA.Himanshu)
    @Test(description = "Verify Admin can successfully approve local advance IDR transfer from Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminApproveLocalAdvanceIDRTransferFromPendingTab(Config testConfigMaker, Config testConfigChecker)
    {
        int userDetailsMaker = 27;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 9;
        int userDetailsChecker = 26;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo"}, userDetailsMaker, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the Advance Limit section");
        paymentHelperMaker.advanceLimitPage = accessHelperMaker.dashBoardPage.clickAdvanceButtonOnLeftSideNavigation();

        paymentHelperMaker.createUiAdvanceTransfer(testConfigMaker, CurrencyEnum.IDR);

        //Checker steps
        testConfigChecker.logStep("Navigate to Customer FrontEnd, Fill details and do login by checker");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfigChecker.logStep("Search for transaction in pending tab with reference id");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();

        testConfigChecker.logStep("Approve the searched transaction");
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(true);

        testConfigChecker.logStep("Validate approve message");
        paymentHelperChecker.transactionsPage.validateApproveSuccessMessage();

        testConfigChecker.logStep("Check the transaction in past tab");
        paymentHelperChecker.transactionsPage.goToPastTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validateApprovedTransactionsPresentInPastTab(1);
        paymentHelperChecker.transactionsPage.validateApprovedTransactionAmountInPastTab(paymentHelperMaker.testConfig.testData.get("amount"));
    }

    @TestVariables(testrailData = "44:C13268", automatedBy = QA.Himanshu)
    @Test(description = "Verify Admin can successfully reject local advance IDR transfer from Pending tab", dataProvider = "GetTwoTestConfig", groups = {"regression", "uiCases"})
    public void adminRejectLocalAdvanceIDRTransferFromPendingTab(Config testConfigMaker, Config testConfigChecker)
    {
        int userDetailsMaker = 29;
        int userDetailsChecker = 28;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 9;
        AccessHelper accessHelperMaker = new AccessHelper(testConfigMaker, userDetailsMaker);
        AccessHelper accessHelperChecker = new AccessHelper(testConfigChecker, userDetailsChecker);
        PaymentHelper paymentHelperMaker = new PaymentHelper(testConfigMaker, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo"}, userDetailsMaker, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfigChecker, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailsChecker, localTransferInfoRowNum);

        //Maker steps
        testConfigMaker.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelperMaker.dashBoardPage = (DashBoardPage) accessHelperMaker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigMaker.logStep("Select the Advance Limit section");
        paymentHelperMaker.advanceLimitPage = accessHelperMaker.dashBoardPage.clickAdvanceButtonOnLeftSideNavigation();

        paymentHelperMaker.createUiAdvanceTransfer(testConfigMaker, CurrencyEnum.IDR);

        //Checker steps
        testConfigChecker.logStep("Navigate to Customer FrontEnd, Fill details and do login by checker");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfigChecker.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfigChecker.logStep("Search for transaction in pending tab with reference id");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfigMaker.testData.get("reference"));
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();

        testConfigChecker.logStep("Reject the searched transaction");
        paymentHelperChecker.transactionsPage.rejectTransferOnPendingTab();

        testConfigChecker.logStep("Validate approve message");
        paymentHelperChecker.transactionsPage.validateNotFoundMessageOnPendingTab(testConfigMaker.testData.get("reference"));
    }
}