package com.CDV.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


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
