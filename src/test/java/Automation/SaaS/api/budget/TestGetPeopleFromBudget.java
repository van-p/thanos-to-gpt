package Automation.SaaS.api.budget;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessEnums.RoleType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestGetPeopleFromBudget extends TestBase
{

    @TestVariables(testrailData = "30:C5058", automatedBy = QA.Mukesh)
    @Test(description = "To verify that we cannot get budget people without authorization", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testGetBudgetPeopleWithoutAuthorization(Config testConfig)
    {
        int userDetailsRowNum = 4;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid and verify that response is accurate");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudgetWithoutAuthorizations, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudgetWithoutAuthorizations, SaasJsonDetails.ErrorMessageResponse);
    }

    @TestVariables(testrailData = "30:C5059", automatedBy = QA.Mukesh)
    @Test(description = "To verify that we cannot get budget people without header params", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testGetBudgetPeopleWithoutHeaders(Config testConfig)
    {
        int userDetailsRowNum = 5;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid and verify that response is accurate");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudgetWithoutHeaders, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudgetWithoutHeaders, SaasJsonDetails.ErrorMessageResponse);
    }

    @TestVariables(testrailData = "30:C5083", automatedBy = QA.Mukesh)
    @Test(description = "To verify that admin can get budget people", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testGetBudgetPeopleWithAdminRole(Config testConfig)
    {
        int userDetailsRowNum = 7;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetTripleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid and verify that response is accurate");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetSingleOwnerResponse);
    }
}