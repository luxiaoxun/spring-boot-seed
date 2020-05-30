package com.luxx.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient DEFAULT_OK_HTTP = createSimpleHttpClient();

    public static OkHttpClient createSimpleHttpClient() {
        return createTimeOutHttpClient(1);
    }

    public static OkHttpClient createTimeOutHttpClient(int timeOut) {
        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.MINUTES)
                .build();
    }

    public static String postJson(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/json;charset=UTF-8");
        return postJson(url, body, header);
    }

    public static String postJson(String url, String queryJson, OkHttpClient okHttpClient) {
        return postJson(url, queryJson, okHttpClient, Collections.EMPTY_MAP);
    }

    public static String postJson(String url, String queryJson, Map<String, String> header) {
        return postJson(url, queryJson, DEFAULT_OK_HTTP, header);
    }

    public static String postJson(String url, String queryJson, OkHttpClient okHttpClient, Map<String, String> header) {
        RequestBody body = RequestBody.create(queryJson, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .headers(Headers.of(header))
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Response postFile(String url, byte[] file, Map<String, String> header, String fileName) throws IOException {
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, requestBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(header))
                .post(multipartBody)
                .build();
        Call call = DEFAULT_OK_HTTP.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getJson(String queryUrl, Map<String, String> header) {
        return getJson(queryUrl, DEFAULT_OK_HTTP, header);
    }

    public static String getJson(String queryUrl, OkHttpClient okHttpClient, Map<String, String> header) {
        Request request = new Request.Builder()
                .url(queryUrl)
                .headers(Headers.of(header))
                .get()
                .build();
        // 该方法会自动关闭
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
