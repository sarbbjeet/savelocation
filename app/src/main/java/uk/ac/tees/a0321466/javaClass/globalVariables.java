package uk.ac.tees.a0321466.javaClass;

import com.google.android.gms.maps.model.LatLng;

public class globalVariables {
    //all global parameter defined here //
    public static final float DEFAULT_ZOOM = 14;  //map zoom value
    public static final int REQUEST_CODE=2222;
//    public static final String basicUrl="https://api.openchargemap.io/v3/poi/?output=json&key=";
//    public static final String openCarChargerKey="0e57a4f7-496c-4468-9b16-91fa9a068f47";

    ///public static final String basicUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=54.568760,-1.240210&radius=5000&type=map&sensor=true&key=";
    //public static final String openCarChargerKey="AIzaSyDLJ5AoArpYWUeb3q84P0OftnHk5yZR788";

    public static  final int VOLLEY_TIMEOUT=10000;  //10s timeout set of volley library response time

    public LatLng currentLatlng;
    public static final String[] search_type = {
            "None",
            "gym",
            "hospital",
            "hotal",
            "bank",
            "post_office",
            "school",
            "university",
            "train_station",
            "airport"
    };

    public globalVariables() {
    }

    public String nearByLocationUrl(double lat, double lng, String searchType){
        String _lat= String.valueOf(lat); ///convert double to string
        String _lng= String.valueOf(lng); ///convert double to string
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + _lat + "," + _lng
                + "&radius=5000&type=" + searchType + "&sensor=true&key=AIzaSyDLJ5AoArpYWUeb3q84P0OftnHk5yZR788";

        return url;
    }
    public LatLng getCurrentLatlng() {
        return currentLatlng;
    }

    public void setCurrentLatlng(LatLng currentLatlng) {
        this.currentLatlng = currentLatlng;
    }
}

