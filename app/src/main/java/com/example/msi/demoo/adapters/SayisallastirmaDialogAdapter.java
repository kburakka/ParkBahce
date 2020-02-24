package com.example.msi.demoo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msi.demoo.R;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.LayerModel;
import com.example.msi.demoo.utils.Utils;

import java.util.List;

public class SayisallastirmaDialogAdapter extends RecyclerView.Adapter<SayisallastirmaDialogAdapter.ViewHolder> {
    private static final String TAG = "SayisallastirmaDialogAd";

    List<LayerModel> layerModels;
    CustomItemClickListener listener;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView sayisallastirmaIcon;
        public TextView sayisallastırmaText;

        public ViewHolder(View view) {
            super(view);

            sayisallastirmaIcon= (ImageView)view.findViewById(R.id.sayisallastirma_item_imageview);
            sayisallastırmaText = (TextView)view.findViewById(R.id.sayisallastirma_item_textview);

        }

    }

    public SayisallastirmaDialogAdapter(List<LayerModel> layerModels, CustomItemClickListener listener, Context context) {
        this.layerModels = layerModels;
        this.listener = listener;
        this.context = context;
    }


    @Override
    public SayisallastirmaDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sayisallastirma_dialog, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, view_holder.getPosition());
            }
        });

        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LayerModel tempLayerModel = layerModels.get(position);

        holder.sayisallastırmaText.setText(tempLayerModel.getAlias());

        if(tempLayerModel.getStyle() != null) {
            if (tempLayerModel.getType().equals("MultiPoint")) {
                Bitmap tempBitmap = Utils.base64ToBitmap(tempLayerModel.getStyle().getDatas().get(0).getImage());
                holder.sayisallastirmaIcon.setImageBitmap(tempBitmap);
            } else if (tempLayerModel.getType().equals("MultiLineString")) {
                Drawable lineVectorDrawable = context.getResources().getDrawable(R.drawable.ic_line);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    lineVectorDrawable.setTint(Color.parseColor(tempLayerModel.getStyle().getDatas().get(0).getLineColor()));
                }

                Bitmap tempBitmapLine = Bitmap.createBitmap(lineVectorDrawable.getIntrinsicWidth(), lineVectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(tempBitmapLine);
                lineVectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                lineVectorDrawable.draw(canvas);

                holder.sayisallastirmaIcon.setImageBitmap(tempBitmapLine);
            } else if (tempLayerModel.getType().equals("MultiPolygon")){
                Drawable lineVectorDrawable = context.getResources().getDrawable(R.drawable.ic_line);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    lineVectorDrawable.setTint(Color.parseColor(tempLayerModel.getStyle().getDatas().get(0).getLineColor()));
                }

                Bitmap tempBitmapLine = Bitmap.createBitmap(lineVectorDrawable.getIntrinsicWidth(), lineVectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(tempBitmapLine);
                lineVectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                lineVectorDrawable.draw(canvas);

                holder.sayisallastirmaIcon.setImageBitmap(tempBitmapLine);
            }

        }else{
            if(tempLayerModel.getLayer().equals("agac")){
                holder.sayisallastirmaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.agac));
            }else if(tempLayerModel.getLayer().equals("nokta")) {
                holder.sayisallastirmaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.nokta));
            } else if(tempLayerModel.getLayer().equals("park")) {
                holder.sayisallastirmaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.park));
            }else if(tempLayerModel.getLayer().equals("donati")) {
                holder.sayisallastirmaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.donati));
            }else if(tempLayerModel.getLayer().equals("yapi")) {
                holder.sayisallastirmaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.yapi));
            }else{
                holder.sayisallastirmaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.polygon));
            }
        }
    }

    @Override
    public int getItemCount() {
        return layerModels.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
