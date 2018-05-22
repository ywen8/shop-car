/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015-10-15 20:32:41
 * Description: 商城主界面Activity (底部包含四个tab item)
 *
 * @version V1.0
 */
package com.yw.car;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.common.PermissionUtils;
import com.yw.car.common.SPDataAsyncManager;
import com.yw.car.fragment.SPAmbitusFragment;
import com.yw.car.fragment.SPBaseFragment;
import com.yw.car.fragment.SPCategoryFragment;
import com.yw.car.fragment.SPHomeSecFragment;
import com.yw.car.fragment.SPPersonFragment;
import com.yw.car.fragment.SPShopCartFragment;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.SPAppData;
import com.yw.car.service.SPResultAmbuitusIDService;
import com.yw.car.utils.UpdateAppUtil;
import com.yw.car.widget.UpdateDialog;

public class SPMainActivity extends SPBaseActivity {

    SPAppData appData;
    RadioButton mCurrRb;
    RadioButton mLastRb;
    RadioButton rbtnHome;
    RadioGroup mRadioGroup;
    RadioButton rbtnPerson;
    RadioButton rbtnCategory;
    RadioButton rbtnShopcart;
    private boolean mKeyDownCount;
    public int mCurrentSelectIndex;
    SPHomeSecFragment mHomeFragment;
    SPPersonFragment mPersonFragment;
    FragmentManager mFragmentManager;
    SPCategoryFragment mCategoryFragment;
    SPShopCartFragment mShopCartFragment;
    SPAmbitusFragment spAmbitusFragment;
    public static final int INDEX_HOME = 0;
    private static SPMainActivity mInstance;
    public static final int INDEX_PERSON = 3;
    public static final int INDEX_CATEGORY = 1;
    public static final int INDEX_SHOPCART = 2;
    public static final String SELECT_INDEX = "selectIndex";
    public static final String CACHE_SELECT_INDEX = "cacheSelectIndex";
    int leftIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mFragmentManager = this.getSupportFragmentManager();
        super.init();
        addFragment();
        hiddenFragment();
        if (savedInstanceState != null) {
            mCurrentSelectIndex = savedInstanceState.getInt(CACHE_SELECT_INDEX, INDEX_HOME);
        } else {
            mCurrentSelectIndex = INDEX_HOME;
        }
        setSelectIndex(mCurrentSelectIndex);
        mInstance = this;
        if (Build.VERSION.SDK_INT >= 23)
            requestMulti();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CACHE_SELECT_INDEX, mCurrentSelectIndex);
        super.onSaveInstanceState(outState);
    }

    public void requestMulti() {
        PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
    }

    @Override
    public void initSubViews() {
        mHomeFragment = SPHomeSecFragment.newInstance();
        mCategoryFragment = SPCategoryFragment.newInstance();
        mShopCartFragment = SPShopCartFragment.newInstance();
        mPersonFragment = SPPersonFragment.newInstance();
        spAmbitusFragment = SPAmbitusFragment.newInstance();
        mRadioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        rbtnHome = (RadioButton) this.findViewById(R.id.rbtn_home);
        rbtnCategory = (RadioButton) this.findViewById(R.id.rbtn_category);
        rbtnShopcart = (RadioButton) this.findViewById(R.id.rbtn_shopcart);
        rbtnPerson = (RadioButton) this.findViewById(R.id.rbtn_mine);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        existsIndexSelect(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        existsIndexSelect();
    }

    @Override
    public void initEvent() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int key) {
                switch (key) {
                    case R.id.rbtn_home:
                        setSelectIndex(INDEX_HOME);
                        break;
                    case R.id.rbtn_category:
                        setSelectIndex(INDEX_CATEGORY);
                        break;
                    case R.id.rbtn_shopcart:
                        setSelectIndex(INDEX_SHOPCART);
                        break;
                    case R.id.rbtn_mine:
                        setSelectIndex(INDEX_PERSON);
                        break;
                }
            }
        });
    }

    public void setLeftScID(SPResultAmbuitusIDService resultAmbuitusIDService) {
        resultAmbuitusIDService.resultScID(leftIndex);
    }

    @Override
    public void initData() {
        SPDataAsyncManager.getInstance(this).startSyncData();       //同步数据
        checkVersion();       //检测版本
    }

    private void checkVersion() {
        try {
            String verName = getPackageManager().getPackageInfo("com.yw.car", 0).versionName;
            SPUserRequest.getUpdateInfo(verName, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    appData = (SPAppData) response;
                    if (appData.getIsNew() == 1)
                        showUpdataDialog();
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showFailedToast(msg);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showUpdataDialog() {
        UpdateDialog.Builder updateBuilder = new UpdateDialog.Builder(this);
        updateBuilder.setMessage(appData.getLog());
        final UpdateDialog dialog = updateBuilder.create();
        updateBuilder.setCancelOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        updateBuilder.setDownloadOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppUtil update = new UpdateAppUtil();
                update.downLoadApk(SPMainActivity.this, appData.getUrl());
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void setSelectIndex(int index) {
        switch (index) {
            case INDEX_HOME:
                showFragment(mHomeFragment);
                changeTabtextSelector(rbtnHome);
                setTitle(getString(R.string.tab_item_home_page));
                mCurrentSelectIndex = INDEX_HOME;
                break;
            case INDEX_CATEGORY:
                showFragment(mCategoryFragment);
                changeTabtextSelector(rbtnCategory);
                setTitle(getString(R.string.tab_item_category));
                mCurrentSelectIndex = INDEX_CATEGORY;
                break;
            case INDEX_SHOPCART:
                mCategoryFragment.onResume();
                showFragment(mShopCartFragment);
                changeTabtextSelector(rbtnShopcart);
                setTitle(getString(R.string.text_item_shopcart));
                mCurrentSelectIndex = INDEX_SHOPCART;
                break;
            case INDEX_PERSON:
                mPersonFragment.onResume();
                showFragment(mPersonFragment);
                changeTabtextSelector(rbtnPerson);
                setTitle(getString(R.string.tab_item_mine));
                mCurrentSelectIndex = INDEX_PERSON;
                break;
        }
    }

    public void setShowFragment(int flag) {
        if (flag == SPMainActivity.INDEX_HOME) {
            showFragment(mHomeFragment);
            changeTabtextSelector(rbtnHome);
            setTitle(getString(R.string.tab_item_home_page));
            rbtnHome.setChecked(true);
        } else if (flag == SPMainActivity.INDEX_CATEGORY) {
            showFragment(mCategoryFragment);
            changeTabtextSelector(rbtnCategory);
            setTitle(getString(R.string.tab_item_category));
            rbtnCategory.setChecked(true);
        } else if (flag == SPMainActivity.INDEX_SHOPCART) {
            showFragment(mShopCartFragment);
            changeTabtextSelector(rbtnShopcart);
            setTitle(getString(R.string.text_item_shopcart));
            rbtnShopcart.setChecked(true);
        } else if (flag == SPMainActivity.INDEX_PERSON) {
            showFragment(mPersonFragment);
            changeTabtextSelector(rbtnPerson);
            setTitle(getString(R.string.tab_item_mine));
            rbtnPerson.setChecked(true);
        }
    }

    //显示当前的fragment
    private void showFragment(SPBaseFragment fragment) {
        hiddenFragment();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.show(fragment);
        mTransaction.commitAllowingStateLoss();
    }

    //隐藏当前的fragment
    private void hiddenFragment() {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mHomeFragment);
        mTransaction.hide(mCategoryFragment);
        mTransaction.hide(mShopCartFragment);
        mTransaction.hide(mPersonFragment);
        mTransaction.commitAllowingStateLoss();
    }

    //添加fragment
    private void addFragment() {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fragmentView, mHomeFragment);
        mTransaction.add(R.id.fragmentView, mCategoryFragment);
        mTransaction.add(R.id.fragmentView, mShopCartFragment);
        mTransaction.add(R.id.fragmentView, mPersonFragment);
        mTransaction.commitAllowingStateLoss();
    }

    public void changeTabtextSelector(RadioButton rb) {
        mLastRb = mCurrRb;
        mCurrRb = rb;
        if (mLastRb != null) {
            mLastRb.setTextColor(getResources().getColor(R.color.color_tab_item_normal));
            mLastRb.setSelected(false);
        }
        if (mCurrRb != null) {
            mCurrRb.setTextColor(getResources().getColor(R.color.commit_rim_gb));
            mCurrRb.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentSelectIndex == 0) {
            if (!mKeyDownCount) {
                mKeyDownCount = true;
                showToast(getString(R.string.exit_again_press));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mKeyDownCount = false;
                    }
                }, 2000);
            } else {
                finish();
            }
        } else {
            setShowFragment(INDEX_HOME);
            mKeyDownCount = false;
        }
    }

    public static SPMainActivity getmInstance() {
        return mInstance;
    }

    private void existsIndexSelect() {
        int selectIndex;
        if (getIntent().hasExtra(SELECT_INDEX)) {
            selectIndex = getIntent().getIntExtra(SELECT_INDEX, -1);
            getIntent().putExtra(SELECT_INDEX, -1);       //清除缓存
            setShowFragment(selectIndex);
        }
    }

    private void existsIndexSelect(Intent intent) {
        int selectIndex;
        if (intent.hasExtra(SELECT_INDEX)) {
            selectIndex = intent.getIntExtra(SELECT_INDEX, -1);
            intent.putExtra(SELECT_INDEX, -1);       //清除缓存
            leftIndex = intent.getIntExtra("sc_id", 0);
            setShowFragment(selectIndex);
        }
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    showToast("Result Permission Grant CODE_RECORD_AUDIO");
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    showToast("Result Permission Grant CODE_GET_ACCOUNTS");
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    showToast("Result Permission Grant CODE_READ_PHONE_STATE");
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    showToast("Result Permission Grant CODE_CALL_PHONE");
                    break;
                case PermissionUtils.CODE_CAMERA:
                    showToast("Result Permission Grant CODE_CAMERA");
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    showToast("Result Permission Grant CODE_ACCESS_FINE_LOCATION");
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    showToast("Result Permission Grant CODE_ACCESS_COARSE_LOCATION");
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    showToast("Result Permission Grant CODE_READ_EXTERNAL_STORAGE");
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    showToast("Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE");
                    break;
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    showToast("Result All Permission Grant");
                    break;
            }
        }
    };

}
