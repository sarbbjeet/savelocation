package uk.ac.tees.a0321466.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.currentLocation;
import uk.ac.tees.a0321466.javaClass.getNearByLocationApi;
import uk.ac.tees.a0321466.javaClass.globalVariables;
import uk.ac.tees.a0321466.javaClass.mapPermission;
import uk.ac.tees.a0321466.javaClass.onCustomCallback;
import uk.ac.tees.a0321466.javaClass.volleyResponseListener;
import uk.ac.tees.a0321466.model.nearbyLocationApiHandler;

import static uk.ac.tees.a0321466.javaClass.globalVariables.search_type;

public class home extends Fragment {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
  //  private Location getCurrentLocation;
    //create reference to classes
    private currentLocation getLocation;
    private mapPermission mPermission;
    private globalVariables gVariables;
    private getNearByLocationApi getNearByLocation_api;
    private nearbyLocationApiHandler apiHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize global variables class
        gVariables = new globalVariables();

        //initialize volley library class to get api
        getNearByLocation_api = new getNearByLocationApi(getActivity());
       //api handler model class initialize
        apiHandler = new nearbyLocationApiHandler();

        //Initialize view
        View view=inflater.inflate(R.layout.fragment_home2,container,false);

     //spinner to select nearby location type/////////////////////////////////////////////////////
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
                            String httpUrl=gVariables.nearByLocationUrl(54.568760,-1.240210,search_type[position]);

       /////////////////http call to volley library to get api response////////////////////////////////////////
                            getNearByLocation_api.httpRequest(httpUrl, new volleyResponseListener() {
                                @Override
                                public void onError(VolleyError err) {
                                    Toast.makeText(getActivity(), err.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    apiHandler.setNearbyApi(jsonObject);
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



        ///initailize location provider client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

 //////////////////////MAP Async   ///////////////////////////////////////////////////////////////////////////////////
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                getLocation=new currentLocation(getActivity(),mFusedLocationProviderClient,mMap);
                new mapPermission(getActivity(), new onCustomCallback() {  //hit callback once gps permission enable
                    @Override
                    public void gpsEnableDone() {
                        //display current location on the map once map permission enable
                        getLocation.clickLoc();  //current location getter
//                        Toast.makeText(getActivity(),"gps..",Toast.LENGTH_SHORT).show();
                    }
                });
       //////////////current location event button ///////////////////////////////////////////////
                view.findViewById(R.id.getLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocation.clickLoc();
                    }
                });
    ///////////////////////////////////////////////////////////////////////////////////////

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}