package uk.ac.tees.a0321466.javaClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0321466.model.locationModel;

public class SqliteHelperClass extends SQLiteOpenHelper {

    //database parameters
    public static final String DATABASE_NAME = "locations.db";
    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static final String LOCATIONS_COLUMN_ID = "id";
    public static final String LOCATIONS_COLUMN_NAME = "name";
    public static final String LOCATIONS_COLUMN_ADDR = "addr";
    public static final String LOCATIONS_COLUMN_RATINGS = "ratings";
    public static final String LOCATIONS_COLUMN_ICONURL = "iconURL";



    public SqliteHelperClass(@Nullable Context context) {
        //set database name, version .
        super(context,"locationsDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //we can create query in the form of string as written below()
//        String createTableStatement= "create table contacts " +
//                "(id integer primary key, name text,age int,studentID int)";
        //write query using global variables
        String createTableStatement= "create table " + LOCATIONS_TABLE_NAME +
                " ( " + LOCATIONS_COLUMN_ID + " integer primary key, " + LOCATIONS_COLUMN_NAME + " text, "
                + LOCATIONS_COLUMN_ADDR + " text, " + LOCATIONS_COLUMN_RATINGS + " text, " + LOCATIONS_COLUMN_ICONURL + " text)";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }


    //method to insert data in the database
    public boolean insertContact(locationModel locationmodel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(LOCATIONS_COLUMN_NAME, locationmodel.getName().toString());
        cv.put(LOCATIONS_COLUMN_ADDR, locationmodel.getAddr());
        cv.put(LOCATIONS_COLUMN_RATINGS, locationmodel.getRating());
        cv.put(LOCATIONS_COLUMN_ICONURL, locationmodel.getIconUrl());
        long insert= db.insert(LOCATIONS_TABLE_NAME,null,cv);
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }

    //get data or view data stored in the database table

    public List<locationModel> viewAll(){
        //define list type
        List<locationModel> returnList= new ArrayList<>();
        String getQuery= "select * from " + LOCATIONS_TABLE_NAME;
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor= db.rawQuery(getQuery,null);
        if(cursor.moveToFirst()){
            do{
                int id= cursor.getInt(0);
                String locationName= cursor.getString(1);
                String locationAddr= cursor.getString(2);
                String locationRating= cursor.getString(3);
                String locationIcon= cursor.getString(4);
                locationModel locationModle= new locationModel(id,locationName,locationAddr,locationRating,locationIcon);
                returnList.add(locationModle);

            }while (cursor.moveToNext());
            db.close();
        }

        //return list of location array
        return returnList;
    }

    //delete database contact(student entry)
    public boolean deleteOne(locationModel locationModle){
        String query= "delete from " + LOCATIONS_TABLE_NAME + " where " + LOCATIONS_COLUMN_ID + " = " + locationModle.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            return false;
        }
        else{
            return true;
        }
    }


}

