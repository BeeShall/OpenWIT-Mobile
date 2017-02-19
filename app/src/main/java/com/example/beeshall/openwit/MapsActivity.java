package com.example.beeshall.openwit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final EditText keyWords = (EditText) findViewById(R.id.txtSearch);
        Button searchBtn = (Button) findViewById(R.id.btnSearch);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Globals.serverAddress +"search";
                JSONObject body = new JSONObject();
                try {
                    body.put("keywords", keyWords.getText());

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, body, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray infoList =  (JSONArray) response.get("items");
                                        //System.out.println(infoList.toString());
                                        makeMap(infoList);
                                    } catch (JSONException e) {
                                        Log.v("Error",e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error.toString());
                                }
                            });
                    QueueSingleton.getInstance(MapsActivity.this).addToRequestQueue(jsObjRequest);
                }
                catch (JSONException e){
                    System.out.println("Exception while creating JSON");

                }
            }
        });



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Map<String, String> coordinates;
        // Loop to fetch relevant data ::
        // For now just pointless data I populate myself.
        String url = Globals.serverAddress +"GetAllEvents";

        JSONObject body = new JSONObject();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray infoList =  (JSONArray) response.get("nearbyevents");
                        makeMap(infoList);
                    } catch (JSONException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.toString());
                }
            });
            QueueSingleton.getInstance(MapsActivity.this).addToRequestQueue(jsObjRequest);


        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(40.7554704, -73.9788531);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void makeMap(JSONArray information) throws JSONException{
        mMap.clear();
        Log.v("Information ::  ", information.toString());
        List <LatLng> coord = new ArrayList<>();
        List <String> titles = new ArrayList<>();
        List <String> descr = new ArrayList<>();
        List <Marker> markers = new ArrayList<>();
        List <View.OnClickListener> clicks = new ArrayList<>();

        for(int i =0; i<information.length(); i++){
            String _title = (String) information.getJSONObject(i).get("title");
            String description = (String) information.getJSONObject(i).get("location");
            JSONObject location = (JSONObject) information.getJSONObject(i).get("coords");
            double lat = Double.parseDouble(location.get("lat").toString());
            double lng = Double.parseDouble(location.get("lng").toString());
            coord.add(new LatLng(lat, lng));
            titles.add(_title);
            descr.add(description);
            markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(_title).snippet(description)));
        }
    }

}
