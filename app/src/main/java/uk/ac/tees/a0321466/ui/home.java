package uk.ac.tees.a0321466.ui;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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


//        Intent intent = new Intent(home.this.getContext(), locationDetailActivity.class);
//////        //intent.putExtra("index",Integer.valueOf(marker.getSnippet()));
//        startActivity(intent);
        getLocation=new currentLocation(getActivity());  //initialize currentLocation class to get location and set marker
        //Initialize global variables class
        /*
      below written lines for set and get variables/data from GlobalClass when we are
      working with fragments.If you want to set/get in normal Activity then use
        this way "gVariables=(GlobalClass)getApplicationContext();"
         */
         gVariables = (GlobalClass)getActivity().getApplication();

        //initialize volley library class to get api
        getNearByLocation_api = new getNearByLocationApi(getActivity());
       //api handler model class initialize
        apiHandler = new nearbyLocationModel();

        //Initialize view
        View view=inflater.inflate(R.layout.fragment_home2,container,false);


        //spinner to select nearby location type/////////////////////////////////////////////////////
       Spinner spnr = view.findViewById(R.id.nearby_location_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, search_type);
        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr.getSelectedItemPosition();
                        //perform search when user select a service from given list
                        if(!search_type[position].equalsIgnoreCase("Choose a Service")) {

                            String httpUrl=gVariables.nearByLocationUrl(search_type[position]);

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
                                    getLocation.setMapMarkers(apiHandler.getlatLngs(),apiHandler.getNames());
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //map fragment initilize
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

 //////////////////////MAP Async   ///////////////////////////////////////////////////////////////////////////////////
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                getLocation.mapReference(mMap); //pass google map reference
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if(marker.getTitle().equalsIgnoreCase("My Location")){
                            //Toast.makeText(getActivity(),"my location",Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.nav_host_fragment,new MyLocation()).commit();
                        }
                        else{
                            Intent intent = new Intent(getActivity(), locationDetailActivity.class);
                            intent.putExtra("index",Integer.valueOf(marker.getSnippet()));
                            startActivity(intent);

                        }

                    }
                });



                ////////map permission class // ////
                new mapPermission(getActivity(), new onCustomCallback() {  //hit callback once gps permission enable
                    @Override
                    public void gpsEnableDone() {
                        //display current location on the map once map permission enable
                        getLocation.pointBackCurrentLocation();  //current location getter
                    }
                });
       //////////////get current location event button ///////////////////////////////////////////////
                view.findViewById(R.id.getLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocation.pointBackCurrentLocation();
//                        Fragment ff= new displayClickedLocation();
//                        FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.nav_host_fragment, ff);
////                        //fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();

                    }
                });

            }
        });

        // Inflate the layout for this fragment
        return view;
    }


}