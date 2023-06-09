package Automation.SaaS.api.claim;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestClaims extends TestBase
{

    int testUserRow = 13;
    String uploadFileName = "invoice_ssum.png";

    @TestVariables(testrailData = "7:C1353", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can submit a claim", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testSubmitNewClaim(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Get claim details to verify all claim data is correctly");
        Response getClaimDetailsResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetClaimDetails, null);
        saasHelper.verifyApiResponse(getClaimDetailsResponse, SaasApiDetails.GetClaimDetails, SaasJsonDetails.GetClaimDetailsResponse);
    }

    @TestVariables(testrailData = "7:C1357", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can update a claim", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testEditClaim(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Retrieve all Source of fund data need for claim");
        Response getClaimSofResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetClaimSourceOfFunds, null);
        saasHelper.verifyApiResponse(getClaimSofResponse, SaasApiDetails.GetClaimSourceOfFunds, null);

        testConfig.logStep("Edit the created claim and verify response is accurate");
        saasHelper.setupUpdateClaimRequestData(false, CurrencyEnum.SGD, getClaimSofResponse);
        Response updateClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.UpdateClaim, SaasJsonDetails.SubmitClaimRequestSchema);
        saasHelper.verifyApiResponse(updateClaimResponse, SaasApiDetails.UpdateClaim, SaasJsonDetails.SubmitClaimResponse);

        testConfig.logStep("Get claim details to verify all data is edited correctly");
        Response getClaimDetailsResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.GetClaimDetails, null);
        saasHelper.verifyApiResponse(getClaimDetailsResponse, SaasApiDetails.GetClaimDetails, SaasJsonDetails.GetClaimDetailsResponse);
    }

    @TestVariables(testrailData = "7:C1466", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can approve a claim", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testApproveClaim(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Approve the claim - expect claim state is pending payment");
        Response approveClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ApproveClaim, null);
        saasHelper.verifyApiResponse(approveClaimResponse, SaasApiDetails.ApproveClaim, null);
    }

    @TestVariables(testrailData = "7:C1334", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can pay a claim", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testPayClaim(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Approve the claim - expect claim state is pending payment");
        Response approveClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ApproveClaim, null);
        saasHelper.verifyApiResponse(approveClaimResponse, SaasApiDetails.ApproveClaim, null);

        testConfig.logStep("Prepare OTP authorization to make payment");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);
        Response oauthTokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenRequestSchema);
        accessHelper.verifyApiResponse(oauthTokenResponse, AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenSuccessResponse);

        testConfig.logStep("Pay the claim - expect claim state is paid");
        Response payClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.PayClaim, SaasJsonDetails.PayClaimRequestSchema);
        saasHelper.verifyApiResponse(payClaimResponse, SaasApiDetails.PayClaim, SaasJsonDetails.PayClaimResponse);
    }

    @TestVariables(testrailData = "7:C1335", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can reject a claim", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testRejectClaim(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Reject the claim - expect claim state is rejected");
        Response approveClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.RejectClaim, SaasJsonDetails.RejectClaimRequestSchema);
        saasHelper.verifyApiResponse(approveClaimResponse, SaasApiDetails.RejectClaim, null);
    }

    @TestVariables(testrailData = "7:C1338", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can schedule claim payment", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testScheduleClaimPayment(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Approve the claim - expect claim state is pending payment");
        Response approveClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ApproveClaim, null);
        saasHelper.verifyApiResponse(approveClaimResponse, SaasApiDetails.ApproveClaim, null);

        testConfig.logStep("Prepare OTP authorization to make payment");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);
        Response oauthTokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenRequestSchema);
        accessHelper.verifyApiResponse(oauthTokenResponse, AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenSuccessResponse);

        testConfig.logStep("Schedule the claim payment and verify response is accurate");
        Response payClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ScheduleClaimPayment, SaasJsonDetails.PayClaimRequestSchema);
        saasHelper.verifyApiResponse(payClaimResponse, SaasApiDetails.ScheduleClaimPayment, SaasJsonDetails.ScheduleClaimPaymentResponse);
    }

    @TestVariables(testrailData = "7:C1342", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can stop scheduled claim payment", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testStopScheduledClaimPayment(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Approve the claim - expect claim state is pending payment");
        Response approveClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ApproveClaim, null);
        saasHelper.verifyApiResponse(approveClaimResponse, SaasApiDetails.ApproveClaim, null);

        testConfig.logStep("Prepare OTP authorization to make payment");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);
        Response oauthTokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenRequestSchema);
        accessHelper.verifyApiResponse(oauthTokenResponse, AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenSuccessResponse);

        testConfig.logStep("Schedule the claim payment and verify response is accurate");
        Response scheduleClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ScheduleClaimPayment, SaasJsonDetails.PayClaimRequestSchema);
        saasHelper.verifyApiResponse(scheduleClaimResponse, SaasApiDetails.ScheduleClaimPayment, SaasJsonDetails.ScheduleClaimPaymentResponse);

        testConfig.logStep("Stop the scheduled claim payment and verify response is accurate");
        Response stopClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.StopScheduledClaimPayment, null);
        saasHelper.verifyApiResponse(stopClaimResponse, SaasApiDetails.StopScheduledClaimPayment, SaasJsonDetails.ScheduleClaimPaymentResponse);
    }

    @TestVariables(testrailData = "7:C1340", automatedBy = QA.Thuc)
    @Test(description = "To verify that Admin can stop a duplicate scheduled claim payment", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testStopADuplicateScheduledClaimPayment(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, testUserRow);
        AccessHelper accessHelper = new AccessHelper(testConfig);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig);

        testConfig.logStep("Authenticate as an Admin user");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Submit a claim - expect claim state is pending review");
        saasHelper.submitClaim(paymentHelper, false, CurrencyEnum.SGD, uploadFileName);

        testConfig.logStep("Approve the claim - expect claim state is pending payment");
        Response approveClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ApproveClaim, null);
        saasHelper.verifyApiResponse(approveClaimResponse, SaasApiDetails.ApproveClaim, null);

        testConfig.logStep("Prepare OTP authorization to make payment");
        Response authResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        accessHelper.verifyApiResponse(authResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);
        Response oauthTokenResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenRequestSchema);
        accessHelper.verifyApiResponse(oauthTokenResponse, AccessApiDetails.PostRequestAdditionalToken, AccessJsonDetails.OtpTokenSuccessResponse);

        testConfig.logStep("Schedule the claim payment and verify response is accurate");
        Response scheduleClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.ScheduleClaimPayment, SaasJsonDetails.PayClaimRequestSchema);
        saasHelper.verifyApiResponse(scheduleClaimResponse, SaasApiDetails.ScheduleClaimPayment, SaasJsonDetails.ScheduleClaimPaymentResponse);

        testConfig.logStep("Stop the scheduled claim payment and verify response is accurate");
        Response stopClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.StopScheduledClaimPayment, null);
        saasHelper.verifyApiResponse(stopClaimResponse, SaasApiDetails.StopScheduledClaimPayment, SaasJsonDetails.ScheduleClaimPaymentResponse);

        testConfig.logStep("Stop this scheduled claim payment again expect action is unauthorized");
        stopClaimResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.StopScheduledClaimPayment, null);
        saasHelper.verifyApiResponse(stopClaimResponse, SaasApiDetails.StopScheduledClaimPayment, SaasJsonDetails.UnauthorizedResponse);
    }
}
