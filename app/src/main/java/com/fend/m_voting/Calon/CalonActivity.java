package com.fend.m_voting.Calon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import com.fend.m_voting.R;

public class CalonActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calon_activity);
        setTitle("Data Master Candidate");

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.idAddCandidate).setOnClickListener(this);
        findViewById(R.id.idReadCandidate).setOnClickListener(this);
        findViewById(R.id.idDeleteVoter).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                Intent back = new Intent();
                setResult(RESULT_OK, back);
                finish();
                break;
            case R.id.idAddCandidate:
                Intent in = new Intent(getBaseContext(), TambahCalon.class);
                startActivity(in);
                break;
            case R.id.idReadCandidate:
                Intent i1n = new Intent(getBaseContext(), LihatCalon.class);
                startActivity(i1n);
                break;
            case R.id.idDeleteVoter:
                Intent i11n = new Intent(getBaseContext(), UACalon.class);
                startActivity(i11n);
                break;
        }
    }
}
