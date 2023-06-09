package Automation.SaaS.customer.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PaymentLinksCreatedPage
{

    private final Config testConfig;

    public PaymentLinksCreatedPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, linkCreatedScreenTitle);
        verifyPaymentLinkCreatedPage();
    }

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;
    @FindBy(xpath = "//span[@data-cy='payment-link-created-screen-number']")
    private WebElement linkNumber;
    @FindBy(xpath = "//a[@data-cy='payment-link-created-screen-short-link']")
    private WebElement linkShortLink;
    @FindBy(xpath = "//button[@data-cy='payment-link-created-screen-done-button']")
    private WebElement doneButton;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-copy-button']")
    private WebElement copyButton;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-send-email-button']")
    private WebElement sendEmailButton;
    @FindBy(xpath = "//div[@data-cy='payment-link-created-screen-title']")
    private WebElement linkCreatedScreenTitle;

    public void verifyPaymentLinkCreatedPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Payment link number", linkNumber);
        testConfig.putRunTimeProperty("paymentLinkNumber", linkNumber.getText());
        AssertHelper.assertElementIsDisplayed(testConfig, "Short link", linkShortLink);
        AssertHelper.assertElementText(testConfig, "Payment Links Created Page Title", "Payment link successfully created!", linkCreatedScreenTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Send Email button", sendEmailButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Copy button", copyButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Done button", doneButton);
    }

    public PaymentLinksPage closePaymentLinkCreatedPage()
    {
        Element.click(testConfig, doneButton, "Done button");
        return new PaymentLinksPage(testConfig);
    }
}
