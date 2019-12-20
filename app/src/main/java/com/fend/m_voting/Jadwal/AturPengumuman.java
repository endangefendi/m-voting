package com.fend.m_voting.Jadwal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fend.m_voting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import android.widget.Toast;

public class AturPengumuman extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databasePengumuman;
    ProgressDialog progressDialog;

    private EditText txttanggal;
    String bulan,tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atur_pengumuman);
        setTitle("Set Jadwal Pengumuman");

        databasePengumuman = FirebaseDatabase.getInstance().getReference("Jadwal Pengumuman");
        Button btntanggalmulai = findViewById(R.id.btntanggAkhir);

        txttanggal = findViewById(R.id.txttanggalakhir);

        btntanggalmulai.setOnClickListener(this);


        Button btnsetMulai =  findViewById(R.id.setMulai);
        btnsetMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tgl=txttanggal.getText().toString();
                if (tgl.isEmpty()) {
                     txttanggal.setError("Set Tanggal"); txttanggal.requestFocus();
                     return;
                }
                progressDialog = ProgressDialog.show(com.fend.m_voting.Jadwal.AturPengumuman.
                                this, "Please wait...",
                        "Processing...", true);
                set();
            }
        });
    }

    private void set() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(
                "Jadwal Pengumuman");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(txttanggal.getText().toString()).exists()){
                    progressDialog.dismiss();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    String id = dataSnapshot.getKey();
                    assert id != null;
                    String setjadwal=txttanggal.getText().toString();
                    Pengumuman insert = new Pengumuman(setjadwal);
                    databasePengumuman.child(id).setValue(insert);
                    Toast.makeText(AturPengumuman.this,
                            "Data berhasil di set", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btntanggAkhir) {
            int mYear, mMonth, mDay;

            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            monthOfYear = monthOfYear + 1;
                            if (dayOfMonth < 10) {
                                tanggal = "0" + dayOfMonth;
                            } else
                                tanggal = String.valueOf(dayOfMonth);
                            if (monthOfYear < 10) {
                                bulan = "0" + monthOfYear;
                            } else
                                bulan = String.valueOf(monthOfYear);
                            String set= (tanggal + "-" + bulan + "-" + year);
                            txttanggal.setText(set);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}