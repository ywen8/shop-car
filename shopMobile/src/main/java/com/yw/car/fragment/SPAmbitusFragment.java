package com.yw.car.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yw.car.R;
import com.yw.car.SPMainActivity;
import com.yw.car.adapter.NetworkImageHolderView;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.rim.SPRimRequest;
import com.yw.car.model.SPHomeBanners;
import com.yw.car.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import com.yw.nice_spanner.RoundImageView;

/**
 * Created by yw on 2018/2/7.
 */

public class SPAmbitusFragment extends SPBaseFragment implements View.OnClickListener {

    static SPAmbitusFragment mFragment;
    private SPMainActivity mActivity;
    SPCommonListModel mCommonListModel;
    private List<SPHomeBanners> banners;
    private SPHomeBanners ad;
    private SPHomeBanners ad1;
    ConvenientBanner homeCBanner;
    RoundImageView icon_top_ad_img, icon_bottom_ad_img;

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initSubView(View view) {

    }

    @Override
    public void initEvent() {
        icon_top_ad_img.setOnClickListener(this);
        icon_bottom_ad_img.setOnClickListener(this);
        homeCBanner.setPageIndicator(new int[]{com.yw.car.R.mipmap.index_white, com.yw.car.R.mipmap.index_red});          //设置两个点图片作为翻页指示器,不设置则没有指示器
        homeCBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        homeCBanner.setPageIndicator(new int[]{com.yw.car.R.drawable.shape_item_index_white, com.yw.car.R.drawable.shape_item_index_red});
        homeCBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        homeCBanner.startTurning(4000);
        homeCBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (banners != null) SPUtils.adTopage(getActivity(), banners.get(position));
            }
        });
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rm_icon_ad_middle_top_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case R.id.rm_icon_ad_middle_bottom_imgv:
                SPUtils.adTopage(getActivity(), ad1);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (SPMainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ambitus, null, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        homeCBanner = (ConvenientBanner) view.findViewById(com.yw.car.R.id.home_banner_cbanner);
        icon_bottom_ad_img = (RoundImageView) view.findViewById(R.id.rm_icon_ad_middle_bottom_imgv);
        icon_top_ad_img = (RoundImageView) view.findViewById(R.id.rm_icon_ad_middle_top_imgv);
        super.init(view);
        return view;

    }

    public static SPAmbitusFragment newInstance() {
        if (mFragment == null)
            mFragment = new SPAmbitusFragment();
        return mFragment;
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPRimRequest.getRimData(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel != null) {
                    if (mCommonListModel.banners != null) {
                        banners = mCommonListModel.banners;
                        List<String> gallerys = new ArrayList<>();
                        for (SPHomeBanners banner : banners)
                            gallerys.add(SPUtils.getImageUrl(banner.getAdCode()));
                        setLoopView(gallerys);
                    }
                    if (mCommonListModel.ad != null) {
                        ad = mCommonListModel.ad;
                        setAdModel(ad, R.id.rm_icon_ad_middle_top_imgv);
                    }
                    if (mCommonListModel.ad1 != null) {
                        ad1 = mCommonListModel.ad1;
                        setAdModel(ad1, R.id.rm_icon_ad_middle_bottom_imgv);
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

    /**
     * 给商品轮播图设置图片路径
     */
    public void setLoopView(List<String> gallerys) {
        if (gallerys == null || gallerys.size() < 1) return;
        homeCBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new NetworkImageHolderView();
            }
        }, gallerys);
    }

    private void setAdModel(SPHomeBanners ad, int id) {
        switch (id) {
            case R.id.rm_icon_ad_middle_top_imgv:
                icon_top_ad_img.setTag(R.id.rm_icon_ad_middle_top_imgv, ad);
                Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                        .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(icon_top_ad_img);
                break;
            case R.id.rm_icon_ad_middle_bottom_imgv:
                icon_bottom_ad_img.setTag(R.id.rm_icon_ad_middle_bottom_imgv, ad);
                Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                        .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(icon_bottom_ad_img);
                break;
        }
    }

}
