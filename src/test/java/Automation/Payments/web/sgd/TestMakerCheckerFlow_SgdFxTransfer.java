package Automation.Payments.web.sgd;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.MakerCheckerFlowType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TestMakerCheckerFlow_SgdFxTransfer extends TestBase
{

    @TestVariables(testrailData = "41:C12528", automatedBy = QA.Akankshi)
    @Test(description = "Verify Admin can successfully approve sgd fx transfer from Pending tab", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void adminApproveSgdFxTransferFromPendingTab(Config testConfig)
    {

        int localTransferInfoRowNum = 1;
        int debitFilterRow = 6;
        int makerDetailDataRow = 22;
        int counterPartyRow = 10;
        int quoteInfoRow = 2;
        int pendingActionRow = 2;
        int userDetails = 23;
        int approvedTransferCount = 1;

        PaymentHelper paymentMakerHelper = new PaymentHelper(testConfig, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo", "PendingActions"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow, pendingActionRow);
        AccessHelper accessMakerHelper = new AccessHelper(testConfig, makerDetailDataRow);

        //Maker steps using API
        testConfig.logStep("Pre condition: Maker get access token and verify token");
        Response response = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessMakerHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Pre condition: Maker get business and verify business");
        Response authResponse = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessMakerHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchemaEasyRequirement);

        testConfig.logStep("Pre condition: Finance Submit user submits sgd fx transfer through api");
        paymentMakerHelper.createPendingActionsForSgdFxTransfer();

        AccessHelper accessHelperChecker = new AccessHelper(testConfig, userDetails);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfig, new String[]{"UserDetails"}, userDetails);

        //Checker steps
        testConfig.logStep("Login to Customer FrontEnd as Admin, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfig.logStep("Search for the pending transfer using reference and approve");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validatePendingTransferDetails();
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(false);

        testConfig.logStep("Validate approved pending transfer is not present in Pending tab after approval");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validatePendingTransferNotPresentInPendingTabAfterApproval();

        testConfig.logStep("Validate approved pending transfer is present in Past tab after approving it from Pending tab");
        paymentHelperChecker.transactionsPage.goToPastTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validateApprovedTransactionsPresentInPastTab(approvedTransferCount);
    }

    @TestVariables(testrailData = "41:C12545", automatedBy = QA.Akankshi)
    @Test(description = "Verify FinanceTransfer can successfully approve sgd fx transfer from Pending tab", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void financeTransferApproveSgdFxTransferFromPendingTab(Config testConfig)
    {

        int localTransferInfoRowNum = 1;
        int debitFilterRow = 6;
        int makerDetailDataRow = 22;
        int counterPartyRow = 10;
        int quoteInfoRow = 2;
        int pendingActionRow = 2;
        int userDetails = 24;
        int approvedTransferCount = 1;

        PaymentHelper paymentMakerHelper = new PaymentHelper(testConfig, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo", "PendingActions"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow, pendingActionRow);
        AccessHelper accessMakerHelper = new AccessHelper(testConfig, makerDetailDataRow);

        //Maker steps using API
        testConfig.logStep("Pre condition: Maker get access token and verify token");
        Response response = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessMakerHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Pre condition: Maker get business and verify business");
        Response authResponse = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessMakerHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchemaEasyRequirement);

        testConfig.logStep("Pre condition: Finance Submit user submits sgd fx transfer through api");
        paymentMakerHelper.createPendingActionsForSgdFxTransfer();

        AccessHelper accessHelperChecker = new AccessHelper(testConfig, userDetails);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfig, new String[]{"UserDetails"}, userDetails);

        //Checker steps
        testConfig.logStep("Login to Customer FrontEnd as Finance Transfer user, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfig.logStep("Search for the pending transfer using reference and approve");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validatePendingTransferDetails();
        paymentHelperChecker.transactionsPage.checkPendingTabCheckbox();
        paymentHelperChecker.transactionsPage.approveTransferOnPendingTab(false);

        testConfig.logStep("Validate approved pending transfer is not present in Pending tab after approval");
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validatePendingTransferNotPresentInPendingTabAfterApproval();

        testConfig.logStep("Validate approved pending transfer is present in Past tab after approving it from Pending tab");
        paymentHelperChecker.transactionsPage.goToPastTab();
        paymentHelperChecker.transactionsPage.enterTextInSearchBox(testConfig.testData.get("reference"));
        paymentHelperChecker.transactionsPage.validateApprovedTransactionsPresentInPastTab(approvedTransferCount);
    }

    @TestVariables(testrailData = "41:C12529", automatedBy = QA.Akankshi)
    @Test(description = "Verify admin can successfully approve sgd fx transfer from Transfer Details screen", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void adminApproveSgdFxTransferFromTransferDetailsScreen(Config testConfig)
    {

        int localTransferInfoRowNum = 1;
        int debitFilterRow = 6;
        int makerDetailDataRow = 22;
        int counterPartyRow = 10;
        int quoteInfoRow = 2;
        int pendingActionRow = 2;
        int userDetails = 23;
        int approvedTransferCount = 1;

        PaymentHelper paymentMakerHelper = new PaymentHelper(testConfig, new String[]{"LocalTransferInfo", "DebitAccountFilter", "CounterPartiesInfo", "QuoteInfo", "PendingActions"},
                localTransferInfoRowNum, debitFilterRow, counterPartyRow, quoteInfoRow, pendingActionRow);
        AccessHelper accessMakerHelper = new AccessHelper(testConfig, makerDetailDataRow);

        //Maker steps using API
        testConfig.logStep("Pre condition: Maker get access token and verify token");
        Response response = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenRequestSchema);
        accessMakerHelper.verifyApiResponse(response, AccessApiDetails.OauthToken, AccessJsonDetails.OauthTokenSuccessfulResponse);

        testConfig.logStep("Pre condition: Maker get business and verify business");
        Response authResponse = accessMakerHelper.sendRequestAndGetResponse(AccessApiDetails.GetAuth, null);
        accessMakerHelper.verifyApiResponse(authResponse, AccessApiDetails.GetAuth, AccessJsonDetails.GetAuthResponseSchemaEasyRequirement);

        testConfig.logStep("Pre condition: Finance Submit user submits sgd fx transfer through api");
        paymentMakerHelper.createPendingActionsForSgdFxTransfer();

        AccessHelper accessHelperChecker = new AccessHelper(testConfig, userDetails);
        PaymentHelper paymentHelperChecker = new PaymentHelper(testConfig, new String[]{"UserDetails"}, userDetails);

        //Checker steps
        testConfig.logStep("Login to Customer FrontEnd as Admin user, Fill details and do login");
        accessHelperChecker.dashBoardPage = (DashBoardPage) accessHelperChecker.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Click on Transactions tab on left side navigation and click on Pending tab");
        paymentHelperChecker.transactionsPage = accessHelperChecker.dashBoardPage.clickTransactionsButtonOnLeftSideNavigation();
        paymentHelperChecker.transactionsPage.goToPendingTab();

        testConfig.logStep("Search for the Pending transfer on Pending tab and approve it from transfer details side panel");
        paymentHelperChecker.transactionsPage.approveTransferFromTransferDetailsSidePanelOnPendingTab(MakerCheckerFlowType.Approve, testConfig.testData.get("reference"));

        testConfig.logStep("Validate approved pending transfer is not present on Pending tab after approval and is displayed on Past tab");
        paymentHelperChecker.transactionsPage.validatePendingTransferNotPresentInPendingTabAfterApprovalAndIsDisplayedInPastTab(testConfig.testData.get("reference"), approvedTransferCount);
    }
}