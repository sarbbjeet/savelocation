package uk.ac.tees.a0321466.javaClass.recycleCardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0321466.R;
import uk.ac.tees.a0321466.javaClass.deleteLocationItem;
import uk.ac.tees.a0321466.model.locationModel;

public class locationViewAdapter extends RecyclerView.Adapter<locationViewAdapter.MyViewHolder>{
    public List<locationModel> locationList;
    deleteLocationItem<locationModel> cl;

    Context c;

    public locationViewAdapter(FragmentActivity activity, List<locationModel> locationList) {
    this.c= activity;
    this.locationList= locationList;
    }

    void RecyclerViewAdapter(List<locationModel> locationList){
        this.locationList = locationList;

    }
    @Override
    public locationViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(locationViewAdapter.MyViewHolder holder, final int position) {
        final locationModel location = this.locationList.get(position);
        holder.name.setText(location.getName());
        holder.addr.setText(location.getAddr());
        holder.rating.setText(location.getRating());
        Picasso.with(this.c).load(location.getIconUrl()).resize(70, 70).into(holder.icon);
       // holder.image.setBackgroundResource(movie.getImage());
      holder.btn_delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              cl.onItemClick(location);
          }
      });

    }

    //click listener for process delete iperations
    public void setOnItemClickListener(deleteLocationItem<locationModel> locationClickListener) {
        cl = locationClickListener;
    }
    @Override
    public int getItemCount() {
        return this.locationList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView addr;
        private TextView rating;
        private ImageView icon;
        private CardView cardView;
        private Button btn_delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ad_name);
            addr = itemView.findViewById(R.id.ad_addr);
            rating = itemView.findViewById(R.id.ad_rating);
            icon = itemView.findViewById(R.id.ad_image);
            cardView = itemView.findViewById(R.id.cardView1);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
