package Automation.Utils.Mobile;

import Automation.Utils.CmdHelper;
import Automation.Utils.Config;
import io.appium.java_client.android.AndroidDriver;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class App
{

    @SuppressWarnings("unchecked")
    public static void launchApplication(Config testConfig)
    {
        if (testConfig.getRunTimeProperty("useRealDevice") == null || testConfig.getRunTimeProperty("useRealDevice").equalsIgnoreCase("false"))
        {
            // First launch Emulator
            Emulator.startEmulator(testConfig);
            Emulator.enableInternet(testConfig, true);
        } else
        {
            testConfig.logComment("Using real device for execution...");
            CmdHelper.executeCommandAndWaitForOutput(testConfig, "echo 'ANDROID_HOME='$ANDROID_HOME & $ANDROID_HOME/platform-tools/adb devices");

        }

        DesiredCapabilities desiredCapabilities = null;
        String mobileOS = testConfig.getRunTimeProperty("platform").toLowerCase().trim();
        if (testConfig.appiumDriver == null)
        {
            OS os = null;
            try
            {
                os = OS.valueOf(mobileOS);
            } catch (IllegalArgumentException e)
            {
                testConfig.logFailToEndExecution("Invalid OS name is passed");
            }
            switch (os)
            {
                case android:
                    desiredCapabilities = new DesiredCapabilities();
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("uiautomator2ServerInstallTimeout", "60000");
                    desiredCapabilities.setCapability("uiautomator2ServerLaunchTimeout", "60000");
                    desiredCapabilities.setCapability("androidInstallTimeout", "120000");
                    desiredCapabilities.setCapability("adbExecTimeout", "60000");
                    desiredCapabilities.setCapability("automationName", "UiAutomator2");
                    desiredCapabilities.setCapability("deviceName", "Android SDK built for x86");
                    desiredCapabilities.setCapability("app", testConfig.getRunTimeProperty("app"));
                    desiredCapabilities.setCapability("appPackage", testConfig.getRunTimeProperty("appPackageName"));
                    desiredCapabilities.setCapability("appActivity", testConfig.getRunTimeProperty("appActivityName"));
                    break;
                case ios:
                    /*
                     * desiredCapabilities = new DesiredCapabilities(); desiredCapabilities.setCapability("platformName", "Android"); desiredCapabilities.setCapability("platformVersion", testConfig.getRunTimeProperty("platformVersion")); //desiredCapabilities.setCapability("automationName", "UiAutomator2"); desiredCapabilities.setCapability("app",absoluteApkfile);
                     * desiredCapabilities.setCapability("appPackage", testConfig.getRunTimeProperty("appPackageName")); desiredCapabilities.setCapability("appActivity", testConfig.getRunTimeProperty("appActivityName"));
                     */
                    break;
                default:
                    break;
            }

            try
            {
                if (testConfig.getRunTimeProperty("useOptimusCloud").equalsIgnoreCase("true"))
                {

                } else
                {
                    StartStopAppiumServer.startAppiumServer(testConfig);
                    testConfig.logComment("Launching '" + testConfig.getRunTimeProperty("MobileAppName") + "' application...");
                    testConfig.appiumDriver = new AndroidDriver(testConfig.appiumServer.getUrl(), desiredCapabilities);
                }
                // testConfig.appiumDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                testConfig.driver = testConfig.appiumDriver;
            } catch (Exception e)
            {
                testConfig.endExecutionOnfailure = true;
                testConfig.logExceptionAndFail(e);
            }
        }
    }

    public static void closeApplication(Config testConfig)
    {
        try
        {
            if (testConfig.appiumDriver != null)
            {
                if (testConfig.getRunTimeProperty("useOptimusCloud").equalsIgnoreCase("true"))
                {

                } else
                {
                    testConfig.appiumDriver.close();
                    testConfig.logComment("Mobile Application closed successfully");
                    StartStopAppiumServer.stopAppiumServer(testConfig);
                }
                testConfig.driver = null;
            }
        } catch (Exception e)
        {
            testConfig.logException("Unable to close Mobile Application for testcase : " + testConfig.testcaseName, e);
        }
    }

    /**
     * DOWNLOADS the specific artifact from the Gitlab
     *
     * @param testConfig       - Pass object of config
     * @param headers
     * @param urlToDownload
     * @param downloadLocation
     */
    private static void downloadArtifactFile(Config testConfig, final Map<String, String> headers, final String urlToDownload, final File downloadLocation)
    {
        testConfig.logComment("Downloading apk file from :- " + urlToDownload);
        File outputFile = new File(String.valueOf(downloadLocation));
        final Response response = RestAssured.given().headers(headers).when().get(urlToDownload).andReturn();
        if (response.getStatusCode() == 200)
        {
            if (outputFile.exists())
            {
                outputFile.delete();
            }

            byte[] fileContents = response.getBody().asByteArray();
            OutputStream outStream = null;
            try
            {
                outStream = new FileOutputStream(outputFile);
                outStream.write(fileContents);
            } catch (Exception e)
            {
                testConfig.logFailToEndExecution("Error while downloading the apk file " + outputFile.getAbsolutePath());
            } finally
            {
                if (outStream != null)
                {
                    try
                    {
                        outStream.close();
                    } catch (IOException e)
                    {
                        testConfig.logWarning("Unable to close outStream");
                    }
                }
            }
            testConfig.logComment("Apk file downloaded successfully");
        } else
        {
            testConfig.logWarning("Unable to download the apk file, will use existing Apk");
        }
    }

    public static void downloadPaymentSdkApk(Config testConfig, final String jobId)
    {
        String urlToDownload = "https://source.abc.io/api/v4/projects/6415/jobs/" + jobId + "/artifacts/app/build/outputs/apk/staging/itms/app-staging-itms.apk";
        File outputFile = new File("TestData" + File.separator + "MobileSdk" + File.separator + "MobileApps" + File.separator + "PaymentSdk.apk");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("PRIVATE-TOKEN", "7CPkDBJ2bkp-ooS86Xzd");
        downloadArtifactFile(testConfig, headers, urlToDownload, outputFile);
    }

    private enum OS
    {
        android,
        ios
    }
}