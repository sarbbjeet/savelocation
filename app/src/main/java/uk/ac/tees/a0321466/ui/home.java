package uk.ac.tees.a0321466.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.currentLocation;
import uk.ac.tees.a0321466.javaClass.globalVariables;
import uk.ac.tees.a0321466.javaClass.mapPermission;
import uk.ac.tees.a0321466.javaClass.onCustomCallback;

import static uk.ac.tees.a0321466.javaClass.globalVariables.search_type;

public class home extends Fragment {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
  //  private Location getCurrentLocation;
    //create reference to classes
    private currentLocation getLocation;
    mapPermission mPermission;
    globalVariables gVariables;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize global variables class
        gVariables = new globalVariables();
        //Initialize view
        View view=inflater.inflate(R.layout.fragment_home2,container,false);

     //spinner to select nearby location type//////////////////////
       Spinner spnr = view.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, search_type);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr.getSelectedItemPosition();
                        if(search_type[position] !="None") {
                            Toast.makeText(getActivity(), gVariables.nearByLocationUrl(23.5688, -1.4575, search_type[position]), Toast.LENGTH_LONG).show();
                        }
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                    //map fragment initilize
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);



        ///initailize location provider client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //map Async
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                getLocation=new currentLocation(getActivity(),mFusedLocationProviderClient,mMap);
                new mapPermission(getActivity(), new onCustomCallback() {  //hit callback once gps permission enable
                    @Override
                    public void gpsEnableDone() {
                        //display current location on the map once map permission enable
                        getLocation.clickLoc();
//                        Toast.makeText(getActivity(),"gps..",Toast.LENGTH_SHORT).show();
                    }
                });
                        //ask for gps permission

                view.findViewById(R.id.getLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocation.clickLoc();
                    }
                });


                //when map is loaded
                //firstly check, Is gps permissions are enable or not
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
//                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    //give gps permission
//                    new mapPermission(getActivity());
//                    //return;
//                }
//                getLocation = new currentLocation(getActivity(), mFusedLocationProviderClient, mMap);

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}