package com.example.msi.demoo.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.dialogfragments.KatmanlarDialog;
import com.example.msi.demoo.models.KatmanlarChild;
import com.example.msi.demoo.models.KatmanlarParent;
import com.example.msi.demoo.utils.Const;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.style.layers.Layer;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class KatmanlarDialogAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "KatmanlarDialogAdapter";

    private Context context;
    private Activity activity;
    private List<KatmanlarParent> katmanlarParentList;
    private Switch openCloseAllLayersSwitch;

    public KatmanlarDialogAdapter(Context context, Activity activity, List<KatmanlarParent> katmanlarParentList,Switch openCloseAllLayersSwitch) {
        this.context = context;
        this.activity = activity;
        this.katmanlarParentList = katmanlarParentList;
        this.openCloseAllLayersSwitch = openCloseAllLayersSwitch;
    }


    @Override
    public int getGroupCount() {
        return katmanlarParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size=0;
        if(katmanlarParentList.get(groupPosition).getKatmanlarChildList()!=null)
            size = katmanlarParentList.get(groupPosition).getKatmanlarChildList().size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return katmanlarParentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return katmanlarParentList.get(groupPosition).getKatmanlarChildList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final KatmanlarParent katmanlarParent = (KatmanlarParent) getGroup(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_katmanlar_dialog_parent, parent, false);

        ImageView parentOpenCloseImageView = convertView.findViewById(R.id.katmanlar_item_open_close_sub);
        CheckBox parentCheckBox = convertView.findViewById(R.id.katmanlar_item_checkbox);
        ImageView parentImageView = convertView.findViewById(R.id.katmanlar_item_imageview);
        TextView parentTextView = convertView.findViewById(R.id.katmanlar_item_textview);

        if (katmanlarParent.getKatmanlarChildList() != null && katmanlarParent.getKatmanlarChildList().size() > 1)
        {
            if(isExpanded){
                parentOpenCloseImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.eksi));
                katmanlarParentList.get(groupPosition).setOpenOrClose(true);
            }else{
                parentOpenCloseImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.arti));
                katmanlarParentList.get(groupPosition).setOpenOrClose(false);
            }
            parentOpenCloseImageView.setVisibility(View.VISIBLE);

            parentImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_view_list));
        }else{
            parentOpenCloseImageView.setVisibility(View.INVISIBLE);

            if(katmanlarParent.getLayer().getStyle() != null){
                if(katmanlarParent.getLayer().getType().equals(Utils.MULTIPOINT_TYPE)){
                    String imageBase64 = katmanlarParent.getLayer().getStyle().getDatas().get(0).getImage();
                    Bitmap bitmap = Utils.base64ToBitmap(imageBase64);
                    parentImageView.setImageBitmap(bitmap);
                }else if(katmanlarParent.getLayer().getType().equals(Utils.MULTILINESTRING_TYPE)){
                    Drawable lineVectorDrawable = context.getResources().getDrawable(R.drawable.ic_line);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        lineVectorDrawable.setTint(Color.parseColor(katmanlarParent.getLayer().getStyle().getDatas().get(0).getLineColor()));
                    }

                    Bitmap tempBitmapLine = Bitmap.createBitmap(lineVectorDrawable.getIntrinsicWidth(), lineVectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tempBitmapLine);
                    lineVectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    lineVectorDrawable.draw(canvas);

                    parentImageView.setImageBitmap(tempBitmapLine);
                }else if(katmanlarParent.getLayer().getType().equals(Utils.MULTIPOLYGON_TYPE)){
                    Drawable lineVectorDrawable = context.getResources().getDrawable(R.drawable.ic_line);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        lineVectorDrawable.setTint(Color.parseColor(katmanlarParent.getLayer().getStyle().getDatas().get(0).getLineColor()));
                    }

                    Bitmap tempBitmapLine = Bitmap.createBitmap(lineVectorDrawable.getIntrinsicWidth(), lineVectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tempBitmapLine);
                    lineVectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    lineVectorDrawable.draw(canvas);

                    parentImageView.setImageBitmap(tempBitmapLine);                }
            }else{
                if(katmanlarParent.getLayer().getLayer().equals("depo")){
                    parentImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.er_depo));
                }else if(katmanlarParent.getLayer().getLayer().equals("iletim_hatti")) {
                    parentImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.er_iletim_hatti));
                } else if(katmanlarParent.getLayer().getLayer().equals("su_kuyusu")){
                    parentImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.er_kuyu));
                }
            }
        }

        parentCheckBox.setChecked(katmanlarParent.isCheckedOrUnchecked());

        //CheckBox'ların listener`larını tanımlıyoruz.
        parentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(parentCheckBox.isChecked())
                {
                    if(Utils.internetControl(context)){
                        katmanlarParentList.get(groupPosition).setCheckedOrUnchecked(true);
                        openAndCloseLayers((KatmanlarParent) getGroup(groupPosition));
                    }

                }else{
                    if(Utils.internetControl(context)){
                        katmanlarParentList.get(groupPosition).setCheckedOrUnchecked(false);
                        openAndCloseLayers((KatmanlarParent) getGroup(groupPosition));
                    }
                }

                KatmanlarDialog.controlToSwitchClicking = true;
                openCloseAllLayersSwitch.setChecked(KatmanlarDialog.allLayersOpenedOrClosedStatus());
                KatmanlarDialog.controlToSwitchClicking = false;
            }
        });


        parentTextView.setText(katmanlarParent.getLayer().getAlias());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final KatmanlarChild katmanlarChild = (KatmanlarChild) getChild(groupPosition, childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_katmanlar_dialog_child, parent, false);

        ImageView childImageView = convertView.findViewById(R.id.katmanlar_item_imageview_child);
        TextView childTextView = convertView.findViewById(R.id.katmanlar_item_textview_child);

        if(katmanlarParentList.get(groupPosition).getLayer().getType().equals(Utils.MULTIPOINT_TYPE)){
            String imageBase64 = katmanlarChild.getLayerStyleData().getImage();
            Bitmap bitmap = Utils.base64ToBitmap(imageBase64);
            childImageView.setImageBitmap(bitmap);
        }else if(katmanlarParentList.get(groupPosition).getLayer().getType().equals(Utils.MULTILINESTRING_TYPE)){
            Drawable line = context.getResources().getDrawable(R.drawable.ic_line);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                line.setTint(Color.parseColor(katmanlarChild.getLayerStyleData().getLineColor()));
            }
            childImageView.setImageDrawable(line);
        }else if(katmanlarParentList.get(groupPosition).getLayer().getType().equals(Utils.MULTIPOLYGON_TYPE)){
            Drawable line = context.getResources().getDrawable(R.drawable.ic_line);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                line.setTint(Color.parseColor(katmanlarChild.getLayerStyleData().getLineColor()));
            }
            childImageView.setImageDrawable(line);
        }
        childTextView.setText(katmanlarChild.getLayerStyleData().getCodedName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    //Alttaki fonksiyon Online modda katmanları açıp-kapatmak için kullanılıyor.
    private void openAndCloseLayers(KatmanlarParent katmanlarParent){
        Log.e(TAG, "openAndCloseLayers: Layer name : " + katmanlarParent.getLayer().getLayer());

        String layersString = Utils.findOpenLayerNames(KatmanlarDialog.katmanList);

        MainActivity.closeOnlineLayers("layers");
        if(layersString != null)
            MainActivity.openOnlineLayers(layersString, "layers");
    }
}
