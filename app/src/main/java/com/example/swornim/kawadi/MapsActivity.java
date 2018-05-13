package com.example.swornim.kawadi;


import android.graphics.Color;
import android.os.AsyncTask;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swornim.kawadi.DataStructure.WasteData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap directionMap;
    private List<WasteData> nearbyList=new ArrayList<>();
    private SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.directionMapFragment);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        /* google map logo in the footer or copyrights logo should be displayed ( by default it displays
        but should not be removed even though we can remove it programmatically by GoogleMapOptions class */

        directionMap = googleMap;
        directionMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_standard));


//        final Polygon polygon=mMap.addPolygon(new PolygonOptions()
//                .add
//                        (
//                        new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon())),
//                        new LatLng(Double.parseDouble(nearbyList.get(1).getSourceLat()), Double.parseDouble(nearbyList.get(1).getSourceLon())),
//                        new LatLng(Double.parseDouble(nearbyList.get(2).getSourceLat()), Double.parseDouble(nearbyList.get(2).getSourceLon())),
//                        new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon()))
//                        )
//                .strokeColor(Color.RED));
//        polygon.setStrokeWidth(6);
//        polygon.setClickable(true);
//        polygon.setTag("polygoneId");
//
//        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
//            @Override
//            public void onPolygonClick(Polygon polygon) {
//
//                Log.i("mytag","polygone tag "+polygon.getTag());
//                Log.i("mytag","polygone id "+polygon.getId());
//                Log.i("mytag","stroke width  "+polygon.getStrokeWidth());
//
//            }
//        });

        Polyline line = directionMap.addPolyline(new PolylineOptions()
                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0), new LatLng(27.68, 85))
                .width(5)
                .color(Color.BLUE));
        line.setClickable(true);

//        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
//            @Override
//            public void onPolylineClick(Polyline polyline) {
//                Log.i("mytag","polyline is cliked width  "+polygon.getPoints());
//
//            }
//        });


        Circle circle = directionMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(nearbyList.get(0).getSourceLat()), Double.parseDouble(nearbyList.get(0).getSourceLon())))

                .radius(50)
                .strokeColor(Color.BLUE)
                .fillColor(Color.YELLOW));
        circle.setClickable(true);


        directionMap.setInfoWindowAdapter(new CustomInformationWindow());

        directionMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                Log.i("mytag","custom info clicked" +marker.getTitle());
                marker.hideInfoWindow();


            }
        });

        directionMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
            }
        });

        /*zoom level details values:

        1 =>world
        5:landmass/continent
        10:city
        15:streets
        20:buildings

         */

        final List<MarkerOptions> options=new ArrayList<>();
        directionMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("mytag","lat is "+latLng.latitude);
                Log.i("mytag","lon is "+latLng.longitude);
                options.add(new MarkerOptions().position(latLng));
                directionMap.addMarker(new MarkerOptions().position(latLng));

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


    private class CustomInformationWindow implements GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            View mView=getLayoutInflater().inflate(R.layout.custom_map_info,null);
            TextView driverName=mView.findViewById(R.id.driverName);
            ImageView driverPhoto=mView.findViewById(R.id.driverPhoto);

            if(marker.getTitle().equals("Gilfoyle")){
                driverPhoto.setImageResource(R.mipmap.gilfoyle);
                driverName.setText(marker.getTitle());

            }
            if(marker.getTitle().equals("Richard")){
                driverPhoto.setImageResource(R.mipmap.richard);
                driverName.setText(marker.getTitle());

            }
            if(marker.getTitle().equals("Eric")){
                driverPhoto.setImageResource(R.mipmap.silicon_valley2);
                driverName.setText(marker.getTitle());

            }
            if(marker.getTitle().equals("buckethead")){
                driverPhoto.setImageResource(R.mipmap.littlehead);
                driverName.setText(marker.getTitle());

            }



            return mView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return  null;
        }
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
            Toast.makeText(getApplicationContext(),"Downloading error check internet connection",Toast.LENGTH_LONG).show();

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;//actual json response of directions
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
                Toast.makeText(getApplicationContext(),"No Json Response from server",Toast.LENGTH_LONG).show();

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
                directionMap.addPolyline(lineOptions);
            }else{
                Toast.makeText(getApplicationContext(),"Probably Routing error",Toast.LENGTH_LONG).show();
            }
        }
    }



}

