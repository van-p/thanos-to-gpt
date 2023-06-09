package Automation.Payments.web.usd;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

import static Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum.SGD;
import static Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum.USD;

public class TestMcaConversionTransfer extends TestBase
{

    @TestVariables(testrailData = "43:C12897", automatedBy = QA.Natali)
    @Test(description = "Verify that user can make fx conversion from Sgd to Usd by checker role", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testFxConversionFromSgdToUsdByChecker(Config testConfig)
    {
        int localTransferInfoRowNum = 1;
        int userDetailDataRow = 12;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailDataRow, localTransferInfoRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Login CF as checker account role");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the SGD Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(SGD.name(), TransferType.valueOf(testConfig.testData.get("transferType")));

        testConfig.logStep("Click on 'Convert Money' button");
        paymentHelper.debitDetailPage.clickConvertMoneyButton();

        testConfig.logStep("Click on 'Convert SGD To USD' radio");
        paymentHelper.debitDetailPage.clickConvertSgdToUsdRadio();

        testConfig.logStep("Click on 'Take Me To Fx Transfer Screen' button'");
        paymentHelper.fxTransferPage = paymentHelper.debitDetailPage.clickTakeMeToFxTransferScreenButton(SGD);

        testConfig.logStep("Input SGD - USD transfer info & click Next button, then validate SGD - USD transfer info");
        paymentHelper.fxTransferPage.inputSgdFxTransferInfo(testConfig.testData);
        paymentHelper.reviewFxTransferPage = paymentHelper.fxTransferPage.clickToNextButton(SGD);
        paymentHelper.reviewFxTransferPage.validateFxTransferInfo(testConfig.testData, SGD, USD);

        testConfig.logStep("Click on Confirm button and then validate FX transfer info on Transfer success overview screen");
        paymentHelper.fxTransferSuccessOverviewPage = paymentHelper.reviewFxTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.fxTransferSuccessOverviewPage.validateFxTransferInfo(testConfig.testData, USD);

        testConfig.logStep("Click on View transfer details button & Validate FX transfer info");
        paymentHelper.fxTransferSuccessDetailPage = paymentHelper.fxTransferSuccessOverviewPage.clickViewFxTransferDetailsButton();
        paymentHelper.fxTransferSuccessDetailPage.validateFxTransferDetailsInfo(testConfig.testData, USD.name());
    }

    @TestVariables(testrailData = "43:C12899", automatedBy = QA.Natali)
    @Test(description = "Verify that user can make fx conversion from Usd to Sgd by checker role", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testFxConversionFromUsdToSgdByChecker(Config testConfig)
    {
        int localTransferInfoRowNum = 5;
        int userDetailDataRow = 12;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo"}, userDetailDataRow, localTransferInfoRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailDataRow);

        testConfig.logStep("Login CF as checker account role");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the USD Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(USD.name(), TransferType.valueOf(testConfig.testData.get("transferType")));

        testConfig.logStep("Click on 'Convert Money' button");
        paymentHelper.debitDetailPage.clickConvertMoneyButton();

        testConfig.logStep("Click on 'Convert USD To SGD' radio");
        paymentHelper.debitDetailPage.clickConvertUsdToSgdRadio();

        testConfig.logStep("Click on 'Take Me To Transfer Screen' button");
        paymentHelper.transferPage = paymentHelper.debitDetailPage.clickTakeMeToTransferScreenButton();

        testConfig.logStep("Input USD -SGD transfer info & click Next button, then validate USD -SGD transfer info");
        paymentHelper.transferPage.inputUsdInstantLocalTransferInfo(testConfig.testData, USD);
        paymentHelper.reviewTransferPage = paymentHelper.transferPage.clickNextButton();
        paymentHelper.reviewTransferPage.validateMcaInstantLocalTransferInfo(USD);

        testConfig.logStep("Click on Confirm button and then validate FX transfer info on Transfer success overview screen");
        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);

        testConfig.logStep("Click on View transfer details button & Validate FX transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
    }
}