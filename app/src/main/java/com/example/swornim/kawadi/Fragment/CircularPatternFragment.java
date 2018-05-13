package com.example.swornim.kawadi.Fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swornim.kawadi.CustomSharedPref;
import com.example.swornim.kawadi.DataStructure.ViewDataWaste;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.DataStructure.WasteData;
import com.example.swornim.kawadi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class CircularPatternFragment extends Fragment {
    private GoogleMap map;
    private ImageView nearby;
    private ImageView circular;
    private ImageView nearbyTrucks;
    private List<Waste> wasteList=new ArrayList<>();//for wastes in waste collection
    private List<WasteData> nearyByList=new ArrayList<>();


    public static CircularPatternFragment newInstance(ViewDataWaste viewDataWaste) {
        CircularPatternFragment fragment = new CircularPatternFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", viewDataWaste);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.circularpatterfragment,container,false);
        ViewDataWaste viewDataWaste=(ViewDataWaste) getArguments().getSerializable("object");
        nearyByList=viewDataWaste.getTotalWastes();


        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.circularMapFragment));
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map=googleMap;
                map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(),R.raw.map_style_standard));
                //trucks position of the driver


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



                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int position=-1;//deosnot make sense in reality
                        for(int i=0;i<nearyByList.size();i++){

                            if(nearyByList.get(i).getSourceId()==marker.getTag()){
                                position=i;
                                break;
                            }
                        }

                        Double lat=Double.parseDouble(new  CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
                        Double lon=Double.parseDouble(new  CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LON"));

                        LatLng destination = new LatLng(
                                Double.parseDouble(nearyByList.get(position).getSourceLat())
                                , Double.parseDouble(nearyByList.get(position).getSourceLon())
                        );
                         LatLng origin = new LatLng(lat,lon);//with respect to driver position

                        String url = getDirectionsUrl(origin, destination);
                            new CircularPatternFragment.DownloadTask().execute(url);
                            // Start downloading json data from Google Directions API
                            //every calls runs different instance but url might cause error as it is global
                        Toast.makeText(getContext(),"Waste clicked "+nearyByList.get(position).getSourceId(),Toast.LENGTH_LONG).show();

                        return false;
                    }
                });


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
        nearbyTrucks=mView.findViewById(R.id.nearbyTrucks);

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*send request to the server for nearby wastes*/

//                Trucks truckDriver=new Trucks();
//                truckDriver.setTruckDriverName("Swornim Bikram Shah");
//                truckDriver.setTruckDriverPnumber("9821233455");
//                truckDriver.setTimestamp(System.currentTimeMillis()+"");
//                truckDriver.setTruckPosLat("27.658844");
//                truckDriver.setTruckPosLon("85.324863");
//                truckDriver.setTruckId("200");
//                truckDriver.setSelfRequest(false);
//                FirebaseFirestore.getInstance().collection("pickers").add(truckDriver).
//                        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.i("mytag","requested  successfully "+documentReference.getId());
//
//
//                                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                                        if (e != null) {
//                                            Log.i("mytag", "Listen failed.", e);
//                                            return;
//                                        }
//
//                                        String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
//                                                ? "Local" : "Server";
//
//                                        if (documentSnapshot != null && documentSnapshot.exists()) {
//                                            Log.i("mytag","data "+documentSnapshot);
//                                            Trucks trucks=documentSnapshot.toObject(Trucks.class);
//                                            Log.i("mytag","truck driver name  "+trucks.getTruckDriverName());
//
//                                            try {
//                                                JSONArray jsonArray=new JSONArray(trucks.getTruckwastes());
//                                                Gson gson=new Gson();
//
//                                                for(int i=0;i<jsonArray.length();i++){
//                                                    WasteData each= gson.fromJson(jsonArray.getJSONObject(i).toString(),WasteData.class);
//                                                    Log.i("mytag","each sourceid "+each.getSourceId());
//
//                                                }
//
//                                            } catch (JSONException jsonException) {
//                                                jsonException.printStackTrace();
//                                            }
//
//                                        } else {
//                                            Log.i("mytag", source + " truck waste is : null");
//                                        }
//
//                                    }
//                                });
//
//                            }
//                        });


                //first zoom camera on nearest and then user clicks to travel

                if (map != null) {
                    map.clear();
                    Double lat=Double.parseDouble(new  CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
                    Double lon=Double.parseDouble(new  CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LON"));
                    LatLng currentLatLng=new LatLng(lat,lon);


                    map.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.garbagetruck)));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15));

                    for (int i = 0; i < nearyByList.size(); i++) {
                        //run only n-1 because its in pair 0 and 1, 1 and 2, 2 and 3 so on
                        LatLng origin = new LatLng(
                                Double.parseDouble(nearyByList.get(i).getSourceLat())
                                , Double.parseDouble(nearyByList.get(i).getSourceLon())
                        );

//                                LatLng destination = new LatLng(
//                                        Double.parseDouble(nearyByList.get(i + 1).getSourceLat())
//                                        , Double.parseDouble(nearyByList.get(i + 1).getSourceLon())
//                                );
                        map.addMarker(new MarkerOptions()
                                .title(nearyByList.get(i).getSourceType())
                                .position(origin)

                        ).setTag(nearyByList.get(i).getSourceId());


//                                String url = .getDirectionsUrl(origin, destination);
//                            new CircularPatternFragment.DownloadTask().execute(url);
//                            // Start downloading json data from Google Directions API
//                            //every calls runs different instance but url might cause error as it is globa

                    }


                }

            }

        });

        circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wasteList.size()>0){

                    List<Integer> weightageContainer=new ArrayList<>();
                    weightageContainer.add(500);
                    weightageContainer.add(300);
                    weightageContainer.add(190);
                    weightageContainer.add(220);
                    weightageContainer.add(225);
                    if(map!=null){
                        map.clear();//clear previous even nothing is present

                        Log.i("mytag",wasteList.size()+"");
                        for(int i=0;i<wasteList.size();i++){
                            LatLng latLng=new LatLng(
                                    Double.parseDouble(wasteList.get(i).getSourceLat()),
                                    Double.parseDouble(wasteList.get(i).getSourceLon())
                            );
//                            map.addMarker(new MarkerOptions().position(latLng).title(wasteList.get(i).getSourceType()));
                            map.addCircle(new CircleOptions()
                                    .center(latLng)
                                    .radius(weightageContainer.get(i))
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.RED));

                        }

                    }

                }else {
                    Toast.makeText(getContext(),"please wait data is coming",Toast.LENGTH_LONG).show();
                }

            }
        });


        nearbyTrucks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* access all the truck driver from pickers collection */
                //todo avoid multiple clicks to send multiple requests
                Toast.makeText(getContext(),"New code is coming broo",Toast.LENGTH_LONG).show();

            }
        });


        return mView;
    }



    /*Direction code starts from here*/
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
    private String downloadUrl(String strUrl) throws IOException {
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
            Toast.makeText(getContext(),"Downloading error check internet connection",Toast.LENGTH_LONG).show();

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

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
                CircularPatternFragment.DownloadTask downloadTask=new CircularPatternFragment.DownloadTask();
                downloadTask.execute(url);

            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            CircularPatternFragment.ParserTask parserTask = new CircularPatternFragment.ParserTask();

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

            if(!jsonData.equals("")) {
                try {
                    jObject = new JSONObject(jsonData[0]);
                    CircularPatternFragment.DirectionsJSONParser parser = new CircularPatternFragment.DirectionsJSONParser();

                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getContext(),"No Json Response from server",Toast.LENGTH_LONG).show();

            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();


            if (result != null) {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(6);
                    lineOptions.color(Color.BLUE);
                }

                // Drawing polyline in the Google Map for the i-th route
                map.addPolyline(lineOptions);
            }else{
                Toast.makeText(getContext(),"Probably Routing error",Toast.LENGTH_LONG).show();
            }
        }
    }





}
