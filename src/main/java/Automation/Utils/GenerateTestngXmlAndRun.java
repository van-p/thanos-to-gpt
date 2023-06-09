package Automation.Utils;

import Automation.Utils.EmailHelper.EmailContentType;
import Automation.Utils.Enums.DatabaseName;
import Automation.Utils.Enums.ProjectName;
import Automation.Utils.Enums.QA;
import Automation.Utils.Enums.QueryType;
import Automation.Utils.TestBase.TestrailData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.TestNGException;
import org.testng.xml.*;
import org.testng.xml.XmlSuite.ParallelMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to Generate TestNG.xml on runtime and execute it, and after execution it send automation report to passed email ids
 *
 * @author MukeshR
 */

public class GenerateTestngXmlAndRun
{

    public static boolean isDebugMode = false;
    public static String slackThreadIdToReply = "";
    public static boolean remoteExecution = true;
    public static TreeMap<String, ArrayList<String>> testClassAndMethodDetails = null;
    @SuppressWarnings("serial")
    public static HashMap<String, String> machines = new HashMap<String, String>()
    {
        {
            put("54.251.79.229", "10.100.11.30");
        }
    };
    static Date startDate = new Date();
    private static boolean isWebServerPresent = false;
    private static TreeMap<String, ArrayList<String>> groupNameAndClassesDetails = null;
    private static TreeMap<String, String> groupNameAndParallelizationDetails = null;
    private static String rerunFailures = null;
    private static final String resultsMachineIp = "10.100.11.30";
    private static final String entityName = "Regression";
    private static String locale = "ID";
    private static String appLanguage = "en";
    private static boolean mobileViewExecution = false;
    private static int variableCount = 1;
    private static String devProjectName = "";
    private static String devPipelineUrl = "";
    private static String devBranchName = "";

    static
    {
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
    }

    public static void main(String... args)
    {
        try
        {
            System.out.println("Total params passed = " + args.length);
            String projectName = checkIfEmpty("projectName", args[0], null);
            String environment = checkIfEmpty("environment", args[1], null);
            String browserName = checkIfEmpty("browserName", args[2], null);
            String sendEmailTo = checkIfEmpty("sendEmailTo", args[3], null);
            String jobBuildTag = checkIfEmpty("jobBuildTag", args[4], null);
            String groupNames = checkIfEmpty("groupNames", args[5], null);
            String sendSlackMessage = checkIfEmpty("sendSlackMessage", args[6], "false");
            String mobileView = checkIfEmpty("isMobileView", args[7], "false");
            String branchName = checkIfEmpty("branchName", args[8], "master");
            String debugMode = checkIfEmpty("debugMode", args[9], "false");
            rerunFailures = checkIfEmpty("rerunFailures", args[10], "false");
            locale = checkIfEmpty("locale", args[11], "SG");
            appLanguage = checkIfEmpty("appLanguage", args[12], "EN");

            if (jobBuildTag.contains("+"))
            {
                devProjectName = jobBuildTag.split("\\+")[0];
                devBranchName = jobBuildTag.split("\\+")[1];
                devPipelineUrl = jobBuildTag.split("\\+")[2];
                jobBuildTag = jobBuildTag.substring(jobBuildTag.indexOf("pipelines/") + 10);
            }

            // These are the extra variables being used
            ParallelMode parallelMode = null;
            String ownerName = "Mukesh:@U04F2AFMGJH";
            String slackChannelName = null;
            String threadCount = Arrays.asList(Enums.BrowserName.values()).contains(browserName.toLowerCase()) ? "15" : "20";
            String browserVersion = "108";
            String resultsDirectory = null;
            String mobileAppName = null;
            boolean isParallelExecution = true;
            boolean runCompleteClass = false;
            boolean mobileAppExecution = false;
            mobileViewExecution = Boolean.parseBoolean(mobileView);
            boolean sendReportOnSlack = Boolean.parseBoolean(sendSlackMessage);
            rerunFailures = rerunFailures.equalsIgnoreCase("false") ? null : rerunFailures;
            isDebugMode = Boolean.parseBoolean(debugMode);
            ProjectName project = ProjectName.valueOf(projectName);
            switch (project)
            {
                case Access:
                    parallelMode = ParallelMode.TESTS;
                    slackChannelName = "#qa-automation:C04J8KCLQTW";
                    ownerName = "Sanjeevan:@U03EP2NR8ET";
                    break;
                case Payments:
                    parallelMode = ParallelMode.TESTS;
                    slackChannelName = "#qa-automation:C04J8KCLQTW";
                    ownerName = "Himanshu:@U04Q8GJ5TUP";
                    break;
                case SaaS:
                    parallelMode = ParallelMode.TESTS;
                    slackChannelName = "#qa-automation:C04J8KCLQTW";
                    ownerName = "Alex:@U01HXBTS4US;Erit:@U02M39AGLG2";
                    break;
                case CustomerFrontend:
                case Dash:
                    parallelMode = ParallelMode.TESTS;
                    slackChannelName = "#qa-automation:C04J8KCLQTW";
                    ownerName = "qa.leads:S0403JQ2APM";
                    break;
                case App:
                    parallelMode = ParallelMode.METHODS;
                    slackChannelName = "#thanos:C04GNRPC4J3";
                    ownerName = "Mukesh:@U04F2AFMGJH";
                    break;
                default:
                    break;
            }

            // Deciding Shared Directory & Results Directory for Automation Results
            resultsDirectory = decideResultsDirectory(resultsMachineIp, projectName, jobBuildTag);

            // Code to get test methods and classes from TestExecutionList sheet
            testClassAndMethodDetails = getTestcasesToExecute(projectName);
            JSONArray testSuiteXml_AllTestcasesToRun = genTestSuiteXmlObject(new JSONObject(groupNameAndClassesDetails), new JSONObject(testClassAndMethodDetails));

            // Create TestNG.xml on runtime and execute it
            generateAndRunTestNGXml(testSuiteXml_AllTestcasesToRun, groupNameAndParallelizationDetails, projectName, environment, browserName, browserVersion, isParallelExecution, parallelMode, threadCount, resultsDirectory, runCompleteClass, groupNames, mobileViewExecution, mobileAppExecution, mobileAppName, branchName, jobBuildTag);

            // After execution of testcases, compose email and send
            triggerNotifications(resultsDirectory, projectName, environment, sendReportOnSlack, slackChannelName, sendEmailTo, browserName, ownerName, groupNames, branchName);

        } catch (Exception e)
        {
            e.printStackTrace();
            commentAndStopExecution("Exception occurred in 'GenerateTestNGXmlAndRun.main'");
        }
        // Sometimes this code creates problem in exiting, so exiting forcefully
        System.exit(0);
    }

    private static void generateAndRunTestNGXml(JSONArray testSuiteXml_AllTestcasesToRun, TreeMap<String, String> groupNameAndInterferingDetails, String projectName, String environment, String browserName, String browserVersion, boolean isParallelExecution, ParallelMode parallelMode, String threadCount, String resultsDirectory, boolean runCompleteClass, String groupNames, Boolean mobileViewExecution, Boolean mobileAppExecution, String mobileAppName, String branchName, String jobBuildTag)
    {
        try
        {
            System.out.println("<----------------- CREATING TESTNG.XML File -------------------->");
            String currentFile = resultsDirectory + File.separator + "RunTime_TestNG.xml";
            File file = new File(currentFile);
            FileWriter writer = new FileWriter(CommonUtilities.normalizePath(currentFile));
            logCommentForDebugging("RunTime_TestNG.xml : " + file);
            // Create an instance on TestNG and XML Suite
            TestNG myTestNG = new TestNG();
            XmlSuite mySuite = new XmlSuite();
            SuiteXmlParser parser = new SuiteXmlParser();
            String failedXml;
            FileInputStream fileInputStream;
            if (rerunFailures != null)
            {
                String jobNum = jobBuildTag.split("-")[jobBuildTag.split("-").length - 1];
                rerunFailures = rerunFailures.equalsIgnoreCase("last") || rerunFailures.equalsIgnoreCase("true") ? String.valueOf(Integer.parseInt(jobNum) - 1) : rerunFailures;
                failedXml = StringUtils.replace(resultsDirectory, jobNum, rerunFailures) + File.separator + "testng-failed.xml";
                try
                {
                    fileInputStream = new FileInputStream(CommonUtilities.normalizePath(failedXml));
                    mySuite = parser.parse(currentFile, fileInputStream, true);
                    List<XmlTest> tests = mySuite.getTests();
                    List<XmlTest> newTests = new ArrayList<>();
                    for (XmlTest test : tests)
                    {
                        String testName = test.getName();
                        XmlTest newTest = test;
                        newTest.setName(testName.replaceAll("\\(failed\\)", ""));
                        newTests.add(newTest);
                    }
                    mySuite.setTests(newTests);
                } catch (FileNotFoundException fnfe)
                {
                    commentAndStopExecution("Exception occurred while reading previous job's testng-failed.xml. File not found.");
                }
                mySuite.setName("Rerun " + groupNames + " cases on " + branchName + " branch");
            } else
            {
                mySuite.setName(groupNames + " cases on " + branchName + " branch");
            }
            // Check if suite is to be run in parallel
            if (isParallelExecution)
            {
                int threadCnt = Integer.parseInt(threadCount);
                mySuite.setParallel(parallelMode);
                mySuite.setThreadCount(threadCnt);
            }
            // Set parameters in TestNG Xml and Overriding various value of Config.properties
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("projectName", projectName);
            parameters.put("environment", environment);
            parameters.put("browserName", browserName);
            parameters.put("browserVersion", browserVersion);
            parameters.put("resultsDirectory", resultsDirectory);
            parameters.put("mobileViewExecution", String.valueOf(mobileViewExecution));
            parameters.put("mobileAppExecution", String.valueOf(mobileAppExecution));
            parameters.put("remoteExecution", String.valueOf(remoteExecution));
            parameters.put("debugMode", String.valueOf(isDebugMode));

            if (!StringUtils.isEmpty(mobileAppName) && mobileAppExecution)
            {
                parameters.put("mobileAppName", mobileAppName);
            }
            parameters.put("locale", locale);
            parameters.put("appLanguage", appLanguage);
            mySuite.setParameters(parameters);

            // Set listeners
            String updatedEnvironmentText = environment;//locale.equalsIgnoreCase("ID") ? environment : environment + "(" + locale.toUpperCase() + ")";
            List<Class<? extends ITestNGListener>> listnerClasses = new ArrayList<Class<? extends ITestNGListener>>();
            listnerClasses.add(org.testng.reporters.FailedReporter.class);
            listnerClasses.add(org.uncommons.reportng.HTMLReporter.class);
            listnerClasses.add(org.uncommons.reportng.JUnitXMLReporter.class);
            myTestNG.setListenerClasses(listnerClasses);
            myTestNG.setUseDefaultListeners(false);

            // Creating the Result Directory where we can save automation results + screenshots + other data
            myTestNG.setOutputDirectory(resultsDirectory);

            // Set ReportNG Properties
            System.setProperty("org.uncommons.reportng.title", projectName + " Test Report - " + updatedEnvironmentText);
            System.setProperty("org.uncommons.reportng.escape-output", "false");
            if (StringUtils.isEmpty(rerunFailures) || StringUtils.equalsIgnoreCase(rerunFailures, "false"))
            {
                // Get all the class names to be run
                for (int i = 0; i < testSuiteXml_AllTestcasesToRun.length(); i++)
                {
                    Iterator<?> groupItr = testSuiteXml_AllTestcasesToRun.getJSONObject(i).keys();
                    JSONObject groupObj = testSuiteXml_AllTestcasesToRun.getJSONObject(i);
                    List<XmlClass> myClasses = null;
                    XmlClass testClass = null;
                    XmlTest myTest = null;
                    while (groupItr.hasNext())
                    {
                        String groupKey = groupItr.next().toString();
                        logCommentForDebugging("Checking group key - " + groupKey + " at index " + i);
                        JSONArray classesObj = new JSONArray(groupObj.get(groupKey).toString());
                        String canRunInParallel = groupNameAndInterferingDetails.get(groupKey);
                        // Create an instance of XmlTest and assign a name for it.
                        myTest = new XmlTest();
                        myTest.setName(groupKey);
                        myTest.setVerbose(0);
                        if (!groupNames.equalsIgnoreCase("NA"))
                        {
                            String[] groups = groupNames.trim().split(",");
                            for (String groupName : groups)
                            {
                                if (groupName.contains("~"))
                                {
                                    myTest.addExcludedGroup(groupName.trim().replace("~", ""));
                                } else
                                {
                                    myTest.addIncludedGroup(groupName.trim());
                                }
                            }
                        }
                        if (canRunInParallel != null && canRunInParallel.equalsIgnoreCase("false"))
                        {
                            myTest.setThreadCount(1);
                        }

                        // Create a list which can contain the classes that you want to run.
                        myClasses = new ArrayList<XmlClass>();
                        for (int k = 0; k < classesObj.length(); k++)
                        {
                            // get a class json object
                            JSONObject classObj = classesObj.getJSONObject(k);
                            ArrayList<String> keys = new ArrayList<String>();
                            Iterator<?> classItr = classObj.keys();
                            // get the classnames
                            while (classItr.hasNext())
                            {
                                String className = classItr.next().toString();
                                logCommentForDebugging("Adding class - " + className);
                                keys.add(className);
                            }
                            for (int l = 0; l < keys.size(); l++)
                            {
                                String classKey = keys.get(l);
                                logCommentForDebugging("Creating Test node for class - " + classKey);
                                try
                                {
                                    // Create an instance of XmlClass and assign a name for it.
                                    testClass = new XmlClass(classKey);
                                } catch (TestNGException e)
                                {
                                    logCommentForDebugging("Exception is: " + ExceptionUtils.getStackTrace(e));
                                }
                                // For TestPlan Run adding particular methods(testcases) in the XML(Here not running full class)
                                if (runCompleteClass)
                                {
                                    ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
                                    String[] methods = null;
                                    String m = classObj.getString(classKey);
                                    if (m.substring(1, m.length() - 1).contains(","))
                                    {
                                        methods = m.substring(1, m.length() - 1).split(", ");
                                        logCommentForDebugging("Including method - ");
                                        for (int j = 0; j < methods.length; j++)
                                        {
                                            logCommentForDebugging(methods[j] + "\t");
                                            methodsToRun.add(new XmlInclude(methods[j]));
                                        }
                                        if (testClass != null)
                                        {
                                            testClass.setIncludedMethods(methodsToRun);
                                        }

                                        logCommentForDebugging("");
                                    } else
                                    {
                                        logCommentForDebugging("Including method - " + m.substring(1, m.length() - 1));
                                        methodsToRun.add(new XmlInclude(m.substring(1, m.length() - 1)));
                                        if (testClass != null)
                                        {
                                            testClass.setIncludedMethods(methodsToRun);
                                        }
                                    }
                                }
                                if (testClass != null)
                                {
                                    myClasses.add(testClass);
                                }
                            }
                        }
                    }
                    // Assign that to the XmlTest Object created earlier.
                    myTest.setXmlClasses(myClasses);
                    myTest.setSuite(mySuite);
                    mySuite.addTest(myTest);
                }
            }
            // Add the suite to the list And then Set the list of Suites to the testNG object we created earlier
            List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
            mySuites.add(mySuite);
            myTestNG.setXmlSuites(mySuites);

            try
            {
                writer.write(mySuite.toXml());
                System.out.println("TestNG.xml for Execution : " + CommonUtilities.convertFilePathToHtmlUrl(file.getAbsolutePath()));
                logCommentForDebugging("Created XML - \n" + mySuite.toXml());
            } catch (Exception e)
            {
                logCommentForDebugging("Exception occured while printing TestNG.xml file...");
                e.printStackTrace();
            }
            writer.close();
            System.out.println("<----------------- EXECUTING TESTNG.XML File -------------------->");
            myTestNG.run();
        } catch (Exception e)
        {
            e.printStackTrace();
            commentAndStopExecution("Exception occurred in 'GenerateTestNGXmlAndRun.generateAndRunTestNGXml'");
        }
    }

    private static void triggerNotifications(String resultsDirectory, String projectName, String environment, boolean sendReportOnSlack, String slackChannelName, String sendEmailTo, String browserName, String ownerName, String groupNames, String branchName)
    {
        String subject = null;
        String passPercentage = null;
        String slackMessageHeading = null;
        String htmlFolderPath = resultsDirectory + File.separator + "html";
        String resultLink = CommonUtilities.convertFilePathToHtmlUrl(htmlFolderPath) + "/index.html";
        try
        {
            // Modifying Contents of Report
            String reportContents = removeUnexecutedClassesAndFormatReport(isWebServerPresent, htmlFolderPath);

            // Composing Subject line for Slack and Email notification
            Matcher matcher = Pattern.compile("class=\"passRate suite\">(\\d+)").matcher(reportContents);
            if (matcher.find())
            {
                passPercentage = matcher.group(1);
            }

            if (Double.parseDouble(passPercentage) <= 95)
            {
                String updatedTagging = "";
                String[] ownerNames = ownerName.split(";");
                for (int i = 0; i < ownerNames.length; i++)
                {
                    String userId = ownerNames[i].split(":")[1];
                    userId = userId.startsWith("@") ? "<" + userId + ">" : "<!subteam^" + userId + ">";
                    if (i > 0)
                        updatedTagging = updatedTagging + " / " + userId;
                    else
                        updatedTagging = userId;
                }
                SlackHelper.slackThreads.add("Yes, as *Thanos* I might be powerful, but even I can't fix my own code! " + updatedTagging + " *let's fix it...* before I snap my fingers again...  :thanos_snap:");
            }

            slackMessageHeading = "*:golf: QA AUTOMATION REPORT :golf:*\n\n*";
            subject = passPercentage + "% : " + entityName + "-" + projectName + " on " + environment + " environment";
            if (!locale.equalsIgnoreCase("ID") || !appLanguage.equalsIgnoreCase("en"))
            {
                //subject = subject + " [" + getFullLocaleName(locale) + "]" + " [" + getFullAppLanguageName(appLanguage) + "]";
            }
            if (browserName.toLowerCase().contains("api"))
            {
                subject = subject + " [Api Cases]";
            } else if (browserName.toLowerCase().contains("mobile"))
            {
                subject = subject + " [Mobile App]";
            } else
            {
                if (mobileViewExecution)
                {
                    subject = subject + " [" + browserName + "-MobileView]";
                } else
                {
                    subject = subject + " [" + browserName + "]";
                }
            }
            if (rerunFailures != null)
            {
                subject = "Rerun - " + subject;
            }
            // Print report in console along with URL
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println(subject);
            ArrayList<ArrayList<String>> data = printReportOnConsole(reportContents);
            System.out.println("Full Report:- " + resultLink);
            System.out.println("=================================================================================");
            if (!StringUtils.isEmpty(devPipelineUrl))
            {
                System.out.println("Developer's Pipeline Url:- " + devPipelineUrl);
            }

            // Code to compose and send report on Email & Slack
            String finalSlackMessage = "";
            if (sendReportOnSlack)
            {
                if (!passPercentage.equalsIgnoreCase("100"))
                {
                    slackMessageHeading = slackMessageHeading.replaceAll("golf", "fire");
                }

                finalSlackMessage = slackMessageHeading + subject + "*\nTotal Test(s) Passed: " + data.get(data.size() - 1).get(1) + "\nTotal Test(s) Failed: " + data.get(data.size() - 1).get(2) + "\n*Full report* : " + resultLink;

                if (!StringUtils.isEmpty(devProjectName))
                {
                    String pipelineId = devPipelineUrl.substring(devPipelineUrl.indexOf("pipelines/") + 10);
                    finalSlackMessage = finalSlackMessage + "\n(Triggered by <" + devPipelineUrl + "|" + devProjectName + " Pipeline #" + pipelineId + "> for `" + devBranchName + "` branch)";
                }
            }

            if (!StringUtils.isEmpty(devProjectName))
            {
                String pipelineId = devPipelineUrl.substring(devPipelineUrl.indexOf("pipelines/") + 10);
                reportContents = reportContents + "<br>(Triggered by <a href='" + devPipelineUrl + "' target='_blank' >" + devProjectName + " Pipeline #" + pipelineId + "</a> for '" + devBranchName + "' branch)";
            }

            if (sendReportOnSlack)
            {
                if (StringUtils.isEmpty(slackThreadIdToReply))
                {
                    reportContents = updateHtmlString(reportContents, getUpdatableDom(slackChannelName.split(":")[1], SlackHelper.triggerSlackNotifications(slackChannelName.split(":")[0], finalSlackMessage, null, slackChannelName.split(":")[1])));
                } else
                {
                    SlackHelper.slackThreads.add(finalSlackMessage);
                    Collections.reverse(SlackHelper.slackThreads);
                    SlackHelper.multipleReplyOnThread(slackChannelName.split(":")[0], slackThreadIdToReply, SlackHelper.slackThreads);
                    reportContents = updateHtmlString(reportContents, getUpdatableDom(slackChannelName.split(":")[1], slackThreadIdToReply));
                }

            }
            EmailHelper.sendEmail(sendEmailTo, subject, reportContents, EmailContentType.Html, null);

            // Code to add the results summary in the database
            String projectsToBeExecuded = "App";
            if (remoteExecution && !projectsToBeExecuded.contains(projectName) && (branchName.equalsIgnoreCase("main")))
            {
                long seconds = (new Date().getTime() - startDate.getTime()) / 1000;
                String duration = String.valueOf(seconds);
                String passedCases = "0";
                String failedCases = "0";
                Document document = Jsoup.parse(reportContents);
                for (org.jsoup.nodes.Element element : document.select("tr.suite>td.passed.number"))
                {
                    passedCases = element.text();
                }
                for (org.jsoup.nodes.Element element : document.select("tr.suite>td.failed.number"))
                {
                    failedCases = element.text();
                }

                addResultsToThanosDatabase(projectName, environment, groupNames, passPercentage, duration, passedCases, failedCases, resultLink);
            }

            if (!passPercentage.equalsIgnoreCase("100"))
            {
                System.exit(1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            commentAndStopExecution("Exception occurred in 'sendEmailAfterExecution'");
        }
    }

    private static String getFullLocaleName(String locale)
    {
        String fullLocaleName = "Unknown";
        switch (locale.toUpperCase())
        {
            case "ID":
                fullLocaleName = "Indonesia";
                break;
            case "SG":
                fullLocaleName = "Singapore";
                break;
            case "TH":
                fullLocaleName = "Thailand";
                break;
            case "VN":
                fullLocaleName = "Vietnam";
                break;
        }
        return fullLocaleName;
    }

    private static String getFullAppLanguageName(String appLanguage)
    {
        String fullAppLanguageName = "Unknown";
        switch (appLanguage.toUpperCase())
        {
            case "ID":
                fullAppLanguageName = "Bahasa";
                break;
            case "EN":
                fullAppLanguageName = "English";
                break;
            case "VI":
                fullAppLanguageName = "Vietnamese";
                break;
        }
        return fullAppLanguageName;
    }

    private static ArrayList<ArrayList<String>> printReportOnConsole(String strText)
    {
        Document doc = Jsoup.parse(strText);
        Elements rows = doc.body().getElementsByTag("tr");
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        int rowCount = 0;
        int max = -1;
        for (org.jsoup.nodes.Element row : rows)
        {
            Elements colHead = row.getElementsByTag("th");
            ArrayList<String> rowData = new ArrayList<>();
            int j = 0;
            for (org.jsoup.nodes.Element col : colHead)
            {
                rowData.add(j, col.text());
                if (!StringUtils.isEmpty(rowData.get(j)) && max < rowData.get(j).length())
                {
                    max = rowData.get(j).length();
                }
                j++;
            }
            j = 0;
            Elements cols = row.getElementsByTag("td");
            for (org.jsoup.nodes.Element col : cols)
            {
                rowData.add(j, col.text());
                if (!StringUtils.isEmpty(rowData.get(j)) && max < rowData.get(j).length())
                {
                    max = rowData.get(j).length();
                }
                j++;
            }
            if (!rowData.get(0).equals("Total"))
            {
                if (rowData.size() > 1)
                {
                    rowData.remove(1);
                }
                if (rowData.size() > 2)
                {
                    rowData.remove(2);
                }
            } else
            {
                rowData.remove(2);
            }
            data.add(rowCount, rowData);
            rowCount++;
        }
        int newMax = max;
        for (int i = 0; i < (max + 42); i++)
        {
            System.out.print("=");
        }
        System.out.println();
        for (int i = 1; i < rowCount; i++)
        {
            System.out.print("| ");
            max = newMax;
            for (int k = 0; k < 4; k++)
            {
                String dataToPrint = "";
                int len = 0;
                try
                {
                    dataToPrint = data.get(i).get(k);
                    System.out.print(dataToPrint);
                    len = dataToPrint.length();
                } catch (Exception e)
                {
                    System.out.print("");
                }
                for (int j = 0; j < max - len; j++)
                {
                    System.out.print(" ");
                }
                System.out.print(" | ");
                max = 10;
            }
            System.out.println();
        }
        for (int i = 0; i < (newMax + 42); i++)
        {
            System.out.print("=");
        }
        System.out.println();
        return data;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Iterator sortedIterator(Iterator it, Comparator comparator)
    {
        List list = new ArrayList();
        while (it.hasNext())
        {
            list.add(it.next());
        }
        Collections.sort(list, comparator);
        return list.iterator();
    }

    private static JSONArray genTestSuiteXmlObject(JSONObject groupNameAndClassesDetails_Json, JSONObject testClassAndMethodDetails_json)
    {
        String groupName = null;
        JSONArray testSuiteXmlObj = new JSONArray();
        try
        {
            Iterator<?> groupNames = sortedIterator(groupNameAndClassesDetails_Json.keys(), Comparator.naturalOrder());
            Iterator<?> testClasses = sortedIterator(testClassAndMethodDetails_json.keys(), Comparator.naturalOrder());
            JSONObject testClassMethodForGroup = null;
            JSONObject groupNameClassesAndMethods_json = new JSONObject();
            while (testClasses.hasNext())
            {
                testClassMethodForGroup = new JSONObject();
                String className = (String) testClasses.next();
                groupNames = groupNameAndClassesDetails_Json.keys();
                logCommentForDebugging("Checking group name for class - " + className);
                if (groupNames.hasNext())
                { // Code for RunByArea execution
                    while (groupNames.hasNext())
                    {
                        groupName = (String) groupNames.next();
                        String classNamesInThatGroup = groupNameAndClassesDetails_Json.get(groupName).toString();
                        if (!classNamesInThatGroup.isEmpty() && !className.isEmpty())
                        {
                            if (classNamesInThatGroup.contains(className))
                            {
                                if (className != null)
                                {
                                    String methodNames = testClassAndMethodDetails_json.get(className).toString();
                                    logCommentForDebugging("Adding in group - " + groupName + " Class - " + className + " And method - " + methodNames);
                                    testClassMethodForGroup.put(className, methodNames);
                                    className = null;
                                }
                                break;
                            }
                        }
                    }
                } else
                { // Code for RunByTestPlan execution
                    String methodNames = testClassAndMethodDetails_json.get(className).toString();
                    logCommentForDebugging("Adding in group - " + groupName + " Class - " + className + " And method - " + methodNames);
                    testClassMethodForGroup.put(className, methodNames);
                    groupName = className.substring(className.lastIndexOf(".") + 1);
                }
                if (groupName != null)
                {
                    // consolidate all classes(along with their methods) that belong to groupName
                    groupNameClassesAndMethods_json.append(groupName, testClassMethodForGroup);
                    groupName = null;
                }
            }
            groupNames = sortedIterator(groupNameClassesAndMethods_json.keys(), Comparator.naturalOrder());
            while (groupNames.hasNext())
            {
                groupName = groupNames.next().toString();
                JSONArray testClassesAndMethods_json = groupNameClassesAndMethods_json.getJSONArray(groupName);
                logCommentForDebugging("Checking group - " + groupName + " testClassesAndMethods " + testClassesAndMethods_json.toString());
                testClasses = null;
                for (int i = 0; i < testClassesAndMethods_json.length(); i++)
                {
                    JSONObject objects = testClassesAndMethods_json.getJSONObject(i);
                    testClasses = objects.keys();
                    while (testClasses.hasNext())
                    {
                        String className = testClasses.next().toString();
                        String methodNames = objects.getString(className);
                        boolean isPresent = false;
                        int index = testSuiteXmlObj.length();
                        for (i = 0; i < index; i++)
                        {
                            if (testSuiteXmlObj.get(i).toString().contains(className))
                            {
                                isPresent = true;
                            }
                        }
                        if (!isPresent)
                        {
                            if (!className.isEmpty() && !methodNames.isEmpty())
                            {
                                JSONObject testSuiteEntryObj = new JSONObject();
                                // excluding empty class array,i.e. groupName is there in excel but the corresponding class is not included in test plan
                                if (!testClassesAndMethods_json.toString().contentEquals("{}"))
                                {
                                    logCommentForDebugging("Adding at index " + index + " in group - " + groupName + " Class - " + className + " And method - " + methodNames);
                                    testSuiteEntryObj.put(groupName, testClassesAndMethods_json);
                                    testSuiteXmlObj.put(index, testSuiteEntryObj);
                                    index++;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return testSuiteXmlObj;
    }

    private static TreeMap<String, ArrayList<String>> getTestcasesToExecute(String projectName)
    {
        // Fetch the "groupNameAndClassesDetails" & "groupNameAndInterferingDetails" after reading GroupNames Sheet
        String groupNamesSheetPath = System.getProperty("user.dir") + File.separator + "Parameters" + File.separator + "TestExecutionList.csv";
        List<String[]> excelData = TestDataReader.getCompleteCsvData(groupNamesSheetPath);
        getGroupNameClassNameAndParallelizationDetails(excelData, projectName);
        TreeMap<String, ArrayList<String>> testClassAndMethodDetails = new TreeMap<String, ArrayList<String>>();
        // Stores the class name and the corresponding method list
        try
        {
            if (StringUtils.isEmpty(rerunFailures))
            {
                for (int i = 1; i < excelData.size(); i++)
                {
                    // Read the class name in current row
                    String currentClass = excelData.get(i)[1];
                    if (!isClassNamePresent(groupNameAndClassesDetails, currentClass))
                    {
                        continue;
                    }
                    ArrayList<String> methods;
                    Class<?> c = Class.forName(currentClass);
                    Method[] method = c.getDeclaredMethods();
                    // Check if current class is already added
                    if (testClassAndMethodDetails.containsKey(currentClass))
                    {
                        methods = testClassAndMethodDetails.get(currentClass);
                        for (Method m : method)
                        {
                            methods.add(m.getName());
                        }
                        logCommentForDebugging("Adding class - " + currentClass + " and methods - " + methods.toString());
                        testClassAndMethodDetails.put(currentClass, methods);
                    } else
                    {
                        logCommentForDebugging("Identifying methods to be run for class - " + currentClass);
                        // This will hold all methods belonging to current class
                        methods = new ArrayList<String>();
                        for (Method a : method)
                        {
                            methods.add(a.getName());
                        }

                        // add all these methods against the current class name
                        logCommentForDebugging("Adding class - " + currentClass + " and methods - " + methods);
                        testClassAndMethodDetails.put(currentClass, methods);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        logCommentForDebugging("Test classes and methods : " + testClassAndMethodDetails);
        return testClassAndMethodDetails;
    }

    private static boolean isClassNamePresent(TreeMap<String, ArrayList<String>> groupNameAndClassesDetails, String className)
    {
        for (Entry<String, ArrayList<String>> groupNameAndClassKeyValuePair : groupNameAndClassesDetails.entrySet())
        {
            ArrayList<String> currentClasses = groupNameAndClassKeyValuePair.getValue();
            if (currentClasses.contains(className))
            {
                return true;
            }
        }
        return false;
    }

    private static void getGroupNameClassNameAndParallelizationDetails(List<String[]> fileData, String projectName)
    {
        // Stores the group name and the corresponding classes list
        groupNameAndClassesDetails = new TreeMap<String, ArrayList<String>>();

        // Stores the group name and corresponding interfering value
        groupNameAndParallelizationDetails = new TreeMap<String, String>();
        try
        {
            int lastRow = fileData.size() - 1;
            String canRunInParallel = null;
            for (int i = 1; i <= lastRow; i++)
            {
                String currentGroupName = "";
                if (i == lastRow)
                {
                    lastRow = fileData.size() - 1;
                }
                String[] groupProjectNameArray = fileData.get(i)[3].toUpperCase().trim().replace(" ", "").split(",");
                if (Arrays.asList(groupProjectNameArray).contains(projectName.toUpperCase()))
                {
                    currentGroupName = fileData.get(i)[0].trim();
                } else
                {
                    continue;
                }
                ArrayList<String> classes;
                // Check if current group is already added
                if (!groupNameAndClassesDetails.containsKey(currentGroupName))
                {
                    classes = new ArrayList<String>();
                    canRunInParallel = fileData.get(i)[2];
                    classes.add(fileData.get(i)[1]);
                    groupNameAndClassesDetails.put(currentGroupName, classes);
                    groupNameAndParallelizationDetails.put(currentGroupName, canRunInParallel);
                } else
                {
                    classes = groupNameAndClassesDetails.get(currentGroupName);
                    classes.add(fileData.get(i)[1]);
                    groupNameAndClassesDetails.put(currentGroupName, classes);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String decideResultsDirectory(String resultsMachineIp, String projectName, String jobBuildTag)
    {
        String currentMachineIp = CommonUtilities.getPublicIP(null);

        // Correcting file separators for non windows
        if (!Config.osName.startsWith("Window"))
        {
            String userDirectory = System.getProperty("user.dir");
            userDirectory = userDirectory.replace("\\", File.separator);
            userDirectory = userDirectory.replace("/", File.separator);
            System.setProperty("user.dir", userDirectory);
        }

        String logs = "";
        String sharedDirectory = System.getProperty("user.dir") + File.separator + ".." + File.separator + "SharedResults";
        String localDirectory = System.getProperty("user.dir") + File.separator + ".." + File.separator + "LocalResults";
        if (Config.osName.startsWith("Linux"))
        {
            if (remoteExecution)
            {
                logs = CmdHelper.executeCommandAndWaitForOutput(null, "mkdir " + sharedDirectory + " & ls " + sharedDirectory);
                if (!logs.contains("web.config"))
                {
                    logCommentForDebugging("Mounting the shared results...");
                    logs = CmdHelper.executeCommandAndWaitForOutput(null, "sh " + System.getProperty("user.dir") + File.separator + "CI-Scripts" + File.separator + "mounting-script-linux.sh " + sharedDirectory);
                }
            }
        } else if (Config.osName.startsWith("Mac"))
        {
            if (remoteExecution)
            {
                logs = CmdHelper.executeCommandAndWaitForOutput(null, "mkdir " + sharedDirectory + " & ls " + sharedDirectory);
                if (!logs.contains("web.config"))
                {
                    logCommentForDebugging("Mounting the shared results...");
                    //logs = CmdHelper.executeCommandAndWaitForOutput(null, "sh " + System.getProperty("user.dir") + File.separator + "CI-Scripts" + File.separator + "mounting-script-mac.sh " + sharedDirectory);
                }
            }
        } else
        {
            if (remoteExecution)
            {
                if (machines.get(currentMachineIp) != null)
                {
                    sharedDirectory = File.separator + File.separator + resultsMachineIp + File.separator + "RegressionResults";
                }
            }
        }

        if (!remoteExecution)
        {
            sharedDirectory = localDirectory;
        }

        isWebServerPresent = machines.get(currentMachineIp) != null || logs.contains("web.config");

        if (!StringUtils.isEmpty(devPipelineUrl))
        {
            jobBuildTag = jobBuildTag + "-" + DataGenerator.generateRandomNumber(5);
        }

        String resultsDirectory = sharedDirectory + File.separator + projectName + File.separator + jobBuildTag.replace("jenkins-", "");

        boolean isCreated = CommonUtilities.createFolder(null, CommonUtilities.normalizePath(resultsDirectory));
        if (!isCreated)
        {
            Log.logCommentWithOptionalConfig(null, "Unable to access the Remote machine, so using local directory...");
            resultsDirectory = localDirectory + File.separator + projectName + File.separator + jobBuildTag.replace("jenkins-", "");
            isCreated = CommonUtilities.createFolder(null, CommonUtilities.normalizePath(resultsDirectory));
            if (!isCreated)
            {
                commentAndStopExecution("Exiting, as Results Directory is not created !");
            }
        }
        return resultsDirectory;
    }

    private static void commentAndStopExecution(String message)
    {
        message = "\033[31m =======>>" + message + "<<======= \033[0m";
        System.out.println("\n" + message + "\n");
        System.exit(1);
    }

    private static String removeUnexecutedClassesAndFormatReport(Boolean isWebServerPresent, String htmlFolderPath)
    {
        String htmlLink = CommonUtilities.convertFilePathToHtmlUrl(htmlFolderPath);
        String resultLink = htmlLink + "/index.html";
        String indexFile = htmlFolderPath + File.separator + "index.html";
        String suitesFile = htmlFolderPath + File.separator + "suites.html";
        String overviewFile = htmlFolderPath + File.separator + "overview.html";
        removeUnexecutedClasses(indexFile);
        String strText = removeUnexecutedClasses(overviewFile);
        strText = strText.replaceAll("output.html", resultLink);
        if (isWebServerPresent)
        {
            strText = strText.replaceAll("href=\"suite", "href=\"" + htmlLink + "/suite");
            strText = strText.replaceAll("Log Output", "Full Report");
        } else
        {
            strText = strText.replaceAll("href=\"suite", "href=\"#");
            strText = strText.replaceAll("target=\"_blank\"", "");
            strText = strText.replaceAll("Log Output", " ");
        }
        Pattern pattern = Pattern.compile("class=\"passRate suite\">(\\d+)");
        Matcher matcher = pattern.matcher(strText);
        if (matcher.find())
        {
            int passPercentage = Integer.parseInt(matcher.group(1));
            if (passPercentage < 100)
            {
                formatReport(suitesFile);
            }
        } else
        {
            commentAndStopExecution("Not able to read percentage from report, so cant compose & send email");
        }
        return strText;
    }

    private static void formatReport(String filePath)
    {
        String strFileData = null;
        File file = new File(CommonUtilities.normalizePath(filePath));
        // Code to change to structure of TestNG Report
        String predefinedFormat = "<!--?xml version=\"1.0\" encoding=\"utf-8\" ?--><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"\" lang=\"\">\n" + " <head> \n" + "  <title>Thanos Test Execution Report</title> \n" + "  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"> \n" + "  <meta name=\"description\" content=\"TestNG unit test results.\"> \n" + "  <link href=\"reportng.css\" rel=\"stylesheet\" type=\"text/css\"> \n" + "  <script type=\"text/javascript\" src=\"reportng.js\"></script>\n" + "  <style type=\"text/css\">\n" + "      .test{\n" + "        font-size:1em;\n" + "        font-family: sans-serif;\n" + "      }\n" + "  </style>\n" + " </head> \n" + " <body style=\"margin-top: 0;\"> \n" + "  <div id=\"sidebarHeader\"> \n" + "   <h2>Thanos Test Execution Report</h2> \n"
                + "   <p> <a href=\"overview.html\" target=\"main\">Overview</a> · <a href=\"output.html\" target=\"main\">Log Output</a> </p> \n" + "  </div> \n" + "  <table id=\"suites\"> \n" + "   <thead> \n" + "    <tr> \n" + "     <th class=\"header suite\" onclick=\"toggleElement('tests-1', 'table-row-group'); toggle('toggle-1')\" style=\"line-height:1.6em;white-space: nowrap;\"> <span id=\"toggle-1\" class=\"toggle\">&#x25bc;</span>List of testcases executed :</th> \n" + "    </tr> \n" + "   </thead> \n" + "   <tbody id=\"tests-1\" class=\"tests\">\n" + "    <tr>\n" + "        <td>\n" + "            <table id=\"suites\" style=\"padding-left:12px;\"> \n" + "               <thead> \n" + "                <tr> \n"
                + "                 <th class=\"suite\" onclick=\"toggleElement('tests-failures', 'table-row-group'); toggle('toggle-failures')\" style=\"text-align:left;line-height:1.5em;background-color: #ff8888;\"> <span id=\"toggle-failures\" class=\"toggle\">&#x25bc;</span>Failed Cases </th> \n" + "                </tr> \n" + "               </thead> \n" + "               <tbody id=\"tests-failures\" class=\"tests\">\n" + "                \n" + "               </tbody>\n" + "           </table>\n" + "        </td>\n" + "    </tr>\n" + "    <tr>\n" + "        <td>\n" + "            <table id=\"suites\" style=\"padding-left:12px;\"> \n" + "               <thead> \n" + "                <tr> \n" + "                 <th class=\"suite\" onclick=\"toggleElement('tests-passed', 'table-row-group'); toggle('toggle-passed')\" style=\"text-align:left;line-height:1.5em;background-color: #88ee88;\"> <span id=\"toggle-passed\" class=\"toggle\">&#x25b6;</span>Passed Cases </th> \n"
                + "                </tr> \n" + "               </thead> \n" + "               <tbody id=\"tests-passed\" class=\"tests\" style=\"display: none;\">\n" + "\n" + "               </tbody>\n" + "           </table>\n" + "        </td>\n" + "    </tr>\n" + "   </tbody> \n" + "  </table>  \n" + " </body>\n" + "</html>";
        try
        {
            strFileData = FileUtils.readFileToString(file, "UTF8");
            Document document1 = Jsoup.parse(strFileData);
            Document document2 = Jsoup.parse(predefinedFormat);
            org.jsoup.nodes.Element testsFailures = document2.getElementById("tests-failures");
            org.jsoup.nodes.Element testsPassed = document2.getElementById("tests-passed");
            for (org.jsoup.nodes.Element element : document1.getElementsByClass("failureIndicator"))
            {
                element = element.parent().parent();
                testsFailures.append(element.html().replace("?", "&#x2718;"));
                element.remove();
            }
            for (org.jsoup.nodes.Element element : document1.getElementsByClass("successIndicator"))
            {
                element = element.parent().parent();
                testsPassed.append(element.html().replace("?", "&#x2714;"));
                element.remove();
            }
            strFileData = document2.toString();
            FileUtils.writeStringToFile(file, strFileData, "UTF8");
        } catch (Exception e)
        {
            System.out.println("Exception while changing structure of TestNG report");
            e.printStackTrace();
        }
    }

    private static String removeUnexecutedClasses(String filePath)
    {
        String strFileData = null;
        File file = new File(CommonUtilities.normalizePath(filePath));
        // Code to remove 'N/A' containing rows from TestNG Report
        try
        {
            strFileData = FileUtils.readFileToString(file, "UTF8");
            strFileData = strFileData.replace("<frameset cols=\"20%,*\">", "<frameset cols=\"24%,*\">");
            Document document = Jsoup.parse(strFileData);
            for (org.jsoup.nodes.Element element : document.select("td:eq(5)"))
            {
                String content = element.getElementsMatchingOwnText("N/A").text();
                if (content.equalsIgnoreCase("N/A"))
                {
                    element = element.parent();
                    element.remove();
                }
            }
            strFileData = document.toString();
            FileUtils.writeStringToFile(file, strFileData, "UTF8");
        } catch (Exception e)
        {
            System.out.println("Exception while removing 'N/A' containing rows from report");
            e.printStackTrace();
        }
        return strFileData;
    }

    private static String checkIfEmpty(String argumentName, String value, String defaultValue)
    {
        String finalValue = null;
        System.out.println("Value of argument[" + variableCount + "]-" + argumentName + " = " + value);
        finalValue = StringUtils.isEmpty(value) || value.equalsIgnoreCase("null") ? defaultValue : value.trim();
        variableCount++;
        return finalValue;
    }

    private static void logCommentForDebugging(String message)
    {
        if (isDebugMode)
        {
            System.out.println(message);
        }
    }

    /**
     * Update the html value the before HTML report
     *
     * @param originalString
     * @param replacableText
     * @return
     */

    private static String updateHtmlString(String originalString, String replacableText)
    {
        int indexOfbody = originalString.indexOf("</p>");
        String Firsthalf = originalString.substring(0, indexOfbody);
        String LastHalf = originalString.substring(indexOfbody);
        return Firsthalf + replacableText + LastHalf;
    }

    /**
     * Generate dynamic slack link(s) and convert to HTML body
     *
     * @param slackChannelId
     * @param threadTimestamp
     * @return
     */

    private static String getUpdatableDom(String slackChannelId, String threadTimestamp)
    {
        String permalink = "https://app.slack.com/archives/" + slackChannelId + "/p" + threadTimestamp;
        String updateDom = "<a href=\"" + permalink + "\" style=\"display: inline-block; margin-left: 15px;\" >Link to Slack Thread</a>";
        return updateDom;
    }

    private static void addResultsToThanosDatabase(String projectName, String environment, String groupName, String percentage, String duration, String passedCases, String failedCases, String resultLink)
    {

        if (ProjectName.CustomerFrontend.name().equalsIgnoreCase(projectName) || ProjectName.Dash.name().equalsIgnoreCase(projectName))
            projectName = "Vertical - " + projectName;

        Config testConfig = new Config();
        String totalCases = Integer.toString(Integer.parseInt(passedCases) + Integer.parseInt(failedCases));
        String temp = resultLink.split("-Tests")[1];
        String buildTag = "Build" + temp.replace("/html/index.html", "");
        //Date formatedDate = LocalDate.now().toLocalDateTime(LocalTime.now()).toDate(TimeZone.getDefault());
        //String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(formatedDate);
        //QaDashboardHelper.uploadAutomationResultsToThanosBucket(pdgName, createdAt, projectName, environment, groupName, duration, percentage, totalCases, passedCases, failedCases, buildTag, resultLink, false);
        String insertQuery = "INSERT INTO `example_results`(`projectName`,`environment`,`groupName`,`duration`,`percentage`,`totalCases`,`passedCases`,`failedCases`,`buildTag`,`resultLink`)VALUES('" + projectName + "','" + environment.toLowerCase() + "','" + groupName + "','" + duration + "','" + percentage + "','" + totalCases + "','" + passedCases + "','" + failedCases + "','" + buildTag + "','" + resultLink + "');";
        int count = (int) Database.executeQuery(testConfig, insertQuery, QueryType.update, DatabaseName.Thanos);
        if (count == 1)
            System.out.println("Entry successfully created in QA Dashboard DB!");
        else
            System.out.println("Failed to create entry in QA Dashboard DB!");

        addMemberSpecificData(testConfig, projectName);
    }

    private static void addMemberSpecificData(Config testConfig, String projectName)
    {
        Map<String, Integer> emailIdCount = new HashMap<>();
        for (TestrailData data : TestBase.testrailData)
        {
            String emailId = data.getEmailId();
            if (StringUtils.isEmpty(emailId))
            {
                System.out.println("Email Id not present for : " + data.getMethodName());
                emailId = "blank";
            }
            int count = emailIdCount.getOrDefault(emailId, 0);
            emailIdCount.put(emailId, count + 1);
        }

        // Print the count of occurrences for each email ID
        for (Map.Entry<String, Integer> entry : emailIdCount.entrySet())
        {
            String email = entry.getKey();
            int automationCount = entry.getValue();
            String name = getQAEnumByEmail(email).toString();
            logCommentForDebugging("For Email: " + email + "count = " + automationCount);

            String selectQuery = "SELECT id FROM member_data WHERE email = '{$email}' AND projectName = '{$projectName}' AND DATE(createdAt)= DATE('{$createdAt}') ORDER BY createdAt DESC LIMIT 1;";
            String updateQuery = "UPDATE member_data SET createdAt='{$createdAt}', automationCount = {$automationCount}, name = '{$name}' WHERE id = {$id};";
            String insertQuery = "INSERT INTO member_data (createdAt, name, email, projectName, automationCount) VALUES ('{$createdAt}', '{$name}', '{$email}', '{$projectName}', {$automationCount});";
            Date formatedDate = LocalDate.now().toLocalDateTime(LocalTime.now()).toDate(TimeZone.getDefault());
            String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(formatedDate);
            testConfig.putRunTimeProperty("createdAt", createdAt);
            testConfig.putRunTimeProperty("name", name);
            testConfig.putRunTimeProperty("email", email);
            testConfig.putRunTimeProperty("projectName", projectName);
            testConfig.putRunTimeProperty("automationCount", String.valueOf(automationCount));

            Map<String, String> result = Database.executeSelectQuery(testConfig, selectQuery, DatabaseName.Thanos);
            if (result != null && result.size() > 0)
            {
                testConfig.putRunTimeProperty("id", result.get("id"));
                Database.executeQuery(testConfig, updateQuery, QueryType.update, DatabaseName.Thanos);
            } else
            {
                Database.executeQuery(testConfig, insertQuery, QueryType.update, DatabaseName.Thanos);
            }
        }
    }

    private static QA getQAEnumByEmail(String email)
    {
        for (QA qa : QA.values())
        {
            if (qa.getEmail().equalsIgnoreCase(email))
            {
                return qa;
            }
        }
        return null;
    }
}