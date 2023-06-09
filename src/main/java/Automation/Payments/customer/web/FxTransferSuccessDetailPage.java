package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

public class FxTransferSuccessDetailPage
{

    private final Config testConfig;

    @FindBy(xpath = "//div[contains(@class, 'transfer-step-completed')]//div[contains(@class,'text-secondary')]")
    private WebElement customerTransferCompletedOverviewAmountText;
    @FindBy(xpath = "//div[@data-cy='transaction-details-title']")
    private WebElement customerTransferSuccessOverviewTitleText;
    @FindBy(xpath = "//a[@data-cy='transaction-details-page-go-home-button']")
    private WebElement customerTransferSuccessDetailGoHomeButton;
    @FindBy(css = "div.transaction-details-mobile__content-amount.text-center > div")
    private WebElement customerTransferInitiatedSourceAmountText;
    @FindBy(css = "main > div.transaction-details")
    private WebElement customerTransferDetailsWrapper;
    @FindBy(css = ".transaction-details-mobile__content-amount")
    private WebElement contentAmountLabel;
    @FindBy(css = ".transaction-details-stepper__content")
    private WebElement transferStepperContentBox;
    @FindBy(css = ".transaction-details-page__share-button--with-home-button")
    private WebElement downloadButton;
    @FindBy(css = ".transaction-details__content-fields")
    private WebElement contentDetailsBox;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-transfer-fee']")
    private WebElement transferFee;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-reference']")
    private WebElement referenceValue;
    @FindBy(xpath = "//div[@data-cy='transactions-details-one-column-recipient-name']")
    private WebElement recipientName;
    @FindBy(xpath = "//span[@class='text-secondary']")
    private WebElement sourceAmount;
    @FindBy(xpath = "//span[@class='text-primary']")
    private WebElement targetAmount;
    @FindBy(xpath = "//div[@data-cy='legacy-transaction-category-select-selected-name']")
    private WebElement categoryValue;

    public FxTransferSuccessDetailPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerTransferSuccessDetailGoHomeButton);
        verifyFxTransferSuccessDetailPage();
    }

    private void verifyFxTransferSuccessDetailPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Transaction Details Page", customerTransferDetailsWrapper);
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Amount Label", contentAmountLabel);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Stepper Content Box", transferStepperContentBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Download Button", downloadButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Details Box", contentDetailsBox);
        // TODO SGD FX transfer does not show Category value
//        AssertHelper.assertElementIsDisplayed(testConfig, "Category", categoryValue);
        AssertHelper.assertElementIsDisplayed(testConfig, "Source Amount", sourceAmount);
        AssertHelper.assertElementIsDisplayed(testConfig, "Target Amount", targetAmount);
        AssertHelper.assertElementIsDisplayed(testConfig, "Recipient Name", recipientName);
        AssertHelper.assertElementIsDisplayed(testConfig, "Reference Value", referenceValue);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Fee", transferFee);
    }

    public void validateFxAmount(String expectSourceAmount, TransferType transferType, String sourceCurrency, String targetCurrency)
    {
        testConfig.logComment(String.format("Validate amount: {%s}", expectSourceAmount));
        WebElement element = null;
        switch (transferType)
        {
            case Completed, Scheduled, Recurring, Submitted, ScheduledSubmitted, RecurringSubmitted ->
            {
                element = customerTransferCompletedOverviewAmountText;
                expectSourceAmount = String.format("%,.2f", Double.valueOf(expectSourceAmount));
            }
            case Initiated ->
            {
                element = customerTransferInitiatedSourceAmountText;
                expectSourceAmount = sourceCurrency + " " + String.format("%,.2f", Double.valueOf(expectSourceAmount)) + "\n" + targetCurrency;
            }
            default -> testConfig.logFail("The 'Transfer Type' option is not defined yet.");
        }

        String actualSourceAmount = Element.getText(testConfig, element, "Actual source amount");
        AssertHelper.compareContains(testConfig, "Transfer amount", expectSourceAmount, actualSourceAmount);
    }

    public void validateTransferTitle(TransferType transferType)
    {
        testConfig.logComment("Validate Transfer title");
        String title = "Transfer page title";
        Element.isElementDisplayed(testConfig, customerTransferSuccessOverviewTitleText);
        String actualTitle = Element.getText(testConfig, customerTransferSuccessOverviewTitleText, title);

        switch (transferType)
        {
            case Initiated ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferInitiated.getName(), actualTitle);
            case Completed ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.TransferCompleted.getName(), actualTitle);
            default -> testConfig.logFail("Please check transfer type");
        }
    }

    public void validateFxTransferDetailsInfo(HashMap<String, String> testData, String targetAmount)
    {
        validateFxAmount(testData.get("amount"), TransferType.valueOf(testData.get("transferType")), testData.get("transferCurrency"), targetAmount);
        validateTransferTitle(TransferType.valueOf(testData.get("transferType")));
    }
}
