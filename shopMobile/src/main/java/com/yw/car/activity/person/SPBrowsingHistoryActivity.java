package com.yw.car.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yw.car.SPMainActivity;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.activity.shop.SPProductDetailActivity_;
import com.yw.car.activity.shop.SPProductListActivity;
import com.yw.car.adapter.SPBrowsingHistoryAdapter;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.SPBrowItem;
import com.yw.car.model.SPBrowingProduct;
import com.yw.car.utils.SPConfirmDialog;
import com.yw.car.widget.swipetoloadlayout.OnLoadMoreListener;
import com.yw.car.widget.swipetoloadlayout.OnRefreshListener;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zw
 */
@EActivity(com.yw.car.R.layout.activity_spbrowsing_history)
public class SPBrowsingHistoryActivity extends SPBaseActivity implements SPBrowsingHistoryAdapter.OnItemClickListener, OnRefreshListener,
        OnLoadMoreListener, SPConfirmDialog.ConfirmDialogListener {

    int mPageIndex = 1;
    private SPBrowsingHistoryAdapter adapter;
    private List<SPBrowingProduct> browingProduct = new ArrayList<>();

    @ViewById(com.yw.car.R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "浏览记录");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        FrameLayout titlebarLayout = (FrameLayout) findViewById(com.yw.car.R.id.titlebar_normal_layout);
        TextView deleteTv = new TextView(this);
        deleteTv.setText("清空");
        deleteTv.setTextColor(this.getResources().getColor(com.yw.car.R.color.color_font_666));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        deleteTv.setPadding(0, 0, 10, 0);
        params.setMargins(0, 0, 10, 0);
        deleteTv.setGravity(Gravity.CENTER_VERTICAL);
        deleteTv.setTextSize(16);
        titlebarLayout.addView(deleteTv, params);
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });
        View emptyView = findViewById(com.yw.car.R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, this);
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        adapter = new SPBrowsingHistoryAdapter(this, this);
        refreshRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onItemClick(SPBrowItem item) {
        Intent intent = new Intent(SPMainActivity.getmInstance(), SPProductDetailActivity_.class);
        intent.putExtra("goodsID", item.getGoodsID() + "");
        startActivity(intent);
    }

    @Override
    public void onSimilarClick(int catId) {
        Intent intent = new Intent(this, SPProductListActivity.class);
        intent.putExtra("category_id", catId);
        startActivity(intent);
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
        this.mPageIndex = 1;
        showLoadingSmallToast();
        SPUserRequest.getBrowHistory(this.mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    browingProduct = (List<SPBrowingProduct>) response;
                    adapter.updateBrowData(browingProduct);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener(SPBrowsingHistoryActivity.this) {
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
        SPUserRequest.getBrowHistory(this.mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPBrowingProduct> list = (List<SPBrowingProduct>) response;
                    browingProduct.addAll(list);
                    adapter.updateBrowData(browingProduct);
                }
            }
        }, new SPFailuredListener(SPBrowsingHistoryActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
    }

    //清空记录
    private void deleteRecord() {
        if (browingProduct.size() <= 0) {
            showToast("当前无记录");
            return;
        }
        showConfirmDialog("确定清空记录吗？", "清空提醒", this, 1);
    }

    @Override
    public void clickOk(int actionType) {
        SPUserRequest.clearBrowHistory(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showSuccessToast(msg);
                browingProduct.clear();
                refreshData();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

}
