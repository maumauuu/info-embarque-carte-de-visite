package com.CDV;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.CDV.R;


public class CarteContact extends AppCompatActivity {

    private String name;
    private String mail;
    private String phone;
    private String adresse;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte_contact);

        i = getIntent();
        name = i.getStringExtra("name");
        phone = i.getStringExtra("phone");
        mail= i.getStringExtra("email");
        adresse = i.getStringExtra("address");
        ((TextView) findViewById(R.id.Name)).setText(name);
        ((TextView) findViewById(R.id.phone)).setText(phone);
        ((TextView) findViewById(R.id.email)).setText(mail);
        ((TextView) findViewById(R.id.adresse)).setText(adresse);

    }
}
