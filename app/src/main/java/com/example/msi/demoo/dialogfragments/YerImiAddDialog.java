package com.example.msi.demoo.dialogfragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.utils.SharedPrefs;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YerImiAddDialog extends DialogFragment {

    private EditText yerimiEditText;
    private Button iptal, kaydet;
    private ImageView kapat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_yerimi_add, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yerimiEditText = rootView.findViewById(R.id.dialog_yerimi_add_edittext);
        iptal = rootView.findViewById(R.id.dialog_yerimi_add_iptal);
        kaydet = rootView.findViewById(R.id.dialog_yerimi_add_kaydet);
        kapat = rootView.findViewById(R.id.dialog_yerimi_add_close);

        kaydet.setOnClickListener(onClickListener);
        iptal.setOnClickListener(onClickListener);
        kapat.setOnClickListener(onClickListener);

        return rootView;
    }

    private void yerImiEkle(){
        String yerImiName = yerimiEditText.getText().toString().trim();

        LatLngBounds latLngBounds = MainActivity.map.getProjection().getVisibleRegion().latLngBounds;
        LatLng latLngBoundsCenter= latLngBounds.getCenter();
        double zoom = MainActivity.map.getCameraPosition().zoom;

        if(yerImiName.length() != 0){

            JSONArray yerImleriJsonArray = null;
            if(SharedPrefs.hasShared(getActivity(), "yer_imleri")){
                String yerImleri = SharedPrefs.getShared(getActivity(), "yer_imleri");
                try {
                    yerImleriJsonArray = new JSONArray(yerImleri);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                yerImleriJsonArray = new JSONArray();
            }

            try {
                JSONObject yerImiJsonObject = new JSONObject();
                yerImiJsonObject.put("name", yerImiName);
                yerImiJsonObject.put("zoom",zoom);
                yerImiJsonObject.put("latitude", latLngBoundsCenter.getLatitude());
                yerImiJsonObject.put("longitude", latLngBoundsCenter.getLongitude());
                yerImleriJsonArray.put(yerImiJsonObject);

                SharedPrefs.setShared(getActivity(), "yer_imleri", yerImleriJsonArray.toString());

                Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_SUCCESS, yerImiName + " yer imlerinize eklendi.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Lütfen bir isim giriniz.");
        }
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_yerimi_add_kaydet:
                    yerImiEkle();
                    dismiss();
                    break;
                case R.id.dialog_yerimi_add_iptal:
                    dismiss();
                    break;
                case R.id.dialog_yerimi_add_close:
                    dismiss();
                    break;
            }
        }
    };
}
