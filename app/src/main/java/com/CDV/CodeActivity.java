package com.CDV;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.CDV.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;

public class CodeActivity extends AppCompatActivity {

    private ImageView image;
    private Code code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        Intent i = getIntent();

        image = (ImageView) findViewById(R.id.imageView);
        code = new Code(i.getStringExtra("data"));

        image.setImageBitmap(code.dataToBitmap());
    }
}
