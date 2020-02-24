package com.example.msi.demoo.dialogfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.app.AppController;
import com.example.msi.demoo.models.IlIlceMahalle;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.turf.TurfMeasurement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;
import info.hoang8f.android.segmented.SegmentedGroup;

@SuppressLint("ValidFragment")
public class KategorikAramaDialog extends DialogFragment{
    private static final String TAG = "KategorikAramaDialog";

    private static final String IL = "il";
    private static final String ILCE = "ilce";
    private static final String MAHALLE = "mahalle";
    private static final String ADA_PARSEL = "ada_parsel";

    public static final String ilSource = "il.source";
    public static final String ilLayer = "il.layer";
    public static final String ilceSource = "ilce.source";
    public static final String ilceLayer = "ilce.layer";
    public static final String mahalleSource = "mahalle.source";
    public static final String mahalleLayer = "mahalle.layer";
    public static final String adaSource = "ada.source";
    public static final String adaLayer = "ada.layer";

    private Activity activity;

    public static boolean isAddedLayers = false;

    private boolean selectedSegmentAdres = true;

//    private IlIlceMahalle selectedIl = null;
    private IlIlceMahalle selectedIlce = null;
    private IlIlceMahalle selectedMahalle = null;

    private ProgressBar progressBar;
//    private Spinner ilSp;
    private Spinner ilceSp, mahalleSp;
    private Button temizle, git;
    private ImageView kapat;

    private EditText adaTV, parselTV;
    private ArrayAdapter<String> ilAdapter, ilceAdapter, mahalleAdapter;

//    private List<IlIlceMahalle> ilList = new ArrayList<>();
    private List<IlIlceMahalle> ilceList = new ArrayList<>();
    private List<IlIlceMahalle> mahalleList = new ArrayList<>();

//    private List<String> ilStringList = new ArrayList<>();
    private List<String> ilceStringList = new ArrayList<>();
    private List<String> mahalleStringList = new ArrayList<>();

    public KategorikAramaDialog(Activity activity) {
        this.activity= activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View rootView = inflater.inflate(R.layout.dialog_kategorik_arama, null);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        SegmentedGroup segmented2 = (SegmentedGroup) rootView.findViewById(R.id.segmented2);
//        segmented2.setTintColor(Color.DKGRAY);
//        segmented2.setTintColor(Color.parseColor("#FFD0FF3C"), Color.parseColor("#FF7B07B2"));
//        segmented2.setTintColor(getResources().getColor(R.color.radio_button_selected_color));

        RadioButton radioButtonAdres = rootView.findViewById(R.id.button21);
        RadioButton radioButtonAdaParsel = rootView.findViewById(R.id.button22);


        progressBar = rootView.findViewById(R.id.dialog_kategorik_arama_progress_bar);
//        ilSp = rootView.findViewById(R.id.dialog_kategorik_arama_il_sp);
        ilceSp = rootView.findViewById(R.id.dialog_kategorik_arama_ilce_sp);
        mahalleSp = rootView.findViewById(R.id.dialog_kategorik_arama_mahalle_sp);
        adaTV = rootView.findViewById(R.id.dialog_kategorik_arama_ada);
        parselTV = rootView.findViewById(R.id.dialog_kategorik_arama_parsel);

        temizle = rootView.findViewById(R.id.dialog_kategorik_arama_temizle);
        git = rootView.findViewById(R.id.dialog_kategorik_arama_git);
        kapat = rootView.findViewById(R.id.dialog_kategorik_arama_close);


//        setIllerSpinner();
        setIlcelerSpinner();
        setMahallelerSpinner();


//        ilSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(ilList.size() > position && position > 0){
//                    selectedIl= ilList.get(position);
//
//                    String ilceUrl;
//                    if(selectedSegmentAdres)
//                        ilceUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_ilce" +
//                            "&maxFeatures=500&outputFormat=application%2Fjson&propertyname=ad,uavtilce&sortby=ad&cql_filter=uavtil=" + selectedIl.getId();
//                    else
//                        ilceUrl = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api//idariYapi/ilceListe/" + selectedIl.getId();
//                    getIlIlceMahallelerinDatas(ilceUrl, Utils.ILCELER);
//
//                }else{
//                    selectedIl = null;
//                    if(ilceStringList.size() > 1){
//                        ilceStringList.clear();
//                        ilceStringList.add(0, "İlçe Seçiniz");
//                        ilceAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        ilceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(ilceList.size() > position && position > 0){

                    for(int i=0; i<ilceList.size(); i++)
                        if(ilceList.get(i).getText().equals(ilceStringList.get(position))){
                            selectedIlce = ilceList.get(i);
                            break;
                        }

                    String mahalleUrl;
                    if(selectedSegmentAdres)
                        mahalleUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_mahalle" +
                                "&maxFeatures=500&outputFormat=application%2Fjson&propertyname=ad,uavtmah&sortby=ad&cql_filter=uavtilce=" + selectedIlce.getId();
                    else
                        mahalleUrl = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api//idariYapi/mahalleListe/" + selectedIlce.getId();
                    getIlIlceMahallelerinDatas(mahalleUrl, Utils.MAHALLELER);

                }else{
                    selectedIlce = null;
                    selectedMahalle = null;
                    if(mahalleStringList.size() > 1){
                        mahalleStringList.clear();
                        mahalleStringList.add(0, "Mahalle Seçiniz");
                        mahalleAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mahalleSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mahalleList.size() > position && position > 0){
                    for(int i=0; i<mahalleList.size(); i++){
                        if(mahalleList.get(i).getText().equals(mahalleStringList.get(position))){
                            selectedMahalle = mahalleList.get(i);

                            Log.e(TAG, "onItemSelected: "+ selectedMahalle.getText());
                        }
                    }
                }else
                    selectedMahalle = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        git.setOnClickListener(onClickListener);
        temizle.setOnClickListener(onClickListener);
        kapat.setOnClickListener(onClickListener);


        segmented2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button21:
                        selectedSegmentAdres = true;

                        adaTV.setVisibility(View.GONE);
                        parselTV.setVisibility(View.GONE);

//                        ilList.clear();
                        ilceList.clear();
                        mahalleList.clear();
//                        ilStringList.clear();
//                        ilStringList.add(0,"İl Seçiniz");
                        ilceStringList.clear();
                        ilceStringList.add(0, "İlçe Seçiniz");
                        mahalleStringList.clear();
                        mahalleStringList.add(0, "Mahalle Seçiniz");


                        if(Utils.internetControl(getContext())){
//                            if(ilList.size() == 0){
//                                String ilUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_il" +
//                                        "&maxFeatures=500&outputFormat=application%2Fjson&propertyname=ad,uavtil&sortby=ad";
//                                getIlIlceMahallelerinDatas(ilUrl, Utils.ILLER);
//                            }

                            if(ilceList.size() == 0){
//                                String ilceUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_ilce" +
//                                        "&maxFeatures=500&outputFormat=application%2Fjson&propertyname=ad,uavtilce&sortby=ad&cql_filter=uavtil=" + selectedIl.getId();
                                String ilceUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_ilce" +
                                        "&maxFeatures=500&outputFormat=application%2Fjson&propertyname=ad,uavtilce&sortby=ad&cql_filter=uavtil=" + 34;
                                getIlIlceMahallelerinDatas(ilceUrl, Utils.ILCELER);
                            }
                        }

                        break;
                    case R.id.button22:
                        selectedSegmentAdres = false;

                        adaTV.setVisibility(View.VISIBLE);
                        parselTV.setVisibility(View.VISIBLE);

//                        ilList.clear();
                        ilceList.clear();
                        mahalleList.clear();
//                        ilStringList.clear();
//                        ilStringList.add(0,"İl Seçiniz");
                        ilceStringList.clear();
                        ilceStringList.add(0, "İlçe Seçiniz");
                        mahalleStringList.clear();
                        mahalleStringList.add(0, "Mahalle Seçiniz");

                        if(Utils.internetControl(getContext())){
//                            if(ilList.size() == 0){
//                                String ilUrl = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api/idariYapi/ilListe";
//                                getIlIlceMahallelerinDatas(ilUrl, Utils.ILLER);
//                            }

                            if(ilceList.size() == 0){
//                                String ilceUrl = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api//idariYapi/ilceListe/" + selectedIl.getId();
                                String ilceUrl = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api//idariYapi/ilceListe/" + 77;
                                getIlIlceMahallelerinDatas(ilceUrl, Utils.ILCELER);
                            }
                        }
                        break;
                }
            }
        });

        radioButtonAdres.setChecked(true);

        return rootView;
    }

//    private void setIllerSpinner(){
//        ilStringList.add(0,"İl Seçiniz");
//
//        ilAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ilStringList);
//        ilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ilSp.setAdapter(ilAdapter);
//
//    }
    private void setIlcelerSpinner(){
        ilceStringList.add(0, "İlçe Seçiniz");

        ilceAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ilceStringList);
        ilceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ilceSp.setAdapter(ilceAdapter);
    }
    private void setMahallelerSpinner(){
        mahalleStringList.add(0, "Mahalle Seçiniz");

        mahalleAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mahalleStringList);
        mahalleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mahalleSp.setAdapter(mahalleAdapter);
    }


    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_kategorik_arama_git:
//                    removeIlToMap();
                    removeIlceToMap();
                    removeMahalleToMap();
                    removeAdaParselToMap();

                    if(selectedSegmentAdres){
//                        if(selectedIl != null)
//                            getIlIlceMahallleAdaGeoJsonForWfs(IL);

                        if(selectedIlce != null)
                            getIlIlceMahallleAdaGeoJsonForWfs(ILCE);

                        if(selectedMahalle != null)
                            getIlIlceMahallleAdaGeoJsonForWfs(MAHALLE);

                    }else{
                        if(selectedMahalle != null && !adaTV.getText().toString().trim().equals("") && !parselTV.getText().toString().trim().equals(""))
                            getIlIlceMahallleAdaGeoJsonForWfs(ADA_PARSEL);
                    }

                    dismiss();
                    break;
                case R.id.dialog_kategorik_arama_temizle:
//                    ilSp.setSelection(0);
//                    ilceStringList.clear();
//                    ilceStringList.add(0, "İlçe Seçiniz");
//                    ilceAdapter.notifyDataSetChanged();
                    ilceSp.setSelection(0);

                    mahalleStringList.clear();
                    mahalleStringList.add(0, "Mahalle Seçiniz");
                    mahalleAdapter.notifyDataSetChanged();

//                    removeIlToMap();
                    removeIlceToMap();
                    removeMahalleToMap();
                    removeAdaParselToMap();
                    isAddedLayers = false;
                    break;
                case R.id.dialog_kategorik_arama_close:
                    dismiss();
                    break;
            }
        }
    };


    private void getIlIlceMahallelerinDatas(String url, String type) {
        progressBar.setVisibility(View.VISIBLE);

//        HttpsTrustManager.allowAllSSL();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "getIlIlceMahalleDatas --> onResponse: " + response);

                        progressBar.setVisibility(View.GONE);

                        if(type.equals(Utils.ILLER)){
//                            setIlList(response);
                        }else if(type.equals(Utils.ILCELER)){
                            setIlceList(response);
                        }else if(type.equals(Utils.MAHALLELER)){
                            setMahalleList(response);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
                Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
                Utils.showCustomToast(activity, Utils.CUSTOM_TOAST_ERROR, "Hata oluştu!");
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


//    private void setIlList(String stringIller){
//        try{
//            JSONObject jsonObject = new JSONObject(stringIller);
//            JSONArray jsonArray = jsonObject.getJSONArray("features");
//
//            List<IlIlceMahalle> tempIlList = new ArrayList<>();
//            for(int i=0; i<jsonArray.length(); i++){
//                Log.e(TAG, "setIlList: " + jsonArray.get(i).toString());
//
//                JSONObject jsonObjectIl = jsonArray.getJSONObject(i);
//                JSONObject properties = jsonObjectIl.getJSONObject("properties");
//                if(selectedSegmentAdres){
//                    String ad = properties.getString("ad");
//                    String uavtil = properties.getString("uavtil");
//                    tempIlList.add(new IlIlceMahalle(uavtil, ad, jsonObjectIl.toString()));
//                }else{
//                    String text = properties.getString("text");
//                    String id = properties.getString("id");
//                    tempIlList.add(new IlIlceMahalle(id, text, jsonObjectIl.toString()));                }
//            }
//
//            ilList.clear();
//            ilList.add(0,  new IlIlceMahalle("-1","İl Seçiniz", null));
//            ilList.addAll(tempIlList);
//
//            ilStringList.clear();
//            for(int i=0; i<ilList.size(); i++)
//                ilStringList.add(ilList.get(i).getText());
//
//            ilAdapter.notifyDataSetChanged();
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
    private void setIlceList(String stringIlceler){
        try{
            JSONObject jsonObject = new JSONObject(stringIlceler);
            JSONArray jsonArray = jsonObject.getJSONArray("features");

            List<IlIlceMahalle> tempIlceList
                    = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++){
                Log.e(TAG, "setIlceList: " + jsonArray.get(i).toString() );

                JSONObject jsonObjectIl = jsonArray.getJSONObject(i);
                JSONObject properties = jsonObjectIl.getJSONObject("properties");

                if(selectedSegmentAdres){
                    String ad = properties.getString("ad");
                    String uavtilce = properties.getString("uavtilce");
                    tempIlceList.add(new IlIlceMahalle(uavtilce, ad, jsonObjectIl.toString()));
                }else{
                    String text = properties.getString("text");
                    String id = properties.getString("id");
                    tempIlceList.add(new IlIlceMahalle(id, text, jsonObjectIl.toString()));
                }

            }

            ilceList.clear();
            ilceList.add(0,  new IlIlceMahalle("-1","İlce Seçiniz", null));
            ilceList.addAll(tempIlceList);

            ilceStringList.clear();
            for(int i=0; i<ilceList.size(); i++)
                ilceStringList.add(ilceList.get(i).getText());

            ilceAdapter.notifyDataSetChanged();
            ilceSp.setSelection(0);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void setMahalleList(String stringMahalleler){
        try{
            JSONObject jsonObject = new JSONObject(stringMahalleler);

            mahalleList.clear();
            mahalleList.add(0, new IlIlceMahalle("-1", "Mahalle Seçiniz", null));

            JSONArray jsonArray = jsonObject.getJSONArray("features");
            for(int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObjectMahalle = jsonArray.getJSONObject(i);
                JSONObject properties = jsonObjectMahalle.getJSONObject("properties");

                if(selectedSegmentAdres){
                    String ad = properties.getString("ad");
                    String uavtmah = properties.getString("uavtmah");
                    mahalleList.add(new IlIlceMahalle(uavtmah, ad, jsonObjectMahalle.toString()));
                }else{
                    String text = properties.getString("text");
                    String id = properties.getString("id");
                    mahalleList.add(new IlIlceMahalle(id, text, jsonObjectMahalle.toString()));
                }
            }

            mahalleStringList.clear();
            for(int i=0; i<mahalleList.size(); i++)
                mahalleStringList.add(mahalleList.get(i).getText());

            mahalleAdapter.notifyDataSetChanged();
            mahalleSp.setSelection(0);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    private void getIlIlceMahallleAdaGeoJsonForWfs(String ilIlceMahalleAdaType){
        String url = "";
        if(ilIlceMahalleAdaType.equals(IL)){
//            url = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_il" +
//                    "&maxFeatures=500&outputFormat=application%2Fjson&cql_filter=uavtil=" + selectedIl.getId();
        }else if(ilIlceMahalleAdaType.equals(ILCE)){
            url = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_ilce" +
                    "&maxFeatures=500&outputFormat=application%2Fjson&cql_filter=uavtilce=" + selectedIlce.getId();
        }else if(ilIlceMahalleAdaType.equals(MAHALLE)){
            url = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_mahalle" +
                    "&maxFeatures=500&outputFormat=application%2Fjson&cql_filter=uavtmah=" + selectedMahalle.getId();
        }else if(ilIlceMahalleAdaType.equals(ADA_PARSEL)){
            url = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api/parsel/" + selectedMahalle.getId() + "/" + adaTV.getText().toString().trim() + "/" + parselTV.getText().toString().trim();
        }


        ACProgressFlower acProgressFlower = Utils.progressDialogLikeIosWithoutText(getContext());
        if(acProgressFlower != null)
            acProgressFlower.show();

//            HttpsTrustManager.allowAllSSL();
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(acProgressFlower != null && acProgressFlower.isShowing())
                            acProgressFlower.dismiss();

                        try {
                            if(ilIlceMahalleAdaType.equals(ADA_PARSEL)){
                                addAdaParselToMap(response);
                            }else{

                                    JSONObject responseJsonObject = new JSONObject(response);
                                    JSONArray features = responseJsonObject.getJSONArray("features");
                                    if(features.length() == 1){
                                        JSONObject feature0 =  features.getJSONObject(0);

                                        if(ilIlceMahalleAdaType.equals(IL)){
//                                            new Handler().postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    addIlToMap(feature0.toString());
//                                                }
//                                            }, 0);
                                        }else if(ilIlceMahalleAdaType.equals(ILCE)){
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addIlceToMap(feature0.toString());
                                                }
                                            }, 0);
                                        }else if(ilIlceMahalleAdaType.equals(MAHALLE)){
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addMahalleToMap(feature0.toString());
                                                }
                                            }, 1200);
                                        }

                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(acProgressFlower != null && acProgressFlower.isShowing())
                    acProgressFlower.dismiss();

                error.printStackTrace();
                Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
                Utils.showCustomToast(activity, Utils.CUSTOM_TOAST_ERROR, "Hata oluştu!");
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(request);
    }

//    private void addIlToMap(String ilGeoJson){
//        Log.e(TAG, "addIlToMap: ilGeojson" + ilGeoJson);
//
//        if(selectedIl != null){
//            isAddedLayers = true;
//            MainActivity.addGeoJsonSourceToMapFromJson(ilSource, ilGeoJson);
//            MainActivity.addGeoJsonPolygonLayerToMap(ilLayer, ilSource, (float) 0.3, "#00d500");
//
//            zoomToIlIlceMahalle(ilGeoJson);
//        }
//    }
//    public static void removeIlToMap() {
//        MainActivity.removeLayerFromMap(ilLayer);
//        MainActivity.removeSourceFromMap(ilSource);
//    }

    private void addIlceToMap(String ilceGeoJson){
        if(selectedIlce != null){
            isAddedLayers = true;
            MainActivity.addGeoJsonSourceToMapFromJson(ilceSource, ilceGeoJson);
            MainActivity.addGeoJsonPolygonLayerBelowOfSpecificLayerToMap(ilceLayer, ilceSource, (float) 0.3, "#d50000", mahalleLayer);

            zoomToIlIlceMahalle(ilceGeoJson);
        }
    }
    public static void removeIlceToMap(){
        MainActivity.removeLayerFromMap(ilceLayer);
        MainActivity.removeSourceFromMap(ilceSource);
    }


    private void addMahalleToMap(String mahalleGeoJson){
        if(selectedMahalle != null){
            isAddedLayers = true;
            MainActivity.addGeoJsonSourceToMapFromJson(mahalleSource, mahalleGeoJson);
            MainActivity.addGeoJsonPolygonLayerToMap(mahalleLayer, mahalleSource, (float) 0.3, "#18ffff");

            zoomToIlIlceMahalle(mahalleGeoJson);
        }
    }
    public static void removeMahalleToMap(){
        MainActivity.removeLayerFromMap(mahalleLayer);
        MainActivity.removeSourceFromMap(mahalleSource);
    }


    private void addAdaParselToMap(String adaGeoJson){
        if(selectedMahalle != null){
            isAddedLayers = true;
            MainActivity.addGeoJsonSourceToMapFromJson(adaSource, adaGeoJson);
            MainActivity.addGeoJsonPolygonLayerToMap(adaLayer, adaSource, (float) 0.3, "#F9DB22");

            zoomToIlIlceMahalle(adaGeoJson);
        }
    }
    public static void removeAdaParselToMap(){
        MainActivity.removeLayerFromMap(adaLayer + ".symbol");
        MainActivity.removeSourceFromMap(adaSource + ".symbol");

        MainActivity.removeLayerFromMap(adaLayer + ".line");
        MainActivity.removeLayerFromMap(adaLayer);
        MainActivity.removeSourceFromMap(adaSource);
    }


    public static void zoomToIlIlceMahalle(String geoJson){
        try {
            JSONObject jsonObject = new JSONObject(geoJson);
            JSONObject jsonObjectGeometry = jsonObject.getJSONObject("geometry");
            String type = jsonObjectGeometry.getString("type");

            double[] bbox;
            if(type.equals("Polygon")){
                bbox = TurfMeasurement.bbox(Polygon.fromJson(jsonObjectGeometry.toString()));
            }else {
                bbox = TurfMeasurement.bbox(MultiPolygon.fromJson(jsonObjectGeometry.toString()));
            }

            MainActivity.moveCameraToBoundingBox(new LatLng(bbox[1], bbox[0]), new LatLng(bbox[3], bbox[2]), 64, 900);
        }catch (Exception e){ e.printStackTrace(); }
    }
}

