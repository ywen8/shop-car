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
 * Description: 订单详情
 *
 * @version V1.0
 */
package com.yw.car.activity.person.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.activity.common.SPCommonWebActivity;
import com.yw.car.activity.common.SPOrderBaseActivity;
import com.yw.car.activity.shop.SPCommodityEvaluationActivity_;
import com.yw.car.activity.shop.SPProductDetailActivity_;
import com.yw.car.activity.shop.SPProductShowListActivity_;
import com.yw.car.activity.shop.SPStoreHomeActivity_;
import com.yw.car.adapter.SPOrderDetailAdaper;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPPersonRequest;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.OrderButtonModel;
import com.yw.car.model.SPCommentData;
import com.yw.car.model.SPProduct;
import com.yw.car.model.order.SPOrder;
import com.yw.car.model.order.SPVrorder;
import com.yw.car.utils.SPConfirmDialog;
import com.yw.car.utils.SPUtils;
import com.yw.car.widget.NoScrollListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.yw.car.activity.shop.SPGroupConfirmOrderActivity_;
//import com.yw.car.activity.shop.SPGroupOrderDetailActivity_;
//import com.yw.car.activity.shop.SPGroupProductDetailActivity_;

@EActivity(R.layout.order_details)
public class SPOrderDetailActivity extends SPOrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener,
        SPOrderDetailAdaper.OnProductClickListener {

    private SPOrder mOrder;                         //订单
    private String mOrderId;                        //订单编号
    private String detailAddress;                   //收货地址详情
    private boolean isGroup = false;
    private boolean isVirtual = false;
    private CommentChangeReceiver mReceiver;

    @ViewById(R.id.order_detail_rl)
    RelativeLayout orderDetailRl;

    @ViewById(R.id.confirm_scrollv)
    ScrollView scrollView;

    @ViewById(R.id.address_rl)
    RelativeLayout addressRl;

    @ViewById(R.id.order_consignee_txtv)
    TextView consigneeTxtv;

    @ViewById(R.id.order_address_txtv)
    TextView addressTxtv;

    @ViewById(R.id.product_list_layout)
    NoScrollListView mProductListVeiw;

    @ViewById(R.id.look_group_btn)
    Button lookGroupBtn;

    @ViewById(R.id.order_button_gallery_lyaout)
    LinearLayout mButtonGallery;

    @ViewById(R.id.fee_goodsfee_txtv)
    TextView feeGoodsFeeTxtv;

    @ViewById(R.id.title_shopping_txtv)
    TextView titleShoppingTxtv;

    @ViewById(R.id.fee_shopping_txtv)
    TextView feeShoppingTxtv;

    @ViewById(R.id.store_name_txtv)
    TextView storeNameTxtv;

    @ViewById(R.id.title_coupon_txtv)
    TextView titleCouponTxtv;

    @ViewById(R.id.fee_coupon_txtv)
    TextView feeCouponTxtv;

    @ViewById(R.id.title_point_txtv)
    TextView titlePointTxtv;

    @ViewById(R.id.fee_prom_txtv)
    TextView feePromTxtv;

    @ViewById(R.id.fee_point_txtv)
    TextView feePointTxtv;

    @ViewById(R.id.title_getphone_txtv)
    TextView titleGetphoneTxtv;

    @ViewById(R.id.phone_number_txtv)
    TextView phoneNumberTxtv;

    @ViewById(R.id.code_ll)
    LinearLayout codeLl;

    @ViewById(R.id.title_balance_txtv)
    TextView titleBalanceTxtv;

    @ViewById(R.id.fee_balance_txtv)
    TextView feeBalanceTxtv;

    @ViewById(R.id.fee_amount_txtv)
    TextView feeAmountTxtv;

    @ViewById(R.id.fee_ordersn_txtv)
    TextView orderSnTxtv;

    @ViewById(R.id.order_status_txtv)
    TextView orderStatusTxtv;

    @ViewById(R.id.order_address_arrow_imgv)
    ImageView orderAddressArrowImgv;

    @ViewById(R.id.fee_addtime_txtv)
    TextView buyTimeTxtv;

    @ViewById(R.id.fee_getpoint_txtv)
    TextView giveIntegralTxtv;

    @ViewById(R.id.fee_paytype_txtv)
    TextView payTypeTxtv;

    @ViewById(R.id.fee_invoce_txtv)
    TextView orderInvoceTv;

    @ViewById(R.id.title_code_txtv)
    TextView titleCodeTv;

    @ViewById(R.id.fee_code_txtv)
    TextView invoceCodeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_detail));
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isVirtual = savedInstanceState.getBoolean("isVirtual");
            isGroup = savedInstanceState.getBoolean("isGroup");
            mOrderId = savedInstanceState.getString("orderId");
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(SPMobileConstants.ACTION_ORDER_CHANGE);
        filter.addAction(SPMobileConstants.ACTION_COMMENT_CHANGE);
        mReceiver = new CommentChangeReceiver();
        registerReceiver(mReceiver, filter);
    }

    @AfterViews
    public void init() {
        super.init();
        if ((getIntent() == null || getIntent().getStringExtra("orderId") == null)) {
            boolean hasOrderId = (mOrderId = SPMobileApplication.getInstance().orderId) != null;
            if (!hasOrderId) {
                showToast(getString(R.string.toast_no_ordersn_data));
                finish();
                return;
            }
        } else {
            isVirtual = getIntent().getBooleanExtra("isVirtual", false);
            isGroup = getIntent().getBooleanExtra("isGroup", false);
            mOrderId = getIntent().getStringExtra("orderId");
        }
        refreshData();
    }

    @Override
    public void initSubViews() {
        orderDetailRl.setVisibility(View.GONE);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        orderAddressArrowImgv.setVisibility(View.GONE);
    }

    public void refreshData() {
        showLoadingSmallToast();
        SPPersonRequest.getOrderDetailByID(mOrderId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mOrder = (SPOrder) response;
                if (mOrder != null) {
                    dealModel();
                    refreshView();
                    orderDetailRl.setVisibility(View.VISIBLE);
                }
            }
        }, new SPFailuredListener(SPOrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
                orderDetailRl.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 处理服务器获取的数据
     */
    public void dealModel() {
        List<SPProduct> products = mOrder.getProducts();
        if (products != null && products.size() > 0) {
            for (SPProduct product : products) {
                product.setOrderStatusCode(mOrder.getOrderStatusCode());
                product.setOrderID(mOrder.getOrderID());
                product.setOrderSN(mOrder.getOrderSN());
            }
        }
        detailAddress = mOrder.getTotalAddress();
    }

    public void refreshView() {
        if (isVirtual) {      //是虚拟订单
            titleShoppingTxtv.setVisibility(View.GONE);
            feeShoppingTxtv.setVisibility(View.GONE);
            titleCouponTxtv.setVisibility(View.GONE);
            feeCouponTxtv.setVisibility(View.GONE);
            titlePointTxtv.setVisibility(View.GONE);
            feePointTxtv.setVisibility(View.GONE);
            titleBalanceTxtv.setVisibility(View.GONE);
            feeBalanceTxtv.setVisibility(View.GONE);
            addressRl.setVisibility(View.GONE);
        } else {      //不是虚拟订单
            if (isGroup) {
                lookGroupBtn.setVisibility(View.VISIBLE);
                if (mOrder.getPayStatus() == 0)
                    addressRl.setVisibility(View.GONE);
                else
                    addressRl.setVisibility(View.VISIBLE);
            } else {
                addressRl.setVisibility(View.VISIBLE);
            }
            titleShoppingTxtv.setVisibility(View.VISIBLE);
            feeShoppingTxtv.setVisibility(View.VISIBLE);
            titleCouponTxtv.setVisibility(View.VISIBLE);
            feeCouponTxtv.setVisibility(View.VISIBLE);
            titlePointTxtv.setVisibility(View.VISIBLE);
            feePointTxtv.setVisibility(View.VISIBLE);
            titleBalanceTxtv.setVisibility(View.VISIBLE);
            feeBalanceTxtv.setVisibility(View.VISIBLE);
        }
        consigneeTxtv.setText(mOrder.getConsignee() + "  " + mOrder.getMobile());
        addressTxtv.setText(detailAddress);
        addressTxtv.setVisibility(View.VISIBLE);
        orderSnTxtv.setText(mOrder.getOrderSN());
        String payname = SPStringUtils.isEmpty(mOrder.getPayName()) ? "无" : mOrder.getPayName();
        payTypeTxtv.setText(payname);
        if (mOrder.getOrderType() == 5) {
            phoneNumberTxtv.setText(mOrder.getMobile());
            if (mOrder.getVrorders() != null && mOrder.getVrorders().size() > 0) {
                codeLl.removeAllViews();
                for (int i = 0; i < mOrder.getVrorders().size(); i++) {
                    View vrorderView = LayoutInflater.from(this).inflate(R.layout.virtual_order_vrorder, null);
                    TextView codeTxt = (TextView) vrorderView.findViewById(R.id.title_getcode_txtv);
                    TextView codeStatusTxt = (TextView) vrorderView.findViewById(R.id.title_getcode_status);
                    SPVrorder vrorder = mOrder.getVrorders().get(i);
                    if (vrorder.getState() == 0) {
                        codeTxt.setText(vrorder.getCode());
                        codeStatusTxt.setText("未使用,有效期至" + getDate(vrorder.getIndate()));
                    } else if (vrorder.getState() == 1) {
                        codeTxt.setText(vrorder.getCode());
                        codeStatusTxt.setText("已使用,有效期至" + getDate(vrorder.getIndate()));
                    } else if (vrorder.getState() == 2) {
                        codeTxt.setText(vrorder.getCode());
                        codeStatusTxt.setText("已过期,有效期至" + getDate(vrorder.getIndate()));
                    }
                    codeLl.addView(vrorderView);
                }
            } else {
                codeLl.setVisibility(View.GONE);
            }
        } else {
            titleGetphoneTxtv.setVisibility(View.GONE);
            phoneNumberTxtv.setVisibility(View.GONE);
            codeLl.setVisibility(View.GONE);
        }
        if (!SPStringUtils.isEmpty(mOrder.getOrderStatusDesc()))
            orderStatusTxtv.setText(mOrder.getOrderStatusDesc());
        if (mOrder != null && !SPStringUtils.isEmpty(mOrder.getStore().getStoreName()))
            storeNameTxtv.setText(mOrder.getStore().getStoreName());
        String goodsPrice = SPStringUtils.isEmpty(mOrder.getGoodsPrice()) ? "0.00" : mOrder.getGoodsPrice();
        feeGoodsFeeTxtv.setText("¥" + goodsPrice);           //商品金额
        String shippingPrice = SPStringUtils.isEmpty(mOrder.getShippingPrice()) ? "0.00" : mOrder.getShippingPrice();
        feeShoppingTxtv.setText("¥" + shippingPrice);          //运费
        String couponPrice = SPStringUtils.isEmpty(mOrder.getCouponPrice()) ? "0.00" : mOrder.getCouponPrice();
        feeCouponTxtv.setText("¥" + couponPrice);           //优惠券抵扣
        String integralMoney = SPStringUtils.isEmpty(mOrder.getIntegralMoney()) ? "0.00" : mOrder.getIntegralMoney();
        feePointTxtv.setText("¥" + integralMoney);            //积分抵扣
        String orderPromAmount = SPStringUtils.isEmpty(mOrder.getOrderPromAmount()) ? "0.00" : mOrder.getOrderPromAmount();
        feePromTxtv.setText("¥" + orderPromAmount);            //活动优惠
        String userMoney = SPStringUtils.isEmpty(mOrder.getUserMoney()) ? "0.00" : mOrder.getUserMoney();
        feeBalanceTxtv.setText("¥" + userMoney);           //余额支付
        String invoce = SPStringUtils.isEmpty(mOrder.getInvoiceTitle()) ? "无" : mOrder.getInvoiceTitle();
        orderInvoceTv.setText(invoce);            //发票抬头
        if (!SPStringUtils.isEmpty(mOrder.getTaxpayer())) {            //纳税人识别号
            invoceCodeTv.setText(mOrder.getTaxpayer());
            titleCodeTv.setVisibility(View.VISIBLE);
            invoceCodeTv.setVisibility(View.VISIBLE);
        } else {
            titleCodeTv.setVisibility(View.GONE);
            invoceCodeTv.setVisibility(View.GONE);
        }
        if (!SPStringUtils.isEmpty(mOrder.getOrderAmount())) {
            String payablesFmt = "¥" + mOrder.getOrderAmount();
            int startIndex = 4;
            int endIndex = payablesFmt.length();
            SpannableString payablesSpanStr = new SpannableString(payablesFmt);
            payablesSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);         //设置区域内文字颜色为洋红色
            feeAmountTxtv.setText(payablesSpanStr);
        }
        if (!SPStringUtils.isEmpty(mOrder.getAddTime())) {         //下单时间
            String buyTime = SPUtils.convertFullTimeFromPhpTime(Long.valueOf(mOrder.getAddTime()));
            buyTimeTxtv.setText(buyTime);
        }
        List<OrderButtonModel> buttonModels = getOrderButtonModelByOrder(mOrder);
        buildProductButtonGallery(mButtonGallery, buttonModels);
        int totalGiveIntegral = 0;
        List<SPProduct> mProducts = mOrder.getProducts();
        for (SPProduct product : mProducts)
            totalGiveIntegral += product.getGiveIntegral();
        giveIntegralTxtv.setText(String.valueOf(totalGiveIntegral));
        SPOrderDetailAdaper mAdapter = new SPOrderDetailAdaper(this, mOrder, mProducts, this);
        mProductListVeiw.setAdapter(mAdapter);
    }

    public String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time * 1000));
    }

    @Click({R.id.store_name_txtv, R.id.look_group_btn, R.id.contact_customer_btn, R.id.call_phone_btn})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.store_name_txtv:        //进入店铺
                Intent intent1 = new Intent(this, SPStoreHomeActivity_.class);
                intent1.putExtra("storeId", mOrder.getStore().getStoreId());
                startActivity(intent1);
                break;
            case R.id.look_group_btn:         //查看拼单详情
//                Intent intent = new Intent(this, SPGroupOrderDetailActivity_.class);
//                if (mOrder.getFounder() != null)
//                    intent.putExtra("found_id", mOrder.getFounder().getFoundId());
//                startActivity(intent);
                break;
            case R.id.contact_customer_btn:        //联系卖家
                if (!SPStringUtils.isEmpty(mOrder.getStore().getStoreQQ()))
                    connectCustomer(mOrder.getStore().getStoreQQ());
                else
                    showToast("暂无卖家联系方式");
                break;
            case R.id.call_phone_btn:        //联系客服
                if (!SPStringUtils.isEmpty(mOrder.getStore().getSellerTel())) {
                    Intent intent2 = new Intent(Intent.ACTION_DIAL);
                    intent2.setData(Uri.parse("tel:" + mOrder.getStore().getSellerTel()));
                    if (intent2.resolveActivity(getPackageManager()) != null)
                        startActivity(intent2);
                } else {
                    showToast("暂无卖家联系电话");
                }
                break;
        }
    }

    private void buildProductButtonGallery(LinearLayout gallery, List<OrderButtonModel> buttonModels) {
        gallery.removeAllViews();
        if (buttonModels != null && buttonModels.size() > 0) {
            for (int i = 0; i < buttonModels.size(); i++) {
                OrderButtonModel buttonModel = buttonModels.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.order_button_gallery_item, gallery, false);
                Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
                button.setText(buttonModel.getText());
                button.setTag(buttonModel.getTag());
                if (buttonModel.isLight()) {
                    button.setBackgroundResource(R.drawable.button_border_r_selector);
                    button.setTextColor(getResources().getColor(R.color.light_red));
                } else {
                    button.setBackgroundResource(R.drawable.button_border_w_selector);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
                button.setOnClickListener(orderButtonClickListener);
                gallery.addView(view);
            }
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.order_button_gallery_item, gallery, false);
            Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
            button.setText("联系客服");
            button.setTag(SPMobileConstants.tagCustomer);
            button.setBackgroundResource(R.drawable.button_border_w_selector);
            button.setTextColor(getResources().getColor(R.color.black));
            button.setOnClickListener(orderButtonClickListener);
            gallery.addView(view);
        }
    }

    public List<OrderButtonModel> getOrderButtonModelByOrder(SPOrder order) {
        List<OrderButtonModel> buttons = new ArrayList<>();
        if (order.getButtom().getPayBtn() == 1) {       //显示支付按钮
            OrderButtonModel payModel = new OrderButtonModel("支付", SPMobileConstants.tagPay, true);
            buttons.add(payModel);
        }
        if (order.getButtom().getCancelBtn() == 1) {      //取消订单按钮
            OrderButtonModel cancelModel = new OrderButtonModel("取消订单", SPMobileConstants.tagCancel, false);
            buttons.add(cancelModel);
        }
        if (order.getButtom().getReceiveBtn() == 1) {      //确认收货按钮
            OrderButtonModel cancelModel = new OrderButtonModel("确认收货", SPMobileConstants.tagReceive, true);
            buttons.add(cancelModel);
        }
        if (order.getButtom().getShippingBtn() == 1) {      //查看物流按钮
            OrderButtonModel cancelModel = new OrderButtonModel("查看物流", SPMobileConstants.tagShopping, true);
            buttons.add(cancelModel);
        }
        if (order.getButtom().getCancelInfoBtn() == 1) {
            OrderButtonModel cancelModel = new OrderButtonModel("取消详情", SPMobileConstants.tagCancelInfo, true);
            buttons.add(cancelModel);
        }
        return buttons;
    }

    public View.OnClickListener orderButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_index_gallery_item_rlayout || v.getId() == R.id.order_product_count_txtv) {
                Intent showIntent = new Intent(SPOrderDetailActivity.this, SPProductShowListActivity_.class);
                SPMobileApplication.getInstance().list = mOrder.getProducts();
                startActivity(showIntent);
            } else if (v.getId() == R.id.id_index_gallery_item_button) {
                int tag = Integer.valueOf(v.getTag().toString());
                switch (tag) {
                    case SPMobileConstants.tagPay:        //立即支付
                        if (isGroup) {
//                            Intent intent = new Intent(SPOrderDetailActivity.this, SPGroupConfirmOrderActivity_.class);
//                            intent.putExtra("group_pay", true);
//                            intent.putExtra("order_sn", mOrder.getOrderSN());
//                            startActivity(intent);
                        } else {
                            SPMobileApplication.getInstance().fellBack = 1;
                            gotoPay(mOrder);
                        }
                        break;
                    case SPMobileConstants.tagCancel:        //取消订单
                        if (mOrder.getPayStatus() == 1) {      //取消已支付订单
                            Intent refundIntent = new Intent(SPOrderDetailActivity.this, SPRefundOrderActivity_.class);
                            refundIntent.putExtra("order", mOrder);
                            startActivity(refundIntent);
                        } else {      //取消未支付订单
                            cancelOrder();
                        }
                        break;
                    case SPMobileConstants.tagCustomer:        //联系客服
                        connectCustomer();
                        break;
                    case SPMobileConstants.tagShopping:        //查看物流
                        lookShopping(mOrder);
                        break;
                    case SPMobileConstants.tagCancelInfo:        //取消详情
                        checkTokenPastDue();
                        break;
                    case SPMobileConstants.tagReceive:        //确认收货
                        confirmReceive(mOrder);
                        break;
                }
            }
        }
    };

    //校验token是否过期
    public void checkTokenPastDue() {
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

    public void toExchange() {
        String url = String.format(SPMobileConstants.URL_ORDER_REFUND_DETAIL, mOrder.getOrderID());
        Intent intent = new Intent(this, SPCommonWebActivity.class);
        intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, "取消详情");
        intent.putExtra(SPMobileConstants.KEY_WEB_URL, url);
        startActivity(intent);
    }

    /**
     * 确认收货
     */
    public void confirmReceive(SPOrder order) {
        showLoadingSmallToast();
        confirmOrderWithOrderID(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                refreshData();
                Intent intent = new Intent(SPMobileConstants.ACTION_ORDER_CHANGE);
                sendBroadcast(intent);
            }
        }, new SPFailuredListener(SPOrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    /**
     * 取消订单
     */
    public void cancelOrder() {
        showConfirmDialog("确定取消订单?", "订单提醒", this, SPMobileConstants.tagCancel);
    }

    @Override
    public void clickOk(int actionType) {
        if (actionType == SPMobileConstants.tagCancel) {
            showLoadingSmallToast();
            cancelOrder(mOrder.getOrderID(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    showSuccessToast(msg);
                    refreshData();
                    Intent intent = new Intent(SPMobileConstants.ACTION_ORDER_CHANGE);
                    sendBroadcast(intent);
                }
            }, new SPFailuredListener(SPOrderDetailActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        }
    }

    //申请售后
    @Override
    public void applyReturn(final SPProduct product) {
        SPPersonRequest.getApplyStatus(product.getRecId(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                int returnId = (int) response;
                if (returnId == 0)      //可以申请售后
                    afterSaleAction(product);
                else      //已经申请售后
                    gotoReturnDetail(returnId);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    //查看退款
    @Override
    public void checkReturn(SPProduct product) {
//        if (product.getReturnId() > 0) {
//            Intent intent = new Intent(SPOrderDetailActivity.this, SPCommonWebActivity.class);
//            intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, "查看退款");
//            intent.putExtra(SPMobileConstants.KEY_WEB_URL, SPMobileConstants.URL_GOODS_ORDER_CHECK);
//            intent.putExtra("returnId", product.getReturnId() + "");
//            startActivity(intent);
//        }
    }

    //去评价
    @Override
    public void commentProduct(SPProduct product) {
        SPCommentData commentData = new SPCommentData();
        commentData.setRecId(product.getRecId());
        commentData.setGoodsId(product.getGoodsID());
        commentData.setOrderId(product.getOrderID());
        commentData.setGoodsName(product.getGoodsName());
        Intent intent = new Intent(this, SPCommodityEvaluationActivity_.class);
        intent.putExtra("comment", commentData);
        startActivity(intent);
    }

    //再次购买
    @Override
    public void buyAgain(SPProduct product) {
        if (isGroup) {
//            Intent intent = new Intent(this, SPGroupProductDetailActivity_.class);
//            SPTeamFounder teamFounder = mOrder.getFounder();
//            intent.putExtra("team_id", teamFounder.getTeamId());
//            intent.putExtra("goods_id", product.getGoodsID());
//            intent.putExtra("item_id", product.getItemId() + "");
//            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SPProductDetailActivity_.class);
            intent.putExtra("goodsID", product.getGoodsID());
            intent.putExtra("itemID", product.getItemId() + "");
            startActivity(intent);
        }
    }

    /**
     * 申请售后
     */
    public void afterSaleAction(final SPProduct product) {
        Intent intent = new Intent(this, SPApplySeriverActivity_.class);
        intent.putExtra("rec_id", product.getRecId() + "");
        startActivityForResult(intent, SPMobileConstants.Result_Code_Refresh);
    }

    /**
     * 售后详情
     */
    public void gotoReturnDetail(int returnId) {
        Intent intent = new Intent(this, SPCommonWebActivity.class);
        intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, "售后详情");
        intent.putExtra(SPMobileConstants.KEY_WEB_URL, SPMobileConstants.URL_RETURN_DETAIL + "&id=" + returnId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:            //支付成功(申请售后)
                refreshData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class CommentChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_ORDER_CHANGE)
                    || intent.getAction().equals(SPMobileConstants.ACTION_COMMENT_CHANGE))
                refreshData();
        }
    }

}
