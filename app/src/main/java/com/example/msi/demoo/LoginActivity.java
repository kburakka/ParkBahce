package com.example.msi.demoo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.msi.demoo.models.LayerField;
import com.example.msi.demoo.models.LayerFieldCodedValue;
import com.example.msi.demoo.models.LayerModel;
import com.example.msi.demoo.models.LayerStyle;
import com.example.msi.demoo.models.LayerStyleData;
import com.example.msi.demoo.models.Percon;
import com.example.msi.demoo.utils.Const;
import com.example.msi.demoo.utils.Utils;
import com.example.msi.demoo.utils.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cc.cloudist.acplibrary.ACProgressFlower;


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    Context context = LoginActivity.this;

    private boolean eyeVisibil = false;

    private EditText emailEditText, passwordEditText;
    private ImageView eye;
    private CheckBox beniHatirlaCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
    }

    private void initialization(){
        Animation scaleAlpha500 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_alpha_500);

        ImageView image = findViewById(R.id.login_image_view);
        image.setAnimation(scaleAlpha500);

        emailEditText = findViewById(R.id.login_eposta_edittext);
        passwordEditText = findViewById(R.id.login_sifre_edittext);
        Button loginButton = findViewById(R.id.login_giris_button);
        eye = findViewById(R.id.login_eye);

        beniHatirlaCheckBox = findViewById(R.id.login_beni_hatirla_checkbox);
        beniHatirlaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    SharedPrefs.setShared(LoginActivity.this, "login_hatirla", "true");
                else
                    SharedPrefs.setShared(LoginActivity.this, "login_hatirla", "false");
            }
        });



        String tempBeniHatirla = null;
        if(SharedPrefs.hasShared(LoginActivity.this,"login_hatirla"))
            tempBeniHatirla = SharedPrefs.getShared(LoginActivity.this, "login_hatirla");

        if(tempBeniHatirla != null && tempBeniHatirla.equals("true")){
            String tempSharedName = null;
            if(SharedPrefs.hasShared(LoginActivity.this,"login_name"))
                tempSharedName = SharedPrefs.getShared(LoginActivity.this, "login_name");

            String tempSharedPassword = null;
            if(SharedPrefs.hasShared(LoginActivity.this, "login_password"))
                tempSharedPassword = SharedPrefs.getShared(LoginActivity.this,"login_password");


            if(tempSharedName != null && tempSharedPassword != null){
                emailEditText.setText(tempSharedName);
                passwordEditText.setText(tempSharedPassword);
                beniHatirlaCheckBox.setChecked(true);
            }
        }

        loginButton.setOnClickListener(onClickListener);
        eye.setOnClickListener(onClickListener);

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(passwordEditText.getText().length() >0 && hasFocus)
                    passwordEditText.setSelection(passwordEditText.getText().length());

            }
        });
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_giris_button:
                    loginProcess();
                    break;
                case R.id.login_eye:
                    if (!eyeVisibil){
                        eyeVisibil = true;
                        eye.setImageDrawable(getResources().getDrawable(R.drawable.eye_black));

                        passwordEditText.setTransformationMethod(null);
//                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                        if(passwordEditText.getText().length() > 0)
                            passwordEditText.setSelection(passwordEditText.getText().length());
                    }else{
                        eyeVisibil = false;
                        eye.setImageDrawable(getResources().getDrawable(R.drawable.eye_grey));

                        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
//                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                        if(passwordEditText.getText().length() > 0)
                            passwordEditText.setSelection(passwordEditText.getText().length());
                    }
                    break;
            }
        }
    };

    private void loginProcess(){
        if(emailEditText.getText().length() == 0 || passwordEditText.getText().length() == 0){
            Utils.showCustomToast(LoginActivity.this, Utils.CUSTOM_TOAST_WARNING,"Lütfen tüm alanları doldurunuz!");
        }else{
            if(beniHatirlaCheckBox.isChecked()){
                SharedPrefs.setShared(LoginActivity.this, "login_name",emailEditText.getText().toString());
                SharedPrefs.setShared(LoginActivity.this, "login_password", passwordEditText.getText().toString());
            }

            ACProgressFlower acProgressFlower = Utils.progressDialogLikeIosWithoutText(LoginActivity.this);
            if(acProgressFlower != null)
                acProgressFlower.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    perconParsing(acProgressFlower);
                }
            }).start();


//            Utils.showCustomToast(LoginActivity.this,Utils.CUSTOM_TOAST_SUCCESS, "Giriş Başarılı.");
//
//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);
//            finish();
        }
    }


    private void perconParsing(ACProgressFlower acProgressFlower){
        boolean perconParsingStatus = false;
        if(Const.percon_string != null){
            try {
                System.out.println(Const.percon_string);
                JSONObject perconJson = new JSONObject(Const.percon_string);

                List<LayerModel> layers = new ArrayList<>();
                JSONArray layersJson = perconJson.getJSONArray("layers");
                for(int i=0; i<layersJson.length(); i++){
                    int layerId = layersJson.getJSONObject(i).getInt("id");
                    String layerLayer = layersJson.getJSONObject(i).getString("layer");
                    String layerAlias= layersJson.getJSONObject(i).getString("alias");
                    String layerType = layersJson.getJSONObject(i).getString("type");
                    String layerWorkspace = layersJson.getJSONObject(i).getString("workspace");

                    Integer layerPermision = null;
                    if(!layersJson.getJSONObject(i).getString( "permission").equals("null"))
                        layerPermision  = layersJson.getJSONObject(i).getInt("permission");

                    Boolean layerRequired= null;
                    if(!layersJson.getJSONObject(i).getString( "is_required").equals("null"))
                        layerRequired = layersJson.getJSONObject(i).getBoolean("is_required");

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    List<LayerField> fields = new ArrayList<>();
                    JSONArray layerFields = layersJson.getJSONObject(i).getJSONArray("fields");
                    for(int j=0; j<layerFields.length(); j++){
                        int fieldId = layerFields.getJSONObject(j).getInt("id");
                        String fieldLayer = layerFields.getJSONObject(j).getString("layer");
                        String fieldField = layerFields.getJSONObject(j).getString("field");
                        String fieldAlias = layerFields.getJSONObject(j).getString("alias");

                        Boolean fieldDomain = null;
                        if(!layerFields.getJSONObject(j).getString("domain").equals("null"))
                            fieldDomain = layerFields.getJSONObject(j).getBoolean("domain");

                        String fieldDomainTable = layerFields.getJSONObject(j).getString("domain_table");

                        Integer fieldLength = null;
                        if(!layerFields.getJSONObject(j).getString("length").equals("null"))
                            fieldLength = layerFields.getJSONObject(j).getInt("length");


                        String fieldType =  layerFields.getJSONObject(j).getString("type");


                        Integer fieldPosition = null;
                        if(!layerFields.getJSONObject(j).getString("position").equals("null"))
                            fieldPosition = layerFields.getJSONObject(j).getInt("position");


                        Boolean fieldRequired= null;
                        if(!layerFields.getJSONObject(j).getString("is_required").equals("null"))
                            fieldRequired = layerFields.getJSONObject(j).getBoolean("is_required");


                        Integer fieldPermision = null;
                        if(!layerFields.getJSONObject(j).getString( "permission").equals("null"))
                            fieldPermision  = layerFields.getJSONObject(j).getInt("permission");


                        List<LayerFieldCodedValue> codedValues = null;
                        if(fieldDomain != null && fieldDomain && layerFields.getJSONObject(j).has("codedValues")){
                            JSONArray fieldCodedValues = layerFields.getJSONObject(j).getJSONArray("codedValues");
                            codedValues = new ArrayList<>();
                            for(int k = 0; k<fieldCodedValues.length(); k++){
                                int codedValuesId = fieldCodedValues.getJSONObject(k).getInt("id");
                                String codedValuesDesc = fieldCodedValues.getJSONObject(k).getString("desc");

                                codedValues.add(new LayerFieldCodedValue(codedValuesId, codedValuesDesc));
                            }
                        }

                        fields.add(new LayerField(fieldId, fieldLayer, fieldField, fieldAlias, fieldDomain, fieldDomainTable,
                                fieldLength, fieldType, fieldPosition, fieldRequired, fieldPermision, codedValues));
                    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    LayerStyle layerStyle =  null;
                    JSONObject style = null;
                    if(!layersJson.getJSONObject(i).getString("style").equals("null") && !layersJson.getJSONObject(i).getString("style").equals("true"))
                        style = layersJson.getJSONObject(i).getJSONObject("style");

                    if(style != null){
                        String layerStyleType = style.getString("type");
                        List<LayerStyleData> layerStyleDatas = new ArrayList<>();

                        JSONArray styleDatas = null;
                        if(!style.getString("data").equals("null"))
                            styleDatas = style.getJSONArray("data");

                        if(styleDatas != null)
                            for(int j=0; j<styleDatas.length(); j++){
                                int styleId = styleDatas.getJSONObject(j).getInt("id");

                                Integer styleCodedId = null;
                                if(!styleDatas.getJSONObject(j).getString("coded_id").equals("null"))
                                    styleCodedId = styleDatas.getJSONObject(j).getInt("coded_id");

                                Integer styleLineWidth = null;
                                if(!styleDatas.getJSONObject(j).getString("line_width").equals("null"))
                                    styleLineWidth = styleDatas.getJSONObject(j).getInt("line_width");

                                String styleLineColor = styleDatas.getJSONObject(j).getString("line_color");

                                Double styleLineOpacity = null;
                                if(! styleDatas.getJSONObject(j).getString("line_opacity").equals("null"))
                                    styleLineOpacity = styleDatas.getJSONObject(j).getDouble("line_opacity");

                                String styleFillColor = styleDatas.getJSONObject(j).getString("fill_color");

                                Double styleFillOpacity = null;
                                if(! styleDatas.getJSONObject(j).getString("fill_opactiy").equals("null"))
                                    styleFillOpacity  = styleDatas.getJSONObject(j).getDouble("fill_opactiy");


                                String styleImage = styleDatas.getJSONObject(j).getString("image");


                                Integer styleWidth = null;
                                if(!styleDatas.getJSONObject(j).getString("width").equals("null"))
                                    styleWidth = styleDatas.getJSONObject(j).getInt("width");

                                Integer styleHeight = null;
                                if(!styleDatas.getJSONObject(j).getString("height").equals("null"))
                                    styleHeight = styleDatas.getJSONObject(j).getInt("height");

                                Integer styleOffsetX = null;
                                if(!styleDatas.getJSONObject(j).getString("offsetX").equals("null"))
                                    styleOffsetX = styleDatas.getJSONObject(j).getInt("offsetX");

                                Integer styleOffsetY = null;
                                if(!styleDatas.getJSONObject(j).getString("offsetY").equals("null"))
                                    styleOffsetY = styleDatas.getJSONObject(j).getInt("offsetY");

                                String styleLayerName = styleDatas.getJSONObject(j).getString("layer_name");
                                String styleFieldName = styleDatas.getJSONObject(j).getString("field_name");
                                String styleCodedName = styleDatas.getJSONObject(j).getString("coded_name");

                                String styleStyleName = styleDatas.getJSONObject(j).getString("style_name");

                                Integer styleMinZoom = null;
                                if(!styleDatas.getJSONObject(j).getString("min_zoom").equals("null"))
                                    styleMinZoom = styleDatas.getJSONObject(j).getInt("min_zoom");

                                Integer styleMaxZoom = null;
                                if(!styleDatas.getJSONObject(j).getString("max_zoom").equals("null"))
                                    styleMaxZoom = styleDatas.getJSONObject(j).getInt("max_zoom");

                                Boolean styleIsLabel = null;
                                if(!styleDatas.getJSONObject(j).getString("is_label").equals("null"))
                                    styleIsLabel = styleDatas.getJSONObject(j).getBoolean("is_label");

                                Integer styleLayerType = null;
                                if(!styleDatas.getJSONObject(j).getString("layer_type").equals("null"))
                                    styleLayerType = styleDatas.getJSONObject(j).getInt("layer_type");

                                String styleWellKnownName = styleDatas.getJSONObject(j).getString("well_known_name");

                                String stylePointColor = styleDatas.getJSONObject(j).getString("point_color");

                                Integer stylePointSize = null;
                                if(!styleDatas.getJSONObject(j).getString("point_size").equals("null"))
                                    styleLayerType = styleDatas.getJSONObject(j).getInt("point_size");

                                Integer stylePointRotation = null;
                                if(!styleDatas.getJSONObject(j).getString("point_rotation").equals("null"))
                                    styleLayerType = styleDatas.getJSONObject(j).getInt("point_rotation");


                                layerStyleDatas.add(new LayerStyleData(styleId, styleCodedId, styleLineWidth, styleLineColor, styleLineOpacity, styleFillColor, styleFillOpacity,
                                        styleImage, styleWidth, styleHeight, styleOffsetX, styleOffsetY, styleLayerName, styleFieldName, styleCodedName, styleStyleName,
                                        styleMinZoom, styleMaxZoom, styleIsLabel, styleLayerType, styleWellKnownName, stylePointColor, stylePointSize, stylePointRotation));
                            }
                        layerStyle  = new LayerStyle(layerStyleType , layerStyleDatas );
                    }

                    layers.add(new LayerModel(layerId, layerLayer, layerAlias, layerType, layerWorkspace, layerPermision, layerRequired, fields, layerStyle));
                }

                //Aşağıda katmanları ve alanları string ve pozisyonlara göre sıralama yapıyoruz.
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                List<LayerModel> tempLayerModels = new ArrayList<>();

//                Map<Integer, LayerModel> mapLayerModels = new TreeMap<>();
                for(int a=0; a<layers.size(); a++){
                    String tempAlias = layers.get(a).getLayer();
                    LayerModel tempLayerModel = layers.get(a);

                    Map<Integer, LayerField> mapLayerFields = new TreeMap<>();
                    List<LayerField> listLayerFieldForNullPosition = new ArrayList<>();
                    for(int b=0; b<tempLayerModel.getFields().size(); b++){
                        Integer tempPosition = tempLayerModel.getFields().get(b).getPosition();
                        LayerField tempLayerField = tempLayerModel.getFields().get(b);
                        if(tempPosition != null)
                            mapLayerFields.put(tempPosition, tempLayerField);
                        else
                            listLayerFieldForNullPosition.add(tempLayerField);
                    }

                    List<LayerField> tempFields = new ArrayList<>();
                    for(Integer key : mapLayerFields.keySet())
                        tempFields.add(mapLayerFields.get(key));

                    for(int c=0; c<listLayerFieldForNullPosition.size(); c++)
                        tempFields.add(listLayerFieldForNullPosition.get(c));

                    tempLayerModel.setFields(tempFields);

//                    mapLayerModels.put(tempAlias, tempLayerModel);

                    tempLayerModels.add(tempLayerModel);
                }

//                List<LayerModel> tempLayerModels = new ArrayList<>();
//                for(String key : mapLayerModels.keySet()){
//                    LayerModel templayerModel= mapLayerModels.get(key);
//                    tempLayerModels.add(templayerModel);
//                }


                MainActivity.percon = new Percon(perconJson.getBoolean("success"), emailEditText.getText().toString().trim(), perconJson.getInt("userID"), tempLayerModels);

                perconParsingStatus=true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(perconParsingStatus){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showCustomToast(LoginActivity.this,Utils.CUSTOM_TOAST_SUCCESS, "Giriş Başarılı.");

                    if (acProgressFlower != null && acProgressFlower.isShowing())
                        acProgressFlower.dismiss();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showCustomToast(LoginActivity.this,Utils.CUSTOM_TOAST_ERROR, "Parsing Error.");

                    if (acProgressFlower != null && acProgressFlower.isShowing())
                        acProgressFlower.dismiss();
                }
            });
        }
    }
}
