package uk.ac.tees.a0321466.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.SqliteHelperClass;
import uk.ac.tees.a0321466.javaClass.deleteLocationItem;
import uk.ac.tees.a0321466.javaClass.recycleCardView.locationViewAdapter;
import uk.ac.tees.a0321466.model.locationModel;


public class FavoriteList extends Fragment {
  RecyclerView recyclerView;
  uk.ac.tees.a0321466.javaClass.recycleCardView.locationViewAdapter locationViewAdapter;
  SqliteHelperClass databasehandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_journeys, container, false);

        /* Sqlite locationSqlite helper class */
        databasehandler = new SqliteHelperClass(getActivity());

        recyclerView = (RecyclerView)view.findViewById(R.id.favList); //get RecyclerView component id
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //create linearLayout to display multi-items
        locationViewAdapter= new locationViewAdapter(getActivity(),getLocationList());//pass locationList with Context to Custom adapter
        recyclerView.setAdapter(locationViewAdapter);

        locationViewAdapter.setOnItemClickListener(new deleteLocationItem<locationModel>() {
            @Override
            public void onItemClick(locationModel clickedLocation) {
                //delete clicked item form the data base using id
                //int id = data.getId();
                if(databasehandler.deleteOne(clickedLocation)){
                    Toast.makeText(getActivity(),clickedLocation.getName()+
                            " location is delete from favorite list", Toast.LENGTH_SHORT).show();
                    //update the items to the recycler view(display) after deleting location item
                    locationViewAdapter= new locationViewAdapter(getActivity(),databasehandler.viewAll());
                    //pass locationList with Context to Custom adapter
                    recyclerView.setAdapter(locationViewAdapter);
                }
                else{
                    Toast.makeText(getActivity(),"Error to delete " + clickedLocation.getName()
                            + " from favorite list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  view;
    }

    /* get all location details store in the database

     */
    private List<locationModel> getLocationList(){
       // List<locationModel> list = new ArrayList<>();
        /* get all data from database and pass to list adapter */
       //list = databasehandler.viewAll();
//        m.setName("Post Office");
//        m.setAddr("7 Tennyson Street");
//        m.setRating("4.6");
//        m.setIconUrl("https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/school-71.png");
       // _list.add(m);
        return databasehandler.viewAll();
    }
}