package Automation.SaaS.customer.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class PaymentLinksEditDetailsPage
{

    private final Config testConfig;
    @FindBy(css = "div.modal-page__title > span")
    private WebElement pageTitle;
    @FindBy(css = "div.remote-select.example-field.payment-link-form__customer-field--disabled > div")
    private WebElement customerNameLabel;
    @FindBy(css = "div.remote-select.example-field.payment-link-form__customer-field--disabled > label")
    private WebElement customerNameDropdown;
    @FindBy(css = "div > form > section > div.amount-input.example-field.q-my-md > div.example-label > div")
    private WebElement amountLabel;
    @FindBy(xpath = "//input[@data-cy='generic-amount-with-currency-dropdown-amount-input']")
    private WebElement amountTextBox;
    @FindBy(css = "button.example-button--cta.example-button--fluid.bg-white.q-mr-md")
    private WebElement cancelButton;
    @FindBy(xpath = "//button[@data-cy='payment-link-create-form-create-cta']")
    private WebElement saveButton;
    @FindBy(css = "div.date-dropdown-select-and-input.example-field.q-pr-sm > label")
    private WebElement dueDateDropdown;
    @FindBy(css = "div.date-dropdown-select-and-input.example-field.q-pl-sm > label")
    private WebElement expiryDateDropdown;
    @FindBy(xpath = "//div[contains(@id,'q-portal--menu--')]//div[@role='listitem'][2]")
    private WebElement datePlusSevenDaysItem;
    @FindBy(xpath = "//div[contains(@id,'q-portal--menu--')]//div[@role='listitem'][1]")
    private WebElement expiryDatePlusSevenDaysItem;
    @FindBy(xpath = "//div[@data-cy='receivables-payment-methods-checkbox-group-checkbox-for-Local Transfer']")
    private WebElement localTransferCheckbox;
    @FindBy(xpath = "//div[@data-cy='receivables-payment-methods-checkbox-group-checkbox-for-International Transfer']")
    private WebElement internationalTransferCheckbox;
    @FindBy(xpath = "//div[@data-cy='receivables-payment-methods-checkbox-group-checkbox-for-PayNow']")
    private WebElement payNowCheckbox;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "form > section > div:nth-child(4) > label > div > div > div > textarea")
    private WebElement descriptionTextBox;
    @FindBy(xpath = "//button[@data-cy='small-confirm-cancel-modal-button-confirm']")
    private WebElement closeWithoutSavingButton;
    @FindBy(xpath = "//button[@data-cy='small-confirm-cancel-modal-button-cancel']")
    private WebElement continueEditingButton;

    public PaymentLinksEditDetailsPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, pageTitle);
        verifyPaymentLinkEditPage();
    }

    public void verifyPaymentLinkEditPage()
    {
        WaitHelper.waitForOptionalElement(testConfig, loadingIcon, "Loading Icon");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
        AssertHelper.assertElementText(testConfig, "Payment Links Edit Detail Page Title", "Edit link details", pageTitle);
        AssertHelper.assertElementText(testConfig, "Customer Name Label", "Customer", customerNameLabel);
        AssertHelper.assertElementText(testConfig, "Amount Label", "Amount", amountLabel);
        AssertHelper.assertElementText(testConfig, "Save Button", "Save", saveButton);
        AssertHelper.assertElementText(testConfig, "Cancel Button", "Cancel", cancelButton);
        if (Objects.isNull(testConfig.getRunTimeProperty("paymentOptions")))
        {
            AssertHelper.compareEquals(testConfig, "Local Transfer Checked", "true", Element.getAttributeText(testConfig, localTransferCheckbox, "aria-checked", "Local Transfer Checkbox"));
            AssertHelper.compareEquals(testConfig, "International Transfer Checked", "true", Element.getAttributeText(testConfig, internationalTransferCheckbox, "aria-checked", "International Transfer Checkbox"));
            AssertHelper.compareEquals(testConfig, "PayNow Transfer Checked", "false", Element.getAttributeText(testConfig, payNowCheckbox, "aria-checked", "PayNow Transfer Checkbox"));
        }
    }

    public PaymentLinksPage editAndDiscardPaymentLinkChanges()
    {
        Element.enterData(testConfig, amountTextBox, "100", "Amount Text Box");
        Element.enterData(testConfig, descriptionTextBox, "Test Description", "Description Text Box");
        Element.click(testConfig, cancelButton, "Cancel Button");
        WaitHelper.waitForElementToBeClickable(testConfig, closeWithoutSavingButton, "Close Without Saving Button");
        Element.click(testConfig, closeWithoutSavingButton, "Close Without Saving Button");
        return new PaymentLinksPage(testConfig);
    }
}
