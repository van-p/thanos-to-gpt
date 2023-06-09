package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasEnums.CardColor;
import Automation.SaaS.customer.helpers.SaasEnums.CardTypeEnum;
import Automation.SaaS.customer.helpers.SaasEnums.PersonTypeEnum;
import Automation.Utils.*;
import Automation.Utils.Element.How;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Objects;

public class CardPage
{

    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    // User list to issue
    @FindBy(xpath = "//div[@data-cy='user-item']//div[contains(@class,'text-h6')]")
    private List<WebElement> customerDebitCardUserActiveList;
    @FindBy(xpath = "//div[@data-cy='user-item-pending']//div[contains(@class,'text-h6')]")
    private List<WebElement> customerDebitCardUserPendingList;
    @FindBy(xpath = "//div[@data-cy='cards-page-header-title']")
    private WebElement customerDebitCardTitle;
    @FindBy(xpath = "//div[@data-cy='cards-list-groups-card']")
    private WebElement customerDebitCardAllCompanyCardsRecords;
    private final String customerDebitCardHolderName = "//div[@data-cy='user-item']//div[text()=\"%s\"]";

    // Select card type format
    @FindBy(xpath = "//div[@class='card-field-wrapper']/div[1]")
    private WebElement customerDebitCardCardFormatLabel;
    @FindBy(xpath = "//div[@name='edit']//div[@data-cy='card-format-item'][2]")
    private WebElement customerDebitCardCardFormatSelectionGreenCardCheckbox;
    @FindBy(xpath = "//button[@data-cy='issue-card-step-card-type-cta-continue']")
    private WebElement customerDebitCardCardFormatSelectionContinueButton;
    @FindBy(xpath = "//div[@data-cy='modal-page-back-or-close-button']")
    private WebElement customerDebitCardCardFormatSelectionCloseButton;

    // Select user to issue
    @FindBy(xpath = "//div[@data-cy='user-item']")
    private WebElement customerDebitCardUsersToIssueItems;
    @FindBy(xpath = "//div[@data-cy='issue-card-step-user-header']")
    private WebElement customerDebitCardUsersToIssueTitle;
    @FindBy(xpath = "//div[@data-cy='issue-card-source-of-fund-select-wrapper']")
    private WebElement customerDebitCardIssueDebitCardCardInfoSourceOfFundDropdown;
    private final String customerDebitCardIssueDebitCardCardInfoSourceOfFundListValue = "//div[@data-cy='card-account-select-modal-information']/div[1]";

    // Input card info input fields
    @FindBy(xpath = "//input[@data-cy='issue-card-name-description-name-input-input']")
    private WebElement customerDebitCardIssueDebitCardCardInfoCardNameTextBox;
    @FindBy(xpath = "//input[@data-cy='issue-card-name-description-purpose-input-input']")
    private WebElement customerDebitCardIssueDebitCardCardInfoCardPurposeTextBox;

    // Category section
    @FindBy(xpath = "//div[@data-cy='issue-card-step-info-select-category']")
    private WebElement customerDebitCardIssueDebitCardCardInfoCategoryDropdown;
    private final String customerDebitCardIssueDebitCardCardInfoCategoryListValue = "//div[contains(@data-cy,'legacy-transaction-category-select-option') and not(contains(@data-cy,'legacy-transaction-category-select-option-add-category'))]";

    // Card color
    private final String customerDebitCardIssueDebitCardCardInfoCardColorIcon = "//div[@data-cy='card-color-select-option-item'][child::div[contains(@style, '%s')]]";
    @FindBy(xpath = "//button[@data-cy='issue-card-step-color-continue-cta']")
    private WebElement customerDebitCardIssueDebitCardCardInfoIssueCardButton;

    @FindBy(xpath = "//button[@data-cy='issue-card-step-info-continue-cta']")
    private WebElement customerDebitCardIssueDebitCardCardInfoContinueButton;
    @FindBy(xpath = "//button[@data-cy='issue-card-confirm-modal-confirm-cta']")
    private WebElement customerDebitCardIssueDebitCardCardInfoConfirmButton;

    @FindBy(xpath = "//div[@data-cy='cards-page-header-cta-issue-card-button-desktop']")
    private WebElement customerDebitCardNewCardButton;

    // Search card
    @FindBy(xpath = "//input[@data-cy='example-filter-search-input']")
    private WebElement customerDebitCardSearchCardNameTextBox;
    private final String customerDebitCardCardItemName = "//div[contains(@data-cy,'cards-list-groups-card-name')][text()='%s']";
    @FindBy(xpath = "(//div[contains(@data-cy,'cards-list-groups-card-name')])[1]")
    private WebElement customerDebitCardFirstRecordCardName;

    // Filter
    @FindBy(xpath = "//div[@data-cy='example-filter']")
    private WebElement customerDebitCardFilter;

    // Card detail value
    @FindBy(xpath = "//span[@data-cy='card-details-see-more']")
    private WebElement customerDebitCardDetailSeeMore;
    @FindBy(xpath = "//div[@data-cy='card-name-value']")
    private WebElement customerDebitCardDetailCardName;
    @FindBy(xpath = "//div[@data-cy='card-purpose-value']")
    private WebElement customerDebitCardDetailCardPurpose;
    @FindBy(xpath = "//div[@data-cy='card-source-of-funds-label']")
    private WebElement customerDebitCardDetailCardSourceOfFund;
    @FindBy(xpath = "//div[@data-cy='field-spend-category-name']")
    private WebElement customerDebitCardDetailCardCategory;

    // Close button on card detail
    @FindBy(xpath = "//div[@data-cy='modal-page-back-or-close-button']")
    private WebElement customerDebitCardDetailCloseButton;

    private final Config testConfig;

    public CardPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerDebitCardTitle);
    }

    public void verifyCardPage()
    {
        AssertHelper.assertElementText(testConfig, "The Debit card page show", Element.getText(testConfig, customerDebitCardTitle, "Debit Card page label"), customerDebitCardTitle);
        AssertHelper.assertElementIsDisplayed(testConfig, "Search card", customerDebitCardSearchCardNameTextBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Card filter", customerDebitCardFilter);
        AssertHelper.assertElementIsDisplayed(testConfig, "Card records", customerDebitCardAllCompanyCardsRecords);
    }

    private void inputCardNameOnTheIssueCardScreen(String cardName, String description)
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardIssueDebitCardCardInfoCardNameTextBox, description);
        Element.enterData(testConfig, customerDebitCardIssueDebitCardCardInfoCardNameTextBox, cardName, description);
    }

    private void inputCardPurposeOnTheIssueCardScreen(String cardPurpose, String purpose)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerDebitCardIssueDebitCardCardInfoCardPurposeTextBox, purpose);
        Element.enterData(testConfig, customerDebitCardIssueDebitCardCardInfoCardPurposeTextBox, cardPurpose, purpose);
    }

    private void selectSourceOfFundOnTheIssueCardScreen(String sourceOfFund, String description)
    {
        if (Objects.nonNull(sourceOfFund))
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardIssueDebitCardCardInfoSourceOfFundDropdown, description);
            Element.click(testConfig, customerDebitCardIssueDebitCardCardInfoSourceOfFundDropdown, description, true);
            WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, customerDebitCardIssueDebitCardCardInfoSourceOfFundListValue), description);
            List<WebElement> sOfItemList = Element.getPageElements(testConfig, How.xPath, customerDebitCardIssueDebitCardCardInfoSourceOfFundListValue);

            boolean isSourceOfFundFound = false;

            for (WebElement i : sOfItemList)
            {
                if (i.getText().equals(sourceOfFund))
                {
                    i.click();
                    isSourceOfFundFound = true;
                    break;
                }
            }

            if (!isSourceOfFundFound)
            {
                testConfig.logFail("Source of fund " + sourceOfFund + " not Found");
            }

        } else
        {
            testConfig.logComment("Set default source of fund for card");
        }
    }

    private void selectCardCategoryOnTheIssueCardScreen(String cardCategory, String description)
    {
        if (Objects.nonNull(cardCategory))
        {
            Element.click(testConfig, customerDebitCardIssueDebitCardCardInfoCategoryDropdown, description, true);
            WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, customerDebitCardIssueDebitCardCardInfoCategoryListValue), description);
            List<WebElement> categoryItemList = Element.getPageElements(testConfig, How.xPath, customerDebitCardIssueDebitCardCardInfoCategoryListValue);
            boolean isCategoryFound = false;

            for (WebElement i : categoryItemList)
            {
                if (i.getText().equals(cardCategory))
                {
                    i.click();
                    isCategoryFound = true;
                    break;
                }
            }

            if (!isCategoryFound)
            {
                testConfig.logFail("Category " + cardCategory + " not Found");
            }
        } else
        {
            testConfig.logComment("Set default category for card");
        }
    }

    private void clickButtonContinueOnTheIssueCardScreen()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerDebitCardIssueDebitCardCardInfoContinueButton, "Click issue card continue button");
        Element.click(testConfig, customerDebitCardIssueDebitCardCardInfoContinueButton, "Click issue card continue button");
    }

    private void selectCardColorIfIssuingItSelf()
    {
        String cardColor = testConfig.testData.get("cardColor");
        this.selectCardColorIconOnTheIssueCardScreen(CardColor.valueOf(cardColor))
                .clickButtonContinueOnTheSelectCardColorScreen();
    }

    private CardPage selectCardColorIconOnTheIssueCardScreen(CardColor cardColor)
    {
        String control = String.format(customerDebitCardIssueDebitCardCardInfoCardColorIcon, cardColor.getRgb());
        WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, control), "Card color");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, control), "Card color " + cardColor.getName());
        return this;
    }

    private CardPage clickButtonContinueOnTheSelectCardColorScreen()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerDebitCardIssueDebitCardCardInfoIssueCardButton, "Issue card color button");
        Element.click(testConfig, customerDebitCardIssueDebitCardCardInfoIssueCardButton, "Issue card color button");
        return this;
    }

    private CardPage clickOnTheConfirmButtonIssueNewCard()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerDebitCardIssueDebitCardCardInfoConfirmButton, "Issue card color button");
        Element.click(testConfig, customerDebitCardIssueDebitCardCardInfoConfirmButton, "Click issue card next step button");
        return this;
    }

    private void fillCardInformation()
    {
        inputCardNameOnTheIssueCardScreen(testConfig.testData.get("cardName"), "Card name");
        inputCardPurposeOnTheIssueCardScreen(testConfig.testData.get("cardPurpose"), "Card purpose");
        selectSourceOfFundOnTheIssueCardScreen(testConfig.testData.get("sourceOfFund"), "Card source of fund");
        selectCardCategoryOnTheIssueCardScreen(testConfig.testData.get("cardSpendCategory"), "Card category");
    }

    private void clickIssueNewCardButton(CardTypeEnum cardType)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardNewCardButton, "Card button");
        Element.click(testConfig, customerDebitCardNewCardButton, "Card button");
        // Handle Green Card selection
        if (cardType.equals(CardTypeEnum.VirtualPhysical))
        {
            Element.click(testConfig, customerDebitCardCardFormatSelectionGreenCardCheckbox, "Physical card checkbox");
            WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardCardFormatSelectionContinueButton, "Continue button");
            Element.click(testConfig, customerDebitCardCardFormatSelectionContinueButton, "Continue button");
        }
    }

    public void selectUserByPersonType(PersonTypeEnum personTypeEnum)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardUsersToIssueItems, "User list");
        switch (personTypeEnum)
        {
            case Themself ->
            {
                testConfig.putRunTimeProperty("cardHolderName", customerDebitCardUserActiveList.get(0).getText());
                customerDebitCardUserActiveList.get(0).click();
            }
            case OtherPerson, Random ->
            {
                int randomActiveUser = DataGenerator.generateRandomNumberInIntRange(1, customerDebitCardUserActiveList.size() - 1);
                testConfig.putRunTimeProperty("cardHolderName", customerDebitCardUserActiveList.get(randomActiveUser).getText());
                customerDebitCardUserActiveList.get(randomActiveUser).click();
            }
            case VerificationPending ->
            {
                int randomPendingUser = DataGenerator.generateRandomNumberInIntRange(0, customerDebitCardUserPendingList.size() - 1);
                testConfig.putRunTimeProperty("cardHolderName", customerDebitCardUserActiveList.get(randomPendingUser).getText());
                customerDebitCardUserActiveList.get(randomPendingUser).click();
            }
            default -> testConfig.logFail("Person Type " + personTypeEnum + " is not defined");
        }
    }

    private void selectUserByPersonName()
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        String cardHolderName = testConfig.testData.get("cardHolderName");
        String control = String.format(customerDebitCardHolderName, cardHolderName);
        WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, control), "Card holder name");
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, control), "Card holder name " + cardHolderName);
    }

    private CardPage searchCardByCardName()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerDebitCardSearchCardNameTextBox, "Search field");
        Element.enterData(testConfig, customerDebitCardSearchCardNameTextBox, testConfig.testData.get("cardName"), "Search field");
        return this;
    }

    private CardPage clickCardNameRecordToViewDetail()
    {
        String cardName = testConfig.testData.get("cardName");
        String control = String.format(customerDebitCardCardItemName, cardName);
        WaitHelper.waitForElementToBeDisplayed(testConfig, Element.getPageElement(testConfig, How.xPath, control), "Card name " + cardName);
        Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, control), "Card name " + cardName);
        return this;
    }

    private CardPage clickOnCardDetailSeeMore()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardDetailSeeMore, "See more");
        Element.click(testConfig, customerDebitCardDetailSeeMore, "See more");
        return this;
    }

    private CardPage validateCardNameOnTheCardRecord()
    {
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
        String expectedCardName = testConfig.testData.get("cardName");
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardFirstRecordCardName, "Card Name");
        AssertHelper.assertElementText(testConfig, "Card Name", expectedCardName, customerDebitCardFirstRecordCardName);
        return this;
    }

    private void validateCardNameOnTheCardDetails(String cardName)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardDetailCardName, "Card Name");
        AssertHelper.assertElementText(testConfig, "Card Name", cardName, customerDebitCardDetailCardName);
    }

    private void validateCardPurposeOnTheCardDetails(String cardPurpose)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardDetailCardPurpose, "Card Purpose");
        AssertHelper.assertElementText(testConfig, "Card Purpose", cardPurpose, customerDebitCardDetailCardPurpose);
    }

    private void validateSourceOfFundOnTheCardDetails(String cardSof)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardDetailCardSourceOfFund, "Card Source Of Fund");
        AssertHelper.assertElementText(testConfig, "Card Source Of Fund", cardSof, customerDebitCardDetailCardSourceOfFund);
    }

    private void validateCardSpendCategoryOnTheCardDetails(String cardCategory)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardDetailCardCategory, "Card Category");
        AssertHelper.assertElementText(testConfig, "Card Category", cardCategory, customerDebitCardDetailCardCategory);
    }

    private void validateCardInformationOnTheCardDetails()
    {
        validateCardNameOnTheCardDetails(testConfig.testData.get("cardName"));
        validateCardPurposeOnTheCardDetails(testConfig.testData.get("cardPurpose"));
        validateSourceOfFundOnTheCardDetails(testConfig.testData.get("sourceOfFund"));
        validateCardSpendCategoryOnTheCardDetails(testConfig.testData.get("cardSpendCategory"));
    }

    private CardPage clickDebitCardDetailCloseButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerDebitCardDetailCloseButton, "Close button");
        Element.click(testConfig, customerDebitCardDetailCloseButton, "Close button");
        return this;
    }

    public void issueNewCardAndSelectCardHolderByType(CardTypeEnum cardTypeEnum, PersonTypeEnum personTypeEnum)
    {
        clickIssueNewCardButton(cardTypeEnum);
        selectUserByPersonType(personTypeEnum);
    }

    public void issueNewCardAndSelectCardHolderByHolderName(CardTypeEnum cardTypeEnum)
    {
        clickIssueNewCardButton(cardTypeEnum);
        selectUserByPersonName();
    }

    public void fillCardInfoAndValidate()
    {
        fillCardInformation();
        clickButtonContinueOnTheIssueCardScreen();
        clickOnTheConfirmButtonIssueNewCard();
        searchCardByCardName();
        validateCardNameOnTheCardRecord();
        clickCardNameRecordToViewDetail();
        clickOnCardDetailSeeMore();
        validateCardInformationOnTheCardDetails();
    }
}
