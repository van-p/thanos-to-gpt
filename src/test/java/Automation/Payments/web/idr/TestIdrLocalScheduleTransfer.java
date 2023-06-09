package Automation.Payments.web.idr;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.Frequency;
import Automation.Payments.customer.helpers.PaymentEnums.StatusEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.customer.helpers.PaymentStaticDataEn.SuccessMessageEnums;
import Automation.Payments.customer.web.ReviewTransferPage;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import Automation.Utils.Enums.DateTimeStringFormatEnum;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestIdrLocalScheduleTransfer extends TestBase
{

    @TestVariables(testrailData = "42:C12661", automatedBy = QA.ThuyNga)
    @Test(description = "Verify Admin can make IDR local transfer with schedule one time and stop transfer", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testAdminMakeIdrScheduledTransferOneTimeAndStopTransfer(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        int userDetails = 17, localTransferInfo = 7, counterPartiesInfo = 8;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "CounterPartiesInfo"}, userDetails, localTransferInfo, counterPartiesInfo);

        String date = DataGenerator.getDateFromToday(2, DateTimeStringFormatEnum.DATE_FORMAT_TRANSFER.getFormat());
        String frequency = Frequency.OneTime.getName();
        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the IDR Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.IDR.name(), TransferType.valueOf(testConfig.testData.get("transferType")));
        testConfig.logStep("Click on 'Make a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(CurrencyEnum.IDR);

        testConfig.logStep("Input IDR schedule information and click on Next button");
        paymentHelper.transferPage.inputScheduledLocalTransferInfo(date, frequency, CurrencyEnum.IDR);
        paymentHelper.transferPage.validateScheduleTransferInfoCorrect(date, frequency);
        paymentHelper.transferPage.clickOnNextButton();

        testConfig.logStep("Validate information on review page");
        paymentHelper.reviewTransferPage = new ReviewTransferPage(testConfig);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateScheduleInfoCorrect(date, frequency);

        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);
        paymentHelper.transferSuccessOverviewPage.validateScheduleInfoCorrect(date, Frequency.OneTime);

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
        paymentHelper.transferSuccessDetailPage.validateScheduleStatusCorrect(StatusEnum.Upcoming.getName());

        testConfig.logStep("Click on Manage button -> Stop transfer & Validate status");
        paymentHelper.transferSuccessDetailPage.clickOnStopTransferButton();
        paymentHelper.transferSuccessDetailPage.validateToastMessageCorrect(SuccessMessageEnums.TransferStopped.getName());
        paymentHelper.transferSuccessDetailPage.validateScheduleStatusCorrect(StatusEnum.Stopped.getName());
    }

    @TestVariables(testrailData = "42:C12662", automatedBy = QA.ThuyNga)
    @Test(description = "Verify Admin can make transfer with schedule weekly and modify schedule transfer", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testAdminMakeIdrRecurringTransferWeeklyAndModifyTransfer(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        int userDetails = 17, localTransferInfo = 8, counterPartiesInfo = 8;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "CounterPartiesInfo"}, userDetails, localTransferInfo, counterPartiesInfo);

        String firstDate = DataGenerator.getDateFromToday(2, DateTimeStringFormatEnum.DATE_FORMAT_TRANSFER.getFormat());
        String secondDate = DataGenerator.getDateFromToday(4, DateTimeStringFormatEnum.DATE_FORMAT_TRANSFER.getFormat());
        String frequency = Frequency.Weekly.getName();
        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the IDR Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.IDR.name(), TransferType.valueOf(testConfig.testData.get("transferType")));
        testConfig.logStep("Click on 'Make a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(CurrencyEnum.IDR);

        testConfig.logStep("Input IDR schedule information and click on Next button");
        paymentHelper.transferPage.inputScheduledLocalTransferInfo(firstDate, frequency, CurrencyEnum.IDR);
        paymentHelper.transferPage.validateScheduleTransferInfoCorrect(firstDate, frequency);
        paymentHelper.transferPage.clickOnNextButton();

        testConfig.logStep("Validate information on review page");
        paymentHelper.reviewTransferPage = new ReviewTransferPage(testConfig);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateScheduleInfoCorrect(firstDate, frequency);

        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);
        paymentHelper.transferSuccessOverviewPage.validateScheduleInfoCorrect(firstDate, Frequency.Weekly);

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
        paymentHelper.transferSuccessDetailPage.validateScheduleStatusCorrect(StatusEnum.NextTransfer.getName());

        testConfig.logStep("Click on Manage button -> Modify transfer & Click on Edit button");
        paymentHelper.transferSuccessDetailPage.modifyTransferButton();
        paymentHelper.transferPage.clickOnEditButton();

        testConfig.logStep("Update IDR schedule and click on Next button");
        paymentHelper.transferPage.selectScheduleDateAndFrequency(secondDate, frequency);
        paymentHelper.transferPage.validateScheduleTransferInfoCorrect(secondDate, frequency);
        paymentHelper.transferPage.clickOnNextButton();

        testConfig.logStep("Validate information on review page");
        paymentHelper.reviewTransferPage = new ReviewTransferPage(testConfig);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateScheduleInfoCorrect(secondDate, frequency);

        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);
        paymentHelper.transferSuccessOverviewPage.validateScheduleInfoCorrect(secondDate, Frequency.Weekly);
    }

    @TestVariables(testrailData = "42:C12663", automatedBy = QA.ThuyNga)
    @Test(description = "Verify Admin can make IDR local transfer with schedule monthly and resume transfer", dataProvider = "getTestConfig", groups = {"regression", "uiCases"})
    public void testAdminMakeIdrRecurringTransferMonthlyAndResumeTransfer(Config testConfig)
    {
        AccessHelper accessHelper = new AccessHelper(testConfig, 3);
        int userDetails = 17, localTransferInfo = 8, counterPartiesInfo = 8;
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, new String[]{"UserDetails", "LocalTransferInfo", "CounterPartiesInfo"}, userDetails, localTransferInfo, counterPartiesInfo);

        String date = DataGenerator.getDateFromToday(2, DateTimeStringFormatEnum.DATE_FORMAT_TRANSFER.getFormat());
        String frequency = Frequency.Monthly.getName();
        testConfig.logStep("Navigate to Customer FrontEnd, Fill details and do login");
        accessHelper.dashBoardPage = (DashBoardPage) accessHelper.doLogin(AfterLoginExpectedLandingPage.DashBoardPage);

        testConfig.logStep("Select the IDR Debit account");
        paymentHelper.debitDetailPage = accessHelper.dashBoardPage.selectDebitAccount(CurrencyEnum.IDR.name(), TransferType.valueOf(testConfig.testData.get("transferType")));
        testConfig.logStep("Click on 'Make a transfer' CTA button and select a random recipient in the list");
        paymentHelper.recipientsPage = paymentHelper.debitDetailPage.clickMakeATransferButton();
        paymentHelper.transferPage = paymentHelper.recipientsPage.selectARecipient(CurrencyEnum.IDR);

        testConfig.logStep("Input IDR schedule information and click on Next button");
        paymentHelper.transferPage.inputScheduledLocalTransferInfo(date, frequency, CurrencyEnum.IDR);
        paymentHelper.transferPage.validateScheduleTransferInfoCorrect(date, frequency);
        paymentHelper.transferPage.clickOnNextButton();

        testConfig.logStep("Validate information on review page");
        paymentHelper.reviewTransferPage = new ReviewTransferPage(testConfig);
        paymentHelper.reviewTransferPage.validateInstantLocalTransferInfo(false);
        paymentHelper.reviewTransferPage.validateScheduleInfoCorrect(date, frequency);

        paymentHelper.transferSuccessOverviewPage = paymentHelper.reviewTransferPage.confirmTransfer(testConfig.testData.get("otp"), TransferType.valueOf(testConfig.testData.get("transferType")));
        paymentHelper.transferSuccessOverviewPage.validateLocalTransferInfo(testConfig.testData);
        paymentHelper.transferSuccessOverviewPage.validateScheduleInfoCorrect(date, Frequency.Monthly);

        testConfig.logStep("Click on View transfer details button & Validate local transfer info");
        paymentHelper.transferSuccessDetailPage = paymentHelper.transferSuccessOverviewPage.clickViewTransferDetailsButton();
        paymentHelper.transferSuccessDetailPage.validateLocalTransferInfo();
        paymentHelper.transferSuccessDetailPage.validateScheduleStatusCorrect(StatusEnum.NextTransfer.getName());

        testConfig.logStep("Click on Manage button -> Stop transfer & Then click on Resume transfer");
        paymentHelper.transferSuccessDetailPage.clickOnStopTransferButton();
        paymentHelper.transferSuccessDetailPage.validateToastMessageCorrect(SuccessMessageEnums.TransferStopped.getName());
        paymentHelper.transferSuccessDetailPage.clickOnResumeTransferButton();
        paymentHelper.transferSuccessDetailPage.validateToastMessageCorrect(SuccessMessageEnums.TransferResumed.getName());
        paymentHelper.transferSuccessDetailPage.validateScheduleStatusCorrect(StatusEnum.NextTransfer.getName());
    }

}