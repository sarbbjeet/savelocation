package uk.ac.tees.a0321466;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
//import androidx.appcompat.widget.Toolbar;
import com.squareup.picasso.Picasso;
import uk.ac.tees.a0321466.javaClass.GlobalClass;
import uk.ac.tees.a0321466.javaClass.SqliteHelperClass;
import uk.ac.tees.a0321466.model.locationModel;
import uk.ac.tees.a0321466.model.nearbyLocationModel;

public class locationDetailActivity extends AppCompatActivity {
    SqliteHelperClass databasehandler;
    nearbyLocationModel nearbyLocationModel;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        /* below lines used to create back button on the toolbar */
//        Toolbar toolbar = findViewById(R.id.location_detail_toolbar);
//        setActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        nearbyLocationModel =new nearbyLocationModel();
        databasehandler = new SqliteHelperClass(getApplicationContext()); /* Sqlite locationSqlite helper class */
        //Initialize global variables class
        /*
      below written lines for set and get variables/data from GlobalClass when we are
      working with normal Activity.If you want to set/get in Fragment then use
        this way "gVariables=(GlobalClass)getActivity().getApplication();"
         */
        GlobalClass globalClass = (GlobalClass)getApplicationContext();

        //get index value passed by google map fragment
        index = getIntent().getExtras().getInt("index"); //case of fragment "this.getArguments.getInt("index)"
        nearbyLocationModel.setNearbyApi(globalClass.getNearbyApi());//pass all received api to perform filter proccess

        //get id of View components
        TextView tv_addr =   findViewById(R.id.tv_addr);
        TextView tv_name =   findViewById(R.id.tv_name);
        TextView tv_Bstatus= findViewById(R.id.tv_Bstatus);
        TextView tv_loc =     findViewById(R.id.tv_loc);
        TextView tv_rating =  findViewById(R.id.tv_rating);
        TextView tv_openH =   findViewById(R.id.tv_openH);
        ImageView iv_icon =   findViewById(R.id.iv_icon);

        //set values to view components
        tv_name.setText( nearbyLocationModel.getName(index));
        tv_addr.setText( nearbyLocationModel.getAddr(index));
        Picasso.with(getApplicationContext()).load( nearbyLocationModel.getIconImg(index))
                .resize(100, 125).into(iv_icon);
        tv_loc.setText(String.valueOf( nearbyLocationModel.getLat(index))
                + ", "  + String.valueOf( nearbyLocationModel.getLng(index)));
        tv_rating.setText(String.valueOf( nearbyLocationModel.getRating(index)));
        tv_Bstatus.setText( nearbyLocationModel.getBusinessStatus(index));

        if( nearbyLocationModel.getBusinessStatus(index) =="OPERATIONAL"){
            tv_openH.setText(String.valueOf( nearbyLocationModel.isOpenNow(index)));
        }



        //back button click listener, custom toolbar
        findViewById(R.id.toolbar_backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move back to home fragment
                  onBackPressed();

            }
        });

        //favorite button press event listener
        /* this button helps to save location details in the sqlite database

         */
        findViewById(R.id.btn_fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (insertLocationToSqlite()) {
                    toastMess("Successfully Location Added");
                    onBackPressed();  //go back
                } else {
                    toastMess("Error To Add Location!");
                }
            }
        });
    }

    private boolean  insertLocationToSqlite(){
        try{
            locationModel location_model=new locationModel(-1,
                    nearbyLocationModel.getName(index),  nearbyLocationModel.getAddr(index),
                    String.valueOf( nearbyLocationModel.getRating(index)),
                    nearbyLocationModel.getIconImg(index));
            //return true if data successfully inserted to the database
            if (databasehandler.insertContact(location_model)) { return true; }
            else { return false;  }
        } catch (Exception err) { return false; }

    }

    /* Toast msg display function */
    private void toastMess(String msg){

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

    }

    /* for toolbar back button item selector */
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}