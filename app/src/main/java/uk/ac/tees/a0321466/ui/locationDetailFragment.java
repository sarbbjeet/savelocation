package uk.ac.tees.a0321466.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import uk.ac.tees.a0321466.ui.home;


public class locationDetailFragment extends Fragment {
    //defined global reference of class
    private nearbyLocationApiHandler apiHandlerClass; //
   // private GlobalClass globalClass; //


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.location_detail_fragment, container, false);
       //initialize classes  //
        apiHandlerClass =new nearbyLocationApiHandler();

        /* Sqlite locationSqlite helper class */
        SqliteHelperClass databasehandler = new SqliteHelperClass(getActivity());

        //Initialize global variables class
        /*
      below written lines for set and get variables/data from GlobalClass when we are
      working with fragments.If you want to set/get in Activities then use
        this way "gVariables=(GlobalClass)getApplicationContext();"
         */
        GlobalClass globalClass = (GlobalClass)getActivity().getApplication();

        //get index value passed by google map fragment / /
        int index = this.getArguments().getInt("index");
        apiHandlerClass.setNearbyApi(globalClass.getNearbyApi());

        //get id of View components
        TextView tv_addr = view.findViewById(R.id.tv_addr);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_Bstatus = view.findViewById(R.id.tv_Bstatus);
        TextView tv_loc = view.findViewById(R.id.tv_loc);
        TextView tv_rating = view.findViewById(R.id.tv_rating);
        TextView tv_openH = view.findViewById(R.id.tv_openH);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        //get values from api handler class
        String name= apiHandlerClass.getName(index);
        String addr= apiHandlerClass.getAddr(index);
        String icon=apiHandlerClass.getIconImg(index);
        Double rating= apiHandlerClass.getRating(index);
        Boolean isOpenNow= apiHandlerClass.isOpenNow(index);
        String businessStatus= apiHandlerClass.getBusinessStatus(index);
        Double lat= apiHandlerClass.getLat(index);
        Double lng= apiHandlerClass.getLng(index);
        //set values to view components
        tv_name.setText(name);
        tv_addr.setText(addr);
        Picasso.with(getActivity()).load(icon).resize(100, 125).into(iv_icon);
        tv_loc.setText(String.valueOf(lat) + ", " + String.valueOf(lng));
        tv_rating.setText(String.valueOf(rating));
        tv_Bstatus.setText(businessStatus);

        if(businessStatus =="OPERATIONAL"){
        tv_openH.setText(String.valueOf(isOpenNow));
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
             try{
                 locationModel location_model=new locationModel(-1,name,addr,String.valueOf(rating),icon);
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
        });



        //String name= apiHandlerClass.getName(index);

//        TextView txt=  view.findViewById(R.id.tv_test);
//        txt.setText(name);
       return view;
    }


}