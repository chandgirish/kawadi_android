package com.example.swornim.kawadi.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swornim.kawadi.CustomSharedPref;
import com.example.swornim.kawadi.DataStructure.Trucks;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.DataStructure.WasteData;
import com.example.swornim.kawadi.MapsActivity;
import com.example.swornim.kawadi.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class CircularPatternFragment extends Fragment {
    private GoogleMap map;
    private ImageView nearby;
    private ImageView circular;
    private List<Waste> wasteList=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.circularpatterfragment,container,false);
        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.circularMapFragment));
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map=googleMap;
                map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(),R.raw.map_style_standard));
                //trucks position of the driver
                Double lat=Double.parseDouble(new  CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
                Double lon=Double.parseDouble(new  CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LON"));
                LatLng currentLatLng=new LatLng(lat,lon);


                map.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.garbagetruck)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15));


//                FirebaseFirestore.getInstance().document("pickers/NWZR9fCKP4h1RJqocBCD").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Log.i("mytag","data "+documentSnapshot);
//                        Trucks trucks=documentSnapshot.toObject(Trucks.class);
//                        Log.i("mytag","truck driver name  "+trucks.getTruckDriverName());
//
//                        try {
//                            JSONArray jsonArray=new JSONArray(trucks.getTruckwastes());
//                            Gson gson=new Gson();
//
//                            for(int i=0;i<jsonArray.length();i++){
//                               WasteData each= gson.fromJson(jsonArray.getJSONObject(i).toString(),WasteData.class);
//                               Log.i("mytag","each sourcetype "+each.getSourceType());
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });


            }
        });

        FirebaseFirestore.getInstance().collection("wastes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for(DocumentSnapshot each:documentSnapshots){
                    wasteList.add(each.toObject(Waste.class));
                }

            }
        });

        circular=mView.findViewById(R.id.circularOptions);
        nearby=mView.findViewById(R.id.nearbyOptions);

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*send request to the server for nearby wastes*/

                Trucks truckDriver=new Trucks();
                truckDriver.setTruckDriverName("Swornim Bikram Shah");
                truckDriver.setTruckDriverPnumber("9821233455");
                truckDriver.setTimestamp(System.currentTimeMillis()+"");
                truckDriver.setTruckPosLat("27.658844");
                truckDriver.setTruckPosLon("85.324863");
                truckDriver.setTruckId("200");
                truckDriver.setSelfRequest(false);
                FirebaseFirestore.getInstance().collection("pickers").add(truckDriver).
                        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i("mytag","requested  successfully "+documentReference.getId());


                                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                        if (e != null) {
                                            Log.i("mytag", "Listen failed.", e);
                                            return;
                                        }

                                        String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                                                ? "Local" : "Server";

                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            Log.i("mytag","data "+documentSnapshot);
                                            Trucks trucks=documentSnapshot.toObject(Trucks.class);
                                            Log.i("mytag","truck driver name  "+trucks.getTruckDriverName());

                                            try {
                                                JSONArray jsonArray=new JSONArray(trucks.getTruckwastes());
                                                Gson gson=new Gson();

                                                for(int i=0;i<jsonArray.length();i++){
                                                    WasteData each= gson.fromJson(jsonArray.getJSONObject(i).toString(),WasteData.class);
                                                    Log.i("mytag","each sourceid "+each.getSourceId());

                                                }

                                            } catch (JSONException jsonException) {
                                                jsonException.printStackTrace();
                                            }

                                        } else {
                                            Log.i("mytag", source + " truck waste is : null");
                                        }

                                    }
                                });

                            }
                        });
            }
        });

        circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wasteList.size()>0){

                    if(map!=null){
                        map.clear();//clear previous even nothing is present

                        for(int i=0;i<wasteList.size();i++){
                            LatLng latLng=new LatLng(
                                    Double.parseDouble(wasteList.get(i).getSourceLat()),
                                    Double.parseDouble(wasteList.get(i).getSourceLon())
                            );
//                            map.addMarker(new MarkerOptions().position(latLng).title(wasteList.get(i).getSourceType()));
                            map.addCircle(new CircleOptions()
                                    .center(new LatLng(Double.parseDouble(wasteList.get(0).getSourceLat()), Double.parseDouble(wasteList.get(0).getSourceLon())))
                                    .radius(25)
                                    .strokeWidth(1)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.YELLOW));


                        }


                    }

                }else {
                    Toast.makeText(getContext(),"please wait data is coming",Toast.LENGTH_LONG).show();
                }

            }
        });





        return mView;
    }




}
