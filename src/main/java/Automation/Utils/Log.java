package Automation.Utils;

import org.testng.Assert;
import org.testng.Reporter;

class Log
{

    public static void comment(Config testConfig, String message)
    {
        comment(testConfig, message, "Black");
    }

    public static void comment(Config testConfig, String message, String color)
    {
        logToStandard(testConfig, message);
        message = "<font color='" + color + "'>" + message + "</font></br>";
        logInReporter(message);
        testConfig.testLog = testConfig.testLog.concat(message);
    }

    public static void commentJson(Config testConfig, String initialMessage, String fullMessage, String color)
    {
        logToStandard(testConfig, initialMessage);
        logToStandard(testConfig, fullMessage);
        fullMessage = fullMessage.replaceAll("\n", "</br>").replaceAll(" ", "&nbsp");
        String updatedMessage = fullMessage;
        String htmlCode = "";
        htmlCode = "<style>\n"
                + ".collapsible {\n"
                + "  background-color: #999;\n"
                + "  color: white;\n"
                + "  cursor: pointer;\n"
                + "  padding: 4px;\n"
                + "  text-align: left;\n"
                + "  font-size: 11px;\n"
                + "}"
                + "\n"
                + ".active, .collapsible:hover {\n"
                + "  background-color: green;\n"
                + "}\n"
                + "\n"
                + ".content {\n"
                + "  padding: 0 16px;\n"
                + "  display: none;\n"
                + "  overflow: hidden;\n"
                + "  background-color: #f1f1f1;\n"
                + "}\n"
                + "</style>"
                + "<script>\n"
                + "var coll = document.getElementsByClassName(\"collapsible\");\n"
                + "var i;\n"
                + "\n"
                + "for (i = 0; i < coll.length; i++) {\n"
                + "  coll[i].addEventListener(\"click\", function() {\n"
                + "    this.classList.toggle(\"active\");\n"
                + "    var content = this.nextElementSibling;\n"
                + "    if (content.style.display === \"block\") {\n"
                + "      content.style.display = \"none\";\n"
                + "    } else {\n"
                + "      content.style.display = \"block\";\n"
                + "    }\n"
                + "  });\n"
                + "}\n"
                + "</script>";
        updatedMessage = htmlCode + updatedMessage;
        updatedMessage = "<font color='" + color + "'>" + initialMessage + "</font>" + htmlCode + "<button type='button' class='collapsible'>Expand this section</button><div class='content'><font color='black'>" + updatedMessage + "</font></div></br>";

        logInReporter(updatedMessage);
        testConfig.testLog = testConfig.testLog.concat(fullMessage);
    }

    public static void fail(Config testConfig, String message)
    {
        failure(testConfig, message);
        Browser.takeScreenshot(testConfig);
    }

    public static void failure(Config testConfig, String message)
    {
        String tempMessage = message;
        testConfig.softAssert.fail(message);
        logToStandard(testConfig, message);
        message = "<font color='Red'>" + message + "</font></br>";
        logInReporter(message);
        testConfig.testLog = testConfig.testLog.concat(message);
        // Stop the execution if end execution flag is ON
        if (testConfig.endExecutionOnfailure)
        {
            Assert.fail(tempMessage);
        }
    }

    public static void logInReporter(String message)
    {
        String timestamp = DataGenerator.getCurrentDateTime("HH:mm:ss");
        Reporter.log("<font style='font-size:110%;font-family:Arial;'>[" + timestamp + "] " + message + "</font>");
    }

    private static void logToStandard(Config testConfig, String message)
    {
        String timestamp = DataGenerator.getCurrentDateTime("HH:mm:ss");
        if (Config.isRemoteExecution)
        {
            if (Config.isDebugMode || Config.isMobileAppExecution)
            {
                System.out.println("[" + testConfig.testcaseName + "][" + timestamp + "] " + message);
            }
        } else
        {
            System.out.println("[" + timestamp + "] " + message);
        }
    }

    public static void pass(Config testConfig, String message)
    {
        logToStandard(testConfig, message);
        message = "<font color='Green'>" + message + "</font></br>";
        logInReporter(message);
        testConfig.testLog = testConfig.testLog.concat(message);
    }

    public static void step(Config testConfig, String message)
    {
        message = "Test Step: " + message;
        logToStandard(testConfig, message);
        message = "<b><font style='color:white;font-size:11px;padding:2px;background-color:#ff9800;'>" + message + "</font></b></br>";
        logInReporter(message);
        testConfig.testLog = testConfig.testLog.concat(message);
    }

    public static void warning(Config testConfig, String message)
    {
        logToStandard(testConfig, message);
        message = "<font color='Orange'>" + message + "</font></br>";
        logInReporter(message);
        testConfig.testLog = testConfig.testLog.concat(message);
    }

    public static void warning(Config testConfig, String message, boolean takeScreenshot)
    {
        if (takeScreenshot)
        {
            Browser.takeScreenshot(testConfig);
        }
        warning(testConfig, message);
    }

    public static void logCommentWithOptionalConfig(Config testConfig, String message)
    {
        if (testConfig != null)
        {
            testConfig.logComment(message);
        } else
        {
            System.out.println(message);
        }
    }
}