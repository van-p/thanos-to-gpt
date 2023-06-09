package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReviewTransferPage
{

    private final Config testConfig;
    @FindBy(xpath = "//div[@data-cy='transfer-step-review-verify-amount']")
    private WebElement customerLocalTransferDetailsAmountText;
    @FindBy(xpath = "//div[@data-cy='source-amount-input-currency-info']")
    private WebElement customerLocalTransferDetailsAmountUSDCurrencyText;
    @FindBy(xpath = "//div[contains(@class,'transaction-category-select__category-name')]")
    private WebElement customerReviewTransferCategoryText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-review-reference']")
    private WebElement customerReviewTransferReferenceText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-review-additional-info-purpose-label']")
    private WebElement additionalInfoPurposeLabel;
    @FindBy(xpath = "//div[@data-cy='transfer-step-review-additional-info-files-label']")
    private WebElement additionalInfoFilesLabel;
    @FindBy(css = ".example-cta-box--fixed.example-cta > div > button")
    private WebElement customerLocalTransferDetailsConfirmButton;
    @FindBy(xpath = "//button[@data-cy='transfer-step-review-transaction-submit-button']")
    private WebElement customerLocalTransferDetailsSubmitButton;
    @FindBy(css = "div.verify-otp-form__input")
    private WebElement otpForm;
    @FindBy(xpath = "//input[@data-cy='digit-input-real']")
    private WebElement otpActivateCodeTextBox;
    @FindBy(css = "[data-cy='transfer-step-make-header-title']")
    private WebElement reviewTransferPageTitle;
    @FindBy(xpath = "//div[@data-cy='transfer-step-make-header-title']")
    private WebElement usdReviewTransferPageTitle;
    @FindBy(css = "[data-cy='currency-account-select']")
    private WebElement currencyAccountSelectBox;
    @FindBy(css = "div.transfer-step-review-verify-amount__amount-box")
    private WebElement sourceAmountBox;
    @FindBy(xpath = "//div[@data-cy='source-amount-input-input-box']")
    private WebElement usdSourceAmountBox;
    @FindBy(css = "[data-cy='transfer-local-arrived-time-panel-tooltip']")
    private WebElement arriveTimeBox;
    @FindBy(xpath = "//div[@data-cy='fx-quote-box-estimated-delivery-time']")
    private WebElement usdArriveTimeBox;
    @FindBy(css = "div.transfer-step-review__detail-panel")
    private WebElement transferMakeForm;
    @FindBy(css = ".legacy-transaction-category-select")
    private WebElement usdTransferMakeForm;
    @FindBy(css = ".transfer-step-make-header__account-number")
    private WebElement transferHeaderAccountNumber;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;
    @FindBy(xpath = "//div[@data-cy='currency-account-select-debit-account-currency-code']/span")
    private WebElement customerLocalTransferDetailsSelectedSourceCurrency;
    @FindBy(xpath = "//div[@data-cy='fx-quote-box-estimated-delivery-time']")
    private WebElement customerReviewTransferFXNotificationText;
    @FindBy(xpath = "//div[@data-cy='fx-quote-stepper-fee']")
    private WebElement customerReviewTransferTransferFeeText;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "[data-cy='transfer-step-review-schedule']")
    private WebElement scheduleDateText;
    @FindBy(css = "[class='transfer-step-review-schedule__frequency']")
    private WebElement scheduleFrequencyText;
    @FindBy(css = "[data-cy='confirm-modify-transfer-modal-continue-button']")
    private WebElement noteSubmitButton;
    @FindBy(css = "[data-cy='confirm-modify-transfer-modal-text-description']")
    private WebElement noteDescriptionButton;
    @FindBy(xpath = "//div[contains(@class,'transaction-details-field__label')]//div")
    private WebElement budgetLabel;
    @FindBy(xpath = "//span[contains(@class,'transfer-budget-select__budget-required')]")
    private WebElement budgetRequiredLabel;
    @FindBy(css = "[data-cy='field-budget-value']")
    private WebElement budgetValue;

    public ReviewTransferPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerLocalTransferDetailsConfirmButton);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar");
        verifyReviewTransferPage();
    }

    private void verifyReviewTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Account Number", transferHeaderAccountNumber);
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Currency Account Select", currencyAccountSelectBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Confirm Button", customerLocalTransferDetailsConfirmButton);
        switch (testConfig.getRunTimeProperty("recipientCurrency").toLowerCase())
        {
            case "usd":
                AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer USD Source Amount Box", usdSourceAmountBox);
                AssertHelper.assertElementIsDisplayed(testConfig, "USD Review Transfer Arrive Time Box", usdArriveTimeBox);
                AssertHelper.assertElementIsDisplayed(testConfig, "USD Review Transfer Make Form", usdTransferMakeForm);
                AssertHelper.assertElementIsDisplayed(testConfig, "Recipient Page Title", usdReviewTransferPageTitle);
                break;
            case "sgd":
                AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Source Amount Box", sourceAmountBox);
                AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Arrive Time Box", arriveTimeBox);
                AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Make Form", transferMakeForm);
                AssertHelper.assertElementText(testConfig, "Recipient Page Title", PaymentHelper.paymentStaticDataBase.getReviewTransferPageTitle(), reviewTransferPageTitle);
                break;
            case "idr":
                AssertHelper.assertElementText(testConfig, "Review Page Title", PaymentHelper.paymentStaticDataBase.getReviewTransferPageTitle(), reviewTransferPageTitle);
                break;
            default:
                testConfig.logFail("Please select another currency");
                break;
        }
    }

    public void validateInstantLocalTransferInfo(boolean isAdditionalInfo)
    {
        AssertHelper.assertElementText(testConfig, "Amount",
                testConfig.testData.get("transferCurrency") + " " + String.format("%,.2f", Double.valueOf(testConfig.testData.get("amount"))),
                customerLocalTransferDetailsAmountText);
        AssertHelper.assertElementText(testConfig, "Category", testConfig.testData.get("transferCategory"), customerReviewTransferCategoryText);
        AssertHelper.assertElementText(testConfig, "Reference", testConfig.testData.get("reference").trim(), customerReviewTransferReferenceText);

        if (!StringUtils.isEmpty(testConfig.getRunTimeProperty("userRole")) && testConfig.getRunTimeProperty("userRole").equalsIgnoreCase("nonAdminBO"))
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, budgetLabel, "Budget Label");
            AssertHelper.compareEquals(testConfig, "Budget Label", PaymentHelper.paymentStaticDataBase.getBudgetLabel(),
                    Element.getText(testConfig, budgetLabel, "Budget Label").trim());
            AssertHelper.compareEquals(testConfig, "Prefilled Budget value", testConfig.testData.get("assignedBudgetName"),
                    Element.getText(testConfig, budgetValue, "Budget Value").trim());
        }

        if (isAdditionalInfo)
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "Purpose Label", additionalInfoPurposeLabel);
            AssertHelper.assertElementIsDisplayed(testConfig, "Files Label", additionalInfoFilesLabel);
        }
    }

    public void validateMcaInstantLocalTransferInfo(CurrencyEnum currencyEnum)
    {
        AssertHelper.assertElementText(testConfig, "Source Currency", currencyEnum.toString(), customerLocalTransferDetailsAmountUSDCurrencyText);
        //TODO FE miss element for source amount
        //        AssertHelper.assertElementText(testConfig, "Amount",testData.get("amount") + ".00", sourceAmountBox);
        AssertHelper.assertElementText(testConfig, "From Source Currency", testConfig.getRunTimeProperty("selectedSourceCurrency"), customerLocalTransferDetailsSelectedSourceCurrency);
        AssertHelper.assertElementText(testConfig, "Transfer Fee", testConfig.getRunTimeProperty("transferFee"), customerReviewTransferTransferFeeText);
        AssertHelper.assertElementText(testConfig, "Category value", testConfig.getRunTimeProperty("transferCategoryValue"), customerReviewTransferCategoryText);
        AssertHelper.assertElementText(testConfig, "Reference", testConfig.getRunTimeProperty("referenceValue").trim(), customerReviewTransferReferenceText);
    }

    public TransferSuccessOverviewPage confirmTransfer(String otpCode, TransferType transferType)
    {
        WaitHelper.waitForOptionalElement(testConfig, spinner, "spinner");
        WaitHelper.waitForElementToBeHidden(testConfig, spinner, "spinner");
        WaitHelper.waitForElementToBeClickable(testConfig, customerLocalTransferDetailsConfirmButton, "Confirm button");
        Element.click(testConfig, customerLocalTransferDetailsConfirmButton, "Confirm button");
        WaitHelper.waitForOptionalElement(testConfig, otpForm, "OTP Form");
        if (Element.isElementDisplayed(testConfig, otpForm))
        {
            Element.enterDataWithoutSelectedElement(testConfig, otpCode, "OTP Code");
        }
        if ((transferType.equals(TransferType.Scheduled) || transferType.equals(TransferType.Recurring)) && Element.isElementDisplayed(testConfig, noteSubmitButton))
        {
            validateScheduleNotePopup();
            Element.click(testConfig, noteSubmitButton, "Got it button on note popup");
        }
        return new TransferSuccessOverviewPage(testConfig, transferType);
    }

    public TransferSuccessOverviewPage submitTransfer(TransferType transferType)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerLocalTransferDetailsSubmitButton, "Submit button");
        Element.click(testConfig, customerLocalTransferDetailsSubmitButton, "Submit button");
        return new TransferSuccessOverviewPage(testConfig, transferType);
    }

    public void validateModifiedAmount(String amount)
    {
        AssertHelper.assertElementText(testConfig, "Amount", "SGD " + amount + ".00", customerLocalTransferDetailsAmountText);
    }

    public void validateScheduleInfoCorrect(String date, String frequency)
    {
        String actual = Element.getText(testConfig, scheduleDateText, "Get schedule date text");
        AssertHelper.compareContains(testConfig, "Get schedule date text", date, actual);
        AssertHelper.assertElementText(testConfig, "schedule frequency info", frequency, scheduleFrequencyText);
    }

    public void validateScheduleNotePopup()
    {
        AssertHelper.assertElementText(testConfig, "schedule note description", "The original transfer will be immediately stopped and a new transfer will be created", noteDescriptionButton);
    }
}