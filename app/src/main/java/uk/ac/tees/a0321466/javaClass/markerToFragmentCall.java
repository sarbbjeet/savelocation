
/* this class help  to perform conditions based selection to move in the Mylocation fragment
and locationDetailActivity. If masker infowindow have "voice" in getSnippet() then move to -->Mylocation fragment with
2 buttons(save and back) and same for others
*/


package uk.ac.tees.a0321466.javaClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.locationDetailActivity;
import uk.ac.tees.a0321466.ui.MyLocation;

public class markerToFragmentCall {
    FragmentManager fm;
    Context context;
    public markerToFragmentCall(Context context, FragmentManager fm) {
        this.context=context;
        this.fm= fm;

    }

    public void fragmentConditions(Marker marker, Place searchPlace, List<Address> voiceLocationArray ){

        if (marker.getTitle().equalsIgnoreCase("My Location")) {
            //Toast.makeText(getActivity(),"my location",Toast.LENGTH_SHORT).show();
            Fragment ff = new MyLocation();
            Bundle bb = new Bundle();
            bb.putString("Navbar","My Location");
            ff.setArguments(bb);
            //FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_host_fragment, ff).addToBackStack("home").commit();
        }
        else if(marker.getSnippet().equalsIgnoreCase("typing")){
            Fragment ff = new MyLocation();
            Bundle bb = new Bundle();
            bb.putString("Navbar","Search Location Details");
            bb.putString("addr", searchPlace.getAddress().toString());
            bb.putString("name", searchPlace.getName().toString());
            bb.putDouble("lat",  searchPlace.getLatLng().latitude);
            bb.putDouble("lng", searchPlace.getLatLng().longitude);
            if(searchPlace.getRating() !=null){
                bb.putDouble("rating", searchPlace.getRating());
            }
            else{
                bb.putDouble("rating", 0.0);
            }

            ff.setArguments(bb);
            //FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_host_fragment, ff).addToBackStack("home").commit();
        }
        else if(marker.getSnippet().equalsIgnoreCase("voice")){
            Fragment ff = new MyLocation();
            Bundle bb = new Bundle();
            String addr = voiceLocationArray.get(0).getAddressLine(0);
            bb.putString("Navbar","Search Location Details");
            bb.putString("addr", addr.toString());
            bb.putString("name", voiceLocationArray.get(0).getFeatureName().toString());
            bb.putDouble("lat",   voiceLocationArray.get(0).getLatitude());
            bb.putDouble("lng", voiceLocationArray.get(0).getLongitude());
            bb.putDouble("rating", 0.0);  //default

            ff.setArguments(bb);
            //FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_host_fragment, ff).addToBackStack("home").commit();
        }


        else {
            Intent intent = new Intent(context.getApplicationContext(), locationDetailActivity.class);
            intent.putExtra("index", Integer.valueOf(marker.getSnippet()));
            context.startActivity(intent);
        }


    }


}