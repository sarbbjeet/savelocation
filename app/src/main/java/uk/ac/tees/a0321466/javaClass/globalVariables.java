package uk.ac.tees.a0321466.javaClass;

import com.google.android.gms.maps.model.LatLng;

class globalVariables {
    //all global parameter defined here //
    public static final float DEFAULT_ZOOM = 14;  //map zoom value
    public static final int REQUEST_CODE=2222;
    public static final String basicUrl="https://api.openchargemap.io/v3/poi/?output=json&key=";
    public static final String openCarChargerKey="0e57a4f7-496c-4468-9b16-91fa9a068f47";
    public static  final int VOLLEY_TIMEOUT=10000;  //10s timeout set of volley library response time

    public LatLng currentLatlng;

    public globalVariables() {
    }

    public LatLng getCurrentLatlng() {
        return currentLatlng;
    }

    public void setCurrentLatlng(LatLng currentLatlng) {
        this.currentLatlng = currentLatlng;
    }
}

