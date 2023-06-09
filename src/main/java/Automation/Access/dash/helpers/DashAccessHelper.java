package Automation.Access.dash.helpers;

import Automation.Access.customer.helpers.AccessEnums;
import Automation.Access.customer.helpers.AccessEnums.*;
import Automation.Access.dash.api.DashAccessApiDetails;
import Automation.Access.dash.api.DashAccessJsonDetails;
import Automation.Access.dash.helpers.DashAccessEnums.EntityType;
import Automation.Access.dash.helpers.DashAccessEnums.FileCollection;
import Automation.Access.dash.helpers.DashAccessEnums.StateCode;
import Automation.Access.dash.helpers.GenieOptionsEnum.BusinessType;
import Automation.Access.dash.helpers.GenieOptionsEnum.ScenarioOption;
import Automation.Access.dash.web.DashHomePage;
import Automation.Access.dash.web.DashLoginPage;
import Automation.Access.dash.web.DashNavigationPage;
import Automation.SaaS.customer.helpers.SaasEnums.FileMimeType;
import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.*;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.*;

public class DashAccessHelper extends ApiHelper
{

    public static DashAccessStaticDataBase dashAccessStaticDataBase;
    public DashHomePage dashHomePage;
    public DashLoginPage dashLoginPage;

    public DashAccessHelper(Config testConfig)
    {
        super(testConfig);
        initialiseStaticData();
    }

    public DashAccessHelper(Config testConfig, int... respectiveSheetRowNumbers)
    {
        super(testConfig, new String[]{"DashUserDetails"}, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public DashAccessHelper(Config testConfig, String[] sheets, int... respectiveSheetRowNumbers)
    {
        super(testConfig, sheets, respectiveSheetRowNumbers);
        initialiseStaticData();
    }

    public void initialiseStaticData()
    {
        if (Config.appLanguage != null)
        {
            AccessEnums.CustomerPortalLanguage.valueOf(Config.appLanguage);
            dashAccessStaticDataBase = new DashAccessStaticDataEn();
        } else
        {
            dashAccessStaticDataBase = new DashAccessStaticDataEn();
        }
    }

    public DashNavigationPage loginToAdminDashboard()
    {
        Browser.navigateToUrl(testConfig, testConfig.getRunTimeProperty("adminDashboardUrl"));
        dashLoginPage = new DashLoginPage(testConfig);
        return dashLoginPage.loginToAdminDashboard(testConfig.getRunTimeProperty("dashUsername"), testConfig.getRunTimeProperty("dashPassword"));
    }

    // api
    public Response sendRequestAndGetResponse(DashAccessApiDetails api, DashAccessJsonDetails jsonDetails)
    {

        switch (api)
        {
            case GetEligibilitiesList:
                testConfig.putRunTimeProperty("source_type", "person");
                testConfig.putRunTimeProperty("elementsPerPage", "10");
                testConfig.putRunTimeProperty("currentPage", "1");
                break;
            case PostNewPeople:
                String randomData = DataGenerator.getTimeinMillSeconds();
                testConfig.putRunTimeProperty("fullName", "Automation People " + randomData);
                testConfig.putRunTimeProperty("email", randomData + "@example.com");
                testConfig.putRunTimeProperty("phone", "+65" + randomData);
                testConfig.putRunTimeProperty("nric", randomData);
                break;
            case GetPeopleDetails:
                testConfig.putRunTimeProperty("_fields",
                        "uuid,reference_code,salutation,full_name,preferred_name,language,id_document_type,country_of_birth.name,marital_status.name,mother_name,spouse_name,nationality.name,npwp,nric,purposes.name,date_of_birth,id_document_expiry_date,npwp_ownership.name,additional_roles.name,additional_roles.uuid,oauth_attempts_left,values.uuid,values.property,values.value,otp_channel.name,businesses.uuid,businesses.name,businesses.reference_code,businesses.role_name,businesses.role_uuid,businesses.registration_role_name,businesses.commission_rule_name,businesses.department_name,businesses.designation_name,kyc.verifications,kyc.state,kyc.result,is_password_resettable,has_enabled_2fa,onboarding_source,authorizations,application.uuid,application.country.name,application.reference_code,application.settings.opening.unincorporated_business_funnel_enabled,state,eligibility,eligibility.note,eligibility.remark,eligibility.uuid,eligibility.state,eligibility.risk_level,requirements,password_setup_link,onboarded_by_affiliate_partner_admin_reason_visible,replicable_file_collections");
                break;
            case PostNewBusinesses:
                String randomDataBusiness = DataGenerator.getTimeinMillSeconds();
                testConfig.putRunTimeProperty("businessName", "Automation Business " + randomDataBusiness);
                testConfig.putRunTimeProperty("registrationNumber", randomDataBusiness);
                break;
            case PostNewModule:
                String name = DataGenerator.generateRandomString(10);
                testConfig.putRunTimeProperty("name", name);
                testConfig.putRunTimeProperty("countryUuids", Country_Uuid.getRandomCountry_Uuid().getCountryUuid());
                testConfig.putRunTimeProperty("isAddOn", true);
                testConfig.putRunTimeProperty("key", DataGenerator.generateRandomString(10));
                testConfig.putRunTimeProperty("planUuids", PlanUuidType.getRandomPlanUuidType().getType());
                testConfig.putRunTimeProperty("modelUuids", ModelUuidsType.getRandomFeatureType().getModelUuids());
                testConfig.putRunTimeProperty("limitType", RestrictionType.UNRESTRICTED.getType());
                testConfig.putRunTimeProperty("billingType", BillingType.ACCESS.getType());
                testConfig.putRunTimeProperty("addOnBillingFrequency", BillingFrequencyType.MONTHLY.getType());
                testConfig.putRunTimeProperty("price", DataGenerator.generateRandomNumber(4));
                testConfig.putRunTimeProperty("currencyCode", "SGD");
                testConfig.putRunTimeProperty("moduleBillingFrequency", BillingFrequencyType.MONTHLY.getType());
                testConfig.putRunTimeProperty("bannerTitle", DataGenerator.generateRandomString(5));
                testConfig.putRunTimeProperty("bannerDescription", DataGenerator.generateRandomString(10));
                break;
            case GetModuleUuid:
                testConfig.putRunTimeProperty("_fields",
                        "description,order,is_public,created_at,module_plans.uuid,uuid,name,key,is_add_on,price,currency_code,billing_frequency,countries.uuid,items.uuid,items.type,items.billing_type,items.limit_type,items.billing_frequency,items.banner_title,items.banner_description,items.model_uuid,items.model_name");
                break;
            case PutModule:
                testConfig.putRunTimeProperty("name", DataGenerator.generateRandomString(10));
                testConfig.putRunTimeProperty("country_uuids", Country_Uuid.getRandomCountry_Uuid().getCountryUuid());
                break;
            case GetBusinessDetails:
                testConfig.putRunTimeProperty("_fields",
                        "uuid,name,bills_email,slug,reference_code,registration_number,province,description,is_primarily_selling_online,website_url,business_model,spent_card_daily_transaction_amount,spent_card_monthly_transaction_amount,card_transaction_limit_currency_code,application.computed_settings,accounts,accounts.reference_code,accounts.uuid,accounts.currency,application.country.name,application.reference_code,country.uuid,country.name,country.code,country_incorporation.name,debit_account.uuid,eligibility.note,eligibility.remark,eligibility.uuid,eligibility.state,eligibility.risk_level,incorporated_date,industry_type.parent.type,industry_type.type,people.uuid,people.full_name,people.purposes,people.reference_code,people.role_name,people.registration_role_name,people.role_slug,people.department_name,people.designation_name,registration_type.name,state,turnover_band.range,values,itt_integration_access,fx_integration_access,robo_accountant_access,mca_integration_access,authorizations,pay_now_in_integration_access,sgd_integration_access,type,business_group.name,business_group.uuid,functional_currency.uuid,functional_currency.code,functional_currency.name,is_affiliate_partner,affiliate_partner_business.uuid,affiliate_partner_business.name,affiliate_partner_business.reference_code,affiliate_partner_onboarded_businesses.uuid,affiliate_partner_onboarded_businesses.name,affiliate_partner_onboarded_businesses.reference_code,replicable_file_collections");
            default:
                return executeRequestAndGetResponse(api, jsonDetails);
        }

        return executeRequestAndGetResponse(api, jsonDetails);
    }

    public void verifyApiResponse(Response response, DashAccessApiDetails api, DashAccessJsonDetails expectedJsonObject)
    {
        switch (api)
        {
            case PostAuthLogin:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.AuthSuccessfulResponse)
                {
                    verifyDashAuthLoginResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case PostOauthToken:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.OauthTokenSuccessfulResponse)
                {
                    verifyOauthToken(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case DeleteAuthLogout:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                break;
            case GetEligibilitiesList:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.EligibilitiesListSuccessfulResponse)
                {
                    verifyEligibilitiesListResponse(response);
                } else
                {
                    testConfig.logFail("No case defined for " + expectedJsonObject);
                }
                break;
            case PostNewPeople:
                verifyCreatePeople(response);
                break;
            case GetPeopleDetails:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.GetPeopleDetailsCreatedFromDashSuccessfulResponse)
                {
                    verifyGetPeopleDetailsCreatedFromDashResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case PostNewBusinesses:
                verifyCreateBusiness(response);
                break;
            case PostNewModule:
                AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
                testConfig.putRunTimeProperty("moduleUuid", response.jsonPath().getString("uuid"));
                break;
            case PutModule:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                break;
            case GetModuleUuid:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.GetNewModuleSchemaResponse)
                {
                    verifyGetModuleDetailResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case GetBusinessDetails:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.GetBusinessDetailsCreatedFromDashSuccessfulResponse)
                {
                    verifyGetBusinessDetailsCreatedFromDashResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case CreateGenieScenario:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.CreateGenieScenarioSuccessfulResponse)
                {
                    verifyCreateGenieScenarioResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case CreateScenarioData:
                switch (expectedJsonObject)
                {
                    case CreateScenarioDataSuccessfulResponse, CreateDataSgdIdrUsdResponse, CreateVerifiedBusinessResponse,
                            CreateDataNewPendingBusinessResponse -> verifyCreatedDataGenieScenarioResponse(response);
                    default -> testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case PostUploadFile:
                if (Objects.requireNonNull(expectedJsonObject) == DashAccessJsonDetails.UploadFileSuccessfulResponse)
                {
                    verifyUploadFileResponse(response);
                } else
                {
                    testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                }
                break;
            case DeleteGenieScenario, DeleteModule:
                AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
                AssertHelper.compareEquals(testConfig, "Response Body", "[]", response.body().path("").toString());
                break;
            default:
                testConfig.logFail("Key-values are not being verified for " + expectedJsonObject.name());
                break;
        }

        if (expectedJsonObject != null)
        {
            verifyJsonResponse(response, api, api.name(), expectedJsonObject, expectedJsonObject.name());
        }
    }

    public Map<String, String> verifyCreatedDataGenieScenarioResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        HashMap<String, String> createdData = new HashMap<>();
        try
        {
            ((org.json.simple.JSONArray) new JSONParser().parse(response.body().asString())).forEach(data ->
            {
                org.json.simple.JSONObject dataObject = (org.json.simple.JSONObject) data;
                org.json.simple.JSONObject valueObject;
                if (dataObject.get("name").equals("Business"))
                {
                    valueObject = ((org.json.simple.JSONObject) dataObject.get("value"));
                    createdData.put("genieBusinessUuid", (String) valueObject.get("uuid"));
                    createdData.put("genieBusinessReferenceCode", (String) valueObject.get("reference_code"));
                }
                if (dataObject.get("name").equals("Director"))
                {
                    valueObject = ((org.json.simple.JSONObject) dataObject.get("value"));
                    createdData.put("genieDirectorUuid", (String) valueObject.get("uuid"));
                    createdData.put("genieDirectorReferenceCode", (String) valueObject.get("reference_code"));
                    createdData.put("genieDirectorEmail", (String) valueObject.get("emailValue"));
                    createdData.put("genieDirectorTokenValue", (String) valueObject.get("tokenValue"));
                }
                if (dataObject.get("name").equals("Director Access Token"))
                {
                    createdData.put("genieDirectorAccessToken", (String) dataObject.get("value"));
                }
            });
        } catch (ParseException e)
        {
            testConfig.logFail("Error while parsing Genie response: " + e.getMessage());
        }
        AssertHelper.compareTrue(testConfig, "status", (Boolean) ((HashMap<?, ?>) ((HashMap<?, ?>) ((ArrayList<?>) response.jsonPath().get()).get(0)).get("value")).get("status"));
        AssertHelper.assertNotNull(testConfig, "reference_code", response.jsonPath().getString("uuid"));
        return createdData;
    }

    private void verifyCreateGenieScenarioResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "uuid", response.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("genieScenarioUuid", response.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("genieScenarioTitle", response.jsonPath().getString("title"));
    }

    private void verifyDashAuthLoginResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Token Type", "2fa", response.jsonPath().getString("token_type"));
    }

    private void verifyOauthToken(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "token_type", "Bearer", response.jsonPath().getString("token_type"));
        AssertHelper.assertNotNull(testConfig, "access_token", response.jsonPath().getString("access_token"));
        AssertHelper.assertNotNull(testConfig, "refresh_token", response.jsonPath().getString("refresh_token"));
        testConfig.putRunTimeProperty(ApiDetails.Headers.Authorization.getValue(), "Bearer " + response.jsonPath().getString("access_token"));
    }

    public void doLogin()
    {
        Response response = sendRequestAndGetResponse(DashAccessApiDetails.PostAuthLogin, DashAccessJsonDetails.LoginRequestSchema);
        verifyApiResponse(response, DashAccessApiDetails.PostAuthLogin, DashAccessJsonDetails.AuthSuccessfulResponse);
        response = sendRequestAndGetResponse(DashAccessApiDetails.PostOauthToken, DashAccessJsonDetails.OauthTokenRequestSchema);
        verifyApiResponse(response, DashAccessApiDetails.PostOauthToken, DashAccessJsonDetails.OauthTokenSuccessfulResponse);
    }

    private void verifyEligibilitiesListResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        JSONArray array = new JSONObject(response.body().asString()).getJSONArray("items");
        int arrayLength = array.length();
        String modelName;
        AssertHelper.compareTrue(testConfig, "Verify array length > 0", arrayLength > 0);
        if (arrayLength > 0)
        {
            for (int i = 0; i < arrayLength; i++)
            {
                modelName = array.getJSONObject(i).get("model_name").toString();
                AssertHelper.compareEquals(testConfig, "Model Name", testConfig.getRunTimeProperty("source_type"), modelName.toLowerCase());
            }
        }
    }

    private void verifyCreatePeople(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "Uuid not null", response.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("peopleUuid", response.jsonPath().getString("uuid"));
    }

    private void verifyGetPeopleDetailsCreatedFromDashResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "State not null", response.jsonPath().getString("state"));
        AssertHelper.assertNotNull(testConfig, "Kyc requirement not null", response.jsonPath().getString("requirements"));
        AssertHelper.assertNotNull(testConfig, "Application setting not null", response.jsonPath().getString("application"));
        AssertHelper.assertNotNull(testConfig, "Password setup link not null", response.jsonPath().getString("password_setup_link"));
    }

    public void createNewGenieScenario(DashAccessApiDetails dashAccessApiDetails, DashAccessJsonDetails dashAccessJsonDetails, DashAccessJsonDetails responseJsonDetails)
    {
        Response response = sendRequestAndGetResponse(dashAccessApiDetails, dashAccessJsonDetails);
        verifyApiResponse(response, dashAccessApiDetails, responseJsonDetails);
    }

    public Map<String, String> generateTestDataUsingCreatedScenario(DashAccessApiDetails dashAccessApiDetails, DashAccessJsonDetails dashAccessJsonDetails, DashAccessJsonDetails responseJsonDetails)
    {
        Response response = sendRequestAndGetResponse(dashAccessApiDetails, dashAccessJsonDetails);
        return verifyCreatedDataGenieScenarioResponse(response);
    }

    public void deleteCreatedGenieScenario(DashAccessApiDetails deleteGenieScenario, DashAccessJsonDetails dashAccessJsonDetails)
    {
        Response response = sendRequestAndGetResponse(deleteGenieScenario, dashAccessJsonDetails);
        verifyApiResponse(response, deleteGenieScenario, dashAccessJsonDetails);
    }

    private void verifyCreateBusiness(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "Uuid not null", response.jsonPath().getString("uuid"));
        testConfig.putRunTimeProperty("businessUuid", response.jsonPath().getString("uuid"));
    }

    private void verifyGetBusinessDetailsCreatedFromDashResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.assertNotNull(testConfig, "State not null", response.jsonPath().getString("state"));
        AssertHelper.assertNotNull(testConfig, "Business Group not null", response.jsonPath().getString("business_group"));
        AssertHelper.assertNotNull(testConfig, "Application setting not null", response.jsonPath().getString("application"));
    }

    public void uploadFileToEntity(String fileName, FileMimeType mimeType)
    {
        testConfig.logComment("Upload file '" + fileName + "' to collection '" + testConfig.getRunTimeProperty("source_collection") + "'");
        String filePath = System.getProperty("user.dir") + File.separator + "TestData" + File.separator + "Access" + File.separator + "UploadFiles" + File.separator + fileName;
        testConfig.putRunTimeProperty("mimeType", mimeType.getName());
        testConfig.putRunTimeProperty("filePath", filePath);
        Response response = sendRequestAndGetResponse(DashAccessApiDetails.PostUploadFile, null);
        verifyApiResponse(response, DashAccessApiDetails.PostUploadFile, DashAccessJsonDetails.UploadFileSuccessfulResponse);
    }

    public void fulfillCreateKycRequirements()
    {
        String fileName = "passport_sg.jpg";
        Response response = sendRequestAndGetResponse(DashAccessApiDetails.GetPeopleDetails, null);
        if (!Boolean.parseBoolean(response.jsonPath().getString("requirements.identity_documents")))
        {
            testConfig.putRunTimeProperty("source_uuid", response.jsonPath().getString("uuid"));
            testConfig.putRunTimeProperty("source_type", EntityType.Person.getName());
            testConfig.putRunTimeProperty("source_collection", FileCollection.Passport.getName());
            testConfig.putRunTimeProperty("type", "passport");
            uploadFileToEntity(fileName, FileMimeType.IMAGE_JPEG);
        }
        if (!Boolean.parseBoolean(response.jsonPath().getString("requirements.selfie")))
        {
            testConfig.putRunTimeProperty("source_uuid", response.jsonPath().getString("uuid"));
            testConfig.putRunTimeProperty("source_type", EntityType.Person.getName());
            testConfig.putRunTimeProperty("source_collection", FileCollection.Selfies.getName());
            uploadFileToEntity(fileName, FileMimeType.IMAGE_JPEG);
        }
        if (!Boolean.parseBoolean(response.jsonPath().getString("requirements.proof_of_personal_address")))
        {
            testConfig.putRunTimeProperty("source_uuid", response.jsonPath().getString("uuid"));
            testConfig.putRunTimeProperty("source_type", EntityType.Person.getName());
            testConfig.putRunTimeProperty("source_collection", FileCollection.ProofOfAddress.getName());
            uploadFileToEntity(fileName, FileMimeType.IMAGE_JPEG);
        }
    }

    public Map<String, String> createNewBusinessWithGenieScenario(BusinessType businessType)
    {
        switch (businessType)
        {
            case NewPendingBusiness ->
            {
                return generateTestDataUsingCreatedScenario(DashAccessApiDetails.CreateScenarioData, DashAccessJsonDetails.CreateDataNewPendingBusinessSchema, DashAccessJsonDetails.CreateDataNewPendingBusinessResponse);
            }
            case VerifiedBusiness ->
            {
                return generateTestDataUsingCreatedScenario(DashAccessApiDetails.CreateScenarioData, DashAccessJsonDetails.CreateVerifiedBusinessSchema, DashAccessJsonDetails.CreateVerifiedBusinessResponse);
            }
            default -> testConfig.logFail("Invalid Business Type");
        }
        return new HashMap<>();
    }

    public Map<String, String> createDataWithGenieScenario(List<ScenarioOption> optionList)
    {
        DashAccessJsonDetails request = findMatchedJsonDetails(optionList, "Request");
        DashAccessJsonDetails response = findMatchedJsonDetails(optionList, "Response");
        if (request == null || response == null)
        {
            testConfig.logFailToEndExecution("Scenario not found for given options. Please follow this guideline to prepare option data: " +
                    "https://weexample.atlassian.net/wiki/spaces/QA/pages/624394307/Genie+Automation+Guideline.");
        }
        return generateTestDataUsingCreatedScenario(DashAccessApiDetails.CreateScenarioData, request, response);
    }


    public DashAccessJsonDetails findMatchedJsonDetails(List<ScenarioOption> optionList, String type)
    {
        ArrayList<String> userInput = new ArrayList<>();
        StringBuilder optionListString = new StringBuilder();
        for (ScenarioOption option : optionList)
        {
            userInput.add(option.name());
            optionListString.append(option.name());
        }
        for (DashAccessJsonDetails constant : DashAccessJsonDetails.values())
        {
            boolean containsAll = true;
            for (String input : userInput)
            {
                if (!constant.name().contains(input))
                {
                    containsAll = false;
                    break;
                }
            }
            if (containsAll && constant.name().length() == (optionListString + type).length())
            {
                return constant;
            }
        }
        return null;
    }

    private void verifyUploadFileResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 201, response.statusCode());
        AssertHelper.compareEquals(testConfig, "File collection", testConfig.getRunTimeProperty("source_collection"), response.jsonPath().getString("collection"));
        AssertHelper.compareEquals(testConfig, "Source type", testConfig.getRunTimeProperty("source_type"), response.jsonPath().getString("model_name").toLowerCase());
        AssertHelper.compareEquals(testConfig, "Model UUID", testConfig.getRunTimeProperty("source_uuid"), response.jsonPath().getString("model_uuid"));
        AssertHelper.compareEquals(testConfig, "Mime type", testConfig.getRunTimeProperty("mimeType"), response.jsonPath().getString("mime_type"));
    }

    public void fulfillCreateKybRequirements()
    {
        Response response = sendRequestAndGetResponse(DashAccessApiDetails.GetBusinessDetails, null);
        testConfig.putRunTimeProperty("type", response.jsonPath().getString("type"));
        response = sendRequestAndGetResponse(DashAccessApiDetails.GetCreateKybRequirements, null);
        if (!Boolean.parseBoolean(response.jsonPath().getString("board_resolution")))
        {
            testConfig.putRunTimeProperty("source_uuid", testConfig.getRunTimeProperty("businessUuid"));
            testConfig.putRunTimeProperty("source_type", EntityType.Business.getName());
            testConfig.putRunTimeProperty("source_collection", FileCollection.BoardResolution.getName());
            uploadFileToEntity("passport_sg.jpg", FileMimeType.IMAGE_JPEG);
        }
    }

    public Response waitingForEntityStateChange(EntityType entityType, StateCode fromStateCode)
    {
        DashAccessApiDetails dashAccessApiDetails = null;
        String statePath = null;
        switch (entityType)
        {
            case Person ->
            {
                dashAccessApiDetails = DashAccessApiDetails.GetPeopleDetails;
                statePath = "state.reference_code";
            }
            case Business ->
            {
                dashAccessApiDetails = DashAccessApiDetails.GetBusinessDetails;
                statePath = "state.reference_code";
            }
            default -> testConfig.logFail("Do not support for Entity " + entityType.getName());
        }

        Response response;
        int retry = 5;
        while (retry > 0)
        {
            response = sendRequestAndGetResponse(dashAccessApiDetails, null);
            if (!fromStateCode.getCode().equals(response.jsonPath().getString(statePath)))
            {
                return response;
            }
            WaitHelper.waitForSeconds(testConfig, 1);
            retry--;
        }
        testConfig.logFail("Status of " + entityType.getName() + " does not change after " + retry + " seconds");
        return null;
    }

    private void verifyGetModuleDetailResponse(Response response)
    {
        AssertHelper.compareEquals(testConfig, "Status Code", 200, response.statusCode());
        AssertHelper.compareEquals(testConfig, "Module name", testConfig.getRunTimeProperty("name"), response.jsonPath().getString("name"));
        AssertHelper.compareEquals(testConfig, "Model Uuid", testConfig.getRunTimeProperty("modelUuids"), response.jsonPath().getString("items[0].model_uuid"));
        AssertHelper.compareEquals(testConfig, "country", testConfig.getRunTimeProperty("countryUuids"), response.jsonPath().getString("countries[0].uuid"));
        AssertHelper.compareEquals(testConfig, "is_add_on", testConfig.getRunTimeProperty("isAddOn"), response.jsonPath().getString("is_add_on"));
        AssertHelper.compareEquals(testConfig, "key", testConfig.getRunTimeProperty("key"), response.jsonPath().getString("key"));
        AssertHelper.compareEquals(testConfig, "plan_uuids", testConfig.getRunTimeProperty("planUuids"), response.jsonPath().getString("module_plans[0].uuid"));
        AssertHelper.compareEquals(testConfig, "add_on_billing_frequency", testConfig.getRunTimeProperty("addOnBillingFrequency"), response.jsonPath().getString("items[0].billing_frequency"));
        AssertHelper.compareEquals(testConfig, "price", testConfig.getRunTimeProperty("price"), response.jsonPath().getString("price"));
        AssertHelper.compareEquals(testConfig, "currency_code", testConfig.getRunTimeProperty("currencyCode"), response.jsonPath().getString("currency_code"));
        AssertHelper.compareEquals(testConfig, "Module type", testConfig.getRunTimeProperty("limitType"), response.jsonPath().getString("items[0].limit_type"));
        AssertHelper.compareEquals(testConfig, "billing_type", testConfig.getRunTimeProperty("billingType"), response.jsonPath().getString("items[0].billing_type"));
        AssertHelper.compareEquals(testConfig, "module_billing_frequency", testConfig.getRunTimeProperty("addOnBillingFrequency"), response.jsonPath().getString("items[0].billing_frequency"));
    }
}