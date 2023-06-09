package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ExpenseDetailsPage extends BasePage
{
    private final Config testConfig;
    @FindBy(xpath = "//div[contains(@data-cy,'accounting-search-dropdown-list-item')][1]/span")
    private WebElement xeroAccount;
    @FindBy(css = "div[data-cy='accounting-expenses-header-title']")
    private WebElement pageTitle;
    @FindBy(css = "button[data-cy='accounting-expenses-navigation-previous-btn'] span[class='text-body1 text-weight-bold']")
    private WebElement previousButton;
    @FindBy(css = "button[data-cy='accounting-expenses-navigation-next-btn'] span[class='text-body1 text-weight-bold']")
    private WebElement nextButton;
    @FindBy(css = "div[data-cy='accounting-expenses-merchant-and-amount-amount']")
    private WebElement amountLabel;
    @FindBy(css = "div[class='rounded-md'] div[data-cy='accounting-details-card-body-wrapper'] div div[data-cy='accounting-search-dropdown-edit-button']")
    private WebElement editMerchantNameButton;
    @FindBy(css = "div[data-cy='accounting-expenses-merchant-and-amount-name']")
    private WebElement merchantNameLabel;
    @FindBy(css = "span[data-cy='accounting-expenses-merchant-and-amount-type']")
    private WebElement bankTransferLabel;
    @FindBy(css = "span[data-cy='accounting-expenses-merchant-and-amount-user']")
    private WebElement userLabel;
    @FindBy(css = "div[data-cy='accounting-transaction-description-header']")
    private WebElement descriptionEditHeader;
    @FindBy(css = "button[data-cy='accounting-edit-cta-edit-button']")
    private WebElement descriptionEditButton;
    @FindBy(css = "p[data-cy='accounting-transaction-description-paragraph']")
    private WebElement bankTransferFor;
    @FindBy(css = ".accounting-details-card__title.text-weight-bold.text-white")
    private WebElement xeroAccountMappingTitle;
    @FindBy(css = "div[class='accounting-search-dropdown q-pa-none text-right justify-end text-right justify-end'] div[data-cy='accounting-search-dropdown-edit-button']")
    private WebElement searchDropdownEditButton;
    @FindBy(css = "div[data-cy='accounting-expenses-attachment-header']")
    private WebElement attachmentsUploadAcceptedForm;
    @FindBy(css = "input[type='file']")
    private WebElement fileUpload;
    @FindBy(css = "div[data-cy='transaction-details-receipt-notification-reminder']")
    private WebElement attachReceiptToTheAboveTr;
    @FindBy(css = "button[data-cy='accounting-expenses-discard-transaction-button']")
    private WebElement discardTransactionFromExpense;
    @FindBy(css = "button[data-cy='accounting-expenses-sync-transaction-details-wrapper-sync-button']")
    private WebElement moveToPendingSync;
    @FindBy(css = ".transaction-details-date-footer__transaction-code.opacity-50.inline-block.q-mt-md.q-px-md.q-py-xs.rounded-2xl")
    private WebElement idLabel;
    @FindBy(css = ".opacity-50.q-mt-md.q-pt-xs")
    private WebElement dateTimeLabel;
    @FindBy(css = "div[data-cy='desktop-modal-header-close-button']")
    private WebElement closeButton;
    @FindBy(css = "input[data-cy='accounting-expenses-merchant-and-amount-search-input']")
    private WebElement selectMerchantSearchTextBox;
    @FindBy(css = "div.q-list.accounting-search-dropdown-list > div.q-item.q-item-type.row.no-wrap.q-item--clickable > div.col-shrink > button")
    private WebElement selectMerchantSearchButton;
    @FindBy(css = "textarea[data-cy='accounting-description-editor-input']")
    private WebElement descriptionEditTextbox;
    @FindBy(css = "button[data-cy='accounting-edit-cta-save-button']")
    private WebElement descriptionEditSaveButton;
    @FindBy(css = "div[data-cy='accounting-search-dropdown-title']")
    private WebElement searchAccountDropdownTitle;
    @FindBy(css = "button.small-confirm-cancel-modal__button.text-positive")
    private WebElement confirmButton;
    @FindBy(css = "button.small-confirm-cancel-modal__button.text-negative")
    private WebElement noButton;

    public ExpenseDetailsPage(Config testConfig)
    {
        super(testConfig);
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, merchantNameLabel);
        verifyExpenseDetailsPage();
    }

    public void verifyExpenseDetailsPage()
    {
        waitForLoaderDisappeared();
        AssertHelper.assertElementText(testConfig, "Page title", SaasHelper.saasStaticDataBase.getExpensePageTitle(), pageTitle);
        AssertHelper.assertElementText(testConfig, "Amount", String.format("%s %.2f", testConfig.testData.get("recipientCurrencyCode"), Double.parseDouble(testConfig.testData.get("amount")) / 100), amountLabel);
        AssertHelper.assertElementText(testConfig, "Bank transfer", testConfig.testData.get("expenseTransferType"), bankTransferLabel);
        AssertHelper.assertElementText(testConfig, "User", testConfig.testData.get("fullName"), userLabel);
    }

    public SyncExpenseWithXeroPage updateAndCloseExpenseDetailsPage()
    {
        updateExpenseMerchantAndDescription();
        selectXeroAccount();
        Element.click(testConfig, closeButton, "Close button", true);
        return new SyncExpenseWithXeroPage(testConfig);
    }

    private void updateExpenseMerchantAndDescription()
    {
        Element.click(testConfig, editMerchantNameButton, "Edit merchant name");
        WaitHelper.waitForElementToBeDisplayed(testConfig, selectMerchantSearchTextBox, "Select merchant search text box");
        Element.enterData(testConfig, selectMerchantSearchTextBox, testConfig.getRunTimeProperty("updatedMerchantName"), "Select merchant search text box");
        WaitHelper.waitForElementToBeClickable(testConfig, selectMerchantSearchButton, "Select merchant search button");
        Element.click(testConfig, selectMerchantSearchButton, "Select merchant search button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, descriptionEditButton, "Edit description");
        Element.click(testConfig, descriptionEditButton, "Edit description");
        WaitHelper.waitForElementToBeClickable(testConfig, descriptionEditTextbox, "Description input");
        testConfig.putRunTimeProperty("updatedDescription", testConfig.getRunTimeProperty("updatedMerchantName") + "description");
        Element.clearDataWithBackSpace(testConfig, descriptionEditTextbox, "Description edit input");
        Element.enterData(testConfig, descriptionEditTextbox, testConfig.getRunTimeProperty("updatedDescription"), "Description edit input");
        Element.click(testConfig, descriptionEditSaveButton, "Description edit save button");
    }

    private void selectXeroAccount()
    {
        Element.click(testConfig, searchDropdownEditButton, "Search dropdown edit button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, xeroAccount, "Xero account item");
        testConfig.putRunTimeProperty("selectedXeroAccount", Element.getText(testConfig, xeroAccount, "Xero account"));
        Element.click(testConfig, xeroAccount, Element.getText(testConfig, xeroAccount, "Xero account"));
        WaitHelper.waitForOptionalElement(testConfig, noButton, "No save for future", 1);
        if (Element.isElementDisplayed(testConfig, noButton))
            Element.click(testConfig, noButton, "No save for future");
        WaitHelper.waitForElementToBeClickable(testConfig, moveToPendingSync, "Move to pending sync button");
        waitForLoaderDisappeared();
    }

}
