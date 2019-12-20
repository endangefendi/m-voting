package com.fend.m_voting.Pemilih;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fend.m_voting.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class TampilQRCodePemilih extends AppCompatActivity {
    String nim;
    Button back;
    TextView etnim;
    ImageView qrImage;
    String TAG = "GenerateQRCode";
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_qrcode_pemilih);
        qrImage = findViewById(R.id.QR_Image);
        etnim = findViewById(R.id.nimET);

        Intent in = getIntent();
        Bundle bun = this.getIntent().getExtras();
        this.setTitle("QR Code");
        if (bun != null)
        {
            etnim.setText(" "+bun.getString("nim"));
            nim = bun.getString("nim");
        }

        QRGEncoder qrgEncoder = new QRGEncoder(nim, null, QRGContents.Type.TEXT, 700);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

        back = findViewById(R.id.button_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
