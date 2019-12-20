package com.fend.m_voting.Pemilih;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fend.m_voting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class TampilPemilih extends AppCompatActivity {
    List<Pemilih> voters;
    ListView lvv;
    String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampil_pemilih);

        lvv = findViewById(R.id.lv_voter);
        voters = new ArrayList<>();

        tampilVoter(this, lvv);

        lvv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pemilih obt = voters.get(position);
                nim = obt.getNIM();

                Bundle bun = new Bundle();
                Intent in = new Intent(getBaseContext(), DetailPemilih.class);
                bun.putString("nim", obt.getNIM()); bun.putString("nama", obt.getName());
                bun.putString("email", obt.getEmail()); bun.putString("phone", obt.getPhone());
                bun.putString("status", obt.getStatus());// bun.putString("pin", obt.getPin());
                in.putExtras(bun);
                startActivity(in);
            }
        });
        tampilVoter(this, lvv);
    }

    public void tampilVoter(final Activity context, final ListView listView) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Voter");
        voters = new ArrayList<>();
        mRef.orderByChild("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                voters.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pemilih vt = postSnapshot.getValue(Pemilih.class);
                    voters.add(vt);
                }
                PemilihList vtlist = new PemilihList(context, voters);
                listView.setAdapter(vtlist);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
