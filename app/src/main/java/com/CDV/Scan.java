package com.CDV;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class Scan extends AppCompatActivity {

    private Button scan;
    private  String name;
    private  String Lastname;
    private  String Email;
    private  String phone;
    private  String Address;
    private  String City;
    private  String Postal;

    private CarteDataSource datasource;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scan = (Button) this.findViewById(R.id.Scan);
        final Activity activity = this;

        datasource = new CarteDataSource(this);
        datasource.open();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });

    }


    protected void Fill(IntentResult result){
        String c = result.getContents();
        name = "ffff";
        Lastname ="ggggg" ;
        Email = "ffffsdsd";
        phone = "22364126";
        Address = "hlglfmg";
        City = "rfdff" ;
        Postal = "26634";

        datasource.createCarte(name, Lastname,Email,phone,Address,City,Postal);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Log.d("Main", "Cancelled scan");
              //  Toast.makeText(this, "Cancelled: ", Toast.LENGTH_SHORT).show();
            } else{
                Log.d("Main", "Scanned" + result.getContents());
                Log.d("Main", "Scanned" + result.getFormatName());
               // Toast.makeText(this, "Scanned: "+ result.getContents(), Toast.LENGTH_SHORT).show();
                Fill(result);
                startActivity(new Intent(Scan.this, ContactActivity.class));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            startActivity(new Intent(Scan.this, ContactActivity.class));
        }

    }
}