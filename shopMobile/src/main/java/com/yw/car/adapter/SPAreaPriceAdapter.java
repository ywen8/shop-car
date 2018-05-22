package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yw.car.R;
import com.yw.car.model.car.SPAreaPrice;

import java.util.List;

/**
 * Created by yw on 2018/2/27.
 */

public class SPAreaPriceAdapter extends RecyclerView.Adapter<SPAreaPriceAdapter.MyViewHolder> {
    private List<SPAreaPrice> list;
    private Context mContext;

    public SPAreaPriceAdapter(Context context, List<SPAreaPrice> list) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_areaprice, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SPAreaPrice areaPrice = list.get(position);
        holder.price.setText(areaPrice.price+"万元");
        holder.name.setText(areaPrice.area);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.areaprice_item_name);
            price = (TextView) itemView.findViewById(R.id.areaprice_item_price);
        }
    }
}
