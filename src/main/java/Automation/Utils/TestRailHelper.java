package Automation.Utils;

import Automation.Utils.Api.ApiHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TestRailHelper extends ApiHelper
{

    private TestRailClient testRailClient = null;

    public TestRailHelper(Config testConfig)
    {
        super(testConfig);
        testRailClient = connectToTestRail(testConfig);
    }

    public TestRailClient connectToTestRail(Config testConfig)
    {
        TestRailClient client = new TestRailClient(testConfig.getRunTimeProperty("TestRailHostUrl"));
        client.setUser(CommonUtilities.decryptMessage(System.getProperty("TestRailUsername").getBytes()));
        client.setPassword(CommonUtilities.decryptMessage(System.getProperty("TestRailPassword").getBytes()));
        testConfig.logComment("Connected to TestRail server successfully.");
        return client;
    }

    public List<JSONObject> getListCaseOfSuite(String project, String suite)
    {
        testConfig.logStep("Get the list test case of suite");
        JSONObject cases = null;
        List<JSONObject> caseOfSuite = new ArrayList<>();
        int offset = 0;
        do
        {
            String path = "get_cases/%s&suite_id=%s&limit=250&offset=%s";
            String fullPath = String.format(path, project, suite, offset);
            for (int retry = 0; retry <= 5; retry++)
            {
                try
                {
                    cases = (JSONObject) testRailClient.sendGet(fullPath);
                    if (!cases.isEmpty() && ((List<JSONObject>) cases.get("cases")).size() > 0)
                    {
                        testConfig.logStep("Get the list test case of suite " + suite + ", successful");
                        caseOfSuite.addAll((List<JSONObject>) cases.get("cases"));
                        JSONObject link = (JSONObject) cases.get("_links");
                        if (!Objects.isNull(link.get("next")))
                        {
                            String next = link.get("next").toString();
                            offset = Integer.parseInt(next.substring(next.lastIndexOf('=') + 1));
                        } else
                        {
                            offset = 0;
                        }
                    }
                    break;
                } catch (Exception e)
                {
                    testConfig.logWarning("Can not get list test case of suite" + suite + ", do retry " + retry + "time");
                } finally
                {
                    if (retry == 5)
                    {
                        testConfig.logFail("Can not get list test case of test suite " + suite + " the issue from Testrail!");
                    }
                }
            }
        } while (offset > 0);
        return caseOfSuite;
    }

    public String createMilestonesAndGetId(String name, String projectID)
    {
        String path = "add_milestone/" + projectID;
        JSONObject jsonObject = null;
        Map milestone = new HashMap();
        milestone.put("name", name);
        String mileStoneId = null;
        for (int retry = 0; retry <= 5; retry++)
        {
            try
            {
                jsonObject = new JSONObject((Map) testRailClient.sendPost(path, milestone));
                if (!jsonObject.isEmpty())
                {
                    mileStoneId = jsonObject.get("id").toString();
                }
                break;
            } catch (Exception e)
            {
                testConfig.logWarning("Can not create milestone for project " + projectID + " , do retry " + retry + "time");
                WaitHelper.waitForSeconds(testConfig, 2);
            } finally
            {
                if (retry == 5)
                {
                    testConfig.logFail("Can not create the Milestone for project " + projectID + ", the issue from Testrail now!");
                }
            }
        }
        return mileStoneId;
    }

    public List<String> createTestCaseListByAttribute(List<JSONObject> testCaseList, Map<String, Object> condition, String currentRefer, String mileStoneType)
    {
        switch (mileStoneType)
        {
            case "FCT" ->
            {
                testConfig.logComment("Do the filter test case for FCT");
                List<String> validTestCaseList = new ArrayList<>();
                testCaseList.stream()
                        .filter(e -> e.get("priority_id").toString().equals(condition.get("priorityId")))
                        .filter(e -> condition.get("typeId").toString().contains(e.get("type_id").toString()))
                        .filter(e -> e.get("custom_platform").toString().contains(condition.get("customPlatform").toString()))
                        .filter(e -> checkReferenceSmaller(currentRefer, e))
                        .map(e -> validTestCaseList.add(e.get("id").toString()))
                        .collect(Collectors.toList());
                return validTestCaseList;
            }
            case "Prod Sanity" ->
            {
                testConfig.logComment("Do the filter test case for Prod Sanity");
                List<String> validTestCaseList = new ArrayList<>();
                testCaseList.stream()
                        .filter(e -> condition.get("typeId").toString().contains(e.get("type_id").toString()))
                        .filter(e -> checkReferenceSmaller(currentRefer, e))
                        .filter(e -> e.get("custom_platform").toString().contains(condition.get("customPlatform").toString()))
                        .map(e -> validTestCaseList.add(e.get("id").toString()))
                        .collect(Collectors.toList());

                testCaseList.stream()
                        .filter(e -> e.get("priority_id").toString().equals(condition.get("priorityId")))
                        .filter(e -> e.get("custom_platform").toString().contains(condition.get("customPlatform").toString()))
                        .filter(e -> checkReferenceEqual(currentRefer, e))
                        .map(e -> validTestCaseList.add(e.get("id").toString()))
                        .collect(Collectors.toList());
                return validTestCaseList;
            }
            default -> testConfig.logWarning("The test run value is incorrect, it should be FCT or Prod Sanity");
        }
        return null;
    }

    public void createTestRun(String projectId, String name, String suiteId, String milestoneId, List<String> cases)
    {
        String path = "add_run/" + projectId;
        Map testRun = new HashMap();
        testRun.put("suite_id", suiteId);
        testRun.put("name", name);
        testRun.put("milestone_id", milestoneId);
        testRun.put("case_ids", cases);
        testRun.put("include_all", false);

        for (int retry = 0; retry <= 5; retry++)
        {
            try
            {
                testRailClient.sendPost(path, testRun);
                break;
            } catch (Exception e)
            {
                testConfig.logComment("Can not create test run for suite " + suiteId + ", do retry " + retry + "time");
                WaitHelper.waitForSeconds(testConfig, 2);
            } finally
            {
                if (retry == 5)
                {
                    testConfig.logFail("Can't create the test run for project " + projectId + "with suite " + suiteId + ", issue from Testrail, let try later!");
                }
            }
        }
    }

    public String platForm(String key)
    {
        Map<String, String> map = new HashMap();
        map.put("API", "1");
        map.put("WEB", "2");
        map.put("Android", "3");
        map.put("iOS", "4");
        return map.get(key);
    }

    public String priority(String key)
    {
        Map<String, String> prio = new HashMap<>();
        prio.put("Critical", "4"); //add more priority later
        return prio.get(key);
    }

    public String typeName(String key)
    {
        Map<String, String> type = new HashMap<>();
        type.put("Regression", "9");
        type.put("Prod Sanity", "11");// add more type later
        return type.get(key);
    }

    public Map condition(String priority, List<String> listTypeId, String platform)
    {
        Map map = new HashMap();
        map.put("priorityId", priority);
        map.put("typeId", listTypeId);
        map.put("customPlatform", platform);
        return map;
    }

    public List<TestRailObject> readDataFromJson(String team, String mileStoneType)
    {
        String csvFile = System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Testrail" + File.separator + "JsonFiles" + File.separator + "TestRail.json";
        List<TestRailObject> dataList = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            JsonObject jsonObject = new JsonParser().parse(br).getAsJsonObject();
            br.close();

            JsonObject paymentsObject = jsonObject.getAsJsonObject(team);

            JsonArray testRailSuitesArray = paymentsObject.getAsJsonArray(mileStoneType);

            for (JsonElement element : testRailSuitesArray)
            {
                JsonObject object = element.getAsJsonObject();

                TestRailObject obj = new TestRailObject();
                obj.setTeamName(object.get("teamName").getAsString());
                obj.setProjectId(object.get("projectId").getAsString());
                obj.setSuiteId(object.get("suiteId").getAsString());
                obj.setPriority(object.get("priorityId").getAsString());
                obj.setTypeName(object.getAsJsonArray("typeId"));
                obj.setPlatform(object.get("platform").getAsString());
                dataList.add(obj);
            }
        } catch (IOException e)
        {
            testConfig.logComment("Can't create the data from json file ");
        }
        return dataList;
    }

    public boolean checkReferenceEqual(String currentRefer, JSONObject testCase)
    {
        if (Objects.isNull(testCase.get("refs")))
        {
            testConfig.logWarning("Please add the Reference for test case " + testCase.get("id").toString());
            return false;
        } else
        {
            return currentRefer.equals(testCase.get("refs").toString());
        }
    }

    public boolean checkReferenceSmaller(String currentRefer, JSONObject testCase)
    {
        if (Objects.isNull(testCase.get("refs")))
        {
            testConfig.logWarning("Please add the Reference for test case " + testCase.get("id").toString());
            return true;
        } else
        {
            char[] tcf = testCase.get("refs").toString().trim().substring(1).replace(".", "").toCharArray();
            char[] cr = currentRefer.trim().substring(1).replace(".", "").toCharArray();

            for (int i = 0; i < tcf.length; i++)
            {
                if (Integer.parseInt(String.valueOf(cr[i])) == Integer.parseInt(String.valueOf(tcf[i])))
                {
                    continue;
                }
                return Integer.parseInt(String.valueOf(cr[i])) >= Integer.parseInt(String.valueOf(tcf[i]));
            }
            return true;

        }
    }

    public void createTestRun(String milestoneName, String verticalTeam, String reference, String mileStoneType)
    {
        String testRun = mileStoneType.trim();
        String mileStoneUrl = "/index.php?/milestones/view/";
        List<TestRailObject> testRailObjects = readDataFromJson(verticalTeam, testRun);
        String curProjectId = "";
        String milestoneId = "";
        for (TestRailObject ts : testRailObjects)
        {
            if (!curProjectId.equals(ts.getProjectId()))
            {
                curProjectId = ts.getProjectId();
                milestoneId = createMilestonesAndGetId(milestoneName, curProjectId);
                testConfig.logComment("The Milestone " + reference + mileStoneType + "of " + verticalTeam + " was created at " + testConfig.getRunTimeProperty("TestRailHostUrl") + mileStoneUrl + milestoneId);
            }
            String testRunName = milestoneName + ts.getTeamName() + ts.getPlatform();
            try
            {
                List<JSONObject> jsonObjectList = getListCaseOfSuite(ts.getProjectId(), ts.getSuiteId());
                List<String> listType = new ArrayList<>();
                for (int i = 0; i < ts.getTypeName().size(); i++)
                {
                    String key = ts.getTypeName().get(i).toString().replace('"', ' ').trim();
                    listType.add(typeName(key));
                }
                List<String> criticalCase = createTestCaseListByAttribute(jsonObjectList, condition(priority(ts.getPriority()), listType, platForm(ts.getPlatform())), reference, testRun);
                if (!criticalCase.isEmpty() && !milestoneId.isEmpty())
                {
                    createTestRun(ts.getProjectId(), testRunName, ts.getSuiteId(), milestoneId, criticalCase);
                } else
                {
                    testConfig.logWarning(" Can't find the valid test case in the suite " + ts.getSuiteId() + " please help to check ");
                }
            } catch (Exception e)
            {
                testConfig.logFail("Can't create the test run for " + verticalTeam + " of Milestone " + milestoneId + "with suite " + ts.getSuiteId() + e);
            }
        }
    }

    public void deleteTestRun(String testRunId)
    {
        String path = "delete_run/" + testRunId;
        for (int retry = 0; retry <= 5; retry++)
        {
            try
            {
                testRailClient.sendPost(path, null);
                break;
            } catch (Exception e)
            {
                testConfig.logComment("Can not delete test run " + testRunId + " do retry " + retry + "time" + "issue is " + e);
                WaitHelper.waitForSeconds(testConfig, 2);
            } finally
            {
                if (retry == 5)
                {
                    testConfig.logFail("Can't delete the test run " + testRunId + ", issue from Testrail, let try later!");
                }
            }
        }
    }
}
