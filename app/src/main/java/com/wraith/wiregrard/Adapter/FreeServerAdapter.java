package com.wraith.wiregrard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.wraith.wiregrard.Model.VpnModel;
import com.wraith.wiregrard.R;

import java.util.List;

public class FreeServerAdapter extends RecyclerView.Adapter<FreeServerAdapter.MyViewHolder> {

    private List<VpnModel> serverLists;
    private Context mContext;
    private OnSelectListener selectListener;

    public FreeServerAdapter(List<VpnModel> serverLists, Context mContext, OnSelectListener selectListener) {
        this.serverLists = serverLists;
        this.mContext = mContext;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_server_indratech, parent, false);
        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.serverCountry.setText(serverLists.get(position).getCountryName());
      /*  Glide.with(mContext)
                .load(serverLists.get(position).getFlagUrl())
                .into(holder.serverIcon);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectListener.onSelected(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return serverLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView serverIcon;
        TextView serverCountry;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            serverIcon = itemView.findViewById(R.id.flag);
            serverCountry = itemView.findViewById(R.id.countryName);
        }
    }

    public interface OnSelectListener {
        void onSelected(int server);
    }

    public void setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }


    public interface ServerSelected {
        void onServerSelected(int server);
    }
}
