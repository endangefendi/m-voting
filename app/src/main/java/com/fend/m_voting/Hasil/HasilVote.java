package com.fend.m_voting.Hasil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fend.m_voting.Calon.Calon;
import com.fend.m_voting.Calon.ListCalon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fend.m_voting.R;

public class HasilVote extends AppCompatActivity {

    List<Calon> artists;
    ListView lvc;
    DatabaseReference databaseJadwal;
    String tglskr;
    TextView Texjumlah,Vote,Notvote;
    Float total;
    ProgressDialog  progressDialog;

    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasil_vote);
        lvc =  findViewById(R.id.lv_candidate);

        Texjumlah = findViewById(R.id.totvoter);
        Vote = findViewById(R.id.sudahvote);
        Notvote = findViewById(R.id.belumvote);

        artists = new ArrayList<>();
       /* lvc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calon ad = artists.get(position);
                Bundle bun= new Bundle();
                Intent in= new Intent(getBaseContext(),DetailHasil.class);
                bun.putString("no",ad.getNo_urut());
                bun.putString("nama",ad.getNama());
                bun.putString("suara",ad.getSuara());
                bun.putString("foto",ad.getFoto());

                in.putExtras(bun);
                startActivity(in);
            }
        });


        lvc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
*/


    }

    public void tampil(final Activity context, final ListView listView) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Candidate");
        artists = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Calon sepatu = postSnapshot.getValue(Calon.class);
                    //adding artist to the list
                    artists.add(sepatu);
                }

                //creating adapter
                ListCalon sepatuAdapter = new ListCalon(context, artists);
                //attaching adapter to the listview
                listView.setAdapter(sepatuAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final Date today = new Date();
        final String sekarang = sdf.format(today).toString();

        databaseJadwal = FirebaseDatabase.getInstance().getReference().child("Jadwal Pengumuman");
        databaseJadwal.orderByChild("tanggalpengumuman").equalTo(sekarang)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            setSemua();
                            setvote();
                            setnotvote();
                            masuk();
                        }else {
                            notif();
                        }
                    }
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        finish();


                    }
                });

    }
    private void masuk() {
        progressDialog.dismiss();
        tampil(this, lvc);
    }

    private void notif(){
        Button back;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HasilVote.this);
        dialogBuilder.setTitle("Peringatan!!!").setIcon(R.drawable.hasil);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.notif_belum_waktu_pengumuman, null);
        dialogBuilder.setView(dialogView);

        databaseJadwal = FirebaseDatabase.getInstance().getReference("Jadwal Pengumuman").child("Jadwal Pengumuman");
        databaseJadwal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView not;
                String nama = (String) dataSnapshot.child("tanggalpengumuman").getValue();
                not = dialogView.findViewById(R.id.setNotif);
                not.setText(nama.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();
        back = dialogView.findViewById(R.id.button_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                finish();
            }
        });
    }

    private void setSemua() {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Voter").orderByChild("nim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nim = (String) ds.child("nim").getValue();
                    if (nim != null) {
                        count = (count + 1);
                    }
                    Texjumlah.setText(String.valueOf(count));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setvote() {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Voter").orderByChild("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String status = (String) ds.child("status").getValue();
                    if (status!=null && status.equals("Vote")) {
                        count = count+1;
                    }
                    Vote.setText(String.valueOf(count));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setnotvote() {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Voter").orderByChild("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String status = (String) ds.child("status").getValue();
                    if (status!=null && status.equals("Not Vote")) {
                        count = count+1;
                    }
                    Notvote.setText(String.valueOf(count));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}