package Automation.Payments.customer.api;

import Automation.Utils.Api.JsonDetails;
import Automation.Utils.Config;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public enum PaymentJsonDetails implements JsonDetails
{

    CreateFxQuotesRequestSchema("requestJson"),
    CreateUsdFxQuotesRequestSchema("requestJson"),
    RequirementDataRequestSchema("requestJson"),
    RequirementDataSubmitRequestSchema("requestJson"),
    RequirementsWithoutFxQuoteIdRequestSchema("requestJson"),
    MakeFxTransferRequestSchema("requestJson"),
    MakeFxTransferWithBudgetRequestSchema("requestJson"),
    MakeFxTransferSuccessfulResponse("responseJson"),
    CreateUsdFxQuoteOnWeekendResponse("responseJson"),
    MakeFxTransferWithoutFeeTypeSuccessResponse("responseJson"),
    MakeLocalTransferSuccessfulResponse("responseJson"),
    RequirementDataSubmitSuccessfulResponse("responseJson"),
    RequirementDataSuccessfulResponse("responseJson"),
    RequirementDataSubmitTransferWiseResponse("responseJson"),
    RequirementDataSuccessfulTransferWiseResponse("responseJson"),
    CreateFxQuotesSuccessfulResponse("responseJson"),
    GetRecipientSuccessfulResponse("responseJson"),
    GetRecipientsListResponse("responseJson"),

    GetDebitAccountSuccessfulResponse("responseJson"),
    GetSgdAndIdrAndUsdDebitAccountResponse("responseJson"),
    GetSgdAndIdrDebitAccountResponse("responseJson"),
    GetUsdAndIdrDebitAccountResponse("responseJson"),
    GetUsdDebitAccountResponse("responseJson"),
    GetSingleDebitAccountResponse("responseJson"),
    GetDoubleDebitAccountsResponse("responseJson"),
    GetTripleDebitAccountsResponse("responseJson"),
    GetSgdDebitAccountResponse("responseJson"),
    GetSgdAndUsdDebitAccountResponse("responseJson"),
    CreateSgdFxQuotesRequestSchema("requestJson"),
    CreatePendingActionLocalFxRequestSchema("requestJson"),
    CreatePendingActionLocalFxSuccessfulResponse("responseJson"),
    GetPendingActionsSuccessfulResponse("responseJson"),
    ApprovePendingActionLocalFxRequestSchema("requestJson"),
    ApprovePendingActionLocalFxSuccessfulResponse("responseJson"),
    GetRecipientSuccessfulResponseTHB("responseJson"),
    GetRecipientSuccessfulResponseIDR("responseJson"),
    GetChargeAccountSuccessfulResponse("responseJson"),
    GetAdvanceRecipientSuccessfulResponseSGD("responseJson"),
    GetRecipientSuccessfulResponseSGD("responseJson"),
    GetRecipientSuccessfulResponseCAD("responseJson"),
    MakeAdvanceTransferRequestSchema("requestJson"),
    GetQuarantinedTransactionResponse("responseJson"),
    GetQuarantinedTransactionResponseIDR("responseJson"),
    MakeLocalTransferWithBudgetRequestSchema("requestJson"),
    MakeLocalTransferWithBudgetSuccessfulResponse("responseJson"),
    GetDebitTransactionsListResponse("responseJson"),
    GetSgdTransactionDetailResponse("responseJson"),
    EmptyResponse("responseJson"),
    GetCleanUpResponse("responseJson"),
    GetDebitAccountSuccessfulCurrenciesResponse("responseJson"),
    GetRecipientSuccessfulCurrenciesResponse("responseJson"),
    GetTransactionByReferenceIdResponse("responseJson");


    String requestOrResponseJson;

    PaymentJsonDetails(String requestOrResponse)
    {
        requestOrResponseJson = requestOrResponse;
    }

    @Override
    public String getJsonFilePath(Config testConfig)
    {
        return System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Payments" + File.separator + "JsonFiles" + File.separator + "customer" + File.separator + requestOrResponseJson + File.separator + this + ".json";
    }

    @Override
    public String getJsonFileData(Config testConfig)
    {
        String filePath = getJsonFilePath(testConfig);
        String jsonString = "";
        try
        {
            byte[] dataFromFile = FileUtils.readFileToByteArray(new File(filePath));
            jsonString = new String(dataFromFile, Charset.defaultCharset());
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return jsonString;
    }

    @Override
    public JSONArray getExpectedJSONArray(Config testConfig)
    {
        try
        {
            return new JSONArray(getJsonFileData(testConfig));
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return null;
    }

    @Override
    public JSONObject getExpectedJSONObject(Config testConfig)
    {
        try
        {
            return new JSONObject(getJsonFileData(testConfig));
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return null;
    }

}
