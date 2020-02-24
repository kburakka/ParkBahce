package com.example.msi.demoo.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.adapters.MediaFragmentAdapter;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.KatmanMedia;

import java.util.List;

@SuppressLint("ValidFragment")
public class MediaFragment extends Fragment {
    private static final String TAG = "MediaFragment";

    private ImageView mediaLeft;
    private ImageView mediaRight;
    private RecyclerView mediaRecyclerView;
    private List<KatmanMedia> mediaList;
    private int position;
    private Context context;
    private Activity activity;

    public MediaFragment(List<KatmanMedia> mediaListForRecycler, int position, Context context, Activity activity){
        this.mediaList = mediaListForRecycler;
        this.position = position;
        this.context = context;
        this.activity = activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media, null);
        MainActivity.mainActivityCurrentObject.toolbar.setVisibility(View.GONE);

        mediaLeft = rootView.findViewById(R.id.dialog_media_left);
        mediaRight = rootView.findViewById(R.id.dialog_media_right);



        ImageView kapat= rootView.findViewById(R.id.dialog_media_close);
        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMediaFragment();
            }
        });

        mediaRecyclerView = rootView.findViewById(R.id.dialog_media_recycler);
        LinearLayoutManager layoutManagerMedia = new LinearLayoutManager(getActivity());
        layoutManagerMedia.setOrientation(LinearLayoutManager.HORIZONTAL);

        mediaRecyclerView.setLayoutManager(layoutManagerMedia);


        MediaFragmentAdapter katmanFragmentMediaAdapter = new MediaFragmentAdapter(mediaList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("position", "Tıklanan Pozisyon:" + position);
                KatmanMedia media = mediaList.get(position);
            }
        }, context, activity);

        mediaRecyclerView.setHasFixedSize(true);
        mediaRecyclerView.setAdapter(katmanFragmentMediaAdapter);
        mediaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mediaRecyclerView.scrollToPosition(position);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mediaRecyclerView);

        mediaRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                position = layoutManagerMedia.findFirstVisibleItemPosition();
                setTheLeftRightVisibility();
            }
        });

        setTheLeftRightVisibility();

        mediaLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position > 0){
                    position--;
                    mediaRecyclerView.scrollToPosition(position);
                }
            }
        });

        mediaRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < mediaList.size()-1){
                    position++;
                    mediaRecyclerView.scrollToPosition(position);
                }
            }
        });

        return rootView;
    }

    private void setTheLeftRightVisibility(){

        if(position > 0)
            mediaLeft.setVisibility(View.VISIBLE);
        else
            mediaLeft.setVisibility(View.GONE);

        if(position< mediaList.size()-1)
            mediaRight.setVisibility(View.VISIBLE);
        else
            mediaRight.setVisibility(View.GONE);

    }

    public void closeMediaFragment(){
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("BROADCAST_MEDIA_FRAG"));
        MainActivity.fragment = getActivity().getSupportFragmentManager().findFragmentByTag("KatmanFragment");
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
