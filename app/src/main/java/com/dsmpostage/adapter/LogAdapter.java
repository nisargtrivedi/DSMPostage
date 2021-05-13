package com.dsmpostage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsmpostage.R;
import com.dsmpostage.model.tblLog;

import java.util.ArrayList;
import java.util.List;


public class LogAdapter extends RecyclerView.Adapter<LogAdapter.Holder> {

    List<tblLog> list;
    List<tblLog> filterListNormal;
    List<tblLog> filterList;
    Context context;
    LayoutInflater inflater;



    public LogAdapter(Context context, List<tblLog> list) {
        this.context = context;
        this.list = list;
        this.filterList=list;
        this.filterListNormal=list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_data, parent, false);



        return new Holder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        tblLog model = filterList.get(position);
        if (filterList.size() > 0) {

            holder.tvName.setText(model.Name);
            holder.tvNo.setText(model.No);
            holder.tvTime.setText(model.Time);
            holder.tvView.setText("View");

            holder.tvView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(model.SignView!=null && !model.SignView.isEmpty()){
                        try {
                            Uri uri = Uri.parse(model.SignView);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }catch (Exception ex){

                        }
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (filterList!=null && filterList.size()>0)?filterList.size():0;
    }



    public class Holder extends RecyclerView.ViewHolder {
        TextView tvNo,tvTime,tvName,tvView;


        public Holder(View v) {
            super(v);
            tvNo = v.findViewById(R.id.tvNo);
            tvTime = v.findViewById(R.id.tvTime);
            tvName=v.findViewById(R.id.tvName);
            tvView=v.findViewById(R.id.tvView);
        }
    }


}
