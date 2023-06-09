package Automation.Utils;

import Automation.Utils.Api.ApiDetails.ApiRequestType;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.Enums.BrowserName;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.PageFactory;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Browser
{

    public static String seleniumGrid = "http://10.100.11.30:4444";

    public static void navigateToUrl(Config testConfig, String url)
    {
        navigateToUrl(testConfig, url, null);
    }

    @SuppressWarnings("static-access")
    public static void navigateToUrl(Config testConfig, String url, List<Cookie> cookies)
    {
        try
        {
            // This is to handles the cases when we are browsing local files
            url = CommonUtilities.convertFilePathToHtmlUrl(url);

            if (testConfig.driver == null)
            {
                if (testConfig.useSingleBrowser && testConfig.staticDriver != null)
                {
                    testConfig.driver = testConfig.staticDriver;
                } else
                {
                    testConfig.driver = openBrowser(testConfig);
                    if (testConfig.useSingleBrowser)
                    {
                        testConfig.staticDriver = testConfig.driver;
                    }
                }
            }
            testConfig.logComment("Navigating to URL : " + url);
            testConfig.driver.get(url);
            addCookies(testConfig, cookies);
        } catch (Exception e)
        {
            testConfig.logException("NavigateToUrl failed, so trying again...", e);
            if (Config.isRemoteExecution)
            {
                closeBrowser(testConfig);
                testConfig.driver = openBrowser(testConfig);
            }
            testConfig.driver.get(url);
            addCookies(testConfig, cookies);
        }
    }

    private static WebDriver openBrowser(Config testConfig)
    {
        WebDriver driver = null;
        try
        {
            String browserName = testConfig.getRunTimeProperty("browser").toLowerCase().trim();
            testConfig.logComment("Launching '" + browserName + "' browser for testcase:- " + testConfig.testcaseName);
            BrowserName browser = BrowserName.valueOf(browserName);
            String downloadFilepath = testConfig.getRunTimeProperty("resultsDirectory") + File.separator + "Downloads" + File.separator + testConfig.testcaseName;
            switch (browser)
            {
                case firefox:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (Config.isMobileViewExecution)
                    {
                        String mobileUserAgent = "Mozilla/5.0 (Linux; Android 6.0.1; Nexus 6P Build/MTC19X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.81 Mobile Safari/537.36";
                        firefoxOptions.addPreference("general.useragent.override", mobileUserAgent);
                    }
                    firefoxOptions.addPreference("browser.download.folderList", 2);
                    firefoxOptions.addPreference("browser.download.dir", downloadFilepath);
                    firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream;application/csv;text/csv;application/vnd.ms-excel;");
                    firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
                    firefoxOptions.addPreference("browser.download.manager.showAlertOnComplete", false);
                    firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
                    firefoxOptions.addPreference("pdfjs.disabled", true);
                    if (Config.isRemoteExecution)
                    {
                        driver = new RemoteWebDriver(new URL(seleniumGrid + "/wd/hub"), firefoxOptions);
                    } else
                    {
                        driver = new FirefoxDriver(firefoxOptions);
                    }
                    break;

                case chrome:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (Config.isMobileViewExecution)
                    {
                        Map<String, String> mobileEmulation = new HashMap<>();
                        mobileEmulation.put("deviceName", "Galaxy Note 3");
                        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                        chromeOptions.addArguments("--window-size=360,640");
                    }

                    if (testConfig.getRunTimeProperty("HeadlessExecution").equalsIgnoreCase("true"))
                    {
                        chromeOptions.addArguments("--headless");
                    }

                    Map<String, Object> preferences = new Hashtable<String, Object>();
                    preferences.put("profile.default_content_settings.popups", 0);
                    preferences.put("download.prompt_for_download", "false");
                    preferences.put("download.default_directory", downloadFilepath);
                    // Try this solution if want to use machines outside of AWS : https://stackoverflow.com/a/50275837/6590190
                    chromeOptions.setExperimentalOption("prefs", preferences);
                    if (Config.isRemoteExecution)
                    {
                        driver = new RemoteWebDriver(new URL(seleniumGrid + "/wd/hub"), chromeOptions);
                    } else
                    {
                        driver = new ChromeDriver(chromeOptions);
                    }
                    break;
            }
        } catch (IllegalArgumentException e)
        {
            testConfig.logFailToEndExecution("Invalid Browser name is passed");
        } catch (MalformedURLException e)
        {
            testConfig.logFailToEndExecution("MalformedURLException for Selenium Grid Address");
        } catch (NullPointerException e)
        {
            testConfig.logFailToEndExecution("NullPointerException, seems like BrowerName is not passed from config.properties file.");
        }

        if (Config.isMobileViewExecution)
        {
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(new Dimension(360, 640));
        } else
        {
            testConfig.logComment("Screen size [before] : " + driver.manage().window().getSize());
            driver.manage().window().maximize();
            testConfig.logComment("Screen size [after] : " + driver.manage().window().getSize());
        }

        if (Config.isRemoteExecution)
        {
            SessionId remoteBrowserSession = ((RemoteWebDriver) driver).getSessionId();
            printNodeIpAddress(testConfig, remoteBrowserSession);
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        }
        Long timeInSeconds = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
        driver.manage().timeouts().pageLoadTimeout((timeInSeconds * 3), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout((timeInSeconds * 3), TimeUnit.SECONDS);
        return driver;
    }

    private static void takeDesktopScreenshot(Config testConfig)
    {
        testConfig.logComment("Taking Desktop Screenshot...");
        try
        {
            File screenshotUrl = new File(CommonUtilities.createFileInResultsDirectory(testConfig, "Screenshots") + ".png");
            Robot robot = new Robot();
            Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage Image = robot.createScreenCapture(capture);
            ImageIO.write(Image, "png", screenshotUrl);

            String href = CommonUtilities.convertFilePathToHtmlUrl(screenshotUrl.getPath());
            testConfig.logComment("<B>Screenshot</B>:- <a href=" + href + " target='_blank' >" + screenshotUrl.getName() + "</a>");
        } catch (AWTException | IOException e)
        {
            testConfig.logException("Unable to take desktop screenshot", e);
        }
    }

    public static void takeScreenshot(Config testConfig)
    {
        if (testConfig.enableScreenshot)
        {
            if (testConfig.driver == null)
            {
                testConfig.logCommentForDebugging("Driver is NULL, so can't take screenshot!");
            } else
            {
                try
                {
                    testConfig.logCommentForDebugging("Taking Screenshot...");
                    File screenshotUrl = new File(CommonUtilities.createFileInResultsDirectory(testConfig, "Screenshots") + ".png");
                    byte[] screenshot = ((TakesScreenshot) testConfig.driver).getScreenshotAs(OutputType.BYTES);
                    FileUtils.writeByteArrayToFile(screenshotUrl, screenshot);
                    String href = CommonUtilities.convertFilePathToHtmlUrl(screenshotUrl.getPath());
                    testConfig.logComment("<B>Screenshot</B>:- <a href=" + href + " target='_blank' >" + screenshotUrl.getName() + "</a>");
                    if (testConfig.appiumDriver == null)
                    {
                        String pageUrl = CommonUtilities.convertFilePathToHtmlUrl(testConfig.driver.getCurrentUrl());
                        testConfig.logComment("<B>Page URL</B>:- <a href=" + pageUrl + " target='_blank' >" + pageUrl + "</a>");
                    }
                } catch (Exception e)
                {
                    testConfig.logException("Unable to take screenshot", e);
                }
            }
        }
    }

    /**
     * Close the current window, quitting the browser if it's the last window currently open.
     *
     * @param testConfig - test config instance for the browser to be closed
     */
    public static void closeBrowser(Config testConfig)
    {
        try
        {
            if (testConfig.driver != null && testConfig.appiumDriver == null)
            {
                testConfig.logComment("Close the browser window with URL:- " + testConfig.driver.getCurrentUrl() + " and title as :- " + testConfig.driver.getTitle());
                testConfig.driver.quit();
                testConfig.logCommentForDebugging("Browser closed successfully for testcase : " + testConfig.testcaseName);
            }
        } catch (Exception e)
        {
            testConfig.logException("Unable to close browser for testcase : " + testConfig.testcaseName, e);
        }
    }

    public static void verifyNewBrowserTabIsOpened(Config testConfig, String titleOfTab)
    {
        List<String> browserTabs = new ArrayList<String>(testConfig.driver.getWindowHandles());
        waitAndSwitchToNewTab(testConfig, browserTabs);
        AssertHelper.compareEquals(testConfig, "Title of new tab", titleOfTab, testConfig.driver.getTitle());
        testConfig.driver.close();
        testConfig.driver.switchTo().window(browserTabs.get(0));
    }

    public static void waitAndSwitchToNewTab(Config testConfig, List<String> browserTabs)
    {
        int retryCount = 5;
        boolean isFound = false;
        for (int i = 1; i <= retryCount; i++)
        {
            if (browserTabs.size() > 1)
            {
                testConfig.driver.switchTo().window(browserTabs.get(1));
                isFound = true;
                break;
            }
            WaitHelper.waitForSeconds(testConfig, 3);
        }
        if (isFound)
        {
            testConfig.logComment("Successfully switched to new tab");
        } else
        {
            testConfig.logFail("Unable to switch to new tab");
        }
    }

    public static void stopExecutionOnBrowser(Config testConfig)
    {
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        js.executeScript("return window.stop");
    }

    public static void refreshBrowser(Config testConfig)
    {
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        js.executeScript("location.reload();");
        testConfig.logComment("Refreshing the browser...");
    }

    /**
     * Warning : Don't use this function for regular cases its just for special case when we need to refresh page and get re-initialized instance of page.
     *
     * @param testConfig  - Pass object of config
     * @param description - comment
     * @param page        - page which is reinitialized
     * @return - Page
     */
    public static Object refreshAndIntializePage(Config testConfig, Object page, String description)
    {
        testConfig.logComment("Executing refreshAndInitializePage for - '" + description + "'");
        Browser.refreshBrowser(testConfig);
        PageFactory.initElements(testConfig.driver, page);
        return page;
    }

    /**
     * This function set the cookies for the browser launch, if already available then set the updated value
     *
     * @param testConfig - Pass object of config
     * @param cookies    - List of cookies to set
     */
    public static void addCookies(Config testConfig, List<Cookie> cookies)
    {
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                testConfig.driver.manage().addCookie(cookie);
            }
        }
    }

    public static String getCurrentUrl(Config testConfig)
    {
        return testConfig.driver == null ? null : testConfig.driver.getCurrentUrl();
    }

    /**
     * This function will hit the hub and call the api of hub to get the ip address of machine where our test case is executing
     *
     * @param testConfig - Pass object of config
     * @param session    - session id
     */
    public static void printNodeIpAddress(Config testConfig, SessionId session)
    {
        try
        {
            ApiHelper apiHelper = new ApiHelper(testConfig);
            RequestSpecification reqspec = apiHelper.initialiseContentType(ContentType.JSON, null);
            reqspec.body("{\"query\":\"{ session (id: \\\"" + session.toString() + "\\\") { nodeUri } } \"}");
            Response response = apiHelper.executeRequestAndGetResponse(seleniumGrid + "/graphql", ApiRequestType.POST, reqspec);
            JsonPath jsonPath = new JsonPath(response.asString());
            String machineIp = new URL(jsonPath.getString("data.session.nodeUri")).getHost();
            //machineIp = CommonUtilities.getMapKeyByValue(GenerateTestngXmlAndRun.machines, machineIp);
            testConfig.logComment("<font color='Blue'><B>Testcase executing on machine : </B>" + machineIp + "</font>");
        } catch (Exception e)
        {
            testConfig.logWarning("Unable to fetch remote machine IP for session ID : " + session);
        }
    }

    public static void bringToFocus(Config testConfig)
    {
        String currentWindowHandle = testConfig.driver.getWindowHandle();
        ((JavascriptExecutor) testConfig.driver).executeScript("alert('Test')");
        testConfig.driver.switchTo().alert().accept();
        testConfig.driver.switchTo().window(currentWindowHandle);
        testConfig.logComment("Brought current window to focus");
    }

    /**
     * Used for opening test-case in new tab
     *
     * @param testConfig test config instance for the browser to be closed
     */
    public static void openNewTab(Config testConfig)
    {
        testConfig.logComment("Opening in New Tab");
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        js.executeScript("window.open('" + testConfig.getRunTimeProperty("ZeusReactURL") + "','_blank')");
    }

    /**
     * This function for switching browser to new tab
     *
     * @param testConfig - Pass object of config
     */
    public static void switchToNewTab(Config testConfig)
    {
        testConfig.logComment("Switch to New Tab");
        List<String> browserTabs = new ArrayList<String>(testConfig.driver.getWindowHandles());
        testConfig.driver.switchTo().window(browserTabs.get(0));
    }

    /**
     * This function for navigating back as per browser history
     *
     * @param testConfig - Pass object of config
     */
    public static void navigateBack(Config testConfig)
    {
        testConfig.logComment("Navigating to last visited URL");
        testConfig.driver.navigate().back();
    }
}
