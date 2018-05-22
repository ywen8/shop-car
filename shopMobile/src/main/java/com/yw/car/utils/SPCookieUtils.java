package com.yw.car.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.yw.car.global.SPMobileApplication;

/**
 * Created by admin on 2017/6/9
 */
public class SPCookieUtils {

    /**
     * 设置cookie
     */
    public static void setCookie(AsyncHttpClient client) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(SPMobileApplication.getInstance().getApplicationContext());
        client.setCookieStore(cookieStore);
    }

}
