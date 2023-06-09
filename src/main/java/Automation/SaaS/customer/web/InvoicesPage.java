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

public class InvoicesPage
{

    private final String filterOption = "//div[@data-cy='filter-option-fields-wrapper-multiple-select']//div[contains(text(),'%s')]";
    @FindBy(xpath = "//button[@data-cy='invoice-page-header-new-invoice-button']")
    private WebElement newInvoiceBtn;

    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']//div[contains(@class,'text-weight-bolder')]")
    private WebElement invoicePageTitle;

    @FindBy(xpath = "//div[@data-cy='invoice-mini-insights-desktop-item-title' and contains(@class,'text-tertiary')][1]")
    private WebElement totalUnpaidLabel;

    @FindBy(css = "div[style='color: rgb(245, 0, 87);'")
    private WebElement overDueLabel;

    @FindBy(css = "div[style='color: rgb(178, 138, 0);")
    private WebElement dueLabel;
    @FindBy(xpath = "//button[contains(@data-cy,'filter-button-state')]")
    private WebElement filterInvoiceStateBtn;

    @FindBy(css = "div[class*='q-checkbox__inner--truthy']")
    private WebElement i;

    @FindBy(xpath = "//button[@data-cy='filter-option-fields-wrapper-button']")
    private WebElement applyFilterBtn;

    @FindBy(xpath = "//tr[@data-cy='invoice-list-table-item'][2]")
    private WebElement firstItemTable;

    @FindBy(xpath = "//div[@role='listitem'][1]")
    private WebElement createInvoiceWithoutPdfBtn;

    @FindBy(xpath = "//div[@role='listitem'][2]")
    private WebElement createInvoiceWithPdfBtn;

    @FindBy(css = "input[class='q-uploader__input overflow-hidden absolute-full'")
    private WebElement uploadFileUpInput;
    @FindBy(xpath = "//span[text()='Confirm']")
    private WebElement confirmYesBtn;

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;


    private final Config testConfig;

    public InvoicesPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, newInvoiceBtn);
        verifyInvoicePage();
    }

    private void verifyInvoicePage()
    {
        AssertHelper.assertElementText(testConfig, "Add new button", SaasHelper.saasStaticDataBase.getAddNewInvoicePageTitle(), newInvoiceBtn);
        AssertHelper.assertElementText(testConfig, "Invoice page title", SaasHelper.saasStaticDataBase.getInvoicePageTitle(), invoicePageTitle);
        AssertHelper.assertElementText(testConfig, "Total out standing label", SaasHelper.saasStaticDataBase.getTotalOutstandingLabel(), totalUnpaidLabel);
        AssertHelper.assertElementText(testConfig, "Over due label", SaasHelper.saasStaticDataBase.getOverdueLabel(), overDueLabel);
        AssertHelper.assertElementText(testConfig, "Due label", SaasHelper.saasStaticDataBase.getDueLabel(), dueLabel);
    }

    public AddNewInvoicePage openNewInvoicePage()
    {
        Element.click(testConfig, this.newInvoiceBtn, "Open create new Invoice Page", false);
        return new AddNewInvoicePage(testConfig);
    }

    public void filterState(String state)
    {
        Element.click(testConfig, filterInvoiceStateBtn, "Filter state button");
        WebElement filterOptionName = Element.getPageElement(testConfig, How.xPath, String.format(filterOption, state));
        Element.click(testConfig, filterOptionName, "Filter state option");
        Element.click(testConfig, applyFilterBtn, "Apply filter button");
    }

    public InvoiceDetailPage openFirstInvoiceDetailPage()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, firstItemTable, "Open first invoice item detail page");
        Element.click(testConfig, firstItemTable, "First invoice item on table");
        return new InvoiceDetailPage(testConfig);
    }
}

