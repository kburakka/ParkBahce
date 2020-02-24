package com.example.msi.demoo.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.FileDataInfo;
import com.example.msi.demoo.models.KatmanMedia;
import com.example.msi.demoo.utils.TouchImageView;
import com.example.msi.demoo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MediaFragmentAdapter extends RecyclerView.Adapter<MediaFragmentAdapter.ViewHolder> {
    private static final String TAG = "MediaFragmentAdapter";

    List<KatmanMedia> list;
    List<Boolean> holderPositionList = new ArrayList<>();
    CustomItemClickListener listener;
    Activity activity;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mediaRelative;

        public TouchImageView mediaImage;

        public ViewHolder(View view) {
            super(view);

            mediaRelative = view.findViewById(R.id.media_item_relative);
            mediaImage= view.findViewById(R.id.media_item_imageview);
        }

    }

    public MediaFragmentAdapter(List<KatmanMedia> stringList, CustomItemClickListener listener, Context context, Activity activity) {
        this.list = stringList;
        this.listener = listener;
        this.context = context;
        this.activity = activity;

        for(int i=0; i<stringList.size(); i++)
            holderPositionList.add(false);
    }


    @Override
    public MediaFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_fragment, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, view_holder.getPosition());
            }
        });

        return view_holder;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KatmanMedia currentKatmanMedia = list.get(position);

        if(currentKatmanMedia.getType().equals(Utils.IMAGE)){
//            holder.mediaImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.photo_black));

            holder.mediaImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(currentKatmanMedia.getUri()) // Uri of the picture
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.ic_file_download_32)
                    .error(R.drawable.toast_error)
                    .into(holder.mediaImage);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void writeKnowledgeToText(TextView knowledgeTextView, FileDataInfo fileDataInfo){
        if(fileDataInfo != null){
            String fileInfoStr = fileDataInfo.getUsername() + "\n" + fileDataInfo.getSize()
                    + "\n" +fileDataInfo.getFname()+ "\n" + fileDataInfo.getTouchDate();

            knowledgeTextView.setText(fileInfoStr);
        }else
            knowledgeTextView.setText("Siz : " + MainActivity.percon.getUsername());
    }

}
