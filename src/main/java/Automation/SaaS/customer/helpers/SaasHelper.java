package Automation.SaaS.customer.helpers;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessEnums.RoleType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasEnums.*;
import Automation.SaaS.customer.web.*;
import Automation.Utils.Api.ApiDetails.Headers;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import Automation.Utils.Enums;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class SaasHelper extends ApiHelper
{

    public static SaasStaticDataBase saasStaticDataBase;
    public BudgetPage budgetPage;
    public PaymentLinksPage paymentLinksPage;
    public PaymentLinksCreatedPage paymentLinksCreatedPage;
    public PaymentLinksEditDetailsPage paymentLinksEditDetailsPage;
    public BillPage billPage;
    public AddNewBillPage addNewBillPage;
    public CardPage cardPage;
    public InvoicesPage invoicesPage;
    public AddNewInvoicePage addNewInvoicePage;
    public AddNewInvoiceItemsPage addNewInvoiceItemsPage;
    public ExportDataPage exportDataPage;
    public PreviewRecordPage previewRecordPage;
    public SelectInvoiceCustomerPage selectInvoiceCustomerPage;
    public XeroPage xeroPage;
    public SyncExpenseWithXeroPage syncExpenseWithXeroPage;
    public ExpenseDetailsPage expenseDetailsPage;

    public SaasHelper(Config testConfig, int... respectiveSheetRowNumbers)
    {
        super(testConfig, new String[]{"UserDetails"}, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public SaasHelper(Config testConfig, String[] sheets, int... respectiveSheetRowNumbers)
    {
        super(testConfig, sheets, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public SaasHelper(Config testConfig)
    {
        super(testConfig);
        initialiseStaticData();
    }

    public void initialiseStaticData()
    {
        if (Config.appLanguage != null)
        {
            AccessEnums.CustomerPortalLanguage.valueOf(Config.appLanguage);
            saasStaticDataBase = new SaasStaticDataEn();
        } else
        {
            saasStaticDataBase = new SaasStaticDataEn();
        }
    }

    public Response sendRequestAndGetResponse(SaasApiDetails saasApiDetails, SaasJsonDetails saasJsonDetails)
    {
        switch (saasApiDetails)
        {
            case GetBudgetsWithStatus:
                testConfig.putRunTimeProperty("page", 0);
                testConfig.putRunTimeProperty("limit", 10);
                testConfig.putRunTimeProperty("type", "owner");
                break;
            case UpdateBudget:
                String url = testConfig.getRunTimeProperty("budgetUuid");
                testConfig.putRunTimeProperty("updatedBudgetName", "Budget Name Updated " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(8, 2).trim());
                testConfig.putRunTimeProperty("updatedBudgetDescription", "Budget Description Updated " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(8, 2).trim());
                testConfig.putRunTimeProperty("updatedAmount", DataGenerator.generateRandomNumberInIntRange(1800, 3600));
                testConfig.putRunTimeProperty("updatedIcon", SaasEnums.BudgetIcon.getRandomIcon());
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, url), saasApiDetails.getApiRequestType(), saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails), getJsonData(saasJsonDetails));
            case DeactivateBudget:
                break;
            case ReactivateBudget:
                String budgetUuid = testConfig.getRunTimeProperty("budgetUuid");
                testConfig.putRunTimeProperty("reactivateBudgetUuid", budgetUuid);
                testConfig.putRunTimeProperty("reactivateBudgetBudgetName", "Budget Name " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(8, 2).trim());
                testConfig.putRunTimeProperty("reactivateBudgetAmount", DataGenerator.generateRandomNumberInIntRange(1800, 3600));
                testConfig.putRunTimeProperty("reactivateBudgetIcon", SaasEnums.BudgetIcon.getRandomIcon());
                testConfig.putRunTimeProperty("isAlert", "false");
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, budgetUuid), saasApiDetails.getApiRequestType(), saasApiDetails.getApiContentType(), getHeaders(saasApiDetails),
                        getParams(saasApiDetails), getFormData(saasApiDetails), getJsonData(saasJsonDetails));
            case GetPeopleFromBudget:
            case GetPeopleFromBudgetWithoutAuthorizations:
            case GetPeopleFromBudgetWithoutHeaders:
            case AddOwnerOrMemberToBudget:
            case RemoveOwnerOrMemberFromBudget:
                url = testConfig.getRunTimeProperty("budgetUuid");
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, url), saasApiDetails.getApiRequestType(), saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails), getJsonData(saasJsonDetails));
            case GetFileUrl:
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, testConfig.getRunTimeProperty("fileUuid")), saasApiDetails.getApiRequestType(), saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails), getJsonData(saasJsonDetails));
            case PutFileUrl:
                testConfig.putRunTimeProperty("source_uuid", testConfig.getRunTimeProperty("claimUuid"));
                testConfig.putRunTimeProperty("source_type", "claim");
                testConfig.putRunTimeProperty("type", "claim");
                testConfig.putRunTimeProperty("source_collection", "claim");
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, testConfig.getRunTimeProperty("fileUuid")), saasApiDetails.getApiRequestType(), saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails), getJsonData(saasJsonDetails));
            case GetClaimDetails:
                saasApiDetails.setFieldsParams("uuid", "target_currency_code", "target_amount", "source_currency_code", "source_amount", "exchange_rate", "number", "category.uuid", "category.icon", "category.name", "merchant_name", "transaction_date", "created_at", "purpose_of_spend", "state_code", "debit_account.uuid", "budget.uuid", "budget.icon", "budget.name", "budget.state", "approvers.role", "approvers.full_name", "reject_reason", "create_person.uuid", "create_person.full_name", "approve_person.full_name", "approve_person.uuid", "paid_person.uuid", "paid_person.full_name", "reject_person.uuid", "reject_person.full_name", "timeline.event", "timeline.event_at", "timeline.event_by", "timeline.event_by_uuid", "timeline.event_details", "budget.auth_person_budget_role", "authorizations.approve", "authorizations.delete", "authorizations.pay", "authorizations.reject", "authorizations.resubmit", "authorizations.update", "authorizations.view", "budget.amount", "budget.spent_amount",
                        "debit_transactions.uuid", "debit_transactions.type");
                break;
            case ApproveClaim:
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, testConfig.getRunTimeProperty("claimUuid")), saasApiDetails.getApiRequestType(),
                        saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails),
                        getJsonData(saasJsonDetails));
            case PayClaim, ScheduleClaimPayment:
                testConfig.putRunTimeProperty(Headers.idempotencyKey.getValue(), DataGenerator.generateRandomGuid());
                setupPayClaimData(saasApiDetails);
                break;
            case StopScheduledClaimPayment:
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig, testConfig.getRunTimeProperty("transferUuid")), saasApiDetails.getApiRequestType(),
                        saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails),
                        getJsonData(saasJsonDetails));
            case BillSubmission:
                testConfig.putRunTimeProperty("source_uuid", testConfig.getRunTimeProperty("business_uuid"));
                testConfig.putRunTimeProperty("bill_number", "INV - " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(4, 2).trim());
                testConfig.putRunTimeProperty("currency_code", testConfig.getRunTimeProperty("currencyCode"));
                testConfig.putRunTimeProperty("amount", 5000);
                testConfig.putRunTimeProperty("unit_price1", 2000);
                testConfig.putRunTimeProperty("unit_price2", 3000);
                break;
            case UpdateXeroBillLineItem:
                String[] strArray = testConfig.getRunTimeProperty("bill_line_item_uuid").replaceAll("[]\\[\\\\]+", "").split(",");
                testConfig.putRunTimeProperty("uuid1", strArray[0]);
                testConfig.putRunTimeProperty("uuid2", strArray[1]);
                testConfig.putRunTimeProperty("source_uuid", testConfig.getRunTimeProperty("bill_uuid"));
                break;
            case GetPendingReviewXeroBill:
                testConfig.putRunTimeProperty(Headers.xexampleBusinessUuid.getValue(), testConfig.getRunTimeProperty("business_uuid"));
                break;
            case AddReportingField:
                testConfig.putRunTimeProperty("reportingFieldName", "test name - " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(4, 2).trim());
                testConfig.putRunTimeProperty("reportingFieldIcon", "default");
                testConfig.putRunTimeProperty("reportingFieldType", ReportingFileType.Text.getType());
                break;
            case DeactivateReportingField:
                testConfig.putRunTimeProperty("reporting_field_state", "deactivated");
                break;
            case GetCardList:
                testConfig.putRunTimeProperty("limit", "20");
                testConfig.putRunTimeProperty("page", "1");
                testConfig.putRunTimeProperty("_fields", "uuid");
                break;
            case FreezeCard:
                testConfig.putRunTimeProperty("action", "temporary-lock");
                break;
            case GetCardDetail:
                saasApiDetails.setFieldsParams("uuid", "card_name", "state", "state_code", "card_color", "description", "holder_name", "card_type", "physical_card_atm_withdrawals_limit_amount", "physical_card_atm_withdrawals_daily_limit_amount", "physical_card_atm_withdrawals_fee_amount", "current_spending", "physical_card_transaction_limit_amount", "maximum_over_consecutive_day", "spend_limit_frequency", "category.uuid", "card_expiry", "last_digit", "provider", "integration", "person.uuid", "card_frozen_at", "is_pin_set", "is_freezing_soon", "budget.name", "budget.uuid", "category.name");
                break;
            case PutUpdateAccessBudget:
                return executeRequestAndGetResponse(saasApiDetails.getUrl(testConfig), saasApiDetails.getApiRequestType(), saasApiDetails.getApiContentType(), getHeaders(saasApiDetails), getParams(saasApiDetails), getFormData(saasApiDetails), getJsonData(saasJsonDetails));
            case GetDebitCardTransaction:
                saasApiDetails.setFieldsParams("expected_date", "provider_transaction.created_at", "provider_transaction.state_code", "provider_transaction.details", "provider_transaction.state_logs", "quarantined_transaction.uuid", "quarantined_transaction.state_code", "internal_notes", "swift_receipt", "fx_transfer.type", "uuid", "type", "code", "note", "frequency", "description", "amount", "fee_amount", "currency_code",
                        "bank_name", "bank_type", "counterparty_uuid", "counterparty_name", "counterparty_number", "counterparty_code", "bank.bank_code", "counterparty_bank_code", "is_debit_card_transaction", "is_fx_transfer_transaction", "category.uuid", "category.icon", "category.color", "category.name", "files", "fx_transfer.uuid", "fx_transfer.state_code", "fx_transfer.bank_code", "person.uuid", "person.full_name", "debit_account.uuid", "debit_account.type", "debit_account.currency_code",
                        "debit_card", "debit_card_transaction", "debit_card_transaction.debit_card.uuid", "debit_card_transaction.debit_card.card_name", "debit_card_transaction.debit_card.description", "debit_card_transaction.debit_card.holder_name", "debit_card_transaction.debit_card.person.uuid", "debit_card_transaction.debit_card.last_digit", "debit_card_transaction.debit_card.expects_receipt", "debit_card_transaction.debit_card.provider", "debit_card_transaction.fees",
                        "debit_card_transaction.from_currency", "debit_card_transaction.to_currency", "debit_card_transaction.provider_transaction_amount", "debit_card_transaction.origin_debit_card_transaction.debit_transaction.uuid", "debit_card_transaction.origin_debit_card_transaction.debit_transaction.amount", "debit_card_transaction.reversal_debit_card_transaction.debit_transaction.amount"
                        , "last_receipt_reminder_sent_at", "last_receipt_reminder_type", "authorizations.send_receipt_reminder");
                break;
            case UpdateCard:
                testConfig.putRunTimeProperty("cardName", testConfig.getRunTimeProperty("cardName") + DataGenerator.generateRandomString(6));
                testConfig.putRunTimeProperty("cardDescription", testConfig.getRunTimeProperty("cardName") + DataGenerator.generateRandomString(6));
                testConfig.putRunTimeProperty("cardCategory", DebitCategory.getRandomCategory().getCategory());
                testConfig.putRunTimeProperty("cardColor", SaasEnums.CardColor.getRandomColor().getColor());
                testConfig.putRunTimeProperty("cardFrequency", CardFrequency.getRandomCardFrequency().getCardFrequency());
                testConfig.putRunTimeProperty("cardAmount", DataGenerator.generateRandomNumberInIntRange(1000, 2000));
                break;
            default:
                return executeRequestAndGetResponse(saasApiDetails, saasJsonDetails);
        }
        return executeRequestAndGetResponse(saasApiDetails, saasJsonDetails);
    }

    public void verifyApiResponse(Response response, SaasApiDetails saasApiDetails, SaasJsonDetails saasJsonDetails)
    {
        switch (saasApiDetails)
        {
            case CreateBudget:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.CreateBudgetResponse)
                {
                    verifyCreatedBudgetResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case UpdateBudget:
                switch (saasJsonDetails)
                {
                    case UpdateBudgetResponse -> verifyUpdatedBudgetResponse(response);
                    case UpdateBudgetResponseWithoutCreditTransaction ->
                            verifyUpdatedBudgetResponseWithoutCreditTransaction(response);
                    default -> testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case DeactivateBudget:
                verifyDeactivatedBudgetResponse(response);
                break;
            case ReactivateBudget:
                verifyReactivatedBudgetResponse(response);
                break;
            case GetPeopleFromBudget:
                switch (saasJsonDetails)
                {
                    case BudgetSingleOwnerResponse -> verifyPeopleFromBudgetWithSingleOwnerResponse(response);
                    case ErrorMessageResponse -> verifyErrorMessageResponse(response);
                    case BudgetMultipleOwnerResponse -> verifyPeopleFromBudgetWithMultipleOwnerResponse(response);
                    case BudgetMultipleOwnerAndMemberResponse ->
                            verifyPeopleFromBudgetWithMultipleOwnerAndMemberResponse(response);
                    case BudgetOwnerRevokedResponse -> verifyBudgetOwnerRevokedSuccessful(response);
                    case BudgetOwnerAndMemberRevokedResponse -> verifyBudgetOwnerAndMemberRevokedSuccessful(response);
                    case BudgetMultipleOwnerAndMemberResponseWithoutAdmin ->
                            verifyNonAdminBudgetOwnerAndMemberDataCorrect(response);
                    default -> testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case IssueNewCard:
                switch (saasJsonDetails)
                {
                    case IssueNewVirtualCardResponse -> verifyNewIssuedVirtualCardResponse(response);
                    case IssueNewPhysicalCardResponse -> verifyNewIssuedPhysicalCardResponse(response);
                    default -> testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case AddOwnerOrMemberToBudget:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.BudgetDetailResponse)
                {
                    verifyBudgetDetailResponseCorrect(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case GetClaimSourceOfFunds:
                verifyGetClaimSourceOfFunds(response);
                break;
            case DeletePaymentLink:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                AssertHelper.compareEquals(testConfig, "Verify response Json", "[]", response.body().asString());
                break;
            case ApproveXeroBill:
                verifyApproveXeroBill(response);
                break;
            case SyncXeroBill:
                verifySyncXeroBill(response);
                break;
            case CreatePaymentLink:
                verifyCreatedPaymentLinkResponse(response);
                break;
            case GetPaymentLink:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.GetPaymentLinkResponse)
                {
                    verifyGetPaymentLinkResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case CreatePaymentLinkNotifier:
                verifyCreatedPaymentLinkNotifierResponse(response);
                break;
            case UpdatePaymentLink:
                verifyUpdatedPaymentLinkResponse(response);
                break;
            case FreezeBudget:
                verifyFrozenBudgetDetailCorrect(response);
                break;
            case UnfreezeBudget:
                verifyUnfrozenBudgetDetailCorrect(response);
                break;
            case GetBudgetsWithStatus:
                verifyBudgetsResponseCorrect(response);
                break;
            case GetPeopleFromBudgetWithoutAuthorizations:
                verifyGetPeopleFromBudgetWithoutAuthorizationsResponse(response);
                break;
            case GetPeopleFromBudgetWithoutHeaders:
                verifyGetPeopleFromBudgetWithoutHeadersResponse(response);
                break;
            case GetCardList:
                verifyCardListResponse(response);
                break;
            case GetCardDetail:
                verifyCardDetailResponse(response);
                break;
            case FreezeCard:
                verifyFreezeCardResponseCorrect(response);
                break;
            case SubmitClaim, UpdateClaim:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.SubmitClaimResponse)
                {
                    verifySubmitClaimResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case RejectClaim:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                AssertHelper.compareEquals(testConfig, "Claim Uuid", testConfig.getRunTimeProperty("claimUuid"), response.jsonPath().getString("uuid"));
                break;
            case GetClaimDetails:
                verifyGetClaimDetailsResponse(response);
                break;
            case ApproveClaim:
                verifyApproveClaimResponse(response);
                break;
            case PayClaim:
                verifyPayClaimResponse(response);
                break;
            case ScheduleClaimPayment:
                verifyScheduledClaimPaymentResponse(response);
                break;
            case StopScheduledClaimPayment:
                switch (saasJsonDetails)
                {
                    case ScheduleClaimPaymentResponse -> verifyStopScheduledClaimPaymentResponse(response);
                    case UnauthorizedResponse ->
                    {
                        AssertHelper.compareEquals(testConfig, "Status Code", 403, response.statusCode());
                        AssertHelper.compareEquals(testConfig, "Error Message", "This action is unauthorized.", response.jsonPath().getString("message"));
                    }
                    default -> testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case GetFileUrl:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.GetFileResponse)
                {
                    verifyGetFileResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case UploadFile:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.UploadReceiptResponse)
                {
                    verifyUploadReceiptResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case PutFileUrl:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.AttachDocumentResponse)
                {
                    verifyAttachDocumentResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case CreateInvoiceCounterParty:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.CreateInvoiceCounterPartyResponse)
                {
                    verifyCreateInvoiceCounterPartyResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case GetInvoiceCounterParty:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.GetInvoiceCounterPartyResponse)
                {
                    verifyGetInvoiceCounterPartyResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case UpdateInvoiceCounterParty:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.UpdateInvoiceCounterPartyResponse)
                {
                    verifyUpdateInvoiceCounterPartyResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case DeleteInvoiceCounterParty:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                AssertHelper.compareEquals(testConfig, "Verify response body", "[]", response.body().asString());
                break;
            case CreateInvoice:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.CreateInvoiceResponse)
                {
                    verifyCreateInvoiceResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case PreviewInvoice:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.PreviewInvoiceResponse)
                {
                    verifyPreviewInvoiceResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case GetInvoiceList:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.GetInvoiceListResponse)
                {
                    verifyGetInvoiceListResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case CreateInvoiceItem:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.CreateInvoiceItemResponse)
                {
                    verifyCreateInvoiceItemResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case DeleteInvoice:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                AssertHelper.compareEquals(testConfig, "Verify delete response body", "[]", response.body().asString());
                break;
            case GetInvoiceMiniInsight:
                if (Objects.requireNonNull(saasJsonDetails) == SaasJsonDetails.GetInvoiceMiniInsightResponse)
                {
                    verifyGetInvoiceMiniInsightResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + saasJsonDetails.name());
                }
                break;
            case GetDetailXeroBill:
                verifyTotalLineItemCorrect(response);
                break;
            case BillSubmission:
                verifyBillSubmission(response);
                break;
            case SaveBillTransferDetail:
                verifySaveBillTransferDetail(response);
                break;
            case SubmitBill:
                verifySubmitBill(response);
                break;
            case ApproveBill:
                verifyApproveBill(response);
                break;
            case GetPendingReviewXeroBill:
                verifyGetPendingReviewXeroBill(response);
                break;
            case GetXeroBillLineItem:
                verifyGetXeroBillLineItem(response);
                break;
            case UpdateXeroBillLineItem:
                verifyUpdateXeroBillLineItem(response);
                break;
            case AddReportingField:
                verifyAddReportingFieldDetailResponse(response);
                break;
            case PutUpdateAccessBudget:
                verifyBudgetUserAccess(response);
                break;
            case GetDetailReportingField:
                verifyGetDetailReportingField(response);
                break;
            case GetXeroAccount:
                testConfig.putRunTimeProperty("accounting_uuid1", response.jsonPath().getString("[0].account_id"));
                testConfig.putRunTimeProperty("accounting_uuid2", response.jsonPath().getString("[1].account_id"));
                break;
            case GetDebitCardTransaction:
                verifyGetDebitCardTransactionField(response);
                break;
            case UpdateCard:
                verifyUpdateCardResponse(response);
                break;
            default:
                testConfig.logFail("Key-values are not being verified for API - " + saasApiDetails.name());
        }
        if (saasJsonDetails != null)
        {
            verifyJsonResponse(response, saasApiDetails, saasApiDetails.name(), saasJsonDetails, saasJsonDetails.name());
        }
    }

    private void verifyUpdateCardResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Card color", testConfig.getRunTimeProperty("cardColor"), response.jsonPath().getString("card_color"));
        AssertHelper.compareEquals(testConfig, "Card type", testConfig.getRunTimeProperty("cardType"), response.jsonPath().getString("card_type"));
        AssertHelper.compareEquals(testConfig, "Card description", testConfig.getRunTimeProperty("cardDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Card name", testConfig.getRunTimeProperty("cardName"), response.jsonPath().getString("card_name"));
        AssertHelper.compareEquals(testConfig, "Card frequency", testConfig.getRunTimeProperty("cardFrequency"), response.jsonPath().getString("spend_limit_frequency"));
        AssertHelper.compareEquals(testConfig, "Card category", testConfig.getRunTimeProperty("cardCategory"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Card maximum amount", testConfig.getRunTimeProperty("cardAmount"), response.jsonPath().getString("maximum_over_consecutive_day"));
        AssertHelper.compareEquals(testConfig, "Card status", "active", response.jsonPath().getString("state.state"));
    }

    private void verifyReactivatedBudgetResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Reactivated Budget uuid", testConfig.getRunTimeProperty("reactivateBudgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Reactivated budget status", "BDAC", response.jsonPath().getString("state"));
        AssertHelper.compareEquals(testConfig, "Reactivated budget amount", testConfig.getRunTimeProperty("reactivateBudgetAmount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Reactivated Budget name", testConfig.getRunTimeProperty("reactivateBudgetBudgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Reactivated Budget icon", testConfig.getRunTimeProperty("reactivateBudgetIcon"), response.jsonPath().getString("icon"));
        AssertHelper.compareEquals(testConfig, "Limit alert notification", testConfig.getRunTimeProperty("isAlert"), response.jsonPath().getString("wants_limit_alert_notification"));
    }

    private void verifyDeactivatedBudgetResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget uuid", testConfig.getRunTimeProperty("budgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Deactivated budget status", "BDDA", response.jsonPath().getString("state"));
    }

    private void verifyGetDebitCardTransactionField(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Card Transaction Uuid", testConfig.getRunTimeProperty("cardTransactionUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Card Uuid", testConfig.getRunTimeProperty("cardUuid"), response.jsonPath().getString("debit_card_transaction.debit_card.uuid"));
        AssertHelper.compareEquals(testConfig, "Card Transaction Type", testConfig.getRunTimeProperty("transactionType"), response.jsonPath().getString("type").toUpperCase());
        AssertHelper.compareEquals(testConfig, "Card Transaction Currency", testConfig.getRunTimeProperty("transactionCurrencyCode"), response.jsonPath().getString("currency_code"));
        if (testConfig.getRunTimeProperty("transactionCurrencyCode") == CurrencyEnum.SGD.toString())
        {
            AssertHelper.compareEquals(testConfig, "Card Transaction Amount", testConfig.getRunTimeProperty("billingAmount"), String.valueOf((response.jsonPath().getString("amount")).charAt(1)));
        } else
        {
            AssertHelper.compareEquals(testConfig, "Card Transaction Amount", testConfig.getRunTimeProperty("transactionAmount"), String.valueOf((response.jsonPath().getString("amount")).charAt(1)));
        }
        AssertHelper.compareEquals(testConfig, "Card Transaction Description", testConfig.getRunTimeProperty("merchantNameLocation"), response.jsonPath().getString("description"));
    }

    private void verifyStopScheduledClaimPaymentResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "Transfer Uuid", testConfig.getRunTimeProperty("transferUuid"));
        AssertHelper.compareEquals(testConfig, "Amount", testConfig.getRunTimeProperty("sourceAmount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Category", testConfig.getRunTimeProperty("categoryName"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Currency Code", testConfig.getRunTimeProperty("currencyCode"), response.jsonPath().getString("currency_code"));
        AssertHelper.compareEquals(testConfig, "State", "stopped", response.jsonPath().getString("state.state"));
        AssertHelper.compareEquals(testConfig, "Reference Code", "DPSP", response.jsonPath().getString("state.reference_code"));
        AssertHelper.compareEquals(testConfig, "Description", String.format("Claim no. %s", testConfig.getRunTimeProperty("claimNumber")), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Internal Note", testConfig.getRunTimeProperty("internalNotes"), response.jsonPath().getString("internal_notes"));
        AssertHelper.compareEquals(testConfig, "Next Payment Date", testConfig.getRunTimeProperty("nextPaymentDate"), response.jsonPath().getString("next_payment_date"));
        AssertHelper.assertNotNull(testConfig, "Stopped at", response.jsonPath().get("stopped_at"));
        AssertHelper.compareEquals(testConfig, "Number of transaction", 0, new ArrayList<>(response.jsonPath().get("transactions")).size());
        AssertHelper.compareEquals(testConfig, "Counterparty Name", testConfig.getRunTimeProperty("recipientAccountName"), response.jsonPath().getString("counterparty_name"));
        AssertHelper.compareEquals(testConfig, "Counterparty Code", testConfig.getRunTimeProperty("recipientAccountNumber"), response.jsonPath().getString("counterparty_code"));
        AssertHelper.compareEquals(testConfig, "Counterparty Bank Code", testConfig.getRunTimeProperty("recipientBankCode"), response.jsonPath().getString("counterparty_bank_code"));
        AssertHelper.compareEquals(testConfig, "Auth Is Update", true, response.jsonPath().get("authorizations.update"));
        AssertHelper.compareEquals(testConfig, "Auth Is Stop", false, response.jsonPath().get("authorizations.stop"));
        AssertHelper.compareEquals(testConfig, "Auth Is Resume", true, response.jsonPath().get("authorizations.resume"));
        AssertHelper.compareEquals(testConfig, "Auth Is Pause", false, response.jsonPath().get("authorizations.pause"));
    }

    private void verifyScheduledClaimPaymentResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "Transfer Uuid", response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Amount", testConfig.getRunTimeProperty("sourceAmount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Category", testConfig.getRunTimeProperty("categoryName"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Currency Code", testConfig.getRunTimeProperty("currencyCode"), response.jsonPath().getString("currency_code"));
        AssertHelper.compareEquals(testConfig, "State", "active", response.jsonPath().getString("state.state"));
        AssertHelper.compareEquals(testConfig, "Reference Code", "DPAC", response.jsonPath().getString("state.reference_code"));
        AssertHelper.compareEquals(testConfig, "Description", String.format("Claim no. %s", testConfig.getRunTimeProperty("claimNumber")), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Internal Note", testConfig.getRunTimeProperty("internalNotes"), response.jsonPath().getString("internal_notes"));
        AssertHelper.compareEquals(testConfig, "Next Payment Date", testConfig.getRunTimeProperty("nextPaymentDate"), response.jsonPath().getString("next_payment_date"));
        AssertHelper.compareEquals(testConfig, "Counterparty Name", testConfig.getRunTimeProperty("recipientAccountName"), response.jsonPath().getString("counterparty_name"));
        AssertHelper.compareEquals(testConfig, "Counterparty Code", testConfig.getRunTimeProperty("recipientAccountNumber"), response.jsonPath().getString("counterparty_code"));
        AssertHelper.compareEquals(testConfig, "Counterparty Bank Code", testConfig.getRunTimeProperty("recipientBankCode"), response.jsonPath().getString("counterparty_bank_code"));
        AssertHelper.compareEquals(testConfig, "Auth Is Update", true, response.jsonPath().get("authorizations.update"));
        AssertHelper.compareEquals(testConfig, "Auth Is Stop", true, response.jsonPath().get("authorizations.stop"));
        AssertHelper.compareEquals(testConfig, "Auth Is Resume", false, response.jsonPath().get("authorizations.resume"));
        AssertHelper.compareEquals(testConfig, "Auth Is Pause", true, response.jsonPath().get("authorizations.pause"));

        testConfig.putRunTimeProperty("transferUuid", response.jsonPath().getString("uuid"));
    }

    private void verifyGetInvoiceMiniInsightResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify total_overdue amount", 0, response.jsonPath().getInt("mini_insights.total_overdue.amount"));
        AssertHelper.compareEquals(testConfig, "Verify total_overdue currency_code", testConfig.testData.get("currencyCode"), response.jsonPath().getString("mini_insights.total_overdue.currency_code"));
        AssertHelper.compareEquals(testConfig, "Verify total_due amount", 0, response.jsonPath().getInt("mini_insights.total_due.amount"));
        AssertHelper.compareEquals(testConfig, "Verify total_due currency_code", testConfig.testData.get("currencyCode"), response.jsonPath().getString("mini_insights.total_due.currency_code"));
        AssertHelper.compareEquals(testConfig, "Verify total_collected amount", 99, response.jsonPath().getInt("mini_insights.total_collected.amount"));
        AssertHelper.compareEquals(testConfig, "Verify total_collected currency_code", testConfig.testData.get("currencyCode"), response.jsonPath().getString("mini_insights.total_collected.currency_code"));
        AssertHelper.compareEquals(testConfig, "Verify total_outstanding amount", 0, response.jsonPath().getInt("mini_insights.total_outstanding.amount"));
        AssertHelper.compareEquals(testConfig, "Verify total_outstanding currency_code", testConfig.testData.get("currencyCode"), response.jsonPath().getString("mini_insights.total_outstanding.currency_code"));
    }

    private void verifyPreviewInvoiceResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify invoiceNumber", testConfig.testData.get("invoiceNumber"), response.jsonPath().getString("invoice_number"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceDescription", testConfig.testData.get("invoiceDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceCurrency", testConfig.testData.get("invoiceCurrency"), response.jsonPath().getString("currency"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceAmount", testConfig.testData.get("invoiceAmount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceTaxRate", testConfig.testData.get("invoiceTaxRate"), response.jsonPath().getString("tax_rate"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceTaxName", testConfig.testData.get("invoiceTaxName"), response.jsonPath().getString("tax_name"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceTaxAmount", testConfig.testData.get("invoiceTaxAmount"), response.jsonPath().getString("tax_amount"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceTotalAmount", testConfig.testData.get("invoiceTotalAmount"), response.jsonPath().getString("total_amount"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceDueDate", testConfig.testData.get("invoiceDueDate"), response.jsonPath().getString("due_date"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceStatus", testConfig.testData.get("invoiceStatus"), response.jsonPath().getString("status"));
        AssertHelper.compareEquals(testConfig, "Verify invoicePaymentStatus", testConfig.testData.get("invoicePaymentStatus"), response.jsonPath().getString("payment_status"));
        AssertHelper.compareEquals(testConfig, "Verify invoicePaymentDate", testConfig.testData.get("invoicePaymentDate"), response.jsonPath().getString("payment_date"));
        AssertHelper.compareEquals(testConfig, "Verify invoicePaymentMode", testConfig.testData.get("invoicePaymentMode"), response.jsonPath().getString("payment_mode"));
        AssertHelper.compareEquals(testConfig, "Verify invoicePaymentReference", testConfig.testData.get("invoicePaymentReference"), response.jsonPath().getString("payment_reference"));
        AssertHelper.compareEquals(testConfig, "Verify invoicePaymentAmount", testConfig.testData.get("invoicePaymentAmount"), response.jsonPath().getString("payment_amount"));
    }

    private void verifyCreateInvoiceItemResponse(Response response)
    {
        int invoiceItemUnitCost = Integer.parseInt(testConfig.testData.get("invoiceItemUnitCost"));
        int invoiceItemQuantity = Integer.parseInt(testConfig.testData.get("invoiceItemQuantity"));
        float invoiceItemDiscountPercentage = Float.parseFloat(testConfig.testData.get("invoiceItemDiscountPercentage"));
        float invoiceItemTaxRate = Float.parseFloat(testConfig.testData.get("invoiceItemTaxRate"));
        float tax = invoiceItemTaxRate * ((invoiceItemUnitCost * invoiceItemQuantity) - (invoiceItemUnitCost * invoiceItemQuantity * invoiceItemDiscountPercentage));
        float totalAmount = tax + (invoiceItemUnitCost * invoiceItemQuantity) - (invoiceItemUnitCost * invoiceItemQuantity * invoiceItemDiscountPercentage);
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify invoice.number", testConfig.getRunTimeProperty("invoiceNumber"), response.jsonPath().getString("invoice.number"));
        AssertHelper.compareEquals(testConfig, "Verify invoice.subtotal", invoiceItemUnitCost * invoiceItemQuantity, response.jsonPath().getInt("invoice.subtotal"));
        AssertHelper.compareEquals(testConfig, "Verify invoice.discount", ((float) invoiceItemUnitCost * invoiceItemQuantity * invoiceItemDiscountPercentage), response.jsonPath().getFloat("invoice.discount"));
        AssertHelper.compareEquals(testConfig, "Verify invoice.tax", tax, response.jsonPath().getFloat("invoice.tax"));
        AssertHelper.compareEquals(testConfig, "Verify invoice.total_amount", totalAmount, response.jsonPath().getFloat("invoice.total_amount"));
        AssertHelper.compareEquals(testConfig, "Verify invoice.invoice_items_count", 1, response.jsonPath().getInt("invoice.invoice_items_count"));
        AssertHelper.compareTrue(testConfig, "Verify currency.decimals", response.jsonPath().getString("currency.decimals").equals("2"));
        AssertHelper.assertNotNull(testConfig, "Verify uuid", response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Verify amount", totalAmount, response.jsonPath().getFloat("amount"));
    }

    private void verifyGetInvoiceListResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        //verify list of json object in data list
        AssertHelper.compareTrue(testConfig, "Verify data list size", response.jsonPath().getList("data").size() > 1);
        //verify all the fields in data list
        AssertHelper.compareEquals(testConfig, "Verify invoiceNumber", testConfig.testData.get("invoiceNumber"), response.jsonPath().getString("data[0].invoice_number"));
    }

    private void verifyCreateInvoiceResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify invoiceCounterpartyName", testConfig.testData.get("counterPartyName"), response.jsonPath().getString("invoice_counterparty.name"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceCounterpartyEmail", testConfig.testData.get("counterPartyEmail"), response.jsonPath().getString("invoice_counterparty.email"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceItems", testConfig.testData.get("invoiceItems"), response.jsonPath().getString("invoice_items"));
        AssertHelper.assertNotNull(testConfig, "Verify uuid", response.jsonPath().getString("uuid"));
        AssertHelper.assertNotNull(testConfig, "Verify number", response.jsonPath().getString("number"));
        AssertHelper.compareEquals(testConfig, "Verify termsAndConditions", testConfig.testData.get("termsAndConditions"), response.jsonPath().getString("terms_and_conditions"));
        AssertHelper.compareEquals(testConfig, "Verify subtotal", testConfig.testData.get("subtotal"), response.jsonPath().getString("subtotal"));
        AssertHelper.compareEquals(testConfig, "Verify discount", testConfig.testData.get("discount"), response.jsonPath().getString("discount"));
        AssertHelper.compareEquals(testConfig, "Verify tax", testConfig.testData.get("tax"), response.jsonPath().getString("tax"));
        AssertHelper.compareEquals(testConfig, "Verify totalAmount", testConfig.testData.get("totalAmount"), response.jsonPath().getString("total_amount"));
        AssertHelper.compareEquals(testConfig, "Verify currencyCode", testConfig.testData.get("currencyCode"), response.jsonPath().getString("currency_code"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceItemsCount", testConfig.testData.get("invoiceItemsCount"), response.jsonPath().getString("invoice_items_count"));
        AssertHelper.compareEquals(testConfig, "Verify invoiceFileType", testConfig.testData.get("invoiceFileType"), response.jsonPath().getString("invoice_file_type"));
        AssertHelper.assertNotNull(testConfig, "Verify paymentLink", response.jsonPath().getString("payment_link"));
        AssertHelper.assertNotNull(testConfig, "Verify documentName", response.jsonPath().getString("document_name").contains(response.jsonPath().getString("number")));
        AssertHelper.assertNotNull(testConfig, "Verify documentName extension", response.jsonPath().getString("document_name").contains(".pdf"));
        AssertHelper.compareEquals(testConfig, "Verify authorizationsUpdate", testConfig.testData.get("authorizationsUpdate"), response.jsonPath().getString("authorizations.update"));
        AssertHelper.compareEquals(testConfig, "Verify authorizationsSubmit", testConfig.testData.get("authorizationsSubmit"), response.jsonPath().getString("authorizations.submit"));
        AssertHelper.compareEquals(testConfig, "Verify authorizationsDelete", testConfig.testData.get("authorizationsDelete"), response.jsonPath().getString("authorizations.delete"));
        testConfig.putRunTimeProperty("invoiceUuid", response.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("invoiceNumber", response.jsonPath().getString("number"));
    }

    private void verifyGetInvoiceCounterPartyResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject jsonObject = new JSONObject(response.asString());
        AssertHelper.compareEquals(testConfig, "Verify type", testConfig.testData.get("counterPartyType"), jsonObject.getString("type"));
        AssertHelper.compareEquals(testConfig, "Verify email", testConfig.testData.get("counterPartyEmail"), jsonObject.getString("email"));
        AssertHelper.compareEquals(testConfig, "Verify name", testConfig.testData.get("counterPartyName"), jsonObject.getString("name"));
        AssertHelper.compareEquals(testConfig, "Verify uuid", testConfig.getRunTimeProperty("invoiceCounterPartyUuid"), jsonObject.getString("uuid"));
    }

    private void verifyUpdateInvoiceCounterPartyResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject jsonObject = new JSONObject(response.asString());
        AssertHelper.compareEquals(testConfig, "Verify type", testConfig.testData.get("counterPartyType"), jsonObject.getString("type"));
        AssertHelper.compareEquals(testConfig, "Verify email", testConfig.getRunTimeProperty("updatedCounterPartyEmail"), jsonObject.getString("email"));
        AssertHelper.compareEquals(testConfig, "Verify name", testConfig.getRunTimeProperty("updatedCounterPartyName"), jsonObject.getString("name"));
        AssertHelper.compareEquals(testConfig, "Verify uuid", testConfig.getRunTimeProperty("invoiceCounterPartyUuid"), jsonObject.getString("uuid"));
    }

    private void verifyGetInvoiceCounterPartyResponseList(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject jsonObject = new JSONObject(response.asString());
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        boolean found = false;
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject dataObject = jsonArray.getJSONObject(i);
            if (dataObject.getString("uuid").equals(testConfig.getRunTimeProperty("invoiceCounterPartyUuid")))
            {
                found = true;
                AssertHelper.compareEquals(testConfig, "Verify type", testConfig.testData.get("counterPartyType"), dataObject.getString("type"));
                AssertHelper.compareEquals(testConfig, "Verify email", testConfig.testData.get("counterPartyEmail"), dataObject.getString("email"));
                AssertHelper.compareEquals(testConfig, "Verify name", testConfig.testData.get("counterPartyName"), dataObject.getString("name"));
                AssertHelper.assertNotNull(testConfig, "Verify uuid", dataObject.getString("uuid"));
            }
        }
        AssertHelper.compareTrue(testConfig, "Verify object exists in data list", found);
    }

    private void verifyCreateInvoiceCounterPartyResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        JSONObject jsonObject = new JSONObject(response.asString());
        AssertHelper.compareEquals(testConfig, "Verify type", testConfig.testData.get("counterPartyType"), jsonObject.getString("type"));
        AssertHelper.compareEquals(testConfig, "Verify email", testConfig.testData.get("counterPartyEmail"), jsonObject.getString("email"));
        AssertHelper.compareEquals(testConfig, "Verify name", testConfig.testData.get("counterPartyName"), jsonObject.getString("name"));
        AssertHelper.assertNotNull(testConfig, "Verify uuid", jsonObject.getString("uuid"));
        testConfig.putRunTimeProperty("invoiceCounterPartyUuid", jsonObject.getString("uuid"));
    }

    private void verifyPayClaimResponse(Response response)
    {
        int fromAmount = Integer.parseInt(response.jsonPath().getString("transaction.balance.from_amount"));
        int toAmount = Integer.parseInt(response.jsonPath().getString("transaction.balance.to_amount"));

        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Status Body", "OK", response.jsonPath().getString("status"));
        AssertHelper.assertNotNull(testConfig, "Transaction Id", response.jsonPath().getString("transaction_id"));
        AssertHelper.assertNotNull(testConfig, "Transaction Uuid", response.jsonPath().getString("transaction.uuid"));
        AssertHelper.compareEquals(testConfig, "Fee Amount", 0, Integer.parseInt(response.jsonPath().getString("transaction.fee_amount")));
        AssertHelper.compareEquals(testConfig, "Amount Payment", fromAmount - toAmount, Integer.parseInt(testConfig.getRunTimeProperty("sourceAmount")));
    }

    private void verifyApproveClaimResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Claim State", ClaimState.PendingPayment.getCode(), response.jsonPath().getString("state_code"));
        AssertHelper.compareEquals(testConfig, "Claim Uuid", testConfig.getRunTimeProperty("claimUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Merchant Name", testConfig.getRunTimeProperty("merchantName"), response.jsonPath().getString("merchant_name"));
        AssertHelper.compareEquals(testConfig, "Target Amount", testConfig.getRunTimeProperty("targetAmount"), response.jsonPath().getString("target_amount"));
        AssertHelper.compareEquals(testConfig, "Target Currency Code", testConfig.getRunTimeProperty("currencyCode"), response.jsonPath().getString("target_currency_code"));
        AssertHelper.compareEquals(testConfig, "Purpose Of Spend", testConfig.getRunTimeProperty("purposeOfSpend"), response.jsonPath().getString("purpose_of_spend"));
    }

    private void verifyCardDetailResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response code", 200, response.getStatusCode());
        AssertHelper.compareEquals(testConfig, "Card Uuid", testConfig.getRunTimeProperty("cardUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Card color", testConfig.getRunTimeProperty("cardColor"), response.jsonPath().getString("card_color"));
        AssertHelper.compareEquals(testConfig, "Card type", testConfig.getRunTimeProperty("cardType"), response.jsonPath().getString("card_type"));
        AssertHelper.compareEquals(testConfig, "Card description", testConfig.getRunTimeProperty("cardDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Card name", testConfig.getRunTimeProperty("cardName"), response.jsonPath().getString("card_name"));
        AssertHelper.compareEquals(testConfig, "Card frequency", testConfig.getRunTimeProperty("cardFrequency"), response.jsonPath().getString("spend_limit_frequency"));
        AssertHelper.compareEquals(testConfig, "Card category", testConfig.getRunTimeProperty("cardCategory"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Card maximum amount", testConfig.getRunTimeProperty("cardAmount"), response.jsonPath().getString("maximum_over_consecutive_day"));
        AssertHelper.compareEquals(testConfig, "Card status", "active", response.jsonPath().getString("state.state"));
        testConfig.putRunTimeProperty("cardHashId", response.jsonPath().getString("integration.external_id"));
    }

    private void verifyAddReportingFieldDetailResponse(Response response)
    {
        testConfig.putRunTimeProperty("field_uuid", response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify reporting field name", testConfig.getRunTimeProperty("reportingFieldName").trim(), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Verify reporting field icon", testConfig.getRunTimeProperty("reportingFieldIcon").trim(), response.jsonPath().getString("icon"));
        AssertHelper.compareEquals(testConfig, "Verify reporting field type", testConfig.getRunTimeProperty("reportingFieldType").trim(), response.jsonPath().getString("field_type"));
    }

    private void verifyGetDetailReportingField(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify reporting field uuid", testConfig.getRunTimeProperty("field_uuid").trim(), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Verify reporting field state", testConfig.getRunTimeProperty("reporting_field_state").trim(), response.jsonPath().getString("state.state"));
    }

    private void verifyGetClaimDetailsResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Claim State", testConfig.getRunTimeProperty("claimState"), response.jsonPath().getString("state_code"));
        AssertHelper.compareEquals(testConfig, "Claim Uuid", testConfig.getRunTimeProperty("claimUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Merchant Name", testConfig.getRunTimeProperty("merchantName"), response.jsonPath().getString("merchant_name"));
        AssertHelper.compareEquals(testConfig, "Target Amount", testConfig.getRunTimeProperty("targetAmount"), response.jsonPath().getString("target_amount"));
        AssertHelper.compareEquals(testConfig, "Target Currency Code", testConfig.getRunTimeProperty("currencyCode"), response.jsonPath().getString("target_currency_code"));
        AssertHelper.compareEquals(testConfig, "Category", testConfig.getRunTimeProperty("categoryName"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Purpose Of Spend", testConfig.getRunTimeProperty("purposeOfSpend"), response.jsonPath().getString("purpose_of_spend"));
    }

    private void verifyGetClaimSourceOfFunds(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONArray items = new JSONArray(response.asString());
        AssertHelper.compareTrue(testConfig, "Verify response Json is not empty", !items.isEmpty());
        for (int i = 0; i < items.length(); i++)
        {
            JSONObject item = items.getJSONObject(i);
            AssertHelper.compareTrue(testConfig, "Verify element contains `uuid` key", item.has("uuid"));
            AssertHelper.compareTrue(testConfig, "Verify element contains `model_type` key", item.has("model_type"));
            AssertHelper.compareTrue(testConfig, "Verify element contains `name` key", item.has("name"));
            AssertHelper.compareTrue(testConfig, "Verify element contains `approvers` key", item.has("approvers"));
            AssertHelper.compareTrue(testConfig, "Verify element contains `icon` key", item.has("icon"));
        }
    }

    public void createBudget(AccessHelper accessHelper, SaasJsonDetails saasJsonDetails, RoleType... roleType)
    {
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfEmployees, null);
        accessHelper.verifyApiResponse(response, AccessApiDetails.GetListOfEmployees, AccessJsonDetails.EmployeesListSuccessfulResponse);

        testConfig.putRunTimeProperty("budgetName", "Budget Name " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(10, 3).trim());
        testConfig.putRunTimeProperty("budgetDescription", "Budget Description " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(10, 3));
        testConfig.putRunTimeProperty("icon", SaasEnums.BudgetIcon.getRandomIcon());
        testConfig.putRunTimeProperty("frequency", SaasEnums.BudgetApiFrequency.getRandomBudgetFrequency().getBudgetFrequency());
        testConfig.putRunTimeProperty("inviterId", getInviterUuid(getJsonArrayFromResponse(response), testConfig.getRunTimeProperty("fullName")));
        testConfig.putRunTimeProperty("amount", DataGenerator.generateRandomNumberInIntRange(1000, 3000));

        if (roleType.length == 1)
        {
            testConfig.putRunTimeProperty("inviteeId", getInviteeUuid(getJsonArrayFromResponse(response), roleType[0]));
            testConfig.putRunTimeProperty("inviteeName", getInviteeName(getJsonArrayFromResponse(response), roleType[0]));
            testConfig.putRunTimeProperty("budgetOwnerRole", roleType[0].getType());
        } else if (roleType.length == 2)
        {
            testConfig.putRunTimeProperty("firstBudgetOwnerId", getInviteeUuid(getJsonArrayFromResponse(response), roleType[0]));
            testConfig.putRunTimeProperty("secondBudgetOwnerId", getInviteeUuid(getJsonArrayFromResponse(response), roleType[1]));
        } else
        {
            testConfig.logFail("RoleType not supported");
        }
        Response createBudgetResponse = sendRequestAndGetResponse(SaasApiDetails.CreateBudget, saasJsonDetails);
        verifyApiResponse(createBudgetResponse, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);
    }

    public void assignNewPersonToBudget(AccessHelper accessHelper, AccessEnums.RoleType roleType, SaasEnums.BudgetMemberType budgetMemberType, boolean canTransfer)
    {
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfEmployees, null);
        accessHelper.verifyApiResponse(response, AccessApiDetails.GetListOfEmployees, AccessJsonDetails.EmployeesListSuccessfulResponse);

        testConfig.putRunTimeProperty("canTransfer", canTransfer);
        testConfig.putRunTimeProperty("budgetMemberType", budgetMemberType.getMemberType());
        testConfig.putRunTimeProperty("personRoleSlug", roleType.getType());
        if (budgetMemberType.equals(BudgetMemberType.Owner))
        {
            testConfig.putRunTimeProperty("newBudgetOwnerUuid", getInviteeUuid(getJsonArrayFromResponse(response), roleType));
            testConfig.putRunTimeProperty("newOwnerCanTransfer", canTransfer);
            testConfig.putRunTimeProperty("newBudgetOwnerName", getInviteeName(getJsonArrayFromResponse(response), roleType));
        } else if (budgetMemberType.equals(BudgetMemberType.Member))
        {
            testConfig.putRunTimeProperty("newBudgetMemberUuid", getInviteeUuid(getJsonArrayFromResponse(response), roleType));
            testConfig.putRunTimeProperty("newMemberCanTransfer", canTransfer);
            testConfig.putRunTimeProperty("newBudgetMemberName", getInviteeName(getJsonArrayFromResponse(response), roleType));
        } else
        {
            testConfig.logFail("Budget member type is not supported");
        }
    }

    public void addUserInfoToBudgetRequest(String userUuid, String fullName, SaasEnums.BudgetMemberType budgetMemberType, boolean canTransfer)
    {
        testConfig.putRunTimeProperty("canTransfer", canTransfer);
        switch (budgetMemberType)
        {
            case Owner ->
            {
                testConfig.putRunTimeProperty("newBudgetOwnerUuid", userUuid);
                testConfig.putRunTimeProperty("newOwnerCanTransfer", canTransfer);
                testConfig.putRunTimeProperty("newBudgetOwnerName", fullName);
            }
            case Member ->
            {
                testConfig.putRunTimeProperty("newBudgetMemberUuid", userUuid);
                testConfig.putRunTimeProperty("newMemberCanTransfer", canTransfer);
                testConfig.putRunTimeProperty("newBudgetMemberName", fullName);
            }
            default ->
            {
                testConfig.logFailToEndExecution("Budget member type is not supported");
            }
        }
    }

    private void verifyBudgetsResponseCorrect(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response code", 200, response.getStatusCode());
        JSONArray array = new JSONObject(response.body().asString()).getJSONArray("data");
        AssertHelper.compareTrue(testConfig, "Budgets list", array.length() > 0);
        for (int i = 0; i < array.length(); i++)
        {
            AssertHelper.compareEquals(testConfig, "Budget state", SaasEnums.BudgetState.Active.getState(), array.getJSONObject(i).getString("state"));

            if (array.getJSONObject(i).getJSONObject("currency").getString("code").equals(testConfig.getRunTimeProperty("currencyCode")))
            {
                testConfig.putRunTimeProperty("budgetUuid", array.getJSONObject(i).getString("uuid"));
                testConfig.putRunTimeProperty("_filters[budget_uuids][0]", array.getJSONObject(i).getString("uuid"));
                break;
            }
        }
    }

    private void verifyUpdatedPaymentLinkResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "uuid", response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "stateCode", testConfig.getRunTimeProperty("stateCode"), response.jsonPath().getString("state_code"));
        AssertHelper.compareEquals(testConfig, "number", testConfig.getRunTimeProperty("number"), response.jsonPath().getString("number"));
        AssertHelper.compareEquals(testConfig, "payableAmount", testConfig.testData.get("updatedAmount"), response.jsonPath().getString("payable_amount"));
        AssertHelper.compareEquals(testConfig, "currencyCode", testConfig.testData.get("currency"), response.jsonPath().getString("currency_code"));
        AssertHelper.compareEquals(testConfig, "status", testConfig.testData.get("status"), response.jsonPath().getString("status"));
        AssertHelper.assertNotNull(testConfig, "created_at", response.jsonPath().getString("created_at"));
        AssertHelper.compareEquals(testConfig, "counterparty.name", testConfig.testData.get("customer"), response.jsonPath().getString("counterparty.name"));
        AssertHelper.compareEquals(testConfig, "created_person.full_name", testConfig.testData.get("fullName"), response.jsonPath().getString("created_person.full_name"));
        AssertHelper.assertNotNull(testConfig, "payment_link.uuid", response.jsonPath().getString("payment_link.uuid"));
        AssertHelper.assertNotNull(testConfig, "payment_link.reference_code", response.jsonPath().getString("payment_link.reference_code"));
        AssertHelper.compareEquals(testConfig, "payment_link.link", String.format("https://example.link/%s", response.jsonPath().getString("payment_link.reference_code")), response.jsonPath().getString("payment_link.link"));
        AssertHelper.assertNotNull(testConfig, "payment_link.url", response.jsonPath().getString("payment_link.url"));
        AssertHelper.assertNotNull(testConfig, "payment_link.counter", response.jsonPath().getString("payment_link.counter"));
        AssertHelper.assertNotNull(testConfig, "payment_link.created_at", response.jsonPath().getString("payment_link.created_at"));
        AssertHelper.assertNotNull(testConfig, "payment_link.updated_at", response.jsonPath().getString("payment_link.updated_at"));
    }

    private void verifyCreatedPaymentLinkNotifierResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "uuid", response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "from_email", testConfig.testData.get("fromEmail"), response.jsonPath().getString("from_email"));
        AssertHelper.compareEquals(testConfig, "to_emails", testConfig.testData.get("toEmails"), response.jsonPath().getList("to_emails").get(0));
        AssertHelper.compareEquals(testConfig, "to_emails size", 1, response.jsonPath().getList("to_emails").size());
        AssertHelper.compareEquals(testConfig, "subject", testConfig.testData.get("subject"), response.jsonPath().getString("subject"));
        AssertHelper.compareEquals(testConfig, "message", testConfig.testData.get("message"), response.jsonPath().getString("message"));
        AssertHelper.compareEquals(testConfig, "cc_emails", testConfig.testData.get("ccEmails"), response.jsonPath().getList("cc_emails").get(0));
        AssertHelper.compareEquals(testConfig, "cc_emails size", 1, response.jsonPath().getList("cc_emails").size());
        AssertHelper.compareEquals(testConfig, "bcc_emails", testConfig.testData.get("bccEmails"), response.jsonPath().getList("bcc_emails").get(0));
        AssertHelper.compareEquals(testConfig, "bcc_emails size", 1, response.jsonPath().getList("bcc_emails").size());
    }

    private void verifyGetPaymentLinkResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "payment link list", response.jsonPath().getList("data").size() > 0);
        AssertHelper.compareEquals(testConfig, "current page", 1, response.jsonPath().getInt("current_page"));
        AssertHelper.compareEquals(testConfig, "uuid", testConfig.testData.get("uuid"), response.jsonPath().getString("data[0].uuid"));
        AssertHelper.compareEquals(testConfig, "stateCode", testConfig.testData.get("stateCode"), response.jsonPath().getString("data[0].state_code"));
        AssertHelper.compareEquals(testConfig, "year", testConfig.testData.get("year"), response.jsonPath().getString("data[0].year"));
        AssertHelper.compareEquals(testConfig, "number", testConfig.testData.get("number"), response.jsonPath().getString("data[0].number"));
        AssertHelper.compareEquals(testConfig, "code", testConfig.testData.get("code"), response.jsonPath().getString("data[0].code"));
        AssertHelper.compareEquals(testConfig, "expiryDate", testConfig.testData.get("expiryDate"), response.jsonPath().getString("data[0].expiry_date"));
        AssertHelper.compareEquals(testConfig, "payableAmount", testConfig.testData.get("payableAmount"), response.jsonPath().getString("data[0].payable_amount"));
        AssertHelper.compareEquals(testConfig, "currencyCode", testConfig.testData.get("currencyCode"), response.jsonPath().getString("data[0].currency_code"));
        AssertHelper.compareEquals(testConfig, "status", testConfig.testData.get("status"), response.jsonPath().getString("data[0].status"));
        AssertHelper.compareEquals(testConfig, "createdAt", testConfig.testData.get("createdAt"), response.jsonPath().getString("data[0].created_at"));
        AssertHelper.compareEquals(testConfig, "updatedAt", testConfig.testData.get("updatedAt"), response.jsonPath().getString("data[0].updated_at"));
        AssertHelper.compareEquals(testConfig, "debitTransactionsCount", testConfig.testData.get("debitTransactionsCount"), response.jsonPath().getString("data[0].debit_transactions_count"));
        AssertHelper.compareEquals(testConfig, "authorizationsCreate", testConfig.testData.get("authorizationsCreate"), response.jsonPath().getString("data[0].authorizations.create"));
        AssertHelper.compareEquals(testConfig, "authorizationsView", testConfig.testData.get("authorizationsView"), response.jsonPath().getString("data[0].authorizations.view"));
        AssertHelper.compareEquals(testConfig, "authorizationsUpdate", testConfig.testData.get("authorizationsUpdate"), response.jsonPath().getString("data[0].authorizations.update"));
        AssertHelper.compareEquals(testConfig, "authorizationsDelete", testConfig.testData.get("authorizationsDelete"), response.jsonPath().getString("data[0].authorizations.delete"));
        AssertHelper.compareEquals(testConfig, "authorizationsMarkAsPaid", testConfig.testData.get("authorizationsMarkAsPaid"), response.jsonPath().getString("data[0].authorizations.mark_as_paid"));
        AssertHelper.compareEquals(testConfig, "authorizationsMarkAsUnpaid", testConfig.testData.get("authorizationsMarkAsUnpaid"), response.jsonPath().getString("data[0].authorizations.mark_as_unpaid"));
        setPaymentLinkInfoFromResponse(response);
    }

    private void verifyUnfrozenBudgetDetailCorrect(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget name", testConfig.getRunTimeProperty("budgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Budget description", testConfig.getRunTimeProperty("budgetDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Budget icon", testConfig.getRunTimeProperty("icon"), response.jsonPath().getString("icon"));
        AssertHelper.compareEquals(testConfig, "Budget frequency", testConfig.getRunTimeProperty("frequency"), response.jsonPath().getString("frequency"));
        AssertHelper.compareEquals(testConfig, "Budget uuid", testConfig.getRunTimeProperty("budgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Budget amount", testConfig.getRunTimeProperty("amount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "State", SaasEnums.BudgetState.Active.getState(), response.jsonPath().getString("state"));
    }

    private void verifyFrozenBudgetDetailCorrect(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget name", testConfig.getRunTimeProperty("budgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Budget description", testConfig.getRunTimeProperty("budgetDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Budget icon", testConfig.getRunTimeProperty("icon"), response.jsonPath().getString("icon"));
        AssertHelper.compareEquals(testConfig, "Budget frequency", testConfig.getRunTimeProperty("frequency"), response.jsonPath().getString("frequency"));
        AssertHelper.compareEquals(testConfig, "Budget uuid", testConfig.getRunTimeProperty("budgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Budget amount", testConfig.getRunTimeProperty("amount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "State", SaasEnums.BudgetState.Deactivated.getState(), response.jsonPath().getString("state"));
    }

    private void verifyCreatedPaymentLinkResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "amount", testConfig.getRunTimeProperty("amount"), String.valueOf(response.jsonPath().getInt("payable_amount")));
        AssertHelper.compareEquals(testConfig, "currency", testConfig.getRunTimeProperty("currency"), response.jsonPath().get("currency_code"));
        AssertHelper.compareEquals(testConfig, "description", testConfig.getRunTimeProperty("description"), response.jsonPath().get("description"));
        AssertHelper.compareEquals(testConfig, "status", testConfig.getRunTimeProperty("status"), response.jsonPath().get("status"));
        AssertHelper.compareEquals(testConfig, "customer", testConfig.getRunTimeProperty("customer"), response.jsonPath().get("counterparty.name"));
        setPaymentLinkInfoFromResponse(response);
    }

    private void setPaymentLinkInfoFromResponse(Response response)
    {
        if (Objects.nonNull(response.body().jsonPath().getString("uuid")))
        {
            testConfig.putRunTimeProperty("paymentLinkUuid", response.body().jsonPath().getString("uuid"));
            testConfig.putRunTimeProperty("stateCode", response.body().jsonPath().getString("state_code"));
            testConfig.putRunTimeProperty("number", response.body().jsonPath().getString("number"));
        } else if (Objects.nonNull(response.body().jsonPath().getString("data.uuid")))
        {
            testConfig.putRunTimeProperty("paymentLinkUuid", response.body().jsonPath().getList("data.uuid").get(0));
        }
    }

    private boolean isNonAdminBudgetOwnerExisted(JSONArray array)
    {
        return IntStream.range(0, array.length()).anyMatch(i -> array.getJSONObject(i).get("uuid").equals(testConfig.getRunTimeProperty("inviteeId")) && !array.getJSONObject(i).get("uuid").equals(array.getJSONObject(i).getJSONObject("budget_inviter").getString("uuid")));
    }

    private void verifyNonAdminBudgetOwnerAndMemberDataCorrect(Response response)
    {
        JSONArray array = new JSONArray(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Size", 2, array.length());
        boolean isAdminExisted = isNewBudgetMemberOrOwnerExisted(array, BudgetMemberType.Owner);
        boolean isNonAdminBudgetOwnerExisted = isNonAdminBudgetOwnerExisted(array);
        boolean isNewBudgetMemberExisted = isNewBudgetMemberOrOwnerExisted(array, BudgetMemberType.Member);
        IntStream.range(0, array.length()).forEach(i ->
        {
            String inviterUuid = array.getJSONObject(i).getJSONObject("budget_inviter").getString("uuid");
            AssertHelper.compareEquals(testConfig, "Inviter uuid", inviterUuid, testConfig.getRunTimeProperty("inviterId"));
        });
        AssertHelper.compareEquals(testConfig, "Is admin existed", false, isAdminExisted);
        AssertHelper.compareEquals(testConfig, "Is non-admin budget owner existed", true, isNonAdminBudgetOwnerExisted);
        AssertHelper.compareEquals(testConfig, "Budget member invitee uuid", true, isNewBudgetMemberExisted);
    }

    private void verifyNewIssuedPhysicalCardResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Card color", testConfig.getRunTimeProperty("cardColor"), response.jsonPath().getString("card_color"));
        AssertHelper.compareEquals(testConfig, "Card type", testConfig.getRunTimeProperty("cardType"), response.jsonPath().getString("card_type"));
        AssertHelper.compareEquals(testConfig, "Card description", testConfig.getRunTimeProperty("cardDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Card name", testConfig.getRunTimeProperty("cardName"), response.jsonPath().getString("card_name"));
        AssertHelper.compareEquals(testConfig, "Card frequency", testConfig.getRunTimeProperty("cardFrequency"), response.jsonPath().getString("spend_limit_frequency"));
        AssertHelper.compareEquals(testConfig, "Card category", testConfig.getRunTimeProperty("cardCategory"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Card maximum amount", testConfig.getRunTimeProperty("cardAmount"), response.jsonPath().getString("maximum_over_consecutive_day"));
        AssertHelper.compareEquals(testConfig, "Card status", "virtual-active", response.jsonPath().getString("state.state"));
        AssertHelper.compareEquals(testConfig, "Card holder name", testConfig.getRunTimeProperty("cardHolderName"), response.jsonPath().getString("card_holder_name"));
        AssertHelper.compareEquals(testConfig, "Delivery address", testConfig.getRunTimeProperty("address"), response.jsonPath().getString("delivery_address.address"));
    }

    private boolean isBudgetPeopleRevoked(JSONArray jsonArray, BudgetMemberType budgetMemberType)
    {
        boolean isBudgetPersonRevoked = false;
        if (budgetMemberType.equals(BudgetMemberType.Owner))
        {
            isBudgetPersonRevoked = IntStream.range(0, jsonArray.length()).anyMatch(i -> jsonArray.getJSONObject(i).getString("uuid").equals(testConfig.getRunTimeProperty("secondBudgetOwnerId")) && jsonArray.getJSONObject(i).getJSONObject("budget_person_state").get("state").equals(SaasEnums.BudgetMemberState.Revoked.getState()));
        } else if (budgetMemberType.equals(BudgetMemberType.Member))
        {
            isBudgetPersonRevoked = IntStream.range(0, jsonArray.length()).anyMatch(i -> jsonArray.getJSONObject(i).getString("uuid").equals(testConfig.getRunTimeProperty("newBudgetMemberUuid")) && jsonArray.getJSONObject(i).getJSONObject("budget_person_state").get("state").equals(SaasEnums.BudgetMemberState.Revoked.getState()));
        }
        return isBudgetPersonRevoked;
    }

    private void verifyBudgetOwnerAndMemberRevokedSuccessful(Response response)
    {
        JSONArray jsonArray = new JSONArray(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget people size", 3, jsonArray.length());
        boolean isBudgetOwnerRevoked = isBudgetPeopleRevoked(jsonArray, BudgetMemberType.Owner);
        boolean isBudgetMemberRevoked = isBudgetPeopleRevoked(jsonArray, BudgetMemberType.Member);
        AssertHelper.compareTrue(testConfig, "Is budget owner revoked?", isBudgetOwnerRevoked);
        AssertHelper.compareTrue(testConfig, "Is budget member revoked?", isBudgetMemberRevoked);
    }

    private void verifyBudgetOwnerRevokedSuccessful(Response response)
    {
        JSONArray jsonArray = new JSONArray(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget people size", 3, jsonArray.length());
        boolean isBudgetOwnerRevoked = isBudgetPeopleRevoked(jsonArray, BudgetMemberType.Owner);
        AssertHelper.compareTrue(testConfig, "Is budget owner revoked?", isBudgetOwnerRevoked);
    }

    private void verifyUpdatedBudgetResponseWithoutCreditTransaction(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget uuid", testConfig.getRunTimeProperty("budgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Updated budget name", testConfig.getRunTimeProperty("updatedBudgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Updated budget description", testConfig.getRunTimeProperty("updatedBudgetDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Update amount", testConfig.getRunTimeProperty("updatedAmount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Frequency", testConfig.getRunTimeProperty("frequency"), response.jsonPath().getString("frequency"));
        AssertHelper.compareEquals(testConfig, "Updated icon", testConfig.getRunTimeProperty("updatedIcon"), response.jsonPath().getString("icon"));
        AssertHelper.compareTrue(testConfig, "Last credit transaction", Objects.isNull(response.jsonPath().getString("last_credit_transaction")));
    }

    private void verifyBudgetDetailResponseCorrect(Response response)
    {
        JSONObject obj = new JSONObject(response.body().asString());
        JSONArray array = obj.getJSONArray("remaining_limit");
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget uuid", testConfig.getRunTimeProperty("budgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Budget name", testConfig.getRunTimeProperty("budgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Budget description", testConfig.getRunTimeProperty("budgetDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Icon", testConfig.getRunTimeProperty("icon"), response.jsonPath().getString("icon"));
        AssertHelper.compareEquals(testConfig, "Frequency", testConfig.getRunTimeProperty("frequency"), response.jsonPath().getString("frequency"));
        AssertHelper.compareEquals(testConfig, "Amount", testConfig.getRunTimeProperty("amount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Currency", testConfig.getRunTimeProperty("currencyCode"), (String) array.get(0));
    }

    private boolean isNewBudgetMemberOrOwnerExisted(JSONArray array, BudgetMemberType type)
    {
        if (type.equals(BudgetMemberType.Owner))
        {
            return IntStream.range(0, array.length()).anyMatch(i -> array.getJSONObject(i).get("uuid").equals(testConfig.getRunTimeProperty("newBudgetOwnerUuid")));
        } else if (type.equals(BudgetMemberType.Member))
        {
            return IntStream.range(0, array.length()).anyMatch(i -> array.getJSONObject(i).get("uuid").equals(testConfig.getRunTimeProperty("newBudgetMemberUuid")));
        }
        return false;
    }

    private void verifyPeopleFromBudgetWithMultipleOwnerAndMemberResponse(Response response)
    {
        JSONArray array = new JSONArray(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Size", 3, array.length());
        boolean isNewBudgetOwnerExisted = isNewBudgetMemberOrOwnerExisted(array, BudgetMemberType.Owner);
        boolean isNewBudgetMemberExisted = isNewBudgetMemberOrOwnerExisted(array, BudgetMemberType.Member);
        IntStream.range(0, array.length()).forEach(i ->
        {
            String inviterUuid = array.getJSONObject(i).getJSONObject("budget_inviter").getString("uuid");
            AssertHelper.compareEquals(testConfig, "Inviter uuid", inviterUuid, testConfig.getRunTimeProperty("inviterId"));
        });
        AssertHelper.compareEquals(testConfig, "Budget owner invitee uuid", true, isNewBudgetOwnerExisted);
        AssertHelper.compareEquals(testConfig, "Budget member invitee uuid", true, isNewBudgetMemberExisted);
    }

    private void verifyPeopleFromBudgetWithMultipleOwnerResponse(Response response)
    {
        JSONArray array = new JSONArray(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Size", 2, array.length());
        for (int i = 0; i < array.length(); i++)
        {
            JSONObject budgetInviter = array.getJSONObject(i).getJSONObject("budget_inviter");
            AssertHelper.compareEquals(testConfig, "Inviter uuid", testConfig.getRunTimeProperty("inviterId"), budgetInviter.getString("uuid"));
        }
        boolean isNewBudgetOwnerExisted = isNewBudgetMemberOrOwnerExisted(array, BudgetMemberType.Owner);
        AssertHelper.compareEquals(testConfig, "New budget owner uuid", true, isNewBudgetOwnerExisted);
    }

    private void verifyCardHolderInfo(Response response)
    {
        if (Objects.isNull(testConfig.getRunTimeProperty("budgetUuid")))
        {
            AssertHelper.compareEquals(testConfig, "Card holder name", testConfig.getRunTimeProperty("personFullName"), response.jsonPath().getString("person.full_name"));
        } else
        {
            if (!testConfig.getRunTimeProperty("holder_role").equals(RoleType.Director.getType()))
            {
                if (testConfig.getRunTimeProperty("budgetMemberType").equals(BudgetMemberType.Owner.getMemberType()))
                {
                    AssertHelper.compareEquals(testConfig, "Card holder role", testConfig.getRunTimeProperty("holder_role"), response.jsonPath().getString("holder_role"));
                    AssertHelper.compareEquals(testConfig, "Card holder person name", testConfig.getRunTimeProperty("newBudgetOwnerName"), response.jsonPath().getString("holder_name"));
                    AssertHelper.compareEquals(testConfig, "Card holder person uuid", testConfig.getRunTimeProperty("newBudgetOwnerUuid"), response.jsonPath().getString("person.uuid"));
                } else if (testConfig.getRunTimeProperty("budgetMemberType").equals(BudgetMemberType.Member.getMemberType()))
                {
                    AssertHelper.compareEquals(testConfig, "Card holder role", testConfig.getRunTimeProperty("holder_role"), response.jsonPath().getString("holder_role"));
                    AssertHelper.compareEquals(testConfig, "Card holder person name", testConfig.getRunTimeProperty("newBudgetMemberName"), response.jsonPath().getString("holder_name"));
                    AssertHelper.compareEquals(testConfig, "Card holder person uuid", testConfig.getRunTimeProperty("newBudgetMemberUuid"), response.jsonPath().getString("person.uuid"));
                }
            } else
            {
                AssertHelper.compareEquals(testConfig, "Card holder person name", testConfig.getRunTimeProperty("full_name"), response.jsonPath().getString("holder_name"));
            }
        }
    }

    private void verifyNewIssuedVirtualCardResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Card color", testConfig.getRunTimeProperty("cardColor"), response.jsonPath().getString("card_color"));
        AssertHelper.compareEquals(testConfig, "Card type", testConfig.getRunTimeProperty("cardType"), response.jsonPath().getString("card_type"));
        AssertHelper.compareEquals(testConfig, "Card description", testConfig.getRunTimeProperty("cardDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Card name", testConfig.getRunTimeProperty("cardName"), response.jsonPath().getString("card_name"));
        AssertHelper.compareEquals(testConfig, "Card frequency", testConfig.getRunTimeProperty("cardFrequency"), response.jsonPath().getString("spend_limit_frequency"));
        AssertHelper.compareEquals(testConfig, "Card category", testConfig.getRunTimeProperty("cardCategory"), response.jsonPath().getString("category.name"));
        AssertHelper.compareEquals(testConfig, "Card maximum amount", testConfig.getRunTimeProperty("cardAmount"), response.jsonPath().getString("maximum_over_consecutive_day"));
        AssertHelper.compareEquals(testConfig, "Card status", "active", response.jsonPath().getString("state.state"));
        testConfig.putRunTimeProperty("holder_role", response.jsonPath().getString("holder_role"));
        verifyCardHolderInfo(response);
        testConfig.putRunTimeProperty("cardUuid", response.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("cardLastDigit", response.jsonPath().getString("uuid"));
    }

    private void verifyErrorMessageResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 401, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Message", "Unauthenticated.", response.jsonPath().getString("message"));
    }

    private void verifyCreatedBudgetResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget name", testConfig.getRunTimeProperty("budgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Budget description", testConfig.getRunTimeProperty("budgetDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Icon", testConfig.getRunTimeProperty("icon"), response.jsonPath().getString("icon"));
        AssertHelper.compareEquals(testConfig, "Frequency", testConfig.getRunTimeProperty("frequency"), response.jsonPath().getString("frequency"));
        testConfig.putRunTimeProperty("budgetUuid", response.body().jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("budget_uuid", response.body().jsonPath().getString("uuid"));
    }

    private void verifyUpdatedBudgetResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Budget uuid", testConfig.getRunTimeProperty("budgetUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Updated budget name", testConfig.getRunTimeProperty("updatedBudgetName"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Updated budget description", testConfig.getRunTimeProperty("updatedBudgetDescription"), response.jsonPath().getString("description"));
        AssertHelper.compareEquals(testConfig, "Update amount", testConfig.getRunTimeProperty("updatedAmount"), response.jsonPath().getString("amount"));
        AssertHelper.compareEquals(testConfig, "Frequency", testConfig.getRunTimeProperty("frequency"), response.jsonPath().getString("frequency"));
        AssertHelper.compareEquals(testConfig, "Updated icon", testConfig.getRunTimeProperty("updatedIcon"), response.jsonPath().getString("icon"));
    }

    private void verifyPeopleFromBudgetWithSingleOwnerResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONArray people = new JSONArray(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Size", 1, people.length());
        AssertHelper.compareEquals(testConfig, "Inviter id", testConfig.getRunTimeProperty("inviterId"), people.getJSONObject(0).getJSONObject("budget_inviter").get("uuid"));
        AssertHelper.compareEquals(testConfig, "Invitee id", testConfig.getRunTimeProperty("inviteeId"), people.getJSONObject(0).getString("uuid"));
    }

    private JSONArray getJsonArrayFromResponse(Response response)
    {
        return new JSONObject(response.body().asString()).getJSONArray("data");
    }

    private String getInviterUuid(JSONArray jsonArray, String name)
    {
        for (int i = 0; i < jsonArray.length(); i++)
        {
            String fullName = jsonArray.getJSONObject(i).getString("full_name");
            String uuid = jsonArray.getJSONObject(i).getString("uuid");
            if (fullName.equals(name))
            {
                return uuid;
            }
        }
        return null;
    }

    private String getInviteeUuid(JSONArray jsonArray, RoleType roleType)
    {
        for (int i = 0; i < jsonArray.length(); i++)
        {
            String uuid = jsonArray.getJSONObject(i).getString("uuid");
            String role = jsonArray.getJSONObject(i).getString("role_slug");
            if (roleType.getType().equals(role))
            {
                return uuid;
            }
        }
        return null;
    }

    private String getInviteeName(JSONArray jsonArray, RoleType roleType)
    {
        for (int i = 0; i < jsonArray.length(); i++)
        {
            String fullName = jsonArray.getJSONObject(i).getString("full_name");
            String role = jsonArray.getJSONObject(i).getString("role_slug");
            if (roleType.getType().equals(role))
            {
                return fullName;
            }
        }
        return null;
    }

    public void setupIssueCardRequestParams(Response response, CardTypeEnum type)
    {
        JSONArray array = new JSONObject(response.body().asString()).getJSONArray("data");
        JSONObject person = array.getJSONObject(DataGenerator.generateRandomNumberInIntRange(0, array.length() - 1));
        if (!Objects.isNull(testConfig.getRunTimeProperty("newBudgetOwnerUuid")))
        {
            testConfig.putRunTimeProperty("personUuid", testConfig.getRunTimeProperty("newBudgetOwnerUuid"));
            testConfig.putRunTimeProperty("personFullName", testConfig.getRunTimeProperty("newBudgetOwnerName"));
        }
        if (!Objects.isNull(testConfig.getRunTimeProperty("newBudgetMemberUuid")))
        {
            testConfig.putRunTimeProperty("personUuid", testConfig.getRunTimeProperty("newBudgetMemberUuid"));
            testConfig.putRunTimeProperty("personFullName", testConfig.getRunTimeProperty("newBudgetMemberName"));
        } else
        {
            testConfig.putRunTimeProperty("personUuid", person.getString("uuid"));
            testConfig.putRunTimeProperty("personFullName", person.getString("full_name"));
            testConfig.putRunTimeProperty("personRoleSlug", person.getString("role_slug"));
        }
        testConfig.putRunTimeProperty("cardAmount", DataGenerator.generateRandomNumberInIntRange(1000, 3000));
        testConfig.putRunTimeProperty("cardFrequency", CardFrequency.getRandomCardFrequency().getCardFrequency());
        testConfig.putRunTimeProperty("cardCategory", DebitCategory.getRandomCategory().getCategory());
        testConfig.putRunTimeProperty("cardColor", "#01D167");
        testConfig.putRunTimeProperty("cardType", type.getCardType());
        testConfig.putRunTimeProperty("cardName", "Card " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(10, 3));
        testConfig.putRunTimeProperty("cardDescription", "Description " + DataGenerator.getRandomAlphaNumberStringFixedLengthWithSpace(10, 3));
    }

    public void setupCreateDebitCardRequestForBudgetPerson(BudgetMemberType budgetMemberType, RoleType roleType, CardTypeEnum cardType)
    {

        switch (budgetMemberType)
        {
            case Owner:
                testConfig.putRunTimeProperty("personUuid", testConfig.getRunTimeProperty("newBudgetOwnerUuid"));
                break;
            case Member:
                testConfig.putRunTimeProperty("personUuid", testConfig.getRunTimeProperty("newBudgetMemberUuid"));
                break;
            default:
                testConfig.logFail("Budget member type not defined or unsupported");
                break;
        }

        if (cardType.equals(CardTypeEnum.VirtualPhysical))
        {
            testConfig.putRunTimeProperty("cardHolderName", "Person " + DataGenerator.generateRandomAlphaNumericString(8));
            testConfig.putRunTimeProperty("deliveryAddressUuid", testConfig.testData.get("addressUuid"));
            testConfig.putRunTimeProperty("address", testConfig.testData.get("mainAddress"));
        }

        testConfig.putRunTimeProperty("cardAmount", DataGenerator.generateRandomNumberInIntRange(10000, 300000));
        testConfig.putRunTimeProperty("cardFrequency", SaasEnums.CardFrequency.getRandomCardFrequency().getCardFrequency());
        testConfig.putRunTimeProperty("cardCategory", SaasEnums.DebitCategory.getRandomCategory().getCategory());
        testConfig.putRunTimeProperty("cardType", cardType.getCardType());
        testConfig.putRunTimeProperty("cardColor", SaasEnums.CardColor.getRandomColor().getColor());
        testConfig.putRunTimeProperty("cardName", "Card " + DataGenerator.generateRandomAlphaNumericString(9));
        testConfig.putRunTimeProperty("cardDescription", "Description " + DataGenerator.generateRandomAlphaNumericString(10));
        testConfig.putRunTimeProperty("holder_role", roleType.getType());
        testConfig.putRunTimeProperty("budgetMemberType", budgetMemberType.getMemberType());
    }

    public void revokeBudgetPersonWithType(BudgetMemberType budgetMemberType)
    {
        if (budgetMemberType.equals(BudgetMemberType.Owner))
        {
            testConfig.putRunTimeProperty("budgetPersonId", testConfig.getRunTimeProperty("secondBudgetOwnerId"));
        } else if (budgetMemberType.equals(BudgetMemberType.Member))
        {
            testConfig.putRunTimeProperty("budgetPersonId", testConfig.getRunTimeProperty("newBudgetMemberUuid"));
        }
        Response response = sendRequestAndGetResponse(SaasApiDetails.RemoveOwnerOrMemberFromBudget, SaasJsonDetails.RemoveBudgetPersonSchema);
        verifyApiResponse(response, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);
    }

    private void verifyGetPeopleFromBudgetWithoutAuthorizationsResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status code", 401, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Error message", "Unauthenticated.", response.body().jsonPath().getString("message"));
    }

    private void verifyGetPeopleFromBudgetWithoutHeadersResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status code", 401, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Error message", "Unauthenticated.", response.body().jsonPath().getString("message"));
    }

    private void verifySubmitClaimResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response code", 200, response.getStatusCode());
        AssertHelper.compareEquals(testConfig, "Merchant name", testConfig.getRunTimeProperty("merchantName"), response.body().jsonPath().getString("merchant_name"));
        AssertHelper.compareEquals(testConfig, "Target amount", testConfig.getRunTimeProperty("targetAmount"), response.body().jsonPath().getString("target_amount"));
        AssertHelper.compareEquals(testConfig, "Target currency", testConfig.getRunTimeProperty("currencyCode"), response.body().jsonPath().getString("target_currency_code"));
        AssertHelper.compareEquals(testConfig, "Purpose of spend", testConfig.getRunTimeProperty("purposeOfSpend"), response.body().jsonPath().getString("purpose_of_spend"));
        AssertHelper.compareEquals(testConfig, "Debit account", testConfig.getRunTimeProperty("debitAccountUuid"), response.body().jsonPath().getString("debit_account.uuid"));
        testConfig.putRunTimeProperty("claimUuid", response.body().jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("claimState", ClaimState.PendingReview.getCode());
    }

    private void verifyGetFileResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response code", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "URL", Objects.nonNull(response.body().jsonPath().getString("url")));
    }

    private void verifyUploadReceiptResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "File type", testConfig.getRunTimeProperty("mimeType"), response.body().jsonPath().getString("mime_type"));
        AssertHelper.compareEquals(testConfig, "File name", testConfig.getRunTimeProperty("fileName"), response.body().jsonPath().getString("original"));
        testConfig.putRunTimeProperty("uploadTime", response.body().jsonPath().getString("created_at").split(" ")[0]);
        testConfig.putRunTimeProperty("fileUuid", response.body().jsonPath().getString("uuid"));
    }

    private void verifyAttachDocumentResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response code", 202, response.getStatusCode());
        AssertHelper.compareEquals(testConfig, "File uuid", testConfig.getRunTimeProperty("fileUuid"), response.body().jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "Collection", testConfig.getRunTimeProperty("source_collection"), response.body().jsonPath().getString("collection"));
        AssertHelper.compareEquals(testConfig, "File name", testConfig.getRunTimeProperty("fileName"), response.body().jsonPath().getString("original"));
        AssertHelper.compareEquals(testConfig, "Mime type", testConfig.getRunTimeProperty("mimeType"), response.body().jsonPath().getString("mime_type"));
    }

    public void setupSubmitClaimRequestData(boolean submitToBudget, PaymentEnums.CurrencyEnum currencyEnum, Response sofResponse, Response fileUploadResponse, Response getFileResponse)
    {
        String modelType = submitToBudget ? ClaimModelType.Budget.getModelType() : ClaimModelType.DebitAccount.getModelType();
        JSONArray jsonArray = new JSONArray(sofResponse.asString());
        JSONObject targetSourceOfFund = getRandomJSONObjectByKeyValue(jsonArray, "model_type", modelType);

        if (submitToBudget)
        {
            testConfig.putRunTimeProperty("budgetUuid", targetSourceOfFund.get("uuid"));
        }
        testConfig.putRunTimeProperty("targetAmount", DataGenerator.generateRandomNumberInIntRange(100, 100000));
        testConfig.putRunTimeProperty("currencyCode", currencyEnum.name());
        testConfig.putRunTimeProperty("merchantName", String.format("Auto Merchant %s", DataGenerator.generateRandomAlphaNumericString(4)));
        testConfig.putRunTimeProperty("transactionDate", DataGenerator.getDate("YYYY-MM-dd", Enums.DateRequired.MonthsBeforeCurrentDate, -3));
        testConfig.putRunTimeProperty("categoryName", DebitCategory.getRandomCategory().getCategory());
        testConfig.putRunTimeProperty("purposeOfSpend", DataGenerator.generateRandomString(10));
        testConfig.putRunTimeProperty("claimSourceOfFund", targetSourceOfFund);

        testConfig.putRunTimeProperty("fileUuid", fileUploadResponse.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("fileName", fileUploadResponse.jsonPath().getString("original"));
        testConfig.putRunTimeProperty("fileCreatedAt", fileUploadResponse.jsonPath().getString("created_at"));
        testConfig.putRunTimeProperty("fileUrl", getFileResponse.jsonPath().getString("url"));
    }

    public void submitClaim(PaymentHelper paymentHelper, boolean isSubmitToBudget, CurrencyEnum currency, String fileName)
    {
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, CurrencyEnum.SGD);

        Response getClaimSofResponse = sendRequestAndGetResponse(SaasApiDetails.GetClaimSourceOfFunds, null);
        verifyApiResponse(getClaimSofResponse, SaasApiDetails.GetClaimSourceOfFunds, null);

        setupUploadFileParams(fileName);
        Response uploadReceiptResponse = sendRequestAndGetResponse(SaasApiDetails.UploadFile, null);
        verifyApiResponse(uploadReceiptResponse, SaasApiDetails.UploadFile, SaasJsonDetails.UploadReceiptResponse);

        Response getFileResponse = sendRequestAndGetResponse(SaasApiDetails.GetFileUrl, null);
        verifyApiResponse(getFileResponse, SaasApiDetails.GetFileUrl, SaasJsonDetails.GetFileResponse);

        setupSubmitClaimRequestData(isSubmitToBudget, currency, getClaimSofResponse, uploadReceiptResponse, getFileResponse);
        Response submitClaimResponse = sendRequestAndGetResponse(SaasApiDetails.SubmitClaim, SaasJsonDetails.SubmitClaimRequestSchema);
        verifyApiResponse(submitClaimResponse, SaasApiDetails.SubmitClaim, SaasJsonDetails.SubmitClaimResponse);

        Response attachDocumentResponse = sendRequestAndGetResponse(SaasApiDetails.PutFileUrl, null);
        verifyApiResponse(attachDocumentResponse, SaasApiDetails.PutFileUrl, SaasJsonDetails.AttachDocumentResponse);
    }

    public void setupUpdateClaimRequestData(boolean submitToBudget, PaymentEnums.CurrencyEnum currencyEnum, Response sofResponse)
    {
        String modelType = submitToBudget ? ClaimModelType.Budget.getModelType() : ClaimModelType.DebitAccount.getModelType();
        JSONArray jsonArray = new JSONArray(sofResponse.asString());
        JSONObject targetSourceOfFund = getRandomJSONObjectByKeyValue(jsonArray, "model_type", modelType);

        if (submitToBudget)
        {
            testConfig.putRunTimeProperty("budgetUuid", targetSourceOfFund.get("uuid"));
        }

        testConfig.putRunTimeProperty("targetAmount", DataGenerator.generateRandomNumberInIntRange(100, 100000));
        testConfig.putRunTimeProperty("currencyCode", currencyEnum.name());
        testConfig.putRunTimeProperty("merchantName", String.format("Auto Merchant %s update", DataGenerator.generateRandomAlphaNumericString(4)));
        testConfig.putRunTimeProperty("transactionDate", DataGenerator.getDate("YYYY-MM-dd", Enums.DateRequired.MonthsBeforeCurrentDate, -2));
        testConfig.putRunTimeProperty("categoryName", DebitCategory.getRandomCategory().getCategory());
        testConfig.putRunTimeProperty("purposeOfSpend", DataGenerator.generateRandomString(10));
        testConfig.putRunTimeProperty("claimSourceOfFund", targetSourceOfFund);
    }


    protected void setupPayClaimData(SaasApiDetails saasApiDetails)
    {
        SaasApiDetails.GetClaimDetails.setFieldsParams("number", "target_currency_code", "target_amount", "source_currency_code", "source_amount", "exchange_rate", "account_holder_name",
                "account_number", "bank.name", "bank.acronym", "bank.transfer_bank_code", "bank.bank_code", "budget.uuid", "budget.name", "budget.icon",
                "budget.amount", "budget.spent_amount", "create_person.full_name", "category.uuid", "category.name");
        Response claimDetailsResponse = executeRequestAndGetResponse(SaasApiDetails.GetClaimDetails, null);
        String claimNumber = claimDetailsResponse.jsonPath().getString("number").substring(3);
        testConfig.putRunTimeProperty("recipientBankCode", claimDetailsResponse.jsonPath().getString("bank.transfer_bank_code"));
        testConfig.putRunTimeProperty("recipientAccountNumber", claimDetailsResponse.jsonPath().getString("account_number"));
        testConfig.putRunTimeProperty("recipientAccountName", claimDetailsResponse.jsonPath().getString("account_holder_name"));
        testConfig.putRunTimeProperty("sourceAmount", claimDetailsResponse.jsonPath().getString("source_amount"));
        testConfig.putRunTimeProperty("claimNumber", claimNumber);
        testConfig.putRunTimeProperty("internalNotes", String.format("Note %s %s", claimNumber, DataGenerator.generateRandomString(3)));
        testConfig.putRunTimeProperty("nextPaymentDate", saasApiDetails.equals(SaasApiDetails.ScheduleClaimPayment) ? DataGenerator.getDate("YYYY-MM-dd", Enums.DateRequired.FutureDate, 3) : "");
    }

    public void setupUploadFileParams(String fileName)
    {
        String mimeType = null;
        for (FileMimeType type : FileMimeType.values())
        {
            if (type.name().endsWith(fileName.split("\\.")[1].toUpperCase()))
            {
                mimeType = type.getName();
                break;
            }
        }
        String filePath = System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "SaaS" + File.separator + "Images" + File.separator + fileName;
        testConfig.putRunTimeProperty("mimeType", mimeType);
        testConfig.putRunTimeProperty("filePath", filePath);
        testConfig.putRunTimeProperty("fileName", fileName);
    }

    private String getRandomDebitCardUuidFromResponse(Response response)
    {
        JSONArray array = getJsonArrayFromResponse(response);
        List<String> debitAccountUuids = new ArrayList<>();
        IntStream.range(0, array.length()).forEach(i -> debitAccountUuids.add(array.getJSONObject(i).getString("uuid")));
        return debitAccountUuids.stream().findAny().orElse(null);
    }

    private void verifyFreezeCardResponseCorrect(Response response)
    {
        JSONObject obj = new JSONObject(response.body().asString()).getJSONObject("state");
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Card uuid", testConfig.getRunTimeProperty("cardUuid"), response.jsonPath().getString("uuid"));
        AssertHelper.compareEquals(testConfig, "State", testConfig.getRunTimeProperty("action"), obj.getString("state"));
    }

    private void verifyCardListResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response code", 200, response.getStatusCode());
        AssertHelper.compareTrue(testConfig, "Check Data Size", response.jsonPath().getList("data").size() > 0);
        AssertHelper.compareTrue(testConfig, "Data", Objects.nonNull(response.jsonPath().getString("data")));
        JSONArray items = getJsonArrayFromResponse(response);
        for (int i = 0; i < items.length(); i++)
        {
            JSONObject item = items.getJSONObject(i);
            AssertHelper.compareTrue(testConfig, "Verify element contains `uuid` key", item.has("uuid"));
            AssertHelper.compareTrue(testConfig, "Check uuid value is not null", Objects.nonNull(item.get("uuid")));
        }
        testConfig.putRunTimeProperty("cardUuid", getRandomDebitCardUuidFromResponse(response));
    }

    private void setBillDetail(CurrencyEnum currency)
    {
        testConfig.putRunTimeProperty("currencyCode", currency.name());
        Response createBillResponse = sendRequestAndGetResponse(SaasApiDetails.BillSubmission, SaasJsonDetails.SubmitBillDetailRequestSchema);
        verifyApiResponse(createBillResponse, SaasApiDetails.BillSubmission, null);
    }

    public void createBillWithLineItemAndApprove(CurrencyEnum currency)
    {
        setBillDetail(currency);
        Response billTransferDetailResponse = sendRequestAndGetResponse(SaasApiDetails.SaveBillTransferDetail, SaasJsonDetails.SubmitBillTransferDetailRequestSchema);
        verifyApiResponse(billTransferDetailResponse, SaasApiDetails.SaveBillTransferDetail, null);

        Response submitBillResponse = sendRequestAndGetResponse(SaasApiDetails.SubmitBill, SaasJsonDetails.SubmitBillRequestSchema);
        verifyApiResponse(submitBillResponse, SaasApiDetails.SubmitBill, null);

        Response approveBillResponse = sendRequestAndGetResponse(SaasApiDetails.ApproveBill, SaasJsonDetails.ApproveBillRequestSchema);
        verifyApiResponse(approveBillResponse, SaasApiDetails.ApproveBill, null);
    }

    private void getPendingReviewXeroBillByType(XeroAccountingType type)
    {
        testConfig.putRunTimeProperty("accounting_type", type.getType());
        Response getPendingReviewResponse = sendRequestAndGetResponse(SaasApiDetails.GetPendingReviewXeroBill, null);
        verifyApiResponse(getPendingReviewResponse, SaasApiDetails.GetPendingReviewXeroBill, null);
    }

    public void syncBillToXero()
    {
        getPendingReviewXeroBillByType(XeroAccountingType.Xero);
        Response getXeroBillLineItemResponse = sendRequestAndGetResponse(SaasApiDetails.GetXeroBillLineItem, null);
        verifyApiResponse(getXeroBillLineItemResponse, SaasApiDetails.GetXeroBillLineItem, null);

        Response getXeroAccount = sendRequestAndGetResponse(SaasApiDetails.GetXeroAccount, null);
        verifyApiResponse(getXeroAccount, SaasApiDetails.GetXeroAccount, null);

        Response updateXeroResponse = sendRequestAndGetResponse(SaasApiDetails.UpdateXeroBillLineItem, SaasJsonDetails.UpdateAccountingBillLineItem);
        verifyApiResponse(updateXeroResponse, SaasApiDetails.UpdateXeroBillLineItem, SaasJsonDetails.UpdateAccountingBillLineItemResponseSchema);

        Response approveXeroResponse = sendRequestAndGetResponse(SaasApiDetails.ApproveXeroBill, SaasJsonDetails.ApproveXeroBillRequestSchema);
        verifyApiResponse(approveXeroResponse, SaasApiDetails.ApproveXeroBill, null);

        Response syncXeroResponse = sendRequestAndGetResponse(SaasApiDetails.SyncXeroBill, SaasJsonDetails.ApproveXeroBillRequestSchema);
        verifyApiResponse(syncXeroResponse, SaasApiDetails.SyncXeroBill, null);
    }

    private void verifyTotalLineItemCorrect(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Total line items", "2", response.jsonPath().getString("line_item_count"));
        AssertHelper.compareEquals(testConfig, "Bill status", testConfig.getRunTimeProperty("bill_sync_state"), response.jsonPath().getString("accounting_invoice.state.state"));
    }

    private void verifyBillSubmission(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify currency code", testConfig.getRunTimeProperty("currency_code"), response.jsonPath().getString("currency.code"));
        testConfig.putRunTimeProperty("bill_uuid", response.jsonPath().getString("uuid"));
    }

    private void verifySaveBillTransferDetail(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify currency code", testConfig.getRunTimeProperty("currency_code"), response.jsonPath().getString("debit_account.currency_code"));
    }

    private void verifySubmitBill(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify uuid", testConfig.getRunTimeProperty("bill_uuid"), response.jsonPath().getString("uuid"));
    }

    private void verifyApproveBill(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify bill number", testConfig.getRunTimeProperty("bill_number"), response.jsonPath().getString("bill_number"));
        AssertHelper.compareEquals(testConfig, "Verify amount", testConfig.getRunTimeProperty("amount"), response.jsonPath().getString("amount"));
    }

    private void verifyGetPendingReviewXeroBill(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "Xero bill list", response.jsonPath().getList("data").size() > 0);
        AssertHelper.compareEquals(testConfig, "current page", 1, response.jsonPath().getInt("current_page"));
    }

    private void verifyGetXeroBillLineItem(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Bill line item Quantity 1", 1, response.jsonPath().getInt("[0].quantity"));
        AssertHelper.compareEquals(testConfig, "Bill line item Quantity 2", 1, response.jsonPath().getInt("[1].quantity"));
        AssertHelper.compareEquals(testConfig, "Bill line item Unit Price 1", testConfig.getRunTimeProperty("unit_price1"), String.valueOf(response.jsonPath().getInt("[0].unit_price")));
        AssertHelper.compareEquals(testConfig, "Bill line item Unit Price 2", testConfig.getRunTimeProperty("unit_price2"), String.valueOf(response.jsonPath().getInt("[1].unit_price")));
        testConfig.putRunTimeProperty("bill_line_item_uuid", response.jsonPath().getString("uuid"));
    }

    private void verifyUpdateXeroBillLineItem(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Line item uuid1", testConfig.getRunTimeProperty("uuid1").trim(), response.jsonPath().getString("[0].uuid"));
        AssertHelper.compareEquals(testConfig, "Line item uuid2", testConfig.getRunTimeProperty("uuid2").trim(), response.jsonPath().getString("[1].uuid"));
    }

    public Response updateBudgetUserAccess(String accessType, boolean canTransfer, String personUuid, SaasJsonDetails saasJsonDetails)
    {
        testConfig.putRunTimeProperty("type", accessType);
        testConfig.putRunTimeProperty("canTransfer", canTransfer);
        testConfig.putRunTimeProperty("personUuid", personUuid);

        return sendRequestAndGetResponse(SaasApiDetails.PutUpdateAccessBudget, saasJsonDetails);
    }

    public void verifyBudgetUserAccess(Response response)
    {
        JSONObject actual = new JSONObject(response.body().asString());
        JSONObject expected = new JSONObject(testConfig.getRunTimeProperty("expectedResponse"));
        AssertHelper.compareJsonValues(testConfig, expected, actual, true);
    }

    public void addBillToPendingSyncXero()
    {
        getPendingReviewXeroBillByType(XeroAccountingType.Xero);

        Response getXeroBillLineItemResponse = sendRequestAndGetResponse(SaasApiDetails.GetXeroBillLineItem, null);
        verifyApiResponse(getXeroBillLineItemResponse, SaasApiDetails.GetXeroBillLineItem, null);

        Response getXeroAccount = sendRequestAndGetResponse(SaasApiDetails.GetXeroAccount, null);
        verifyApiResponse(getXeroAccount, SaasApiDetails.GetXeroAccount, null);

        Response updateXeroResponse = sendRequestAndGetResponse(SaasApiDetails.UpdateXeroBillLineItem, SaasJsonDetails.UpdateAccountingBillLineItem);
        verifyApiResponse(updateXeroResponse, SaasApiDetails.UpdateXeroBillLineItem, SaasJsonDetails.UpdateAccountingBillLineItemResponseSchema);

        Response approveXeroResponse = sendRequestAndGetResponse(SaasApiDetails.ApproveXeroBill, SaasJsonDetails.ApproveXeroBillRequestSchema);
        verifyApiResponse(approveXeroResponse, SaasApiDetails.ApproveXeroBill, null);
    }

    public void verifyApproveXeroBill(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify response Json", "[]", response.body().asString());
        testConfig.putRunTimeProperty("bill_sync_state", "approved");
    }

    private String getRandomDebitCardTransactionUuidFromResponse(Response response)
    {
        JSONArray array = getJsonArrayFromResponse(response);
        List<String> debitAccountUuids = new ArrayList<>();
        IntStream.range(0, array.length()).forEach(i -> debitAccountUuids.add(array.getJSONObject(i).getString("uuid")));
        return debitAccountUuids.stream().findAny().orElse(null);
    }

    public void verifySyncXeroBill(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Verify response Json", "[]", response.body().asString());
        testConfig.putRunTimeProperty("bill_sync_state", "syncing");
    }
}