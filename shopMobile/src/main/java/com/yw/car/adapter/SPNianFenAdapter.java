package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yw.car.R;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */

public class SPNianFenAdapter extends RecyclerView.Adapter<SPNianFenAdapter.MyViewHolder> {
    public List<String> list;
    public Context mContext;
    private CarTypeItemListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartype_item, parent, false);
        SPNianFenAdapter.MyViewHolder carTypeAdapter = new SPNianFenAdapter.MyViewHolder(view);

        return carTypeAdapter;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String nianfen=list.get(position);
        holder.chepinpai.setText(nianfen);
        holder.chepinpai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.carTypeItemClick(nianfen);

            }
        });
    }

    public SPNianFenAdapter(List<String> list, Context mContext, CarTypeItemListener listener) {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chepinpai;

        public MyViewHolder(View itemView) {
            super(itemView);
            chepinpai = (TextView) itemView.findViewById(R.id.chepinpai);
        }
    }

    public interface CarTypeItemListener {
        void carTypeItemClick(String name);
    }
}
