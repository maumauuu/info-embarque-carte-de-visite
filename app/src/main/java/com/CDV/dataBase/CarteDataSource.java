package com.CDV.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;



public class CarteDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME,
        MySQLiteHelper.COLUMN_FULLNAME, MySQLiteHelper.COLUMN_EMAIL, MySQLiteHelper.COLUMN_NUMERO,
            MySQLiteHelper.COLUMN_ADDRESS, MySQLiteHelper.COLUMN_CITY, MySQLiteHelper.COLUMN_POSTAL};

    private String[] allColumnsProfil = {MySQLiteHelper.PROFIL_ID, MySQLiteHelper.PROFIL_NAME,
            MySQLiteHelper.PROFIL_FULLNAME, MySQLiteHelper.PROFIL_EMAIL, MySQLiteHelper.PROFIL_NUMERO,
            MySQLiteHelper.PROFIL_ADDRESS, MySQLiteHelper.PROFIL_CITY, MySQLiteHelper.PROFIL_POSTAL};


    public CarteDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Carte createCarte(String name, String fullname, String email, String numero, String address, String city, String postal){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_FULLNAME, fullname);
        values.put(MySQLiteHelper.COLUMN_EMAIL, email);
        values.put(MySQLiteHelper.COLUMN_NUMERO, numero);
        values.put(MySQLiteHelper.COLUMN_ADDRESS, address);
        values.put(MySQLiteHelper.COLUMN_CITY, city);
        values.put(MySQLiteHelper.COLUMN_POSTAL, postal);

        long insertId = database.insert(MySQLiteHelper.TABLE_CARTE, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CARTE, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Carte newCarte = cursorToComment(cursor);
        cursor.close();

        return newCarte;

    }

    public Carte createProfil(String name, String fullname, String email, String numero, String address, String city, String postal){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PROFIL_NAME, name);
        values.put(MySQLiteHelper.PROFIL_FULLNAME, fullname);
        values.put(MySQLiteHelper.PROFIL_EMAIL, email);
        values.put(MySQLiteHelper.PROFIL_NUMERO, numero);
        values.put(MySQLiteHelper.PROFIL_ADDRESS, address);
        values.put(MySQLiteHelper.PROFIL_CITY, city);
        values.put(MySQLiteHelper.PROFIL_POSTAL, postal);

        long insertId = database.insert(MySQLiteHelper.TABLE_PROFIL, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFIL, allColumnsProfil, MySQLiteHelper.PROFIL_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Carte newCarte = cursorToComment(cursor);
        cursor.close();

        return newCarte;

    }

    public void deleteCarte(Carte carte){
        long id = carte.getId();
        database.delete(MySQLiteHelper.TABLE_CARTE, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Carte> getAllCarte(){
        List<Carte> cartes = new ArrayList<Carte>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CARTE, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Carte carte = cursorToComment(cursor);
            cartes.add(carte);
            cursor.moveToNext();
        }

        cursor.close();
        return cartes;
    }

    public List<Carte> getAllProfil(){
        List<Carte> cartes = new ArrayList<Carte>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFIL, allColumnsProfil, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Carte carte = cursorToComment(cursor);
            cartes.add(carte);
            cursor.moveToNext();
        }

        cursor.close();
        return cartes;
    }

    private Carte cursorToComment(Cursor cursor){
        Carte carte = new Carte();
        carte.setId(cursor.getLong(0));
        carte.setName(cursor.getString(1));
        carte.setFullname(cursor.getString(2));
        carte.setEmail(cursor.getString(3));
        carte.setNumero(cursor.getString(4));
        carte.setAddress(cursor.getString(5));
        carte.setCity(cursor.getString(6));
        carte.setPostal(cursor.getString(7));
        return carte;
    }
}
