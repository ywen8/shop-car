package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yw.car.R;
import com.yw.car.model.car.SPTown;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */

public class SPWeiZhangCityAdapter extends RecyclerView.Adapter<SPWeiZhangCityAdapter.MyViewHolder> {
    public List<SPTown> list;
    public Context mContext;
    private ShengFenSelect shengFenSelect;

    public SPWeiZhangCityAdapter(List<SPTown> list, Context mContext, ShengFenSelect shengFenSelect) {
        this.list = list;
        this.mContext = mContext;
        this.shengFenSelect = shengFenSelect;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartype_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SPTown spProvince = list.get(position);
        holder.shengfen.setText(spProvince.cityname);
        holder.shengfen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shengFenSelect.onClickItemShgneFen(spProvince);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shengfen;

        public MyViewHolder(View itemView) {
            super(itemView);
            shengfen = (TextView) itemView.findViewById(R.id.chepinpai);
        }
    }

    public interface ShengFenSelect {
        void onClickItemShgneFen(SPTown spProvince);
    }
}
