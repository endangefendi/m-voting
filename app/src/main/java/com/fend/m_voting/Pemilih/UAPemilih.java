package com.fend.m_voting.Pemilih;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import com.fend.m_voting.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class UAPemilih extends AppCompatActivity {
    List<Pemilih> artists;
    ListView lvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampil_pemilih);


        lvv =  findViewById(R.id.lv_voter);
        artists = new ArrayList<>();
        tampilVoter(this, lvv);
        lvv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pemilih obt = artists.get(i);
                UpdateDelete(obt.getNIM(), obt.getName(), obt.getEmail(),
                        obt.getPhone(), obt.getStatus());
                return;
            }
        });
    }

    public void UpdateDelete(final String nim, String nama,
                             String emial, String phone, final String status) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.uapemilih, null);
        dialogBuilder.setView(dialogView);

        final EditText ednim =  dialogView.findViewById(R.id.nimVoter);
        final EditText ednama =  dialogView.findViewById(R.id.nameVoter);
        final EditText edemail =  dialogView.findViewById(R.id.emailVoter);
        final EditText edphone =  dialogView.findViewById(R.id.telpVoter);
        final EditText edstatus =  dialogView.findViewById(R.id.status);
        //final EditText edPin =  dialogView.findViewById(R.id.pin);

        final Button buttonUpdate =  dialogView.findViewById(R.id.buttonUpdateVoter);
        final Button buttonDelete =  dialogView.findViewById(R.id.buttonDeleteVoter);

        ednim.setText(nim); ednama.setText(nama); edemail.setText(emial);
        edphone.setText(phone); edstatus.setText(status); //edPin.setText(pin);
        ednim.setEnabled(false);

        dialogBuilder.setTitle(nim);
        final AlertDialog b = dialogBuilder.create(); b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nim = ednim.getText().toString().trim();
                String nama = ednama.getText().toString().trim();
                String email = edemail.getText().toString().trim();
                String phone = edphone.getText().toString().trim();
                String status = edstatus.getText().toString().trim();
                //String pin = edPin.getText().toString().trim();

                if (!TextUtils.isEmpty(nim)) {
                    UbahDelete(nim, nama, email, phone, status);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(com.fend.m_voting.Pemilih.UAPemilih.this)
                        .setTitle("Warning !!!")
                        .setMessage("Are you sure delete this data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                final DatabaseReference dR = FirebaseDatabase.getInstance().
                                        getReference("Voter").child(nim);
                                dR.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String s =dataSnapshot.child("status").getValue().toString();
                                        if (s.equals("Vote")){
                                            Toast.makeText(getApplicationContext(), "Status data is not deleted",
                                                    Toast.LENGTH_LONG).show();
                                            //b.dismiss();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Delete successfully",
                                                    Toast.LENGTH_LONG).show();
                                            dR.removeValue();
                                            b.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "Data is not exist",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show().getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
            }
        });
    }

    private boolean UbahDelete(String x, String y, String z, String a, String b) {
        DatabaseReference dR = FirebaseDatabase.getInstance().
                getReference("Voter").child(x);
        Pemilih artist = new Pemilih(x, y, z, a, b);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Success",
                Toast.LENGTH_LONG).show();
        return true;
    }


    public void tampilVoter(final Activity context, final ListView listView) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Voter");
        artists = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Pemilih sepatu = postSnapshot.getValue(Pemilih.class);
                    //adding artist to the list
                    artists.add(sepatu);
                }

                //creating adapter
                PemilihList sepatuAdapter = new PemilihList(context, artists);
                //attaching adapter to the listview
                listView.setAdapter(sepatuAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
