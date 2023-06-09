package Automation.Payments.web.idr;

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

public class TestIdrLocalTransfer extends TestBase
{

    @TestVariables(testrailData = "42:C12735", automatedBy = QA.ThuyNga)
    @Test(description = "Verify Admin can make IDR local transfer success", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testAdminMakeInstantIdrLocalTransferByAvailableRecipient(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        int userDetails = 17, localTransferInfo = 6, counterPartiesInfo = 8;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "CounterPartiesInfo"}, userDetails, localTransferInfo, counterPartiesInfo);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the IDR Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.IDR.name(), TransferType.valueOf(testConfig.testData.get("transferType")));
        testConfig.logStep("Click on 'Make a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(CurrencyEnum.IDR);

        testConfig.logStep("Input Local transfer info & click Next button, then validate instant local transfer info");
        paymentHelper.reviewTransferPage = (ReviewTransferPage) paymentHelper.transferPage.inputInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);

        testConfig.logStep("Click on Confirm button, Input OTP code, then validate Local transfer info on Transfer success overview screen");
        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
    }
}