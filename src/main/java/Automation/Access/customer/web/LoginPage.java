package Automation.Access.customer.web;

import Automation.Access.customer.helpers.AccessEnums.AfterLoginExpectedLandingPage;
import Automation.Access.customer.helpers.AccessEnums.UserNameType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.web.AccountsListPage;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage
{

    private final Config testConfig;
    @FindBy(css = "div[class='example-field'] div[class='example-label__text']")
    private WebElement usernameLabel;
    @FindBy(name = "username")
    private WebElement usernameField;
    @FindBy(name = "password")
    private WebElement passwordField;
    @FindBy(xpath = "//div[@class='q-checkbox__bg absolute']//*[name()='svg']")
    private WebElement rememberMeCheckbox;
    @FindBy(xpath = "//button[@data-cy='login-step-start-submit-button']")
    private WebElement nextButton;
    @FindBy(xpath = "//div[@data-cy='digit-input-mask']")
    private WebElement systemOtpActivateCodeTextBox;
    @FindBy(css = "div.digit-input__input.flex.flex-center.text-weight-medium.cursor-pointer.digit-input__input--highlight.digit-input__input--blinking")
    private WebElement systemOtpActivateCodeFocusingTextBox;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[contains(@class,'login-step-two-factor-authentication__form-header')]")
    private WebElement headerTitle;

    @FindBy(xpath = "//div[@data-cy='verify-two-factor-authentication-otp-header']/p")
    private WebElement descriptiveText;

    @FindBy(xpath = "//form[@data-cy='verify-two-factor-authentication-otp-form']")
    private WebElement OTPForm;
    @FindBy(xpath = "//form[@data-cy='verify-two-factor-authentication-otp-form']//div[@data-cy='digit-input-box']")
    private WebElement inputOTPBox;
    @FindBy(xpath = "//p[@data-cy='login-steps-locked-attempts']")
    private WebElement lockedAttemptsMessage;

    @FindBy(xpath = "//div[@data-cy='login-remaining-attempts-error']//div[@class='text-weight-medium']")
    private WebElement lockedRemainingAttemptsError;

    @FindBy(css = "div.otp-error-message > div")
    private WebElement otpErrorMessage;
    @FindBy(css = "div.text-weight-bold.text-tertiary.cursor-pointer.q-mt-md.row")
    private WebElement forgotPasswordLink;

    public LoginPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, nextButton);
        verifyLoginPage();
    }

    private void verifyLoginPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Remember me checkbox", rememberMeCheckbox);
        AssertHelper.assertElementText(testConfig, "Search by name or email", AccessHelper.accessStaticDataBase.getUsernameLabel(), usernameLabel);
    }

    public Object fillDetailsAndLogin(String username, String password, AfterLoginExpectedLandingPage afterLoginExpectedLandingPage)
    {
        enterEmailAndPassword(username, password);
        boolean isOtpDisplayed = Element.isElementDisplayed(testConfig, systemOtpActivateCodeTextBox);
        if (isOtpDisplayed)
        {
            enterOTP("1234");
        }
        return switch (afterLoginExpectedLandingPage)
        {
            case AccountsListPage -> new AccountsListPage(testConfig);
            case DashBoardPage -> new DashBoardPage(testConfig);
            case LoginPage -> this;
        };
    }

    public void enterEmailAndPassword(String username, String password)
    {
        Element.enterData(testConfig, usernameField, username, "Username");
        Element.enterData(testConfig, passwordField, password, "Password");
        Element.click(testConfig, nextButton, "Next button");
        WaitHelper.waitForElementToBeHidden(testConfig, nextButton, "Next button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader icon");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loader icon");
    }

    private void enterOTP(String otp)
    {
        String[] otpArray = otp.split("");
        for (String s : otpArray)
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, systemOtpActivateCodeFocusingTextBox, "OTP box focused");
            WaitHelper.waitForMilliSeconds(testConfig, 100);
            Element.enterDataWithoutSelectedElement(testConfig, s, "OTP");
        }
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeHidden(testConfig, systemOtpActivateCodeTextBox, "2FA screen");
    }

    public LoginPage loginWith3IncorrectPasswordAttempts(String username, String password, int numberOfFailedAttempts)
    {
        int remainingAttemptsCount = numberOfFailedAttempts;
        do
        {
            Element.enterData(testConfig, usernameField, username, "Username");
            Element.enterData(testConfig, passwordField, password, "Password");
            Element.click(testConfig, nextButton, "Login button");
            WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader icon");
            WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loader icon");
            remainingAttemptsCount--;
            if (remainingAttemptsCount > 0)
                validateLoginRemainingAttemptsError(remainingAttemptsCount);
        } while (remainingAttemptsCount > 0);
        return this;
    }

    public void validateLoginRemainingAttemptsError(int loginRemainingAttemptsCount)
    {
        String expectText = String.format(AccessHelper.accessStaticDataBase.getLoginRemainingAttemptsError(), loginRemainingAttemptsCount);
        AssertHelper.assertElementText(testConfig, "Login remaining attempts error", expectText, lockedRemainingAttemptsError);
    }

    public LoginPage loginWith3IncorrectOtpAttempts(String username, String password, int numberOfFailedAttempts)
    {
        Element.enterData(testConfig, usernameField, username, "Username");
        Element.enterData(testConfig, passwordField, password, "Password");
        Element.click(testConfig, nextButton, "Login button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");

        WaitHelper.waitForElementToBeDisplayed(testConfig, systemOtpActivateCodeFocusingTextBox, "OTP box focused");
        int remainingAttemptsCount = numberOfFailedAttempts;
        do
        {
            for (int i = 0; i < 4; i++)
            {
                Element.enterDataWithoutSelectedElement(testConfig, "9", "OTP");
                WaitHelper.waitForMilliSeconds(testConfig, 500);
            }
            WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
            WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
            remainingAttemptsCount--;
            if (remainingAttemptsCount > 0)
                validateOtpErrorMessage(remainingAttemptsCount);
        } while (remainingAttemptsCount > 0);
        return this;
    }

    public void validateOtpErrorMessage(int otpRemainingAttemptsCount)
    {
        String expectText = String.format(AccessHelper.accessStaticDataBase.getOtpErrorMessage(), otpRemainingAttemptsCount);
        AssertHelper.assertElementText(testConfig, "OTP error message", expectText, otpErrorMessage);
    }

    public void validateLockedAttemptsMessage(int numberOfFailedAttempts)
    {
        String expectText = String.format(AccessHelper.accessStaticDataBase.getLockedAttemptsMessage(), numberOfFailedAttempts);
        AssertHelper.assertElementText(testConfig, "Locked attempts message", expectText, lockedAttemptsMessage);
    }

    public void verifyTwoFactorAuthenticationPage(boolean isDisplayed, UserNameType userNameType)
    {
        String emailOrPhone;
        if (userNameType.equals(UserNameType.Email))
        {
            emailOrPhone = testConfig.getRunTimeProperty("username");
        } else
        {
            emailOrPhone = testConfig.getRunTimeProperty("phoneNumber");
        }

        if (isDisplayed)
        {
            AssertHelper.assertElementIsDisplayed(testConfig, "2-step verification title", headerTitle);
            AssertHelper.assertElementText(testConfig, "2-step verification title", AccessHelper.accessStaticDataBase.get2StepVerificationTitle(), headerTitle);
            AssertHelper.assertElementIsDisplayed(testConfig, "2-step verification descriptive text", descriptiveText);
            String expectDescriptiveText = String.format(AccessHelper.accessStaticDataBase.get2StepVerificationDescriptiveText(), getMaskedEmailOrPhone(emailOrPhone, userNameType));
            AssertHelper.assertElementText(testConfig, "2-step verification descriptive text", expectDescriptiveText, descriptiveText);
            AssertHelper.assertElementIsDisplayed(testConfig, "OTP box", inputOTPBox);
        } else
        {
            AssertHelper.assertElementIsNotDisplayed(testConfig, "2-step verification title", headerTitle);
            AssertHelper.assertElementIsNotDisplayed(testConfig, "2-step verification descriptive text", descriptiveText);
            AssertHelper.assertElementIsNotDisplayed(testConfig, "OTP box", inputOTPBox);
        }
    }

    private String getMaskedEmailOrPhone(String emailOrPhone, UserNameType userNameType)
    {
        if (userNameType.equals(UserNameType.Email))
        {
            // Example: admin_usec_01@example.com , masked to a***********1@example.com
            String username = emailOrPhone.split("@")[0];
            String domain = emailOrPhone.split("@")[1];
            String partOfText = username.substring(1, username.length() - 1);
            String markedUsername = username.replaceAll(partOfText, partOfText.replaceAll(".", "*"));
            return markedUsername.concat("@".concat(domain));
        } else
        {
            // Example: +657834324556 , masked to +65 7*********6
            String dialCode = emailOrPhone.substring(0, 3);
            String number = emailOrPhone.substring(3);
            String partOfText = number.substring(1, number.length() - 1);
            String maskedPhoneNumber = number.replaceAll(partOfText, partOfText.replaceAll(".", "*"));
            return dialCode.concat(" ").concat(maskedPhoneNumber);
        }
    }

    public UpdatePersonalDetailsPage clickOnForgotPasswordLink()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, forgotPasswordLink, "Forgot Password link");
        Element.click(testConfig, forgotPasswordLink, "Forgot Password link");
        return new UpdatePersonalDetailsPage(testConfig);
    }

}
