package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.CurrencyEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentEnums.TransferType;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.customer.helpers.PaymentStaticDataEn.FromSourceCurrencyEnum;
import Automation.Utils.*;
import Automation.Utils.Element.How;
import Automation.Utils.Enums.DateTimeStringFormatEnum;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class TransferPage
{

    private final Config testConfig;
    private final String customerLocalTransferDetailsCategoryItem = ".q-item__label";
    private final String customerLocalTransferDetailsTransferFee = "//div[@data-cy='fx-quote-stepper-fee']";
    private final String customerLocalTransferDetailsFxNotification = "//div[@data-cy='fx-quote-box-estimated-delivery-time']";
    private final String systemDatePickerYearItem = "//*[starts-with(@class,'q-date__years-item')]//span[text()='%s']";
    private final String systemDatePickerMonthItem = "//*[starts-with(@class,'q-date__months-item')]//span[text()='%s']";
    private final String systemDatePickerDayItem = "//*[starts-with(@class,'q-date__calendar-item')]/button//span[text()='%s']";
    private final String frequencyItem = "//*[contains(@class,'q-item q-item-type')]//span[text()='%s']";
    @FindBy(xpath = "//input[@data-cy='source-amount-input-input-box-input']")
    private WebElement customerLocalTransferDetailsAmountTextBox;
    @FindBy(css = "div.transfer-step-make-form > div.transfer-step-make-form__category-dropdown > div > div.example-field > label")
    private WebElement customerLocalTransferDetailsCategoryDropDown;
    @FindBy(xpath = "//input[@data-cy='transfer-step-make-transaction-category-select-input']")
    private WebElement customerLocalTransferDetailsCategoryTextBox;
    @FindBy(xpath = "//button[@data-cy='transfer-step-make-reference-button']")
    private WebElement customerLocalTransferDetailsReferenceButton;
    @FindBy(xpath = "//*[contains(@data-cy,'reference-input')]")
    private WebElement customerLocalTransferDetailsReferenceTextBox;
    @FindBy(css = ".example-cta-box.example-cta-box--fixed.example-cta > div > button")
    private WebElement customerLocalTransferDetailsNextButton;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "div.example-page__header > div > div")
    private WebElement transferPageTitle;
    @FindBy(xpath = "//div[@data-cy='transfer-step-make-header-title']")
    private WebElement usdTransferPageTitle;
    @FindBy(css = "[data-cy='currency-account-select']")
    private WebElement currencyAccountSelectBox;
    @FindBy(css = "div.source-amount-input__source-amount-box")
    private WebElement sourceAmountBox;
    @FindBy(css = "[data-cy='transfer-local-arrived-time-panel-tooltip']")
    private WebElement arriveTimeBox;
    @FindBy(xpath = "//div[@data-cy='fx-quote-box-estimated-delivery-time']")
    private WebElement fxNotificationArriveTimeBox;
    @FindBy(css = "div.transfer-step-make-form")
    private WebElement transferMakeForm;
    @FindBy(xpath = "//button[@data-cy='dynamic-requirements-form-cta']")
    private WebElement transferSubmitForm;
    @FindBy(css = ".transfer-step-make-header__account-number")
    private WebElement transferHeaderAccountNumber;
    @FindBy(css = "[data-cy='transfer-step-make-header-currency-code']")
    private WebElement currencyBankInfoText;
    @FindBy(css = "[data-cy='transfer-step-make-header-reference-number']")
    private WebElement accountNumberBankInfoText;
    @FindBy(css = "[class='input-value']")
    private WebElement bankNameBankInfoText;
    @FindBy(xpath = "//div[@data-cy='fx-quote-box-estimated-delivery-time']")
    private WebElement customerLocalTransferDetailsNotificationText;
    @FindBy(css = "div[role='option']")
    private WebElement customerLocalTransferDetailsCategoryListBox;
    @FindBy(css = "span[role='presentation']")
    private WebElement customerLocalTransferDetailsCategoryDropdownHidden;
    @FindBy(xpath = "//div[@data-cy='legacy-transaction-category-select-selected-name']")
    private WebElement customerLocalTransferDetailsCategoryItem1;
    @FindBy(xpath = "//div[@data-cy='example-header-desktop-back-button']")
    private WebElement backButton;
    @FindBy(xpath = "//*[@data-cy='transfer-step-make-schedule-button']")
    private WebElement scheduleButton;
    @FindBy(xpath = "//*[@data-cy='schedule-payment-form-date-input']")
    private WebElement scheduleDatePicker;
    @FindBy(xpath = "//*[@data-cy='example-date-picker-select-year-button']")
    private WebElement systemDatePickerSelectYearButton;
    @FindBy(xpath = "//*[@data-cy='example-date-picker-select-month-button']")
    private WebElement systemDatePickerSelectMonthButton;
    @FindBy(xpath = "//*[contains(@class,'schedule-payment-form__frequency')]/label")
    private WebElement frequencyDropdown;
    @FindBy(css = "[data-cy='schedule-payment-form-cta']")
    private WebElement scheduleContinueButton;
    @FindBy(css = "[data-cy='schedule-payment-form-transfer-date']")
    private WebElement scheduleDateText;
    @FindBy(xpath = "//div[@class='q-mt-sm']")
    private WebElement scheduleFrequencyText;
    @FindBy(css = "[class*='edit-field-button']")
    private WebElement scheduleEditButton;
    @FindBy(css = "[data-cy='schedule-payment-form-toggle']")
    private WebElement scheduleToggle;
    @FindBy(xpath = "//input[@data-cy='reference-input']")
    private WebElement referenceValueText;
    @FindBy(xpath = "//div[contains(@class,'transaction-details-field__label')]//div")
    private WebElement budgetLabel;
    @FindBy(xpath = "//span[contains(@class,'transfer-budget-select__budget-required')]")
    private WebElement budgetRequiredLabel;
    @FindBy(css = "[data-cy='field-budget-value']")
    private WebElement budgetValue;

    public TransferPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, customerLocalTransferDetailsAmountTextBox);
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loading bar");
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsNextButton, "Next Button");
        verifyTransferPage();
    }

    private void verifyTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Account Number", transferHeaderAccountNumber);
        AssertHelper.assertElementIsDisplayed(testConfig, "Next Button", customerLocalTransferDetailsNextButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Currency Account Select", currencyAccountSelectBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Source Amount Box", sourceAmountBox);
        switch (testConfig.getRunTimeProperty("recipientCurrency").toLowerCase())
        {
            case "usd":
                AssertHelper.assertElementIsDisplayed(testConfig, "Recipient Name", usdTransferPageTitle);
                break;
            case "idr":
            case "sgd":
                AssertHelper.assertElementIsDisplayed(testConfig, "Arrive Time Box", arriveTimeBox);
                AssertHelper.assertElementIsDisplayed(testConfig, "Transfer Make Form", transferMakeForm);
                AssertHelper.assertPartialElementText(testConfig, "Transfer Page Title", PaymentHelper.paymentStaticDataBase.getTransferPageTitle(), transferPageTitle);
                break;
            default:
                testConfig.logFail("Invalid recipient sent");
                break;
        }

    }

    public Object inputInstantLocalTransferInfo(boolean isAdvanceTransfer)
    {
        testConfig.testData.put("transferTitle", Objects.requireNonNull(getTransferTitleByTransferType(TransferType.valueOf(testConfig.testData.get("transferType")))).getName());
        testConfig.testData.put("transferOverviewTitle", Objects.requireNonNull(getTransferOverviewTitleByTransferType(TransferType.valueOf(testConfig.testData.get("transferType")))).getName());
        Element.enterData(testConfig, customerLocalTransferDetailsAmountTextBox, testConfig.testData.get("amount"), "Transfer amount");
        enterCategory();
        enterReference();
        if (!StringUtils.isEmpty(testConfig.getRunTimeProperty("userRole")) && testConfig.getRunTimeProperty("userRole").equalsIgnoreCase("nonAdminBO"))
        {
            WaitHelper.waitForElementToBeDisplayed(testConfig, budgetLabel, "Budget Label");
            AssertHelper.compareEquals(testConfig, "Budget Label", PaymentHelper.paymentStaticDataBase.getBudgetLabel(),
                    Element.getText(testConfig, budgetLabel, "Budget Label").trim());
            AssertHelper.compareEquals(testConfig, "Prefilled Budget value", testConfig.testData.get("assignedBudgetName"),
                    Element.getText(testConfig, budgetValue, "Budget Value").trim());
        }
        clickOnNextButton();
        if (isAdvanceTransfer)
            return new AdditionalInformationPage(testConfig);
        else
            return new ReviewTransferPage(testConfig);
    }

    public void enterCategory()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsCategoryDropDown, "Category Dropdown");
        Element.click(testConfig, customerLocalTransferDetailsCategoryDropDown, "Category Dropdown", true);
        WebElement item = Element.getPageElement(testConfig, How.css, customerLocalTransferDetailsCategoryItem);
        WaitHelper.waitForOptionalElement(testConfig, item, "Category Dropdown Item");
        WaitHelper.waitForSeconds(testConfig, 1);
        List<WebElement> categories = Element.getPageElements(testConfig, How.css, customerLocalTransferDetailsCategoryItem);
        int categoryIndex = DataGenerator.generateRandomNumberInIntRange(0, Objects.requireNonNull(categories).size() - 2);
        WebElement categoryItem = categories.get(categoryIndex);
        String category = Element.getText(testConfig, categoryItem, "Category Item");
        testConfig.logComment("Category -->" + category);
        testConfig.testData.put("transferCategory", category);
        Element.click(testConfig, categoryItem, "Category Item");
    }

    public void enterReference()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsReferenceButton, "Reference Button");
        Element.click(testConfig, customerLocalTransferDetailsReferenceButton, "Reference button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsReferenceTextBox, "Reference Text box");
        Element.enterData(testConfig, customerLocalTransferDetailsReferenceTextBox, testConfig.testData.get("reference"), "Reference");
    }

    public void inputUsdInstantLocalTransferInfo(Map<String, String> testData, CurrencyEnum currencyEnum)
    {
        //'selected Source Currency', 'transfer Title', transfer Overview Title;
        testConfig.putRunTimeProperty("selectedSourceCurrency", Objects.requireNonNull(getSourceCurrencyByCurrency(currencyEnum)).getName());
        testConfig.putRunTimeProperty("transferTitle", Objects.requireNonNull(getTransferTitleByTransferType(TransferType.valueOf(testData.get("transferType")))).getName());
        testConfig.putRunTimeProperty("transferOverviewTitle", Objects.requireNonNull(getTransferOverviewTitleByTransferType(TransferType.valueOf(testData.get("transferType")))).getName());

        // Input source amount;
        Element.enterData(testConfig, customerLocalTransferDetailsAmountTextBox, testData.get("amount"), "Transfer amount");
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsNotificationText, "FX quote notification");

        // Get value of transfer fee;
        String transferFee = Element.getText(testConfig, How.xPath, customerLocalTransferDetailsTransferFee, "Transfer Fee");
        testData.put("transferFee", Objects.requireNonNull(transferFee));

        // Click category dropdown;
        WaitHelper.waitForElementToBeClickable(testConfig, customerLocalTransferDetailsCategoryDropDown, "Category Dropdown");
        Element.click(testConfig, customerLocalTransferDetailsCategoryDropDown, "Category Dropdown", false);

        // Select and click random category;
        WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsCategoryListBox, "Category list box");
        List<WebElement> categories = Element.getPageElements(testConfig, How.css, customerLocalTransferDetailsCategoryItem);
        int categoryIndex = DataGenerator.generateRandomNumberInIntRange(0, Objects.requireNonNull(categories).size() - 2);
        if (Objects.requireNonNull(categories).size() == 0 || Objects.requireNonNull(categories).size() == 1)
        {
            categoryIndex = Objects.requireNonNull(categories).size();
        }
        WebElement categoryItem = categories.get(categoryIndex);
        WaitHelper.waitForElementToBeClickable(testConfig, categoryItem, "Category Item");
        Element.click(testConfig, categoryItem, "Category Item");
        WaitHelper.waitForElementAttributeToBe(testConfig, customerLocalTransferDetailsCategoryDropdownHidden, "aria-hidden", "true", "wait for aria-selected");

        // Get value of category, FX notification");
        testConfig.putRunTimeProperty("transferCategoryValue", Element.getText(testConfig, customerLocalTransferDetailsCategoryItem1, "Category Item"));
        int retry = 0;
        for (retry = 0; retry < 5; retry++)
        {
            String fxNotificationText = Element.getText(testConfig, How.xPath, customerLocalTransferDetailsFxNotification, "FX notification");
            if (fxNotificationText != null && !fxNotificationText.isEmpty())
            {
                testConfig.logComment("fxNotificationText-->" + fxNotificationText);
                testConfig.putRunTimeProperty("fxUsdNotification", Objects.requireNonNull(fxNotificationText));
                break;
            }
            WaitHelper.waitForSeconds(testConfig, 1);
        }
        AssertHelper.compareFalse(testConfig, "Fx Notification Text not found", retry >= 5);

        // Click Reference button and input value;
        if (Element.isElementDisplayed(testConfig, customerLocalTransferDetailsReferenceButton))
        {
            Element.click(testConfig, customerLocalTransferDetailsReferenceButton, "Reference button");
            WaitHelper.waitForElementToBeDisplayed(testConfig, customerLocalTransferDetailsReferenceTextBox, "Reference Text box");
            Element.enterData(testConfig, customerLocalTransferDetailsReferenceTextBox, testData.get("reference"), "Reference");
            String referenceValue = Element.getAttributeText(testConfig, referenceValueText, "data-value", "Reference Value");
            testConfig.putRunTimeProperty("referenceValue", Objects.requireNonNull(referenceValue));
        }
    }

    private TransferTitleEnum getTransferTitleByTransferType(TransferType transferType)
    {
        TransferTitleEnum expectTitle;
        switch (transferType)
        {
            case Recurring, RecurringSubmitted:
                expectTitle = TransferTitleEnum.RecurringTransfer;
                break;
            case Scheduled, ScheduledSubmitted:
                expectTitle = TransferTitleEnum.ScheduledTransfer;
                break;
            case Submitted:
                expectTitle = TransferTitleEnum.TransferSubmitted;
                break;
            case Initiated:
                expectTitle = TransferTitleEnum.TransferInitiated;
                break;
            case Completed:
                expectTitle = TransferTitleEnum.TransferCompleted;
                break;
            default:
                return null;
        }
        return expectTitle;
    }

    private TransferTitleEnum getTransferOverviewTitleByTransferType(TransferType transferType)
    {
        TransferTitleEnum expectTitle;
        switch (transferType)
        {
            case Recurring:
                expectTitle = TransferTitleEnum.RecurringTransfer;
                break;
            case Scheduled:
                expectTitle = TransferTitleEnum.ScheduledTransfer;
                break;
            case Submitted:
                expectTitle = TransferTitleEnum.TransferSubmitted;
                break;
            case Initiated:
                expectTitle = TransferTitleEnum.TransferInitiated;
                break;
            case Completed:
                expectTitle = TransferTitleEnum.TransferReceived;
                break;
            case ScheduledSubmitted:
                expectTitle = TransferTitleEnum.ScheduledTransferSubmitted;
                break;
            case RecurringSubmitted:
                expectTitle = TransferTitleEnum.RecurringTransferSubmitted;
                break;
            default:
                return null;
        }
        return expectTitle;
    }

    public void validateBankInfoCorrect()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, currencyBankInfoText, "Wait for bank info displays");
        AssertHelper.assertElementText(testConfig, "currency on bank info", testConfig.testData.get("recipientCurrencyCode"), currencyBankInfoText);
        AssertHelper.assertElementText(testConfig, "bank account number on bank info", testConfig.testData.get("recipientAccountNumber"), accountNumberBankInfoText);
        String actualBank = Element.getText(testConfig, bankNameBankInfoText, "Get text bank name");
        AssertHelper.compareContains(testConfig, "bank account number on bank info", testConfig.testData.get("recipientBankName"), actualBank);
    }

    private FromSourceCurrencyEnum getSourceCurrencyByCurrency(CurrencyEnum currencyEnum)
    {
        FromSourceCurrencyEnum expectTitle;
        switch (currencyEnum)
        {
            case SGD:
                expectTitle = FromSourceCurrencyEnum.FromSgdAccount;
                break;
            case USD:
                expectTitle = FromSourceCurrencyEnum.FromUsdAccount;
                break;
            default:
                return null;
        }
        return expectTitle;
    }

    public RecipientListPage clickBackButton()
    {
        WaitHelper.waitForPageLoad(testConfig, customerLocalTransferDetailsNextButton);
        Element.click(testConfig, backButton, "back Button");
        return new RecipientListPage(testConfig);
    }

    public void clickOnNextButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerLocalTransferDetailsNextButton, "Next Button");
        Element.click(testConfig, customerLocalTransferDetailsNextButton, "Next button");
    }

    public void inputScheduledLocalTransferInfo(String date, String frequency, CurrencyEnum currencyEnum)
    {
        testConfig.testData.put("transferTitle", Objects.requireNonNull(getTransferTitleByTransferType(TransferType.valueOf(testConfig.testData.get("transferType")))).getName());
        testConfig.testData.put("transferOverviewTitle", Objects.requireNonNull(getTransferOverviewTitleByTransferType(TransferType.valueOf(testConfig.testData.get("transferType")))).getName());
        Element.enterData(testConfig, customerLocalTransferDetailsAmountTextBox, testConfig.testData.get("amount"), "Transfer amount");
        enterCategory();
        enterReference();
        clickOnScheduleButton();
        selectScheduleDateAndFrequency(date, frequency);
    }

    public void selectScheduleDateAndFrequency(String date, String frequency)
    {
        selectScheduleDate(date);
        selectFrequency(frequency);
        clickOnScheduleContinueButton();
    }

    public void clickOnScheduleButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, scheduleButton, "schedule button");
        Element.click(testConfig, scheduleButton, "schedule button", true);
    }

    public void selectScheduleDate(String date)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, scheduleDatePicker, "schedule picker");
        Element.click(testConfig, scheduleDatePicker, "schedule date field");
        selectDatePicker(date, DateTimeStringFormatEnum.DATE_FORMAT_TRANSFER.getFormat());
    }

    public void selectDatePicker(String date, String dateTimeFormatter)
    {
        LocalDate localDate = DataGenerator.convertStringToLocalDateGivenFormat(date, dateTimeFormatter);
        selectYear(localDate.getYear());
        selectMonth(localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        selectDay(localDate.getDayOfMonth());
    }

    private void selectYear(int selectedYear)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, systemDatePickerSelectYearButton, "system date picker select year button");
        Element.click(testConfig, systemDatePickerSelectYearButton, "system date picker select year button");
        WebElement selectedYearItem = Element.getPageElement(testConfig, How.xPath, String.format(systemDatePickerYearItem, selectedYear));
        WaitHelper.waitForElementToBeDisplayed(testConfig, selectedYearItem, "selected year item");
        Element.click(testConfig, selectedYearItem, " selected year item");
        WaitHelper.waitForElementToBeHidden(testConfig, selectedYearItem, "selected year item");
    }

    private void selectMonth(String selectedMonth)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, systemDatePickerSelectMonthButton, "system date picker select month button");
        Element.click(testConfig, systemDatePickerSelectMonthButton, "system date picker select month button");
        WebElement selectedMonthItem = Element.getPageElement(testConfig, How.xPath, String.format(systemDatePickerMonthItem, selectedMonth));
        WaitHelper.waitForElementToBeDisplayed(testConfig, selectedMonthItem, "wait selected month item");
        Element.click(testConfig, selectedMonthItem, "Click on selected month item");
        WaitHelper.waitForElementToBeHidden(testConfig, selectedMonthItem, "wait for selected month item hidden");
    }

    private void selectDay(int selectedDay)
    {
        WebElement selectedDayItem = Element.getPageElement(testConfig, How.xPath, String.format(systemDatePickerDayItem, selectedDay));
        WaitHelper.waitForElementToBeDisplayed(testConfig, selectedDayItem, "system date day item");
        Element.click(testConfig, selectedDayItem, "Click on system date day item");
        WaitHelper.waitForElementToBeHidden(testConfig, selectedDayItem, "wait for selected month item hidden");
    }

    public void selectFrequency(String selectedFrequency)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, frequencyDropdown, "frequency dropdown");
        Element.click(testConfig, frequencyDropdown, "Click on frequency dropdown");
        WebElement selectedFrequencyItem = Element.getPageElement(testConfig, How.xPath, String.format(frequencyItem, selectedFrequency));
        WaitHelper.waitForElementToBeDisplayed(testConfig, selectedFrequencyItem, "selected frequency item");
        Element.click(testConfig, selectedFrequencyItem, "selected frequency item");
        WaitHelper.waitForElementToBeHidden(testConfig, selectedFrequencyItem, "selected frequency item");
    }

    public void clickOnScheduleContinueButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, scheduleContinueButton, "wait for schedule continue button displays");
        Element.click(testConfig, scheduleContinueButton, "click on schedule continue button");
        WaitHelper.waitForElementToBeHidden(testConfig, scheduleContinueButton, "wait for schedule continue button hidden");
    }

    public void validateScheduleTransferInfoCorrect(String date, String frequency)
    {
        AssertHelper.assertElementText(testConfig, "schedule date text", date, scheduleDateText);
        String actual = Element.getText(testConfig, scheduleFrequencyText, "Get schedule frequency text");
        AssertHelper.compareContains(testConfig, "schedule frequency info", frequency, actual);
        AssertHelper.assertElementIsDisplayed(testConfig, "schedule edit button", scheduleEditButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "schedule frequency text", scheduleToggle);
    }

    public void clickOnEditButton()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, scheduleEditButton, "edit button");
        Element.click(testConfig, scheduleEditButton, "edit button", true);
    }

    public ReviewTransferPage clickNextButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, customerLocalTransferDetailsNextButton, "Next button");
        Element.click(testConfig, customerLocalTransferDetailsNextButton, "Next button");
        return new ReviewTransferPage(testConfig);
    }
}