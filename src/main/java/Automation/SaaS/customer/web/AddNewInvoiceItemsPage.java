package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewInvoiceItemsPage
{

    private final String invoiceItemInput = "//div[contains(@class,'add-invoice-item-step__items-card')]//tbody/tr[%s]//td[%s]//input";
    private final String dueDateField = "//div[@role='menu']//span[text()='%s']";
    private final String totalAmountLabel = "//span[@data-cy='invoice-item-desktop-row-amount' and text()='%s']";

    private final Config testConfig;

    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']//div[contains(@class,'text-weight-bolder')]")
    private WebElement addNewInvoiceItemsPageTitle;

    @FindBy(xpath = "//div[contains(@class,'q-stepper__tab--active')]//div[@class='q-stepper__title']")
    private WebElement addNewInvoiceItemsTitle;

    @FindBy(xpath = "//button[@data-cy='add-invoice-item-step-add-button']")
    private WebElement newItemBtn;

    @FindBy(xpath = "//button[@data-cy='create-invoice-add-new-step-next-button']")
    private WebElement saveInvoiceItemBtn;

    @FindBy(xpath = "//div[contains(@class,'date-dropdown-select-and-input')]//input")
    private WebElement dueDateInput;

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    @FindBy(xpath = "//button[@data-cy='create-invoice-add-new-step-next-button']")
    private WebElement sendInvoiceButton;

    @FindBy(css = "i[class='q-icon fas fa-caret-down q-select__dropdown-icon rotate-180']")
    private WebElement closeDropdownIcon;

    @FindBy(xpath = "//tr/td[1]")
    private WebElement numberHeader;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading'))]")
    private WebElement loadSystemIconItem;

    @FindBy(xpath = "//div[@data-cy='invoice-summary-card-subtotal-item']/div[contains(@class,'text-right')]")
    private WebElement summaryInfoSubtotal;

    @FindBy(xpath = "//div[@data-cy='invoice-summary-card-discount-item']/div[contains(@class,'text-right')]")
    private WebElement summaryInfoDiscount;

    private final String summaryInfoTotalAmount = "//div[@data-cy='invoice-summary-card-total_amount-item']/div[contains(@class,'text-right')]";

    public AddNewInvoiceItemsPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, newItemBtn);
        verifyAddNewInvoiceItemsPage();
    }

    private void verifyAddNewInvoiceItemsPage()
    {
        AssertHelper.assertElementText(testConfig, "Title Add new invoice items page", SaasHelper.saasStaticDataBase.getAddNewInvoicePageTitle(), addNewInvoiceItemsPageTitle);
        AssertHelper.assertElementText(testConfig, "Progress bar add new items", SaasHelper.saasStaticDataBase.getAddNewInvoiceItemsPageTitle(), addNewInvoiceItemsTitle);
    }


    private void inputDescriptionField(int index, String value, String description)
    {
        String formattedLocator = String.format(invoiceItemInput, index, 2);
        WebElement descriptionField = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeDisplayed(testConfig, descriptionField, "Description Input");
        Element.enterData(testConfig, descriptionField, value, description);

    }

    private void waitLoadItem()
    {
        Element.click(testConfig, numberHeader, "Number header item");
        WaitHelper.waitForElementToBeDisplayed(testConfig, loadSystemIconItem, "Loader", 40);
        WaitHelper.waitForElementToBeHidden(testConfig, loadSystemIconItem, "Loader", 10);
    }

    private void inputUnitPriceField(int index, String value, String description)
    {
        String formattedLocator = String.format(invoiceItemInput, index, 3);
        WebElement unitPriceField = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        Element.enterData(testConfig, unitPriceField, String.valueOf(value), description);
        waitLoadItem();
    }

    private void inputQuantityField(int index, String value, String description)
    {
        WebElement quantityField = Element.getPageElement(testConfig, How.xPath, String.format(invoiceItemInput, index, 4));
        Element.enterData(testConfig, quantityField, String.valueOf(value), description);
        waitLoadItem();
    }

    private void inputDiscountAmount(int index, String value, String description)
    {
        WebElement discountField = Element.getPageElement(testConfig, How.xPath, String.format(invoiceItemInput, index, 6));
        WaitHelper.waitForElementToBeDisplayed(testConfig, discountField, description);
        Element.enterData(testConfig, discountField, String.valueOf(value), description);
        waitLoadItem();
    }

    public void addInvoiceItems(ArrayList<HashMap> invoiceItems)
    {
        testConfig.logComment("Add line items with matched amount " + invoiceItems.size());
        for (int i = 0; i < invoiceItems.size(); i++)
        {
            inputDescriptionField(i + 1, (String) invoiceItems.get(i).get("invoiceItemDescription"), "Input Description");
            inputQuantityField(i + 1, (String) invoiceItems.get(i).get("invoiceItemQuantity"), "Input Quantity");
            inputUnitPriceField(i + 1, String.valueOf(invoiceItems.get(i).get("invoiceItemUnitCost")), "Input Unit Price");
            inputDiscountAmount(i + 1, String.valueOf(invoiceItems.get(i).get("invoiceItemDiscountValue")), "Input Discount");
        }
        WaitHelper.waitForElementToLoadWithGivenPropertyValue(testConfig, How.xPath, summaryInfoTotalAmount, "text", testConfig.testData.get("TotalAmount"));
        WaitHelper.waitForElementToBeClickable(testConfig, this.saveInvoiceItemBtn, "Save invoice button");
        Element.click(testConfig, saveInvoiceItemBtn, "Click to save invoice items");
    }

    public void addInvoiceDetail()
    {
        WaitHelper.waitForPageLoad(testConfig, dueDateInput);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");

        WaitHelper.waitForElementToBeClickable(testConfig, dueDateInput, "Add line item button");
        Element.click(testConfig, dueDateInput, "Due Date field");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, String.format(dueDateField, testConfig.testData.get("dueDate"))), "Click to dueDateInput");
        Element.click(testConfig, saveInvoiceItemBtn, "Click to save invoice items");
    }

    public InvoiceSentPage sendInvoice()
    {
        addInvoiceDetail();
        WaitHelper.waitForPageLoad(testConfig, sendInvoiceButton);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeClickable(testConfig, sendInvoiceButton, "Send invoice button");
        Element.click(testConfig, sendInvoiceButton, "Click to Send Invoice Button");
        return new InvoiceSentPage(testConfig);
    }

    public void validateTotalAmountInSummaryInfo()
    {
        AssertHelper.assertElementText(testConfig, "Validate Subtotal", testConfig.testData.get("subtotal"), summaryInfoSubtotal);
        AssertHelper.assertElementText(testConfig, "Validate Discount", testConfig.testData.get("discount"), summaryInfoDiscount);
        AssertHelper.assertElementText(testConfig, "Validate Total Amount", testConfig.testData.get("totalAmount"), Element.getPageElement(testConfig, How.xPath, summaryInfoTotalAmount));
    }

    public InvoiceSentPage sendInvoiceWithUploadFileAndRequireField(ArrayList<HashMap> invoiceItems)
    {
        addInvoiceDetail();
        WaitHelper.waitForPageLoad(testConfig, sendInvoiceButton);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeClickable(testConfig, sendInvoiceButton, "Send invoice button");
        Element.click(testConfig, sendInvoiceButton, "Click to sendInvoiceButton");
        return new InvoiceSentPage(testConfig);
    }
}
