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
 * Description: 店铺街道
 *
 * @version V1.0
 */
package com.yw.car.activity.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.ListDividerItemDecoration;
import com.yw.car.adapter.SPStoreStreetAdapter;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.global.SPSaveData;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.shop.SPStoreRequest;
import com.yw.car.model.shop.SPStore;
import com.yw.car.utils.SPUtils;
import com.yw.car.widget.swipetoloadlayout.OnLoadMoreListener;
import com.yw.car.widget.swipetoloadlayout.OnRefreshListener;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.store_street)
public class SPStoreStreetActivity extends SPBaseActivity implements SPStoreStreetAdapter.StoreStreetListener, OnRefreshListener,
        OnLoadMoreListener, View.OnClickListener {

    String lng;                       //经度
    String lat;                       //纬度
    String district;                  //筛选的城市
    int scope = 10000;                //默认范围
    int mPageIndex = 1;
    String keyword = "";              //搜索关键词
    List<SPStore> stores;
    boolean canClick = true;
    private String sc_id = "1";
    SPStoreStreetAdapter mAdapter;
    private RecommendReceiver mReceiver;

    @ViewById(R.id.back_txtv)
    TextView backTxtv;

    @ViewById(R.id.searchkey_edtv)
    EditText searchkeyEdtv;

    @ViewById(R.id.search_txtv)
    TextView searchTxtv;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle bundle) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
        IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_GOODS_RECOMMEND);
        mReceiver = new RecommendReceiver();
        registerReceiver(mReceiver, filter);
        String sc = getIntent().getStringExtra("sc_id");
        if (sc != null && sc != "0") {
            sc_id = sc;
        }
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        lng = SPSaveData.getString(this, SPMobileConstants.KEY_LONGITUDE);
        lat = SPSaveData.getString(this, SPMobileConstants.KEY_LATITUDE);
        district = SPSaveData.getString(this, SPMobileConstants.KEY_LOCATION_ADDRESS);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, this);
        Drawable dividler = getResources().getDrawable(R.drawable.divider_store_street);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(dividler));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        mAdapter = new SPStoreStreetAdapter(this, this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        backTxtv.setOnClickListener(this);
        searchTxtv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        canClick = true;
    }

    @Override
    public void initData() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_txtv:
                finish();
                break;
            case R.id.search_txtv:
                search();
                break;
        }
    }

    private void search() {
        keyword = searchkeyEdtv.getText().toString();
        if (searchkeyEdtv.hasFocus() || !keyword.trim().isEmpty()) {
            refreshData();
            searchkeyEdtv.clearFocus();
            SPUtils.hideSoftInput(searchkeyEdtv);
        }
    }

    public void refreshData() {
        mPageIndex = 1;
        RequestParams params = new RequestParams();
//        params.put("p", mPageIndex);
//        params.put("lng", lng);
//        params.put("lat", lat);
//        params.put("city", district);
//        if (!keyword.trim().isEmpty())
//            params.put("search_key", keyword);
//        params.put("scope", scope);
        params.put("sc_id", sc_id);
        showLoadingSmallToast();
        SPStoreRequest.storeStreet(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null) {
                    SPCommonListModel commonModel = (SPCommonListModel) response;
                    if (commonModel.stores != null && commonModel.stores.size() > 0) {        //店铺列表
                        stores = commonModel.stores;
                        mAdapter.updateData(stores);
                        refreshRecyclerView.showData();
                    } else {
                        refreshRecyclerView.showEmpty();
                    }
                }
            }
        }, new SPFailuredListener(SPStoreStreetActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    public void loadMoreData() {
        mPageIndex++;
        RequestParams params = new RequestParams();
        params.put("p", mPageIndex);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("city", district);
        if (!keyword.trim().isEmpty())
            params.put("search_key", keyword);
        params.put("scope", scope);
        SPStoreRequest.storeStreet(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null) {
                    SPCommonListModel commonModel = (SPCommonListModel) response;
                    if (commonModel.stores != null) {        //店铺列表
                        stores.addAll(commonModel.stores);
                        mAdapter.updateData(stores);
                    }
                }
            }
        }, new SPFailuredListener(SPStoreStreetActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
    }

    @Override
    public void onEnterStoreClick(int storeId) {
        if (!canClick) return;
        canClick = false;
        Intent storeIntent = new Intent(this, SPStoreHomeActivity_.class);
        storeIntent.putExtra("storeId", storeId);
        startActivity(storeIntent);
    }

    @Override
    public void onStoreMapClick(SPStore store) {
        if (!canClick) return;
        canClick = false;
        Intent intent = new Intent(this, SPStoreMapActivity_.class);
        intent.putExtra("store", store);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class RecommendReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String goodsId = SPMobileApplication.getInstance().data;
            if (intent.getAction().equals(SPMobileConstants.ACTION_GOODS_RECOMMEND)) {
                Intent detailIntent = new Intent(SPStoreStreetActivity.this, SPProductDetailActivity_.class);
                detailIntent.putExtra("goodsId", goodsId);
                SPStoreStreetActivity.this.startActivity(detailIntent);
            }
        }
    }

}
