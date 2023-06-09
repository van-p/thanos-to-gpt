package Automation.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CmdHelper
{

    /**
     * This function is used to execute the shell or batch commands and wait till the time full output is received, it supports both mac and windows
     *
     * @param testConfig - object of Config
     * @param cmd        - command to be executed
     * @return - returns all the logs for above cmd
     */
    public static String executeCommandAndWaitForOutput(Config testConfig, String cmd)
    {
        String fullLogs = "";
        Log.logCommentWithOptionalConfig(testConfig, "Executing shell command:- " + cmd);
        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (Config.osName.contains("Windows"))
            {
                processBuilder.command("cmd.exe", "/c", cmd);
            } else
            {
                processBuilder.command("bash", "-c", cmd);
            }

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            Log.logCommentWithOptionalConfig(testConfig, "======================================================");
            while ((line = reader.readLine()) != null)
            {
                fullLogs = fullLogs.concat("\n" + line);
                Log.logCommentWithOptionalConfig(testConfig, line);
            }
            Log.logCommentWithOptionalConfig(testConfig, "======================================================");
            int exitCode = process.waitFor();
            if (exitCode != 0)
            {
                Log.logCommentWithOptionalConfig(testConfig, "Execution ended for above command with error code : " + exitCode);
            }
        } catch (Exception e)
        {
            Log.logCommentWithOptionalConfig(testConfig, "Unable to execute shell command :- " + cmd);
            e.printStackTrace();
        }
        return fullLogs;
    }

    /**
     * This function is used to execute the shell or batch commands but does not returns the output, it supports both mac and windows
     *
     * @param testConfig - object of Config
     * @param cmd        - command to be executed
     */
    public static void executeCommandAndExit(Config testConfig, String cmd)
    {
        Log.logCommentWithOptionalConfig(testConfig, "Executing shell command:- " + cmd);
        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (Config.osName.contains("Windows"))
            {
                processBuilder.command("cmd.exe", "/c", cmd);
            } else
            {
                processBuilder.command("bash", "-c", cmd);
            }

            Process process = processBuilder.start();
            boolean exitCode = process.waitFor(10, TimeUnit.SECONDS);
            if (exitCode)
            {
                Log.logCommentWithOptionalConfig(testConfig, "Execution successful for command:- " + cmd);
            } else
            {
                Log.logCommentWithOptionalConfig(testConfig, "Execution ended for above command with exit code : " + exitCode);
            }
        } catch (Exception e)
        {
            Log.logCommentWithOptionalConfig(testConfig, "Unable to execute shell command :- " + cmd);
            e.printStackTrace();
        }
    }
}