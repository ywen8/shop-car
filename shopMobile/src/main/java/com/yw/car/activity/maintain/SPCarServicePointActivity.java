package com.yw.car.activity.maintain;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.yw.car.R;
import com.yw.car.activity.baidu.SPBaiDuBaseActivity;
import com.yw.car.adapter.HomeFragmentPageAdapter;
import com.yw.car.fragment.SPInsuranceFragment;
import com.yw.car.fragment.SPMaintainFragment;
import com.yw.car.fragment.SPUpkeepFragment;
import com.yw.car.fragment.SPUsedCarFragment;
import com.yw.car.model.car.SPMaintain;
import com.yw.car.service.OnFragmentInteractionListener;
import com.yw.car.widget.NoScrollViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yw on 2018/2/11.
 */
@EActivity(R.layout.activity_car_service)
public class SPCarServicePointActivity extends SPBaiDuBaseActivity implements OnFragmentInteractionListener {
    @ViewById(R.id.car_service_group)
    RadioGroup car_service_group;

    @ViewById(R.id.car_service_content)
    NoScrollViewPager car_service_content;

    @Override
    public void initSubViews() {
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(SPInsuranceFragment.newInstance());
        mFragmentList.add(SPUpkeepFragment.newInstance());
        mFragmentList.add(SPMaintainFragment.newInstance());
        mFragmentList.add(SPUsedCarFragment.newInstance());
        car_service_content.setAdapter(new HomeFragmentPageAdapter(getSupportFragmentManager(), mFragmentList));
        car_service_content.setCurrentItem(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        car_service_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch (i) {
                    case R.id.car_service_baoxian:
                        car_service_content.setCurrentItem(0);// 设置当前页面
                        break;
                    case R.id.car_service_baoyang:
                        car_service_content.setCurrentItem(1);
                        break;
                    case R.id.car_service_weixiu:
                        car_service_content.setCurrentItem(2);
                        break;
                    case R.id.car_service_ershoueche:
                        car_service_content.setCurrentItem(3);
                        break;
                }
            }
        });

        car_service_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        car_service_group.check(R.id.car_service_baoxian);
                        break;
                    case 1:
                        car_service_group.check(R.id.car_service_baoyang);
                        break;
                    case 2:
                        car_service_group.check(R.id.car_service_weixiu);
                        break;
                    case 3:
                        car_service_group.check(R.id.car_service_ershoueche);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "汽车服务点");
        super.onCreate(savedInstanceState);
    }


    @AfterViews
    public void init() {
        super.init();
    }


    @Override
    public void onFragmentInteraction(SPMaintain maintain) {
        setLatWithLog(maintain);

    }


}
