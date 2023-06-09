package Automation.Payments.customer.web;


import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class ReviewFxTransferPage
{

    private final Config testConfig;
    @FindBy(xpath = "//div[contains(@class,'transaction-category-select__category-name')]")
    private WebElement customerReviewTransferCategoryText;
    @FindBy(xpath = "//div[@data-cy='transfer-step-review-reference']")
    private WebElement customerReviewTransferReferenceText;
    @FindBy(css = ".example-cta-box--fixed.example-cta > div > button")
    private WebElement customerLocalTransferDetailsConfirmButton;
    @FindBy(css = "div.verify-otp-form__input")
    private WebElement otpForm;
    @FindBy(xpath = "//div[@data-cy='transfer-step-make-header-title']")
    private WebElement usdReviewTransferPageTitle;
    @FindBy(xpath = "//div[@data-cy='currency-account-select-debit-account-currency-code']")
    private WebElement currencyAccountSelectBox;
    @FindBy(xpath = "//div[@data-cy='source-amount-input-currency-info']")
    private WebElement sourceAmountBox;
    @FindBy(xpath = "//div[@data-cy='target-amount-input-currency-info']")
    private WebElement targetAmountBox;
    @FindBy(xpath = "//div[@data-cy='source-amount-input-input-box']")
    private WebElement usdSourceAmountBox;
    @FindBy(xpath = "//div[@data-cy='fx-quote-box-estimated-delivery-time']")
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
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    public ReviewFxTransferPage(Config config, CurrencyEnum currencyEnum)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerLocalTransferDetailsConfirmButton);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar");
        switch (currencyEnum)
        {
            case SGD -> verifySgdReviewFxTransferPage();
            case USD -> verifyUsdReviewFxTransferPage();
            default -> testConfig.logFail("Please select another currency");
        }
    }

    private void verifySgdReviewFxTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Account Number", transferHeaderAccountNumber);
        AssertHelper.assertElementIsDisplayed(testConfig, "", currencyAccountSelectBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Source Amount Box", sourceAmountBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Target Amount Box", targetAmountBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Arrive Time Box", arriveTimeBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Confirm Button", customerLocalTransferDetailsConfirmButton);
    }

    private void verifyUsdReviewFxTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Account Number", transferHeaderAccountNumber);
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer Currency Account Select", currencyAccountSelectBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Review Transfer USD Source Amount Box", usdSourceAmountBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "USD Review Transfer Arrive Time Box", usdArriveTimeBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "USD Review Transfer Make Form", usdTransferMakeForm);
        AssertHelper.assertElementIsDisplayed(testConfig, "Submit Button", customerLocalTransferDetailsConfirmButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Recipient Page Title", usdReviewTransferPageTitle);
    }

    public void validateFxTransferInfo(Map<String, String> testData, CurrencyEnum sourceAmount, CurrencyEnum targetAmount)
    {
        AssertHelper.assertElementText(testConfig, "Source amount", sourceAmount.name(), sourceAmountBox);
        AssertHelper.assertElementText(testConfig, "Target amount", targetAmount.name(), targetAmountBox);
        AssertHelper.assertElementText(testConfig, "Category", testData.get("transferCategory"), customerReviewTransferCategoryText);
        AssertHelper.assertElementText(testConfig, "Reference", testData.get("reference").trim(), customerReviewTransferReferenceText);
    }

    public FxTransferSuccessOverviewPage confirmTransfer(String otpCode, TransferType transferType)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, spinner, "spinner");
        WaitHelper.waitForElementToBeClickable(testConfig, customerLocalTransferDetailsConfirmButton, "Confirm button");
        Element.click(testConfig, customerLocalTransferDetailsConfirmButton, "Confirm button");
        WaitHelper.waitForOptionalElement(testConfig, otpForm, "OTP Form");
        if (Element.isElementDisplayed(testConfig, otpForm))
        {
            Element.enterDataWithoutSelectedElement(testConfig, otpCode, "OTP Code");
        }
        return new FxTransferSuccessOverviewPage(testConfig, transferType);
    }
}