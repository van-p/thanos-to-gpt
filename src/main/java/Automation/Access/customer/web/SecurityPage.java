package Automation.Access.customer.web;

import Automation.Access.customer.helpers.AccessHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SecurityPage
{

    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']")
    private WebElement securityTitleLabel;

    @FindBy(xpath = "//div[@data-cy='security-change-pin-card']")
    private WebElement securityChangePinCard;

    @FindBy(xpath = "//div[@data-cy='security-change-pin-card']//div[@class='example-label__text']")
    private WebElement securityChangePinCardTitle;

    @FindBy(xpath = "//div[@data-cy='security-change-two-factor-authentication-card']//div[@class='example-label__text']")
    private WebElement securityChange2faCardTitle;

    @FindBy(xpath = "//div[@data-cy='security-change-two-factor-authentication-card-business-toggle']")
    private WebElement security2faBusinessToggle;

    @FindBy(xpath = "//div[@data-cy='security-change-two-factor-authentication-card-admin-toggle']")
    private WebElement security2faAdminToggle;

    @FindBy(xpath = "//div[@data-cy='security-change-default-otp-card']")
    private WebElement securityChangeDefaultOTPCard;

    @FindBy(xpath = "//div[@data-cy='security-change-default-otp-card']//div[@class='example-label__text']")
    private WebElement securityChangeDefaultOTPCardTitle;

    @FindBy(css = "div[data-cy='default-otp-medium-page-email']")
    private WebElement defaultOtpMediumPageEmailOption;

    @FindBy(css = "div[data-cy='default-otp-medium-page-phone']")
    private WebElement defaultOtpMediumPagePhoneOption;

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    @FindBy(xpath = "//div[contains(@class,'q-notification__message')]")
    private WebElement notificationMessage;

    @FindBy(xpath = "//button[@data-cy='turn-off-two-factor-authentication-confirm-modal-turn-off-button']")
    private WebElement twoFAConfirmModalTurnOffButton;

    @FindBy(xpath = "//input[@data-cy='password-confirm-form-password-input-input']")
    private WebElement passwordConfirmFormInput;

    @FindBy(xpath = "//button[@data-cy='password-confirm-form-submit']")
    private WebElement passwordConfirmFormSubmit;

    @FindBy(xpath = "//input[@data-cy='password-reset-step-enter-email-email-input-input']")
    private WebElement passwordResetEmailInput;

    @FindBy(xpath = "//button[@data-cy='password-reset-step-enter-email-submit']")
    private WebElement passwordResetEmailSubmit;

    private final Config testConfig;

    public SecurityPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
        WaitHelper.waitForPageLoad(testConfig, securityTitleLabel);
        verifySecurityPage();
    }

    public void verifySecurityPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Security Title Page", securityTitleLabel);
        AssertHelper.assertElementText(testConfig, "Security Title Page", AccessHelper.accessStaticDataBase.getSecurityTitle(), securityTitleLabel);
        AssertHelper.assertElementIsDisplayed(testConfig, "Security Change Pin Card", securityChangePinCard);
        AssertHelper.assertElementText(testConfig, "Security Change Pin Card Title", AccessHelper.accessStaticDataBase.getSecurityChangePinCardTitle(), securityChangePinCardTitle);
        AssertHelper.assertElementText(testConfig, "Security Change 2FA Card Title", AccessHelper.accessStaticDataBase.getSecurityChange2faCardTitle(),
                securityChange2faCardTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Security 2FA Business Toggle",
                security2faBusinessToggle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Security 2FA Admin Toggle",
                security2faAdminToggle);
        AssertHelper.assertElementText(testConfig, "Security Change Default OTP Card Title", AccessHelper.accessStaticDataBase.getSecurityChangeDefaultOtpCardTitle(), securityChangeDefaultOTPCardTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Security Change Default OTP Card", securityChangeDefaultOTPCard);
    }


    public void clickOn2faCardAdminToggle()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, security2faAdminToggle, "2FA Card Admin Toggle");
        Element.click(testConfig, security2faAdminToggle, "2FA Card Admin Toggle");
    }

    public void clickOn2faCardBusinessToggle()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, security2faBusinessToggle, "2FA Card Business Toggle");
        Element.click(testConfig, security2faBusinessToggle, "2FA Card Business Toggle");
    }

    public void validateToastMessage(String expectedMessage)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, notificationMessage, "Notification message");
        AssertHelper.assertElementText(testConfig, "Compare notification message", expectedMessage, notificationMessage);
    }

    public void disable2fa()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, twoFAConfirmModalTurnOffButton, "2FA Turn Off Button");
        Element.click(testConfig, twoFAConfirmModalTurnOffButton, "2FA Turn Off Button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, passwordConfirmFormInput, "Current password");
        Element.enterData(testConfig, passwordConfirmFormInput, testConfig.testData.get("password"), "Current password");
        WaitHelper.waitForElementToBeClickable(testConfig, passwordConfirmFormSubmit, "Password Submit Button");
        Element.click(testConfig, passwordConfirmFormSubmit, "Password Submit Button");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
    }

    public void verifyPersonal2faStateIsCorrect(boolean expectState)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, security2faAdminToggle, "2FA Card Admin Toggle");
        String actualState = Element.getAttributeText(testConfig, security2faAdminToggle, "aria-checked", "2FA Card Admin Toggle");
        AssertHelper.compareEquals(testConfig, "2FA Card Admin Toggle", String.valueOf(expectState), actualState);
    }

    public void verifyOrganisation2faStateIsCorrect(boolean expectState)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, security2faBusinessToggle, "2FA Card Organisation Toggle");
        String actualState = Element.getAttributeText(testConfig, security2faBusinessToggle, "aria-checked", "2FA Card Organisation Toggle");
        AssertHelper.compareEquals(testConfig, "2FA Card Organisation Toggle", String.valueOf(expectState), actualState);
    }

    public Object clickOnChangeLoginPasswordCard()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, securityChangePinCard, "Change Password Card");
        Element.click(testConfig, securityChangePinCard, "Change Password Card");
        return new UpdatePersonalDetailsPage(testConfig);
    }

    public void clickOnChangeDefaultOtpCard()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, securityChangeDefaultOTPCard, "Change Default OTP Card");
        Element.click(testConfig, securityChangeDefaultOTPCard, "Change Default OTP Card");
    }

    public void clickOnDefaultOtpMediumPageEmailOption()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, defaultOtpMediumPageEmailOption, "Default Otp Medium Page Email Option");
        Element.click(testConfig, defaultOtpMediumPageEmailOption, "Default Otp Medium Page Email Option");
    }

    public void clickOnDefaultOtpMediumPagePhoneOption()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, defaultOtpMediumPagePhoneOption, "Default Otp Medium Page Phone Option");
        Element.click(testConfig, defaultOtpMediumPagePhoneOption, "Default Otp Medium Page Phone Option");
    }

}
