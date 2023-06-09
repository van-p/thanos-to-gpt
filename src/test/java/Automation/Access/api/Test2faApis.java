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
import org.testng.annotations.Test;

public class Test2faApis extends TestBase
{

    @TestVariables(testrailData = "8:C13778", automatedBy = QA.TanHo)
    @Test(description = "Test disable Organisation 2FA and enable Personal 2FA", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "2fa"})
    public void testEnablePersonal2fa(Config testConfig)
    {
        int userDetailsRowNum = 34;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API Delete /v1/businesses/{$businessUuid}/2fa to disable Organisation 2FA");
        Response getOrganisation2faResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeleteBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2faResponse, AccessApiDetails.DeleteBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Execute API Put /v1/2fa to enable Personal 2FA");
        Response getPersonal2faResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPersonal2fa, null);
        accessHelper.verifyApiResponse(getPersonal2faResponse, AccessApiDetails.PutPersonal2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("API Get /v1/auth & validate auth response has hasEnabled2fa=false and has2faEnabledBusiness=false");
        check2faStatus(testConfig, accessHelper, true, false);

    }

    @TestVariables(testrailData = "8:C13779", automatedBy = QA.TanHo)
    @Test(description = "Test disable Organisation 2FA and disable Personal 2FA", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "2fa"})
    public void testDisablePersonal2fa(Config testConfig)
    {
        int userDetailsRowNum = 35;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API Delete /v1/businesses/{$businessUuid}/2fa to disable Organisation 2FA");
        Response getOrganisation2faResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeleteBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2faResponse, AccessApiDetails.DeleteBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Execute API Delete /v1/2fa to disable Personal 2FA");
        Response getPersonal2faResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.DeletePersonal2fa, null);
        accessHelper.verifyApiResponse(getPersonal2faResponse, AccessApiDetails.DeletePersonal2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("API Get /v1/auth & validate auth response has hasEnabled2fa=false and has2faEnabledBusiness=false");
        check2faStatus(testConfig, accessHelper, false, false);

    }

    @TestVariables(testrailData = "8:C13780", automatedBy = QA.TanHo)
    @Test(description = "Test enable Organisation 2FA and enable Personal 2FA", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases", "2fa"})
    public void testEnableOrganisation2fa(Config testConfig)
    {
        int userDetailsRowNum = 36;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API Put /v1/businesses/{$businessUuid}/2fa to enable Organisation 2FA");
        Response getOrganisation2faResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutBusiness2fa, null);
        accessHelper.verifyApiResponse(getOrganisation2faResponse, AccessApiDetails.PutBusiness2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("Execute API Put /v1/2fa to enable Personal 2FA");
        Response getPersonal2faResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.PutPersonal2fa, null);
        accessHelper.verifyApiResponse(getPersonal2faResponse, AccessApiDetails.PutPersonal2fa, AccessJsonDetails.Get2faSuccessfulResponse);

        testConfig.logStep("API Get /v1/auth & validate auth response has hasEnabled2fa=false and has2faEnabledBusiness=false");
        check2faStatus(testConfig, accessHelper, true, true);
    }

    private void check2faStatus(Config testConfig, AccessHelper accessHelper, boolean hasEnabled2fa, boolean has2faEnabledBusiness)
    {
        Response getAuthResponse = accessHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth2fa, null);
        testConfig.putRunTimeProperty("hasEnabled2fa", hasEnabled2fa);
        testConfig.putRunTimeProperty("has2faEnabledBusiness", has2faEnabledBusiness);
        accessHelper.verifyApiResponse(getAuthResponse, AccessApiDetails.GetAuth2fa, AccessJsonDetails.GetAuth2faSuccessfulResponse);
    }
}
