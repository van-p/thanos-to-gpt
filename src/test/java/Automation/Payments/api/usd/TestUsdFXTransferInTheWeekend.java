package Automation.Payments.api.usd;

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

public class TestUsdFXTransferInTheWeekend extends TestBase
{

    @TestVariables(testrailData = "43:C12894", automatedBy = QA.Truyen)
    @Test(description = "Verify Admin can not create USD FX in the weekend for SG account ", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testAdminSGCannotMakeTransferUsdFxOnWeekend(Config testConfig)
    {
        int detailDataRow = 16;
        int debitFilterRow = 4;
        int counterPartyRow = 6;
        int quoteRow = 3;
        AccessHelper accessHelper = new AccessHelper(testConfig, detailDataRow);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"QuoteInfo", "DebitAccountFilter", "CounterPartiesInfo"}, quoteRow, debitFilterRow, counterPartyRow);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/debit-accounts - Admin get USD debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.USD);

        testConfig.logStep("Set CounterpartiesUuid to prepare for quote request");
        testConfig.putRunTimeProperty("counterPartyUuid", testConfig.testData.get("recipientUuid"));

        testConfig.logStep("Execute API POST /v1/fx/quotes - Create Fx quotes and verify the error response");
        Response createFxQuotesResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuotesRequestSchema);
        paymentHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuoteOnWeekendResponse);
    }

    @TestVariables(testrailData = "43:C12896", automatedBy = QA.Truyen)
    @Test(description = "Verify Finance Transfer Only can not create USD FX in the weekend for SG account ", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testFinanceTransferSGCannotMakeTransferUsdFxOnWeekend(Config testConfig)
    {
        int detailDataRow = 18;
        int debitFilterRow = 4;
        int counterPartyRow = 6;
        int quoteRow = 3;

        AccessHelper accessHelper = new AccessHelper(testConfig, detailDataRow);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"QuoteInfo", "DebitAccountFilter", "CounterPartiesInfo"}, quoteRow, debitFilterRow, counterPartyRow);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginNonAdminRoleResponse, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/debit-accounts - Admin get USD debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.USD);

        testConfig.logStep("Set CounterpartiesUuid to prepare for quote request");
        testConfig.putRunTimeProperty("counterPartyUuid", testConfig.testData.get("recipientUuid"));

        testConfig.logStep("Execute API POST /v1/fx/quotes - Create Fx quotes and verify the error response");
        Response createFxQuotesResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuotesRequestSchema);
        paymentHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuoteOnWeekendResponse);
    }

    @TestVariables(testrailData = "43:C12893", automatedBy = QA.Truyen)
    @Test(description = "Verify Admin can not create USD FX in the weekend for NonSG account ", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testAdminNonSGCannotMakeTransferUsdFxOnWeekend(Config testConfig)
    {
        int detailDataRow = 19;
        int debitFilterRow = 5;
        int counterPartyRow = 9;
        int quoteRow = 3;
        AccessHelper accessHelper = new AccessHelper(testConfig, detailDataRow);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"QuoteInfo", "DebitAccountFilter", "CounterPartiesInfo"}, quoteRow, debitFilterRow, counterPartyRow);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/debit-accounts - Admin get USD debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.USD);

        testConfig.logStep("Set CounterpartiesUuid to prepare for quote request");
        testConfig.putRunTimeProperty("counterPartyUuid", testConfig.testData.get("recipientUuid"));

        testConfig.logStep("Execute API POST /v1/fx/quotes - Create Fx quotes and verify the error response");
        Response createFxQuotesResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuotesRequestSchema);
        paymentHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuoteOnWeekendResponse);
    }

    @TestVariables(testrailData = "43:C12895", automatedBy = QA.Truyen)
    @Test(description = "Verify Finance Transfer Only can not create USD FX in the weekend for NonSG account ", dataProvider = "getTestConfig", groups = {"regression", "apiCases"})
    public void testFinanceTransferNonSGCannotMakeTransferUsdFxOnWeekend(Config testConfig)
    {
        int detailDataRow = 20;
        int debitFilterRow = 5;
        int counterPartyRow = 9;
        int quoteRow = 3;

        AccessHelper accessHelper = new AccessHelper(testConfig, detailDataRow);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"QuoteInfo", "DebitAccountFilter", "CounterPartiesInfo"}, quoteRow, debitFilterRow, counterPartyRow);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginNonAdminRoleResponse, BusinessType.Business);

        testConfig.logStep("Execute API GET /v1/debit-accounts - Admin get USD debit account and verify account list");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDebitAccountSuccessfulResponse, PaymentEnums.CurrencyEnum.USD);

        testConfig.logStep("Set CounterpartiesUuid to prepare for quote request");
        testConfig.putRunTimeProperty("counterPartyUuid", testConfig.testData.get("recipientUuid"));

        testConfig.logStep("Execute API POST /v1/fx/quotes - Create Fx quotes and verify the error response");
        Response createFxQuotesResponse = paymentHelper.sendRequestAndGetResponse(PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuotesRequestSchema);
        paymentHelper.verifyApiResponse(createFxQuotesResponse, PaymentApiDetails.CreateFxQuotes, PaymentJsonDetails.CreateUsdFxQuoteOnWeekendResponse);
    }

}
