package Automation.Utils;

import Automation.Utils.Element.How;
import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WaitHelper
{

    private static String getCallerClassName()
    {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++)
        {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(WaitHelper.class.getName()) && !ste.getClassName().contains("Helper") && ste.getClassName().indexOf("java.lang.Thread") != 0)
            {
                return ste.getClassName();
            }
        }
        return null;
    }

    private static WebDriverWait getWebDriverWait(Config testConfig, int secondsToWait)
    {
        Long timeInSeconds = null;
        if (secondsToWait == -1)
        {
            timeInSeconds = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
            // timeInSeconds = Duration.ofSeconds(Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime")));
        } else
        {
            timeInSeconds = (long) secondsToWait;
            // timeInSeconds = Duration.ofSeconds(secondsToWait);
        }
        return new WebDriverWait(testConfig.driver, Duration.ofSeconds(timeInSeconds));
        // return new WebDriverWait(testConfig.driver, timeInSeconds);
    }

    public static void waitForAlert(Config testConfig)
    {
        int max = 50, count = 0;
        while (!Popup.isAlertPresent(testConfig) && count <= max)
        {
            waitForSeconds(testConfig, 2);
            count += 2;
        }
    }

    public static void waitForElementAttributeToBe(Config testConfig, WebElement element, String attributeName, String attributeValue, String description)
    {
        testConfig.logComment("Started waiting for '" + description + "' to have '" + attributeName + "'=" + attributeValue);
        WebDriverWait wait = getWebDriverWait(testConfig, -1);
        try
        {
            wait.until(ExpectedConditions.attributeToBe(element, attributeName, attributeValue));
        } catch (TimeoutException exc)
        {
            testConfig.logFailToEndExecution("Element '" + description + "' is having '" + attributeName + "'=" + attributeValue + ", after waiting for " + testConfig.getRunTimeProperty("ObjectWaitTime") + " seconds");
        }
    }

    public static void waitForElementToBeClickable(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Started waiting for '" + description + "' to be clickable");
        WebDriverWait wait = getWebDriverWait(testConfig, -1);
        try
        {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException exc)
        {
            testConfig.logFailToEndExecution("Element '" + description + "' is not clickable");
        }
    }

    public static void waitForElementToBeDisplayed(Config testConfig, WebElement element, String description)
    {
        int timeInSeconds = Integer.parseInt(testConfig.getRunTimeProperty("ObjectWaitTime"));
        waitForElementToBeDisplayed(testConfig, element, description, timeInSeconds);
    }

    public static void waitForElementToBeDisplayed(Config testConfig, By locator, String description)
    {
        int timeInSeconds = Integer.parseInt(testConfig.getRunTimeProperty("ObjectWaitTime"));
        testConfig.logComment("Started waiting for '" + description + "' to be displayed");
        WebDriverWait wait = getWebDriverWait(testConfig, timeInSeconds);
        WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        if (ele == null)
        {
            testConfig.logFailToEndExecution("Element '" + description + "' is NOT visible even after waiting for " + (Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime")) + " seconds"));
        } else
        {
            testConfig.logComment(description + " is visible now");
        }
    }

    public static void waitForElementToBeDisplayed(Config testConfig, WebElement element, String description, int timeInSeconds)
    {
        testConfig.logComment("Started waiting for '" + description + "' to be displayed");
        if (element == null)
        {
            testConfig.logFailToEndExecution("Element '" + description + "' is NULL, so can't waitForElementToBeDisplayed !!");
        } else
        {
            WebDriverWait wait = getWebDriverWait(testConfig, timeInSeconds);
            try
            {
                wait.until(ExpectedConditions.visibilityOf(element));
            } catch (TimeoutException exc)
            {
                testConfig.logFailToEndExecution("Element '" + description + "' is NOT visible even after waiting for " + timeInSeconds + " seconds");
            }
        }
    }

    public static void waitForElementToBeHidden(Config testConfig, By locator, String description)
    {
        testConfig.logComment("Started waiting for '" + description + "' to be hidden");
        WebDriverWait wait = getWebDriverWait(testConfig, -1);
        Boolean isHidden = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));

        if (isHidden)
        {
            testConfig.logComment(description + " is hidden now");
        } else
        {
            testConfig.logFailToEndExecution("Element '" + description + "' is NOT hidden even after waiting for " + (Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime")) + " seconds"));
        }
    }

    public static void waitForElementToBeHidden(Config testConfig, WebElement element, String description)
    {
        int timeInSeconds = Integer.parseInt(testConfig.getRunTimeProperty("ObjectWaitTime"));
        waitForElementToBeHidden(testConfig, element, description, timeInSeconds);
    }

    public static void waitForElementToBeHidden(Config testConfig, WebElement element, String description, int timeInSeconds)
    {
        testConfig.logComment("Started waiting for '" + description + "' to be hidden");
        WebDriverWait wait = getWebDriverWait(testConfig, timeInSeconds);
        List<WebElement> elements = Collections.singletonList(element);
        if (element != null)
        {
            try
            {
                wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
            } catch (TimeoutException exc)
            {
                testConfig.logFailToEndExecution("Element '" + description + "' is NOT hidden even after waiting for " + timeInSeconds + " seconds");
            }
        } else
        {
            testConfig.logWarning("Element in NULL, so cant wait for hidden");
        }
    }

    /**
     * Wait until element do not have given property value
     *
     * @param testConfig    - Pass object of config
     * @param how           - How to locate the element
     * @param what          - value of xpath/css
     * @param propertyName  - attribute name
     * @param propertyValue - attribute value
     * @return true or false
     */
    public static boolean waitForElementToLoadUntilNotHaveGivenPropertyValue(Config testConfig, How how, String what, String propertyName, String propertyValue)
    {
        Boolean isAttributeRemoved = false;
        Long timeInSeconds = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
        testConfig.logComment("Started waiting for Element to Not have '" + propertyValue + "' in '" + propertyName + "' at :- " + new Date() + ". Wait upto " + timeInSeconds + " seconds.");
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(testConfig.driver);

        // wait.withTimeout(ObjectWaitTime, TimeUnit.SECONDS);
        // wait.pollingEvery(1, TimeUnit.SECONDS);
        wait.withTimeout(Duration.ofSeconds(timeInSeconds));
        wait.pollingEvery(Duration.ofSeconds(1));

        isAttributeRemoved = wait.until(new Function<WebDriver, Boolean>()
        {
            public Boolean apply(WebDriver webDriver)
            {
                String value = null;
                try
                {
                    value = Element.getPageElement(testConfig, how, what).getAttribute(propertyName);
                } catch (StaleElementReferenceException se)
                {
                    value = Element.getPageElement(testConfig, how, what).getAttribute(propertyName);
                }

                if (propertyValue.isEmpty())
                {
                    return !value.isEmpty();
                } else return value != null && !value.contains(propertyValue);
            }
        });

        if (isAttributeRemoved)
        {
            testConfig.logComment("Element with '" + propertyValue + "' in '" + propertyName + "' is removed now");
        } else
        {
            testConfig.logWarning("Element with '" + propertyValue + "' in '" + propertyName + "' is still NOT removed");
        }

        return isAttributeRemoved;
    }

    /**
     * Wait until element have given property value
     *
     * @param testConfig     - Pass object of config
     * @param how            - how to locate the element
     * @param valueOfLocator - valueOfLocator
     * @param propertyName   - name
     * @param propertyValue  - value
     */
    public static void waitForElementToLoadWithGivenPropertyValue(Config testConfig, How how, String valueOfLocator, String propertyName, String propertyValue)
    {
        Date startDate = new Date();
        double timeTaken = 0;
        Long timeInSeconds = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
        testConfig.logComment("Started waiting for Element to have '" + propertyValue + "' in '" + propertyName + "' at :- " + startDate + ". Wait upto " + timeInSeconds + " seconds.");

        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(testConfig.driver);
        // wait.withTimeout(ObjectWaitTime, TimeUnit.SECONDS);
        // wait.pollingEvery(1, TimeUnit.SECONDS);
        wait.withTimeout(Duration.ofSeconds(timeInSeconds));
        wait.pollingEvery(Duration.ofSeconds(1));
        try
        {
            wait.until(new Function<WebDriver, Boolean>()
            {
                public Boolean apply(WebDriver webDriver)
                {
                    String className = null;
                    WebElement element = null;
                    try
                    {
                        element = Element.getPageElement(testConfig, how, valueOfLocator);
                        if (element != null)
                        {
                            className = element.getAttribute(propertyName);
                        }
                    } catch (StaleElementReferenceException se)
                    {
                        element = Element.getPageElement(testConfig, how, valueOfLocator);
                        if (element != null)
                        {
                            className = element.getAttribute(propertyName);
                        }
                    }
                    return className != null && className.equals(propertyValue);
                }
            });
        } catch (TimeoutException e)
        {
            Date endDate = new Date();
            timeTaken = (endDate.getTime() - startDate.getTime()) / 1000.00;
            testConfig.logWarning("'" + propertyValue + "' from '" + propertyName + "' still NOT removed in :- " + timeTaken + " seconds");
        }
        Date endDate = new Date();
        timeTaken = (endDate.getTime() - startDate.getTime()) / 1000.00;
        testConfig.logComment("'" + propertyValue + "' from '" + propertyName + "' removed in :- " + timeTaken + " seconds.");
    }

    public static boolean waitForIframeAndSwitch(Config testConfig, String iframeId)
    {
        testConfig.logComment("Wait for iframe '" + iframeId + "' and switch");
        testConfig.driver.switchTo().defaultContent();
        WebDriverWait wait = getWebDriverWait(testConfig, -1);
        try
        {
            int counter = Integer.parseInt(iframeId);
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(counter));
        } catch (NumberFormatException e)
        {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeId));
        } catch (TimeoutException exc)
        {
            testConfig.logFailToEndExecution("Iframe is not loaded even after waiting for " + testConfig.getRunTimeProperty("ObjectWaitTime") + " seconds");
            return false;
        }
        return true;
    }

    /**
     * This method is created to wait for any element, but if element will not appear then also testcase will NOT fail.
     *
     * @param testConfig  - Pass object of config
     * @param element     - Weblement
     * @param description - comment to show in logs
     */
    public static void waitForOptionalElement(Config testConfig, WebElement element, String description)
    {
        if (element == null)
        {
            testConfig.logWarning("Element in NULL, so cant wait for hidden");
            return;
        }
        testConfig.logComment("Started waiting for '" + description + "'");
        WebDriverWait wait = getWebDriverWait(testConfig, -1);
        try
        {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException exc)
        {
            testConfig.logWarning("Optional element '" + description + "' is not visible!");
        } catch (StaleElementReferenceException exc)
        {
            testConfig.logWarning("Optional element '" + description + "' is not visible!");
        }
    }

    /**
     * This method is created to wait for any element in a timeInSeconds period, but if element will not appear then also testcase will NOT fail.
     *
     * @param testConfig    - Pass object of config
     * @param element       - Weblement
     * @param description   - comment to show in logs
     * @param timeInSeconds - time in seconds to wait for element
     */
    public static void waitForOptionalElement(Config testConfig, WebElement element, String description, int timeInSeconds)
    {
        if (element == null)
        {
            testConfig.logWarning("Element in NULL, so cant wait for hidden");
            return;
        }
        testConfig.logComment("Started waiting for '" + description + "'");
        WebDriverWait wait = getWebDriverWait(testConfig, timeInSeconds);
        try
        {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException exc)
        {
            testConfig.logWarning("Optional element '" + description + "' is not visible!");
        }
    }

    /**
     * This method is created to wait for loading of new page, if passed element will not appear then testcase will fail.
     *
     * @param testConfig - Pass object of config
     * @param element    - weblement
     */
    public static void waitForPageLoad(Config testConfig, WebElement element)
    {
        int timeInSeconds = Integer.parseInt(testConfig.getRunTimeProperty("ObjectWaitTime"));
        waitForPageLoad(testConfig, element, timeInSeconds);
    }

    /**
     * This method is created to wait for loading of new page, if passed element will not appear then testcase will fail.
     *
     * @param testConfig    - Pass object of config
     * @param element       - weblement
     * @param timeInSeconds - time as integer
     */
    public static void waitForPageLoad(Config testConfig, WebElement element, int timeInSeconds)
    {
        double timeTaken = 0;
        String callingClassName = getCallerClassName();
        String currentPageName = callingClassName.substring(callingClassName.lastIndexOf('.') + 1);
        Date startDate = new Date();
        testConfig.logComment("Started waiting for '" + currentPageName + "' to load at:- " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(startDate) + ". Wait upto " + timeInSeconds + " seconds.");
        WebDriverWait wait = getWebDriverWait(testConfig, timeInSeconds);
        try
        {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e)
        {
            Date endDate = new Date();
            timeTaken = (endDate.getTime() - startDate.getTime()) / 1000.00;
            testConfig.logFailToEndExecution("'" + currentPageName + "' NOT loaded even after :- " + timeTaken + " seconds.");
        }
        Date endDate = new Date();
        timeTaken = (endDate.getTime() - startDate.getTime()) / 1000.00;
        testConfig.logComment(currentPageName + " loaded in :- " + timeTaken + " seconds.");
        if (timeTaken > 120)
        {
            testConfig.logComment("<B><font color='Red'>" + currentPageName + " is loaded after " + timeTaken / 60 + " minutes.</font></B>");
        }
    }

    /**
     * This method is created to add static wait for given seconds of time
     *
     * @param testConfig - Pass object of config
     * @param seconds    - time in integer
     */
    public static void waitForSeconds(Config testConfig, int seconds)
    {
        int milliseconds = seconds * 1000;
        try
        {
            Log.logCommentWithOptionalConfig(testConfig, "Waiting for '" + seconds + "' seconds");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e)
        {
            testConfig.logExceptionAndFail(e);
        }
    }

    /**
     * This method is created to add static wait for given milliseconds of time
     *
     * @param testConfig   - Pass object of config
     * @param milliseconds - time in integer
     */
    public static void waitForMilliSeconds(Config testConfig, int milliseconds)
    {
        try
        {
            Log.logCommentWithOptionalConfig(testConfig, "Waiting for '" + milliseconds + "' milliseconds");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e)
        {
            testConfig.logExceptionAndFail(e);
        }
    }

    /**
     * This method verifies the presence of file in a folder
     *
     * @param testConfig       - object of config
     * @param commonFileFolder - path of directory where file is being download
     * @param fileName         - name of file which is being downloaded
     * @return - returns exact file path of downloaded file
     */
    public static String waitForFileDownload(Config testConfig, String commonFileFolder, String fileName)
    {
        File downloadedFolder = null;
        String exactFilePath = null;
        int retryCount = 1;
        while (retryCount <= 30)
        {
            boolean isFound = false;
            downloadedFolder = new File(commonFileFolder);
            File[] files = downloadedFolder.listFiles();
            for (File f : files)
            {
                if (f.isDirectory())
                {
                    for (File file : f.listFiles())
                    {
                        if (file.getName().contains(fileName) && !file.getName().endsWith(".crdownload"))
                        {
                            testConfig.logComment("Report has been downloaded  : " + file.getName());
                            isFound = true;
                            exactFilePath = downloadedFolder + File.separator + f.getName() + File.separator + file.getName();
                            break;
                        }
                    }
                    if (isFound)
                    {
                        break;
                    }
                } else
                {
                    if (f.getName().contains(fileName) && !f.getName().endsWith(".crdownload"))
                    {
                        testConfig.logComment("Report has been downloaded  : " + f.getName());
                        isFound = true;
                        exactFilePath = downloadedFolder + File.separator + f.getName();
                        break;
                    }
                }
            }
            if (isFound)
            {
                break;
            } else
            {
                WaitHelper.waitForSeconds(testConfig, 10);
            }
            retryCount++;
        }
        return exactFilePath;
    }
}