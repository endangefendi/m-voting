package com.fend.m_voting.Calon;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class UACalon extends AppCompatActivity {
    List<Calon> artists; String f;
    ListView lvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_calon);
        lvc =  findViewById(R.id.lv_candidate);

        artists = new ArrayList<>();
        tampilCandidate(this, lvc);
        lvc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calon ad = artists.get(position);
                f = ad.getFoto();
                updateordelete(ad.getNo_urut(), ad.getNama(), ad.getAngkatan(), ad.getVisi(), ad.getMisi(),  ad.getDes(),  ad.getFoto(), ad.getSuara());
                return;
            }
        });
        tampilCandidate(this, lvc);
    }

    public void updateordelete(final String no, String nama, String angkatan, String visi,
                               String misi, String deskripsi, String foto, String suara) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.uacalon, null);
        dialogBuilder.setView(dialogView);

        final EditText no_urut =  dialogView.findViewById(R.id.edit_text_no);
        final EditText namacalon =  dialogView.findViewById(R.id.edit_text_nama);
        final EditText ang =  dialogView.findViewById(R.id.edit_text_angkatan);
        final EditText v =  dialogView.findViewById(R.id.edit_text_visi);
        final EditText m =  dialogView.findViewById(R.id.edit_text_misi);
        final EditText des =  dialogView.findViewById(R.id.description);
        final EditText s =  dialogView.findViewById(R.id.suara);

        final Button buttonUpdate =  dialogView.findViewById(R.id.buttonUpdateCandidate);
        final Button buttonDelete =  dialogView.findViewById(R.id.buttonDeleteCandidate);

        no_urut.setText(no); namacalon.setText(nama); ang.setText(angkatan);
        v.setText(visi); m.setText(misi); s.setText(suara);
        des.setText(deskripsi);

        s.setEnabled(false); no_urut.setEnabled(false);
        dialogBuilder.setTitle("Opsi No Urut "+no);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no = no_urut.getText().toString().trim();
                String nama = namacalon.getText().toString().trim();
                String angkatan = ang.getText().toString().trim();
                String visi = v.getText().toString().trim();
                String misi = m.getText().toString().trim();
                String suara = s.getText().toString().trim();
                String d = des.getText().toString().trim();

                if (!TextUtils.isEmpty(no)) {
                    UbahCandidate(no, nama, angkatan, visi, misi, d, f, suara);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(com.fend.m_voting.Calon.UACalon.this)
                        .setTitle("Warning !!!")
                        .setMessage("Are you sure delete this data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                hapusCandidate(no);
                                b.dismiss();
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

    private boolean hapusCandidate(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(
                "Candidate").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Delete successfully",
                Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean UbahCandidate(String x, String y, String z, String a, String b,
                                  String c, String d, String e) {
        DatabaseReference dR = FirebaseDatabase.getInstance().
                getReference("Candidate").child(x);

        Calon artist = new Calon(x, y, z, a, b, c, d, e );
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
        return true;
    }

    public void tampilCandidate(final Activity context, final ListView listView) {
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
                CalonList sepatuAdapter = new CalonList(context, artists);
                //attaching adapter to the listview
                listView.setAdapter(sepatuAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}