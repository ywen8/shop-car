package com.yw.car.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.adapter.SPInsuranceAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPMaintain;
import com.yw.car.service.OnFragmentInteractionListener;

import java.util.List;

/**
 * Created by yw on 2018/2/14.
 */


public class SPInsuranceFragment extends SPBaseFragment {
    private static SPInsuranceFragment mFragment;
    private RecyclerView insurance_cyclerview;
    SPCommonListModel mCommonListModel;
    SPInsuranceAdapter adapter;
    private OnFragmentInteractionListener onFragmentInteractionListener;

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initSubView(View view) {
        insurance_cyclerview = (RecyclerView) view.findViewById(R.id.insurance_cyclerview);
    }

    @Override
    public void initEvent() {
        insurance_cyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void initData() {
        refreshData();
    }

    public static SPInsuranceFragment newInstance() {
        if (mFragment == null)
            mFragment = new SPInsuranceFragment();
        return mFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insurance, null, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        super.init(view);
        return view;

    }

    private void refreshData() {
        showLoadingSmallToast();
        RequestParams params=new RequestParams();
        params.add("type","1");
        SPMaintainRequest.getCarService(params,new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel != null) {
                    if (mCommonListModel.maintains != null) {
                        setbaoxian(mCommonListModel.maintains);
                    }
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
            }
        });
    }

    private void setbaoxian(List<SPMaintain> maintains) {
        adapter = new SPInsuranceAdapter(getActivity(), maintains,onFragmentInteractionListener);
        insurance_cyclerview.setAdapter(adapter);
    }


}
