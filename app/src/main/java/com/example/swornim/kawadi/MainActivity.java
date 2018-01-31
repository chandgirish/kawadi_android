package com.example.swornim.kawadi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.swornim.kawadi.DataStructure.Firestore;
import com.example.swornim.kawadi.DataStructure.NearbyRequest;
import com.example.swornim.kawadi.DataStructure.Trucks;
import com.example.swornim.kawadi.DataStructure.Values;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.DOMConfiguration;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private Location location;
    private ProgressBar progressBar;
    private Button registerDriver;
    private Button driverRequest;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar) findViewById(R.id.progressbarID);
        registerDriver=(Button) findViewById(R.id.registerDriver);
        driverRequest=(Button) findViewById(R.id.driverRequest);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(LocationStatus,new IntentFilter("locationService"));



         locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

 
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //mintime is on how much timeinterval the app should get the new data
            //mindis: is when actually the app should be notified that every 10 meter distance change
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null) {
                Log.i("mytag", "GPS"+location.getLatitude() + " lat");
                Log.i("mytag", "GPS"+location.getLongitude() + " lon");
            }


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            //mintime is on how much timeinterval the app should get the new data
            //mindis: is when actually the app should be notified that every 10 meter distance change
            location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null) {
                Log.i("mytag", "NET"+location.getLatitude() + " lat");
                Log.i("mytag", "NET"+location.getLongitude() + " lon");
            }
//
        }else{
           showGpsSettings();//force user to enable the location

        }


        final DocumentReference documentReference= FirebaseFirestore.getInstance().document("pickers/trucks");

        final Trucks truckObject=new Trucks();
        truckObject.setTruckDriverName("bikram shah");
        truckObject.setTruckDriverPnumber("981231231");
        truckObject.setTruckId("asd32qwada2asdad");
        truckObject.setTimestamp(System.currentTimeMillis()+"");


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

     FirebaseFirestore.getInstance().collection("pickers").add(truckObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                //after inserting new documents add the data(if docuements dont matter u can do this )
//                Log.i("mytag","getid "+documentReference.getId());//current added documents name
//                Log.i("mytag","getfirestoresettings "+documentReference.getFirestore().getFirestoreSettings()+"");//firestore db settings
//                Log.i("mytag","getfirestore app "+documentReference.getFirestore().getApp()+"");//info abt the project app ids andso on
//                Log.i("mytag","get path "+documentReference.getPath()+"");

            }
        });

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
                if(documentSnapshots!=null) {
                    for (DocumentSnapshot each : documentSnapshots.getDocuments()) {
                        Log.i("mytag", each.getData() + "");

                    }
                }

            }
        });

        final Waste waste=new Waste();
        waste.setSourceId("12121");
        waste.setSourceLat("27.698840");
        waste.setSourceLon("85.313860");
        waste.setSourceStatus(Values.WASTE_AVAILABLE);//available

        FirebaseFirestore.getInstance().collection("wastes").add(waste).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                if(documentReference!=null){
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.i("mytag",documentSnapshot.getData()+"");

                        }
                    });
                }

            }
        });

        registerDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Trucks trucks1=new Trucks();
                trucks1.setTruckId("1");
                trucks1.setTruckDriverName("Bikram thapa");
                trucks1.setTruckPosLat("27.698845");
                trucks1.setTruckPosLon("85.313870");
                trucks1.setTimestamp(System.currentTimeMillis()+"");

             Trucks trucks2=new Trucks();
                trucks2.setTruckId("2");
                trucks2.setTruckDriverName("Shyam hari shrestha ");
                trucks2.setTruckPosLat("30.698849");
                trucks2.setTruckPosLon("85.313877");
                trucks2.setTimestamp(System.currentTimeMillis()+"");

             Trucks trucks3=new Trucks();
                trucks3.setTruckId("3");
                trucks3.setTruckDriverName("NIraj  khadka");
                trucks3.setTruckPosLat("31.698445");
                trucks3.setTruckPosLon("85.313990");
                trucks3.setTimestamp(System.currentTimeMillis()+"");



                FirebaseFirestore.getInstance().collection("registerDrivers").add(trucks1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {


                    }
                }); FirebaseFirestore.getInstance().collection("registerDrivers").add(trucks2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {

                    }
                }); FirebaseFirestore.getInstance().collection("registerDrivers").add(trucks3).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {


                    }
                });
            }
        });

        driverRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NearbyRequest request=new NearbyRequest();
                request.setWaste(waste);
                request.setStatus(null);
                request.setTruckId("1");
                FirebaseFirestore.getInstance().collection("nearbyRequest").add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {
//                        Log.i("mytag","getid "+documentReference.getId());//current added documents name
//                        Log.i("mytag","get path "+documentReference.get().getResult().toObject(NearbyRequest.class).getTruckId()+"");

                    }
                });
            }
        });



    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("mytag",location+"");

        if((Object)location.getLatitude() instanceof Double){
            progressBar.setVisibility(View.GONE);
            //update the current location of the user address
            //also stop listening futher updates
            locationManager.removeUpdates(this);
        }

        Log.i("mytag","nothing called");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i("mytag",s+" provider enabled");

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("mytag",s+" provider disabled");

    }

    private void showGpsSettings(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("Location Settings");
        alertDialog.setMessage("Allow location for KAWADI");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

    private BroadcastReceiver LocationStatus =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals("locationService")){
//
//
//            }

        }
    };






    }

