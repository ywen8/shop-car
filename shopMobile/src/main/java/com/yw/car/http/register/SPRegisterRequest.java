package com.yw.car.http.register;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPMobileHttptRequest;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.model.car.SPCarInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yw on 2018/3/8.
 */

public class SPRegisterRequest {
    public static void getRimData(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("user", "updateUserCar");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int state = response.getInt("status");
                    if (state == 1) {
                        spSuccessListener.onRespone(msg, null);
                    } else {
                        spFailuredListener.onRespone(msg, -1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    spFailuredListener.onRespone(e.getMessage(), -1);
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

    public static void takeInfo(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("user", "userCar");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        SPCarInfo carInfo = SPJsonUtil.fromJsonToModel(resultJson, SPCarInfo.class);
                        spSuccessListener.onRespone("success", carInfo);
                    } else {
//                        spFailuredListener.onRespone(msg, -1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    spFailuredListener.onRespone(e.getMessage(), -1);
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
