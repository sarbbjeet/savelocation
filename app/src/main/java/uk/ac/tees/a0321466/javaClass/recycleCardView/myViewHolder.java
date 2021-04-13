package uk.ac.tees.a0321466.javaClass.recycleCardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.a0321466.R;


/*
Import class to get id of view components used in adapter xml file.
after that these ids are used in main Adapter call to pass data to view component
according to the position of in the List Array.
 */
public class myViewHolder extends RecyclerView.ViewHolder {
    public TextView name,addr,rating;
    public ImageView icon,btn_delete;;
    public CardView cardView;
    public myViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.ad_name);
        addr = itemView.findViewById(R.id.ad_addr);
        rating = itemView.findViewById(R.id.ad_rating);
        icon = itemView.findViewById(R.id.ad_image);
        cardView = itemView.findViewById(R.id.cardView1);
        btn_delete = itemView.findViewById(R.id.btn_delete);
    }
}
