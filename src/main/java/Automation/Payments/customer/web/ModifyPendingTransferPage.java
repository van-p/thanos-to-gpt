package Automation.Payments.customer.web;

import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class ModifyPendingTransferPage
{

    private final Config testConfig;
    private final String modifyAmountTextbox = "//input[@data-cy='source-amount-input-input-box-input']";
    @FindBy(xpath = "//button[@data-cy='transfer-step-make-transaction-submit-button']")
    private WebElement nextButton;
    @FindBy(xpath = "//input[@data-cy='source-amount-input-input-box-input']")
    private WebElement amountTextBox;

    public ModifyPendingTransferPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, nextButton);
        verifyModifyPendingTransferPage();
    }

    public void verifyModifyPendingTransferPage()
    {
        AssertHelper.assertElementIsDisplayed(testConfig, "Amount text box", amountTextBox);
        AssertHelper.assertElementIsDisplayed(testConfig, "Next button", nextButton);
    }

    public void modifyAmount(String updatedAmount)
    {
        WaitHelper.waitForElementToBeClickable(testConfig, amountTextBox, "amount text box");
        Element.enterData(testConfig, amountTextBox, updatedAmount, "Updated amount");
        String recipientName = Element.getText(testConfig, How.xPath, modifyAmountTextbox, "Modify Amount");
        testConfig.putRunTimeProperty("modifyAmount", Objects.requireNonNull(recipientName));
    }

    public ReviewTransferPage clickNextButton()
    {
        WaitHelper.waitForElementToBeClickable(testConfig, nextButton, "next button");
        Element.click(testConfig, nextButton, "next button");
        WaitHelper.waitForElementToBeHidden(testConfig, nextButton, "next button");
        return new ReviewTransferPage(testConfig);
    }
}