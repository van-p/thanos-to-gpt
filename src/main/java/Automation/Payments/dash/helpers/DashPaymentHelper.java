package Automation.Payments.dash.helpers;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Payments.dash.api.DashPaymentApiDetails;
import Automation.Payments.dash.api.DashPaymentJsonDetails;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.WaitHelper;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class DashPaymentHelper extends ApiHelper
{
    public static DashPaymentStaticDataBase dashPaymentStaticDataBase;

    public DashPaymentHelper(Config testConfig)
    {
        super(testConfig);
        initialiseStaticData();
    }

    public DashPaymentHelper(Config testConfig, int... respectiveSheetRowNumbers)
    {
        super(testConfig, new String[]{"DashUserDetails"}, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public DashPaymentHelper(Config testConfig, String[] sheets, int... respectiveSheetRowNumbers)
    {
        super(testConfig, sheets, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public void initialiseStaticData()
    {
        if (Config.appLanguage != null)
        {
            AccessEnums.CustomerPortalLanguage.valueOf(Config.appLanguage);
            dashPaymentStaticDataBase = new DashPaymentStaticDataEn();
        } else
        {
            dashPaymentStaticDataBase = new DashPaymentStaticDataEn();
        }
    }

    // api
    public Response sendRequestAndGetResponse(DashPaymentApiDetails api, DashPaymentJsonDetails jsonDetails)
    {
        return executeRequestAndGetResponse(api, jsonDetails);
    }

    public void verifyApiResponse(Response response, DashPaymentApiDetails api, DashPaymentJsonDetails paymentJsonDetails)
    {
        switch (api)
        {
            case GetQuarantinedTransactionList:
                if (Objects.requireNonNull(paymentJsonDetails) == DashPaymentJsonDetails.GetQuarantinedTransactionListResponse)
                {
                    verifyQuarantinedTransactionListFromDash(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for API - " + paymentJsonDetails.name());
                }
                break;
            case GetQuarantinedTransactionDetailFromDash:
                if (Objects.requireNonNull(paymentJsonDetails) == DashPaymentJsonDetails.GetQuarantinedTransactionDetailResponse)
                {
                    verifyQuarantinedTransactionDetailFromDash(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for API - " + paymentJsonDetails.name());
                }
                break;
            case RejectQuarantinedTransaction:
            case ApproveQuarantinedTransaction:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                break;
            case GetDashTransactionDetail:
                if (Objects.requireNonNull(paymentJsonDetails) == DashPaymentJsonDetails.GetApprovedQuarantinedTransactionResponse)
                {
                    verifyApprovedQuarantinedTransactionDetail(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for API - " + paymentJsonDetails.name());
                }
                break;
            case LiveTinkering:
                if (Objects.requireNonNull(paymentJsonDetails) == DashPaymentJsonDetails.CommandOnFlyResponse)
                {
                    verifyCommandResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for API - " + paymentJsonDetails.name());
                }
                break;
            default:
                testConfig.logFail("Key-values are not being verified for API - " + paymentJsonDetails.name());
        }

        if (paymentJsonDetails != null)
        {
            verifyJsonResponse(response, api, api.name(), paymentJsonDetails, paymentJsonDetails.name());
        }
    }

    private void verifyCommandResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Exit code", 0, response.body().jsonPath().getInt("exit_code"));
        AssertHelper.compareTrue(testConfig, "Result", Objects.nonNull(response.body().jsonPath().getString("result")));
    }

    private void verifyQuarantinedTransactionListFromDash(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "Transaction List should not be 0", response.jsonPath().getList("items").size() > 0);
        JSONObject jsonResponseBody = new JSONObject(response.body().asString());
        JSONArray quarantinedTransactions = jsonResponseBody.getJSONArray("items");
        int i = 0;
        for (i = 0; i < quarantinedTransactions.length(); i++)
        {
            if (quarantinedTransactions.getJSONObject(i).getString("uuid").equals(testConfig.getRunTimeProperty("quarantinedUuid")))
                break;
        }
        if (i < quarantinedTransactions.length())
        {
            JSONObject quarantinedTransaction = quarantinedTransactions.getJSONObject(i);
            AssertHelper.compareTrue(testConfig, "Transaction Reference Code", quarantinedTransaction.get("reference_code").equals(testConfig.getRunTimeProperty("quarantinedReferenceCode")));
            AssertHelper.compareEquals(testConfig, "Quarantined Transaction Amount", -Integer.parseInt(testConfig.testData.get("amount")), quarantinedTransaction.get("source_amount"));
            AssertHelper.compareEquals(testConfig, "Quarantined Transaction Type", "outbound", quarantinedTransaction.getString("type"));
            AssertHelper.compareEquals(testConfig, "Quarantined Transaction Currency code", testConfig.testData.get("chargeAccountCurrencyCode"), quarantinedTransaction.getString("source_currency_code"));
            AssertHelper.compareEquals(testConfig, "Quarantined Transaction counterparty_name", testConfig.testData.get("recipientName"), quarantinedTransaction.getString("counterparty_name"));
            AssertHelper.compareEquals(testConfig, "Quarantined Transaction State code", testConfig.getRunTimeProperty("quarantinedStateCode"), quarantinedTransaction.getString("state_code"));
        } else
            testConfig.logFail("Created Transaction is not present in quarantined list");
    }

    private void verifyQuarantinedTransactionDetailFromDash(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Quarantined Transaction UUID", testConfig.getRunTimeProperty("quarantinedUuid"), response.jsonPath().get("uuid"));
        AssertHelper.compareTrue(testConfig, "Transaction Reference Code", response.jsonPath().get("reference_code").equals(testConfig.getRunTimeProperty("quarantinedReferenceCode")));
        AssertHelper.compareEquals(testConfig, "Quarantined Transaction Amount", -Integer.parseInt(testConfig.testData.get("amount")), response.jsonPath().get("source_amount"));
        AssertHelper.compareEquals(testConfig, "Quarantined Transaction Type", "outbound", response.jsonPath().getString("type"));
        AssertHelper.compareEquals(testConfig, "Quarantined Transaction Currency code", testConfig.testData.get("chargeAccountCurrencyCode"), response.jsonPath().getString("source_currency_code"));
        AssertHelper.compareEquals(testConfig, "Quarantined Transaction counterparty_name", testConfig.testData.get("recipientName"), response.jsonPath().getString("counterparty_name"));
        AssertHelper.compareEquals(testConfig, "Quarantined Transaction State code", testConfig.getRunTimeProperty("quarantinedStateCode"), response.jsonPath().getString("state_code"));
    }

    public void verifyApprovedQuarantinedTransaction()
    {
        int i = 0;
        Response quarantinedTransactionDetail = null;
        for (i = 0; i < 5; i++)
        {
            quarantinedTransactionDetail = sendRequestAndGetResponse(DashPaymentApiDetails.GetQuarantinedTransactionStateFromDash, null);
            if (quarantinedTransactionDetail.jsonPath().getString("state.state").equalsIgnoreCase("approved"))
                break;
            WaitHelper.waitForSeconds(testConfig, 2);
        }
        if (i == 5)
            testConfig.logFail("Quarantined Transaction did not get approved");
        else
            testConfig.logPass("Quarantined Transaction got approved");
    }

    public void verifyRejectedQuarantinedTransaction()
    {
        int i = 0;
        Response quarantinedTransactionDetail = null;
        for (i = 0; i < 5; i++)
        {
            quarantinedTransactionDetail = sendRequestAndGetResponse(DashPaymentApiDetails.GetQuarantinedTransactionStateFromDash, null);
            if (quarantinedTransactionDetail.jsonPath().getString("state.state").equalsIgnoreCase("rejected"))
                break;
            WaitHelper.waitForSeconds(testConfig, 2);
        }
        if (i == 5)
            testConfig.logFail("Quarantined Transaction did not get rejected");
        else
            testConfig.logPass("Quarantined Transaction got rejected");
    }

    private void verifyApprovedQuarantinedTransactionDetail(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "type", "debit", response.jsonPath().getString("type"));
        AssertHelper.compareEquals(testConfig, "Transaction uuid", testConfig.getRunTimeProperty("transactionUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Transaction code", testConfig.getRunTimeProperty("transactionId"), response.jsonPath().getString("code"));
        AssertHelper.compareEquals(testConfig, "Transaction Amount", -Integer.parseInt(testConfig.testData.get("amount")), Integer.parseInt(response.jsonPath().getString("amount")));
        AssertHelper.compareEquals(testConfig, "Transaction Currency", testConfig.testData.get("chargeAccountCurrencyCode"), response.jsonPath().getString("currency"));
        AssertHelper.compareEquals(testConfig, "recipient_name", testConfig.testData.get("recipientName"), response.jsonPath().getString("counter_party_name"));
        AssertHelper.compareEquals(testConfig, "counter_party_code", testConfig.testData.get("recipientAccountNumber"), response.jsonPath().getString("counter_party_code"));
    }

    public void setCommandLine(DashPaymentEnums.CommandLine commandLine)
    {
        testConfig.putRunTimeProperty("command", commandLine.getCommand());
    }
}
