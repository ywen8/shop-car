package com.yw.car.widget.swipetoloadlayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SuperRefreshRecyclerView extends FrameLayout {

    private int mIndex;
    private boolean move;
    private View emptyView;
    private RelativeLayout errorView;
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView.LayoutManager layoutManager;

    public SuperRefreshRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public SuperRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SuperRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(com.yw.car.R.layout.layout_super_refresh_recycler, this);
        emptyView = findViewById(com.yw.car.R.id.layout_empty);
        errorView = (RelativeLayout) findViewById(com.yw.car.R.id.layout_error);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(com.yw.car.R.id.swipe_to_load);
        recyclerView = (RecyclerView) findViewById(com.yw.car.R.id.swipe_target);
    }

    public void init(RecyclerView.LayoutManager layoutManager, OnRefreshListener onRefreshListener, OnLoadMoreListener onLoadMoreListener) {
        recyclerView.setLayoutManager(layoutManager);
        this.layoutManager = layoutManager;
        swipeToLoadLayout.setOnRefreshListener(onRefreshListener);
        swipeToLoadLayout.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.setOnScrollListener(new RecyclerViewListener());
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }

    public void showEmpty() {
        swipeToLoadLayout.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
        errorView.setVisibility(GONE);
    }

    public void showError() {
        swipeToLoadLayout.setVisibility(GONE);
        emptyView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
    }

    public void showData() {
        swipeToLoadLayout.setVisibility(VISIBLE);
        emptyView.setVisibility(GONE);
        errorView.setVisibility(GONE);
    }

    public void setEmptyView(View view) {
        emptyView = view;
    }

    public boolean isRefreshing() {
        return swipeToLoadLayout.isRefreshing();
    }

    public void setRefreshing(boolean refreshing) {
        swipeToLoadLayout.setRefreshing(refreshing);
    }

    public boolean isLoadingMore() {
        return swipeToLoadLayout.isLoadingMore();
    }

    public void setLoadingMore(boolean loadMore) {
        swipeToLoadLayout.setLoadingMore(loadMore);
    }

    public void setLoadingMoreEnable(boolean enable) {
        swipeToLoadLayout.setLoadMoreEnabled(enable);
    }

    public void setRefreshEnabled(boolean enable) {
        swipeToLoadLayout.setRefreshEnabled(enable);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void moveToPosition(int n) {
        mIndex = n;
        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) layoutManager;
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {      //当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {      //当要置顶的项已经在屏幕上显示时
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {        //当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(n);
            move = true;        //这里这个变量是用在RecyclerView滚动监听里面的
        }
    }

    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动
            if (move) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) layoutManager;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = recyclerView.getChildAt(n).getTop();
                    //最后的移动
                    recyclerView.scrollBy(0, top);
                }
            }
        }
    }

}
