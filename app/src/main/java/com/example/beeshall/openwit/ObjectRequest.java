package com.example.beeshall.openwit;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by beeshall on 2/18/17.
 */

public class ObjectRequest extends JsonObjectRequest {
    public ObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public ObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> responseHeaders = response.headers;
        Response<JSONObject> resp = super.parseNetworkResponse(response);
        for(String key: responseHeaders.keySet()){
            try {
                resp.result.put(key, responseHeaders.get(key));
            }
            catch (JSONException e){
                System.out.println(e.getMessage());
            }
        }

        return resp;
    }
}
