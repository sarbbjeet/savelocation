
/*
I am using this class to get current device location and
create markers according to lat/lng values which are received from google nearby location
api. After creating the marker this class have methods to open new fragment to display clicked
marker location complete details.
 */

package uk.ac.tees.a0321466.javaClass;
import android.app.Activity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.ui.locationDetailFragment;

import static uk.ac.tees.a0321466.javaClass.GlobalClass.DEFAULT_ZOOM;
import static uk.ac.tees.a0321466.javaClass.GlobalClass.default_LatLng;
public class currentLocation extends Fragment {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location getCurrentLocation;
    GoogleMap mMap;
    Context mActivity;
    FragmentManager fm;
    LatLng current_latLng =new LatLng(0.0,0.0); //latitude & longitude of current location

    GlobalClass globalClass;


    //constructor of currentLocation class and below parameters are passing by called class
    public currentLocation(uk.ac.tees.a0321466.ui.home home, Context _mActivity, FusedLocationProviderClient _mFusedLocationProviderClient, GoogleMap _map) {
        mActivity = _mActivity;
        mFusedLocationProviderClient = _mFusedLocationProviderClient;
        mMap = _map;
        fm= home.getFragmentManager();  //connect getFragmentManager with home.this instance

        globalClass = (GlobalClass)home.getActivity().getApplication();  //access Global Class method and varibles

        ////////////////////google map marker infoWindow click listener ////////////////////////
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //code used to call new fragment "displayClickLocation"
                //this new fragment show details of map marker location point where user click
                Fragment ff= new locationDetailFragment();  //location details fragment
                Bundle bundle = new Bundle();
                bundle.putInt("index",Integer.valueOf(marker.getSnippet())); //pass "index of clicked marker location" to the new fragment
                ff.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, ff);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //Toast.makeText(mActivity,marker.getSnippet(), Toast.LENGTH_LONG).show();
            }

        });
    }


/*
function to create markers on the google map
 */
    public void setMapMarkers(ArrayList<LatLng> latLngs, ArrayList<String> names) {
        mMap.clear();  //remove set markers from the google map

        //set again current location marker
        mMap.addMarker(new MarkerOptions().position(current_latLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mycar)));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom((current_latLng), DEFAULT_ZOOM);
        mMap.animateCamera(location);

        for (int i = 0; i < latLngs.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title(
                    names.get(i)).snippet(String.valueOf(i)).icon(BitmapFromVector(mActivity, R.drawable.map_marker_icon)));


//            Picasso.with(mActivity).load(url).resize(84, 125).into(target);
            //mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).snippet(connectionType.get(i)).title(
            //      stationName.get(i)).icon(BitmapFromVector(mActivity, R.drawable.map_marker_icon)));

        }
    }

    //map focus towards current location and get current location latitude and longitude
    public void pointBackCurrentLocation() {
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener((Activity) mActivity, new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        getCurrentLocation = task.getResult();
                        if (getCurrentLocation != null ) {
                            globalClass.setGcurrentLocation(getCurrentLocation);  //pass current location to globalclass

                            if(current_latLng.latitude != getCurrentLocation.getLatitude() && current_latLng.longitude != getCurrentLocation.getLongitude()){
                                mMap.clear(); //clear previous markers
                                current_latLng = new LatLng(getCurrentLocation.getLatitude(), getCurrentLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(current_latLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mycar)));

                            }

                        } else {
                            //Toast.makeText(getActivity(),"gps not here", Toast.LENGTH_SHORT).show();
                            mMap.clear(); //clear previous markers
                            current_latLng = default_LatLng;  //set current location default
                            mMap.addMarker(new MarkerOptions().position(current_latLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mycar)));
                        }

                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(current_latLng,DEFAULT_ZOOM);
                        mMap.animateCamera(location);
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }


/* method to convert image/ according to google marker acceptable .

 */
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