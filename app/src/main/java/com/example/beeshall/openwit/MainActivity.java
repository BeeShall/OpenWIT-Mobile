package com.example.beeshall.openwit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText userName = (EditText) findViewById(R.id.inputUserName);
        final EditText password = (EditText) findViewById(R.id.inputPassword);

        Button logInBtn = (Button) findViewById(R.id.btnLogIn);
        System.out.print(logInBtn.getText());

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Globals.serverAddress +"login_mobile";
                JSONObject body = new JSONObject();
                try {
                    body.put("username", userName.getText());
                    body.put("password", password.getText());

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, body, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent newIntent = new Intent(MainActivity.this, SearchActivity.class);
                                    try {
                                        if ((boolean)response.get("success")) {

                                            Log.v("id", response.toString());
                                            newIntent.putExtra("user", response.get("user").toString() );
                                            startActivity(newIntent);
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
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
                            });
                    QueueSingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);
                }
                catch (JSONException e){
                    System.out.println("Exception while creating JSON");

                }


            }
        });

        Button signUpBtn = (Button) findViewById(R.id.btnRegister);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(newIntent);

            }
        });











    }
}
