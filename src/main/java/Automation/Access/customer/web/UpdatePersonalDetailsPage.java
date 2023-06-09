package Automation.Access.customer.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Calendar;
import java.util.Locale;

import static Automation.Utils.CommonUtilities.convertDateStringToCalendar;

public class UpdatePersonalDetailsPage
{

    private final String identityTypeRegistrationItem = "//div[@role='listbox']//div[@class='q-item__label']/span[starts-with(text(),'%s')]";
    @FindBy(css = ".person-details-edit-step-password__header.text-h4.text-weight-bolder.q-mx-auto")
    private WebElement updateEmailAddressTitleLabel;
    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']")
    private WebElement updateEmailAddressTitle;
    @FindBy(xpath = "//input[@data-cy='password-confirm-form-password-input-input']")
    private WebElement currentLoginPasswordField;
    @FindBy(xpath = "//button[@data-cy='password-confirm-form-submit']")
    private WebElement nextUpdateMobileNumberButton;
    @FindBy(xpath = "//div[@data-cy='digit-input-mask']")
    private WebElement systemOtpActivateCodeTextBox;
    @FindBy(css = "div.digit-input__input.flex.flex-center.text-weight-medium.cursor-pointer.digit-input__input--highlight.digit-input__input--blinking")
    private WebElement systemOtpActivateCodeFocusingTextBox;
    @FindBy(xpath = "//input[@data-cy='security-questions-form-identity-type-select-input']")
    private WebElement identityTypeRegistrationDropDown;
    @FindBy(xpath = "//input[@data-cy='security-questions-form-identity-number-input']")
    private WebElement identityNumberField;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-select-year-button']")
    private WebElement datePickerSelectYearButton;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-select-month-button']")
    private WebElement datePickerSelectMonthButton;
    private static final String datePickerYearMonthItem = "//div[@data-cy='example-date-picker']//span[@class='block'][text()='%s']";
    private static final String datePickerDayItem = "//div[contains(@class,'q-date__calendar-item--in')]//span[@class='block'][text()='%s']";
    @FindBy(xpath = "//input[@data-cy='person-details-edit-step-insert-email-input']")
    private WebElement newEmailAddressField;
    @FindBy(xpath = "//input[@data-cy='person-details-edit-step-insert-phone-input']")
    private WebElement newMobileNumberField;
    @FindBy(xpath = "//button[@data-cy='person-details-edit-step-insert-submit']")
    private WebElement nextUpdateEmailAddressButton;
    @FindBy(xpath = "//input[@data-cy='security-questions-form-date-of-birth']")
    private WebElement dateOfBirthCalendar;
    @FindBy(xpath = "//button[@data-cy='security-questions-form-submit']")
    private WebElement securityQuestionsConfirmButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "input[data-cy='password-reset-step-enter-email-email-input-input']")
    private WebElement passwordResetEmailInput;
    @FindBy(css = "button[data-cy='password-reset-step-enter-email-submit']")
    private WebElement passwordResetEmailSubmit;
    @FindBy(css = "input[data-cy='password-alpha-numeric-form-password-input']")
    private WebElement alphaNumericPasswordInput;
    @FindBy(css = "input[data-cy='password-alpha-numeric-form-password-repeated-input']")
    private WebElement alphaNumericPasswordRepeatedInput;
    @FindBy(css = "button[data-cy='password-alpha-numeric-form-submit']")
    private WebElement alphaNumericPasswordSubmit;
    @FindBy(css = "div.q-mb-sm.text-h5.text-weight-bolder")
    private WebElement successMessage;

    private final Config testConfig;

    public UpdatePersonalDetailsPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
    }

    public void inputCurrentLoginPasswordAndEnterOtp()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.currentLoginPasswordField, "current Login Password Field");
        Element.enterData(testConfig, currentLoginPasswordField, testConfig.testData.get("password"), "Pin");
        WaitHelper.waitForElementToBeClickable(testConfig, nextUpdateMobileNumberButton, "next Update Mobile Number Button");
        Element.click(testConfig, this.nextUpdateMobileNumberButton, "next Update Mobile Number Button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForOptionalElement(testConfig, systemOtpActivateCodeTextBox, "Otp Activate Code Text Box");
        enterOTP(testConfig.getRunTimeProperty("otp"));
    }

    public void inputCurrentEmailAndEnterOtp()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, passwordResetEmailInput, "Email Field");
        Element.enterData(testConfig, passwordResetEmailInput, testConfig.testData.get("username"), "Email");
        WaitHelper.waitForElementToBeClickable(testConfig, passwordResetEmailSubmit, "Next Button");
        Element.click(testConfig, passwordResetEmailSubmit, "Next Button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForOptionalElement(testConfig, systemOtpActivateCodeTextBox, "Otp Activate Code Text Box");
        testConfig.logComment("Enter Email OTP");
        enterOTP(testConfig.getRunTimeProperty("otp"));
        testConfig.logComment("Enter Phone OTP");
        enterOTP(testConfig.getRunTimeProperty("otp"));
    }

    private void enterOTP(String otp)
    {
        String[] otpArray = otp.split("");
        for (int i = 0; i < otpArray.length; i++)
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, systemOtpActivateCodeFocusingTextBox, "OTP box focused");
            Element.enterDataWithoutSelectedElement(testConfig, otpArray[i], "OTP");
            WaitHelper.waitForMilliSeconds(testConfig, 500);
        }
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
    }

    public void selectIdentityTypeRegistration(String itemValue)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, identityTypeRegistrationDropDown, "Identity Type Registration Dropdown");
        Element.click(testConfig, identityTypeRegistrationDropDown, "Identity Type Registration Dropdown", true);
        String itemXpath = String.format(identityTypeRegistrationItem, itemValue);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(itemXpath), "Identity Type Registration Dropdown Item");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, itemXpath), "Identity Type Registration Item");
    }

    public void inputIdentityNumber(String identityNumber)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, this.identityNumberField, "identity Number Field");
        Element.enterData(testConfig, identityNumberField, identityNumber, "identity Number");
    }

    private void selectYear(int selectedYear)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, datePickerSelectYearButton, "Date Picker Select Year Button");
        Element.click(testConfig, datePickerSelectYearButton, "Date Picker Select Year Button");
        String itemXpath = String.format(datePickerYearMonthItem, selectedYear);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(itemXpath), "Date Picker Year Month Item");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, itemXpath), "Date Picker Year Month Item");
    }

    private void selectMonth(String selectedMonth)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, datePickerSelectYearButton, "Date Picker Select Year Button");
        Element.click(testConfig, datePickerSelectMonthButton, "Date Picker Select Month Button");
        String itemXpath = String.format(datePickerYearMonthItem, selectedMonth);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(itemXpath), "Date Picker Year Month Item");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, itemXpath), "Date Picker Year Month Item");
    }

    private void selectDay(int selectedDay)
    {
        String itemXpath = String.format(datePickerDayItem, selectedDay);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(itemXpath), "Date Picker Day Item");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, itemXpath), "Date Picker Day Item");
    }

    public void selectDatePicker(String selectedDate, String format)
    {
        Calendar cal = convertDateStringToCalendar(selectedDate, format);
        selectYear(cal.get(Calendar.YEAR));
        selectMonth(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
        selectDay(cal.get(Calendar.DAY_OF_MONTH));
    }

    public void clickDateOfBirthCalendar()
    {
        Element.click(testConfig, this.dateOfBirthCalendar, "click Date Of Birth");
    }

    public void clickSecurityQuestionsConfirmButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, securityQuestionsConfirmButton, "Security Questions Confirm Button");
        Element.click(testConfig, securityQuestionsConfirmButton, "Security Questions Confirm Button");
    }

    public void inputNewEmailAddress(String email)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.newEmailAddressField, "new Email Address Field");
        Element.enterData(testConfig, newEmailAddressField, email.toLowerCase(), "new Email Address");
        WaitHelper.waitForElementToBeDisplayed(testConfig, nextUpdateEmailAddressButton, "next Update Email Address Button");
        WaitHelper.waitForElementToBeClickable(testConfig, this.nextUpdateEmailAddressButton, "new Email Address Field");
        Element.click(testConfig, this.nextUpdateEmailAddressButton, "next Update Email Address Button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeDisplayed(testConfig, systemOtpActivateCodeTextBox, "Otp Activate Code Text Box");
        enterOTP(testConfig.getRunTimeProperty("otp"));
    }

    public void inputNewMobileNumber(long mobilePhone)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.newMobileNumberField, "new mobile number Field");
        Element.enterData(testConfig, newMobileNumberField, String.valueOf(mobilePhone), "new mobile number");
        WaitHelper.waitForElementToBeDisplayed(testConfig, nextUpdateEmailAddressButton, "next Update Email Address Button");
        WaitHelper.waitForElementToBeClickable(testConfig, this.nextUpdateEmailAddressButton, "new Email Address Field");
        Element.click(testConfig, this.nextUpdateEmailAddressButton, "next Update Email Address Button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeDisplayed(testConfig, systemOtpActivateCodeTextBox, "Otp Activate Code Text Box");
        enterOTP(testConfig.getRunTimeProperty("otp"));
    }

    public void inputAllFieldsInSecurityQuestion()
    {
        selectIdentityTypeRegistration(testConfig.testData.get("identityType"));
        inputIdentityNumber(testConfig.testData.get("identityNumber"));
        clickDateOfBirthCalendar();
        String date = testConfig.testData.get("dateOfBirth");
        String[] a = date.split("-");
        selectDatePicker(a[0], a[1]);
        clickSecurityQuestionsConfirmButton();
    }

    public void inputPasswordAndConfirmPassword(String password, String repeatedPassword)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, alphaNumericPasswordInput, "Password Input");
        Element.enterData(testConfig, alphaNumericPasswordInput, password, "Password");
        WaitHelper.waitForElementToBeDisplayed(testConfig, alphaNumericPasswordRepeatedInput, "Confirm Password Input");
        Element.enterData(testConfig, alphaNumericPasswordRepeatedInput, repeatedPassword, "Password");
        WaitHelper.waitForElementToBeClickable(testConfig, alphaNumericPasswordSubmit, "Alphanumeric Password Confirm Button");
        Element.click(testConfig, alphaNumericPasswordSubmit, "Alphanumeric Password Confirm Button");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public void verifySuccessMessageIsDisplayed(String expectMessage)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, successMessage, "Success message");
        AssertHelper.assertElementText(testConfig, "Compare success message", expectMessage, successMessage);
    }
}
