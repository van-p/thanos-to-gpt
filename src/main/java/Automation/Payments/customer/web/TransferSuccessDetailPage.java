package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TransferSuccessDetailPage
{

    private final Config testConfig;

    @FindBy(xpath = "//div[contains(@class, 'transfer-step-completed')]//div[contains(@class,'text-secondary')]")
    private WebElement customerTransferCompletedOverviewAmountText;
    @FindBy(xpath = "//div[@data-cy='transaction-details-title']")
    private WebElement customerTransferSuccessOverviewTitleText;
    @FindBy(css = "[data-cy*='details-page-go-home-button']")
    private WebElement customerTransferSuccessDetailGoHomeButton;
    @FindBy(css = "div.transaction-details-mobile__content-amount.text-center > div")
    private WebElement customerTransferInitiatedSourceAmountText;
    @FindBy(css = "main > div.transaction-details")
    private WebElement customerTransferDetailsWrapper;
    @FindBy(css = ".transaction-details-mobile__content-amount")
    private WebElement contentAmountLabel;
    @FindBy(css = ".transaction-details-stepper__content")
    private WebElement transferStepperContentBox;
    @FindBy(css = ".transaction-details-receipt-notification__missing-receipt")
    private WebElement addMissingReceiptButton;
    @FindBy(css = ".transaction-details-page__share-button--with-home-button")
    private WebElement downloadButton;
    @FindBy(css = ".transaction-details__content-fields")
    private WebElement contentDetailsBox;
    @FindBy(css = ".transaction-details__content-column2")
    private WebElement contentDetailColumnsBox;
    @FindBy(css = "[data-cy*='details-page-manage-button']")
    private WebElement manageButton;
    @FindBy(css = "[data-cy='modify-upcoming-transfer-form-modal-stop-button']")
    private WebElement stopTransferButton;
    @FindBy(css = "[data-cy='transaction-details-stepper-resume-cta']")
    private WebElement resumeTransferButton;
    @FindBy(css = "[data-cy='modify-upcoming-transfer-form-modal-modify-button']")
    private WebElement modifyTransferButton;
    @FindBy(css = ".horizontal-stepper__top-section>div[class*='horizontal-stepper__step']:last-of-type>div[class*='text-subtitle']")
    private WebElement finalStatusText;
    @FindBy(xpath = "//div[@class='q-notification__message col']")
    private WebElement toastMessage;
    @FindBy(css = "[class*='scheduled-payment-first-stopped-tip-modal__footer'] button")
    private WebElement recurringTooltipGotItButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[contains(@class,'transaction-details-field__label')]//div")
    private WebElement budgetLabel;
    @FindBy(xpath = "//span[contains(@class,'transfer-budget-select__budget-required')]")
    private WebElement budgetRequiredLabel;
    @FindBy(css = "[data-cy='field-budget-value']")
    private WebElement budgetValue;

    public TransferSuccessDetailPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerTransferSuccessDetailGoHomeButton);
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading bar");
        verifyTransferSuccessDetailPage();
    }

    private void verifyTransferSuccessDetailPage()
    {
        TransferType actual = TransferType.valueOf(testConfig.testData.get("transferType"));
        AssertHelper.assertElementIsDisplayed(testConfig, "Transaction Details Page", customerTransferDetailsWrapper);
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Amount Label", contentAmountLabel);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Stepper Content Box", transferStepperContentBox);
        if (!actual.equals(TransferType.Scheduled) && !actual.equals(TransferType.Recurring))
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "Download Button", downloadButton);
        }
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Details Box", contentDetailsBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Detail Columns Box", contentDetailColumnsBox);
    }

    public void validateAmount(String expectAmount, TransferType transferType, String transferCurrency)
    {
        testConfig.logComment(String.format("Validate amount: {%s}", expectAmount));
        WebElement element = null;
        switch (transferType)
        {
            case Submitted, ScheduledSubmitted, RecurringSubmitted ->
            {
                element = customerTransferCompletedOverviewAmountText;
                expectAmount = String.format("%,.2f", Double.valueOf(expectAmount));
            }
            case Completed, Initiated, Scheduled, Recurring ->
            {
                element = customerTransferInitiatedSourceAmountText;
                expectAmount = transferCurrency + " " + String.format("%,.2f", Double.valueOf(expectAmount));
            }
            default -> testConfig.logFail("The 'Transfer Type' option is not defined yet.");
        }
        Element.isElementDisplayed(testConfig, element);
        AssertHelper.assertElementText(testConfig, "Transfer amount", expectAmount, element);
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
            case Scheduled ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.ScheduledTransfer.getName(), actualTitle);
            case Recurring ->
                    AssertHelper.compareEquals(testConfig, title, TransferTitleEnum.RecurringTransfer.getName(), actualTitle);
            default -> testConfig.logFail("Please check transfer type");
        }
    }

    public void validateLocalTransferInfo()
    {
        validateAmount(testConfig.testData.get("amount"), TransferType.valueOf(testConfig.testData.get("transferType")),
                testConfig.testData.get("transferCurrency"));
        validateTransferTitle(TransferType.valueOf(testConfig.testData.get("transferType")));
        if (!StringUtils.isEmpty(testConfig.getRunTimeProperty("userRole")) && testConfig.getRunTimeProperty("userRole").equalsIgnoreCase("nonAdminBO"))
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, budgetValue, "Budget Label");
            AssertHelper.compareEquals(testConfig, "Budget Label", "Budget",
                    Element.getText(testConfig, budgetLabel, "Budget Label").trim());
            AssertHelper.compareEquals(testConfig, "Prefilled Budget value", testConfig.testData.get("assignedBudgetName"),
                    Element.getText(testConfig, budgetValue, "Budget Value").trim());
        }
    }

    public void validateScheduleStatusCorrect(String status)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, finalStatusText, "status text");
        String actualStatusText = Element.getText(testConfig, finalStatusText, "status text");
        AssertHelper.compareContains(testConfig, "schedule transfer button", status, actualStatusText);
    }

    public void clickOnManageButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, manageButton, "Manage button");
        Element.click(testConfig, manageButton, "Manage button", true);
    }

    public void clickOnStopTransferButton()
    {
        clickOnManageButton();
        WaitHelper.waitForElementToBeDisplayed(testConfig, stopTransferButton, "stop transfer button");
        Element.isElementDisplayed(testConfig, stopTransferButton);
        Element.click(testConfig, stopTransferButton, "stop transfer button");
        WaitHelper.waitForElementToBeHidden(testConfig, stopTransferButton, "stop transfer button");
    }

    public void modifyTransferButton()
    {
        clickOnManageButton();
        WaitHelper.waitForElementToBeClickable(testConfig, modifyTransferButton, "modify transfer button");
        Element.click(testConfig, modifyTransferButton, "modify transfer button");
        WaitHelper.waitForElementToBeHidden(testConfig, modifyTransferButton, "modify transfer button");
    }

    public void clickOnResumeTransferButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, resumeTransferButton, "resume transfer button");
        Element.click(testConfig, resumeTransferButton, "resume transfer button");
        WaitHelper.waitForElementToBeHidden(testConfig, resumeTransferButton, "resume transfer button");
    }

    public void validateToastMessageCorrect(String expectedMessage)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, toastMessage, "toast message");
        if (Element.isElementDisplayed(testConfig, toastMessage))
        {
            AssertHelper.assertElementText(testConfig, "toast message", expectedMessage, toastMessage);
            WaitHelper.waitForElementToBeHidden(testConfig, toastMessage, "toast message");
            WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "loading");
        } else if (Element.isElementDisplayed(testConfig, recurringTooltipGotItButton))
        {
            Element.click(testConfig, recurringTooltipGotItButton, "Got it button on Recurring transfer tooltip popup");
            WaitHelper.waitForElementToBeHidden(testConfig, recurringTooltipGotItButton, "recurring tooltip got it button");
        }
    }
}