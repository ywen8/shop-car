/*
  shopmobile for tpshop
  ============================================================================
  版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
  网站地址: http://www.tp-shop.cn
  ——————————————————————————————————————
  这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
  不允许对程序代码以任何形式任何目的的再发布。
  ============================================================================
  Author: 飞龙  wangqh01292@163.com
  Date: @date 2015年10月20日 下午7:19:26
  Description:商城Fragment

  @version V1.0
 */
package com.yw.car.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.SPMainActivity;
import com.yw.car.activity.common.PinYinActivity_;
import com.yw.car.activity.common.SPCommonWebActivity;
import com.yw.car.activity.common.SPSearchCommonActivity_;
import com.yw.car.activity.maintain.SPBaoXianActivity_;
import com.yw.car.activity.maintain.SPBigMainTainActivity_;
import com.yw.car.activity.maintain.SPCarServicePointActivity_;
import com.yw.car.activity.maintain.SPMainTainActivity_;
import com.yw.car.activity.maintain.SPSecodeCarActivity_;
import com.yw.car.activity.maintain.SPSmallMainTainActivity_;
import com.yw.car.activity.maintain.SPWeiZhangAcitivty_;
import com.yw.car.activity.person.SPCouponCenterActivity_;
import com.yw.car.activity.person.SPCouponListActivity_;
import com.yw.car.activity.person.user.SPMessageCenterActivity_;
import com.yw.car.activity.shop.SPAmbitusActivity_;
import com.yw.car.activity.shop.SPFlashSaleActivity_;
import com.yw.car.activity.shop.SPGoodsPromoteActivity_;
import com.yw.car.activity.shop.SPGroupListActivity_;
import com.yw.car.activity.shop.SPIntegralMallActivity_;
import com.yw.car.activity.shop.SPProductDetailActivity_;
import com.yw.car.activity.shop.SPProductListActivity;
import com.yw.car.activity.shop.SPStoreStreetActivity_;
import com.yw.car.adapter.DividerGridItemDecoration;
import com.yw.car.adapter.NetworkImageHolderView;
import com.yw.car.adapter.SPProductListHomeAdapter;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.global.SPSaveData;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.home.SPHomeRequest;
import com.yw.car.model.SPAffiche;
import com.yw.car.model.SPHomeBanners;
import com.yw.car.model.SPProduct;
import com.yw.car.utils.SPConfirmDialog;
import com.yw.car.utils.SPUtils;
import com.yw.car.view.TextSwitchView;
import com.yw.car.widget.CountdownView;
import com.yw.car.widget.SPProductScrollView;
import com.yw.car.widget.swipetoloadlayout.OnLoadMoreListener;
import com.yw.car.widget.swipetoloadlayout.OnRefreshListener;
import com.yw.car.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

//import com.yw.car.model.activity.shop.SPFightGroupsActivity_;

/**
 * 首页 -> 商城首页
 */
public class SPHomeSecFragment extends SPBaseFragment implements View.OnClickListener, SPConfirmDialog.ConfirmDialogListener,
        SPProductListHomeAdapter.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {

    TextView cityTv;
    View mHeaderView;
    View groupLayout;
    View orderLayout;
    TextView msgView;
    private long time;
    View personLayout;
    TextView personTxtv;
    int mPageIndex = 1;
    private int centerindex;
    ImageButton topBtn;
    View homeTitleView;
    View categoryLayout;
    View shopcartLayout;
    EditText searchText;
    View promotionLayout;
    View storeStreetLayout;
    View brandStreetLayout;
    View smallMaintainLayout;
    View bigsmallmaintainLayout;
    View car_service;
    View weixiuLayout;
    View weiXZhangLayout;
    View ershoucheLayout;
    View meirongLayout;
    View baoxianLayout;
    View youkaLayout;
    View gentduoLayout;
    View center_li_gonggao;
    TextSwitchView switchView;
    TextView mFlashSaleTxtv;                            //限时限量
    private View flashLayout;
    ConvenientBanner homeCBanner;
    CountdownView flashCoutdownv;                       //倒计时
    private SPMainActivity mActivity;
    GridLayoutManager mLayoutManager;
    SPProductScrollView mFlashSalePsv;                  //抢购
    SPProductListHomeAdapter mAdapter;
    private CountTimeRunnable runnable;
    static SPHomeSecFragment mFragment;
    SPCommonListModel mCommonListModel;
    private List<SPHomeBanners> banners;
    SmartRecyclerAdapter mSmartRecyclerAdapter;
    private List<SPAffiche> affiches;
    SuperRefreshRecyclerView refreshRecyclerView;
    public LocationClient mLocationClient = null;
    List<SPProduct> mFavourites = new ArrayList<>();
    public final static int MSG_POSITION_CHANGE = 1;
    public BDLocationListener myListener = new MyLocationListener();
    private ImageView ad1, ad2, ad3, ad4, ad5, ad6, ad7, ad8, ad9, ad10, ad11, ad12, ad13;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_POSITION_CHANGE:
                    BDLocation location = (BDLocation) msg.obj;
                    if (location == null || location.getCity() == null)
                        return;
                    String city = location.getCity().replace('市', ' ').trim();
                    SPSaveData.cacheLocation(getActivity(), String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), city);
                    String district = SPSaveData.getString(getActivity(), SPMobileConstants.KEY_LOCATION_ADDRESS);
                    if (!SPStringUtils.isEmpty(district))
                        cityTv.setText(district);
                    if (!district.equals(city)) {
                        String changeTo = getResources().getString(com.yw.car.R.string.change_to);
                        showConfirmDialog("定位到'" + city + "',是否切换城市?", "提示信息", changeTo, SPHomeSecFragment.this, 1);
                    }
                    break;
            }
        }
    };

    public static SPHomeSecFragment newInstance() {
        if (mFragment == null)
            mFragment = new SPHomeSecFragment();
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (SPMainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity());             //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);            //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.yw.car.R.layout.home_sec_fragment, null, false);
        mHeaderView = inflater.inflate(com.yw.car.R.layout.home_sec_header_view, null);
        flashLayout = mHeaderView.findViewById(com.yw.car.R.id.flash_layout);
        flashCoutdownv = (CountdownView) mHeaderView.findViewById(com.yw.car.R.id.flash_coutdownv);
        ad1 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_middle_top_imgv);
        ad2 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_middle_left1_imgv);
        ad3 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_middle_left2_imgv);
        ad4 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_middle_right_imgv);
        ad5 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_new_right_imgv);
        ad6 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_new_right1_imgv);
        ad7 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_new_right2_imgv);
        ad8 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_hot_top_right_imgv);
        ad9 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_hot_top_left_imgv);
        ad10 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_hot_bottom1_imgv);
        ad11 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_hot_bottom2_imgv);
        ad12 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_hot_bottom3_imgv);
        ad13 = (ImageView) mHeaderView.findViewById(com.yw.car.R.id.icon_ad_hot_bottom4_imgv);
        categoryLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_categroy_layout);
        storeStreetLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_store_street_layout);
        brandStreetLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_brand_street_layout);
        weixiuLayout = mHeaderView.findViewById(R.id.home_rim_weixiu);
        smallMaintainLayout = mHeaderView.findViewById(com.yw.car.R.id.home_rim_smallmaintain);
        ershoucheLayout = mHeaderView.findViewById(R.id.home_rim_ershouche);
        weiXZhangLayout = mHeaderView.findViewById(R.id.home_rim_weizhang);
        bigsmallmaintainLayout = mHeaderView.findViewById(R.id.home_rim_bigsmallmaintain);
        car_service = mHeaderView.findViewById(R.id.home_rim_car_service);
        promotionLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_promotion_layout);
        groupLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_group_layout);
        center_li_gonggao = mHeaderView.findViewById(R.id.home_center_li_gonggao);
        meirongLayout = mHeaderView.findViewById(R.id.home_rim_meirong);
        shopcartLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_shopcart_layout);
        orderLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_order_layout);
        personLayout = mHeaderView.findViewById(com.yw.car.R.id.home_menu_person_layout);
        personTxtv = (TextView) mHeaderView.findViewById(com.yw.car.R.id.home_menu_person_txtv);
        mFlashSaleTxtv = (TextView) mHeaderView.findViewById(com.yw.car.R.id.flash_sale_more_txtv);
        mFlashSalePsv = (SPProductScrollView) mHeaderView.findViewById(com.yw.car.R.id.flash_sale_scrollv);
        switchView = (TextSwitchView) mHeaderView.findViewById(R.id.home_center_gonggao);
        topBtn = (ImageButton) view.findViewById(com.yw.car.R.id.top_ibtn);
        homeTitleView = view.findViewById(com.yw.car.R.id.toprela);
        gentduoLayout = mHeaderView.findViewById(R.id.home_rim_gengduo);
        baoxianLayout = mHeaderView.findViewById(R.id.home_rim_baoxian);
        homeTitleView.getBackground().setAlpha(0);
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(com.yw.car.R.id.super_recyclerview);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);     //设置排版方式
        refreshRecyclerView.init(mLayoutManager, this, this);
        Drawable drawable = getResources().getDrawable(com.yw.car.R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPProductListHomeAdapter(getActivity(), this);
        mSmartRecyclerAdapter = new SmartRecyclerAdapter(mAdapter);
        mSmartRecyclerAdapter.setHeaderView(mHeaderView);
        youkaLayout = mHeaderView.findViewById(R.id.home_rim_youka);
        refreshRecyclerView.setAdapter(mSmartRecyclerAdapter);
        searchText = (EditText) homeTitleView.findViewById(com.yw.car.R.id.searchkey_edtv);
        cityTv = (TextView) view.findViewById(com.yw.car.R.id.city_tv);
        msgView = (TextView) view.findViewById(com.yw.car.R.id.image_right);
        homeCBanner = (ConvenientBanner) mHeaderView.findViewById(com.yw.car.R.id.home_banner_cbanner);
        super.init(view);
        return view;
    }

    @Override
    public void initSubView(View view) {
    }

    @Override
    public void initEvent() {
        categoryLayout.setOnClickListener(this);
        shopcartLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        promotionLayout.setOnClickListener(this);
        brandStreetLayout.setOnClickListener(this);
        storeStreetLayout.setOnClickListener(this);
        personLayout.setOnClickListener(this);
        gentduoLayout.setOnClickListener(this);
        orderLayout.setOnClickListener(this);
        smallMaintainLayout.setOnClickListener(this);
        car_service.setOnClickListener(this);
        mFlashSaleTxtv.setOnClickListener(this);
        weixiuLayout.setOnClickListener(this);
        ad1.setOnClickListener(this);
        meirongLayout.setOnClickListener(this);
        ad2.setOnClickListener(this);
        ad3.setOnClickListener(this);
        ad4.setOnClickListener(this);
        ad5.setOnClickListener(this);
        ad6.setOnClickListener(this);
        center_li_gonggao.setOnClickListener(this);
        ad7.setOnClickListener(this);
        youkaLayout.setOnClickListener(this);
        ad8.setOnClickListener(this);
        ad9.setOnClickListener(this);
        weiXZhangLayout.setOnClickListener(this);
        baoxianLayout.setOnClickListener(this);
        ad10.setOnClickListener(this);
        ad11.setOnClickListener(this);
        ershoucheLayout.setOnClickListener(this);
        ad12.setOnClickListener(this);
        ad13.setOnClickListener(this);
        bigsmallmaintainLayout.setOnClickListener(this);
        searchText.setOnClickListener(this);
        cityTv.setOnClickListener(this);
        msgView.setOnClickListener(this);
        topBtn.setOnClickListener(this);
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
        refreshRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int distance = getScollYDistance();
                float alpha = distance / 550f;
                if (alpha >= 1) alpha = 1;
                int bgAlpha = (int) (alpha * 255);
                homeTitleView.getBackground().setAlpha(bgAlpha);
                if (distance >= 550)
                    topBtn.setVisibility(View.VISIBLE);
                else
                    topBtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        String address = SPSaveData.getString(getActivity(), SPMobileConstants.KEY_LOCATION_ADDRESS);
        if (!SPStringUtils.isEmpty(address))
            cityTv.setText(address);
    }

    /**
     * 获取RecycleView滑动距离
     */
    public int getScollYDistance() {
        GridLayoutManager layoutManager = (GridLayoutManager) refreshRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
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

    public void refreshData() {
        this.mPageIndex = 1;
        showLoadingSmallToast();
        SPHomeRequest.getHomePageData(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel != null) {
                    if (mCommonListModel.banners != null) {
                        banners = mCommonListModel.banners;
                        List<String> gallerys = new ArrayList<>();
                        for (SPHomeBanners banner : banners)
                            gallerys.add(SPUtils.getImageUrl(banner.getAdCode()));
                        setLoopView(gallerys);
                    }
                    if (mCommonListModel.flashSales != null && mCommonListModel.flashSales.size() > 0) {
                        time = mCommonListModel.flashSales.get(0).getEndTime();
                        mFlashSalePsv.setDataSource(mCommonListModel.flashSales);        //设置秒杀
                        setCountTime();       //设置秒杀倒计时
                        flashLayout.setVisibility(View.VISIBLE);
                    } else {
                        flashLayout.setVisibility(View.GONE);
                    }
                    if (mCommonListModel.ads != null)
                        setAdModel(mCommonListModel.ads);

                    if (mCommonListModel.affiches != null)
                        affiches = mCommonListModel.affiches;
                        setAffices(affiches);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
            }
        });
        SPHomeRequest.getFavouritePageData(this.mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    mFavourites = (List<SPProduct>) response;
                    mAdapter.updateData(mFavourites);
                    mAdapter.notifyDataSetChanged();
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

    public void loadMoreData() {
        mPageIndex++;
        SPHomeRequest.getFavouritePageData(this.mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPProduct> moreFavourts = (List<SPProduct>) response;
                    mFavourites.addAll(moreFavourts);
                    mAdapter.updateData(mFavourites);
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

    //秒杀倒计时
    private void setCountTime() {
        long timeCount = SPUtils.getTimeCount(System.currentTimeMillis(), time);
        if (timeCount <= 0) {
            refreshData();           //倒计时结束自动刷新
            return;
        }
        flashCoutdownv.updateShow(timeCount);
        if (runnable != null) runnable.stop();
        runnable = new CountTimeRunnable();
        new Handler().postDelayed(runnable, 1000);
    }

    private class CountTimeRunnable implements Runnable {
        private boolean isStop = false;

        private void stop() {
            isStop = true;
        }

        @Override
        public void run() {
            if (!isStop)
                setCountTime();
        }
    }

    private void setAffices(List<SPAffiche> affices) {
        switchView.setResources(affices);
        switchView.setTextStillTime(10000);
        switchView.setOnclickCallBack(callBack);
    }

    private void setAdModel(List<SPHomeBanners> ads) {
        for (SPHomeBanners ad : ads) {
            switch (ad.getPid()) {
                case 506:
                    ad1.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(mActivity).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad1);
                    break;
                case 507:
                    ad2.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad2);
                    break;
                case 508:
                    ad3.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad3);
                    break;
                case 509:
                    ad4.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad4);
                    break;
                case 510:
                    ad5.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad5);
                    break;
                case 511:
                    ad6.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad6);
                    break;
                case 512:
                    ad7.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad7);
                    break;
                case 513:
                    ad8.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad8);
                    break;
                case 514:
                    ad9.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad9);
                    break;
                case 515:
                    ad10.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad10);
                    break;
                case 516:
                    ad11.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad11);
                    break;
                case 517:
                    ad12.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad12);
                    break;
                case 518:
                    ad13.setTag(com.yw.car.R.id.image_tag, ad);
                    Glide.with(getActivity()).load(SPUtils.getImageUrl(ad.getAdCode())).asBitmap().fitCenter()
                            .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ad13);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        SPHomeBanners ad = null;
        try {
            ad = (SPHomeBanners) v.getTag(com.yw.car.R.id.image_tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (v.getId()) {
            case com.yw.car.R.id.searchkey_edtv:                    //搜索
                Intent intent = new Intent(getActivity(), SPSearchCommonActivity_.class);
                startActivity(intent);
                break;
            case com.yw.car.R.id.home_menu_categroy_layout:         //分类
                this.mActivity.setShowFragment(SPMainActivity.INDEX_CATEGORY);
                break;
            case com.yw.car.R.id.home_menu_shopcart_layout:         //金豆商城
                startupIntegralMall();
                break;
            case com.yw.car.R.id.home_menu_group_layout:            //团购
                startupGroupList();
                break;
            case com.yw.car.R.id.home_menu_promotion_layout:        //商品促销
                startupGoodsPromote();
                break;
            case com.yw.car.R.id.top_ibtn:                          //回到顶部
                scrollTop();
                break;
            case com.yw.car.R.id.image_right:                       //消息中心
                if (!checkLogin()) return;
                getActivity().startActivity(new Intent(getActivity(), SPMessageCenterActivity_.class));
                break;
            case com.yw.car.R.id.city_tv:                           //城市切换
                Intent pinyinintent = new Intent(getActivity(), PinYinActivity_.class);
                getActivity().startActivity(pinyinintent);
                break;
            case com.yw.car.R.id.home_menu_brand_street_layout:     //品牌街

                startBrandStreet();
                break;
            case com.yw.car.R.id.home_menu_store_street_layout:     //店铺街
                startAmbitus();
                break;
            case com.yw.car.R.id.home_menu_person_layout:           //我要拼团
                //getActivity().startActivity(new Intent(getActivity(), SPFightGroupsActivity_.class));
                this.mActivity.setShowFragment(SPMainActivity.INDEX_PERSON);
                personTxtv.setText(getString(com.yw.car.R.string.text_item_person));
                break;
            case com.yw.car.R.id.home_menu_order_layout:            //领券中心
                startupCouponCenter();
                break;
            case com.yw.car.R.id.flash_sale_more_txtv:              //抢购
                startupFlashSale();
                break;
            case com.yw.car.R.id.icon_ad_middle_top_imgv:           //中间广告位
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_middle_left2_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_middle_left1_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_middle_right_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_new_right_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_new_right1_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_new_right2_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_hot_top_right_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_hot_top_left_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_hot_bottom1_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_hot_bottom2_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_hot_bottom3_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.icon_ad_hot_bottom4_imgv:
                SPUtils.adTopage(getActivity(), ad);
                break;
            case com.yw.car.R.id.home_rim_smallmaintain:
                if (!checkLogin()) return;
                startSmallmaintain();
                break;
            case R.id.home_rim_bigsmallmaintain:
                if (!checkLogin()) return;
                startBigmaintain();
                break;
            case R.id.home_rim_car_service:
                startCarService();
//                startupStoreStreet();
                break;
            case R.id.home_rim_weixiu:
                startWeiXiu();
                break;
            case R.id.home_rim_weizhang:
                startWeizhang();
                break;
            case R.id.home_rim_ershouche:
                startershouche();
                break;
            case R.id.home_rim_meirong:
                showToast("暂未开通!");
                break;
            case R.id.home_rim_youka:
                showToast("暂未开通!");
                break;
            case R.id.home_rim_gengduo:
                showToast("暂未开通!");
                break;
            case R.id.home_rim_baoxian:
                startBaoxian();
                break;
            case R.id.home_center_li_gonggao:
                showCenterGongGao();
                break;
        }
    }

    public void showCenterGongGao() {
        SPAffiche affiche=affiches.get(centerindex);
        if(affiche!=null){
            Intent intent = new Intent(getActivity(), SPCommonWebActivity.class);
            intent.putExtra(SPMobileConstants.KEY_WEB_TITLE, affiche.getTitle());
            intent.putExtra(SPMobileConstants.KEY_WEB_URL, affiche.getLink());
            startActivity(intent);
        }
    }

    boolean checkLogin() {
        if (!SPMobileApplication.getInstance().isLogined()) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }

    public void startBaoxian() {
        Intent intent = new Intent(getActivity(), SPBaoXianActivity_.class);
        getActivity().startActivity(intent);
    }

    public void startWeiXiu() {
        Intent intent = new Intent(getActivity(), SPMainTainActivity_.class);
        getActivity().startActivity(intent);
    }

    public void startWeizhang() {
        Intent intent = new Intent(getActivity(), SPWeiZhangAcitivty_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 金豆商城
     */
    public void startupIntegralMall() {
        Intent intent = new Intent(getActivity(), SPIntegralMallActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 品牌街
     */
    public void startBrandStreet() {
        Intent intent = new Intent(getActivity(), SPCouponListActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 店铺街
     */
    public void startupStoreStreet() {
        Intent intent = new Intent(getActivity(), SPStoreStreetActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 小保养
     */
    public void startSmallmaintain() {
        Intent intent = new Intent(getActivity(), SPSmallMainTainActivity_.class);
        getActivity().startActivity(intent);
    }

    public void startershouche() {
        Intent intent = new Intent(getActivity(), SPSecodeCarActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 大保养
     */

    public void startBigmaintain() {
        Intent intent = new Intent(getActivity(), SPBigMainTainActivity_.class);
        getActivity().startActivity(intent);
    }

    public void startCarService() {
        Intent intent = new Intent(getActivity(), SPCarServicePointActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 周边
     */
    public void startAmbitus() {
        Intent intent = new Intent(getActivity(), SPAmbitusActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 回到顶部
     */
    public void scrollTop() {
        refreshRecyclerView.moveToPosition(0);
        topBtn.setVisibility(View.GONE);
        homeTitleView.getBackground().setAlpha(0);
    }

    /**
     * 团购
     */
    public void startupGroupList() {
        Intent groupIntent = new Intent(getActivity(), SPGroupListActivity_.class);
        getActivity().startActivity(groupIntent);
    }

    /**
     * 商品促销
     */
    public void startupGoodsPromote() {
        Intent promoteIntent = new Intent(getActivity(), SPGoodsPromoteActivity_.class);
        startActivity(promoteIntent);
    }

    /**
     * 领券中心
     */
    public void startupCouponCenter() {
        getActivity().startActivity(new Intent(getActivity(), SPCouponCenterActivity_.class));
    }

    /**
     * 抢购
     */
    public void startupFlashSale() {
        Intent flashSaleIntent = new Intent(getActivity(), SPFlashSaleActivity_.class);
        getActivity().startActivity(flashSaleIntent);
    }

    @Override
    public void onItemClick(SPProduct product) {
        startupActivity(product.getGoodsID());
    }

    public void startupActivity(String goodsID) {
        Intent intent = new Intent(SPMainActivity.getmInstance(), SPProductDetailActivity_.class);
        intent.putExtra("goodsID", goodsID);
        startActivity(intent);
    }

    @Override
    public void clickOk(int actionType) {
        if (actionType == 1) {
            String city = SPSaveData.getString(getActivity(), SPMobileConstants.KEY_LOCATION);
            SPSaveData.putValue(getActivity(), SPMobileConstants.KEY_LOCATION_ADDRESS, city);
            cityTv.setText(city);
        }
    }

    @Override
    public void onSimilarClick(int catId) {
        Intent intent = new Intent(getActivity(), SPProductListActivity.class);
        intent.putExtra("category_id", catId);
        getActivity().startActivity(intent);
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocationClient.stop();                                                    //停止定位
            StringBuffer sb = new StringBuffer(256);                                   //获取定位结果
            sb.append("time : ");
            sb.append(location.getTime());                                             //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());                                          //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());                                         //获取纬度信息
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());                                        //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());                                           //获取定位精准度
            if (location.getLocType() == BDLocation.TypeGpsLocation) {                 //GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());                                        //单位:公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());                              //获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());                                     //获取海拔高度信息,单位米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());                                    //获取方向信息,单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());                                      //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {      //网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());                                      //获取地址信息
                sb.append("\n区县 : ");
                sb.append(location.getDistrict());                                     //获取地址信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());                                    //获取运营商信息
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {      //离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                showToast("服务端网络定位失败");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                showToast("网络不同导致定位失败");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                showToast("无法获取有效定位依据导致定位失败");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());      //位置语义化信息
            List<Poi> list = location.getPoiList();         // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Message msg = mHandler.obtainMessage(MSG_POSITION_CHANGE);
            msg.obj = location;
            mHandler.sendMessage(msg);
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选,默认高精度,设置定位模式,高精度,低功耗,仅设备
        option.setCoorType("bd09ll");
        //可选,默认gcj02,设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选,默认0,即仅定位一次,设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选,设置是否需要地址信息,默认不需要
        option.setOpenGps(true);
        //可选,默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选,默认false,设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选,默认false,设置是否需要位置语义化结果,可以在BDLocation.getLocationDescribe里得到,结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选,默认false,设置是否需要POI结果,可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选,默认true,定位SDK内部是一个SERVICE,并放到了独立进程,设置是否在stop的时候杀死这个进程,默认不杀死
        option.SetIgnoreCacheException(false);
        //可选,默认false,设置是否收集CRASH信息,默认收集
        option.setEnableSimulateGps(false);
        //可选,默认false,设置是否需要过滤GPS仿真结果,默认需要
        mLocationClient.setLocOption(option);
    }

    private TextSwitchView.OnclickCallBack callBack = new TextSwitchView.OnclickCallBack() {
        @Override
        public void clickCallBack(int index) {
            centerindex = index;
        }
    };

}
