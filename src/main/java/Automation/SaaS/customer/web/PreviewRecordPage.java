package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasEnums;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class PreviewRecordPage
{

    private final Config testConfig;
    @FindBy(css = "[data-cy=transactions-advance-record-modal-description]")
    WebElement previewRowItem;
    String previewRowItemCount = "[data-cy=transaction-list-item]";
    String billRowItemCount = "[data-cy=payable-list-table-row-item]";

    @FindBy(css = ".modal-page__back-button")
    private WebElement closeButton;
    @FindBy(css = ".transaction-list-desktop-header")
    private WebElement transactionListDesktopHeader;
    @FindBy(css = "tr[data-cy='payable-list-table-row-item']")
    private WebElement payableListDesktopHeader;
    @FindBy(css = ".cards-list-groups-header-desktop.cards-list-groups-header-desktop--left-gap.text-secondary")
    private WebElement cardListDesktopHeader;
    @FindBy(css = ".text-h4.text-weight-bolder.q-mt-none.q-mb-sm")
    private WebElement previewRecordModalHeading;
    @FindBy(css = ".transactions-advance-record-modal__description.text-weight-regular.q-mt-sm")
    private WebElement previewRecordModalDescription;
    @FindBy(css = ".transaction-list-desktop-header__type")
    private WebElement typeColumnHeader;
    @FindBy(css = ".transaction-list-desktop-header__counterparty")
    private WebElement counterpartyColumnHeader;
    @FindBy(css = ".transaction-list-desktop-header__user")
    private WebElement userColumnHeader;
    @FindBy(css = ".transaction-list-desktop-header__reference")
    private WebElement referenceColumnHeader;
    @FindBy(css = ".transaction-list-desktop-header__category")
    private WebElement categoryColumnHeader;
    @FindBy(css = ".transaction-list-desktop-header__attachment")
    private WebElement attachmentsColumnHeader;
    @FindBy(css = ".transaction-list-desktop-header__amount")
    private WebElement amountColumnHeader;
    @FindBy(css = "div[data-cy='invoice-list-table']")
    private WebElement invoiceListDesktopHeader;
    @FindBy(css = "div[data-cy='budgets-list-item-desktop']")
    private WebElement budgetsListDesktopHeader;
    @FindBy(css = "tr[data-cy='claim-list-table-item']")
    private WebElement claimListDesktopHeader;

    @FindBy(css = "th[data-cy='accounting-sync-table-header-bill_number']")
    private WebElement billNumberColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-state_code']")
    private WebElement statusColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-counterparty']")
    private WebElement recipientColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-due_date']")
    private WebElement billDueDateColumnHeader;
    @FindBy(css = "th[data-cy='accounting-sync-table-header-amount']")
    private WebElement billAmountColumnHeader;

    @FindBy(css = "body div th:nth-child(1)")
    private WebElement claimStatusColumnHeader;
    @FindBy(css = "body div th:nth-child(2)")
    private WebElement claimMemberColumnHeader;
    @FindBy(css = "body div th:nth-child(3)")
    private WebElement claimSubmittedOnColumnHeader;
    @FindBy(css = ".text-left.claim-table__budget-column")
    private WebElement claimSubmittedInColumnHeader;
    @FindBy(css = "body div th:nth-child(4)")
    private WebElement claimSubmittedByColumnHeader;
    @FindBy(css = "th[class='text-right']")
    private WebElement claimAmountColumnHeader;

    @FindBy(css = ".cards-list-groups-header-desktop.cards-list-groups-header-desktop--left-gap.text-secondary")
    private WebElement cardColumnHeader;
    @FindBy(css = "div[class='cards-list-group q-pt-md'] div:nth-child(2)")
    private WebElement cardDetailsColumnHeader;
    @FindBy(css = "div[class='cards-list-group q-pt-md'] div:nth-child(3)")
    private WebElement cardUserColumnHeader;
    @FindBy(css = "div[class='cards-list-group q-pt-md'] div:nth-child(4)")
    private WebElement cardBudgetColumnHeader;
    @FindBy(css = "div[class='cards-list-group q-pt-md'] div:nth-child(5)")
    private WebElement cardCategoryColumnHeader;
    @FindBy(css = "div[class='cards-list-group q-pt-md'] div:nth-child(6)")
    private WebElement cardStatusColumnHeader;
    @FindBy(css = ".cards-list-groups-header-desktop.q-pr-lg.text-secondary.cards-list-groups-header-desktop--frequency-column")
    private WebElement cardFrequencyColumnHeader;
    @FindBy(css = ".cards-list-groups-header-desktop.text-secondary.q-pr-lg.cards-list-groups-header-desktop--right-gap")
    private WebElement cardSpendCardLimitColumnHeader;
    @FindBy(css = "div[data-cy='cards-list-groups-card']")
    private WebElement cardList;

    @FindBy(css = ".row.justify-between.items-center.budgets-list-item-desktop__left-gap.cursor-pointer")
    private WebElement budgetIcon;
    @FindBy(css = ".budgets-list-header-desktop_item.text-secondary.budgets-list-header-desktop_item--left-gap")
    private WebElement budgetIconColumnHeader;
    @FindBy(css = "div[class='budgets-list-header-desktop'] div:nth-child(2)")
    private WebElement budgetNameColumnHeader;
    @FindBy(css = "div[class='budgets-list-header-desktop'] div:nth-child(3)")
    private WebElement budgetOwnerColumnHeader;
    @FindBy(css = "div[class='budgets-list-header-desktop'] div:nth-child(4)")
    private WebElement budgetMemberColumnHeader;
    @FindBy(css = "div[class='budgets-list-header-desktop'] div:nth-child(5)")
    private WebElement budgetStatusColumnHeader;
    @FindBy(css = "div[class='budgets-list-header-desktop'] div:nth-child(6)")
    private WebElement budgetFrequencyColumnHeader;
    @FindBy(css = "div[class='budgets-list-header-desktop'] div:nth-child(7)")
    private WebElement budgetSpendBudgetLimitColumnHeader;
    @FindBy(css = ".budget-spend-limit-bar")
    private WebElement budgetSpendLimitBar;

    @FindBy(css = ".q-table__container.q-table__card.q-table--no-wrap.invoice-table.invoice-table")
    private WebElement invoiceTable;
    @FindBy(css = "body div th:nth-child(1)")
    private WebElement invoiceNumberColumnHeader;
    @FindBy(css = "body div th:nth-child(2)")
    private WebElement invoiceStatusColumnHeader;
    @FindBy(css = "body div th:nth-child(3)")
    private WebElement invoiceCustomerColumnHeader;
    @FindBy(css = "body div th:nth-child(4)")
    private WebElement invoiceDateColumnHeader;
    @FindBy(css = "body div th:nth-child(5)")
    private WebElement invoiceDueDateColumnHeader;
    @FindBy(css = "body div th:nth-child(6)")
    private WebElement invoiceReminderColumnHeader;
    @FindBy(css = "body div th:nth-child(7)")
    private WebElement invoiceAmountColumnHeader;
    @FindBy(css = "body div th:nth-child(8)")
    private WebElement invoicePaymentColumnHeader;
    private final String claimRowItemCount = "[data-cy=claim-list-table-item]";
    private final String budgetRowItemCount = "[data-cy=budgets-list-item-desktop]";
    private final String invoiceRowItemCount = "[data-cy=invoice-list-table-item]";
    @FindBy(css = ".transaction-list-desktop-header__budget")
    private WebElement budgetColumnHeader;

    public PreviewRecordPage(Config testConfig, SaasEnums.ExportModule module)
    {
        this.testConfig = testConfig;
        PageFactory.initElements(testConfig.driver, this);
        verifyPreviewRecordPage(module);
    }

    private void verifyPreviewRecordPage(SaasEnums.ExportModule module)
    {
        switch (module)
        {
            case AccountStatements, MoneyOut -> WaitHelper.waitForPageLoad(testConfig, transactionListDesktopHeader);
            case Bills -> WaitHelper.waitForPageLoad(testConfig, payableListDesktopHeader);
            case Cards -> WaitHelper.waitForPageLoad(testConfig, cardListDesktopHeader);
            case Invoices -> WaitHelper.waitForPageLoad(testConfig, invoiceListDesktopHeader);
            case Budgets -> WaitHelper.waitForPageLoad(testConfig, budgetsListDesktopHeader);
            case Claims -> WaitHelper.waitForPageLoad(testConfig, claimListDesktopHeader);
            default -> testConfig.logFail("Unexpected module: " + module);
        }
        AssertHelper.assertElementText(testConfig, "Page heading", "Preview record(s)", previewRecordModalHeading);
        AssertHelper.assertElementIsDisplayed(testConfig, "Close button", closeButton);
        AssertHelper.compareEquals(testConfig, "Preview row item", String.format(SaasHelper.saasStaticDataBase.getFilteredNumberOfRecordsLabel(), testConfig.getRunTimeProperty("numberOfRecord")).split("\\.")[0], Element.getText(testConfig, previewRowItem, "Number of records desc").split("\\.")[0]);
    }

    public ExportDataPage validatePreviewTransactionsAndCloseModal(SaasEnums.ExportModule module)
    {
        switch (module)
        {
            case AccountStatements, MoneyOut -> validateAccountStatementsPreviewTransactions();
            case Bills -> validateBillPreviewTransactions();
            case Budgets -> validateBudgetsPreviewTransactions();
            case Claims -> validateClaimsPreviewTransactions();
            case Cards -> validateCardsPreviewTransactions();
            case Invoices -> validateInvoicesPreviewTransactions();
            case MoneyIn -> testConfig.logFail("MoneyIn module is not supported");
            default -> testConfig.logFail("Unexpected module: " + module);
        }
        Element.click(testConfig, closeButton, "Close button");
        return new ExportDataPage(testConfig);
    }

    private void validateAccountStatementsPreviewTransactions()
    {
        AssertHelper.assertElementText(testConfig, "Type column header", SaasHelper.saasStaticDataBase.getTypeHeader(), typeColumnHeader);
        AssertHelper.assertElementText(testConfig, "Counterparty column header", SaasHelper.saasStaticDataBase.getCounterpartyHeader(), counterpartyColumnHeader);
        AssertHelper.assertElementText(testConfig, "User column header", SaasHelper.saasStaticDataBase.getUserHeader(), userColumnHeader);
        AssertHelper.assertElementText(testConfig, "Reference column header", SaasHelper.saasStaticDataBase.getReferenceHeader(), referenceColumnHeader);
        AssertHelper.assertElementText(testConfig, "Category column header", SaasHelper.saasStaticDataBase.getCategoryHeader(), categoryColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget column header", SaasHelper.saasStaticDataBase.getBudgetHeader(), budgetColumnHeader);
        AssertHelper.assertElementText(testConfig, "Attachments column header", SaasHelper.saasStaticDataBase.getAttachmentsHeader(), attachmentsColumnHeader);
        AssertHelper.assertElementText(testConfig, "Amount column header", SaasHelper.saasStaticDataBase.getAmountHeader(), amountColumnHeader);
        AssertHelper.compareTrue(testConfig, "Preview row item display", !Objects.requireNonNull(Element.getPageElements(testConfig, Element.How.css, previewRowItemCount)).isEmpty());
    }

    private void validateBillPreviewTransactions()
    {
        AssertHelper.assertElementText(testConfig, "Bill number column header", SaasHelper.saasStaticDataBase.getBillNumberHeader(), billNumberColumnHeader);
        AssertHelper.assertElementText(testConfig, "Status column header", SaasHelper.saasStaticDataBase.getStatusHeader(), statusColumnHeader);
        AssertHelper.assertElementText(testConfig, "Recipient column header", SaasHelper.saasStaticDataBase.getRecipientHeader(), recipientColumnHeader);
        AssertHelper.assertElementText(testConfig, "Bill due date column header", SaasHelper.saasStaticDataBase.getBillDueDateHeader(), billDueDateColumnHeader);
        AssertHelper.assertElementText(testConfig, "Bill amount column header", SaasHelper.saasStaticDataBase.getBillAmountHeader(), billAmountColumnHeader);
        AssertHelper.compareTrue(testConfig, "Preview row item display", !Objects.requireNonNull(Element.getPageElements(testConfig, Element.How.css, billRowItemCount)).isEmpty());
    }

    private void validateBudgetsPreviewTransactions()
    {
        AssertHelper.assertElementText(testConfig, "Budget icon column header", SaasHelper.saasStaticDataBase.getBudgetIconHeader(), budgetIconColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget name column header", SaasHelper.saasStaticDataBase.getBudgetNameHeader(), budgetNameColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget owner column header", SaasHelper.saasStaticDataBase.getBudgetOwnerHeader(), budgetOwnerColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget member column header", SaasHelper.saasStaticDataBase.getBudgetMemberHeader(), budgetMemberColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget status column header", SaasHelper.saasStaticDataBase.getBudgetStatusHeader(), budgetStatusColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget frequency column header", SaasHelper.saasStaticDataBase.getBudgetFrequencyHeader(), budgetFrequencyColumnHeader);
        AssertHelper.assertElementText(testConfig, "Budget spend budget limit column header", SaasHelper.saasStaticDataBase.getBudgetSpendBudgetLimitHeader(), budgetSpendBudgetLimitColumnHeader);
        AssertHelper.assertElementIsDisplayed(testConfig, "Budget spend limit bar", budgetSpendLimitBar);
        AssertHelper.compareTrue(testConfig, "Preview row item display", !Objects.requireNonNull(Element.getPageElements(testConfig, Element.How.css, budgetRowItemCount)).isEmpty());
    }

    private void validateClaimsPreviewTransactions()
    {
        AssertHelper.assertElementText(testConfig, "Claim status column header", SaasHelper.saasStaticDataBase.getClaimStatusHeader(), claimStatusColumnHeader);
        AssertHelper.assertElementText(testConfig, "Claim member column header", SaasHelper.saasStaticDataBase.getClaimMemberHeader(), claimMemberColumnHeader);
        AssertHelper.assertElementText(testConfig, "Claim submitted on column header", SaasHelper.saasStaticDataBase.getClaimSubmittedOnHeader(), claimSubmittedOnColumnHeader);
        AssertHelper.assertElementText(testConfig, "Claim submitted in column header", SaasHelper.saasStaticDataBase.getClaimSubmittedInHeader(), claimSubmittedInColumnHeader);
        AssertHelper.assertElementText(testConfig, "Claim submitted by column header", SaasHelper.saasStaticDataBase.getClaimSubmittedByHeader(), claimSubmittedByColumnHeader);
        AssertHelper.assertElementText(testConfig, "Claim amount column header", SaasHelper.saasStaticDataBase.getClaimAmountHeader(), claimAmountColumnHeader);
        AssertHelper.compareTrue(testConfig, "Preview row item display", !Objects.requireNonNull(Element.getPageElements(testConfig, Element.How.css, claimRowItemCount)).isEmpty());
    }

    private void validateCardsPreviewTransactions()
    {
        AssertHelper.assertElementText(testConfig, "Card column header", SaasHelper.saasStaticDataBase.getCardHeader(), cardColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card details column header", SaasHelper.saasStaticDataBase.getCardDetailsHeader(), cardDetailsColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card user column header", SaasHelper.saasStaticDataBase.getCardUserHeader(), cardUserColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card budget column header", SaasHelper.saasStaticDataBase.getCardBudgetHeader(), cardBudgetColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card category column header", SaasHelper.saasStaticDataBase.getCardCategoryHeader(), cardCategoryColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card status column header", SaasHelper.saasStaticDataBase.getCardStatusHeader(), cardStatusColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card frequency column header", SaasHelper.saasStaticDataBase.getCardFrequencyHeader(), cardFrequencyColumnHeader);
        AssertHelper.assertElementText(testConfig, "Card spend card limit column header", SaasHelper.saasStaticDataBase.getCardSpendCardLimitHeader(), cardSpendCardLimitColumnHeader);
        AssertHelper.assertElementIsDisplayed(testConfig, "Card list", cardList);
    }

    private void validateInvoicesPreviewTransactions()
    {
        AssertHelper.assertElementText(testConfig, "Invoice number column header", SaasHelper.saasStaticDataBase.getInvoiceNumberHeader(), invoiceNumberColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice status column header", SaasHelper.saasStaticDataBase.getInvoiceStatusHeader(), invoiceStatusColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice customer column header", SaasHelper.saasStaticDataBase.getInvoiceCustomerHeader(), invoiceCustomerColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice date column header", SaasHelper.saasStaticDataBase.getInvoiceDateHeader(), invoiceDateColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice due date column header", SaasHelper.saasStaticDataBase.getInvoiceDueDateHeader(), invoiceDueDateColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice amount column header", SaasHelper.saasStaticDataBase.getInvoiceAmountHeader(), invoiceAmountColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice reminder column header", SaasHelper.saasStaticDataBase.getInvoiceReminderHeader(), invoiceReminderColumnHeader);
        AssertHelper.assertElementText(testConfig, "Invoice payment column header", SaasHelper.saasStaticDataBase.getInvoicePaymentHeader(), invoicePaymentColumnHeader);
        AssertHelper.compareTrue(testConfig, "Preview row item display", !Objects.requireNonNull(Element.getPageElements(testConfig, Element.How.css, invoiceRowItemCount)).isEmpty());
    }

}
