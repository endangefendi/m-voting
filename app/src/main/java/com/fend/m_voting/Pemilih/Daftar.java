package com.fend.m_voting.Pemilih;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fend.m_voting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


public class Daftar extends AppCompatActivity {
    DatabaseReference databaseVoter;
    EditText edNIM,edNama,edEmail,edPhone;
    Button save,back;
    ProgressDialog progressDialog;
    String nim, nama, email, phone;
    String status = "Not Vote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);
        setTitle("Register");

        edNIM =findViewById(R.id.nimVoter);
        edNama =findViewById(R.id.nameVoter);
        edPhone =findViewById(R.id.telpVoter);
        edEmail =findViewById(R.id.emailVoter);
        //edPin =findViewById(R.id.pinVoter);

        databaseVoter = FirebaseDatabase.getInstance().getReference("Voter");

        back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent();
                setResult(RESULT_OK, back);
                finish();
            }
        });
        save = findViewById(R.id.ButtonSave);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nim = edNIM.getText().toString().trim();
                nama = edNama.getText().toString().trim();
                email = edEmail.getText().toString().trim();
                phone = edPhone.getText().toString().trim();

                cek();
            }
        });

    }
    public void konfir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sure your data is correct?")
                .setMessage("Press (Yes) to register")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        progressDialog = ProgressDialog.show(com.fend.m_voting.Pemilih.Daftar.
                                        this, "Please wait...",
                                "Processing...", true);
                        regis();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
        alert.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
    }

    private void cek() {
        if (nim.isEmpty()) {
            edNIM.setError(getString(R.string.input_error_nim)); edNIM.requestFocus();
            return;
        }
        if (nim.length() != 9) {
            edNIM.setError(getString(R.string.input_error_nim_length)); edNIM.requestFocus();
            return;
        }

        if (nama.isEmpty()) {
            edNama.setError(getString(R.string.input_error_nama)); edNama.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            edEmail.setError(getString(R.string.input_error_email));
            edEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError(getString(R.string.input_error_email_invalid)); edEmail.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            edPhone.setError(getString(R.string.input_error_phone)); edPhone.requestFocus();
            return;
        }

        if (phone.length() < 9 ) {
            edPhone.setError(getString(R.string.input_error_phone_invalid)); edPhone.requestFocus();
            return;
        }
        if (phone.length() > 15 ) {
            edPhone.setError(getString(R.string.input_error_phone_invalid)); edPhone.requestFocus();
            return;
        }

        databaseVoter = FirebaseDatabase.getInstance().getReference().child("Voter");
        databaseVoter.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            edEmail.setError(email+" already exists");
                            edEmail.requestFocus();
                        } else {
                            ceknope();
                        }
                    }
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        edEmail.setError(email+" already exists");
                        edEmail.requestFocus();
                    }
                });
    }

    private void ceknope() {
        databaseVoter.orderByChild("phone").equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            edPhone.setError(phone+" already exists");
                            edPhone.requestFocus();
                        } else {
                            konfir();
                        }
                    }
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        edPhone.setError(phone+" already exists");
                        edPhone.requestFocus();

                    }
                });
    }

    public void regis() {
        progressDialog.show();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Voter");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(edNIM.getText().toString()).exists()){
                    progressDialog.dismiss();
                    edNIM.setError("NIM already exists");
                    edNIM.requestFocus();
                }
                else{
                    progressDialog.dismiss();
                    Pemilih insert = new Pemilih(nim,nama,email,phone,status);
                    databaseVoter.child(nim).setValue(insert);
                    Button back;
                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog
                            .Builder(Daftar.this);
                    dialogBuilder.setTitle("Registration Success").setIcon(R.drawable.sukses);

                    LayoutInflater inflater = getLayoutInflater();
                    @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.notif_regis_sukses, null);
                    dialogBuilder.setView(dialogView);
                    final android.support.v7.app.AlertDialog b = dialogBuilder.create();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });
    }



}