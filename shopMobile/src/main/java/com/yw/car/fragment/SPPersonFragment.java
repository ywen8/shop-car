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
 * Description:SPPersonFragment
 *
 * @version V1.0
 */
package com.yw.car.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.activity.common.BadgeView;
import com.yw.car.activity.common.SPCommonWebActivity;
import com.yw.car.activity.person.SPBrowsingHistoryActivity_;
import com.yw.car.activity.person.SPCollectListActivity_;
import com.yw.car.activity.person.SPCouponCenterActivity_;
import com.yw.car.activity.person.SPCouponListActivity_;
import com.yw.car.activity.person.SPMerchantsSettledActivity_;
import com.yw.car.activity.person.SPOfflineAllianceActivity_;
import com.yw.car.activity.person.SPSettingListActivity_;
import com.yw.car.activity.person.address.SPConsigneeAddressListActivity_;
import com.yw.car.activity.person.catipal.SPCapitalManageActivity_;
import com.yw.car.activity.person.order.SPCommentCenterActivity_;
import com.yw.car.activity.person.order.SPOrderListActivity_;
import com.yw.car.activity.person.order.SPVirtualOrderActivity_;
import com.yw.car.activity.person.user.SPLoginActivity_;
import com.yw.car.activity.person.user.SPMessageCenterActivity_;
import com.yw.car.activity.person.user.SPSaveCarInfoActivity_;
import com.yw.car.activity.person.user.SPUserDetailsActivity_;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.global.SPShopCartManager;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;
import com.yw.car.model.person.SPUser;
import com.yw.car.utils.SPOrderUtils;
import com.yw.car.utils.SPUtils;

/**
 * 首页 -> 我的
 */
public class SPPersonFragment extends SPBaseFragment implements View.OnClickListener {

    View focusLL;
    View recordLL;
    View myPoints;
    View myCoupon;
    View myBalance;
    View collectLL;
    View orderAview;                 //我的订单
    View couponView;                 //优惠券
    View aboutUsView;
    View accountView;
    Button settingBtn;
    Button messageBtn;
    View feedBackView;
    private int error;
    View settingsView;               //设置
    TextView pointTxtv;              //金豆
    View integrateView;              //金豆,余额
    View collectLayout;              //收藏
    View waitPayLayout;              //待支付
    TextView levelName;
    View waitSendLayout;             //待发货
    ImageView levelImgv;
    View xxtmCenterView;
    View groupOrderView;             //拼团订单
    View createStoreView;
    TextView balanceTxtv;            //余额
    BadgeView badWaitPay;
    BadgeView badWaitSend;
    TextView nicknameTxtv;           //昵称
    View virtualOrderView;           //虚拟订单
    View couponCenterView;
    View waitReturnLayout;           //退换货
    View waitReceiveLayout;          //待收货
    View waitCommentLayout;          //待评价
    BadgeView badUnComment;
    View receiveAddressView;         //收货地址
    View person_car_info;
    public Context mContext;
    private TextView favTxt;
    private TextView hisTxt;
    private TextView likeTxt;
    TextView couponCountTxtv;        //优惠券数量
    BadgeView badReturnGoods;
    BadgeView badWaitReceive;
    SimpleDraweeView nickImage;
    View distributionCenterView;
    RelativeLayout header_relayout;
    static SPPersonFragment mFragment;

    public static SPPersonFragment newInstance() {
        if (mFragment == null)
            mFragment = new SPPersonFragment();
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.yw.car.R.layout.person_fragment, null, false);
        super.init(view);
        return view;
    }

    @Override
    public void initSubView(View view) {
        collectLL = view.findViewById(com.yw.car.R.id.collectLL);
        focusLL = view.findViewById(com.yw.car.R.id.focusLL);
        recordLL = view.findViewById(com.yw.car.R.id.recordLL);
        favTxt = (TextView) view.findViewById(com.yw.car.R.id.txt_fav);
        likeTxt = (TextView) view.findViewById(com.yw.car.R.id.txt_like);
        hisTxt = (TextView) view.findViewById(com.yw.car.R.id.txt_his);
        waitPayLayout = view.findViewById(com.yw.car.R.id.personal_order_waitpay_layout);
        waitSendLayout = view.findViewById(com.yw.car.R.id.personal_order_waitsend_layout);
        waitReceiveLayout = view.findViewById(com.yw.car.R.id.personal_order_waitreceive_layout);
        waitCommentLayout = view.findViewById(com.yw.car.R.id.personal_order_waitcomment_layout);
        waitReturnLayout = view.findViewById(com.yw.car.R.id.personal_order_returned);
        orderAview = view.findViewById(com.yw.car.R.id.person_order_aview);
        collectLayout = view.findViewById(com.yw.car.R.id.person_collect_aview);
        integrateView = view.findViewById(com.yw.car.R.id.person_integrate_rlayout);                       //金豆,余额
        receiveAddressView = view.findViewById(com.yw.car.R.id.person_receive_address_aview);              //收货地址
        couponView = view.findViewById(com.yw.car.R.id.person_coupon_aview);                               //优惠券
        settingsView = view.findViewById(com.yw.car.R.id.person_system_setting);                           //设置
        virtualOrderView = view.findViewById(com.yw.car.R.id.virtual_order_aview);                         //虚拟订单
        groupOrderView = view.findViewById(com.yw.car.R.id.group_order_aview);                             //拼团订单
        groupOrderView.setVisibility(View.GONE);
        person_car_info = view.findViewById(R.id.person_car_info);
        aboutUsView = view.findViewById(com.yw.car.R.id.person_about_us);
        feedBackView = view.findViewById(com.yw.car.R.id.person_feedback);
        couponCenterView = view.findViewById(com.yw.car.R.id.person_coupon_center);
        distributionCenterView = view.findViewById(com.yw.car.R.id.person_distribution_center);
        xxtmCenterView = view.findViewById(com.yw.car.R.id.person_xxtm_center);
        createStoreView = view.findViewById(com.yw.car.R.id.person_create_store);
        if (!SPMobileConstants.IS_DISTRIBUTION)
            distributionCenterView.setVisibility(View.GONE);
        balanceTxtv = (TextView) view.findViewById(com.yw.car.R.id.person_balance_txtv);                   //余额
        pointTxtv = (TextView) view.findViewById(com.yw.car.R.id.person_point_txtv);                       //金豆
        couponCountTxtv = (TextView) view.findViewById(com.yw.car.R.id.person_coupon_txtv);                //优惠券数量
        nicknameTxtv = (TextView) view.findViewById(com.yw.car.R.id.nickname_txtv);                        //昵称
        header_relayout = (RelativeLayout) view.findViewById(com.yw.car.R.id.header_relayout);
        nickImage = (SimpleDraweeView) view.findViewById(com.yw.car.R.id.head_mimgv);
        settingBtn = (Button) view.findViewById(com.yw.car.R.id.setting_btn);
        messageBtn = (Button) view.findViewById(com.yw.car.R.id.message_btn);
        accountView = view.findViewById(com.yw.car.R.id.account_rlayout);
        levelImgv = (ImageView) view.findViewById(com.yw.car.R.id.level_img);
        levelName = (TextView) view.findViewById(com.yw.car.R.id.level_name_txtv);
        myBalance = view.findViewById(com.yw.car.R.id.my_balance);
        myPoints = view.findViewById(com.yw.car.R.id.my_points);
        myCoupon = view.findViewById(com.yw.car.R.id.my_coupon);
        badWaitPay = new BadgeView(getActivity(), waitPayLayout);
        badWaitSend = new BadgeView(getActivity(), waitSendLayout);
        badWaitReceive = new BadgeView(getActivity(), waitReceiveLayout);
        badUnComment = new BadgeView(getActivity(), waitCommentLayout);
        badReturnGoods = new BadgeView(getActivity(), waitReturnLayout);
    }

    @Override
    public void initEvent() {
        collectLL.setOnClickListener(this);
        focusLL.setOnClickListener(this);
        recordLL.setOnClickListener(this);
        waitPayLayout.setOnClickListener(this);
        waitSendLayout.setOnClickListener(this);
        waitReceiveLayout.setOnClickListener(this);
        waitCommentLayout.setOnClickListener(this);
        waitReturnLayout.setOnClickListener(this);
        collectLayout.setOnClickListener(this);
        integrateView.setOnClickListener(this);
        receiveAddressView.setOnClickListener(this);
        orderAview.setOnClickListener(this);
        couponView.setOnClickListener(this);
        settingsView.setOnClickListener(this);
        virtualOrderView.setOnClickListener(this);
        groupOrderView.setOnClickListener(this);
        aboutUsView.setOnClickListener(this);
        feedBackView.setOnClickListener(this);
        couponCenterView.setOnClickListener(this);
        distributionCenterView.setOnClickListener(this);
        xxtmCenterView.setOnClickListener(this);
        createStoreView.setOnClickListener(this);
        header_relayout.setOnClickListener(this);
        nickImage.setOnClickListener(this);
        nicknameTxtv.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
        accountView.setOnClickListener(this);
        myCoupon.setOnClickListener(this);
        myPoints.setOnClickListener(this);
        myBalance.setOnClickListener(this);
        person_car_info.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void initData() {
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    public void refreshData() {
        boolean isLogin = SPMobileApplication.getInstance().isLogined();
        if (isLogin) {
            SPUserRequest.getUserInfo(new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    if (response != null) {
                        SPUser user = (SPUser) response;
                        SPMobileApplication.getInstance().setLoginUser(user);
                        SPShopCartManager.getInstance(getActivity()).setShopCount(user.getCartCount());         //更新购物车数量
                        error = 0;
                        refreshView();
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    if (SPUtils.isTokenExpire(errorCode)) {
                        error = errorCode;
                        SPMobileApplication.getInstance().exitLogin();
                    }
                    refreshView();
                }
            });
        } else {
            refreshView();
        }
    }

    //刷新View数据
    private void refreshView() {
        String balance = "0";
        String point = "0";
        int collectCount = 0;
        int focsCount = 0;
        int recordCount = 0;
        int returnCount;
        int waitPay;
        int waitSend;
        int waitReceive;
        int unCommentCount;
        String couponCount = "";
        String nickName = "登录/注册";
        String headPic;
        SPUser user = null;
        if (SPMobileApplication.getInstance().isLogined() && !SPUtils.isTokenExpire(error))
            user = SPMobileApplication.getInstance().getLoginUser();
        if (user != null) {
            headPic = user.getHeadPic();
            balance = String.valueOf(user.getUserMoney());
            point = String.valueOf(user.getPayPoints());
            collectCount = user.getCollectCount();
            focsCount = user.getFocusCount();
            recordCount = user.getVisitCount();
            returnCount = user.getReturnCount();
            waitPay = user.getWaitPay();
            waitSend = user.getWaitSend();
            waitReceive = user.getWaitReceive();
            couponCount = user.getCouponCount();
            unCommentCount = user.getUnCommentCount();
            if (!(SPStringUtils.isEmpty(user.getMobile()) && SPStringUtils.isEmpty(user.getNickName())))
                nickName = SPStringUtils.isEmpty(user.getNickName()) ? user.getMobile() : user.getNickName();
            if (!SPStringUtils.isEmpty(headPic)) {
                SPMobileConstants.KEY_HEAD_PIC = SPUtils.getImageUrl(headPic);
                nickImage.setImageURI(SPUtils.getImageUri(getActivity(), SPMobileConstants.KEY_HEAD_PIC));
            } else {
                SPMobileConstants.KEY_HEAD_PIC = "";
                Uri uri = new Uri.Builder().scheme("res").path(String.valueOf(com.yw.car.R.drawable.person_default_head)).build();
                nickImage.setImageURI(uri);
            }
        } else {
            waitPay = 0;
            waitSend = 0;
            waitReceive = 0;
            unCommentCount = 0;
            returnCount = 0;
            Uri uri = new Uri.Builder().scheme("res").path(String.valueOf(com.yw.car.R.drawable.person_default_head)).build();
            nickImage.setImageURI(uri);
        }
        favTxt.setText(collectCount + "");
        likeTxt.setText(focsCount + "");
        hisTxt.setText(recordCount + "");
        balanceTxtv.setText(balance);
        pointTxtv.setText(point);
        if (waitPay != 0) {
            if (waitPay < 10)
                badWaitPay.setBadgeMargin(40, 25);
            else
                badWaitPay.setBadgeMargin(30, 25);
            badWaitPay.setText("" + waitPay);
            badWaitPay.show();
        } else {
            badWaitPay.hide();
        }
        if (waitSend != 0) {
            if (waitSend < 10)
                badWaitSend.setBadgeMargin(40, 25);
            else
                badWaitSend.setBadgeMargin(30, 25);
            badWaitSend.setText("" + waitSend);
            badWaitSend.show();
        } else {
            badWaitSend.hide();
        }
        if (waitReceive != 0) {
            if (waitReceive < 10)
                badWaitReceive.setBadgeMargin(40, 25);
            else
                badWaitReceive.setBadgeMargin(30, 25);
            badWaitReceive.setText("" + waitReceive);
            badWaitReceive.show();
        } else {
            badWaitReceive.hide();
        }
        if (unCommentCount != 0) {
            if (unCommentCount < 10)
                badUnComment.setBadgeMargin(40, 25);
            else
                badUnComment.setBadgeMargin(30, 25);
            badUnComment.setText("" + unCommentCount);
            badUnComment.show();
        } else {
            badUnComment.hide();
        }
        if (returnCount != 0) {
            if (returnCount < 10)
                badReturnGoods.setBadgeMargin(40, 25);
            else
                badReturnGoods.setBadgeMargin(30, 25);
            badReturnGoods.setText("" + returnCount);
            badReturnGoods.show();
        } else {
            badReturnGoods.hide();
        }
        if (SPStringUtils.isEmpty(couponCount))
            couponCountTxtv.setText("0");
        else
            couponCountTxtv.setText(String.valueOf(couponCount));
        nicknameTxtv.setText(nickName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.yw.car.R.id.person_order_aview:                    //我的订单
                if (!checkLogin()) return;
                startupOrderList(SPOrderUtils.OrderStatus.all.value());
                break;
            case com.yw.car.R.id.personal_order_waitpay_layout:         //待付款
                if (!checkLogin()) return;
                startupOrderList(SPOrderUtils.OrderStatus.waitPay.value());
                break;
            case com.yw.car.R.id.personal_order_waitsend_layout:        //待发货
                if (!checkLogin()) return;
                startupOrderList(SPOrderUtils.OrderStatus.waitSend.value());
                break;
            case com.yw.car.R.id.personal_order_waitreceive_layout:     //待收货
                if (!checkLogin()) return;
                startupOrderList(SPOrderUtils.OrderStatus.waitReceive.value());
                break;
            case com.yw.car.R.id.personal_order_waitcomment_layout:     //待评价
                if (!checkLogin()) return;
                Intent orderIntent = new Intent(getActivity(), SPCommentCenterActivity_.class);
                SPUser user = SPMobileApplication.getInstance().getLoginUser();
                orderIntent.putExtra("had", user.getCommentCount());
                orderIntent.putExtra("no", user.getUnCommentCount());
                orderIntent.putExtra("serve", user.getServiceCount());
                startActivity(orderIntent);
                break;
            case com.yw.car.R.id.personal_order_returned:               //退换货
                if (!checkLogin()) return;
                checkTokenPastDue();
                break;
            case com.yw.car.R.id.person_collect_aview:                  //资产管理
            case com.yw.car.R.id.my_points:
            case com.yw.car.R.id.my_balance:
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPCapitalManageActivity_.class));
                break;
            case com.yw.car.R.id.person_receive_address_aview:          //收货地址
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPConsigneeAddressListActivity_.class));
                break;
            case com.yw.car.R.id.person_coupon_aview:                   //优惠券
            case com.yw.car.R.id.my_coupon:
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPCouponListActivity_.class));
                break;
            case com.yw.car.R.id.person_system_setting:                 //设置
            case com.yw.car.R.id.setting_btn:
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPSettingListActivity_.class));
                break;

            case com.yw.car.R.id.virtual_order_aview:                   //虚拟订单
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPVirtualOrderActivity_.class));
                break;
            case com.yw.car.R.id.nickname_txtv:                         //个人资料
            case com.yw.car.R.id.head_mimgv:
                if (!checkLogin()) return;
                loginOrDetail();
                break;
            case com.yw.car.R.id.person_about_us:                       //关于我们
                Intent intent2 = new Intent(getActivity(), SPCommonWebActivity.class);
                intent2.putExtra(SPMobileConstants.KEY_WEB_TITLE, getResources().getString(com.yw.car.R.string.person_about_us));
                intent2.putExtra(SPMobileConstants.KEY_WEB_URL, SPMobileConstants.URL_ABOUT_US);
                startActivity(intent2);
                break;
            case com.yw.car.R.id.person_feedback:                       //意见反馈
                if (!checkLogin()) return;
                break;
            case com.yw.car.R.id.person_coupon_center:                  //领券中心
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPCouponCenterActivity_.class));
                break;
            case com.yw.car.R.id.person_distribution_center:            //分销中心

                break;
            case com.yw.car.R.id.person_xxtm_center:                    //线下同盟
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPOfflineAllianceActivity_.class));
                break;
            case com.yw.car.R.id.person_create_store:                   //我要开店
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPMerchantsSettledActivity_.class));
                break;
            case com.yw.car.R.id.message_btn:                           //消息中心
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPMessageCenterActivity_.class));
                break;
            case com.yw.car.R.id.collectLL:                             //收藏
                if (!checkLogin()) return;
                Intent intent3 = new Intent(getActivity(), SPCollectListActivity_.class);
                intent3.putExtra("index", 0);
                startActivity(intent3);
                break;
            case com.yw.car.R.id.focusLL:                               //关注
                if (!checkLogin()) return;
                Intent intent4 = new Intent(getActivity(), SPCollectListActivity_.class);
                intent4.putExtra("index", 1);
                startActivity(intent4);
                break;
            case com.yw.car.R.id.recordLL:                              //记录
                if (!checkLogin()) return;
                startActivity(new Intent(getActivity(), SPBrowsingHistoryActivity_.class));
                break;
            case R.id.person_car_info:
                if (!checkLogin()) return;
                Intent carintent = new Intent(getActivity(), SPSaveCarInfoActivity_.class);
                carintent.putExtra("isShow", true);
                startActivity(carintent);
                break;
        }
    }

    private void loginOrDetail() {
        if (SPMobileApplication.getInstance().isLogined())
            startActivity(new Intent(getActivity(), SPUserDetailsActivity_.class));
        else
            startActivity(new Intent(getActivity(), SPLoginActivity_.class));
    }

    //校验token是否过期
    public void checkTokenPastDue() {
        SPUserRequest.getTokenStatus(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                toExchange();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                if (SPUtils.isTokenExpire(errorCode))
                    toLoginPage();      //跳转到登录页
            }
        });
    }

    public void toExchange() {
        Intent intent1 = new Intent(getActivity(), SPCommonWebActivity.class);
        intent1.putExtra(SPMobileConstants.KEY_WEB_TITLE, getResources().getString(com.yw.car.R.string.title_exchange_list));
        intent1.putExtra(SPMobileConstants.KEY_WEB_URL, SPMobileConstants.URL_RETURN_LIST);
        startActivity(intent1);
    }

    boolean checkLogin() {
        if (!SPMobileApplication.getInstance().isLogined() || SPUtils.isTokenExpire(error)) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }

    public void startupOrderList(int orderStatus) {
        Intent allOrderList = new Intent(getActivity(), SPOrderListActivity_.class);
        allOrderList.putExtra("orderStatus", orderStatus);
        startActivity(allOrderList);
    }

}
