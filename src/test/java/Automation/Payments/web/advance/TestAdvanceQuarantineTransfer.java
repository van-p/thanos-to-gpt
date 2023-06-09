package Automation.Payments.web.advance;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestAdvanceQuarantineTransfer extends TestBase
{
    @TestVariables(testrailData = "44:C13260,C13262", automatedBy = QA.Himanshu)
    @Test(description = "Verify that Admin with SGD Advance account only can see Transfer Failed status", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void failedSGDTransactionAfterQuarantineReject(Config testConfig)
    {
        int userDetails = 25;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 3;
        int chargeAccountDetails = 2;
        int counterPartyRow = 11;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetails);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo", "ChargeAccountFilter", "CounterPartiesInfo"},
                userDetails, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum, chargeAccountDetails, counterPartyRow);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the Advance Limit Section");
        paymentHelper.advanceLimitPage = accessHelper.dashBoardPage.clickAdvanceButtonOnLeftSideNavigation();

        paymentHelper.createUiAdvanceTransfer(testConfig, PaymentEnums.CurrencyEnum.SGD);

        paymentHelper.rejectQuarantinedTransaction(testConfig, accessHelper);

        testConfig.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelper.transactionsPage = accessHelper.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelper.transactionsPage.goToPastTab();
        paymentHelper.transactionsPage.verifyFailedTransactionOnPastTabList(testConfig.testData.get("reference"));
        paymentHelper.transactionsPage.verifyRollbackTransactionOnPastTabList("Rollback of transaction");
    }

    @TestVariables(testrailData = "44:C13277,C13279", automatedBy = QA.Himanshu)
    @Test(description = "Verify that Admin with IDR Advance account only can see Transfer Failed status", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void failedIDRTransactionAfterQuarantineReject(Config testConfig)
    {
        int userDetails = 26;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 10;
        int chargeAccountDetails = 3;
        int counterPartyRow = 12;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetails);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo", "ChargeAccountFilter", "CounterPartiesInfo"},
                userDetails, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum, chargeAccountDetails, counterPartyRow);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the Advance Limit Section");
        paymentHelper.advanceLimitPage = accessHelper.dashBoardPage.clickAdvanceButtonOnLeftSideNavigation();

        paymentHelper.createUiAdvanceTransfer(testConfig, PaymentEnums.CurrencyEnum.IDR);

        paymentHelper.rejectQuarantinedTransaction(testConfig, accessHelper);

        testConfig.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelper.transactionsPage = accessHelper.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelper.transactionsPage.goToPastTab();
        paymentHelper.transactionsPage.verifyFailedTransactionOnPastTabList(testConfig.testData.get("reference"));
        paymentHelper.transactionsPage.verifyRollbackTransactionOnPastTabList("Rollback of transaction");
    }
}
