package com.CDV;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editFullname;
    private EditText editEmail;
    private EditText editNumero;
    private EditText editAddress;
    private EditText editCity;
    private EditText editPostal;

    private Button buttonOk;

    private CarteDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.name);
        editFullname = (EditText) findViewById(R.id.fullname);
        editEmail = (EditText) findViewById(R.id.email);
        editNumero = (EditText) findViewById(R.id.numero);
        editAddress = (EditText) findViewById(R.id.address);
        editCity = (EditText) findViewById(R.id.city);
        editPostal = (EditText) findViewById(R.id.postal);

        datasource = new CarteDataSource(this);
        datasource.open();

        buttonOk = (Button) findViewById(R.id.save);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    datasource.createCarte(editName.getText().toString(), editFullname.getText().toString(), editEmail.getText().toString(),
                            editNumero.getText().toString(), editAddress.getText().toString(), editCity.getText().toString(), editPostal.getText().toString());

                    editName.setText("");
                    editFullname.setText("");
                    editEmail.setText("");
                    editNumero.setText("");
                    editAddress.setText("");
                    editCity.setText("");
                    editPostal.setText("");

                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                }

        });

    }

    @Override
    protected void onResume(){
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause(){
        datasource.close();
        super.onPause();
    }
}
