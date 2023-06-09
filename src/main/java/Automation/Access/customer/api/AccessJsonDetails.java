package Automation.Access.customer.api;

import Automation.Utils.Api.JsonDetails;
import Automation.Utils.Config;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public enum AccessJsonDetails implements JsonDetails
{
    AuthLoginRequestSchema("requestJson"),
    AuthLoginFailedLoginResponse("responseJson"),
    GetUserAccessesSuccessfulResponse("responseJson"),
    AuthLoginNonAdminRoleResponse("responseJson"),
    OauthTokenRequestSchema("requestJson"),
    OauthTokenOtpRequestSchema("requestJson"),
    OauthTokenSuccessfulResponse("responseJson"),
    GetAuthResponseSchema("responseJson"),
    GetAuthResponseSchemaEasyRequirement("responseJson"),
    GetAuth2faSuccessfulResponse("responseJson"),
    GetListOfPeopleSuccessfulResponse("responseJson"),
    EmployeesListSuccessfulResponse("responseJson"),
    PostAuthTokenRequestSchema("requestJson"),
    PostAuthTokenSuccessfulResponse("responseJson"),
    EmployeesInBudgetListSuccessResponse("responseJson"),
    AuthLoginOkSingleBusinessWithDebitAccount("responseJson"),
    GetPlanDetailsSuccessfulResponse("responseJson"),
    GetFilterUsersSuccessfulResponse("responseJson"),
    GetAllNavigationItemSuccessfulResponse("responseJson"),
    GetAvailableNavigationItemSuccessfulResponse("responseJson"),
    UpdatePreferredNameRequestSchema("requestJson"),
    UpdatePreferredNameSuccessfulResponse("responseJson"),
    Get2faSuccessfulResponse("responseJson"),
    GetOptionsOtpChannelSuccessfulResponse("responseJson"),
    PutAuthOtpChannelRequestSchema("requestJson"),
    GetAuthOtpChannelSuccessfulResponse("responseJson"),
    OtpTokenRequestSchema("requestJson"),
    OtpTokenSuccessResponse("responseJson"),
    MissingInfoAuthLoginSingleBusinessSuccessfulResponse("responseJson"),
    AuthLoginMultipleBusinessesSuccessfulResponse("responseJson"),
    PutPasswordChangeRequestSchema("requestJson"),
    PutPasswordChangeSuccessResponse("responseJson"),
    PutPasswordResetRequestSchema("requestJson"),
    PutPasswordResetSuccessResponse("responseJson"),
    UpdatePhoneNumberRequestSchema("requestJson"),
    UpdatePhoneNumberSuccessfulResponse("responseJson"),
    PutBusinessHeadquarterRequestSchema("requestJson"),
    PutBusinessHeadquarterSuccessResponse("responseJson"),
    PutNumberOfEmployeeRequestSchema("requestJson"),
    PutNumberOfEmployeeSuccessResponse("responseJson");

    String requestOrResponseJson;

    AccessJsonDetails(String requestOrResponse)
    {
        requestOrResponseJson = requestOrResponse;
    }

    @Override
    public String getJsonFilePath(Config testConfig)
    {
        return System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Access" + File.separator + "JsonFiles" + File.separator + "customer" + File.separator + requestOrResponseJson + File.separator + this + ".json";
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
