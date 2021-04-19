package uk.ac.tees.a0321466.javaClass;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* class help to get latitude and longitude by using location name
  */
public class textToLocationConverter_Geo {

    Context context;
    public textToLocationConverter_Geo( Context context) {
        this.context = context;
    }

    public  List<Address> geoLocate(String locationName){
        Geocoder geocoder = new Geocoder(context);
        List<Address> listAddr = new ArrayList<>();
        try{
            listAddr = geocoder.getFromLocationName(locationName,1);

        }catch(IOException error){
            Toast.makeText(context.getApplicationContext(),"Error to get address",Toast.LENGTH_SHORT).show();
        }

        return listAddr;
    }
}
