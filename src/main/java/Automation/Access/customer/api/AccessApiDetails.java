package Automation.Access.customer.api;

import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Config;
import io.restassured.http.ContentType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

public enum AccessApiDetails implements ApiDetails
{
    PostAuthToken("/v1/auth/token", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.xexampleApplication, Headers.xAuthSession),
    PostAuthLogin("/v1/auth/login", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.xAuthSession),
    GetUserAccesses("/v1/user-accesses", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "business_uuid, business_type, limit, page", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    OauthToken("/oauth/token", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null),
    GetAuth("/v1/auth", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication),
    GetAuth2fa("/v1/auth?_fields[]=has_enabled_2fa&_fields[]=has_2fa_enabled_business", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication),
    GetAuthOtpChannel("/v1/auth?_fields[]=otp_channel", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication),
    PutAuthOtpChannel("/v1/auth", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication),
    GetOptionsOtpChannel("/v1/options?type=otp_channel", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication),
    GetListOfEmployees("/v1/access", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "source_uuid,source_type,limit,page,search,order_by", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetListOfEmployeesInBudget("/v1/access", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "source_uuid,source_type,budget_uuid,limit,page,search,order_by", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetListOfPeople("/v1/people/{$businessUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "is_creating_debit_card,page,limit,search,budget_uuid", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetPlanDetails("/v1/user-accesses/plan-details-and-extra-charges", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "business_uuid,business_type,_fields", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetFilterUsers("/v1/filters", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "source_uuid,source_type,entity_type", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetAllNavigationItems("/v1/navigation-items/business-person/all", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "_fields", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetAvailableNavigationItems("/v1/navigation-items/business-person/available", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "_fields", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutPersonal2fa("/v1/2fa", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeletePersonal2fa("/v1/2fa", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutBusiness2fa("/v1/businesses/{$businessUuid}/2fa", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeleteBusiness2fa("/v1/businesses/{$businessUuid}/2fa", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutUpdatePreferredName("/v1/auth", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, "preferred_name", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PostRequestAdditionalToken("/oauth/token", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.xAuthSession),
    PostUpdatePhoneNumber("/v1/phones", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutPasswordChange("/v1/passwords/update", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutPasswordReset("/v1/passwords/reset", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutBusinessProperties("/v1/businesses/{$businessUuid}/properties", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication);

    String apiPath;
    ApiHost apiHost;
    ApiRequestType apiRequestType;
    ContentType apiContentType;
    String formParams;
    String requestParams;
    ArrayList<Headers> requiredHeaders;

    AccessApiDetails(String tempPath, ApiHost tempApiHost, ApiRequestType tempApiRequestType, ContentType tempApiContentType, String tempRequestParams, String tempFormParams, Headers... tempRequiredHeaders)
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
