package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewRecipientPage
{

    private final Config testConfig;
    @FindBy(xpath = "//img[@data-cy='counterparty-currency-dropdown']")
    private WebElement counterpartyCurrencyDropdown;
    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']")
    private WebElement newRecipientPageTitle;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;

    //LocalSgdBank
    @FindBy(xpath = "//div[@class='q-item__label']")
    private WebElement sgdLocalBankName;

    //IDR
    @FindBy(css = "[data-cy='bank_code-input']")
    private WebElement bankCodeTextBox;
    @FindBy(css = "[data-cy='bank_account_number-input']")
    private WebElement bankAccountNumberTextBox;
    @FindBy(css = "[data-cy='account_holder_name-input']")
    private WebElement bankAccountHolderNameTextBox;
    @FindBy(xpath = "//*[@class='q-item__label']")
    private WebElement bankNameSuggestDropdown;
    @FindBy(xpath = "//button[@data-cy='dynamic-requirements-form-cta']//span[text()='Verify']")
    private WebElement verifyButton;
    @FindBy(xpath = "//button[@data-cy='dynamic-requirements-form-cta']//span[text()='Continue']")
    private WebElement continueButton;
    @FindBy(css = "[data-cy='bank_code']>[class$='mt-md']")
    private WebElement bankNameValueText;
    @FindBy(css = "[data-cy='bank_account_number']>[class^='input-value']")
    private WebElement accountNumberValueText;

    public NewRecipientPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, bankCodeTextBox);
        WaitHelper.waitForElementToBeClickable(testConfig, bankCodeTextBox, "Bank Code");
        verifyRecipientListPage();
    }

    private void verifyRecipientListPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "CounterParty Currency Dropdown", counterpartyCurrencyDropdown);
        AssertHelper.assertElementText(testConfig, "Recipient Page Title", PaymentHelper.paymentStaticDataBase.getNewRecipientPageTitle(), newRecipientPageTitle);
    }

    public TransferPage inputSgdLocalBankInfo()
    {
        Element.enterData(testConfig, bankCodeTextBox, testConfig.testData.get("recipientBankName"), "Bank Name");
        WaitHelper.waitForElementToBeDisplayed(testConfig, sgdLocalBankName, "Bank Name");
        Element.click(testConfig, sgdLocalBankName, "Bank Name");

        WaitHelper.waitForElementToBeClickable(testConfig, bankAccountNumberTextBox, "Account Number Text Box");
        Element.enterData(testConfig, bankAccountNumberTextBox, testConfig.testData.get("recipientAccountNumber"), "Recipient Bank Account number");

        WaitHelper.waitForElementToBeClickable(testConfig, bankAccountHolderNameTextBox, "Recipient Name");
        Element.enterData(testConfig, bankAccountHolderNameTextBox, testConfig.testData.get("recipientName"), "Recipient Name");

        clickContinueButton();
        testConfig.putRunTimeProperty("recipientCurrency", CurrencyEnum.SGD.name());
        return new TransferPage(testConfig);
    }

    private void clickContinueButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, continueButton, "Continue Button");
        Element.click(testConfig, continueButton, "Continue button");
    }

    public void selectBank()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, bankCodeTextBox, "bank field");
        Element.enterData(testConfig, bankCodeTextBox, testConfig.testData.get("recipientBankName"), "bank name");
        WaitHelper.waitForElementToBeDisplayed(testConfig, bankNameSuggestDropdown, "bank dropdown");
        Element.click(testConfig, bankNameSuggestDropdown, "selected bank on suggestion list");
        WaitHelper.waitForElementToBeHidden(testConfig, bankNameSuggestDropdown, "selected bank on suggestion list");
    }

    public void inputBankAccountNumber()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, bankAccountNumberTextBox, "bank account number field");
        Element.enterData(testConfig, bankAccountNumberTextBox, testConfig.testData.get("recipientAccountNumber"), "bank account number");
    }

    public void inputAccountHolderName()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, bankAccountHolderNameTextBox, "account holder name");
        Element.click(testConfig, bankAccountHolderNameTextBox, "bank account number");
        Element.enterData(testConfig, bankAccountHolderNameTextBox, testConfig.testData.get("recipientName"), "bank account number");
    }

    public TransferPage clickOnContinueButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, continueButton, "Continue Button");
        Element.click(testConfig, continueButton, "Continue button");
        return new TransferPage(testConfig);
    }

    public void validateBankDisplayCorrect()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, bankNameValueText, "bank name");
        AssertHelper.assertElementIsDisplayed(testConfig, "bank name", bankNameValueText);
        AssertHelper.assertElementText(testConfig, "bank name", testConfig.testData.get("recipientBankName"), bankNameValueText);
        AssertHelper.assertElementText(testConfig, "bank account number", testConfig.testData.get("recipientAccountNumber"), accountNumberValueText);
    }

    public void validateAccountHolderNameDisplayWithDefaultValue()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "account holder name", bankAccountHolderNameTextBox);
        String actual = Element.getAttributeText(testConfig, bankAccountHolderNameTextBox, "data-value", "Get value of account holder name");
        AssertHelper.compareEquals(testConfig, "account holder name", testConfig.testData.get("defaultAccountHolderName"), actual);
    }
}