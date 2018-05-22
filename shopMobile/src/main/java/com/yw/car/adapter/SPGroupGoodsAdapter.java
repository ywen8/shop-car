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
 * Date: @date 2015年10月30日 下午10:03:56
 * Description: 分类 -> 左边菜单 adapter
 *
 * @version V1.0
 */
package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.yw.car.R;
//import com.yw.car.model.activity.shop.SPGroupProductDetailActivity_;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.model.shop.SPGroupGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zw
 */
public class SPGroupGoodsAdapter extends RecyclerView.Adapter<SPGroupGoodsAdapter.GroupViewHolder> {

    private Context mContext;
    private List<SPGroupGoods> SPGroupGoods;

    public SPGroupGoodsAdapter(Context context) {
        this.mContext = context;
    }

    public void updateData(List<SPGroupGoods> SPGroupGoods) {
        if (SPGroupGoods == null)
            SPGroupGoods = new ArrayList<>();
        this.SPGroupGoods = SPGroupGoods;
        this.notifyDataSetChanged();
    }

    @Override
    public SPGroupGoodsAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.group_good_item, parent, false);
        return new SPGroupGoodsAdapter.GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SPGroupGoodsAdapter.GroupViewHolder holder, int position) {
        final SPGroupGoods groupGoods = SPGroupGoods.get(position);
        String url = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, groupGoods.getGoodsId());
        Glide.with(mContext).load(url).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodImg);
        holder.goodName.setText(groupGoods.getActName());
        holder.goodPrice.setText("￥" + groupGoods.getTeamPrice());
        holder.goodSale.setText("已拼" + groupGoods.getSalesSum() + "件");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext, SPGroupProductDetailActivity_.class);
                intent.putExtra("team_id", groupGoods.getTeamId());
                intent.putExtra("goods_id", groupGoods.getGoodsId());
                intent.putExtra("item_id", groupGoods.getItemId());
                mContext.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        if (SPGroupGoods != null) return SPGroupGoods.size();
        return 0;
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView goodImg;
        TextView goodName;
        TextView goodSale;
        TextView goodPrice;

        GroupViewHolder(View itemView) {
            super(itemView);
            goodImg = (ImageView) itemView.findViewById(R.id.good_img);
            goodName = (TextView) itemView.findViewById(R.id.good_name);
            goodPrice = (TextView) itemView.findViewById(R.id.good_price);
            goodSale = (TextView) itemView.findViewById(R.id.good_sale);
        }
    }

}
