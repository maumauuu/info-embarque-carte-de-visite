package com.CDV;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.CDV.fragment.GestionContactFragment;
import com.CDV.fragment.ProfilFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String Name;
    private String LastName;
    private String Email;
    private String Phone;
    private String Address;
    private String City;
    private String Postal;

    private String num="5554";
    private String msg;

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
        Fragment fragment = new ProfilFragment();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("Profil");

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

            setTitle("Profil");
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



    public void contact(View view){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }




    public void getContact(Cursor cursor){
        String str_phone="";
        String email= "";
        String address="";
        cursor.moveToFirst();
        //recupere le nom et prenom u contact
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        //recupere l'indice des numeros de tel
        String id = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID)) + "";

        try {
            //recupere le tel du contact
            JSONObject phone = getContactPhones(id);
            //3: telephone travail
            str_phone = phone.getString("3");
            email = getContactEmail(id);
            address = getContactAddress(id);
        }catch (Exception e) {
            Log.v("ContactPicker", "Parsing contact failed: " + e.getMessage());
        }
        carte_contact = new Intent(this, CarteContact.class);
        carte_contact.putExtra("name",name);
        carte_contact.putExtra("phone",str_phone);
        carte_contact.putExtra("email",email);
        carte_contact.putExtra("address",address);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        //Code pour recuperer les infos du contact choisi
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            getContact(cursor);
            startActivity(carte_contact);

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


    //methode pour ajouter les contacts dans la BD du tel
    public void Ajouter_contact(ArrayList<ContentProviderOperation> ops, int rawContactInsertIndex, Activity act,
                                String Name, String LastName, String Phone, String Email, String Address, String Postal,
                                String City) {

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, Phone)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, TYPE_WORK).build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, LastName + " " + Name)
                .build());
        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, Email)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "2").build());


        //Postal Address

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                            /*.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, "Postbox")*/

                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, Address)

                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, City)

                            /*.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, "region")*/

                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, Postal)

                            /*.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, "country")*/

                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "2")


                .build());
        try {
            ContentProviderResult[] res = act.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

}