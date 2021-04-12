package uk.ac.tees.a0321466.javaClass;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import static uk.ac.tees.a0321466.javaClass.GlobalClass.VOLLEY_TIMEOUT;

    public class getNearByLocationApi {
        private String url="";
        private Context mActivity;
        volleyResponseListener callback;

        public getNearByLocationApi(Context _mActivity) {
            mActivity=_mActivity;
           // callback = (volleyResponseListener) mActivity; //invoke ..

        }

        public void httpRequest(String url, volleyResponseListener volleyResponseListener){
            callback = (volleyResponseListener) volleyResponseListener;

            RequestQueue queue = Volley.newRequestQueue(mActivity);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onResponse(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error);
                }
            });


            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
            //change timeout of volley request ...
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    VOLLEY_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

    }

