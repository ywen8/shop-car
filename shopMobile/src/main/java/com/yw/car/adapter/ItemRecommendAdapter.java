package com.yw.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.yw.car.R;
import com.yw.car.model.SPProduct;

import java.util.List;

/**
 * item页底部的推荐商品适配器
 */
public class ItemRecommendAdapter implements Holder<List<SPProduct>> {

    private GridView gv_recommend_goods;

    @Override
    public View createView(final Context context) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.item_recommend, null);
        gv_recommend_goods = (GridView) rootview.findViewById(R.id.gv_recommend_goods);
        return rootview;
    }

    @Override
    public void UpdateUI(final Context context, int position, final List<SPProduct> data) {
        gv_recommend_goods.setAdapter(new ItemRecommendGoodsAdapter(context, data));
    }

}
