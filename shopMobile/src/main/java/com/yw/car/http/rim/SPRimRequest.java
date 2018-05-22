package com.yw.car.http.rim;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPMobileHttptRequest;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.model.SPHomeBanners;
import com.yw.car.model.SPRimHeadlin;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yw on 2018/2/9.
 */

public class SPRimRequest {

    public static void getRimData(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("index", "aroundPage");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);
                    SPCommonListModel commonModel = new SPCommonListModel();
                    if (resultJson != null) {
                        if (!resultJson.isNull("banner")) {          //首页轮播广告
                            JSONArray bannerArray = resultJson.getJSONArray("banner");
                            if (bannerArray != null)
                                commonModel.banners = SPJsonUtil.fromJsonArrayToList(bannerArray, SPHomeBanners.class);
                        }
                        if (!resultJson.isNull("ad1")) {          //首页其他广告
                            JSONObject adObject = resultJson.getJSONObject("ad1");
                            if (adObject != null)
                                commonModel.ad = SPJsonUtil.fromJsonToModel(adObject, SPHomeBanners.class);
                        }
                        if (!resultJson.isNull("ad2")) {          //首页其他广告
                            JSONObject adObject = resultJson.getJSONObject("ad2");
                            if (adObject != null)
                                commonModel.ad1 = SPJsonUtil.fromJsonToModel(adObject, SPHomeBanners.class);
                        }
                        if (!resultJson.isNull("newAds")) {
                            JSONObject newObject = resultJson.getJSONObject("newAds");
                            if (newObject != null) {
                                commonModel.newAds = SPJsonUtil.fromJsonToModel(newObject, SPRimHeadlin.class);
                            }
                        }

                        spSuccessListener.onRespone("success", commonModel);
                    } else {
                        spFailuredListener.onRespone(msg, -1);
                    }

                } catch (Exception e) {
                    spFailuredListener.onRespone(e.getMessage(), -1);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                spFailuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                spFailuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                spFailuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });

    }
}
