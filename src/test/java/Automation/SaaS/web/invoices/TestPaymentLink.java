package Automation.SaaS.web.invoices;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.*;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.SaaS.customer.api.SaasApiDetails;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasEnums.PaymentLinkStatus;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.SaaS.customer.web.PaymentLinksPage;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestPaymentLink extends TestBase
{

    @TestVariables(testrailData = "11:C8998", automatedBy = QA.Van)
    @Test(description = "Verify user creates Payment Links to business with fully information successfully ", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testPaymentLinkFullInformation(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 11);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "PaymentLinkDetails"}, 11, 1);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        testConfig.logStep("Create a payment link with full information and verify payment link created page UI, then close the created page");
        saasHelper.paymentLinksCreatedPage = saasHelper.paymentLinksPage.createNewLink();
        saasHelper.paymentLinksCreatedPage.verifyPaymentLinkCreatedPage();
        saasHelper.paymentLinksCreatedPage.closePaymentLinkCreatedPage();
        testConfig.logStep("Validate new Payment Link displays in Payment Link list");
        saasHelper.paymentLinksPage.verifyPaymentLink(testConfig.getRunTimeProperty("paymentLinkNumber"));
    }

    @TestVariables(testrailData = "11:C9001", automatedBy = QA.Van)
    @Test(description = "Verify user discards Payment Link update successfully", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testDiscardPaymentLink(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 19);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "PaymentLinkDetails"}, 19, 2);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        testConfig.logStep("Create a payment link with full information and verify payment link created page UI, then close the created page");
        saasHelper.paymentLinksCreatedPage = saasHelper.paymentLinksPage.createNewLink();
        saasHelper.paymentLinksCreatedPage.verifyPaymentLinkCreatedPage();
        saasHelper.paymentLinksCreatedPage.closePaymentLinkCreatedPage();
        testConfig.logStep("Validate new Payment Link displays in Payment Link list");
        saasHelper.paymentLinksPage.verifyPaymentLink(testConfig.getRunTimeProperty("paymentLinkNumber"));
        testConfig.logStep("Edit the payment link and verify payment link edit page UI");
        saasHelper.paymentLinksEditDetailsPage = saasHelper.paymentLinksPage.selectEditPaymentLinkDetailOption(testConfig.getRunTimeProperty("paymentLinkNumber"));
        saasHelper.paymentLinksEditDetailsPage.verifyPaymentLinkEditPage();
        testConfig.logStep("Edit then Discard the payment link update and verify payment link is not updated");
        saasHelper.paymentLinksEditDetailsPage.editAndDiscardPaymentLinkChanges();
        saasHelper.paymentLinksPage.verifyPaymentLink(testConfig.getRunTimeProperty("paymentLinkNumber"));
    }

    @TestVariables(testrailData = "11:C9008", automatedBy = QA.Van)
    @Test(description = "Verify user added Payment Link from Customize Quick Access dialog successfully ", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testAddPaymentLinkQuickAccess(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 19);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "PaymentLinkDetails"}, 23, 2);
        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Add the Payment Link item in Customize Quick Access dialog and verify Payment Link item is added");
        accessHelper.dashBoardPage.addQuickAccessItemAndVerifyUpdatedMessage(QuickAccessItem.PaymentLinks);
        accessHelper.dashBoardPage.verifyQuickAccessItem(QuickAccessItem.PaymentLinks, true);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnQuickAccess(QuickAccessItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        testConfig.logStep("Navigate to dashboard page and verify dashboard page UI");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Home, AfterNavigationPage.DashBoardPage);
        testConfig.logStep("Remove the Payment Link item in Customize Quick Access dialog and verify Payment Link item is removed");
        accessHelper.dashBoardPage.removeQuickAccessItemAndVerifyUpdatedMessage(QuickAccessItem.PaymentLinks);
        accessHelper.dashBoardPage.verifyQuickAccessItem(QuickAccessItem.PaymentLinks, false);
    }

    @TestVariables(testrailData = "11:C9000", automatedBy = QA.Van)
    @Test(description = "Verify user deletes existing Payment Links successfully ", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testDeletePaymentLinkSuccessfully(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 7);
        AccessHelper accessHelper = new AccessHelper(testConfig, 36);

        testConfig.logStep("Pre condition: Create payment link data and verify payment link created successfully");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponseRequiredOnly);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        String paymentLinkNumber = testConfig.getRunTimeProperty("number");
        testConfig.logStep("Select and delete the payment link and verify payment link is deleted");
        saasHelper.paymentLinksPage.openPaymentLinkDetail(paymentLinkNumber).deletePaymentLink(paymentLinkNumber).verifyPaymentLinkDeleted(paymentLinkNumber);
    }

    @TestVariables(testrailData = "11:C9005", automatedBy = QA.Van)
    @Test(description = "Verify user marks the Payment Link as paid manually  successfully", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testSuccessfullyMarkPaymentLinkAsPaidManually(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 8);
        AccessHelper accessHelper = new AccessHelper(testConfig, 37);

        testConfig.logStep("Pre condition: Create payment link data and verify payment link created successfully");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponseRequiredOnly);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        String paymentLinkNumber = testConfig.getRunTimeProperty("number");
        testConfig.logStep("Select and mark the payment link as paid and verify payment link status is changed to paid");
        saasHelper.paymentLinksPage.openPaymentLinkDetail(paymentLinkNumber).markPaymentLinkStatusManually(paymentLinkNumber, PaymentLinkStatus.Paid).verifyPaymentLinkStatus(PaymentLinkStatus.Paid);
    }

    @TestVariables(testrailData = "11:C9006", automatedBy = QA.Van)
    @Test(description = "Verify user marks as unpaid manually the Payment Link successfully", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testSuccessfullyMarkPaymentLinkAsUnPaidManually(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 9);
        AccessHelper accessHelper = new AccessHelper(testConfig, 38);

        testConfig.logStep("Pre condition: Create payment link data and verify payment link created successfully");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponseRequiredOnly);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI. Select and mark the payment link as paid and verify payment link status is changed to paid");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        String paymentLinkNumber = testConfig.getRunTimeProperty("number");
        saasHelper.paymentLinksPage.openPaymentLinkDetail(paymentLinkNumber).markPaymentLinkStatusManually(paymentLinkNumber, PaymentLinkStatus.Paid).verifyPaymentLinkStatus(PaymentLinkStatus.Paid).closePaymentLinkDetailsPage();
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Home, AfterNavigationPage.DashBoardPage);
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);

        testConfig.logStep("Select and mark the payment link as unpaid and verify payment link status is changed to Due");
        saasHelper.paymentLinksPage.openPaymentLinkDetail(paymentLinkNumber).markPaymentLinkStatusManually(paymentLinkNumber, PaymentLinkStatus.Due).verifyPaymentLinkStatus(PaymentLinkStatus.Due);
    }

    @TestVariables(testrailData = "11:C8996", automatedBy = QA.Van)
    @Test(description = "Verify Paid Payment Link should be blocked from editing", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testPaidPaymentLinkBlockedFromEditing(Config testConfig)
    {
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"PaymentLinkDetails"}, 10);
        AccessHelper accessHelper = new AccessHelper(testConfig, 39);

        testConfig.logStep("Pre condition: Create payment link data and verify payment link created successfully");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);
        Response createPaymentLinkResponse = saasHelper.sendRequestAndGetResponse(SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkRequestSchema);
        saasHelper.verifyApiResponse(createPaymentLinkResponse, SaasApiDetails.CreatePaymentLink, SaasJsonDetails.CreatePaymentLinkResponseRequiredOnly);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI. Select and mark the payment link as paid and verify payment link status is changed to paid");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        String paymentLinkNumber = testConfig.getRunTimeProperty("number");
        saasHelper.paymentLinksPage.openPaymentLinkDetail(paymentLinkNumber).markPaymentLinkStatusManually(paymentLinkNumber, PaymentLinkStatus.Paid).verifyPaymentLinkStatus(PaymentLinkStatus.Paid).closePaymentLinkDetailsPage();
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.Home, AfterNavigationPage.DashBoardPage);
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);

        testConfig.logStep("Select and verify paid payment link is blocked from editing");
        saasHelper.paymentLinksPage.verifyPaidPaymentLink(paymentLinkNumber);
    }

    @TestVariables(testrailData = "11:C8990", automatedBy = QA.Van)
    @Test(description = "Verify user creates Payment Links to individual with required information only successfully", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testCreatePaymentLinkForIndividualWithRequiredInfo(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 40);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "PaymentLinkDetails"}, 40, 11);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        testConfig.logStep("Create a payment link with full information and verify payment link created page UI, then close the created page");
        saasHelper.paymentLinksCreatedPage = saasHelper.paymentLinksPage.createNewLink();
        saasHelper.paymentLinksCreatedPage.verifyPaymentLinkCreatedPage();
        saasHelper.paymentLinksCreatedPage.closePaymentLinkCreatedPage();
        testConfig.logStep("Validate new Payment Link displays in Payment Link list");
        saasHelper.paymentLinksPage.verifyPaymentLink(testConfig.getRunTimeProperty("paymentLinkNumber"));
    }

    @TestVariables(testrailData = "11:C8999", automatedBy = QA.Van)
    @Test(description = "Verify user creates Payment Links to business with required information only successfully", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testCreatePaymentLinkForBusinessWithRequiredInfo(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 41);
        SaasHelper saasHelper = new SaasHelper(testConfig, new String[]{"UserDetails", "PaymentLinkDetails"}, 41, 12);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);
        testConfig.logStep("Navigate to Payment Link page and verify payment link page UI");
        saasHelper.paymentLinksPage = (PaymentLinksPage) accessHelper.dashBoardPage.navigateOnMenu(SubMenuItem.PaymentLinks, AfterNavigationPage.PaymentLinks);
        testConfig.logStep("Create a payment link with full information and verify payment link created page UI, then close the created page");
        saasHelper.paymentLinksCreatedPage = saasHelper.paymentLinksPage.createNewLink();
        saasHelper.paymentLinksCreatedPage.verifyPaymentLinkCreatedPage();
        saasHelper.paymentLinksCreatedPage.closePaymentLinkCreatedPage();
        testConfig.logStep("Validate new Payment Link displays in Payment Link list");
        saasHelper.paymentLinksPage.verifyPaymentLink(testConfig.getRunTimeProperty("paymentLinkNumber"));
    }

}
