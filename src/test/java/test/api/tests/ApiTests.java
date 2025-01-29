package test.api.tests;

import com.google.gson.*;
import io.qameta.allure.Description;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import test.api.auxiliary.ApiClient;
import test.api.auxiliary.ApiConfig;
import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.api.dto.Lists;
import test.api.dto.SimpleFilter;
import test.api.dto.enums.STATUS;
import test.api.dto.enums.TYPES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class ApiTests {
    public static final String LOGIN_BODY = "src/test/java/test/api/jsons/loginBody.json";
    public static final String PRE_ESTIMATE_JSON = "src/test/java/test/api/jsons/pre_estimate.json";
    public static final String COMPLEX_JSON = "src/test/java/test/api/jsons/complex.json";

    Playwright pw = Playwright.create();
    Browser chromium = pw.chromium().launch();
    Page page = chromium.newPage();
    ApiClient apiClient = new ApiClient(page);
    String appID;

    private static void assertRespStatus(APIResponse response, int expectedCode) {
        Assert.assertEquals(response.status(), expectedCode, "Unexpected behaviour, actual -> " + response.text());
    }

    @Test(priority = 1)
    public void testAuthorize() throws Exception {
        String jsonBody = apiClient.loadJsonFromFile(LOGIN_BODY);
        apiClient.sendPostRequest(ApiConfig.URL_LOGIN, jsonBody);

        String jsonClient = apiClient.convertByte2String(apiClient.sendGetRequest(ApiConfig.URL_CLIENTS));
        JSONObject jsonObject = new JSONObject(jsonClient);
        String mdmCode = jsonObject.getJSONObject("result") // получаем MDM код организации из структуры JSON
                .getJSONObject("data")
                .getJSONObject("multiClient")
                .getJSONArray("all")
                .getJSONObject(0)
                .getString("mdmCode");

        apiClient.sendPostRequest(ApiConfig.URL_SET_ACTIVE_CLIENT,
                "{\"mdmCode\":\"" + mdmCode + "\"}");
        APIResponse getUserInfo = apiClient.sendGetRequest(ApiConfig.URL_GET_USER_INFO);
        assertRespStatus(getUserInfo, 200);


    }


    @Test(priority = 2)
    public void testPreEstimate() {
        APIResponse preEstimApp = apiClient.sendPostRequest(ApiConfig.URL_CREATE_PRE_ESTIMATE, "");
        assertRespStatus(preEstimApp, 200);
        JSONObject preEstimJson = new JSONObject(apiClient.convertByte2String(preEstimApp));
        appID = preEstimJson.getString("id");
    }

    @Test(priority = 3)
    public void testCreateDraftPreEstimate() throws Exception {
        String preEstimBody = apiClient.loadJsonFromFile(PRE_ESTIMATE_JSON);
        APIResponse preEstimate = apiClient.sendPutRequest(ApiConfig.URL_APPLICATION + appID, preEstimBody);
        assertRespStatus(preEstimate, 200);
    }

    @Test(priority = 4)
    public void testPreEstimDraftIsExist() throws Exception {
        String listBody = new Gson().toJson(new Lists(2, "", new ArrayList<>(Arrays.asList(TYPES.PRE_ESTIMATE_APPLICATION)), new ArrayList<>(Arrays.asList(STATUS.DRAFT)))); // получаем JSON из нашего класса с заданными паметрами Limit,Query,Type,Status
        APIResponse list = apiClient.sendPostRequest(ApiConfig.URL_LIST, listBody);
        JsonArray jsonArray = JsonParser.parseString(apiClient.convertByte2String(list)).getAsJsonArray();
        JSONObject application = new JSONObject(String.valueOf(jsonArray.get(0)));
        Assert.assertEquals(application.get("id"), appID, "Last application is not the same as was created in " +
                "previous test or test completed incorrect, should be id = " + appID);
    }

    @Test(priority = 5)
    public void testComplexApp() {
        APIResponse complexAPI = apiClient.sendPostRequest(ApiConfig.URL_CREATE_COMPLEX, "");
        assertRespStatus(complexAPI, 200);
        JSONObject complexJSON = new JSONObject(apiClient.convertByte2String(complexAPI));
        appID = complexJSON.getString("id");
    }

    @Test(priority = 6)
    @Description("Test for Create Draft Complex app")
    public void testCreateDraftComplex() throws Exception {
        String complexBody = apiClient.loadJsonFromFile(COMPLEX_JSON);
        APIResponse complex = apiClient.sendPutRequest(ApiConfig.URL_APPLICATION + appID, complexBody);
        assertRespStatus(complex, 200);
    }

    @Test(priority = 7)
    @Description("Test for delivery conditions dictionary")
    public void testDeliveryConditions() {
        APIResponse delCond = apiClient.sendGetRequest(ApiConfig.URL_GET_DEL_COND);
        assertRespStatus(delCond, 200);
        JSONArray arr = new JSONObject(delCond.text()).getJSONArray("deliveryConditions"); // получаем строку из респонса, на её основе создаем ананомный джейсон, и получаем массив по ключу из этого json
        Assert.assertEquals(arr.length(), 15, "delivery conditions is incorrect, actual is " +
                arr.length() + ", but expected " + 15); // 15 кол-во условий которое может существовать по спеке
    }

    @Test(priority = 8)
    @Description("Test for countries dictionary")
    public void testCountries() {
        APIResponse countries = apiClient.sendGetRequest(ApiConfig.URL_GET_COUNTRIES);
        assertRespStatus(countries, 200);
        JSONArray countriesList = new JSONObject(countries.text()).getJSONArray("countries");
        Assert.assertEquals(countriesList.length(), 253, "expected size " +
                253 + ", actual is " + countriesList.length());

    }

    @Test(priority = 9)
    @Description("Check currency dictionary")
    public void testCurrencyDictionary() {
        APIResponse response = apiClient.sendGetRequest(ApiConfig.URL_GET_CURRENCY);
        assertRespStatus(response, 200);
        JSONArray currency = new JSONObject(response.text()).getJSONArray("currencies");
        Assert.assertEquals(currency.length(), 162, "expected size " +
                162 + ", actual is " + currency.length());
    }

    @Test(priority = 9)
    @Description("Check counterparties")
    public void testCounterparties() {
        String filterJson = new Gson().toJson(new SimpleFilter(20, ""));
        APIResponse response = apiClient.sendPostRequest(ApiConfig.URL_POST_COUNTERPARTY, filterJson);
        assertRespStatus(response, 200);
        JsonArray jsonArray = JsonParser.parseString(apiClient.convertByte2String(response)).getAsJsonArray();
        JSONObject object = new JSONObject(String.valueOf(jsonArray.get(0)));
        Assert.assertFalse(object.get("counterpartyName").toString().isEmpty(), "Counterparties name can't be empty, whole response " + response.text());
    }

    @Test(priority = 9)
    @Description("Check contracts")
    public void testContractsList() {
        String filterJson = new Gson().toJson(new SimpleFilter(20, ""));
        APIResponse response = apiClient.sendPostRequest(ApiConfig.URL_POST_CONTRACTS, filterJson);
        assertRespStatus(response, 200);
        JsonArray jsonArray = JsonParser.parseString(apiClient.convertByte2String(response)).getAsJsonArray();
        JSONObject object = new JSONObject(String.valueOf(jsonArray.get(0)));
        Assert.assertFalse(object.get("contractNumber").toString().isEmpty(), "contract number can't be empty, whole response " + response.text());
    }

    @Test(priority = 9)
    public void testComplexDraftIsExist() {
        String listBody = new Gson().toJson(new Lists(2, "", new ArrayList<>(Arrays.asList(TYPES.CONSOLIDATED_APPLICATION)), new ArrayList<>(Arrays.asList(STATUS.DRAFT)))); // получаем JSON из нашего класса с заданными паметрами Limit,Query,Type,Status
        APIResponse list = apiClient.sendPostRequest(ApiConfig.URL_LIST, listBody);
        JsonArray jsonArray = JsonParser.parseString(apiClient.convertByte2String(list)).getAsJsonArray();
        JSONObject application = new JSONObject(String.valueOf(jsonArray.get(0)));
        Assert.assertEquals(application.get("id"), appID, "Last application is not the same as was created in " +
                "previous test or test completed incorrect, should be id = " + appID);
    }

    @Test(dataProvider = "statusAndTypesProvider", priority = 10)
    @Description("Check that search is work correct with 'send offer' status ")
    public void testCheckSearchByStatus(STATUS status, TYPES type) {
        Lists listOfApps = new Lists(100, "", new ArrayList<>(Collections.singletonList(type)), new ArrayList<>(Collections.singletonList(status)));

        String listOfAppsInString = new Gson().toJson(listOfApps);
        APIResponse response = apiClient.sendPostRequest(ApiConfig.URL_LIST, listOfAppsInString);
        JsonArray jsonArray = JsonParser.parseString(apiClient.convertByte2String(response)).getAsJsonArray();

        jsonArray.forEach(object -> {
            String code = object.getAsJsonObject().get("status").getAsJsonObject().get("code").toString();
            String finishResult = code.substring(1, code.length() - 1);
            Assert.assertTrue(finishResult.equalsIgnoreCase(status.toString()), "actual result is " + finishResult);
        });
    }

    @DataProvider(name = "statusAndTypesProvider")
    public Object[][] statusAndTypesProvider() {
        STATUS[] statuses = STATUS.values();
        TYPES[] types = TYPES.values();
        Object[][] data = new Object[statuses.length * types.length][2]; // двойка(константа) потому что два столбца

        int index = 0;
        for (STATUS status : statuses) {
            for (TYPES type : types) {
                data[index][0] = status; // Заполняем статус
                data[index][1] = type;   // Заполняем тип
                index++;
            }
        }
        return data;
    }

//    @Test(priority = 11)
//    public void deleteDraft() {
//        APIResponse deleteDraft = apiClient.sendDeleteRequest(ApiConfig.URL_APPLICATION + appID);
//        assertRespStatus(deleteDraft, 204);
//    }
}