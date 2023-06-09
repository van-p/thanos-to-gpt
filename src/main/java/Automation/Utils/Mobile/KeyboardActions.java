package Automation.Utils.Mobile;

import Automation.Utils.Config;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class KeyboardActions
{

    public static void keyboardDownKey(Config testConfig, int tabCount)
    {
        try
        {
            Actions actions = new Actions(testConfig.driver);
            for (int i = 0; i <= tabCount; i++)
            {
                actions.sendKeys(Keys.ARROW_DOWN).build().perform();
            }
        } catch (Exception e)
        {
            testConfig.logComment("Could not switch tabs as it was not present, Exception found is ...." + e);
        }
    }

    public static void keyboardEnter(Config testConfig)
    {
        try
        {
            Actions actions = new Actions(testConfig.driver);
            actions.sendKeys(Keys.ENTER).build().perform();
        } catch (Exception e)
        {
            testConfig.logComment("Could not switch tabs as it was not present, Exception found is ...." + e);
        }
    }

    public static void keyboardTabSwitch(Config testConfig, int tabCount)
    {
        try
        {
            Actions actions = new Actions(testConfig.driver);
            for (int i = 0; i <= tabCount; i++)
            {
                actions.sendKeys(Keys.TAB).build().perform();
            }
        } catch (Exception e)
        {
            testConfig.logComment("Could not switch tabs as it was not present, Exception found is ...." + e);
        }
    }

    public static void selectFromDropdown(Config testConfig, WebElement textBoxElement, String clikableText)
    {
        try
        {
            Actions builder = new Actions(testConfig.driver);
            builder.keyDown(textBoxElement, Keys.SHIFT).sendKeys(textBoxElement, clikableText).keyUp(textBoxElement, Keys.SHIFT).build();
        } catch (Exception e)
        {
            testConfig.logComment("Could not hide keyboard as it was not present, Exception found is ...." + e);
        }
    }

}