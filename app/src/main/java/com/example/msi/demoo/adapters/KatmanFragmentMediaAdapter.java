package com.example.msi.demoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.msi.demoo.R;
import com.example.msi.demoo.fragments.KatmanFragment;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.KatmanMedia;
import com.example.msi.demoo.utils.Utils;

import java.util.List;

public class KatmanFragmentMediaAdapter extends RecyclerView.Adapter<KatmanFragmentMediaAdapter.ViewHolder> {
    private static final String TAG = "KatmanFragmentMediaAdap";

    List<KatmanMedia> list;
    CustomItemClickListener listener;
    Activity activity;
    Context context;
    String katmanType;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout katmanRelative;
        public CardView katmanImageCardView;
        public ImageView katmanImage;
        public ImageView katmanDeleteImageView;

        public ViewHolder(View view) {
            super(view);
            katmanRelative = view.findViewById(R.id.katman_item_relative);

            katmanImageCardView = view.findViewById(R.id.katman_item_imageview_card);
            katmanImage = view.findViewById(R.id.katman_item_imageview);

            katmanDeleteImageView = view.findViewById(R.id.katman_item_delete_imageview);
        }
    }

    public KatmanFragmentMediaAdapter(List<KatmanMedia> stringList, CustomItemClickListener listener, Context context, Activity activity, String katmanType) {
        this.list = stringList;
        this.listener = listener;
        this.context = context;
        this.activity = activity;
        this.katmanType = katmanType;
    }


    @Override
    public KatmanFragmentMediaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_katman_fragment_media, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        
        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KatmanMedia currentKatmanMedia = list.get(position);

        if(currentKatmanMedia.getType().equals(Utils.IMAGE)){
            holder.katmanImageCardView.setVisibility(View.VISIBLE);
            holder.katmanImage.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(currentKatmanMedia.getUri()) // Uri of the picture
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.ic_file_download_32)
                    .error(R.drawable.toast_error)
                    .into(holder.katmanImage);

        }

        holder.katmanRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });

        if(katmanType.equals(KatmanFragment.KATMAN_TYPE_KNOWLEDGE))
            holder.katmanDeleteImageView.setVisibility(View.INVISIBLE);

        holder.katmanDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.toast_warning);
                builder.setTitle(Html.fromHtml("<font color='#45666D'>Uyarı</font>"));
                builder.setMessage(list.get(position).getType() + " silinsin mi?");
                builder.setCancelable(false);
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.show();
               
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
