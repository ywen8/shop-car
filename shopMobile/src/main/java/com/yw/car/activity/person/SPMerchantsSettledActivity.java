package com.yw.car.activity.person;/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月3日 下午10:04:49
 * Description: 商品收藏列表
 *
 * @version V1.0
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.person.SPUser;
import com.yw.car.widget.ProgressWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * @author zw
 */
@EActivity(R.layout.activity_spmerchants_settled)
public class SPMerchantsSettledActivity extends SPBaseActivity {

    @ViewById(R.id.webView)
    ProgressWebView webView;

    @ViewById(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true, "入驻须知");
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }
    @Override
    public void initEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUserRequest.getApply(new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        JSONObject object = (JSONObject) response;
                        Intent intent = new Intent(SPMerchantsSettledActivity.this, SPMerchantsShopActivity_.class);
                        intent.putExtra("merchantsResult", object.toString());
                        startActivity(intent);
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showFailedToast(msg);
                    }
                });
            }
        });
    }

    @Override
    public void initData() {
        String mWebUrl = SPMobileConstants.URL_NEWJOIN_AGREEMENT;
        RequestParams params = new RequestParams();
        if (SPMobileApplication.getInstance().isLogined()) {
            SPUser user = SPMobileApplication.getInstance().getLoginUser();
            params.put("user_id", user.getUserID());
            if (!SPStringUtils.isEmpty(user.getToken())) params.put("token", user.getToken());
        }
        if (SPMobileApplication.getInstance().getDeviceId() != null) {
            String imei = SPMobileApplication.getInstance().getDeviceId();
            params.put("unique_id", imei);
        }
        int idx = mWebUrl.indexOf("?");
        String connecter = "&";
        if (idx == -1) connecter = "?";
        webView.loadUrl(mWebUrl + connecter + params);
        showLoadingSmallToast();
        if (webView != null)
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    mLoadingSmallDialog.dismiss();
                }
            });
    }
}
