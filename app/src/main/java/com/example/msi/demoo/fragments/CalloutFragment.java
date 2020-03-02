package com.example.msi.demoo.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.app.AppController;
import com.example.msi.demoo.dialogfragments.KatmanlarDialog;
import com.example.msi.demoo.models.LayerField;
import com.example.msi.demoo.models.LayerFieldCodedValue;
import com.example.msi.demoo.models.LayerModel;
import com.example.msi.demoo.utils.Const;
import com.example.msi.demoo.utils.HttpsTrustManager;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.geojson.Feature;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

@SuppressLint("ValidFragment")
public class CalloutFragment extends Fragment {
    private Button calloutDüzenle, calloutBilgi, calloutGeometriDüzenle, calloutSil, calloutNotEkle, calloutKmGit;
    private RelativeLayout calloutChildRelative, calloutParentRelative;
    private static final String TAG = "CalloutFragment";

    private PointF pointF = null;
    private Feature selectedFeature = null;
    private JSONObject jsonObject = null;

    private Integer permission = 0;
    private String featureId = null;
    private String feature = null;
    private String featureAlias = null;
    private JSONObject jsonGeometry = null;
    private String geomType = null;
    public CalloutFragment(PointF pointF, JSONObject jsonObject){
        this.pointF = pointF;
        this.jsonObject = jsonObject;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_callout, container, false);

        int mWidth= this.getResources().getDisplayMetrics().widthPixels;
        int mHeight= this.getResources().getDisplayMetrics().heightPixels;

        pointF = new PointF(mWidth/2,mHeight/2);
        calloutDüzenle = rootView.findViewById(R.id.callout_düzenle);
        calloutBilgi= rootView.findViewById(R.id.callout_bilgi);
        calloutGeometriDüzenle= rootView.findViewById(R.id.callout_geometri_düzenle);
        calloutSil = rootView.findViewById(R.id.callout_sil);
        calloutNotEkle = rootView.findViewById(R.id.callout_not_ekle);
        calloutKmGit= rootView.findViewById(R.id.callout_km_git);
        calloutChildRelative = rootView.findViewById(R.id.callout_child_relative);
        calloutParentRelative = rootView.findViewById(R.id.callout_parent_relative);

        calloutDüzenle.setOnClickListener(onClickListener);
        calloutBilgi.setOnClickListener(onClickListener);
        calloutGeometriDüzenle.setOnClickListener(onClickListener);
        calloutSil.setOnClickListener(onClickListener);
        calloutNotEkle.setOnClickListener(onClickListener);
        calloutKmGit.setOnClickListener(onClickListener);

        calloutChildRelative.setX(pointF.x - Utils.convertDpToPx(getContext(), 100));

        try {
            if(jsonObject.has("id")){
                featureId = jsonObject.getString("id");
                String[] splitId = featureId.split("\\.");
                feature = splitId[0];
            }
            jsonGeometry = jsonObject.getJSONObject("geometry");
            geomType = jsonGeometry.getString("type");
            findPermission();
            setTheCalloutWithPermission(geomType);

            featureAlias = findLayerAliasOfFeature(feature);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callout_düzenle:
                        MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_UPDATE, jsonObject);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment,"KatmanFragment").addToBackStack("CalloutFragment").commit();
                    break;
                case R.id.callout_bilgi:
                    MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_KNOWLEDGE, (JSONObject) jsonObject);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("CalloutFragment").commit();
                    break;
                case R.id.callout_geometri_düzenle:
//                    removeSymbolOrLineLayer();
//
//                    if(Const.login_mode.equals(Utils.LOGIN_MODE_ONLINE)){
//                        MainActivity.fragment= new GeometryEditFragment(jsonObject);
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "GeometryEditFragment").addToBackStack("CalloutFragment").commit();
//                    }else if(Const.login_mode.equals(Utils.LOGIN_MODE_OFFLINE)){
//                        if(!selectedFeature.getStringProperty(MainActivity.PROPERTY_ALTERNATION).equals(Utils.ADD)
//                                && !selectedFeature.getStringProperty(MainActivity.PROPERTY_ALTERNATION).equals(Utils.NOT_ADD)){
//                            MainActivity.fragment= new GeometryEditFragment(jsonObject, selectedFeature);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "GeometryEditFragment").addToBackStack("CalloutFragment").commit();
//                        }else
//                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Offline eklenmiş envanterin geometrisi düzenlenemez!");
//                    }
                    break;
                case R.id.callout_sil:
                    alertDialogForDelete();
                    break;
                case R.id.callout_not_ekle:
//                    removeSymbolOrLineLayer();
//
//                    if(Const.login_mode.equals(Utils.LOGIN_MODE_ONLINE)){
//                        MainActivity.fragment= new KatmanFragment(KatmanFragment.KATMAN_TYPE_NOT_ADD, jsonObject);
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("CalloutFragment").commit();
//                    }else if(Const.login_mode.equals(Utils.LOGIN_MODE_OFFLINE)){
//                        if(!selectedFeature.getStringProperty(MainActivity.PROPERTY_ALTERNATION).equals(Utils.ADD)){
//                            MainActivity.fragment= new KatmanFragment(KatmanFragment.KATMAN_TYPE_NOT_ADD, selectedFeature, jsonObject);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("CalloutFragment").commit();
//                        }else
//                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Offline eklenmiş envantere not eklenemez!");
//
//                    }
                    break;
                case R.id.callout_km_git:
//                    cancelCalloutFragment();
//
//                    KmGitDialog kmGitDialog = new KmGitDialog(jsonObject);
//                    kmGitDialog.show(getActivity().getSupportFragmentManager(),"KmGitDialog");
                    break;
            }
        }
    };

    private void alertDialogForDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.toast_warning);
        builder.setTitle(Html.fromHtml("<font color='#45666D'>Uyarı</font>"));
        builder.setMessage(featureAlias + " silinsin mi?");
        builder.setCancelable(false);
        builder.setNegativeButton("Hayır", null);
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String jsonForDelete = writeJsonForDelete().toString();
                postTheDataToGeoserver(Const.feature_add__url, jsonForDelete );
            }
        });
        builder.show();
    }
    private void postTheDataToGeoserver(String url, String xmlData){
        ACProgressFlower acProgressFlower = Utils.progressDialogLikeIosWithoutText(getContext());
        if(acProgressFlower != null)
            acProgressFlower.show();

        Log.e(TAG, "DATA : " + xmlData);

//        HttpsTrustManager.allowAllSSL();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(acProgressFlower != null && acProgressFlower.isShowing())
                            acProgressFlower.dismiss();
                        Log.e(TAG, "postTheXmlDataToGeoserver -> onResponse: " +response);

                        if(response.contains("<wfs:totalDeleted>1</wfs:totalDeleted>")) {
                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_SUCCESS, Utils.REMOVE + " başarılı.");

                            //Map`e ekli layerları güncelliyoruz
                            MainActivity.refreshAddedLayers();
                        }else
                        Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, Utils.REMOVE + " başarısız.");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(acProgressFlower != null && acProgressFlower.isShowing())
                            acProgressFlower.dismiss();
                        Utils.showCustomToast(getActivity(),Utils.CUSTOM_TOAST_ERROR, Utils.REMOVE + " başarısız.");

                        error.printStackTrace();
                        Log.e(TAG, "onErrorResponse: " + error.getMessage() + error.toString());
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
//                return "text/plain;  charset=utf-8";
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
    private JSONObject writeJsonForDelete(){
        JSONObject deleteJsonObject = new JSONObject();

        try {
            deleteJsonObject.put("type", "Feature");
            deleteJsonObject.put("geometry", jsonObject.getJSONObject("geometry"));

////////////////////////////////////////////////////////////////////////////////////////////////////
            JSONObject propertiesJsonObject = new JSONObject();
            LayerModel layerModel = null;
            for(int i = 0; i< KatmanlarDialog.katmanList.size(); i++)
                if(KatmanlarDialog.katmanList.get(i).getLayer().getLayer().equals(feature)){
                    layerModel= KatmanlarDialog.katmanList.get(i).getLayer();
                    break;
                }
            List<LayerField> layerFields = layerModel.getFields();

            JSONObject isExistProperties = jsonObject.getJSONObject("properties");
            Iterator<String> keys = isExistProperties.keys();

            while(keys.hasNext()){
                String key = keys.next();
                String value = isExistProperties.getString(key);

                for(int i=0; i<layerFields.size(); i++){
                    if(layerFields.get(i).getField().equals(key)){
                        if(layerFields.get(i).isDomain()!= null && layerFields.get(i).isDomain()){
                            List<LayerFieldCodedValue> layerFieldCodedValue = layerFields.get(i).getCodedValues();

                            for(int j=0; j<layerFieldCodedValue.size(); j++){
                                if(String.valueOf(layerFieldCodedValue.get(j).getDesc()).equals(value)){
                                    propertiesJsonObject.put(key, layerFieldCodedValue.get(j).getId());
                                }
                            }
                        }else{
                            if(!key.equals("touch_by")){
                                if(!value.equals("null"))
                                    propertiesJsonObject.put(key, value);
                                else
                                    propertiesJsonObject.put(key, JSONObject.NULL);
                            }else
                                propertiesJsonObject.put(key, MainActivity.percon.getUserId());
                        }
                    }
                }

            }

            deleteJsonObject.put("properties", propertiesJsonObject);
//            deleteJsonObject.put("properties", jsonObject.getJSONObject("properties"));
////////////////////////////////////////////////////////////////////////////////////////////////////

            deleteJsonObject.put("id", featureId);
            deleteJsonObject.put("operation", "Delete");
            deleteJsonObject.put("typeName", feature);
            deleteJsonObject.put("workspace", "park");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return deleteJsonObject;
    }
    public static String findLayerAliasOfFeature(String featureLayerName){
        String featureLayerAlias = null;
        if(featureLayerName != null)
            for(int i = 0; i<MainActivity.percon.getLayers().size(); i++)
                if(MainActivity.percon.getLayers().get(i).getLayer().equals(featureLayerName)){
                    featureLayerAlias = MainActivity.percon.getLayers().get(i).getAlias();
                    break;
                }
        return featureLayerAlias;
    }
    private void findPermission(){
        for(int i=0; i<MainActivity.percon.getLayers().size(); i++){
            if(MainActivity.percon.getLayers().get(i).getLayer().equals(feature)){
                if(MainActivity.percon.getLayers().get(i).getPermision() != null)
                    permission = MainActivity.percon.getLayers().get(i).getPermision();
            }
        }
    }

    private void setTheCalloutWithPermission(String type){
        calloutDüzenle.setVisibility(View.VISIBLE);
        calloutBilgi.setVisibility(View.VISIBLE);
        calloutGeometriDüzenle.setVisibility(View.VISIBLE);
        calloutSil.setVisibility(View.VISIBLE);
        calloutKmGit.setVisibility(View.GONE);
        if(!feature.equals("not")) {
            calloutNotEkle.setVisibility(View.VISIBLE);
            calloutChildRelative.setY(pointF.y - Utils.convertDpToPx(getContext(), 240));
        }
    }
}
