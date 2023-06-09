package Automation.SaaS.api.cards;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.dash.api.DashSaasApiDetails;
import Automation.SaaS.dash.api.DashSaasJsonDetails;
import Automation.SaaS.dash.helpers.DashSaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestCardTransferApis extends TestBase
{

    @TestVariables(testrailData = "31:C9202", automatedBy = QA.Lam)
    @Test(description = "To verify that admin can make card transaction by virtual card", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testCardTransactionVirtualCardByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 56;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);
        DashSaasHelper dashSaasHelper = new DashSaasHelper(testConfig, 1);
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to get token and Authenticate as an Admin user" +
                "then get debit account with SGD currency");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
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

        testConfig.logStep("Login into dash with valid email and password user");
        dashAccessHelper.doLogin();

        testConfig.logStep("Make Card transaction for debit virtual card, will receive response 00 from BE then card can make successfully " +
                "and different 00 then make card transaction failed ");
        Response cardTransactionResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDDebitCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);
    }

    @TestVariables(testrailData = "31:C9211", automatedBy = QA.Lam)
    @Test(description = "Verify that admin can make virtual card transaction refund", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testCardTransactionRefundVirtualCardByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 56;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);
        DashSaasHelper dashSaasHelper = new DashSaasHelper(testConfig, 1);
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to get token and Authenticate as an Admin user" +
                "then get debit account with SGD currency");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
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

        testConfig.logStep("Login into dash with valid email and password user");
        dashAccessHelper.doLogin();

        testConfig.logStep("Make Card transaction for debit virtual card with currency is SGD, will receive response 00 from BE then card can make successfully " +
                "and different 00 then make card transaction failed ");
        Response cardTransactionResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDDebitCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);

        testConfig.logStep("Make Card transaction Refund for debit virtual card, Based on the Previous card transaction with currency SGD " +
                "The output response is 00 then card can refund successfully and different value the card refund failed");
        Response cardTransactionRefundResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDReversalCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionRefundResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);
    }

    @TestVariables(testrailData = "31:C9205", automatedBy = QA.Lam)
    @Test(description = "Verify that employee can make virtual card transaction", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testCardTransactionVirtualCardByEmployee(Config testConfig)
    {
        int adminDetailsRowNum = 56;
        int employeeDetailsRowNum = 58;
        int addressDetailsRowNum = 1;
        DashSaasHelper dashSaasHelper = new DashSaasHelper(testConfig, 1);
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        SaasHelper saasHelperAdmin = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, adminDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelperAdmin = new AccessHelper(testConfig, adminDetailsRowNum);
        PaymentHelper paymentHelperAdmin = new PaymentHelper(testConfig, adminDetailsRowNum);

        testConfig.logStep("Admin: Login as Admin user and get debit account of SGD");
        accessHelperAdmin.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelperAdmin.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Admin: Get list of people then put user information for config data to assigned to budget " +
                " Then Create a budget, assigned with role type employee is budget owner");
        saasHelperAdmin.createBudget(accessHelperAdmin, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, AccessEnums.RoleType.Employee);
        saasHelperAdmin.assignNewPersonToBudget(accessHelperAdmin, AccessEnums.RoleType.FinanceTransferOnly, SaasEnums.BudgetMemberType.Member, true);

        testConfig.logStep("Admin: Trigger the right of User is budget member and user can not make transfer right on this budget ");
        Response budgetDetailResponse = saasHelperAdmin.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelperAdmin.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        SaasHelper saasHelperEmployee = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, employeeDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelperEmployee = new AccessHelper(testConfig, employeeDetailsRowNum);
        PaymentHelper paymentHelperEmployee = new PaymentHelper(testConfig, employeeDetailsRowNum);

        testConfig.logStep("Employee: Login as Employee user and get debit account of SGD");
        accessHelperEmployee.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelperEmployee.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Employee: Get List of people to assigned card, if budget member is existed then assigned to that user " +
                "because employee only assigned card to that BM or themselves");
        Response peopleResponse = accessHelperEmployee.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelperEmployee.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelperEmployee.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Employee: Issue a card and assigned to that Budget member above");
        Response issuedCardResponse = saasHelperEmployee.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelperEmployee.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Employee: Get card detail of card issue above to verify on card detail page and get cardhashId to make card transaction");
        Response getCardDetailResponse = saasHelperEmployee.sendRequestAndGetResponse(SaasApiDetails.GetCardDetail, null);
        saasHelperEmployee.verifyApiResponse(getCardDetailResponse, SaasApiDetails.GetCardDetail, SaasJsonDetails.GetCardDetailResponse);

        testConfig.logStep("Login into dash with valid email and password user");
        dashAccessHelper.doLogin();

        testConfig.logStep("Make Card transaction for debit virtual card with currency is SGD, will receive response 00 from back end it means make card transaction successfully. if not" +
                "make card transaction failed");
        Response cardTransactionResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDDebitCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);
    }

    @TestVariables(testrailData = "31:C9213", automatedBy = QA.Lam)
    @Test(description = "Verify that employee can make virtual refund card transaction", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testCardTransactionRefundVirtualCardByEmployee(Config testConfig)
    {
        int adminDetailsRowNum = 56;
        int employeeDetailsRowNum = 58;
        int addressDetailsRowNum = 1;
        DashSaasHelper dashSaasHelper = new DashSaasHelper(testConfig, 1);
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        SaasHelper saasHelperAdmin = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, adminDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelperAdmin = new AccessHelper(testConfig, adminDetailsRowNum);
        PaymentHelper paymentHelperAdmin = new PaymentHelper(testConfig, adminDetailsRowNum);

        testConfig.logStep("Admin: Login as Admin user and get debit account of SGD");
        accessHelperAdmin.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelperAdmin.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Admin: Get list of people then put user information for config data to assigned to budget " +
                " Then Create a budget, assigned with role type employee is budget owner");
        saasHelperAdmin.createBudget(accessHelperAdmin, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, AccessEnums.RoleType.Employee);
        saasHelperAdmin.assignNewPersonToBudget(accessHelperAdmin, AccessEnums.RoleType.FinanceTransferOnly, SaasEnums.BudgetMemberType.Member, true);

        testConfig.logStep("Admin: Trigger the right of User is budget member and user can not make transfer right on this budget ");
        Response budgetDetailResponse = saasHelperAdmin.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelperAdmin.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        SaasHelper saasHelperEmployee = new SaasHelper(testConfig, new String[]{"UserDetails", "AddressDetails"}, employeeDetailsRowNum, addressDetailsRowNum);
        AccessHelper accessHelperEmployee = new AccessHelper(testConfig, employeeDetailsRowNum);
        PaymentHelper paymentHelperEmployee = new PaymentHelper(testConfig, employeeDetailsRowNum);

        testConfig.logStep("Employee: Login as Employee user and get debit account of SGD");
        accessHelperEmployee.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelperEmployee.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Employee: Get List of people to assigned card, if budget member is existed then assigned to that user " +
                "because employee only assigned card to that BM or themselves");
        Response peopleResponse = accessHelperEmployee.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelperEmployee.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelperEmployee.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Employee: Issue a card and assigned to that BM above");
        Response issuedCardResponse = saasHelperEmployee.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelperEmployee.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Employee: Get card detail of card issue above to verify on card detail page and get cardhashId to make card transaction");
        Response getCardDetailResponse = saasHelperEmployee.sendRequestAndGetResponse(SaasApiDetails.GetCardDetail, null);
        saasHelperEmployee.verifyApiResponse(getCardDetailResponse, SaasApiDetails.GetCardDetail, SaasJsonDetails.GetCardDetailResponse);

        testConfig.logStep("Login into dash with valid email and password user");
        dashAccessHelper.doLogin();

        testConfig.logStep("Make Card transaction for debit virtual card with currency is SGD, will receive response 00 from BE then card can make successfully " +
                "and different 00 then make card transaction failed ");
        Response cardTransactionResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDDebitCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);

        testConfig.logStep("Make Card transaction Refund for debit virtual card, Based on the Previous card transaction with currency SGD " +
                "The output response is 00 it means can refund card transaction successfully");
        Response cardTransactionRefundResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDReversalCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionRefundResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);
    }

    @TestVariables(testrailData = "31:C9194", automatedBy = QA.Lam)
    @Test(description = "Verify that admin can get debit card transaction", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testGetVirtualDebitCardTransactionByAdmin(Config testConfig)
    {
        int userDetailsRowNum = 56;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);
        DashSaasHelper dashSaasHelper = new DashSaasHelper(testConfig, 1);
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);

        testConfig.logStep("Get token and Authenticate as an Admin user then get debit account with SGD currency");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Get list of people in the business of SGD then get a person to assign card to him");
        Response peopleResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetListOfPeople, null);
        accessHelper.verifyApiResponse(peopleResponse, AccessApiDetails.GetListOfPeople, AccessJsonDetails.GetListOfPeopleSuccessfulResponse);
        saasHelper.setupIssueCardRequestParams(peopleResponse, SaasEnums.CardTypeEnum.Virtual);

        testConfig.logStep("Issue a virtual card and assign card to that person chosen from list above");
        Response issuedCardResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardRequestSchema);
        saasHelper.verifyApiResponse(issuedCardResponse, SaasApiDetails.IssueNewCard, SaasJsonDetails.IssueNewVirtualCardResponse);

        testConfig.logStep("Get card detail of card issue above to verify on card detail page");
        Response getCardDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetCardDetail, null);
        saasHelper.verifyApiResponse(getCardDetailResponse, SaasApiDetails.GetCardDetail, SaasJsonDetails.GetCardDetailResponse);

        testConfig.logStep("Login into dash with valid email and password user");
        dashAccessHelper.doLogin();

        testConfig.logStep("Make Card transaction for debit virtual card with currency is SGD, The output response is 00 it means can make card transaction successfully");
        Response cardTransactionResponse = dashSaasHelper.sendRequestAndGetResponse(DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeSGDDebitCardTransactionRequestSchema);
        dashSaasHelper.verifyApiResponse(cardTransactionResponse, DashSaasApiDetails.MakeDebitCardTransaction, DashSaasJsonDetails.MakeDebitCardTransactionResponse);

        testConfig.logStep("Authenticate as an Admin user then get debit account with SGD currency again");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, AccessEnums.BusinessType.Business);
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Get Debit card transaction detail on card transaction detail page from card transaction above ");
        Response getCardTransactionResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetDebitCardTransaction, null);
        saasHelper.verifyApiResponse(getCardTransactionResponse, SaasApiDetails.GetDebitCardTransaction, SaasJsonDetails.GetDebitCardTransactionSGDResponse);
    }
}