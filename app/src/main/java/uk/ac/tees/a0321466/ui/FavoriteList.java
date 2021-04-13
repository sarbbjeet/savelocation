package uk.ac.tees.a0321466.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        View view =inflater.inflate(R.layout.favorite_list_fragment, container, false);

        /* Sqlite locationSqlite helper class */
        databasehandler = new SqliteHelperClass(getActivity());

        recyclerView = view.findViewById(R.id.favList); //get RecyclerView component id
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //create linearLayout to display multi-items
        locationViewAdapter=new locationViewAdapter(getActivity(),getLocationList());//pass locationList with Context to Custom adapter
        recyclerView.setAdapter(locationViewAdapter);

        locationViewAdapter.setOnItemClickListener(new deleteLocationItem<locationModel>() {
            @Override
            public void onItemClick(locationModel clickedLocation) {
                //delete clicked item form the data base using id
                int position = clickedLocation.getId();
                if(databasehandler.deleteOne(position)){
                    Toast.makeText(getActivity(),clickedLocation.getName()+
                            " location is delete from favorite list", Toast.LENGTH_SHORT).show();
                    locationViewAdapter.RecyclerViewAdapter(databasehandler.viewAll());
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
        return databasehandler.viewAll();
    }
}