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

public class TestUpdateExistingBudget extends TestBase
{

    @TestVariables(testrailData = "30:C5003", automatedBy = QA.Mukesh)
    @Test(description = "To verify that Admin user can update budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testUpdateExistingBudget(Config testConfig)
    {
        int userDetailsRowNum = 2;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Update the budget and verify the updated budget information is correct");
        Response updateBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdateBudget, SaasJsonDetails.UpdateBudgetSingleOwnerRequestSchema);
        saasHelper.verifyApiResponse(updateBudgetResponse, SaasApiDetails.UpdateBudget, SaasJsonDetails.UpdateBudgetResponseWithoutCreditTransaction);
    }

    @TestVariables(testrailData = "30:C5013", automatedBy = QA.Phong)
    @Test(description = "To verify that Admin user can deactivate budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminDeactivateExistingBudget(Config testConfig)
    {
        int userDetailsRowNum = 73;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Deactivate the budget and verify the budget status is deactivated");
        Response deactivateBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeactivateBudget, null);
        saasHelper.verifyApiResponse(deactivateBudgetResponse, SaasApiDetails.DeactivateBudget, SaasJsonDetails.BudgetDeactivatedDetailResponse);
    }

    @TestVariables(testrailData = "30:C5010", automatedBy = QA.Phong)
    @Test(description = "To verify that Admin user can reactivate budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminReactivateExistingBudget(Config testConfig)
    {
        int userDetailsRowNum = 74;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Deactivate the budget and verify the budget status is deactivated");
        Response deactivateBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.DeactivateBudget, null);
        saasHelper.verifyApiResponse(deactivateBudgetResponse, SaasApiDetails.DeactivateBudget, SaasJsonDetails.BudgetDeactivatedDetailResponse);

        testConfig.logStep("Reactivate the budget and verify the budget status is active");
        Response reactivateBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ReactivateBudget, SaasJsonDetails.BudgetReactivatedDetailRequest);
        saasHelper.verifyApiResponse(reactivateBudgetResponse, SaasApiDetails.ReactivateBudget, SaasJsonDetails.BudgetReactivatedDetailResponse);
    }

    @TestVariables(testrailData = "30:C5013", automatedBy = QA.Mukesh)
    @Test(description = "To verify that Admin user can deactivate existing budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminUserCanFreezeBudget(Config testConfig)
    {
        int userDetailsRowNum = 20;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/budgets/{budgetUuid}/deactivate with valid budget uuid and verify that response is accurate");
        Response updateBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.FreezeBudget, null);
        saasHelper.verifyApiResponse(updateBudgetResponse, SaasApiDetails.FreezeBudget, SaasJsonDetails.FrozenBudgetResponse);
    }

    @TestVariables(testrailData = "30:C5010", automatedBy = QA.Mukesh)
    @Test(description = "To verify that Admin user can reactivate deactivated budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAdminUserCanUnfreezeBudget(Config testConfig)
    {
        int userDetailsRowNum = 21;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetDoubleDebitAccountsResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/budgets/{budgetUuid}/deactivate with valid budget uuid and verify that response is accurate");
        Response updateBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.FreezeBudget, null);
        saasHelper.verifyApiResponse(updateBudgetResponse, SaasApiDetails.FreezeBudget, SaasJsonDetails.FrozenBudgetResponse);

        testConfig.logStep("Execute Api /v1/budgets/{budgetUuid}/deactivate with valid budget uuid and verify that response is accurate");
        Response unfreezeBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UnfreezeBudget, SaasJsonDetails.UnfreezeBudgetRequestSchema);
        saasHelper.verifyApiResponse(unfreezeBudgetResponse, SaasApiDetails.UnfreezeBudget, SaasJsonDetails.UnfreezeBudgetResponse);
    }
}