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
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasEnums.BudgetMemberType;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestAddOwnerMemberToBudget extends TestBase
{

    @TestVariables(testrailData = "30:C4988", automatedBy = QA.Mukesh)
    @Test(description = "To verify that Admin can Create-accesses - Add budget member/budget owner to existing budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testAddOwnerAndOrMemberToBudget(Config testConfig)
    {
        int userDetailsRowNum = 3;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget owner");
        saasHelper.assignNewPersonToBudget(accessHelper, RoleType.FinanceTransferOnly, SaasEnums.BudgetMemberType.Owner, true);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        Response budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetOwnerRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that new budget owner is added");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerResponse);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget member");
        saasHelper.assignNewPersonToBudget(accessHelper, RoleType.FinanceSubmitOnly, BudgetMemberType.Member, false);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that new budget member is added");
        budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetMultipleOwnerAndMemberResponse);
    }
}
