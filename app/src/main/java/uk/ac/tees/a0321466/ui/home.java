package uk.ac.tees.a0321466.ui;


import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.SectionStatePageAdapter;
import uk.ac.tees.a0321466.javaClass.currentLocation;
import uk.ac.tees.a0321466.javaClass.getNearByLocationApi;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.mapPermission;
import uk.ac.tees.a0321466.javaClass.onCustomCallback;
import uk.ac.tees.a0321466.javaClass.volleyResponseListener;
import uk.ac.tees.a0321466.locationDetailActivity;
import uk.ac.tees.a0321466.model.nearbyLocationModel;

import static uk.ac.tees.a0321466.javaClass.GlobalClass.search_type;

public class home extends Fragment {
    //create reference to classes
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private currentLocation getLocation;
    private mapPermission mPermission;
    private GlobalClass gVariables; //user can access data from any activity
    private getNearByLocationApi getNearByLocation_api;
    private nearbyLocationModel apiHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        setHasOptionsMenu(true);


        getLocation = new currentLocation(getActivity());  //initialize currentLocation class to get location and set marker
        //Initialize global variables class
        /*
      below written lines for set and get variables/data from GlobalClass when we are
      working with fragments.If you want to set/get in normal Activity then use
        this way "gVariables=(GlobalClass)getApplicationContext();"
         */
        gVariables = (GlobalClass) getActivity().getApplication();

        //initialize volley library class to get api
        getNearByLocation_api = new getNearByLocationApi(getActivity());
        //api handler model class initialize
        apiHandler = new nearbyLocationModel();


        ////////map permission class // ////
        new mapPermission(getActivity(), new onCustomCallback() {  //hit callback once gps permission enable
            @Override
            public void gpsEnableDone() {
                //display current location on the map once map permission enable
                getLocation.pointBackCurrentLocation();  //current location getter
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //map fragment initilize
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //////////////////////MAP Async   ///////////////////////////////////////////////////////////////////////////////////
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                getLocation.mapReference(mMap); //pass google map reference
                /* location infoWindow event listener */
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (marker.getTitle().equalsIgnoreCase("My Location")) {
                            //Toast.makeText(getActivity(),"my location",Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.nav_host_fragment, new MyLocation()).commit();
                        } else {
                            Intent intent = new Intent(getActivity(), locationDetailActivity.class);
                            intent.putExtra("index", Integer.valueOf(marker.getSnippet()));
                            startActivity(intent);
                        }
                    }
                });

                //////////////get current location event button ///////////////////////////////////////////////
                view.findViewById(R.id.getLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocation.pointBackCurrentLocation();
                    }
                });

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /* menu created on the toolbar listener
       */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.custom_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location_search:
                //set toolbar title /
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search..");

                return true;
            case R.id.items_dropDown:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select Nearby Service");

                return true;
            case R.id.items_mic:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Speak for search");

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }




    /* function to handle spinner which havig nearby location services,
    main task of spinner to get user input and send to the google map server, after that create the marker
    with getting api resonse from google api server
     */

    public void spinnerListner(MenuItem item) {
        //spinner to select nearby location type/////////////////////////////////////////////////////
       // MenuItem searchItem = menu.findItem(R.id.items_dropDown);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, search_type);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item); // get the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner.getSelectedItemPosition();
                        //perform search when user select a service from given list
                        if (!search_type[position].equalsIgnoreCase("Choose a Service")) {

                            String httpUrl = gVariables.nearByLocationUrl(search_type[position]);

                            /////////////////http call to volley library to get api response////////////////////////////////////////
                            getNearByLocation_api.httpRequest(httpUrl, new volleyResponseListener() {
                                @Override
                                public void onError(VolleyError err) {
                                    Toast.makeText(getActivity(), err.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    /* pass recieved data to Global variable
                                    class so we can access this data from any activity */
                                    gVariables.setNearbyApi(jsonObject);
                                    apiHandler.setNearbyApi(jsonObject);
                                    //send lat/lng and  name of locations to create map marker
                                    getLocation.setMapMarkers(apiHandler.getlatLngs(), apiHandler.getNames());
                                    // Toast.makeText(getActivity(), String.valueOf(apiHandler.getlatLngs()) , Toast.LENGTH_LONG).show();

                                }
                            });
                            //  Toast.makeText(getActivity(), gVariables.nearByLocationUrl(23.5688, -1.4575, search_type[position]), Toast.LENGTH_LONG).show();
                        }
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
    }
}