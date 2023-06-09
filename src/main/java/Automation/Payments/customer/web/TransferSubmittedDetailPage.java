package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentStaticDataEn.PendingTransferLabelEnum;
import Automation.Payments.customer.helpers.PaymentStaticDataEn.TransactionButtonEnum;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TransferSubmittedDetailPage
{

    private final Config testConfig;
    @FindBy(xpath = "//button[@data-cy='maker-payment-details-page-modify-cta']")
    private WebElement customerTransferSuccessDetailGoHomeButton;
    @FindBy(css = "main > div.transaction-details")
    private WebElement customerTransferDetailsWrapper;
    @FindBy(css = ".transaction-details-stepper__content")
    private WebElement transferStepperContentBox;
    @FindBy(css = ".transaction-details__content-fields")
    private WebElement contentDetailsBox;
    @FindBy(css = ".transaction-details__content-column2")
    private WebElement contentDetailColumnsBox;
    @FindBy(xpath = "//div[@data-cy='transaction-details-title']")
    private WebElement customerTransferDetailsTitle;
    @FindBy(css = ".pending-transfer-details-page__pending-label span")
    private WebElement customerTransferDetailsPendingApprovalNotification;
    @FindBy(xpath = "//div[@data-cy='transactions-details-one-column-recipient-name']")
    private WebElement customerTransferDetailsRecipientName;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-reference']")
    private WebElement customerTransferDetailsReference;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-transfer-fee']")
    private WebElement customerTransferDetailsTransferFee;
    @FindBy(xpath = "//button[@data-cy='maker-payment-details-page-modify-cta']")
    private WebElement customerTransferDetailsManageTransferButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[@data-cy='legacy-transaction-category-select-selected-name']")
    private WebElement customerReviewTransferCategoryText;

    public TransferSubmittedDetailPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerTransferSuccessDetailGoHomeButton);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar");
        WaitHelper.waitForElementToBeHidden(testConfig, spinner, "Loading spinner");
        verifyTransferSubmitDetailPage();
    }

    private void verifyTransferSubmitDetailPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Transaction Details Page", customerTransferDetailsWrapper);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transaction Details Title", customerTransferDetailsTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Pending approval notification", customerTransferDetailsPendingApprovalNotification);
        AssertHelper.assertElementIsDisplayed(testConfig, "Recipient Name", customerTransferDetailsRecipientName);
        AssertHelper.assertElementIsDisplayed(testConfig, "Reference", customerTransferDetailsReference);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Fee", customerTransferDetailsTransferFee);
        AssertHelper.assertElementIsDisplayed(testConfig, "Manage Transfer Button", customerTransferDetailsManageTransferButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Stepper Content Box", transferStepperContentBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Details Box", contentDetailsBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Content Detail Columns Box", contentDetailColumnsBox);
    }

    public void validateInstantLocalTransferInfo()
    {
        AssertHelper.assertElementText(testConfig, "Category", testConfig.getRunTimeProperty("transferCategoryValue"), customerReviewTransferCategoryText);
        AssertHelper.assertElementText(testConfig, "Transfer Submitted title", TransferTitleEnum.TransferSubmitted.getName(), customerTransferDetailsTitle);
        AssertHelper.assertElementText(testConfig, "Pending approval notification", PendingTransferLabelEnum.PendingApproval.getName(), customerTransferDetailsPendingApprovalNotification);
        AssertHelper.assertElementText(testConfig, "Reference", testConfig.getRunTimeProperty("reference").trim(), customerTransferDetailsReference);
        AssertHelper.assertElementText(testConfig, "Manage Transfer Button", TransactionButtonEnum.Manage.getName(), customerTransferDetailsManageTransferButton);
    }
}