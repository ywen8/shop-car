package com.yw.car.http.maintain;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPMobileHttptRequest;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.model.car.SPCarBrand;
import com.yw.car.model.car.SPMaintain;
import com.yw.car.model.car.SPMotorcycle;
import com.yw.car.model.car.SPRecord;
import com.yw.car.model.car.SPTown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yw on 2018/2/9.
 */

public class SPMaintainRequest {
    private static SPCommonListModel spCommonListModel = new SPCommonListModel();

    public static void getCarBrand(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("index", "carBrand");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.carbrands = SPJsonUtil.fromJsonArrayToList(resultJson, SPCarBrand.class);
                        spSuccessListener.onRespone("success", spCommonListModel);
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

    public static void getCarBrandModel(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("index", "carBrandModel");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.carmodels = SPJsonUtil.fromJsonArrayToList(resultJson, SPMotorcycle.class);
                        spSuccessListener.onRespone("success", spCommonListModel);
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

    public static void getCarService(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("index", "infoIndex");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.maintains = SPJsonUtil.fromJsonArrayToList(resultJson, SPMaintain.class);
                        spSuccessListener.onRespone("success", spCommonListModel);
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

    public static void addbaoxian(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("user", "addaintain");
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

    public static void getArea(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_WEIZHANGSHENGFEN;
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    JSONObject jsonObject = (JSONObject) resultJson.get(0);
                    if (jsonObject != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("citys");
                        spCommonListModel.towns = SPJsonUtil.fromJsonArrayToList(jsonArray, SPTown.class);
                        spSuccessListener.onRespone("success", spCommonListModel);
                    } else {
                        spFailuredListener.onRespone(msg, -1);
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

    public static void searchWeiZhang(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_SEAECH;
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        JSONArray jsonArray = resultJson.getJSONArray("lists");
                        spCommonListModel.records = SPJsonUtil.fromJsonArrayToList(jsonArray, SPRecord.class);
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
}
