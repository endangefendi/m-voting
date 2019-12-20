package com.fend.m_voting.Pemilih;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.fend.m_voting.R;

public class PemilihActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pemilih_activity);

        setTitle("Data Master Voter");

        findViewById(R.id.idBack).setOnClickListener(this);
        findViewById(R.id.idReadVoter).setOnClickListener(this);
        findViewById(R.id.idDeleteVoter).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idBack:
                Intent back = new Intent();
                setResult(RESULT_OK, back);
                finish();
                break;
            case R.id.idReadVoter:
                Intent i = new Intent(getApplicationContext(), TampilPemilih.class);
                startActivity(i);
                break;
            case R.id.idDeleteVoter:
                Intent ill = new Intent(getApplicationContext(), UAPemilih.class);
                startActivity(ill);
                break;

        }
    }

}


