package uk.ac.tees.a0321466;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.File;

import uk.ac.tees.a0321466.javaClass.AirplaneModeBroadcastReceiver;
import uk.ac.tees.a0321466.javaClass.MySharedPref22;
import uk.ac.tees.a0321466.javaClass.SectionStatePageAdapter;
import uk.ac.tees.a0321466.ui.FavoriteList;
import uk.ac.tees.a0321466.ui.MyLocation;
import uk.ac.tees.a0321466.ui.Home_mainLogic;
import uk.ac.tees.a0321466.ui.profile;

import static android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED;
import static uk.ac.tees.a0321466.javaClass.MySharedPref22.IMAGE;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ViewPager viewPager;
    private SectionStatePageAdapter sectionStatePageAdapter;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView username;
    MySharedPref22 mySharedPref22;
    StorageReference storageReference;
    StorageReference fileRef;
    String imagelink;
    ImageView imageView; // profile image view component
    AirplaneModeBroadcastReceiver airplaneModeBroadcastReceiver =new AirplaneModeBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mySharedPref22 = MySharedPref22.getInstance(MainActivity.this);

        /* code for pager adapter to access fragment (custom changes)*/
        sectionStatePageAdapter =new SectionStatePageAdapter(getSupportFragmentManager());
        sectionStatePageAdapter.addFragment(new Home_mainLogic(), "home_fragment");
        sectionStatePageAdapter.addFragment(new MyLocation(), "mylocation_fragment");
        sectionStatePageAdapter.addFragment(new profile(), "profile_fragment");
        sectionStatePageAdapter.addFragment(new FavoriteList(), "favoriteList_fragment");
///////////////////////////////////////////////////////////////////////////////////////////////////////////

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_journeys, R.id.nav_profile,
                R.id.nav_myLocation)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* drawer side menu event listener
         */


        View hView = navigationView.getHeaderView(0);
        username = hView.findViewById(R.id.nav_user_name);
        username.setText(mySharedPref22.getName());
        imageView = hView.findViewById(R.id.nav_user_image);

        /* read fire cloud store image*/

          /* read the cloud store image using storage reference
       do some modifications here in the name of profile name because i want to store images for every user
         */
        imagelink = "users/"+ fAuth.getCurrentUser().getUid() + "/profile.jpg";
        fileRef= storageReference.child(imagelink);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

//
//        ImageView imageView = hView.findViewById(R.id.nav_user_image);
//        String imagePath = mySharedPref22.getValue(IMAGE);
//
//
//        if (imagePath != null && !imagePath.isEmpty()) {
//            imageView.setImageURI(Uri.fromFile(new File(imagePath)));
//        }



        /* access logout button defined in the main_drawer.xml */
        navigationView.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                confirmLogout();
                drawer.closeDrawers();
                return true;
            }
        });



        readFireBaseFireStore();
    }

    private void confirmLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void logout(){
        fAuth.signOut();
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        finish();

    }



    /* read firebase data */

    public void readFireBaseFireStore() {
        //userTypeBtn // means which button is pressed
        String userId= fAuth.getCurrentUser().getUid();
        DocumentReference db = fStore.collection("users")
                .document(userId);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String first_name = document.getString("firstName");


                                username.setText(first_name);
                                mySharedPref22.saveName(first_name);
//                                String last_name = document.getString("lastName");
//                                String email = document.getString("email");
//                                String dob = document.getString("DOB");
//                                String _mobile = document.getString("mobile");

                            }
                        }else {
                        }
                        // Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Reload profile image and name when menu presses
         */
        username.setText(mySharedPref22.getName());
        imagelink = "users/"+ fAuth.getCurrentUser().getUid() + "/profile.jpg";
        fileRef= storageReference.child(imagelink);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneModeBroadcastReceiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeBroadcastReceiver);
    }
}