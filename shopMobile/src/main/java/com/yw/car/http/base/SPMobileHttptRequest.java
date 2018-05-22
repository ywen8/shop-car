package com.yw.car.http.base;/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月28日 下午8:13:39
 * Description:所有URL请求的基类
 *
 * @version V1.0
 */

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.model.person.SPUser;
import com.yw.car.utils.SMobileLog;
import com.yw.car.utils.SPCookieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;


/**
 * 网络请求基类,实现GET,POS方法,网络请求已经是异步实现,调用时不需要额外开辟新线程调用
 *
 * @author 飞龙
 */
public class SPMobileHttptRequest {

    /**
     * GET请求
     */
    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client;
        if (params == null) params = new RequestParams();
        if (SPMobileApplication.getInstance().isLogined()) {
            SPUser user = SPMobileApplication.getInstance().getLoginUser();
            params.put("user_id", user.getUserID());
            if (!SPStringUtils.isEmpty(user.getToken())) params.put("token", user.getToken());
        }
        if (SPMobileApplication.getInstance().getDeviceId() != null) {
            String imei = SPMobileApplication.getInstance().getDeviceId();
            params.put("unique_id", imei);
        }
        try {
            configSign(params, url);
            SMobileLog.i("REQUEST_URL", "GET:" + url + "&" + params);
            if (isHttps(url)) {
                InputStream ins = null;
                String result = "";
                ins = SPMobileApplication.getContext().getAssets().open("gd.cer"); // 下载的证书放到项目中的assets目录中
                CertificateFactory cerFactory = CertificateFactory
                        .getInstance("X.509");
                Certificate cer = cerFactory.generateCertificate(ins);
                KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
                keyStore.load(null, null);
                keyStore.setCertificateEntry("trust", cer);
                SSLSocketFactory
                        ssl = new SSLSocketFactory(
                        keyStore);
                ssl.setHostnameVerifier(ssl.getHostnameVerifier());// 记得设置主机为严格认证，这样才会起作用，证书不对时会直接返回请求失败
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", ssl, 80));// 只注册一个证书，http也不能使用
                schemeRegistry.register(new Scheme("https", ssl, 443));// 只注册一个https证书，只能打开一个网址
                client = new AsyncHttpClient(schemeRegistry);
            } else {
                client = new AsyncHttpClient();
            }

            client.setTimeout(10 * 1000);              //设置超时时间10s
            SPCookieUtils.setCookie(client);           //保存cookie
            client.get(url, params, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseHandler.onFailure(-1, new Header[]{}, new Throwable(e.getMessage()), new JSONObject());
        }
    }

    /**
     * POST请求
     */

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client;
        try {
            if (isHttps(url)) {
                InputStream ins = null;
                String result = "";
                ins = SPMobileApplication.getContext().getAssets().open("gd.cer"); // 下载的证书放到项目中的assets目录中
                CertificateFactory cerFactory = CertificateFactory
                        .getInstance("X.509");
                Certificate cer = cerFactory.generateCertificate(ins);
                KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
                keyStore.load(null, null);
                keyStore.setCertificateEntry("trust", cer);
                SSLSocketFactory ssl = new SSLSocketFactory(
                        keyStore);
                ssl.setHostnameVerifier(ssl.getHostnameVerifier());// 记得设置主机为严格认证，这样才会起作用，证书不对时会直接返回请求失败
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", ssl, 80));// 只注册一个证书，http也不能使用
                schemeRegistry.register(new Scheme("https", ssl, 443));// 只注册一个https证书，只能打开一个网址
                client = new AsyncHttpClient(schemeRegistry);
            } else {
                client = new AsyncHttpClient();
            }

            client.setTimeout(10 * 1000);              //设置超时时间10s
            SPCookieUtils.setCookie(client);           //保存cookie
            if (params == null) params = new RequestParams();
            if (SPMobileApplication.getInstance().isLogined()) {
                SPUser user = SPMobileApplication.getInstance().getLoginUser();
                params.put("user_id", user.getUserID());
                if (!SPStringUtils.isEmpty(user.getToken())) params.put("token", user.getToken());
            }
            if (SPMobileApplication.getInstance().getDeviceId() != null) {
                String imei = SPMobileApplication.getInstance().getDeviceId();
                params.put("unique_id", imei);
            }

            configSign(params, url);
            client.post(url, params, responseHandler);
            SMobileLog.i("REQUEST_URL", "POST:" + url + "&" + params);
        } catch (Exception e) {
            e.printStackTrace();
            responseHandler.onFailure(-1, new Header[]{}, new Throwable(e.getMessage()), new JSONObject());
        }
    }

    /**
     * 根据控制器和action组装请求URL
     */
    public static String getRequestUrl(String c, String action) {
        return SPMobileConstants.BASE_URL + "&c=" + c + "&a=" + action;
    }

    public static String getRequestUrl(String m, String c, String a) {
        return SPMobileConstants.BASE_URL_PREFIX + m + "&c=" + c + "&a=" + a;
    }

    /**
     * 每一个访问接口都要调用改函数进行签名
     */
    private static void configSign(RequestParams params, String url) {
        long locaTime = SPCommonUtils.getCurrentTime();
        String time = String.valueOf(locaTime);
        Map<String, String> paramsMap = convertRequestParamsToMap(params);
        String signFmt = SPCommonUtils.signParameter(paramsMap, time, SPMobileConstants.SP_SIGN_PRIVATGE_KEY, url);
        params.put("sign", signFmt);
        params.put("time", time);
    }

    private static Map<String, String> convertRequestParamsToMap(RequestParams params) {
        Map<String, String> paramsMap = new HashMap<>();
        if (params != null) {
            try {
                String[] items = params.toString().split("&");
                if (items != null) {
                    for (String keyValue : items) {
                        String[] keyValues = keyValue.split("=");
                        if (keyValues != null && keyValues.length == 2)
                            paramsMap.put(keyValues[0], keyValues[1]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paramsMap;
    }

    public static List<String> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String item = jsonArray.getString(i);
            itemList.add(item);
        }
        return itemList;
    }

    public static boolean isHttps(String url) {
        if (url.split(":")[0].equals("http")) {
            return false;
        } else {
            return true;
        }

    }

}
