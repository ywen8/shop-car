package com.yw.car.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.SPMainActivity;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.NetworkImageHolderView;
import com.yw.car.adapter.SPStoreStreetAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.rim.SPRimRequest;
import com.yw.car.http.shop.SPStoreRequest;
import com.yw.car.model.SPHomeBanners;
import com.yw.car.model.SPRimHeadlin;
import com.yw.car.model.shop.SPStore;
import com.yw.car.utils.SPUtils;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;
import com.yw.nice_spanner.RoundImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yw on 2018/2/9.
 */
@EActivity(R.layout.activity_ambitus)
public class SPAmbitusActivity extends SPBaseActivity implements View.OnClickListener, SPStoreStreetAdapter.StoreStreetListener {

    SPCommonListModel mCommonListModel;
    private List<SPHomeBanners> banners;
    private SPHomeBanners ad;
    private SPHomeBanners ad1;
    private SPRimHeadlin newads;
    List<SPStore> stores;
    boolean canClick = true;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    private SPAmbitusActivity spAmbitusActivity;

    @ViewById(R.id.home_banner_cbanner)
    ConvenientBanner homeCBanner;
    @ViewById(R.id.rm_icon_ad_middle_top_imgv)
    RoundImageView icon_top_ad_img;

    @ViewById(R.id.rm_icon_ad_middle_bottom_imgv)
    RoundImageView icon_bottom_ad_img;

    @ViewById(R.id.rm_center_headlin_tv)
    TextView headlin_tv;
    SPStoreStreetAdapter mAdapter;

    @ViewById(R.id.home_rim_bh)
    LinearLayout bh;
    @ViewById(R.id.home_rim_lr)
    LinearLayout lr;
    @ViewById(R.id.home_rim_jiud)
    LinearLayout jiud;
    @ViewById(R.id.home_rim_ms)
    LinearLayout ms;
    @ViewById(R.id.home_rim_tg)
    LinearLayout tg;
    @ViewById(R.id.home_rim_sh)
    LinearLayout sh;
    @ViewById(R.id.home_rim_xx)
    LinearLayout xx;
    @ViewById(R.id.home_rim_jingdian)
    LinearLayout jingdian;

    String lng;                       //经度
    String lat;                       //纬度
    String district;
    int scope = 10000;

    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        mAdapter = new SPStoreStreetAdapter(this, this);
        refreshRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        refreshData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "周边");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initEvent() {
        icon_top_ad_img.setOnClickListener(this);
        icon_bottom_ad_img.setOnClickListener(this);
        jingdian.setOnClickListener(this);
        sh.setOnClickListener(this);
        lr.setOnClickListener(this);
        xx.setOnClickListener(this);
        tg.setOnClickListener(this);
        ms.setOnClickListener(this);
        bh.setOnClickListener(this);
        jiud.setOnClickListener(this);
        homeCBanner.setPageIndicator(new int[]{com.yw.car.R.mipmap.index_white, com.yw.car.R.mipmap.index_red});          //设置两个点图片作为翻页指示器,不设置则没有指示器
        homeCBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        homeCBanner.setPageIndicator(new int[]{com.yw.car.R.drawable.shape_item_index_white, com.yw.car.R.drawable.shape_item_index_red});
        homeCBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        homeCBanner.startTurning(4000);
        homeCBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (banners != null)
                    SPUtils.adTopage(SPAmbitusActivity.this, banners.get(position));
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, SPStoreStreetActivity_.class);
        switch (view.getId()) {
            case R.id.rm_icon_ad_middle_top_imgv:
                SPUtils.adTopage(SPAmbitusActivity.this, ad);
                break;
            case R.id.rm_icon_ad_middle_bottom_imgv:
                SPUtils.adTopage(SPAmbitusActivity.this, ad1);
                break;
            case R.id.home_rim_bh:
//                gotoShopcart(2);
                intent.putExtra("sc_id", "2");
                startActivity(intent);
                break;
            case R.id.home_rim_sh:
//                gotoShopcart(8);
                intent.putExtra("sc_id", "8");
                startActivity(intent);
                break;
            case R.id.home_rim_xx:
//                gotoShopcart(7);
                intent.putExtra("sc_id", "7");
                startActivity(intent);
                break;
            case R.id.home_rim_ms:
//                gotoShopcart(1);
                intent.putExtra("sc_id", "1");
                startActivity(intent);
                break;
            case R.id.home_rim_jiud:
//                gotoShopcart(6);
                intent.putExtra("sc_id", "6");
                startActivity(intent);
                break;
            case R.id.home_rim_lr:
//                gotoShopcart(5);
                intent.putExtra("sc_id", "5");
                startActivity(intent);
                break;
            case R.id.home_rim_jingdian:
//                gotoShopcart(3);
                intent.putExtra("sc_id", "3");
                startActivity(intent);
                break;
            case R.id.home_rim_tg:
//                gotoShopcart(4);
                intent.putExtra("sc_id", "4");
                startActivity(intent);
                break;

        }
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
                    if (mCommonListModel.newAds != null) {
                        newads = mCommonListModel.newAds;
                        setHeadlin(newads);
                    }
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showToast(msg);
            }
        });

        RequestParams params = new RequestParams();
//        params.put("p", mPageIndex);
//        params.put("lng", lng);
//        params.put("lat", lat);
//        params.put("city", district);
//        if (!keyword.trim().isEmpty())
//            params.put("search_key", keyword);
//        params.put("scope", scope);
        params.put("sc_id", 1);
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
        }, new SPFailuredListener(SPAmbitusActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
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
                Glide.with(SPAmbitusActivity.this).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                        .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(icon_top_ad_img);
                break;
            case R.id.rm_icon_ad_middle_bottom_imgv:
                icon_bottom_ad_img.setTag(R.id.rm_icon_ad_middle_bottom_imgv, ad);
                Glide.with(SPAmbitusActivity.this).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                        .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(icon_bottom_ad_img);
                break;
        }
    }

    private void setHeadlin(SPRimHeadlin newads) {
        if (newads.getAdName() != null) {
            headlin_tv.setText(newads.getAdName().toString());
        } else {
            headlin_tv.setText("周边生活精彩上线，千万商家助力生活。沙坡头9.9包游〜〜");
        }

    }

    @AfterViews
    public void init() {
        super.init();
    }

    /**
     * 进入购物车
     */
    public void gotoShopcart(int sc_id) {
        Intent shopcartIntent = new Intent(this, SPMainActivity.class);
        shopcartIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_CATEGORY);
        shopcartIntent.putExtra("sc_id", sc_id);
        startActivity(shopcartIntent);
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
    protected void onResume() {
        super.onResume();
        canClick = true;
    }
}
