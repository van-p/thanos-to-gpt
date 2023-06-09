package Automation.Access.dash.api;

import Automation.Utils.Api.JsonDetails;
import Automation.Utils.Config;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public enum DashAccessJsonDetails implements JsonDetails
{
    LoginRequestSchema("requestJson"),
    AuthSuccessfulResponse("responseJson"),
    OauthTokenRequestSchema("requestJson"),
    OauthTokenSuccessfulResponse("responseJson"),
    EligibilitiesListSuccessfulResponse("responseJson"),
    PostNewPeopleSchema("requestJson"),
    GetPeopleDetailsCreatedFromDashSuccessfulResponse("responseJson"),
    PostNewBusinessSchema("requestJson"),
    GetBusinessDetailsCreatedFromDashSuccessfulResponse("responseJson"),
    PostNewGenieScenarioSchema("requestJson"),
    CreateGenieScenarioSuccessfulResponse("responseJson"),
    CreateScenarioDataSuccessfulResponse("responseJson"),
    CreateScenarioDataSchema("requestJson"),
    CreateDataSchemaSgdIdrUsd("requestJson"),
    CreateDataSgdIdrUsdResponse("responseJson"),
    CreateVerifiedBusinessSchema("requestJson"),
    CreateVerifiedBusinessResponse("responseJson"),
    CreateDataNewPendingBusinessSchema("requestJson"),
    CreateDataNewPendingBusinessResponse("responseJson"),
    BusinessSettingsKybVerifiedKycVerifiedRequest("requestJson"),
    KybVerifiedKycVerifiedBusinessSettingsResponse("responseJson"),
    IntegrationsMultiRoleBudgetRequest("requestJson"),
    IntegrationsMultiRoleBudgetResponse("responseJson"),
    PostNewModuleSchema("requestJson"),
    PostNewModuleSchemaResponse("responseJson"),
    PutModuleSchema("requestJson"),
    PutModuleSchemaResponse("responseJson"),
    GetNewModuleSchemaResponse("responseJson"),
    UploadFileSuccessfulResponse("responseJson");
    String requestOrResponseJson;

    DashAccessJsonDetails(String requestOrResponse)
    {
        requestOrResponseJson = requestOrResponse;
    }

    @Override
    public String getJsonFilePath(Config testConfig)
    {
        return System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Access" + File.separator + "JsonFiles" + File.separator + "dash" + File.separator + requestOrResponseJson + File.separator + this + ".json";
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
