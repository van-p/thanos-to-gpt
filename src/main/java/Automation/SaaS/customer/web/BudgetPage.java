package Automation.SaaS.customer.web;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessEnums.CompanyRole;
import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasEnums.BudgetMemberType;
import Automation.SaaS.customer.helpers.SaasEnums.BudgetRole;
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

public class BudgetPage
{

    private final Config testConfig;
    private final String budgetCategoryXpath = "//div[contains(@data-cy,'budget-category-select-modal-item')][child::div[text()='%s']]";
    private final String budgetOwnerUserXpath = "//div[contains(@data-cy,'budget-member-item')][descendant::div[text()='%s']]";
    private final String budgetQuickActionsXpath = "//div[@data-cy='quick-actions-%s-action']";
    private final String budgetNameOnListXpath = "//div[@data-cy='budgets-list-item-desktop-budget-name' and text()='%s']";
    private static final String companyRadioButton = "//div[@data-cy='multi-users-access-invite-user-data-role']//div[text()='%s']";
    private final String budgetPersonNameXpath = "//div[@data-cy='budget-person-name' and text()='%s']";
    private final String budgetMemberUserNameXpath = "//div[@data-cy='budget-member-item']//div[text()='%s']";
    // budget detail
    @FindBy(xpath = "//div[@data-cy='budget-details-modal-header-title']")
    private WebElement budgetDetailHeaderTitle;
    @FindBy(xpath = "//div[@data-cy='budget-details-modal-header-role-label']")
    private WebElement budgetDetailHeaderRoleLabel;
    @FindBy(xpath = "//div[@data-cy='budget-details-modal']//div[@data-cy='budget-spend-limit-bar-limit-amount']")
    private WebElement budgetDetailSummarySpentLimitAmountLabel;
    @FindBy(xpath = "//div[@data-cy='budget-summary-collapsible-button']/span")
    private WebElement budgetDetailSummaryShowDetailButton;
    @FindBy(xpath = "//div[@data-cy='budget-details-card-budget-frequency']")
    private WebElement budgetDetailSummaryFrequencyLabel;
    @FindBy(xpath = "//div[contains(@class,'budget-summary__source-of-fund')]//div[@class='ellipsis']")
    private WebElement budgetDetailSummarySourceOfFundLabel;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-analytics']")
    private WebElement budgetDetailAnalyticsViewAllButton;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-people']")
    private WebElement budgetDetailPeopleViewAllButton;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-transactions']")
    private WebElement budgetDetailTransactionsViewAllButton;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-cards']")
    private WebElement budgetDetailCardsViewAllButton;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-claims']")
    private WebElement budgetDetailClaimsViewAllButton;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-activity history']")
    private WebElement budgetDetailHistoryViewAllButton;
    //budget member
    @FindBy(xpath = "//div[@data-cy='budget-members-page-modal']")
    private WebElement budgetUserAccessSidePage;
    @FindBy(xpath = "//div[@role='tab'][descendant::div[text()='Budget owners']]")
    private WebElement budgetUserAccessBudgetOwnersTab;
    @FindBy(xpath = "//div[@role='tab'][descendant::div[text()='Budget members']]")
    private WebElement budgetUserAccessBudgetMembersTab;
    @FindBy(xpath = "//button[@data-cy='multi-users-access-invite-user-data-continue-button']")
    private WebElement sendInviteButton;
    // first time landing
    @FindBy(xpath = "//div[text()='Budgets']")
    private WebElement budgetTitleLabel;
    @FindBy(xpath = "//div[@class='budgets-explainer-page']")
    private WebElement explainerPage;
    @FindBy(xpath = "//div[@class='budgets-explainer-page']//button[contains(@class,'budgets-explainer-page')]")
    private WebElement nextExplainerButton;
    @FindBy(xpath = "//div[@class='budgets-explainer-page']//button[contains(@class,'budgets-explainer-page')][descendant::div[text()='Continue ']]")
    private WebElement continueExplainerButton;
    @FindBy(xpath = "//div[@data-cy='budgets-page-new-budget-button']")
    private WebElement newBudgetButton;
    // Low balance reminders
    @FindBy(xpath = "//div[contains(@class,'budget-low-balance-alert-banner__title')]")
    private WebElement lowBalanceRemindersTitle;
    @FindBy(xpath = "//div[contains(@class,'budget-low-balance-alert-banner')]/p[contains(@class,'text-subtitle2')]")
    private WebElement lowBalanceRemindersSubTitle;
    @FindBy(xpath = "//div[contains(@class,'budget-low-balance-alert-banner__cta')]")
    private WebElement updateBalanceButton;
    // search and filter
    @FindBy(xpath = "//input[@data-cy='example-filter-search-input']")
    private WebElement searchInput;
    @FindBy(xpath = "//button[@data-cy='filter-button-state_code']")
    private WebElement statusFilter;
    @FindBy(xpath = "//button[@data-cy='filter-button-spent_limit_approaching']")
    private WebElement spentFilter;
    @FindBy(xpath = "//button[@data-cy='filter-button-expiring_soon']")
    private WebElement expiringSoonFilter;
    @FindBy(xpath = "//button[@data-cy='filter-button-more_filters']")
    private WebElement moreFilter;
    @FindBy(xpath = "//div[@data-cy='budget-member-item-selected']//div[contains(@class,'text-secondary')]")
    private WebElement matchedUsers;
    @FindBy(xpath = "//input[@placeholder='Search by name or email']")
    private WebElement searchByNameOrEmail;
    // more filter side panel
    @FindBy(xpath = "//div[@data-cy='filter-modal-wrapper']")
    private WebElement moreFilterSidePanel;
    @FindBy(xpath = "//button[@data-cy='filter-modal-wrapper-header-reset-button']")
    private WebElement resetAllFilterButton;
    @FindBy(xpath = "//input[@data-cy='filter-remote-select-search-input']")
    private WebElement moreFilterSearchInput;
    @FindBy(xpath = "//div[@data-cy='filter-modal-wrapper-tabs-item-name']")
    private WebElement moreFilterItem;
    @FindBy(xpath = "//button[@data-cy='filter-modal-wrapper-apply-filter-button']")
    private WebElement moreFilterApplyButton;
    // table record
    @FindBy(xpath = "//div[@class='budgets-list-header-desktop']")
    private WebElement tableHeaderPanel;
    @FindBy(xpath = "//div[@data-cy='budgets-list-items-container']")
    private WebElement tableContentPanel;
    @FindBy(xpath = "//div[@data-cy='budgets-list-item-desktop']")
    private WebElement tableRecord;
    // new budget side pane
    @FindBy(xpath = "//span[@data-cy='create-budget-header-title']")
    private WebElement createHeaderTitle;
    @FindBy(xpath = "//input[@data-cy='budget-create-step-purpose-name-input']")
    private WebElement createBudgetNameInput;
    @FindBy(xpath = "//input[@data-cy='budget-create-step-purpose-description-input']")
    private WebElement createBudgetPurposeInput;
    @FindBy(xpath = "//div[@data-cy='budget-category-select-control']")
    private WebElement createBudgetIconSelect;
    @FindBy(xpath = "//input[@data-cy='budget-category-select-modal-search']")
    private WebElement createBudgetSearchIconInput;
    @FindBy(xpath = "//button[@data-cy='budget-create-step-purpose-continue-button']")
    private WebElement createBudgetPurposeContinueButton;
    @FindBy(xpath = "//input[@data-cy='budget-create-step-controls-amount-input-input-box-input']")
    private WebElement createBudgetSpentLimitAmountInput;
    @FindBy(xpath = "//div[@aria-label='One-time']")
    private WebElement createBudgetFrequencyOnetimeRadio;
    @FindBy(xpath = "//div[@aria-label='Recurring']")
    private WebElement createBudgetFrequencyRecurringRadio;
    @FindBy(xpath = "//input[@data-cy='budget-create-step-controls-expiry-date-step']")
    private WebElement createBudgetExpiryDateSelect;
    @FindBy(xpath = "//div[@data-cy='source-of-fund-select-label']/following-sibling::label")
    private WebElement createBudgetSourceOfFundSelect;
    @FindBy(xpath = "//button[@data-cy='budget-create-step-controls-continue-button']")
    private WebElement createBudgetContinueButton;
    @FindBy(xpath = "//input[@data-cy='budget-create-step-owners-search']")
    private WebElement createBudgetOwnerSearchInput;
    @FindBy(xpath = "//div[contains(@data-cy,'budget-member-item')]")
    private WebElement createBudgetOwnerList;
    @FindBy(xpath = "//div[contains(@class,'budget-transfer-rights-select-modal__option')][descendant::img[contains(@src,'customers.finance.transfer-only.svg')]]")
    private WebElement budgetOwnerTransferRightButton;
    @FindBy(xpath = "//div[contains(@class,'budget-transfer-rights-select-modal__option')][descendant::img[contains(@src,'no-transfer-or-submit-right')]]")
    private WebElement budgetOwnerNoTransferRightButton;
    @FindBy(xpath = "//button[@data-cy='budget-create-step-owners-submit-button']")
    private WebElement budgetCreateButton, budgetEditUpdateButton;
    @FindBy(xpath = "//button[@data-cy='budget-create-confirm-modal-confirm-button']")
    private WebElement budgetProceedButton;
    @FindBy(xpath = "//button[@data-cy='budget-create-step-success-submit']")
    private WebElement viewBudgetButton;
    @FindBy(xpath = "//img[@data-cy='budget-details-modal-header-actions-close-icon']")
    private WebElement budgetDetailCloseButton;
    @FindBy(xpath = "//div[@data-cy='quick-actions-add-people-action']")
    private WebElement addPeopleButton;
    @FindBy(xpath = "//div[@data-cy='add-budget-person-role-select-modal-option-owner']")
    private WebElement addBudgedOwnerButton;
    @FindBy(xpath = "//div[@data-cy='budget-members-select-modal-invite-user']")
    private WebElement inviteNewUserButton;
    // date picker
    @FindBy(xpath = "//div[@data-cy='example-date-picker']")
    private WebElement datePickerPanel;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-select-month-button']")
    private WebElement datePickerMonthButton;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-select-month-button']//span[@class='block']")
    private WebElement datePickerMonthLabel;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-select-year-button']")
    private WebElement datePickerYearButton;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-select-year-button']//span[@class='block']")
    private WebElement datePickerYearLabel;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-increment-month-button']")
    private WebElement datePickerIncrementMonthButton;
    @FindBy(xpath = "//button[@data-cy='example-date-picker-decrement-month-button']")
    private WebElement datePickerDecrementMonthButton;
    @FindBy(xpath = "//div[@data-cy='example-date-picker']//button[descendant::i[contains(@class,'fa-chevron-right')]]")
    private WebElement datePickerYearContentRightButton;
    @FindBy(xpath = "//div[@data-cy='example-date-picker']//button[descendant::i[contains(@class,'fa-chevron-left')]]")
    private WebElement datePickerYearContentLeftButton;
    // edit budget
    @FindBy(xpath = "//button[@data-cy='budget-details-modal-edit-cta']")
    private WebElement editBudgetButton;
    @FindBy(xpath = "//*[@data-cy='create-budget-step-controls-expiry-date-step']//img[@alt='Close']")
    private WebElement editBudgetClearExpiredDate;
    @FindBy(xpath = "data-cy='create-budget-step-owners-header-section'")
    private WebElement editBudgetOwnerHeader;
    // notify
    @FindBy(xpath = "//div[@id='q-notify']//div[@data-cy='q-notification']")
    private WebElement notifyPopup;
    //spinner
    @FindBy(xpath = "//*[contains(@class,'q-spinner')]")
    private WebElement spinner;
    @FindBy(xpath = "//input[@data-cy='budget-set-spend-limit-modal-amount-input-box-input']")
    private WebElement updateLimitInputBar;
    @FindBy(xpath = "//button[@data-cy='budget-set-spend-limit-modal-update-button']")
    private WebElement updateLimitButton;
    //invite user
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//input[@data-cy='multi-users-access-invite-user-data-full-name-input']")
    private WebElement fullNameField;
    @FindBy(xpath = "//input[@data-cy='multi-users-access-invite-user-data-email-input']")
    private WebElement emailField;
    @FindBy(xpath = "//div[@data-cy='add-budget-person-role-select-modal-option-owner']")
    private WebElement addNewBudgetOwnerOption;
    @FindBy(xpath = "//button[@data-cy='budget-members-select-modal-submit-button']")
    private WebElement assignBudgetPeopleButton;
    @FindBy(xpath = "//div[@data-cy='budget-detail-section-wrapper-view-all-people']")
    private WebElement viewAllBudgetPeopleButton;
    @FindBy(xpath = "//div[@data-cy='budget-people-group-person']")
    private List<WebElement> allBudgetPeopleList;
    @FindBy(xpath = "//div[@data-cy='budget-people-owners-group']")
    private WebElement budgetPeopleListSection;
    @FindBy(xpath = "//div[@data-cy='budget-role-modify-access']")
    private WebElement modifyAccessButton;
    @FindBy(xpath = "//div[@data-cy='budget-permission-update-modal-revoke-access-title']")
    private WebElement revokeAccessOption;
    @FindBy(xpath = "//button[@data-cy='budget-member-revoked-by-admin-modal-confirm-cta']")
    private WebElement revokeAccessButton;
    @FindBy(xpath = "//div[@data-cy='budget-role-permission-label']")
    private WebElement budgetPeopleNoAccessLabel;
    @FindBy(xpath = "//div[@data-cy='budget-people-members-group']")
    private WebElement budgetMemberListSection;
    @FindBy(xpath = "//div[@data-cy='budget-members-page-new-user-button']")
    private WebElement budgetPeopleNewUserButton;
    @FindBy(xpath = "//div[@data-cy='add-budget-person-role-select-modal-option-member']")
    private WebElement budgetMemberOption;
    @FindBy(xpath = "//div[@data-cy='budget-member-item']")
    private WebElement budgetMemberList;
    @FindBy(xpath = "//div[@data-cy='issue-card-option-modal-option-not-issue-card']/div")
    private WebElement noIssueCardOption;
    @FindBy(xpath = "//div[@role='tab' and @aria-selected='false']")
    private WebElement budgetMemberTab;
    @FindBy(xpath = "//div[@data-cy='budget-member-page-modal']//div[@data-cy='modal-page-back-or-close-button']")
    private WebElement budgetPeopleBackButton;
    @FindBy(xpath = "//div[@class='budget-people-group budget-members-page__group--active']")
    private WebElement budgetPeopleActiveLabel;
    //unfreeze Budget
    @FindBy(xpath = "//button[@data-cy='budget-details-modal-header-actions-menu-option-dropdown']")
    private WebElement menuOptionsDropdown;
    @FindBy(xpath = "//div[@data-cy='menu-option-dropdown-item']")
    private WebElement freezeBudgetButton;
    @FindBy(xpath = "//button[@data-cy='budget-deactivate-modal-freeze-budget-cta']")
    private WebElement freezeBudgetConfirmButton;
    @FindBy(xpath = "//div[@data-cy='q-notification']")
    private WebElement budgetStatusToastMessage;
    @FindBy(xpath = "//div[contains(@class,'deactivated-badge')]")
    private WebElement frozenBadge;
    @FindBy(xpath = "//button[contains(@class,'example-button--cta')]")
    private WebElement unfreezeButton;
    @FindBy(xpath = "//span[@data-cy='budget-create-header-title']")
    private WebElement pageHeaderTitle;
    @FindBy(xpath = "//button[contains(@data-cy,'continue-button')]")
    private WebElement continueButton;
    @FindBy(xpath = "//button[@data-cy='budget-create-step-owners-submit-button']")
    private WebElement unfreezeBudgetButton;

    public BudgetPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForElementToBeDisplayed(testConfig, tableRecord, "Budget List");
        AssertHelper.assertElementIsDisplayed(testConfig, "Budget Page", budgetTitleLabel);
    }

    public void inputBudgetName(String budgetName)
    {
        Element.enterData(testConfig, createBudgetNameInput, budgetName, "budget name");
    }

    public void selectBudgetIcon(String iconName)
    {
        Element.click(testConfig, createBudgetIconSelect, "Budget Icon", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetSearchIconInput, "Search Icon");
        Element.enterData(testConfig, createBudgetSearchIconInput, iconName, "Budget Icon name");
        WebElement category = Element.getPageElement(testConfig, How.xPath, String.format(budgetCategoryXpath, iconName));
        WaitHelper.waitForElementToBeDisplayed(testConfig, category, iconName + "category");
        Element.click(testConfig, category, iconName, false);
    }

    public void inputBudgetSpentLimitAmount(String amount)
    {
        Element.enterData(testConfig, createBudgetSpentLimitAmountInput, String.valueOf(amount), "Budget spent limit amount");
    }

    public void selectFrequency(SaasEnums.BudgetUiFrequency frequency)
    {
        String recurringFrequency = "//div[@role='option'][descendant::span[text()='%s']]";
        switch (frequency)
        {
            case OneTime -> Element.click(testConfig, createBudgetFrequencyOnetimeRadio, "One-time frequency", true);
            case RecurringMonthly, RecurringQuarterly ->
            {
                Element.click(testConfig, createBudgetFrequencyRecurringRadio, "Recurring frequency", true);
                Element.getPageElement(testConfig, How.xPath, String.format(recurringFrequency, frequency.getFrequencyName())).click();
            }
            default -> testConfig.logFail("Frequency is invalid: " + frequency);
        }
    }

    public void selectBudgetExpireDate(String date, String format)
    {
        //todo: https://weexample.atlassian.net/browse/ATTF-640
    }

    public void selectSourceOfFund(String sourceOfFund)
    {
        Element.click(testConfig, createBudgetSourceOfFundSelect, "SOF", true);
        WaitHelper.waitForElementToBeDisplayed(testConfig, By.xpath("//div[@role='listbox']"), sourceOfFund);
        WebElement elementSOF = Element.getPageElement(testConfig, How.xPath, String.format("//div[@role='listbox']//div[child::div[text()='%s']]", sourceOfFund));
        Element.click(testConfig, elementSOF, "SOF", true);
    }

    public void selectBudgetOwner(String username, String userRole, BudgetRole budgetRole)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetOwnerList, "Budget Owner selector");
        WebElement userElement = Element.getPageElement(testConfig, How.xPath, String.format(budgetOwnerUserXpath, username));
        String selected = Element.getAttributeText(testConfig, userElement, "data-cy", "data-cy");
        if (!selected.equals("budget-member-item-selected"))
        {
            Element.click(testConfig, userElement, username, true);
        }

        if (!userRole.equals("Admin"))
        {
            if (budgetRole == BudgetRole.OwnerTransfer)
            {
                WaitHelper.waitForElementToBeClickable(testConfig, budgetOwnerTransferRightButton, "Budget Owner selector");
                Element.click(testConfig, budgetOwnerTransferRightButton, "Transfer without approval", true);
            }
            if (budgetRole == BudgetRole.OwnerNoTransfer)
            {
                WaitHelper.waitForElementToBeClickable(testConfig, budgetOwnerNoTransferRightButton, "Budget Owner selector");
                Element.click(testConfig, budgetOwnerNoTransferRightButton, "No transfer rights", true);
            }
        }
        WaitHelper.waitForSeconds(testConfig, 3);
    }

    public void selectBudgetOwners(String admins, String budgetOwnerTransfers, String budgetOwnerNoTransfers)
    {
        if (admins != null)
        {
            for (String admin : admins.split("/"))
            {
                selectBudgetOwner(admin, "Admin", BudgetRole.OwnerTransfer);
            }
        }

        if (budgetOwnerTransfers != null)
        {
            for (String transfer : budgetOwnerTransfers.split("/"))
            {
                selectBudgetOwner(transfer, "NonAdmin", BudgetRole.OwnerTransfer);
            }
        }

        if (budgetOwnerNoTransfers != null)
        {
            for (String noTransfer : budgetOwnerNoTransfers.split("/"))
            {
                selectBudgetOwner(noTransfer, "NonAdmin", BudgetRole.OwnerNoTransfer);
            }
        }
    }

    public void clickProceedButton()
    {
        Element.click(testConfig, budgetProceedButton, "Proceed button", true);
    }

    public void clickViewBudgetButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, viewBudgetButton, "View Budget button");
        Element.click(testConfig, viewBudgetButton, "View Budget", true);
    }

    public void clickInviteNewUserButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, addPeopleButton, "Add People Button");
        Element.click(testConfig, addPeopleButton, "Add People Button", true);
        WaitHelper.waitForElementToBeDisplayed(testConfig, addBudgedOwnerButton, "Add Budged Owner Button");
        Element.click(testConfig, addBudgedOwnerButton, "Add Budged Owner Button", true);
        WaitHelper.waitForElementToBeDisplayed(testConfig, inviteNewUserButton, "Invite New User Button");
        Element.click(testConfig, inviteNewUserButton, "Invite New User Button", true);
    }

    private void clickOnCompanyRoleRadioButtonWithIndex(CompanyRole role)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        String formattedLocator = String.format(companyRadioButton, role.getRole());
        WebElement radioBtn = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeClickable(testConfig, radioBtn, "Company role button");
        Element.click(testConfig, radioBtn, "Company role", true);
    }

    public void createAndOpenDetailBudget(String budgetName)
    {
        createBudget();
        clickViewBudgetButton();
        clickCloseButton();
        searchBudgetByName(budgetName);
        clickFirstBudget();
    }

    public void verifyIfAddedUsersFound(String user)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        Element.enterData(testConfig, searchByNameOrEmail, user, "input user name");
        WaitHelper.waitForElementToBeClickable(testConfig, searchByNameOrEmail, "Search by name or email");
        AssertHelper.assertElementText(testConfig, "User name", user, matchedUsers);
    }

    public void clickOnSendInviteButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, sendInviteButton, "Send Invite Button");
        Element.click(testConfig, sendInviteButton, "Send Invite Button", true);
    }

    private void inputUserFullName(String value)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        Element.enterData(testConfig, fullNameField, value, "Full Name Field");
    }

    private void inputEmailField(String email)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        Element.enterData(testConfig, emailField, email, "Email field");
    }

    public void inviteBudgetUserByCompanyRole(String fullName, String email, CompanyRole companyRole)
    {
        inputUserFullName(fullName);
        inputEmailField(email);
        clickOnCompanyRoleRadioButtonWithIndex(companyRole);
        clickOnSendInviteButton();
    }

    public void clickSummaryShowDetails()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetDetailSummaryShowDetailButton, "Summary > Show detail");
        Element.click(testConfig, budgetDetailSummaryShowDetailButton, "Show detail", false);
    }

    public void verifyBudgetDetailHeader()
    {
        AssertHelper.assertElementText(testConfig, "Budget name", testConfig.testData.get("budgetName"), budgetDetailHeaderTitle);
        String roleLabel = Element.getText(testConfig, budgetDetailHeaderRoleLabel, "Role label");
        String userRole = testConfig.testData.get("userRole");
        if (userRole.contains(BudgetMemberType.Admin.getMemberType()))
        {
            roleLabel.matches("You.re Admin");
            testConfig.logComment("User role: " + roleLabel);
        } else
        {
            AssertHelper.assertElementText(testConfig, "Role label", userRole, budgetDetailHeaderRoleLabel);
        }
    }

    public void verifyBudgetDetailSummary(String budgetFrequencyFromApiResponse)
    {
        testConfig.logComment(Element.getText(testConfig, budgetDetailSummarySpentLimitAmountLabel, "Spent limit"));
        AssertHelper.assertElementText(testConfig, "Spend limit", testConfig.testData.get("sourceOfFund").substring(0, 3) + " " + String.format("%,.2f", Double.parseDouble(testConfig.testData.get("spendLimit"))), budgetDetailSummarySpentLimitAmountLabel);
        if (Element.getText(testConfig, budgetDetailHeaderTitle, "Budget name").contains("Edit"))
        {
            String budgetEditFrequency = null;
            if (budgetFrequencyFromApiResponse.equals("bullet"))
            {
                budgetEditFrequency = "One-time";
            } else if (budgetFrequencyFromApiResponse.equals("monthly"))
            {
                budgetEditFrequency = "Monthly";
            } else
            {
                budgetEditFrequency = "Quarterly";
            }
            AssertHelper.assertElementText(testConfig, "Frequency", budgetEditFrequency, budgetDetailSummaryFrequencyLabel);
        } else
        {
            AssertHelper.assertElementText(testConfig, "Frequency", testConfig.testData.get("frequency"), budgetDetailSummaryFrequencyLabel);
        }
        AssertHelper.assertElementText(testConfig, "Source of fund", testConfig.testData.get("sourceOfFund"), budgetDetailSummarySourceOfFundLabel);
    }

    public void verifyPeople()
    {
        Element.click(testConfig, budgetDetailPeopleViewAllButton, "People > View All");
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetUserAccessSidePage, "User and Access");
    }

    public void createBudget()
    {
        Element.click(testConfig, newBudgetButton, "New budget button", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetNameInput, "budget create screen");
        Element.enterData(testConfig, createBudgetNameInput, testConfig.testData.get("budgetName"), "budget name");

        if (testConfig.testData.get("purpose") != null)
        {
            Element.enterData(testConfig, createBudgetPurposeInput, testConfig.testData.get("purpose"), "budget purpose");
        }

        if (testConfig.testData.get("icon") != null)
        {
            selectBudgetIcon(testConfig.testData.get("icon"));
        }

        Element.click(testConfig, createBudgetPurposeContinueButton, "Continue on Purpose step", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetSourceOfFundSelect, "SOF", 10);
        Element.enterData(testConfig, createBudgetSpentLimitAmountInput, testConfig.testData.get("spendLimit"), "Budget spent limit amount");
        selectFrequency(SaasEnums.BudgetUiFrequency.getByName(testConfig.testData.get("frequency")));

        if (testConfig.testData.get("expiryDate") != null)
        {
            selectBudgetExpireDate(testConfig.testData.get("expiryDate"), "ddMMyyyy");
        }

        selectSourceOfFund(testConfig.testData.get("sourceOfFund"));
        Element.click(testConfig, createBudgetContinueButton, "Continue button to create budget", true);
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetOwnerList, "Budget owner list is displayed");
        selectBudgetOwners(testConfig.testData.get("budgetOwnerAdmin"), testConfig.testData.get("budgetOwnerTransfer"), testConfig.testData.get("budgetOwnerNoTransfer"));
        WaitHelper.waitForElementToBeClickable(testConfig, budgetCreateButton, "The budget create button clickable");
        Element.click(testConfig, budgetCreateButton, "Create budget button", true);
    }

    public void verifyAccountsListPage()
    {
        AssertHelper.assertElementText(testConfig, "The budget page show", Element.getText(testConfig, budgetTitleLabel, "Budget page label"), budgetTitleLabel);
    }

    public void verifyBudgetDetail(String budgetFrequencyFromApiResponse)
    {
        verifyBudgetDetailHeader();
        clickSummaryShowDetails();
        verifyBudgetDetailSummary(budgetFrequencyFromApiResponse);
    }

    public void clickFirstBudget()
    {
        Element.clickUntilNextElementIsLoaded(testConfig, tableRecord, "First budget", budgetDetailHeaderTitle);
    }

    public void editBudget()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, editBudgetButton, "Budget detail");
        Element.click(testConfig, editBudgetButton, "Edit button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetNameInput, "Budget name");

        if (testConfig.testData.get("budgetName") != null)
        {
            Element.clearDataWithBackSpace(testConfig, createBudgetNameInput, "Budget name");
            inputBudgetName(testConfig.testData.get("budgetName"));
        }

        if (testConfig.testData.get("purpose") != null)
        {
            Element.enterData(testConfig, createBudgetPurposeInput, testConfig.testData.get("purpose"), "budget purpose ");
        }

        if (testConfig.testData.get("icon") != null)
        {
            selectBudgetIcon(testConfig.testData.get("icon"));
        }

        Element.click(testConfig, createBudgetPurposeContinueButton, "Continue on Purpose step", true);
        WaitHelper.waitForElementToBeDisplayed(testConfig, createBudgetSourceOfFundSelect, "Source of fund");

        if (testConfig.testData.get("spendLimit") != null)
        {
            Element.click(testConfig, createBudgetSpentLimitAmountInput, "Budget limit");
            Element.clearDataWithBackSpace(testConfig, createBudgetSpentLimitAmountInput, "Budget limit");
            inputBudgetSpentLimitAmount(testConfig.testData.get("spendLimit"));
        }

        if (testConfig.testData.get("expiryDate") != null)
        {
            selectBudgetExpireDate(testConfig.testData.get("expiryDate"), "ddMMyyyy");
        } else
        {
            if (Element.isElementDisplayed(testConfig, editBudgetClearExpiredDate))
            {
                Element.click(testConfig, editBudgetClearExpiredDate, "Clear Expired Date");
            }
        }

        Element.click(testConfig, createBudgetContinueButton, "Continue button to create budget", false);
        selectBudgetOwners(testConfig.testData.get("budgetOwnerAdmin"), testConfig.testData.get("budgetOwnerTransfer"), testConfig.testData.get("budgetOwnerNoTransfer"));
        WaitHelper.waitForPageLoad(testConfig, budgetCreateButton, 3);
        Element.click(testConfig, budgetCreateButton, "Create budget button", false);
    }

    public void verifyBudgetUpdateNotify()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, notifyPopup, "Budget Updated");
        AssertHelper.assertElementText(testConfig, "Budget notify", "Budget updated!", notifyPopup);
        WaitHelper.waitForElementToBeHidden(testConfig, notifyPopup, "Budget Updated Notify");
    }

    public void clickCloseButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetDetailCloseButton, "Close button");
        Element.click(testConfig, budgetDetailCloseButton, "Close button", true);
    }

    private void setBudgetSpendLimit()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetDetailHeaderTitle, "Budget detail header");
        WebElement updateLimitAction = Element.getPageElement(testConfig, How.xPath, String.format(budgetQuickActionsXpath, AccessEnums.BudgetQuickActions.UpdateLimit.getName()));
        Element.click(testConfig, updateLimitAction, "Update limit action", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, updateLimitInputBar, "Update Limit Input bar");
        Element.enterData(testConfig, updateLimitInputBar, testConfig.testData.get("spendLimit"), "Spend limit");
        WaitHelper.waitForElementToBeClickable(testConfig, updateLimitButton, "Update limit button");
        Element.click(testConfig, updateLimitButton, "Update limit button", false);
    }

    private void verifyBudgetSpendLimitIsUpdated()
    {
        WaitHelper.waitForElementToBeHidden(testConfig, updateLimitButton, "Update limit button");
        WaitHelper.waitForSeconds(testConfig, 3);
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetDetailSummarySpentLimitAmountLabel, "Budget detail summary spent limit amount label");
        AssertHelper.assertElementText(testConfig, "Spend limit", testConfig.testData.get("sourceOfFund").substring(0, 3) + " " + String.format("%,.2f", Double.parseDouble(testConfig.testData.get("spendLimit"))), budgetDetailSummarySpentLimitAmountLabel);
        verifyBudgetLimitUpdateNotify();
    }

    public void verifyBudgetLimitUpdateNotify()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, notifyPopup, "Budget Limit Updated");
        AssertHelper.assertElementText(testConfig, "Budget notify", "Spend limit updated", notifyPopup);
        WaitHelper.waitForElementToBeHidden(testConfig, notifyPopup, "Budget Limit Updated Notify");
    }

    private void searchBudgetByName(String budgetName)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, searchInput, "Search budget bar");
        Element.enterData(testConfig, searchInput, budgetName, "Search budget");
        WaitHelper.waitForElementToBeDisplayed(testConfig, tableRecord, "Budget List");
    }

    public void setAndVerifyNewBudgetSpendLimit(String budgetName)
    {
        searchBudgetByName(budgetName);
        clickFirstBudget();
        setBudgetSpendLimit();
        verifyBudgetSpendLimitIsUpdated();
    }

    public void updateBudgetAndVerifyNewUpdatedBudget(String budgetFrequencyFromApiResponse, String budgetName)
    {
        searchBudgetByName(budgetName);
        clickFirstBudget();
        editBudget();
        verifyBudgetUpdateNotify();
        verifyBudgetDetail(budgetFrequencyFromApiResponse);
    }

    private void addAndVerifyBudgetOwnersAreAdded(String budgetName)
    {
        searchBudgetByName(budgetName);
        clickFirstBudget();
        addNewBudgetOwners();
        verifyAddBudgetPeopleNotifyPopup();
        clickViewAllBudgetPeople();
        verifyNewBudgetOwnersAreAdded();
    }

    public void addAndVerifyBudgetPeopleAreAdded(BudgetRole budgetRole, String budgetName)
    {
        switch (budgetRole)
        {
            case Owner:
                addAndVerifyBudgetOwnersAreAdded(budgetName);
                break;
            case Member:
                addAndVerifyBudgetMembersAreAdded();
                break;
            default:
                testConfig.logComment("The inputted role is invalid");
                break;
        }
    }

    private void addNewBudgetOwners()
    {
        WebElement addPeopleQuickAction = Element.getPageElement(testConfig, How.xPath, String.format(budgetQuickActionsXpath, AccessEnums.BudgetQuickActions.AddPeople.getName()));
        WaitHelper.waitForElementToBeClickable(testConfig, addPeopleQuickAction, "Add people");
        Element.click(testConfig, addPeopleQuickAction, "Add people quick action");
        WaitHelper.waitForElementToBeClickable(testConfig, addNewBudgetOwnerOption, "Add new BO Option");
        Element.click(testConfig, addNewBudgetOwnerOption, "Add new BO Option");
        selectBudgetOwners(null, testConfig.testData.get("budgetOwnerTransfer"), testConfig.testData.get("budgetOwnerNoTransfer"));
        WaitHelper.waitForElementToBeClickable(testConfig, assignBudgetPeopleButton, "Assign budget people button");
        Element.click(testConfig, assignBudgetPeopleButton, "Assign budget people button");
    }

    private void verifyAddBudgetPeopleNotifyPopup()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, notifyPopup, "Add budget people");
        AssertHelper.assertElementText(testConfig, "Add budget people", "User(s) have been added to budget!", notifyPopup);
        WaitHelper.waitForElementToBeHidden(testConfig, notifyPopup, "Add budget people");
    }

    private void clickViewAllBudgetPeople()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, viewAllBudgetPeopleButton, "View all budget people button");
        Element.click(testConfig, viewAllBudgetPeopleButton, "View all budget people button", true);
    }

    private void verifyNewBudgetOwnersAreAdded()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetPeopleListSection, "All budget people list");
        String addedBudgetOwnerTransfers = testConfig.testData.get("budgetOwnerTransfer");
        if (addedBudgetOwnerTransfers != null)
        {
            for (String budgetOwnerTransfer : addedBudgetOwnerTransfers.split("/"))
            {
                AssertHelper.assertPartialElementTextList(testConfig, "New Bo transfer is added into budget people list", budgetOwnerTransfer, allBudgetPeopleList);
            }
        }
        String addedBudgetOwnerNoTransfers = testConfig.testData.get("budgetOwnerNoTransfer");
        if (addedBudgetOwnerNoTransfers != null)
        {
            for (String budgetOwnerNoTransfer : addedBudgetOwnerNoTransfers.split("/"))
            {
                AssertHelper.assertPartialElementTextList(testConfig, "New Bo no transfer is added into budget people list", budgetOwnerNoTransfer, allBudgetPeopleList);
            }
        }
    }

    private void removeAndVerifyBudgetOwnersAreRemoved()
    {
        removeBudgetOwnerNoTransfer();
        verifyBudgetPersonIsRemovedFromTheBudget();
        clickOnBudgetPeopleBackButton();
        removeBudgetOwnerTransfer();
        verifyBudgetPersonIsRemovedFromTheBudget();
    }

    private void removeBudgetOwnerTransfer()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetPeopleActiveLabel, "Budget people active label");
        String addedBudgetOwnerTransfers = testConfig.testData.get("budgetOwnerTransfer");
        if (addedBudgetOwnerTransfers != null)
        {
            for (String budgetOwnerTransfer : addedBudgetOwnerTransfers.split("/"))
            {
                WebElement budgetOwnerTransferOnList = Element.getPageElement(testConfig, How.xPath, String.format(budgetPersonNameXpath, budgetOwnerTransfer));
                Element.click(testConfig, budgetOwnerTransferOnList, "Budget owner transfer on list");
                revokeBudgetPeopleAccess();
            }
        }
    }

    private void removeBudgetOwnerNoTransfer()
    {
        String addedBudgetOwnerTransfers = testConfig.testData.get("budgetOwnerNoTransfer");
        if (addedBudgetOwnerTransfers != null)
        {
            for (String budgetOwnerTransfer : addedBudgetOwnerTransfers.split("/"))
            {
                WebElement budgetOwnerNoTransferOnList = Element.getPageElement(testConfig, How.xPath, String.format(budgetPersonNameXpath, budgetOwnerTransfer));
                Element.click(testConfig, budgetOwnerNoTransferOnList, "Budget owner no transfer on list");
                revokeBudgetPeopleAccess();
            }
        }
    }

    private void revokeBudgetPeopleAccess()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, modifyAccessButton, "Modify access button");
        Element.click(testConfig, modifyAccessButton, "Modify access button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, revokeAccessOption, "Revoke access option");
        Element.click(testConfig, revokeAccessOption, "Revoke access option");
        WaitHelper.waitForElementToBeDisplayed(testConfig, revokeAccessButton, "Revoke access button");
        Element.click(testConfig, revokeAccessButton, "Revoke access button");
    }

    private void verifyBudgetPersonIsRemovedFromTheBudget()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, notifyPopup, "Access revoked");
        AssertHelper.assertElementText(testConfig, "Access revoked", "Access revoked", notifyPopup);
        WaitHelper.waitForElementToBeHidden(testConfig, notifyPopup, "Access revoked");
        AssertHelper.assertElementText(testConfig, "No access label", "No access", budgetPeopleNoAccessLabel);
    }

    private void addAndVerifyBudgetMembersAreAdded()
    {
        clickOnBudgetPeopleBackButton();
        clickOnBudgetMemberTab();
        addNewBudgetMembers();
        verifyAddBudgetPeopleNotifyPopup();
        verifyNewBudgetMembersAreAdded();
    }

    private void clickOnBudgetPeopleBackButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetPeopleBackButton, "Budget people back button");
        Element.click(testConfig, budgetPeopleBackButton, "Budget people back button");
    }

    private void verifyNewBudgetMembersAreAdded()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetMemberListSection, "All budget people list");
        String addedBudgetMembers = testConfig.testData.get("budgetMember");
        if (addedBudgetMembers != null)
        {
            for (String budgetMember : addedBudgetMembers.split("/"))
            {
                AssertHelper.assertPartialElementTextList(testConfig, "New Budget member is added into budget people list", budgetMember, allBudgetPeopleList);
            }
        }
    }

    private void addNewBudgetMembers()
    {
        Element.click(testConfig, budgetPeopleNewUserButton, "Budget people new user button");
        WaitHelper.waitForElementToBeClickable(testConfig, budgetMemberOption, "Budget member option");
        Element.click(testConfig, budgetMemberOption, "Budget member option");
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetMemberList, "Budget member list");
        String budgetMembers = testConfig.testData.get("budgetMember");
        if (budgetMembers != null)
        {
            for (String budgetMember : budgetMembers.split("/"))
            {
                WebElement selectedBudgetMember = Element.getPageElement(testConfig, How.xPath, String.format(budgetOwnerUserXpath, budgetMember));
                Element.click(testConfig, selectedBudgetMember, "selected budget member");
                WaitHelper.waitForElementToBeClickable(testConfig, noIssueCardOption, "No issue card option");
                Element.click(testConfig, noIssueCardOption, "No issue card option");
            }
            WaitHelper.waitForElementToBeClickable(testConfig, assignBudgetPeopleButton, "Assign budget people button");
            Element.click(testConfig, assignBudgetPeopleButton, "Assign budget people button");
        }

    }

    private void clickOnBudgetMemberTab()
    {
        Element.click(testConfig, budgetMemberTab, "Budget member tab");
    }

    private void removeAndVerifyBudgetMembersAreRemoved()
    {
        String addedBudgetMembers = testConfig.testData.get("budgetMember");
        if (addedBudgetMembers != null)
        {
            for (String budgetMember : addedBudgetMembers.split("/"))
            {
                WaitHelper.waitForElementToBeDisplayed(testConfig, budgetPeopleActiveLabel, "Budget people active label");
                WebElement budgetMemberOnList = Element.getPageElement(testConfig, How.xPath, String.format(budgetPersonNameXpath, budgetMember));
                Element.click(testConfig, budgetMemberOnList, "Budget member on list");
                revokeBudgetPeopleAccess();
                verifyBudgetPersonIsRemovedFromTheBudget();
                clickOnBudgetPeopleBackButton();
            }
        }
    }

    public void removeAndVerifyBudgetPeopleAreRemoved(BudgetRole budgetRole)
    {
        switch (budgetRole)
        {
            case Owner:
                removeAndVerifyBudgetOwnersAreRemoved();
                break;
            case Member:
                removeAndVerifyBudgetMembersAreRemoved();
                break;
            default:
                testConfig.logComment("The inputted role is invalid");
                break;
        }
    }

    public void freezeBudgetAndVerify()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, viewBudgetButton, "view budget");
        Element.click(testConfig, viewBudgetButton, "view budget", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, menuOptionsDropdown, "menu option");
        Element.click(testConfig, menuOptionsDropdown, "menu option", false);
        Element.click(testConfig, freezeBudgetButton, "freeze button", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, freezeBudgetConfirmButton, "freeze budget button");
        Element.click(testConfig, freezeBudgetConfirmButton, "freeze budget");
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetStatusToastMessage, "freeze confirm message");
        AssertHelper.assertElementText(testConfig, "toast message", "Budget has been frozen", budgetStatusToastMessage);
        AssertHelper.assertElementText(testConfig, "frozen badge", "Frozen", frozenBadge);
    }

    public void unfreezeBudgetAndVerify()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, unfreezeButton, "unfreeze button");
        Element.click(testConfig, unfreezeButton, "unfreeze button", false);
        AssertHelper.assertElementText(testConfig, "page header", "Unfreeze budget", pageHeaderTitle);
        Element.click(testConfig, continueButton, "continue", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, continueButton, "continue");
        Element.click(testConfig, continueButton, "continue", false);
        WaitHelper.waitForElementToBeHidden(testConfig, budgetStatusToastMessage, "toast message hidden");
        Element.click(testConfig, unfreezeBudgetButton, "unfreeze budget button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, budgetStatusToastMessage, "toast message");
        AssertHelper.assertElementText(testConfig, "unfrozen budget", "Budget has been unfrozen", budgetStatusToastMessage);
    }
}
