package Automation.Access.customer.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PersonalDetailsPage
{

    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']")
    private WebElement personalDetailsTitleLabel;
    @FindBy(xpath = "//button[@data-cy='person-details-page-email-button']")
    private WebElement workEmailUpdateButton;
    @FindBy(xpath = "//button[@data-cy='person-details-page-phone-button']")
    private WebElement mobileNumberUpdateButton;
    @FindBy(xpath = "//div[@data-cy='person-details-page-person-edit-preferred-name']//span[text()='Update']")
    private WebElement preferredNameUpdateButton;
    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']")
    private WebElement updateEmailAddressTitle;
    @FindBy(xpath = "//input[@data-cy='person-update-preferred-name-input']")
    private WebElement preferredNameField;
    @FindBy(xpath = "//span[text()='Save']")
    private WebElement preferredNameSaveButton;
    @FindBy(xpath = "//div[@data-cy='person-details-page-person-edit-email']//div[@class='input-value q-mt-sm']")
    private WebElement matchedEmail;
    @FindBy(xpath = "//div[@data-cy='person-details-page-person-edit-phone']//div[@class='q-mt-md']")
    private WebElement matchedMobileNumber;
    @FindBy(xpath = "//div[@data-cy='person-details-page-person-edit-preferred-name']//div[@class='input-value q-mt-sm']")
    private WebElement matchedPreferredNameValue;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "div[class='q-notification__message col']")
    private WebElement toastMessageEmail;

    private final Config testConfig;

    public PersonalDetailsPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading Icon");
        WaitHelper.waitForPageLoad(testConfig, personalDetailsTitleLabel);
        WaitHelper.waitForElementToBeDisplayed(testConfig, personalDetailsTitleLabel, "Person details title page");
    }

    public void inputNewPreferredName(String preferredName)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, this.preferredNameField, "preferred Name Field");
        Element.clearDataWithBackSpace(testConfig, preferredNameField, "Clear current preferred Name");
        Element.enterData(testConfig, preferredNameField, preferredName, "preferred Name");
        Element.click(testConfig, preferredNameSaveButton, "click on Save Button");
        WaitHelper.waitForElementToBeHidden(testConfig, preferredNameSaveButton, "wait save button");
    }

    public UpdatePersonalDetailsPage clickOnWorkEmailUpdateButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.workEmailUpdateButton, "Work Email Update button");
        Element.click(testConfig, this.workEmailUpdateButton, "Work Email Update button");
        return new UpdatePersonalDetailsPage(testConfig);
    }

    public UpdatePersonalDetailsPage clickOnMobileNumberUpdateButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.mobileNumberUpdateButton, "Mobile Number Update button");
        Element.click(testConfig, this.mobileNumberUpdateButton, "Mobile Number Update");
        return new UpdatePersonalDetailsPage(testConfig);
    }

    public void clickOnPreferredNameUpdateButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.preferredNameUpdateButton, "preferred Name Update button");
        Element.click(testConfig, this.preferredNameUpdateButton, "preferred Name Update");
        WaitHelper.waitForElementToBeDisplayed(testConfig, updateEmailAddressTitle, "Invite User Form title");
    }

    public void verifyIfUpdatedEmailFound(String email)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, toastMessageEmail, "wait for toast message hidden");
        AssertHelper.assertElementText(testConfig, "work email", email.toLowerCase(), matchedEmail);
    }

    public void verifyIfUpdatedMobileNumberFound(String updatedMobilePhone)
    {
        String a = String.format("//div[@data-cy='person-details-page-person-edit-phone']//div[@class='q-mt-md'][contains(text(),'%s')]", updatedMobilePhone);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(a), "WaitHelper");
        AssertHelper.assertElementText(testConfig, "mobile number", ("+65 ") + updatedMobilePhone, matchedMobileNumber);
    }

    public void verifyIfUpdatedPreferredNameFound(String preferredName)
    {
        String a = String.format("//div[@data-cy='person-details-page-person-edit-preferred-name']//div[text()=' %s']", preferredName);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(a), "WaitHelper");
        AssertHelper.assertElementText(testConfig, "preferred Name", preferredName, matchedPreferredNameValue);
    }

}
