/* interface used to perform callback type of proccess
pass as a argument in the calling method. .
 */

package uk.ac.tees.a0321466.javaClass;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface volleyResponseListener {
        void onError(VolleyError err);
        void onResponse(JSONObject jsonObject);

    }
