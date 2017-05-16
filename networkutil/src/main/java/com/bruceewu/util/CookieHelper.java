package com.bruceewu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by mac
 */

public class CookieHelper {
    private Map<String, Cookie> mDomain2Cookies;
    private SharedPreferences mCookiePrefs;
    private static volatile CookieHelper stInstance;

    private CookieHelper(Context context) {
        mDomain2Cookies = new HashMap<>();
        mCookiePrefs = context.getSharedPreferences(cookie_tag(), 0);
        Map<String, ?> prefsMap = mCookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            String key = entry.getKey();
            String val = (String) entry.getValue();
            try {
                if (val.isEmpty()) {
                    remove_pref(key);
                    continue;
                }
                if (key.isEmpty()) continue;

                List<HttpCookie> httpCookies = HttpCookie.parse(val);
                if (!httpCookies.isEmpty()) {
                    HttpCookie httpCookie = httpCookies.get(0);
                    Cookie cookie = parse_cookie(httpCookie);
                    mDomain2Cookies.put(key, cookie);
                }
            } catch (Exception ex) {
                remove_pref(key);
            }
        }
    }

    //此处需要在Application创建的时候就完成
    public static CookieHelper onApplicationCreate(Context context) {
        if (stInstance == null) {
            synchronized (CookieHelper.class) {
                if (stInstance == null) {
                    stInstance = new CookieHelper(context);
                }
            }
        }
        return stInstance;
    }

    public static CookieHelper instance() {
        return stInstance;
    }

    private Cookie parse_cookie(HttpCookie httpCookie) {
        if (!httpCookie.hasExpired()) {
            String domain = httpCookie.getDomain();
            Cookie.Builder builder = new Cookie.Builder();
            builder.name(httpCookie.getName())
                    .value(httpCookie.getValue())
                    .path(httpCookie.getPath().startsWith("/") ? httpCookie.getPath() : "/");
            if (domain != null && !domain.isEmpty()) {
                builder.domain(domain);
            }

            if (httpCookie.getSecure()) {
                builder.secure();
            }
            long max_age = httpCookie.getMaxAge();
            if (max_age == 0) {
                builder.expiresAt(Long.MIN_VALUE);
            } else {
                builder.expiresAt(System.currentTimeMillis() + max_age * 1000);
            }

            return builder.build();
        }
        return null;
    }

    private void add_cookie(String key, Cookie cookie) {
        if (cookie == null) return;
        List<HttpCookie> cookies = HttpCookie.parse(cookie.toString());
        HttpCookie hCookie = cookies.get(0);
        synchronized (CookieHelper.class) {
            if (hCookie.hasExpired()) {
                remove_pref(key);
            } else {
                Cookie tmp = mDomain2Cookies.get(key);
                if (tmp == null || !tmp.equals(cookie)) {
                    add_pref(key, cookie);
                }
            }
        }
    }

    private Cookie get_cookie(String key) {
        synchronized (CookieHelper.class) {
            Cookie cookie = mDomain2Cookies.get(key);
            if (cookie != null) {
                List<HttpCookie> cookies = HttpCookie.parse(cookie.toString());
                HttpCookie hCookie = cookies.get(0);
                if (hCookie.hasExpired()) {
                    remove_pref(key);
                } else {
                    return cookie;
                }
            }
        }
        return null;
    }

    private void remove_pref(String key) {
        synchronized (CookieHelper.class) {
            mDomain2Cookies.remove(key);
            SharedPreferences.Editor writer = mCookiePrefs.edit();
            writer.remove(key);
            writer.apply();
        }
    }

    private void add_pref(String key, Cookie cookie) {
        synchronized (CookieHelper.class) {
            mDomain2Cookies.put(key, cookie);
            SharedPreferences.Editor writer = mCookiePrefs.edit();
            writer.putString(key, cookie.toString());
            writer.apply();
        }
    }

    public void write(HttpUrl url, List<Cookie> cookies) {
        String key = parse_domain(url);
        if (null != cookies && cookies.size() > 0) {
            for (Cookie item : cookies) {
                if (sesssion_name().equals(item.name())) {
                    add_cookie(key, item);
                }
            }
        }
    }

    public List<Cookie> read(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        String key = parse_domain(url);
        Cookie cookie = get_cookie(key);
        if (cookie != null) {
            ret.add(cookie);
        }
        return ret;
    }

    private String session_id(String url) {
        HttpUrl hurl = HttpUrl.parse(url);
        String key = parse_domain(hurl);
        Cookie cookie = get_cookie(key);
        return cookie == null ? null : cookie.value();
    }

    public String add_session(String url) {
        String temp = url + "";
        if (can_add(temp)) {
            String session = session_id(url);
            if (!TextUtils.isEmpty(session)) {
                temp += session_tag() + session;
            }
        }
        return temp;
    }

    protected boolean can_add(String url) {
        if (url != null && !url.startsWith("file:")) {
            HttpUrl hUrl = HttpUrl.parse(url);
            if (hUrl != null) {
                String host = hUrl.host();
                if (!TextUtils.isEmpty(host)) {
                    return checkHost(host);
                }
            }
        }
        return false;
    }

    protected String sesssion_name() {
        return "MPHPSESSID";
    }

    protected String session_tag() {
        return "&HPHPSESSID=";
    }

    protected String cookie_tag() {
        return "Cookies_Prefs";
    }

    protected String parse_domain(HttpUrl url) {
        return url.host();
    }

    protected boolean checkHost(String host) {
        return true;
    }
}
