package com.fend.m_voting.Hasil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fend.m_voting.R;
import com.squareup.picasso.Picasso;

public class DetailHasil extends AppCompatActivity {
    TextView no_urut,nama,suara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_hasil);

        no_urut= findViewById(R.id.edit_text_no);

        nama= findViewById(R.id.edit_text_nama);
        suara= findViewById(R.id.suara);
        ImageView imageView = findViewById(R.id.foto_calon);


        Intent in=getIntent();
        Bundle bun=this.getIntent().getExtras();
        this.setTitle("Candidate = "+bun.getString("no"));

        no_urut.setText(bun.getString("no"));
        nama.setText(bun.getString("nama"));
        suara.setText(bun.getString("suara"));

        Picasso.get()
                .load(bun.getString("foto"))
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

    }

}