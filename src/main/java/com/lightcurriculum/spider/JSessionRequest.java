package com.lightcurriculum.spider;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class JSessionRequest {
    static final Headers DEFAULT_HEADERS = new Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
            .add("Accept-Encoding", "gzip, deflate, br")
            .add("Connection", "keep-alive")
            .build();

    private final Headers headers;
    private final OkHttpClient client;

    public JSessionRequest() {
        this.headers = DEFAULT_HEADERS;
        client = new OkHttpClient.Builder().cookieJar(new CookieStore()).callTimeout(3, TimeUnit.SECONDS).build();
    }

    public JSessionRequest(Headers h) {
        this.headers = h;
        client = new OkHttpClient.Builder().cookieJar(new CookieStore()).callTimeout(3, TimeUnit.SECONDS).build();
    }

    public Response post(String url, Map<String, String> postParam) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (postParam != null) {
            for (Map.Entry<String, String> entry : postParam.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).headers(headers).post(builder.build()).build();
        return client.newCall(request).execute();
    }

    public Response post(String url) throws IOException {
        return post(url, null);
    }

    public Response get(String url, Map<String, String> getParam) throws IOException {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (getParam != null) {
            for (Map.Entry<String, String> entry : getParam.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(builder.build()).headers(headers).build();
        return client.newCall(request).execute();
    }

    public Response get(String url) throws IOException {
        return get(url, null);
    }
}
