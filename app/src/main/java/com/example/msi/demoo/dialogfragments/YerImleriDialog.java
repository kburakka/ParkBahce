package com.example.msi.demoo.dialogfragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.adapters.YerImleriDialogAdapter;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.YerImi;
import com.example.msi.demoo.utils.SharedPrefs;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YerImleriDialog extends DialogFragment {



    public List<YerImi> yerImiList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_yer_imleri, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageView kapat = rootView.findViewById(R.id.dialog_yerimleri_close);
        ProgressBar progressBar = rootView.findViewById(R.id.dialog_yerimleri_progress_bar);
        RecyclerView recyclerView = rootView.findViewById(R.id.dialog_yerimleri_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);
        fillTheYerImiList();
        progressBar.setVisibility(View.GONE);


        YerImleriDialogAdapter adapter_items= new YerImleriDialogAdapter(getContext(), getActivity(), yerImiList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                YerImi yerImi = yerImiList.get(position);
                MainActivity.moveCameraPosition(new LatLng(yerImi.getLatitude(),yerImi.getLongitude()), yerImi.getZoom());
                dismiss();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    private void fillTheYerImiList(){
        if(SharedPrefs.hasShared(getActivity(), "yer_imleri")){
            String yerImleriString = SharedPrefs.getShared(getActivity(), "yer_imleri");
            try {
                JSONArray yerImleriJsonArray = new JSONArray(yerImleriString);

                for(int i=0; i<yerImleriJsonArray.length(); i++){
                    JSONObject yerImiJsonObject = yerImleriJsonArray.getJSONObject(i);
                    String name = yerImiJsonObject.getString("name");
                    double zoom = yerImiJsonObject.getDouble("zoom");
                    double latitude = yerImiJsonObject.getDouble("latitude");
                    double longitude = yerImiJsonObject.getDouble("longitude");

                    YerImi yerImi = new YerImi(name, zoom, latitude, longitude);
                    yerImiList.add(yerImi);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
