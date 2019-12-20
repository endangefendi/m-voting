package com.fend.m_voting.Calon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.fend.m_voting.R;

public class LihatCalon extends AppCompatActivity {
    List<Calon> artists;
    ListView lvc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_calon);

        lvc =  findViewById(R.id.lv_candidate);

        artists = new ArrayList<>();
        tampilCanidate(this, lvc);

        lvc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        lvc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calon ad = artists.get(position);
                Bundle bun= new Bundle();
                Intent in= new Intent(getBaseContext(),DetailCalon.class);
                bun.putString("no",ad.getNo_urut()); bun.putString("nama",ad.getNama());
                bun.putString("angkatan",ad.getAngkatan()); bun.putString("visi",ad.getVisi());
                bun.putString("misi",ad.getMisi()); bun.putString("suara", ad.getSuara());
                bun.putString("des",ad.getDes());
                in.putExtras(bun); startActivity(in);
            }
        });
        tampilCanidate(this, lvc);
    }

    public void tampilCanidate(final Activity context, final ListView listView) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Candidate");
        artists = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Calon sepatu = postSnapshot.getValue(Calon.class);
                    artists.add(sepatu);
                }
                CalonList sepatuAdapter = new CalonList(context, artists);
                listView.setAdapter(sepatuAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}