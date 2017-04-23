package com.CDV.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.CDV.MainActivity;
import com.CDV.R;
import com.CDV.dataBase.CarteDataSource;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;

import java.util.ArrayList;


public class AddContactFragment extends Fragment {

    private  String name;
    private  String Lastname;
    private  String Email;
    private  String phone;
    private  String Address;
    private  String City;
    private  String Postal;

    private CarteDataSource datasource;


    public AddContactFragment() {
        // Required empty public constructor
    }



    protected void Fill(IntentResult result){
        // String c = result.getContents();
        String c = "Thomas,SGN, , ,3 rue des lilas,Nice,06000";
        String Tc[] = c.split(",");

        name = Tc[0];
        Lastname =Tc[1];
        Email = Tc[2];
        phone = Tc[3];
        Address = Tc[4];
        City = Tc[5] ;
        Postal = Tc[6];

        /*ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null).build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, "9X-XXXXXXXXX")
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.TYPE, "1").build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, "Mike Sullivan")
                .build());
        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, "abc@aho.com")
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "2").build());


        //Postal Address

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, "Postbox")

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, "street")

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, "city")

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, "region")

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, "postcode")

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, "country")

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "3")


                .build());


        //Organization details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, "Devindia")
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, "Developer")
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, "0")

                .build());
        //IM details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Im.DATA, "ImName")
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.Im.DATA5, "2")


                .build());
        try {
            ContentProviderResult[] res = getActivity().getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i("info", "OK!");*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        datasource = new CarteDataSource(getActivity());
        datasource.open();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_scan, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        Log.i("info", "OK1!");
        if(result!=null){
            if(result.getContents()==null){
                Log.i("info", "OK2!");
            } else{
                Toast.makeText(getActivity(), "Contact ajouté", Toast.LENGTH_SHORT).show();
                Fill(result);
                Log.i("info", "OK3!");
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

    }

}
