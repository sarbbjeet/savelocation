package uk.ac.tees.a0321466.javaClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import uk.ac.tees.a0321466.R;

import static uk.ac.tees.a0321466.javaClass.globalVariables.DEFAULT_ZOOM;
public class currentLocation extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location getCurrentLocation;
    GoogleMap mMap;
    Context mActivity;
    globalVariables gVariables;  //global variable class

    public currentLocation(Context _mActivity,FusedLocationProviderClient _mFusedLocationProviderClient, GoogleMap _map) {
        mActivity = _mActivity;
        mFusedLocationProviderClient = _mFusedLocationProviderClient;
        mMap = _map;
        gVariables = new globalVariables(); // initilize global variable class
    }
    public void clickLoc(){

        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener((Activity) mActivity, new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        getCurrentLocation = task.getResult();
                        if (getCurrentLocation != null) {
                            //  LatLng defaultLocation= new LatLng(54.5742982466006, -1.2349123090100282);
                            // LatLng latLng = new LatLng(54.5742982466006, -1.2349123090100282);
                            LatLng current_latLng= new LatLng(getCurrentLocation.getLatitude(),getCurrentLocation.getLongitude());
                            gVariables.setCurrentLatlng(current_latLng);  //set current lat and lng to global variable class
                            //  mMap.addMarker(new MarkerOptions().position(latLng).title("electric car house").icon(BitmapDescriptorFactory.fromResource(R.drawable.electric_img)));
                            mMap.addMarker(new MarkerOptions().position(current_latLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mycar)));
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(current_latLng,DEFAULT_ZOOM);
                            mMap.animateCamera(location);

                        }
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /* This method used to create map marker according to receive api lat/lng array value
    and display station name and connection type
     */
    public void setMapMarkers(ArrayList<LatLng> latLngs, ArrayList<String>stationName, ArrayList<String>connectionType){
        for(int i=0;i<latLngs.size();i++){
            mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).snippet(connectionType.get(i)).title(
                    stationName.get(i)).icon(BitmapFromVector(mActivity, R.drawable.map_marker_icon)));

            //mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).snippet(connectionType.get(i)).title(
            //      stationName.get(i)).icon(BitmapFromVector(mActivity, R.drawable.map_marker_icon)));


        }


    }
    //map focus towards current location
    public void pointBackCurrentLocation(){
        //LatLng defaultLocation= new LatLng(getCurrentLocation.getLatitude(),getCurrentLocation.getLongitude());
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(gVariables.getCurrentLatlng(),DEFAULT_ZOOM);
        mMap.animateCamera(location);
    }



    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);


        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}



