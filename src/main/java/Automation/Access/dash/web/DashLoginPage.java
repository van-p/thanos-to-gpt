package Automation.Access.dash.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashLoginPage
{

    private final Config testConfig;
    @FindBy(xpath = "//input[@data-cy='the-login-email']")
    private WebElement emailTextBox;
    @FindBy(xpath = "//input[@data-cy='the-login-password']")
    private WebElement passwordTextBox;
    @FindBy(xpath = "//button[@data-cy='the-login-submit-button']")
    private WebElement continueButton;
    @FindBy(xpath = "//input[@data-cy='the-login-token']")
    private WebElement tokenTextBox;
    @FindBy(xpath = "//button[@data-cy='the-login-submit-button']/span[.='Login']")
    private WebElement loginButton;

    public DashLoginPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, continueButton);
        verifyDashLoginPage();
    }

    public void verifyDashLoginPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Email textbox", emailTextBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Password textbox", passwordTextBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Continue button", continueButton);
    }

    public DashNavigationPage loginToAdminDashboard(String username, String pin)
    {
        Element.enterData(testConfig, emailTextBox, username, "Username");
        Element.enterData(testConfig, passwordTextBox, pin, "Password");
        Element.click(testConfig, continueButton, "Continue button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, tokenTextBox, "Token Textbox");

        Element.enterData(testConfig, tokenTextBox, testConfig.getRunTimeProperty("dashToken"), "Token");
        Element.click(testConfig, loginButton, "Login button");

        return new DashNavigationPage(testConfig);
    }
}
