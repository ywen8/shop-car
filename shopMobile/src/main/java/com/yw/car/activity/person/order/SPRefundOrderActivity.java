/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 订单列表
 *
 * @version V1.0
 */
package com.yw.car.activity.person.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.SPOrderBaseActivity;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPPersonRequest;
import com.yw.car.model.SPProduct;
import com.yw.car.model.order.SPOrder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zw on 2018/4/23
 */
@EActivity(R.layout.refund_order)
public class SPRefundOrderActivity extends SPOrderBaseActivity implements View.OnClickListener {

    @ViewById(R.id.refund_reason_rl)
    RelativeLayout refundReasonRl;

    @ViewById(R.id.refund_reason)
    TextView refundReason;

    @ViewById(R.id.refund_contact)
    EditText refundContact;

    @ViewById(R.id.refund_mobile)
    EditText refundMobile;

    @ViewById(R.id.refund_btn)
    Button refundBtn;

    private SPOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "取消订单");
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
        refundReasonRl.setOnClickListener(this);
        refundBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            order = (SPOrder) getIntent().getSerializableExtra("order");
            refundContact.setText(order.getConsignee());
            refundMobile.setText(order.getMobile());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refund_reason_rl:
                selectReason();
                break;
            case R.id.refund_btn:
                refund();
                break;
        }
    }

    private void selectReason() {
        final String[] items = getResources().getStringArray(R.array.refund_reason);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择原因");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                refundReason.setText(items[item]);
            }
        });
        builder.show();
    }

    private void refund() {
        if (refundReason.getText().toString().trim().isEmpty()) {
            showToast("请选择退款原因");
            return;
        }
        if (refundContact.getText().toString().trim().isEmpty()) {
            showToast("请填写联系人");
            return;
        }
        if (refundMobile.getText().toString().trim().isEmpty()) {
            showToast("请填写手机号码");
            return;
        }
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.put("order_id", order.getOrderID());
        params.put("user_note", refundReason.getText().toString());
        params.put("consignee", refundContact.getText().toString());
        params.put("mobile", refundMobile.getText().toString());
        SPPersonRequest.cancelOrder(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                Intent intent = new Intent(SPMobileConstants.ACTION_ORDER_CHANGE);
                sendBroadcast(intent);
                finish();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    @Override
    public void checkReturn(SPProduct product) {

    }

    @Override
    public void buyAgain(SPProduct product) {

    }
}