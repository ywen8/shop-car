package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yw.car.R;
import com.yw.car.model.car.SPMotorcycle;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */

public class SPModelTypeAdapter extends RecyclerView.Adapter<SPModelTypeAdapter.MyViewHolder> {
    public List<SPMotorcycle> brands;
    public Context mContext;
    private CarTypeItemListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartype_item, parent, false);
        SPModelTypeAdapter.MyViewHolder carTypeAdapter = new SPModelTypeAdapter.MyViewHolder(view);

        return carTypeAdapter;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SPMotorcycle spCarBrand = brands.get(position);
        holder.chepinpai.setText(spCarBrand.getModelName());
        holder.chepinpai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.carTypeItemClick(spCarBrand);

            }
        });
    }

    public SPModelTypeAdapter(List<SPMotorcycle> brands, Context mContext, CarTypeItemListener listener) {
        this.brands = brands;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chepinpai;

        public MyViewHolder(View itemView) {
            super(itemView);
            chepinpai = (TextView) itemView.findViewById(R.id.chepinpai);
        }
    }

    public interface CarTypeItemListener {
        void carTypeItemClick(SPMotorcycle name);
    }
}
