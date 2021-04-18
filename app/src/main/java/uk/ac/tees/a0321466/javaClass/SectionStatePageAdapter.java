package uk.ac.tees.a0321466.javaClass;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0321466.R;

public class SectionStatePageAdapter  extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    private FragmentManager fm1;

    public SectionStatePageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fm1 = fm;
    }
    public void addFragment(Fragment fragment, String fragmentName){
        fragmentList.add(fragment);
        fragmentTitleList.add(fragmentName);
    }
    public void changeFragment(int id){
        FragmentTransaction ft = fm1.beginTransaction();
        ft.replace(R.id.nav_host_fragment,fragmentList.get(id));

  }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
