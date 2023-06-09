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

public class InvoiceDetailPage
{

    private static final String elementButton = "//button//span[text()='%s']";
    private final Config testConfig;

    @FindBy(xpath = "//div[@data-cy='invoice-details-wrapper-header-title']")
    private WebElement InvoiceDetailPageTitle;
    @FindBy(xpath = "//div[@data-cy='invoice-details-wrapper-header-payments-tab']")
    private WebElement paymentTab;

    @FindBy(xpath = "//div[@data-cy='invoice-payment-no-transaction-found-card-mark-paid-manually-cta']")
    private WebElement markPaidBtn;
    @FindBy(xpath = "//button[@data-cy='receivable-manual-mark-paid-modal-confirm-cta']")
    private WebElement confirmBtn;
    @FindBy(css = "span[class*='text-body1 text-weight-bold text-subtitle1']")
    private WebElement subPaidTitle;
    @FindBy(xpath = "//button[@data-cy='receivable-manaully-marked-paid-card-mark-unpaid-cta']")
    private WebElement markUnPaidBtn;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    public InvoiceDetailPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        verifyDetailInvoicePage();
    }

    private void verifyDetailInvoicePage()
    {
        AssertHelper.assertElementText(testConfig, "Title detail invoice page", SaasHelper.saasStaticDataBase.getInvoiceDetailPageTitle(), InvoiceDetailPageTitle);
    }

    public void markInvoicePaid()
    {
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        Element.click(testConfig, paymentTab, "paymentTab");

        WebElement markPaid = Element.getPageElement(testConfig, How.xPath, String.format(elementButton, "Mark as paid manually"));
        WaitHelper.waitForElementToBeDisplayed(testConfig, markPaid, "markPaidBtn ");
        Element.click(testConfig, markPaid, "markPaidBtn", true);

        WaitHelper.waitForElementToBeDisplayed(testConfig, confirmBtn, "confirmBtn");
        Element.click(testConfig, confirmBtn, "confirmBtn", true);
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
    }

    public void validateInvoiceMarkedPaid()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Validation mark unpaid button", markUnPaidBtn);
        AssertHelper.assertElementText(testConfig, "Sub paid title", SaasHelper.saasStaticDataBase.getInvoiceDetailPaidPageTitle(), subPaidTitle);
    }
}

