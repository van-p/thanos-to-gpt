package Automation.Utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class Element
{

    /**
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement to be checked
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void check(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Check '" + description + "'");
        if (element == null)
        {
            testConfig.logFailToEndExecution("Element '" + description + "' is NULL, so can't check the checkbox");
        } else
        {
            try
            {
                if (!element.isSelected())
                {
                    try
                    {
                        clickWithoutLog(testConfig, element);
                        WaitHelper.waitForSeconds(testConfig, 1);
                    } catch (StaleElementReferenceException e)
                    {
                        testConfig.logWarning("Stale element reference exception. Trying again...");
                        clickWithoutLog(testConfig, element);
                    }
                } else
                {
                    testConfig.logFail("Checkbox is already checked, so can't re-check it !!");
                }
            } catch (NoSuchElementException e)
            {
                testConfig.logFailToEndExecution("Element '" + description + "' not found on page, so can't check the checkbox");
            }
        }
    }

    /*
     * Warning : Don't use this function for regular cases its just for special case when clear() method is not working
     * @param testConfig - Pass object of config Config instance used for logging
     * @param element WebElement to be cleared
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void clearDataWithBackSpace(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Clear the text from " + description + "'");
        int length = element.getAttribute("value").length();
        for (int i = 0; i < length; i++)
        {
            element.sendKeys(Keys.BACK_SPACE);
        }
    }

    /**
     * Use this function to click on an element, incase element is not visible then it will scroll first and then click
     *
     * @param testConfig         - Pass object of Config instance used for logging
     * @param elementToBeClicked WebElement to be clicked
     * @param description        logical name of specified WebElement, used for Logging purposes in report
     * @param scroll             used true whenever user want to scroll the element on the top of the page.
     */
    public static void click(Config testConfig, WebElement elementToBeClicked, String description, boolean... scroll)
    {
        if (elementToBeClicked != null)
        {
            testConfig.logComment("Click on '" + description + "'");
            try
            {
                // Scroll Up or Down if element is not visible
                JavascriptExecutor jse = (JavascriptExecutor) testConfig.driver;
                if (scroll.length > 0 && scroll[0])
                {
                    jse.executeScript("arguments[0].scrollIntoView(true)", elementToBeClicked);
                } else
                {
                    jse.executeScript("arguments[0].scrollIntoView(false)", elementToBeClicked);
                }
            } catch (WebDriverException wde)
            {
                testConfig.logException("Unable to scroll", wde);
            }
            // Then click element
            elementToBeClicked.click();
        } else
        {
            if (testConfig.endExecutionOnfailure)
            {
                testConfig.logFailToEndExecution("Element '" + description + "' not found on the page");
            } else
            {
                testConfig.logFail("Element '" + description + "' not found on the page");
            }
        }
    }

    /**
     * Use this function if you want to click on any element, without using JS scrolling
     *
     * @param testConfig         - Pass object of Config instance used for logging
     * @param elementToBeClicked WebElement to be clicked
     * @param description        logical name of specified WebElement, used for Logging purposes in report
     * @param scroll             used true whenever user want to scroll the element on the top of the page.
     */
    public static void clickWithoutScroll(Config testConfig, WebElement elementToBeClicked, String description, boolean... scroll)
    {
        try
        {
            if (elementToBeClicked != null)
            {
                testConfig.logComment("Click on '" + description + "'");
                elementToBeClicked.click();
            } else
            {
                if (testConfig.endExecutionOnfailure)
                {
                    testConfig.logFailToEndExecution("Element '" + description + "' not found on the page");
                } else
                {
                    testConfig.logFail("Element '" + description + "' not found on the page");
                }
            }
        } catch (Exception e)
        {
            testConfig.logExceptionToEndExecution("Element '" + description + "' not found on the page", e);
        }
    }

    public static void clickAndWaitForElementToHide(Config testConfig, WebElement elementToBeClicked, String description)
    {
        click(testConfig, elementToBeClicked, description);
        WaitHelper.waitForElementToBeHidden(testConfig, elementToBeClicked, description);
    }

    /**
     * Clicks on element using JavaScript
     *
     * @param testConfig         - Pass object of config For logging
     * @param elementToBeClicked - Element to be clicked
     * @param description        For logging
     */
    public static void clickThroughJS(Config testConfig, WebElement elementToBeClicked, String description)
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
            js.executeScript("arguments[0].click();", elementToBeClicked);
            testConfig.logComment("Clicked on " + description);
        } catch (Exception e)
        {
            testConfig.logFail("Unable to clickThroughJS on element '" + description + "'");
        }
    }

    /**
     * Warning : Don't use this function for regular cases its just for special case when click is not working in 1 time This function will keep clicking on the 'elementToBeClicked' till the time 'elementToHide' is becomes invisible from the page.
     *
     * @param testConfig         - Pass object of config
     * @param elementToBeClicked - weblement
     * @param description        - comment to show in logs
     * @param elementToHide      - web lement
     */
    public static void clickUntilNextElementIsHidden(Config testConfig, WebElement elementToBeClicked, String description, WebElement elementToHide)
    {
        testConfig.logComment("Executing clickUntilNextElementIsHidden for : " + description);
        click(testConfig, elementToBeClicked, description);
        WaitHelper.waitForSeconds(testConfig, 3);
        int counter = 5;
        while (counter > 0 && Element.isElementDisplayed(testConfig, elementToHide))
        {
            JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
            js.executeScript("arguments[0].click();", elementToBeClicked);
            WaitHelper.waitForSeconds(testConfig, 3);

            if (!Element.isElementDisplayed(testConfig, elementToHide))
            {
                break;
            }

            elementToBeClicked.click();
            WaitHelper.waitForSeconds(testConfig, 2);
            counter--;
        }
    }

    /**
     * Warning : Don't use this function for regular cases its just for special case when click is not working in 1 time This function will keep clicking on the 'elementToBeClicked' till the time 'nextElement' is load displayed.
     *
     * @param testConfig         - Pass object of config
     * @param elementToBeClicked - weblement
     * @param description        - comment to show in logs
     * @param nextElement        - weblement
     */
    public static void clickUntilNextElementIsLoaded(Config testConfig, WebElement elementToBeClicked, String description, WebElement nextElement)
    {
        testConfig.logComment("Executing clickUntilNextElementIsLoaded for : " + description);
        WaitHelper.waitForElementToBeClickable(testConfig, elementToBeClicked, description);
        click(testConfig, elementToBeClicked, "elementToBeClicked and wait for nextElement");
        WaitHelper.waitForSeconds(testConfig, 3);
        int counter = 5;
        while (counter > 0 && !Element.isElementDisplayed(testConfig, nextElement))
        {
            JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
            js.executeScript("arguments[0].click();", elementToBeClicked);
            WaitHelper.waitForSeconds(testConfig, 3);

            if (Element.isElementDisplayed(testConfig, nextElement))
            {
                break;
            }

            elementToBeClicked.click();
            WaitHelper.waitForSeconds(testConfig, 2);
            counter--;
        }
    }

    /**
     * Click without logging
     *
     * @param testConfig - Pass object of config
     * @param element
     */
    private static void clickWithoutLog(Config testConfig, WebElement element)
    {
        try
        {
            JavascriptExecutor jse = (JavascriptExecutor) testConfig.driver;
            jse.executeScript("arguments[0].scrollIntoView(false)", element);
            element.click();
        } catch (WebDriverException wde)
        {
            element.click();
        }
    }

    public static void dragAndDropElement(Config testConfig, WebElement sourceElement, WebElement targetElement, String sourceDescription, String targetDescription)
    {
        testConfig.logComment("Dragging element " + sourceDescription + " to " + targetDescription);
        Actions actions = new Actions(testConfig.driver);
        actions.dragAndDrop(sourceElement, targetElement).perform();
    }

    /**
     * This method is used to enter string in edit text box.
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement to be clicked
     * @param value       String value to be entered
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void enterData(Config testConfig, WebElement element, String value, String description)
    {
        testConfig.logComment("Enter the " + description + " as '" + value + "'");
        element.clear();
        if (!Config.isMobileAppExecution && !StringUtils.isEmpty(element.getAttribute("value")))
        {
            new Actions(testConfig.driver).click(element).sendKeys(Keys.END).keyDown(Keys.SHIFT).sendKeys(Keys.HOME).keyUp(Keys.SHIFT).sendKeys(Keys.BACK_SPACE).sendKeys("").perform();
        }
        element.sendKeys(value);
    }

    /**
     * This method is used to enter string using Action class without pointing to an element.
     *
     * @param testConfig  - Pass object of Config instance used for logging
     * @param value       String value to be entered
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void enterDataWithoutSelectedElement(Config testConfig, String value, String description)
    {
        testConfig.logComment("Enter the " + description + " as '" + value + "'");
        Actions actions = new Actions(testConfig.driver);
        actions.sendKeys(value).perform();
    }

    /**
     * This method is used to enter string in edit text box. It also mask the value that needs to be entered
     *
     * @param testConfig         - Pass object of config Config instance used for logging
     * @param element            WebElement to be clicked
     * @param value              String value to be entered
     * @param description        logical name of specified WebElement, used for Logging purposes in report
     * @param isMaskingMandatory - takes true or false
     */
    public static void enterDataAndMaskLogs(Config testConfig, WebElement element, String value, String description, boolean... isMaskingMandatory)
    {
        boolean maskData = StringUtils.equalsIgnoreCase(testConfig.getRunTimeProperty("environment"), "production") | StringUtils.equalsIgnoreCase(testConfig.getRunTimeProperty("environment"), "sandbox");
        if (isMaskingMandatory.length > 0)
        {
            maskData = isMaskingMandatory[0];
        }

        if (maskData)
        {
            testConfig.logComment("Enter the " + description + " as '" + CommonUtilities.maskString(value) + "'");
        } else
        {
            testConfig.logComment("Enter the " + description + " as '" + value + "'");
        }

        element.clear();
        if (!StringUtils.isEmpty(element.getAttribute("value")))
        {
            new Actions(testConfig.driver).click(element).sendKeys(Keys.END).keyDown(Keys.SHIFT).sendKeys(Keys.HOME).keyUp(Keys.SHIFT).sendKeys(Keys.BACK_SPACE).sendKeys("").perform();
        }
        element.sendKeys(value);
    }

    /**
     * Enters the given 'value'in the specified File name WebElement
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     Filename WebElement where data needs to be entered
     * @param value       value to the entered
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void enterFileName(Config testConfig, WebElement element, String value, String description)
    {
        if (!StringUtils.isEmpty(value))
        {
            testConfig.logComment("Enter the " + description + " as '" + value + "'");
            element.sendKeys(value);
        } else
        {
            testConfig.logComment("Skipped file entry for " + description);
        }
    }

    public static List<WebElement> getAllDropdownOptions(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Get all options of " + description + ".");
        Select sel = new Select(element);
        return sel.getOptions();
    }

    /**
     * Gets the WebElement using the specified locator technique on the passed driver page
     *
     * @param testConfig test config instance for the driver
     * @param how        Locator technique to use
     * @param what       element to be found with given technique (any arguments in this string will be replaced with run time properties)
     * @return found WebElement
     */
    public static WebElement getPageElement(Config testConfig, How how, String what)
    {
        testConfig.logComment("Get the WebElement with " + how + ":" + what);
        what = testConfig.replaceArgumentsWithRunTimeProperties(what);
        try
        {
            switch (how)
            {
                case className:
                    return testConfig.driver.findElement(By.className(what));
                case css:
                    return testConfig.driver.findElement(By.cssSelector(what));
                case id:
                    return testConfig.driver.findElement(By.id(what));
                case linkText:
                    return testConfig.driver.findElement(By.linkText(what));
                case name:
                    return testConfig.driver.findElement(By.name(what));
                case partialLinkText:
                    return testConfig.driver.findElement(By.partialLinkText(what));
                case tagName:
                    return testConfig.driver.findElement(By.tagName(what));
                case xPath:
                    return testConfig.driver.findElement(By.xpath(what));
                default:
                    return null;
            }
        } catch (StaleElementReferenceException e1)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            // retry
            WaitHelper.waitForSeconds(testConfig, 2);
            testConfig.logComment("Retrying getting element" + how + ":" + what);
            return getPageElement(testConfig, how, what);
        } catch (NoSuchElementException e)
        {
            testConfig.logWarning("Could not find the element on page");
            return null;
        }
    }

    /**
     * Gets the WebElement using the specified locator technique on the passed driver page
     *
     * @param testConfig test config instance for the driver
     * @param how        Locator technique to use
     * @param what       element to be found with given technique (any arguments in this string will be replaced with run time properties)
     * @return found WebElement
     */
    public static WebElement getPageElementWithRetry(Config testConfig, How how, String what)
    {
        WebElement element = null;
        for (int i = 0; i < 5; i++)
        {
            element = getPageElement(testConfig, how, what);
            if (element != null)
            {
                return element;
            }
            WaitHelper.waitForSeconds(testConfig, 2);
        }
        return element;
    }

    /**
     * Gets the WebElement using the specified locator technique on the passed driver page
     *
     * @param testConfig test config instance for the driver
     * @param how        Locator technique to use
     * @param what       element to be found with given technique (any arguments in this string will be replaced with run time properties)
     * @return found WebElement
     */
    public static List<WebElement> getPageElementsWithRetry(Config testConfig, How how, String what)
    {
        List<WebElement> elements = null;
        for (int i = 0; i < 5; i++)
        {
            elements = getPageElements(testConfig, how, what);
            if (elements.size() != 0)
            {
                return elements;
            }
            WaitHelper.waitForSeconds(testConfig, 2);
        }
        return elements;
    }

    /**
     * Gets the list of WebElements using the specified locator technique on the passed driver page
     *
     * @param testConfig test config instance for the driver
     * @param how        Locator technique to use
     * @param what       element to be found with given technique (any arguments in this string will be replaced with run time properties)
     * @return List of WebElements Found
     */
    public static List<WebElement> getPageElements(Config testConfig, How how, String what)
    {
        testConfig.logComment("Get the List of WebElements with " + how + ":" + what);
        try
        {
            switch (how)
            {
                case className:
                    return testConfig.driver.findElements(By.className(what));
                case css:
                    return testConfig.driver.findElements(By.cssSelector(what));
                case id:
                    return testConfig.driver.findElements(By.id(what));
                case linkText:
                    return testConfig.driver.findElements(By.linkText(what));
                case name:
                    return testConfig.driver.findElements(By.name(what));
                case partialLinkText:
                    return testConfig.driver.findElements(By.partialLinkText(what));
                case tagName:
                    return testConfig.driver.findElements(By.tagName(what));
                case xPath:
                    return testConfig.driver.findElements(By.xpath(what));
                default:
                    return null;
            }
        } catch (StaleElementReferenceException e1)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            // retry
            return getPageElements(testConfig, how, what);
        } catch (Exception e)
        {
            testConfig.logWarning("Could not find the list of the elements on page");
            return null;
        }
    }

    public static WebElement getSelectedDropdownOption(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Get first selected option of " + description + ".");
        Select sel = new Select(element);
        return sel.getFirstSelectedOption();
    }

    public static String getSlectedValue(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Get text of '" + description + "'");
        String text = null;
        try
        {
            Select select = new Select(element);
            WebElement option = select.getFirstSelectedOption();
            text = option.getText();
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            Select select = new Select(element);
            WebElement option = select.getFirstSelectedOption();
            text = option.getText();
        }

        return text;
    }

    /**
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param how         locator strategy to find element
     * @param what        element locator
     * @param description logical name of specified WebElement, used for Logging purposes in report
     * @return - returns text
     */
    public static String getText(Config testConfig, How how, String what, String description)
    {
        testConfig.logComment("Get text of '" + description + "'");
        String text = null;
        try
        {
            WebElement elm = Element.getPageElement(testConfig, how, what);
            text = Element.getText(testConfig, elm, description);
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            WebElement elm = Element.getPageElement(testConfig, how, what);
            text = Element.getText(testConfig, elm, description);
        }
        return text;
    }

    /**
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement whose text is needed
     * @param description logical name of specified WebElement, used for Logging purposes in report
     * @return - returns text
     */
    public static String getText(Config testConfig, WebElement element, String description)
    {
        testConfig.logCommentForDebugging("Get text of '" + description + "'");
        String text = "";

        try
        {
            try
            {
                // Scroll Up or Down if element is not visible
                JavascriptExecutor jse = (JavascriptExecutor) testConfig.driver;
                jse.executeScript("arguments[0].scrollIntoView(false)", element);
            } catch (WebDriverException wde)
            {
            }

            text = element.getText();
            if (text.equals(""))
            {
                text = element.getAttribute("value");
                if (text == null)
                {
                    text = "";
                }
            }
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");

            text = element.getText();
            if (text.equals(""))
            {
                text = element.getAttribute("value");
            }
        } catch (NoSuchElementException e)
        {
            testConfig.logWarning("Element '" + description + "' is not found on the page, so can't getText");
            text = "";
        }
        return text;
    }

    /**
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement whose attribute text is needed
     * @param attribute   attribute name whose text is needed
     * @param description logical name of specified attribute of WebElement, used for Logging purposes in report
     * @return - returns text
     */
    public static String getAttributeText(Config testConfig, WebElement element, String attribute, String description)
    {
        testConfig.logCommentForDebugging("Get text of attribute '" + description + "'");
        String text = "";

        try
        {
            try
            {
                // Scroll Up or Down if element is not visible
                JavascriptExecutor jse = (JavascriptExecutor) testConfig.driver;
                jse.executeScript("arguments[0].scrollIntoView(false)", element);
            } catch (WebDriverException wde)
            {
            }

            text = element.getAttribute(attribute);
            if (text == null)
            {
                text = "";
            }
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");

            text = element.getAttribute(attribute);
            if (text == null)
            {
                text = "";
            }
        } catch (NoSuchElementException e)
        {
            testConfig.logWarning("Attribute or Element '" + description + "' is not found on the page, so can't getText");
            text = "";
        }
        return text;
    }

    /**
     * Hide Revealed Element
     *
     * @param testConfig - Pass object of config
     * @param csspath    - locator path
     */
    public static void hideRevealedElement(Config testConfig, String csspath)
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
            String strJS = "document.querySelectorAll(\"" + csspath + "\")[0]";
            testConfig.logComment(strJS);
            js.executeScript(strJS + ".style.display = \"none\";");
            js.executeScript(strJS + ".style.visibility = 'hidden';");
            js.executeScript(strJS + ".style.opacity = 1;");
            js.executeScript(strJS + ".style.width = '1px';");
            js.executeScript(strJS + ".style.height = '1px';");
            testConfig.logComment("Revealed element with css path " + csspath + " is hidden now");
        } catch (Exception e)
        {
            testConfig.logWarning("Exception occured in hiding element.");
        }
    }

    public static Boolean isElementDisplayed(Config testConfig, WebElement element)
    {
        Boolean visible = true;
        if (element == null)
        {
            return false;
        }
        try
        {
            visible = element.isDisplayed();
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            try
            {
                visible = element.isDisplayed();
            } catch (Exception exc)
            {
                visible = false;
            }
        } catch (NoSuchElementException e)
        {
            visible = false;
        } catch (ElementNotInteractableException e)
        {
            visible = false;
        }
        return visible;
    }

    /**
     * Verifies if element is editable on the page
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     element to be verified
     * @param description description logical name of specified WebElement, used for Logging purposes in report
     * @return - true or false
     */
    public static Boolean isElementEditable(Config testConfig, WebElement element, String description)
    {
        Boolean editable = false;
        try
        {
            enterData(testConfig, element, "Edited Data", description);
            testConfig.logComment("Element " + description + " is editable");
            editable = true;
        } catch (InvalidElementStateException e)
        {
            testConfig.logComment("Element " + description + " is not editable");
            editable = false;
        }
        return editable;
    }

    public static Boolean IsElementEnabled(Config testConfig, WebElement element)
    {
        Boolean visible = true;
        if (element == null)
        {
            return false;
        }
        try
        {
            visible = element.isEnabled();
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            visible = element.isDisplayed();
        } catch (NoSuchElementException e)
        {
            visible = false;
        } catch (ElementNotInteractableException e)
        {
            visible = false;
        }
        return visible;
    }

    /**
     * Presses the given Key in the specified WebElement
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     Filename WebElement where data needs to be entered
     * @param key         key to the entered
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void KeyPress(Config testConfig, WebElement element, Keys key, String description)
    {
        testConfig.logComment("Press the key '" + key.toString() + "' on " + description);
        element.sendKeys(key);
    }

    /**
     * @param testConfig  test config instance for the driver instance
     * @param element     WebElement on which mouse is to be moved
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void mouseMove(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Move Mouse on '" + description + "'");
        // Locatable hoverItem = (Locatable) element;
        // Mouse mouse = ((HasInputDevices) testConfig.driver).getMouse();
        // mouse.mouseMove(hoverItem.getCoordinates());
        // Point elementPoint = element.getLocation();
        Actions action = new Actions(testConfig.driver);
        action.moveToElement(element).perform();
        // action.moveToElement(element, elementPoint.getX(), elementPoint.getY()).build().perform();
    }

    /**
     * @param testConfig  test config instance for the driver instance
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void clickViaCoordinates(Config testConfig, int x, int y, String description)
    {
        testConfig.logComment("Click Mouse on '" + description + "' at coordinates -" + x + "," + y);
        Actions action = new Actions(testConfig.driver);
        action.moveByOffset(x, y).click().build().perform();
    }

    /**
     * Method used to scroll up and down horizontally in browser
     *
     * @param testConfig - Pass object of config
     * @param from       - pointer in string
     * @param to         - pointer in string
     */
    public static void pageScroll(Config testConfig, String from, String to)
    {
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        js.executeScript("window.scrollBy(" + from + "," + to + ")");
    }

    /**
     * Warning : Don't use this function for regular cases its just for special case when we need to wait and refresh the page to get the element
     *
     * @param testConfig     - Pass object of config
     * @param how            - how to fetch the element
     * @param valueOfLocator - value of locator
     * @param description    - comment
     * @return - weblement
     */
    public static WebElement refreshAndGetPageElement(Config testConfig, How how, String valueOfLocator, String description)
    {
        int counter = 5;
        testConfig.logComment("Executing refreshAndGetPageElement for element - '" + description + "'");
        WaitHelper.waitForSeconds(testConfig, 5);
        WebElement element = Element.getPageElement(testConfig, how, valueOfLocator);
        while (counter > 0 && !Element.isElementDisplayed(testConfig, element))
        {
            Browser.refreshBrowser(testConfig);
            WaitHelper.waitForSeconds(testConfig, 5);
            element = Element.getPageElement(testConfig, how, valueOfLocator);
            counter--;
        }
        if (counter <= 0)
        {
            testConfig.logFailToEndExecution("Even after 5 tries, element '" + description + "' is not loaded");
        }
        return element;
    }

    /**
     * Makes changes in element's style to make it visible on page
     *
     * @param testConfig - Pass object of config
     * @param csspath    - for locating element
     * @return Webelement found
     */
    public static WebElement reveal(Config testConfig, String csspath)
    {
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        String strJS = "document.querySelectorAll(\"" + csspath + "\")[0]";
        testConfig.logComment(strJS);
        js.executeScript(strJS + ".style.display = \"block\";");
        js.executeScript(strJS + ".style.visibility = 'visible';");
        js.executeScript(strJS + ".style.opacity = 1;");
        js.executeScript(strJS + ".style.width = '1px';");
        js.executeScript(strJS + ".style.height = '1px';");
        WebElement elementToBeClicked = getPageElement(testConfig, How.css, csspath);
        testConfig.logComment("Revealed element with css path " + csspath);
        return elementToBeClicked;
    }

    /**
     * Makes changes in element's style to make it visible on page
     *
     * @param testConfig - Pass object of config
     * @param element    - Webelement to reveal
     * @return Webelement
     */
    public static WebElement reveal(Config testConfig, WebElement element)
    {
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        js.executeScript("arguments[0].style.display = \"block\";", element);
        js.executeScript("arguments[0].style.visibility = 'visible';", element);
        js.executeScript("arguments[0].style.opacity = 1;", element);
        js.executeScript("arguments[0].style.width = '1px';", element);
        js.executeScript("arguments[0].style.height = '1px';", element);
        return element;
    }

    /**
     * This function reveals and clicks on element
     *
     * @param testConfig  - Pass object of config - for logging purposes
     * @param csspath     - path to element
     * @param description - for logging purposes
     */
    public static void revealAndClick(Config testConfig, String csspath, String description)
    {
        WebElement elementToBeClicked = reveal(testConfig, csspath);
        try
        {
            Element.click(testConfig, elementToBeClicked, description);
        } catch (StaleElementReferenceException elementReferenceException)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            elementToBeClicked = getPageElement(testConfig, How.css, csspath);
            Element.click(testConfig, elementToBeClicked, description);
        }
        hideRevealedElement(testConfig, csspath);
    }

    /**
     * Selects the given 'value' attribute for the specified WebElement
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement to select
     * @param value       value to the selected
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void selectDropdownValue(Config testConfig, WebElement element, String value, String description)
    {
        if (!StringUtils.isEmpty(value))
        {
            testConfig.logComment("Select the " + description + " dropdown value '" + value + "'");
            Select sel = new Select(element);
            sel.selectByValue(value);
        } else
        {
            testConfig.logComment("Skipped value selection for " + description);
        }
    }

    /**
     * Selects the given visible text 'value' for the specified WebElement
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement to select
     * @param value       visible text value to the selected
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void selectVisibleText(Config testConfig, WebElement element, String value, String description)
    {
        if (!StringUtils.isEmpty(value))
        {
            testConfig.logComment("Select the " + description + " dropdown text '" + value + "'");

            Select sel = new Select(element);
            sel.selectByVisibleText(value);

            try
            {
                sel = new Select(element);
                element.click();
                sel.selectByVisibleText(value);
            } catch (Exception e)
            {
            }
        } else
        {
            testConfig.logComment("Skipped text selection for " + description);
        }
    }

    /**
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement to select
     * @param value       String of Visible text values to be selected
     * @param description for logging purpose.
     */
    public static void selectVisibleText(Config testConfig, WebElement element, String[] value, String description)
    {
        Select sel = new Select(element);
        sel.deselectAll();
        for (int i = 0; i < value.length; i++)
        {
            if (!StringUtils.isEmpty(value[i]))
            {
                testConfig.logComment("Select the " + description + " dropdown text '" + value[i] + "'");
                sel.selectByVisibleText(value[i]);
            } else
            {
                testConfig.logComment("Skipped text selection for " + description);
            }
        }
    }

    public static void selectByValue(Config testConfig, WebElement element, String value, String description)
    {
        Select sel = new Select(element);
        if (!StringUtils.isEmpty(value))
        {
            testConfig.logComment("Select the " + description + " dropdown text '" + value + "'");
            sel.selectByValue(value);
        } else
        {
            testConfig.logComment("Value of the element is empty");
            testConfig.logComment("Skipped text selection for " + description);
        }
    }

    /**
     * This function can be used to add/update the value of any attribute of a weblement
     *
     * @param testConfig     - Pass object of config
     * @param element        - weblement
     * @param attributeName  - attributeName
     * @param attributeValue - attributeValue
     */
    public static void setAttribute(Config testConfig, WebElement element, String attributeName, String attributeValue)
    {
        JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attributeName, attributeValue);
    }

    /**
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     WebElement to be unchecked
     * @param description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void uncheck(Config testConfig, WebElement element, String description)
    {
        testConfig.logComment("Un-Check '" + description + "'");
        if (element == null)
        {
            testConfig.logFail("Element is NULL, so can't uncheck the checkbox!!");
        } else
        {
            if (element.isSelected())
            {
                try
                {
                    clickWithoutLog(testConfig, element);
                    WaitHelper.waitForSeconds(testConfig, 1);
                } catch (StaleElementReferenceException e)
                {
                    testConfig.logWarning("Stale element reference exception. Trying again...");
                    clickWithoutLog(testConfig, element);
                }
            } else
            {
                testConfig.logFail("Checkbox is already unchecked, so can't uncheck it again !!");
            }
        }
    }

    /**
     * This function reveals file input element and sends file path
     *
     * @param testConfig  - Pass object of config - for logging purposes
     * @param csspath     - path to uploader input
     * @param filePath    - path to file
     * @param description - for logging purposes
     */
    public static void uploadFile(Config testConfig, String csspath, String filePath, String description)
    {
        WebElement fileInput = reveal(testConfig, csspath);
        try
        {
            Element.enterFileName(testConfig, fileInput, filePath, description);
        } catch (StaleElementReferenceException elementReferenceException)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            fileInput = getPageElement(testConfig, How.css, csspath);
            Element.enterFileName(testConfig, fileInput, filePath, description);
        }

        try
        {
            fileInput = getPageElement(testConfig, How.css, csspath);
            if (!Popup.isAlertPresent(testConfig) && fileInput.isDisplayed())
            {
                Element.clickThroughJS(testConfig, fileInput, "File input");
                WaitHelper.waitForSeconds(testConfig, 1);
                Element.enterFileName(testConfig, fileInput, filePath, description);
                WaitHelper.waitForSeconds(testConfig, 3);
            }
        } catch (Exception ex)
        {
            testConfig.logComment("Exception occured while trying to upload : " + ex);
        }
    }

    /**
     * Only use this function when uploadFile function is not working + upload button and input file textbox are two different elements
     *
     * @param testConfig          - Pass object of config
     * @param uploadButtonElement - element of upload button
     * @param inputFileElement    - webelement of input type element
     * @param filePath            - path of file to be uploaded
     * @param description         - comment
     */
    public static void uploadFileExtended(Config testConfig, WebElement uploadButtonElement, WebElement inputFileElement, String filePath, String description)
    {
        Element.click(testConfig, uploadButtonElement, "Upload Button");
        WaitHelper.waitForSeconds(testConfig, 1);
        Element.enterFileName(testConfig, inputFileElement, filePath, description);
        WaitHelper.waitForSeconds(testConfig, 3);
    }

    /**
     * Verifies if element is absent on the page
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     element to be verified
     * @param description description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void verifyElementNotPresent(Config testConfig, WebElement element, String description)
    {
        try
        {
            if (!isElementDisplayed(testConfig, element))
            {
                testConfig.logPass("Verified the absence of element '" + description + "' on the page");
            } else
            {
                testConfig.logFail("Element '" + description + "' is present on the page");
            }
        } catch (StaleElementReferenceException e)
        {
            testConfig.logComment("Stale element reference exception. Trying again...");
            if (!isElementDisplayed(testConfig, element))
            {
                testConfig.logPass("Verified the absence of element '" + description + "' on the page");
            } else
            {
                testConfig.logFail("Element '" + description + "' is present on the page");
            }
        }
    }

    /**
     * Verifies if element is present on the page
     *
     * @param testConfig  - Pass object of config Config instance used for logging
     * @param element     element to be verified
     * @param description description logical name of specified WebElement, used for Logging purposes in report
     */
    public static void verifyElementPresent(Config testConfig, WebElement element, String description)
    {
        // Scroll Up or Down if element is not visible
        try
        {
            JavascriptExecutor jse = (JavascriptExecutor) testConfig.driver;
            jse.executeScript("arguments[0].scrollIntoView(false)", element);
        } catch (WebDriverException wde)
        {
        }
        if (element != null && element.isDisplayed())
        {
            testConfig.logPass("Verified the presence of element '" + description + "' on the page");
        } else
        {
            testConfig.logFail("Element '" + description + "' is not present on the page");
        }
    }

    public static WebElement getIframeElement(Config testConfig, How how, String what)
    {
        List<WebElement> frames = testConfig.driver.findElements(By.tagName("iframe"));

        if (frames.isEmpty())
        {
            return null;
        }
        WebElement element = null;

        for (WebElement fr : frames)
        {
            if (element != null)
            {
                return element;
            }

            try
            {
                testConfig.driver.switchTo().frame(fr);
            } catch (StaleElementReferenceException e)
            {
                testConfig.driver.switchTo().defaultContent();
                try
                {
                    testConfig.driver.switchTo().frame(fr);
                } catch (StaleElementReferenceException ex)
                {
                    testConfig.driver.switchTo().defaultContent();
                }
            }

            element = getPageElement(testConfig, how, what);

            if (element == null)
            {
                element = getIframeElement(testConfig, how, what);
            }
        }
        return element;
    }

    public enum How
    {
        accessibility,
        className,
        css,
        id,
        linkText,
        name,
        partialLinkText,
        tagName,
        xPath
    }
}
