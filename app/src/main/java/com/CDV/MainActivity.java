package com.CDV;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.CDV.adapter.SlidingMenuAdapter;
import com.CDV.dataBase.CarteDataSource;
import com.CDV.fragment.GestionContactFragment;
import com.CDV.fragment.ProfilFragment;
import com.CDV.model.ItemSlideMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import static com.CDV.util.util.Fill;
import static com.CDV.util.util.Ajouter_contact;

public class MainActivity extends ActionBarActivity {


    private CarteDataSource datasource;

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private static String Name;
    private static String LastName;
    private static String Email;
    private static String Phone;
    private static String Address;
    private static String City;
    private static String Postal;

    private final int CONTACT_PICKER_RESULT= 2017;

    public static void setName(String Name1){Name = Name1;}
    public static void setLastName(String LastName1){LastName = LastName1;}
    public static void setEmail(String Email1){Email = Email1;}
    public static void setPhone(String Phone1){Phone = Phone1;}
    public static void setAddress(String Address1){Address = Address1;}
    public static void setCity(String City1){City = City1;}
    public static void setPostal(String Postal1){Postal = Postal1;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        datasource = new CarteDataSource(this);
        datasource.open();


        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        listSliding.add(new ItemSlideMenu(R.drawable.contact, "See contact"));
        listSliding.add(new ItemSlideMenu(R.drawable.add, "Add Contact"));
        listSliding.add(new ItemSlideMenu(R.drawable.contact, "Profil"));

        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle(listSliding.get(0).getTitle());

        listViewSliding.setItemChecked(0, true);

        drawerLayout.closeDrawer(listViewSliding);


        replaceFragment(0);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setTitle(listSliding.get(position).getTitle());

                listViewSliding.setItemChecked(position, true);

                replaceFragment(position);

                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



    private void replaceFragment(int pos)  {
       Bundle bundl = new Bundle();
        bundl.putInt("pos", pos);
        android.support.v4.app.Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new GestionContactFragment();
                fragment.setArguments(bundl);

                break;
            case 1:
                fragment = new GestionContactFragment();
                fragment.setArguments(bundl);

                break;
            case 2:
                fragment = new ProfilFragment();
                break;
            default:
                fragment = new ProfilFragment();
                break;
        }

        if(null!=fragment) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    //methode pour lancer le scanner de qrcode
    public  void scanner(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void send(View view){
    }

    public void contact(View view){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            Log.d("phone number", name);

        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {

                if (result.getContents() == null) {
                } else {
                    String c = result.getContents();
                    Fill(c);

                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                    int rawContactInsertIndex = ops.size();

                    Ajouter_contact(ops, rawContactInsertIndex, this, Name, LastName, Phone, Email, Address, Postal, City);

                    Toast.makeText(this, "Contact ajouté", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
                startActivity(new Intent(this, MainActivity.class));

            }

        }
    }



}
