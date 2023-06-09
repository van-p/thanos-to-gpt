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

public class TestDefaultOtpFlows extends TestBase
{

    @TestVariables(testrailData = "8:C13784", automatedBy = QA.TanHo)
    @Test(description = "Verify Email is set as default otp successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testEmailSetAsDefaultOtp(Config testConfig)
    {
        int userDetailsRowNum = 64;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Prepare data: Login with admin user and set phone as default otp using API");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
        Response getOptionsOtpChannelResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetOptionsOtpChannel, null);
        accessHelper.setUpOtpChannelUuid(getOptionsOtpChannelResponse, OtpChannelType.Phone);
        accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutAuthOtpChannel, AccessJsonDetails.PutAuthOtpChannelRequestSchema);
        Response getAuthResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuthOtpChannel, AccessJsonDetails.GetAuthOtpChannelSuccessfulResponse);
        accessHelper.verifyDefaultOtpIsSet(getAuthResponse, OtpChannelType.Phone);

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(
                AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(
                QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Set Email as default otp and verify the notification message");
        accessHelper.securityPage.clickOnChangeDefaultOtpCard();
        accessHelper.securityPage.clickOnDefaultOtpMediumPageEmailOption();
        accessHelper.securityPage.validateToastMessage(AccessHelper.accessStaticDataBase.getEmailDefaultOtpDeliverySuccessMessage());

        testConfig.logStep("Log out and re-login");
        accessHelper.dashBoardPage.clickLogoutButton();
        accessHelper.loginPage.enterEmailAndPassword(testConfig.getRunTimeProperty("username"), testConfig.getRunTimeProperty("password"));

        testConfig.logStep("Verify Email 2FA modal should be present");
        accessHelper.loginPage.verifyTwoFactorAuthenticationPage(true, UserNameType.Email);
    }

    @TestVariables(testrailData = "8:C13783", automatedBy = QA.TanHo)
    @Test(description = "Verify Phone is set as default otp successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testPhoneSetAsDefaultOtp(Config testConfig)
    {
        int userDetailsRowNum = 65;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Prepare data: Login with admin user and set email as default otp using API");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
        Response getOptionsOtpChannelResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetOptionsOtpChannel, null);
        accessHelper.setUpOtpChannelUuid(getOptionsOtpChannelResponse, OtpChannelType.Email);
        accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutAuthOtpChannel, AccessJsonDetails.PutAuthOtpChannelRequestSchema);
        Response getAuthResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuthOtpChannel, AccessJsonDetails.GetAuthOtpChannelSuccessfulResponse);
        accessHelper.verifyDefaultOtpIsSet(getAuthResponse, OtpChannelType.Email);

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(
                AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(
                QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Set Phone as default otp and verify the notification message");
        accessHelper.securityPage.clickOnChangeDefaultOtpCard();
        accessHelper.securityPage.clickOnDefaultOtpMediumPagePhoneOption();
        accessHelper.securityPage.validateToastMessage(AccessHelper.accessStaticDataBase.getPhoneDefaultOtpDeliverySuccessMessage());

        testConfig.logStep("Log out and re-login");
        accessHelper.dashBoardPage.clickLogoutButton();
        accessHelper.loginPage.enterEmailAndPassword(testConfig.getRunTimeProperty("username"), testConfig.getRunTimeProperty("password"));

        testConfig.logStep("Verify Phone 2FA modal should be present");
        accessHelper.loginPage.verifyTwoFactorAuthenticationPage(true, UserNameType.Phone);
    }
}
