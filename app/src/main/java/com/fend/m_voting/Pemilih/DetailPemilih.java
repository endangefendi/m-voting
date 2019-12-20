package com.fend.m_voting.Pemilih;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fend.m_voting.R;

public class DetailPemilih extends AppCompatActivity {
    TextView nim,nama,password,email, telp, stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pemilih);

        nim= findViewById(R.id.edit_text_nim);
        password= findViewById(R.id.edit_text_password);
        nama= findViewById(R.id.edit_text_name);
        email= findViewById(R.id.edit_text_email);
        telp= findViewById(R.id.telp);
        stat= findViewById(R.id.status);
        // pin= findViewById(R.id.edit_text_pin);

        Intent in=getIntent();
        Bundle bun=this.getIntent().getExtras();
        this.setTitle("Voter = "+bun.getString("nim"));

        nim.setText(bun.getString("nim"));
        nama.setText(bun.getString("nama"));
        email.setText(bun.getString("email"));
        telp.setText(bun.getString("phone"));
        stat.setText(bun.getString("status"));
        //pin.setText(bun.getString("pin"));
    }
}
