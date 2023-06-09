package Automation.SaaS.api.cards;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestUpdateExistingCard extends TestBase
{

    @TestVariables(testrailData = "31:C9261", automatedBy = QA.Lam)
    @Test(description = "To verify that admin can freeze virtual card", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testFreezeVirtualCardByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 6;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to get token and login");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);

        testConfig.logStep("Execute Api /v1/debit-accounts to get debit account with specific currency");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api /v1/debit-cards to get card list on card page");
        Response getCardListResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetCardList, null);
        saasHelper.verifyApiResponse(getCardListResponse, SaasApiDetails.GetCardList, SaasJsonDetails.GetCardListResponse);

        testConfig.logStep("Execute Api /v1/debit-cards/{$cardUuid}/temporary-lock to freeze card");
        Response freezeCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.FreezeCard, SaasJsonDetails.FreezeCardRequestSchema);
        saasHelper.verifyApiResponse(freezeCardResponse, SaasApiDetails.FreezeCard, SaasJsonDetails.FreezeCardResponse);
    }

    @TestVariables(testrailData = "30:C9626", automatedBy = QA.Lam)
    @Test(description = "To verify that employee can freeze virtual card", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testFreezeVirtualCardByEmployeeUser(Config testConfig)
    {
        int userDetailsRowNum = 10;
        int addressDetailsRowNum = 1;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, userDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to login as Employee user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts to get debit account of SGD");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api /v1/access to get list of people then put user information for config data to assigned to budget " +
                " Then Execute Api /v1/budgets to create a budget with role type employee is budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, AccessEnums.RoleType.Employee);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget owner");
        saasHelper.assignNewPersonToBudget(accessHelper, AccessEnums.RoleType.FinanceTransferOnly, SaasEnums.BudgetMemberType.Owner, true);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses to trigger the right of user is budget owner and user can make transfer right on this budget ");
        Response budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetOwnerRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerResponse);

        testConfig.logStep("Execute Api /v1/debit-cards to issue virtual card for budget owner from budget above");
        saasHelper.setupCreateDebitCardRequestForBudgetPerson(SaasEnums.BudgetMemberType.Owner, AccessEnums.RoleType.FinanceTransferOnly, SaasEnums.CardTypeEnum.Virtual);
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Execute Api /v1/debit-cards/{$cardUuid}/temporary-lock to freeze card on card detail page");
        Response freezeCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.FreezeCard, SaasJsonDetails.FreezeCardRequestSchema);
        saasHelper.verifyApiResponse(freezeCardResponse, SaasApiDetails.FreezeCard, SaasJsonDetails.FreezeCardResponse);
    }

    @TestVariables(testrailData = "31:C9259", automatedBy = QA.Lam)
    @Test(description = "Verify that admin can freeze virtual card with USD", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testFreezeVirtualCardUSDByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 72;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Get token and Authenticate as an Admin user and get debit account with USD currency");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.USD);

        testConfig.logStep("Get list of people in the business of USD then get a person to assign virtual card to him");
        Response peopleResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelper.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelper.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Issue a virtual card and assign card to that person chosen from list above");
        Response issuedCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Get card detail of card issue above to verify on card detail");
        Response getCardDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetCardDetail, null);
        saasHelper.verifyApiResponse(getCardDetailResponse, SaasApiDetails.GetCardDetail, SaasJsonDetails.GetCardDetailResponse);

        testConfig.logStep("Execute Api /v1/debit-cards/{$cardUuid}/temporary-lock to freeze card and card status is now Frozen");
        Response freezeCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.FreezeCard, SaasJsonDetails.FreezeCardRequestSchema);
        saasHelper.verifyApiResponse(freezeCardResponse, SaasApiDetails.FreezeCard, SaasJsonDetails.FreezeCardResponse);
    }

    @TestVariables(testrailData = "31:C9274", automatedBy = QA.Lam)
    @Test(description = "Verify that admin can edit virtual card", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testEditVirtualCardByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 79;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Get token and Authenticate as an Admin user and get debit account with SGD currency");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Get list of people in the business of SGD then get a person to assign virtual card to him");
        Response peopleResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelper.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelper.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Issue a virtual card and assign card to that person chosen from list above");
        Response issuedCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Get card detail of card issue above to verify on card detail");
        Response getCardDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetCardDetail, null);
        saasHelper.verifyApiResponse(getCardDetailResponse, SaasApiDetails.GetCardDetail, SaasJsonDetails.GetCardDetailResponse);

        testConfig.logStep("Update card information: card name, card Description, card category, card color, card frequently, card amount " +
                "then verify update card successfully with correct information editing");
        Response updateCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdateCard, SaasJsonDetails.UpdateCardRequestSchema);
        saasHelper.verifyApiResponse(updateCardResponse, SaasApiDetails.UpdateCard, SaasJsonDetails.UpdateCardResponse);
    }
}