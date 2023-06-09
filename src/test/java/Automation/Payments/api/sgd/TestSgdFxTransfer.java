package Automation.Payments.api.sgd;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
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

public class TestSgdFxTransfer extends TestBase
{

    @TestVariables(testrailData = "41:C12553", automatedBy = QA.Van)
    @Test(description = "Verify Payments - Local (CB) - SGD Account- Checker Approve Pending FX transfer", dataProvider = "GetTwoTestConfig", groups = {"regression", "apiCases"})
    public void testSgdFxTransferWithoutDocument(Config testConfigMaker, Config testConfigChecker)
    {
        int localTransferInfoRowNum = 1;
        int debitFilterRow = 2;
        int makerDetailDataRow = 1;
        int checkerDetailDataRow = 2;
        int counterPartyRow = 2;
        int quoteInfoRow = 2;
        int pendingActionRow = 1;

        PaymentHelper paymentMakerHelper = new PaymentHelper(testConfigMaker, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo", "PendingActions"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow, pendingActionRow);
        PaymentHelper paymentCheckerHelper = new PaymentHelper(testConfigChecker, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo", "PendingActions"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow, pendingActionRow);
        AccessHelper accessMakerHelper = new AccessHelper(testConfigMaker, makerDetailDataRow);
        AccessHelper accessCheckerHelper = new AccessHelper(testConfigChecker, checkerDetailDataRow);

        testConfigMaker.logStep("Execute API GET /oauth/token - Maker get access token and verify token");
        Response response = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessMakerHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfigMaker.logStep("Execute API GET /v1/auth - Maker get business and verify business");
        Response authResponse = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessMakerHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchemaEasyRequirement);

        testConfigMaker.logStep("Execute API GET /v1/debit-accounts - Maker get SGD debit account and verify account list");
        paymentMakerHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfigMaker.logStep("Execute API GET /v1/counterparties - Maker get recipient and verify recipient data");
        Response getRecipientResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.GetRecipient, null);
        paymentMakerHelper.verifyApiResponse(getRecipientResponse, PaymentApiDetails.GetRecipient, PaymentJsonDetails.GetRecipientSuccessfulResponse);

        testConfigMaker.logStep("Execute API POST /v1/fx/quotes - Maker create Fx quotes and verify quote data");
        Response createFxQuotesResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateSgdFxQuotesRequestSchema);
        paymentMakerHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateFxQuotesSuccessfulResponse);

        testConfigMaker.logStep("Execute API POST /v1/fx/requirements - Maker create requirement data and verify requirement data");
        Response requirementDataResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataRequestSchema);
        paymentMakerHelper.verifyApiResponse(requirementDataResponse, PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataSuccessfulTransferWiseResponse);

        testConfigMaker.logStep("Execute API POST /v1/fx/requirements?submit=1 - Maker submit requirement data and verify submitted requirement data");
        Response requirementDataSubmitResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitRequestSchema);
        paymentMakerHelper.verifyApiResponse(requirementDataSubmitResponse, PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitTransferWiseResponse);

        testConfigMaker.logStep("Execute API POST /v1/pending-actions - Maker clean pending actions if there are any, submit a Local FX transfer and verify pending actions");
        paymentMakerHelper.cleanPendingActions(PaymentJsonDetails.EmptyResponse);
        Response makeFxTransferResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.CreatePendingActions, PaymentJsonDetails.CreatePendingActionLocalFxRequestSchema);
        paymentMakerHelper.verifyApiResponse(makeFxTransferResponse, PaymentApiDetails.CreatePendingActions, PaymentJsonDetails.CreatePendingActionLocalFxSuccessfulResponse);

        testConfigMaker.logStep("Execute API GET /v1/pending-actions - Maker get pending actions and verify pending actions");
        Response getPendingActionsResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.GetPendingActions, null);
        paymentMakerHelper.verifyApiResponse(getPendingActionsResponse, PaymentApiDetails.GetPendingActions, PaymentJsonDetails.GetPendingActionsSuccessfulResponse);
        testConfigChecker.putRunTimeProperty("pendingActionUuid", testConfigMaker.getRunTimeProperty("pendingActionUuid"));

        testConfigChecker.logStep("Execute API GET /oauth/token - Checker get access token and verify token");
        Response checkerResponse = accessCheckerHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessCheckerHelper.verifyApiResponse(checkerResponse, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfigChecker.logStep("Execute API GET /v1/auth - Checker get business and verify business");
        Response checkerAuthResponse = accessCheckerHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessCheckerHelper.verifyApiResponse(checkerAuthResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchemaEasyRequirement);

        testConfigChecker.logStep("Execute API POST /v1/pending-actions - Checker approve pending transfer an verify approved transfer");
        Response approvePendingTransferResponse = paymentCheckerHelper.sendRequestAndGetResponse(PaymentApiDetails.ApprovePendingAction, null);
        paymentCheckerHelper.verifyApiResponse(approvePendingTransferResponse, PaymentApiDetails.ApprovePendingAction, PaymentJsonDetails.ApprovePendingActionLocalFxSuccessfulResponse);
    }

    @TestVariables(testrailData = "41:C14425", automatedBy = QA.Akankshi)
    @Test(description = "Verify Finance Submit user can successfully submit sgd FX transfer", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testMakerSubmitSgdFxTransfer(Config testConfigMaker)
    {
        int localTransferInfoRowNum = 1;
        int debitFilterRow = 2;
        int makerDetailDataRow = 1;
        int counterPartyRow = 2;
        int quoteInfoRow = 2;
        int pendingActionRow = 1;

        PaymentHelper paymentMakerHelper = new PaymentHelper(testConfigMaker, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo", "PendingActions"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow, pendingActionRow);
        AccessHelper accessMakerHelper = new AccessHelper(testConfigMaker, makerDetailDataRow);

        testConfigMaker.logStep("Execute API GET /oauth/token - Maker get access token and verify token");
        Response response = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessMakerHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfigMaker.logStep("Execute API GET /v1/auth - Maker get business and verify business");
        Response authResponse = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessMakerHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchemaEasyRequirement);

        testConfigMaker.logStep("Execute API GET /v1/debit-accounts - Maker get SGD debit account and verify account list");
        paymentMakerHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfigMaker.logStep("Execute API GET /v1/counterparties - Maker get recipient and verify recipient data");
        Response getRecipientResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.GetRecipient, null);
        paymentMakerHelper.verifyApiResponse(getRecipientResponse, PaymentApiDetails.GetRecipient, PaymentJsonDetails.GetRecipientSuccessfulResponse);

        testConfigMaker.logStep("Execute API POST /v1/fx/quotes - Maker create Fx quotes and verify quote data");
        Response createFxQuotesResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateSgdFxQuotesRequestSchema);
        paymentMakerHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateFxQuotesSuccessfulResponse);

        testConfigMaker.logStep("Execute API POST /v1/fx/requirements - Maker create requirement data and verify requirement data");
        Response requirementDataResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataRequestSchema);
        paymentMakerHelper.verifyApiResponse(requirementDataResponse, PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataSuccessfulTransferWiseResponse);

        testConfigMaker.logStep("Execute API POST /v1/fx/requirements?submit=1 - Maker submit requirement data and verify submitted requirement data");
        Response requirementDataSubmitResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitRequestSchema);
        paymentMakerHelper.verifyApiResponse(requirementDataSubmitResponse, PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitTransferWiseResponse);

        testConfigMaker.logStep("Execute API POST /v1/pending-actions - Maker clean pending actions if there are any, submit a Local FX transfer and verify pending actions");
        paymentMakerHelper.cleanPendingActions(PaymentJsonDetails.EmptyResponse);
        Response makeFxTransferResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.CreatePendingActions, PaymentJsonDetails.CreatePendingActionLocalFxRequestSchema);
        paymentMakerHelper.verifyApiResponse(makeFxTransferResponse, PaymentApiDetails.CreatePendingActions, PaymentJsonDetails.CreatePendingActionLocalFxSuccessfulResponse);

        testConfigMaker.logStep("Execute API GET /v1/pending-actions - Maker get pending actions and verify pending actions");
        Response getPendingActionsResponse = paymentMakerHelper.sendRequestAndGetResponse(PaymentApiDetails.GetPendingActions, null);
        paymentMakerHelper.verifyApiResponse(getPendingActionsResponse, PaymentApiDetails.GetPendingActions, PaymentJsonDetails.GetPendingActionsSuccessfulResponse);
    }
}
