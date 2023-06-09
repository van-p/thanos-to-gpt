package Automation.SaaS.dash.helpers;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasEnums.CardTransactionType;
import Automation.SaaS.dash.api.DashSaasApiDetails;
import Automation.SaaS.dash.api.DashSaasJsonDetails;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.Objects;

public class DashSaasHelper extends ApiHelper
{
    public static DashSaasStaticDataBase dashSaasStaticDataBase;

    public DashSaasHelper(Config testConfig)
    {
        super(testConfig);
        initialiseStaticData();
    }

    public DashSaasHelper(Config testConfig, int... respectiveSheetRowNumbers)
    {
        super(testConfig, new String[]{"DashUserDetails"}, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public DashSaasHelper(Config testConfig, String[] sheets, int... respectiveSheetRowNumbers)
    {
        super(testConfig, sheets, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public void initialiseStaticData()
    {
        if (Config.appLanguage != null)
        {
            AccessEnums.CustomerPortalLanguage.valueOf(Config.appLanguage);
            dashSaasStaticDataBase = new DashSaasStaticDataEn();
        } else
        {
            dashSaasStaticDataBase = new DashSaasStaticDataEn();
        }
    }

    public Response sendRequestAndGetResponse(DashSaasApiDetails dashSaasApiDetails, DashSaasJsonDetails dashSaasJsonDetails)
    {

        if (Objects.requireNonNull(dashSaasApiDetails) == DashSaasApiDetails.MakeDebitCardTransaction)
        {
            switch (dashSaasJsonDetails)
            {
                case MakeSGDDebitCardTransactionRequestSchema ->
                        settingForDebitCardTransaction(CardTransactionType.Debit, CurrencyEnum.SGD);
                case MakeUSDDebitCardTransactionRequestSchema ->
                        settingForDebitCardTransaction(CardTransactionType.Debit, CurrencyEnum.USD);
                case MakeSGDReversalCardTransactionRequestSchema ->
                        settingForDebitCardTransaction(CardTransactionType.Reversal, CurrencyEnum.SGD);
                case MakeUSDReversalCardTransactionRequestSchema ->
                        settingForDebitCardTransaction(CardTransactionType.Reversal, CurrencyEnum.USD);
                default -> testConfig.logFail("Key-values are not being verified for " + dashSaasJsonDetails.name());
            }
        } else
        {
            return executeRequestAndGetResponse(dashSaasApiDetails, dashSaasJsonDetails);
        }
        return executeRequestAndGetResponse(dashSaasApiDetails, dashSaasJsonDetails);
    }

    public void verifyApiResponse(Response response, DashSaasApiDetails dashSaasApiDetails, DashSaasJsonDetails expectedJsonObject)
    {
        if (Objects.requireNonNull(dashSaasApiDetails) == DashSaasApiDetails.MakeDebitCardTransaction)
        {
            verifyMakeDebitCardTransaction(response);
        } else
        {
            testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
        }

        if (expectedJsonObject != null)
        {
            verifyJsonResponse(response, dashSaasApiDetails, dashSaasApiDetails.name(), expectedJsonObject, expectedJsonObject.name());
        }
    }

    private void verifyMakeDebitCardTransaction(Response response)
    {
        JSONObject object = new JSONObject(response.body().jsonPath().getString("result"));
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "result", Objects.nonNull(response.body().jsonPath().getString("result")));
        AssertHelper.compareEquals(testConfig, "responseCode", "00", object.getString("responseCode"));
        testConfig.putRunTimeProperty("oldTransactionId", testConfig.getRunTimeProperty("transactionId"));
        testConfig.putRunTimeProperty("oldSystemTraceAuditNumber", testConfig.getRunTimeProperty("systemTraceAuditNumber"));
        testConfig.putRunTimeProperty("cardTransactionUuid", object.getString("partnerReferenceNumber"));
    }

    private void settingForDebitCardTransaction(SaasEnums.CardTransactionType cardTransactionType, PaymentEnums.CurrencyEnum currency)
    {
        if (cardTransactionType.getCardTransactionType().equalsIgnoreCase("DEBIT"))
        {
            testConfig.putRunTimeProperty("transactionId", DataGenerator.generateRandomString(20));
            testConfig.putRunTimeProperty("transactionType", cardTransactionType.getCardTransactionType());
            testConfig.putRunTimeProperty("billingAmount", DataGenerator.generateRandomNumberInIntRange(1, 9));
            testConfig.putRunTimeProperty("billingCurrencyCode", currency);
            testConfig.putRunTimeProperty("transactionAmount", DataGenerator.generateRandomNumberInIntRange(1, 9));
            testConfig.putRunTimeProperty("transactionCurrencyCode", currency);
            testConfig.putRunTimeProperty("merchantNameLocation", "Upwork " + DataGenerator.generateRandomString(20));
            testConfig.putRunTimeProperty("systemTraceAuditNumber", DataGenerator.generateRandomNumberInIntRange(100, 10000000));
        } else
        {
            testConfig.putRunTimeProperty("transactionId", DataGenerator.generateRandomString(20));
            testConfig.putRunTimeProperty("transactionType", cardTransactionType.getCardTransactionType());
            testConfig.putRunTimeProperty("billingAmount", testConfig.getRunTimeProperty("billingAmount"));
            testConfig.putRunTimeProperty("billingCurrencyCode", currency);
            testConfig.putRunTimeProperty("transactionAmount", testConfig.getRunTimeProperty("transactionAmount"));
            testConfig.putRunTimeProperty("transactionCurrencyCode", currency);
            testConfig.putRunTimeProperty("originalTransactionId", testConfig.getRunTimeProperty("oldTransactionId"));
            testConfig.putRunTimeProperty("systemTraceAuditNumber", "null");
            testConfig.putRunTimeProperty("merchantNameLocation", "Reversal " + DataGenerator.generateRandomString(20));
            testConfig.putRunTimeProperty("originalSystemTraceAuditNumber", testConfig.getRunTimeProperty("oldSystemTraceAuditNumber"));
        }
    }
}
