package Automation.Payments.api.advance;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.api.DashAccessJsonDetails;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Payments.customer.api.PaymentApiDetails;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.dash.api.DashPaymentApiDetails;
import Automation.Payments.dash.api.DashPaymentJsonDetails;
import Automation.Payments.dash.helpers.DashPaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestAdvanceLocalTransfer extends TestBase
{

    @TestVariables(testrailData = "44:C13261", automatedBy = QA.Himanshu)
    @Test(description = "Verify Advance Local Transfer Flow for SGD Advance Only Business", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testAdvanceLocalTransfer(Config testConfig)
    {
        int userDetailDataRow = 8;
        int chargeAccountDetails = 1;
        int transferInfoRowNum = 3;
        int counterPartyRow = 3;
        int advanceTransferAdditionalInfoRowNum = 1;

        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "ChargeAccountFilter", "CounterPartiesInfo", "LocalTransferInfo", "AdvanceTransferAdditionalInfo"},
                userDetailDataRow, chargeAccountDetails, counterPartyRow, transferInfoRowNum, advanceTransferAdditionalInfoRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Execute API GET /oauth/token - Get access token and verify token");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/auth - Get business and verify business");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchema);

        testConfig.logStep("Execute API GET /v1/debit-accounts?types[]=current&types[]=charge - Get charge account");
        Response getChargeAccountResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetChargeAccount, null);
        paymentHelper.verifyApiResponse(getChargeAccountResponse, PaymentApiDetails.GetChargeAccount, PaymentJsonDetails.GetChargeAccountSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/counterparties - Get recipient and verify recipient data");
        Response getRecipientResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetRecipient, null);
        paymentHelper.verifyApiResponse(getRecipientResponse, PaymentApiDetails.GetRecipient, PaymentJsonDetails.GetAdvanceRecipientSuccessfulResponseSGD);

        testConfig.logStep("Execute API POST /v1/debit-accounts/transfer-outside - Make advance transfer and verify transfer info");
        Response advanceTransferResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.MakeLocalTransfer, PaymentJsonDetails.MakeAdvanceTransferRequestSchema);
        paymentHelper.verifyApiResponse(advanceTransferResponse, PaymentApiDetails.MakeLocalTransfer, PaymentJsonDetails.MakeLocalTransferSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/debit-transactions/{$transactionUuid} - Verify check Transaction detail");
        Response getTransactionDetailResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetTransactionDetail, null);
        paymentHelper.verifyApiResponse(getTransactionDetailResponse, PaymentApiDetails.GetTransactionDetail, PaymentJsonDetails.GetQuarantinedTransactionResponse);

    }

    @TestVariables(testrailData = "44:C13045,C13046,C13047,C13119", automatedBy = QA.Himanshu)
    @Test(description = "Verify Quarantined Transaction approval flow for Advance Local Transfer Flow for SGD Advance Only Business", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testApproveQuarantineTransfer(Config testConfig)
    {
        testAdvanceLocalTransfer(testConfig);
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        DashPaymentHelper dashPaymentHelper = new DashPaymentHelper(testConfig, 1);

        testConfig.logStep("Execute API GET /dashboard/auth/login-password - Get Dash Login Session");
        Response response = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostAuthLogin, DashAccessJsonDetails.LoginRequestSchema);
        dashAccessHelper.verifyApiResponse(response, DashAccessApiDetails.PostAuthLogin, DashAccessJsonDetails.AuthSuccessfulResponse);

        testConfig.logStep("Execute API GET /oauth/token - Get Dash access token and verify token");
        Response authResponse = dashAccessHelper.sendRequestAndGetResponse(DashAccessApiDetails.PostOauthToken, DashAccessJsonDetails.OauthTokenRequestSchema);
        dashAccessHelper.verifyApiResponse(authResponse, DashAccessApiDetails.PostOauthToken, DashAccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Execute API GET /dashboard/quarantined-transactions - Get Quarantined Transaction List from dash and check current Transaction");
        Response quarantinedTransactionList = dashPaymentHelper.sendRequestAndGetResponse(DashPaymentApiDetails.GetQuarantinedTransactionList, null);
        dashPaymentHelper.verifyApiResponse(quarantinedTransactionList, DashPaymentApiDetails.GetQuarantinedTransactionList, DashPaymentJsonDetails.GetQuarantinedTransactionListResponse);

        testConfig.logStep("Execute API GET /dashboard/quarantined-transactions/{$quarantinedUuid} - Get Quarantined Transaction Detail from dash");
        Response quarantinedTransactionDetail = dashPaymentHelper.sendRequestAndGetResponse(DashPaymentApiDetails.GetQuarantinedTransactionDetailFromDash, null);
        dashPaymentHelper.verifyApiResponse(quarantinedTransactionDetail, DashPaymentApiDetails.GetQuarantinedTransactionDetailFromDash, DashPaymentJsonDetails.GetQuarantinedTransactionDetailResponse);

        testConfig.logStep("Execute API PUT /dashboard/quarantined-transactions/{$quarantinedUuid}/approve - Approve Quarantined Transaction from dash");
        Response quarantinedTransactionApproved = dashPaymentHelper.sendRequestAndGetResponse(DashPaymentApiDetails.ApproveQuarantinedTransaction, DashPaymentJsonDetails.ApproveQuarantinedTransactionRequestSchema);
        dashPaymentHelper.verifyApiResponse(quarantinedTransactionApproved, DashPaymentApiDetails.ApproveQuarantinedTransaction, null);

        testConfig.logStep("Execute API GET /dashboard/quarantined-transactions/{$quarantinedUuid} - Get Quarantined Transaction state and check it should be approved");
        dashPaymentHelper.verifyApprovedQuarantinedTransaction();

        testConfig.logStep("Execute API GET /dashboard/debit-transactions/{$transactionUuid} - Verify check Transaction detail in dash");
        Response getTransactionDetailResponse = dashPaymentHelper.sendRequestAndGetResponse(DashPaymentApiDetails.GetDashTransactionDetail, null);
        dashPaymentHelper.verifyApiResponse(getTransactionDetailResponse, DashPaymentApiDetails.GetDashTransactionDetail, DashPaymentJsonDetails.GetApprovedQuarantinedTransactionResponse);
        //todo - check Transfer complete status on CF once transaction approved. Blocked right now on stage because Queues
        // are not working.

    }

}
