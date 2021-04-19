package uk.ac.tees.a0321466;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import uk.ac.tees.a0321466.javaClass.SectionStatePageAdapter;
import uk.ac.tees.a0321466.ui.FavoriteList;
import uk.ac.tees.a0321466.ui.MyLocation;
import uk.ac.tees.a0321466.ui.Home_mainLogic;
import uk.ac.tees.a0321466.ui.profile;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ViewPager viewPager;
    private SectionStatePageAdapter sectionStatePageAdapter;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fAuth = FirebaseAuth.getInstance();

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

        /* access logout button defined in the main_drawer.xml */
        navigationView.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                confirmLogout();
                drawer.closeDrawers();
                return true;
            }
        });
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



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}