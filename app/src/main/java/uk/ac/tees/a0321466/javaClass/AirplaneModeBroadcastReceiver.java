
/* Broadcast receiver class to Listen AirplaneMode ON or OFF Event
 */

package uk.ac.tees.a0321466.javaClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AirplaneModeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())){
            if(intent.getBooleanExtra("state",false)){
                Toast.makeText(context,"Airplane mode is Enabled",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"Airplane mode is Disabled",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
