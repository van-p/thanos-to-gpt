package Automation.Access.dash.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashBusinessesPage
{

    private final Config testConfig;

    @FindBy(xpath = "//h3[@class='title']")
    private WebElement pageTitle;

    DashBusinessesPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, pageTitle);
        verifyDashBusinessesPage();
    }

    private void verifyDashBusinessesPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Page Title", pageTitle);
        AssertHelper.assertElementText(testConfig, "Page Title", "Businesses", pageTitle);
    }
}
