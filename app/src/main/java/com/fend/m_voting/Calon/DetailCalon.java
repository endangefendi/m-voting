package com.fend.m_voting.Calon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.widget.TextView;

import com.fend.m_voting.R;

public class DetailCalon extends AppCompatActivity {
    TextView no_urut,nama,angkatan,visi, misi, jml_suara, des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_calon);

        no_urut= findViewById(R.id.edit_text_no);
        angkatan= findViewById(R.id.edit_text_angkatan);
        nama= findViewById(R.id.edit_text_nama);
        visi= findViewById(R.id.edit_text_visi);
        misi= findViewById(R.id.edit_text_misi);
        des= findViewById(R.id.description);
        jml_suara= findViewById(R.id.suara);


        Intent in=getIntent();
        Bundle bun=this.getIntent().getExtras();
        this.setTitle("Candidate = "+bun.getString("no"));

        no_urut.setText(bun.getString("no"));
        nama.setText(bun.getString("nama"));
        angkatan.setText(bun.getString("angkatan"));
        visi.setText(bun.getString("visi"));
        misi.setText(bun.getString("misi"));
        des.setText(bun.getString("des"));
        jml_suara.setText(bun.getString("suara"));

    }

}

