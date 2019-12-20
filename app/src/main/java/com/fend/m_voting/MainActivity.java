package com.fend.m_voting;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fend.m_voting.Hasil.HasilVote;
import com.fend.m_voting.Pemilih.Daftar;
import com.fend.m_voting.Pemilih.TampilQRCodePemilih;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    EditText n;
    String nim;
    String sta;
    DatabaseReference databaseVoter;
    DatabaseReference databaseJadwalRegis;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (firebaseAuth.getCurrentUser() != null) {
            Intent i = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(i);
        }

        findViewById(R.id.idabout).setOnClickListener(this);
        findViewById(R.id.idvote).setOnClickListener(this);
        findViewById(R.id.idhasil).setOnClickListener(this);
        findViewById(R.id.idregistrasi).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);


    }

   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idabout:
                about();
                break;
            case R.id.idvote:
                inputvote();
                break;
            case R.id.idhasil:
                Intent q4q = new Intent(getApplicationContext(), HasilVote.class);
                startActivity(q4q);
                break;
            case R.id.idregistrasi:
                progressDialog.show();
                cektanggal();
                break;
        }
    }

    private void inputvote() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.sudah_regis, null);
        dialogBuilder.setView(dialogView);
        final Button buttonv =  dialogView.findViewById(R.id.masuk);

        n=dialogView.findViewById(R.id.editText_nim);
        n.requestFocus();

        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nim = n.getText().toString().trim();

                if (nim.isEmpty()) {
                    n.setError(getString(R.string.input_error_kode));
                    n.requestFocus(); return;
                }

                if (nim.length()!= 9) {
                    n.setError(getString(R.string.input_error_kode_length));
                    n.requestFocus(); return;
                }

                databaseVoter = FirebaseDatabase.getInstance().getReference("Voter")
                        .child(n.getText().toString().trim());
                databaseVoter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nama = (String) dataSnapshot.child("nama").getValue();
                        sta = (String) dataSnapshot.child("status").getValue();
                        if (sta == null) {
                            n.setError(getString(R.string.no_data_nim));
                            n.requestFocus();
                        } else
                        if ((sta.equals("Not Vote"))){//Belum vote
                            Bundle bun = new Bundle();
                            Intent in = new Intent(getBaseContext(), TampilQRCodePemilih.class);
                            bun.putString("nim", nim);
                            in.putExtras(bun);
                            startActivity(in);
                        } else
                        if ((sta.equals("Vote"))){
                            n.setError("Cannot display QR Code ... NIM is already Vote");
                            n.requestFocus();
                        }else {

                            n.setError(getString(R.string.input_error_kode));
                            n.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    public void about() {
        Button back;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("About App").setIcon(R.drawable.hasil);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.about, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();
        back = dialogView.findViewById(R.id.button_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }


    protected void cektanggal() {
        databaseJadwalRegis = FirebaseDatabase.getInstance().getReference("Jadwal Registrasi").child("Jadwal Registrasi");
        databaseJadwalRegis.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tanggalmulai = String.valueOf(dataSnapshot.child("tanggalmulai").getValue());
                String tanggalakhir = String.valueOf(dataSnapshot.child("tanggalakhir").getValue());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date today = new Date();
                    //String sekarang = sdf.format(today).toString();
                    Date jadwalakhir = sdf.parse(tanggalakhir);
                    Date jadwalmulai = sdf.parse(tanggalmulai);
                    if (today.after(jadwalmulai) && today.before(jadwalakhir)) {
                        Intent qq = new Intent(getApplicationContext(), Daftar.class);
                        startActivity(qq);
                        progressDialog.dismiss();
                    } else if ((today.compareTo(jadwalmulai) >= 0 && today.compareTo(jadwalakhir) <= 0)
                            || (today.equals(tanggalakhir))) {
                        progressDialog.dismiss();
                        Intent qq = new Intent(getApplicationContext(), Daftar.class);
                        startActivity(qq);
                    } else notif();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void notif(){
        progressDialog.dismiss();
        Button back;

        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                MainActivity.this);
        dialogBuilder.setTitle("Peringatan!!!").setIcon(R.drawable.hasil);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.notif_bukan_waktu_regis, null);
        dialogBuilder.setView(dialogView);

        databaseJadwalRegis = FirebaseDatabase.getInstance().getReference("Jadwal Registrasi").child("Jadwal Registrasi");
        databaseJadwalRegis.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView awal,akhir;
                String tanggalmulai = String.valueOf(dataSnapshot.child("tanggalmulai").getValue());
                String tanggalakhir = String.valueOf(dataSnapshot.child("tanggalakhir").getValue());
                awal = dialogView.findViewById(R.id.settglmulai);
                akhir = dialogView.findViewById(R.id.settglakhir);
                akhir.setText(tanggalakhir);
                awal.setText(tanggalmulai);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.show();
        back = dialogView.findViewById(R.id.button_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    public void login() {
        progressDialog.dismiss();
        final EditText txtEmailLogin;
        TextView button3;
        final EditText txtPwd;
        Button button1, button2;
        FirebaseDatabase mDatabase;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Login Administrator").setIcon(R.drawable.user_dark);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        button1 = dialogView.findViewById(R.id.button_login);
        button2 = dialogView.findViewById(R.id.button_cancel);
        button3 = dialogView.findViewById(R.id.lupa_password);

        txtEmailLogin = dialogView.findViewById(R.id.edit_text_username);
        txtPwd = dialogView.findViewById(R.id.edit_text_password);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                progressDialog.show();
                forgot();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmailLogin.getText().toString();
                String password = txtPwd.getText().toString();

                if (email.isEmpty()) {
                    txtEmailLogin.setError("Input username");
                    txtEmailLogin.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    txtPwd.setError("Input password");
                    txtPwd.requestFocus();
                    return;
                } else {
                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,
                            "Please wait...", "Proccessing...", true);
                    (firebaseAuth.signInWithEmailAndPassword(email, password)).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            Toast.makeText(MainActivity.this, "Login successful",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance();
                                            Intent i3 = new Intent(getApplicationContext(), Dashboard.class);
                                            startActivity(i3);
                                        } else {
                                            Toast.makeText(MainActivity.this,
                                                    "Akun anda belum terverifikasi... " +
                                                            "Silahkan Cek Email Anda",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Log.e("ERROR", task.getException().toString());
                                        txtEmailLogin.requestFocus();
                                        Toast.makeText(MainActivity.this, task.getException()
                                                .getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

    private void forgot() {
        progressDialog.dismiss();
        final EditText edtEmail;
        final FirebaseAuth mAuth;
        Button btnBack;
        Button btnResetPassword;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Forgot Password?").setIcon(R.drawable.user_dark);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reset_password, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        edtEmail =  dialogView.findViewById(R.id.edt_reset_email);
        btnResetPassword = dialogView.findViewById(R.id.btn_reset_password);
        btnBack = dialogView.findViewById(R.id.btn_back);

        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Enter your email!");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("Warning " );
                                    builder.setMessage("Check email to reset your password!");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            login();
                                        }
                                    });
                                    android.app.AlertDialog alert = builder.create();
                                    alert.show();
                                    alert.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));

                                } else {
                                    edtEmail.setError("Fail to send reset password email!\nEmail not valid");
                                }
                            }
                        });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                progressDialog.show();
                login();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.about) {
            about();
            return true;
        }
        if (id == R.id.button_login) {
            progressDialog.show();
            login();
            return true;
        }

        if (id == R.id.close) {
            System.exit(0);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setTitle("Yakin ingin keluar?");
        builder.setMessage("Tekan (Yes) untuk keluar");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                dialog.cancel();
                Intent back = new Intent();
                setResult(RESULT_OK, back);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
        alert.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
    }

}
