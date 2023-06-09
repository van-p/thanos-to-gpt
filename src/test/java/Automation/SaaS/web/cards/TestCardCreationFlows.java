package Automation.SaaS.web.cards;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.SubMenuItem;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.SaaS.customer.helpers.SaasEnums.CardTypeEnum;
import Automation.SaaS.customer.helpers.SaasEnums.PersonTypeEnum;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.CardPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestCardCreationFlows extends TestBase
{

    @TestVariables(testrailData = "31:C11044", automatedBy = QA.Erit)
    @Test(description = "Verify that Director Admin can issue new virtual card for the other admin", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminIssueNewCardForTheOtherAdmin(Config testConfig)
    {
        int customerDetailsRowNum = 49;
        int cardDetailsRowNum = 1;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "CardDetails"}, customerDetailsRowNum, cardDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Go to Card page");
        saasHelper.cardPage = (CardPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Cards, AfterNavigationPage.CardPage);

        testConfig.logStep("Submit to issue new card and select card holder by card holder name");
        saasHelper.cardPage.issueNewCardAndSelectCardHolderByHolderName(CardTypeEnum.Virtual);

        testConfig.logStep("Input and submit new card, then validate the card information");
        saasHelper.cardPage.fillCardInfoAndValidate();
    }

    @TestVariables(testrailData = "31:C9671", automatedBy = QA.Erit)
    @Test(description = "Verify that Director Admin can issue new virtual card for any person", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "uiCases"})
    public void testAdminIssueNewCardForTheOtherUser(Config testConfig)
    {
        int customerDetailsRowNum = 49;
        int cardDetailsRowNum = 2;
        AccessHelper accessHelper = new AccessHelper(testConfig, customerDetailsRowNum);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "CardDetails"}, customerDetailsRowNum, cardDetailsRowNum);

        testConfig.logStep("Login to CF as admin user");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Go to Card page");
        saasHelper.cardPage = (CardPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Cards, AfterNavigationPage.CardPage);

        testConfig.logStep("Submit to issue new card and select card holder by type");
        saasHelper.cardPage.issueNewCardAndSelectCardHolderByType(CardTypeEnum.Virtual, PersonTypeEnum.OtherPerson);

        testConfig.logStep("Input and submit new card, then validate the card information");
        saasHelper.cardPage.fillCardInfoAndValidate();
    }
}
