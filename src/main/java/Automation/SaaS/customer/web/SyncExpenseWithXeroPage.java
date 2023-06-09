package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SyncExpenseWithXeroPage extends BasePage
{
    private final Config testConfig;
    @FindBy(css = "div[data-cy='accounting-sync-page-header-page-title']")
    private WebElement pageTitle;
    @FindBy(css = "button[data-cy='accounting-sync-page-header-map-accounts-cta']")
    private WebElement mapXeroAccounts;
    @FindBy(css = "button[data-cy='accounting-header-menu-options'] span[class='q-btn__content text-center col items-center q-anchor--skip justify-center row']")
    private WebElement topRightSettingButton;
    @FindBy(css = "button[id='accounting-sync-procedure-tabs-incomplete']")
    private WebElement pendingReviewTab;
    @FindBy(css = "button[id='accounting-sync-procedure-tabs-pending']")
    private WebElement pendingSyncTab;
    @FindBy(css = "button[id='accounting-sync-procedure-tabs-completed']")
    private WebElement syncedTab;
    @FindBy(css = "input[data-cy='example-filter-search-input']")
    private WebElement searchTextBox;
    @FindBy(css = "div[data-cy='accounting-sync-table-header-select-all']")
    private WebElement checkboxAllColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-merchant']")
    private WebElement merchantColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-person']")
    private WebElement userColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-description']")
    private WebElement descriptionColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-files']")
    private WebElement attachmentsColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-amount']")
    private WebElement amountColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-category']")
    private WebElement categoryColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-mapping_account']")
    private WebElement xeroAccountColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-tax']")
    private WebElement taxRateColumnHeader;
    @FindBy(css = "button[data-cy='accounting-expenses-sync-more-actions-cta']")
    private WebElement moreActionsButton;
    @FindBy(css = "button[data-cy='accounting-expenses-sync-list-cta']")
    private WebElement moveToPendingSyncButton;
    @FindBy(css = "div[data-cy='accounting-expenses-sync-more-actions-cta-discard-transaction']")
    private WebElement discardSelectedActionItem;
    @FindBy(css = "div[data-cy='accounting-expenses-sync-more-actions-cta-request-for-receipts']")
    private WebElement requestReceiptsForSelectedActionItem;
    @FindBy(css = "button[data-cy='filter-button-debit_account']")
    private WebElement accountFilterButton;
    @FindBy(css = "button[data-cy='filter-button-transaction_date']")
    private WebElement dateRangeFilterButton;
    @FindBy(css = "button[data-cy='filter-button-accounting_account_ids']")
    private WebElement xeroAccountFilterButton;
    @FindBy(css = "button[data-cy='filter-button-more_filters']")
    private WebElement moreFiltersButton;
    @FindBy(css = "button[data-cy='accounting-expenses-sync-pending-sync-cta']")
    private WebElement syncSelectedExpensesButton;
    @FindBy(css = "button[data-cy='filter-option-fields-wrapper-button']")
    private WebElement applyFilterButton;
    @FindBy(css = "div[data-cy='accounting-expenses-sync-table-columns-merchant']")
    private List<WebElement> merchantTableCells;
    @FindBy(xpath = "//div[contains(@class,'q-badge--single-line') and @aria-label='1']")
    private WebElement pendingReviewOneBadge;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(1)")
    private WebElement checkBoxCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(2)")
    private WebElement merchantCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(3)")
    private WebElement userCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(4)")
    private WebElement descriptionCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(5)")
    private WebElement attachmentCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(6)")
    private WebElement amountCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(7)")
    private WebElement categoryCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(9)")
    private WebElement xeroAccountCell;
    @FindBy(css = "tr[data-cy='accounting-expenses-sync-table-item-row'] td:nth-child(10)")
    private WebElement taxRateCell;
    @FindBy(css = "button.small-confirm-cancel-modal__button.text-positive")
    private WebElement confirmButton;
    @FindBy(css = "button.small-confirm-cancel-modal__button.text-negative")
    private WebElement noButton;

    public SyncExpenseWithXeroPage(Config testConfig)
    {
        super(testConfig);
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, pendingReviewTab);
    }

    public SyncExpenseWithXeroPage verifySyncExpensesWithXeroPage()
    {
        AssertHelper.assertElementText(testConfig, "Page title", SaasHelper.saasStaticDataBase.getExpenseWithXeroPageTitle(), pageTitle);
        AssertHelper.compareContains(testConfig, "Pending Review tab", SaasHelper.saasStaticDataBase.getPendingReviewTabTitle(), Element.getText(testConfig, pendingReviewTab, "Pending Review tab title"));
        AssertHelper.assertElementText(testConfig, "Pending Sync tab", SaasHelper.saasStaticDataBase.getPendingSyncTabTitle(), pendingSyncTab);
        AssertHelper.assertElementText(testConfig, "Synced tab", SaasHelper.saasStaticDataBase.getSyncedTabTitle(), syncedTab);
        AssertHelper.assertElementText(testConfig, "More Actions button", SaasHelper.saasStaticDataBase.getMoreActionsButtonTitle(), moreActionsButton);
        AssertHelper.assertElementText(testConfig, "Move to Pending Sync button", SaasHelper.saasStaticDataBase.getMoveToPendingSyncButtonTitle(), moveToPendingSyncButton);
        return this;
    }

    public void openTransactionWithFilter(SaasEnums.SourceOfFund sourceOfFund)
    {
        waitForLoaderDisappeared();
        if (sourceOfFund != null)
        {
            filterAccount(sourceOfFund);
            waitForLoaderDisappeared();
        }
        openTransaction();
    }

    public void filterAccount(SaasEnums.SourceOfFund sourceOfFund)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, accountFilterButton, "Account filter button");
        Element.click(testConfig, accountFilterButton, "Account filter button");
        WebElement sourceOfFundCheckBox = Element.getPageElementWithRetry(testConfig, Element.How.xPath, String.format("//span[contains(.,'%s')]/parent::div//div[contains(@class,'q-checkbox')][1]", sourceOfFund.getAccountName()));
        WaitHelper.waitForElementToBeClickable(testConfig, sourceOfFundCheckBox, "Account filter checkbox");
        Element.click(testConfig, sourceOfFundCheckBox, "Account filter checkbox");
        WaitHelper.waitForElementToBeClickable(testConfig, applyFilterButton, "Apply filter button");
        Element.click(testConfig, applyFilterButton, "Apply filter button");
        waitForLoaderDisappeared();
    }

    public ExpenseDetailsPage openTransaction()
    {
        String searchText = String.valueOf(Double.parseDouble(testConfig.testData.get("amount")) / 100);
        searchAndVerifyExpenseTableRow(searchText);
        waitForLoaderDisappeared();
        WaitHelper.waitForElementToBeDisplayed(testConfig, pendingReviewOneBadge, "Pending review badge");
        Element.click(testConfig, merchantTableCells.get(0), String.format("Row with amount: %s", searchText));
        return new ExpenseDetailsPage(testConfig);
    }

    private void verifyExpenseRowDetails()
    {
        String expenseAmount = String.format("%.2f", Double.parseDouble(testConfig.testData.get("amount")) / 100);
        String expenseAmountDesc = String.format("%.2f", Double.parseDouble(testConfig.testData.get("amount")) / 100);
        String merchantName = testConfig.testData.get("merchantName");
        if (testConfig.getRunTimeProperty("updatedMerchantName") != null)
        {
            merchantName = testConfig.getRunTimeProperty("updatedMerchantName");
        }
        AssertHelper.assertElementText(testConfig, "Merchant name", merchantName, merchantCell);
        if (testConfig.getRunTimeProperty("updatedDescription") != null)
        {
            AssertHelper.assertElementText(testConfig, "Description", testConfig.getRunTimeProperty("updatedDescription"), descriptionCell);
        } else
        {
            AssertHelper.assertElementText(testConfig, "Description", String.format(testConfig.testData.get("description"), expenseAmountDesc, merchantName), descriptionCell);
        }
        String amountCellText = Element.getText(testConfig, amountCell, "Amount cell").strip();
        AssertHelper.compareEquals(testConfig, "Amount", expenseAmount, amountCellText.split(" \n")[1]);
        AssertHelper.compareEquals(testConfig, "Currency", testConfig.testData.get("recipientCurrencyCode"), amountCellText.split(" \n")[0]);
        AssertHelper.assertElementText(testConfig, "Category", testConfig.getRunTimeProperty("categoryName"), categoryCell);
        if (testConfig.getRunTimeProperty("selectedXeroAccount") != null)
        {
            AssertHelper.assertElementText(testConfig, "Selected Xero account", testConfig.getRunTimeProperty("selectedXeroAccount"), xeroAccountCell);
        }
        AssertHelper.assertElementText(testConfig, "Attachment", testConfig.testData.get("attachmentCount"), attachmentCell);
        AssertHelper.assertElementText(testConfig, "Tax", testConfig.testData.get("taxRate"), taxRateCell);
    }

    private void searchAndVerifyExpenseTableRow(String searchText)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, searchTextBox, "Search text box");
        Element.enterData(testConfig, searchTextBox, searchText, "Search text box");
        waitForLoaderDisappeared();
        verifyExpenseRowDetails();
    }

    public void moveExpenseToPendingSyncAndVerify(String value, boolean confirmMove)
    {
        waitForLoaderDisappeared();
        Element.click(testConfig, Element.getPageElement(testConfig, Element.How.xPath, String.format("//td[contains(.,'%s')]/parent::tr/td[1]", value)), String.format("Checkbox for %s", value));
        AssertHelper.assertElementEnabled(testConfig, "More actions button", moreActionsButton);
        WaitHelper.waitForElementToBeClickable(testConfig, moveToPendingSyncButton, "Move to pending sync button");
        Element.click(testConfig, moveToPendingSyncButton, "Move to pending sync button");
        if (confirmMove)
        {
            WaitHelper.waitForElementToBeClickable(testConfig, confirmButton, "Confirm button");
            Element.click(testConfig, confirmButton, "Confirm button");
        } else
        {
            WaitHelper.waitForElementToBeClickable(testConfig, noButton, "No button");
            Element.click(testConfig, noButton, "No button");
        }
        waitForLoaderDisappeared();
        AssertHelper.assertElementEnabled(testConfig, "Pending sync tab", pendingSyncTab);
        AssertHelper.assertElementText(testConfig, "Sync selected expense button", "Sync Selected Expenses", syncSelectedExpensesButton);
        searchAndVerifyExpenseTableRow(value);
    }
}
