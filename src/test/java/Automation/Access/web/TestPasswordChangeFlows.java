package Automation.Access.web;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessEnums.QuickAccessItem;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Access.customer.web.SecurityPage;
import Automation.Access.customer.web.UpdatePersonalDetailsPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestPasswordChangeFlows extends TestBase
{
    private Config testConfig;
    private AccessHelper accessHelper;
    private final String newPassword = "12345678Ac!";

    @TestVariables(testrailData = "8:C12241", automatedBy = QA.TanHo)
    @Test(description = "Verify admin can change his password successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testPasswordChangeFlows(Config testConfig)
    {
        accessHelper = new AccessHelper(testConfig, 62);
        this.testConfig = testConfig;

        testConfig.logStep("Login to CF as admin user & navigate to Security page");
        testConfig.putRunTimeProperty("originPassword", testConfig.testData.get("password"));
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        accessHelper.securityPage = (SecurityPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.Security, AfterNavigationPage.SecurityPage);

        testConfig.logStep("Click on 'Change login password' card");
        accessHelper.updatePersonalDetailsPage = (UpdatePersonalDetailsPage) accessHelper.securityPage.clickOnChangeLoginPasswordCard();

        testConfig.logStep("Input current login password & verify OTP");
        accessHelper.updatePersonalDetailsPage.inputCurrentLoginPasswordAndEnterOtp();

        testConfig.logStep("Input Security Questions & click on Confirm button");
        accessHelper.updatePersonalDetailsPage.inputAllFieldsInSecurityQuestion();

        testConfig.logStep("Input password & confirm password");
        accessHelper.updatePersonalDetailsPage.inputPasswordAndConfirmPassword(newPassword, newPassword);

        testConfig.logStep("Verify notification message & success message");
        accessHelper.updatePersonalDetailsPage.verifySuccessMessageIsDisplayed(AccessHelper.accessStaticDataBase.getLoginPasswordChangedSuccessText());

        testConfig.logStep("Verify admin authentication with new password");
        testConfig.testData.put("password", newPassword);
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
    }

    @TestVariables(testrailData = "8:C12250", automatedBy = QA.TanHo)
    @Test(description = "Verify admin can reset his password successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testPasswordResetFlows(Config testConfig)
    {
        accessHelper = new AccessHelper(testConfig, 63);
        this.testConfig = testConfig;

        testConfig.logStep("Click on Forgot Password link on Login page");
        testConfig.putRunTimeProperty("originPassword", testConfig.testData.get("password"));
        accessHelper.updatePersonalDetailsPage = (UpdatePersonalDetailsPage) accessHelper.clickOnForgotPasswordLink();

        testConfig.logStep("Enter Email address & verify OTP");
        accessHelper.updatePersonalDetailsPage.inputCurrentEmailAndEnterOtp();

        testConfig.logStep("Input Security Questions & click on Confirm button");
        accessHelper.updatePersonalDetailsPage.inputAllFieldsInSecurityQuestion();

        testConfig.logStep("Input password & confirm password");
        accessHelper.updatePersonalDetailsPage.inputPasswordAndConfirmPassword(newPassword, newPassword);

        testConfig.logStep("Verify notification message & success message");
        accessHelper.updatePersonalDetailsPage.verifySuccessMessageIsDisplayed(AccessHelper.accessStaticDataBase.getLoginPasswordResetSuccessText());

        testConfig.logStep("Verify admin authentication with new password");
        testConfig.testData.put("password", newPassword);
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
    }

    @AfterMethod(description = "Restore origin password", alwaysRun = true)
    public void restoreOriginPassword()
    {
        testConfig.logStep("Change current password back to origin password");
        testConfig.testData.put("identityType", "passport");
        testConfig.testData.put("dateOfBirth", "1990-09-13");
        testConfig.testData.put("oldPassword", newPassword);
        testConfig.testData.put("newPassword", testConfig.getRunTimeProperty("originPassword"));
        testConfig.testData.put("confirmPassword", testConfig.getRunTimeProperty("originPassword"));
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPasswordChange, AccessJsonDetails.PutPasswordChangeRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.PutPasswordChange, AccessJsonDetails.PutPasswordChangeSuccessResponse);
    }
}
