package Automation.Payments.customer.web;

import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.web.DashBoardPage;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AccountsListPage
{

    private final String businessXpath = "//div[contains(@data-cy,'business-selector-page-item') ]//div[text()='%s']";

    @FindBy(css = "img[alt='Logo']")
    private WebElement exampleLogo;

    @FindBy(css = "div[aria-label='ACTIVE']")
    private WebElement activeSection;

    @FindBy(css = ".auth-desktop-sidebar__subtitle")
    private WebElement chooseBusinessMessage;

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;

    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    private final Config testConfig;

    public AccountsListPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, activeSection);
    }

    public void verifyAccountsListPage()
    {
        AssertHelper.assertElementText(testConfig, "Choose business message", PaymentHelper.paymentStaticDataBase.getChooseBusinessMessage(), chooseBusinessMessage);
        AssertHelper.assertElementIsDisplayed(testConfig, "exampleLogo", exampleLogo);
    }

    public Object clickToChooseBusiness(String businessName, AfterNavigationPage afterNavigationPage)
    {
        String businessItemPath = String.format(businessXpath, businessName);
        WebElement businessNameElement = Element.getPageElement(testConfig, How.xPath, businessItemPath);

        WaitHelper.waitForElementToBeClickable(testConfig, businessNameElement, businessName);
        Element.click(testConfig, businessNameElement, businessName, true);

        return switch (afterNavigationPage)
        {
            case DashBoardPage -> new DashBoardPage(testConfig);
            default -> null;
        };
    }
}
