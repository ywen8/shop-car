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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description:确认订单
 *
 * @version V1.0
 */
package com.yw.car.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.activity.common.SPPayListActivity_;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.shop.SPShopRequest;
import com.yw.car.model.SPProduct;
import com.yw.car.model.order.SPOrder;
import com.yw.car.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(com.yw.car.R.layout.order_confirm_virtual_order)
public class SPConfirmVirtualOrderActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(com.yw.car.R.id.phone_edtv)
    EditText phoneEdtv;

    @ViewById(com.yw.car.R.id.message_edtv)
    EditText messageEdtv;

    @ViewById(com.yw.car.R.id.goodImg)
    ImageView goodImg;

    @ViewById(com.yw.car.R.id.goodName)
    TextView goodName;

    @ViewById(com.yw.car.R.id.goodPrice)
    TextView goodPrice;

    @ViewById(com.yw.car.R.id.goodNum)
    TextView goodNum;

    @ViewById(com.yw.car.R.id.totalFee)
    TextView totalFee;

    @ViewById(com.yw.car.R.id.pay_btn)
    Button payBtn;

    private int num;
    private int itemId;
    private SPProduct product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(com.yw.car.R.string.title_confirm_order));
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
        payBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent == null) return;
        product = (SPProduct) intent.getSerializableExtra("good");
        num = intent.getIntExtra("num", 1);
        itemId = intent.getIntExtra("itemId", 0);
        refreshView();
    }

    private void refreshView() {
        String imgUrl = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());
        Glide.with(this).load(imgUrl).asBitmap().fitCenter().placeholder(com.yw.car.R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(goodImg);
        goodName.setText(product.getGoodsName());
        String singlePrice = SPStringUtils.isEmpty(product.getShopPrice()) ? product.getGoodsPrice() : product.getShopPrice();
        goodPrice.setText("￥" + singlePrice);
        goodNum.setText("x" + num);
        double totalPrice = Double.parseDouble(singlePrice) * num;
        totalFee.setText("支付金额:￥" + totalPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.yw.car.R.id.pay_btn:
                orderCommint();
                break;
        }
    }

    /**
     * 提交订单
     */
    public void orderCommint() {
        if (phoneEdtv.getText().toString().trim().isEmpty()) {
            showToast("请填写手机号");
            return;
        }
        if (!SPUtils.isPhoneLegal(phoneEdtv.getText().toString())) {
            showToast("手机号格式错误");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("goods_id", product.getGoodsID());
        params.put("item_id", itemId);
        params.put("goods_num", num);
        params.put("user_note", messageEdtv.getText().toString());
        params.put("mobile", phoneEdtv.getText().toString());
        showLoadingSmallToast();
        SPShopRequest.submitOrder2(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                String masterOrderSN = (String) response;
                startUpPay(masterOrderSN);
            }
        }, new SPFailuredListener(SPConfirmVirtualOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    /**
     * 启动支付页面支付
     */
    public void startUpPay(final String masterOrderSN) {
        SPShopRequest.getOrderAmountWithMasterOrderSN(masterOrderSN, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null && Double.valueOf(response.toString()) > 0) {
                    String amount = response.toString();
                    SPOrder order = new SPOrder(masterOrderSN, amount, true);
                    gotoPay(order);      //进入支付页面支付
                }
            }
        }, new SPFailuredListener(SPConfirmVirtualOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    /**
     * 进入支付页面支付
     */
    public void gotoPay(SPOrder order) {
        Intent payIntent = new Intent(this, SPPayListActivity_.class);
        payIntent.putExtra("order", order);
        startActivity(payIntent);
        this.finish();
    }

}
