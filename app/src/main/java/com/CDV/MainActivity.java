package com.CDV;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.CDV.fragment.GestionContactFragment;
import com.CDV.fragment.ProfilFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static com.CDV.util.util.Ajouter_contact;
import static com.CDV.util.util.Fill;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new ProfilFragment();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("VisitCard");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Bundle bundl = new Bundle();
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            bundl.putInt("pos", 0);

            fragment = new GestionContactFragment();
            fragment.setArguments(bundl);
        } else if (id == R.id.nav_gallery) {
            bundl.putInt("pos", 1);

            fragment = new GestionContactFragment();
            fragment.setArguments(bundl);

        } else if (id == R.id.nav_slideshow) {

            fragment = new ProfilFragment();
        }


        if(null!=fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        //Code pour recuperer les infos du contact choisi
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