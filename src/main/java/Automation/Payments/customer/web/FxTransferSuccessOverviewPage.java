package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class FxTransferSuccessOverviewPage
{

    private final Config testConfig;
    @FindBy(xpath = "//div[contains(@class, 'transfer-step-completed')]//div[contains(@class,'text-secondary')]")
    private WebElement secondaryAmountText;
    @FindBy(xpath = "//div[contains(@class, 'transaction-details') and contains(@class, 'content-amount')]/div/div")
    private WebElement customerTransferOverviewAmountText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-with-notification-header-title']")
    private WebElement customerTransferSuccessOverviewTitleText;
    @FindBy(xpath = "//button[@data-cy='transfer-step-completed-view-transfer-detail-button']")
    private WebElement transferDetailsButton;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-amount']")
    private WebElement transactionModalAmountText;
    @FindBy(css = ".transfer-step-completed-with-notification__transfer-amount.bg-white > div > div.text-secondary> div")
    private WebElement submittedOverviewAmountText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-amount']")
    private WebElement initiatedOverviewAmountText;
    @FindBy(css = ".transfer-step-completed-with-notification__new-balance-text")
    private WebElement newBalanceLabel;
    @FindBy(css = ".counterparty-details-dropdown")
    private WebElement counterpartyDetailsDropdown;
    @FindBy(css = ".transfer-step-completed__go-to-home-button")
    private WebElement goToHomeButton;
    @FindBy(xpath = "//div[contains(text(), 'Transfer processing')]")
    private WebElement overviewTemporaryTitleText;

    public FxTransferSuccessOverviewPage(Config config, TransferType transferType)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, transferDetailsButton);
        WaitHelper.waitForElementToBeDisplayed(testConfig, goToHomeButton, "go to Home button");
        verifyTransferFXSuccessOverviewPage(transferType);
    }

    private void verifyTransferFXSuccessOverviewPage(TransferType transferType)
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "View transfer details", transferDetailsButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Counterparty details", counterpartyDetailsDropdown);
        AssertHelper.assertElementIsDisplayed(testConfig, "Go to Home button", goToHomeButton);
        if (transferType != TransferType.Submitted)
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "New balance label", newBalanceLabel);
        }
    }

    public void validateFxAmount(String expectSourceAmount, TransferType transferType, String sourceCurrency, CurrencyEnum targetCurrency)
    {
        testConfig.logComment(String.format("Validate amount: {%s}", expectSourceAmount));
        String format = "%,.2f";
        String desc = "Transfer amount";
        expectSourceAmount = String.format(format, Double.valueOf(expectSourceAmount));
        switch (transferType)
        {
            case Completed, Scheduled, Recurring ->
                    AssertHelper.assertElementText(testConfig, desc, expectSourceAmount, secondaryAmountText);
            case Submitted ->
            {
                expectSourceAmount = sourceCurrency + " " + expectSourceAmount;
                AssertHelper.assertElementText(testConfig, desc, expectSourceAmount, transactionModalAmountText);
            }
            case ScheduledSubmitted, RecurringSubmitted ->
                    AssertHelper.assertElementText(testConfig, desc, expectSourceAmount, submittedOverviewAmountText);
            case Initiated ->
            {
                expectSourceAmount = sourceCurrency + " " + expectSourceAmount + "\n" + targetCurrency;
                String actualAmount = Element.getText(testConfig, initiatedOverviewAmountText, "Actual amount");
                AssertHelper.compareContains(testConfig, desc, expectSourceAmount, actualAmount);
            }
            default -> testConfig.logFail("The 'Transfer Type' option is not defined yet.");
        }
    }

    public void validateTransferTitle(TransferType transferType)
    {
        testConfig.logComment("Validate Transfer page title");
        String title = "Transfer page title";
        WaitHelper.waitForElementToBeHidden(testConfig, overviewTemporaryTitleText, "Transfer processing");

        String actualTitle = Element.getText(testConfig, customerTransferSuccessOverviewTitleText, title);
        switch (transferType)
        {
            case Initiated ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferInitiated.getName(), actualTitle);
            case Completed ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferCompleted.getName(), actualTitle);
            case Submitted ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferSubmitted.getName(), actualTitle);
            default -> testConfig.logFail("Please check transfer type");
        }
    }

    public void validateFxTransferInfo(Map<String, String> testData, CurrencyEnum targetCurrency)
    {
        validateFxAmount(testData.get("amount"), TransferType.valueOf(testData.get("transferType")), testData.get("transferCurrency"), targetCurrency);
        validateTransferTitle(TransferType.valueOf(testData.get("transferType")));
    }

    public FxTransferSuccessDetailPage clickViewFxTransferDetailsButton()
    {
        Element.isElementDisplayed(testConfig, transferDetailsButton);
        Element.click(testConfig, transferDetailsButton, "View Transfer Details");
        return new FxTransferSuccessDetailPage(testConfig);
    }
}
