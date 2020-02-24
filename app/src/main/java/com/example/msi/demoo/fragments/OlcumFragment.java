package com.example.msi.demoo.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msi.demoo.MainActivity;
import com.example.msi.demoo.R;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class OlcumFragment extends Fragment {
    private static final String TAG = "OlcumFragment";


    public String addOrRemoveType = null;
    private List<LatLng> latLngListForBackForward = new ArrayList<>();
    public  List<LatLng> latLngList = new ArrayList<>();
    public  List<Point> pointList = new ArrayList<>();
    public  List<Point> pointsForPointLayer = new ArrayList<>();

    public String selectedText = "";
    private TextView infoWindowText;
    private TextView uzunluk,alan,koordinat;
    private ImageView back, forward;
    private ImageButton iptal;


    //Marker, line, polygon eklerken kullanacağımız source ve layerlar burada
    private String olcumMarkerName = "olcum_marker";
    private String olcumSymbolSourceId = "olcum.symbol.source";
    private String olcumSymbolLayerId = "olcum.symbol.layer";
    private String olcumLineSourceId = "olcum.line.source";
    private String olcumLineLayerId = "olcum.line.layer";
    private String olcumPolygonSourceId = "olcum.polygon.source";
    private String olcumPolygonLayerId = "olcum.polygon.layer";


    public OlcumFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_olcum, container, false);

        infoWindowText = rootView.findViewById(R.id.olcum_frag_info_window);

        iptal = rootView.findViewById(R.id.olcum_frag_cancel_button);
        uzunluk = rootView.findViewById(R.id.olcum_frag_uzunluk);
        alan = rootView.findViewById(R.id.olcum_frag_alan);
        koordinat = rootView.findViewById(R.id.olcum_frag_koordinat);
        back = rootView.findViewById(R.id.olcum_frag_back);
        forward = rootView.findViewById(R.id.olcum_frag_forward);

        //default seçim için
        selectedText = Utils.KOORDINAT;
        backgroundSetting(koordinat);

        MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(false);
        setTheAlphaOfBackForwardButtons();

        back.setOnClickListener(onClickListener);
        forward.setOnClickListener(onClickListener);
        iptal.setOnClickListener(onClickListener);
        uzunluk.setOnClickListener(onClickListener);
        alan.setOnClickListener(onClickListener);
        koordinat.setOnClickListener(onClickListener);
        return rootView;
    }

    private void backgroundSetting(TextView textView){
        if (textView == uzunluk){
            uzunluk.setBackground(getResources().getDrawable(R.drawable.background_selected_textview));
            alan.setBackground(getResources().getDrawable(R.drawable.background_selectable_textview));
            koordinat.setBackground(getResources().getDrawable(R.drawable.background_selectable_textview));
        }else if(textView == alan){
            uzunluk.setBackground(getResources().getDrawable(R.drawable.background_selectable_textview));
            alan.setBackground(getResources().getDrawable(R.drawable.background_selected_textview));
            koordinat.setBackground(getResources().getDrawable(R.drawable.background_selectable_textview));
        }else if(textView == koordinat){
            uzunluk.setBackground(getResources().getDrawable(R.drawable.background_selectable_textview));
            alan.setBackground(getResources().getDrawable(R.drawable.background_selectable_textview));
            koordinat.setBackground(getResources().getDrawable(R.drawable.background_selected_textview));
        }
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.olcum_frag_back:
                    if(latLngList.size() != 0){
                        addOrRemoveType = Utils.REMOVE;
                        latLngList.remove(latLngList.size()-1);
                        drawShapes();
                    }
                    break;
                case R.id.olcum_frag_forward:
                    if(latLngListForBackForward.size() > latLngList.size()){
                        addOrRemoveType = null;
                        latLngList.add(latLngListForBackForward.get(latLngList.size()));
                        drawShapes();
                    }
                    break;
                case R.id.olcum_frag_uzunluk:
                    selectedText = Utils.UZUNLUK;
                    backgroundSetting(uzunluk);
                    removeShapes();
                    latLngListForBackForward.clear();
                    drawShapes();
                    break;
                case R.id.olcum_frag_alan:
                    selectedText = Utils.ALAN;
                    backgroundSetting(alan);
                    removeShapes();
                    latLngListForBackForward.clear();
                    drawShapes();
                    break;
                case R.id.olcum_frag_koordinat:
                    selectedText = Utils.KOORDINAT;
                    backgroundSetting(koordinat);
                    removeShapes();
                    latLngListForBackForward.clear();
                    drawShapes();
                    break;
                case R.id.olcum_frag_cancel_button:
                    cancelOLcumFragment();
                    break;
            }
        }
    };

    public void setTheAlphaOfBackForwardButtons(){
        if(latLngList.size() == 0)
            back.setImageAlpha(100);
        else
            back.setImageAlpha(255);

        if(latLngListForBackForward.size() == 0) {
            forward.setImageAlpha(100);
        }else {
            if (latLngListForBackForward.size() > latLngList.size())
                forward.setImageAlpha(255);
            else
                forward.setImageAlpha(100);
        }
    }

    public void cancelOLcumFragment(){
        MainActivity.mainActivityCurrentObject.openCloseMainButtonsVisibilitiesForFragments(true);
        removeShapes();
        latLngListForBackForward.clear();
        MainActivity.fragment = null;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void drawShapes(){
        setLatLngListForBackForwardOnChanging();

        if(latLngList.size() != 0){
            if(latLngList.size() == 1) {
                removePolygonLineAndPoint();
                convertLatLngsToPoints();
                addOrRefreshPointsForOlcum();

                infoWindowText.setVisibility(View.VISIBLE);
                infoWindowText.setText("Enlem:\n" +String.format(" %.6f", latLngList.get(0).getLatitude()) + "\n\n" +
                        "Boylam:\n" + String.format(" %.6f",latLngList.get(0).getLongitude()));

            }else if(selectedText.equals(Utils.UZUNLUK)) {
                removePolygonLineAndPoint();
                convertLatLngsToPoints();
                addOrRefreshLineForOlcum();
                addOrRefreshPointsForOlcum();

                infoWindowText.setVisibility(View.VISIBLE);
                double totalDistance = calculateDistance(latLngList);
                infoWindowText.setText( "Uzunluk:\n" + String.format(" %.3f", totalDistance) + " m");

            }else if(latLngList.size() == 2 && selectedText.equals(Utils.ALAN)) {
                removePolygonLineAndPoint();
                convertLatLngsToPoints();
                addOrRefreshLineForOlcum();
                addOrRefreshPointsForOlcum();

                infoWindowText.setVisibility(View.VISIBLE);
                infoWindowText.setText( "Alan:\n " + 0.000 + " m2");

            }else if(latLngList.size() > 2 && selectedText.equals(Utils.ALAN)) {
                removePolygonLineAndPoint();
                convertLatLngsToPoints();
                addOrRefreshPolygonForOlcum();
                addOrRefreshPointsForOlcum();

                infoWindowText.setVisibility(View.VISIBLE);
                double area = calculateArea(latLngList);
                infoWindowText.setText( "Alan:\n" + String.format(" %.3f m2", area));
            }

        }else{
            removeShapes();
        }

        setTheAlphaOfBackForwardButtons();
    }
    private void removeShapes(){
        removePolygonLineAndPoint();

        infoWindowText.setVisibility(View.GONE);
        latLngList.clear();
        pointList.clear();
        pointsForPointLayer.clear();
    }

    private void setLatLngListForBackForwardOnChanging(){
        if(addOrRemoveType != null){
            if(addOrRemoveType.equals(Utils.ADD)){
                if(latLngList.size() > latLngListForBackForward.size()){
                    for(int i=latLngListForBackForward.size(); i<latLngList.size(); i++)
                        latLngListForBackForward.add(latLngList.get(i));
                }else{
                    latLngListForBackForward.clear();
                    for(int i = 0; i < latLngList.size(); i++)
                        latLngListForBackForward.add(latLngList.get(i));
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
    private void addOrRefreshPointsForOlcum() {
        if(selectedText.equals(Utils.UZUNLUK)){
            pointsForPointLayer.clear();
            pointsForPointLayer.add(pointList.get(0));
            if(pointList.size() > 1){
                pointsForPointLayer.add(pointList.get(pointList.size()-1));
            }
        }

        if(MainActivity.map.getStyle().getLayer(olcumSymbolLayerId) == null){
            if(selectedText.equals(Utils.UZUNLUK))
                MainActivity.addGeoJsonSourceToMapFromGeometry(olcumSymbolSourceId, MultiPoint.fromLngLats(pointsForPointLayer));
            else
                MainActivity.addGeoJsonSourceToMapFromGeometry(olcumSymbolSourceId, MultiPoint.fromLngLats(pointList));

            MainActivity.addGeoJsonPointLayerToMap(olcumSymbolLayerId, olcumSymbolSourceId, olcumMarkerName, BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ellipse_green));

        }else{
            if(selectedText.equals(Utils.UZUNLUK))
                MainActivity.refreshGeojsonSource(olcumSymbolSourceId, MultiPoint.fromLngLats(pointsForPointLayer));
            else
                MainActivity.refreshGeojsonSource(olcumSymbolSourceId, MultiPoint.fromLngLats(pointList));
        }

    }
    private void addOrRefreshLineForOlcum() {
        if (MainActivity.map.getStyle().getLayer(olcumLineLayerId) == null) {
            MainActivity.addGeoJsonSourceToMapFromGeometry(olcumLineSourceId, LineString.fromLngLats(pointList));
            MainActivity.addGeoJsonLineLayerToMap(olcumLineLayerId, olcumLineSourceId, "#F63535");
        } else {
            MainActivity.refreshGeojsonSource(olcumLineSourceId, LineString.fromLngLats(pointList));
        }
    }
    private void addOrRefreshPolygonForOlcum() {
        List<List<Point>> pointPolygonList = new ArrayList<>();
        pointPolygonList.add(pointList);

        if (MainActivity.map.getStyle().getLayer(olcumPolygonLayerId) == null) {
            MainActivity.addGeoJsonSourceToMapFromGeometry(olcumPolygonSourceId, Polygon.fromLngLats(pointPolygonList));
            MainActivity.addGeoJsonPolygonLayerToMap(olcumPolygonLayerId, olcumPolygonSourceId, (float) 0.5,"#643bb2");
        } else {
            MainActivity.refreshGeojsonSource(olcumPolygonSourceId, Polygon.fromLngLats(pointPolygonList));
        }
    }
    private void removePolygonLineAndPoint(){
        MainActivity.removeLayerFromMap(olcumSymbolLayerId);
        MainActivity.removeSourceFromMap(olcumSymbolSourceId);

        MainActivity.removeLayerFromMap(olcumLineLayerId);
        MainActivity.removeSourceFromMap(olcumLineSourceId);

        MainActivity.removeLayerFromMap(olcumPolygonLayerId);
        MainActivity.removeSourceFromMap(olcumPolygonSourceId);
    }


    private double calculateDistance(List<LatLng>  latLngs){
        double distance = 0;
        for(int i = 0; i<latLngs.size()-1; i++){
            distance += latLngs.get(i).distanceTo(latLngs.get(i+1));
        }
        return distance;
    }
    private double calculateArea(List<LatLng> locations) {
        double kEarthRadius = 6378137.0;

        if (locations.size()> 2) {
            double area = 0.0;

            for(int i=0; i<locations.size(); i++){

                LatLng p1 = locations.get(i > 0 ? i - 1 : locations.size() - 1);
                LatLng p2 = locations.get(i);
                area += radians(p2.getLongitude() - p1.getLongitude()) * (2 + Math.sin(radians(p1.getLatitude())) + Math.sin(radians(p2.getLatitude())));
            }

            area = -(area * kEarthRadius * kEarthRadius / 2);

            return Math.max(area, -area);
        } else
            return 0;
    }
    private Double radians(Double degrees){
        return degrees * Math.PI / 180;
    }

}
