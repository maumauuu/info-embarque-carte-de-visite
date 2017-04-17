package com.CDV.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.CDV.MainActivity;
import com.CDV.R;
import com.CDV.dataBase.CarteDataSource;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


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

    public void scanner(View view){
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
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

        datasource.createCarte(name, Lastname,Email,phone,Address,City,Postal);


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
        if(result!=null){
            if(result.getContents()==null){

            } else{
                Toast.makeText(getActivity(), "Contact ajouté", Toast.LENGTH_SHORT).show();
                Fill(result);
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

    }

}
