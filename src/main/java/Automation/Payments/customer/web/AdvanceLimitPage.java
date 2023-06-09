package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentStaticDataEn;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AdvanceLimitPage
{

    private final Config testConfig;

    @FindBy(xpath = "//div[contains(text(),'Advance limit')]")
    private WebElement AdvancePageTitle;
    @FindBy(xpath = "//button[@data-cy='advance-limit-page-header-actions-make-a-transfer']")
    private WebElement submitTransferButton;
    @FindBy(xpath = "//div[@data-cy='advance-limit-summary-card']")
    private WebElement advanceLimitSummaryCard;
    @FindBy(xpath = "//div[@data-cy='advance-limit-page-header-advance-info']")
    private WebElement importantInfoCard;
    @FindBy(xpath = "//div[text()='Unbilled transactions']")
    private WebElement unbilledTransactionTab;
    @FindBy(xpath = "//div[text()='Last statement']")
    private WebElement lastStatementTransactionTab;

    @FindBy(xpath = "//div[text()='Past statements']")
    private WebElement pastStatementTransactionTab;

    @FindBy(xpath = "//div[contains(@class,'advance-limit-summary-card__right-column')]//div[contains(@class,'text-body1')]")
    private List<WebElement> advanceCardLimit;

    @FindBy(xpath = "//img[@alt='advance sof banner']")
    private WebElement additionalFeeModalImage;
    @FindBy(xpath = "//div[@data-cy='advance-transfer-additional-fee-modal-heading']")
    private WebElement additionalFeeModalHeading;
    @FindBy(xpath = "//div[@data-cy='advance-transfer-additional-fee-modal-text']")
    private WebElement additionalFeeModalSubHeading;

    @FindBy(xpath = "//span[text()='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//div[@data-cy='advance-transfer-additional-fee-modal-payments-type-button']")
    private WebElement paymentTypeButton;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    public AdvanceLimitPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, AdvancePageTitle);
        verifyAdvanceLimitPage();
    }

    public void verifyAdvanceLimitPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "submit Transfer button", submitTransferButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Advance Limit Summary Card", advanceLimitSummaryCard);
        AssertHelper.assertElementIsDisplayed(testConfig, "Unbilled Transaction Tab", unbilledTransactionTab);
        AssertHelper.assertElementIsDisplayed(testConfig, "Past Statement Tab", pastStatementTransactionTab);
    }

    public AdvanceLimitPage clickSubmitATransferButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, submitTransferButton, "Submit A Transfer");
        Element.click(testConfig, submitTransferButton, "Submit a transfer button");
        return this;
    }

    public RecipientListPage verifyAdditionalFeeModalAndClickContinue()
    {
        WaitHelper.waitForOptionalElement(testConfig, additionalFeeModalImage, "Additional fee modal");
        if (Element.isElementDisplayed(testConfig, additionalFeeModalHeading))
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "Additional Fee Modal Image", additionalFeeModalImage);
            AssertHelper.compareEquals(testConfig, "Additional Fee Modal Heading", PaymentStaticDataEn.advanceAdditionalFeeModalMessages.AdditionalFeeHeading.getName(), Element.getText(testConfig, additionalFeeModalHeading, "Modal heading"));
            AssertHelper.compareEquals(testConfig, "Additional Fee Modal Sub Heading", PaymentStaticDataEn.advanceAdditionalFeeModalMessages.AdditionalFeeSubHeading.getName(), Element.getText(testConfig, additionalFeeModalSubHeading, "Modal sub heading"));
            AssertHelper.assertElementIsDisplayed(testConfig, "Additional Fee Modal continue button", continueButton);
            AssertHelper.assertElementIsDisplayed(testConfig, "Additional Fee Modal Payment Type button", paymentTypeButton);
            Element.click(testConfig, continueButton, "Additional Fee Modal continue button");
            WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar after input");
        }
        return new RecipientListPage(testConfig);
    }

}