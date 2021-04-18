package uk.ac.tees.a0321466.ui;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.mapPermission;
import uk.ac.tees.a0321466.javaClass.onCustomCallback;


public class MyLocation extends Fragment {
    mapPermission mp;
    FusedLocationProviderClient mFusedLocationProviderClient;
    GlobalClass gVariables;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         //set toolbar title /
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Location");



        View view= inflater.inflate(R.layout.fragment_my_location, container, false);
        gVariables =(GlobalClass)getActivity().getApplication();
        mp= new mapPermission(getActivity(), new onCustomCallback() {
            @Override
            public void gpsEnableDone() {
                if(gVariables.getGcurrentLocation() !=null){
                    getMyLocationName(gVariables.getGcurrentLocation());

                }

            }
        });

    return view ;
    }




/* function to get name of location with the help of latitiude and longitude value

 */
    private void getMyLocationName(Location lastKnownLocation) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();


            Toast.makeText(getActivity(), address.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}