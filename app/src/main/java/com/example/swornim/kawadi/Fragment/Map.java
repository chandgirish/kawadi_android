package com.example.swornim.kawadi.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.swornim.kawadi.DataStructure.Paths;
import com.example.swornim.kawadi.DataStructure.TruckData;
import com.example.swornim.kawadi.DataStructure.WasteData;
import com.example.swornim.kawadi.R;
import com.example.swornim.kawadi.Tabactivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

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

public class Map extends Fragment implements  OnMapReadyCallback{

    private MapView mapView;
    private GoogleMap googleMap;
    public List<WasteData> recommendedWastes = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.map_view,container,false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mapView=view.findViewById(R.id.directionMapView);
        if(mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        MapsInitializer.initialize(getContext());

        renderData(googleMap);


    }



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
        return data;//actual json response of directions
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
                DownloadTask downloadTask=new DownloadTask();
                downloadTask.execute(url);

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

            if(!jsonData.equals("")) {
                try {
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

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
                    lineOptions.width(7);
                    lineOptions.color(Color.RED);
                }

                // Drawing polyline in the Google Map for the i-th route
                googleMap.addPolyline(lineOptions);
            }else{
                Toast.makeText(getContext(),"Probably Routing error",Toast.LENGTH_LONG).show();
            }
        }
    }


    private class CustomWindowInforamtion implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {

            View mView=getLayoutInflater().inflate(R.layout.activity_circular,null);
            return mView;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View mView=getLayoutInflater().inflate(R.layout.activity_circular,null);
            return mView;        }
    }


    private void renderData(final GoogleMap googleMap) {
        FirebaseFirestore.getInstance().collection("testPickers").document("9813847444").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

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
                                if(!wasteJobject.optString("paths").equals("null") && wasteJobject.optString("paths")!=null){
                                    JSONArray paths = new JSONArray(wasteJobject.optString("paths"));
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
                            //add all the wastes to the google map instance

                            for(WasteData each:recommendedWastes){

                                LatLng latLng=new LatLng
                                        (
                                                Double.parseDouble(each.getSourceLat()),
                                                Double.parseDouble(each.getSourceLon())
                                        );
                                googleMap.addMarker(new MarkerOptions().position(latLng).title(each.getAddress()));
                                googleMap.setInfoWindowAdapter(new CustomWindowInforamtion());
                                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        marker.hideInfoWindow();
                                    }
                                });
                            }



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



}
