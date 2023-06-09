package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

public class AddNewInvoicePage
{

    private static final String customerEmailOption = "//span[text()='%s']";
    private static final String buttonField = "//button//div[text()='%s']";

    private final Config testConfig;
    @FindBy(xpath = "//div[contains(@class,'example-header__titles example-header')]")
    private WebElement addNewInvoicePageTitle;

    @FindBy(xpath = "//div[@role='listitem'][1]")
    private WebElement createInvoiceWithoutPdfBtn;

    @FindBy(xpath = "//div[@role='listitem'][2]")
    private WebElement createInvoiceWithPdfBtn;

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    @FindBy(css = "input[class='q-uploader__input overflow-hidden absolute-full'")
    private WebElement uploadFileUpInput;
    @FindBy(xpath = "//span[text()='Confirm']")
    private WebElement confirmYesBtn;

    public AddNewInvoicePage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, addNewInvoicePageTitle);
        verifyAddNewInvoicePage();
    }

    private void verifyAddNewInvoicePage()
    {
        AssertHelper.assertElementText(testConfig, "Title Add new invoice page", SaasHelper.saasStaticDataBase.getAddNewInvoicePageTitle(), addNewInvoicePageTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Create Invoice without pdf button", createInvoiceWithoutPdfBtn);
        AssertHelper.assertElementIsDisplayed(testConfig, "Create Invoice with pdf button", createInvoiceWithPdfBtn);
    }

    public SelectInvoiceCustomerPage createNewInvoiceWithoutPdf()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, createInvoiceWithoutPdfBtn, "");
        Element.click(testConfig, createInvoiceWithoutPdfBtn, "Click on create invoice without pdf button");
        return new SelectInvoiceCustomerPage(testConfig);
    }

    public SelectInvoiceCustomerPage createNewInvoiceWithPdf(String fileName)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, createInvoiceWithoutPdfBtn, "Create invoice with Pdf button");
        Element.enterData(testConfig, uploadFileUpInput, System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "SaaS" + File.separator + "Images" + File.separator + fileName, "description");
        WaitHelper.waitForElementToBeDisplayed(testConfig, confirmYesBtn, "Confirm Yes button");
        Element.click(testConfig, confirmYesBtn, "Confirm Yes button", true);
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        return new SelectInvoiceCustomerPage(testConfig);
    }
}