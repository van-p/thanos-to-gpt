package Automation.SaaS.web.bookkeeping;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.api.PaymentApiDetails;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.XeroPage;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestExpenseSync extends TestBase
{
    @TestVariables(testrailData = "41:C12528", automatedBy = QA.Van)
    @Test(description = "Verify Admin can Sync transaction to Xero", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void adminMoveExpenseToReview(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 61);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "ExpenseSync"}, 61, 1);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo"}, 1, 1, 1);

        testConfig.logStep("Precondition: Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        testConfig.logStep("Precondition: Execute API GET /v1/debit-accounts - Admin get SGD debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulCurrenciesResponse, PaymentEnums.CurrencyEnum.SGD);
        testConfig.logStep("Precondition: Execute API GET /v1/counterparties - Admin get recipient and verify recipient data");
        Response getRecipientResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetRecipient, null);
        paymentHelper.verifyApiResponse(getRecipientResponse, PaymentApiDetails.GetRecipient, PaymentJsonDetails.GetRecipientSuccessfulCurrenciesResponse);
        testConfig.logStep("Precondition: Execute API POST /v1/debit-accounts/transfer-outside - Initiate sgd local instant transfer and verify transfer info");
        Response localTransferResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.MakeLocalTransfer, PaymentJsonDetails.MakeLocalTransferWithBudgetRequestSchema);
        paymentHelper.verifyApiResponse(localTransferResponse, PaymentApiDetails.MakeLocalTransfer, PaymentJsonDetails.MakeLocalTransferSuccessfulResponse);

        testConfig.logStep("Login to Customer FrontEnd as Admin, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Go to Xero page, open Expense Sync.");
        saasHelper.xeroPage = (XeroPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Xero, AccessEnums.AfterNavigationPage.XeroPage);
        saasHelper.syncExpenseWithXeroPage = saasHelper.xeroPage.openSyncExpenseWithXero();
        testConfig.logStep("Update transaction info. Able to update");
        saasHelper.expenseDetailsPage = saasHelper.syncExpenseWithXeroPage.openTransaction();
        testConfig.putRunTimeProperty("updatedMerchantName", testConfig.testData.get("merchantName") + DataGenerator.generateRandomAlphaNumericString(4));
        saasHelper.syncExpenseWithXeroPage = saasHelper.expenseDetailsPage.updateAndCloseExpenseDetailsPage().verifySyncExpensesWithXeroPage();

        testConfig.logStep("Add to pending sync. Able to add to pending sync. Validate merchant name. merchant name displayed in list");
        saasHelper.syncExpenseWithXeroPage.moveExpenseToPendingSyncAndVerify(testConfig.getRunTimeProperty("updatedMerchantName"), true);
    }

}
