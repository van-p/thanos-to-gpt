package Automation.Payments.web.advance;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestAdvanceTransferBOEmployee extends TestBase
{
    @TestVariables(testrailData = "44:C13031", automatedBy = QA.Himanshu)
    @Test(description = "Verify that Employee User Can Submit A Local Instant Transfer Initiated as Budget Owner", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void verifyAdvanceTransferBOEmployee(Config testConfig)
    {
        int userDetails = 31;
        int advanceTransferAdditionalInfoRowNum = 2;
        int localTransferInfoRowNum = 11;
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetails);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "AdvanceTransferAdditionalInfo"}, userDetails, localTransferInfoRowNum, advanceTransferAdditionalInfoRowNum);

        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AccessEnums.AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the Advance Limit Section");
        paymentHelper.recipientsPage = accessHelper.dashBoardPage.clickOnMakeTransferBOButton();
        testConfig.putRunTimeProperty("userRole", "nonAdminBO");
        paymentHelper.createUiAdvanceTransfer(testConfig, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
    }
}
