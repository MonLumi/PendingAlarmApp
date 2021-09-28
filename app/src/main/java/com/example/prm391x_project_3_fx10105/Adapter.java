package com.example.prm391x_project_3_fx10105;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public interface IAlarmList{
        void onAlarmClicked(int index);
    }

    ArrayList<Alarm> list;
    Context mContext;
    IAlarmList toListFrag;

    public Adapter(ArrayList<Alarm> list, Context context) {
        this.list = list;
        this.mContext = context;
        this.toListFrag = (IAlarmList) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm alarm = list.get(position);
        holder.tvTitle.setText(alarm.getTitle());
        holder.tvTime.setText(alarm.getShowTime());

        ImageView ivIndicator = holder.ivIndicator;
        if (alarm.getStatus().equals("online")) ivIndicator.setBackgroundColor(mContext.getColor(R.color.light_orange));
        if (alarm.getStatus().equals("stopped")) ivIndicator.setBackgroundColor(mContext.getColor(R.color.red));
        if (alarm.getStatus().equals("done")) ivIndicator.setBackgroundColor(mContext.getColor(R.color.green));

        holder.itemView.setTag((int) position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvTime;
        ImageView ivIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.recycle_title);
            tvTime = itemView.findViewById(R.id.recycle_time);
            ivIndicator = itemView.findViewById(R.id.recycle_indicator);

            itemView.setOnClickListener(view -> toListFrag.onAlarmClicked((int) itemView.getTag()));
        }
    }

}
