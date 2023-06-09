package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DebitDetailPage
{

    private final Config testConfig;

    @FindBy(xpath = "//img[@alt='Make a transfer']")
    private WebElement makeATransferButton;
    @FindBy(xpath = "//img[@alt='Submit a transfer']")
    private WebElement submitATransferButtonOnAccountDetailPage;
    @FindBy(xpath = "//img[@alt='View transactions']")
    private WebElement viewTransferButton;

    @FindBy(xpath = "//div[@data-cy='quick-actions-transfer-action']")
    private WebElement submitATransferButton;

    @FindBy(xpath = "//img[@alt='View transactions']")
    private WebElement viewTransactionsButton;

    @FindBy(xpath = "//div[contains(@class,'debit-account-details-page__balance')]")
    private WebElement debitBalanceText;

    @FindBy(xpath = "//img[@class='currency-flag']/following-sibling::div/div[1]")
    private WebElement debitDetailPageTitle;
    @FindBy(xpath = "//div[@data-cy='quick-actions-convert-currency-action']")
    private WebElement convertMoneyButton;
    @FindBy(xpath = "//label[@data-cy='convert-currency-list-item-label-SGD-USD']//p")
    private WebElement convertMoneyFromSgdToUsdRadio;
    @FindBy(xpath = "//h3[@data-cy='convert-currency-modal-title']")
    private WebElement transferToConvertMoneyTitle;
    @FindBy(xpath = "//label[@data-cy='convert-currency-list-item-label-USD-SGD']//p")
    private WebElement convertMoneyFromUsdToSgdRadio;
    @FindBy(xpath = "//button[@data-cy='convert-currency-modal-cta']")
    private WebElement takeMeToTransferScreenButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    public DebitDetailPage(Config config, TransferType transferType)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, viewTransactionsButton);
        verifyDebitDetailPage(transferType);
    }

    private void verifyDebitDetailPage(TransferType transferType)
    {
        switch (transferType)
        {
            case Completed, Initiated, Scheduled, Recurring:
            {
                AssertHelper.assertElementIsDisplayed(testConfig, "Make a transfer", makeATransferButton);
            }
            case Submitted, ScheduledSubmitted, RecurringSubmitted:
            {
                AssertHelper.assertElementIsDisplayed(testConfig, "Submit a transfer", submitATransferButton);
            }
            default:
            {
                testConfig.logComment("Option to submit or make a transfer is not present");
            }
        }
        AssertHelper.assertElementIsDisplayed(testConfig, "View transactions", viewTransactionsButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Debit Account balance", debitBalanceText);
        AssertHelper.assertElementText(testConfig, "Detail Page Title", testConfig.testData.get("transferCurrency") + " account", debitDetailPageTitle);
    }

    public RecipientListPage clickMakeATransferButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, makeATransferButton, "Make A Transfer");
        Element.click(testConfig, makeATransferButton, "Click on Make a transfer button");
        return new RecipientListPage(testConfig);
    }

    public RecipientListPage clickSubmitATransferButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, submitATransferButton, "Submit A Transfer");
        Element.click(testConfig, submitATransferButton, "Submit a transfer button");
        return new RecipientListPage(testConfig);
    }

    public void clickConvertMoneyButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, convertMoneyButton, "Convert Money button");
        Element.click(testConfig, convertMoneyButton, "click on Convert Money button");
    }

    public void clickConvertSgdToUsdRadio()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, convertMoneyFromSgdToUsdRadio, "Convert Money from SGD to USD radio");
        Element.click(testConfig, convertMoneyFromSgdToUsdRadio, "Click on Convert Money from SGD to USD radio");
    }

    public void clickConvertUsdToSgdRadio()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, convertMoneyFromUsdToSgdRadio, "Convert Money from USD to SGD radio");
        Element.click(testConfig, convertMoneyFromUsdToSgdRadio, "Click on Convert Money from USD to SGD radio");
        testConfig.putRunTimeProperty("recipientCurrency", CurrencyEnum.USD.name());
    }

    public FxTransferPage clickTakeMeToFxTransferScreenButton(CurrencyEnum currencyEnum)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, takeMeToTransferScreenButton, "Take Me To Transfer Screen Button");
        Element.click(testConfig, takeMeToTransferScreenButton, "Click on Take Me To Transfer Screen Button");
        return new FxTransferPage(testConfig, currencyEnum);
    }

    public TransferPage clickTakeMeToTransferScreenButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, takeMeToTransferScreenButton, "Take Me To Transfer Screen Button");
        Element.click(testConfig, takeMeToTransferScreenButton, "Click on Take Me To Transfer Screen Button");
        return new TransferPage(testConfig);
    }
}