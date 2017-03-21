package com.CDV;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContactItemActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvFullName;
    private TextView tvEmail;
    private TextView tvNum;
    private TextView tvAddress;
    private TextView tvCity;
    private TextView tvPostal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_item);

        Intent i = getIntent();

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvNum = (TextView) findViewById(R.id.tvNum);
        tvName = (TextView) findViewById(R.id.tvName);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvPostal = (TextView) findViewById(R.id.tvPostal);


        tvEmail.setText(i.getStringExtra("email"));
        tvNum.setText(i.getStringExtra("num"));
        tvName.setText(i.getStringExtra("name"));
        tvFullName.setText(i.getStringExtra("fullname"));
        tvAddress.setText(i.getStringExtra("address"));
        tvCity.setText(i.getStringExtra("city"));
        tvPostal.setText(i.getStringExtra("postal"));

        tvNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvNum.getText().toString() ));

                startActivity(appel);
            }
        });


    }
}