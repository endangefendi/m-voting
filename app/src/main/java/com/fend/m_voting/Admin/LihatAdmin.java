package com.fend.m_voting.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fend.m_voting.R;


public class LihatAdmin extends AppCompatActivity {
    TextView nim,nama,password,email, telp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_admin);
        setTitle("Data Admin");

        nim= findViewById(R.id.edit_text_nim);
        password= findViewById(R.id.edit_text_password);
        nama= findViewById(R.id.edit_text_name);
        email= findViewById(R.id.edit_text_email);
        telp= findViewById(R.id.telp);
        Intent in=getIntent();
        Bundle bun=this.getIntent().getExtras();
        this.setTitle(bun.getString("email"));

        nim.setText(bun.getString("nim"));
        password.setText("*******");
        //password.setText(bun.getString("password"));
        nama.setText(bun.getString("nama"));
        email.setText(bun.getString("email"));
        telp.setText(bun.getString("telp"));

    }
}