package com.wangcong.warehousefirewarning.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangcong.warehousefirewarning.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 13307 on 2018/1/10.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<DataBean> recordList;
    DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    static class ViewHolder extends RecyclerView.ViewHolder {
        View dataView;
        TextView data_time;
        TextView data_tem;
        TextView data_hum;
        TextView data_smoke;


        public ViewHolder(View view) {
            super(view);
            dataView = view;
            data_time = (TextView) view.findViewById(R.id.data_time);
            data_tem = (TextView) view.findViewById(R.id.data_tem);
            data_hum = (TextView) view.findViewById(R.id.data_hum);
            data_smoke = (TextView) view.findViewById(R.id.data_smoke);
        }
    }

    public RecordAdapter(List<DataBean> reList) {
        recordList = reList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.fruitView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Fruit fruit = recordList.get(position);
//                Toast.makeText(v.getContext(), "you clicked view " + fruit.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Fruit fruit = recordList.get(position);
//                Toast.makeText(v.getContext(), "you clicked image " + fruit.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataBean data = recordList.get(position);
        Date date = new Date(Long.valueOf(data.getTimestamp()));
        holder.data_time.setText(sdf.format(date).toString());
        holder.data_tem.setText(String.valueOf(data.getTem()));
        holder.data_hum.setText(String.valueOf(data.getHum()));
        holder.data_smoke.setText(String.valueOf(data.getSmoke()));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

}