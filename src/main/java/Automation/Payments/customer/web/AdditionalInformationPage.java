package Automation.Payments.customer.web;

import Automation.Payments.customer.helpers.PaymentStaticDataEn;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

public class AdditionalInformationPage
{

    private final Config testConfig;

    @FindBy(xpath = "//div[contains(@class,'text-h4')]")
    private WebElement tellUsMoreHeading;

    @FindBy(xpath = "//div[contains(@class,'text-subtitle1')]")
    private WebElement tellUsMoreSubHeading;

    @FindBy(xpath = "//input[@data-cy='transfer-additional-info-relationship-select-input']//parent::div/parent::div/following-sibling::div/i[contains(@class,'q-select__dropdown-icon')]")
    private WebElement selectRelationshipDropdown;

    @FindBy(xpath = "//input[@data-cy='transfer-additional-info-purpose-select-input']//parent::div/parent::div/following-sibling::div/i[contains(@class,'q-select__dropdown-icon')]")
    private WebElement purposeTransactionDropdown;

    @FindBy(xpath = "//input[contains(@class,'q-uploader__input')]")
    private WebElement uploadImageButton;

    @FindBy(xpath = "//button[@data-cy='transfer-additional-info-next-cta']")
    private WebElement nextButton;

    public AdditionalInformationPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, selectRelationshipDropdown);
        verifyAdditionalInformationPage();
    }

    public void verifyAdditionalInformationPage()
    {
        AssertHelper.compareEquals(testConfig, "Tell us more heading", PaymentStaticDataEn.AdditionalInformationForm.TellUsMoreHeading.getName(), Element.getText(testConfig, tellUsMoreHeading, "Tell us more heading"));
        AssertHelper.compareEquals(testConfig, "Tell us more subheading", PaymentStaticDataEn.AdditionalInformationForm.TellUsMoreSubHeading.getName(), Element.getText(testConfig, tellUsMoreSubHeading, "Tell us more subheading"));
    }

    public ReviewTransferPage fillAdditionalInformation()
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, selectRelationshipDropdown, "Select relationship dropdown");
        Element.click(testConfig, selectRelationshipDropdown, "Select relationship dropdown");
        WaitHelper.waitForSeconds(testConfig, 1);
        WebElement relationshipOption = Element.getPageElement(testConfig, How.xPath, "//span[text()=" + "\"" + testConfig.testData.get("recipientRelationship") + "\"]");
        Element.click(testConfig, relationshipOption, "Relationship option");
        WaitHelper.waitForElementToBeDisplayed(testConfig, purposeTransactionDropdown, "Transaction purpose dropdown");
        Element.click(testConfig, purposeTransactionDropdown, "Select Transaction purpose dropdown");
        WaitHelper.waitForSeconds(testConfig, 1);
        WebElement transferPurposeOption = Element.getPageElement(testConfig, How.xPath, "//span[text()=" + "\"" + testConfig.testData.get("transferPurposeOption") + "\"]");
        Element.click(testConfig, transferPurposeOption, "Transfer purpose option");
        String imagePath = System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Payments" + File.separator + "Images" + File.separator + "advanceTransferAdditionalInfoImage.jpg";
        Element.enterData(testConfig, uploadImageButton, imagePath, "Upload image");
        WaitHelper.waitForElementToBeClickable(testConfig, nextButton, "Next button");
        Element.click(testConfig, nextButton, "Next button");
        return new ReviewTransferPage(testConfig);
    }

}