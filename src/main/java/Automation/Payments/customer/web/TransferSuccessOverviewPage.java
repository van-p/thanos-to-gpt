package Automation.Payments.customer.web;

import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentEnums.Frequency;
import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Utils.*;
import Automation.Utils.Enums.DateTimeStringFormatEnum;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

import static Automation.Utils.CommonUtilities.getDayOfMonthSuffix;

public class TransferSuccessOverviewPage
{

    private final Config testConfig;
    @FindBy(xpath = "//div[contains(@class, 'transaction-details') and contains(@class, 'content-amount')]/div/div")
    private WebElement customerTransferOverviewAmountText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-with-notification-header-title']")
    private WebElement customerTransferSuccessOverviewTitleText;
    @FindBy(xpath = "//button[@data-cy='transfer-step-completed-view-transfer-detail-button'] | //*[contains(@class,'scheduled-payment-edit-completed__cta')]//button[2]")
    private WebElement viewTransferDetailsButton;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-amount']")
    private WebElement customerTransferSubmittedTransactionModalAmountText;
    @FindBy(css = ".transfer-step-completed-with-notification__transfer-amount.bg-white > div > div.text-secondary> div")
    private WebElement customerTransferSubmittedOverviewAmountText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-amount']")
    private WebElement customerOverviewAmountText;
    @FindBy(css = ".transfer-step-completed-with-notification__new-balance-text")
    private WebElement newBalanceLabel;
    @FindBy(css = ".counterparty-details-dropdown")
    private WebElement counterpartyDetailsDropdown;
    @FindBy(css = ".transfer-step-completed__go-to-home-button")
    private WebElement goToHomeButton;
    @FindBy(xpath = "//*[contains(@class,'scheduled-payment-edit-completed__cta')]//button[1]")
    private WebElement goBackToTransactionButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = ".text-subtitle2.q-mt-md > span.text-weight-bold")
    private WebElement headerExpectedDate;
    @FindBy(xpath = "//div[contains(text(), 'Transfer processing')]")
    private WebElement customerTransferSuccessOverviewTemporaryTitleText;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//*[@data-cy='transfer-step-completed-with-notification-scheduled-text']")
    private WebElement scheduledText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-with-notification-fx-transfer-amount']")
    private WebElement newBalanceAfterTransfer;

    public TransferSuccessOverviewPage(Config config, TransferType transferType)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, viewTransferDetailsButton);
        WaitHelper.waitForElementToBeDisplayed(testConfig, viewTransferDetailsButton, "View transfer details");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading bar");
        verifyTransferSuccessOverviewPage(transferType);
    }

    private void verifyTransferSuccessOverviewPage(TransferType transferType)
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "View transfer details", viewTransferDetailsButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Counterparty details", counterpartyDetailsDropdown);
        if (Element.isElementDisplayed(testConfig, goToHomeButton))
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "Go to Home button", goToHomeButton);
        } else
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "Go back to transaction button", goBackToTransactionButton);
        }
        if (!StringUtils.isEmpty(testConfig.getRunTimeProperty("userRole")) && testConfig.getRunTimeProperty("userRole").equalsIgnoreCase("nonAdminBO"))
            return;

        if (transferType.equals(TransferType.Initiated) || transferType.equals(TransferType.Completed))
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "New balance label", newBalanceLabel);
        }
    }

    public void validateAmount(String expectAmount, TransferType transferType, String transferCurrency)
    {
        testConfig.logComment(String.format("Validate amount: {%s}", expectAmount));
        WaitHelper.waitForPageLoad(testConfig, customerOverviewAmountText);
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerOverviewAmountText, "amount");
        String format = "%,.2f";
        String desc = "Transfer amount";
        expectAmount = String.format(format, Double.valueOf(expectAmount));
        switch (transferType)
        {
            case ScheduledSubmitted, RecurringSubmitted ->
                    AssertHelper.assertElementText(testConfig, desc, expectAmount, customerTransferSubmittedOverviewAmountText);
            case Completed, Initiated, Scheduled, Recurring, Submitted ->
            {
                expectAmount = transferCurrency + " " + expectAmount;
                AssertHelper.assertElementText(testConfig, desc, expectAmount, customerOverviewAmountText);
            }
            default -> testConfig.logFail("The 'Transfer Type' option is not defined yet.");
        }
    }

    public void validateTransferTitle(TransferType transferType)
    {
        testConfig.logComment("Validate Transfer page title");
        String title = "Transfer page title";
        WaitHelper.waitForPageLoad(testConfig, customerTransferSuccessOverviewTitleText);
        WaitHelper.waitForElementToBeHidden(testConfig, customerTransferSuccessOverviewTemporaryTitleText, "Transfer processing");
        String actualTitle = Element.getText(testConfig, customerTransferSuccessOverviewTitleText, title);
        switch (transferType)
        {
            case Initiated ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferInitiated.getName(), actualTitle);
            case Completed ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferCompleted.getName(), actualTitle);
            case Submitted ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferSubmitted.getName(), actualTitle);
            case Scheduled ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.ScheduledTransfer.getName(), actualTitle);
            case Recurring ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.RecurringTransfer.getName(), actualTitle);
            default -> testConfig.logFail("Please check transfer type");
        }
    }

    public void validateLocalTransferInfo(Map<String, String> testData)
    {
        validateAmount(testData.get("amount"), TransferType.valueOf(testData.get("transferType")), testData.get("transferCurrency"));
        validateTransferTitle(TransferType.valueOf(testData.get("transferType")));
    }

    public void validateModifiedLocalTransferInfo(String amount, Map<String, String> testData)
    {
        validateAmount(amount, TransferType.valueOf(testData.get("transferType")), testData.get("transferCurrency"));
        validateTransferTitle(TransferType.valueOf(testData.get("transferType")));
    }

    public TransferSuccessDetailPage clickViewTransferDetailsButton()
    {
        Element.isElementDisplayed(testConfig, viewTransferDetailsButton);
        Element.click(testConfig, viewTransferDetailsButton, "View Transfer Details");
        return new TransferSuccessDetailPage(testConfig);
    }

    public DashBoardPage clickGoToHomeButton()
    {
        Element.isElementDisplayed(testConfig, goToHomeButton);
        Element.click(testConfig, goToHomeButton, "Go To Home button");
        return new DashBoardPage(testConfig);
    }

    public TransferSubmittedDetailPage clickViewTransferDetailsButtonFromSubmitTransfer()
    {
        Element.isElementDisplayed(testConfig, viewTransferDetailsButton);
        Element.click(testConfig, viewTransferDetailsButton, "View Transfer Details");
        return new TransferSubmittedDetailPage(testConfig);
    }

    public void validateScheduleInfoCorrect(String date, Frequency frequency)
    {
        testConfig.logComment("Validate Schedule info correct");
        WaitHelper.waitForElementToBeDisplayed(testConfig, scheduledText, "schedule text");
        LocalDate localDate = DataGenerator.convertStringToLocalDateGivenFormat(date, DateTimeStringFormatEnum.DATE_FORMAT_TRANSFER.getFormat());
        String expectedDate = localDate.getDayOfMonth() + getDayOfMonthSuffix(localDate.getDayOfMonth());
        switch (frequency)
        {
            case OneTime ->
                    AssertHelper.assertElementText(testConfig, "onetime info on overview page", String.format("Scheduled for %s", date), scheduledText);
            case Weekly ->
                    AssertHelper.assertElementText(testConfig, "weekly info on overview page", String.format("Repeats every %s", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US)), scheduledText);
            case Monthly ->
                    AssertHelper.assertElementText(testConfig, "monthly info on overview page", String.format("Repeats every %s", expectedDate), scheduledText);
            default -> testConfig.logFail("Please check frequency");
        }
    }
}