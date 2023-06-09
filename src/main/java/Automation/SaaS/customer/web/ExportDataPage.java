package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.Utils.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.Objects;

public class ExportDataPage
{

    private final Config testConfig;
    private final String allStatusItems = "div[data-cy='filter-option-fields-wrapper-multiple-select']";
    private final String statusItem = "//div[@data-cy='filter-option-fields-wrapper-multiple-select' and .='%s']";
    String formatButtonxPath = "//button[@data-cy='transactions-export-format-list-item']//p[.='%s']";
    String sourceOfFundXpath = "//span[contains(normalize-space(),'SGD')]/parent::div/div[contains(@data-cy,'check')]";
    String accountXpath = "//div/span[contains(normalize-space(),'SGD')]";
    @FindBy(css = "h1")
    private WebElement pageTitle;
    @FindBy(css = "button[data-cy='transactions-advance-export-module-group-list-item-expense']")
    private WebElement moneyOutModuleButton;
    @FindBy(css = "div[data-cy='transactions-advance-export-module-list-statements']")
    private WebElement accountStatementModuleButton;
    @FindBy(css = "button[data-cy='transactions-advance-export-module-group-list-item-money-in']")
    private WebElement moneyInButton;
    @FindBy(css = "button[data-cy='transactions-advance-export-module-group-list-see-all']")
    private WebElement seeAllModulesButton;
    @FindBy(css = "button[data-cy='transactions-advance-export-guide-actions-desktop-guide-button']")
    private WebElement howItWorksButton;
    @FindBy(css = "button[data-cy='transactions-advance-export-guide-actions-desktop-faq-button']")
    private WebElement faqButton;
    @FindBy(css = "button[data-cy='transactions-advance-export-button']")
    private WebElement exportButton;
    @FindBy(css = "div[class='transactions-advance-export-filter'] button[data-cy=filter-button-debit_account]")
    private WebElement filterDebitAccountDropdown;
    @FindBy(css = "button[data-cy='filter-button-more_filters']")
    private WebElement moreFiltersButton;
    @FindBy(xpath = "//button[@data-cy='filter-button-transaction_date' or @data-cy='filter-button-claim_date']")
    private WebElement dateRangeDropdown;
    @FindBy(css = "button[data-cy='filter-button-due_date']")
    private WebElement dueDateRangeDropdown;
    @FindBy(css = "button[data-cy='filter-button-budget']")
    private WebElement budgetDropdown;
    @FindBy(css = "button[data-cy='filter-button-type']")
    private WebElement transactionTypeFilterDropdown;
    @FindBy(css = "button[data-cy='filter-button-attachment']")
    private WebElement attachmentFilterDropdown;
    @FindBy(css = "button[data-cy='filter-button-debit_category_names']")
    private WebElement spendCategoryFilterDropdown;
    @FindBy(css = "button[data-cy='filter-button-debit_transaction_created_by']")
    private WebElement employeeFilterDropdown;
    @FindBy(css = "button[data-cy='filter-button-approvals_from_approvers']")
    private WebElement approversFilterDropdown;
    @FindBy(css = "button[data-cy='transactions-advance-export-record-count-preview']")
    private WebElement previewRecordButton;
    @FindBy(css = "div.transactions-advance-export-record-count span strong")
    private WebElement numberOfRecordsLabel;
    @FindBy(xpath = "//div[@data-cy='transactions-export-format-list']//button[contains(@class,'transactions-advance-export-format-button--active')]")
    private WebElement defaultFormatButton;
    @FindBy(css = "button[data-cy='transactions-export-format-list-item-see-all']")
    private WebElement seeAllFormatButton;
    @FindBy(css = "button[data-cy='transactions-export-format-list-item-see-all']")
    private WebElement seeAllFormatsButton;
    @FindBy(css = "button[data-cy='transactions-export-file-type-list-csv']")
    private WebElement csvButton;
    @FindBy(css = "button[data-cy='transactions-export-file-type-list-xlsx']")
    private WebElement xlsxButton;
    @FindBy(css = "button[data-cy='transactions-export-file-type-list-pdf']")
    private WebElement pdfButton;
    @FindBy(css = ".q-checkbox__inner.relative-position.non-selectable.q-checkbox__inner--falsy")
    private WebElement includeAllAttachmentsCheckbox;
    @FindBy(css = ".text-sub4.text-secondary.text-weight-bold")
    private WebElement whatDoYouWantToExportLabel;
    @FindBy(css = "input[data-cy='transactions-advance-export-module-list-modal-search-input']")
    private WebElement moduleSearchField;
    @FindBy(css = ".q-item__label.row.items-center")
    private WebElement searchItem;
    @FindBy(xpath = "//div[@data-cy='filter-option-list-item-all']")
    private WebElement allItem;
    @FindBy(xpath = "//div[@data-cy='filter-option-list-item-last_30_days']")
    private WebElement last30DaysItem;
    @FindBy(xpath = "//div[@data-cy='filter-option-list-item-last_60_days']")
    private WebElement last60DaysItem;
    @FindBy(xpath = "//div[@data-cy='filter-option-list-item-last_90_days']")
    private WebElement last90DaysItem;
    @FindBy(xpath = "//div[@data-cy='filter-option-list-item-custom']")
    private WebElement customItem;
    @FindBy(xpath = "//span[@data-cy='filter-option-list-custom-period-item-from-date']")
    private WebElement customFromDate;
    @FindBy(xpath = "//span[@data-cy='filter-option-list-custom-period-item-to-date']")
    private WebElement customToDate;
    private WebElement sgdAccountItem;
    @FindBy(xpath = "//span[contains(normalize-space(),'Advance')]")
    private WebElement advanceAccountItem;
    @FindBy(css = "button[data-cy=filter-option-fields-wrapper-button]")
    private WebElement applyFilterButton;
    @FindBy(css = ".text-h5")
    private WebElement exportSuccessfulTitle;
    @FindBy(css = "span[class='inline-block text-h6'] span")
    private WebElement successfulMessageDetails;
    @FindBy(css = "[data-cy='transactions-export-success-modal-ok']")
    private WebElement gotItButton;
    @FindBy(css = ".text-sub4.text-secondary.text-weight-bold")
    private WebElement modalTitle;
    @FindBy(css = "button[data-cy='filter-button-state_codes']")
    private WebElement statusDropdown;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(css = "button[data-cy='filter-button-expiring_soon'].filter-button__cta--active")
    private WebElement expiringSoonFilterSelected;
    @FindBy(css = "button[data-cy='filter-button-expiring_soon']")
    private WebElement expiringSoonFilter;

    public ExportDataPage(Config testConfig)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, accountStatementModuleButton);
        verifyExportDataPage();
    }

    private void verifyExportDataPage()
    {
        AssertHelper.assertElementText(testConfig, "Page heading", "Export data", pageTitle);
        AssertHelper.assertElementText(testConfig, "Export button", "Export", exportButton);
        AssertHelper.assertElementIsDisplayed(testConfig, "Account statement", accountStatementModuleButton);
    }

    //select module
    public void selectModule(SaasEnums.ExportModule module)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, seeAllModulesButton, "See All Modules");
        switch (module)
        {
            case AccountStatements ->
                    Element.click(testConfig, accountStatementModuleButton, "Account Statement Module");
            case MoneyIn -> Element.click(testConfig, moneyInButton, "Money In Module");
            case MoneyOut -> Element.click(testConfig, moneyOutModuleButton, "Money Out Module");
            case Bills, Cards, Budgets, Invoices, Claims ->
            {
                Element.click(testConfig, seeAllModulesButton, "See All Modules");
                WaitHelper.waitForElementToBeClickable(testConfig, moduleSearchField, "Module Search Field");
                Element.enterData(testConfig, moduleSearchField, module.toString(), "Module Search Field");
                Element.click(testConfig, modalTitle, "Complete Search");
                Element.click(testConfig, searchItem, "Search Item: " + module);
            }
            default -> testConfig.logFail("Invalid module selected");
        }
        waitForLoaderDisappeared();
    }

    private void waitForLoaderDisappeared()
    {
        if (Element.isElementDisplayed(testConfig, loader))
            WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        if (Element.isElementDisplayed(testConfig, loadingIcon))
            WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }

    public void selectDateRange(SaasEnums.ExportDateRange dateRange)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, dateRangeDropdown, "Date Range Dropdown");
        Element.click(testConfig, dateRangeDropdown, "Date Range Dropdown");
        WaitHelper.waitForElementToBeClickable(testConfig, allItem, "All Item");
        switch (dateRange)
        {
            case All -> Element.click(testConfig, allItem, "All Item");
            case Last30Days -> Element.click(testConfig, last30DaysItem, "Last 30 Days Item");
            case Last90Days -> Element.click(testConfig, last90DaysItem, "Last 90 Days Item");
            case Last60Days -> Element.click(testConfig, last60DaysItem, "Last 60 Days Item");
            case Custom ->
            {
                Element.click(testConfig, customItem, "Custom Item");
                selectCustomDateRange("01/01/2021", "01/01/2021");
            }
            default -> testConfig.logFail("Invalid date range selected");
        }
        WaitHelper.waitForElementToBeClickable(testConfig, previewRecordButton, "Preview Record button");
        testConfig.putRunTimeProperty("numberOfRecord", Element.getText(testConfig, numberOfRecordsLabel, "Number of Records"));
    }

    public void selectCustomDateRange(String fromDate, String toDate)
    {
        //TODO: Need to implement date picker
    }

    public void selectBillStatusFilter(SaasEnums.BillStatus billStatus)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, statusDropdown, "Status Dropdown");
        Element.click(testConfig, statusDropdown, "Status Dropdown");
        switch (billStatus)
        {
            case All ->
            {
                for (WebElement item : Objects.requireNonNull(Element.getPageElementsWithRetry(testConfig, Element.How.css, allStatusItems)))
                {
                    WaitHelper.waitForElementToBeClickable(testConfig, item, "Status item");
                    Element.click(testConfig, item, "Status Item");
                }
            }

            case Paid, Draft ->
                    Element.click(testConfig, Element.getPageElement(testConfig, Element.How.xPath, String.format(statusItem, billStatus)), billStatus.toString());
            default -> testConfig.logFail("Invalid bill status selected");
        }
        Element.click(testConfig, applyFilterButton, "Apply Filter Button");
    }

    public void selectExportFileType(SaasEnums.FileType fileType)
    {
        switch (fileType)
        {
            case CSV -> Element.click(testConfig, csvButton, "CSV File Type Button");
            case PDF -> Element.click(testConfig, pdfButton, "PDF File Type Button");
            case XLSX -> Element.click(testConfig, xlsxButton, "XLSX File Type Button");
            default -> testConfig.logFail("Invalid file type selected");
        }
    }

    public void selectAccountSourceOfFund(SaasEnums.SourceOfFund account)
    {
        boolean isRadioButtonType = false;
        waitForLoaderDisappeared();
        WaitHelper.waitForElementToBeDisplayed(testConfig, filterDebitAccountDropdown, "Account Dropdown");
        Element.click(testConfig, filterDebitAccountDropdown, "Account Dropdown");
        WebElement accountItem = Element.getPageElementWithRetry(testConfig, Element.How.xPath, String.format(sourceOfFundXpath, account.toString()));
        if (accountItem == null)
        {
            accountItem = Element.getPageElementWithRetry(testConfig, Element.How.xPath, String.format(accountXpath, account));
            isRadioButtonType = true;
        }
        WaitHelper.waitForElementToBeClickable(testConfig, accountItem, "Account Item");
        switch (account)
        {
            case IdrDebitAccount ->
            {
                Element.click(testConfig, accountItem, "IDR Account Item");
                testConfig.putRunTimeProperty("selectedCurrency", "IDR");
            }
            case UsdDebitAccount ->
            {
                Element.click(testConfig, accountItem, "USD Account Item");
                testConfig.putRunTimeProperty("selectedCurrency", "USD");
            }
            case SgdDebitAccount ->
            {
                Element.click(testConfig, accountItem, "SGD Account Item");
                testConfig.putRunTimeProperty("selectedCurrency", "SGD");
            }
            case AdvanceLimit -> Element.click(testConfig, advanceAccountItem, "Advance Account Item");
            default -> testConfig.logFail("Invalid account selected");
        }
        if (!isRadioButtonType)
            Element.click(testConfig, applyFilterButton, "Apply Filter Button");
        testConfig.putRunTimeProperty("numberOfRecord", Element.getText(testConfig, numberOfRecordsLabel, "Number of Records"));
    }

    public PreviewRecordPage previewRecord(SaasEnums.ExportModule module)
    {
        waitForLoaderDisappeared();
        WaitHelper.waitForElementToBeDisplayed(testConfig, numberOfRecordsLabel, "Number of record");
        testConfig.putRunTimeProperty("numberOfRecord", Element.getText(testConfig, numberOfRecordsLabel, "Number of Records"));
        WaitHelper.waitForElementToBeClickable(testConfig, previewRecordButton, "Preview Record Button");
        Element.click(testConfig, previewRecordButton, "Preview Record Button");
        return new PreviewRecordPage(testConfig, module);
    }

    public void selectExportFormat(SaasEnums.ExportFormat format)
    {
        WebElement formatButton = Element.getPageElementWithRetry(testConfig, Element.How.xPath, String.format(formatButtonxPath, format.toString()));
        WaitHelper.waitForElementToBeClickable(testConfig, formatButton, "Format Button");
        switch (format)
        {
            case Default ->
                    AssertHelper.assertElementIsDisplayed(testConfig, "Default Format Button", defaultFormatButton);
            case Xero, Netsuite -> Element.click(testConfig, formatButton, String.format("%s Format Button", format));
            case SeeAll -> Element.click(testConfig, seeAllFormatButton, "See All Format Button");
            default -> testConfig.logFail("Invalid format selected");
        }
    }

    public void selectIncludeAllAttachmentsCheckbox()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, includeAllAttachmentsCheckbox, "Include All Attachments Checkbox");
        Element.click(testConfig, includeAllAttachmentsCheckbox, "Include All Attachments Checkbox");
    }

    public void exportDataAndValidateExportedFile(SaasEnums.FileType fileType, SaasEnums.ExportModule module)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, exportButton, "Export Button");
        Element.click(testConfig, exportButton, "Export Button");
        WaitHelper.waitForElementToBeDisplayed(testConfig, exportSuccessfulTitle, "Export Successful Title");
        AssertHelper.assertElementText(testConfig, "Export Successful Title", "Export successful!", exportSuccessfulTitle);
        AssertHelper.assertElementText(testConfig, "Successful Message Details", "Your selected export was successfully generated and downloaded!", successfulMessageDetails);
        Element.click(testConfig, gotItButton, "Got It Button");
        String businessName = testConfig.testData.get("businessName").trim().replace(" ", "_").toLowerCase() + "_";
        String downloadsFolderPath = testConfig.getRunTimeProperty("resultsDirectory") + File.separator + "Downloads" + File.separator + testConfig.testcaseName;

        switch (module)
        {
            case AccountStatements -> businessName += testConfig.getRunTimeProperty("selectedCurrency").toLowerCase();
            case MoneyOut -> businessName += "expense";
            case Bills -> businessName += "bill";
            case Invoices -> businessName += "invoice";
            case MoneyIn -> businessName += "income";
            case Claims -> businessName += "claim";
            case Budgets -> businessName += "budget";
            case Cards -> businessName += "debit-card";
            default -> testConfig.logFail("Invalid module selected");
        }
        AssertHelper.compareTrue(testConfig, String.format("Check file downloaded: %s in folder: %s", businessName, downloadsFolderPath), CommonUtilities.findFileContainsName(testConfig, downloadsFolderPath, businessName, fileType.toString(), 3));
    }

    public void selectDueDateRange(SaasEnums.ExportDateRange dueDateRange)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, dueDateRangeDropdown, "Due Date Range Dropdown");
        Element.click(testConfig, dueDateRangeDropdown, "Due Date Range Dropdown");
        WaitHelper.waitForElementToBeClickable(testConfig, allItem, "All Item");
        switch (dueDateRange)
        {
            case All -> Element.click(testConfig, allItem, "All Item");
            case Last30Days -> Element.click(testConfig, last30DaysItem, "Last 30 Days Item");
            case Last90Days -> Element.click(testConfig, last90DaysItem, "Last 90 Days Item");
            case Custom -> Element.click(testConfig, customItem, "Custom Item");
            default -> testConfig.logFail("Invalid due date range selected");
        }
        WaitHelper.waitForElementToBeDisplayed(testConfig, previewRecordButton, "Preview Record button");
        testConfig.putRunTimeProperty("numberOfRecord", Element.getText(testConfig, numberOfRecordsLabel, "Number of Records"));
    }

    public void selectClaimStatusFilter(SaasEnums.ClaimStatus claimStatus)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, statusDropdown, "Claim Status Dropdown");
        Element.click(testConfig, statusDropdown, "Claim Status Dropdown");
        switch (claimStatus)
        {
            case All ->
            {
                for (WebElement item : Objects.requireNonNull(Element.getPageElementsWithRetry(testConfig, Element.How.css, allStatusItems)))
                {
                    WaitHelper.waitForElementToBeClickable(testConfig, item, "Status item");
                    Element.click(testConfig, item, "Status Item");
                }
            }

            case Paid, Rejected ->
                    Element.click(testConfig, Element.getPageElement(testConfig, Element.How.xPath, String.format(statusItem, claimStatus)), claimStatus.toString());
            default -> testConfig.logFail("Invalid bill status selected");
        }
        Element.click(testConfig, applyFilterButton, "Apply Filter Button");
    }

    public void filterExpiringSoonBudgets(boolean isExpiringSoon)
    {
        boolean isExpiringSoonFilterSelected = Element.isElementDisplayed(testConfig, expiringSoonFilterSelected);
        if (isExpiringSoon && isExpiringSoonFilterSelected)
        {
            testConfig.logComment("Expiring Soon filter is already selected");
        } else if (isExpiringSoon != isExpiringSoonFilterSelected)
        {
            WaitHelper.waitForElementToBeClickable(testConfig, expiringSoonFilter, "Expiring Soon Checkbox");
            Element.click(testConfig, expiringSoonFilter, "Expiring Soon Checkbox");
        }
    }

}
