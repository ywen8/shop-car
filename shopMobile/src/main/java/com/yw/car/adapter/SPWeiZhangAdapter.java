package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yw.car.R;
import com.yw.car.model.car.SPRecord;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */

public class SPWeiZhangAdapter extends RecyclerView.Adapter<SPWeiZhangAdapter.MyViewHolder> {

    private List<SPRecord> list;
    private Context mContext;

    public SPWeiZhangAdapter(List<SPRecord> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weizhang_item, parent, false);
        SPWeiZhangAdapter.MyViewHolder viewHolder = new SPWeiZhangAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SPRecord record = list.get(position);
        holder.fen.setText(record.fen + "分");
        holder.miaoshu.setText(record.act);
        if (Integer.valueOf(record.handled) == 1) {
            holder.chuli.setText("已处理");
        } else {
            holder.chuli.setText("未处理");
        }
        holder.date.setText(record.date);
        holder.money.setText(record.money+"元");
        holder.quyu.setText(record.area);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fen;
        TextView money;
        TextView miaoshu;
        TextView date;
        TextView chuli;
        TextView quyu;

        public MyViewHolder(View itemView) {
            super(itemView);
            fen = (TextView) itemView.findViewById(R.id.fen);
            money = (TextView) itemView.findViewById(R.id.money);
            miaoshu = (TextView) itemView.findViewById(R.id.miaoshu);
            date = (TextView) itemView.findViewById(R.id.date);
            chuli = (TextView) itemView.findViewById(R.id.chuli);
            quyu = (TextView) itemView.findViewById(R.id.quyu);
        }
    }
}
