package com.example.msi.demoo.dialogfragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msi.demoo.R;
import com.mapbox.geojson.Feature;

@SuppressLint("ValidFragment")
public class ParselBilgisiDialog extends DialogFragment {
    private Feature feature;

    public ParselBilgisiDialog(Feature feature) {
        this.feature = feature;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_parsel_bilgisi, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView mahalleAdi = rootView.findViewById(R.id.dialog_parsel_bilgisi_mahalle_adi);
        TextView adaNo = rootView.findViewById(R.id.dialog_parsel_bilgisi_ada_no);
        TextView parselNo = rootView.findViewById(R.id.dialog_parsel_bilgisi_parsel_no);
        TextView alan = rootView.findViewById(R.id.dialog_parsel_bilgisi_alan);
        TextView mevki = rootView.findViewById(R.id.dialog_parsel_bilgisi_mevki);
        TextView nitelik = rootView.findViewById(R.id.dialog_parsel_bilgisi_nitelik);


        ImageView kapat = rootView.findViewById(R.id.dialog_parsel_bilgisi_add_close);
        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mahalleAdi.setText(feature.getStringProperty("mahalleAd"));
        adaNo.setText(feature.getStringProperty("adaNo"));
        parselNo.setText(feature.getStringProperty("parselNo"));
        alan.setText(feature.getStringProperty("alan"));
        mevki.setText(feature.getStringProperty("mevkii"));
        nitelik.setText(feature.getStringProperty("nitelik"));
        return rootView;
    }
}
