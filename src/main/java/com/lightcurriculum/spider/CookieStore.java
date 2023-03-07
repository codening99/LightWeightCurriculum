package com.lightcurriculum.spider;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookieStore implements CookieJar {
    private final HashMap<String, List<Cookie>> _cookieStore = new HashMap<>();
    @Override
    public void saveFromResponse(HttpUrl url, @NotNull List<Cookie> cookies) {
        List<Cookie> currentCookies = _cookieStore.get(url.host());
        List<Cookie> responseCookies = new ArrayList<>(cookies);
        if (currentCookies != null && responseCookies.size() > 0 && currentCookies.size() > 0) {
            for (Cookie currentCookie : currentCookies) {

                if (!responseCookies.contains(currentCookie) && !currentCookie.value().equals("")) {
                    responseCookies.add(currentCookie);
                }
            }
        }
        _cookieStore.put(url.host(), responseCookies);
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = _cookieStore.get(url.host());
        List<Cookie> requestCookies = new ArrayList<>();
        if (cookies != null && cookies.size() > 0) {
            for (Cookie currentCookie : cookies) {
                if (!currentCookie.value().equals("")) {
                    requestCookies.add(currentCookie);
                }
            }
        }
        return requestCookies;
    }
}
