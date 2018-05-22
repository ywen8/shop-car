package com.yw.car.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.activity.shop.SPConfirmOrderActivity_;
import com.yw.car.activity.shop.SPConfirmVirtualOrderActivity_;
//import com.yw.car.model.activity.shop.SPGroupProductDetailActivity_;
import com.yw.car.activity.shop.SPProductDetailActivity;
import com.yw.car.adapter.SPProductSpecListAdapter;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.entity.SerializableMap;
import com.yw.car.global.SPSaveData;
import com.yw.car.global.SPShopCartManager;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.model.SPProduct;
import com.yw.car.model.SPSpecPriceModel;
import com.yw.car.model.shop.SPProductSpec;
import com.yw.car.utils.SPShopUtils;
import com.yw.car.utils.SPUtils;
import com.yw.car.widget.tagview.Tag;
import com.yw.car.widget.tagview.TagListView;
import com.yw.car.widget.tagview.TagView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqh on 2016/6/20
 */
public class BottomPopUpDialog extends DialogFragment implements TagListView.OnTagClickListener, View.OnClickListener {

    View cartView;
    int storeCount;
    Button plusBtn;
    int mCartCount;
    View mFooterView;
    TextView skuTxtv;
    TextView nameTxtv;
    ImageView picImgv;
    ListView specListv;
    TextView priceTxtv;
    EditText cartCountEtxtv;
    TextView storeCountTxtv;
    private boolean isGroup;                                                    //是否是拼团
    private Builder mBuilder;
    Button addCartBtn, buyBtn;
    boolean isDispatch = true;                                                  //是否可以配送
    private LinearLayout joinLl;
    public String price = "0.0";
    SPProductDetailActivity activity;
    SPProductSpecListAdapter specAdapter;
    private Map<String, String> selectSpecMap;                                  //保存选择的规格ID
    public Map<String, SPSpecPriceModel> specPriceMap;                          //存储规格对应的价格
    Map<String, List<SPProductSpec>> productSpecGroupMap;

    public static BottomPopUpDialog getInstance(Builder builder) {
        BottomPopUpDialog dialog = new BottomPopUpDialog();
        dialog.mBuilder = builder;
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SPProductDetailActivity) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //该方法需要放在onViewCreated比较合适,若在onStart在部分机型(如:小米3)会出现闪烁的情况
        getDialog().getWindow().setWindowAnimations(com.yw.car.R.style.bottom_dialogAnim);            //显示动画bottom_dialogAnim,BottomDialogAnimation
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;            //底部弹出的DialogFragment的高度,如果是MATCH_PARENT则铺满整个窗口
        params.horizontalMargin = 0;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, com.yw.car.R.style.dialog_bottom_full);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.yw.car.R.layout.product_details_spec, null);
        mFooterView = inflater.inflate(com.yw.car.R.layout.product_spec_list_footer, null);
        this.productSpecGroupMap = activity.getSepcGroupMap();
        this.selectSpecMap = activity.getSelectSpecMap();
        this.specPriceMap = activity.getSpecPriceMap();
        initView(view);
        registerListener(view);
        return view;
    }

    private void initView(View view) {
        picImgv = (ImageView) view.findViewById(com.yw.car.R.id.product_pic_imgv);
        nameTxtv = (TextView) view.findViewById(com.yw.car.R.id.tv_name);
        priceTxtv = (TextView) view.findViewById(com.yw.car.R.id.tv_price);
        skuTxtv = (TextView) view.findViewById(com.yw.car.R.id.tv_sku);
        joinLl = (LinearLayout) view.findViewById(com.yw.car.R.id.join_ll);
        TextView joinTxt = (TextView) view.findViewById(com.yw.car.R.id.join_txt);
        joinTxt.setOnClickListener(this);
        plusBtn = (Button) view.findViewById(com.yw.car.R.id.cart_plus_btn);
        specListv = (ListView) view.findViewById(com.yw.car.R.id.product_spec_lstv);
        specListv.addFooterView(mFooterView);
        cartView = view.findViewById(com.yw.car.R.id.product_spec_cart_layout);
        storeCountTxtv = (TextView) view.findViewById(com.yw.car.R.id.product_spec_store_count_txtv);
        addCartBtn = (Button) view.findViewById(com.yw.car.R.id.add_cart_btn);
        buyBtn = (Button) view.findViewById(com.yw.car.R.id.buy_btn);
        if (activity.getProduct().getIsVirtual() == 1 || activity.getProduct().getExchangeIntegral() > 0) {
            if (activity.getProduct().getIsVirtual() != 1)
                buyBtn.setText("立即兑换");
            addCartBtn.setVisibility(View.GONE);
        }
        addCartBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        cartCountEtxtv = (EditText) mFooterView.findViewById(com.yw.car.R.id.cart_count_dtxtv);
        mFooterView.findViewById(com.yw.car.R.id.cart_minus_btn).setOnClickListener(this);
        mFooterView.findViewById(com.yw.car.R.id.cart_plus_btn).setOnClickListener(this);
        Collection<String> selectSpecs = (selectSpecMap == null) ? null : selectSpecMap.values();
        specAdapter = new SPProductSpecListAdapter(activity, this, selectSpecs);
        specListv.setAdapter(specAdapter);
        if (!SPStringUtils.isEmpty(mBuilder.product.getGoodsID())) {
            String imgUrl = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mBuilder.product.getGoodsID());
            Glide.with(this).load(imgUrl).asBitmap().fitCenter().placeholder(com.yw.car.R.drawable.icon_product_null)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(picImgv);
        }
        specAdapter.setData(productSpecGroupMap);
        specAdapter.notifyDataSetChanged();
        refreshPrice();
    }

    /**
     * 点击空白处,隐藏dialog
     */
    private void registerListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    dismiss();
                return false;
            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPrice(String price) {
        this.price = price;
        if (priceTxtv != null)
            priceTxtv.setText(price);
    }

    public void setCount(int count) {
        this.storeCount = count;
        if (storeCountTxtv != null)
            refreshPrice();
    }

    public void canDispatch(boolean isDispatch) {
        this.isDispatch = isDispatch;
    }

    public void isGroupTeam(boolean isGroup) {
        this.isGroup = isGroup;
        if (joinLl != null)
            refreshPrice();
    }

    @Override
    public void onTagClick(TagView tagView, Tag tag) {
        selectSpecMap.put(tag.getKey(), tag.getValue());
        sendSpecChangeBroadCast();
        refreshPrice();
    }

    public void sendSpecChangeBroadCast() {
        Intent specIntent = new Intent(SPMobileConstants.ACTION_SPEC_CHNAGE);
        SerializableMap serializableMap = new SerializableMap();
        serializableMap.setMap(selectSpecMap);
        specIntent.putExtra("spec", serializableMap);
        int selectCount;
        try {
            selectCount = Integer.valueOf(cartCountEtxtv.getText().toString());
        } catch (Exception e) {
            selectCount = 0;
            e.printStackTrace();
        }
        specIntent.putExtra("count", selectCount);
        getActivity().sendBroadcast(specIntent);
    }

    /**
     * 刷新价格
     */
    private void refreshPrice() {
        int cartCount = loadSelectCount();
        cartCountEtxtv.setText(String.valueOf(cartCount));
        nameTxtv.setText(mBuilder.product.getGoodsName());
        if (selectSpecMap == null || selectSpecMap.size() < 1) {       //无规格
            if (activity.getProduct().getExchangeIntegral() > 0) {       //金豆商品
                priceTxtv.setText(price);
            } else {
                if (activity.getProduct().getActivity().getPromType() == 1
                        || activity.getProduct().getActivity().getPromType() == 2
                        || activity.getProduct().getActivity().getPromType() == 3)       //活动商品显示活动价
                    price = activity.getProduct().getActivity().getPromPrice() + "";
                else
                    price = mBuilder.product.getShopPrice();
                priceTxtv.setText("¥" + price);
            }
            if (activity.getProduct().getActivity().getPromType() == 1 || activity.getProduct().getActivity().getPromType() == 2)
                storeCount = activity.getProduct().getActivity().getPromStoreCount();
            else
                storeCount = mBuilder.product.getStoreCount();
            if (activity.getProduct().getActivity().getPromType() == 6)
                joinLl.setVisibility(View.VISIBLE);
            else
                joinLl.setVisibility(View.GONE);
        } else {       //有规格
            String priceModelKey = SPShopUtils.getPriceModelkey(selectSpecMap.values());
            if (activity.getProduct().getExchangeIntegral() > 0) {        //金豆商品
                priceTxtv.setText(price);
            } else {
                price = activity.getProduct().getShopPrice();
                priceTxtv.setText("¥" + price);
            }
            if (storeCountTxtv == null)
                storeCount = SPShopUtils.getShopStoreCount(priceModelKey, specPriceMap);
            String sku = SPShopUtils.getGoodsSku(priceModelKey, specPriceMap);
            if (!SPStringUtils.isEmpty(sku))
                skuTxtv.setText("商品编号: " + sku);
            else
                skuTxtv.setText("商品编号: 无");
            if (isGroup)
                joinLl.setVisibility(View.VISIBLE);
            else
                joinLl.setVisibility(View.GONE);
        }
        if (storeCount > 0 && isDispatch) {
            addCartBtn.setEnabled(true);
            buyBtn.setEnabled(true);
            addCartBtn.setBackgroundResource(com.yw.car.R.drawable.button_yellow_selector);
            buyBtn.setBackgroundResource(com.yw.car.R.drawable.button_selector);
        } else {
            addCartBtn.setEnabled(false);
            buyBtn.setEnabled(false);
            addCartBtn.setBackgroundColor(mBuilder.activity.getResources().getColor(com.yw.car.R.color.button_bg_gray));
            buyBtn.setBackgroundColor(mBuilder.activity.getResources().getColor(com.yw.car.R.color.gray));
        }
        String residue = "数量(剩余" + storeCount + "件)";
        SpannableString residueSpanStr = getResidueSpanString(residue);
        storeCountTxtv.setText(residueSpanStr);
    }

    public SpannableString getResidueSpanString(String residue) {
        int startIndex = 2;
        int endIndex = residue.length();
        SpannableString residueSpanStr = new SpannableString(residue);
        residueSpanStr.setSpan(new ForegroundColorSpan(mBuilder.activity.getResources().getColor(com.yw.car.R.color.light_red)), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);          //设置区域内文字颜色为洋红色
        return residueSpanStr;
    }

    public void onButtonClick(final View v) {
        mCartCount = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
        switch (v.getId()) {
            case com.yw.car.R.id.join_txt:               //进入拼团商品详情
               /* String priceModelKey = SPShopUtils.getPriceModelkey(selectSpecMap.values());
                int itemId = SPShopUtils.getItemId(priceModelKey, specPriceMap);
                int promId = SPShopUtils.getPromId(priceModelKey, specPriceMap);
                Intent teamIntent = new Intent(activity, SPGroupProductDetailActivity_.class);
                teamIntent.putExtra("team_id", promId + "");
                teamIntent.putExtra("goods_id", activity.getProduct().getGoodsID());
                teamIntent.putExtra("item_id", itemId + "");
                startActivity(teamIntent);*/
                break;
            case com.yw.car.R.id.cart_minus_btn:         //减少数量
                if (mCartCount <= 1) {
                    activity.showToast(mBuilder.activity.getString(com.yw.car.R.string.toast_count_not_small_zero));
                    return;
                }
                mCartCount--;
                cartCountEtxtv.setText(String.valueOf(mCartCount));
                cacheCartCount(mCartCount);
                sendSpecChangeBroadCast();
                break;
            case com.yw.car.R.id.cart_plus_btn:          //增加数量
                if (mCartCount >= storeCount) {
                    activity.showToast(getString(com.yw.car.R.string.toast_low_stocks));
                    return;
                }
                mCartCount++;
                cartCountEtxtv.setText(String.valueOf(mCartCount));
                cacheCartCount(mCartCount);
                sendSpecChangeBroadCast();
                break;
            case com.yw.car.R.id.add_cart_btn:            //加入购物车
                Integer count1 = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
                if (count1 < 1) {
                    activity.showToast(mBuilder.activity.getString(com.yw.car.R.string.toast_not_datal));
                    return;
                }
                String priceModelKey1 = SPShopUtils.getPriceModelkey(selectSpecMap.values());
                int itemId1 = SPShopUtils.getItemId(priceModelKey1, specPriceMap);
                SPShopCartManager.getInstance(activity).shopCartGoodsOperation(activity.getProduct().getGoodsID(), itemId1,
                        count1, new SPSuccessListener() {
                            @Override
                            public void onRespone(String msg, Object response) {
                                activity.showSuccessToast(msg);
                                dismiss();
                            }
                        }, new SPFailuredListener() {
                            @Override
                            public void onRespone(String msg, int errorCode) {
                                if (msg != null && !msg.equals("")) {
                                    if (SPUtils.isTokenExpire(errorCode)) {
                                        activity.showToastUnLogin();
                                        activity.toLoginPage();
                                    } else {
                                        activity.showFailedToast(msg);
                                    }
                                }
                            }
                        });
                break;
            case com.yw.car.R.id.buy_btn:                 //立即购买(立即兑换)
                Integer count2 = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
                if (count2 < 1) {
                    activity.showToast(mBuilder.activity.getString(com.yw.car.R.string.toast_not_datal));
                    return;
                }
                String priceModelKey2 = SPShopUtils.getPriceModelkey(selectSpecMap.values());
                int itemId2 = SPShopUtils.getItemId(priceModelKey2, specPriceMap);
                if (activity.getProduct().getIsVirtual() == 1) {           //虚拟商品立即购买
                    Intent intent = new Intent(activity, SPConfirmVirtualOrderActivity_.class);
                    intent.putExtra("good", activity.getProduct());
                    intent.putExtra("num", count2);
                    intent.putExtra("itemId", itemId2);
                    activity.startActivity(intent);
                    return;
                }
                if (activity.getProduct().getExchangeIntegral() > 0) {           //金豆商品立即兑换
                    Intent confirmIntent = new Intent(getActivity(), SPConfirmOrderActivity_.class);
                    confirmIntent.putExtra("isIntegralGood", true);
                    confirmIntent.putExtra("goodId", activity.getProduct().getGoodsID());
                    confirmIntent.putExtra("itemId", itemId2 + "");
                    confirmIntent.putExtra("num", count2 + "");
                    activity.startActivity(confirmIntent);
                    return;
                }
                Intent confirmIntent = new Intent(getActivity(), SPConfirmOrderActivity_.class);        //普通商品立即购买
                confirmIntent.putExtra("isBuyNow", true);
                confirmIntent.putExtra("goodId", activity.getProduct().getGoodsID());
                confirmIntent.putExtra("itemId", itemId2 + "");
                confirmIntent.putExtra("num", count2 + "");
                activity.startActivity(confirmIntent);
                break;
        }
    }

    public void cacheCartCount(int count) {
        SPSaveData.putValue(activity, SPMobileConstants.KEY_CART_COUNT, count);
    }

    public int loadSelectCount() {
        return SPSaveData.getInteger(activity, SPMobileConstants.KEY_CART_COUNT, 1);
    }

    @Override
    public void onClick(View v) {
        onButtonClick(v);
    }

    public static class Builder {
        Activity activity;
        private SPProduct product;

        //设置数据
        public Builder setDialogData(Activity activity, SPProduct product) {
            this.product = product;
            this.activity = activity;
            return this;
        }

        public BottomPopUpDialog create() {
            return BottomPopUpDialog.getInstance(this);
        }
    }

}
