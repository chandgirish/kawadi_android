package com.example.swornim.kawadi;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swornim.kawadi.DataStructure.Firestore;
import com.example.swornim.kawadi.DataStructure.NearbyRequest;
import com.example.swornim.kawadi.DataStructure.Trucks;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button updateMap;
    private Button addwastes;
    private TextView read;
    private List<Nearby> nearbyList=new ArrayList<>();
    private static String jsonresponse;

    LatLng sydney5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        read=(TextView) findViewById(R.id.read);
        updateMap=(Button)findViewById(R.id.mapButton);
        addwastes=(Button)findViewById(R.id.addwastes);
        updateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*request nearby wastes*/

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {


                    }else {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                0);

                    }

                }else{
                    Log.i("mytag","request has been sent");

                    updateMap();
                }

            }
        });

        addwastes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                read.setText(jsonresponse);
//
//                File file=new File(Environment.getExternalStorageDirectory(),"json.txt");
//                if(!file.exists()){
//                    try {
//                        file.createNewFile();
//                        FileOutputStream fileout=openFileOutput(file, MODE_PRIVATE);
//                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                        outputWriter.write(jsonresponse);
//                        outputWriter.close();
//
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }


                //add 20 wastes explicitly
//
//                List<Waste> wasteList=new ArrayList<>();
//                Waste waste1=new Waste();
//                waste1.setSourceLat("27.698840");
//                waste1.setSourceLon("85.313860");
//                waste1.setSourceId("1");
//
//                Waste waste2=new Waste();
//                waste2.setSourceLat("27.608837");
//                waste2.setSourceLon("85.314867");
//                waste2.setSourceId("2");
//
//                Waste waste3=new Waste();
//                waste3.setSourceLat("27.658845");
//                waste3.setSourceLon("85.324864");
//                waste3.setSourceId("3");
//
//                Waste waste4=new Waste();
//                waste4.setSourceLat("27.678847");
//                waste4.setSourceLon("85.314964");
//                waste4.setSourceId("4");
//
//                Waste waste5=new Waste();
//                waste5.setSourceLat("27.628833");
//                waste5.setSourceLon("85.3166865");
//                waste5.setSourceId("5");
//
//
//
//                wasteList.add(waste1);
//                wasteList.add(waste2);
//                wasteList.add(waste3);
//                wasteList.add(waste4);
//                wasteList.add(waste5);
//
//                for(int i=0;i<wasteList.size();i++){
//                    FirebaseFirestore.getInstance().collection("wastes").add(wasteList.get(i)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            task.getResult().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    Log.i("mytag",documentSnapshot.toObject(Waste.class).getSourceLat()+"");
//                                }
//                            });
//                        }
//                    });
//                }

//                Waste waste1=new Waste();
//                 waste1.setSourceLat("27.668837");
//                 waste1.setSourceLon("85.384867");
//
//                waste1.setSourceId("6");
//                waste1.setSourceType("Home");
//                FirebaseFirestore.getInstance().collection("wastes").add(waste1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.i("mytag","added successfully "+documentReference.getId());
//                    }
//                });


                //request from truck driver
//
//                Trucks truckDriver=new Trucks();
//                truckDriver.setTruckDriverName("HARI THAPA");
//                truckDriver.setTruckDriverPnumber("9812312312");
//                truckDriver.setTimestamp(System.currentTimeMillis()+"");
//                truckDriver.setTruckPosLat("27.668840");
//                truckDriver.setTruckPosLon("85.314867");
//                truckDriver.setTruckId("1");
//                truckDriver.setSelfRequest(true);
//                FirebaseFirestore.getInstance().collection("pickers").add(truckDriver).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.i("mytag","requested  successfully "+documentReference.getId());
//                    }
//                });



            }
        });



        FirebaseFirestore.getInstance().document("pickers/4Wj9Yt8BjCnEiSOi3IbP").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.i("mytag", "Listen failed.", e);
                    return;
                }

                String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    if(documentSnapshot.toObject(Trucks.class).getTruckwastes()!=null)
                        Log.i("mytag", source + " data: " + documentSnapshot.toObject(Trucks.class).getTruckwastes());
                    String jsonString=documentSnapshot.toObject(Trucks.class).getTruckwastes();
                    try {
                        JSONArray root=new JSONArray(jsonString);//array
                        for(int i=0;i<root.length();i++){
                            JSONObject jsonObject=root.getJSONObject(i);
                            Gson gson=new Gson();
                            Nearby nearby=gson.fromJson(jsonObject.toString(),Nearby.class);
                            nearbyList.add(nearby);
                            Log.i("mytag","sourceid  "+nearby.sourceId);

                        }

                        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);



                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    Log.i("mytag", source + " truck waste is : null");
                }


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //show the new style of map
//        MapStyleOptions mapStyleOptions=MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style_json);
//        mMap.setMapStyle(mapStyleOptions);
        // Add a marker in Sydney and move the camera

        final Polygon polygon=mMap.addPolygon(new PolygonOptions()
                .add
                        (
                        new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon())),
                        new LatLng(Double.parseDouble(nearbyList.get(1).getSourceLat()), Double.parseDouble(nearbyList.get(1).getSourceLon())),
                        new LatLng(Double.parseDouble(nearbyList.get(2).getSourceLat()), Double.parseDouble(nearbyList.get(2).getSourceLon())),
                        new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon()))
                        )
                .strokeColor(Color.RED));
        polygon.setStrokeWidth(5);
        polygon.setClickable(true);
        polygon.setTag("polygoneId");

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {

                Log.i("mytag","polygone tag "+polygon.getTag());
                Log.i("mytag","polygone id "+polygon.getId());
                Log.i("mytag","stroke width  "+polygon.getStrokeWidth());

            }
        });

        Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0), new LatLng(27.68, 85))
                        .width(5)
                        .color(Color.BLUE));
        line.setClickable(true);

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                Log.i("mytag","polyline is cliked width  "+polygon.getPoints());

            }
        });


        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon())))

                .radius(50)
                .strokeColor(Color.BLUE)
                .fillColor(Color.YELLOW));

        circle.setClickable(true);

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {

                Toast.makeText(getApplicationContext(),"Wastages around KOTESHWOR AREA ",Toast.LENGTH_SHORT).show();
            }
        });





        LatLng sydney1 =  new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon()));
        LatLng sydney2 =  new LatLng(Double.parseDouble(nearbyList.get(1).getSourceLat()), Double.parseDouble(nearbyList.get(1).getSourceLon()));
        LatLng sydney3 =  new LatLng(Double.parseDouble(nearbyList.get(2).getSourceLat()), Double.parseDouble(nearbyList.get(2).getSourceLon()));
        LatLng sydney4 =  new LatLng(Double.parseDouble(nearbyList.get(3).getSourceLat()), Double.parseDouble(nearbyList.get(3).getSourceLon()));

//        Log.i("mytag","distance gap "+SphericalUtil.computeDistanceBetween(sydney1,sydney2));//distance in meters

        mMap.addMarker(new MarkerOptions().position(sydney1).title("Marker in Sydney1"));
        mMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Sydney2"));
        mMap.addMarker(new MarkerOptions().position(sydney3).title("Marker in Sydney3"));
        mMap.addMarker(new MarkerOptions().position(sydney4).title("Marker in Sydney4"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney3,16));
        /*zoom level details values:

        1 =>world
        5:landmass/continent
        10:city
        15:streets
        20:buildings

         */


//        FirebaseFirestore.getInstance().collection("wastes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                for(DocumentSnapshot eachDoc:task.getResult().getDocuments()){
//                    Waste eachWaste=eachDoc.toObject(Waste.class);
////                    LatLng eachLatlon=new LatLng(Double.parseDouble(eachWaste.getSourceLat()),Double.parseDouble(eachWaste.getSourceLon()));
////                    mMap.addMarker(new MarkerOptions().position(eachLatlon).title(eachWaste.getSourceId()));
//
//                }
//
//            }
//        });
        final List<MarkerOptions> options=new ArrayList<>();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("mytag","lat is "+latLng.latitude);
                Log.i("mytag","lon is "+latLng.longitude);
                 options.add(new MarkerOptions().position(latLng));
                 mMap.addMarker(new MarkerOptions().position(latLng));

                if(options.size()==2){
                    // Getting URL to the Google Directions API
                    LatLng origin=options.get(0).getPosition();
                    LatLng destination=options.get(1).getPosition();
                    String url = getDirectionsUrl(origin, destination);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                    options.clear();

                }else{

                }

            }
        });


    }

    private void requestRoutingData(final String origin, final String destination) throws IOException {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+destination+"&waypoints=optimize:true&key=AIzaSyBBX6pCmyvDIFmrD3FAh7WzDpls0kfOTZg",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            jsonresponse=response;

                            int maxLogSize = 1000;
                            for(int i = 0; i <= jsonresponse.length() / maxLogSize; i++) {
                                int start = i * maxLogSize;
                                int end = (i+1) * maxLogSize;
                                end = end > jsonresponse.length() ? jsonresponse.length() : end;
                                Log.i("mytag", jsonresponse.substring(start, end));
                            }


                        }
                    }, new com.android.volley.Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("mytag", "something went wrong during the fetch process"+error);
                }
            }){

                };


            requestQueue.add(stringRequest);

    }


    private class Nearby implements Serializable{
        private String sourceType;//HOMES OR OFFICES OR OTHERS
        private String sourceLat;
        private String sourceLon;
        private String sourceId;//unique id of the application# drivers inputs this id
        private String sourceStatus;
        private String distance;
        private String duration;
        private List<Nearby> paths;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSourceLon() {
            return sourceLon;
        }

        public void setSourceLon(String sourceLon) {
            this.sourceLon = sourceLon;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getSourceLat() {
            return sourceLat;
        }

        public void setSourceLat(String sourceLat) {
            this.sourceLat = sourceLat;
        }

        public String getSourceStatus() {
            return sourceStatus;
        }

        public void setSourceStatus(String sourceStatus) {
            this.sourceStatus = sourceStatus;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public List<Nearby> getPaths() {
            return paths;
        }

        public void setPaths(List<Nearby> paths) {
            this.paths = paths;
        }
    }



    private void updateMap(){

        Trucks trucks = new Trucks();
        trucks.setTimestamp(System.currentTimeMillis() + "");
        trucks.setTruckId("9812121212");

        //send the request for getting the wastes nearby
        //todo maintaing single doc in each nearbyplaces collections
        FirebaseFirestore.getInstance().collection("pickers/hello/nearbyplaces").document("nearbyplaceDoc").set(trucks).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("mytag","failed to request "+e);
            }
        });

        //listeners for wastes
        FirebaseFirestore.getInstance().document("pickers/hello/nearbyplaces/recommendedWastes").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e!=null) {
                    Log.i("mytag", "listened failed " + e);
                    return;
                }
                if(documentSnapshot!=null && documentSnapshot.exists()){
                    Log.i("mytag", "data" + documentSnapshot.getData());

                            /*NOTE : CLient sends the data but if that data is also same in database then server wont push the data so in that way both client and server have consistent data
                              * with server has less load
                               *
                              firebase api maintains the cache data

                              But whenever the data changes it call backs immediately
                              */

                }

            }


        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateMap();
                    Log.i("mytag","user allowed location service for this application");

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

            }
        }


        /*map direction api*/
        public class DirectionsJSONParser {

            /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
            public List<List<HashMap<String,String>>> parse(JSONObject jObject){

                List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;

                try {

                    jRoutes = jObject.getJSONArray("routes");

                    /** Traversing all routes */
                    for(int i=0;i<jRoutes.length();i++){
                        jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                        List path = new ArrayList<HashMap<String, String>>();

                        /** Traversing all legs */
                        for(int j=0;j<jLegs.length();j++){
                            jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for(int k=0;k<jSteps.length();k++){
                                String polyline = "";
                                polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                /** Traversing all points */
                                for(int l=0;l<list.size();l++){
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                    hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                    path.add(hm);
                                }
                            }
                            routes.add(path);
                        }
                    }
                    Log.i("mytag","traversing completed");

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                }

                return routes;
            }
            /**
             * Method to decode polyline points
             * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
             * */
            private List<LatLng> decodePoly(String encoded) {

                List<LatLng> poly = new ArrayList<LatLng>();
                int index = 0, len = encoded.length();
                int lat = 0, lng = 0;

                while (index < len) {
                    int b, shift = 0, result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lat += dlat;

                    shift = 0;
                    result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lng += dlng;

                    LatLng p = new LatLng((((double) lat / 1E5)),
                            (((double) lng / 1E5)));
                    poly.add(p);
                }

                return poly;
            }
        }



    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String key="key="+"AIzaSyBBX6pCmyvDIFmrD3FAh7WzDpls0kfOTZg";
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+key;
        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.i("mytag", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);//json response
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }








}
