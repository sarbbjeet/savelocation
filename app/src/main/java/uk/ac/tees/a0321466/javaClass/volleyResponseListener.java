package uk.ac.tees.a0321466.javaClass;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface volleyResponseListener {
        void onError(VolleyError err);
        void onResponse(JSONObject jsonObject);

    }
