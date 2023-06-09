package Automation.SaaS.customer.api;

import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Config;
import io.restassured.http.ContentType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

public enum SaasApiDetails implements ApiDetails
{
    CreateBudget("/v1/budgets", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UpdateBudget("/v1/budgets/{$budgetUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeactivateBudget("/v1/budgets/{$budgetUuid}/deactivate", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    ReactivateBudget("/v1/budgets/{$budgetUuid}/reactivate", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetPeopleFromBudget("/v1/budgets/{$budgetUuid}/people", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "order_by", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetPeopleFromBudgetWithoutHeaders("/v1/budgets/{$budgetUuid}/people", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "order_by", null),
    GetPeopleFromBudgetWithoutAuthorizations("/v1/budgets/{$budgetUuid}/people", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "order_by", null, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    IssueNewCard("/v1/debit-cards", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    SubmitClaim("/v1/claims", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    ApproveClaim("/v1/claims/{$claimUuid}/approve", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    RejectClaim("/v1/claims/{$claimUuid}/reject", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PayClaim("/v1/debit-accounts/transfer-outside", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAdditionalToken, Headers.idempotencyKey),
    ScheduleClaimPayment("/v1/debit-scheduled-payments", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAdditionalToken, Headers.idempotencyKey),
    StopScheduledClaimPayment("/v1/debit-scheduled-payments/{$transferUuid}/stop", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UpdateClaim("/v1/claims/{$claimUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetClaimDetails("/v1/claims/{$claimUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "business_uuid", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetClaimSourceOfFunds("/v1/claims/source-of-funds", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UploadFile("/v1/files/document", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.MULTIPART, null, "filePath,mimeType", Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetFileUrl("/v1/files/{$fileUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutFileUrl("/v1/files/{$fileUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, "source_uuid,source_type,type,source_collection", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    AddOwnerOrMemberToBudget("/v1/budgets/{$budgetUuid}/create-accesses", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    RemoveOwnerOrMemberFromBudget("/v1/budgets/{$budgetUuid}/revoke-access", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    CreatePaymentLink("/v1/payment-links", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeletePaymentLink("/v1/payment-links/{$paymentLinkUuid}?source_type=business&source_uuid={$businessUuid}", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAccountUuid),
    FreezeBudget("/v1/budgets/{$budgetUuid}/deactivate", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UnfreezeBudget("/v1/budgets/{$budgetUuid}/reactivate", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetBudgetsWithStatus("/v1/budgets?_fields[]=uuid&_fields[]=state&_fields[]=currency.code&_fields[]=spent_amount", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "page,limit,source_type,source_uuid,type,state_codes[]", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetPaymentLink("/v1/payment-links?source_type=business&source_uuid={$businessUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    CreatePaymentLinkNotifier("/v1/payment-links/notifiers", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UpdatePaymentLink("/v1/payment-links/{$paymentLinkUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAccountUuid),
    CreateInvoiceCounterParty("/v1/invoice-counterparties", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetInvoiceCounterParty("/v1/invoice-counterparties/{$invoiceCounterPartyUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UpdateInvoiceCounterParty("/v1/invoice-counterparties/{$invoiceCounterPartyUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeleteInvoiceCounterParty("/v1/invoice-counterparties/{$invoiceCounterPartyUuid}", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    CreateInvoice("/v1/invoices", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetInvoiceList("/v1/invoices?source_type=business&source_uuid={$businessUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAccountUuid),
    PreviewInvoice("/v1/invoices/{$invoiceUuid}/preview", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    CreateInvoiceItem("/v1/invoice-items", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    UpdateInvoiceItem("/v1/invoice-items/{$invoiceUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeleteInvoice("/v1/invoices/{$invoiceUuid}", ApiHost.ApiBasePath, ApiRequestType.DELETE, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PayInvoice("/v1/invoices/{$invoiceUuid}/paid?paid_at={$invoicePaidAt}&paid_note={$invoicePaidNote}&source_type=business&source_uuid={$businessUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    SubmitInvoice("/v1/invoices/{$invoiceUuid}/submit", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAccountUuid),
    GetInvoiceInsight("/v1/invoice-insights?source_uuid={$businessUuid}&source_type=business", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAccountUuid),
    GetInvoiceMiniInsight("/v1/invoice-insights?source_uuid={$businessUuid}&source_type=business&_fields[]=mini_insights", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication, Headers.xexampleAccountUuid),
    GetInvoiceNotification("/v1/invoice-notifications?source_uuid={$businessUuid}&source_type=business", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetInvoiceReminder("/v1/invoice-reminders?source_uuid={$invoiceUuid}&source_type=invoice&type=reminder", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    CreateInvoiceReminder("/v1/invoice-reminders", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    BillSubmission("/v1/bill-pay-submissions", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetBillLineItem("/v1/bill-pay-submissions/{$bill_uuid}/line-items?source_type=business&source_uuid={$source_uuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid, Headers.xexampleAccountUuid),
    SubmitBill("/v1/bill-pay-submissions/{$bill_uuid}/submit?_fields[]=authorizations.approve&_fields[]=authorizations.review&_fields[]=uuid", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    ApproveBill("/v1/bill-pay-submissions/{$bill_uuid}/approve", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    SaveBillTransferDetail("/v1/bill-pay-submissions/{$bill_uuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetXeroBillLineItem("/v1/accounting/line-items?source_type=bill-pay-submission&source_uuid={$bill_uuid}&type=xero", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    UpdateXeroBillLineItem("/v1/accounting/line-items", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    ApproveXeroBill("/v1/accounting/bills/approve", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    SyncXeroBill("/v1/accounting/bills/sync", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetDetailXeroBill("/v1/accounting/bills/{$bill_uuid}?_fields[]=uuid&_fields[]=contact&_fields[]=bill_number&_fields[]=currency_code&_fields[]=accounting_invoice.state&_fields[]=line_item_count&type={$accounting_type}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetPendingReviewXeroBill("/v1/accounting/bills?page=1&limit=10&step=pending_review_invoices&_fields[]=uuid&_fields[]=accounting_invoice.state&_fields[]=line_item_count&type={$accounting_type}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    AddReportingField("/v1/accounting/reporting-fields", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetCardList("/v1/debit-cards", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "limit,page,_fields", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    FreezeCard("/v1/debit-cards/{$cardUuid}/temporary-lock", ApiHost.ApiBasePath, ApiRequestType.POST, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    GetCardDetail("/v1/debit-cards/{$cardUuid}/show-without-unmask", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "limit,page,_fields", null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    PutUpdateAccessBudget("/v1/budgets/{$budgetUuid}/update-access", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleBusinessUuid, Headers.xexampleApplication),
    DeactivateReportingField("/v1/accounting/reporting-fields/{$field_uuid}/deactivate", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetDetailReportingField("/v1/accounting/reporting-fields/{$field_uuid}?_fields[]=uuid&_fields[]=name&_fields[]=icon&_fields[]=type&_fields[]=code&_fields[]=description&_fields[]=field_type&_fields[]=state&_fields[]=authorizations.update&_fields[]=authorizations.activate&_fields[]=authorizations.deactivate", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    GetXeroAccount("/v1/accounting/accounts?type={$accounting_type}&include_tax=1", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid, Headers.xexampleAccountUuid),
    GetDebitCardTransaction("/v1/debit-transactions/{$cardTransactionUuid}", ApiHost.ApiBasePath, ApiRequestType.GET, ContentType.JSON, "_fields", null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid),
    UpdateCard("/v1/debit-cards/{$cardUuid}", ApiHost.ApiBasePath, ApiRequestType.PUT, ContentType.JSON, null, null, Headers.Authorization, Headers.xexampleApplication, Headers.xexampleBusinessUuid);

    String apiPath;
    ApiHost apiHost;
    ApiRequestType apiRequestType;
    ContentType apiContentType;
    String formParams;
    String requestParams;
    ArrayList<Headers> requiredHeaders;

    SaasApiDetails(String tempPath, ApiHost tempApiHost, ApiRequestType tempApiRequestType, ContentType tempApiContentType, String tempRequestParams, String tempFormParams, Headers... tempRequiredHeaders)
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

    public void setFormParams(String formParams)
    {
        this.formParams = formParams;
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

    public void setFieldsParams(String... values)
    {
        if (values == null || values.length == 0)
        {
            throw new IllegalArgumentException("At least one input value is required");
        }
        StringBuilder sb = new StringBuilder("?");
        for (String value : values)
        {
            sb.append("_fields[]=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1); // remove the last '&' character
        this.apiPath = this.apiPath + sb;
    }
}
