package uk.ac.tees.a0321466.javaClass;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static uk.ac.tees.a0321466.javaClass.globalVariables.REQUEST_CODE;
public class mapPermission extends AppCompatActivity {
    private Context contextActivity;
    //MapsActivity.OnCustomEventListener _mp;
    onCustomCallback callback;  //gps callback when gps enable done

    public mapPermission(Context cActivity, onCustomCallback onCustomCallback) {
        contextActivity=cActivity;
        callback = (onCustomCallback) onCustomCallback; //invoke ....

        //_mp=mp;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //below if condition to check permission is granted or not
            if (contextActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && contextActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                final AlertDialog.Builder abuilder = new AlertDialog.Builder(contextActivity);
                abuilder.setTitle("Location Permission");
                abuilder.setMessage("Please grant the location permission");
                abuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions((Activity) contextActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                        //requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                    }
                });
                abuilder.setNegativeButton(android.R.string.no, null);
                abuilder.show();
            } else {  //else permission is granted
                isGpsEnable();
            }
        }

    }

    //check gps status, Is it enable or not?
    private void isGpsEnable() {
        //String provider=Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        String provider= Settings.Secure.getString (contextActivity.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) {
            enableGPS();
        } else {
           // Toast.makeText(contextActivity,"gPS connect", Toast.LENGTH_SHORT).show();

//            _mp.onEvent();

              callback.gpsEnableDone(); //this call a method, which is defined in MapsActivity
            //Intent _intent = new Intent(contextActivity, map.class);
            //startActivityForResult(_intent, MAP_CODE_REQUEST);
        }
    }

    private void enableGPS(){
        final AlertDialog.Builder _builder = new AlertDialog.Builder(contextActivity);
        _builder.setMessage("Your GPS is disabled. Do you want to enable?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        try {
                            contextActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } catch(Exception e) {
                            Toast.makeText(contextActivity,e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = _builder.create();
        alert.show();
    }
}


