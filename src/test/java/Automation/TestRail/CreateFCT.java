package Automation.TestRail;

import Automation.Utils.Config;
import Automation.Utils.TestBase;
import Automation.Utils.TestRailHelper;
import org.testng.annotations.Test;

public class CreateFCT extends TestBase
{


    private final String reference = "V1.5.0";
    private final String fct = " FCT ";
    private final String milestoneFct = reference + fct;
    private final String prodSanity = " Prod Sanity ";
    private final String milestoneProdSanity = reference + prodSanity;
    private TestRailHelper testRailHelper = null;


    @Test(dataProvider = "getTestConfig")
    public void createTestRunForFCT(Config testConfig)
    {
        testRailHelper = new TestRailHelper(testConfig);
        testConfig.logStep("Create the Test Run for Payments");
        testRailHelper.createTestRun(milestoneFct, "SaaS", reference, fct);
    }

    @Test(dataProvider = "getTestConfig")
    public void createTestRunForProdSanity(Config testConfig)
    {
        testRailHelper = new TestRailHelper(testConfig);
        testConfig.logStep("Create the Test Run for Payments");
        testRailHelper.createTestRun(milestoneProdSanity, "Access", reference, prodSanity);
    }

}