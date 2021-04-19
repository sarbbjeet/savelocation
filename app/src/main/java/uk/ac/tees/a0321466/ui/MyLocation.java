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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.SqliteHelperClass;
import uk.ac.tees.a0321466.javaClass.mapPermission;
import uk.ac.tees.a0321466.javaClass.onCustomCallback;
import uk.ac.tees.a0321466.model.locationModel;
import uk.ac.tees.a0321466.model.nearbyLocationModel;


public class MyLocation extends Fragment {
    mapPermission mp;
    FusedLocationProviderClient mFusedLocationProviderClient;
    GlobalClass gVariables;
    TextView tv_addr,tv_city, tv_postcode,tv_country,tv_lat ,tv_lng;
    String nav_name="";
    String name="";
    String addr="";
    Double lat =0.0;
    Double lng =0.0;
    Double rating = 0.0;

    LinearLayout saveBackBtnLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_my_location, container, false);
        saveBackBtnLayout = view.findViewById(R.id.btns_group1);

        /* view component id  get
         */
        tv_addr=view.findViewById(R.id.myAddr);
        tv_city=view.findViewById(R.id.myCity);
        tv_postcode=view.findViewById(R.id.myPostcode);
        tv_country=view.findViewById(R.id.myCountry);
        tv_lat=view.findViewById(R.id.myLat);
        tv_lng=view.findViewById(R.id.myLng);
        /* data from parent fragment */


        if(getArguments() !=null) {
            addr = getArguments().getString("addr");
            lat = getArguments().getDouble("lat");
            lng = getArguments().getDouble("lng");
            nav_name = getArguments().getString("Navbar");
            name = getArguments().getString("name");
            rating = getArguments().getDouble("rating");

        }
        if(!nav_name.equalsIgnoreCase("")){
            //set toolbar title /
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(nav_name);

        }
        else{
            //set toolbar default title  /
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Location");
        }
        /////////////////////////////////////////////
        gVariables =(GlobalClass)getActivity().getApplication();
        mp= new mapPermission(getActivity(), new onCustomCallback() {
            @Override
            public void gpsEnableDone() {
                if(gVariables.getGcurrentLocation() !=null){
                    if(!nav_name.equalsIgnoreCase("My Location")
                            && !nav_name.equalsIgnoreCase("") ){
                        setSearchLocationInfo(addr,lat,lng);
                        //Toast.makeText(getActivity(),addr.toString(),Toast.LENGTH_LONG).show();
                        saveBackBtnLayout.setVisibility(saveBackBtnLayout.VISIBLE); //save and back button visible
                    }
                    else {

                        setMyLocationInfo(gVariables.getGcurrentLocation());
                    }
                }

            }
        });



        /* back button event listner */
        view.findViewById(R.id.btn_back_myLoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });



        /* Save button event listner */
        view.findViewById(R.id.btn_fav_myLoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (insertLocationToSqlite()) {
                    toastMess("Successfully Location Added");
                   // getActivity().onBackPressed();  //go back
                } else {
                    toastMess("Error To Add Location!");
                }

            }
        });

        return view ;
    }


    private boolean  insertLocationToSqlite(){
        try{
            locationModel location_model=new locationModel(-1,
                    name,  addr,
                    String.valueOf( rating),
                    "");
            //return true if data successfully inserted to the database
            if (new SqliteHelperClass(getActivity()).insertContact(location_model)) { return true; }
            else { return false;  }
        } catch (Exception err) { return false; }

    }


    /* function to get name of location with the help of latitiude and longitude value
     */
    private void setMyLocationInfo(Location lastKnownLocation) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            /* print above data to my location fragment
             */
            List l  = Arrays.asList(address.split(","));
            List k  = Arrays.asList(String.valueOf(l.get(1)).split(" "));
            String Street = String.valueOf(l.get(0));
            String City = String.valueOf(k.get(1));
            tv_addr.setText(Street);
            tv_city.setText(City);
            tv_postcode.setText(postalCode);
            tv_country.setText(country);
            tv_lat.setText(String.valueOf(lastKnownLocation.getLatitude()));
            tv_lat.setText(String.valueOf(lastKnownLocation.getLongitude()));



            //Toast.makeText(getActivity(), address.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setSearchLocationInfo(String addr, double lat, double lng){
        try{
            String city,street,postalCode,country;
            List<String> addrArr = Arrays.asList(addr.split(","));
            if(addrArr.size()>=4){
                city=String.valueOf(addrArr.get(1));
                street=String.valueOf(addrArr.get(0));
                postalCode=String.valueOf(addrArr.get(2));;
                country=String.valueOf(addrArr.get(3));
            }
            else {
                city=String.valueOf(addrArr.get(1));
                street=String.valueOf(addrArr.get(0));
                postalCode=String.valueOf(addrArr.get(3));;
                country=String.valueOf(addrArr.get(4));
            }

            tv_addr.setText(street);
            tv_city.setText(city);
            tv_postcode.setText(postalCode);
            tv_country.setText(country);
            tv_lat.setText(String.valueOf(lat));
            tv_lat.setText(String.valueOf(lng));


        }catch (Exception io){


        }


    }



    /* Toast msg display function */
    private void toastMess(String msg){

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

    }

}