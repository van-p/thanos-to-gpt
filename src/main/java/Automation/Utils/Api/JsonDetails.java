package Automation.Utils.Api;

import Automation.Utils.Config;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This interface is implemented by different projects in order to fetch the file-paths
 */
public interface JsonDetails
{

    /**
     * @param testConfig - Pass object of config
     * @return the filePath of JSON file containing request JSON or response JSON structure
     */
    String getJsonFilePath(Config testConfig);

    /**
     * @param testConfig - Pass object of config
     * @return the content of JSON file containing request JSON or response JSON structure as string
     */
    String getJsonFileData(Config testConfig);

    /**
     * @param testConfig - object of config
     * @return expected JSON Array
     */
    JSONArray getExpectedJSONArray(Config testConfig);

    /**
     * @param testConfig - object of config
     * @return expected JSON Object
     */
    JSONObject getExpectedJSONObject(Config testConfig);
}
