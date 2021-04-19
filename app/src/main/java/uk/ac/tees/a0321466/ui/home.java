package uk.ac.tees.a0321466.ui;


import android.content.ClipData;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.SectionStatePageAdapter;
import uk.ac.tees.a0321466.javaClass.currentLocation;
import uk.ac.tees.a0321466.javaClass.getNearByLocationApi;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.mapPermission;
import uk.ac.tees.a0321466.javaClass.markerToFragmentCall;
import uk.ac.tees.a0321466.javaClass.onCustomCallback;
import uk.ac.tees.a0321466.javaClass.textToLocationConverter_Geo;
import uk.ac.tees.a0321466.javaClass.volleyResponseListener;
import uk.ac.tees.a0321466.locationDetailActivity;
import uk.ac.tees.a0321466.model.nearbyLocationModel;

import static android.app.Activity.RESULT_OK;
import static uk.ac.tees.a0321466.javaClass.GlobalClass.openKey;
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
    TextView tv_search;
    RelativeLayout spinnerLayout;  //to handle dropdown nearby services list
    RelativeLayout searchLayout;  //to handle search location
    Place searchPlace;
    List<Address> voiceLocationArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        setHasOptionsMenu(true); //toolbar menu visable


        spinnerLayout=view.findViewById(R.id.nearby_spinner_layout);
        searchLayout=view.findViewById(R.id.search_layout);


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

        //add spinner logic to drop down nearby location services
        spinnerListner(view);
        //add logic for location search by typing
        locationSearchByTyping(view);


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
                        /* below function contain fragment based conditions
                        and it pass data to mylocation fragment and nearby
                         */
                        new markerToFragmentCall(getActivity(),getFragmentManager())
                         .fragmentConditions(marker,searchPlace,voiceLocationArray); //pass arrays
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
                //set toolbar title //
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search Location");
                searchLayout.setVisibility(RelativeLayout.VISIBLE); //visible now //layout visible
                spinnerLayout.setVisibility(RelativeLayout.GONE); //Invisible

                return true;
            case R.id.items_dropDown:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select Nearby Service");
                searchLayout.setVisibility(RelativeLayout.GONE); //INvisible now
                spinnerLayout.setVisibility(RelativeLayout.VISIBLE); //Visible

                return true;
            case R.id.items_mic:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Speak for search");
                spinnerLayout.setVisibility(RelativeLayout.GONE); //INvisible now
                searchLayout.setVisibility(RelativeLayout.GONE); //Invisible
                openVoiceDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }





    /* function to handle spinner which havig nearby location services,
    main task of spinner to get user input and send to the google map server, after that create the marker
    with getting api resonse from google api server
     */

    public void spinnerListner(View view) {
        //spinner to select nearby location type/////////////////////////////////////////////////////
        Spinner spinner = view.findViewById(R.id.nearby_location_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, search_type);

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


    /* logic for autocomplete search "Location search by typing"

     */

    private void locationSearchByTyping(View view){

        Places.initialize(getActivity(),openKey);
        tv_search=view.findViewById(R.id.tv_searchLocation);
        //et_search.setFocusable(false);

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place field list
                List<Place.Field> placeList = Arrays.asList(Place.Field.ADDRESS
                        ,Place.Field.LAT_LNG,Place.Field.NAME);
                //intent create

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        ,placeList).build(getActivity());

                //start activity
                startActivityForResult(intent,100);

            }
        });


    }

    /* open google assistant */

    private void openVoiceDialog(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent,200);

    }



    /* wait for all places results, this method will run when all places data
    loaded after that we can search location which we want by typing on the google
    search location
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/* code for google search location */
    if (requestCode==100 && resultCode == RESULT_OK){
        //success
        searchPlace = Autocomplete.getPlaceFromIntent(data);  //Place

        tv_search.setText(String.format(" %s", searchPlace.getName()));
        getLocation.setSearchLocationMapMarker(searchPlace.getLatLng(),searchPlace.getName(),"typing");  //set marker
//       Toast.makeText(getActivity(), String.valueOf(searchPlace.getLatLng()),Toast.LENGTH_SHORT).show();

    }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
        //if error
        //status Initialize
        Status status= Autocomplete.getStatusFromIntent(data);
        Toast.makeText(getActivity(),status.getStatusMessage().toString(),Toast.LENGTH_SHORT).show();

    }
    //////////////////////////////////////////////////////////
        /* code for google voice assistant */
        if (requestCode==200 && resultCode == RESULT_OK){
            ArrayList<String> list=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voice = list.get(0);


            if(voice.equalsIgnoreCase("open camera")){
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(camera);
            }
            else if(!voice.equalsIgnoreCase("")){
               voiceLocationArray=new textToLocationConverter_Geo(getActivity()).geoLocate(voice);
                if(voiceLocationArray.size()>0){
                    Log.d(
                            "t2",voiceLocationArray.get(0).toString()
                    );
                    double lat1 = voiceLocationArray.get(0).getLatitude();
                    double lng1 = voiceLocationArray.get(0).getLongitude();
                    String palceName= voiceLocationArray.get(0).getFeatureName();
                    getLocation.setSearchLocationMapMarker(new LatLng(lat1,lng1),palceName,"voice");  //set marker
                }
                else{
                    splashMsg("Sorry enable to find Address!!");
                }
            }
        }
        else{
         //  splashMsg("google assistant error !!");
        }

    }

    /* method to print toast msg

    */
    private void splashMsg(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}