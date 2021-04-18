package uk.ac.tees.a0321466.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.currentLocation;
import uk.ac.tees.a0321466.javaClass.getNearByLocationApi;
import uk.ac.tees.a0321466.javaClass.volleyResponseListener;
import uk.ac.tees.a0321466.model.nearbyLocationModel;

import static uk.ac.tees.a0321466.javaClass.GlobalClass.search_type;


public class SearchLocation extends Fragment {
    private GlobalClass gVariables; //user can access data from any activity
    private getNearByLocationApi getNearByLocation_api;
    private nearbyLocationModel apiHandler;
    private currentLocation getLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_location, container, false);
        setHasOptionsMenu(true);


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






        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.custom_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.items_dropDown);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, search_type);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(searchItem); // get the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                int position = spinner.getSelectedItemPosition();
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




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        AutoCompleteTextView searchView = (AutoCompleteTextView) searchItem.getActionView();
//        searchView.setOnEditorActionListener((textView, action_id, keyEvent) ->{
//            Toast.makeText(getActivity(),textView.toString(),Toast.LENGTH_LONG).show();
//            if (action_id== EditorInfo.IME_ACTION_SEARCH
//                || action_id == EditorInfo.IME_ACTION_DONE
//                || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                || keyEvent.getAction()==KeyEvent.KEYCODE_ENTER){
//
//              //  Toast.makeText(getActivity(),textView.toString(),Toast.LENGTH_LONG).show();
//                //execute method to search
//            }
//          return false;
//         });

//        AutoCompleteTextView searchView = (AutoCompleteTextView) searchItem.getActionView();

//        searchView.setListSelection(new AutoCompleteTextView.);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        change button on the keyboard with enter search text
        //        searchView.setImeOptions(EditorInfo.IME_ACTION_GO);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                //do something
//                return false;
//            }
//        });


  super.onCreateOptionsMenu(menu, inflater);
    }
}