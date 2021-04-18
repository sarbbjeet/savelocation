package uk.ac.tees.a0321466;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
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
import uk.ac.tees.a0321466.ui.SearchLocation;
import uk.ac.tees.a0321466.ui.home;
import uk.ac.tees.a0321466.ui.profile;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ViewPager viewPager;
    private SectionStatePageAdapter sectionStatePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* code for pager adapter to access fragment (custom changes)*/
        sectionStatePageAdapter =new SectionStatePageAdapter(getSupportFragmentManager());
        sectionStatePageAdapter.addFragment(new home(), "home_fragment");
        sectionStatePageAdapter.addFragment(new MyLocation(), "mylocation_fragment");
        sectionStatePageAdapter.addFragment(new profile(), "profile_fragment");
        sectionStatePageAdapter.addFragment(new SearchLocation(), "searchLocation_fragment");
        sectionStatePageAdapter.addFragment(new FavoriteList(), "favoriteList_fragment");
///////////////////////////////////////////////////////////////////////////////////////////////////////////

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_journeys, R.id.nav_profile,
                R.id.nav_myLocation, R.id.nav_searchLocation)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

   // @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}