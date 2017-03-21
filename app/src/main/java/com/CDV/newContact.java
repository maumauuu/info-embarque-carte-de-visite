package com.CDV;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class newContact extends AppCompatActivity{
        private Button add;
        private Button see;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        add = (Button) findViewById(R.id.AddContact);
        see = (Button) findViewById(R.id.SeeContact);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(newContact.this, Scan.class));

            }
        });

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(newContact.this, ContactActivity.class));

            }
        });
    }

}
