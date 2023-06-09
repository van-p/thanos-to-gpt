package Automation.SaaS.api.budget;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentApiDetails;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestMakeTransferWithBudget extends TestBase
{

    @TestVariables(testrailData = "30:C5071", automatedBy = QA.Mukesh)
    @Test(description = "To verify that admin can make IDR transfer for budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testMakeIdrTransferForBudget(Config testConfig)
    {
        int userDetailDataRow = 34;
        int counterPartyRow = 1;

        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "CounterPartiesInfo"}, userDetailDataRow, counterPartyRow);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Execute Api POST /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.IDR);

        testConfig.logStep("Execute API GET /v1/budgets with valid info and get budget_uuid");
        Response getBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetBudgetsWithStatus, null);
        saasHelper.verifyApiResponse(getBudgetResponse, SaasApiDetails.GetBudgetsWithStatus, SaasJsonDetails.BudgetsResponse);

        testConfig.logStep("Execute API GET /v1/counterparties with valid info and get counterparty_uuid, recipient_account_name, recipient_account_number and recipient_bank_code");
        paymentHelper.getCounterpartyInfoByCurrencyCode(PaymentEnums.CurrencyEnum.IDR);

        testConfig.logStep("Execute API POST /v1/auth/token with valid info and get x_auth_session");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);

        testConfig.logStep("Execute API POST /oauth/token with valid x_auth_session and scope = debit-transaction.store to get x-example-additional-token");
        Response oauthTokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenRequestSchema);
        accessHelper.verifyApiResponse(oauthTokenResponse, AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenSuccessResponse);

        testConfig.logStep("Execute API POST /v1/debit-accounts/transfer-outside - Make local transfer with budget and verify transfer info");
        Response localTransferResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.MakeLocalTransferWithOtp, PaymentJsonDetails.MakeLocalTransferWithBudgetRequestSchema);
        paymentHelper.verifyApiResponse(localTransferResponse, PaymentApiDetails.MakeLocalTransferWithOtp, PaymentJsonDetails.MakeLocalTransferWithBudgetSuccessfulResponse);

        testConfig.logStep("Execute API GET /v1/debit-transactions with budget_uuid in previous step and verify that data is accurate");
        paymentHelper.checkIfTransactionExistsWithRetry(5);
    }

    @TestVariables(testrailData = "30:C5111", automatedBy = QA.Mukesh)
    @Test(description = "To verify that admin can make FX transfer for budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testMakeFxTransferForBudget(Config testConfig)
    {
        int userDetailDataRow = 35;
        int counterPartyRow = 2;

        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "CounterPartiesInfo"}, userDetailDataRow, counterPartyRow);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get SGD debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute API /v1/counterparties with recipient currency code and get counterparty_uuid, recipient_account_name, recipient_account_number and recipient_bank_code");
        paymentHelper.getCounterpartyInfoByCurrencyCode(PaymentEnums.CurrencyEnum.CAD);

        testConfig.logStep("Execute API /v1/budgets with valid info and get SGD budget_uuid");
        Response getBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetBudgetsWithStatus, null);
        saasHelper.verifyApiResponse(getBudgetResponse, SaasApiDetails.GetBudgetsWithStatus, SaasJsonDetails.BudgetsResponse);

        testConfig.logStep("Execute API POST /v1/fx/quotes - Create Fx quotes and verify quote data");
        paymentHelper.createAndVerifyFxQuoteData(PaymentEnums.CurrencyEnum.SGD, PaymentEnums.CurrencyEnum.CAD, PaymentEnums.ExternalService.TransferWise);

        testConfig.logStep("Execute API POST /v1/fx/requirements - Requirement data and verify requirement data");
        Response requirementResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementsWithoutFxQuoteIdRequestSchema);
        paymentHelper.verifyApiResponse(requirementResponse, PaymentApiDetails.RequirementData, PaymentJsonDetails.RequirementDataSuccessfulTransferWiseResponse);

        testConfig.logStep("Execute API POST /v1/fx/requirements?submit=1 - Requirement data and verify submitted requirement data");
        Response requirementSubmitResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementsWithoutFxQuoteIdRequestSchema);
        paymentHelper.verifyApiResponse(requirementSubmitResponse, PaymentApiDetails.RequirementDataSubmit, PaymentJsonDetails.RequirementDataSubmitTransferWiseResponse);

        testConfig.logStep("Execute API POST /v1/fx/transfers - Make Fx transfer with SGD budget_uuid and verify transfer info");
        Response response = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.MakeFxTransfer, PaymentJsonDetails.MakeFxTransferWithBudgetRequestSchema);
        paymentHelper.verifyApiResponse(response, PaymentApiDetails.MakeFxTransfer, PaymentJsonDetails.MakeFxTransferWithoutFeeTypeSuccessResponse);

        testConfig.logStep("Execute API GET /v1/debit-transactions with budget_uuid in previous step and verify that data is accurate");
        paymentHelper.checkIfTransactionExistsWithRetry(5);
    }
}
