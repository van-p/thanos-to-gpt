package Automation.SaaS.customer.web;

import Automation.Utils.*;
import Automation.Utils.Element.How;
import Automation.Utils.Enums.DateRequired;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class PaymentLinksPage
{

    private final Config testConfig;
    private final String totalOutstandingDueXpath = "//div[@data-cy='invoice-mini-insights-desktop-item-title']";
    @FindBy(css = "div.q-table__bottom.row.items-center.q-table__bottom--nodata")
    private WebElement nodataFoundLabel;
    @FindBy(css = "main > div.example-card.example-card--small > section > div")
    private WebElement paymentLinksPageWelcomeCard;
    @FindBy(css = "div.date-dropdown-select-and-input.example-field.q-pr-sm > label")
    private WebElement dueDateDropdown;
    @FindBy(css = "div.date-dropdown-select-and-input.example-field.q-pl-sm > label")
    private WebElement expiryDateDropdown;
    @FindBy(xpath = "//div[contains(@id,'q-portal--menu--')]//div[@role='listitem'][2]")
    private WebElement datePlusSevenDaysItem;
    @FindBy(xpath = "//div[contains(@id,'q-portal--menu--')]//div[@role='listitem'][1]")
    private WebElement expiryDatePlusSevenDaysItem;
    @FindBy(xpath = "//button[@data-cy='invoice-generic-header-cta']")
    private WebElement newLinkButton;
    @FindBy(xpath = "//button[@data-cy='invoices-generic-header-dropdown']")
    private WebElement menuDropdownButton;
    @FindBy(xpath = "//div[@data-cy='invoices-generic-header-title']")
    private WebElement paymentLinksPageTitle;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//input[@data-cy='example-filter-search-input']")
    private WebElement searchTextbox;
    @FindBy(xpath = "//div[@data-cy='payment-links-list-table']")
    private WebElement paymentLinkListTable;
    @FindBy(xpath = "//button[@data-cy='filter-button-sort']")
    private WebElement tableSortFilterButton;
    @FindBy(xpath = "//button[@data-cy='filter-button-state_codes']")
    private WebElement tableStatusFilterButton;
    @FindBy(xpath = "//button[@data-cy='filter-button-due_date']")
    private WebElement tableDueFilterButton;
    @FindBy(xpath = "//button[@data-cy='filter-button-match_status']")
    private WebElement tablePaymentFilterButton;
    @FindBy(xpath = "//button[@data-cy='filter-button-more_filters']")
    private WebElement tableMoreFilterButton;
    @FindBy(css = "div.col-shrink.text-h4.text-weight-bolder > span.q-icon.q-mr-md")
    private WebElement pageIcon;
    @FindBy(xpath = "//input[@data-cy='payment-link-create-form-customer-input']")
    private WebElement customerNameTextbox;
    @FindBy(xpath = "//div[@data-cy='counterparties-list-item']")
    private WebElement linkCustomerItem;
    @FindBy(xpath = "//input[@data-cy='generic-amount-with-currency-dropdown-amount-input']")
    private WebElement linkAmountTextBox;
    @FindBy(xpath = "//label//textarea")
    private WebElement linkDescriptionTextBox;
    @FindBy(xpath = "//div[@data-cy='receivables-payment-methods-checkbox-group-checkbox-for-Local Transfer']")
    private WebElement localTransferCheckbox;
    @FindBy(xpath = "//div[@data-cy='receivables-payment-methods-checkbox-group-checkbox-for-International Transfer']")
    private WebElement internationalTransferCheckbox;
    @FindBy(xpath = "//div[@data-cy='receivables-payment-methods-checkbox-group-checkbox-for-PayNow']")
    private WebElement payNowCheckbox;
    @FindBy(xpath = "//button[@data-cy='payment-link-create-form-create-cta']")
    private WebElement createLinkButton;
    @FindBy(css = "button.example-button--cta.example-button--fluid")
    private WebElement cancelButton;
    @FindBy(xpath = "//span[@data-cy='payment-link-created-screen-number']")
    private WebElement linkNumber;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-short-link']")
    private WebElement linkShortLink;
    @FindBy(xpath = "//button[@data-cy='payment-link-created-screen-done-button']")
    private WebElement doneButton;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-copy-button']")
    private WebElement copyButton;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-send-email-button']")
    private WebElement sendEmailButton;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-title']")
    private WebElement linkCreatedScreenTitle;
    @FindBy(css = "tr > td:nth-child(1) > span")
    private WebElement linkNumberInTable;
    @FindBy(css = "tr > td:nth-child(2) > span > div > div > span > span:nth-child(2)")
    private WebElement linkStatusInTable;
    @FindBy(css = "tr > td.q-td.text-left.text-weight-bold > span")
    private WebElement linkCustomerInTable;
    @FindBy(css = "tr > td:nth-child(4) > span")
    private WebElement linkDueDateInTable;
    @FindBy(css = "tr > td:nth-child(5) > span")
    private WebElement linkExpiryDateInTable;
    @FindBy(css = "tr > td:nth-child(6) > span")
    private WebElement linkAmountInTable;
    @FindBy(css = "tr > td:nth-child(6) > span > div > div.text-subtitle2.text-weight-regular")
    private WebElement linkCurrencyInTable;
    @FindBy(css = "tr > td:nth-child(7) > span > button")
    private WebElement linkPaymentActionsInTable;
    @FindBy(css = "thead > tr > th:nth-child(1)")
    private WebElement linkNumberInTableHeader;
    @FindBy(css = "thead > tr > th:nth-child(2)")
    private WebElement linkStatusInTableHeader;
    @FindBy(css = "thead > tr > th:nth-child(3)")
    private WebElement linkCustomerInTableHeader;
    @FindBy(css = "thead > tr > th:nth-child(4)")
    private WebElement linkDueDateInTableHeader;
    @FindBy(css = "thead > tr > th:nth-child(5)")
    private WebElement linkExpiryDateInTableHeader;
    @FindBy(css = "thead > tr > th:nth-child(6)")
    private WebElement linkAmountInTableHeader;
    @FindBy(css = "thead > tr > th:nth-child(7)")
    private WebElement linkPaymentActionsInTableHeader;
    @FindBy(css = "div:nth-child(1) > div.q-item__section.column.q-item__section--main")
    private WebElement editPaymentLink;

    public PaymentLinksPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, newLinkButton);
        verifyPaymentLinkPage();
    }

    private void verifyPaymentLinkPage()
    {
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loading bar");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar");
        AssertHelper.assertElementText(testConfig, "new Link Button", "New link", newLinkButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Menu Dropdown Button", menuDropdownButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Search text box", searchTextbox);
        AssertHelper.assertElementText(testConfig, "Payment Links Page Title", "Payment Links", paymentLinksPageTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Page icon", pageIcon);
        AssertHelper.assertElementIsDisplayed(testConfig, "Payment links table", paymentLinkListTable);
        AssertHelper.assertElementIsDisplayed(testConfig, "Sort filter", tableSortFilterButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Status filter", tableStatusFilterButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Due filter", tableDueFilterButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Payment filter", tablePaymentFilterButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "More filter", tableMoreFilterButton);
        verifyPaymentLinkTotal();
        verifyPaymentLinkTable();
    }

    private void verifyPaymentLinkTotal()
    {
        List<WebElement> totalElements = Element.getPageElements(testConfig, How.xPath, totalOutstandingDueXpath);
        AssertHelper.assertElementText(testConfig, "Total Outstanding", "TOTAL OUTSTANDING", totalElements.get(0));
        AssertHelper.assertElementText(testConfig, "OverDue", "OVERDUE", totalElements.get(1));
        AssertHelper.assertElementText(testConfig, "Due", "DUE", totalElements.get(2));
    }

    private void verifyPaymentLinkTable()
    {
        AssertHelper.assertElementText(testConfig, "Payment links table header link number", "LINK #", linkNumberInTableHeader);
        AssertHelper.assertElementText(testConfig, "Payment links table header link status", "STATUS", linkStatusInTableHeader);
        AssertHelper.assertElementText(testConfig, "Payment links table header link customer", "CUSTOMER", linkCustomerInTableHeader);
        AssertHelper.assertElementText(testConfig, "Payment links table header link due date", "DUE DATE", linkDueDateInTableHeader);
        AssertHelper.assertElementText(testConfig, "Payment links table header link expiry date", "EXPIRY DATE", linkExpiryDateInTableHeader);
        AssertHelper.assertElementText(testConfig, "Payment links table header link amount", "AMOUNT", linkAmountInTableHeader);
        AssertHelper.assertElementText(testConfig, "Payment links table header link payment actions", "ACTION", linkPaymentActionsInTableHeader);
    }

    public PaymentLinksCreatedPage createNewLink()
    {
        Element.click(testConfig, newLinkButton, "New Link Button");
        WaitHelper.waitForOptionalElement(testConfig, loadingIcon, "Loading Icon");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeClickable(testConfig, customerNameTextbox, "Customer Name Textbox");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
        Element.click(testConfig, customerNameTextbox, "Customer Name");
        WaitHelper.waitForElementToBeDisplayed(testConfig, linkCustomerItem, "Link Customer Item");
        Element.click(testConfig, linkCustomerItem, "Link Customer Item");
        WaitHelper.waitForElementToBeDisplayed(testConfig, linkAmountTextBox, "Link Amount Textbox");
        Element.enterData(testConfig, linkAmountTextBox, testConfig.testData.get("amount"), "Link Amount Textbox");
        if (testConfig.testData.get("description") != null)
        {
            Element.enterData(testConfig, linkDescriptionTextBox, testConfig.testData.get("description"), "Link Description Textbox");
        }
        if (testConfig.testData.get("dueDate") != null)
        {
            Element.click(testConfig, dueDateDropdown, "Due Date Dropdown");
            Element.click(testConfig, datePlusSevenDaysItem, "Due Date Plus Seven Days Item");
            WaitHelper.waitForElementToBeHidden(testConfig, datePlusSevenDaysItem, "Due Date Plus Seven Days Item");
        }
        if (testConfig.testData.get("linkExpiryDate") != null)
        {
            Element.click(testConfig, expiryDateDropdown, "Expiry Date Dropdown");
            Element.click(testConfig, expiryDatePlusSevenDaysItem, "Expiry Date Plus Seven Days Item");
            WaitHelper.waitForElementToBeHidden(testConfig, expiryDatePlusSevenDaysItem, "Expiry Date Plus Seven Days Item");
        }
        AssertHelper.compareEquals(testConfig, "Local Transfer Checked", "true", Element.getAttributeText(testConfig, localTransferCheckbox, "aria-checked", "Local Transfer Checkbox"));
        AssertHelper.compareEquals(testConfig, "International Transfer Checked", "true", Element.getAttributeText(testConfig, internationalTransferCheckbox, "aria-checked", "International Transfer Checkbox"));
        AssertHelper.compareEquals(testConfig, "PayNow Transfer Checked", "false", Element.getAttributeText(testConfig, payNowCheckbox, "aria-checked", "PayNow Transfer Checkbox"));
        Element.click(testConfig, createLinkButton, "Create Link Button");
        return new PaymentLinksCreatedPage(testConfig);
    }

    public void verifyPaymentLink(String linkNumber)
    {
        Element.enterData(testConfig, searchTextbox, linkNumber, "Search Textbox");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        AssertHelper.compareEquals(testConfig, "Table Link Number", linkNumber, Element.getText(testConfig, linkNumberInTable, "Link Number In Table"));
        AssertHelper.compareEquals(testConfig, "Table Link Status", testConfig.testData.get("status"), Element.getText(testConfig, linkStatusInTable, "Link Status In Table"));
        AssertHelper.compareEquals(testConfig, "Table Link Customer", testConfig.testData.get("customer"), Element.getText(testConfig, linkCustomerInTable, "Link Customer In Table"));
        if (testConfig.testData.get("dueDate") != null)
        {
            AssertHelper.compareEquals(testConfig, "Table Link Due Date", testConfig.testData.get("dueDate"), Element.getText(testConfig, linkDueDateInTable, "Link Due Date In Table"));
        } else
        {
            AssertHelper.compareEquals(testConfig, "Table Link Due Date", "-", Element.getText(testConfig, linkDueDateInTable, "Link Due Date In Table"));
        }
        if (testConfig.testData.get("linkExpiryDate") != null)
        {
            AssertHelper.compareEquals(testConfig, "Table Link Expiry Date", testConfig.testData.get("linkExpiryDate"), Element.getText(testConfig, linkExpiryDateInTable, "Link Expiry Date In Table"));
        } else
        {
            String defaultExpiryDate = DataGenerator.getDate("dd MMM yyyy", DateRequired.FutureDate, 30);
            AssertHelper.compareEquals(testConfig, "Table Link Expiry Date", defaultExpiryDate, Element.getText(testConfig, linkExpiryDateInTable, "Link Expiry Date In Table"));
        }
        AssertHelper.compareEquals(testConfig, "Table Link Amount", testConfig.testData.get("amount"), Element.getText(testConfig, linkAmountInTable, "Link Amount In Table").split("\n")[1].split("\\.")[0]);
        AssertHelper.compareEquals(testConfig, "Table Link Currency", testConfig.testData.get("currency"), Element.getText(testConfig, linkCurrencyInTable, "Link Currency In Table"));
    }

    public PaymentLinksEditDetailsPage selectEditPaymentLinkDetailOption(String paymentLinkNumber)
    {
        WebElement paymentLinkAction = Element.getPageElement(testConfig, How.xPath, "//td[contains(.,'" + paymentLinkNumber + "')]/following-sibling::td[6]/span");
        Element.click(testConfig, paymentLinkAction, "Payment Link Action");
        WaitHelper.waitForElementToBeDisplayed(testConfig, editPaymentLink, "Edit link details");
        Element.click(testConfig, editPaymentLink, "Edit link details");
        return new PaymentLinksEditDetailsPage(testConfig);
    }

    public PaymentLinksDetailsPage openPaymentLinkDetail(String paymentLinkNumber)
    {
        Element.enterData(testConfig, searchTextbox, paymentLinkNumber, "Search Textbox");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        Element.click(testConfig, linkNumberInTable, "Link Number In Table");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        return new PaymentLinksDetailsPage(testConfig);
    }

    public void verifyPaymentLinkDeleted(String paymentLinkNumber)
    {
        Element.enterData(testConfig, searchTextbox, paymentLinkNumber, "Search Textbox");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        AssertHelper.assertElementIsNotDisplayed(testConfig, "Payment link number", linkNumberInTable);
        AssertHelper.assertElementText(testConfig, "No data found", "No data available", nodataFoundLabel);
    }

    public void verifyPaidPaymentLink(String paymentLinkNumber)
    {
        Element.enterData(testConfig, searchTextbox, paymentLinkNumber, "Search Textbox");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        AssertHelper.assertElementIsNotDisplayed(testConfig, "Payment link action dropdown", Element.getPageElement(testConfig, How.xPath, String.format("//td[contains(.,'%s')]/following-sibling::td[6]/span", paymentLinkNumber)));
        AssertHelper.compareEquals(testConfig, "Table Link Status", "Paid", Element.getText(testConfig, linkStatusInTable, "Link Status In Table"));
    }
}
