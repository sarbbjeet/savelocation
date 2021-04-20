package uk.ac.tees.a0321466.javaClass;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref22 {

    private static final String USER_NAME = "user_name";
    public static final String IMAGE = "image";
    private static final String USER_ID = "user_id";
    private static final String IS_LOGIN = "is_login";
    private static final String USER_RATING = "user_rating";
    private static final String MY_PREF_NAME = "Save Location";

    private static uk.ac.tees.a0321466.javaClass.MySharedPref22 mySharedPref22;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
//    private Gson gson;

    protected MySharedPref22(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }


    public static synchronized uk.ac.tees.a0321466.javaClass.MySharedPref22 getInstance(Context context) {
        if (mySharedPref22 == null)
            mySharedPref22 = new uk.ac.tees.a0321466.javaClass.MySharedPref22(context);

        return mySharedPref22;
    }


    public void saveName(String name) {
        editor.putString(USER_NAME, name).commit();
    }

    public void saveValue(String name, String key) {
        editor.putString(key, name).commit();
    }

    public String getName() {
        return sharedPreferences.getString(USER_NAME, null);
    }
    public String getValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void saveID(String id) {
        editor.putString(USER_ID, id).commit();
    }

    public void saveUserRating(float rating) {
        editor.putFloat(USER_RATING, rating).commit();
    }

    public float getUserRating() {
        return sharedPreferences.getFloat(USER_RATING, 0f);
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void setLogin(boolean value) {
        editor.putBoolean(IS_LOGIN, value).commit();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

}
