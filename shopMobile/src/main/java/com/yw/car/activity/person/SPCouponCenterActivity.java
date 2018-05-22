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
 * Description: 优惠券领券中心
 *
 * @version V1.0
 */
package com.yw.car.activity.person;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPCouponCenterTabAdapter;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPPersonRequest;
import com.yw.car.model.SPCategory;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(com.yw.car.R.layout.coupon_center)
public class SPCouponCenterActivity extends SPBaseActivity {

    private List<SPCategory> mCategorys;

    @ViewById(com.yw.car.R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    @ViewById(com.yw.car.R.id.coupon_center_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(com.yw.car.R.string.title_coupon_center));
        super.onCreate(savedInstanceState);
        mCategorys = new ArrayList<>();
        buildData();
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    public void buildData() {
        showLoadingSmallToast();
        SPPersonRequest.getCouponCenterCategoryWithType(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    mCategorys = (List<SPCategory>) response;
                    buildTitleView();
                } else {
                    showFailedToast("没有优惠券数据");
                }
            }
        }, new SPFailuredListener(SPCouponCenterActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    public void buildTitleView() {
        if (mCategorys == null) {
            showToast(getResources().getString(com.yw.car.R.string.toast_no_coupon_category));
            return;
        }
        SPCouponCenterTabAdapter fragPagerAdapter = new SPCouponCenterTabAdapter(getSupportFragmentManager(), mCategorys);
        mViewPager.setAdapter(fragPagerAdapter);
        mViewPager.setOffscreenPageLimit(mCategorys.size());
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mCategorys.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mCategorys.get(index).getName());
                simplePagerTitleView.setNormalColor(getResources().getColor(com.yw.car.R.color.sub_title));
                simplePagerTitleView.setSelectedColor(getResources().getColor(com.yw.car.R.color.light_red));
                simplePagerTitleView.setLines(1);
                simplePagerTitleView.setMinWidth(UIUtil.dip2px(SPCouponCenterActivity.this, 120));
                simplePagerTitleView.setTextSize(12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(com.yw.car.R.color.light_red));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));
    }

}
