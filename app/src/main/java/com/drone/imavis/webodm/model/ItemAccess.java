package com.drone.imavis.webodm.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drone.imavis.webodm.controller.ConnectionController;
//import com.example.is2.test2qrventory.LoginActivity;
//import com.example.is2.test2qrventory.MainActivity;
//import com.example.is2.test2qrventory.controller.AppController;
//import com.example.is2.test2qrventory.model.Category;
//import com.example.is2.test2qrventory.model.Domain;
//import com.example.is2.test2qrventory.model.Item;
//import com.example.is2.test2qrventory.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrian on 15.06.2016.
 */
public class ItemAccess {

//    private String url ="http://qrventory.square7.ch/v1/item";
//    private String url_items ="http://qrventory.square7.ch/v1/event-items/";
//    private String userApiKey;
//    private Item item;
//
//    private String item_json_body;
//
//    public ItemAccess(String userApiKey) {
//        this.userApiKey = userApiKey;
//    }
//
//    public void getItem(final VolleyResponseListener listener, long item_id) {
//
//        String url_new = url += "/" + item_id;
//
//        // Request a string response from the provided URL.
//        StringRequest getItemRequest = new StringRequest(Request.Method.GET, url_new,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        item = null;
//                        try {
//                            JSONObject item_response = new JSONObject(response);
//                            boolean isError = item_response.getBoolean("error");
//
//                            if(!isError) {
//                                item = new Item();
//                                item.setId(item_response.getLong("IdItem"));
//                                item.setName(item_response.getString("Name"));
//                                item.setDescription(item_response.getString("Description"));
//                                item.setBarcodeURL(item_response.getString("Barcode"));
//                                item.setQRcodeURL(item_response.getString("QRcode"));
//                                item.setImageURL(item_response.getString("Image"));
//                                item.setIsQR((item_response.getInt("IsQR") == 1) ? true : false);
//                            }
//
//                        } catch (JSONException e) {
//                            item = null;
//                            e.printStackTrace();
//                        }
//                        listener.onResponse(item);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //textViewResponse = (TextView) findViewById(R.id.textViewResponse);
//                        //textViewResponse.setText("That didn't work!");
//                        listener.onError(error.toString());
//                    }
//                }){
//            /*@Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("email", user.getEmail());
//                params.put("password", user.getPassword());
//                return params;
//            }*/
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("X-Authorization", userApiKey);
//                return params;
//            }
//        };
//
//        // Add the request to the RequestQueue.
//        //MainActivity.getInstance().getRequestQueue().add(stringRequest);
//
//        // Adding request to request queue
//        ConnectionController.getInstance().addToRequestQueue(getItemRequest);
//    }
//
//    public void getEventItemsThatExists(final VolleyResponseListener listener, long domain_id, long event_id) {
//        int exists = 1; // true
//        String url = url_items + domain_id + "/" + event_id + "/" + exists;
//
//        // Request a string response from the provided URL.
//        StringRequest getItemRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        List<Item> item_list = handleGetItems(response);
//                        listener.onResponse(item_list);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //textViewResponse = (TextView) findViewById(R.id.textViewResponse);
//                        //textViewResponse.setText("That didn't work!");
//                        listener.onError(error.toString());
//                    }
//                }){
//            /*@Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("email", user.getEmail());
//                params.put("password", user.getPassword());
//                return params;
//            }*/
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("X-Authorization", userApiKey);
//                return params;
//            }
//        };
//
//        // Add the request to the RequestQueue.
//        //MainActivity.getInstance().getRequestQueue().add(stringRequest);
//
//        // Adding request to request queue
//        ConnectionController.getInstance().addToRequestQueue(getItemRequest);
//    }
//
//    public void getEventItemsThatNotExists(final VolleyResponseListener listener, long domain_id, long event_id) {
//        int exists = 0; // false
//        String url = url_items + domain_id + "/" + event_id + "/" + exists;
//
//        // Request a string response from the provided URL.
//        StringRequest getItemRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        List<Item> item_list = handleGetItems(response);
//                        listener.onResponse(item_list);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //textViewResponse = (TextView) findViewById(R.id.textViewResponse);
//                        //textViewResponse.setText("That didn't work!");
//                        listener.onError(error.toString());
//                    }
//                }){
//            /*@Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("email", user.getEmail());
//                params.put("password", user.getPassword());
//                return params;
//            }*/
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("X-Authorization", userApiKey);
//                return params;
//            }
//        };
//
//        // Add the request to the RequestQueue.
//        //MainActivity.getInstance().getRequestQueue().add(stringRequest);
//
//        // Adding request to request queue
//        ConnectionController.getInstance().addToRequestQueue(getItemRequest);
//    }
//
//    private JSONObject toJsonParser(Item item, long domain_id, int category_parent_id) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("Name", item.getName());
//            jsonObject.put("Description", item.getDescription());
//            jsonObject.put("IsQR", (item.getIsQR() == true) ? 1 : 0);
//            jsonObject.put("QRcode", item.getQRcodeURL());
//            jsonObject.put("Barcode", item.getBarcodeURL());
//            jsonObject.put("Image", item.getImageURL());
//            jsonObject.put("Category_Domain_IdDomain", domain_id);
//            jsonObject.put("Category_IdCategory", category_parent_id);
//            //JSONObject jsonObject2 = new JSONObject().put("answers", jsonObject);
//            //jsonObject2.put("user_id", 12);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    public void addItem(final VolleyResponseListener listener, final Item item, long domain_id, int category_parent_id) {
//
//        JSONObject item_json = toJsonParser(item, domain_id, category_parent_id);
//        item_json_body = item_json.toString();
//
//
//        // Request a string response from the provided URL.
//        StringRequest addItemRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Item item_new = null;
//                        try {
//                            JSONObject json_response = new JSONObject(response);
//                            boolean isError = json_response.getBoolean("error");
//
//                            if(!isError) {
//                                long category_id = json_response.getLong("IdItem");
//                                //String image_url = json_response.getString("Image");
//
//                                if(category_id > 0) {
//                                    item_new = item;
//                                    item_new.setId(category_id);
//                                    //category_new.setImageURL(image_url);
//                                } else {
//                                    item_new = null;
//                                }
//                            }
//
//                            //byte[] decodedString = Base64.decode(json_response.getString("image"), Base64.DEFAULT);
//                            //Bitmap bitmap_decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                            //user.setImage(bitmap_decoded);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        listener.onResponse(item_new);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //textViewResponse = (TextView) findViewById(R.id.textViewResponse);
//                        //textViewResponse.setText("That didn't work!");
//                        listener.onError(error.toString());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("X-Authorization", userApiKey);
//                return params;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return String.format("application/json; charset=utf-8");
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return item_json_body == null ? null : item_json_body.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
//                            item_json_body, "utf-8");
//                    return null;
//                }
//            }
//        };
//
//        // Adding request to request queue
//        ConnectionController.getInstance().addToRequestQueue(addItemRequest);
//    }
//
//    private List<Item> handleGetItems(String response) {
//        List<Item> item_list = null;
//        try {
//            JSONObject response_json = null;
//            response_json = new JSONObject(response);
//            boolean isError = response_json.getBoolean("error");
//
//            if(!isError) {
//                JSONArray items = response_json.getJSONArray("Items");
//                item_list = new ArrayList<>();
//
//                Item item = null;
//                for (int i = 0; i < items.length(); i++) {
//                    item = new Item();
//                    item.setId(items.getJSONObject(i).getLong("IdItem"));
//                    item.setName(items.getJSONObject(i).getString("Name"));
//                    item.setDescription(items.getJSONObject(i).getString("Description"));
//                    item.setBarcodeURL(items.getJSONObject(i).getString("Barcode"));
//                    item.setQRcodeURL(items.getJSONObject(i).getString("QRcode"));
//                    item.setImageURL(items.getJSONObject(i).getString("Image"));
//                    item.setIsQR((items.getJSONObject(i).getInt("IsQR") == 1) ? true : false);
//                    item_list.add(item);
//                }
//            }
//        } catch (JSONException e) {
//            item_list = null;
//            e.printStackTrace();
//        }
//        return item_list;
//    }

}
