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

public class InvoiceSentPage
{

    private final String invoiceSentTitle = "//span[contains(text(),'%s')]";

    private final Config testConfig;

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    @FindBy(css = "button[class*='add-invoice-sent-successfully-step__done-button']")
    private WebElement doneButton;

    @FindBy(css = "button[class*='invoice-sent-successfully-step__reminder-button']")
    private WebElement reminderButton;


    public InvoiceSentPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, reminderButton);
        verifyInvoiceSentPage();
    }

    private void verifyInvoiceSentPage()
    {
        AssertHelper.assertElementText(testConfig, "Step Done sent invoice page", SaasHelper.saasStaticDataBase.getDoneStep(), doneButton);
        AssertHelper.assertElementText(testConfig, "Step Reminder sent invoice page", SaasHelper.saasStaticDataBase.getReminderStep(), reminderButton);
    }

    public void verifyInvoiceSent(String customerName)
    {
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        AssertHelper.assertElementIsDisplayed(testConfig, "Invoice Sent message", Element.getPageElement(testConfig, How.xPath, String.format(invoiceSentTitle, SaasHelper.saasStaticDataBase.getInvoiceSentTitle())));
        AssertHelper.assertElementIsDisplayed(testConfig, "Invoice Sent email message", Element.getPageElement(testConfig, How.xPath, String.format(invoiceSentTitle, customerName)));
    }
}
