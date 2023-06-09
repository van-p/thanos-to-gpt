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
import Automation.SaaS.customer.helpers.SaasEnums.BudgetMemberType;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestModifyAccessBudget extends TestBase
{

    @TestVariables(testrailData = "30:C4999", automatedBy = QA.Duc)
    @Test(description = "Verify admin can update access of budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testUpdateAccessBudget(Config testConfig)
    {
        int testDataRow = 54;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetUserInfo"}, testDataRow, 1);
        AccessHelper accessHelper = new AccessHelper(testConfig, testDataRow);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, testDataRow);
        String testUserUuid = testConfig.testData.get("userUuid");
        String userName = testConfig.testData.get("userName");

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Execute Api /v1/access with valid budget_uuid and get one person to assign to budget member");
        saasHelper.addUserInfoToBudgetRequest(testUserUuid, userName, BudgetMemberType.Member, false);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        Response expected = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        testConfig.putRunTimeProperty("expectedResponse", expected.body().asString());

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/update-access to be budget owner can transfer");
        Response response = saasHelper.updateBudgetUserAccess("owner", true, testUserUuid, SaasJsonDetails.UpdateAccessBudgetSchema);

        testConfig.logStep("Verify update-access to be budget owner can transfer");
        saasHelper.verifyApiResponse(response, SaasApiDetails.PutUpdateAccessBudget, SaasJsonDetails.UpdateAccessBudgetResponse);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/update-access to be budget owner cannot transfer");
        response = saasHelper.updateBudgetUserAccess("owner", false, testUserUuid, SaasJsonDetails.UpdateAccessBudgetSchema);

        testConfig.logStep("Verify update-access to be budget owner cannot transfer");
        saasHelper.verifyApiResponse(response, SaasApiDetails.PutUpdateAccessBudget, SaasJsonDetails.UpdateAccessBudgetResponse);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/update-access to be budget member");
        response = saasHelper.updateBudgetUserAccess("member", false, testUserUuid, SaasJsonDetails.UpdateAccessBudgetSchema);

        testConfig.logStep("Verify update-access to be budget owner cannot transfer");
        saasHelper.verifyApiResponse(response, SaasApiDetails.PutUpdateAccessBudget, SaasJsonDetails.UpdateAccessBudgetResponse);
    }

    @TestVariables(testrailData = "30:C4993", automatedBy = QA.Mukesh)
    @Test(description = "To verify that admin can revoke access of budget member/budget owner to existing budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testRevokeAccessOfBudgetOwnerAndMember(Config testConfig)
    {
        int customerDetailsRowNum = 8;
        SaasHelper saasHelper = new SaasHelper(testConfig, customerDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, customerDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetMultipleOwnerRequestSchema, RoleType.Director, RoleType.FinanceTransferOnly);

        testConfig.logStep("Execute Api /v1/access with valid budgetUuid and get one person to assign to budget member");
        saasHelper.assignNewPersonToBudget(accessHelper, RoleType.FinanceViewOnly, BudgetMemberType.Member, false);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/create-accesses with valid info and verify that response is accurate");
        Response budgetDetailResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.AddBudgetMemberRequestSchema);
        saasHelper.verifyApiResponse(budgetDetailResponse, SaasApiDetails.AddOwnerOrMemberToBudget, SaasJsonDetails.BudgetDetailResponse);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/revoke-accesses with valid info and verify that response is accurate");
        saasHelper.revokeBudgetPersonWithType(BudgetMemberType.Owner);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that we don't see the revoked budget owner");
        Response budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetOwnerRevokedResponse);

        testConfig.logStep("Execute Api /v1/budgets/{$budgetUuid}/revoke-accesses with valid info and verify that response is accurate");
        saasHelper.revokeBudgetPersonWithType(BudgetMemberType.Member);

        testConfig.logStep("Execute Api /v1/budgets/{id}/people with valid budget uuid to verify that we don't see the revoked budget member");
        budgetPeopleResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetPeopleFromBudget, null);
        saasHelper.verifyApiResponse(budgetPeopleResponse, SaasApiDetails.GetPeopleFromBudget, SaasJsonDetails.BudgetOwnerAndMemberRevokedResponse);
    }
}
