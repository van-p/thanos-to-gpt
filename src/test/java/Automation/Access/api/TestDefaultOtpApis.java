package Automation.Access.api;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessEnums.OtpChannelType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class TestDefaultOtpApis extends TestBase
{

    @TestVariables(testrailData = "8:C13786", automatedBy = QA.TanHo)
    @Test(description = "Verify email is set as default otp successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testSetEmailAsDefaultOtp(Config testConfig)
    {
        int userDetailsRowNum = 37;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login with admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Set up OTP channel uuid using email uuid");
        Response getOptionsOtpChannelResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetOptionsOtpChannel, null);
        accessHelper.setUpOtpChannelUuid(getOptionsOtpChannelResponse, OtpChannelType.Email);

        testConfig.logStep("Set email as default otp");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutAuthOtpChannel, AccessJsonDetails.PutAuthOtpChannelRequestSchema);
        verifyUuidIsNotNullInResponse(testConfig, response);

        testConfig.logStep("Verify email is set as default otp successfully");
        Response getAuthResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuthOtpChannel, AccessJsonDetails.GetAuthOtpChannelSuccessfulResponse);
        accessHelper.verifyDefaultOtpIsSet(getAuthResponse, OtpChannelType.Email);
    }

    @TestVariables(testrailData = "8:C13785", automatedBy = QA.TanHo)
    @Test(description = "Verify phone is set as default otp successfully", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testSetPhoneAsDefaultOtp(Config testConfig)
    {
        int userDetailsRowNum = 38;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login with admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Set up OTP channel uuid using phone uuid");
        Response getOptionsOtpChannelResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetOptionsOtpChannel, null);
        accessHelper.setUpOtpChannelUuid(getOptionsOtpChannelResponse, OtpChannelType.Phone);

        testConfig.logStep("Set phone as default otp");
        Response response = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutAuthOtpChannel, AccessJsonDetails.PutAuthOtpChannelRequestSchema);
        verifyUuidIsNotNullInResponse(testConfig, response);

        testConfig.logStep("Verify phone is set as default otp successfully");
        Response getAuthResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuthOtpChannel, AccessJsonDetails.GetAuthOtpChannelSuccessfulResponse);
        accessHelper.verifyDefaultOtpIsSet(getAuthResponse, OtpChannelType.Phone);
    }

    public void verifyUuidIsNotNullInResponse(Config testConfig, Response response)
    {
        JSONObject object = new JSONObject(response.body().asString());
        AssertHelper.compareTrue(testConfig, "uuid is not null", !object.isNull("uuid"));
    }
}
