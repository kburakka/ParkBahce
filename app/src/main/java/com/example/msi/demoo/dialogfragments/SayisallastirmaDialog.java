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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.adapters.SayisallastirmaDialogAdapter;
import com.example.msi.demoo.fragments.SayisallastirmaFragment;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.LayerModel;

import java.util.ArrayList;
import java.util.List;

public class SayisallastirmaDialog extends DialogFragment {
    private static final String TAG = "SayisallastirmaDialog";

    private TextView yetkiYok;
    private ImageView kapat;
    private RecyclerView recyclerView;
    private SayisallastirmaDialogAdapter sayisallastirmaDialogAdapter;
    private List<LayerModel> layerModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_sayisallastirma, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yetkiYok = rootView.findViewById(R.id.dialog_sayisallastirma_yetki_yok);
        kapat = rootView.findViewById(R.id.dialog_sayisallastırma_close);
        recyclerView = rootView.findViewById(R.id.dialog_sayisallastirma_recycler);

        //Permission kontrolu yapılacak o yuzden blocklandı.
//        layerModels = MainActivity.percon.getLayers();

        //il,ilce,mahalle kontrolü yapılıypr.
        for(int i=0; i<MainActivity.percon.getLayers().size(); i++){
            if(!MainActivity.percon.getLayers().get(i).getLayer().equals("il") && !MainActivity.percon.getLayers().get(i).getLayer().equals("ilce")
                    && !MainActivity.percon.getLayers().get(i).getLayer().equals("mahalle") ){
                layerModels.add(MainActivity.percon.getLayers().get(i));
            }
        }


        if(layerModels.size() != 0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);

            recyclerView.setLayoutManager(layoutManager);


            sayisallastirmaDialogAdapter = new SayisallastirmaDialogAdapter(layerModels, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Log.e("position", "Tıklanan Pozisyon:" + position + "\n" + layerModels.get(position).getAlias());
                    dismiss();
                    MainActivity.fragment = new SayisallastirmaFragment(layerModels.get(position));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment,"SayisallastirmaFragment").addToBackStack(null).commit();
                }
            }, getContext());

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(sayisallastirmaDialogAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }else{
            recyclerView.setVisibility(View.GONE);
            yetkiYok.setVisibility(View.VISIBLE);
        }

        kapat.setOnClickListener(onClickListener);

        return rootView;
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_sayisallastırma_close:
                    dismiss();
                    break;
            }
        }
    };

}
