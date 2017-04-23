package com.CDV;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.CDV.util.Code;

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
