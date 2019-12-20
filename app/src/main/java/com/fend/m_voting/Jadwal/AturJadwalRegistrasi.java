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
import android.widget.Toast;

import com.fend.m_voting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AturJadwalRegistrasi extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databaseRegis;
    ProgressDialog progressDialog;

    private EditText txttanggalawal,txttanggalakhir;

    String tanggalawal, bulanawal, bulanakhir, tanggalakhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atur_jadwal_registrasi);
        setTitle("Set Jadwal Registrasi");

        databaseRegis = FirebaseDatabase.getInstance().getReference("Jadwal Registrasi");
        Button btnTanggalMulai =  findViewById(R.id.btntanggalMulai);
        Button btnTanggalAkhir =  findViewById(R.id.btntanggalAkhir);

        txttanggalawal =  findViewById(R.id.txttanggalmulai);
        txttanggalakhir = findViewById(R.id.txttanggalakhir);

        btnTanggalMulai.setOnClickListener(this);
        btnTanggalAkhir.setOnClickListener(this);

        Button btnSetAkhir = findViewById(R.id.setAkhir);
        btnSetAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tgl1 = txttanggalawal.getText().toString();
                if (tgl1.isEmpty()) {
                    txttanggalawal.setError("Set Tanggal");
                    txttanggalawal.requestFocus();
                    return;
                }
                String tgl2 = txttanggalawal.getText().toString();
                if (tgl2.isEmpty()) {
                    txttanggalakhir.setError("Set Tanggal");
                    txttanggalakhir.requestFocus();
                    return;
                }
                progressDialog = ProgressDialog.show(com.fend.m_voting.Jadwal.AturJadwalRegistrasi.
                                this, "Please wait...",
                        "Processing...", true);
                set();
            }
        });
    }

    private void set() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(
                "Jadwal Registrasi");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(txttanggalawal.getText().toString()).exists()) {
                    progressDialog.dismiss();
                    finish();
                } else {
                    progressDialog.dismiss();
                    String id = dataSnapshot.getKey();
                    assert id != null;
                    String awal=txttanggalawal.getText().toString();
                    String akhir=txttanggalakhir.getText().toString();
                    JadwalRegistrasi insert = new JadwalRegistrasi(awal,akhir);
                    databaseRegis.child(id).setValue(insert);
                    Toast.makeText(AturJadwalRegistrasi.this,
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
        switch (v.getId()) {
            case R.id.btntanggalMulai:

                Calendar c = Calendar.getInstance();
                int mYearawal = c.get(Calendar.YEAR);
                int mMonthawal = c.get(Calendar.MONTH);
                int mDayawal = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogawal = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yearawal, int monthOfYearawal, int dayOfMonthawal) {
                                monthOfYearawal=monthOfYearawal+1;
                                if (dayOfMonthawal<10){
                                    tanggalawal ="0"+dayOfMonthawal;
                                }
                                else
                                    tanggalawal= String.valueOf(dayOfMonthawal);
                                if (monthOfYearawal < 10){
                                    bulanawal = "0"+monthOfYearawal;
                                }else
                                    bulanawal=String.valueOf(monthOfYearawal);
                                String set =(tanggalawal + "-" + bulanawal + "-" + yearawal);
                                txttanggalawal.setText(set);

                            }
                        }, mYearawal, mMonthawal, mDayawal);
                datePickerDialogawal.show();
                break;

            case R.id.btntanggalAkhir:

                Calendar ca = Calendar.getInstance();
                int mYearakhir = ca.get(Calendar.YEAR);
                int mMonthakhir = ca.get(Calendar.MONTH);
                int mDayakhir = ca.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogakhir = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yearakhir, int monthOfYearakhir, int dayOfMonthakhir) {
                                monthOfYearakhir=monthOfYearakhir+1;
                                if (dayOfMonthakhir<10){
                                    tanggalakhir ="0"+dayOfMonthakhir;
                                }
                                else
                                    tanggalakhir= String.valueOf(dayOfMonthakhir);
                                if (monthOfYearakhir < 10){
                                    bulanakhir = "0"+monthOfYearakhir;
                                }else
                                    bulanakhir=String.valueOf(monthOfYearakhir);
                                String set=(tanggalakhir + "-" + bulanakhir + "-" + yearakhir);
                                txttanggalakhir.setText(set);


                            }
                        }, mYearakhir, mMonthakhir, mDayakhir);
                datePickerDialogakhir.show();
                break;

        }
    }
}