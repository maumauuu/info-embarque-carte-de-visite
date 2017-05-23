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

    private String[] allColumnsProfil = {MySQLiteHelper.PROFIL_ID, MySQLiteHelper.PROFIL_NAME,
            MySQLiteHelper.PROFIL_FULLNAME, MySQLiteHelper.PROFIL_EMAIL, MySQLiteHelper.PROFIL_NUMERO,
            MySQLiteHelper.PROFIL_ADDRESS, MySQLiteHelper.PROFIL_CITY, MySQLiteHelper.PROFIL_POSTAL};

    private String[] allColumnsImage = {MySQLiteHelper.IMAGE_ID, MySQLiteHelper.IMAGE_CHEMIN};

    public CarteDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
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

    public Image createImage(String chemin){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.IMAGE_CHEMIN, chemin);

        long insertId = database.insert(MySQLiteHelper.TABLE_IMAGE, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_IMAGE, allColumnsImage, MySQLiteHelper.IMAGE_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Image newImage = cursorToCommentImage(cursor);
        cursor.close();

        return newImage;

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

    public List<Image> getAllImage(){
        List<Image> images = new ArrayList<Image>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_IMAGE, allColumnsImage, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Image image = cursorToCommentImage(cursor);
            images.add(image);
            cursor.moveToNext();
        }

        cursor.close();
        return images;
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

    private Image cursorToCommentImage(Cursor cursor){
        Image image = new Image();
        image.setId(cursor.getLong(0));
        image.setChemin(cursor.getString(1));
        return image;
    }
}
