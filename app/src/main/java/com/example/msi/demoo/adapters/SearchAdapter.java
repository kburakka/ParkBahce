package com.example.msi.demoo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.msi.demoo.R;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.SearchMatch;
import com.example.msi.demoo.utils.Utils;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchMatch> searchMatchList;
    CustomItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout searchRelative;
        public TextView searchName;
        public TextView searchTableName;

        public ViewHolder(View view) {
            super(view);

            searchRelative =  view.findViewById(R.id.item_search_relative);
            searchName =  view.findViewById(R.id.item_search_ilce_mah_name);
            searchTableName =  view.findViewById(R.id.item_search_type);
        }

    }

    public SearchAdapter(List<SearchMatch> searchMatchList, CustomItemClickListener listener) {
        this.searchMatchList = searchMatchList;
        this.listener = listener;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SearchMatch searchMatch = searchMatchList.get(position);

        if(searchMatch.getType().equals(Utils.ILCELER)){
            holder.searchName.setText(searchMatch.getIlceAd());
            holder.searchTableName.setText("İlçe");
        }else if(searchMatch.getType().equals(Utils.MAHALLELER)){
            holder.searchName.setText(searchMatch.getMahAd());
            holder.searchTableName.setText("Mahalle");
        }


        holder.searchRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchMatchList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
