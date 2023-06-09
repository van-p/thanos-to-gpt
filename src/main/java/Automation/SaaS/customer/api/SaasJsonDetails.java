package Automation.SaaS.customer.api;

import Automation.Utils.Api.JsonDetails;
import Automation.Utils.Config;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public enum SaasJsonDetails implements JsonDetails
{
    CreateBudgetSingleOwnerRequestSchema("requestJson"),
    CreateBudgetResponse("responseJson"),
    BudgetDeactivatedDetailResponse("responseJson"),
    BudgetReactivatedDetailResponse("responseJson"),
    BudgetReactivatedDetailRequest("requestJson"),
    UpdateBudgetSingleOwnerRequestSchema("requestJson"),
    UpdateBudgetResponse("responseJson"),
    UpdateBudgetResponseWithoutCreditTransaction("responseJson"),
    BudgetSingleOwnerResponse("responseJson"),
    ErrorMessageResponse("responseJson"),
    IssueNewVirtualCardRequestSchema("requestJson"),
    IssueNewPhysicalCardRequestSchema("requestJson"),

    IssueNewVirtualCardResponse("responseJson"),
    IssueNewPhysicalCardResponse("responseJson"),
    AddBudgetOwnerRequestSchema("requestJson"),
    AddBudgetMemberRequestSchema("requestJson"),
    BudgetMultipleOwnerResponse("responseJson"),
    BudgetMultipleOwnerAndMemberResponse("responseJson"),
    BudgetDetailResponse("responseJson"),
    CreateBudgetMultipleOwnerRequestSchema("requestJson"),
    RemoveBudgetPersonSchema("requestJson"),
    BudgetOwnerRevokedResponse("responseJson"),
    BudgetOwnerAndMemberRevokedResponse("responseJson"),
    BudgetMultipleOwnerAndMemberResponseWithoutAdmin("responseJson"),
    UnauthorizedResponse("responseJson"),
    SubmitClaimRequestSchema("requestJson"),
    SubmitClaimResponse("responseJson"),
    RejectClaimRequestSchema("requestJson"),
    PayClaimRequestSchema("requestJson"),
    PayClaimResponse("responseJson"),
    ScheduleClaimPaymentResponse("responseJson"),
    UploadReceiptResponse("responseJson"),
    AttachDocumentResponse("responseJson"),
    GetFileResponse("responseJson"),
    CreatePaymentLinkRequestSchema("requestJson"),
    CreatePaymentLinkResponse("responseJson"),
    FrozenBudgetResponse("responseJson"),
    UnfreezeBudgetRequestSchema("requestJson"),
    UnfreezeBudgetResponse("responseJson"),
    BudgetsResponse("responseJson"),
    GetPaymentLinkResponse("responseJson"),
    CreatePaymentLinkNotifierRequestSchema("requestJson"),
    CreatePaymentLinkNotifierResponse("responseJson"),
    UpdatePaymentLinkRequestSchema("requestJson"),
    UpdatePaymentLinkResponse("responseJson"),
    CreatePaymentLinkResponseRequiredOnly("responseJson"),
    CreateInvoiceCounterPartyRequestSchema("requestJson"),
    CreateInvoiceCounterPartyResponse("responseJson"),
    GetInvoiceCounterPartyRequestSchema("requestJson"),
    GetInvoiceCounterPartyResponse("responseJson"),
    UpdateInvoiceCounterPartyRequestSchema("requestJson"),
    UpdateInvoiceCounterPartyResponse("responseJson"),
    CreateInvoiceRequestSchema("requestJson"),
    CreateInvoiceResponse("responseJson"),
    PreviewInvoiceResponse("responseJson"),
    GetInvoiceListResponse("responseJson"),
    CreateInvoiceItemRequestSchema("requestJson"),
    CreateInvoiceItemResponse("responseJson"),
    GetInvoiceInsightResponse("responseJson"),
    GetInvoiceMiniInsightResponse("responseJson"),
    GetInvoiceReminderResponse("responseJson"),
    SubmitInvoiceResponse("responseJson"),
    PayInvoiceResponse("responseJson"),
    GetInvoiceNotificationResponse("responseJson"),
    CreateInvoiceReminderResponse("responseJson"),
    GetClaimDetailsResponse("responseJson"),
    SubmitBillDetailRequestSchema("requestJson"),
    SubmitBillTransferDetailRequestSchema("requestJson"),
    SubmitBillRequestSchema("requestJson"),
    ApproveBillRequestSchema("requestJson"),
    UpdateAccountingBillLineItem("requestJson"),
    ApproveXeroBillRequestSchema("requestJson"),
    GetPendingReviewXeroBillResponseSchema("responseJson"),
    UpdateAccountingBillLineItemResponseSchema("responseJson"),
    AddReportingFieldRequestSchema("requestJson"),
    FreezeCardRequestSchema("requestJson"),
    GetCardListResponse("responseJson"),
    FreezeCardResponse("responseJson"),
    DeactivateReportingFieldRequestSchema("requestJson"),
    GetCardDetailResponse("responseJson"),
    UpdateAccessBudgetSchema("requestJson"),
    UpdateAccessBudgetResponse("responseJson"),
    GetDebitCardTransactionSGDResponse("responseJson"),
    UpdateCardRequestSchema("requestJson"),
    UpdateCardResponse("responseJson");

    String requestOrResponseJson;

    SaasJsonDetails(String requestOrResponse)
    {
        requestOrResponseJson = requestOrResponse;
    }

    @Override
    public String getJsonFilePath(Config testConfig)
    {
        return System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "SaaS" + File.separator + "JsonFiles" + File.separator + "customer" + File.separator + requestOrResponseJson + File.separator + this + ".json";
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
