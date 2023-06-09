package Automation.Utils.Api;

import Automation.Utils.Api.ApiDetails.ApiRequestType;
import Automation.Utils.Api.ApiDetails.Headers;
import Automation.Utils.AssertHelper;
import Automation.Utils.CommonUtilities;
import Automation.Utils.Config;
import Automation.Utils.TestDataReader;
import com.google.gson.JsonSyntaxException;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

/**
 * This class contains the complete framework required to execute APIs using restassured All the properties/parameters are being set using these functions
 */
public class ApiHelper
{

    public HashMap<String, String> expectedTestData;
    public Config testConfig;

    public ApiHelper(Config testConfig)
    {
        this.testConfig = testConfig;
    }

    public ApiHelper(Config testConfig, String[] sheetNames, int... respectiveSheetRowNumbers)
    {
        this.testConfig = testConfig;
        if (respectiveSheetRowNumbers != null)
        {
            int j = 0;
            for (int i : respectiveSheetRowNumbers)
            {
                if (i > 0)
                {
                    TestDataReader testDataReader = testConfig.getExcelSheet(sheetNames[j]);
                    testConfig.testData.putAll(testDataReader.getTestData(testConfig, i));
                    testConfig.logCommentForDebugging("Sheet : " + sheetNames[j] + " And Row : " + i);
                }
                j++;
            }
        }
    }

    /**
     * @param apiDetails - object of class ApiDetails that contain all the information of the api
     * @param jsonFile   - json file if json body is to be posted
     * @return - api response
     */
    public Response executeRequestAndGetResponse(ApiDetails apiDetails, Object jsonFile)
    {
        String body = getJsonData(jsonFile);
        HashMap<String, String> headers = getHeaders(apiDetails);
        HashMap<String, String> formData = getFormData(apiDetails);
        HashMap<String, String> params = getParams(apiDetails);

        return executeRequestAndGetResponse(apiDetails.getUrl(testConfig), apiDetails.getApiRequestType(), apiDetails.getApiContentType(), headers, params, formData, body);
    }

    /**
     * initialize and set the headers, body, params, form params for the request based on api
     *
     * @param url            - url of api
     * @param apiRequestType - apiRequestType
     * @param apiContentType - apiContentType
     * @param headers        - authorization headers
     * @param params         - parameters to be passed
     * @param formData       - form parameters
     * @param body           - body paramters
     * @return - api response
     */
    public Response executeRequestAndGetResponse(String url, ApiRequestType apiRequestType, ContentType apiContentType, HashMap<String, String> headers, HashMap<String, String> params, HashMap<String, String> formData, String body)
    {
        if (testConfig.testData != null)
        {
            String merchantName = testConfig.getRunTimeProperty("merchantName");
            String merchantId = testConfig.getRunTimeProperty("merchantId");
            if (!(StringUtils.isEmpty(merchantName) && StringUtils.isEmpty(merchantId)))
            {
                testConfig.logColorfulComment("MerchantName = " + merchantName + ", MerchantId = " + merchantId, "Brown");
            }
        }
        testConfig.logColorfulComment("Executing Api = " + apiRequestType + " " + url, "Blue");
        RequestSpecification reqspec = initialiseContentType(apiContentType, null);
        if (body != null)
        {
            reqspec = reqspec.body(body);
            testConfig.logCommentJson("Body = ", CommonUtilities.maskSensitiveDetails(testConfig, body), "Black");
        }
        if (params != null)
        {
            reqspec = reqspec.params(params);
            testConfig.logColorfulComment("Params = " + CommonUtilities.maskSensitiveDetails(testConfig, params), "Black");
        }
        if (formData != null)
        {
            reqspec = reqspec.formParams(formData);
            if (formData.get("filePath") != null)
            {
                reqspec.config(new RestAssuredConfig().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)));
                reqspec.multiPart("file", new File(formData.get("filePath")), formData.get("mimeType"));
            }
            testConfig.logColorfulComment("Form Data = " + CommonUtilities.maskSensitiveDetails(testConfig, formData), "Black");
        }
        if (headers != null)
        {
            reqspec = reqspec.headers(headers);
            testConfig.logColorfulComment("Headers = " + CommonUtilities.maskSensitiveDetails(testConfig, headers), "Black");
        }
        return executeRequestAndGetResponse(url, apiRequestType, reqspec);
    }

    /**
     * execute the request
     *
     * @param apiUrl         - url of api to be executed
     * @param apiRequestType - apiRequestType
     * @param reqspec        - object of RequestSpecification
     * @return response of the executed api
     */
    public Response executeRequestAndGetResponse(String apiUrl, ApiRequestType apiRequestType, RequestSpecification reqspec)
    {
        Response response = null;

        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        try
        {
            switch (apiRequestType)
            {
                case GET:
                    response = reqspec.when().get(apiUrl);
                    break;
                case POST:
                    response = reqspec.when().post(apiUrl);
                    break;
                case DELETE:
                    response = reqspec.when().delete(apiUrl);
                    break;
                case PUT:
                    response = reqspec.when().put(apiUrl);
                    break;
                case PATCH:
                    response = reqspec.when().patch(apiUrl);
                    break;
                case OPTIONS:
                    response = reqspec.when().options(apiUrl);
                    break;
            }
        } catch (Exception e)
        {
            testConfig.logException("Api Execution failed, so trying again...", e);
            switch (apiRequestType)
            {
                case GET:
                    response = reqspec.when().get(apiUrl);
                    break;
                case POST:
                    response = reqspec.when().post(apiUrl);
                    break;
                case DELETE:
                    response = reqspec.when().delete(apiUrl);
                    break;
                case PUT:
                    response = reqspec.when().put(apiUrl);
                    break;
                case PATCH:
                    response = reqspec.when().patch(apiUrl);
                    break;
                case OPTIONS:
                    response = reqspec.when().options(apiUrl);
                    break;
            }
        }
        testConfig.logCommentForDebugging("Api Executed");
        try
        {
            testConfig.logCommentJson("Api Response = ", CommonUtilities.formatStringAsJson(response.asString()), "Blue");
        } catch (JsonSyntaxException jse)
        {
            try
            {
                testConfig.logColorfulComment("Api Response = ", "Blue");
                Document doc = Jsoup.parse(response.asString().replaceAll("window", "w"));
                if (!doc.getElementsByTag("form").isEmpty())
                {
                    testConfig.logColorfulComment(doc.toString(), "Black");
                }
            } catch (Exception e)
            {
            }
        }
        return response;
    }

    /**
     * get the required form data for api in hashmap. the values of form params are extracted from runtime properties
     *
     * @param apiName - Name of the api
     * @return - api response
     */
    public HashMap<String, String> getFormData(ApiDetails apiName)
    {
        if (apiName.getFormParams() == null)
        {
            return null;
        }
        HashMap<String, String> params = new HashMap<>();
        String[] requiredRequestParams = apiName.getFormParams().split(",");
        for (String key : requiredRequestParams)
        {
            String value = "";
            if (key.contains("=") && !key.startsWith("=") && !key.endsWith("="))
            {
                value = key.split("=")[1];
                key = key.split("=")[0];
            } else if (key.startsWith("=") || key.endsWith("="))
            {
                key = key.replace("=", "");
            } else
            {
                value = testConfig.getRunTimeProperty(key);
            }

            if (StringUtils.isEmpty(value))
            {
                value = "";
            }

            params.put(key, value);
        }
        return params;
    }

    /**
     * get the required headers for api in hashmap
     *
     * @param apiname - name of the api
     * @return - Hashmap of headers
     */
    protected HashMap<String, String> getHeaders(ApiDetails apiname)
    {
        HashMap<String, String> headers = new HashMap<>();
        for (Headers header : apiname.getRequiredHeaders())
        {
            String value = testConfig.getRunTimeProperty(header.getValue());
            if (value != null)
            {
                if (!value.equalsIgnoreCase("{removeHeader}"))
                {
                    headers.put(header.getValue(), value);
                }
            }
        }
        return headers;
    }

    /**
     * get the jsonbody
     *
     * @param jsonObject - object of json
     * @return body for api
     */
    public String getJsonData(Object jsonObject)
    {
        if (jsonObject == null)
        {
            return null;
        }
        String filePath = null;
        String jsonString = null;
        if (jsonObject instanceof JsonDetails)
        {
            filePath = ((JsonDetails) jsonObject).getJsonFilePath(testConfig);
        } else if (jsonObject instanceof JSONObject)
        {
            jsonString = jsonObject.toString();
        } else
        {
            filePath = (String) jsonObject;
        }
        try
        {
            jsonString = StringUtils.isEmpty(filePath) ? jsonString : new String(FileUtils.readFileToByteArray(new File(filePath)), Charset.defaultCharset());
            jsonString = testConfig.replaceArgumentsWithRunTimeProperties(jsonString);
            jsonString = CommonUtilities.formatStringAsJson(jsonString);
        } catch (IOException e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return jsonString;
    }

    /**
     * get the parameters from testData or runtime properties and return a hashmap containing the required values based on api
     *
     * @param apiName - api name
     * @return - returns hashmap of params
     */
    public HashMap<String, String> getParams(ApiDetails apiName)
    {
        if (apiName.getRequestParams() == null)
        {
            return null;
        }
        HashMap<String, String> params = new HashMap<>();
        String[] requiredRequestParams = apiName.getRequestParams().split(",");
        for (String key : requiredRequestParams)
        {
            String value = testConfig.getRunTimeProperty(key);
            if (StringUtils.isEmpty(value))
            {
                continue;
            }
            params.put(key, value);
        }
        return params;
    }

    /**
     * initialize the content-type
     *
     * @param contentType - contentType
     * @param reqspec     - RequestSpecification
     * @return - returns RequestSpecification
     */
    public RequestSpecification initialiseContentType(ContentType contentType, RequestSpecification reqspec)
    {
        reqspec = reqspec == null ? given() : reqspec; // initialize request if null
        reqspec = contentType == null || contentType.equals(ContentType.ANY) ? reqspec : reqspec.contentType(contentType); // ignore content-type is it's null or */*
        reqspec = contentType != null && contentType.equals(ContentType.JSON) ? reqspec.accept("application/json") : reqspec; // add accept if content-type is json

        //Updating hostname to http://127.0.0.1:portNumber if execution mode is local and endpoint is private and running on Mars
        if (!Config.isRemoteExecution && testConfig.getRunTimeProperty("isMarsEndpoint") != null
                && Boolean.valueOf(testConfig.getRunTimeProperty("isMarsEndpoint")) && testConfig.getRunTimeProperty("portNumber") != null)
        {
            reqspec.proxy(ProxySpecification.host("127.0.0.1").withPort(Integer.parseInt(testConfig.getRunTimeProperty("portNumber"))));
        }

        reqspec.relaxedHTTPSValidation();
        if (Config.isDebugMode)
        {
            reqspec.log().all();
        }
        RestAssuredConfig config = new RestAssuredConfig();
        config = config.encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
        reqspec = reqspec.config(config);
        return reqspec;
    }

    public Object getValueForKeyFromResponse(Response response, String key)
    {
        JSONObject jsonObj = new JSONObject(response.asString());
        Object value = null;
        try
        {
            value = jsonObj.get(key);
        } catch (JSONException e)
        {
            testConfig.logFailToEndExecution("No " + key + " found in response object");
        }
        return value;
    }

    public String generateAndEncodeCurlToBase64(ApiDetails apiDetails, Object jsonFile)
    {
        String body = getJsonData(jsonFile);
        HashMap<String, String> headers = getHeaders(apiDetails);
        HashMap<String, String> formData = getFormData(apiDetails);
        HashMap<String, String> params = getParams(apiDetails);
        apiDetails.getApiContentType().equals(ContentType.JSON);
        String url = apiDetails.getUrl(testConfig);
        apiDetails.getApiRequestType();

        if (apiDetails.getApiContentType().equals(ContentType.JSON))
        {
            headers.put("content-type", "application/json");
            headers.put("accept", "application/json");
        }

        //handling query params this will create "?key1=value1&key2=value2"

        if (params != null)
        {
            String finalParams = "?";
            for (Map.Entry<String, String> head : params.entrySet())
            {
                finalParams = finalParams + head.getKey() + "=" + head.getValue() + "&";
            }
            finalParams = StringUtils.removeEnd(finalParams, "&");
            url = url + finalParams;
        }

        String curl = "curl -X " + apiDetails.getApiRequestType() + " '" + url + "' \\" + "\n";
        if (headers != null)
        {
            String finalHeaders = "";
            for (Map.Entry<String, String> head : headers.entrySet())
            {
                finalHeaders = finalHeaders + "-H " + "'" + head.getKey() + ": " + head.getValue() + "' \\" + "\n";
            }
            curl = curl + finalHeaders;
            //formatting so that for debugging purpose directly can be copied and used as curl on terminal.
            curl = StringUtils.removeEnd(curl, "\n");
            curl = StringUtils.removeEnd(curl, "\\");
        }
        if (body != null)
        {
            curl = curl + "\\" + "\n";
            curl = curl + "-d " + "'" + body + "'";
        }
        if (formData != null)
        {
            curl = curl + "\\" + "\n";
            String finalformParam = "";
            for (Map.Entry<String, String> head : formData.entrySet())
            {
                finalformParam = finalformParam + "-F " + "'" + head.getKey() + "=\"" + head.getValue() + "\"" + "'" + " \\" + "\n";
            }
            curl = curl + finalformParam;

            //formatting so that for debugging purpose directly can be copied and used as curl on terminal.
            curl = StringUtils.removeEnd(curl, "\n");
            curl = StringUtils.removeEnd(curl, " \\");
        }
        testConfig.logColorfulComment("CURL Request : \n" + curl, "blue");
        String encodedCurl = CommonUtilities.getBase64EncodeString(curl);
        testConfig.logColorfulComment("Encoded Curl : \n" + encodedCurl, "blue");
        return encodedCurl;
    }

    /**
     * Returns a random JSONObject from the provided JSONArray where the specified key matches the specified value.
     * Use cases: When you want to get random element of a JSONArray from a return api response
     *
     * @param jsonArray The JSONArray to search for matching JSONObjects
     * @param key       The key to match
     * @param value     The value to match
     * @return A random JSONObject from the JSONArray where the specified key matches the specified value,
     * or null if no matching JSONObject is found.
     */
    public JSONObject getRandomJSONObjectByKeyValue(JSONArray jsonArray, String key, String value)
    {
        List<JSONObject> matchingObjects = jsonArray.toList().stream()
                .map(obj -> new JSONObject((HashMap) obj))
                .filter(obj -> obj.getString(key).equals(value))
                .collect(Collectors.toList());

        if (matchingObjects.isEmpty())
        {
            return null;
        }

        Collections.shuffle(matchingObjects);

        return matchingObjects.stream().findFirst().orElse(null);
    }

    public void verifyJsonResponse(Response response, ApiDetails api, String apiName, JsonDetails expectedJsonObject, String jsonName)
    {
        try
        {
            boolean isVerified = true;
            boolean isFailed = false;
            JSONObject jsonObj;
            JSONArray jsonArrObj;
            if (response.asString().trim().charAt(0) == '[')
            {
                jsonArrObj = new JSONArray(response.body().asString());
                testConfig.logComment("Length of json array is:  " + jsonArrObj.length());
                for (int i = 0; i < jsonArrObj.length(); i++)
                {
                    isVerified = AssertHelper.compareJsonKeys(testConfig, expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i), jsonArrObj.getJSONObject(i), "JSON response Keys for " + apiName + " API & " + jsonName);
                    if (!isVerified)
                        isFailed = true;

                }
            } else if (response.asString().trim().charAt(0) == '{')
            {
                jsonObj = new JSONObject(response.asString());
                isVerified = AssertHelper.compareJsonKeys(testConfig, expectedJsonObject.getExpectedJSONObject(testConfig), jsonObj, "JSON response Keys for " + apiName + " API & " + jsonName);
                if (!isVerified)
                    isFailed = true;
            }
            if (!isFailed)
                testConfig.logPass("Verified - JSON response Keys for " + apiName + " API & " + jsonName);
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail("Failed to verify " + jsonName + " for " + apiName + " API; ", e);
        }
    }
}