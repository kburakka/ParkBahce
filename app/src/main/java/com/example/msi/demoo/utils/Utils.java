package com.example.msi.demoo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msi.demoo.R;
import com.example.msi.demoo.models.KatmanlarParent;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static android.content.Context.WINDOW_SERVICE;

public class Utils {
    private static final String TAG = "Utils";

    public static final String LOGIN_MODE_ONLINE = "Online";
    public static final String LOGIN_MODE_OFFLINE = "Offline";

    public static final String NOT_ADD = "Not Ekleme";
    public static final String ADD = "Ekleme";
    public static final String REMOVE = "Silme";
    public static final String UPDATE = "Güncelleme";
    public static final String KNOWLEDGE = "Bilgi";

    public static final String UZUNLUK = "Uzunluk";
    public static final String ALAN = "Alan";
    public static final String KOORDINAT = "Koordinat";

    public static final String POINT_TYPE = "Point";
    public static final String MULTIPOINT_TYPE = "MultiPoint";
    public static final String LINE_TYPE = "Line";
    public static final String MULTILINESTRING_TYPE = "MultiLineString";
    public static final String POLYGON_TYPE = "Polygon";
    public static final String MULTIPOLYGON_TYPE = "MultiPolygon";

    public static final String REAL = "Real";
    public static final String VIRTUAL = "Virtual";
    public static final String SELECTED = "Selected";

    public static final String CUSTOM_TOAST_SUCCESS = "success";
    public static final String CUSTOM_TOAST_WARNING = "warning";
    public static final String CUSTOM_TOAST_ERROR = "error";

    public static final String BOLGELER = "Bölgeler";
    public static final String SUBELER= "Şubeler";
    public static final String YOLLAR = "Yollar";

    public static final String ILLER = "İller";
    public static final String ILCELER = "İlçeler";
    public static final String MAHALLELER = "Mahalleler";

    public static final String IMAGE = "Resim";
    public static final String VIDEO = "Video";
    public static final String FILE= "Dosya";


    public static void showCustomToast(Activity activity, String type, String text) {
        View view = activity.getLayoutInflater().inflate(R.layout.custom_toast, null);

        LinearLayout linearLayout = view.findViewById(R.id.custom_toast_relative);
        ImageView imageView = view.findViewById(R.id.custom_toast_image);
        TextView textView = view.findViewById(R.id.custom_toast_text);

        textView.setText(text);

        if(type.equals(CUSTOM_TOAST_SUCCESS)){
            linearLayout.setBackground(activity.getResources().getDrawable(R.drawable.background_custom_toast_success));
            imageView.setBackground(activity.getResources().getDrawable(R.drawable.toast_success));
        }else if(type.equals(CUSTOM_TOAST_WARNING)){
            linearLayout.setBackground(activity.getResources().getDrawable(R.drawable.background_custom_toast_warning));
            imageView.setBackground(activity.getResources().getDrawable(R.drawable.toast_warning));
        }else if(type.equals(CUSTOM_TOAST_ERROR)){
            linearLayout.setBackground(activity.getResources().getDrawable(R.drawable.background_custom_toast_error));
            imageView.setBackground(activity.getResources().getDrawable(R.drawable.toast_error));
        }

        Toast toast = new Toast(activity);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setMargin(0, (float) 0.15);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public static ACProgressFlower progressDialogLikeIosWithText(Context context, String title){
        ACProgressFlower acProgressFlower = null;

        try {
            acProgressFlower = new ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .speed(4)
                    .text(title)
                    .textSize(20)
                    .fadeColor(Color.DKGRAY).build();

            acProgressFlower.setCancelable(false);
        }catch (Exception e ){ e.printStackTrace(); }

        return acProgressFlower;
    }
    public static ACProgressFlower progressDialogLikeIosWithoutText(Context context){
        ACProgressFlower acProgressFlower = null;

        try {
        acProgressFlower = new ACProgressFlower.Builder(context)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .speed(4)
                    .fadeColor(Color.DKGRAY).build();

            acProgressFlower.setCancelable(false);
        }catch (Exception e){ e.printStackTrace(); }

        return acProgressFlower;
    }


    public static boolean internetControl(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat.format(new Date());
    }


    public static List<Double> metersToDegrees(double x, double y){
        double latitude = Math.atan(Math.exp(x * Math.PI / 20037508.34)) * 360 / Math.PI - 90;
        double longitude = y * 180 / 20037508.34 ;

        List<Double> latLng = new ArrayList<>();
        latLng.add(latitude);
        latLng.add(longitude);

        return latLng;
    }
    public static List<Double> degreesToMeters(double x, double y){
        double latitude = Math.log(Math.tan((90 + x) * Math.PI / 360)) / (Math.PI/ 180);
        latitude = latitude * 20037508.34 / 180;

        double longitude = y* 20037508.34 / 180;

        List<Double> latLng = new ArrayList<>();
        latLng.add(latitude);
        latLng.add(longitude);

        return latLng;
    }


    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public static Bitmap base64ToBitmap(String b64) {
        try {
            byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        return null;
    }


    public static Bitmap makeTransparentBitmap(Bitmap bmp, int alpha) {
        Bitmap transBmp = Bitmap.createBitmap(bmp.getWidth(),
                bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBmp);
        final Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(bmp, 0, 0, paint);
        return transBmp;
    }
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

//        source.recycle();
        return retVal;
    }


    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }



    // Custom method to get screen width in dp/dip using Context object
    public static int getScreenWidthInDPs(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.widthPixels / dm.density);
    }
    // Custom method to get screen height in dp/dip using Context object
    public static int getScreenHeightInDPs(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.heightPixels / dm.density);
    }


    // Custom method to get android device screen width in pixels
    public static int getScreenWidthInPixels(Activity activity){
        DisplayMetrics dm  = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    // Custom method to get android device screen height in pixels
    public static int getScreenHeightInPixels(Activity activity){
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }



    public static String findOpenLayerNames(List<KatmanlarParent> katmanlarParents){
        StringBuilder layerNamesThatWasOpen = null;
//        List<String> katmanlar = new ArrayList<>();
        String[] katmanlar = new String[5];

        for(int i=0; i<katmanlarParents.size(); i++){
            if(katmanlarParents.get(i).isCheckedOrUnchecked()){
                if (katmanlarParents.get(i).getLayer().getLayer().equals("park")){
                    katmanlar[0] = "park";
                }
                else if(katmanlarParents.get(i).getLayer().getLayer().equals("donati")){
                    katmanlar[1] = "donati";
                }
                else if(katmanlarParents.get(i).getLayer().getLayer().equals("yapi")){
                    katmanlar[2] = "yapi";
                }
                else if(katmanlarParents.get(i).getLayer().getLayer().equals("agac")){
                    katmanlar[3] = "agac";
                }
                else if(katmanlarParents.get(i).getLayer().getLayer().equals("nokta")){
                    katmanlar[4] = "nokta";
                }

            }
        }

        for (int i=0; i<katmanlar.length; i++){
            if (katmanlar[i] != null){
                if(layerNamesThatWasOpen == null){
                    layerNamesThatWasOpen= new StringBuilder(katmanlar[i]);
                }else{
                    layerNamesThatWasOpen.append(",").append(katmanlar[i]);
                }
            }
        }
        if(layerNamesThatWasOpen != null)
            return layerNamesThatWasOpen.toString();
        else
            return null;
    }

    public static String findOpenLayerNamesForMapClick(List<KatmanlarParent> katmanlarParents){
        StringBuilder layerNamesThatWasOpen = null;
        for(int i=0; i<katmanlarParents.size(); i++){
            if(!katmanlarParents.get(i).getLayer().getLayer().equals("il") && !katmanlarParents.get(i).getLayer().getLayer().equals("ilce") &&
                    !katmanlarParents.get(i).getLayer().getLayer().equals("mahalle") && katmanlarParents.get(i).isCheckedOrUnchecked()){
                if(layerNamesThatWasOpen == null){
                    layerNamesThatWasOpen= new StringBuilder(katmanlarParents.get(i).getLayer().getLayer());
                }else{
                    layerNamesThatWasOpen.append(",").append(katmanlarParents.get(i).getLayer().getLayer());
                }
            }
        }
        if(layerNamesThatWasOpen != null)
            return layerNamesThatWasOpen.toString();
        else
            return null;
    }
}
