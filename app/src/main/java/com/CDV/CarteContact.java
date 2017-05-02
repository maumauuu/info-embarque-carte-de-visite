package com.CDV;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.CDV.R;


public class CarteContact extends AppCompatActivity {


    private String origin;
    private String name;
    private String mail;
    private String phone;
    private String adresse;
    private String msg;
    private String[] msgs;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte_contact);

        i = getIntent();

        origin =i.getStringExtra("origin");


        if(origin == "SMS"){
            msg = i.getStringExtra("msg");
            msgs = msg.split("\n");
            name= msgs[1];
            mail = msgs[2];
            adresse = msgs[3];
            phone = i.getStringExtra("phone");



        }else {

            name = i.getStringExtra("name");
            phone = i.getStringExtra("phone");
            mail = i.getStringExtra("email");
            adresse = i.getStringExtra("address");
        }
        ((TextView) findViewById(R.id.Name)).setText(name);
        ((TextView) findViewById(R.id.phone)).setText(phone);
        ((TextView) findViewById(R.id.email)).setText(mail);
        ((TextView) findViewById(R.id.adresse)).setText(adresse);

    }
    
}
