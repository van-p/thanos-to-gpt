package Automation.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertHelper
{

    public static void assertDynamicText(Config testConfig, String description, List<String> expectedText, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        String result = Element.getText(testConfig, element, description);
        AssertHelper.compareTrue(testConfig, "Element text:" + result + " contains in the list", expectedText.contains(result));
    }

    public static void assertElementEnabled(Config testConfig, String description, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        boolean result = element.isEnabled();
        if (result)
        {
            testConfig.logPass("Element '" + description + "' is enabled on the page");
        } else
        {
            testConfig.logFail("Element '" + description + "' is NOT enabled on the page");
        }
    }

    /**
     * This method is used to assert if Element Is Displayed, if element is not displayed then testcase will fail
     *
     * @param testConfig  - Pass object of config
     * @param description - comment to show in logs - comment to show in logs
     * @param element     - weblement
     */
    public static void assertElementIsDisplayed(Config testConfig, String description, WebElement element)
    {
        boolean result = Element.isElementDisplayed(testConfig, element);
        if (result)
        {
            testConfig.logPass("Element '" + description + "' is displayed on the page");
        } else
        {
            testConfig.logFail("Element '" + description + "' is NOT displayed on the page");
        }
    }

    /**
     * This method is used to assert if Element Is NOT Displayed, if element is STILL displayed then testcase will fail
     *
     * @param testConfig  - Pass object of config
     * @param description - comment to show in logs - comment to show in logs
     * @param element     - webelement
     */
    public static void assertElementIsNotDisplayed(Config testConfig, String description, WebElement element)
    {
        boolean result = Element.isElementDisplayed(testConfig, element);
        if (result)
        {
            testConfig.logFail("Element '" + description + "' is still displayed on the page");
        } else
        {
            testConfig.logPass("Element '" + description + "' is not displayed on the page");
        }
    }

    public static void assertElementNotEnabled(Config testConfig, String description, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        boolean result = element.isEnabled();
        if (result)
        {
            testConfig.logFail("Element '" + description + "' is still enabled on the page");
        } else
        {
            testConfig.logPass("Element '" + description + "' is not enabled on the page");
        }
    }

    public static void assertElementSelected(Config testConfig, String description, boolean expectedSelection, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        boolean actualSelection = element.isSelected();
        AssertHelper.compareEquals(testConfig, description, expectedSelection, actualSelection);
    }

    public static void assertElementSelectedDropdown(Config testConfig, String description, String expectedSelection, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        String actualSelection = Element.getSlectedValue(testConfig, element, description);
        AssertHelper.compareEquals(testConfig, description, expectedSelection, actualSelection);
    }

    /**
     * This method is used to assert if Text of an Element, if text of element does not match with expected text then testcase will fail
     *
     * @param testConfig   - Pass object of config
     * @param description  - comment to show in logs
     * @param expectedText - expected value to match
     * @param element      - weblement
     */
    public static void assertElementText(Config testConfig, String description, String expectedText, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        String result = Element.getText(testConfig, element, description);
        AssertHelper.compareEquals(testConfig, description, expectedText, result);
    }

    /**
     * This method is used to assert URL Link of an Element, if the link of element does not match with expected link then testcase will fail
     *
     * @param testConfig   - Pass object of config
     * @param description  - comment to show in logs
     * @param expectedLink - expected value to match
     * @param element      - weblement
     */
    public static void assertElementLink(Config testConfig, String description, String expectedLink, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        String result = Element.getAttributeText(testConfig, element, "href", description);
        AssertHelper.compareEquals(testConfig, description, expectedLink, result);
    }

    public static void assertImageIsLoaded(Config testConfig, String description, WebElement element)
    {
        WaitHelper.waitForElementToBeDisplayed(testConfig, element, description);
        Boolean imagePresent = (Boolean) ((JavascriptExecutor) testConfig.driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", element);
        // Incase image is not loaded then wait for 5 seconds and then try again
        if (!imagePresent)
        {
            testConfig.logWarning(description + " image is not loaded in first time, so trying again...");
            WaitHelper.waitForSeconds(testConfig, 5);
            imagePresent = (Boolean) ((JavascriptExecutor) testConfig.driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", element);
        }
        AssertHelper.compareTrue(testConfig, description, imagePresent);
    }

    public static void assertNotNull(Config testConfig, String description, Object object)
    {
        if (object == null)
        {
            testConfig.logFail("Failed to verify '" + description + "' as Not NULL");
        } else
        {
            testConfig.logPass("Verified '" + description + "' as Not NULL");
        }
    }

    public static void assertNull(Config testConfig, String description, Object object)
    {
        if (object != null)
        {
            testConfig.logFail("Failed to verify '" + description + "' as NULL");
        } else
        {
            testConfig.logPass("Verified '" + description + "' as NULL");
        }
    }

    public static void assertPartialElementText(Config testConfig, String description, String expectedText, WebElement element)
    {
        WaitHelper.waitForOptionalElement(testConfig, element, description);
        String result = Element.getText(testConfig, element, description);
        AssertHelper.compareContains(testConfig, description, expectedText, result);
    }

    public static void assertPartialElementTextList(Config testConfig, String description, String expectedText, List<WebElement> elementList)
    {
        for (WebElement groupName : elementList)
        {
            WaitHelper.waitForOptionalElement(testConfig, groupName, description);
            String result = Element.getText(testConfig, groupName, description);
            if (result.contains(expectedText))
            {
                AssertHelper.compareContains(testConfig, description, expectedText, result);
            }
        }
    }

    public static void assertPartialElementTextNotInList(Config testConfig, String description, String expectedText, List<WebElement> elementList)
    {
        for (WebElement groupName : elementList)
        {
            String result = Element.getText(testConfig, groupName, description);
            AssertHelper.compareFalse(testConfig, description, result.contains(expectedText));
        }
    }

    /**
     * Compare 2 strings to check if 'expected' string is present in 'actual' string
     *
     * @param testConfig - Pass object of config
     * @param what       - comment
     * @param expected   - expected
     * @param actual     - actual
     */
    public static void compareContains(Config testConfig, String what, String expected, String actual)
    {
        actual = actual.trim();
        if (actual != null)
        {
            if (!actual.contains(expected.trim()))
            {
                Pattern expectedPattern = Pattern.compile(expected.substring(0, 2));
                // getFirstMatchingPoint
                int findPoint = 0;
                Matcher m = expectedPattern.matcher(actual.trim());
                if (m.find())
                {
                    findPoint = m.start();
                } else
                {
                    findPoint = -1;
                }
                actual = actual.substring(0, findPoint) + "<br/>" + actual.substring(findPoint);
                testConfig.logFail(what, expected, actual);
            } else
            {
                testConfig.logPass(what, expected + "...");
            }
        } else
        {
            testConfig.logFail(what, expected, actual);
        }
    }

    public static void compareContainsInList(Config testConfig, List<WebElement> expectedElem, String actual)
    {
        List<String> expectedTexts = new ArrayList<String>();
        actual = actual.trim();
        if (actual != null)
        {
            for (WebElement elemText : expectedElem)
            {
                expectedTexts.add(Element.getText(testConfig, elemText, "Adding String values"));
            }
            if (expectedTexts.contains(actual))
            {
                testConfig.logComment(actual + " present in the fetched texts");
            } else
            {
                testConfig.logFail(actual + " NOT present in the fetched texts");
            }
        } else
        {
            testConfig.logFail(actual + " NOT present in the fetched texts");
        }
    }

    /**
     * Compare two integer, double or float type values using a generic function.
     *
     * @param <T>        - object
     * @param testConfig - Pass object of config
     * @param what       - comment
     * @param expected   - expected
     * @param actual     - actual
     */
    public static <T> void compareEquals(Config testConfig, String what, T expected, T actual)
    {
        if (expected == null & actual == null)
        {
            testConfig.logPass(what, actual);
            return;
        }

        if (actual != null)
        {
            if (!actual.equals(expected))
            {
                testConfig.logFail(what, expected, actual);
            } else
            {
                testConfig.logPass(what, actual);
            }
        } else
        {
            testConfig.logFail(what, expected, actual);
        }
    }

    /**
     * This method is used to compare a value to false. If the value is false, the test case passes else fails.
     *
     * @param testConfig    - Pass object of config
     * @param conditionDesc - provide details about comparison being made or condition to be verified
     * @param actual        - actual
     */
    public static void compareFalse(Config testConfig, String conditionDesc, boolean actual)
    {
        if (!actual)
        {
            testConfig.logPass("Verified '" + conditionDesc + "'");
        } else
        {
            testConfig.logFail("Failed to verify '" + conditionDesc + "'");
        }
    }

    /**
     * compare the key stuctures of 2 JSONs
     *
     * @param testConfig   - Pass object of config
     * @param expectedJson - expected
     * @param actualJson   - actual
     * @param description  - comment to show in logs
     */
    public static boolean compareJsonKeys(Config testConfig, JSONObject expectedJson, JSONObject actualJson, String description)
    {
        try
        {
            if (expectedJson == null)
            {
                if (actualJson == null)
                {
                    if (Config.isDebugMode)
                        testConfig.logPass("Verified '" + description + "'");
                    return true;
                }
                testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                testConfig.logFail("Actual JSON is not null. Expected is: '" + expectedJson + "' but Actual is: '" + CommonUtilities.formatStringAsJson(actualJson.toString()) + "'.");
                return false;
            }
            if (expectedJson.keySet().isEmpty())
            {
                if (actualJson.keySet().isEmpty())
                {
                    if (Config.isDebugMode)
                        testConfig.logPass("Verified '" + description + "'");
                    return true;
                }
                testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                testConfig.logFail("Actual JSON is not empty. Expected has: '" + expectedJson.keySet() + "' but Actual has: '" + actualJson.keySet() + "'.");
                return false;
            }
            List<String> exList = Arrays.asList(JSONObject.getNames(expectedJson));
            Collections.sort(exList);
            List<String> acList = Arrays.asList(JSONObject.getNames(actualJson));
            Collections.sort(acList);
            if (!acList.containsAll(exList))
            {
                testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                testConfig.logFail("Actual JSON doesn't contain all expected keys. Expected has: '" + expectedJson.keySet() + "' but Actual has: '" + actualJson.keySet() + "'.");
                expectedJson.keySet().removeAll(actualJson.keySet());
                testConfig.logComment("Missing keys - " + expectedJson.keySet());
                return false;
            }
            for (String key : exList)
            {
                Object exVal = expectedJson.get(key);
                Object acVal = actualJson.get(key);
                if (exVal.getClass() != acVal.getClass())
                {
                    testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                    testConfig.logFail("Classes of actual and expected key '" + key + "' are different. Expected is: '" + exVal.getClass() + "' but Actual is: '" + acVal.getClass() + "'.");
                    return false;
                }
                if (exVal instanceof JSONObject)
                {
                    testConfig.logCommentForDebugging("Verifying key: '" + key + "'");
                    compareJsonKeys(testConfig, new JSONObject(exVal.toString()), new JSONObject(acVal.toString()), description);
                } else if (exVal instanceof JSONArray)
                {
                    JSONArray exArr = new JSONArray(exVal.toString());
                    JSONArray acArr = new JSONArray(acVal.toString());
                    if (exArr.length() == acArr.length())
                    {
                        for (int i = 0; i < acArr.length(); i++)
                        {
                            Object e = exArr.get(i);
                            Object a = acArr.get(i);
                            if (e.getClass() != a.getClass())
                            {
                                testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                                testConfig.logFail("Classes of actual and expected json array objects '" + key + "' are different. Expected is: '" + e.getClass() + "' but Actual is: '" + a.getClass() + "'.");
                                return false;
                            }
                            if (a instanceof JSONObject)
                            {
                                compareJsonKeys(testConfig, new JSONObject(e.toString()), new JSONObject(a.toString()), description);
                            }
                        }
                    } else
                    {
                        Object e = exArr.get(0);
                        for (Object a : acArr)
                        {
                            if (e.getClass() != a.getClass())
                            {
                                testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                                testConfig.logFail("Classes of actual and expected json array objects '" + key + "' are different. Expected is: '" + e.getClass() + "' but Actual is: '" + a.getClass() + "'.");
                                return false;
                            }
                            if (a instanceof JSONObject)
                            {
                                compareJsonKeys(testConfig, new JSONObject(e.toString()), new JSONObject(a.toString()), description);
                            }
                        }
                    }
                } else if (acVal instanceof JSONObject || acVal instanceof JSONArray)
                {
                    testConfig.logCommentForDebugging("Failed to verify '" + description + "'");
                    testConfig.logFail("Actual is not json object or json array but it should be. Expected is: '" + CommonUtilities.formatStringAsJson(expectedJson.toString()) + "' but Actual is: '" + CommonUtilities.formatStringAsJson(actualJson.toString()) + "'.");
                    return false;
                }
            }
            if (Config.isDebugMode)
                testConfig.logPass("Verified '" + description + "'");
            return true;
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail("Failed to verify the Response Keys!", e);
            return false;
        }
    }

    /**
     * This method is used to compare a value to True. If the value is True, the test case passes else fails.
     *
     * @param testConfig    - Pass object of config
     * @param conditionDesc - provide details about comparison being made or condition to be verified
     * @param actual        - actual
     */
    public static void compareTrue(Config testConfig, String conditionDesc, boolean actual)
    {
        if (!actual)
        {
            testConfig.logFail("Failed to verify '" + conditionDesc + "'");
        } else
        {
            testConfig.logPass("Verified '" + conditionDesc + "'");
        }
    }

    /**
     * Matches string from an ArraayList<String>
     *
     * @param inputStr
     * @param items
     * @return
     */
    public static boolean assertValuePresentInList(Config testConfig, String what, String[] items, String inputStr)
    {
        if (!Arrays.stream(items).parallel().anyMatch(inputStr::contains))
        {
            testConfig.logFail("Failed to verify '" + what + "' as " + "'" + inputStr + "'");
        } else
        {
            testConfig.logPass("Verified '" + what + "' as " + "'" + inputStr + "'");
        }
        return true;
    }

    /*
     * This function is to compare values for each key for 2 jsons with similar structure.
     */
    public static void compareJsonValues(Config testConfig, JSONObject expectedJson, JSONObject actualJson, boolean ignoreCasing)
    {
        if (expectedJson.isEmpty())
        {
            return;
        }
        List<String> exList = Arrays.asList(JSONObject.getNames(expectedJson));
        Collections.sort(exList);
        List<String> acList = Arrays.asList(JSONObject.getNames(actualJson));
        Collections.sort(acList);

        for (String key : exList)
        {
            Object exVal = expectedJson.get(key);
            Object acVal = actualJson.get(key);
            if (exVal instanceof JSONObject)
            {
                compareJsonValues(testConfig, new JSONObject(exVal.toString()), new JSONObject(acVal.toString()), ignoreCasing);
            } else if (exVal instanceof JSONArray)
            {
                JSONArray exArr = new JSONArray(exVal.toString());
                JSONArray acArr = new JSONArray(acVal.toString());
                if (exArr.length() == acArr.length())
                {
                    for (int i = 0; i < acArr.length(); i++)
                    {
                        Object e = exArr.get(i);
                        Object a = acArr.get(i);
                        if (e.getClass() != a.getClass())
                        {
                        }
                        if (a instanceof JSONObject)
                        {
                            compareJsonValues(testConfig, new JSONObject(e.toString()), new JSONObject(a.toString()), ignoreCasing);
                        }
                    }
                } else
                {
                    testConfig.logFail("Failed to verify due to different length of JsonArrays. Expected length : " + exArr.length() + " Actual length : " + acArr.length());
                }
            } else
            {
                if (ignoreCasing)
                {
                    AssertHelper.compareEquals(testConfig, key, expectedJson.get(key).toString().toLowerCase(), actualJson.get(key).toString().toLowerCase());
                } else
                {
                    AssertHelper.compareEquals(testConfig, key, expectedJson.get(key), actualJson.get(key));
                }
            }
        }
    }

    /**
     * Compare 2 csv files.
     *
     * @param testConfig             the Config object
     * @param description            what files are being compared
     * @param expectedOutputFileName the filename with the expected data
     * @param actualOutputFileName   the actual csv file
     */
    public static void compareCsvFiles(Config testConfig, String description, String expectedOutputFileName, String actualOutputFileName)
    {
        BufferedReader actualFile;
        BufferedReader expectedFile;
        try
        {
            actualFile = new BufferedReader(new FileReader(actualOutputFileName));
            expectedFile = new BufferedReader(new FileReader(expectedOutputFileName));
            String actualDataRow = actualFile.readLine();
            String expectedDataRow = expectedFile.readLine();
            int i = 0;
            while (expectedDataRow != null)
            {
                i++;
                expectedDataRow = expectedDataRow.replace("\"", "");
                AssertHelper.compareEquals(testConfig, "Output CSV Row No " + i, expectedDataRow, actualDataRow);
                actualDataRow = actualFile.readLine();
                expectedDataRow = expectedFile.readLine();
            }
            actualFile.close();
            expectedFile.close();
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail("Something went wrong with File", e);
        }
    }
}