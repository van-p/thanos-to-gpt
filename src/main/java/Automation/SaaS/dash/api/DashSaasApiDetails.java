package Automation.SaaS.dash.api;

import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Config;
import io.restassured.http.ContentType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

public enum DashSaasApiDetails implements ApiDetails
{
    MakeDebitCardTransaction("/dashboard/webhook-presets/947ee1c4-6e13-4bd9-8c3a-fe26cda4b854", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization);

    String apiPath;
    ApiHost apiHost;
    ApiRequestType apiRequestType;
    ContentType apiContentType;
    String formParams;
    String requestParams;
    ArrayList<Headers> requiredHeaders;

    DashSaasApiDetails(String tempPath, ApiHost tempApiHost, ApiRequestType tempApiRequestType, ContentType tempApiContentType, String tempRequestParams, String tempFormParams, Headers... tempRequiredHeaders)
    {
        apiPath = tempPath;
        apiHost = tempApiHost;
        apiRequestType = tempApiRequestType;
        apiContentType = tempApiContentType;
        formParams = tempFormParams;
        requestParams = tempRequestParams;
        requiredHeaders = new ArrayList<>();
        Collections.addAll(requiredHeaders, tempRequiredHeaders);
    }

    @Override
    public ContentType getApiContentType()
    {
        return apiContentType;
    }

    @Override
    public ApiHost getApihost()
    {
        return apiHost;
    }

    @Override
    public ApiRequestType getApiRequestType()
    {
        return apiRequestType;
    }

    @Override
    public String getFormParams()
    {
        return formParams;
    }

    @Override
    public String getApiPath()
    {
        return apiPath;
    }

    @Override
    public String getRequestParams()
    {
        return requestParams;
    }

    public void setRequestParams(String requestParams)
    {
        this.requestParams = requestParams;
    }

    @Override
    public ArrayList<Headers> getRequiredHeaders()
    {
        return requiredHeaders;
    }

    public void setRequiredHeaders(ArrayList<Headers> requiredHeaders)
    {
        this.requiredHeaders = requiredHeaders;
    }

    @Override
    public String getUrl(Config testConfig, Object... params)
    {
        String tempPath = apiPath;
        if (tempPath.contains("{$"))
        {
            tempPath = testConfig.replaceArgumentsWithRunTimeProperties(apiPath);
        }
        if (apiHost != null)
        {
            return MessageFormat.format(testConfig.getRunTimeProperty(apiHost.toString()) + tempPath, params);
        }
        return MessageFormat.format(tempPath, params);
    }
}
