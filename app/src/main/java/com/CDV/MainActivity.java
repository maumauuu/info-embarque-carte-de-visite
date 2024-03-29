package com.CDV;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.CDV.dataBase.Carte;
import com.CDV.dataBase.CarteDataSource;
import com.CDV.dataBase.Image;
import com.CDV.fragment.GestionContactFragment;
import com.CDV.util.RefreshEvent;
import com.CDV.util.RoundedImageView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ACTION_MMS_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static final String MMS_DATA_TYPE = "application/vnd.wap.mms-message";

    private String Name;
    private String LastName;
    private String Email;
    private String Phone;
    private String Address;
    private String City;
    private String Postal;

    private RoundedImageView imageMenu;
    private TextView prenomMenu;
    private TextView nomMenu;
    private TextView mailMenu;
    private TextView numMenu;
    private TextView adresseMenu;

    private TextView textName;
    private TextView textNumero;
    private TextView textEmail;
    private TextView textAdress;
    private RoundedImageView img;


    private String expectedPrefix = "CDV";
    private String msg;
    private String num;

    //nulero a transmettre au fragment apres le scan
    private String c;

    private Intent carte_contact;
    private final int CONTACT_PICKER_RESULT= 2017;
    private final int PICK_IMAGE = 25;
    private final int MODIFICATION = 10;

    private CarteDataSource dataSource;

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



        imageMenu = (RoundedImageView) findViewById(R.id.photo);
        prenomMenu = (TextView) findViewById(R.id.textView5);
        nomMenu = (TextView) findViewById(R.id.textView6);
        mailMenu = (TextView) findViewById(R.id.textView7);
        numMenu = (TextView) findViewById(R.id.textView8);
        adresseMenu = (TextView) findViewById(R.id.textView9);

        dataSource = new CarteDataSource(this);

        dataSource.open();
        List<Carte> cartes = dataSource.getAllProfil();
        if (cartes.size() != 0) {
            Carte carte = cartes.get(cartes.size()-1);
            prenomMenu.setText(carte.getName());
            nomMenu.setText(carte.getFullname());
            mailMenu.setText(carte.getEmail());
            numMenu.setText(carte.getNumero());
            adresseMenu.setText(carte.getAddress()+", "+carte.getPostal()+", "+carte.getCity());
        }

        List<Image> images = dataSource.getAllImage();
        if (images.size() != 0) {
            Image image = images.get(images.size()-1);

            imageMenu.setImageBitmap(BitmapFactory.decodeFile(image.getChemin()));

        }
        dataSource.close();

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundl = new Bundle();
        bundl.putInt("pos", 0);
        Fragment fragment = new GestionContactFragment();
        fragment.setArguments(bundl);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("VisitCard");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    }

    String getExpectedPrefix() {
        return expectedPrefix;
    }


    public void choosePicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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



    public void Scanner(View view){
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
        img = (RoundedImageView) findViewById(R.id.imgContact);

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

            getContactPhoto(id);

        }catch (Exception e) {
            Log.v("ContactPicker", "Parsing contact failed: " + e.getMessage());
        }
    }

    private void getContactPhoto(String id) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

            if (inputStream != null) {
                System.out.println(inputStream.toString());
                photo = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(photo);
            } else{
                img.setBackgroundColor(getResources().getColor(R.color.black));
            }


            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 65) {
            Log.d("read", "pause");
            if (resultCode == 0) {
                //Si skip on ne fait  rien
            }

            if (resultCode == 1) {
                Log.d("read", "llll");
                carte_contact = new Intent(this, CarteContact.class);
                carte_contact.putExtra("msg", msg);
                carte_contact.putExtra("origin", "SMS");
                carte_contact.putExtra("phone", num);
                startActivity(carte_contact);
            }
        }

        //Code pour recuperer les infos du contact choisi
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            RelativeLayout carteProfilOther = (RelativeLayout) findViewById(R.id.carte_other);
            carteProfilOther.setVisibility(LinearLayout.VISIBLE);
            getContact(cursor);
         }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                //on recupere lecontenu du qrcode
                c = result.getContents();
            }

        }

        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){

            Uri uri = data.getData();

            dataSource.open();
            dataSource.createImage(getRealPathFromURI(this, uri));
            dataSource.close();

        }

        if(requestCode == MODIFICATION){
            dataSource.open();
            List<Carte> cartes = dataSource.getAllProfil();
            if (cartes.size() != 0) {
                Carte carte = cartes.get(cartes.size()-1);
                prenomMenu.setText(carte.getName());
                nomMenu.setText(carte.getFullname());
                mailMenu.setText(carte.getEmail());
                numMenu.setText(carte.getNumero());
                adresseMenu.setText(carte.getAddress()+", "+carte.getPostal()+", "+carte.getCity());
            }

            List<Image> images = dataSource.getAllImage();
            if (images.size() != 0) {
                Image image = images.get(images.size()-1);

                imageMenu.setImageBitmap(BitmapFactory.decodeFile(image.getChemin()));

            }
            dataSource.close();
        }
    }




    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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


    @Override
    public void onResume(){
        super.onResume();
        EventBus.getDefault().post(new RefreshEvent(c));

    }


}