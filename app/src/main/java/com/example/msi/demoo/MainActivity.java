package com.example.msi.demoo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.msi.demoo.adapters.SearchAdapter;
import com.example.msi.demoo.app.AppController;
import com.example.msi.demoo.dialogfragments.GpsCihazlariDialog;
import com.example.msi.demoo.dialogfragments.KategorikAramaDialog;
import com.example.msi.demoo.dialogfragments.KatmanlarDialog;
import com.example.msi.demoo.dialogfragments.ParselBilgisiDialog;
import com.example.msi.demoo.dialogfragments.SayisallastirmaDialog;
import com.example.msi.demoo.dialogfragments.YerImiAddDialog;
import com.example.msi.demoo.dialogfragments.YerImleriDialog;
import com.example.msi.demoo.fragments.CalloutFragment;
import com.example.msi.demoo.fragments.MediaFragment;
import com.example.msi.demoo.fragments.OlcumFragment;
import com.example.msi.demoo.fragments.SayisallastirmaFragment;
import com.example.msi.demoo.interfaces.CustomItemClickListener;
import com.example.msi.demoo.models.Percon;
import com.example.msi.demoo.models.SearchMatch;
import com.example.msi.demoo.utils.Const;
import com.example.msi.demoo.utils.Utils;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.turf.TurfMeasurement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";

    public Toolbar toolbar;

    public static Context context;
    public static MainActivity mainActivityCurrentObject;
    public static Fragment fragment;

    private PermissionsManager permissionsManager;
    private MapView mapView;
    public static MapboxMap map;

    private boolean menuFabDurumu = false;
    private Animation fab_acik, fab_kapali, ileri_donme, geri_donme;
    private ImageView fabMenu, fabSayisallastirma, fabOlcum, fabAdaParsel, fabTemizle;
    public ImageView gpsConnectedImage;

    public static Percon percon = null;
    private PointF clickedPointF = null;


    private boolean adaParselFabDurumu = false;
    private List<Feature> featureListForAdaSource = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private ConstraintLayout mainButtonsDecordLayout;

    private RecyclerView searchRecyclerView;
    private List<SearchMatch> searchMatchList = new ArrayList<>();
    private SearchView searchView = null;
    private MenuItem searchMenuItem;
    private static boolean isAddedSearchedData = false;

    Handler searchHandler = new Handler();
    Runnable searchRunnable = null;

    String SEARCH_REQUEST_TAG = "search_request";

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), "qwertyukj");// Mapbox Access token
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#80C4D1"));
        setSupportActionBar(toolbar);

        fab_acik = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_acik);
        fab_kapali = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_kapali);
        ileri_donme = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ileri_donme);
        geri_donme = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.geri_donme);

        fabMenu = findViewById(R.id.fab_menu);
        fabSayisallastirma = findViewById(R.id.fab_sayısallaştırma);
        fabOlcum = findViewById(R.id.fab_olcum);
        fabAdaParsel = findViewById(R.id.fab_ada_parsel);
        fabTemizle = findViewById(R.id.fab_temizle);

        ImageView fabKonum = findViewById(R.id.fab_konum);
        ImageView fabYerimi = findViewById(R.id.fab_yerimi);

        gpsConnectedImage = findViewById(R.id.gps_connected_image);

        fabMenu.setOnClickListener(onClickListener);
        fabSayisallastirma.setOnClickListener(onClickListener);
        fabOlcum.setOnClickListener(onClickListener);
        fabAdaParsel.setOnClickListener(onClickListener);
        fabTemizle.setOnClickListener(onClickListener);
        fabKonum.setOnClickListener(onClickListener);
        fabYerimi.setOnClickListener(onClickListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button exitApp = findViewById(R.id.exit_app);
        exitApp.setOnClickListener(onClickListener);


        context = getApplicationContext();
        mainActivityCurrentObject = this;

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        searchRecyclerView = findViewById(R.id.search_listview);

        mainButtonsDecordLayout = findViewById(R.id.main_buttons_decord_layout);
        mainButtonsDecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView != null) {
                    searchView.setQuery("", false);
                    searchView.onActionViewCollapsed();
                    searchMenuItem.collapseActionView();

                    AppController.getInstance().cancelPendingRequests(SEARCH_REQUEST_TAG);

                    mainButtonsDecordLayout.setClickable(false);
                    mainButtonsDecordLayout.setFocusable(false);
                }
            }
        });
        mainButtonsDecordLayout.setClickable(false);
        mainButtonsDecordLayout.setFocusable(false);
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (map != null)
            map.removeOnMapClickListener(this);

        mapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fragment = findCurrentFragmentInContainer();
            if (fragment instanceof OlcumFragment) {
                ((OlcumFragment) fragment).cancelOLcumFragment();
            } else if (fragment instanceof MediaFragment) {
                ((MediaFragment) fragment).closeMediaFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    public JSONObject convertCrs(JSONObject jsonObject){
        JSONObject jsonObjectRtrn = jsonObject;
        try{
            JSONObject geometry = jsonObjectRtrn.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            String type = geometry.getString("type");

            if(type.equals(Utils.MULTIPOINT_TYPE)){
                Double lng = coordinates.getJSONArray(0).getDouble(0);
                Double lat = coordinates.getJSONArray(0).getDouble(1);

                List<Double> point = Utils.metersToDegrees(lat,lng);


                jsonObjectRtrn.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).put(0,point.get(1));
                jsonObjectRtrn.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).put(1,point.get(0));
            }else if (type.equals(Utils.MULTILINESTRING_TYPE)){
                for (int i = 0; i < coordinates.length(); i++){
                    for (int a = 0; a < coordinates.getJSONArray(i).length(); a++){
                        Double lng = coordinates.getJSONArray(i).getJSONArray(a).getDouble(0);
                        Double lat = coordinates.getJSONArray(i).getJSONArray(a).getDouble(1);

                        List<Double> point = Utils.metersToDegrees(lat,lng);

                        jsonObjectRtrn.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(i).getJSONArray(a).put(0,point.get(1));
                        jsonObjectRtrn.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(i).getJSONArray(a).put(1,point.get(0));
                    }
                }
            }else if (type.equals(Utils.MULTIPOLYGON_TYPE)){
                    for (int i = 0; i < coordinates.length(); i++) {
                        for (int a = 0; a < coordinates.getJSONArray(i).length(); a++) {
                            for (int e = 0; e < coordinates.getJSONArray(i).getJSONArray(a).length(); e++){

                                Double lng = coordinates.getJSONArray(i).getJSONArray(a).getJSONArray(e).getDouble(0);
                                Double lat = coordinates.getJSONArray(i).getJSONArray(a).getJSONArray(e).getDouble(1);

                                List<Double> point = Utils.metersToDegrees(lat, lng);

                                 jsonObjectRtrn.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(i).getJSONArray(a).getJSONArray(e).put(0, point.get(1));
                                 jsonObjectRtrn.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(i).getJSONArray(a).getJSONArray(e).put(1, point.get(0));
                        }
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectRtrn;
    }

    public void getFeatureWithId(String id){
        if (id.contains(".")){

            ///4326
            String type = id.split("\\.")[0];
//            String url = "http://78.46.197.92:6080/geoserver/park/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=park:"+type+"&maxFeatures=50&outputFormat=application/json" +
//                    "&featureid=" + id;
//
//            ACProgressFlower progressFlower = Utils.progressDialogLikeIosWithoutText(MainActivity.this);
//            if (progressFlower != null)
//                progressFlower.show();
//
////        HttpsTrustManager.allowAllSSL();
//            StringRequest getRequest = new StringRequest(Request.Method.GET,
//                    url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.e(TAG, "onResponse: getClickedFeatureInfossOfLayers : " + response);
//
//                            if (progressFlower != null && progressFlower.isShowing())
//                                progressFlower.dismiss();
//
//                            try {
//                                JSONObject obj = new JSONObject(response);
//                                if (obj.has("features")) {
//                                JSONArray jsonArray = obj.getJSONArray("features");
//                                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                JSONObject geometry = jsonObject.getJSONObject("geometry");
//                                String type = geometry.getString("type");
//
//                                MainActivity.addGeoJsonSourceToMapFromJson("clicked", String.valueOf(jsonArray.get(0)));
//                                if(type.equals(Utils.MULTIPOINT_TYPE)){
////                                    addGeoJsonPointLayerToMap("clicked","clicked","click_marker", BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_marker));
//                                }else if(type.equals(Utils.MULTILINESTRING_TYPE)){
//
//                                }else if(type.equals(Utils.MULTIPOLYGON_TYPE)){
//                                    MainActivity.addGeoJsonPolygonLayerToMap("clicked", "clicked", (float) 0.3, "#18ffff");
//                                }
//
//                                CameraPosition position = new CameraPosition.Builder()
//                                .target(new LatLng(mapclickLoc.getLatitude(), mapclickLoc.getLongitude())).build();
//
//                                 map.setCameraPosition(position);
//
//                                }
//                                else if (obj.has("success") && !obj.getBoolean("success")){
//                                    Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_WARNING, obj.getString("message"));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (progressFlower != null && progressFlower.isShowing())
//                        progressFlower.dismiss();
//
//                    error.printStackTrace();
//                    Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
//                }
//            }) {
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> param = new HashMap<String, String>();
//                    return param;
//                }
//            };
//
//            //Add the realibility on the connection.
//            getRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            getRequest.setShouldCache(false);
//            AppController.getInstance().addToRequestQueue(getRequest);


            ///3857

            String url2 = "http://78.46.197.92:6080/geoserver/park/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=park:"+type+"&maxFeatures=50&outputFormat=application/json" +
                    "&featureid=" + id +"&SRSName=urn:x-ogc:def:crs:EPSG:3857";

            ACProgressFlower progressFlower2 = Utils.progressDialogLikeIosWithoutText(MainActivity.this);
            if (progressFlower2 != null)
                progressFlower2.show();

//        HttpsTrustManager.allowAllSSL();
            StringRequest getRequest2 = new StringRequest(Request.Method.GET,
                    url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "onResponse: getClickedFeatureInfossOfLayers : " + response);

                            if (progressFlower2 != null && progressFlower2.isShowing())
                                progressFlower2.dismiss();

                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.has("features")) {
                                    JSONArray jsonArray = obj.getJSONArray("features");
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    callCalloutFragment(jsonObject);

                                    JSONObject jsonObject1 = convertCrs(jsonObject);
                                    MainActivity.addGeoJsonSourceToMapFromJson("clicked", String.valueOf(jsonObject1));

                                    JSONObject geometry = jsonObject.getJSONObject("geometry");
                                    String type = geometry.getString("type");

                                    if(type.equals(Utils.MULTIPOINT_TYPE)){
                                        addGeoJsonPointLayerToMap("clicked","clicked","click_marker", BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_marker));
                                    }else if(type.equals(Utils.MULTILINESTRING_TYPE)){

                                    }else if(type.equals(Utils.MULTIPOLYGON_TYPE)){
                                        MainActivity.addGeoJsonPolygonLayerToMap("clicked", "clicked", (float) 0.3, "#18ffff");
                                    }

                                    CameraPosition position = new CameraPosition.Builder()
                                            .target(new LatLng(mapclickLoc.getLatitude(), mapclickLoc.getLongitude())).build();

                                    map.setCameraPosition(position);

                                }
                                else if (obj.has("success") && !obj.getBoolean("success")){
                                    Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_WARNING, obj.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressFlower2 != null && progressFlower2.isShowing())
                        progressFlower2.dismiss();

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
            getRequest2.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getRequest2.setShouldCache(false);
            AppController.getInstance().addToRequestQueue(getRequest2);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainButtonsDecordLayout.setClickable(true);
                mainButtonsDecordLayout.setFocusable(true);
            }
        });

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_katmanlar) {
            KatmanlarDialog katmanlarDialog = new KatmanlarDialog();
            katmanlarDialog.show(getSupportFragmentManager(), "KatmanlarDialog");
        } else if (id == R.id.nav_yer_imleri) {
            YerImleriDialog yerImleriDialog = new YerImleriDialog();
            yerImleriDialog.show(getSupportFragmentManager(), "YerImleriDialog");
        } else if (id == R.id.nav_gps_cihazları) {
            GpsCihazlariDialog gpsCihazlariDialog = new GpsCihazlariDialog();
            gpsCihazlariDialog.show(getSupportFragmentManager(), "GpsCihazlarıDialog");
        } else if (id == R.id.nav_kategorik_search) {
            KategorikAramaDialog kategorikAramaDialog = new KategorikAramaDialog(MainActivity.this);
            kategorikAramaDialog.show(getSupportFragmentManager(), "KategorikAramaDialog");
        } else if (id == R.id.nav_basemap) {
            switchBaseMapOnline();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.exit_app:
                    Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_SUCCESS, "Çıkış yapıldı.");
                    Intent exitIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(exitIntent);
                    finish();
                    break;

                case R.id.fab_menu:
                    if (menuFabDurumu) {
                        menuFabDurumu = false;

                        fabMenu.startAnimation(geri_donme);

                        fabSayisallastirma.startAnimation(fab_kapali);
                        fabOlcum.startAnimation(fab_kapali);
                        fabAdaParsel.startAnimation(fab_kapali);
                        fabTemizle.startAnimation(fab_kapali);

                        fabSayisallastirma.setClickable(false);
                        fabOlcum.setClickable(false);
                        fabAdaParsel.setClickable(false);
                        fabTemizle.setClickable(false);
                    } else {
                        menuFabDurumu = true;

                        fabMenu.startAnimation(ileri_donme);

                        fabSayisallastirma.startAnimation(fab_acik);
                        fabOlcum.startAnimation(fab_acik);
                        fabAdaParsel.startAnimation(fab_acik);
                        fabTemizle.startAnimation(fab_acik);

                        fabSayisallastirma.setClickable(true);
                        fabOlcum.setClickable(true);
                        fabAdaParsel.setClickable(true);
                        fabTemizle.setClickable(true);
                    }
                    break;

                case R.id.fab_olcum:
                    fragment = new OlcumFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "OlcumFragment").addToBackStack(null).commit();
                    break;
                case R.id.fab_sayısallaştırma:
                    SayisallastirmaDialog sayisallastirmaDialog = new SayisallastirmaDialog();
                    sayisallastirmaDialog.show(getSupportFragmentManager(), "SayisallastirmaDialog");
                    break;

                case R.id.fab_ada_parsel:
                    if (adaParselFabDurumu) {
                        adaParselFabDurumu = false;
                        fabAdaParsel.setImageDrawable(getResources().getDrawable(R.drawable.ada_parsel_off));
                    } else {
                        adaParselFabDurumu = true;
                        fabAdaParsel.setImageDrawable(getResources().getDrawable(R.drawable.ada_parsel_on));
                    }
                    break;

                case R.id.fab_temizle:
                    if (KategorikAramaDialog.isAddedLayers || isAddedSearchedData) {
                        featureListForAdaSource.clear();
//                        KategorikAramaDialog.removeIlToMap();
                        KategorikAramaDialog.removeIlceToMap();
                        KategorikAramaDialog.removeMahalleToMap();
                        KategorikAramaDialog.removeAdaParselToMap();
                        KategorikAramaDialog.removeParkToMap();
                        KategorikAramaDialog.removeClickToMap();
                        KategorikAramaDialog.removeOlcumToMap();
                        KategorikAramaDialog.removeSayisallastirmaToMap();
                        KategorikAramaDialog.isAddedLayers = false;
                        isAddedSearchedData = false;
                    }
                    break;

                case R.id.fab_konum:
                    enableLocationComponent(map.getStyle());
                    break;

                case R.id.fab_yerimi:
                    YerImiAddDialog yerimiAddDialog = new YerImiAddDialog();
                    yerimiAddDialog.show(getSupportFragmentManager(), "YerImiAddDialog");
                    break;
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s.toCharArray().length > 0) {
            if (Utils.internetControl(context)) {
                searchedDataGeting(s, Utils.ILCELER);
            } else
                Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_ERROR, "Offline Moddasınız!");
        } else
            searchRecyclerView.setVisibility(View.GONE);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.toCharArray().length > 2) {
            if (Utils.internetControl(context)) {
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        searchedDataGeting(s, Utils.ILCELER);
                    }
                };
                searchHandler.postDelayed(searchRunnable, 250);
            } else
                Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_ERROR, "Offline Moddasınız!");
        } else
            searchRecyclerView.setVisibility(View.GONE);

        return true;
    }

    private void searchedDataGeting(String ilceMahalleStr, String ilceMahalleType) {
        String searchUrl = "";
        if (ilceMahalleType.equals(Utils.ILCELER)) {
            searchUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_ilce&maxFeatures=50&outputFormat=application/json" +
                    "&propertyName=ad,uavtilce,uavtil&sortBy=ad&cql_filter=" + "ad%20LIKE%20%27%25" + ilceMahalleStr.substring(0, 1).toUpperCase() + ilceMahalleStr.substring(1) + "%25%27%20%20and%20uavtil=34";

        } else if (ilceMahalleType.equals(Utils.MAHALLELER)) {
            searchUrl = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_mahalle&maxFeatures=50&outputFormat=application/json" +
                    "&propertyName=ad,uavtmah,uavtilce,il&sortBy=ad&cql_filter=" + "ad%20LIKE%20%27%25" + ilceMahalleStr.substring(0, 1).toUpperCase() + ilceMahalleStr.substring(1) + "%25%27%20%20and%20il=34";
        }else if (ilceMahalleType.equals("park")) {
            searchUrl = "http://78.46.197.92:6080/geoserver/park/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=park:park&maxFeatures=50&outputFormat=application/json" +
                    "&propertyName=ad&sortBy=ad&cql_filter=" + "ad%20LIKE%20%27%25" + ilceMahalleStr.substring(0, 1).toUpperCase() + ilceMahalleStr.substring(1) + "%25%27%20";
        }

        Log.e(TAG, "searchedDataGeting: search URl : " + searchUrl);

        //        HttpsTrustManager.allowAllSSL();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, "onResponse: " + response);

                        if (ilceMahalleType.equals(Utils.ILCELER))
                            searchMatchList.clear();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("features");

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject properties = jsonArray.getJSONObject(i).getJSONObject("properties");

                                    if (ilceMahalleType.equals(Utils.ILCELER)) {
                                        String ilceId = properties.getString("uavtilce");
                                        String ilceAd = properties.getString("ad");
                                        searchMatchList.add(new SearchMatch(Utils.ILCELER, ilceId, ilceAd));
                                    } else if (ilceMahalleType.equals(Utils.MAHALLELER)) {
                                        String mahalleId = properties.getString("uavtmah");
                                        String mahalleAd = properties.getString("ad");
                                        String ilceId = properties.getString("uavtilce");
                                        searchMatchList.add(new SearchMatch(Utils.MAHALLELER, mahalleId, mahalleAd, ilceId));
                                    } else if(ilceMahalleType.equals("park")){
                                        String parkAd = properties.getString("ad");
                                        String parkId = jsonArray.getJSONObject(i).getString("id");
                                        searchMatchList.add(new SearchMatch("Park",parkId,parkAd));
                                    }
                                }
                            }

                            if (ilceMahalleType.equals(Utils.ILCELER))
                                searchedDataGeting(ilceMahalleStr, Utils.MAHALLELER);
                            else if (ilceMahalleType.equals(Utils.MAHALLELER))
                                searchedDataGeting(ilceMahalleStr, "park");
                            else if (ilceMahalleType.equals("park")){
                                searchResultShow();
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
                Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_ERROR, "Hata oluştu!");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        //Add the realibility on the connection.
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(getRequest);
    }

    private void searchResultShow() {
        if (searchMatchList.size() != 0) {
            LinearLayoutManager layoutManagerAttribute = new LinearLayoutManager(this);
            layoutManagerAttribute.setOrientation(LinearLayoutManager.VERTICAL);

            searchRecyclerView.setLayoutManager(layoutManagerAttribute);

            final SearchAdapter searchAdapter = new SearchAdapter(searchMatchList, new CustomItemClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onItemClick(View v, int position) {
                    searchResultClicked(searchMatchList.get(position));

                    searchView.setQuery("", false);
                    searchView.onActionViewCollapsed();
                    searchMenuItem.collapseActionView();

                    AppController.getInstance().cancelPendingRequests(SEARCH_REQUEST_TAG);

                    mainButtonsDecordLayout.setClickable(false);
                    mainButtonsDecordLayout.setFocusable(false);

                    searchRecyclerView.setVisibility(View.GONE);
                }
            });
            searchRecyclerView.setVisibility(View.VISIBLE);
            searchRecyclerView.setHasFixedSize(true);
            searchRecyclerView.setAdapter(searchAdapter);
            searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            searchRecyclerView.setVisibility(View.GONE);
        }
    }

    private void searchResultClicked(SearchMatch clickedSearchMatch) {
        if (clickedSearchMatch.getType().equals(Utils.ILCELER)) {
            String url =  "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_ilce&maxFeatures=50&outputFormat=application/json" +
                    "&cql_filter=uavtilce=" + clickedSearchMatch.getIlceId();
            getForSearchedResultLayer(url, Utils.ILCELER);

        } else if (clickedSearchMatch.getType().equals(Utils.MAHALLELER)) {
            String url = "http://159.69.2.10:8080/geoserver/burakegitim/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=burakegitim:tr_mahalle&maxFeatures=50&outputFormat=application/json" +
                    "&cql_filter=uavtmah=" + clickedSearchMatch.getMahId();
            getForSearchedResultLayer(url, Utils.MAHALLELER);
        } else if (clickedSearchMatch.getType().equals("Park")) {
            String url = "http://78.46.197.92:6080/geoserver/park/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=park:park&maxFeatures=50&outputFormat=application/json" +
                    "&featureid=" + clickedSearchMatch.getIlceId();
            getForSearchedResultLayer(url, "Park");
        }

    }

    //Bolge,sube,il,ilçe aramada geojson`ları bu fonksiyon ile alırız.
    private void getForSearchedResultLayer(String url, String type) {
//        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest searchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray featuresJsonArray = response.getJSONArray("features");
                            JSONObject jsonObject = featuresJsonArray.getJSONObject(0);

                            if (type.equals(Utils.ILCELER)) {
                                KategorikAramaDialog.removeIlceToMap();
                                MainActivity.addGeoJsonSourceToMapFromJson(KategorikAramaDialog.ilceSource, jsonObject.toString());
                                MainActivity.addGeoJsonPolygonLayerBelowOfSpecificLayerToMap(KategorikAramaDialog.ilceLayer, KategorikAramaDialog.ilceSource, (float) 0.3, "#d50000", KategorikAramaDialog.mahalleLayer);
                            } else if (type.equals(Utils.MAHALLELER)) {
                                KategorikAramaDialog.removeMahalleToMap();
                                MainActivity.addGeoJsonSourceToMapFromJson(KategorikAramaDialog.mahalleSource, jsonObject.toString());
                                MainActivity.addGeoJsonPolygonLayerToMap(KategorikAramaDialog.mahalleLayer, KategorikAramaDialog.mahalleSource, (float) 0.3, "#18ffff");
                            }

                            KategorikAramaDialog.zoomToIlIlceMahalle(jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "onErrorResponse: " + error.getMessage() + error.toString());
                Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_ERROR, "Hata oluştu!");
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        searchRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        searchRequest.setShouldCache(false);
        searchRequest.setTag(SEARCH_REQUEST_TAG);
        AppController.getInstance().addToRequestQueue(searchRequest);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void openCloseMainButtonsVisibilitiesForFragments(boolean openVisibilities) {
        if (openVisibilities) {
            fabMenu.setImageAlpha(255);
            fabSayisallastirma.setImageAlpha(255);
            fabOlcum.setImageAlpha(255);
            fabAdaParsel.setImageAlpha(255);
            fabTemizle.setImageAlpha(255);
        } else {
            fabMenu.setImageAlpha(0);
            fabSayisallastirma.setImageAlpha(0);
            fabOlcum.setImageAlpha(0);
            fabAdaParsel.setImageAlpha(0);
            fabTemizle.setImageAlpha(0);
        }


        fabMenu.setClickable(openVisibilities);
        if (openVisibilities) {
            if (menuFabDurumu) {
                fabSayisallastirma.setClickable(openVisibilities);
                fabOlcum.setClickable(openVisibilities);
                fabAdaParsel.setClickable(openVisibilities);
                fabTemizle.setClickable(openVisibilities);
            }
        } else {
            fabSayisallastirma.setClickable(openVisibilities);
            fabOlcum.setClickable(openVisibilities);
            fabAdaParsel.setClickable(openVisibilities);
            fabTemizle.setClickable(openVisibilities);
        }
    }

    public Fragment findCurrentFragmentInContainer() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;

//        mapboxMap.setStyle(new Style.Builder().fromUrl("http://cbsservis.kgm.gov.tr:8080/styles/klokantech-basic.json"));
        mapboxMap.setStyle(new Style.Builder().fromUrl("http://cbsservis.kgm.gov.tr:8080/styles/klokantech-basic.json"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                map.getUiSettings().setRotateGesturesEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setLogoEnabled(false);
                map.getUiSettings().setAttributionEnabled(false);

                determineWillOpenLayersAtMapOnBeginning();

                map.addOnMapClickListener(MainActivity.this);
//                map.addOnCameraIdleListener(MainActivity.this);

                enableLocationComponent(style);
            }
        });

//        mapboxMap.setStyle(new Style.Builder().fromUrl("http://192.168.20.191:8091/styles/dark-matter/style.json"), new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//                map.getUiSettings().setRotateGesturesEnabled(false);
//                map.getUiSettings().setCompassEnabled(false);
//                map.getUiSettings().setLogoEnabled(false);
//                map.getUiSettings().setAttributionEnabled(false);
//
//                determineWillOpenLayersAtMapOnBeginning();
//
//                map.addOnMapClickListener(MainActivity.this);
////                map.addOnCameraIdleListener(MainActivity.this);
//
//                enableLocationComponent(style);
//            }
//        });

        //Sadece Turkiye bolgesi için sınırları belirliyoruz
        LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
                .include(new LatLng(44.111084, 46.458317))
                .include(new LatLng(33.577561, 19.653318))
                .build();
        mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);   // Set the boundary area for the map camera
        mapboxMap.setMinZoomPreference(3.5);                                // Set the minimum zoom level of the map camera
    }

    //MainActivity ilk açıldığı zaman katmanları percondan alıyor ve katmanların açık olmasını sağlıyor
    private void determineWillOpenLayersAtMapOnBeginning() throws NullPointerException {
        KatmanlarDialog.fillList();

        if (Utils.internetControl(context)) {
            for (int i = 0; i < KatmanlarDialog.katmanList.size(); i++)
                KatmanlarDialog.katmanList.get(i).setCheckedOrUnchecked(true);

            determineForOpenLayersAtLoginModeChanging();
        }
    }


    public void switchBaseMapOnline() {
        String hybridRasterSourceUrl = "https://mt1.google.com/vt/lyrs=y@113&hl=tr&x={x}&y={y}&z={z}";

        if (map.getStyle().getLayer("layer.basemap") == null) {
            openBaseMapOnOnline(hybridRasterSourceUrl);
        } else {
            closeBaseMapOnOnline();
        }
    }

    public synchronized void openBaseMapOnOnline(String rasterSourceUrl) {
        MainActivity.addRasterSourceToMap("source.basemap", rasterSourceUrl);

        String firstLocationLayerId = null;
        for (Layer singleLayer : MainActivity.map.getStyle().getLayers()) {
            Log.e(TAG, "onMapReady: layer id = " + singleLayer.getId());
            if (singleLayer.getId().contains("location")) {
                firstLocationLayerId = singleLayer.getId();
                break;
            }
        }

        if (firstLocationLayerId != null)
            MainActivity.addRasterLayerBelowOfSpecificLayerToMap("layer.basemap", "source.basemap", firstLocationLayerId);
        else
            MainActivity.addRasterLayerToMap("layer.basemap", "source.basemap");


        MainActivity.refreshAddedLayers();
    }

    public synchronized void closeBaseMapOnOnline() {
        MainActivity.removeLayerFromMap("layer.basemap");
        MainActivity.removeSourceFromMap("source.basemap");
    }


    LatLng mapclickLoc = new LatLng(0,0);
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        clickedPointF = map.getProjection().toScreenLocation(point);
        mapclickLoc = point;
        Log.e(TAG, "onMapClick: point --> LAT: " + point.getLatitude() + "LON : " + point.getLongitude());

        fragment = findCurrentFragmentInContainer();
        if (fragment instanceof OlcumFragment) {
            OlcumFragment tempOlcumFrag = (OlcumFragment) fragment;
            if (tempOlcumFrag.latLngList.size() < 1) {
                tempOlcumFrag.addOrRemoveType = Utils.ADD;
                tempOlcumFrag.latLngList.add(point);
                tempOlcumFrag.drawShapes();
            } else {
                if (!tempOlcumFrag.selectedText.equals(Utils.KOORDINAT)) {
                    tempOlcumFrag.addOrRemoveType = Utils.ADD;
                    tempOlcumFrag.latLngList.add(point);
                    tempOlcumFrag.drawShapes();
                } else {
                    tempOlcumFrag.addOrRemoveType = Utils.ADD;
                    tempOlcumFrag.latLngList.set(0, point);
                    tempOlcumFrag.drawShapes();
                }
            }
        } else if (fragment instanceof SayisallastirmaFragment) {
            SayisallastirmaFragment tempSayisallastirmaFrag = (SayisallastirmaFragment) fragment;
            if (tempSayisallastirmaFrag.latLngList.size() < 1) {
                tempSayisallastirmaFrag.addOrRemoveType = Utils.ADD;
                tempSayisallastirmaFrag.latLngList.add(point);
                tempSayisallastirmaFrag.drawShapes();
            } else {
                if (tempSayisallastirmaFrag.geometryType.equals(Utils.MULTILINESTRING_TYPE)) {
                    if (!tempSayisallastirmaFrag.controlIsExistPointPreviously(point)) {
                        tempSayisallastirmaFrag.addOrRemoveType = Utils.ADD;
                        tempSayisallastirmaFrag.latLngList.add(point);
                        tempSayisallastirmaFrag.drawShapes();
                    }
                } else if (tempSayisallastirmaFrag.geometryType.equals(Utils.MULTIPOINT_TYPE)) {
                    tempSayisallastirmaFrag.addOrRemoveType = Utils.ADD;
                    tempSayisallastirmaFrag.latLngList.set(0, point);
                    tempSayisallastirmaFrag.drawShapes();
                } else if  (tempSayisallastirmaFrag.geometryType.equals(Utils.MULTIPOLYGON_TYPE)){
                    if (!tempSayisallastirmaFrag.controlIsExistPointPreviously(point)) {
                        tempSayisallastirmaFrag.addOrRemoveType = Utils.ADD;
                        tempSayisallastirmaFrag.latLngList.add(point);
                        tempSayisallastirmaFrag.drawShapes();
                    }
                }
            }
        } else {
            if (adaParselFabDurumu) {
                List<Feature> tempClickedFeatures = map.queryRenderedFeatures(clickedPointF, KategorikAramaDialog.adaLayer);

                if (!tempClickedFeatures.isEmpty()) {
                    Log.e(TAG, "onMapClick: " + tempClickedFeatures.get(0).toString());
                    ParselBilgisiDialog parselBilgisiDialog = new ParselBilgisiDialog(tempClickedFeatures.get(0));
                    parselBilgisiDialog.show(getSupportFragmentManager(), "ParselBilgisiDialog");
                } else
                    getAdaParselGeoJsonForMapClick(point);
            } else {
                handleClickingForCalloutFrag(clickedPointF);
//                CameraPosition position = new CameraPosition.Builder()
//                        .target(new LatLng(point.getLatitude(), point.getLongitude())) // Sets the new camera position
//                        .build(); //
//
//                map.setCameraPosition(position);
            }
        }


        return false;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void handleClickingForCalloutFrag(PointF pointF) {
        if (Utils.internetControl(MainActivity.this)) {
            clickedPointF = pointF;

            fragment = findCurrentFragmentInContainer();
            if (KatmanlarDialog.katmanList != null && KatmanlarDialog.katmanList.size() != 0) {
                boolean katmanGösterimi = false;
                for (int i = 0; i < KatmanlarDialog.katmanList.size(); i++) {
                    if (!KatmanlarDialog.katmanList.get(i).getLayer().getLayer().equals("il") && !KatmanlarDialog.katmanList.get(i).getLayer().getLayer().equals("ilce") &&
                            !KatmanlarDialog.katmanList.get(i).getLayer().getLayer().equals("mahalle") && KatmanlarDialog.katmanList.get(i).isCheckedOrUnchecked()) {
                        katmanGösterimi = true;
                        break;
                    }
                }

                if (katmanGösterimi) {
                    String url = prepareUrlForGetLayers(katmanGösterimi);
                    getClickedFeatureInfossOfLayers(url);
                }
            }
        }
    }

    private String prepareUrlForGetLayers(boolean katmanGosterimi) {
        String layersString = null;
        if (katmanGosterimi)
            layersString = Utils.findOpenLayerNamesForMapClick(KatmanlarDialog.katmanList);        //Aktif Katman isimleri alınıyor


        //Tıklanılan screen location`ında bir rectangle oluşturduk.
        RectF rectF = new RectF(clickedPointF.x - 200, clickedPointF.y - 200, clickedPointF.x + 200, clickedPointF.y + 200);

        LatLng leftBottom = map.getProjection().fromScreenLocation(new PointF(rectF.left, rectF.bottom));
        LatLng rightTop = map.getProjection().fromScreenLocation(new PointF(rectF.right, rectF.top));

        //LeftBottom için Degree - metric çevrimi yapılıyor.
        List<Double> latLngLeftBottom = Utils.degreesToMeters(leftBottom.getLatitude(), leftBottom.getLongitude());
        double leftBottomLat = latLngLeftBottom.get(0);
        double leftBottomLong = latLngLeftBottom.get(1);

        //RightTop için Degree - metric çevrimi yapılıyor.
        List<Double> latLngRightTop = Utils.degreesToMeters(rightTop.getLatitude(), rightTop.getLongitude());
        double rightTopLat = latLngRightTop.get(0);
        double rightTopLong = latLngRightTop.get(1);

        String url = Const.host_with_port + "/geoserver/park/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetFeatureInfo&FORMAT=image%2Fpng&TRANSPARENT=true" +
                "&QUERY_LAYERS=" + layersString +
                "&LAYERS=" + layersString +
                "&INFO_FORMAT=application%2Fjson" +
                "&feature_count=5&I=50&J=50" +
                "&CRS=EPSG%3A3857" +
                "&STYLES=" +
                "&WIDTH=101&HEIGHT=101" +
                "&BBOX=" + leftBottomLong + "," + leftBottomLat + "," + rightTopLong + "," + rightTopLat;

        Log.e(TAG, "prepareUrlForGetLayers:     URL : " + url);

        return url;
    }

    private void getClickedFeatureInfossOfLayers(String url) {
        ACProgressFlower progressFlower = Utils.progressDialogLikeIosWithoutText(MainActivity.this);
        if (progressFlower != null)
            progressFlower.show();

//        HttpsTrustManager.allowAllSSL();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: getClickedFeatureInfossOfLayers : " + response);

                        if (progressFlower != null && progressFlower.isShowing())
                            progressFlower.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("features")) {
                                showClickedLayersDialog(obj.getJSONArray("features"));
                            } else if (obj.has("success") && !obj.getBoolean("success"))
                                Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_WARNING, obj.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressFlower != null && progressFlower.isShowing())
                    progressFlower.dismiss();

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
        getRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(getRequest);
    }

    public void callCalloutFragment(JSONObject jsonObject){
            fragment = new CalloutFragment(clickedPointF, jsonObject);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "CalloutFragment").addToBackStack(null).commit();

    }

    private void showClickedLayersDialog(JSONArray jsonArray) {
        try {
            KategorikAramaDialog.removeClickToMap();
            if (jsonArray.length() > 1) {

                CharSequence[] charSequences = new CharSequence[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++)
                    charSequences[i] = ((JSONObject) jsonArray.get(i)).getString("id");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#45666D'>Envanter Seçiniz</font>"));
                builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /// BILGI BOLUMU
//                        try {
//                            MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_KNOWLEDGE, (JSONObject) jsonArray.get(which));
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("CalloutFragment").commit();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
                        try {

//                            callCalloutFragment((JSONObject) jsonArray.get(which));
                            ///BURAYA

                            JSONObject jsonObject = ((JSONObject) jsonArray.get(which));
                            String id = jsonObject.getString("id");

                            getFeatureWithId(id);

//                            JSONObject jsonObject = ((JSONObject) jsonArray.get(which));
//                            JSONObject geometry = jsonObject.getJSONObject("geometry");
////                            JSONArray coordinates = geometry.getJSONArray("coordinates");
//                            String type = geometry.getString("type");
////                            List<LatLng> latLngs = new ArrayList<>();
////
//                            if(type.equals(Utils.MULTIPOINT_TYPE)){
////                                List<Double> enBoy = Utils.metersToDegrees(coordinates.getJSONArray(0).getDouble(1), coordinates.getJSONArray(0).getDouble(0));
////
////                                if(coordinates.getJSONArray(0).length() == 3)
////                                    latLngs.add(new LatLng(enBoy.get(0), enBoy.get(1), coordinates.getJSONArray(0).getDouble(2)));
////                                else if(coordinates.getJSONArray(0).length() == 2)
////                                    latLngs.add(new LatLng(enBoy.get(0), enBoy.get(1), -5000));
//
//                            }else if(type.equals(Utils.MULTILINESTRING_TYPE)){
////                                for(int i=0; i<coordinates.getJSONArray(0).length(); i++){
////                                    JSONArray latlngJsonArray = coordinates.getJSONArray(0).getJSONArray(i);
////                                    List<Double> enBoy = Utils.metersToDegrees(latlngJsonArray.getDouble(1), latlngJsonArray.getDouble(0));
////
////                                    if(latlngJsonArray.length() == 3)
////                                        latLngs.add(new LatLng(enBoy.get(0), enBoy.get(1), latlngJsonArray.getDouble(2)));
////                                    else if(latlngJsonArray.length() == 2)
////                                        latLngs.add(new LatLng(enBoy.get(0), enBoy.get(1), -5000));
////                                }
//                            }else if(type.equals(Utils.MULTIPOLYGON_TYPE)){
////                                for(int a=0; a<coordinates.getJSONArray(0).length(); a++){
////                                    for(int i=0; i<coordinates.getJSONArray(a).length(); i++) {
////                                        JSONArray latlngJsonArray = coordinates.getJSONArray(a).getJSONArray(0).getJSONArray(i);
////                                        List<Double> enBoy = Utils.metersToDegrees(latlngJsonArray.getDouble(1), latlngJsonArray.getDouble(0));
////
////                                        if(latlngJsonArray.length() == 3)
////                                            latLngs.add(new LatLng(enBoy.get(0), enBoy.get(1), latlngJsonArray.getDouble(2)));
////                                        else if(latlngJsonArray.length() == 2)
////                                            latLngs.add(new LatLng(enBoy.get(0), enBoy.get(1), -5000));
////                                    }
////                                }
////
////                                JSONArray coordinatesJsonArrayy = new JSONArray();
////
////                                JSONArray coordinatesJsonArray = new JSONArray();
////
////                                JSONArray coordinateJsonArray0 = new JSONArray();
////                                for(int i = 0; i<latLngs.size(); i++){
////                                    JSONArray coordinateJsonArray0i = new JSONArray();
////                                    List<Double> xy = Utils.degreesToMeters(latLngs.get(i).getLatitude(), latLngs.get(i).getLongitude());
////                                    Double x = xy.get(0);
////                                    Double y = xy.get(1);
////                                    coordinateJsonArray0i.put(y);
////                                    coordinateJsonArray0i.put(x);
////
////                                    if (i == latLngs.size()-1){
////                                        List<Double> xy2 = Utils.degreesToMeters(latLngs.get(0).getLatitude(), latLngs.get(0).getLongitude());
////                                        Double x2 = xy2.get(0);
////                                        Double y2 = xy2.get(1);
////                                        coordinateJsonArray0i.put(y2);
////                                        coordinateJsonArray0i.put(x2);
////                                    }
////
////                                    coordinateJsonArray0.put(i, coordinateJsonArray0i);
////                                }
////
////                                coordinatesJsonArray.put(coordinateJsonArray0);
////
////                                coordinatesJsonArrayy.put(coordinatesJsonArray);
////
////                                geometry.put("coordinates", coordinatesJsonArrayy);
//
//                                MainActivity.addGeoJsonSourceToMapFromJson("clicked", String.valueOf(jsonArray.get(which)));
//                                MainActivity.addGeoJsonPolygonLayerToMap("clicked", "clicked", (float) 0.3, "#18ffff");
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });

            } else if (jsonArray.length() == 1) {
                if (jsonArray.get(0) != null) {
                    /// BILGI BOLUMU
//                    MainActivity.fragment = new KatmanFragment(KatmanFragment.KATMAN_TYPE_KNOWLEDGE, (JSONObject) jsonArray.get(0));
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.fragment, "KatmanFragment").addToBackStack("CalloutFragment").commit();
                    try {
                        JSONObject jsonObject = ((JSONObject) jsonArray.get(0));
                        String id = jsonObject.getString("id");

                        getFeatureWithId(id);



                        ///BURAYA
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("CalloutFragment");
                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }

        } catch (JSONException e) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("CalloutFragment");
            if(fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            e.printStackTrace();
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getAdaParselGeoJsonForMapClick(LatLng latLng) {
        String url = "https://cbsservis.tkgm.gov.tr/megsiswebapi.v3/api/parsel/" + latLng.getLatitude() + "/" + latLng.getLongitude();

        ACProgressFlower acProgressFlower = Utils.progressDialogLikeIosWithoutText(MainActivity.this);
        if (acProgressFlower != null)
            acProgressFlower.show();

//            HttpsTrustManager.allowAllSSL();
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (acProgressFlower != null && acProgressFlower.isShowing())
                            acProgressFlower.dismiss();

                        Log.e(TAG, "getAdaParselGeoJsonForWfs --> onResponse: " + response);

                        try {
                            KategorikAramaDialog.removeAdaParselToMap();

                            KategorikAramaDialog.isAddedLayers = true;
                            MainActivity.addGeoJsonSourceToMapFromJson(KategorikAramaDialog.adaSource, response);
                            MainActivity.addGeoJsonPolygonLayerToMap(KategorikAramaDialog.adaLayer, KategorikAramaDialog.adaSource, (float) 0.3, "#F9DB22");
                            isAddedSearchedData = true;
                            KategorikAramaDialog.zoomToIlIlceMahalle(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (acProgressFlower != null && acProgressFlower.isShowing())
                    acProgressFlower.dismiss();

                error.printStackTrace();
                Log.e(TAG, "onErrorResponse:\n" + error.getMessage());
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(request);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings({"MissingPermission"})
    public void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = map.getLocationComponent();

            //Konum görselini özelleştirme için kullanılabilir.
//            LocationComponentOptions options = LocationComponentOptions.builder(this)
////                    .foregroundDrawable(R.drawable.)
//                    .foregroundTintColor(Color.parseColor("#FFFFFF"))
////                    .backgroundDrawable(R.drawable.)
//                    .backgroundTintColor(Color.parseColor("#0090FF"))
////                    .bearingDrawable(R.drawable.konum_yon)
//                    .bearingTintColor(Color.parseColor("#0090FF"))
//                    .compassAnimationEnabled(Boolean.TRUE)
//                    .accuracyAlpha((float)0.3)
//                    .accuracyColor(Color.parseColor("#0090FF"))
//                    .accuracyAnimationEnabled(Boolean.TRUE)
//                    .build();

//            locationComponent.activateLocationComponent(this, loadedMapStyle,options);
            locationComponent.activateLocationComponent(this, loadedMapStyle);

            // Enable to make component visible"
            checkLocationRequest(locationComponent);

            if (map.getLocationComponent().isLocationComponentEnabled()) {
                locationComponent.setCameraMode(CameraMode.TRACKING);
                map.getLocationComponent().zoomWhileTracking(15);
            }
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void checkLocationRequest(LocationComponent locationComponent) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            if (((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationComponent.setLocationComponentEnabled(true);
                map.getLocationComponent().setCameraMode(CameraMode.TRACKING);
                map.getLocationComponent().zoomWhileTracking(15);
            } else {
                Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_WARNING, "Konumu aktifleştirin!");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    map.getLocationComponent().setLocationComponentEnabled(true);
                } else {
                    Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_WARNING, "Konumu aktifleştirin!");
                }
            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this,"Kullanıcı konum izni açıklaması", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_ERROR, "Konum iznini reddettiniz.");
        }
    }


    //Bağlanılan Gps cihazından aldığımız anlamlı veri ile location`ı güncelliyoruz.
    public void updateLocationWithGpsDevice(boolean updatedLocation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment = findCurrentFragmentInContainer();

                if (updatedLocation) {
                    Location location = new Location("gps_device");
                    location.setLatitude(GpsCihazlariDialog.gpsData.getLatitude());
                    location.setLongitude(GpsCihazlariDialog.gpsData.getLongitude());
                    location.setAltitude(GpsCihazlariDialog.gpsData.getAltitude());
                    map.getLocationComponent().forceLocationUpdate(location);

                    if (fragment instanceof SayisallastirmaFragment) {
                        if (((SayisallastirmaFragment) fragment).gpsKnowledgeTextView.getVisibility() == View.GONE)
                            ((SayisallastirmaFragment) fragment).gpsKnowledgeTextView.setVisibility(View.VISIBLE);

                        ((SayisallastirmaFragment) fragment).gpsKnowledgeTextView.setText(" <--GPS-->" + "\n\n" +
                                "Enlem:\n" + String.format(" %.6f", GpsCihazlariDialog.gpsData.getLatitude()) + "\n\n" +
                                "Boylam:\n" + String.format(" %.6f", GpsCihazlariDialog.gpsData.getLongitude()));
                    }
                } else {
                    if (mainActivityCurrentObject.gpsConnectedImage.getVisibility() == View.VISIBLE) {
                        mainActivityCurrentObject.gpsConnectedImage.setVisibility(View.GONE);

                        if (fragment instanceof SayisallastirmaFragment) {
                            if (((SayisallastirmaFragment) fragment).gpsKnowledgeTextView.getVisibility() == View.VISIBLE)
                                ((SayisallastirmaFragment) fragment).gpsKnowledgeTextView.setVisibility(View.GONE);
                        }

                        Utils.showCustomToast(MainActivity.this, Utils.CUSTOM_TOAST_ERROR, "Gps Bağlantısı Koptu!");
                    }
                }
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void moveCameraPosition(LatLng latLng, double zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(zoom)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static void moveCameraToBoundingBox(LatLng latLngNortheast, LatLng latLngSouthwest, int padding, int durationMs) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(latLngNortheast) // Northeast
                .include(latLngSouthwest) // Southwest
                .build();

        map.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, padding), durationMs);
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void addRasterSourceToMap(String sourceId, String url) {
        RasterSource rasterSource = new RasterSource(sourceId, new TileSet("tileset", url), 256);

        if (map.getStyle().getSource(sourceId) == null)
            map.getStyle().addSource(rasterSource);
    }

    public static void addRasterLayerToMap(String layerId, String sourceId) {
        RasterLayer rasterLayer = new RasterLayer(layerId, sourceId);
//        rasterLayer.setProperties(PropertyFactory.visibility(Property.NONE));


        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(rasterLayer);
    }

    public static void addRasterLayerBelowOfSpecificLayerToMap(String layerId, String sourceId, String specificLayer) {
        RasterLayer rasterLayer = new RasterLayer(layerId, sourceId);
//        rasterLayer.setProperties(PropertyFactory.visibility(Property.NONE));


        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayerBelow(rasterLayer, specificLayer);
    }


    public static void addGeojsonSourceToMapFromUrl(String sourceId, String url) {
        URL geoJsonUrl = null;
        try {
            geoJsonUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        GeoJsonSource geoJsonSource = new GeoJsonSource(sourceId, geoJsonUrl);
        if (map.getStyle().getSource(sourceId) == null)
            map.getStyle().addSource(geoJsonSource);
    }

    public static void addGeoJsonSourceToMapFromJson(String sourceId, String geoJson) {
        GeoJsonSource geoJsonSource;

        if (sourceId.equals(KategorikAramaDialog.adaSource)) {
            mainActivityCurrentObject.featureListForAdaSource.add(Feature.fromJson(geoJson));
            geoJsonSource = new GeoJsonSource(sourceId, FeatureCollection.fromFeatures(mainActivityCurrentObject.featureListForAdaSource));

            //Aşagıda ada/parsel`in aymbol layerı için sourcxe hazırlıyoruz.
            List<Feature> featuresForSymbolLayers = new ArrayList<>();
            for (int i = 0; i < mainActivityCurrentObject.featureListForAdaSource.size(); i++) {
                Geometry geometry = mainActivityCurrentObject.featureListForAdaSource.get(i).geometry();

                double[] bbox = TurfMeasurement.bbox(geometry);
                Point point = TurfMeasurement.midpoint(Point.fromLngLat(bbox[0], bbox[1]), Point.fromLngLat(bbox[2], bbox[3]));

                Feature tempFeature = Feature.fromGeometry(point);
                tempFeature.addStringProperty("adaNo", mainActivityCurrentObject.featureListForAdaSource.get(i).getStringProperty("adaNo"));
                tempFeature.addStringProperty("parselNo", mainActivityCurrentObject.featureListForAdaSource.get(i).getStringProperty("parselNo"));
                featuresForSymbolLayers.add(tempFeature);

                Log.e(TAG, "addGeoJsonSourceToMapFromJson:\n lat: " + point.latitude() + " --- lon: " + point.longitude());
                Log.e(TAG, "addGeoJsonSourceToMapFromJson:\n ada: " + tempFeature.getStringProperty("adaNo") + " --- parsel: " + tempFeature.getStringProperty("parselNo"));
            }
            GeoJsonSource geoJsonSourceForSymbol = new GeoJsonSource(sourceId + ".symbol", FeatureCollection.fromFeatures(featuresForSymbolLayers));
            if (map.getStyle().getSource(sourceId + ".symbol") == null)
                map.getStyle().addSource(geoJsonSourceForSymbol);

        } else {
            geoJsonSource = new GeoJsonSource(sourceId, geoJson);
        }

        Source source = map.getStyle().getSource(sourceId);
        if (source != null){
            map.getStyle().removeSource(source);
        }
        Source source2 = map.getStyle().getSource(sourceId);
        if (source2 == null){
            map.getStyle().addSource(geoJsonSource);
        }
    }

    public static void addGeoJsonSourceToMapFromGeometry(String sourceId, Geometry geometry) {
        GeoJsonSource geoJsonSource = new GeoJsonSource(sourceId, geometry);

        if (map.getStyle().getSource(sourceId) == null)
            map.getStyle().addSource(geoJsonSource);
    }

    public static void addGeoJsonSourceToMapFromFeatureColl(String sourceId, FeatureCollection features) {
        GeoJsonSource geoJsonSource = new GeoJsonSource(sourceId, features);

        if (map.getStyle().getSource(sourceId) == null)
            map.getStyle().addSource(geoJsonSource);
    }

    public static synchronized void refreshGeojsonSource(String sourceId, String geoJson) {
        GeoJsonSource geoJsonSource = (GeoJsonSource) map.getStyle().getSource(sourceId);
        if (geoJsonSource != null) {
            geoJsonSource.setGeoJson(geoJson);
        }
    }

    public static synchronized void refreshGeojsonSource(String sourceId, Geometry geometry) {
        GeoJsonSource geoJsonSource = (GeoJsonSource) map.getStyle().getSource(sourceId);
        if (geoJsonSource != null) {
            geoJsonSource.setGeoJson(geometry);
        }
    }

    public static synchronized void refreshGeojsonSource(String sourceId, FeatureCollection featureCollection) {
        GeoJsonSource geoJsonSource = (GeoJsonSource) map.getStyle().getSource(sourceId);
        if (geoJsonSource != null) {
            geoJsonSource.setGeoJson(featureCollection);
        }
    }


    public static void addMarkerIconImagesToMap(String name, Bitmap bitmap) {
        if (map.getStyle().getImage(name) == null)
            map.getStyle().addImage(name, bitmap);
    }

    //Text`i point layer ile birlikte haritaya ekleyeceğiz.
    public static void addGeoJsonTextPointLayerToMap(String layerId, String sourceId) {
        String[] fonts = new String[1];
        fonts[0] = "Open Sans Regular";

        SymbolLayer symbolLayer = new SymbolLayer(layerId, sourceId).withProperties(
                iconAllowOverlap(true),
                PropertyFactory.textField(Expression.concat(Expression.get("adaNo"), Expression.literal(" / "), Expression.get("parselNo"))),
                PropertyFactory.textFont(fonts)
        );

        symbolLayer.setMinZoom(15);
        symbolLayer.setMaxZoom(23);

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(symbolLayer);
    }

    //Marker iconu point layer ile birlikte haritaya ekleyeceğiz.Point Layerın featureları aynı icona sahip olacak.
    public static void addGeoJsonPointLayerToMap(String layerId, String sourceId, String bitmapName, Bitmap bitmap) {
        addMarkerIconImagesToMap(bitmapName, bitmap);

        SymbolLayer symbolLayer = new SymbolLayer(layerId, sourceId)
                .withProperties(
                        iconImage(bitmapName),
                        iconSize(1.0f),
                        iconAllowOverlap(true)
                );

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(symbolLayer);
    }

    //Marker iconları önceden haritaya ekleyip sonra icon`u feature`un propertisinden bulup point layer oluşturacağız.Point Layerın featureları farklı iconlara sahip olacak.
    public static void addGeoJsonPointLayerToMapDiffIcons(String layerId, String sourceId, String bitmapName) {
        SymbolLayer symbolLayer = new SymbolLayer(layerId, sourceId)
                .withProperties(
                        iconImage(bitmapName),
                        iconAllowOverlap(true),
                        iconSize(1.0f)
                );

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(symbolLayer);
    }

    //Line color`ı line layer ile birlikte haritaya ekleyeceğiz.Line Layer`ın featureları aynı color`a sahip olacak.
    public static void addGeoJsonLineLayerToMap(String layerId, String sourceId, String colorString) {
        LineLayer lineLayer = new LineLayer(layerId, sourceId);

        lineLayer.setProperties(
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(Color.parseColor(colorString))
        );

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(lineLayer);
    }

    public static LineLayer addGeoJsonDiscreteLineLayerToMap(String layerId, String sourceId, Float lineWidthValue, String color) {
        LineLayer lineLayer = new LineLayer(layerId, sourceId);

        lineLayer.setProperties(
                PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(lineWidthValue),
                PropertyFactory.lineColor(Color.parseColor(color))
        );

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(lineLayer);

        return lineLayer;
    }

    public static synchronized void addGeoJsonPolygonLayerToMap(String layerId, String sourceId, Float opacityValue, String fiilColorString) {
        FillLayer fillLayer = new FillLayer(layerId, sourceId);
        fillLayer.setProperties(
                PropertyFactory.fillColor(Color.parseColor(fiilColorString)),
                PropertyFactory.fillOpacity(opacityValue)
        );

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayer(fillLayer);

        if (layerId.equals(KategorikAramaDialog.adaLayer) || layerId.equals("clicked")) {
            addGeoJsonDiscreteLineLayerToMap(layerId + ".line", sourceId, 3.f, "#d50000");
            addGeoJsonTextPointLayerToMap(layerId + ".symbol", sourceId + ".symbol");
        }
    }


    public static void addGeoJsonPolygonLayerBelowOfSpecificLayerToMap(String layerId, String sourceId, Float opacityValue, String fiilColorString, String specificLayer) {
        FillLayer fillLayer = new FillLayer(layerId, sourceId);
        fillLayer.setProperties(
                PropertyFactory.fillColor(Color.parseColor(fiilColorString)),
                PropertyFactory.fillOpacity(opacityValue)
        );

        if (map.getStyle().getLayer(layerId) == null)
            map.getStyle().addLayerBelow(fillLayer, specificLayer);
    }


    public static void removeSourceFromMap(String sourceId) {
        if (map != null && map.getStyle() != null) {
            if (map.getStyle().getSource(sourceId) != null)
                map.getStyle().removeSource(sourceId);
        }
    }

    public static void removeLayerFromMap(String layerId) {
        if (map != null && map.getStyle() != null) {
            if (map.getStyle().getLayer(layerId) != null)
                map.getStyle().removeLayer(layerId);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Alttaki 3 fonksiyon Online modda katmanları açıp-kapatmak için kullanılıyor.
    public static void determineForOpenLayersAtLoginModeChanging() {
        String layersString = Utils.findOpenLayerNames(KatmanlarDialog.katmanList);

        if (layersString != null)
            openOnlineLayers(layersString, "layers");
    }

    public static synchronized void openOnlineLayers(String layersNames, String identifier) {
//        String rasterSourceUrl = Const.host_with_port + "/geoserver/kgm/wms?service=WMS&version=1.3.0&" +
//                "request=GetMap&transparent=true&layers="+layersNames+"&bbox={bbox-epsg-3857}&width=256&height=256" +
//                "&srs=EPSG%3A3857&format=image%2Fpng&token=" + Const.token;

        String rasterSourceUrl = Const.host_with_port + "/geoserver/park/wms?service=WMS&version=1.1.0&" +
                "request=GetMap&transparent=true&layers=" + layersNames + "&bbox={bbox-epsg-3857}&width=256&height=256" +
                "&srs=EPSG%3A3857&format=image%2Fpng";

        Log.e(TAG, "openOnlineLayers: URL : " + rasterSourceUrl);


        MainActivity.addRasterSourceToMap("katman.source." + identifier, rasterSourceUrl);
        MainActivity.addRasterLayerToMap("katman.layer." + identifier, "katman.source." + identifier);
    }

    public static synchronized void closeOnlineLayers(String identifier) {
        MainActivity.removeLayerFromMap("katman.layer." + identifier);
        MainActivity.removeSourceFromMap("katman.source." + identifier);
    }

    public static void refreshAddedLayers() {

        closeOnlineLayers("layers");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                determineForOpenLayersAtLoginModeChanging();
            }
        }, 100);
    }
}
