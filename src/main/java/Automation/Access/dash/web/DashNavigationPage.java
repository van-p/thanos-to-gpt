package Automation.Access.dash.web;

import Automation.Access.dash.helpers.DashAccessEnums.NavigationMenu;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class DashNavigationPage
{

    private final Config testConfig;

    @FindBy(xpath = "//aside/ul[@role='menubar']")
    private WebElement menuBar;
    @FindBy(xpath = "//li[@data-cy='user-menu']/div")
    private WebElement userMenu;
    @FindBy(xpath = "//button[@data-cy='user-menu-logout-button']")
    private WebElement logoutButton;
    @FindBy(xpath = "//div[contains(@id,'notification')]")
    private WebElement notificationTooltip;
    @FindBy(xpath = "//aside[contains(@class, 'menu')]//div[contains(@class, 'collapsed')]")
    private WebElement navigationLeftMenuCollapsed;
    private final String navigationLeftMenuXpath = "//aside/ul[not(contains(@class, 'collapse')) and @role='menubar']//li[not(contains(@class, 'is-opened'))]//div[contains(@class, 'menu__title')]//span[normalize-space() = '%s']";
    private final String navigationSubMenuXpath = "//span[normalize-space() = '%s']/ancestor::li[@class='el-sub-menu is-opened'][1]//li//span[normalize-space() = '%s']";

    public DashNavigationPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, menuBar);
        verifyNavigationPage();
    }

    public void verifyNavigationPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Menu Bar", menuBar);
        AssertHelper.assertElementIsDisplayed(testConfig, "User Menu", userMenu);
        WaitHelper.waitForElementToBeDisplayed(testConfig, navigationLeftMenuCollapsed, "Collapsed button");
        Element.click(testConfig, navigationLeftMenuCollapsed, "Collapsed button");
    }

    public DashLoginPage logoutFromDash()
    {
        WaitHelper.waitForElementToBeHidden(testConfig, notificationTooltip, "Notification tooltip");
        userMenu.click();
        logoutButton.click();
        return new DashLoginPage(testConfig);
    }

    public Object navigateToMenu(NavigationMenu menu)
    {
        if (Objects.isNull(menu.getParent()))
        {
            String parentMenu = String.format(navigationLeftMenuXpath, menu.getName());
            WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(parentMenu), menu.getName());
            Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, parentMenu), menu.getName());
        } else
        {
            navigateToMenu(menu.getParent());
            String subMenu = String.format(navigationSubMenuXpath, menu.getParent().getName(), menu.getName());
            WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(subMenu), menu.getName());
            Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, subMenu), menu.getName());
        }

        return switch (menu)
        {
            case BusinessesBusinesses -> new DashBusinessesPage(testConfig);
            default -> null;
        };
    }
}
