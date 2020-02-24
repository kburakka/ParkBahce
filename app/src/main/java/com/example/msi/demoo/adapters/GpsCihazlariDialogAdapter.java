package com.example.msi.demoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msi.demoo.R;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.BtDevice;

import java.util.List;

public class GpsCihazlariDialogAdapter extends RecyclerView.Adapter<GpsCihazlariDialogAdapter.ViewHolder> {
    List<BtDevice> deviceList;
    CustomItemClickListener listener;
    Activity activity;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView gpsCihazlariItemImage;
        public TextView  gpsCihazlariItemName;
        public TextView gpsCihazlariItemBaglandi;
        public ImageView gpsCihazlariItemImageConnected;

        public ViewHolder(View view) {
            super(view);
            gpsCihazlariItemImage= view.findViewById(R.id.gps_cihazlari_item_image);
            gpsCihazlariItemName = view.findViewById(R.id.gps_cihazlari_item_name);
            gpsCihazlariItemBaglandi = view.findViewById(R.id.gps_cihazlari_item_baglandi);
            gpsCihazlariItemImageConnected = view.findViewById(R.id.gps_cihazlari_item_connected);
        }
    }

    public GpsCihazlariDialogAdapter(List<BtDevice> list, CustomItemClickListener listener, Activity activity, Context context) {
        this.deviceList = list;
        this.listener = listener;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public GpsCihazlariDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gps_cihazlari_dialog, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, view_holder.getPosition());
            }
        });

        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(deviceList.get(position).getBluetoothDevice().getName() != null)
            holder.gpsCihazlariItemName.setText(deviceList.get(position).getBluetoothDevice().getName());
        else
            holder.gpsCihazlariItemName.setText(deviceList.get(position).getBluetoothDevice().getAddress());


        if(deviceList.get(position).isConnectStatu()){
            holder.gpsCihazlariItemImageConnected.setVisibility(View.VISIBLE);
            holder.gpsCihazlariItemBaglandi.setVisibility(View.VISIBLE);
        }else{
            holder.gpsCihazlariItemImageConnected.setVisibility(View.GONE);
        }

        if(!deviceList.get(position).getConnectInfo().equals("")){
            holder.gpsCihazlariItemBaglandi.setVisibility(View.VISIBLE);
            holder.gpsCihazlariItemBaglandi.setText(deviceList.get(position).getConnectInfo());
        }else
            holder.gpsCihazlariItemBaglandi.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
