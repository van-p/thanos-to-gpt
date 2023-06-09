package Automation.SaaS.customer.web;

import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Config;
import Automation.Utils.Element;
import Automation.Utils.Element.How;
import Automation.Utils.WaitHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

public class BillPage
{

    private final String renderImgViewer = "//img[@data-cy='invoice-file-viewer-image-renderer']";
    @FindBy(xpath = "//button[@data-cy='payable-page-header-add-new-button']")
    private WebElement newBillBtn;

    @FindBy(xpath = "//button[@data-cy='payable-invoice-upload-card-skip-button']")
    private WebElement skipToUploadBillBtn;

    @FindBy(xpath = "//div[@data-cy='example-header-teleport-titles']//div[contains(@class,'text-weight-bolder')]")
    private WebElement billPageTitle;

    @FindBy(xpath = "//div[@data-cy='invoice-mini-insights-desktop-item-title' and contains(@class,'text-tertiary')][1]")
    private WebElement totalUnpaidLabel;

    @FindBy(css = "div[style='color: rgb(245, 0, 87);'")
    private WebElement overDueLabel;

    @FindBy(css = "div[style='color: rgb(178, 138, 0);")
    private WebElement dueLabel;

    @FindBy(css = "input[class='q-uploader__input overflow-hidden absolute-full']")
    private WebElement uploadFileUpInput;
    @FindBy(xpath = "//button[@data-cy='small-confirm-cancel-modal-button-confirm']")
    private WebElement confirmYesBtn;
    @FindBy(xpath = "//img[@data-cy='invoice-file-viewer-image-renderer']")
    private WebElement imgRenderViewer;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;


    private final Config testConfig;

    public BillPage(Config config)
    {
        this.testConfig = config;
        PageFactory.initElements(testConfig.driver, this);
        WaitHelper.waitForPageLoad(testConfig, newBillBtn);
        verifyBillPage();
    }

    private void verifyBillPage()
    {
        AssertHelper.assertElementText(testConfig, "Add new button", SaasHelper.saasStaticDataBase.getAddNewButtonLabel(), newBillBtn);
        AssertHelper.assertElementText(testConfig, "Bill page title", SaasHelper.saasStaticDataBase.getBillPageTitle(), billPageTitle);
        AssertHelper.assertElementText(testConfig, "Total unpaid label", SaasHelper.saasStaticDataBase.getTotalUnpaidLabel(), totalUnpaidLabel);
        AssertHelper.assertElementText(testConfig, "Over due label", SaasHelper.saasStaticDataBase.getOverdueLabel(), overDueLabel);
        AssertHelper.assertElementText(testConfig, "Due label", SaasHelper.saasStaticDataBase.getDueLabel(), dueLabel);
    }

    public AddNewBillPage openAddNewBillPageSkipUploadFile()
    {
        Element.click(testConfig, this.newBillBtn, "Open new Bill Page", false);
        WaitHelper.waitForElementToBeClickable(testConfig, this.skipToUploadBillBtn, "New User(s) button");
        Element.click(testConfig, this.skipToUploadBillBtn, "Skip to upload a bill", false);
        return new AddNewBillPage(testConfig);
    }

    public AddNewBillPage openAddNewBillPageAndUploadFile(String fileName)
    {
        Element.click(testConfig, this.newBillBtn, "Open new Bill Page", false);
        WaitHelper.waitForElementToBeDisplayed(testConfig, skipToUploadBillBtn, "");
        Element.enterData(testConfig, uploadFileUpInput, System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "SaaS" + File.separator + "Images" + File.separator + fileName, "description");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        WaitHelper.waitForElementToBeDisplayed(testConfig, confirmYesBtn, "");
        Element.click(testConfig, confirmYesBtn, "", true);
        WaitHelper.waitForElementToLoadWithGivenPropertyValue(testConfig, How.xPath, renderImgViewer, "alt", fileName);
        return new AddNewBillPage(testConfig);
    }
}

