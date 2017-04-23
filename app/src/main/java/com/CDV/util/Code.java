package com.CDV.util;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK;

public class Code {

    private String data;
    private QRCodeWriter writer;

    public Code(String data){
        this.data = data;
        this.writer = new QRCodeWriter();
    }

    public Bitmap dataToBitmap(){
        Bitmap bmp = null;
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = 512;
            int height = 512;
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y))
                        bmp.setPixel(x, y, Color.BLACK);
                    else
                        bmp.setPixel(x, y, Color.WHITE);
                }
            }
        } catch (WriterException e) {
            //Log.e("QR ERROR", ""+e);

        }

        return bmp;
    }






}
