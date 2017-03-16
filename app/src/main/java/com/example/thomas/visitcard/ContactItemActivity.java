package com.example.thomas.visitcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactItemActivity extends AppCompatActivity {

    private TextView tvEmail;
    private TextView tvNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_item);

        Intent i = getIntent();

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvNum = (TextView) findViewById(R.id.tvNumero);

        tvEmail.setText(i.getStringExtra("email"));
        tvNum.setText(i.getStringExtra("num"));

    }
}