package com.example.msi.demoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msi.demoo.R;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.YerImi;
import com.example.msi.demoo.utils.SharedPrefs;
import com.example.msi.demoo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class YerImleriDialogAdapter extends RecyclerView.Adapter<YerImleriDialogAdapter.ViewHolder> {

    private Activity activity;
    private Context context;
    private List<YerImi> yerImiList;
    private CustomItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView yerimleriText;
        ImageView yerimleriDelete;
        ImageView yerimleriSagOk;

        public ViewHolder(View view) {
            super(view);

            yerimleriText = view.findViewById(R.id.yerimleri_item_textview);
            yerimleriDelete = view.findViewById(R.id.yerimleri_item_del_imageview);
            yerimleriSagOk= view.findViewById(R.id.yerimleri_item_imageview);
        }
    }

    public YerImleriDialogAdapter(Context context, Activity activity, List<YerImi> yerImiList, CustomItemClickListener listener) {
        this.context = context;
        this.activity = activity;
        this.yerImiList = yerImiList;
        this.listener = listener;
    }


    @Override
    public YerImleriDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yerimleri_dialog, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        YerImi yerImi = yerImiList.get(position);

        holder.yerimleriText.setText(yerImi.getYerImiName());

        holder.yerimleriDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.toast_warning);
                builder.setTitle(Html.fromHtml("<font color='#45666D'>Uyarı</font>"));
                builder.setMessage(yerImi.getYerImiName()+ " yerimi silinsin mi?");
                builder.setCancelable(false);
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        yerImiList.remove(position);
                        String yerImleriSharedPref = listToJsonArrayString();
                        SharedPrefs.setShared(activity,"yer_imleri", yerImleriSharedPref);
                        Utils.showCustomToast(activity, Utils.CUSTOM_TOAST_SUCCESS, yerImi.getYerImiName()+ " yer imlerinizden çıkartıldı.");
                        notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

        holder.yerimleriSagOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return yerImiList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String listToJsonArrayString(){
        JSONArray yerImleriJsonArray = new JSONArray();

        for(int i=0; i<yerImiList.size(); i++){
            try {
                JSONObject yerImiJsonObject = new JSONObject();
                yerImiJsonObject.put("name", yerImiList.get(i).getYerImiName());
                yerImiJsonObject.put("zoom",yerImiList.get(i).getZoom());
                yerImiJsonObject.put("latitude", yerImiList.get(i).getLatitude());
                yerImiJsonObject.put("longitude", yerImiList.get(i).getLongitude());
                yerImleriJsonArray.put(yerImiJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return yerImleriJsonArray.toString();
    }
}
