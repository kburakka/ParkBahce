package com.example.msi.demoo.dialogfragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.adapters.KatmanlarDialogAdapter;
import com.example.msi.demoo.models.KatmanlarChild;
import com.example.msi.demoo.models.KatmanlarParent;
import com.example.msi.demoo.models.LayerModel;
import com.example.msi.demoo.utils.Const;
import com.example.msi.demoo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class KatmanlarDialog extends DialogFragment {
    private static final String TAG = "KatmanlarDialog";

    public static ExpandableListView expandableListView;
    public static ArrayList<KatmanlarParent> katmanList = new ArrayList<>();
    public KatmanlarDialogAdapter katmanlarDialogAdapter;

    public static boolean controlToSwitchClicking = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_katmanlar, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView kapat = rootView.findViewById(R.id.dialog_katmanlar_close);
        Switch openCloseAllLayersSwitch = rootView.findViewById(R.id.dialog_katmanlar_open_close_all_layer);

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        openCloseAllLayersSwitch.setChecked(allLayersOpenedOrClosedStatus());

        openCloseAllLayersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!controlToSwitchClicking){
                    if(isChecked){
                        if(Utils.internetControl(getContext())){
                            for(int i=0; i<katmanList.size(); i++)
                                if(!katmanList.get(i).isCheckedOrUnchecked())
                                    katmanList.get(i).setCheckedOrUnchecked(true);

                            expandableListView.invalidateViews();
                            MainActivity.refreshAddedLayers();

                        }

                    }else{
                        if(Utils.internetControl(getContext())){
                            for(int i=0; i<katmanList.size(); i++)
                                if(katmanList.get(i).isCheckedOrUnchecked())
                                    katmanList.get(i).setCheckedOrUnchecked(false);

                            expandableListView.invalidateViews();
                            MainActivity.closeOnlineLayers("layers");

                        }
                    }
                }
            }
        });

        expandableListView = rootView.findViewById(R.id.dialog_katmanlar_expandable_listview);

        katmanlarDialogAdapter = new KatmanlarDialogAdapter(getContext(), getActivity(), katmanList, openCloseAllLayersSwitch);

        if(katmanList.size() == 0){
            fillList();
            expandableListView.setAdapter(katmanlarDialogAdapter);
        } else {
            expandableListView.setAdapter(katmanlarDialogAdapter);
        }

        return rootView;
    }

    public static void fillList(){
        if(katmanList.size() == 0){
            List<LayerModel> layerModels = MainActivity.percon.getLayers();

            if(layerModels != null && layerModels.size() > 0){
                for(int i=0; i<layerModels.size(); i++){
                    List<KatmanlarChild> tempKatmanlarChild = new ArrayList<>();

                    if(layerModels.get(i).getStyle() != null){
                        if(layerModels.get(i).getStyle() != null && layerModels.get(i).getStyle().getType().equals("coded")) {
                            for(int j =0; j<layerModels.get(i).getStyle().getDatas().size(); j++){
                                tempKatmanlarChild.add(new KatmanlarChild(null, layerModels.get(i).getStyle().getDatas().get(j)));
                            }
                        }
                    }

                    if (!layerModels.get(i).getLayer().equals("ilce") && !layerModels.get(i).getLayer().equals("mahalle") && !layerModels.get(i).getLayer().equals("il")){
                        katmanList.add(new KatmanlarParent(false, false, null, layerModels.get(i), tempKatmanlarChild) );
                    }



//                    if (layerModels.get(i).getLayer().equals("park")){
//                        katmanList.add(0,new KatmanlarParent(false, false, null, layerModels.get(i), tempKatmanlarChild) );
//                    }else if(layerModels.get(i).getLayer().equals("donati")){
//                        katmanList.add(1,new KatmanlarParent(false, false, null, layerModels.get(i), tempKatmanlarChild) );
//                    }else if(layerModels.get(i).getLayer().equals("yapi")){
//                        katmanList.add(2,new KatmanlarParent(false, false, null, layerModels.get(i), tempKatmanlarChild) );
//                    }else if(layerModels.get(i).getLayer().equals("agac")){
//                        katmanList.add(3,new KatmanlarParent(false, false, null, layerModels.get(i), tempKatmanlarChild) );
//                    }else if(layerModels.get(i).getLayer().equals("nokta")){
//                        katmanList.add(4,new KatmanlarParent(false, false, null, layerModels.get(i), tempKatmanlarChild) );
//                    }
                }
            }
        }
    }

    public static boolean allLayersOpenedOrClosedStatus(){
        boolean allLayersIsOpenedOrClosed = true;
        for(int i=0; i<katmanList.size(); i++)
            if(!katmanList.get(i).isCheckedOrUnchecked()){
                allLayersIsOpenedOrClosed = false;
                break;
            }

        Log.e(TAG, "allLayersOpenedOrClosedStatus:  allLayersIsOpened : " + allLayersIsOpenedOrClosed);
        return allLayersIsOpenedOrClosed;
    }
}
