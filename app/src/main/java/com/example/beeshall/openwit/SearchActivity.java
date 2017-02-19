package com.example.beeshall.openwit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        final String user = (String) intent.getExtras().get("user");

        final NumberPicker proximityPicker = (NumberPicker) findViewById(R.id.numProx);
        proximityPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        proximityPicker.setMaxValue(25);
        proximityPicker.setMinValue(1);
        proximityPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return ""+(value*5);
            }
        });

        final EditText keywords = (EditText) findViewById(R.id.txtKeyword);
        final Spinner searchType = (Spinner) findViewById(R.id.spinSearchType);


        Button search = (Button) findViewById(R.id.btnSearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Globals.serverAddress +"search";
                Log.v("URl",url);
                JSONObject body = new JSONObject();
                try {
                    body.put("keywords", keywords.getText());
                    body.put("distance", proximityPicker.getValue()*5);
                    body.put("type", (searchType.getSelectedItem()).toString().toLowerCase());
                    Log.v("Type ", "" +(searchType.getSelectedItem()).toString());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, body, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent newIntent = new Intent(SearchActivity.this, MapsActivity.class);
                                    try {
                                        if ((boolean)response.get("success")) {
                                            newIntent.putExtra("user", user);
                                            newIntent.putExtra("data", response.toString());
                                            startActivity(newIntent);
                                        }
                                        else{
                                            Toast.makeText(SearchActivity.this,"Search failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (JSONException e){
                                        System.out.println("Exception while creating JSON");

                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error.toString());
                                }
                            }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> headers = new HashMap<String, String>();
                            headers.put("user",user);
                            return headers;
                        }
                    };
                    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    QueueSingleton.getInstance(SearchActivity.this).addToRequestQueue(jsObjRequest);

                }
                catch (JSONException e){
                    System.out.println("Exception while creating JSON");

                }

            }
        });

        Button act_search = (Button) findViewById(R.id.btnSearchAct);
        Button act_map = (Button) findViewById(R.id.btnMap);
        Button act_create = (Button) findViewById(R.id.btnCreateAct);

        act_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, SearchActivity.class));
            }
        });

        act_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MapsActivity.class));
            }
        });

        /*act_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, CreateActivity.class));
            }
        });*/

    }


}
