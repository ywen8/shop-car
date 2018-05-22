package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.yw.car.R;

import com.yw.car.common.SPMobileConstants;
import com.yw.car.model.shop.SPFightGroupsGood;

import org.json.JSONException;

import java.util.List;

/**
 * Created by zw on 2018/1/9
 */
public class SPFightGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPFightGroupsGood> fightGroupsGoods;

   /* public SPFightGroupsAdapter(SPFightGroupsActivity activity) {
        this.mContext = activity;
    }*/

    public void updateData(List<SPFightGroupsGood> fightGroupsGoods) {
        if (fightGroupsGoods == null) return;
        this.fightGroupsGoods = fightGroupsGoods;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.fight_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final SPFightGroupsGood fightGroupsGood = fightGroupsGoods.get(position);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, fightGroupsGood.getGoodsId());
        Glide.with(mContext).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.goodsImg);
        holder.goodsName.setText(fightGroupsGood.getActName());
        holder.neederNum.setText(fightGroupsGood.getNeeder() + "人团");
        holder.teamPrice.setText("¥" + fightGroupsGood.getTeamPrice());
        if (fightGroupsGood.getGoodsObj() != null) {
            try {
                holder.shopPrice.setText("单买¥" + fightGroupsGood.getGoodsObj().getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            holder.shopPrice.setText("单买¥" + fightGroupsGood.getShopPrice());
        }
        holder.fightGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (fightGroupsGoods == null) return 0;
        return fightGroupsGoods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImg;
        TextView goodsName;
        TextView neederNum;
        TextView teamPrice;
        TextView shopPrice;
        Button fightGroupsBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImg = (ImageView) itemView.findViewById(R.id.goods_img);
            goodsName = (TextView) itemView.findViewById(R.id.goods_name);
            neederNum = (TextView) itemView.findViewById(R.id.needer_num);
            teamPrice = (TextView) itemView.findViewById(R.id.team_price);
            shopPrice = (TextView) itemView.findViewById(R.id.shop_price);
            fightGroupsBtn = (Button) itemView.findViewById(R.id.fight_groups_btn);
        }
    }

}
