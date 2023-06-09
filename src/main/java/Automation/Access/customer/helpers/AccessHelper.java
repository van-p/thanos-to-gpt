package Automation.Access.customer.helpers;

import Automation.Access.customer.api.AccessApiDetails;
import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.*;
import Automation.Access.customer.web.*;
import Automation.Payments.customer.web.AccountsListPage;
import Automation.SaaS.customer.web.ExportDataPage;
import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Api.ApiDetails.Headers;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.AssertHelper;
import Automation.Utils.Browser;
import Automation.Utils.Config;
import Automation.Utils.DataGenerator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class AccessHelper extends ApiHelper
{

    public static AccessStaticDataBase accessStaticDataBase;
    public UserAccessPage userAccessPage;
    public LoginPage loginPage;
    public DashBoardPage dashBoardPage;
    public AccountsListPage accountsListPage;
    public PersonalDetailsPage personalDetailsPage;
    public SecurityPage securityPage;
    public UpdatePersonalDetailsPage updatePersonalDetailsPage;
    public ExportDataPage exportDataPage;

    public AccessHelper(Config testConfig, int... respectiveSheetRowNumbers)
    {
        super(testConfig, new String[]{"UserDetails"}, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public AccessHelper(Config testConfig, String[] sheets, int... respectiveSheetRowNumbers)
    {
        super(testConfig, sheets, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public void initialiseStaticData()
    {
        if (Config.appLanguage != null)
        {
            CustomerPortalLanguage.valueOf(Config.appLanguage);
            accessStaticDataBase = new AccessStaticDataEn();
        } else
        {
            accessStaticDataBase = new AccessStaticDataEn();
        }
    }

    public Response sendRequestAndGetResponse(AccessApiDetails accessApiDetails, AccessJsonDetails accessJsonDetails)
    {
        switch (accessApiDetails)
        {
            case PostAuthLogin:
                testConfig.putRunTimeProperty("email", testConfig.testData.get("username"));
                testConfig.putRunTimeProperty("token", testConfig.testData.get("password"));
                break;
            case GetAuth:
                testConfig.putRunTimeProperty(Headers.xexampleApplication.getValue(), AccessHelper.accessStaticDataBase.getexampleApplication());
                break;
            case GetListOfEmployees:
                testConfig.putRunTimeProperty("search", "");
                testConfig.putRunTimeProperty("order_by", "full_name");
                break;
            case GetListOfPeople:
                String url = accessApiDetails.getUrl(testConfig, "businessUuid");
                testConfig.putRunTimeProperty("is_creating_debit_card", 1);
                testConfig.putRunTimeProperty("search", "");
                testConfig.putRunTimeProperty("budget_uuid", "");
                return executeRequestAndGetResponse(accessApiDetails.getUrl(testConfig, url), accessApiDetails.getApiRequestType(), accessApiDetails.getApiContentType(), getHeaders(accessApiDetails), getParams(accessApiDetails), getFormData(accessApiDetails), getJsonData(accessJsonDetails));
            case PostAuthToken:
                testConfig.putRunTimeProperty(Headers.xexampleApplication.getValue(), "CNSING");
                break;
            case GetFilterUsers:
                testConfig.putRunTimeProperty("entity_type", "user-access");
                break;
            case GetPlanDetails:
                testConfig.putRunTimeProperty("_fields", "number_of_addition_users, number_of_active_users, number_of_users_included, user_cost,plan_setting, plan.uuid,plan.key, subscription.uuid,subscription.is_active");
                break;
            case GetAllNavigationItems:
                testConfig.putRunTimeProperty("_fields", "icon_url,key,uuid,title,position,spaces.uuid,spaces.key,spaces.type,spaces.title,spaces.icon_url,spaces.position,spaces.web_destination,spaces.parent_title,spaces.parent_uuid,spaces.values,spaces.is_locked,spaces.desktop_icon_url,spaces.actions.uuid,spaces.actions.key,spaces.actions.type,spaces.actions.title,spaces.actions.position,spaces.actions.icon_url,spaces.actions.web_destination,spaces.actions.parent_title,spaces.actions.parent_uuid,spaces.actions.values,spaces.actions.is_locked");
                break;
            case GetAvailableNavigationItems:
                testConfig.putRunTimeProperty("_fields", "uuid,key,type,title,is_locked,icon_url,position,web_destination,badge");
                break;
            case PutUpdatePreferredName:
                testConfig.putRunTimeProperty("preferred_name", DataGenerator.generateRandomString(7));
                break;
            case PutBusinessProperties:
                switch (accessJsonDetails)
                {
                    case PutBusinessHeadquarterRequestSchema:
                    {
                        List<String> countryList = Arrays.asList("BO", "KH", "DZ", "AQ", "AU");
                        testConfig.putRunTimeProperty("businessHeadquarter", countryList.get(DataGenerator.generateRandomNumberInIntRange(0, countryList.size() - 1)));
                        break;
                    }
                    case PutNumberOfEmployeeRequestSchema:
                    {
                        List<String> numberOfEmployeeList = Arrays.asList("1", "2-10", "11-50", "51-200", "201-500", "500+");
                        testConfig.putRunTimeProperty("numberOfEmployee", numberOfEmployeeList.get(DataGenerator.generateRandomNumberInIntRange(1, numberOfEmployeeList.size() - 1)));
                        break;
                    }
                    default:
                        testConfig.logFail("No case defined for " + accessJsonDetails.name());
                }
                break;
            case PostUpdatePhoneNumber:
                testConfig.putRunTimeProperty("phoneNumber", "+95" + DataGenerator.generateRandomNumber(10));
                break;
            default:
                return executeRequestAndGetResponse(accessApiDetails, accessJsonDetails);
        }
        return executeRequestAndGetResponse(accessApiDetails, accessJsonDetails);
    }

    public void verifyApiResponse(Response response, AccessApiDetails accessApiDetails, AccessJsonDetails accessJsonDetails)
    {
        switch (accessApiDetails)
        {
            case PostAuthLogin:
                switch (accessJsonDetails)
                {
                    case AuthLoginOkSingleBusinessWithDebitAccount, AuthLoginNonAdminRoleResponse ->
                            verifyAuthLoginOkSingleBusinessWithDebitAccount(response);
                    case MissingInfoAuthLoginSingleBusinessSuccessfulResponse ->
                            verifyMissingInfoAuthLoginSuccessful(response);
                    case AuthLoginMultipleBusinessesSuccessfulResponse ->
                            verifyAuthLoginMultipleBusinessesSuccessful(response);
                    default -> testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetUserAccesses:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetUserAccessesSuccessfulResponse)
                {
                    verifyGetUserAccessesSuccessfulResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case OauthToken:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.OauthTokenSuccessfulResponse)
                {
                    verifyOauthTokenSuccessfulResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetListOfPeople:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetListOfPeopleSuccessfulResponse)
                {
                    verifyGetListOfPeopleSuccessfulResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetAuth:
                switch (accessJsonDetails)
                {
                    case GetAuthResponseSchema, GetAuthResponseSchemaEasyRequirement -> verifyGetAuth(response);
                    default -> testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetAuth2fa:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetAuth2faSuccessfulResponse)
                {
                    verifyGetAuth2faResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetListOfEmployees:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.EmployeesListSuccessfulResponse)
                {
                    verifyEmployeesListResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetListOfEmployeesInBudget:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.EmployeesInBudgetListSuccessResponse)
                {
                    verifyEmployeesInBudgetListResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case PostAuthToken:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.PostAuthTokenSuccessfulResponse)
                {
                    verifyAuthSessionNotNull(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetPlanDetails:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetPlanDetailsSuccessfulResponse)
                {
                    verifyGetPlanDetailsSuccessfulResponse(response, accessJsonDetails);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetFilterUsers:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetFilterUsersSuccessfulResponse)
                {
                    verifyGetFilterUsersSuccessfulResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetAllNavigationItems:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetAllNavigationItemSuccessfulResponse)
                {
                    verifyAllNavigationItems(response, accessJsonDetails);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetAvailableNavigationItems:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetAvailableNavigationItemSuccessfulResponse)
                {
                    verifyAvailableNavigationItems(response, accessJsonDetails);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case PutPersonal2fa, PutBusiness2fa, DeletePersonal2fa, DeleteBusiness2fa, PutPasswordChange, PutPasswordReset:
                switch (accessJsonDetails)
                {
                    case Get2faSuccessfulResponse, PutPasswordChangeSuccessResponse, PutPasswordResetSuccessResponse ->
                            AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                    default -> testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case PutUpdatePreferredName:
                AssertHelper.compareEquals(testConfig, "Status Code", 202, response.statusCode());
                AssertHelper.compareEquals(testConfig, "Updated preferred name", testConfig.getRunTimeProperty("preferred_name"), response.jsonPath().getString("preferred_name"));
                break;
            case PostRequestAdditionalToken:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.OtpTokenSuccessResponse)
                {
                    verifyOtpTokenResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + accessJsonDetails.name());
                }
                break;
            case GetOptionsOtpChannel:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.GetOptionsOtpChannelSuccessfulResponse)
                {
                    verifyGetOptionsOtpChannelSuccessfulResponse(response);
                } else
                {
                    testConfig.logFail("No case defined for " + accessJsonDetails.name());
                }
            case PostUpdatePhoneNumber:
                if (Objects.requireNonNull(accessJsonDetails) == AccessJsonDetails.UpdatePhoneNumberSuccessfulResponse)
                {
                    verifyUpdatePhoneNumber(response);
                } else
                {
                    testConfig.logFail("No case defined for " + accessJsonDetails.name());
                }
                break;
            case PutBusinessProperties:
                switch (accessJsonDetails)
                {
                    case PutBusinessHeadquarterSuccessResponse -> verifyBusinessHeadquarterSuccessfulResponse(response);
                    case PutNumberOfEmployeeSuccessResponse -> verifyNumberOfEmployeeSuccessfulResponse(response);
                    default -> testConfig.logFail("No case defined for " + accessJsonDetails.name());
                }
                break;
            default:
                testConfig.logFail("Key-values are not being verified for API - " + accessApiDetails.name());
        }
        if (accessJsonDetails != null)
        {
            verifyJsonResponse(response, accessApiDetails, accessApiDetails.name(), accessJsonDetails, accessJsonDetails.name());
        }
    }

    private void verifyOtpTokenResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Response status", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "Is access token not null", Objects.nonNull(response.body().jsonPath().getString("access_token")));
        AssertHelper.compareEquals(testConfig, "Token type", "Bearer", response.body().jsonPath().getString("token_type"));
        testConfig.putRunTimeProperty(ApiDetails.Headers.xexampleAdditionalToken.getValue(), response.body().jsonPath().getString("access_token"));
    }


    public void doAuthentication(AccessJsonDetails accessJsonDetails, BusinessType businessType)
    {
        testConfig.putRunTimeProperty("page", 1);
        testConfig.putRunTimeProperty("limit", 10);
        testConfig.putRunTimeProperty("business_type", businessType.getType());
        testConfig.putRunTimeProperty("source_type", businessType.getType());
        Response tokenResponse = sendRequestAndGetResponse(AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenRequestSchema);
        verifyApiResponse(tokenResponse, AccessApiDetails.PostAuthToken, AccessJsonDetails.PostAuthTokenSuccessfulResponse);

        Response response = sendRequestAndGetResponse(AccessApiDetails.PostAuthLogin, AccessJsonDetails.AuthLoginRequestSchema);
        verifyApiResponse(response, AccessApiDetails.PostAuthLogin, accessJsonDetails);
    }

    private void verifyAuthLoginOkSingleBusinessWithDebitAccount(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Full name", testConfig.testData.get("fullName"), response.jsonPath().getString("person.full_name"));
        AssertHelper.compareEquals(testConfig, "Gender", testConfig.testData.get("gender"), response.jsonPath().getString("person.gender"));
        AssertHelper.compareEquals(testConfig, "Primary email", testConfig.testData.get("username"), response.jsonPath().getString("person.primary_email.email"));
        AssertHelper.compareTrue(testConfig, "Debit Account", Objects.nonNull(response.jsonPath().getString("business.debit_account.uuid")));
        testConfig.putRunTimeProperty(Headers.Authorization.getValue(), "Bearer " + response.jsonPath().getString("access_token"));
        testConfig.putRunTimeProperty(Headers.xexampleBusinessUuid.getValue(), response.jsonPath().getString("business.uuid"));
        testConfig.putRunTimeProperty(Headers.xexampleApplication.getValue(), response.jsonPath().getString("business.application.reference_code"));
        testConfig.putRunTimeProperty("business_uuid", response.jsonPath().getString("business.uuid"));
        testConfig.putRunTimeProperty("businessUuid", response.jsonPath().getString("business.uuid"));
        testConfig.putRunTimeProperty("source_uuid", response.jsonPath().getString("business.uuid"));
    }

    private void verifyMissingInfoAuthLoginSuccessful(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Full name", testConfig.testData.get("fullName"), response.jsonPath().getString("person.full_name"));
        AssertHelper.compareEquals(testConfig, "Gender", testConfig.testData.get("gender"), response.jsonPath().getString("person.gender"));
        AssertHelper.compareEquals(testConfig, "Primary email", testConfig.testData.get("username"), response.jsonPath().getString("person.primary_email.email"));
        AssertHelper.compareEquals(testConfig, "KYC State", true, Boolean.parseBoolean(response.jsonPath().getString("person.eligibility.authorizations.update_missing_info")));
        AssertHelper.compareEquals(testConfig, "KYB State", true, Boolean.parseBoolean(response.jsonPath().getString("business.eligibility.authorizations.update_missing_info")));
        testConfig.putRunTimeProperty(Headers.Authorization.getValue(), "Bearer " + response.jsonPath().getString("access_token"));
        testConfig.putRunTimeProperty(Headers.xexampleBusinessUuid.getValue(), response.jsonPath().getString("business.uuid"));
        testConfig.putRunTimeProperty(Headers.xexampleApplication.getValue(), response.jsonPath().getString("business.application.reference_code"));
        testConfig.putRunTimeProperty("businessUuid", response.jsonPath().getString("business.uuid"));
    }

    private void verifyAuthLoginMultipleBusinessesSuccessful(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Full name", testConfig.testData.get("fullName"), response.jsonPath().getString("person.full_name"));
        AssertHelper.compareEquals(testConfig, "Gender", testConfig.testData.get("gender"), response.jsonPath().getString("person.gender"));
        AssertHelper.compareEquals(testConfig, "Primary email", testConfig.testData.get("username"), response.jsonPath().getString("person.primary_email.email"));
        testConfig.putRunTimeProperty(Headers.Authorization.getValue(), "Bearer " + response.jsonPath().getString("access_token"));
        JSONObject data = new JSONObject(response.body().asString());
        JSONArray businesses = data.getJSONArray("businesses");
        AssertHelper.compareTrue(testConfig, "Number of businesses must greater than 1", businesses.length() > 1);
        IntStream.range(0, businesses.length()).forEach(i ->
        {
            testConfig.putRunTimeProperty("businessReferenceCode" + i, businesses.getJSONObject(i).getString("reference_code"));
            testConfig.putRunTimeProperty("businessUuid" + i, businesses.getJSONObject(i).getString("uuid"));
        });
    }

    private void verifyGetUserAccessesSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Current page", testConfig.getRunTimeProperty("page"), response.jsonPath().getString("current_page"));
        JSONArray array = new JSONObject(response.body().asString()).getJSONArray("data");
        boolean isAdminExisted = IntStream.range(0, array.length()).anyMatch(i -> array.getJSONObject(i).getString("full_name").equals(testConfig.testData.get("fullName")));
        AssertHelper.compareTrue(testConfig, "Is admin existed?", isAdminExisted);
    }

    private void verifyEmployeesInBudgetListResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject data = new JSONObject(response.body().asString());
        JSONArray array = data.getJSONArray("data");
        boolean isExists = IntStream.range(0, array.length()).noneMatch(i ->
        {
            JSONObject object = array.getJSONObject(i);
            return object.getString("full_name").equals(testConfig.getRunTimeProperty("fullName"));
        });
        if (Objects.isNull(testConfig.getRunTimeProperty("budgetOwnerRole")) || testConfig.getRunTimeProperty("budgetOwnerRole").equals(AccessEnums.RoleType.Director.getType()))
        {
            AssertHelper.compareEquals(testConfig, "Is admin present?", true, isExists);
        } else
        {
            AssertHelper.compareEquals(testConfig, "Is admin present?", false, isExists);
        }
    }

    private void verifyAuthSessionNotNull(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareTrue(testConfig, "Auth session token", Objects.nonNull(response.jsonPath().getString("X-Auth-Session")));
        AssertHelper.compareEquals(testConfig, "Is verified", true, response.jsonPath().getBoolean("is_verified"));
        testConfig.putRunTimeProperty(Headers.xAuthSession.getValue(), response.jsonPath().getString("X-Auth-Session"));
    }

    private void verifyEmployeesListResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject data = new JSONObject(response.body().asString());
        JSONArray array = data.getJSONArray("data");
        boolean isExists = IntStream.range(0, array.length()).anyMatch(i ->
        {
            JSONObject object = array.getJSONObject(i);
            return object.getString("full_name").equals(testConfig.getRunTimeProperty("fullName"));
        });
        AssertHelper.compareTrue(testConfig, "Employee", isExists);
    }

    private void verifyGetAuth(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "reference_code", testConfig.testData.get("personReferenceCode"), response.jsonPath().getString("reference_code"));
        AssertHelper.compareEquals(testConfig, "businesses[0].name", testConfig.testData.get("businessName"), response.jsonPath().getString("businesses[0].name"));
        AssertHelper.compareEquals(testConfig, "primary_email", testConfig.testData.get("username"), response.jsonPath().getString("primary_email.email"));
        testConfig.putRunTimeProperty(Headers.xexampleBusinessUuid.getValue(), response.jsonPath().getString("businesses[0].uuid"));
        testConfig.putRunTimeProperty("business_uuid", response.jsonPath().getString("businesses[0].uuid"));
        testConfig.putRunTimeProperty(Headers.xexampleAccountUuid.getValue(), testConfig.testData.get("accountUuid"));
    }

    public void verifyGetAuth2faResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "has_enabled_2fa", testConfig.getRunTimeProperty("hasEnabled2fa"), response.jsonPath().getString("has_enabled_2fa"));
        AssertHelper.compareEquals(testConfig, "has_2fa_enabled_business", testConfig.getRunTimeProperty("has2faEnabledBusiness"), response.jsonPath().getString("has_2fa_enabled_business"));
    }

    private void verifyOauthTokenSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "token_type", "Bearer", response.jsonPath().getString("token_type"));
        AssertHelper.assertNotNull(testConfig, "access_token", response.jsonPath().getString("access_token"));
        AssertHelper.assertNotNull(testConfig, "refresh_token", response.jsonPath().getString("refresh_token"));
        testConfig.putRunTimeProperty(Headers.Authorization.getValue(), "Bearer " + response.jsonPath().getString("access_token"));
    }

    private void verifyGetPlanDetailsSuccessfulResponse(Response response, AccessJsonDetails expectedJsonObject)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject object = new JSONObject(response.body().asString());
        AssertHelper.compareEquals(testConfig, "is_active", expectedJsonObject.getExpectedJSONObject(testConfig).getJSONObject("subscription").get("is_active"), object.getJSONObject("subscription").get("is_active"));
        AssertHelper.compareEquals(testConfig, "plan_key", expectedJsonObject.getExpectedJSONObject(testConfig).getJSONObject("plan").get("key"), object.getJSONObject("plan").get("key"));
        AssertHelper.compareEquals(testConfig, "uuid", expectedJsonObject.getExpectedJSONObject(testConfig).getJSONObject("plan").get("uuid"), object.getJSONObject("plan").get("uuid"));
        AssertHelper.compareEquals(testConfig, "user_cost", expectedJsonObject.getExpectedJSONObject(testConfig).get("user_cost"), object.get("user_cost"));
        AssertHelper.compareEquals(testConfig, "number_of_users_included", expectedJsonObject.getExpectedJSONObject(testConfig).get("number_of_users_included"), object.get("number_of_users_included"));
        AssertHelper.compareEquals(testConfig, "number_of_active_users", expectedJsonObject.getExpectedJSONObject(testConfig).get("number_of_active_users"), object.get("number_of_active_users"));
        AssertHelper.compareEquals(testConfig, "number_of_addition_users", expectedJsonObject.getExpectedJSONObject(testConfig).get("number_of_addition_users"), object.get("number_of_addition_users"));
    }

    private void verifyGetFilterUsersSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Name", "[Search]", response.jsonPath().getString("fields.name"));
        AssertHelper.compareEquals(testConfig, "Key", "[search]", response.jsonPath().getString("fields.key"));
        AssertHelper.compareEquals(testConfig, "Placeholder", "[Search by name or email]", response.jsonPath().getString("fields.placeholder"));
        AssertHelper.compareEquals(testConfig, "Group", "[search]", response.jsonPath().getString("fields.group"));
        AssertHelper.compareEquals(testConfig, "Visible", "[true]", response.jsonPath().getString("fields.visible"));
        AssertHelper.compareEquals(testConfig, "Type", "[text]", response.jsonPath().getString("fields.type"));
    }

    public void verifyAllNavigationItems(Response response, AccessJsonDetails expectedJsonObject)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONArray array = new JSONArray(response.body().asString());
        AssertHelper.compareTrue(testConfig, "item list size is larger than 0", array.length() > 0);
        for (int i = 0; i < array.length(); i++)
        {
            AssertHelper.compareEquals(testConfig, "uuid", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("uuid"), array.getJSONObject(i).get("uuid"));
            AssertHelper.compareEquals(testConfig, "title", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("title"), array.getJSONObject(i).get("title"));
            AssertHelper.compareEquals(testConfig, "key", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("key"), array.getJSONObject(i).get("key"));
            AssertHelper.compareEquals(testConfig, "position", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("position"), array.getJSONObject(i).get("position"));
        }
    }

    public void verifyAvailableNavigationItems(Response response, AccessJsonDetails expectedJsonObject)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONArray array = new JSONArray(response.body().asString());
        AssertHelper.compareTrue(testConfig, "item list size is larger than 0", array.length() > 0);
        for (int i = 0; i < array.length(); i++)
        {
            AssertHelper.compareEquals(testConfig, "uuid", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("uuid"), array.getJSONObject(i).get("uuid"));
            AssertHelper.compareEquals(testConfig, "title", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("title"), array.getJSONObject(i).get("title"));
            AssertHelper.compareEquals(testConfig, "key", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("key"), array.getJSONObject(i).get("key"));
            AssertHelper.compareEquals(testConfig, "position", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("position"), array.getJSONObject(i).get("position"));
            AssertHelper.compareEquals(testConfig, "icon_url", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("icon_url"), array.getJSONObject(i).get("icon_url"));
            AssertHelper.compareEquals(testConfig, "badge", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("badge"), array.getJSONObject(i).get("badge"));
            AssertHelper.compareEquals(testConfig, "is_locked", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("is_locked"), array.getJSONObject(i).get("is_locked"));
            AssertHelper.compareEquals(testConfig, "web_destination", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("web_destination"), array.getJSONObject(i).get("web_destination"));
            AssertHelper.compareEquals(testConfig, "type", expectedJsonObject.getExpectedJSONArray(testConfig).getJSONObject(i).get("type"), array.getJSONObject(i).get("type"));
        }
    }

    private void verifyGetListOfPeopleSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Current page", testConfig.getRunTimeProperty("page"), response.jsonPath().getString("current_page"));
        JSONArray array = new JSONObject(response.body().asString()).getJSONArray("data");
        AssertHelper.compareTrue(testConfig, "People list is not empty", array.length() > 0);
        testConfig.putRunTimeProperty("personUuid", array.getJSONObject(0).get("uuid"));
    }

    public void verifyGetOptionsOtpChannelSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONObject object = new JSONObject(response.body().asString());
        AssertHelper.compareEquals(testConfig, "Name", testConfig.getRunTimeProperty("otpChannelName"), object.getString("name"));
    }

    public void setUpOtpChannelUuid(Response response, OtpChannelType otpChannel)
    {
        JSONArray channels = new JSONArray(response.asString());
        String otpChannelUuid;
        if (otpChannel.equals(OtpChannelType.Email))
        {
            otpChannelUuid = channels.getJSONObject(0).getString("uuid");
        } else
        {
            otpChannelUuid = channels.getJSONObject(1).getString("uuid");
        }
        testConfig.putRunTimeProperty("otpChannelName", otpChannel.name());
        testConfig.putRunTimeProperty("otpChannelUuid", otpChannelUuid);
    }

    public void verifyDefaultOtpIsSet(Response response, OtpChannelType otpChannelType)
    {
        String otpChannelName = response.jsonPath().getString("otp_channel.name");
        String otpChannelUuid = response.jsonPath().getString("otp_channel.uuid");
        AssertHelper.compareEquals(testConfig, "OTP channel name", otpChannelType.name(), otpChannelName);
        AssertHelper.compareEquals(testConfig, "OTP channel uuid", testConfig.getRunTimeProperty("otpChannelUuid"), otpChannelUuid);
    }

    // Web
    public Object doLogin(AccessEnums.AfterLoginExpectedLandingPage afterLoginExpectedLandingPage)
    {
        Browser.navigateToUrl(testConfig, testConfig.getRunTimeProperty("CustomerPortalUrl"));
        loginPage = new LoginPage(testConfig);
        return loginPage.fillDetailsAndLogin(testConfig.testData.get("username"), testConfig.testData.get("password"), afterLoginExpectedLandingPage);
    }

    public Object clickOnForgotPasswordLink()
    {
        Browser.navigateToUrl(testConfig, testConfig.getRunTimeProperty("CustomerPortalUrl"));
        loginPage = new LoginPage(testConfig);
        return loginPage.clickOnForgotPasswordLink();
    }

    public List<String> inviteUsersWithCompanyRoleAndAccessRole(CompanyRole role, AccessRole accessRole)
    {
        userAccessPage = (UserAccessPage) dashBoardPage.navigateOnMenu(SubMenuItem.Users, AfterNavigationPage.UserAccessPage);
        userAccessPage.clickOnNewUserButton();
        List<String> users = userAccessPage.inviteUsersWithCompanyRoleAndAccessRole(role, accessRole);
        userAccessPage.clickOnSendInviteButton();
        userAccessPage.clickOnDoneButton();
        return users;
    }

    public Object doLoginWithIncorrectCredentials(AccessEnums.LoginType loginType)
    {
        Browser.navigateToUrl(testConfig, testConfig.getRunTimeProperty("CustomerPortalUrl"));
        loginPage = new LoginPage(testConfig);
        return switch (loginType)
        {
            case IncorrectPassword3Times ->
                    loginPage.loginWith3IncorrectPasswordAttempts(testConfig.testData.get("username"), testConfig.testData.get("password"), 3);
            case IncorrectOtp3Times ->
                    loginPage.loginWith3IncorrectOtpAttempts(testConfig.testData.get("username"), testConfig.testData.get("password"), 3);
            default -> null;
        };
    }

    public String getAuthToken()
    {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) testConfig.driver;
        String authTokenValue = (String) jsExecutor.executeScript(String.format("return window.localStorage.getItem('%s');", "access_token"));
        return authTokenValue.substring(9);
    }

    public void verifyUpdatePhoneNumber(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Phone Number", testConfig.getRunTimeProperty("phoneNumber"), response.jsonPath().getString("phone"));
        AssertHelper.compareEquals(testConfig, "State", "PHNE", response.jsonPath().getString("state"));
    }

    public void verifyBusinessHeadquarterSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 202, response.statusCode());
        JSONArray array = new JSONArray(response.body().asString());
        for (int i = 0; i < array.length(); i++)
        {
            if (testConfig.getRunTimeProperty("businessHeadquarter").equals(array.getJSONObject(i).getString("value")))
            {
                testConfig.logPass("Business Headquarter value is updated to " + array.getJSONObject(i).getString("value"));
                break;
            } else if (i == array.length() - 1)
            {
                testConfig.logFail("Business Headquarter value is not updated ");
            }
        }
    }

    public void verifyNumberOfEmployeeSuccessfulResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 202, response.statusCode());
        JSONArray array = new JSONArray(response.body().asString());
        for (int i = 0; i < array.length(); i++)
        {
            if (testConfig.getRunTimeProperty("numberOfEmployee").equals(array.getJSONObject(i).get("value")))
            {
                testConfig.logPass("Number of Employee value is updated to " + array.getJSONObject(i).get("value"));
                break;
            } else if (i == array.length() - 1)
            {
                testConfig.logFail("Number of Employee value is not updated ");
            }
        }
    }
}