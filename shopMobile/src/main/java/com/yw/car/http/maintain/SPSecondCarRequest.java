package com.yw.car.http.maintain;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPMobileHttptRequest;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.model.car.SPAreaPrice;
import com.yw.car.model.car.SPAssessCity;
import com.yw.car.model.car.SPCheXing;
import com.yw.car.model.car.SPPin;
import com.yw.car.model.car.SPProvince;
import com.yw.car.model.car.SPSearch;
import com.yw.car.model.car.SPShengFen;
import com.yw.car.model.car.SPXiLie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yw on 2018/2/24.
 */

public class SPSecondCarRequest {
    private static SPCommonListModel spCommonListModel = new SPCommonListModel();

    public static void getshengfen(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_SHENGFEN;
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.provinces = SPJsonUtil.fromJsonArrayToList(resultJson, SPProvince.class);
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

    public static void getWeiZhang(final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_WEIZHANGSHENGFEN;
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        List<SPShengFen> shengFens = SPJsonUtil.fromJsonArrayToList(resultJson, SPShengFen.class);
                        spSuccessListener.onRespone("success", shengFens);
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

    public static void assesscity(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_CITY;
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONArray resultJson = response.getJSONArray(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.assessCities = SPJsonUtil.fromJsonArrayToList(resultJson, SPAssessCity.class);
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

    public static void brandcar(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_CARBRAND;
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);
                    if (resultJson != null) {
                        spCommonListModel.pins = SPJsonUtil.fromJsonToModel(resultJson, SPPin.class);
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

    public static void xilei(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_XILIE;
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);
                    JSONArray jsonArray = resultJson.getJSONArray("pinpai_list");
                    if (jsonArray != null) {
                        spCommonListModel.xiLies = SPJsonUtil.fromJsonArrayToList(jsonArray, SPXiLie.class);
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

    public static void chexing(RequestParams params, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_CHEXING;
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);
                    JSONArray jsonArray = resultJson.getJSONArray("data");
                    if (jsonArray != null) {
                        spCommonListModel.cheXings = SPJsonUtil.fromJsonArrayToList(jsonArray, SPCheXing.class);
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

    public static void searchAssess(RequestParams requestParams, final SPSuccessListener spSuccessListener, final SPFailuredListener spFailuredListener) {
        assert (spSuccessListener != null);
        assert (spFailuredListener != null);
        String url = SPMobileConstants.URL_SEARCH_ASSESS;
        SPMobileHttptRequest.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString("reason");
                    JSONObject resultJson = response.getJSONObject(SPMobileConstants.Response.RESULT);

                    String jsonArray = resultJson.getString("est_price_result");
                    JSONArray json = resultJson.getJSONArray("est_price_area");

                    if (jsonArray != null && json != null) {
                        SPSearch spSearch = new SPSearch();
                        List<SPAreaPrice> list = SPJsonUtil.fromJsonArrayToList(json, SPAreaPrice.class);
                        String[] strs = jsonArray.substring(0, jsonArray.length() - 1).split(",");
                        spSearch.str = strs;
                        spSearch.list = list;
                        spSuccessListener.onRespone("success", spSearch);
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

}
