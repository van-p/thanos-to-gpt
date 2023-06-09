package Automation.SaaS.web.budget;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessEnums.RoleType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.BudgetPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestBudgetCreationFlows extends TestBase
{

    @TestVariables(testrailData = "30:C5074", automatedBy = QA.Duc)
    @Test(description = "Admin - Creating New Budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testCreateSgdBudget(Config testConfig)
    {
        int userDetailsRowNum = 46;
        int budgetDetailsRowNum = 1;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Now, create a budget by filling all the relevant details");
        saasHelper.budgetPage.createBudget();

        testConfig.logStep("Verify that all the budget detail are correct");
        saasHelper.budgetPage.clickViewBudgetButton();
        saasHelper.budgetPage.verifyBudgetDetail(null);
    }

    @TestVariables(testrailData = "30:C5021", automatedBy = QA.Phong)
    @Test(description = "Admin - Creating New Idr Budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testCreateIdrBudget(Config testConfig)
    {
        int userDetailsRowNum = 18;
        int budgetDetailsRowNum = 3;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Now, create a budget by filling all the relevant details");
        saasHelper.budgetPage.createBudget();

        testConfig.logStep("Verify that all the budget detail are correct");
        saasHelper.budgetPage.clickViewBudgetButton();
        saasHelper.budgetPage.verifyBudgetDetail(null);
    }

    @TestVariables(testrailData = "S30:C5026", automatedBy = QA.Duc)
    @Test(description = "Verify that the admin can edit an existing Idr budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testEditBudget(Config testConfig)
    {
        int userDetailsRowNum = 48;
        int budgetDetailsRowNum = 2;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.IDR);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Get the created budget information and verify the information is correct");
        Response createBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema);
        saasHelper.verifyApiResponse(createBudgetResponse, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Edit and verify budget edited successfully");
        saasHelper.budgetPage.updateBudgetAndVerifyNewUpdatedBudget(createBudgetResponse.jsonPath().getString("frequency"), createBudgetResponse.jsonPath().getString("name"));
    }

    @TestVariables(testrailData = "30:C5090", automatedBy = QA.Phong)
    @Test(description = "Verify that admin can set new spend limit for budget by using quick action, expected result is the new spend limit is shown", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testSetBudgetSpendLimit(Config testConfig)
    {
        int userDetailsRowNum = 45;
        int budgetDetailsRowNum = 4;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Get the created budget information and verify the information is correct");
        Response createBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema);
        saasHelper.verifyApiResponse(createBudgetResponse, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Set new budget spend limit and verify that the new limit is updated");
        saasHelper.budgetPage.setAndVerifyNewBudgetSpendLimit(createBudgetResponse.jsonPath().getString("name"));
    }

    @TestVariables(testrailData = "S30:C5075", automatedBy = QA.Phong)
    @Test(description = "Verify that the admin can edit an existing Sgd budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testEditSgdBudget(Config testConfig)
    {
        int userDetailsRowNum = 47;
        int budgetDetailsRowNum = 5;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Get the created budget information and verify the information is correct");
        Response createBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema);
        saasHelper.verifyApiResponse(createBudgetResponse, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Edit and verify budget edited successfully");
        saasHelper.budgetPage.updateBudgetAndVerifyNewUpdatedBudget(createBudgetResponse.jsonPath().getString("frequency"), createBudgetResponse.jsonPath().getString("name"));
    }

    @TestVariables(testrailData = "S30:C5022", automatedBy = QA.Phong)
    @Test(description = "Verify that the admin can add and remove Budget owners and Budget members from Idr Budgets", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAddRemoveBOBMFromIdrBudget(Config testConfig)
    {
        int userDetailsRowNum = 57;
        int budgetDetailsRowNum = 6;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Idr Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.IDR);

        testConfig.logStep("Create new budget with Idr source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Get the created budget information and verify the information is correct");
        Response createBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema);
        saasHelper.verifyApiResponse(createBudgetResponse, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);

        new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, 6);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Add and Verify Budget owners are added to the budget");
        saasHelper.budgetPage.addAndVerifyBudgetPeopleAreAdded(SaasEnums.BudgetRole.Owner, createBudgetResponse.jsonPath().getString("name"));

        testConfig.logStep("Remove and Verify Budget owners are removed from the budget");
        saasHelper.budgetPage.removeAndVerifyBudgetPeopleAreRemoved(SaasEnums.BudgetRole.Owner);

        testConfig.logStep("Add and Verify Budget members are added to the budget");
        saasHelper.budgetPage.addAndVerifyBudgetPeopleAreAdded(SaasEnums.BudgetRole.Member, null);

        testConfig.logStep("Remove and Verify Budget members are removed from the budget");
        saasHelper.budgetPage.removeAndVerifyBudgetPeopleAreRemoved(SaasEnums.BudgetRole.Member);
    }

    @TestVariables(testrailData = "S30:C5029", automatedBy = QA.Phong)
    @Test(description = "Verify that the admin can add and remove Budget owners and Budget members from Sgd Budgets", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAddRemoveBOBMFromSgdBudget(Config testConfig)
    {
        int userDetailsRowNum = 75;
        int budgetDetailsRowNum = 7;
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Get Sgd Debit Account uuid of the business and set for the create budget step");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Create new budget with Sgd source of fund and admin is the budget owner");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);

        testConfig.logStep("Get the created budget information and verify the information is correct");
        Response createBudgetResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema);
        saasHelper.verifyApiResponse(createBudgetResponse, SaasApiDetails.CreateBudget, SaasJsonDetails.CreateBudgetResponse);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Now, navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Add and Verify Budget owners are added to the budget");
        saasHelper.budgetPage.addAndVerifyBudgetPeopleAreAdded(SaasEnums.BudgetRole.Owner, createBudgetResponse.jsonPath().getString("name"));

        testConfig.logStep("Remove and Verify Budget owners are removed from the budget");
        saasHelper.budgetPage.removeAndVerifyBudgetPeopleAreRemoved(SaasEnums.BudgetRole.Owner);

        testConfig.logStep("Add and Verify Budget members are added to the budget");
        saasHelper.budgetPage.addAndVerifyBudgetPeopleAreAdded(SaasEnums.BudgetRole.Member, null);

        testConfig.logStep("Remove and Verify Budget members are removed from the budget");
        saasHelper.budgetPage.removeAndVerifyBudgetPeopleAreRemoved(SaasEnums.BudgetRole.Member);
    }
}
