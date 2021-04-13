package uk.ac.tees.a0321466.javaClass.recycleCardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.deleteLocationItem;
import uk.ac.tees.a0321466.model.locationModel;

public class locationViewAdapter extends RecyclerView.Adapter<myViewHolder> {
    public List<locationModel> locationList;
    deleteLocationItem<locationModel> getdeleteClickListener;  // interface method to pass delete button event
    Context context;
   //constructor of class get 2 parameters from parent class
    public locationViewAdapter(FragmentActivity activity, List<locationModel> locationList) {
        this.context = activity;
        this.locationList = locationList; //location list Array

    }


/*   method to update location list array
ex: when item is insert or delete from location list
 */
    public void RecyclerViewAdapter(List<locationModel> locationList) {
        this.locationList = locationList;

    }

    /* pre-defined method to pass View reference to myViewHolder class
     */
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        final locationModel location = this.locationList.get(position);
        //pass data to view components as per list position
        holder.name.setText(location.getName());
        holder.addr.setText(location.getAddr());
        holder.rating.setText(location.getRating());
        //picasso is use to pass url link(image) to ImageView component
        Picasso.with(this.context).load(location.getIconUrl()).resize(70, 70).into(holder.icon);

        //delete button event listener (this is also a part of view component)
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdeleteClickListener.onItemClick(location);  //activate onItemClick  event
            }
        });
    }

    /* function to pass method from called class to this class so this reference
    can easily used to call a method defined in called class("onItemClick")
     */
    public void setOnItemClickListener(deleteLocationItem<locationModel> locationClickListener) {
        getdeleteClickListener = locationClickListener;
    }

    //method to find size of location list means items are stored in the favorite list
    @Override
    public int getItemCount() {
        return this.locationList.size();
    }
}
