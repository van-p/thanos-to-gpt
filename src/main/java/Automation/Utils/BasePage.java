package Automation.Utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BasePage
{
    protected final Config testConfig;
    @FindBy(xpath = "//div[(contains(@class, 'q-inner-loading') or contains(@class, 'example-loader')) or (contains(@class, 'el-loading-mask') and contains(@class, 'active'))]")
    private WebElement loadingIcon;
    @FindBy(xpath = "//div[(contains(@class, 'q-loading-bar') and @role='progressbar') or (contains(@class, 'el-loading')) or (contains(@class, 'inner-loading')) or (contains(@class, 'q-img__loading'))]")
    private WebElement loader;

    public BasePage(Config testConfig)
    {
        this.testConfig = testConfig;
    }

    protected void waitForLoaderDisappeared()
    {
        testConfig.logComment("Waiting for loader to disappear");
        WaitHelper.waitForOptionalElement(testConfig, loader, "Loader", 1);
        if (Element.isElementDisplayed(testConfig, loader))
            WaitHelper.waitForElementToBeHidden(testConfig, loader, "Loader");
        if (Element.isElementDisplayed(testConfig, loadingIcon))
            WaitHelper.waitForElementToBeHidden(testConfig, loadingIcon, "Loading icon");
    }
}
