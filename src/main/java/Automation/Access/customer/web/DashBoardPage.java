package Automation.Access.customer.web;

import Automation.Access.customer.helpers.AccessEnums.AfterNavigationPage;
import Automation.Access.customer.helpers.AccessEnums.QuickAccessItem;
import Automation.Access.customer.helpers.AccessEnums.SubMenuItem;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.web.AdvanceLimitPage;
import Automation.Payments.customer.web.DebitDetailPage;
import Automation.Payments.customer.web.RecipientListPage;
import Automation.Payments.customer.web.TransactionsPage;
import Automation.SaaS.customer.web.*;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DashBoardPage
{

    private final Config testConfig;
    private final String subMenuXpath = "//div[@data-cy='dashboard-sidebar-default-expanded-section'][child::div[text()='%s']]//a/span";
    private final String subMenuItemXpath = "//div[@data-cy='dashboard-sidebar-default-expanded-section'][child::div[text()='%s']]//a[@alt='%s']";
    private final String parentExpansionItem = "//div[@data-cy='dashboard-sidebar-expansion-section'][descendant::div[text()='%s']]";
    private final String subExpansionItem = "//a[@data-cy='dashboard-sidebar-menu-item']/span[text()='%s']";
    private final String expanded = "//div[@style]";
    private final String debitAccount = "//*[@data-cy='account-details-list-currency' and contains(text(), '%s')]";
    @FindBy(xpath = "//a[@data-cy='dashboard-sidebar-list-home-item']")
    private WebElement iconHome;
    @FindBy(xpath = "//a[@data-cy='dashboard-sidebar-menu-item']")
    private WebElement menuItems;
    @FindBy(xpath = "//div[@data-cy='dashboard-businesses-switcher']")
    private WebElement dashboardBusinessSwitcher;
    @FindBy(xpath = "//a[@data-cy='navigation-item-CNSING.make_a_transfer']")
    private WebElement makeATransferButton;
    @FindBy(xpath = "//a[@data-cy='navigation-item-CNSING.budgets-make_a_transfer']")
    private WebElement makeATransferButtonBO;
    @FindBy(xpath = "//a[@data-cy='navigation-item-CNSING.submit_a_transfer']")
    private WebElement submitATransferButton;
    @FindBy(xpath = "//a[@data-cy='dashboard-sidebar-menu-item' and @alt='Transactions']")
    private WebElement transactionsButton;
    @FindBy(xpath = "//a[@data-cy='dashboard-sidebar-menu-item' and @alt='Advance limit']")
    private WebElement advanceButton;
    @FindBy(css = "button.example-generic-notification__button-subtitle")
    private WebElement reviewLaterButton;
    @FindBy(css = "a.dashboard-quick-action__add-button")
    private WebElement quickAccessAddItem;
    @FindBy(css = "div.q-card.example-modal-card > div > label > div > div > div > input")
    private WebElement quickAccessSearchItemTextBox;
    @FindBy(css = "div.customise-quick-access-page-content__space-items > div > div > button")
    private WebElement quickAccessSaveButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement spinner;
    @FindBy(css = "div > div > div > div.q-notification__message.col")
    private WebElement notificationMessage;
    private final String quickAccessItemPlaceHolder = "//a[@data-cy='navigation-item-CNSING.%s']";
    private final String quickAccessItemCheckedPlaceHolder = "%s//span[contains(@class,'mint-badge-button')]/div[@aria-checked='true']";
    private final String quickAccessItemUncheckedPlaceHolder = "%s//span[contains(@class,'mint-badge-button')]/div[@aria-checked='false']";
    @FindBy(css = "div[data-cy='customise-quick-action-modal-title']")
    private WebElement quickAccessItemPanelTitle;
    @FindBy(xpath = "//div[data-cy='alpha-numeric-password-enable-modal-do-it-later']")
    private WebElement doItLatterButton;
    @FindBy(xpath = "//div[@data-cy='dashboard-businesses-switcher']")
    private WebElement businessSwitcherDropdown;
    @FindBy(xpath = "//div[@data-cy='multiple-business-card-logout-button']")
    private WebElement logoutButton;

    @FindBy(xpath = "//button[@data-cy='dashboard-top-bar-navigation-item']/img[@alt='Settings']")
    private WebElement settingsButton;

    @FindBy(xpath = "//a[@data-cy='dashboard-top-bar-navigation-space-item'][@alt='Security']")
    private WebElement securityButton;

    public DashBoardPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, menuItems);
        verifyHomePage();
        skipPaymentMethodPopupIfShown();
        skipUpdatePinPopupIfShown();
    }

    public Object navigateOnMenu(SubMenuItem subMenuItem, AfterNavigationPage afterNavigationPage)
    {
        boolean isElementExist = false;
        if (subMenuItem.equals(SubMenuItem.Home))
        {
            Element.click(testConfig, iconHome, "Home", true);
            return this;
        }
        switch (subMenuItem.getParent())
        {
            case Overview, Spend, Receive ->
            {
                String itemXpath = String.format(subMenuItemXpath, subMenuItem.getParent().getName(), subMenuItem.getName());
                WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath(itemXpath), subMenuItem.getName());

                String menuItemPath = String.format(subMenuXpath, subMenuItem.getParent().getName());
                List<WebElement> listSubMenuElement = Element.getPageElements(testConfig, How.xPath, menuItemPath);
                if (listSubMenuElement.size() > 3)
                {
                    Element.mouseMove(testConfig, listSubMenuElement.get(0), "sub menu");
                }

                Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, itemXpath), subMenuItem.getName(), true);
            }
            case Directory, Accounting, UserAndAccess ->
            {
                String xpathParent = String.format(parentExpansionItem, subMenuItem.getParent().getName());
                String xpathExpanded = xpathParent.concat(expanded);
                String xpathSubItem = String.format(subExpansionItem, subMenuItem.getName());
                WebElement expandedElement = Element.getPageElement(testConfig, How.xPath, xpathExpanded);
                String style = Element.getAttributeText(testConfig, expandedElement, "style", "Get style attribute");

                if (style.equals("display: none;"))
                {
                    WaitHelper.waitForElementToBeClickable(testConfig, Element.getPageElement(testConfig, How.xPath, xpathParent), subMenuItem.getName());
                    Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, xpathParent), "Click to open " + subMenuItem.getParent(), true);
                }
                WaitHelper.waitForElementToBeClickable(testConfig, Element.getPageElement(testConfig, How.xPath, xpathSubItem), subMenuItem.getName());
                Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, xpathSubItem), subMenuItem.getName(), true);
            }
            default ->
            {
                return null;
            }
        }

        return switch (afterNavigationPage)
        {
            case DashBoardPage -> this;
            case UserAccessPage -> new UserAccessPage(testConfig);
            case BudgetPage -> new BudgetPage(testConfig);
            case PaymentLinks -> new PaymentLinksPage(testConfig);
            case BillPage -> new BillPage(testConfig);
            case CardPage -> new CardPage(testConfig);
            case ExportDataPage -> new ExportDataPage(testConfig);
            case InvoicesPage -> new InvoicesPage(testConfig);
            case XeroPage -> new XeroPage(testConfig);
            default -> null;
        };
    }

    public void verifyHomePage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Dashboard page", dashboardBusinessSwitcher);
    }

    public Object navigateOnQuickAccess(QuickAccessItem quickAccessItem, AfterNavigationPage afterNavigationPage)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElementWithRetry(testConfig, How.xPath, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_"))), String.format("Quick Access - %s", quickAccessItem.getItem()));
        Element.click(testConfig, Element.getPageElementWithRetry(testConfig, How.xPath, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_"))), String.format("%s button on quick access", quickAccessItem.getItem()), false);

        return switch (afterNavigationPage)
        {
            case RecipientList -> new RecipientListPage(testConfig);
            case SecurityPage -> new SecurityPage(testConfig);
            case DashBoardPage -> this;
            case PaymentLinks -> new PaymentLinksPage(testConfig);
            case PersonalDetailsPage -> new PersonalDetailsPage(testConfig);
            default -> null;
        };
    }

    public void skipPaymentMethodPopupIfShown()
    {
        WaitHelper.waitForSeconds(testConfig, 3);
        if (Element.isElementDisplayed(testConfig, reviewLaterButton))
        {
            Element.click(testConfig, reviewLaterButton, "Review latter", false);
        }
    }

    public DebitDetailPage selectDebitAccount(String currency, TransferType transferType)
    {
        String debitCurrency = String.format(debitAccount, currency);
        WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, debitCurrency), "Debit Account");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, debitCurrency), currency + " Account");
        testConfig.putRunTimeProperty("recipientCurrency", currency);
        return new DebitDetailPage(testConfig, transferType);
    }

    public void skipUpdatePinPopupIfShown()
    {
        if (Element.isElementDisplayed(testConfig, doItLatterButton))
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, doItLatterButton, "Review latter");
            Element.click(testConfig, doItLatterButton, "Review latter", false);
        }
    }

    public DashBoardPage addQuickAccessItemAndVerifyUpdatedMessage(QuickAccessItem quickAccessItem)
    {
        WebElement element = Element.getPageElementWithRetry(testConfig, How.xPath, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")));
        if (element != null)
        {
            removeQuickAccessItemAndVerifyUpdatedMessage(quickAccessItem);
        }
        WaitHelper.waitForElementToBeDisplayed(testConfig, quickAccessAddItem, "Quick Access - Add Item");
        Element.click(testConfig, quickAccessAddItem, "Add item button on quick access", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, quickAccessSearchItemTextBox, "Quick Access - Search Item Text Box");
        Element.enterData(testConfig, quickAccessSearchItemTextBox, quickAccessItem.getItem(), "Search item text box on quick access");
        Element.click(testConfig, quickAccessItemPanelTitle, "Complete search action", false);
        element = Element.getPageElement(testConfig, How.xPath, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")));
        WaitHelper.waitForElementToBeClickable(testConfig, element, "Quick Access - Item");
        Element.click(testConfig, element, "Quick Access Item");
        WaitHelper.waitForElementToBeClickable(testConfig, quickAccessSaveButton, "Quick Access - Save Button");
        Element.click(testConfig, quickAccessSaveButton, "Save button on quick access", false);
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        AssertHelper.assertElementText(testConfig, "Quick Access - Notification Message", "Quick access section updated!", notificationMessage);
        return this;
    }

    public void verifyQuickAccessItem(QuickAccessItem quickAccessItem, boolean isPresent)
    {
        WebElement element = Element.getPageElementWithRetry(testConfig, How.xPath, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")));
        if (isPresent)
        {
            AssertHelper.assertElementText(testConfig, "Quick Access Item", quickAccessItem.getItem(), element);
        } else
        {
            AssertHelper.assertElementIsNotDisplayed(testConfig, "Quick Access Item", element);
        }
    }

    public DashBoardPage removeQuickAccessItemAndVerifyUpdatedMessage(QuickAccessItem quickAccessItem)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeDisplayed(testConfig, quickAccessAddItem, "Quick Access - Add Item");
        Element.click(testConfig, quickAccessAddItem, "Add item button on quick access", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, quickAccessSearchItemTextBox, "Quick Access - Search Item Text Box");
        Element.enterData(testConfig, quickAccessSearchItemTextBox, quickAccessItem.getItem(), "Search item text box on quick access");
        Element.click(testConfig, quickAccessItemPanelTitle, "Complete search action", false);
        List<WebElement> elements = Element.getPageElementsWithRetry(testConfig, How.xPath, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")));
        if (elements.size() > 1)
        {
            testConfig.logComment("Item is present in both the sections. Removing the item from the second section.");
            WaitHelper.waitForElementToBeClickable(testConfig, elements.get(1), "Quick Access - Item");
            WaitHelper.waitForElementToBeClickable(testConfig, Element.getPageElement(testConfig, How.xPath, String.format(quickAccessItemCheckedPlaceHolder, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")))), "Quick Access - Item Checked");
            Element.click(testConfig, elements.get(1), "Quick Access - Item Checked");
            WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, String.format(quickAccessItemUncheckedPlaceHolder, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")))), "Quick Access - Item Unchecked");
        } else
        {
            WaitHelper.waitForElementToBeClickable(testConfig, elements.get(0), "Quick Access - Item");
            Element.click(testConfig, elements.get(0), "Quick Access - Item Checked");
            WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, String.format(quickAccessItemUncheckedPlaceHolder, String.format(quickAccessItemPlaceHolder, quickAccessItem.getItem().toLowerCase().replace(" ", "_")))), "Quick Access - Item Unchecked");
        }
        WaitHelper.waitForElementToBeClickable(testConfig, quickAccessSaveButton, "Quick Access - Save Button");
        Element.click(testConfig, quickAccessSaveButton, "Save button on quick access", false);
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        AssertHelper.assertElementText(testConfig, "Quick Access - Notification Message", "Quick access section updated!", notificationMessage);
        return this;
    }

    public RecipientListPage clickOnMakeTransferBOButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, makeATransferButtonBO, "Make a Transfer Button");
        Element.click(testConfig, makeATransferButtonBO, "Make a Transfer Button");
        return new RecipientListPage(testConfig);
    }

    public TransactionsPage clickTransactionsButtonOnLeftSideNavigation()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, transactionsButton, "Transactions button on left side navigation");
        Element.click(testConfig, transactionsButton, "Transactions button on left side navigation", true);
        return new TransactionsPage(testConfig);
    }

    public AdvanceLimitPage clickAdvanceButtonOnLeftSideNavigation()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, advanceButton, "Advance button on left side navigation");
        Element.click(testConfig, advanceButton, "Advance button on left side navigation", true);
        return new AdvanceLimitPage(testConfig);
    }

    public LoginPage clickLogoutButton()
    {
        Element.click(testConfig, businessSwitcherDropdown, "Business switcher dropdown");

        WaitHelper.waitForElementToBeClickable(testConfig, logoutButton, "logout button");
        Element.click(testConfig, logoutButton, "logout button");
        return new LoginPage(testConfig);
    }

}
