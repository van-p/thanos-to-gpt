package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.*;
import Automation.Utils.Element.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FxTransferPage
{

    private final Config testConfig;
    private final String categoryItem = ".q-item__label";
    private final String customerFXTransferDetailsRecipientName = "//div[@data-cy='transfer-step-make-header-title']";
    @FindBy(xpath = "//input[@data-cy='source-amount-input-input-box-input']")
    private WebElement amountTextBox;
    @FindBy(css = "div.transfer-step-make-form > div.transfer-step-make-form__category-dropdown > div > div.example-field > label")
    private WebElement CategoryDropDown;
    @FindBy(xpath = "//button[@data-cy='transfer-step-make-reference-button']")
    private WebElement referenceButton;
    @FindBy(xpath = "//*[contains(@data-cy,'reference-input')]")
    private WebElement referenceTextBox;
    @FindBy(css = ".example-cta-box.example-cta-box--fixed.example-cta > div > button")
    private WebElement nextButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "div.example-page__header > div > div")
    private WebElement transferPageTitle;
    @FindBy(xpath = "//div[@data-cy='transfer-step-make-header-title']")
    private WebElement usdTransferPageTitle;
    @FindBy(xpath = "//div[@data-cy='currency-account-select-debit-account-currency-code']")
    private WebElement currencyAccountSelectBox;
    @FindBy(css = "div.source-amount-input__source-amount-box")
    private WebElement sourceAmountBox;
    @FindBy(css = "div.transfer-step-make-form")
    private WebElement transferMakeForm;
    @FindBy(css = ".transfer-step-make-header__account-number")
    private WebElement transferHeaderAccountNumber;


    public FxTransferPage(Config config, CurrencyEnum currencyEnum)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, amountTextBox);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeDisplayed(testConfig, nextButton, "Wait for Next Button");
        switch (currencyEnum)
        {
            case SGD -> verifySgdFxTransferPage();
            case USD -> verifyUsdFxTransferPage();
            default -> testConfig.logFail("Please select another currency");
        }
    }

    private void verifySgdFxTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Account Number", transferHeaderAccountNumber);
        AssertHelper.assertElementIsDisplayed(testConfig, "Next Button", nextButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Currency Account Select", currencyAccountSelectBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Source Amount Box", sourceAmountBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Make Form", transferMakeForm);
        AssertHelper.assertPartialElementText(testConfig, "Transfer Page Title", PaymentHelper.paymentStaticDataBase.getTransferPageTitle(), transferPageTitle);
    }

    private void verifyUsdFxTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Account Number", transferHeaderAccountNumber);
        AssertHelper.assertElementIsDisplayed(testConfig, "Next Button", nextButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Currency Account Select", currencyAccountSelectBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "USD Source Amount Box", sourceAmountBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Recipient Name", usdTransferPageTitle);
    }

    public void inputSgdFxTransferInfo(Map<String, String> testData)
    {
        String recipientName = Element.getText(testConfig, How.xPath, customerFXTransferDetailsRecipientName, "Recipient Name");
        testConfig.putRunTimeProperty("recipientName", Objects.requireNonNull(recipientName));
        testData.put("transferTitle", Objects.requireNonNull(getTransferTitleByTransferType(TransferType.valueOf(testData.get("transferType")))).getName());
        testData.put("transferOverviewTitle", Objects.requireNonNull(getTransferTitleByTransferType(TransferType.valueOf(testData.get("transferType")))).getName());

        // Input source amount
        WaitHelper.waitForElementToBeDisplayed(testConfig, amountTextBox, "Transfer amount");
        Element.enterData(testConfig, amountTextBox, testData.get("amount"), "Transfer amount");

        // Click category dropdown;
        WaitHelper.waitForElementToBeDisplayed(testConfig, CategoryDropDown, "Category Dropdown");
        WaitHelper.waitForElementToBeClickable(testConfig, CategoryDropDown, "Category Dropdown");
        Element.click(testConfig, CategoryDropDown, "Category Dropdown", true);
        WebElement item = Element.getPageElement(testConfig, How.css, categoryItem);
        WaitHelper.waitForOptionalElement(testConfig, item, "Category Dropdown Item");

        // Select random category;
        List<WebElement> categories = Element.getPageElements(testConfig, How.css, categoryItem);
        int categoryIndex = DataGenerator.generateRandomNumberInIntRange(0, Objects.requireNonNull(categories).size() - 2);
        WebElement categoryItem = categories.get(categoryIndex);
        testData.put("transferCategory", Element.getText(testConfig, categoryItem, "Category Item"));

        // Click random category;
        WaitHelper.waitForElementToBeClickable(testConfig, categoryItem, "Category Item");
        Element.click(testConfig, categoryItem, "Category Item");
        WaitHelper.waitForElementToBeDisplayed(testConfig, referenceButton, "Reference button");

        // Click Reference button and input value
        if (Element.isElementDisplayed(testConfig, referenceButton))
        {
            WaitHelper.waitForElementToBeClickable(testConfig, referenceButton, "Reference button");
            Element.click(testConfig, referenceButton, "Reference button");
            WaitHelper.waitForElementToBeDisplayed(testConfig, referenceTextBox, "Reference Text box");
            Element.enterData(testConfig, referenceTextBox, testData.get("reference"), "Reference");
        }
    }

    public ReviewFxTransferPage clickToNextButton(CurrencyEnum currencyEnum)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, nextButton, "Next button");
        Element.click(testConfig, nextButton, "Next button");
        return new ReviewFxTransferPage(testConfig, currencyEnum);
    }

    private TransferTitleEnum getTransferTitleByTransferType(TransferType transferType)
    {
        TransferTitleEnum expectTitle;
        switch (transferType)
        {
            case Recurring, RecurringSubmitted:
                expectTitle = TransferTitleEnum.RecurringTransfer;
                break;
            case Scheduled, ScheduledSubmitted:
                expectTitle = TransferTitleEnum.ScheduledTransfer;
                break;
            case Submitted:
                expectTitle = TransferTitleEnum.TransferSubmitted;
                break;
            case Initiated:
                expectTitle = TransferTitleEnum.TransferInitiated;
                break;
            case Completed:
                expectTitle = TransferTitleEnum.TransferCompleted;
                break;
            default:
                return null;
        }
        return expectTitle;
    }

}