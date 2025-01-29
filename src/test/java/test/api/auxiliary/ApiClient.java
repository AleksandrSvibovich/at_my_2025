package test.api.auxiliary;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ApiClient {
    private final Page page;

    public ApiClient(Page page) {
        this.page = page;
    }

    public APIResponse sendPostRequest(String url, String body) {
        return page.request().post(url,
                RequestOptions.create()
                        .setData(body)
                        .setHeader("Content-Type", "application/json"));
    }

    public APIResponse sendPutRequest(String url, String body) {
        return page.request().put(url,
                RequestOptions.create()
                        .setData(body)
                        .setHeader("Content-Type", "application/json"));
    }

    public APIResponse sendDeleteRequest(String url) {
        return page.request().delete(url);
    }

    public APIResponse sendPostRequestWithHeaders(String url, String body, Map<String, String> headers) {
        RequestOptions requestOptions = RequestOptions.create().setData(body);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equalsIgnoreCase("set-cookie")) {
                key = "cookie";
                value = value.replace("\n", " ");
            }
            requestOptions.setHeader(key, value);
        }

        return page.request().post(url, requestOptions);
    }

    public String convertByte2String(APIResponse resp) {
        return new String(resp.body(), StandardCharsets.UTF_8);
    }

    public String loadJsonFromFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }

    public APIResponse sendGetRequest(String url) {
        return page.request().get(url);
    }

    public APIResponse sendGetRequestHeaders(String url, Map<String, String> headers) {
        RequestOptions requestOptions = RequestOptions.create();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equalsIgnoreCase("set-cookie")) {
                key = "cookie";
                value = value.replace("\n", " ");
            }
            requestOptions.setHeader(key, value);
        }

        return page.request().get(url, requestOptions);


    }


}