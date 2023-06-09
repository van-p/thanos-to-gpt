package Automation.SaaS.api;

import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.api.DashAccessJsonDetails;
import Automation.Access.dash.helpers.DashAccessHelper;
import Automation.Access.dash.helpers.GenieOptionsEnum.BusinessType;
import Automation.Access.dash.helpers.GenieOptionsEnum.ScenarioOption;
import Automation.Utils.Config;
import Automation.Utils.TestBase;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestGenieScenario extends TestBase
{

    @Test(description = "Create a new Genie Scenario and create test data using this scenario.", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT)
    public void testCreateNewGenieScenario(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();
        testConfig.logStep("Create a new Genie Scenario and create test data using this scenario. Then clean up the scenario.");
        dashAccessHelper.createNewGenieScenario(DashAccessApiDetails.CreateGenieScenario, DashAccessJsonDetails.PostNewGenieScenarioSchema, DashAccessJsonDetails.CreateGenieScenarioSuccessfulResponse);
        Map<String, String> createdBusinessDetails = dashAccessHelper.generateTestDataUsingCreatedScenario(DashAccessApiDetails.CreateScenarioData, DashAccessJsonDetails.CreateScenarioDataSchema, DashAccessJsonDetails.CreateScenarioDataSuccessfulResponse);
        dashAccessHelper.deleteCreatedGenieScenario(DashAccessApiDetails.DeleteGenieScenario, null);
        testConfig.logComment("Business UUID: " + createdBusinessDetails.get("genieBusinessUuid"));
        testConfig.logComment("Reference Code: " + createdBusinessDetails.get("genieBusinessReferenceCode"));
    }

    @Test(description = "Create test data using existing genie scenario.", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT)
    public void testCreateDataUsingExistingGenieScenario(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();
        testConfig.logStep("Create test data using the existing scenario.");
        Map<String, String> createdBusinessDetails = dashAccessHelper.generateTestDataUsingCreatedScenario(DashAccessApiDetails.CreateScenarioData, DashAccessJsonDetails.CreateDataSchemaSgdIdrUsd, DashAccessJsonDetails.CreateDataSgdIdrUsdResponse);
        testConfig.logComment("Business UUID: " + createdBusinessDetails.get("genieBusinessUuid"));
        testConfig.logComment("Reference Code: " + createdBusinessDetails.get("genieBusinessReferenceCode"));
    }

    @Test(description = "Create pending business using existing genie scenario.", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT)
    public void testCreatePendingBusinessUsingExistingGenieScenario(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();
        testConfig.logStep("Create test data using the existing scenario.");
        Map<String, String> createdBusinessDetails = dashAccessHelper.createNewBusinessWithGenieScenario(BusinessType.NewPendingBusiness);
        testConfig.logComment("Business UUID: " + createdBusinessDetails.get("genieBusinessUuid"));
        testConfig.logComment("Reference Code: " + createdBusinessDetails.get("genieBusinessReferenceCode"));
    }

    @Test(description = "Create verified business using existing genie scenario.", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT)
    public void testCreateVerifiedAccountWithOptionListUsingExistingGenieScenario(Config testConfig)
    {
        DashAccessHelper dashAccessHelper = new DashAccessHelper(testConfig, 1);
        testConfig.logStep("Login to Dashboard");
        dashAccessHelper.doLogin();
        testConfig.logStep("Create test data using the existing scenario.");
        List<ScenarioOption> scenarioOptions = new ArrayList<>();
        scenarioOptions.add(ScenarioOption.KybVerified);
        scenarioOptions.add(ScenarioOption.KycVerified);
        scenarioOptions.add(ScenarioOption.BusinessSettings);
        Map<String, String> createdBusinessDetails = dashAccessHelper.createDataWithGenieScenario(scenarioOptions);
        testConfig.logComment("Business UUID: " + createdBusinessDetails.get("genieBusinessUuid"));
        testConfig.logComment("Reference Code: " + createdBusinessDetails.get("genieBusinessReferenceCode"));
    }

}
