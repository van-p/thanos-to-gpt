package Automation.Utils;

import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase.TestrailData;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import org.testng.internal.TestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IInvokedMethodListener, IAnnotationTransformer, IRetryAnalyzer
{

    int counter = 1;
    int retryMaxLimit = 2;

    public void onTestFailure(ITestResult result)
    {
        Config[] testConfigs = TestBase.threadLocalConfig.get();
        if (testConfigs != null)
        {
            for (Config testConfig : testConfigs)
            {
                if (testConfig != null)
                {
                    testConfig.logComment("***************EXECUTION OF TESTCASE ENDS HERE***************");
                    testConfig.testResult = false;
                    Browser.takeScreenshot(testConfig);
                    testConfig.endTest(result);
                }
            }
        }
    }

    public void onTestSuccess(ITestResult result)
    {
        Config[] testConfigs = TestBase.threadLocalConfig.get();
        if (testConfigs != null)
        {
            for (Config testConfig : testConfigs)
            {
                if (testConfig != null)
                {
                    testConfig.endTest(result);
                } else
                {
                    System.out.println("testConfig object not found in onTestSuccess");
                }
            }
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult)
    {
        // Method to check if testcase failed with soft asserts (Log.Fail) i.e. status as success, to do assertAll, and mark the test case as fail
        if (method.isTestMethod() && (testResult.getStatus() == TestResult.SUCCESS || testResult.getStatus() == TestResult.FAILURE))
        {
            String errorMessage = "";
            Config[] testConfigs = TestBase.threadLocalConfig.get();
            if (testConfigs != null)
            {
                for (Config testConfig : testConfigs)
                {
                    if (testConfig != null)
                    {
                        try
                        {
                            if (testResult.getStatus() == TestResult.SUCCESS)
                            {
                                testConfig.softAssert.assertAll();
                            } else
                            {
                                if (testResult.getThrowable().toString().contains("java.lang.AssertionError"))
                                {
                                    StackTraceElement[] stackTrace = {new StackTraceElement("", "", "", 0)};
                                    testResult.getThrowable().setStackTrace(stackTrace);
                                }
                            }
                        } catch (AssertionError e)
                        {
                            errorMessage = errorMessage + e.getMessage();
                            testResult.setStatus(TestResult.FAILURE);
                            testResult.setThrowable(new AssertionError(errorMessage));
                            StackTraceElement[] stackTrace = {new StackTraceElement("places highlighted via Soft Assert Mechanism in full logs", "", "", 0)};
                            testResult.getThrowable().setStackTrace(stackTrace);
                            Log.failure(testConfig, errorMessage);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result)
    {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        TestrailData testCase = getTestrailData(method);
        TestBase.testrailData.add(testCase);
    }

    private TestrailData getTestrailData(Method method)
    {
        Test testAnnotation = method.getAnnotation(Test.class);
        TestVariables variablesAnnotation = method.getAnnotation(TestVariables.class);

        String testRailData = (variablesAnnotation != null) ? variablesAnnotation.testrailData() : "";
        QA automatedBy = (variablesAnnotation != null) ? variablesAnnotation.automatedBy() : QA.Mukesh;
        return new TestrailData(method.getDeclaringClass().getSimpleName() + "." + method.getName(), testRailData, automatedBy);
    }

    public void onTestSkipped(ITestResult result)
    {
        Config[] testConfigs = TestBase.threadLocalConfig.get();
        if (testConfigs != null)
        {
            for (Config testConfig : testConfigs)
            {
                if (testConfig != null)
                {
                    String message = "Test case skipped " + testConfig.testcaseName;

                    System.out.println(message);
                    message = "<font color='Orange'>" + message + "</font></br>";
                    Reporter.log(message);
                } else
                {
                    System.out.println("testConfig object not found in onTestSkipped");
                }
            }
        }
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {

    }

    public void onStart(ITestContext context)
    {

    }

    public void onFinish(ITestContext context, ITestResult testResult)
    {
        try
        {
            for (int i = 0; i < context.getAllTestMethods().length; i++)
            {
                ITestNGMethod method = context.getAllTestMethods()[i];
                if (method.getRetryAnalyzer(testResult) != null)
                {
                    if (!context.getFailedTests().getResults(method).isEmpty() && !context.getPassedTests().getResults(method).isEmpty())
                    {
                        context.getFailedTests().removeResult(method);
                    }
                    if (!context.getSkippedTests().getResults(method).isEmpty() && !context.getFailedTests().getResults(method).isEmpty())
                    {
                        context.getSkippedTests().removeResult(method);
                    }
                    if (!context.getSkippedTests().getResults(method).isEmpty() && !context.getPassedTests().getResults(method).isEmpty())
                    {
                        context.getSkippedTests().removeResult(method);
                    }
                }
            }
        } catch (Exception e)
        {
            System.out.println("Exception in onFinish function");
            e.printStackTrace();
        }
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult)
    {
        if (method.isTestMethod())
        {
            Config[] testConfigs = TestBase.threadLocalConfig.get();
            if (testConfigs != null)
            {
                for (Config testConfig : testConfigs)
                {
                    if (testConfig != null)
                    {
                        String message = "Execution started for testcase - " + testResult.getMethod().getDescription();
                        testConfig.logColorfulComment("<b><font style='color:white;font-size:13px;padding:2px;background-color:#2196F3;'>" + message + "</font></b></br>", "Blue");
                    }
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod)
    {
        annotation.setRetryAnalyzer(TestListener.class);
    }

    @Override
    public boolean retry(ITestResult result)
    {
        Config[] testConfigs = TestBase.threadLocalConfig.get();
        if (testConfigs != null)
        {
            for (Config testConfig : testConfigs)
            {
                if (testConfig != null)
                {
                    if (testConfig.retry && counter < retryMaxLimit)
                    {
                        counter++;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
