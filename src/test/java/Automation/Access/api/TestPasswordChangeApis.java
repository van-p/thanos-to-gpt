package Automation.Access.api;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestPasswordChangeApis extends TestBase
{

    private Config testConfig;
    private AccessHelper accessHelper;

    @TestVariables(testrailData = "8:C13399", automatedBy = QA.TanHo)
    @Test(description = "Verify password should be changed successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testPasswordChangeApis(Config testConfig)
    {
        accessHelper = new AccessHelper(testConfig, 60);
        this.testConfig = testConfig;

        testConfig.logStep("Login with admin user and store the origin password");
        testConfig.putRunTimeProperty("originPassword", testConfig.testData.get("password"));
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Change current password to new password");
        testConfig.testData.put("oldPassword", testConfig.testData.get("password"));
        testConfig.testData.put("newPassword", "12345678Ac!");
        testConfig.testData.put("confirmPassword", "12345678Ac!");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPasswordChange, AccessJsonDetails.PutPasswordChangeRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.PutPasswordChange, AccessJsonDetails.PutPasswordChangeSuccessResponse);

        testConfig.logStep("Verify admin authentication with new password");
        testConfig.testData.put("password", "12345678Ac!");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
    }

    @TestVariables(testrailData = "8:C13396", automatedBy = QA.TanHo)
    @Test(description = "Verify password should be reset successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testPasswordResetApis(Config testConfig)
    {
        accessHelper = new AccessHelper(testConfig, 61);
        this.testConfig = testConfig;

        testConfig.logStep("Verify email OTP and get token");
        testConfig.putRunTimeProperty("originPassword", testConfig.testData.get("password"));
        Response tokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(tokenResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenOtpRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Reset with new password");
        testConfig.testData.put("newPassword", "12345678Ac!");
        testConfig.testData.put("confirmPassword", "12345678Ac!");
        Response passwordResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPasswordReset, AccessJsonDetails.PutPasswordResetRequestSchema);
        accessHelper.verifyApiResponse(passwordResponse, AccessApiDetails.PutPasswordReset, AccessJsonDetails.PutPasswordResetSuccessResponse);

        testConfig.logStep("Verify admin authentication with new password");
        testConfig.testData.put("password", "12345678Ac!");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
    }

    @AfterMethod(description = "Restore origin password", alwaysRun = true)
    public void restoreOriginPassword()
    {
        testConfig.logStep("Change current password back to origin password");
        testConfig.testData.put("oldPassword", "12345678Ac!");
        testConfig.testData.put("newPassword", testConfig.getRunTimeProperty("originPassword"));
        testConfig.testData.put("confirmPassword", testConfig.getRunTimeProperty("originPassword"));
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPasswordChange, AccessJsonDetails.PutPasswordChangeRequestSchema);
        accessHelper.verifyApiResponse(response, AccessApiDetails.PutPasswordChange, AccessJsonDetails.PutPasswordChangeSuccessResponse);
    }

}
