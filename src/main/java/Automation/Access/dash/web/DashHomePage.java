package Automation.Access.dash.web;

import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashHomePage
{

    private final Config testConfig;
    @FindBy(xpath = "//div[@class='the-home__search']")
    private WebElement searchTitleText;
    @FindBy(xpath = "//input[@data-cy='search-bar']")
    private WebElement searchBarTextBox;

    public DashHomePage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, searchBarTextBox);
        verifyDashHomePage();
    }

    public void verifyDashHomePage()
    {
        AssertHelper.assertElementText(testConfig, "Search title", DashAccessHelper.dashAccessStaticDataBase.getDashHomePageTitle(), searchTitleText);
        AssertHelper.assertElementIsDisplayed(testConfig, "Search Box", searchBarTextBox);
    }
}
