package Automation.SaaS.api.cards;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessEnums.RoleType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasEnums.BudgetMemberType;
import Automation.SaaS.customer.helpers.SaasEnums.CardTypeEnum;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestCardCreationApis extends TestBase
{

    @TestVariables(testrailData = "30:C9223", automatedBy = QA.Mukesh)
    @Test(description = "To verify that admin can issue new virtual card for non-budget member", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testIssueNewCardForNonBudgetPerson(Config testConfig)
    {
        int userDetailsRowNum = 6;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api /v1/peoples/{businessUuid} with valid credentials and extract the person_uuid");
        Response peopleResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelper.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelper.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Execute Api /v1/debit-cards with valid information and verify that response is accurate");
        Response issuedCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);
    }

    @TestVariables(testrailData = "30:C9212", automatedBy = QA.Mukesh)
    @Test(description = "To verify that employee user who is budget owner can issue new virtual card for other budget owner/member", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testIssueNewVirtualCardByEmployeeUser(Config testConfig)
    {
        int userDetailsRowNum = 9;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Employee);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget owner");
        saasHelper.assignNewPersonToBudget(accessHelper, AccessEnums.RoleType.FinanceTransferOnly, SaasEnums.BudgetMemberType.Owner, true);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        Response budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetOwnerRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that new budget owner is added");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerResponse);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget member");
        saasHelper.assignNewPersonToBudget(accessHelper, AccessEnums.RoleType.FinanceSubmitOnly, SaasEnums.BudgetMemberType.Member, false);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that new budget member is added");
        budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerAndMemberResponse);

        testConfig.logStep("Execute Api /v1/debit-cards with valid information and verify that virtual card is issued for budget owner");
        verifyIfVirtualCardIssuedForBudgetPerson(saasHelper, SaasEnums.BudgetMemberType.Owner, AccessEnums.RoleType.FinanceTransferOnly);

        testConfig.logStep("Execute Api /v1/debit-cards with valid information and verify that virtual card is issued for budget member");
        verifyIfVirtualCardIssuedForBudgetPerson(saasHelper, SaasEnums.BudgetMemberType.Member, AccessEnums.RoleType.FinanceSubmitOnly);
    }

    @TestVariables(testrailData = "30:C9227", automatedBy = QA.Mukesh)
    @Test(description = "To verify that employee user who is budget owner can issue new blue card for other budget owner/member", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testIssueNewBlueCardByEmployeeUser(Config testConfig)
    {
        int userDetailsRowNum = 10;
        int addressDetailsRowNum = 1;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, userDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Employee);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget owner");
        saasHelper.assignNewPersonToBudget(accessHelper, AccessEnums.RoleType.FinanceTransferOnly, SaasEnums.BudgetMemberType.Owner, true);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        Response budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetOwnerRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that new budget owner is added");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerResponse);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget member");
        saasHelper.assignNewPersonToBudget(accessHelper, AccessEnums.RoleType.FinanceSubmitOnly, SaasEnums.BudgetMemberType.Member, false);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that new budget member is added");
        budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerAndMemberResponse);

        testConfig.logStep("Execute Api /v1/debit-cards with valid information and verify that physical card is issued for budget owner");
        verifyIfPhysicalCardIssuedForBudgetPerson(saasHelper, SaasEnums.BudgetMemberType.Owner, AccessEnums.RoleType.FinanceTransferOnly);

        testConfig.logStep("Execute Api /v1/debit-cards with valid information and verify that physical card is issued for budget member");
        verifyIfPhysicalCardIssuedForBudgetPerson(saasHelper, SaasEnums.BudgetMemberType.Member, AccessEnums.RoleType.FinanceSubmitOnly);
    }

    @TestVariables(testrailData = "31:C9190", automatedBy = QA.Lam)
    @Test(description = "To verify that Admin can get virtual card detail information correctly on card detail page", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testGetCardDetailByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 6;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to get token and Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);

        testConfig.logStep("Execute Api /v1/debit-accounts to get debit account with SGD currency ");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api /v1/peoples/{businessUuid} to get list of people in the business of SGD then get a person to assign card to him");
        Response peopleResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelper.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelper.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Execute Api /v1/debit-cards to issue a virtual card and assign card to that person chosen from list above");
        Response issuedCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Execute Api /v1/debit-cards/{$cardUuid}/show-without-unmask to get card detail of card issue above to verify on card detail page");
        Response getCardDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetCardDetail, null);
        saasHelper.verifyApiResponse(getCardDetailResponse, SaasApiDetails.GetCardDetail, SaasJsonDetails.GetCardDetailResponse);
    }

    @TestVariables(testrailData = "31:C9228", automatedBy = QA.Lam)
    @Test(description = "Verify that Finance can issue new virtual card for budget member", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testIssueNewCardForNonBudgetPersonByFinance(Config testConfig)
    {
        int adminDetailsRowNum = 70;
        int financeDetailsRowNum = 71;
        int addressDetailsRowNum = 1;
        SaasHelper saasHelperAdmin = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, adminDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelperAdmin = new AccessHelper(testConfig, adminDetailsRowNum);
        PaymentHelper paymentHelperAdmin = new PaymentHelper(testConfig, adminDetailsRowNum);

        testConfig.logStep("Admin: Login as Admin user and get debit account of SGD");
        accessHelperAdmin.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelperAdmin.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Admin: Create a budget then assigned Finance is Budget Owner and Employee is Budget member");
        saasHelperAdmin.createBudget(accessHelperAdmin, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.FinanceTransferOnly);
        saasHelperAdmin.assignNewPersonToBudget(accessHelperAdmin, RoleType.Employee, SaasEnums.BudgetMemberType.Member, true);

        testConfig.logStep("Admin: Trigger the right of Employee is budget member and user can not make transfer right on this budget ");
        Response budgetDetailResponse = saasHelperAdmin.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelperAdmin.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        SaasHelper saasHelperFinance = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, financeDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelperFinance = new AccessHelper(testConfig, financeDetailsRowNum);
        PaymentHelper paymentHelperFinance = new PaymentHelper(testConfig, financeDetailsRowNum);

        testConfig.logStep("Finance: Login as Finance user and get debit account of SGD");
        accessHelperFinance.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelperFinance.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Finance: Get List of people to assigned card, if budget member is existed then assigned to that user " +
                "because Finance only assigned card to that Budget Member or themselves");
        Response peopleResponse = accessHelperFinance.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelperFinance.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelperFinance.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Finance: Issue a card and assigned to that Budget Member above");
        Response issuedCardResponse = saasHelperFinance.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelperFinance.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);
    }

    private void verifyIfPhysicalCardIssuedForBudgetPerson(SaasHelper saasHelper, BudgetMemberType budgetMemberType, RoleType roleType)
    {
        saasHelper.setupCreateDebitCardRequestForBudgetPerson(budgetMemberType, roleType, CardTypeEnum.VirtualPhysical);
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewPhysicalCardRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewPhysicalCardResponse);
    }

    private void verifyIfVirtualCardIssuedForBudgetPerson(SaasHelper saasHelper, BudgetMemberType budgetMemberType, RoleType roleType)
    {
        saasHelper.setupCreateDebitCardRequestForBudgetPerson(budgetMemberType, roleType, CardTypeEnum.Virtual);
        Response response = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(response, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);
    }
}
