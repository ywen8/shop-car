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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:MineFragment
 *
 * @version V1.0
 */
package com.yw.car.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yw.car.activity.shop.SPCommodityEvaluationActivity_;
import com.yw.car.adapter.ListDividerItemDecoration;
import com.yw.car.adapter.SPOrderCenterListAdapter;
import com.yw.car.adapter.SPOrderCenterServiceAdapter;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.SPCommentData;
import com.yw.car.model.shop.SPServiceCommentList;
import com.yw.car.widget.swipetoloadlayout.OnLoadMoreListener;
import com.yw.car.widget.swipetoloadlayout.OnRefreshListener;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 评价中心
 */
public class SPCommentCenterFragment extends SPBaseFragment implements SPOrderCenterListAdapter.OnItemClickListener, OnRefreshListener,
        OnLoadMoreListener, SPOrderCenterServiceAdapter.OnItemClickListener {

    int mPageIndex;
    View mEmptyView;
    protected int mType;                                     //0:未评论,1:已评论
    List<SPCommentData> comment;                             //商品评价列表
    SPOrderCenterListAdapter mAdapter;
    SPOrderCenterServiceAdapter mAdapterA;
    SuperRefreshRecyclerView refreshRecyclerView;
    List<SPServiceCommentList> serviceCommentList;           //服务评价列表

    public static SPCommentCenterFragment newInstance(int type) {
        SPCommentCenterFragment fragment = new SPCommentCenterFragment();
        fragment.mType = type;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.yw.car.R.layout.person_order_fragment_view, null, false);
        super.init(view);
        return view;
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    @Override
    public void initSubView(View view) {
        mEmptyView = view.findViewById(com.yw.car.R.id.empty_rlayout);
        TextView tv = (TextView) view.findViewById(com.yw.car.R.id.empty_txtv);
        if (mType == 0) {
            tv.setText("没有待评价的商品哦~");
        } else if (mType == 1) {
            tv.setText("没有已评价的商品哦~");
        } else {
            tv.setText("没有未完成的服务评价哦~");
        }
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(com.yw.car.R.id.super_recyclerview);
        refreshRecyclerView.setEmptyView(mEmptyView);
        refreshRecyclerView.init(new LinearLayoutManager(getActivity()), this, this);
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        if (mType == 2) {             //设置分割线
            Drawable divilder = getResources().getDrawable(com.yw.car.R.drawable.divider_white_large);
            refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(divilder));
            mAdapterA = new SPOrderCenterServiceAdapter(getActivity(), this);
            refreshRecyclerView.setAdapter(mAdapterA);
        } else {             //设置分割线
            Drawable divilder = getResources().getDrawable(com.yw.car.R.drawable.divider_order_list_item);
            refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(divilder));
            mAdapter = new SPOrderCenterListAdapter(getActivity(), this, mType);
            refreshRecyclerView.setAdapter(mAdapter);
        }
        refreshData();
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
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
        mPageIndex = 1;
        showLoadingSmallToast();
        if (mType == 2) {       //服务评价
            SPUserRequest.getserviceComment(mPageIndex, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    refreshRecyclerView.setRefreshing(false);
                    if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                        serviceCommentList = (List<SPServiceCommentList>) response;
                        mAdapterA.updateData(serviceCommentList);
                        refreshRecyclerView.showData();
                    } else {
                        refreshRecyclerView.showEmpty();
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    refreshRecyclerView.setRefreshing(false);
                    showFailedToast(msg);
                }
            });
        } else {        //商品评价
            SPUserRequest.getComment(mPageIndex, mType, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    refreshRecyclerView.setRefreshing(false);
                    if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                        comment = (List<SPCommentData>) response;
                        mAdapter.updateData(comment);
                        refreshRecyclerView.showData();
                    } else {
                        refreshRecyclerView.showEmpty();
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    refreshRecyclerView.setRefreshing(false);
                    showFailedToast(msg);
                }
            });
        }
    }

    public void loadMoreData() {
        mPageIndex++;
        if (mType == 2) {         //服务评价
            SPUserRequest.getserviceComment(mPageIndex, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    refreshRecyclerView.setLoadingMore(false);
                    if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                        List<SPServiceCommentList> list = (List<SPServiceCommentList>) response;
                        serviceCommentList.addAll(list);
                        mAdapterA.updateData(serviceCommentList);
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    refreshRecyclerView.setLoadingMore(false);
                    showFailedToast(msg);
                    mPageIndex--;
                }
            });
        } else {          //商品评价
            SPUserRequest.getComment(mPageIndex, mType, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    refreshRecyclerView.setLoadingMore(false);
                    if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                        List<SPCommentData> list = (List<SPCommentData>) response;
                        comment.addAll(list);
                        mAdapter.updateData(comment);
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    refreshRecyclerView.setLoadingMore(false);
                    showFailedToast(msg);
                    mPageIndex--;
                }
            });
        }
    }

    @Override
    public void onItemClick(SPCommentData comment) {         //商品评价
        Intent intent = new Intent(getActivity(), SPCommodityEvaluationActivity_.class);
        intent.putExtra("comment", comment);
        startActivity(intent);
    }

    @Override
    public void onItemClick(String OrderId) {          //服务评价
        Intent intent = new Intent(getActivity(), SPCommodityEvaluationActivity_.class);
        intent.putExtra("orderId", OrderId);
        startActivity(intent);
    }

}
