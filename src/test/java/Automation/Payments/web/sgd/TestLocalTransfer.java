package Automation.Payments.web.sgd;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.customer.web.ReviewTransferPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestLocalTransfer extends TestBase
{

    @TestVariables(testrailData = "41:C12517", automatedBy = QA.Van)
    @Test(description = "Verify Admin can successfully do sgd local instant transfer", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testInstantTransfer(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo"}, 3, 1);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the SGD Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.SGD.name(), TransferType.valueOf(testConfig.testData.get("transferType")));
        testConfig.logStep("Click on 'Make a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(CurrencyEnum.SGD);

        testConfig.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelper.reviewTransferPage = (ReviewTransferPage) paymentHelper.transferPage.inputInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);

        testConfig.logStep("Click on Confirm button, Input OTP code, then validate Local transfer info on Transfer success overview screen");
        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
    }

    @TestVariables(testrailData = "41:C12486", automatedBy = QA.Akankshi)
    @Test(description = "Verify Admin can successfully add and delete sgd local bank recipient", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testAdminCanAddDeleteSgdLocalBankRecipient(Config testConfig)
    {
        int localTransferInfoRowNum = 1;
        int counterPartiesInfoRowNum = 5;
        int userDetailsRowNum = 3;
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "CounterPartiesInfo"}, userDetailsRowNum, localTransferInfoRowNum, counterPartiesInfoRowNum);

        testConfig.logStep("Login to Customer Frontend as Admin, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the SGD Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.SGD.name(), TransferType.valueOf(testConfig.testData.get("transferType")));

        testConfig.logStep("Click on 'Make a transfer' CTA button and then click on Add New Recipient");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        paymentHelper.newRecipientPage = paymentHelper.recipientsPage.clickAddNewRecipient();

        testConfig.logStep("Add SGD Local Bank details and click on Continue button");
        paymentHelper.transferPage = paymentHelper.newRecipientPage.inputSgdLocalBankInfo();

        testConfig.logStep("On the Amount Input screen displayed, click Back button to go back to recipient List screen");
        paymentHelper.recipientsPage = paymentHelper.transferPage.clickBackButton();

        testConfig.logStep("Search for the recipient created above and delete it");
        paymentHelper.recipientsPage.searchRecipientUsingName(testConfig.testData.get("recipientName"));
        paymentHelper.recipientsPage.deleteRecipient();

        testConfig.logStep("validate recipient is successfully deleted");
        paymentHelper.recipientsPage.validateRecipientIsDeleted(testConfig.testData.get("recipientName"));
    }
}