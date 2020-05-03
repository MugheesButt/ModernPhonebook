package com.mugheesnadeem.i160029;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Contacts.db" ;
    private static final int DATABASE_VERSION = 6 ;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_CONTACT_TABLE = "CREATE TABLE " + Contract.ContactEntry.TABLE_NAME + "(" +
                Contract.ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.ContactEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                Contract.ContactEntry.COLUMN_PHONE + " TEXT NOT NULL, " +
                Contract.ContactEntry.COLUMN_IMAGE + " TEXT);" ;

        final String CREATE_LOCATION_TABLE = "CREATE TABLE " + Contract.LocationEntry.TABLE_NAME + "(" +
                Contract.LocationEntry.COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY, " +
                Contract.LocationEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                Contract.LocationEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                Contract.LocationEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                "FOREIGN KEY (" +
                Contract.LocationEntry.COLUMN_CONTACT_ID +
                ") REFERENCES " +
                Contract.ContactEntry.TABLE_NAME +
                " ( " + Contract.ContactEntry._ID + "));" ;

        db.execSQL(CREATE_CONTACT_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        final String DELETE_CONTACT_TABLE = "DROP TABLE IF EXISTS " + Contract.ContactEntry.TABLE_NAME;
        final String DELETE_LOCATION_TABLE = "DROP TABLE IF EXISTS " + Contract.LocationEntry.TABLE_NAME;
        db.execSQL(DELETE_CONTACT_TABLE);
        db.execSQL(DELETE_LOCATION_TABLE);
        onCreate(db);

    }

                                                                /*              CONTACT              */


    public boolean addNewContact(String Name , String Phone, String Address, String path)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(Contract.ContactEntry.COLUMN_NAME , Name);
        cv.put(Contract.ContactEntry.COLUMN_PHONE , Phone);
        cv.put(Contract.ContactEntry.COLUMN_IMAGE , path);

        long result = db.insert(Contract.ContactEntry.TABLE_NAME, null , cv) ;
        int id = (int)result ;

        if (result == -1)
            return false ;
        else
            {

                String adr , lon , lat ;
                StringTokenizer tokens = new StringTokenizer(Address, ":");
                adr = tokens.nextToken();
                lon = tokens.nextToken();
                lat = tokens.nextToken();
                addNewLocation(id,adr, Double.valueOf(lat) , Double.valueOf(lon));

                return true;
        }
    }


    public boolean updateContact(ContactModel cm) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Contract.ContactEntry._ID + "=?" ;
        String[] selectionArgs = {String.valueOf(cm.getId())};

        ContentValues values = new ContentValues();
        values.put(Contract.ContactEntry.COLUMN_NAME, cm.getName());
        values.put(Contract.ContactEntry.COLUMN_PHONE , cm.getPhno());
        values.put(Contract.ContactEntry.COLUMN_IMAGE, cm.getImage().toString());

        long result = db.update(Contract.ContactEntry.TABLE_NAME,
                values,
                selection ,
                selectionArgs
        );

        if (result == -1)
            return false ;
        else
            return true ;
    }


    public ContactModel getContact(int id)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] Columns = {
                Contract.ContactEntry._ID,
                Contract.ContactEntry.COLUMN_NAME,
                Contract.ContactEntry.COLUMN_PHONE,
                Contract.ContactEntry.COLUMN_IMAGE
        };

        String selection = Contract.ContactEntry._ID + "=?" ;
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(Contract.ContactEntry.TABLE_NAME,
                Columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (cursor != null)
            cursor.moveToFirst();

        ContactModel cm = new ContactModel();
        cm.setId(Integer.valueOf(cursor.getString(0)));
        cm.setName(cursor.getString(1));
        cm.setPhno(cursor.getString(2));
        cm.setImage(Uri.parse(cursor.getString(3)));


        return cm ;
    }


    public ArrayList<ContactModel> getAllContacts() {

        ArrayList<ContactModel> ContactsList = new ArrayList<ContactModel>() ;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] Columns = {
                Contract.ContactEntry._ID,
                Contract.ContactEntry.COLUMN_NAME,
                Contract.ContactEntry.COLUMN_PHONE,
                Contract.ContactEntry.COLUMN_IMAGE
        };

        String sort = Contract.ContactEntry.COLUMN_NAME + " ASC";

        Cursor cursor = db.query(
                Contract.ContactEntry.TABLE_NAME,
                Columns,
                null,
                null,
                null ,
                null ,
                sort
        ) ;

        if(cursor.moveToFirst()) {
            do {

                ContactModel cm = new ContactModel();
                cm.setId(Integer.valueOf(cursor.getString(0)));
                cm.setName(cursor.getString(1));
                cm.setPhno(cursor.getString(2));
                cm.setImage(Uri.parse(cursor.getString(3)));

                ContactsList.add(cm);


            } while (cursor.moveToNext()) ;
        }


        return ContactsList;

    }

    public int countContacts()
    {

        return getAllContacts().size();

    }

    public boolean deleteContact (int id){

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Contract.ContactEntry._ID + "=" + id;

        long result;
        result = db.delete(Contract.ContactEntry.TABLE_NAME,selection,null) ;

        if (result == -1)
            return false ;
        else
        {
            boolean res ;
            res = deleteLocation(id);
            if (res == false)
                return false;
            else
                return true ;
        }
    }



                                                                            /*              ADDRESS                   */



    public boolean addNewLocation(int Con_id , String Name , double lat, double lon)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(Contract.LocationEntry.COLUMN_CONTACT_ID, Con_id);
        cv.put(Contract.LocationEntry.COLUMN_NAME , Name);
        cv.put(Contract.LocationEntry.COLUMN_LATITUDE , lat);
        cv.put(Contract.LocationEntry.COLUMN_LONGITUDE , lon);

        long result = db.insert(Contract.LocationEntry.TABLE_NAME, null , cv) ;

        if (result == -1)
            return false ;
        else
            return true ;
    }


    public boolean updateLocation(LocationModel lm) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Contract.LocationEntry.COLUMN_CONTACT_ID + "=?" ;
        String[] selectionArgs = {String.valueOf(lm.getId())};

        ContentValues values = new ContentValues();
        values.put(Contract.LocationEntry.COLUMN_NAME, lm.getName());
        values.put(Contract.LocationEntry.COLUMN_LATITUDE , lm.getLat());
        values.put(Contract.LocationEntry.COLUMN_LONGITUDE, lm.getLon());

        long result = db.update(Contract.LocationEntry.TABLE_NAME,
                values,
                selection ,
                selectionArgs
        );

        if (result == -1)
            return false ;
        else
            return true ;
    }


    public LocationModel getLocation(int Con_id)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] Columns = {
                Contract.LocationEntry.COLUMN_CONTACT_ID,
                Contract.LocationEntry.COLUMN_NAME,
                Contract.LocationEntry.COLUMN_LATITUDE,
                Contract.LocationEntry.COLUMN_LONGITUDE
        };

        String selection = Contract.LocationEntry.COLUMN_CONTACT_ID + "=?" ;
        String[] selectionArgs = {String.valueOf(Con_id)};

        Cursor cursor = db.query(Contract.LocationEntry.TABLE_NAME,
                Columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (cursor != null)
            cursor.moveToFirst();

        LocationModel lm = new LocationModel();
        lm.setId(Integer.valueOf(cursor.getString(0)));
        lm.setName(cursor.getString(1));
        lm.setLat(Double.valueOf(cursor.getString(2)));
        lm.setLon(Double.valueOf(cursor.getString(3)));

        return lm ;
    }


    public boolean deleteLocation (int id){

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Contract.LocationEntry.COLUMN_CONTACT_ID + "=" + id;

        long result;
        result = db.delete(Contract.LocationEntry.TABLE_NAME,selection,null) ;

        if (result == -1)
            return false ;
        else
            return true ;
    }





}
