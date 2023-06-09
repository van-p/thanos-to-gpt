package Automation.Access.dash.api;

import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Config;
import io.restassured.http.ContentType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

public enum DashAccessApiDetails implements ApiDetails
{
    PostAuthLogin("/dashboard/auth/login-password", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.xAuthSession),
    DeleteAuthLogout("/dashboard/auth/logout", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization),
    PostOauthToken("/oauth/token", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null),
    GetEligibilitiesList("/dashboard/eligibilities", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "source_type,elementsPerPage,currentPage", null, Headers.Authorization),
    PostNewPeople("/dashboard/people", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization),
    GetPeopleDetails("/dashboard/people/{$peopleUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "_fields", null, Headers.Authorization),
    PostNewBusinesses("/dashboard/businesses", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization),
    GetBusinessDetails("/dashboard/businesses/{$businessUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "_fields", null, Headers.Authorization),
    PostUploadFile("/dashboard/files/document", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.MULTIPART, "source_uuid,source_type,source_collection,type", "mimeType,filePath", Headers.Authorization),
    CreateGenieScenario("/dashboard/genie-scenarios", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization),
    CreateScenarioData("/dashboard/genie-scenarios/process", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization),
    DeleteGenieScenario("/dashboard/genie-scenarios/{$genieScenarioUuid}", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization),
    GetModuleUuid("/dashboard/premium-modules/{$moduleUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "_fields", null, Headers.Authorization),
    PutModule("/dashboard/premium-modules/{$moduleUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization),
    DeleteModule("/dashboard/premium-modules/{$moduleUuid}", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization),
    PostNewModule("/dashboard/premium-modules", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization),
    GetCreateKybRequirements("/dashboard/businesses/{$businessUuid}/create-kyb-requirements", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "type", null, Headers.Authorization);

    String apiPath;
    ApiHost apiHost;
    ApiRequestType apiRequestType;
    ContentType apiContentType;
    String formParams;
    String requestParams;
    ArrayList<Headers> requiredHeaders;

    DashAccessApiDetails(String tempPath, ApiHost tempApiHost, ApiRequestType tempApiRequestType, ContentType tempApiContentType, String tempRequestParams, String tempFormParams, Headers... tempRequiredHeaders)
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

    @Override
    public ArrayList<Headers> getRequiredHeaders()
    {
        return requiredHeaders;
    }

    public void setRequestParams(String requestParams)
    {
        this.requestParams = requestParams;
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
