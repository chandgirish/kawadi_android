package com.example.swornim.kawadi;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.swornim.kawadi.DataStructure.Trucks;
import com.example.swornim.kawadi.DataStructure.Values;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private ProgressBar progressBar;
    private Button registerDriver;
    private Button driverRequest;
    private Button addwaste;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean gpsSettingsOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.splashProgressbar);
        progressBar.setVisibility(View.VISIBLE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("mytag", "All location settings are satisfied.");
                        //location is already on find the current location
                        getCurrentLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("mytag", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 0x1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("mytag", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        showGpsSettings();
                        Log.i("mytag", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });


        final DocumentReference documentReference = FirebaseFirestore.getInstance().document("pickers/trucks");

        final Trucks truckObject = new Trucks();
        truckObject.setTruckDriverName("bikram shah");
        truckObject.setTruckDriverPnumber("981231231");
        truckObject.setTruckId("asd32qwada2asdad");
        truckObject.setTimestamp(System.currentTimeMillis() + "");


        //set the custom objects inside the documents trucks
//        DocumentReference documentReference1= FirebaseFirestore.getInstance().document("pickers/trucks");
//        documentReference1.set(truckObject).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
////                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////                    @Override
////                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                        Log.i("mytag",task.getResult().getData()+"");
////                        DocumentSnapshot documentSnapshot=task.getResult();
////
////                    }
////                });
//
//            }
//        });

        //add new documents in a existing collections with auto id of document but datahasnot been added :using add()
//
//     FirebaseFirestore.getInstance().collection("pickers").add(truckObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(final DocumentReference documentReference) {
//                //after inserting new documents add the data(if docuements dont matter u can do this )
////                Log.i("mytag","getid "+documentReference.getId());//current added documents name
////                Log.i("mytag","getfirestoresettings "+documentReference.getFirestore().getFirestoreSettings()+"");//firestore db settings
////                Log.i("mytag","getfirestore app "+documentReference.getFirestore().getApp()+"");//info abt the project app ids andso on
////                Log.i("mytag","get path "+documentReference.getPath()+"");
//
//            }
//        });

//
//     FirebaseFirestore.getInstance().collection("pickers").add(truckObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(final DocumentReference documentReference) {
//
////                Log.i("mytag","nothing called"+ documentReference.get().getResult());
//                Log.i("mytag","documentreference"+ documentReference);
//
////                documentReference.get().getResult().toObject(User.class);
//                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Log.i("mytag",documentSnapshot.getData()+"");
//                        Log.i("mytag",documentSnapshot.toObject(Trucks.class).getTruckDriverName()+"");
//
//                    }
//                });
//
//            }
//        });


        /*
         BY default the ordering is ascending so setting from latest timestamp and limit 1 means latest single data

        */
        FirebaseFirestore.getInstance().collection("pickers").orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots != null) {
                    for (DocumentSnapshot each : documentSnapshots.getDocuments()) {
//                        Log.i("mytag", each.getData() + "");

                    }
                }

            }
        });

        final Waste waste = new Waste();
        waste.setSourceId("12121");
        waste.setSourceLat("27.698840");
        waste.setSourceLon("85.313860");
        waste.setSourceStatus(Values.WASTE_AVAILABLE);//available
//
//        FirebaseFirestore.getInstance().collection("wastes").add(waste).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                if(documentReference!=null){
//                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            Log.i("mytag",documentSnapshot.getData()+"");
//
//                        }
//                    });
//                }
//
//            }
//        });


//        registerDriver.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog);
//                builder.setTitle("Register new driver ?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Trucks trucks2 = new Trucks();
//                        trucks2.setTruckId("2");
//                        trucks2.setTruckPosLat(new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
//                        trucks2.setTruckPosLon( new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LON") );
//                        trucks2.setTruckDriverPnumber("9813847444");
//                        trucks2.setTruckDriverName("Shyam hari shrestha ");
//
//                        trucks2.setSelfRequest(false);
//                        trucks2.setTimestamp(System.currentTimeMillis() + "");
//
//                        //set creates custom id rather than push() id
//                        FirebaseFirestore.getInstance().
//                                collection("testPickers").
//                                document(trucks2.getTruckDriverPnumber()).
//                                set(trucks2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.i("mytag", "succesfully added new wastes");
//                            }
//                        });
//                    }
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//            }
//
//        });
//
//        driverRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog);
//                builder.setTitle("Request nearby waste ?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Trucks trucks2 = new Trucks();
//                        trucks2.setTruckPosLat(new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
//                        trucks2.setTruckPosLon( new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LON") );
//                        trucks2.setTruckDriverName("Christina Rana");
//                        trucks2.setSelfRequest(true);
//
//                        FirebaseFirestore.getInstance().document("testPickers/9813847444").set(trucks2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.i("mytag", "Request sent successfully");
//                            }
//                        });
//
//                    }
//
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//
//            }
//
//        });
//
//
//        addwaste.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog);
//                builder.setTitle("add new waste ?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Waste waste = new Waste();
//                        waste.setSourceId("1");
//                        waste.setSourceLat( new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
//                        waste.setSourceLon( new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LON"));
//
//                        //set creates custom id rather than push() id
//                        FirebaseFirestore.getInstance().
//                                collection("wastes").
//                                add(waste).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.i("mytag", "successfully added new waste");
//
//                            }
//                        });
//                    }
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//            }
//
//        });


        //listen for changes

//        FirebaseFirestore.getInstance().collection("testPickers").document("9813847444").addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//
//                if (e != null) {
//                    Log.i("mytag", "Listen failed.", e);
//                    return;
//                }
//
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    Log.i("mytag", "Current data: " + documentSnapshot.getData());
//
//                    TruckData truckData=documentSnapshot.toObject(TruckData.class);
//                    Log.i("mytag", "address "+truckData.getTruckDriverName());
//                    Log.i("mytag", "SIZE "+truckData.getTruckwaste());
//                   List<WasteData> wastes= new Gson().fromJson(truckData.getTruckwaste(),List.class);
//
//                    try {
//                        JSONArray truckwaste=new JSONArray(truckData.getTruckwaste());
//                        Log.i("mytag", "SIZE "+truckwaste.length());
//
//                        List<WasteData> recommendedWaste=new ArrayList<>();
//
//                        for(int i=0;i<truckwaste.length();i++){
//
//                            WasteData eachwaste=new WasteData();
//                            JSONObject wasteJobject= (JSONObject) truckwaste.get(i);
//                            eachwaste.setAddress(wasteJobject.optString ("address"));
//                            eachwaste.setDistance(wasteJobject.optString ("distance"));
//                            eachwaste.setDuration(wasteJobject.optString ("duration"));
//                            eachwaste.setSourceId(wasteJobject.optString ("sourceId"));
//                            eachwaste.setSourceStatus(wasteJobject.optString ("sourceStatus"));
//                            eachwaste.setSourceWeight(wasteJobject.optString ("sourceWeight"));
//                            eachwaste.setSourceLat(wasteJobject.optString ("sourceLat"));
//                            eachwaste.setSourceLon(wasteJobject.optString ("sourceLon"));
//                            JSONArray paths=new JSONArray(wasteJobject.optString ("paths"));
//                            List<Paths> eachpaths=new ArrayList<>();
//                            for(int j=0;j<paths.length();j++){
//                                JSONObject eachpathwaste= (JSONObject) paths.get(j);
//                                Paths eachpath=new Paths();
//                                eachpath.setAddress(eachpathwaste.optString ("address"));
//                                eachpath.setDistance(eachpathwaste.optString ("distance"));
//                                eachpath.setDuration(eachpathwaste.optString ("duration"));
//                                eachpath.setSourceId(eachpathwaste.optString ("sourceId"));
//                                eachpath.setSourceStatus(eachpathwaste.optString ("sourceStatus"));
//                                eachpath.setSourceWeight(eachpathwaste.optString ("sourceWeight"));
//                                eachpath.setSourceLat(eachpathwaste.optString ("sourceLat"));
//                                eachpath.setSourceLon(eachpathwaste.optString ("sourceLon"));
//                                eachpaths.add(eachpath);
//
//                            }
//                            eachwaste.setPaths(eachpaths);
//                            recommendedWaste.add(eachwaste);
//                        }
//
//
//
//                        for (WasteData each:recommendedWaste){
//                            Log.i("mytag","each id "+each.getSourceId());
//                            for(Paths mutual:recommendedWaste.get(0).getPaths()){
//                                Log.i("mytag","mutual id "+mutual.getSourceId());
//
//                            }
//                        }
//
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                    }
//
//
//                } else {
//                    Log.i("mytag", "Current data: null");
//                }
//
//            }
//        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 0x1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("mytag", "User agreed to on the location");
                        //find the current location
                        getCurrentLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("mytag", "User didnt not aggreed to on the location");
                        getCurrentLocation();
                        break;
                }
                break;


             case 100:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("mytag", "User agreed to on the location");
                        getCurrentLocation();

                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("mytag", "User didnt not aggreed to provide location permission");
                        break;
                }
                break;

        }
    }


    @Override
    public void onLocationChanged(Location location) {
           Log.i("mytag", location + "");
           progressBar.setVisibility(View.GONE);

        new CustomSharedPref(getApplicationContext()).setSharedPref("USER_CURRENT_LOCATION_LAT", String.valueOf(location.getLatitude()));
            new CustomSharedPref(getApplicationContext()).setSharedPref("USER_CURRENT_LOCATION_LON", String.valueOf(location.getLongitude()));
            progressBar.setVisibility(View.GONE);
            locationManager.removeUpdates(this);
            Toast.makeText(getApplicationContext(), "Location found successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this,Authentication.class));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i("mytag", s + " provider enabled");

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("mytag", s + " provider disabled");

    }

    private void showGpsSettings() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Location Settings");
        alertDialog.setMessage("Allow location for KAWADI");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gpsSettingsOn = true;
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showGpsSettings();

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.show();


    }


    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);//request to determine which code is what type of request

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }else{
            //permission has been granted

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("mytag","mfusedlocationclient  called success");


                            if(location==null){
                                new CustomSharedPref(getApplicationContext()).setSharedPref("USER_CURRENT_LOCATION_LAT", null);
                                new CustomSharedPref(getApplicationContext()).setSharedPref("USER_CURRENT_LOCATION_LON", null);
//                                Toast.makeText(getApplicationContext(), "Location is null", Toast.LENGTH_LONG).show();
//                                Toast.makeText(getApplicationContext(), "Please once connect to the internet for a minute", Toast.LENGTH_LONG).show();


                            }else {
                                // Got last known location. In some rare situations this can be null.
                                new CustomSharedPref(getApplicationContext()).setSharedPref("USER_CURRENT_LOCATION_LAT", location.getLatitude() + "");
                                new CustomSharedPref(getApplicationContext()).setSharedPref("USER_CURRENT_LOCATION_LON", location.getLongitude() + "");
//                                Toast.makeText(getApplicationContext(), location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG).show();
                                    // Logic to handle location object

                            }
                        }
                    });
            Log.i("mytag","Permission has been granted for both fine and coarse");
            /*Last known location might never be the current location  so requesting for a new location explicitly*/
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            try{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                        5, this);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                        5, this);
            }catch (Exception e){
                progressBar.setVisibility(View.GONE);
                Log.i("mytag","Error in location manager");

            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(gpsSettingsOn){
            getCurrentLocation();
            gpsSettingsOn=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

