package com.example.swornim.kawadi;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swornim.kawadi.DataStructure.Nearby;
import com.example.swornim.kawadi.DataStructure.Status;
import com.example.swornim.kawadi.Interface.IMetaData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickerNearbyActivity extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap googleMap;
    private int maxTry=0;
    private List<Nearby> nearbyList=new ArrayList<>();
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_nearby);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.picker_nearby_acitivity_mf);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getPickers();

        this.googleMap=googleMap;
            MapsInitializer.initialize(getApplicationContext());
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.map_style_night));
            googleMap.clear();

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" +marker.getTitle()));
                    startActivity(dialIntent);
                    return false;
                }
            });

    }


    private void getPickers(){


        OkHttpClient client = new OkHttpClient();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1)); // <- add this line

        RequestBody body = new FormEncodingBuilder()
                .add("getPickers", "yes")
                .build();

        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(IMetaData.serverUrl+"/kawadi_pickers.php")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {

                Log.i("mytag ", "Picker nearby response "+request.body()+" "+e.getMessage() );

            }

            @Override
            public void onResponse(Response r) throws IOException {


                String response = r.body().string();
                Log.i("mytag ", "Picker nearby response "+response );

                Type listType = new TypeToken<List<Nearby>>() {}.getType();
                nearbyList = new Gson().fromJson(response, listType);
                renderPickers();

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleMap!=null){
            getPickers();
        }
    }

    private void renderPickers(){

        if(nearbyList.size()>0){
            if(this.googleMap!=null){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for(int i=0;i<nearbyList.size();i++){
                            Double uLocationLat=Double.parseDouble(nearbyList.get(i).getuLocationLat());
                            Double uLocationLon=Double.parseDouble(nearbyList.get(i).getuLocationLon());

                            googleMap
                                    .addMarker(new MarkerOptions()
                                            .title(nearbyList.get(i).getuPNumber())
                                            .position(new LatLng(uLocationLat,uLocationLon)).icon(BitmapDescriptorFactory.fromResource(R.drawable.garbage_truck)));
                        }
                        Double uLocationLat=Double.parseDouble(nearbyList.get(0).getuLocationLat());
                        Double uLocationLon=Double.parseDouble(nearbyList.get(0).getuLocationLon());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(uLocationLat,uLocationLon), 14));


                    }
                });

            }

        }

    }

}
