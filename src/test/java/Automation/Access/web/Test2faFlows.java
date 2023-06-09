package Automation.Access.web;


import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.*;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Access.customer.web.SecurityPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class Test2faFlows extends TestBase
{

    @TestVariables(testrailData = "8:C113", automatedBy = QA.TanHo)
    @Test(description = "Verify Personal 2FA is enabled successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases", "2fa"})
    public void testEnablePersonal2fa(Config testConfig)
    {
        int userDetailsRowNum = 30;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login API with admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Prepare data: Disable Organisation 2FA using API");
        Response getOrganisation2FaResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeleteBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2FaResponse, AccessApiDetails.DeleteBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Prepare data: Disable Personal 2FA using API");
        Response getPersonal2FaResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeletePersonal2fa, null);
        accessHelper.verifyApiResponse(getPersonal2FaResponse, AccessApiDetails.DeletePersonal2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Enable Personal 2FA");
        accessHelper.securityPage.clickOn2faCardAdminToggle();

        testConfig.logStep("Validate notification message & Personal 2FA state is true");
        accessHelper.securityPage.validateToastMessage(AccessHelper.accessStaticDataBase.getEnabledPersonal2faSuccessMessage());
        accessHelper.securityPage.verifyPersonal2faStateIsCorrect(true);

        testConfig.logStep("Log out and re-login");
        accessHelper.dashBoardPage.clickLogoutButton();
        accessHelper.loginPage.enterEmailAndPassword(testConfig.getRunTimeProperty("username"), testConfig.getRunTimeProperty("password"));

        testConfig.logStep("Verify 2FA modal should be present");
        accessHelper.loginPage.verifyTwoFactorAuthenticationPage(true, UserNameType.Email);
    }

    @TestVariables(testrailData = "8:C112", automatedBy = QA.TanHo)
    @Test(description = "Verify Personal 2FA is disabled successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases", "2fa"})
    public void testDisablePersonal2fa(Config testConfig)
    {
        int userDetailsRowNum = 31;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login API with admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Prepare data: Disable Organisation 2FA using API");
        Response getOrganisation2FaResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeleteBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2FaResponse, AccessApiDetails.DeleteBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Prepare data: Enable Personal 2FA using API");
        Response getPersonal2FaResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPersonal2fa, null);
        accessHelper.verifyApiResponse(getPersonal2FaResponse, AccessApiDetails.PutPersonal2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Disable Personal 2FA");
        accessHelper.securityPage.clickOn2faCardAdminToggle();
        accessHelper.securityPage.disable2fa();

        testConfig.logStep("Validate notification message & Personal 2FA state is false");
        accessHelper.securityPage.validateToastMessage(AccessHelper.accessStaticDataBase.getDisabledPersonal2faSuccessMessage());
        accessHelper.securityPage.verifyPersonal2faStateIsCorrect(false);

        testConfig.logStep("Log out and re-login");
        accessHelper.dashBoardPage.clickLogoutButton();
        accessHelper.loginPage.enterEmailAndPassword(testConfig.getRunTimeProperty("username"), testConfig.getRunTimeProperty("password"));

        testConfig.logStep("Verify 2FA modal should not be present");
        accessHelper.loginPage.verifyTwoFactorAuthenticationPage(false, UserNameType.Email);
    }

    @TestVariables(testrailData = "8:C116", automatedBy = QA.TanHo)
    @Test(description = "Verify Organisation 2FA is enabled successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases", "2fa"})
    public void testEnableOrganisation2fa(Config testConfig)
    {
        int userDetailsRowNum = 32;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login API with admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Prepare data: Disable Organisation 2FA using API");
        Response getOrganisation2FaResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeleteBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2FaResponse, AccessApiDetails.DeleteBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Enable Organisation 2FA");
        accessHelper.securityPage.clickOn2faCardBusinessToggle();

        testConfig.logStep("Validate notification message & Personal 2FA state is true");
        accessHelper.securityPage.validateToastMessage(AccessHelper.accessStaticDataBase.getEnabledOrganisation2faSuccessMessage());
        accessHelper.securityPage.verifyOrganisation2faStateIsCorrect(true);

        testConfig.logStep("Log out and re-login");
        accessHelper.dashBoardPage.clickLogoutButton();
        accessHelper.loginPage.enterEmailAndPassword(testConfig.getRunTimeProperty("username"), testConfig.getRunTimeProperty("password"));

        testConfig.logStep("Verify 2FA modal should be present");
        accessHelper.loginPage.verifyTwoFactorAuthenticationPage(true, UserNameType.Email);
    }

    @TestVariables(testrailData = "8:C117", automatedBy = QA.TanHo)
    @Test(description = "Verify Organisation 2FA is disabled successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases", "2fa"})
    public void testDisableOrganisation2fa(Config testConfig)
    {
        int userDetailsRowNum = 33;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login API with admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Prepare data: Enable Organisation 2FA using API");
        Response getOrganisation2FaResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2FaResponse, AccessApiDetails.PutBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Disable Organisation 2FA");
        accessHelper.securityPage.clickOn2faCardBusinessToggle();
        accessHelper.securityPage.disable2fa();

        testConfig.logStep("Validate notification message & Personal 2FA state is false");
        accessHelper.securityPage.validateToastMessage(AccessHelper.accessStaticDataBase.getDisabledOrganisation2faSuccessMessage());
        accessHelper.securityPage.verifyOrganisation2faStateIsCorrect(false);

        testConfig.logStep("Log out and re-login");
        accessHelper.dashBoardPage.clickLogoutButton();
        accessHelper.loginPage.enterEmailAndPassword(testConfig.getRunTimeProperty("username"), testConfig.getRunTimeProperty("password"));

        testConfig.logStep("Verify 2FA modal should be present");
        accessHelper.loginPage.verifyTwoFactorAuthenticationPage(true, UserNameType.Email);
    }

}
