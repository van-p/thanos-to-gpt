package Automation.Access.web;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.QuickAccessItem;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Access.customer.web.PersonalDetailsPage;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestUpdatePersonalDetails extends TestBase
{

    @TestVariables(testrailData = "6:C8556", automatedBy = QA.Quan)
    @Test(description = "To verify that user is able to update the Email address", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testUpdateEmailAddress(Config testConfig)
    {
        int userDetailsRowNum = 42;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Personal Details Page");
        accessHelper.personalDetailsPage = (PersonalDetailsPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.PersonalDetails, AfterNavigationPage.PersonalDetailsPage);

        testConfig.logStep("Click on Update Work email button");
        accessHelper.updatePersonalDetailsPage = accessHelper.personalDetailsPage.clickOnWorkEmailUpdateButton();

        testConfig.logStep("Input current password and click on next button");
        accessHelper.updatePersonalDetailsPage.inputCurrentLoginPasswordAndEnterOtp();

        testConfig.logStep("Input security question and click on next button");
        accessHelper.updatePersonalDetailsPage.inputAllFieldsInSecurityQuestion();

        testConfig.logStep("Input new email address and verify updated mail found");
        String email = DataGenerator.generateRandomAlphaNumericString(7) + "@example.com";
        accessHelper.updatePersonalDetailsPage.inputNewEmailAddress(email);
        accessHelper.personalDetailsPage.verifyIfUpdatedEmailFound(email);

        // Will replace duplicate code by API TC later
        testConfig.logStep("Update email to previous email");
        accessHelper.personalDetailsPage.clickOnWorkEmailUpdateButton();
        accessHelper.updatePersonalDetailsPage.inputCurrentLoginPasswordAndEnterOtp();
        accessHelper.updatePersonalDetailsPage.inputAllFieldsInSecurityQuestion();
        accessHelper.updatePersonalDetailsPage.inputNewEmailAddress(testConfig.testData.get("username"));
        accessHelper.personalDetailsPage.verifyIfUpdatedEmailFound(testConfig.testData.get("username"));
    }

    @TestVariables(testrailData = "6:C8557", automatedBy = QA.Quan)
    @Test(description = "To verify that user is able to update the Mobile number", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testUpdateMobileNumber(Config testConfig)
    {
        int userDetailsRowNum = 41;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Personal Details Page");
        accessHelper.personalDetailsPage = (PersonalDetailsPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.PersonalDetails, AfterNavigationPage.PersonalDetailsPage);

        testConfig.logStep("Click on Update Mobile number button");
        accessHelper.updatePersonalDetailsPage = accessHelper.personalDetailsPage.clickOnMobileNumberUpdateButton();

        testConfig.logStep("Input current password and click on next button");
        accessHelper.updatePersonalDetailsPage.inputCurrentLoginPasswordAndEnterOtp();

        testConfig.logStep("Input security question and click on next button");
        accessHelper.updatePersonalDetailsPage.inputAllFieldsInSecurityQuestion();

        testConfig.logStep("Input new mobile number and verify updated mobile number found");
        long updatedMobilePhone = DataGenerator.generateRandomNumber(9);
        accessHelper.updatePersonalDetailsPage.inputNewMobileNumber(updatedMobilePhone);
        accessHelper.personalDetailsPage.verifyIfUpdatedMobileNumberFound(String.valueOf(updatedMobilePhone));
    }

    @TestVariables(testrailData = "6:C8558", automatedBy = QA.Quan)
    @Test(description = "To verify that user is able to update the Preferred name", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testUpdatePreferredName(Config testConfig)
    {
        int userDetailsRowNum = 41;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Personal Details Page");
        accessHelper.personalDetailsPage = (PersonalDetailsPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.PersonalDetails, AfterNavigationPage.PersonalDetailsPage);

        testConfig.logStep("Click on Update Preferred name button");
        accessHelper.personalDetailsPage.clickOnPreferredNameUpdateButton();

        testConfig.logStep("clear and input new Preferred name");
        String preferredName = DataGenerator.generateRandomAlphaNumericString(7);
        accessHelper.personalDetailsPage.inputNewPreferredName(preferredName);

        testConfig.logStep("verify new Preferred name");
        accessHelper.personalDetailsPage.verifyIfUpdatedPreferredNameFound(preferredName);
    }
}