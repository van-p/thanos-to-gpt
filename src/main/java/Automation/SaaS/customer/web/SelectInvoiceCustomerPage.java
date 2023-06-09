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

public class SelectInvoiceCustomerPage
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
    @FindBy(xpath = "//div[@class='desktop-modal__wrapper relative']//input[@type='text']")
    private WebElement searchInput;

    public SelectInvoiceCustomerPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, searchInput);
        verifySelectInvoiceCustomerPage();
    }

    private void verifySelectInvoiceCustomerPage()
    {
        AssertHelper.assertElementText(testConfig, "Title select customer invoice page", SaasHelper.saasStaticDataBase.getSelectCustomerPageTitle(), addNewInvoicePageTitle);
        WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, String.format(buttonField, SaasHelper.saasStaticDataBase.getNewCustomerButtonLabel())), "Wait add new customer element");
        AssertHelper.assertElementIsDisplayed(testConfig, "Search text input", searchInput);
    }

    public AddNewInvoiceItemsPage selectCustomer(String email)
    {
        Element.enterData(testConfig, searchInput, email, "Search customer input");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WebElement emailOption = Element.getPageElement(testConfig, How.xPath, String.format(customerEmailOption, email));
        WaitHelper.waitForElementToBeDisplayed(testConfig, emailOption, "Wait Customer email element");
        WaitHelper.waitForElementToBeClickable(testConfig, emailOption, "Customer email element");
        Element.click(testConfig, emailOption, "Select customer email", true);
        return new AddNewInvoiceItemsPage(testConfig);
    }
}

