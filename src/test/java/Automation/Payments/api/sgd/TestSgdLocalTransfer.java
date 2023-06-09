package Automation.Payments.api.sgd;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentApiDetails;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestSgdLocalTransfer extends TestBase
{

    @TestVariables(testrailData = "41:C13971", automatedBy = QA.Akankshi)
    @Test(description = "Verify Admin can initiate sgd local instant transfer", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testSgdLocalInstantTransfer(Config testConfig)
    {
        int localTransferInfoRowNum = 1;
        int debitFilterRow = 3;
        int detailDataRow = 10;
        int counterPartyRow = 4;

        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow);
        AccessHelper accessHelper = new AccessHelper(testConfig, detailDataRow);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/debit-accounts - Admin get SGD debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute API GET /v1/counterparties - Admin get recipient and verify recipient data");
        Response getRecipientResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetRecipient, null);
        paymentHelper.verifyApiResponse(getRecipientResponse, PaymentApiDetails.GetRecipient, PaymentJsonDetails.GetRecipientSuccessfulResponseSGD);

        testConfig.logStep("Execute API POST /v1/debit-accounts/transfer-outside - Initiate sgd local instant transfer and verify transfer info");
        Response localTransferResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.MakeLocalTransfer, PaymentJsonDetails.MakeLocalTransferWithBudgetRequestSchema);
        paymentHelper.verifyApiResponse(localTransferResponse, PaymentApiDetails.MakeLocalTransfer, PaymentJsonDetails.MakeLocalTransferSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/debit-transactions/{$transactionUuid} - Verify Transaction detail");
        Response getTransactionDetailResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetTransactionDetail, null);
        paymentHelper.verifyApiResponse(getTransactionDetailResponse, PaymentApiDetails.GetTransactionDetail, PaymentJsonDetails.GetSgdTransactionDetailResponse);
    }
}