package com.example.msi.demoo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.models.LayerModel;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.example.msi.demoo.MainActivity.context;

import static com.example.msi.demoo.MainActivity.map;

@SuppressLint("ValidFragment")
public class SayisallastirmaFragment extends Fragment {
    private static final String TAG = "SayisallastirmaFragment";

    private static String tempTextOfGpsNoktasiAt = null;

    public String geometryType = null;
    public String addOrRemoveType = null;

    public List<LatLng> latLngList = new ArrayList<>();
    private List<Point> pointList = new ArrayList<>();
    private List<LatLng> latLngListForForward = new ArrayList<>();

    private LayerModel layerModel = null;

    private Button iptal, tamamla;
    private ImageView back, forward;
    private TextView katmanNameTextView;
    public TextView gpsNoktasıAt;
    public TextView gpsKnowledgeTextView;
    private RelativeLayout relativeLayout;

    //Marker ve line eklerken kullanacağımız source ve layerlar burada
    private String sayisallastirmaMarkerName = "sayisallastirma_marker";
    private String sayisallastirmaSymbolSourceId = "sayisallastirma.symbol.source";
    private String sayisallastirmaSymbolLayerId = "sayisallastirma.symbol.layer";
    private String sayisallastirmaLineSourceId = "sayisallastirma.line.source";
    private String sayisallastirmaLineLayerId = "sayisallastirma.line.layer";
    private String sayisallastirmaPolygonSourceId = "sayisallastirma.polygon.source";
    private String sayisallastirmaPolygonLayerId = "sayisallastirma.polygon.layer";

    public SayisallastirmaFragment(LayerModel layerModel) {
        this.layerModel = layerModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sayisallastirma, container, false);

        katmanNameTextView = rootView.findViewById(R.id.sayisallastirma_frag_name_textview);
        gpsNoktasıAt = rootView.findViewById(R.id.sayisallastirma_frag_gps_noktasi);
        gpsKnowledgeTextView = rootView.findViewById(R.id.sayisallastirma_frag_gps_info_window);
        back = rootView.findViewById(R.id.sayisallastirma_frag_back);
        forward = rootView.findViewById(R.id.sayisallastirma_frag_forward);
        iptal = rootView.findViewById(R.id.sayisallastirma_frag_cancel_button);
        tamamla = rootView.findViewById(R.id.sayisallastirma_frag_tamamla_button);

        //Gps noktası at ve 3 point için visibility ayarlarken kullanacaz
        relativeLayout = rootView.findViewById(R.id.sayisallastirma_frag_gps_3d_layout);

        if (tempTextOfGpsNoktasiAt != null)
            gpsNoktasıAt.setText(tempTextOfGpsNoktasiAt);

        MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(false);
        setTheAlphaOfBackForwardButtons();

        back.setOnClickListener(onClickListener);
        forward.setOnClickListener(onClickListener);
        gpsNoktasıAt.setOnClickListener(onClickListener);
        tamamla.setOnClickListener(onClickListener);
        iptal.setOnClickListener(onClickListener);

        katmanNameTextView.setText(layerModel.getAlias());
        geometryType = layerModel.getType();

        return rootView;
    }

    @Override
    public void onPause() {
        tempTextOfGpsNoktasiAt = gpsNoktasıAt.getText().toString();
        super.onPause();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sayisallastirma_frag_back:
                    if (latLngList.size() != 0) {
                        addOrRemoveType = Utils.REMOVE;
                        latLngList.remove(latLngList.size() - 1);
                        drawShapes();
                    }
                    break;
                case R.id.sayisallastirma_frag_forward:
                    if (latLngListForForward.size() > latLngList.size()) {
                        addOrRemoveType = null;
                        latLngList.add(latLngListForForward.get(latLngList.size()));
                        drawShapes();
                    }
                    break;
                case R.id.sayisallastirma_frag_gps_noktasi:
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Konum için izin vermelisiniz.");
                        return;
                    }

                    if (PermissionsManager.areLocationPermissionsGranted(context) && map.getLocationComponent().getLastKnownLocation() != null){
                        double lastLatitude;
                        double lastLongitude;
                        double lastAltitude;
                        double bearing = 0;

                        Location location = map.getLocationComponent().getLastKnownLocation();
                        Float heading = map.getLocationComponent().getCompassEngine().getLastHeading();

                        lastLatitude = location.getLatitude();
                        lastLongitude = location.getLongitude();
                        if(location.getAltitude() != 0)
                            lastAltitude = location.getAltitude();
                        else
                            lastAltitude = -5000;
                        bearing = (double) heading;


                        Log.e(TAG, "onClick: bearing :  " + bearing);

                        if(geometryType.equals(Utils.MULTIPOINT_TYPE)){
                            if(latLngList.size() == 1)
                                latLngList.clear();


                                addOrRemoveType = Utils.ADD;
                                latLngList.add(new LatLng(lastLatitude, lastLongitude, lastAltitude));
                                drawShapes();
                        }else if (geometryType.equals(Utils.MULTILINESTRING_TYPE)){
                            if(!controlIsExistPointPreviously(new LatLng(lastLatitude, lastLongitude, lastAltitude))) {
                                addOrRemoveType = Utils.ADD;
                                latLngList.add(new LatLng(lastLatitude, lastLongitude, lastAltitude));
                                drawShapes();
                            }
                        }else if (geometryType.equals(Utils.MULTIPOLYGON_TYPE)){
                            if(!controlIsExistPointPreviously(new LatLng(lastLatitude, lastLongitude, lastAltitude))) {
                                addOrRemoveType = Utils.ADD;
                                latLngList.add(new LatLng(lastLatitude, lastLongitude, lastAltitude));
                                drawShapes();
                            }
                        }
                    }
                    break;
                case R.id.sayisallastirma_frag_cancel_button:
                    cancelSayisallastirmaFragment();
                    break;
                case R.id.sayisallastirma_frag_tamamla_button:
                    if(geometryType.equals(Utils.MULTIPOINT_TYPE)){
                        if(latLngList.size() > 0){
                            MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_ADD, layerModel, Utils.MULTIPOINT_TYPE, latLngList);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("SayisallastirmaFragment").commit();
                            removeShapes();
                            MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(true);
                        }else
                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Henüz bir geometry noktası girmediniz!");
                    }else if (geometryType.equals(Utils.MULTILINESTRING_TYPE)) {
                        if (latLngList.size() > 1) {
                            MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_ADD, layerModel, Utils.MULTILINESTRING_TYPE, latLngList);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("SayisallastirmaFragment").commit();
                            removeShapes();
                            MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(true);
                        } else
                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Henüz bir line noktaları girmediniz!");
                    }else if (geometryType.equals(Utils.MULTIPOLYGON_TYPE)){
                        if (latLngList.size() > 1) {
                            MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_ADD, layerModel, Utils.MULTIPOLYGON_TYPE, latLngList);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("SayisallastirmaFragment").commit();
                            removeShapes();
                            MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(true);
                        } else
                            Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Henüz bir polygon noktaları girmediniz!");
                    }
                    break;

            }
        }
    };

    public void setTheAlphaOfBackForwardButtons(){
        if(latLngList.size() == 0)
            back.setImageAlpha(100);
        else
            back.setImageAlpha(255);


        if(latLngListForForward.size() == 0) {
            forward.setImageAlpha(100);
        }else {
            if (latLngListForForward.size() > latLngList.size())
                forward.setImageAlpha(255);
            else
                forward.setImageAlpha(100);
        }
    }

    public void cancelSayisallastirmaFragment(){
        MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(true);
        removeShapes();
        latLngListForForward.clear();
        getActivity().getSupportFragmentManager().popBackStack();
    }


    public boolean controlIsExistPointPreviously(LatLng newLatLng){
        boolean control = false;
        for(int i=0; i<latLngList.size(); i++){
            if(latLngList.get(i).equals(newLatLng)){
                control = true;
//                Utils.showCustomToast(getActivity(), Utils.CUSTOM_TOAST_WARNING, "Önceden point eklenmiş!");
                break;
            }
        }

        return control;
    }

    public void drawShapes(){
        setLatLngListForForwardOnChanging();

        if(latLngList.size() != 0){
            convertLatLngsToPoints();
            addOrRefreshLineAndPoints();
        }else
            removeShapes();

        setTheAlphaOfBackForwardButtons();
    }
    private void removeShapes(){
        removeLineAndPoints();
        pointList.clear();

        latLngList.clear();
    }

    private void setLatLngListForForwardOnChanging(){
        if(addOrRemoveType != null){
            if(addOrRemoveType.equals(Utils.ADD)){
                if(latLngList.size() > latLngListForForward.size()){
                    for(int i=latLngListForForward.size(); i<latLngList.size(); i++)
                        latLngListForForward.add(latLngList.get(i));
                }else{
                    latLngListForForward.clear();
                    for(int i = 0; i < latLngList.size(); i++)
                        latLngListForForward.add(latLngList.get(i));
                }
            }
        }
    }

    private void convertLatLngsToPoints(){
        pointList.clear();
        for(int i=0; i<latLngList.size(); i++){
            pointList.add(Point.fromLngLat(latLngList.get(i).getLongitude(), latLngList.get(i).getLatitude()));
        }
    }
    private void addOrRefreshLineAndPoints() {
        if(geometryType.equals(Utils.MULTILINESTRING_TYPE)) {
            if (MainActivity.map.getStyle().getLayer(sayisallastirmaLineLayerId) == null) {
                MainActivity.addGeoJsonSourceToMapFromGeometry(sayisallastirmaLineSourceId, LineString.fromLngLats(pointList));
                MainActivity.addGeoJsonLineLayerToMap(sayisallastirmaLineLayerId, sayisallastirmaLineSourceId, "#F63535");
            } else {
                MainActivity.refreshGeojsonSource(sayisallastirmaLineSourceId, LineString.fromLngLats(pointList));
            }
        }else if(geometryType.equals(Utils.MULTIPOLYGON_TYPE)){
            List polygon = new ArrayList();
            polygon.add(pointList);
            if (MainActivity.map.getStyle().getLayer(sayisallastirmaPolygonLayerId) == null) {
                MainActivity.addGeoJsonSourceToMapFromGeometry(sayisallastirmaPolygonSourceId, Polygon.fromLngLats(polygon));
                MainActivity.addGeoJsonLineLayerToMap(sayisallastirmaPolygonLayerId, sayisallastirmaPolygonSourceId, "#F63535");
            } else {
                MainActivity.refreshGeojsonSource(sayisallastirmaPolygonSourceId, Polygon.fromLngLats(polygon));
            }
        }

        if(MainActivity.map.getStyle().getLayer(sayisallastirmaSymbolLayerId) == null){
            MainActivity.addGeoJsonSourceToMapFromGeometry(sayisallastirmaSymbolSourceId, MultiPoint.fromLngLats(pointList));
            MainActivity.addGeoJsonPointLayerToMap(sayisallastirmaSymbolLayerId, sayisallastirmaSymbolSourceId, sayisallastirmaMarkerName, BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ellipse_green));
        }else{
            MainActivity.refreshGeojsonSource(sayisallastirmaSymbolSourceId, MultiPoint.fromLngLats(pointList));
        }
    }
    private void removeLineAndPoints(){
        MainActivity.removeLayerFromMap(sayisallastirmaSymbolLayerId);
        MainActivity.removeSourceFromMap(sayisallastirmaSymbolSourceId);

        MainActivity.removeLayerFromMap(sayisallastirmaLineLayerId);
        MainActivity.removeSourceFromMap(sayisallastirmaLineSourceId);
    }

}
