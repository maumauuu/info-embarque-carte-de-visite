package com.CDV;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.CDV.fragment.GestionContactFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String Name;
    private String LastName;
    private String Email;
    private String Phone;
    private String Address;
    private String City;
    private String Postal;

    private TextView textName;
    private TextView textNumero;
    private TextView textEmail;
    private TextView textAdress;


    private String expectedPrefix = "CDV";
    private String msg;
    private String num;

    //numero pour l'envoi du sms
    private EditText send_num;

    private Intent carte_contact;
    private final int CONTACT_PICKER_RESULT= 2017;

    public void setName(String Name1){Name = Name1;}
    public void setLastName(String LastName1){LastName = LastName1;}
    public void setEmail(String Email1){Email = Email1;}
    public void setPhone(String Phone1){Phone = Phone1;}
    public void setAddress(String Address1){Address = Address1;}
    public void setCity(String City1){City = City1;}
    public void setPostal(String Postal1){Postal = Postal1;}

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
        Bundle bundl = new Bundle();
        bundl.putInt("pos", 1);
        Fragment fragment = new GestionContactFragment();
        fragment.setArguments(bundl);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("VisitCard");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Gestion de la reception des sms
        MyReceiver receiver = new MyReceiver(this);
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(receiver, filter);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null) {
                int from = bundle.getInt("From");
            } else {
                Log.d("MainActivity", "Bundle null for " + getPackageName());
            }
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e("MainActivity", ex.toString());

        }

        send_num = (EditText)findViewById(R.id.phone);
    }

    String getExpectedPrefix() {
        return expectedPrefix;
    }


    //creation du menu de settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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


    //methode pour choisir le contact dan la liste du tel
    public void contact(View view){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }




    public void getContact(Cursor cursor){
        textName = (TextView) findViewById(R.id.textname);
        textNumero = (TextView) findViewById(R.id.textnumero);
        textEmail = (TextView) findViewById(R.id.textemail);
        textAdress = (TextView) findViewById(R.id.textadresse);
        String str_phone="";
        String email= "";
        String address="";
        cursor.moveToFirst();
        //recupere le nom et prenom u contact
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        //recupere l'indice des numeros de tel
        String id = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID)) + "";
        textName.setText(name);

        try {
            //recupere le tel du contact
            JSONObject phone = getContactPhones(id);
            //3: telephone travail
            str_phone = phone.getString("3");
            textNumero.setText(str_phone);
            email = getContactEmail(id);
            textEmail.setText(email);
            address = getContactAddress(id);
            textAdress.setText(address);
        }catch (Exception e) {
            Log.v("ContactPicker", "Parsing contact failed: " + e.getMessage());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                //on recupere lecontenu du qrcode
                String c = result.getContents();
                send_num.setText(c);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            startActivity(new Intent(this, MainActivity.class));
        }

        if (requestCode == 65) {
            Log.d("read", "pause");
            if (resultCode == 0) {
                //Si skip on ne fait  rien

            }

            if (resultCode == 1) {
                Log.d("read", "llll");
                carte_contact = new Intent(this, CarteContact.class);
                carte_contact.putExtra("msg",msg);
                carte_contact.putExtra("origin","SMS");
                carte_contact.putExtra("phone",num);
                startActivity(carte_contact);
            }
        }

        //Code pour recuperer les infos du contact choisi
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            LinearLayout carteProfilOther = (LinearLayout)findViewById(R.id.carte_other);
            carteProfilOther.setVisibility(LinearLayout.VISIBLE);
            getContact(cursor);

        }
    }

    //methode pour remplir les champs
    public void Fill(String c) {

        String Tc[] = c.split(";");

        setName(Tc[0]);
        setLastName(Tc[1]);
        setEmail(Tc[2]);
        setPhone(Tc[3]);
        setAddress(Tc[4]);
        setCity(Tc[5]);
        setPostal(Tc[6]);
    }

    //Recuperation de tous les numeros du contact rangé dans un JSON
    private JSONObject getContactPhones(String id ) throws JSONException {
        Cursor phonesCur = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id}, null);

        JSONObject phones = new JSONObject();

        while (phonesCur.moveToNext()) {
            int index = phonesCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNumber = phonesCur.getString(index);
            int type = phonesCur
                    .getInt(phonesCur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            phones.put(type + "", phoneNumber);
        }
        phonesCur.close();

        return phones;
    }


    //recuperation de l'email
    private String getContactEmail(String id) {
        String email = "";
        Cursor emailCur = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id}, null);
        while (emailCur.moveToNext())
            email = emailCur
                    .getString(emailCur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        emailCur.close();
        return email;
    }

    //recuperation de l'adresse complete (rue + postal +ville)
    private String getContactAddress(String id){
        String address="";
        Cursor addressCur = getContentResolver().query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,null,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                new String[]{id},null);
        while(addressCur.moveToNext()){
            address = addressCur.getString((addressCur
                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA)));
        }
        addressCur.close();
        return  address;

    }

    public void sms(String from, String body) {
        Intent i = new Intent(this,Pause.class);
        msg = body;
        num = from;
        startActivityForResult(i,65);
    }


    //gestion du menu de settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}