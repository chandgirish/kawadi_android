package com.example.swornim.kawadi.Fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.swornim.kawadi.CustomSharedPref;
import com.example.swornim.kawadi.DataStructure.Paths;
import com.example.swornim.kawadi.DataStructure.TruckData;
import com.example.swornim.kawadi.DataStructure.Trucks;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.DataStructure.WasteData;
import com.example.swornim.kawadi.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swornim on 6/18/2017.
 */

public class Newsfeed extends Fragment {

    public List<WasteData> recommendedWastes = new ArrayList<>();
    private ArrayAdapter<WasteData> newsAdapter;
    private ListView listView;
    private Dialog loadingDialog;
    private MapFragment mapView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.newsfeed_fragment, container, false);
        listView = mView.findViewById(R.id.newsfeed_fragment_lsitview);
        newsAdapter = new NewsfeedAdapter(getContext(), recommendedWastes);
        listView.setAdapter(newsAdapter);

        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Getting waste nearby", Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog);
                builder.setTitle("Request waste ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestNearByWaste();
                    }
                });

                builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        return mView;

    }


    private class NewsfeedAdapter extends ArrayAdapter<WasteData> implements View.OnClickListener {

        public NewsfeedAdapter(@NonNull Context context, List<WasteData> recommendedWastes) {
            super(context, R.layout.newsfeed_fragment_custom, recommendedWastes);
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View mView = convertView;
            if (convertView == null) {
                //initalize the mview
                mView = LayoutInflater.from(getContext()).inflate(R.layout.newsfeed_fragment_custom, parent, false);
            }

            TextView address = mView.findViewById(R.id.newsfeed_fragment_address);
            TextView source = mView.findViewById(R.id.newsfeed_fragment_sourceType);
            TextView amount = mView.findViewById(R.id.newsfeed_fragment_custom_amount);
            TextView weight = mView.findViewById(R.id.newsfeed_fragment_custom_weight);
            TextView distance = mView.findViewById(R.id.newsfeed_fragment_custom_distance);

            //it works but lags so do it in recyler view
            //MapView mapView=mView.findViewById(R.id.newsfeed_fragment_custom_map);
//            mapView.onCreate(null);
//            mapView.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                    MapsInitializer.initialize(getContext());
//                    googleMap.addMarker(new MarkerOptions().position(new LatLng(-34,151)));
//                }
//            });

            //register all the listener
            address.setText(recommendedWastes.get(position).getAddress());
            source.setText(recommendedWastes.get(position).getSourceStatus());
            amount.setText("Rs."+recommendedWastes.get(position).getSourceStatus());
            distance.setText(recommendedWastes.get(position).getDistance()+"m");


            return mView;
        }

        @Override
        public void onClick(View v) {

//
            }

        }




    private void requestNearByWaste() {

        Trucks trucks2 = new Trucks();
        String latitude = new CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LAT");
        String longitude = new CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LON");

        trucks2.setTruckPosLat(latitude);
        trucks2.setTruckPosLon(longitude);
        trucks2.setTruckDriverPnumber("9813847444");
        trucks2.setTruckDriverName("Swornim shah");
        trucks2.setSelfRequest(true);

        //request the nearby wastes
        FirebaseFirestore.getInstance().document("testPickers/9813847444").set(trucks2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("mytag", "Request sent successfully");
                renderData();
            }
        });

    }


    private void checkOnlineOffline() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                "http://digitate-convention.000webhostapp.com/beatme/SavedEvents.php",
                "https://us-central1-shivaya-6c9f9.cloudfunctions.net/newDriverRequest",

                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("online")) {
//                            requestNearByWaste();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "You are in offline mode", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> map = new HashMap<>();
                //send the json format
                Gson gson = new Gson();
                return map;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                return header;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void renderData() {
        FirebaseFirestore.getInstance().collection("testPickers").document("9813847444").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(recommendedWastes.size()>0){recommendedWastes.clear();}

                if (e != null) {
                    Log.i("mytag", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.i("mytag", "Current data: " + documentSnapshot.getData());

                    TruckData truckData = documentSnapshot.toObject(TruckData.class);
                    Log.i("mytag", "address " + truckData.getTruckDriverName());
                    Log.i("mytag", "SIZE " + truckData.getTruckwaste());
                    List<WasteData> wastes = new Gson().fromJson(truckData.getTruckwaste(), List.class);

                    try {
                        if (truckData != null && truckData.getTruckwaste() != null) {

                            JSONArray truckwaste = new JSONArray(truckData.getTruckwaste());
                            Log.i("mytag", "SIZE " + truckwaste.length());
                            for (int i = 0; i < truckwaste.length(); i++) {

                                WasteData eachwaste = new WasteData();
                                JSONObject wasteJobject = (JSONObject) truckwaste.get(i);
                                eachwaste.setAddress(wasteJobject.optString("address"));
                                eachwaste.setDistance(wasteJobject.optString("distance"));
                                eachwaste.setDuration(wasteJobject.optString("duration"));
                                eachwaste.setSourceId(wasteJobject.optString("sourceId"));
                                eachwaste.setSourceStatus(wasteJobject.optString("sourceStatus"));
                                eachwaste.setSourceWeight(wasteJobject.optString("sourceWeight"));
                                eachwaste.setSourceLat(wasteJobject.optString("sourceLat"));
                                eachwaste.setSourceLon(wasteJobject.optString("sourceLon"));


                                if(wasteJobject.getString("paths")!=null && !wasteJobject.getString("paths").equals("null")) {
                                    JSONArray paths = new JSONArray(wasteJobject.getString("paths"));
                                    List<Paths> eachpaths = new ArrayList<>();
                                    for (int j = 0; j < paths.length(); j++) {
                                        JSONObject eachpathwaste = (JSONObject) paths.get(j);
                                        Paths eachpath = new Paths();
                                        eachpath.setAddress(eachpathwaste.optString("address"));
                                        eachpath.setDistance(eachpathwaste.optString("distance"));
                                        eachpath.setDuration(eachpathwaste.optString("duration"));
                                        eachpath.setSourceId(eachpathwaste.optString("sourceId"));
                                        eachpath.setSourceStatus(eachpathwaste.optString("sourceStatus"));
                                        eachpath.setSourceWeight(eachpathwaste.optString("sourceWeight"));
                                        eachpath.setSourceLat(eachpathwaste.optString("sourceLat"));
                                        eachpath.setSourceLon(eachpathwaste.optString("sourceLon"));
                                        eachpaths.add(eachpath);

                                    }
                                    eachwaste.setPaths(eachpaths);

                                }
                                recommendedWastes.add(eachwaste);
                            }
                            //update the listview
                            newsAdapter = new NewsfeedAdapter(getContext(), recommendedWastes);
                            listView.setAdapter(newsAdapter);


                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                } else {
                    Log.i("mytag", "Current data: null");
                }

            }
        });
    }


    @Override
    public void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
        super.onResume();
    }


    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        if (mapView != null) {
            mapView.onLowMemory();
        }
        super.onLowMemory();
    }

    @Override
    public void onStart() {
        if (mapView != null) {
            mapView.onStart();
        }
        super.onStart();
        renderData();
        //dont request the firebase ,it will be requested by alert dialog conformation
    }

    @Override
    public void onStop() {
        if (mapView != null) {
            mapView.onStop();
        }
        super.onStop();
    }

}



