package com.CDV;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PhoneNumber extends AppCompatActivity {

    private EditText num;
    private String msg;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        num = (EditText) findViewById(R.id.phone);
        send = (Button) findViewById(R.id.Send);


        Intent i = getIntent();
        msg = i.getStringExtra("msg");

    }

    //envoie le sms contenant la carte de visite
    public void Sendsms(View view){
        SmsManager.getDefault().sendTextMessage(num.getText().toString(), null, msg, null, null);
        Toast.makeText(this, "SMS envoyé", Toast.LENGTH_SHORT).show();
    }

    //Demarre le sanner de qrcode
    public  void scanner(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                //on recupere lecontenu du qrcode
                String c = result.getContents();
                c = "5554";
                num.setText(c);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
