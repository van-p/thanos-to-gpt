package Automation.Payments.dash.api;

import Automation.Utils.Api.JsonDetails;
import Automation.Utils.Config;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public enum DashPaymentJsonDetails implements JsonDetails
{
    GetQuarantinedTransactionListResponse("responseJson"),
    GetQuarantinedTransactionDetailResponse("responseJson"),
    ApproveQuarantinedTransactionRequestSchema("requestJson"),
    GetApprovedQuarantinedTransactionResponse("responseJson"),
    CommandOnFlyRequestSchema("requestJson"),
    CommandOnFlyResponse("responseJson"),
    RejectQuarantinedTransactionRequestSchema("requestJson");

    String requestOrResponseJson;

    DashPaymentJsonDetails(String requestOrResponse)
    {
        requestOrResponseJson = requestOrResponse;
    }

    @Override
    public String getJsonFilePath(Config testConfig)
    {
        return System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Payments" + File.separator + "JsonFiles" + File.separator + "dash" + File.separator + requestOrResponseJson + File.separator + this + ".json";
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
