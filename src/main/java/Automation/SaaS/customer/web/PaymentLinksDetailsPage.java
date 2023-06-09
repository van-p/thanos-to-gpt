package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasEnums.PaymentLinkStatus;
import Automation.Utils.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentLinksDetailsPage
{

    private final Config testConfig;
    @FindBy(css = "div.modal-page__title > header > div > div.text-h4")
    private WebElement pageTitle;
    @FindBy(css = "div.modal-page__title > header > div:nth-child(2) > div.invoice-ticket-header__top-details > div > div.col")
    private WebElement customerNameLabel;
    @FindBy(css = "button[data-cy='payment-link-details-header-menu']")
    private WebElement actionDropdown;
    @FindBy(css = "div.amount-value__value")
    private WebElement amountLabel;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "button[data-cy='invoice-details-tab-delete-button']")
    private WebElement deletePaymentLinkButton;
    @FindBy(css = "button[data-cy='invoice-payment-no-transaction-found-card-mark-paid-manually-cta']")
    private WebElement markAsPaidManuallyButton;
    @FindBy(css = "button[data-cy='receivable-manaully-marked-paid-card-mark-unpaid-cta']")
    private WebElement markAsUnPaidManuallyButton;
    @FindBy(css = "div.approval-policy-form-delete-range__cta > button.text-pink-13.q-btn--actionable.q-focusable.q-hoverable.example-button.example-button--cta.example-button--fluid")
    private WebElement deletePaymentLinkConfirmation;
    @FindBy(css = "div.q-notification__message")
    private WebElement toastMessage;
    @FindBy(css = "div[data-cy='payment-link-status-badge-label']")
    private WebElement linkStatusLabel;
    @FindBy(css = "div[data-cy='payment-link-details-header-payments-tab']")
    private WebElement paymentsTab;
    @FindBy(css = "div.invoice-ticket-header__bottom-details > div.text-right")
    private WebElement paidOnLabel;
    @FindBy(css = "div.invoice-section-card > div:nth-child(1) > div.row > span.text-subtitle1")
    private WebElement paidSubtitleText;
    @FindBy(css = "span[data-cy='receivable-manually-marked-paid-card-transaction-date']")
    private WebElement transactionDateLabel;
    @FindBy(css = "button[data-cy='receivable-manual-mark-paid-modal-confirm-cta']")
    private WebElement confirmButton;
    @FindBy(css = "div[data-cy='modal-page-back-or-close-button']")
    private WebElement closeButton;

    public PaymentLinksDetailsPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, pageTitle);
        verifyPaymentLinkDetailsPage();
    }

    public void verifyPaymentLinkDetailsPage()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerNameLabel, "Customer name");
        AssertHelper.assertElementText(testConfig, "Payment Links Edit Detail Page Title", String.format("Link #%s", testConfig.getRunTimeProperty("number")), pageTitle);
        AssertHelper.assertElementText(testConfig, "Customer Name", testConfig.testData.get("customer"), customerNameLabel);
        AssertHelper.assertElementText(testConfig, "Amount Label", String.valueOf(Double.parseDouble(testConfig.testData.get("amount")) / 100), amountLabel);
    }

    public PaymentLinksPage deletePaymentLink(String paymentLinkNumber)
    {
        Element.click(testConfig, deletePaymentLinkButton, "Delete link");
        WaitHelper.waitForElementToBeClickable(testConfig, deletePaymentLinkConfirmation, "Delete link confirmation");
        Element.click(testConfig, deletePaymentLinkConfirmation, "Delete link confirmation");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeDisplayed(testConfig, toastMessage, "Delete link success message");
        AssertHelper.assertElementText(testConfig, "Delete link success message", String.format("Payment link %s deleted successfully", paymentLinkNumber), toastMessage);
        return new PaymentLinksPage(testConfig);
    }

    public PaymentLinksDetailsPage markPaymentLinkStatusManually(String paymentLinkNumber, SaasEnums.PaymentLinkStatus paymentLinkStatus)
    {
        Element.click(testConfig, paymentsTab, "Payments tabs");
        String message = String.format("Payment link #%s marked as paid", paymentLinkNumber);
        if (paymentLinkStatus.equals(PaymentLinkStatus.Paid))
        {
            WaitHelper.waitForElementToBeClickable(testConfig, markAsPaidManuallyButton, "Mark as paid manually");
            Element.click(testConfig, markAsPaidManuallyButton, "Mark as paid manually");
            WaitHelper.waitForElementToBeDisplayed(testConfig, confirmButton, "Confirm paid");
            Element.click(testConfig, confirmButton, "Confirm paid");
        } else
        {
            WaitHelper.waitForElementToBeClickable(testConfig, markAsUnPaidManuallyButton, "Mark as unpaid manually");
            Element.click(testConfig, markAsUnPaidManuallyButton, "Mark as unpaid manually");
            message = String.format("Payment link #%s marked as unpaid", paymentLinkNumber);
        }
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeDisplayed(testConfig, toastMessage, "Mark payment link toast message");
        AssertHelper.assertElementText(testConfig, "Payment link toast message", message, toastMessage);
        return this;
    }

    public PaymentLinksDetailsPage verifyPaymentLinkStatus(PaymentLinkStatus paymentLinkStatus)
    {
        String format = "dd MMM yyyy";
        String date = DateTimeFormatter.ofPattern(format).format(LocalDate.now());
        if (paymentLinkStatus.equals(PaymentLinkStatus.Paid))
        {
            WaitHelper.waitForElementToBeHidden(testConfig, actionDropdown, "action dropdown");
            AssertHelper.assertElementText(testConfig, "Payment link paid date", String.format("Paid on\n%s", date), paidOnLabel);
            AssertHelper.assertElementText(testConfig, "Payment link paid text", "Payment link marked as paid", paidSubtitleText);
            AssertHelper.assertElementText(testConfig, "Payment link transaction date", date, transactionDateLabel);
        } else
        {
            date = DateTimeFormatter.ofPattern(format).format(DataGenerator.convertStringToLocalDateGivenFormat(testConfig.testData.get("paymentLinkExpiryDate"), "yyyy-MM-dd"));
            WaitHelper.waitForElementToBeDisplayed(testConfig, actionDropdown, "action dropdown");
            AssertHelper.assertElementText(testConfig, "Payment link expiry date", String.format("Expiry date\n%s", date), paidOnLabel);
            AssertHelper.assertElementIsNotDisplayed(testConfig, "Payment link paid text", paidSubtitleText);
            AssertHelper.assertElementIsNotDisplayed(testConfig, "Payment link transaction date", transactionDateLabel);
        }
        AssertHelper.assertElementText(testConfig, "Payment link status", paymentLinkStatus.toString(), linkStatusLabel);
        return this;
    }

    public PaymentLinksPage closePaymentLinkDetailsPage()
    {
        Element.click(testConfig, closeButton, "Close button");
        return new PaymentLinksPage(testConfig);
    }
}
