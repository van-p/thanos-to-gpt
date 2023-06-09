package Automation.SaaS.web.budget;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.BudgetPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestBudgetUpdationFlows extends TestBase
{

    @TestVariables(testrailData = "30:C5027", automatedBy = QA.Sunil)
    @Test(description = "Verify that Admin can reactivate a budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testVerifyThatAdminCanReactivateABudget(Config testConfig)
    {
        int userDetailsRowNum = 76;
        int budgetDetailsRowNum = 8;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login as admin");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Create a budget by filling all the relevant details");
        saasHelper.budgetPage.createBudget();

        testConfig.logStep("Freeze the created budget and verify");
        saasHelper.budgetPage.freezeBudgetAndVerify();

        testConfig.logStep("Unfreeze the frozen budget and verify");
        saasHelper.budgetPage.unfreezeBudgetAndVerify();
    }

    @TestVariables(testrailData = "S30:C5023", automatedBy = QA.Phong)
    @Test(description = "Verify that the admin can freeze Idr Budgets", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testFreezeIdrBudget(Config testConfig)
    {
        int userDetailsRowNum = 77;
        int budgetDetailsRowNum = 9;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login as admin");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Create a budget by filling all the relevant details");
        saasHelper.budgetPage.createBudget();

        testConfig.logStep("Freeze the created budget and verify");
        saasHelper.budgetPage.freezeBudgetAndVerify();
    }

    @TestVariables(testrailData = "S30:C5076", automatedBy = QA.Phong)
    @Test(description = "Verify that the admin can freeze Sgd Budgets", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testFreezeSgdBudget(Config testConfig)
    {
        int userDetailsRowNum = 78;
        int budgetDetailsRowNum = 10;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login as admin");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(AccessEnums.SubMenuItem.Budgets, AccessEnums.AfterNavigationPage.BudgetPage);

        testConfig.logStep("Create a budget by filling all the relevant details");
        saasHelper.budgetPage.createBudget();

        testConfig.logStep("Freeze the created budget and verify");
        saasHelper.budgetPage.freezeBudgetAndVerify();
    }
}
