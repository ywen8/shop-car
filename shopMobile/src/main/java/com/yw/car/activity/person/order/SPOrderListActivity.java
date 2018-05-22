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

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.activity.common.SPCommonWebActivity;
import com.yw.car.activity.common.SPOrderBaseActivity;
import com.yw.car.activity.shop.SPStoreHomeActivity_;
import com.yw.car.adapter.ListDividerItemDecoration;
import com.yw.car.adapter.SPOrderListAdapter;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPPersonRequest;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.SPProduct;
import com.yw.car.model.order.SPOrder;
import com.yw.car.utils.SPConfirmDialog;
import com.yw.car.utils.SPOrderUtils;
import com.yw.car.utils.SPOrderUtils.OrderStatus;
import com.yw.car.utils.SPUtils;
import com.yw.car.widget.swipetoloadlayout.OnLoadMoreListener;
import com.yw.car.widget.swipetoloadlayout.OnRefreshListener;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/1
 */
@EActivity(R.layout.order_list)
public class SPOrderListActivity extends SPOrderBaseActivity implements OnRefreshListener, OnLoadMoreListener,
        SPConfirmDialog.ConfirmDialogListener {

    private int pageIndex;
    private List<SPOrder> orders;
    private OrderStatus orderStatus;                        //订单类型
    private SPOrder currentSelectOrder;                     //选中的订单
    private SPOrderListAdapter mAdapter;
    private OrderChangeReceiver mReceiver;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPMobileConstants.tagPay:         //支付订单
                    SPMobileApplication.getInstance().fellBack = 3;
                    SPOrder order = (SPOrder) msg.obj;
                    order.setVirtual(false);
                    gotoPay(order);
                    break;
                case SPMobileConstants.tagCancel:        //取消订单
                    currentSelectOrder = (SPOrder) msg.obj;
                    cancelOrder();
                    break;
                case SPMobileConstants.tagShopping:        //查看物流
                    lookShopping((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagReceive:        //确认收货
                    confirmReceive((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagCancelInfo:       //取消详情
                    checkTokenPastDue((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.tagCustomer:       //联系客服
                    connectSaller((SPOrder) msg.obj);
                    break;
                case SPMobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:      //订单详情
                    Intent detailIntent = new Intent(SPOrderListActivity.this, SPOrderDetailActivity_.class);
                    detailIntent.putExtra("orderId", ((SPOrder) msg.obj).getOrderID());
                    startActivity(detailIntent);
                    break;
                case SPMobileConstants.MSG_CODE_STORE_HOME_ACTION:       //店铺首页
                    Intent storeIntent = new Intent(SPOrderListActivity.this, SPStoreHomeActivity_.class);
                    storeIntent.putExtra("storeId", Integer.parseInt(msg.obj.toString()));
                    startActivity(storeIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "订单列表");
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SPMobileConstants.ACTION_ORDER_CHANGE);                //监听订单状态变化
        filter.addAction(SPMobileConstants.ACTION_COMMENT_CHANGE);              //监听在评价页面评论完成
        mReceiver = new OrderChangeReceiver();
        registerReceiver(mReceiver, filter);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, this);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPOrderListAdapter(this, mHandler);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            int value = getIntent().getIntExtra("orderStatus", 0);
            orderStatus = SPOrderUtils.getOrderStatusByValue(value);
        } else {
            orderStatus = OrderStatus.all;
        }
        String title = SPOrderUtils.getOrderTitlteWithOrderStatus(orderStatus);
        setTitle(title);
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onLoadMore() {
        loadMoreData();
    }

    public void refreshData() {
        pageIndex = 1;
        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("p", pageIndex);
        showLoadingSmallToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
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
        }, new SPFailuredListener(SPOrderListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    public void loadMoreData() {
        pageIndex++;
        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type)) params.put("type", type);
        params.put("p", pageIndex);
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPOrder> tempOrders = (List<SPOrder>) response;
                    orders.addAll(tempOrders);
                    mAdapter.updateData(orders);
                }
            }
        }, new SPFailuredListener(SPOrderListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                pageIndex--;
            }
        });
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
        if (actionType == SPMobileConstants.tagCancel) {
            showLoadingSmallToast();
            cancelOrder(currentSelectOrder.getOrderID(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    showSuccessToast(msg);
                    refreshData();
                }
            }, new SPFailuredListener(SPOrderListActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        }
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
            }
        }, new SPFailuredListener(SPOrderListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    //校验token是否过期
    public void checkTokenPastDue(final SPOrder order) {
        SPUserRequest.getTokenStatus(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                toExchange(order);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                if (SPUtils.isTokenExpire(errorCode))
                    toLoginPage();      //跳转到登录页
            }
        });
    }

    public void toExchange(SPOrder order) {
        String url = String.format(SPMobileConstants.URL_ORDER_REFUND_DETAIL, order.getOrderID());
        Intent intent = new Intent(this, SPCommonWebActivity.class);
        intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, "取消详情");
        intent.putExtra(SPMobileConstants.KEY_WEB_URL, url);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:             //支付成功
                refreshData();
                break;
        }
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
            if (intent.getAction().equals(SPMobileConstants.ACTION_ORDER_CHANGE)
                    || intent.getAction().equals(SPMobileConstants.ACTION_COMMENT_CHANGE))
                refreshData();
        }
    }

}