package Automation.Access.web;

import Automation.Access.customer.helpers.AccessEnums.*;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Access.customer.web.UserAccessPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

import java.util.List;

public class TestInviteUserFlowsByUsersAndAccess extends TestBase
{

    @TestVariables(testrailData = "6:C4878", automatedBy = QA.Mukesh)
    @Test(description = "To verify that When Director Admin of Singapore Business user invite Non-Director Admin, None of the Documents(AOL/LOA etc) is ask to sign", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testInviteNonDirectorAdminUser(Config testConfig)
    {
        int userDetailsRowNum = 2;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to New users popup and invite user as non-admin user");
        accessHelper.userAccessPage = (UserAccessPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Users, AfterNavigationPage.UserAccessPage);
        List<String> users = accessHelper.inviteUsersWithCompanyRoleAndAccessRole(CompanyRole.NonDirector, AccessRole.Admin);

        testConfig.logStep("Verify that invited users could be searched");
        accessHelper.userAccessPage.verifyIfAddedUsersFound(users);
    }

    @TestVariables(testrailData = "6:C9945", automatedBy = QA.Sanjeevan)
    @Test(description = "To verify that admin is able to invite new user on MUA page", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testInviteDirectorAdminUser(Config testConfig)
    {
        int userDetailsRowNum = 9;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to New users popup and invite user as director admin user");
        accessHelper.userAccessPage = (UserAccessPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Users, AfterNavigationPage.UserAccessPage);
        List<String> users = accessHelper.inviteUsersWithCompanyRoleAndAccessRole(CompanyRole.Director, AccessRole.Admin);

        testConfig.logStep("Verify that invited users could be searched");
        accessHelper.userAccessPage.verifyIfAddedUsersFound(users);
    }

    @TestVariables(testrailData = "6:C4936", automatedBy = QA.Sanjeevan)
    @Test(description = "To verify that Send Invite CTA is not enabled if error is present in user details", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testSendInviteCtaDisabledInCaseOfError(Config testConfig)
    {
        int userDetailsRowNum = 10;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to New users popup and invite user as director admin user");
        accessHelper.userAccessPage = (UserAccessPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Users, AfterNavigationPage.UserAccessPage);

        testConfig.logStep("Click on +New user(s) button to add new users");
        accessHelper.userAccessPage.clickOnNewUserButton();

        testConfig.logStep("Verify that Send Invite CTA is not enabled if error is present in user details form");
        accessHelper.userAccessPage.verifySendInviteButtonDisabledForFormError();
    }

    @TestVariables(testrailData = "6:C4872", automatedBy = QA.Priya)
    @Test(description = "To verify that LOA/AOL must not come when Admin User update the access role of any user", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testLoaNotComingWhenUpdatingAccessRole(Config testConfig)
    {
        int userDetailsRowNum = 21;
        int userRowNum = 2;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to the Users And Access Page");
        accessHelper.userAccessPage = (UserAccessPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Users, AfterNavigationPage.UserAccessPage);

        testConfig.logStep("Click on User row");
        accessHelper.userAccessPage.selectUser(userRowNum);

        testConfig.logStep("Click on Modify Access Button");
        accessHelper.userAccessPage.clickOnModifyAccessButton();

        testConfig.logStep("Click on Update Access Role Button");
        accessHelper.userAccessPage.clickOnUpdateAccessRoleButton();

        testConfig.logStep(" Update Role");
        String updatedRole = accessHelper.userAccessPage.updateRole(userRowNum);

        testConfig.logStep("Verify that LOA/AOL must not come and toast message states 'Username's account access updated'");
        accessHelper.userAccessPage.verifyUpdateRoleToastMessage(userRowNum);

        testConfig.logStep("Verify that Role has been updated or not");
        accessHelper.userAccessPage.verifyIfRoleUpdated(userRowNum, updatedRole);
    }
}