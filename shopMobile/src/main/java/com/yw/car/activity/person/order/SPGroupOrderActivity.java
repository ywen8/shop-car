/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: zw
 * Date: @date 2017-6-7
 * Description: 拼团订单列表
 *
 * @version V1.0
 */
package com.yw.car.activity.person.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.activity.common.SPOrderBaseActivity;
//import com.yw.car.activity.shop.SPGroupConfirmOrderActivity_;
import com.yw.car.activity.shop.SPStoreHomeActivity_;
import com.yw.car.adapter.ListDividerItemDecoration;
import com.yw.car.adapter.SPOrderListAdapter;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPPersonRequest;
import com.yw.car.model.SPProduct;
import com.yw.car.model.order.SPOrder;
import com.yw.car.utils.SPConfirmDialog;
import com.yw.car.widget.swipetoloadlayout.OnLoadMoreListener;
import com.yw.car.widget.swipetoloadlayout.OnRefreshListener;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_spgroup_order)
public class SPGroupOrderActivity extends SPOrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener, OnRefreshListener,
        OnLoadMoreListener, View.OnClickListener {

    /*code_15拼团模块逻辑代码*/
    @ViewById(R.id.all_order_txt)
    TextView allOrder;      //全部订单

    @ViewById(R.id.wait_pay_txt)
    TextView waitPay;       //待付款

    @ViewById(R.id.wait_group_txt)
    TextView waitGroup;       //待成团

    @ViewById(R.id.wait_send_txt)
    TextView waitSend;       //待发货

    @ViewById(R.id.wait_recevice_txt)
    TextView waitRecevice;       //待收货

    @ViewById(R.id.has_done_txt)
    TextView hasDone;       //已完成

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;
    /*code_15拼团模块逻辑代码*/

    private String type;                      //订单类型
    private int mPageIndex = 1;
    private List<SPOrder> orders;
    private SPOrder currentSelectOrder;       //选中的订单
    private SPOrderListAdapter mAdapter;
    private OrderChangeReceiver mReceiver;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /*code_15拼团模块逻辑代码*/
            switch (msg.what) {
                case SPMobileConstants.tagPay:       //支付订单
//                    SPOrder order = (SPOrder) msg.obj;
//                    Intent intent = new Intent(SPGroupOrderActivity.this, SPGroupConfirmOrderActivity_.class);
//                    intent.putExtra("group_pay", true);
//                    intent.putExtra("order_sn", order.getOrderSN());
//                    startActivity(intent);
                    break;
                case SPMobileConstants.tagCancel:       //取消订单
                    currentSelectOrder = (SPOrder) msg.obj;
                    cancelOrder();
                    break;
                case SPMobileConstants.tagShopping:       //查看物流
                    lookShopping((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagReceive:       //确认收货
                    confirmReceive((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagReturn:
                    break;
                case SPMobileConstants.tagCustomer:       //联系客服
                    connectSaller((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:       //订单详情
                    Intent detailIntent = new Intent(SPGroupOrderActivity.this, SPOrderDetailActivity_.class);
                    detailIntent.putExtra("isGroup", true);
                    detailIntent.putExtra("orderId", ((SPOrder) msg.obj).getOrderID());
                    startActivity(detailIntent);
                    break;
                case SPMobileConstants.MSG_CODE_STORE_HOME_ACTION:       //店铺首页
                    Intent storeIntent = new Intent(SPGroupOrderActivity.this, SPStoreHomeActivity_.class);
                    storeIntent.putExtra("storeId", Integer.parseInt(msg.obj.toString()));
                    startActivity(storeIntent);
                    break;
            }
            /*code_15拼团模块逻辑代码*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "拼团订单");
        super.onCreate(savedInstanceState);
        /*code_15拼团模块逻辑代码*/
        IntentFilter filter = new IntentFilter();
        filter.addAction(SPMobileConstants.ACTION_ORDER_CHANGE);         //监听订单状态变化
        filter.addAction(SPMobileConstants.ACTION_COMMENT_CHANGE);       //监听在评价页面评论完成
        mReceiver = new OrderChangeReceiver();
        registerReceiver(mReceiver, filter);
        /*code_15拼团模块逻辑代码*/
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        /*code_15拼团模块逻辑代码*/
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, this);
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        Drawable dividler = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(dividler));             //设置分割线
        mAdapter = new SPOrderListAdapter(this, mHandler);
        refreshRecyclerView.setAdapter(mAdapter);
        /*code_15拼团模块逻辑代码*/
    }

    @Override
    public void initEvent() {
        /*code_15拼团模块逻辑代码*/
        allOrder.setOnClickListener(this);
        waitPay.setOnClickListener(this);
        waitGroup.setOnClickListener(this);
        waitSend.setOnClickListener(this);
        waitRecevice.setOnClickListener(this);
        hasDone.setOnClickListener(this);
        /*code_15拼团模块逻辑代码*/
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onClick(View v) {
        /*code_15拼团模块逻辑代码*/
        switch (v.getId()) {
            case R.id.all_order_txt:      //全部订单
                type = null;
                checkType(true, false, false, false, false, false);
                refreshData();
                break;
            case R.id.wait_pay_txt:       //待付款
                type = "WAITPAY";
                checkType(false, true, false, false, false, false);
                refreshData();
                break;
            case R.id.wait_group_txt:       //待成团
                type = "WAITTEAM";
                checkType(false, false, true, false, false, false);
                refreshData();
                break;
            case R.id.wait_send_txt:       //待发货
                type = "WAITSEND";
                checkType(false, false, false, true, false, false);
                refreshData();
                break;
            case R.id.wait_recevice_txt:       //待收货
                type = "WAITRECEIVE";
                checkType(false, false, false, false, true, false);
                refreshData();
                break;
            case R.id.has_done_txt:       //已完成
                type = "WAITCCOMMENT";
                checkType(false, false, false, false, false, true);
                refreshData();
                break;
        }
        /*code_15拼团模块逻辑代码*/
    }

    private void checkType(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
        /*code_15拼团模块逻辑代码*/
        if (a) {
            allOrder.setTextColor(getResources().getColor(R.color.light_red));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            waitGroup.setTextColor(getResources().getColor(R.color.person_info_text));
            waitSend.setTextColor(getResources().getColor(R.color.person_info_text));
            waitRecevice.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (b) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.light_red));
            waitGroup.setTextColor(getResources().getColor(R.color.person_info_text));
            waitSend.setTextColor(getResources().getColor(R.color.person_info_text));
            waitRecevice.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (c) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            waitGroup.setTextColor(getResources().getColor(R.color.light_red));
            waitSend.setTextColor(getResources().getColor(R.color.person_info_text));
            waitRecevice.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (d) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            waitGroup.setTextColor(getResources().getColor(R.color.person_info_text));
            waitSend.setTextColor(getResources().getColor(R.color.light_red));
            waitRecevice.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (e) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            waitGroup.setTextColor(getResources().getColor(R.color.person_info_text));
            waitSend.setTextColor(getResources().getColor(R.color.person_info_text));
            waitRecevice.setTextColor(getResources().getColor(R.color.light_red));
            hasDone.setTextColor(getResources().getColor(R.color.person_info_text));
        } else if (f) {
            allOrder.setTextColor(getResources().getColor(R.color.person_info_text));
            waitPay.setTextColor(getResources().getColor(R.color.person_info_text));
            waitGroup.setTextColor(getResources().getColor(R.color.person_info_text));
            waitSend.setTextColor(getResources().getColor(R.color.person_info_text));
            waitRecevice.setTextColor(getResources().getColor(R.color.person_info_text));
            hasDone.setTextColor(getResources().getColor(R.color.light_red));
        }
        /*code_15拼团模块逻辑代码*/
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onLoadMore() {
        loadMoreData();
    }

    private void refreshData() {
        /*code_15拼团模块逻辑代码*/
        this.mPageIndex = 1;
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type))
            params.put("type", type);
        params.put("p", this.mPageIndex);
        showLoadingSmallToast();
        SPPersonRequest.getGroupOrderList(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    orders = (List<SPOrder>) response;
                    mAdapter.updateData(orders);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener(SPGroupOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
        /*code_15拼团模块逻辑代码*/
    }

    public void loadMoreData() {
        /*code_15拼团模块逻辑代码*/
        mPageIndex++;
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type))
            params.put("type", type);
        params.put("p", mPageIndex);
        SPPersonRequest.getGroupOrderList(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPOrder> lists = (List<SPOrder>) response;
                    orders.addAll(lists);
                    mAdapter.updateData(orders);
                }
            }
        }, new SPFailuredListener(SPGroupOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
        /*code_15拼团模块逻辑代码*/
    }

    /**
     * 联系客服
     */
    private void connectSaller(SPOrder mOrder) {
        String qq = mOrder.getStore().getStoreQQ();
        if (!SPStringUtils.isEmpty(qq))
            connectCustomer(qq);
        else
            showToast("暂无联系方式");
    }

    /**
     * 取消订单
     */
    public void cancelOrder() {
        if (currentSelectOrder.getPayStatus() == 1) {      //取消已支付的订单
            Intent refundIntent = new Intent(this, SPRefundOrderActivity_.class);
            refundIntent.putExtra("order", currentSelectOrder);
            startActivity(refundIntent);
        } else {      //取消未支付的订单
            showConfirmDialog("确定取消订单", "订单提醒", this, SPMobileConstants.tagCancel);
        }
    }

    @Override
    public void clickOk(int actionType) {
        /*code_15拼团模块逻辑代码*/
        if (actionType == SPMobileConstants.tagCancel) {
            showLoadingSmallToast();
            cancelOrder(currentSelectOrder.getOrderID(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    showSuccessToast(msg);
                    refreshData();
                }
            }, new SPFailuredListener(SPGroupOrderActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        }
        /*code_15拼团模块逻辑代码*/
    }

    /**
     * 确认收货
     */
    public void confirmReceive(SPOrder order) {
        /*code_15拼团模块逻辑代码*/
        showLoadingSmallToast();
        confirmOrderWithOrderID(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(SPGroupOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
        /*code_15拼团模块逻辑代码*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*code_15拼团模块逻辑代码*/
        switch (requestCode) {
            case SPMobileConstants.Result_Code_PAY:
                refreshData();
                break;
        }
        /*code_15拼团模块逻辑代码*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void checkReturn(SPProduct product) {

    }

    @Override
    public void buyAgain(SPProduct product) {

    }

    class OrderChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*code_15拼团模块逻辑代码*/
            if (intent.getAction().equals(SPMobileConstants.ACTION_ORDER_CHANGE)
                    || intent.getAction().equals(SPMobileConstants.ACTION_COMMENT_CHANGE))
                refreshData();
            /*code_15拼团模块逻辑代码*/
        }
    }

}
