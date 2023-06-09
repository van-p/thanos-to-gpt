package Automation.Access.web;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.CompanyRole;
import Automation.Access.customer.helpers.AccessEnums.SubMenuItem;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.BudgetPage;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestInviteUserFlowsByBudget extends TestBase
{

    @TestVariables(testrailData = "6:C9948", automatedBy = QA.Quan)
    @Test(description = "To verify admin can invite user by adding in budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testInviteNonDirectorAdminUser(Config testConfig)
    {
        int userDetailsRowNum = 40;
        int budgetDetailsRowNum = 1;

        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "BudgetDetails"}, userDetailsRowNum, budgetDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Navigate to Customer Portal, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Verify landing on Budget page by clicking on menu");
        saasHelper.budgetPage = (BudgetPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Budgets, AfterNavigationPage.BudgetPage);

        testConfig.logStep("Invite Budget Owner to Business and verify full name info");
        saasHelper.budgetPage.createAndOpenDetailBudget(testConfig.testData.get("budgetName"));
        saasHelper.budgetPage.clickInviteNewUserButton();
        String email = DataGenerator.generateRandomString(7) + "@example.com";
        String fullName = DataGenerator.generateRandomString(9);
        saasHelper.budgetPage.inviteBudgetUserByCompanyRole(fullName, email, CompanyRole.Director);
        saasHelper.budgetPage.verifyIfAddedUsersFound(fullName);
    }
}
