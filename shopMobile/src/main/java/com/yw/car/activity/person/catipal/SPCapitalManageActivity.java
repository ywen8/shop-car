/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: Ben  wangqh01292@163.com
 * Date: @date 2017-4-25
 * Description: 资金管理
 *
 * @version V1.0
 */
package com.yw.car.activity.person.catipal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.activity.common.SPCommonWebActivity;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.person.SPUser;
import com.yw.car.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(com.yw.car.R.layout.activity_spcapital_manage)
public class SPCapitalManageActivity extends SPBaseActivity {

    @ViewById(com.yw.car.R.id.txt_frozen_balance)
    TextView txt_frozen_balance;

    @ViewById(com.yw.car.R.id.txt_balance_value)
    TextView txt_balance_value;

    String title = "";
    String webUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void initData() {
        SPUser user = SPMobileApplication.getInstance().getLoginUser();
        txt_balance_value.setText(user.getUserMoney());
        txt_frozen_balance.setText(getString(com.yw.car.R.string.capital_frozen_balance, user.getFrozenMoney()));
    }

    public void onTopUpClick(View view) {
        startActivity(new Intent(this, SPTopUpActivity_.class));
    }

    public void onWithdrawClick(View view) {
        startActivity(new Intent(this, SPWithdrawActivity_.class));
    }

    public void onBackBtnClick(View view) {
        finish();
    }

    @Click({com.yw.car.R.id.capital_balance_detail, com.yw.car.R.id.capital_point_detail, com.yw.car.R.id.capital_recharge_history, com.yw.car.R.id.capital_withdraw_history})
    void viewClicked(View v) {
        switch (v.getId()) {
            case com.yw.car.R.id.capital_balance_detail:        //余额明细
                title = getResources().getString(com.yw.car.R.string.capital_account_detail);
                webUrl = SPMobileConstants.URL_ACCOUNT_HOSTORY;
                break;
            case com.yw.car.R.id.capital_point_detail:          //金豆明细
                title = getResources().getString(com.yw.car.R.string.capital_point_detail);
                webUrl = SPMobileConstants.URL_POINT_HISTORY;
                break;
            case com.yw.car.R.id.capital_recharge_history:      //充值记录
                title = getResources().getString(com.yw.car.R.string.capital_recharge_history);
                webUrl = SPMobileConstants.URL_RECHARGE_HISTORY;
                break;
            case com.yw.car.R.id.capital_withdraw_history:      //提现记录
                title = getResources().getString(com.yw.car.R.string.capital_withdraw_history);
                webUrl = SPMobileConstants.URL_WITHDRAW_HISTORY;
                break;
        }
        checkTokenPastDue();
    }

    private void checkTokenPastDue() {
        SPUserRequest.getTokenStatus(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                toExchange();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                if (SPUtils.isTokenExpire(errorCode))
                    toLoginPage();      //跳转到登录页
            }
        });
    }

    private void toExchange() {
        Intent intent = new Intent(this, SPCommonWebActivity.class);
        intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, title);
        intent.putExtra(SPMobileConstants.KEY_WEB_URL, webUrl);
        startActivity(intent);
    }

}
