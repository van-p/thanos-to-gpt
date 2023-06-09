package Automation.Utils;


import com.google.gson.JsonArray;

public class TestRailObject
{
    private String teamName;
    private String suiteId;
    private String priority;

    private JsonArray typeName;
    private String platform;
    private String projectId;

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public String getSuiteId()
    {
        return suiteId;
    }

    public void setSuiteId(String suiteId)
    {
        this.suiteId = suiteId;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }


    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public JsonArray getTypeName()
    {
        return typeName;
    }

    public void setTypeName(JsonArray typeName)
    {
        this.typeName = typeName;
    }
}
