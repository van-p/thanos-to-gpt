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

public class AddNewBillPage
{

    private static final String lineItemField = "//div[contains(@class,'payable-line-items-table')]//tbody/tr[%s]//td[%s]//input";
    private static final String lineItemTotalField = "//div[contains(@class,'payable-line-items-table')]//tbody/tr[%s]//td[%s]//span[@data-cy='payable-line-item-amount-preview-amount']";
    private static final String currencyOption = "//div[@role='option']//div[text()='%s']";
    @FindBy(xpath = "//div[@data-cy='accounting-sync-message-content']")
    private WebElement errorMessageLabel;

    private final Config testConfig;
    @FindBy(xpath = "//input[@data-cy='payable-invoice-basic-details-card-bill-number-input']")
    private WebElement totalAmountInput;

    @FindBy(xpath = "//input[@data-cy='generic-amount-with-currency-dropdown-amount-input']")
    private WebElement amountInput;

    @FindBy(xpath = "//input[@data-cy='generic-amount-with-currency-dropdown-currency']")
    private WebElement currencyInput;

    @FindBy(xpath = "//div[@data-cy='generic-amount-with-currency-dropdown-amount']//i[@role='presentation']")
    private WebElement currencyDropdown;

    @FindBy(xpath = "//button[@data-cy='payable-line-item-details-add-cta']")
    private WebElement addLineItemsBtn;
    @FindBy(xpath = "//div[@data-cy='payable-line-items-total-difference']//span[@data-cy='payable-line-item-amount-preview-amount']")
    private WebElement differenceAmountLabel;
    @FindBy(xpath = "//input[@data-cy='payable-invoice-basic-details-card-bill-number-input']")
    private WebElement addLineItemBtn;
    @FindBy(xpath = "//button[@data-cy='payable-line-items-list-save-cta']")
    private WebElement saveLineItemBtn;
    @FindBy(xpath = "//button[@data-cy='payable-line-items-dialog-header-new-line-item-cta']")
    private WebElement newLineItemBtn;
    @FindBy(xpath = "//div[@data-cy='payable-line-item-details-count']")
    private WebElement lineItemCountLbl;
    @FindBy(xpath = "//div[@data-cy='payable-create-procedure-header-title']")
    private WebElement addNewBillPageTitle;
    @FindBy(xpath = "//div[@data-cy='payable-invoice-upload-file-uploader']")
    private WebElement uploadFileUpBtn;
    @FindBy(css = "span[class='text-weight-bolder text-h6 inline-block q-mr-auto']")
    private WebElement headerLineItems;
    @FindBy(xpath = "//div[@data-cy='payable-line-items-error-messages-list-ocr-success']")
    private WebElement messageLabel;


    public AddNewBillPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, uploadFileUpBtn);
        verifyAddNewBillPage();
    }

    private void verifyAddNewBillPage()
    {
        AssertHelper.assertElementText(testConfig, "Title Add new bill page", SaasHelper.saasStaticDataBase.getAddNewBillPageTitle(), addNewBillPageTitle);
        WaitHelper.waitForElementToBeDisplayed(testConfig, totalAmountInput, "Total Amount Input");
        WaitHelper.waitForElementToBeDisplayed(testConfig, currencyDropdown, "Currency dropdown");
    }

    private void inputDescriptionField(int index, String value, String description)
    {
        String formattedLocator = String.format(lineItemField, index, 2);
        WebElement descriptionField = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeDisplayed(testConfig, descriptionField, "Total Amount Input");
        Element.enterData(testConfig, descriptionField, value, description);
    }

    private void inputQuantityField(int index, String value, String description)
    {
        String formattedLocator = String.format(lineItemField, index, 3);
        WebElement quantityField = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeDisplayed(testConfig, quantityField, description);
        Element.enterData(testConfig, quantityField, String.valueOf(value), description);
    }

    private void inputUnitPriceField(int index, String value, String description)
    {
        String formattedLocator = String.format(lineItemField, index, 4);
        WebElement quantityField = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeDisplayed(testConfig, quantityField, description);
        Element.enterData(testConfig, quantityField, String.valueOf(value), description);
    }

    public void addLineItems(ArrayList<HashMap> lineItems)
    {
        testConfig.logComment(String.format("Add line items with matched amount", lineItems.size()));
        for (int i = 0; i < lineItems.size(); i++)
        {
            inputDescriptionField(i + 1, (String) lineItems.get(i).get("Description"), "Input Description");
            inputQuantityField(i + 1, (String) lineItems.get(i).get("Quantity"), "Input Quantity");
            inputUnitPriceField(i + 1, String.valueOf(lineItems.get(i).get("UnitPrice")), "Input Unit Price");
        }

        Element.click(testConfig, saveLineItemBtn, "Click to save line items");
    }

    public void addLineItem(ArrayList lineItems, int invoiceAmount)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, currencyInput, "Currency input");
        Element.click(testConfig, currencyDropdown, "Click on currency option", true);
        Element.enterData(testConfig, currencyInput, "SGD", "Input currency amount");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, String.format(currencyOption, "Singapore Dollar")), "Click on currency option");
        Element.enterData(testConfig, amountInput, String.valueOf(invoiceAmount), "Input invoice amount");
        WaitHelper.waitForElementToBeClickable(testConfig, addLineItemsBtn, "Add line item button");
        Element.click(testConfig, addLineItemsBtn, "Click on add line items button", true);
        addLineItems(lineItems);
    }

    public void validateTotalLineItem(int expectTotalLineItem)
    {
        Element.isElementDisplayed(testConfig, lineItemCountLbl);
        AssertHelper.assertElementText(testConfig, "Total line items", expectTotalLineItem + " items", lineItemCountLbl);
    }

    public void validateLineItemInfo(ArrayList<HashMap> expectLineItems)
    {
        Element.click(testConfig, addLineItemsBtn, "Click on add line items button", true);
        WaitHelper.waitForElementToBeDisplayed(testConfig, headerLineItems, "Header line item panel");
        for (int i = 0; i < expectLineItems.size(); i++)
        {
            AssertHelper.assertElementText(testConfig, "Validate Description", (String) expectLineItems.get(i).get("Description"), Element.getPageElement(testConfig, How.xPath, String.format(lineItemField, i + 1, 2)));
            AssertHelper.assertElementText(testConfig, "Validate Quantity", (String) expectLineItems.get(i).get("Quantity"), Element.getPageElement(testConfig, How.xPath, String.format(lineItemField, i + 1, 3)));
            AssertHelper.assertElementText(testConfig, "Validate Unit Price", expectLineItems.get(i).get("UnitPrice") + ".00", Element.getPageElement(testConfig, How.xPath, String.format(lineItemField, i + 1, 4)));
            AssertHelper.assertElementText(testConfig, "Validate Total", expectLineItems.get(i).get("Total") + ".00", Element.getPageElement(testConfig, How.xPath, String.format(lineItemTotalField, i + 1, 5)));
        }
    }

    public void validateLineItemErrorMessage()
    {
        Element.isElementDisplayed(testConfig, errorMessageLabel);
        AssertHelper.assertElementText(testConfig, " Validate the total does not match the invoice amount", SaasHelper.saasStaticDataBase.getNotMatchedAmountErrorMessage(), errorMessageLabel);
    }

    public void editLineItems(ArrayList lineItems, int invoiceAmount)
    {
        Element.click(testConfig, addLineItemsBtn, "Click on edit line items button", true);
        addLineItems(lineItems);
    }

    public void validateLineItemMessage(String message)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, messageLabel, "Message label");
        AssertHelper.assertElementText(testConfig, "Validate message Ocr can be captured Bill line items values", message, messageLabel);
    }
}

