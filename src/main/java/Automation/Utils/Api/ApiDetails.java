package Automation.Utils.Api;

import Automation.Utils.Config;
import io.restassured.http.ContentType;

import java.util.ArrayList;

/**
 * Interface to get the details of the APIS irrespective of the project
 */
public interface ApiDetails
{

    /**
     * @return the content-type required to hit the API
     */
    ContentType getApiContentType();

    /**
     * @return the hostname where the API is to be hit
     */
    ApiHost getApihost();

    /**
     * @return the request type of the API
     */
    ApiRequestType getApiRequestType();

    /**
     * @return return the String containing the form parameters to be submitted in order to hit the API
     */
    String getFormParams();

    /**
     * @return the path (or sub url) to hit the API
     */
    String getApiPath();

    /**
     * @return the comma-separated parameter names for a GET request
     */
    String getRequestParams();

    /**
     * @return the arraylist of the headers required to hit the API
     */
    ArrayList<Headers> getRequiredHeaders();

    /**
     * @param testConfig - Pass object of config
     * @param params     - Pass object of params you want to append in the url
     * @return the Complete URL of API to hit
     */
    String getUrl(Config testConfig, Object... params);

    /**
     * Host where API is to be hit
     */
    enum ApiHost
    {
        ApiBasePath
    }

    /**
     * Request type for API
     */
    enum ApiRequestType
    {
        DELETE,
        GET,
        POST,
        PUT,
        PATCH,
        OPTIONS
    }

    /**
     * Headers required to hit the API
     */
    enum Headers
    {
        Authorization("Authorization"),
        Accept("Accept"),
        apiKey("apiKey"),
        api_key("api-key"),
        xexampleBusinessUuid("x-example-business-uuid"),
        xexampleApplication("x-example-application"),
        xexampleAccountUuid("x-example-account-uuid"),
        xAuthSession("x-auth-session"),
        xexampleAdditionalToken("x-example-additional-token"),
        idempotencyKey("idempotency-key");

        private final String value;

        Headers(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }

}
