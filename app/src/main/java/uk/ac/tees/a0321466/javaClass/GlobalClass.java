package uk.ac.tees.a0321466.javaClass;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

public class GlobalClass extends Application {
    //all global parameter defined here //
    public static final float DEFAULT_ZOOM = 14;  //map zoom value
    public static final int REQUEST_CODE = 2222;
//    public static final String basicUrl="https://api.openchargemap.io/v3/poi/?output=json&key=";
//    public static final String openCarChargerKey="0e57a4f7-496c-4468-9b16-91fa9a068f47";

    ///public static final String basicUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=54.568760,-1.240210&radius=5000&type=map&sensor=true&key=";
    //public static final String openCarChargerKey="AIzaSyDLJ5AoArpYWUeb3q84P0OftnHk5yZR788";

    public static final int VOLLEY_TIMEOUT = 10000;  //10s timeout set of volley library response time

    //default latitude and longitude of "Tennyson street,Middlebrough"
    public static final LatLng default_LatLng = new LatLng(54.568760, -1.240210);

    public Location gcurrentLocation; //set and get current location globaly / /
    public JSONObject nearbyApi;
    //Below array is used to create spinner list, which help to search different nearby locations
    public static final String[] search_type = {
            "Choose a Service",
            "gym",
            "hospital",
            "hotal",
            "bank",
            "post_office",
            "school",
            "university",
            "police",
            "restaurant",
            "train_station",
            "airport"
    };

    public GlobalClass() {
    }


    public Location getGcurrentLocation() {
        return gcurrentLocation;
    }

    public void setGcurrentLocation(Location gcurrentLocation) {
        this.gcurrentLocation = gcurrentLocation;
    }

    //create url link
    public String nearByLocationUrl(String searchType) {
        double lat,lng;

        if(getGcurrentLocation() !=null){
            lat=getGcurrentLocation().getLatitude();
            lng=getGcurrentLocation().getLongitude();
        }
        else{
            lat=default_LatLng.latitude;
            lng=default_LatLng.longitude;
        }
        String _lat = String.valueOf(lat); ///convert double to string
        String _lng = String.valueOf(lng); ///convert double to string
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + _lat + "," + _lng
                + "&radius=5000&type=" + searchType + "&sensor=true&key=AIzaSyDLJ5AoArpYWUeb3q84P0OftnHk5yZR788";

        return url;
    }

    public JSONObject getNearbyApi() {
        return nearbyApi;
    }

    public void setNearbyApi(JSONObject nearbyApi) {
        this.nearbyApi = nearbyApi;
    }


}

