package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentEnums.MakerCheckerFlowType;
import Automation.Payments.customer.helpers.PaymentEnums.TransferTitleEnum;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.Payments.customer.helpers.PaymentStaticDataEn;
import Automation.Payments.customer.helpers.PaymentStaticDataEn.SuccessMessageEnums;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Objects;

public class TransactionsPage
{

    private final Config testConfig;

    @FindBy(xpath = "//div[@data-cy='transaction-page-header-title']")
    private WebElement TransactionsPageTitle;
    @FindBy(xpath = "//button[@data-cy='transaction-list-type-selector-past']")
    private WebElement pastTab;
    @FindBy(xpath = "//button[@data-cy='transaction-list-type-selector-upcoming']")
    private WebElement upcomingTab;
    @FindBy(xpath = "//button[@data-cy='transaction-list-type-selector-pending']")
    private WebElement pendingTab;
    @FindBy(xpath = "//input[@data-cy='example-filter-search-input']")
    private WebElement searchTextBox;
    @FindBy(xpath = "//div[@data-cy='marker-pending-list-item-checkbox']")
    private WebElement checkboxOfPendingTransfer;
    @FindBy(xpath = "//div[@data-cy='pending-transactions-list-item']")
    private WebElement pendingTransferRowItem;
    @FindBy(xpath = "//div[@data-cy='select-transactions-select-all-checkbox']")
    private WebElement checkboxToSelectAllTransfers;
    @FindBy(xpath = "//button[@data-cy='pending-transactions-list-maker-delete-cta']")
    private WebElement deleteButtonOnPendingTab;
    @FindBy(xpath = "//button[@data-cy='bulk-rejection-modal-cta']")
    private WebElement deleteConfirmButtonOnPendingTab;
    @FindBy(xpath = "//div[@class='q-notification__message col']")
    private WebElement successMessageOnPendingTab;
    @FindBy(xpath = "//button[@data-cy='pending-transactions-list-approve']")
    private WebElement approveButtonOnPendingTab;
    @FindBy(xpath = "//button[@data-cy='bulk-approval-modal-cta']")
    private WebElement approveConfirmButtonOnPendingTab;
    @FindBy(css = "div.verify-otp-form__input")
    private WebElement otpForm;
    @FindBy(xpath = "//input[@data-cy='digit-input-real']")
    private WebElement otpActivateCodeTextBox;
    @FindBy(xpath = "//div[@class='transactions-group__transaction-list']/div")
    private List<WebElement> pastTransactionsList;
    @FindBy(xpath = "//div[@data-cy='transaction-list-item-row-data-reference']")
    private WebElement pendingTransferReference;
    @FindBy(xpath = "//div[@data-cy='transaction-list-item-row-data-reference']")
    private List<WebElement> referenceRowItemList;
    @FindBy(xpath = "//div[@class='pending-transactions-list__action-group pending-transactions-list__action-group--first-group pending-transactions-list__action-group--last-group']/div")
    private List<WebElement> pendingTransactionsList;
    @FindBy(xpath = "//button[@data-cy='pending-transfer-details-page-more']")
    private WebElement moreButtonOnTransactionDetailScreen;
    @FindBy(xpath = "//div[@data-cy='modify-maker-payment-modal-modify']")
    private WebElement modifyButtonOnTransactionDetailScreen;
    @FindBy(xpath = "//div[@data-cy='transaction-list-item-row-data-counterparty-name']")
    private List<WebElement> counterPartyNameList;
    @FindBy(xpath = "//span[@data-cy='transaction-list-item-amount-amount']")
    private List<WebElement> transferAmountList;
    @FindBy(xpath = "//div[@data-cy='transaction-list-item-amount']//span[contains(@class,'sign')]")
    private List<WebElement> transferAmountSignList;
    @FindBy(xpath = "//div[@data-cy='transaction-list-item-amount']//span[contains(@class,'text-body')]")
    private List<WebElement> transferAmountCurrencyList;
    @FindBy(xpath = "//div[@data-cy='transaction-list-item-row-data-counterparty-name']")
    private WebElement pendingTransferCounterparty;
    @FindBy(xpath = "//span[@data-cy='transaction-list-item-amount-amount']")
    private WebElement pendingTransferSourceAmount;
    @FindBy(xpath = "//button[@data-cy='maker-payment-details-page-modify-cta']")
    private WebElement manageTransferButtonOnTransferDetailOnPendingTab;
    @FindBy(xpath = "//div[@data-cy='modify-maker-payment-modal-modify']")
    private WebElement manageTransferOptionOnPendingTab;
    @FindBy(xpath = "//button[@data-cy='pending-transactions-list-reject']")
    private WebElement rejectButtonOnPendingTab;
    @FindBy(xpath = "//button[@data-cy='bulk-rejection-modal-cta']")
    private WebElement rejectConfirmButtonOnPendingTab;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;
    @FindBy(xpath = "//div[@data-cy='transaction-details-title']")
    private WebElement transactionDetailTitle;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-non-mca-amount']//div")
    private WebElement transactionDetailAmount;
    @FindBy(xpath = "//div[@data-cy='transactions-details-one-column-amount']//following-sibling::div/div[1]//div[1]")
    private WebElement transactionFailedText1;
    @FindBy(xpath = "//div[@data-cy='transactions-details-one-column-amount']//following-sibling::div/div[1]//div[2]")
    private WebElement transactionFailedText2;
    @FindBy(xpath = "//div[@data-cy='horizontal-stepper-step-item-text-local-transaction-failed']")
    private WebElement transactionFailedText;
    @FindBy(xpath = "//div[@data-cy='transactions-details-one-column-recipient-name']")
    private WebElement transactionDetailRecipientName;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-reference']")
    private WebElement transactionDetailReferenceName;
    @FindBy(xpath = "//div[@data-cy='transaction-details-one-column-transfer-fee']")
    private WebElement transactionDetailTransferFee;
    @FindBy(xpath = "//div[@data-cy='desktop-modal-header-close-button']")
    private WebElement transactionDetailCloseButton;
    @FindBy(xpath = "//div[@class='pending-transactions-list__container']//div[contains(., 'No results found with ')]")
    private WebElement noResultFoundMessageOnPendingTab;
    @FindBy(css = ".transactions-list__empty-transaction-text")
    private WebElement noResultFoundMessageOnPastTab;
    @FindBy(xpath = "//button[@data-cy='pending-transfer-details-page-approve-button']")
    private WebElement approveButtonOnTransferDetailsSideScreen;
    @FindBy(xpath = "//button[@data-cy='approve-maker-payment-modal-approve']")
    private WebElement approveConfirmButtonOnTransferDetailsSideScreen;
    @FindBy(xpath = "//button[@data-cy='exchange-rate-updated-modal-cta']")
    private WebElement exchangeRateUpdatedConfirmationOkButton;
    @FindBy(xpath = "//div[@data-cy='transfer-step-completed-with-notification-header-title']")
    private WebElement transferTitleAfterApprovalFromTransferDetailsScreen;


    public TransactionsPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForElementToBeDisplayed(testConfig, searchTextBox, "Search Text box");
        verifyTransactionsPage();
    }

    public void verifyTransactionsPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Past Tab", pastTab);
        AssertHelper.assertElementIsDisplayed(testConfig, "Past Tab", upcomingTab);
        AssertHelper.assertElementIsDisplayed(testConfig, "Pending Tab", pendingTab);
    }

    public void goToPendingTab()
    {
        Element.click(testConfig, pendingTab, "Pending tab");
    }

    public void goToPastTab()
    {
        Element.click(testConfig, pastTab, "Past tab");
    }

    public int findTransactionInListByReference(String reference)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, pendingTransferReference, "Reference row item");
        int i = 0;
        for (i = 0; i < referenceRowItemList.size(); i++)
        {
            if (Element.getText(testConfig, referenceRowItemList.get(i), "Reference Value").equalsIgnoreCase(reference))
            {
                break;
            }
        }
        if (i == referenceRowItemList.size())
        {
            testConfig.logFail("Searched Transaction is not found in past tab: " + reference);
        }
        return i;
    }

    public void verifyFailedTransactionOnPastTabList(String reference)
    {
        int index = findTransactionInListByReference(reference);
        AssertHelper.compareTrue(testConfig, "Reference of rollback transaction", Element.getText(testConfig, referenceRowItemList.get(index - 1),
                "Rollback Transaction reference").contains("Rollback of transaction"));
        AssertHelper.compareEquals(testConfig, "Verifying CounterParty names", Element.getText(testConfig, counterPartyNameList.get(index), "Counter part name of original txn"),
                Element.getText(testConfig, counterPartyNameList.get(index - 1), "Counter part name of rollback txn"));
        AssertHelper.compareEquals(testConfig, "Verifying Amount", Element.getText(testConfig, transferAmountList.get(index), "Amount of original txn"),
                Element.getText(testConfig, transferAmountList.get(index - 1), "Amount of rollback txn"));
        AssertHelper.compareEquals(testConfig, "Verifying Currency", Element.getText(testConfig, transferAmountCurrencyList.get(index), "Currency of original txn"),
                Element.getText(testConfig, transferAmountCurrencyList.get(index - 1), "Currency of rollback txn"));
        AssertHelper.compareEquals(testConfig, "Verifying sign of original transfer", Element.getText(testConfig, transferAmountSignList.get(index), "Sign of original txn"), "–");
        AssertHelper.compareEquals(testConfig, "Verifying sign of rollback transfer", Element.getText(testConfig, transferAmountSignList.get(index - 1), "Sign of rollback txn"), "+");
        verifyFailedTransfer(index);

    }

    public void verifyRollbackTransactionOnPastTabList(String reference)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, pendingTransferReference, "Reference row item");
        int i = 0;
        for (i = 0; i < referenceRowItemList.size(); i++)
        {
            if (Element.getText(testConfig, referenceRowItemList.get(i), "Reference Value").contains(reference))
            {
                break;
            }
        }
        if (i == referenceRowItemList.size())
        {
            testConfig.logFail("Searched Transaction is not found in past tab: " + reference);
        }
        verifyReceivedTransfer(i);

    }

    public void verifyFailedTransfer(int index)
    {
        Element.click(testConfig, referenceRowItemList.get(index), "Click on Failed transfer");
        WaitHelper.waitForElementToBeDisplayed(testConfig, transactionDetailTitle, "Transaction Detail Title");
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transaction Detail Title", Element.getText(testConfig, transactionDetailTitle,
                "Get Transaction Detail Title"), "Transfer Failed");
        String transferAmount = Element.getText(testConfig, transactionDetailAmount, "Get Transaction Detail Amount");
        String[] amount = transferAmount.trim().split(" ");
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer currency", amount[0], testConfig.testData.get("transferCurrency"));
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer amount", amount[1].replaceAll("\\.", "").replaceAll(",", ""), testConfig.testData.get("amount"));
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer text1", Element.getText(testConfig, transactionFailedText1, "Failed Transfer text1"), PaymentStaticDataEn.FailedTransferDetailPage.title.getName());
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer text2", Element.getText(testConfig, transactionFailedText2, "Failed Transfer text2"), PaymentStaticDataEn.FailedTransferDetailPage.subtitle.getName());
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer Icon", Element.getText(testConfig, transactionFailedText, "Failed icon text"), "Failed");
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer Recipient name", Element.getText(testConfig, transactionDetailRecipientName, "Recipient name"), testConfig.testData.get("recipientName"));
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer Reference Name", Element.getText(testConfig, transactionDetailReferenceName, "Reference"), testConfig.testData.get("reference"));
        String transferFee = Element.getText(testConfig, transactionDetailTransferFee, "Transfer fee");
        int fee = Integer.parseInt(transferFee.split(" ")[1].replaceAll("\\.", ""));
        AssertHelper.compareEquals(testConfig, "Verifying Failed Transfer Fee", fee, 0);
        Element.click(testConfig, transactionDetailCloseButton, "Close button");
        WaitHelper.waitForElementToBeHidden(testConfig, transactionFailedText1, "Failed Transfer text1");
        WaitHelper.waitForElementToBeClickable(testConfig, pendingTransferReference, "Reference row item");
    }

    public void verifyReceivedTransfer(int index)
    {
        WaitHelper.waitForSeconds(testConfig, 1);
        Element.click(testConfig, referenceRowItemList.get(index), "Received transfer");
        WaitHelper.waitForElementToBeDisplayed(testConfig, transactionDetailRecipientName, "Transaction Detail Recipient name");
        AssertHelper.compareEquals(testConfig, "Verifying Received Transaction Detail Title", Element.getText(testConfig, transactionDetailTitle,
                "Get Transaction Detail Title"), "Transfer received");
        String transferAmount = Element.getText(testConfig, transactionDetailAmount, "Get Transaction Detail Amount");
        String[] amount = transferAmount.trim().split(" ");
        testConfig.logComment(amount.toString());
        AssertHelper.compareEquals(testConfig, "Verifying Received Transfer currency", amount[0], testConfig.testData.get("transferCurrency"));
        AssertHelper.compareEquals(testConfig, "Verifying Received Transfer amount", amount[1].replaceAll("\\.", "").replaceAll(",", ""), testConfig.testData.get("amount"));
        AssertHelper.compareEquals(testConfig, "Verifying Received Transfer Recipient name", Element.getText(testConfig, transactionDetailRecipientName,
                "Received Transfer Recipient name"), testConfig.testData.get("recipientName"));
        AssertHelper.compareContains(testConfig, "Verifying Received Transfer Reference Name", "Rollback of transaction", Element.getText(testConfig, transactionDetailReferenceName,
                "Received Transfer Reference Name"));
    }

    public void enterTextInSearchBox(String reference)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, searchTextBox, "SearchBox");
        Element.enterData(testConfig, searchTextBox, reference, "Reference in search text box");
        WaitHelper.waitForOptionalElement(testConfig, loader, "loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "loader");
    }

    public void validatePendingTransferDetails()
    {
        AssertHelper.assertElementText(testConfig, "Reference", testConfig.testData.get("reference"), pendingTransferReference);
        AssertHelper.assertElementText(testConfig, "Amount", Integer.parseInt(testConfig.testData.get("sourceAmount")) / 100 + ".00", pendingTransferSourceAmount);
        AssertHelper.assertElementText(testConfig, "Counterparty Name", testConfig.testData.get("recipientName"), pendingTransferCounterparty);
    }

    public void checkPendingTabCheckbox()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, checkboxOfPendingTransfer, "Checkbox");
        Element.click(testConfig, checkboxOfPendingTransfer, "Pending transfer checkbox");
    }

    public void clickPendingTransfer(String transferReference)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, pendingTransferRowItem, "pending transfer row");
        int pendingTransfer = 0;
        for (WebElement reference : referenceRowItemList)
        {
            if (reference.getText().equals(transferReference))
            {
                Element.click(testConfig, pendingTransferRowItem, "Pending transfer");
                pendingTransfer = pendingTransfer + 1;
            }
        }
        if (pendingTransfer == 0)
        {
            testConfig.logFail("Pending transfer not found in the Pending list");
        }
    }

    public void clickMoreButtonOnTransferDetailsScreen()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, moreButtonOnTransactionDetailScreen, "More button on Transfer details side screen");
        Element.click(testConfig, moreButtonOnTransactionDetailScreen, "More button on Transfer details side screen");
    }

    public ModifyPendingTransferPage clickModifyButtonOnTransferDetailsScreen()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, modifyButtonOnTransactionDetailScreen, "Modify button");
        WaitHelper.waitForElementToBeClickable(testConfig, modifyButtonOnTransactionDetailScreen, "Modify button on Transfer details side screen");
        Element.click(testConfig, modifyButtonOnTransactionDetailScreen, "Modify button on Transfer details side screen");
        return new ModifyPendingTransferPage(testConfig);
    }

    public void deletePendingTransferOnPendingTab()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, deleteButtonOnPendingTab, "Delete button");
        Element.click(testConfig, deleteButtonOnPendingTab, "Delete button on Pending tab");

        WaitHelper.waitForElementToBeClickable(testConfig, deleteConfirmButtonOnPendingTab, "Delete Confirm button");
        Element.click(testConfig, deleteConfirmButtonOnPendingTab, "Delete confirm button");

        WaitHelper.waitForElementToBeDisplayed(testConfig, successMessageOnPendingTab, "Delete Success Message");
        AssertHelper.assertElementText(testConfig, "Validate Delete transfer success message", SuccessMessageEnums.TransferDeleted.getName(), successMessageOnPendingTab);
    }

    public void approveTransferOnPendingTab(boolean isAdvanceTransfer)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, approveButtonOnPendingTab, "Approve button");
        Element.click(testConfig, approveButtonOnPendingTab, "Approve button on Pending tab");

        WaitHelper.waitForElementToBeClickable(testConfig, approveConfirmButtonOnPendingTab, "Approve Confirm button");
        Element.click(testConfig, approveConfirmButtonOnPendingTab, "Approve confirm button");
        if (!isAdvanceTransfer)
        {
            enterOTP();
        }
    }

    public void validateApproveSuccessMessage()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, successMessageOnPendingTab, "Approve Success Message");
        AssertHelper.assertElementText(testConfig, "Validate Approve transfer success message", SuccessMessageEnums.TransferApproved.getName(), successMessageOnPendingTab);
    }

    public void validatePendingTransferNotPresentInPendingTabAfterApproval()
    {
        AssertHelper.assertElementIsNotDisplayed(testConfig, "Approved Pending transfer", pendingTransferRowItem);
    }

    public void validateBulkApproveSuccessMessage()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, successMessageOnPendingTab, "Bulk Approve Success Message");
        AssertHelper.assertElementText(testConfig, "Validate Bulk Approve transfer success message", SuccessMessageEnums.TransfersBulkApprove.getName(), successMessageOnPendingTab);
        WaitHelper.waitForElementToBeHidden(testConfig, successMessageOnPendingTab, "Success Message on Pending tab");
    }

    public void enterOTP()
    {
        WaitHelper.waitForOptionalElement(testConfig, otpForm, "OTP Form");
        if (Element.isElementDisplayed(testConfig, otpForm))
        {
            Element.enterDataWithoutSelectedElement(testConfig, PaymentHelper.paymentStaticDataBase.getOTP(), "OTP Code");
        }
    }

    public void checkSelectAllCheckBox()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, checkboxOfPendingTransfer, "Checkbox");
        Element.click(testConfig, checkboxToSelectAllTransfers, "Select All transactions checkbox");
    }

    public void validateApprovedTransactionsPresentInPastTab(int transfers)
    {
        AssertHelper.compareEquals(testConfig, "Past Transactions List size", transfers, pastTransactionsList.size());
    }

    public void validateApprovedTransactionAmountInPastTab(String expectedAmount)
    {
        String amountText = Element.getText(testConfig, transferAmountList.get(0), "Amount of original txn");
        amountText = amountText.replaceAll(",", "").replaceAll("\\.", "");
        AssertHelper.compareEquals(testConfig, "Verifying Amount", Integer.parseInt(expectedAmount), Integer.parseInt(amountText) / 100);

    }

    public void rejectTransferOnPendingTab()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, rejectButtonOnPendingTab, "Reject button");
        Element.click(testConfig, rejectButtonOnPendingTab, "Reject button on Pending tab");

        WaitHelper.waitForElementToBeClickable(testConfig, rejectConfirmButtonOnPendingTab, "Reject Confirm button");
        Element.click(testConfig, rejectConfirmButtonOnPendingTab, "Reject confirm button");
        enterOTP();
    }

    public void validateRejectSuccessMessage()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, successMessageOnPendingTab, "Reject Success Message");
        AssertHelper.assertElementText(testConfig, "Validate Reject transfer success message", SuccessMessageEnums.TransferRejected.getName(), successMessageOnPendingTab);
    }

    public void validateNotFoundMessageOnPendingTab(String referenceValue)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, noResultFoundMessageOnPendingTab, "No Result Found Message");
        AssertHelper.assertElementText(testConfig, "Validate No Result Found Message on Pending tab", SuccessMessageEnums.NotResultFound.getName() + " " + "'" + referenceValue + "'.", noResultFoundMessageOnPendingTab);
    }

    public void validateNotFoundMessageOnPastTab(String referenceValue)
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Validate No transaction on Past Tab", noResultFoundMessageOnPastTab);
        AssertHelper.assertElementText(testConfig, "Validate No transaction on past tab", SuccessMessageEnums.NoTransaction.getName(), noResultFoundMessageOnPastTab);
    }

    private void approveTransferFromTransferDetailsSidePanel(MakerCheckerFlowType flowType)
    {
        if (Objects.requireNonNull(flowType) == MakerCheckerFlowType.Approve)
        {
            WaitHelper.waitForElementToBeClickable(testConfig, approveButtonOnTransferDetailsSideScreen, "Approve button on Transfer Details Side Panel");
            Element.click(testConfig, approveButtonOnTransferDetailsSideScreen, "Approve button on Transfer Details Side Panel");
            WaitHelper.waitForOptionalElement(testConfig, exchangeRateUpdatedConfirmationOkButton, "Ok got it button on exchange rate updated confirmation popup");
            if (Element.isElementDisplayed(testConfig, exchangeRateUpdatedConfirmationOkButton))
            {
                Element.click(testConfig, exchangeRateUpdatedConfirmationOkButton, "Ok got it button on exchange rate updated confirmation popup");
                WaitHelper.waitForElementToBeClickable(testConfig, approveButtonOnTransferDetailsSideScreen, "Approve button on Transfer Details Side Panel");
                Element.click(testConfig, approveButtonOnTransferDetailsSideScreen, "Approve button on Transfer Details Side Panel");
            }
            WaitHelper.waitForElementToBeClickable(testConfig, approveConfirmButtonOnTransferDetailsSideScreen, "Approve confirm button on Transfer Details Side Panel");
            Element.click(testConfig, approveConfirmButtonOnTransferDetailsSideScreen, "Approve confirm button on Transfer Details Side Panel");
            enterOTP();
            WaitHelper.waitForOptionalElement(testConfig, loader, "loader");
            WaitHelper.waitForElementToBeHidden(testConfig, loader, "loader");
        } else
        {
            testConfig.logFail("The 'Maker Checker' flow type option is not defined yet.");
        }
    }

    private void validateTransferAfterApprovalFromTransferDetailsSidePanel()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, transferTitleAfterApprovalFromTransferDetailsScreen, "Transfer title after approval");
        AssertHelper.assertElementText(testConfig, "Transfer Title", TransferTitleEnum.TransferInitiated.getName(), transferTitleAfterApprovalFromTransferDetailsScreen);
    }

    private void clickCloseIconOnTransferDetailsSidePanel()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, transactionDetailCloseButton, "Close Icon on Transfer Details Side Panel");
        Element.click(testConfig, transactionDetailCloseButton, "Close Icon on Transfer Details Side Panel");
    }

    public void approveTransferFromTransferDetailsSidePanelOnPendingTab(MakerCheckerFlowType flowType, String reference)
    {
        enterTextInSearchBox(reference);
        validatePendingTransferDetails();
        clickPendingTransfer(reference);
        approveTransferFromTransferDetailsSidePanel(flowType);
        validateTransferAfterApprovalFromTransferDetailsSidePanel();
        clickCloseIconOnTransferDetailsSidePanel();
    }

    public void validatePendingTransferNotPresentInPendingTabAfterApprovalAndIsDisplayedInPastTab(String reference, int transfers)
    {
        enterTextInSearchBox(reference);
        validatePendingTransferNotPresentInPendingTabAfterApproval();
        goToPastTab();
        enterTextInSearchBox(reference);
        validateApprovedTransactionsPresentInPastTab(transfers);
    }
}