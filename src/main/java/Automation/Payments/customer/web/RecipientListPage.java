package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.customer.helpers.PaymentStaticDataEn.SuccessMessageEnums;
import Automation.Utils.*;
import Automation.Utils.Element.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Objects;

public class RecipientListPage
{

    private static final String recipientList = "//*[contains(@class,'counterparties-list-item__') and contains(.,'%s')]";
    private final Config testConfig;
    @FindBy(xpath = "//a[@data-cy='counterparty-step-select-do-bulk-transfers']")
    private WebElement bulkTransferButton;
    @FindBy(xpath = "//input[@class='q-field__native q-placeholder']")
    private WebElement recipientListSearchTextBox;
    @FindBy(css = "div.counterparty-step-select__add")
    private WebElement addNewRecipientButton;
    @FindBy(xpath = "//span[@data-cy='counterparty-step-select-bulk-transfers-help-text']")
    private WebElement bulkTransferHelpLabel;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;
    @FindBy(css = "div.example-page__header > div > div")
    private WebElement recipientPageTitle;
    @FindBy(css = "[data-cy='counterparties-list-item'] [class*='counterparty-name']")
    private WebElement counterpartyNameOnItemText;
    @FindBy(css = "[data-cy='counterparties-list-item'] [class*='data--currency']")
    private WebElement currencyOnItemText;
    @FindBy(css = "[data-cy='counterparties-list-item'] [class*='data--number']")
    private WebElement accountNumberOnItemText;
    @FindBy(css = "[data-cy='counterparties-list-item'] [data-cy*='bank-name']")
    private WebElement bankNameOnItemText;
    @FindBy(css = "[data-cy='counterparties-list-item-dropdown']")
    private WebElement threeDotDeleteIcon;
    @FindBy(css = "[class*='justify-center text-negative']")
    private WebElement deleteButton;
    @FindBy(css = "button[class*='text-negative']")
    private WebElement deleteConfirmButton;
    @FindBy(xpath = "//div[@data-cy='counterparties-list-item-dropdown']")
    private WebElement threeDotsToDelete;
    @FindBy(xpath = "//div[@class='q-notification__message col']")
    private WebElement successMessage;
    @FindBy(xpath = "//div[@data-cy='counterparties-list-item']")
    private WebElement recipientListItem;

    //div[@data-cy='counterparties-list-item']
    public RecipientListPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, recipientListSearchTextBox);
        verifyRecipientListPage();
    }

    private void verifyRecipientListPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "New recipient Button", addNewRecipientButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Search text box", recipientListSearchTextBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Bulk Transfer Help Label", bulkTransferHelpLabel);
        AssertHelper.assertElementText(testConfig, "Recipient Page Title", PaymentHelper.paymentStaticDataBase.getRecipientListPageTitle(), recipientPageTitle);
    }

    public TransferPage selectARecipient(String recipient, CurrencyEnum currencyEnum)
    {
        searchRecipientInListByString(recipient);
        testConfig.putRunTimeProperty("recipientCurrency", currencyEnum.name());
        return selectARecipientWithRandomIndexInList(recipient);
    }

    public TransferPage selectARecipient(CurrencyEnum currencyEnum)
    {
        searchRecipientInListByString(currencyEnum.name());
        testConfig.putRunTimeProperty("recipientCurrency", currencyEnum.name());
        return selectARecipientWithRandomIndexInList(currencyEnum.name());
    }

    private TransferPage selectARecipientWithRandomIndexInList(String recipient)
    {
        List<WebElement> listOfItems = Element.getPageElements(testConfig, How.xPath, String.format(recipientList, recipient));
        if (!Objects.requireNonNull(listOfItems).isEmpty())
        {
            int randomIndex = DataGenerator.generateRandomNumberInIntRange(0, Objects.requireNonNull(listOfItems).size() - 1);
            WaitHelper.waitForElementToBeDisplayed(testConfig, listOfItems.get(randomIndex), "Recipient item " + randomIndex);
            WaitHelper.waitForElementToBeClickable(testConfig, listOfItems.get(randomIndex), "Recipient item " + randomIndex);
            Element.click(testConfig, listOfItems.get(randomIndex), String.format("Click random item [%s]", randomIndex), true);
        } else
        {
            testConfig.logFail("Cannot get recipients list. ");
        }
        return new TransferPage(testConfig);
    }

    private void searchRecipientInListByString(String recipient)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, recipientListSearchTextBox, "Search for recipient");
        Element.enterData(testConfig, recipientListSearchTextBox, recipient, "Search for recipient");
        WaitHelper.waitForOptionalElement(testConfig, spinner, "Loading spinner after input");
        WaitHelper.waitForElementToBeHidden(testConfig, spinner, "Loading spinner after input");
        WaitHelper.waitForElementToBeClickable(testConfig, recipientListSearchTextBox, "Search");
    }

    public void validateBankInfoCorrectOnRecipientList()
    {
        AssertHelper.assertElementText(testConfig, "account holder name", testConfig.testData.get("recipientName"), counterpartyNameOnItemText);
        AssertHelper.assertElementText(testConfig, "currency", testConfig.testData.get("recipientCurrencyCode"), currencyOnItemText);
        AssertHelper.assertElementText(testConfig, "bank number", testConfig.testData.get("recipientAccountNumber"), accountNumberOnItemText);
        if (testConfig.testData.get("recipientCurrencyCode").contains("IDR"))
        {
            AssertHelper.assertElementText(testConfig, "bank name", String.format("(%s)", testConfig.testData.get("recipientBankName")), bankNameOnItemText);
        }
    }

    public NewRecipientPage clickAddNewRecipient()
    {
        Element.click(testConfig, addNewRecipientButton, "Add New Recipient");
        return new NewRecipientPage(testConfig);
    }

    public void searchRecipientUsingName(String recipientName)
    {
        Element.enterData(testConfig, recipientListSearchTextBox, recipientName, "Search for recipient using name");
        WaitHelper.waitForOptionalElement(testConfig, spinner, "Loading spinner after input");
        WaitHelper.waitForElementToBeHidden(testConfig, spinner, "Loading spinner after input");
    }

    public void deleteRecipient()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, threeDotsToDelete, "Three Dots");
        Element.click(testConfig, threeDotsToDelete, "three dots to delete");

        WaitHelper.waitForElementToBeClickable(testConfig, deleteButton, "delete button");
        Element.click(testConfig, deleteButton, "delete button");

        WaitHelper.waitForElementToBeClickable(testConfig, deleteConfirmButton, "Delete Confirm Button");
        Element.click(testConfig, deleteConfirmButton, "Delete confirm button");
    }

    public void validateRecipientIsDeleted(String recipientName)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, successMessage, "Delete success message");
        AssertHelper.assertElementText(testConfig, "Delete success message", SuccessMessageEnums.RecipientDeleted.getName(), successMessage);
        WaitHelper.waitForElementToBeHidden(testConfig, successMessage, "Delete Success message");

        searchRecipientUsingName(recipientName);
        AssertHelper.assertElementIsNotDisplayed(testConfig, "Recipient List Item", recipientListItem);
    }
}