package com.mugheesnadeem.i160029;

import android.provider.BaseColumns;

public class Contract {

    private Contract(){};

    public static class ContactEntry implements BaseColumns {

        public static final String TABLE_NAME = "Contact" ;
        public static final String COLUMN_NAME = "Name" ;
        public static final String COLUMN_PHONE = "Phone" ;
        public static final String COLUMN_IMAGE = "Image" ;

    }

    public static class LocationEntry {

        public static final String TABLE_NAME = "Address" ;
        public static final String COLUMN_CONTACT_ID = "ContactID" ;
        public static final String COLUMN_NAME = "Name" ;
        public static final String COLUMN_LATITUDE = "Latitude" ;
        public static final String COLUMN_LONGITUDE = "Longitude" ;

    }
}
