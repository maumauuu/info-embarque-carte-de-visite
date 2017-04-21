package com.CDV.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_CARTE = "carte";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NUMERO = "numero";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_POSTAL = "postal";

    public static final String TABLE_PROFIL = "profil";
    public static final String PROFIL_ID = "_id";
    public static final String PROFIL_NAME = "name";
    public static final String PROFIL_FULLNAME = "fullname";
    public static final String PROFIL_EMAIL = "email";
    public static final String PROFIL_NUMERO = "numero";
    public static final String PROFIL_ADDRESS = "address";
    public static final String PROFIL_CITY = "city";
    public static final String PROFIL_POSTAL = "postal";

    private static final String DATABASE_NAME = "carte.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_CARTE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_FULLNAME
            + " text not null, " + COLUMN_EMAIL
            + " text not null, " + COLUMN_NUMERO
            + " text not null, " + COLUMN_ADDRESS
            + " text not null, " + COLUMN_CITY
            + " text not null, " + COLUMN_POSTAL
            + " text not null);";

    private static final String DATABASE_CREATE_PROFIL = "create table "
            + TABLE_PROFIL + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + PROFIL_NAME
            + " text not null, " + PROFIL_FULLNAME
            + " text not null, " + PROFIL_EMAIL
            + " text not null, " + PROFIL_NUMERO
            + " text not null, " + PROFIL_ADDRESS
            + " text not null, " + PROFIL_CITY
            + " text not null, " + PROFIL_POSTAL
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_PROFIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFIL + ";");
        onCreate(db);
    }
}
