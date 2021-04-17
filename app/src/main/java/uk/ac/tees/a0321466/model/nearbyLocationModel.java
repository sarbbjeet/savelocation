package uk.ac.tees.a0321466.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class nearbyLocationModel {
    private JSONObject nearbyApi =new JSONObject();
    private double lat,lng;
    private String name,iconImg, addr,businessStatus;
    boolean isOpenNow,permanentlyClosed;
    double rating;
    private ArrayList<LatLng> latLng = new ArrayList<LatLng>();
    private ArrayList<String> names= new ArrayList<String>();
    public nearbyLocationModel() {
    }


    public JSONObject getNearbyApi() {
        return nearbyApi;
    }

    public void setNearbyApi(JSONObject nearbyApi1) {
        nearbyApi = nearbyApi1;
    }



    //get name of all locations  //
    public ArrayList<String> getNames() {
        names.clear();
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            for(int i=0;i<res.length();i++){
                JSONObject element = (JSONObject) res.get(i);
                name = element.getString("name");
                names.add(i,name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return names;
    }

    //get all locations latitude and longitude .////
   public ArrayList<LatLng> getlatLngs(){
        latLng.clear(); //clear array list
       try {
           JSONArray res = nearbyApi.getJSONArray("results");
           for(int i=0;i<res.length();i++){
               JSONObject element = (JSONObject) res.get(i);
               JSONObject geo= (JSONObject) element.getJSONObject("geometry");
               JSONObject location = (JSONObject) geo.getJSONObject("location");
               lat = location.getDouble("lat");
               lng = location.getDouble("lng");
               latLng.add(i,new LatLng(lat,lng));
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }

        return latLng;
   }

    public double getLat(int itemIndex) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            JSONObject geo= (JSONObject) element.getJSONObject("geometry");
            JSONObject location = (JSONObject) geo.getJSONObject("location");
            lat = location.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return lat;
    }

    public double getLng(int itemIndex) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            JSONObject geo= (JSONObject) element.getJSONObject("geometry");
            JSONObject location = (JSONObject) geo.getJSONObject("location");
            lng = location.getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lng;
    }

    public String getName(int itemIndex) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            name = element.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getIconImg(int itemIndex) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            iconImg = element.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return iconImg;
    }

    public String getAddr(int itemIndex) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            addr = element.getString("vicinity");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addr;
    }

    public String getBusinessStatus(int itemIndex) {  //OPERATIONAL  //CLOSED_TEMPORARILY
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            businessStatus = element.getString("business_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return businessStatus;
    }

    public boolean isOpenNow(int itemIndex) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            JSONObject openingHour= (JSONObject) element.getJSONObject("opening_hours");
            isOpenNow = openingHour.getBoolean("open_now");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isOpenNow;
    }

    public boolean isPermanentlyClosed(int itemIndex) {

        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(itemIndex);
            permanentlyClosed =  element.getBoolean("permanently_closed");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return permanentlyClosed;
    }

    public double getRating(int index) {
        try {
            JSONArray res = nearbyApi.getJSONArray("results");
            JSONObject element = (JSONObject) res.get(index);
            rating =  element.getDouble("rating");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rating;
    }




}
