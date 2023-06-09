package Automation.Payments.web.idr;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestIdrLocalRecipient extends TestBase
{

    @TestVariables(testrailData = "42:C12697", automatedBy = QA.ThuyNga)
    @Test(description = "Verify Admin can create, search, delete new IDR recipient from IDR debit account", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testCreateSearchDeleteNewIdrLocalTransfer(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        int userDetails = 17, localTransferInfo = 6, counterPartiesInfo = 7;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "CounterPartiesInfo"}, userDetails, localTransferInfo, counterPartiesInfo);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the IDR Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.IDR.name(), TransferType.valueOf(testConfig.testData.get("transferType")));
        testConfig.logStep("Click on 'Make a transfer' CTA button");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        testConfig.logStep("Click on 'New Recipient' CTA button and Input valid bank code and account number and click on 'Verify button");
        paymentHelper.newRecipientPage = paymentHelper.recipientsPage.clickAddNewRecipient();
        paymentHelper.newRecipientPage.inputBankAccountNumber();
        paymentHelper.newRecipientPage.selectBank();

        testConfig.logStep("Verify bank information displays correct");
        paymentHelper.newRecipientPage.validateBankDisplayCorrect();
        paymentHelper.newRecipientPage.validateAccountHolderNameDisplayWithDefaultValue();

        testConfig.logStep("Change account holder name and click on Continue button");
        paymentHelper.newRecipientPage.inputAccountHolderName();
        paymentHelper.transferPage = paymentHelper.newRecipientPage.clickOnContinueButton();

        testConfig.logStep("Verify account holder name displays on transfer and go back");
        paymentHelper.transferPage.validateBankInfoCorrect();
        paymentHelper.recipientsPage = paymentHelper.transferPage.clickBackButton();

        testConfig.logStep("Search this recipient and verify bank information correct");
        paymentHelper.recipientsPage.searchRecipientUsingName(testConfig.testData.get("recipientName"));
        paymentHelper.recipientsPage.validateBankInfoCorrectOnRecipientList();

        testConfig.logStep("Delete this recipient and verify this recipient is deleted");
        paymentHelper.recipientsPage.deleteRecipient();
        paymentHelper.recipientsPage.validateRecipientIsDeleted(testConfig.testData.get("recipientName"));
    }
}