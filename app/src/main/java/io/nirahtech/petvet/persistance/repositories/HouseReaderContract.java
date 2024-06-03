package io.nirahtech.petvet.persistance.repositories;

import android.provider.BaseColumns;

public class HouseReaderContract {
    private HouseReaderContract() {}

    public static class HouseEntry implements BaseColumns {
        public static final String TABLE_NAME = "house";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + HouseEntry.TABLE_NAME + " (" +
                        HouseEntry._ID + " INTEGER PRIMARY KEY," +
                        HouseEntry.COLUMN_NAME_NAME + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + HouseEntry.TABLE_NAME;
    }
}
