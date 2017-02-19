package com.example.beeshall.openwit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText userName = (EditText) findViewById(R.id.txtUserName);
        final EditText email = (EditText) findViewById(R.id.txtEmail);
        final EditText zipcode = (EditText) findViewById(R.id.txtZip);
        final EditText password = (EditText) findViewById(R.id.txtPassword);
        final EditText confPass = (EditText) findViewById(R.id.txtConfPass);

        Button register = (Button) findViewById(R.id.btnSignUp);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Globals.serverAddress +"signup_mobile";
                JSONObject body = new JSONObject();
                try {
                    body.put("username", userName.getText());
                    body.put("password", password.getText());
                    body.put("email", email.getText());
                    body.put("zip", zipcode.getText());

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, body, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        System.out.print(response.toString());
                                        Log.v("resposne",response.toString());
                                        if ((boolean)response.get("success")) {
                                            Intent newIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                            finishActivity(0);
                                            startActivity(newIntent);
                                        }
                                        else{
                                            Toast.makeText(SignUpActivity.this,"Invalid field values",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        System.out.print(e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error.toString());
                                }
                            });
                    QueueSingleton.getInstance(SignUpActivity.this).addToRequestQueue(jsObjRequest);
                }
                catch (JSONException e){
                    System.out.println("Exception while creating JSON");

                }

            }
        });
    }
}
