package com.example.msi.demoo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.msi.demoo.BuildConfig;
import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.adapters.KatmanFragmentMediaAdapter;
import com.example.msi.demoo.app.AppController;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.KatmanMedia;
import com.example.msi.demoo.models.LayerField;
import com.example.msi.demoo.models.LayerFieldCodedValue;
import com.example.msi.demoo.models.LayerModel;
import com.example.msi.demoo.models.PropertyObject;
import com.example.msi.demoo.models.SearchMatch;
import com.example.msi.demoo.utils.Const;
import com.example.msi.demoo.utils.FileUtil;
import com.example.msi.demoo.utils.HttpsTrustManager;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.example.msi.demoo.MainActivity.context;


@SuppressLint("ValidFragment")
public class KatmanFragment extends Fragment {
    private static final String TAG = "KatmanFragment";

    public static final String KATMAN_TYPE_ADD = "Ekle";
    public static final String KATMAN_TYPE_NOT_ADD = "Not Ekle";
    public static final String KATMAN_TYPE_UPDATE = "Düzenle";
    public static final String KATMAN_TYPE_KNOWLEDGE = "Bilgi";

/////////////////////////////////////////////////////////////////////////////////////////////////////
    private String userChoosenTask;
    private int CAMERA = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private String mCurrentPhotoPath;
////////////////////////////////////////////////////////////////////////////////////////////////////

    private TextView baslik;
    private String tempBaslik;
    private Button iptal, kaydet, kapat;

    private RecyclerView mediaRecyclerView;
    private List<KatmanMedia> mediaListForRecycler = new ArrayList<KatmanMedia>();
    private KatmanFragmentMediaAdapter katmanFragmentMediaAdapter;

    private boolean mediaFragmenttanGelindi = false;

    private ImageView photoCek;

    private LinearLayout mediaAddLinear;

    private LinearLayout linearLayoutForMedias;
    private LinearLayout linearLayoutForDynamic;
    private boolean adding = false;

    private List<PropertyObject> propertyObjectList = new ArrayList<>();
    private List<LayerField> layerFieldsForSpinner = new ArrayList<>();

    public String katmanType;

    private String geometryType = null;
    private LayerModel layerModel = null;
    private List<LatLng> latLngs = new ArrayList<>();


    private JSONObject jsonObject = null;
    private JSONObject properties = null;
    private String featureId = null;
    private String feature = null;


    @SuppressLint("ValidFragment")
    public KatmanFragment(String katmanType, LayerModel layerModel, String geometryType, List<LatLng> latLngs){
        this.katmanType = katmanType;
        this.layerModel = layerModel;
        this.geometryType = geometryType;
        this.latLngs.addAll(latLngs);
    }

    @SuppressLint("ValidFragment")
    public KatmanFragment(String katmanType, JSONObject jsonObject){
        this.katmanType = katmanType;
        this.jsonObject= jsonObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_katman, container, false);
        MainActivity.mainActivityCurrentObject.toolbar.setVisibility(View.GONE);

        baslik = rootView.findViewById(R.id.katman_frag_baslik);
        photoCek = rootView.findViewById(R.id.katman_frag_fotocek);

        mediaAddLinear = rootView.findViewById(R.id.katman_frag_fotocek_linear);

        iptal = rootView.findViewById(R.id.katman_frag_cancel_button);
        kaydet= rootView.findViewById(R.id.katman_frag_save_button);
        kapat = rootView.findViewById(R.id.katman_frag_close_button);

        linearLayoutForMedias = rootView.findViewById(R.id.katman_frag_media_linear);
        mediaRecyclerView = rootView.findViewById(R.id.katman_frag_media_recyclerview);

        linearLayoutForDynamic = rootView.findViewById(R.id.katman_frag_attribute_linear);

        iptal.setOnClickListener(onClickListener);
        kaydet.setOnClickListener(onClickListener);
        kapat.setOnClickListener(onClickListener);

        if(katmanType != null)
            setVisibilityOfButtons(katmanType);
        else
            setVisibilityOfButtons(KATMAN_TYPE_KNOWLEDGE);


        if(katmanType.equals(KATMAN_TYPE_ADD)){
            Log.e(TAG, "onCreateView: BAKKKK : " + layerModel.toString() );
            feature = layerModel.getLayer();
        }else{
            Log.e(TAG, "onCreateView: BAKKKK : " + jsonObject.toString() );

            try {

                featureId = jsonObject.getString("id");
                feature = featureId.split("\\.")[0];

                geometryType = jsonObject.getJSONObject("geometry").getString("type");
                properties = jsonObject.getJSONObject("properties");

            } catch (JSONException e) { e.printStackTrace(); }
        }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(!mediaFragmenttanGelindi){
            if (katmanType.equals(KATMAN_TYPE_ADD)){
                fillListDataForAdd();
            }else if(katmanType.equals(KATMAN_TYPE_UPDATE) || katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
                fillListDataForEditKnowledge();
            }
        }else{
            for(int i=0; i<propertyObjectList.size(); i++){
                if(propertyObjectList.get(i).getObject() != null){
                    RelativeLayout relativeLayoutParent = propertyObjectList.get(i).getRelativeLayoutParent();
                    if(relativeLayoutParent != null){
                        if(relativeLayoutParent.getParent()!= null){
                            ((ViewGroup)relativeLayoutParent.getParent()).removeView(relativeLayoutParent);
                        }
                        linearLayoutForDynamic.addView(relativeLayoutParent);
                    }
                }
            }

            baslik.setText(tempBaslik);
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        LinearLayoutManager layoutManagerMedia = new LinearLayoutManager(getActivity());
        layoutManagerMedia.setOrientation(LinearLayoutManager.HORIZONTAL);

        mediaRecyclerView.setLayoutManager(layoutManagerMedia);

        katmanFragmentMediaAdapter = new KatmanFragmentMediaAdapter(mediaListForRecycler, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("position", "Tıklanan Pozisyon:" + position);

                KatmanMedia clickedKatmanMedia = mediaListForRecycler.get(position);

                if(clickedKatmanMedia.getUri() != null){
                    tempBaslik = baslik.getText().toString();
                    mediaFragmenttanGelindi = false;

                    MainActivity.fragment = new MediaFragment(mediaListForRecycler, position, getContext(), getActivity());
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "MediaFragment").addToBackStack("KatmanFragment").commit();
                }
            }
        }, getContext(), getActivity(), katmanType);

        mediaRecyclerView.setHasFixedSize(true);
        mediaRecyclerView.setAdapter(katmanFragmentMediaAdapter);
        mediaRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mediaRecyclerView);


        setVisibilityOfAttachments();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.mainActivityCurrentObject.toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(listenToMediaFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listenToMediaFragment, new IntentFilter("BROADCAST_MEDIA_FRAG"));
    }


    private void fillListDataForAdd() {
        String tempKatmanName = layerModel.getAlias();
        baslik.setText(tempKatmanName.substring(0, 1).toUpperCase() + tempKatmanName.substring(1) + " " + katmanType);

        int i=0;
        for(LayerField layerField : layerModel.getFields()){

            if(layerField.getPermision() < 2)
                continue;

            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_katman_fragment_attribute, null);
            AttributeViewHolder holder = new AttributeViewHolder(v);

            RelativeLayout relativeLayoutParent = addDynamicViews(layerField, i, holder.relative1, holder.relative2, holder.relative3, holder.relativeParent);
//            if(relativeLayoutParent.getParent()!= null){
//                ((ViewGroup)relativeLayoutParent.getParent()).removeView(relativeLayoutParent);
//            }
            relativeLayoutParent.setPadding(0, (int) Utils.convertDpToPx(getContext(), 16), (int) Utils.convertDpToPx(getContext(), 16), 0);
            if(adding){
                linearLayoutForDynamic.addView(relativeLayoutParent);
                propertyObjectList.get(propertyObjectList.size()-1).setRelativeLayoutParent(relativeLayoutParent);
            }else{
                propertyObjectList.get(propertyObjectList.size()-1).setRelativeLayoutParent(relativeLayoutParent);
            }
            i++;
        }

    }
    private void fillListDataForEditKnowledge(){
        List<LayerField> layerFieldList = new ArrayList<>();
        for(int i=0; i<MainActivity.percon.getLayers().size(); i++){
            if(MainActivity.percon.getLayers().get(i).getLayer().equals(feature)){
                LayerModel layerModelTemp  = MainActivity.percon.getLayers().get(i);
                String katmanNameTemp = layerModelTemp.getAlias();
                baslik.setText(katmanNameTemp.substring(0, 1).toUpperCase() + katmanNameTemp.substring(1) + " " + katmanType);

                layerFieldList = layerModelTemp.getFields();
            }
        }

        Iterator<String> propertyKeys= properties.keys();

        int i = 0;
        while(propertyKeys.hasNext()){
            String keyI = propertyKeys.next();

            for(int j=0; j<layerFieldList.size(); j++){
                LayerField layerField = layerFieldList.get(j);
                if(layerField.getField().equals(keyI)){

                    if(katmanType.equals(KATMAN_TYPE_KNOWLEDGE) && layerField.getPermision()<1)
                        continue;
                    if(katmanType.equals(KATMAN_TYPE_UPDATE) && layerField.getPermision()<3)
                        continue;

                    View v = LayoutInflater.from(getContext()).inflate(R.layout.item_katman_fragment_attribute, null);
                    AttributeViewHolder holder = new AttributeViewHolder(v);

                    RelativeLayout relativeLayoutParent = addDynamicViews(layerField, i, holder.relative1, holder.relative2, holder.relative3, holder.relativeParent);
//                    if(relativeLayoutParent.getParent()!= null){
//                        ((ViewGroup)relativeLayoutParent.getParent()).removeView(relativeLayoutParent);
//                    }

                    relativeLayoutParent.setPadding(0, (int) Utils.convertDpToPx(getContext(), 16), (int) Utils.convertDpToPx(getContext(), 16), 0);
                    if(adding){
                        linearLayoutForDynamic.addView(relativeLayoutParent);
                        propertyObjectList.get(propertyObjectList.size()-1).setRelativeLayoutParent(relativeLayoutParent);
                    }else{
                        propertyObjectList.get(propertyObjectList.size()-1).setRelativeLayoutParent(relativeLayoutParent);
                    }
                }
            }
            i++;
        }
    }

    private void setVisibilityOfButtons(String value){
        if(value.equals(KATMAN_TYPE_ADD)){
            iptal.setVisibility(View.VISIBLE);
            kaydet.setVisibility(View.VISIBLE);
            kapat.setVisibility(View.GONE);
        }else if(value.equals(KATMAN_TYPE_KNOWLEDGE)){
            iptal.setVisibility(View.GONE);
            kaydet.setVisibility(View.GONE);
            kapat.setVisibility(View.VISIBLE);
        }else if (value.equals(KATMAN_TYPE_UPDATE)){
            iptal.setVisibility(View.VISIBLE);
            kaydet.setVisibility(View.VISIBLE);
            kapat.setVisibility(View.GONE);
        }
    }
    private void setVisibilityOfAttachments(){
        LayerModel tempLayerModel = null;

        switch (katmanType) {
            case KATMAN_TYPE_ADD:
                tempLayerModel = layerModel;
                break;
            case KATMAN_TYPE_KNOWLEDGE:
                for (int i = 0; i < MainActivity.percon.getLayers().size(); i++) {
                    if (MainActivity.percon.getLayers().get(i).getLayer().equals(feature))
                        tempLayerModel = MainActivity.percon.getLayers().get(i);
                }
                break;
        }

        if(tempLayerModel != null)
            if(Utils.internetControl(getContext())){
                linearLayoutForMedias.setVisibility(View.VISIBLE);

                if(katmanType != null && !katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
                    photoCek.setOnClickListener(onClickListener);
                }else
                    mediaAddLinear.setVisibility(View.GONE);

                if(featureId != null && !mediaFragmenttanGelindi && katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
                    getTheAttachmentDataOfLayerFeatureWithId(featureId);
                }

            }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Lütfen gerekli izinleri veriniz!");
                    alertBuilder.setMessage("External storage, Camera izinleri gerekli.");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("photo"))
                        takePhotoFromCamera();
//                    else if(userChoosenTask.equals("attachment_downloading"))
//                        getTheAttachmentsDataInfoOfLayerFeature(featureId);
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = null;
                if(Build.VERSION.SDK_INT >= 24)
                    photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                else
                    photoURI = Uri.fromFile(photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dcimFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_DCIM);
        if(!dcimFile.exists()) {
            dcimFile.mkdirs();
        }
        File cameraDcimFile = new File(dcimFile.getPath() + File.separator + "Camera");
        if(!cameraDcimFile.exists()) {
            cameraDcimFile.mkdirs();
        }
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }

        if (requestCode == CAMERA) {
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            updateListAfterAdding( new KatmanMedia(imageUri, Utils.IMAGE, false,null));

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

    private void updateListAfterAdding(KatmanMedia media){
        mediaListForRecycler.add( media );
        katmanFragmentMediaAdapter.notifyDataSetChanged();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.katman_frag_fotocek:
                    boolean checkPermissionFoto = checkPermission(getContext());
                    userChoosenTask = "photo";
                    if(checkPermissionFoto){
                        if(mediaListForRecycler.size() < 1){
                            takePhotoFromCamera();
                        }else{
                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING,"Bir fotograf hakkınız var!");
                        }
                    }
                    break;
                case R.id.katman_frag_save_button:
                    boolean areValidValues = setTheFinalAttributeKeyValue();
                    if(areValidValues){
                        String jsonForAddAndEdit = writeJsonForAdd().toString();
                        Log.e(TAG, "onClick: FİRST JSON : " + jsonForAddAndEdit);

                        if(Utils.internetControl(getContext()))
                        {
                            if(katmanType.equals(KATMAN_TYPE_ADD) )
                            {
                                if(mediaListForRecycler.size() > 0){
                                    postTheDataToGeoserverWithAttachment(Const.feature_add__url, jsonForAddAndEdit);
                                }else{
                                    postTheDataToGeoserver(Const.feature_add__url, jsonForAddAndEdit);
                                }
                            }
                            else if(katmanType.equals(KATMAN_TYPE_UPDATE)){
                                boolean mediaAdded = false;
                                for(int i=0; i<mediaListForRecycler.size(); i++){
                                    if(!mediaListForRecycler.get(i).getExistService()){
                                        mediaAdded = true;
                                        break;
                                    }
                                }

                                if(mediaAdded) {
                                    boolean checkPermission = checkPermission(getContext());
                                    if(checkPermission)
                                        postTheDataToGeoserverWithAttachment(Const.feature_add__url , jsonForAddAndEdit);
                                    else
                                        Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_WARNING, "Gerekli izinleri sağlamalısınız.");
                                }else
                                    postTheDataToGeoserver(Const.feature_add__url, jsonForAddAndEdit);
                            }
                        }
                    }
                    break;
                case R.id.katman_frag_close_button:
                    setTheActiveFragment();
                    break;
                case R.id.katman_frag_cancel_button:
                    setTheActiveFragment();
                    break;
            }
        }
    };

    private void setTheActiveFragment(){
        if(katmanType.equals(KATMAN_TYPE_ADD) ){
            MainActivity.fragment = getActivity().getSupportFragmentManager().findFragmentByTag("SayisallastirmaFragment");
            getActivity().getSupportFragmentManager().popBackStack();
        }else if(katmanType.equals(KATMAN_TYPE_UPDATE) || katmanType.equals(KATMAN_TYPE_KNOWLEDGE) || katmanType.equals(KATMAN_TYPE_NOT_ADD)){
            MainActivity.fragment = getActivity().getSupportFragmentManager().findFragmentByTag("CalloutFragment");
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private RelativeLayout addDynamicViews(LayerField layerField, int position, RelativeLayout rel1, RelativeLayout rel2, RelativeLayout rel3, RelativeLayout relParent) {

        if(layerField.isDomain() != null && layerField.isDomain()) {
            adding = true;

            TextView textView = setTextViewInLayout(layerField, rel1);
            rel1.addView(textView);

            if(katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
                EditText editText = setEditTextInLayout(layerField, rel2);
                rel2.addView(editText);
            }else{
                Spinner spinner = setSpinnerInLayout(layerField, rel2);
                rel2.addView(spinner);
            }
        }else{
            if(layerField.getType().equals("int") || layerField.getType().equals("int2") || layerField.getType().equals("int4")){
                adding = true;
                TextView textView = setTextViewInLayout(layerField, rel1);
                rel1.addView(textView);

                EditText editText = setEditTextInLayout(layerField, rel2);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                rel2.addView(editText);
                if(katmanType.equals(KATMAN_TYPE_ADD)){
                    if (layerField.getField().equals("ilce")){
                        findIlceMahalle(latLngs.get(0),"ilce",editText);
                        editText.setText(ilceAd);
                    }else if (layerField.getField().equals("mahalle")){
                        findIlceMahalle(latLngs.get(0),"mahalle",editText);
                        editText.setText(mahAd);
                    }
                }
            } else if(layerField.getType().equals("float") || layerField.getType().equals("float4") || layerField.getType().equals("float8") || layerField.getType().equals("uzunluk")){
                adding = true;
                TextView textView = setTextViewInLayout(layerField, rel1);
                rel1.addView(textView);

                EditText editText = setEditTextInLayout(layerField, rel2);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                rel2.addView(editText);
            } else if(layerField.getType().equals("text") || layerField.getType().equals("varchar")){
                adding = true;
                TextView textView = setTextViewInLayout(layerField, rel1);
                rel1.addView(textView);

                EditText editText = setEditTextInLayout(layerField, rel2);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                rel2.addView(editText);
                if(katmanType.equals(KATMAN_TYPE_ADD)){
                    if  (layerField.getField().equals("park_id")){
                    findPark(latLngs.get(0),editText);
                    editText.setText(parkAd);

                    }
                }

            } else if(layerField.getType().equals("bool")){
                adding = true;
                TextView textView = setTextViewInLayout(layerField, rel1);
                rel1.addView(textView);

                RadioGroup radioGroup = setRadioGroupInLayout(layerField, rel2);
                rel2.addView(radioGroup);
            } else if (layerField.getType().equals("touch_date") || layerField.getType().equals("date")){
                adding = true;
                TextView textView = setTextViewInLayout(layerField, rel1);
                rel1.addView(textView);


                EditText editText = setEditTextInLayout(layerField, rel2);

                if(!katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
                    editText.setText(Utils.getCurrentDate());
                    editText.setEnabled(false);
                }

                rel2.addView(editText);
            } else if (layerField.getType().equals("touch_by")){
                adding = true;
                TextView textView = setTextViewInLayout(layerField, rel1);
                rel1.addView(textView);

                EditText editText = setEditTextInLayout(layerField, rel2);
                if(!katmanType.equals(KATMAN_TYPE_KNOWLEDGE))
                    editText.setText(MainActivity.percon.getUsername());
                editText.setEnabled(false);
                rel2.addView(editText);
            }else {
                Log.e(TAG, "addDynamicViews: BILINMEYEN TYPE" );
                adding = false;
                propertyObjectList.add(new PropertyObject(layerField, findTheValueOfKey(layerField), null, null));
            }

            if(katmanType.equals(KATMAN_TYPE_ADD) && layerField.getField().equals("alan")){
                adding = false;
            }
        }

        return relParent;
    }

    Integer ilceId = null;
    String ilceAd = null;
    Integer mahId = null;
    String mahAd = null;
    String parkId = null;
    String parkAd = null;

    private void findIlceMahalle(LatLng latLng , String type, EditText editText){
        String url = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:"+type+"&maxFeatures=1&outputFormat=application%2Fjson&cql_filter=intersects(geom,%20POINT("+latLng.getLongitude()+"%20"+latLng.getLatitude()+"))";
//        HttpsTrustManager.allowAllSSL();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("features");

                            if (jsonArray.length() > 0) {

                                JSONObject properties = jsonArray.getJSONObject(0).getJSONObject("properties");
                                if (type == "mahalle"){
                                    mahId = properties.getInt("uavtmah");
                                    mahAd = properties.getString("ad");
                                    editText.setText(mahAd);
                                }else if(type == "ilce"){
                                    ilceId = properties.getInt("uavtilce");
                                    ilceAd = properties.getString("ad");
                                    editText.setText(ilceAd);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        //Add the realibility on the connection.
        getRequest .setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(getRequest );
    }

    private void findPark(LatLng latLng, EditText editText){
        String url = "http://78.46.197.92:6080/geoserver/park/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=park%3Apark&maxFeatures=50&outputFormat=application%2Fjson&cql_filter=intersects(geom,%20POINT("+latLng.getLongitude()+"%20"+latLng.getLatitude()+"))";
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("features");

                            if (jsonArray.length() > 0) {

                                JSONObject properties = jsonArray.getJSONObject(0).getJSONObject("properties");
                                    parkId = jsonArray.getJSONObject(0).getString("id");
                                    parkAd = properties.getString("ad");
                                    editText.setText(parkAd);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        //Add the realibility on the connection.
        getRequest .setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(getRequest );
    }

    private TextView setTextViewInLayout(LayerField layerField, RelativeLayout rel1) {
        RelativeLayout.LayoutParams layoutParamRel1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParamRel1.addRule(RelativeLayout.CENTER_VERTICAL, rel1.getId());
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParamRel1);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        if(layerField.getAlias().equals("touch_date"))
            textView.setText("tarih");
        else if(layerField.getAlias().equals("touch_by"))
            textView.setText("kullanıcı");
        else
            textView.setText(layerField.getAlias());

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
                || (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            textView.setTextSize(17);
        }else
            textView.setTextSize(14);


        textView.setTextColor(Color.parseColor("#EFEFEF"));

        return textView;
    }

    private void getNameFromId(EditText editText, String type, String id){
        String url = "";
       if (type.equals("mah") || type.equals("ilce")){
           String typeee = type;
           if (type.equals("mah")){
               typeee = "mahalle";
           }
           url = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:"+typeee+"&maxFeatures=1&outputFormat=application%2Fjson&cql_filter=uavt"+type+"="+id;
       }else if (type.equals("park")){
           url = "http://78.46.197.92:6080/geoserver/park/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=park%3Apark&maxFeatures=50&outputFormat=application%2Fjson&featureid=park."+id;
       }

//        HttpsTrustManager.allowAllSSL();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("features");

                            if (jsonArray.length() > 0) {

                                JSONObject properties = jsonArray.getJSONObject(0).getJSONObject("properties");
                                editText.setText(properties.getString("ad"));


                                    if (type == "mah"){
                                        mahId = properties.getInt("uavtmah");
                                        mahAd = properties.getString("ad");
                                    }else if(type == "ilce"){
                                        ilceId = properties.getInt("uavtilce");
                                        ilceAd = properties.getString("ad");
                                    }else if (type == "park"){
                                        parkAd = properties.getString("ad");
                                        parkId = jsonArray.getJSONObject(0).getString("id");
                                    }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        //Add the realibility on the connection.
        getRequest .setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(getRequest );
    }
    private EditText setEditTextInLayout(LayerField layerField, RelativeLayout rel2){
        RelativeLayout.LayoutParams layoutParamRel2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamRel2.addRule(RelativeLayout.CENTER_VERTICAL, rel2.getId());
        EditText editText = new EditText(getContext());
        editText.setLayoutParams(layoutParamRel2);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setPadding(16,8,8,8);
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setBackground(null);
//        editText.setBackground(getContext().getResources().getDrawable(R.drawable.background_edittext_6));

        if(!katmanType.equals(KATMAN_TYPE_KNOWLEDGE))
            editText.setHint(layerField.getAlias().substring(0, 1).toUpperCase() + layerField.getAlias().substring(1) + " giriniz...");


        editText.setTextSize(14);
        editText.setTextColor(Color.parseColor("#000000"));

        String valueOfKey = findTheValueOfKey(layerField);
        if(layerField.isDomain() != null && layerField.isDomain()){
            if(valueOfKey != null){
                editText.setText(findCodedsToValuesOfJsonObject(Integer.parseInt(valueOfKey), layerField));
            }
        }else{
            if(valueOfKey != null)
                if (layerField.getField().equals("ilce")){
                    getNameFromId(editText,"ilce",valueOfKey);
                }else if(layerField.getField().equals("mahalle")){
                    getNameFromId(editText,"mah",valueOfKey);
                }else if (layerField.getField().equals("park_id")){
                    getNameFromId(editText,"park",valueOfKey);
                }else{
                    editText.setText(valueOfKey);
                }
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        if(katmanType.equals(KATMAN_TYPE_KNOWLEDGE))
            editText.setEnabled(false);

        propertyObjectList.add(new PropertyObject(layerField, findTheValueOfKey(layerField), editText, null));

        return editText;
    }

    private RadioGroup setRadioGroupInLayout(LayerField layerField, RelativeLayout rel2){
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        Color.BLACK //disabled
                        ,Color.parseColor("#0090FF") //enabled
                }
        );

        rel2.setBackground(null);

        RelativeLayout.LayoutParams layoutParamRadio = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParamRadio.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, rel2.getId());
        RadioGroup radioGroup = new RadioGroup(getContext() );
        radioGroup.setGravity(Gravity.RIGHT);
        radioGroup.setLayoutParams(layoutParamRadio);
        radioGroup.setGravity(Gravity.CENTER);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setBackgroundColor(Color.parseColor("#1A2129"));

        RelativeLayout.LayoutParams layoutParamRadioYok = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RadioButton radioButtonYok = new RadioButton(getContext() );
        radioButtonYok.setFocusable(true);
        radioButtonYok.setLayoutParams(layoutParamRadioYok);
        radioButtonYok.setText("Hayır");

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
                || (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            radioButtonYok.setTextSize(18);
        }else
            radioButtonYok.setTextSize(16);

        radioButtonYok.setTextColor(Color.parseColor("#EFEFEF"));



        RelativeLayout.LayoutParams layoutParamRadioVar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamRadioVar.setMargins((int)Utils.convertDpToPx(getContext() ,40),0,0,0);
        RadioButton radioButtonVar = new RadioButton(getContext());
        radioButtonVar.setLayoutParams(layoutParamRadioVar);
        radioButtonVar.setText("Evet");

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
                || (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            radioButtonVar.setTextSize(18);
        }else
            radioButtonVar.setTextSize(16);

        radioButtonVar.setTextColor(Color.parseColor("#EFEFEF"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            radioButtonYok.setButtonTintList(colorStateList);
            radioButtonVar.setButtonTintList(colorStateList);
        }



        radioGroup.addView(radioButtonYok);
        radioGroup.addView(radioButtonVar);

        if(katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
            if(Boolean.parseBoolean(findTheValueOfKey(layerField))){
                radioButtonVar.setChecked(true);
                radioButtonYok.setChecked(false);
            }else{
                radioButtonVar.setChecked(false);
                radioButtonYok.setChecked(true);
            }

            radioButtonVar.setClickable(false);
            radioButtonYok.setClickable(false);
            radioGroup.setClickable(false);
        }


        propertyObjectList.add(new PropertyObject(layerField, findTheValueOfKey(layerField), radioGroup, null));

        return radioGroup;
    }

    private Button setButtonInLayout(LayerField layerField, RelativeLayout rel3){
        RelativeLayout.LayoutParams layoutParamButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParamButton.addRule(RelativeLayout.CENTER_IN_PARENT, rel3.getId());
        Button button = new Button(getContext() );
        button.setLayoutParams(layoutParamButton);
        button.setGravity(Gravity.CENTER);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
                || (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            button.setTextSize(16);
        }else
            button.setTextSize(12);


        button.setTextColor(Color.parseColor("#45666D"));
        button.setAllCaps(false);
        button.setBackgroundResource(R.drawable.background_exit_button);

        return button;
    }

    private Spinner setSpinnerInLayout(LayerField layerField, RelativeLayout rel2){
        RelativeLayout.LayoutParams layoutParamRel2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamRel2.addRule(RelativeLayout.CENTER_VERTICAL, rel2.getId());
        Spinner spinner = new Spinner(getContext());
        spinner.setLayoutParams(layoutParamRel2);
        spinner.setGravity(Gravity.CENTER_VERTICAL);
//        spinner.setBackground(getContext() .getResources().getDrawable(R.drawable.background_edittext_6));

        layerFieldsForSpinner.add(layerField);

        List<LayerFieldCodedValue> layerFieldCodedValueList = layerField.getCodedValues();
        List<String> spinnerIdItemList =new ArrayList<String>();
        spinnerIdItemList.add(layerField.getAlias().substring(0, 1).toUpperCase() + layerField.getAlias().substring(1) + " Seçiniz");

        for(int i=0; i<layerFieldCodedValueList.size(); i++){
            spinnerIdItemList.add(String.valueOf(layerFieldCodedValueList.get(i).getDesc()));
        }

        //Spinner'lar için adapterleri hazırlıyoruz.
        ArrayAdapter dataAdapterForSpinner = new ArrayAdapter<String>(getContext() , android.R.layout.simple_spinner_item, spinnerIdItemList );
        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterForSpinner);

        if(katmanType.equals(KATMAN_TYPE_UPDATE)) {
            for(int i=0; i< layerFieldCodedValueList .size(); i++){
                if(String.valueOf(layerFieldCodedValueList.get(i).getId()).equals(findTheValueOfKey(layerField))){
                    spinner.setSelection(i+1);
                }
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItemPosition() != 0){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        propertyObjectList.add(new PropertyObject(layerField, findTheValueOfKey(layerField), spinner, null));

        return spinner;
    }

    public class AttributeViewHolder {
        public RelativeLayout relativeParent;
        public RelativeLayout relative1;
        public RelativeLayout relative2;
        public RelativeLayout relative3;

        public AttributeViewHolder(View view) {
            relativeParent = view.findViewById(R.id.item_katman_frag_attribute_rel);
            relative1 = relativeParent.findViewById(R.id.item_katman_frag_relative1);
            relative2 = relativeParent.findViewById(R.id.item_katman_frag_relative2);
            relative3 = relativeParent.findViewById(R.id.item_katman_frag_relative3);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Initialize a new BroadcastReceiver instance for listen to coming from MediaFragment
    public BroadcastReceiver listenToMediaFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: GELEN BROADCAST --> mediaFragmenttanGelindi : TRUE");
            mediaFragmenttanGelindi = true;
        }
    };


    private String  findCodedsToValuesOfJsonObject(Integer value, LayerField layerField){
        Log.e(TAG, "findCodedsToValuesOfJsonObject: GELEN JSON WITH CODED: " +  value);

        String returnValueOfCoded = "";

        for(int i=0; i<layerField.getCodedValues().size(); i++){
            if(value == layerField.getCodedValues().get(i).getId()){
                returnValueOfCoded = layerField.getCodedValues().get(i).getDesc();
                break;
            }
        }

        Log.e(TAG, "setCodedsOfJsonObjectForOffline: GIDEN VALUES"  + returnValueOfCoded);

        return returnValueOfCoded;
    }

    private String findTheValueOfKey(LayerField layerField){
        if(!katmanType.equals(KATMAN_TYPE_ADD)){
            try {
                if(properties.has(layerField.getField()))
                {
                    if(!String.valueOf(properties.get(layerField.getField())).equals("null"))
//                        if(String.valueOf(properties.get(layerField.getField())).equals("ilce")){
//                            findIlce(latLngs.get(0),"ilce");
//                            return String.valueOf(properties.get(layerField.getField()));
//                        }else if(String.valueOf(properties.get(layerField.getField())).equals("ilce")) {
//                            return String.valueOf(properties.get(layerField.getField()));
//
//                        }else{
//                            return String.valueOf(properties.get(layerField.getField()));
//                        }
                        return String.valueOf(properties.get(layerField.getField()));

                    else
                        return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean setTheFinalAttributeKeyValue(){
        boolean validValues = true;
        for(int i=0; i<propertyObjectList.size(); i++){
            Boolean isRequired = propertyObjectList.get(i).getLayerField().isRequired();
            String alias = propertyObjectList.get(i).getLayerField().getAlias();
            String key  = propertyObjectList.get(i).getLayerField().getField();
            Object object = propertyObjectList.get(i).getObject();

            if(object instanceof  EditText){
                String tempValue = ((EditText) object).getText().toString();
                if(!tempValue.trim().equals("")){
                    if(key.equals("touch_by")) {
                        if (!katmanType.equals(KATMAN_TYPE_KNOWLEDGE)){
//                            propertyObjectList.get(i).setValue(String.valueOf(MainActivity.percon.getUserId()));
                            propertyObjectList.get(i).setValue(String.valueOf(MainActivity.percon.getUsername()));
                        }
                    }else {
                        propertyObjectList.get(i).setValue(tempValue);
                    }
                }else{
                    propertyObjectList.get(i).setValue(null);

                    if(isRequired != null && isRequired){
                        validValues = false;
                        Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING,alias + " boş olamaz");
                    }
                }

            }else if(object instanceof  Spinner){
                String selectedDesc = ((Spinner) object).getSelectedItem().toString();

                List<LayerFieldCodedValue> layerFieldCodedValues = null;
                for(int a=0; a<layerFieldsForSpinner.size(); a++)
                    if(key.equals(layerFieldsForSpinner.get(a).getField())){
                        layerFieldCodedValues = layerFieldsForSpinner.get(a).getCodedValues();
                        break;
                    }
                String id = null;
                if(layerFieldCodedValues != null)
                    for(int b=0; b<layerFieldCodedValues.size(); b++)
                        if(selectedDesc.equals(layerFieldCodedValues.get(b).getDesc())){
                            id = String.valueOf(layerFieldCodedValues.get(b).getId());
                            break;
                        }
                if(id != null){
                    propertyObjectList.get(i).setValue(id);
                } else{
                    propertyObjectList.get(i).setValue(null);
                    if(isRequired != null && isRequired){
                        validValues = false;
                        Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING,alias + " seçmelisiniz");
                    }
                }
            }else if(object instanceof  RadioGroup){
                RadioButton radioButtonYok  = (RadioButton) ((RadioGroup) object).getChildAt(0);
                RadioButton radioButtonVar = (RadioButton)((RadioGroup) object).getChildAt(1);
                if(radioButtonYok.getId() == ((RadioGroup) object).getCheckedRadioButtonId()) {
                    propertyObjectList.get(i).setValue("false");
                }else if(radioButtonVar.getId() == ((RadioGroup) object).getCheckedRadioButtonId()){
                    propertyObjectList.get(i).setValue("true");
                }else
                    propertyObjectList.get(i).setValue(null);

                String value = propertyObjectList.get(i).getValue();
                if(value == null && isRequired != null && isRequired){
                        validValues = false;
                        Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING,alias + " seçmelisiniz");
                }
            }

            Log.e(TAG, "AAAAAAAAAAA onClick: \nKEY : " + key + "\nVALUE : " + propertyObjectList.get(i).getValue());
        }

        return validValues;
    }
    private List<LatLng> latLngsForUpdate = new ArrayList<>();


    private void fillLatLngsForUpdate(){
        try {
            JSONObject geometryJson = jsonObject.getJSONObject("geometry");
            String type = geometryJson.getString("type");
            JSONArray coordinates = geometryJson.getJSONArray("coordinates");

            if(type.equals(Utils.MULTIPOINT_TYPE)){
                List<Double> enBoy = Utils.metersToDegrees(coordinates.getJSONArray(0).getDouble(1), coordinates.getJSONArray(0).getDouble(0));

                if(coordinates.getJSONArray(0).length() == 3)
                    latLngsForUpdate.add(new LatLng(enBoy.get(0), enBoy.get(1), coordinates.getJSONArray(0).getDouble(2)));
                else if(coordinates.getJSONArray(0).length() == 2)
                    latLngsForUpdate.add(new LatLng(enBoy.get(0), enBoy.get(1), -5000));

            }else if(type.equals(Utils.MULTILINESTRING_TYPE)){
                for(int i=0; i<coordinates.getJSONArray(0).length(); i++){
                    JSONArray latlngJsonArray = coordinates.getJSONArray(0).getJSONArray(i);
                    List<Double> enBoy = Utils.metersToDegrees(latlngJsonArray.getDouble(1), latlngJsonArray.getDouble(0));

                    if(latlngJsonArray.length() == 3)
                        latLngsForUpdate.add(new LatLng(enBoy.get(0), enBoy.get(1), latlngJsonArray.getDouble(2)));
                    else if(latlngJsonArray.length() == 2)
                        latLngsForUpdate.add(new LatLng(enBoy.get(0), enBoy.get(1), -5000));
                }
            }else if(type.equals(Utils.MULTIPOLYGON_TYPE)){
                for(int a=0; a<coordinates.getJSONArray(0).length(); a++){
                    for(int i=0; i<coordinates.getJSONArray(a).length(); i++) {
                        JSONArray latlngJsonArray = coordinates.getJSONArray(a).getJSONArray(0).getJSONArray(i);
                        List<Double> enBoy = Utils.metersToDegrees(latlngJsonArray.getDouble(1), latlngJsonArray.getDouble(0));

                        if(latlngJsonArray.length() == 3)
                            latLngsForUpdate.add(new LatLng(enBoy.get(0), enBoy.get(1), latlngJsonArray.getDouble(2)));
                        else if(latlngJsonArray.length() == 2)
                            latLngsForUpdate.add(new LatLng(enBoy.get(0), enBoy.get(1), -5000));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Geoserver`a post edeceğimiz GEOJSON`ı oluşturmak için kullanıyoruz.
    private JSONObject writeJsonForAdd(){
        JSONObject jsonObjectSample = new JSONObject();

        try {
            jsonObjectSample.put("type", "Feature");

            if(katmanType.equals(KATMAN_TYPE_ADD)){
                JSONObject geometryJsonObject = new JSONObject();
                if(geometryType.equals(Utils.MULTIPOINT_TYPE)){
                    geometryJsonObject.put("type", Utils.MULTIPOINT_TYPE);

                    JSONArray coordinatesJsonArray = new JSONArray();

                    JSONArray coordinateJsonArray0 = new JSONArray();
                    List<Double> xy = Utils.degreesToMeters(latLngs.get(0).getLatitude(), latLngs.get(0).getLongitude());
                    Double x = xy.get(0);
                    Double y = xy.get(1);
                    coordinateJsonArray0.put(y);
                    coordinateJsonArray0.put(x);

                    coordinatesJsonArray.put(coordinateJsonArray0);
                    geometryJsonObject.put("coordinates", coordinatesJsonArray);

                }else if(geometryType.equals(Utils.MULTILINESTRING_TYPE)){
                    geometryJsonObject.put("type", Utils.MULTILINESTRING_TYPE);

                    JSONArray coordinatesJsonArray = new JSONArray();

                    JSONArray coordinateJsonArray0 = new JSONArray();
                    for(int i = 0; i<latLngs.size(); i++){
                        JSONArray coordinateJsonArray0i = new JSONArray();
                        List<Double> xy = Utils.degreesToMeters(latLngs.get(i).getLatitude(), latLngs.get(i).getLongitude());
                        Double x = xy.get(0);
                        Double y = xy.get(1);
                        coordinateJsonArray0i.put(y);
                        coordinateJsonArray0i.put(x);

                        coordinateJsonArray0.put(i, coordinateJsonArray0i);
                    }

                    coordinatesJsonArray.put(coordinateJsonArray0);
                    geometryJsonObject.put("coordinates", coordinatesJsonArray);
                }else if (geometryType.equals(Utils.MULTIPOLYGON_TYPE)){
                    geometryJsonObject.put("type", Utils.MULTIPOLYGON_TYPE);

                    JSONArray coordinatesJsonArrayy = new JSONArray();

                    JSONArray coordinatesJsonArray = new JSONArray();

                    JSONArray coordinateJsonArray0 = new JSONArray();
                    for(int i = 0; i<latLngs.size(); i++){
                        JSONArray coordinateJsonArray0i = new JSONArray();
                        List<Double> xy = Utils.degreesToMeters(latLngs.get(i).getLatitude(), latLngs.get(i).getLongitude());
                        Double x = xy.get(0);
                        Double y = xy.get(1);
                        coordinateJsonArray0i.put(y);
                        coordinateJsonArray0i.put(x);

                        if (i == latLngs.size()-1){
                            List<Double> xy2 = Utils.degreesToMeters(latLngs.get(0).getLatitude(), latLngs.get(0).getLongitude());
                            Double x2 = xy2.get(0);
                            Double y2 = xy2.get(1);
                            coordinateJsonArray0i.put(y2);
                            coordinateJsonArray0i.put(x2);
                        }

                        coordinateJsonArray0.put(i, coordinateJsonArray0i);
                    }

                    coordinatesJsonArray.put(coordinateJsonArray0);

                    coordinatesJsonArrayy.put(coordinatesJsonArray);

                    geometryJsonObject.put("coordinates", coordinatesJsonArrayy);
                }
                jsonObjectSample.put("geometry", geometryJsonObject);
            }else if(katmanType.equals(KATMAN_TYPE_UPDATE)){
//                if(latLngsForUpdate.size() == 0)
//                    fillLatLngsForUpdate();
//
////                jsonObjectSample.put("geometry", this.jsonObjectSample.getJSONObject("geometry"));
//                JSONObject geometryJsonObject = new JSONObject();
//                if(geometryType.equals(Utils.MULTIPOINT_TYPE)){
//                    geometryJsonObject.put("type", Utils.MULTIPOINT_TYPE);
//
//                    JSONArray coordinatesJsonArray = new JSONArray();
//
//                    JSONArray coordinateJsonArray0 = new JSONArray();
//                    List<Double> xy = Utils.degreesToMeters(latLngsForUpdate.get(0).getLatitude(), latLngsForUpdate.get(0).getLongitude());
//                    Double x = xy.get(0);
//                    Double y = xy.get(1);
//                    coordinateJsonArray0.put(y);
//                    coordinateJsonArray0.put(x);
//
//                    if(latLngsForUpdate.get(0).getAltitude() == 0)
//                        coordinateJsonArray0.put(-5000);
//                    else
//                        coordinateJsonArray0.put(latLngsForUpdate.get(0).getAltitude());
//
//
//                    coordinatesJsonArray.put(coordinateJsonArray0);
//                    geometryJsonObject.put("coordinates", coordinatesJsonArray);
//
//                }else if(geometryType.equals(Utils.MULTILINESTRING_TYPE)){
//                    geometryJsonObject.put("type", Utils.MULTILINESTRING_TYPE);
//
//                    JSONArray coordinatesJsonArray = new JSONArray();
//
//                    JSONArray coordinateJsonArray0 = new JSONArray();
//                    for(int i = 0; i<latLngsForUpdate.size(); i++){
//                        JSONArray coordinateJsonArray0i = new JSONArray();
//                        List<Double> xy = Utils.degreesToMeters(latLngsForUpdate.get(i).getLatitude(), latLngsForUpdate.get(i).getLongitude());
//                        Double x = xy.get(0);
//                        Double y = xy.get(1);
//                        coordinateJsonArray0i.put(y);
//                        coordinateJsonArray0i.put(x);
//
//                        if(latLngsForUpdate.get(i).getAltitude() == 0)
//                            coordinateJsonArray0i.put(-5000);
//                        else
//                            coordinateJsonArray0i.put(latLngsForUpdate.get(i).getAltitude());
//
//
//                        coordinateJsonArray0.put(i, coordinateJsonArray0i);
//                    }
//                    coordinatesJsonArray.put(coordinateJsonArray0);
//                    geometryJsonObject.put("coordinates", coordinatesJsonArray);
//                }else if (geometryType.equals(Utils.MULTIPOLYGON_TYPE)){
//                    geometryJsonObject.put("type", Utils.MULTIPOLYGON_TYPE);
//
//                    JSONArray coordinatesJsonArrayy = new JSONArray();
//
//                    JSONArray coordinatesJsonArray = new JSONArray();
//
//                    JSONArray coordinateJsonArray0 = new JSONArray();
//                    for(int i = 0; i<latLngsForUpdate.size(); i++){
//                        JSONArray coordinateJsonArray0i = new JSONArray();
//                        List<Double> xy = Utils.degreesToMeters(latLngsForUpdate.get(i).getLatitude(), latLngsForUpdate.get(i).getLongitude());
//                        Double x = xy.get(0);
//                        Double y = xy.get(1);
//                        coordinateJsonArray0i.put(y);
//                        coordinateJsonArray0i.put(x);
//
//                        if (i == latLngsForUpdate.size()-1){
//                            List<Double> xy2 = Utils.degreesToMeters(latLngsForUpdate.get(0).getLatitude(), latLngsForUpdate.get(0).getLongitude());
//                            Double x2 = xy2.get(0);
//                            Double y2 = xy2.get(1);
//                            coordinateJsonArray0i.put(y2);
//                            coordinateJsonArray0i.put(x2);
//                        }
//
//                        coordinateJsonArray0.put(i, coordinateJsonArray0i);
//                    }
//
//                    coordinatesJsonArray.put(coordinateJsonArray0);
//
//                    coordinatesJsonArrayy.put(coordinatesJsonArray);
//
//                    geometryJsonObject.put("coordinates", coordinatesJsonArrayy);
//                }
//                jsonObjectSample.put("geometry", geometryJsonObject);

                JSONObject geometryJson = jsonObject.getJSONObject("geometry");
                jsonObjectSample.put("geometry", geometryJson);


            }


            JSONObject propertiesJsonObject = new JSONObject();
            for (PropertyObject propertyObject : propertyObjectList){
                if(propertyObject.getValue() != null)
                    if (propertyObject.getLayerField().getField().equals("ilce")){
                        propertiesJsonObject.put(propertyObject.getLayerField().getField(), ilceId);
                    }else if(propertyObject.getLayerField().getField().equals("mahalle")){
                        propertiesJsonObject.put(propertyObject.getLayerField().getField(), mahId);
                    }else if(propertyObject.getLayerField().getField().equals("park_id")){
                        if (parkId != null){
                                String[] arrOfStr = parkId.split("\\.");
                                if (arrOfStr.length > 1){
                                    propertiesJsonObject.put(propertyObject.getLayerField().getField(), arrOfStr[1]);
                                }
                        }
                    }else{
                        propertiesJsonObject.put(propertyObject.getLayerField().getField(), propertyObject.getValue());
                    }
                else
                    propertiesJsonObject.put(propertyObject.getLayerField().getField(), JSONObject.NULL);

            }
            jsonObjectSample.put("properties", propertiesJsonObject );

            if(!katmanType.equals(KATMAN_TYPE_ADD) && !katmanType.equals(KATMAN_TYPE_NOT_ADD))
                jsonObjectSample.put("id", featureId);

            if(katmanType.equals(KATMAN_TYPE_ADD)){
                jsonObjectSample.put("operation", "Insert");
                jsonObjectSample.put("typeName", layerModel.getLayer());
            }else if(katmanType.equals(KATMAN_TYPE_UPDATE)){
                jsonObjectSample.put("operation", "Update");
                jsonObjectSample.put("typeName", feature);
            }

            jsonObjectSample.put("workspace", "park");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObjectSample;
    }

    private void postTheDataToGeoserver(String url, String xmlData){
        ACProgressFlower acProgressFlower = Utils.progressDialogLikeIosWithoutText(getContext());
        if(acProgressFlower != null)
            acProgressFlower.show();

        Log.e(TAG, "postTheDataToGeoserver --> " + xmlData );

        HttpsTrustManager.allowAllSSL();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(acProgressFlower != null && acProgressFlower.isShowing())
                            acProgressFlower.dismiss();
                        Log.e(TAG, "postTheXmlDataToGeoserver -> onResponse: " +response);

                        if(response != null){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.has("success")){
                                    if(jsonObject.getBoolean("success")) {
                                        Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_SUCCESS, "Veri " + Utils.ADD + " başarılı.");
                                        //Map`e ekli layerları güncelliyoruz
                                        MainActivity.refreshAddedLayers();
                                    }else
                                        Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, "Veri "+Utils.ADD+" başarısız.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else
                            Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, "Veri "+Utils.ADD+" başarısız.");



                        MainActivity.fragment = null;
                        getActivity().getSupportFragmentManager().popBackStack();
                        getActivity().getSupportFragmentManager().popBackStack();
//                        setTheActiveFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(acProgressFlower != null && acProgressFlower.isShowing())
                            acProgressFlower.dismiss();
                        Log.e(TAG, "onErrorResponse: ERROR RESPONSE : " + error.getMessage());


                        Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, "Veri "+Utils.ADD+" başarısız.");

                        error.printStackTrace();
                        Log.e(TAG, "onErrorResponse: " + error.getMessage() + error.toString());
                    }
                }
                ) {

                    @Override
                    public String getBodyContentType() {
//                        return "text/plain;  charset=utf-8";
                        return "application/json;  charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");

                        return headers;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return xmlData == null ? null : xmlData.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", xmlData, "utf-8");
                            return null;
                        }
                    }
                };
        // Add the realibility on the connection.
        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(postRequest);
    }
    @SuppressLint("StaticFieldLeak")
    private void postTheDataToGeoserverWithAttachment(String url, String xmlData){
        ACProgressFlower acProgressFlower = Utils.progressDialogLikeIosWithoutText(getContext());

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(acProgressFlower != null)
                    acProgressFlower.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    MultipartBody.Builder builderNew = new MultipartBody.Builder("------WebKitFormBoundaryGIGnJ43LpDeU3x2F")
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("transaction", xmlData);

                    for (int i = 0; i < mediaListForRecycler.size(); i++) {
//                        File file = new File(mediaListForRecycler.get(i).getUri().toString());           //Hata veriyor o yüzden FileUtil classı kullanıldı.

                        if(!mediaListForRecycler.get(i).getExistService()){
                            File file = FileUtil.from(context, mediaListForRecycler.get(i).getUri());

                            if (file.exists()) {
//                                double fileSize =(double) file.length()/(1024*1024);
//
//                                if(fileSize <= 8){
//                                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
//                                    String mimeType = Objects.requireNonNull(MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension));
//                                    MediaType mediaType = MediaType.parse(mimeType);
//
//                                    Log.e(TAG, "doInBackground: fileExtension : " + fileExtension );
//                                    Log.e(TAG, "doInBackground: mimeType : " + mimeType );
//                                    Log.e(TAG, "doInBackground: file.getName : " + file.getName() );
//
//                                    builderNew.addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file));
//                                }else{
                                    if(mediaListForRecycler.get(i).getType().equals(Utils.IMAGE)){
                                        File scaledImageFile = scaleDownImageFile(file, 1024);
//                                        File scaledImageFile = scaleDownImage(file);

                                        if(scaledImageFile != null && scaledImageFile.exists()){
                                            double scaledImageFileSize =(double) scaledImageFile.length()/(1024*1024);
                                            if(scaledImageFileSize <= 8){
                                                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(scaledImageFile .getPath());
                                                String mimeType = Objects.requireNonNull(MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension));
                                                MediaType mediaType = MediaType.parse(mimeType);

                                                Log.e(TAG, "doInBackground: scaledImageFile - fileExtension : " + fileExtension );
                                                Log.e(TAG, "doInBackground: scaledImageFile - mimeType : " + mimeType );
                                                Log.e(TAG, "doInBackground: scaledImageFile - file.getName : " + scaledImageFile.getName() );

                                                builderNew.addFormDataPart("file", scaledImageFile.getName(), RequestBody.create(mediaType, scaledImageFile));
                                            }else{
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_WARNING, "Resim boyutu çok büyük!");
                                                    }
                                                });
                                            }

                                        }
                                    }
//                                }
                            }
                        }
                    }

                    MultipartBody requestBody = builderNew.build();


                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();


//                    OkHttpClient okHttpClient = getUnsafeOkHttpClient(); //Bu sertifika probleminden dolayı yazıldı.
                    OkHttpClient okHttpClient = new OkHttpClient();
                    OkHttpClient eagerClient = okHttpClient.newBuilder()
                            .readTimeout(25000, TimeUnit.MILLISECONDS)
                            .build();

                    okhttp3.Response response = eagerClient.newCall(request).execute();
//                    okhttp3.Response response = okHttpClient.newCall(request).execute();

                    Log.e(TAG, "doInBackground: okhttp3  RESPONSE : " + response.toString());

                    if(response.isSuccessful())
                        return response.body().string();
                    else
                        return null;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(acProgressFlower != null && acProgressFlower.isShowing())
                    acProgressFlower.dismiss();

                Log.e(TAG, "onPostExecute: RESULT : " + result);

                if(result != null){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if(jsonObject.has("success")){
                            if(jsonObject.getBoolean("success")) {
                                Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_SUCCESS, "Veri " + Utils.ADD + " başarılı.");
                                //Map`e ekli layerları güncelliyoruz
                                MainActivity.refreshAddedLayers();
                            }else
                                Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, "Veri "+Utils.ADD+" başarısız.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, "Veri "+Utils.ADD+" başarısız.");


                MainActivity.fragment = null;
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().popBackStack();

//                setTheActiveFragment();
            }

        }.execute();
    }

    //Library kullanmadan Image file resize yapıldı.
    private File scaleDownImageFile(File imgFileOrig, float maxImageSize){

        Bitmap realImage = BitmapFactory.decodeFile(imgFileOrig.getAbsolutePath());

        Log.e(TAG, "scaleDownBitmap: imgFileOrig.getAbsolutePath() : " + imgFileOrig.getAbsolutePath());

        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Log.e(TAG, "scaleDownBitmap: ratio : " + ratio );

        Log.e(TAG, "scaleDownBitmap: real with : " + realImage.getWidth());
        Log.e(TAG, "scaleDownBitmap: real height : " + realImage.getHeight());
        Log.e(TAG, "scaleDownBitmap: with : " + width );
        Log.e(TAG, "scaleDownBitmap: height : " + height );

        if(ratio >= 1.0)
            return imgFileOrig;

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, false);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

//        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Kgm_Attachment");     //External Storage`e kaydedeceksek burayı kullanacagız.
        File folder = new File(getContext().getFilesDir(), "Kgm_Attachment");   //Internal Storage`e kaydedeceksek burayı kullanacagız.

        if (!folder.exists())
            folder.mkdir();

        File newFile = null;
        if(folder.exists()){

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_scaled" + timeStamp;

            newFile = new File(folder.getPath() + File.separator + imageFileName + ".jpg");
            Log.e(TAG, "scaleDownBitmap: new file path : " + newFile.getAbsolutePath());

            try {
                if(newFile.exists())
                    newFile.delete();

                newFile.createNewFile();

                if(newFile.exists()){
                    FileOutputStream fo = new FileOutputStream(newFile);
                    fo.write(outStream.toByteArray());
                    fo.flush();
                    fo.close();

                    if(newBitmap != null){
                        newBitmap.recycle();
                        newBitmap = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return newFile;
    }


    private void getTheAttachmentDataOfLayerFeatureWithId(String id) {
        String url = Const.feature_attachment_get_url + id;

        ACProgressFlower progressFlower = Utils.progressDialogLikeIosWithoutText(getContext());
        if(progressFlower != null)
            progressFlower.show();


//        HttpsTrustManager.allowAllSSL();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(progressFlower != null && progressFlower.isShowing())
                            progressFlower.dismiss();


                        Log.e(TAG, "onResponse: getTheAttachmentDataOfLayerFeatureWithId : " + response );

                        String url = Const.feature_attachment_get_url + id;
                        //Resimde şu an için yalnızca Glide kullanıyoruz.
                        updateListAfterAdding(new KatmanMedia(Uri.parse(url), Utils.IMAGE, true, null));

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressFlower != null && progressFlower.isShowing())
                    progressFlower.dismiss();


                error.printStackTrace();
                Log.e(TAG, "getTheAttachmentDataOfLayerFeatureWithId --> onErrorResponse:\n" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        //Add the realibility on the connection.
        getRequest .setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(getRequest);
    }
}
