package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class XeroPage extends BasePage
{
    private final Config testConfig;
    @FindBy(css = ".transactions-solution-integration-with-export__solution-name.col.text-h4.text-weight-bolder")
    private WebElement pageTitle;
    @FindBy(css = "button[data-cy='transactions-solution-integration-with-export-map-solution-accounts-button']")
    private WebElement mapXeroAccountsButton;
    @FindBy(css = "div[data-cy='accounting-homepage-review-sync-card-expenses-card'] div[data-cy='accounting-integration-sync-item-card-title']")
    private WebElement expensesLink;
    @FindBy(css = "div[data-cy='accounting-homepage-review-sync-card-expenses-card'] div[data-cy='accounting-integration-sync-item-card-description']")
    private WebElement createSpendMoneyTransactionLink;
    @FindBy(css = "button[data-cy='accounting-homepage-connection-card-connect-button']")
    private WebElement manageConnection;
    @FindBy(css = "div[data-cy='accounting-homepage-review-sync-card-title']")
    private WebElement reviewAndSyncTitle;
    @FindBy(css = "div[data-cy='accounting-homepage-connection-card-title'] div[class='q-mt-md']")
    private WebElement connectedWithXero;
    @FindBy(css = "div[data-cy='accounting-homepage-connection-card-organization']")
    private WebElement organizationCard;
    @FindBy(css = "button[data-cy='transactions-integration-export-card-export-cta']")
    private WebElement exportButton;
    @FindBy(css = ".debit-accounts-horizontal-list-item.rounded-md.flex.items-center.no-wrap.cursor-pointer.debit-accounts-horizontal-list-item--active")
    private WebElement advanceAccountButton;
    @FindBy(css = "div[data-cy='debit-accounts-horizontal-list-SGD']")
    private WebElement sgdAccountButton;
    @FindBy(css = "div[data-cy='debit-accounts-horizontal-list-HKD']")
    private WebElement hkdAccountButton;
    @FindBy(css = "div[data-cy='debit-accounts-horizontal-list-IDR']")
    private WebElement iDRAccountButton;
    @FindBy(css = "div[data-cy='debit-accounts-horizontal-list-USD']")
    private WebElement usdAccountButton;
    @FindBy(css = "div[data-cy='debit-accounts-horizontal-list-EUR']")
    private WebElement eurAccountButton;
    @FindBy(css = ".text-h6.text-weight-bold.q-mb-xs")
    private WebElement exportTransactionsForXero;
    @FindBy(css = ".q-checkbox__bg.absolute")
    private WebElement includeAttachmentsCheckbox;
    @FindBy(css = "span[data-cy='example-date-range-input-from-date-text']")
    private WebElement fromDate;
    @FindBy(css = "span[data-cy='example-date-range-input-to-date-text']")
    private WebElement toDate;

    public XeroPage(Config testConfig)
    {
        super(testConfig);
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, organizationCard);
        verifyXeroPage();
    }

    public SyncExpenseWithXeroPage openSyncExpenseWithXero()
    {
        waitForLoaderDisappeared();
        WaitHelper.waitForElementToBeClickable(testConfig, expensesLink, "Expense link");
        Element.click(testConfig, expensesLink, "Expense link");
        return new SyncExpenseWithXeroPage(testConfig);
    }

    public void verifyXeroPage()
    {
        waitForLoaderDisappeared();
        AssertHelper.assertElementText(testConfig, "Page title", "Xero", pageTitle);
        AssertHelper.assertElementText(testConfig, "Map Xero accounts button", SaasHelper.saasStaticDataBase.getMapXeroAccountText(), mapXeroAccountsButton);
        AssertHelper.assertElementText(testConfig, "Create spend money transaction link", SaasHelper.saasStaticDataBase.getSpendLinkText(), createSpendMoneyTransactionLink);
        AssertHelper.assertElementText(testConfig, "Manage connection button", SaasHelper.saasStaticDataBase.getManageConnectionButtonText(), manageConnection);
        AssertHelper.assertElementText(testConfig, "Connected with Xero", SaasHelper.saasStaticDataBase.getConnectedWithXeroSubtitle(), connectedWithXero);
        AssertHelper.assertElementText(testConfig, "Export button", SaasHelper.saasStaticDataBase.getExportButtonText(), exportButton);
    }
}
