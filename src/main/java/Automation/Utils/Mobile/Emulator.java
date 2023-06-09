package Automation.Utils.Mobile;

import Automation.Utils.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.TimeUnit;

public class Emulator
{

    public static void startEmulator(Config testConfig)
    {
        String sdkPath = null;
        if (Config.osName.startsWith("Window"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Android" + File.separator + "Sdk" + File.separator;
        } else if (Config.osName.startsWith("Mac"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Android" + File.separator + "sdk" + File.separator;
        }

        String emulatorPath = sdkPath + "tools" + File.separator + "emulator";
        testConfig.logComment("Launching emulator device '" + testConfig.getRunTimeProperty("EmulatorName") + "'...");
        String[] aCommand = new String[]{emulatorPath, "-avd", testConfig.getRunTimeProperty("EmulatorName"), "-wipe-data", "-noaudio"};
        try
        {
            Process process = new ProcessBuilder(aCommand).start();
            process.waitFor(60, TimeUnit.SECONDS);
            testConfig.logComment("Emulator launched successfully");
        } catch (Exception e)
        {
            testConfig.logComment("Emulator could not be launched");
            e.printStackTrace();
        }
    }

    public static void closeEmulator(Config testConfig)
    {
        String sdkPath = null;
        if (Config.osName.startsWith("Window"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Android" + File.separator + "Sdk" + File.separator;
        } else if (Config.osName.startsWith("Mac"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Android" + File.separator + "sdk" + File.separator;
        }
        String adbPath = sdkPath + "platform-tools" + File.separator + "adb";
        String[] aCommand = new String[]{adbPath, "emu", "kill"};
        try
        {
            Process process = new ProcessBuilder(aCommand).start();
            process.waitFor(5, TimeUnit.SECONDS);
            testConfig.logComment("Emulator closed successfully");
        } catch (Exception e)
        {
            testConfig.logComment("Emulator still running...");
            e.printStackTrace();
        }
    }

    public static void installApp(Config testConfig, String appPath)
    {
        String sdkPath = null;
        if (Config.osName.startsWith("Window"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Android" + File.separator + "Sdk" + File.separator;
        } else if (Config.osName.startsWith("Mac"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Android" + File.separator + "sdk" + File.separator;
        }
        String adbPath = sdkPath + "platform-tools" + File.separator + "adb";
        testConfig.logComment("installing the required app ...");
        String[] aCommand = new String[]{adbPath, "install", appPath};
        try
        {
            Process process = new ProcessBuilder(aCommand).start();
            // process.waitFor();
            process.waitFor(20, TimeUnit.SECONDS);
            testConfig.logComment("App installed successfully");
        } catch (Exception e)
        {
            testConfig.logComment("App could not be installed ...");
            e.printStackTrace();
        }
    }

    public static void enableInternet(Config testConfig, Boolean enableInternet)
    {
        String state = "";
        if (enableInternet)
        {
            state = "enable";
        } else
        {
            state = "disable";
        }

        String sdkPath = null;
        if (Config.osName.startsWith("Window"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Android" + File.separator + "Sdk" + File.separator;
        } else if (Config.osName.startsWith("Mac"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Android" + File.separator + "sdk" + File.separator;
        }
        String adbPath = sdkPath + "platform-tools" + File.separator + "adb";
        String[] aCommand = new String[]{adbPath, "shell", "svc", "data", state};
        try
        {
            Process process = new ProcessBuilder(aCommand).start();
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (Exception e)
        {
            testConfig.logWarning("Status of Internet not updated...");
            e.printStackTrace();
        }
    }

    // Deletes all data associated with a package.
    public static void deletePackageData(Config testConfig)
    {
        String sdkPath = null;
        if (Config.osName.startsWith("Window"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Android" + File.separator + "Sdk" + File.separator;
        } else if (Config.osName.startsWith("Mac"))
        {
            sdkPath = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Android" + File.separator + "sdk" + File.separator;
        }
        String adbPath = sdkPath + "platform-tools" + File.separator + "adb";
        String[] aCommand = new String[]{adbPath, "shell", "pm", "clear", testConfig.getRunTimeProperty("appPackageName")};
        try
        {
            Process process = new ProcessBuilder(aCommand).start();
            process.waitFor(5, TimeUnit.SECONDS);
            testConfig.logComment("Internet closed successfully");
        } catch (Exception e)
        {
            testConfig.logComment("Internet still running...");
            e.printStackTrace();
        }
    }

    public static void downloadFileFromURL(Config testConfig, String urlString, File destination)
    {
        try
        {
            testConfig.logComment("downloading the latest apk file from the URL.");
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            testConfig.logComment("File downloaded successfully.");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}