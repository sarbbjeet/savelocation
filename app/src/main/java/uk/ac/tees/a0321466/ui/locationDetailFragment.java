
/* this fragment used to show detail information about
the location, where a user clicked on the markers
  */

package uk.ac.tees.a0321466.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.SqliteHelperClass;
import uk.ac.tees.a0321466.model.locationModel;
import uk.ac.tees.a0321466.model.nearbyLocationApiHandler;



public class locationDetailFragment extends Fragment {
    SqliteHelperClass databasehandler;
    nearbyLocationApiHandler apiHandlerClass;
    int index;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.location_detail_fragment, container, false);

        //initialize classes //
        apiHandlerClass =new nearbyLocationApiHandler();

        /* Sqlite locationSqlite helper class */
        databasehandler = new SqliteHelperClass(getActivity());

        //Initialize global variables class
        /*
      below written lines for set and get variables/data from GlobalClass when we are
      working with fragments.If you want to set/get in Activities then use
        this way "gVariables=(GlobalClass)getApplicationContext();"
         */
        GlobalClass globalClass = (GlobalClass)getActivity().getApplication();

        //get index value passed by google map fragment / /
        index = this.getArguments().getInt("index");
        apiHandlerClass.setNearbyApi(globalClass.getNearbyApi());

        //get id of View components
        TextView tv_addr = view.findViewById(R.id.tv_addr);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_Bstatus = view.findViewById(R.id.tv_Bstatus);
        TextView tv_loc = view.findViewById(R.id.tv_loc);
        TextView tv_rating = view.findViewById(R.id.tv_rating);
        TextView tv_openH = view.findViewById(R.id.tv_openH);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        //set values to view components
        tv_name.setText(apiHandlerClass.getName(index));
        tv_addr.setText(apiHandlerClass.getAddr(index));
        Picasso.with(getActivity()).load(apiHandlerClass.getIconImg(index))
                .resize(100, 125).into(iv_icon);
        tv_loc.setText(String.valueOf(apiHandlerClass.getLat(index))
                + ", "  + String.valueOf(apiHandlerClass.getLat(index)));
        tv_rating.setText(String.valueOf(apiHandlerClass.getRating(index)));
        tv_Bstatus.setText(apiHandlerClass.getBusinessStatus(index));

        if(apiHandlerClass.getBusinessStatus(index) =="OPERATIONAL"){
        tv_openH.setText(String.valueOf(apiHandlerClass.isOpenNow(index)));
        }

        //back button click listener
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //move back to home fragment
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new home());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        //favorite button press event listener
        /* this button helps to save location details in the sqlite database

         */
        view.findViewById(R.id.btn_fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertLocationToSqlite();

            }
        });


       return view;
    }

    /*
    insert data to sqlite database
     */
    public void insertLocationToSqlite(){
        try{
            locationModel location_model=new locationModel(-1,
                    apiHandlerClass.getName(index), apiHandlerClass.getAddr(index),
                    String.valueOf(apiHandlerClass.getRating(index)),
                      apiHandlerClass.getIconImg(index));
            //return true if data successfully inserted to the database
            if (databasehandler.insertContact(location_model)) {
                Toast.makeText(getActivity(), "Successfully added to favorite list", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Error to add to favorite list", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception err) {
            Toast.makeText(getActivity(), err.toString(), Toast.LENGTH_SHORT).show();
        }


    }


}