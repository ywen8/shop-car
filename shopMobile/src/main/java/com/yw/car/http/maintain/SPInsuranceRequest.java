package com.yw.car.http.maintain;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPMobileHttptRequest;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.model.car.SPInsurance;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yw on 2018/2/27.
 */

public class SPInsuranceRequest {
    private static SPCommonListModel spCommonListModel = new SPCommonListModel();

    public static void takeInurance(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("index", "carInsurance");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.spInsurances = SPJsonUtil.fromJsonArrayToList(resultJson, SPInsurance.class);
                        spSuccessListener.onRespone("success", spCommonListModel);
                    } else {
                        spFailuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
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

    public static void comitBaoJia(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("user", "addInsurance");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
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
                } catch (Exception e) {
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
