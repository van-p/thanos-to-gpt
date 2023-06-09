package Automation.Access.customer.web;

import Automation.Access.customer.helpers.AccessEnums.AccessRole;
import Automation.Access.customer.helpers.AccessEnums.CompanyRole;
import Automation.Access.customer.helpers.AccessEnums.FinanceRights;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Utils.*;
import Automation.Utils.Element.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static Automation.Access.customer.helpers.AccessEnums.AccessRole.Admin;

public class UserAccessPage
{

    private static final String fullNameField = "(//div[@data-cy='user-invite-form-item'])[%s]//input[@data-cy='user-invite-form-item-full-name-input']";
    private static final String emailField = "(//div[@data-cy='user-invite-form-item'])[%s]//input[@data-cy='user-invite-form-item-email-input']";
    private static final String accessRoleDropdown = "(//div[@data-cy='user-invite-form-item'])[%s]//div[@data-cy='user-invite-form-item-role-label']/parent::div/label";
    private static final String roleOption = "//div[@role='listbox']//div[@class='q-virtual-scroll__content']//div[@class='q-item__label' and descendant::text()='%s']";
    private static final String companyRadioButton = "(//div[@data-cy='user-invite-form-item'])[%s]//div[@data-cy='user-invite-form-item-registration-role-radio']//div[@aria-label='%s']";
    private final String idNumberField = "(//div[@data-cy='user-invite-form-item'])[%s]//input[@data-cy='user-invite-form-item-nric-input']";
    private final String userList = "//div[@data-cy='users-list-item-%s']/div[@data-cy='users-list-item-desktop-full-name']";
    private final String accessRole = "//div[@data-cy='users-access-invite-step-role-group-item']//div[text()='%s']";
    private final String role = "(//div[@data-cy='users-list-item-desktop-role'])[%s]";
    @FindBy(css = "button[data-cy='multi-users-access-page-header-primary-button']")
    private WebElement newUserButton;
    @FindBy(css = "div[data-cy='multi-users-access-invite-user-data-title']")
    private WebElement inviteUserFormTitle;
    @FindBy(css = "button[data-cy='user-access-bulk-invite-form-add-more']")
    private WebElement addMoreButton;
    @FindBy(css = "button[data-cy='user-access-bulk-invite-form-continue-button']")
    private WebElement sendInviteButton;
    @FindBy(css = "button[data-cy='user-access-bulk-invite-success-done-button']")
    private WebElement doneButton;
    @FindBy(css = "input[data-cy='example-filter-search-input']")
    private WebElement searchUserField;
    @FindBy(css = "div[data-cy='users-list'] div[data-cy*='users-list-item-desktop-full-name']")
    private WebElement matchedUsers;
    @FindBy(css = "img[src='img/settings/multi-users-access/roles/customers.director.svg']")
    private WebElement adminIcon;
    @FindBy(css = "img[src='img/settings/multi-users-access/roles/customers.finance.svg']")
    private WebElement financeIcon;
    @FindBy(css = "img[src='img/settings/multi-users-access/roles/customers.finance.transfer-only.svg']")
    private WebElement financeTransferIcon;
    @FindBy(css = "img[src='img/settings/multi-users-access/roles/customers.finance.submit-only.svg']")
    private WebElement financeSubmitIcon;
    @FindBy(css = "img[src='img/settings/multi-users-access/roles/customers.finance.no-transfer-or-submit-right.svg']")
    private WebElement financeViewIcon;
    @FindBy(css = "img[src='img/settings/multi-users-access/roles/customers.card-only.svg']")
    private WebElement employeeIcon;
    @FindBy(css = "input[name='nric']")
    private List<WebElement> nationalIdFields;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "div[data-cy='multi-users-access-page-header-title']")
    private WebElement userAccessPageTitle;
    @FindBy(css = "button.q-field__focusable-action")
    private WebElement clearButton;
    @FindBy(css = "div[data-cy='user-access-permission-modify-access']")
    private WebElement modifyAccessButton;

    @FindBy(xpath = "//div[text()='Update access role']")
    private WebElement updateAccessRoleButton;

    @FindBy(xpath = "//div[@data-cy='users-access-invite-step-role-title']")
    private WebElement updateRoleLabel;

    @FindBy(css = "div[class='q-notification__message col']")
    private WebElement toastMessage;

    private final Config testConfig;

    public UserAccessPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, newUserButton);
        verifyUserAccessPage();
    }

    private void verifyUserAccessPage()
    {
        AssertHelper.assertElementText(testConfig, "User and Access", AccessHelper.accessStaticDataBase.getUserAccessPageTitle(), userAccessPageTitle);
        WaitHelper.waitForElementToBeDisplayed(testConfig, searchUserField, "Search user");
        AssertHelper.assertElementIsDisplayed(testConfig, "Search user", searchUserField);
    }

    private List<String> generateRandomListOfUserNames()
    {
        int number = DataGenerator.generateRandomNumberInIntRange(1, 5);
        List<String> usersList = new ArrayList<>();
        IntStream.range(0, number).forEach(
                i -> usersList.add("User " + DataGenerator.generateRandomString(15))
        );
        return usersList;
    }

    public void clickOnNewUserButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, this.newUserButton, "New User(s) button");
        Element.click(testConfig, this.newUserButton, "New User(s) button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, inviteUserFormTitle, "Invite User Form title");
    }

    private UserAccessPage inputUserFullName(int index, String value, String description)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        String formattedLocator = String.format(fullNameField, index);
        WebElement nameField = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeDisplayed(testConfig, nameField, description);
        Element.enterData(testConfig, Objects.requireNonNull(nameField), value, description);
        return this;
    }

    private UserAccessPage inputEmailField(int index, String value, String description)
    {
        String formattedLocator = String.format(emailField, index);
        WebElement email = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeDisplayed(testConfig, email, description);
        Element.enterData(testConfig, email, value, description);
        return this;
    }

    private void clickOnAddMoreButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, this.addMoreButton, "Add more button");
        Element.click(testConfig, this.addMoreButton, "Add More button", true);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    private UserAccessPage clickOnCompanyRoleRadioButtonWithIndex(int index, CompanyRole role)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        String formattedLocator = String.format(companyRadioButton, index, role.getRole());
        WebElement radioBtn = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeClickable(testConfig, radioBtn, "Company role button");
        Element.click(testConfig, radioBtn, "Company role", true);
        return this;
    }

    private UserAccessPage clickOnAccessRoleDropdownWithIndex(int index)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        String formattedLocator = String.format(accessRoleDropdown, index);
        WebElement accessDropdown = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeClickable(testConfig, accessDropdown, "Access role dropdown");
        Element.click(testConfig, accessDropdown, "Access role dropdown", true);
        return this;
    }

    private UserAccessPage clickOnAccessRoleOption(AccessRole accessRole)
    {
        switch (accessRole)
        {
            case Admin ->
            {
                WaitHelper.waitForElementToBeClickable(testConfig, adminIcon, Admin.getRole());
                Element.click(testConfig, adminIcon, Admin.getRole(), true);
                WaitHelper.waitForElementToBeHidden(testConfig, adminIcon, Admin.getRole());
            }
            case Finance ->
            {
                WaitHelper.waitForElementToBeClickable(testConfig, financeIcon, AccessRole.Finance.getRole());
                Element.click(testConfig, financeIcon, AccessRole.Finance.getRole(), true);
                clickOnFinanceRightOption();
            }
            case Employee ->
            {
                WaitHelper.waitForElementToBeClickable(testConfig, employeeIcon, AccessRole.Employee.getRole());
                Element.click(testConfig, employeeIcon, AccessRole.Employee.getRole(), true);
                WaitHelper.waitForElementToBeHidden(testConfig, employeeIcon, AccessRole.Employee.getRole());
            }
            default -> testConfig.logFail("Access role " + accessRole + " is not defined");
        }
        return this;
    }

    private UserAccessPage clickOnFinanceRightOption()
    {
        FinanceRights financeRights = FinanceRights.getRandomFinanceRights();
        switch (financeRights)
        {
            case FinanceTransferRights ->
            {
                WaitHelper.waitForElementToBeClickable(testConfig, financeTransferIcon, FinanceRights.FinanceTransferRights.getRights());
                Element.click(testConfig, financeTransferIcon, FinanceRights.FinanceTransferRights.getRights(), true);
                WaitHelper.waitForElementToBeHidden(testConfig, financeTransferIcon, FinanceRights.FinanceTransferRights.getRights());
            }
            case FinanceSubmitRights ->
            {
                WaitHelper.waitForElementToBeClickable(testConfig, financeSubmitIcon, FinanceRights.FinanceSubmitRights.getRights());
                Element.click(testConfig, financeSubmitIcon, FinanceRights.FinanceSubmitRights.getRights(), true);
                WaitHelper.waitForElementToBeHidden(testConfig, financeSubmitIcon, FinanceRights.FinanceSubmitRights.getRights());
            }
            case FinanceViewRights ->
            {
                WaitHelper.waitForElementToBeClickable(testConfig, financeViewIcon, FinanceRights.FinanceViewRights.getRights());
                Element.click(testConfig, financeViewIcon, FinanceRights.FinanceViewRights.getRights(), true);
                WaitHelper.waitForElementToBeHidden(testConfig, financeViewIcon, FinanceRights.FinanceViewRights.getRights());
            }
            default -> testConfig.logFail("Finance right " + financeRights + " is not defined");
        }
        return this;
    }

    private void enterIdNumber(int index)
    {
        String formattedLocator = String.format(idNumberField, index);
        WebElement element = Element.getPageElementWithRetry(testConfig, How.xPath, formattedLocator);
        if (Objects.nonNull(element))
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, element, "Id number field");
            Element.enterData(testConfig, element, DataGenerator.generateRandomAlphaNumericString(13), "Enter value to id number field");
        }
    }

    public List<String> inviteUsersWithCompanyRoleAndAccessRole(CompanyRole role, AccessRole accessRole)
    {
        List<String> userList = generateRandomListOfUserNames();
        if (userList.isEmpty())
        {
            testConfig.logFail("Failed to generate list of user names! Please check again");
        } else
        {
            for (int i = 0; i < userList.size(); i++)
            {
                this.clickOnCompanyRoleRadioButtonWithIndex(i + 1, role)
                        .inputUserFullName(i + 1, userList.get(i), "Full name")
                        .inputEmailField(i + 1, DataGenerator.generateRandomAlphaNumericString(5) + "." + DataGenerator.generateRandomAlphaNumericString(5) + "@example.com", "Email field")
                        .clickOnAccessRoleDropdownWithIndex(i + 1)
                        .clickOnAccessRoleOption(accessRole)
                        .enterIdNumber(i + 1);
                if (i < userList.size() - 1)
                {
                    clickOnAddMoreButton();
                }
            }
        }
        return userList;
    }

    public void clickOnSendInviteButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, this.sendInviteButton, "Send Invite button");
        Element.click(testConfig, this.sendInviteButton, "Send Invite button", true);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public void clickOnDoneButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.doneButton, "Done button");
        Element.click(testConfig, this.doneButton, "Done button", false);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public void selectUser(int userNumber)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.newUserButton, "New User button");
        String formattedLocator = String.format(userList, userNumber);
        WebElement user = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeClickable(testConfig, user, "User row");
        Element.click(testConfig, user, "User row", false);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public String getUserName(int userNumber)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.newUserButton, "New User button");
        String formattedLocator = String.format(userList, userNumber);
        return Element.getPageElement(testConfig, How.xPath, formattedLocator).getText();
    }

    public String updateRole(int userNumber)
    {
        String updatedRole = null;
        String formattedRoleLocator = String.format(role, userNumber + 1);
        String role = Element.getPageElement(testConfig, How.xPath, formattedRoleLocator).getText();
        WaitHelper.waitForElementToBeClickable(testConfig, updateRoleLabel, "Update Role Label");
        String formattedLocator = null;
        switch (role.toUpperCase())
        {
            case "ADMIN", "FINANCE":
                formattedLocator = String.format(accessRole, AccessRole.Employee.getRole());
                updatedRole = AccessRole.Employee.getRole();
                break;
            case "EMPLOYEE":
                formattedLocator = String.format(accessRole, AccessRole.Admin.getRole());
                updatedRole = AccessRole.Admin.getRole();
                break;
            default:
                testConfig.logFail("Incorrect role, Pls check");
        }

        WebElement accessRole = Element.getPageElement(testConfig, How.xPath, formattedLocator);
        WaitHelper.waitForElementToBeClickable(testConfig, accessRole, "User role");
        Element.click(testConfig, accessRole, "User role", false);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        return updatedRole;
    }

    public void clickOnModifyAccessButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.modifyAccessButton, "Modify Access button");
        Element.click(testConfig, this.modifyAccessButton, "Modify Access button", false);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public void clickOnUpdateAccessRoleButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, this.updateAccessRoleButton, "Update Access Role button");
        Element.click(testConfig, this.updateAccessRoleButton, "Update Access Role button", false);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public void verifyIfAddedUsersFound(List<String> users)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, searchUserField, "Search user field");
        for (String user : users)
        {
            Element.enterData(testConfig, searchUserField, user, "Search user field");
            WaitHelper.waitForElementToBeClickable(testConfig, clearButton, "Clear button");
            WaitHelper.waitForElementToBeDisplayed(testConfig, this.matchedUsers, "User name");
            AssertHelper.assertElementText(testConfig, "User name", user, this.matchedUsers);
            Element.clearDataWithBackSpace(testConfig, this.searchUserField, "Search user field");
            WaitHelper.waitForElementToBeHidden(testConfig, clearButton, "Clear button");

        }
    }

    public void verifySendInviteButtonDisabledForFormError()
    {
        clickOnCompanyRoleRadioButtonWithIndex(1, CompanyRole.Director);
        inputUserFullName(1, "", "Full name");
        inputEmailField(1, DataGenerator.generateRandomAlphaNumericString(5) + "." + DataGenerator.generateRandomAlphaNumericString(5) + "@example.com", "Email field");
        AssertHelper.assertElementNotEnabled(testConfig, "Send Invite button", sendInviteButton);
    }

    public void verifyUpdateRoleToastMessage(int userNumber)
    {
        String expectedMessage = getUserName(userNumber) + "’s account access updated";
        AssertHelper.compareEquals(testConfig, "Toast Message Text", expectedMessage, toastMessage.getText());
    }

    public void verifyIfRoleUpdated(int userNumber, String updatedRole)
    {
        String formattedRoleLocator = String.format(role, userNumber + 1);
        String role = Element.getPageElement(testConfig, How.xPath, formattedRoleLocator).getText();
        AssertHelper.compareEquals(testConfig, "Updated Role", updatedRole, role);
    }
}