package com.drone.imavis.webodm.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.example.is2.test2qrventory.LoginActivity;
//import com.example.is2.test2qrventory.controller.AppController;
//import com.example.is2.test2qrventory.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrian on 14.06.2016.
 */


public class Login {
/*
    private String url ="http://qrventory.square7.ch/v1/login";
    private User user = null;

    public Login(String email, String password) {
        user = new User(email,password);
    }

    public void authentifiacation(final VolleyResponseListener listener) {

        // Request a string response from the provided URL.
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG", response);

                        JSONObject person = null;
                        try {
                            person = new JSONObject(response);
                            user.setIdUser(person.getLong("IdUser"));
                            user.setEmail(person.getString("Email"));
                            user.setApiKey(person.getString("ApiKey"));
                            user.setFirstname(person.getString("Firstname"));
                            user.setLastname(person.getString("Lastname"));
                            user.setImageURL(person.getString("Image"));

                            //byte[] decodedString = Base64.decode(person.getString("image"), Base64.DEFAULT);
                            //Bitmap bitmap_decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            //user.setImage(bitmap_decoded);

                            listener.onResponse(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textViewResponse = (TextView) findViewById(R.id.textViewResponse);
                        //textViewResponse.setText("That didn't work!");
                        listener.onError(error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        AppController.getInstance().addToRequestQueue(stringLoginRequest);
    }
    */
}
