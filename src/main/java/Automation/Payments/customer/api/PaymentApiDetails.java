package Automation.Payments.customer.api;

import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Config;
import io.restassured.http.ContentType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

public enum PaymentApiDetails implements ApiDetails
{
    GetDebitAccount("/v1/debit-accounts", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetRecipient("/v1/counterparties", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "business_uuid,search,limit,page", null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    CreateFxQuotes("/v1/fx/quotes", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    RequirementData("/v1/fx/requirements", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    RequirementDataSubmit("/v1/fx/requirements?submit=1", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    MakeFxTransfer("/v1/fx/transfers", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetDebitAccountWithQueryParams("/v1/debit-accounts?business_uuid={$businessUuid}&_fields[]=uuid&_fields[]=currency_code&sort_by_balance=1", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    CreatePendingActions("/v1/pending-actions", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    ApprovePendingAction("/v1/pending-actions/{$pendingActionUuid}/approve", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid, Headers.xexampleAccountUuid),
    GetPendingActions("/v1/pending-actions", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "state_code,source_type,source_uuid", null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid, Headers.xexampleAccountUuid),
    DeletePendingActions("/v1/pending-actions/{$pendingActionUuid}", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, "source_type,source_uuid", null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid, Headers.xexampleAccountUuid),
    GetChargeAccount("/v1/debit-accounts?types[]=current&types[]=charge&_fields[]=uuid&_fields[]=balance&_fields[]=currency&_fields[]=currency_codes&_fields[]=type&_fields[]=authorizations", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetTransactionDetail("/v1/debit-transactions/{$transactionUuid}?_fields[]=expected_date&_fields[]=provider_transaction&_fields[]=quarantined_transaction&_fields[]=uuid&_fields[]=type&_fields[]=amount&_fields[]=currency_code&_fields[]=bank_name&_fields[]=counterparty_uuid&_fields[]=counterparty_name&_fields[]=counterparty_code&_fields[]=bank&_fields[]=counterparty_bank_code&_fields[]=debit_account&_fields[]=created_at", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    MakeLocalTransfer("/v1/debit-accounts/transfer-outside", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetDebitTransactionsListByBudget("/v1/debit-transactions", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "source_type,source_uuid,page,limit,_filters[budget_uuids][0]", null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    MakeLocalTransferWithOtp("/v1/debit-accounts/transfer-outside", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid, Headers.xexampleAdditionalToken),
    GetTransactionByReferenceId("/v1/debit-transactions", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "source_type,source_uuid,_filters[search]", null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid);

    String apiPath;
    ApiHost apiHost;
    ApiRequestType apiRequestType;
    ContentType apiContentType;
    String formParams;
    String requestParams;
    ArrayList<Headers> requiredHeaders;

    PaymentApiDetails(String tempPath, ApiHost tempApiHost, ApiRequestType tempApiRequestType, ContentType tempApiContentType, String tempRequestParams, String tempFormParams, Headers... tempRequiredHeaders)
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
