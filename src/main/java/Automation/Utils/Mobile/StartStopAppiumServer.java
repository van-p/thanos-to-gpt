package Automation.Utils.Mobile;

import Automation.Utils.Config;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

public class StartStopAppiumServer
{

    public static void startAppiumServer(Config testConfig)
    {
        killAllAppiumServers(testConfig);
        testConfig.logComment("Starting new Appium Server...");
        // Set Capabilities
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("noReset", testConfig.getRunTimeProperty("noReset"));
        desiredCapabilities.setCapability("fullReset", testConfig.getRunTimeProperty("fullReset"));
        try
        {
            AppiumServiceBuilder builder = new AppiumServiceBuilder();
            // builder.withIPAddress("127.0.0.1");
            builder.usingAnyFreePort();
            builder.withCapabilities(desiredCapabilities);
            builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
            builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
            builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));

            // Start the server with the builder
            testConfig.appiumServer = AppiumDriverLocalService.buildService(builder);
            testConfig.appiumServer.start();
            testConfig.logComment("Appium Server started on :- " + testConfig.appiumServer.getUrl());
        } catch (Exception e)
        {
            testConfig.logWarning("Unable to start appium server");
            e.printStackTrace();
        }
    }

    public static void stopAppiumServer(Config testConfig)
    {
        try
        {
            testConfig.appiumServer.stop();
            testConfig.logComment("Appium Server stopped");
        } catch (Exception e)
        {
            testConfig.logWarning("Unable to stop appium server");
            e.printStackTrace();
        }
    }

    private static void killAllAppiumServers(Config testConfig)
    {
        if (!Config.isRemoteExecution)
        {
            // Code to stop all Appium servers
            if (Config.osName.startsWith("Window"))
            {
                testConfig.logCommentForDebugging("Closing all appium servers on Windows");
                try
                {
                    Runtime.getRuntime().exec("taskkill /f /im node.exe");
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else if (Config.osName.startsWith("Mac"))
            {
                testConfig.logCommentForDebugging("Closing all appium servers on Mac");
                String[] command = {"/usr/bin/killall", "-KILL", "node"};
                try
                {
                    Runtime.getRuntime().exec(command);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            testConfig.logComment("All Appium servers stopped first");
        }
    }
}