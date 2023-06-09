package Automation.Payments.api.idr;

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

public class TestIdrFxTransfer extends TestBase
{

    @TestVariables(testrailData = "42:C12693", automatedBy = QA.Van)
    @Test(description = "Verify Checker make Fx transfer success without document - Http 201", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testIdrFxTransferWithoutDocument(Config testConfig)
    {
        int localTransferInfoRowNum = 1;
        int debitFilterRow = 1;
        int userDetailDataRow = 5;
        int counterPartyRow = 1;
        int quoteInfoRow = 1;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo"},
                userDetailDataRow, localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Execute API GET /oauth/token - Get access token and verify token");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/auth - Get business and verify business");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchema);

        testConfig.logStep("Execute API GET /v1/debit-accounts - Get IDR debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.IDR);

        testConfig.logStep("Execute API GET /v1/counterparties - Get recipient and verify recipient data");
        Response getRecipientResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.GetRecipient, null);
        paymentHelper.verifyApiResponse(getRecipientResponse, PaymentApiDetails.GetRecipient, PaymentJsonDetails.GetRecipientSuccessfulResponseTHB);

        testConfig.logStep("Execute API POST /v1/fx/quotes - Create Fx quotes and verify quote data");
        Response createFxQuotesResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateFxQuotesRequestSchema);
        paymentHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateFxQuotesSuccessfulResponse);

        testConfig.logStep("Execute API POST /v1/fx/requirements - Requirement data and verify requirement data");
        Response requirementDataResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataRequestSchema);
        paymentHelper.verifyApiResponse(requirementDataResponse, PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataSuccessfulResponse);

        testConfig.logStep("Execute API POST /v1/fx/requirements?submit=1 - Requirement data and verify submitted requirement data");
        Response requirementDataSubmitResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitRequestSchema);
        paymentHelper.verifyApiResponse(requirementDataSubmitResponse, PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitSuccessfulResponse);

        testConfig.logStep("Execute API POST /v1/fx/transfers - Make Fx transfer and verify transfer info");
        Response makeFxTransferResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.MakeFxTransfer, PaymentJsonDetails.MakeFxTransferRequestSchema);
        paymentHelper.verifyApiResponse(makeFxTransferResponse, PaymentApiDetails.MakeFxTransfer, PaymentJsonDetails.MakeFxTransferSuccessfulResponse);
    }

}
