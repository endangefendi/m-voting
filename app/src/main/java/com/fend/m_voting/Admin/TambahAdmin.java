package com.fend.m_voting.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.fend.m_voting.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class TambahAdmin extends AppCompatActivity implements View.OnClickListener{
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtnim;
    private EditText txtnama;
    private EditText Tphone;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_admin);

        setTitle("Add Admin App");
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.ButtonSave).setOnClickListener(this);

        txtEmailAddress = findViewById(R.id.edit_text_email);
        txtPassword = findViewById(R.id.edit_text_password);
        txtnama = findViewById(R.id.edit_text_name);
        txtnim = findViewById(R.id.edit_text_nim);
        Tphone = findViewById(R.id.telp);
    }

    public void konfir() {
        AlertDialog.Builder builder = new AlertDialog.Builder (com.fend.m_voting.Admin.TambahAdmin.this)
                .setTitle("Warning !!!")
                .setMessage("Do you want Save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        com.fend.m_voting.Admin.TambahAdmin.this.addAdmin();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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


    public void addAdmin() {
        final String phone = Tphone.getText().toString().trim();
        final String email = txtEmailAddress.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String name = txtnama.getText().toString().trim();
        final String nim = txtnim.getText().toString().trim();

        if (nim.length() != 9 ) {
            txtnim.setError("NIM not valid"); txtnim.requestFocus(); return;
        }
        if (nim.isEmpty()) {
            txtnim.setError(getString(R.string.input_error_nim)); txtnim.requestFocus(); return;
        }
        if (password.length() < 6) {
            txtPassword.setError(getString(R.string.input_error_password_length)); txtPassword.requestFocus(); return;
        }
        if (password.isEmpty()) {
            txtPassword.setError(getString(R.string.input_error_password)); txtPassword.requestFocus(); return;
        }
        if (name.isEmpty()) {
            txtnama.setError(getString(R.string.input_error_nama)); txtnama.requestFocus(); return;
        }
        if (email.isEmpty()) {
            txtEmailAddress.setError(getString(R.string.input_error_email)); txtEmailAddress.requestFocus(); return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmailAddress.setError(getString(R.string.input_error_email_invalid)); txtEmailAddress.requestFocus();return;
        }
        if (phone.isEmpty()) {
            Tphone.setError(getString(R.string.input_error_phone)); Tphone.requestFocus(); return;
        }
        if (phone.length() < 9 ) {
            Tphone.setError(getString(R.string.input_error_phone_invalid)); Tphone.requestFocus(); return;
        }
        if (phone.length() > 15){
            Tphone.setError(getString(R.string.input_error_phone_invalid)); Tphone.requestFocus(); return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(this,
                "Please wait...","Processing...", true);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Admin insert = new Admin(nim, password, name, email, phone);
                                                FirebaseDatabase.getInstance().getReference("Administrator")
                                                        .child(nim).setValue(insert);
                                                Toast.makeText(TambahAdmin.this,
                                                        "Registrasi sukses.. cek email untuk memverifikasi data Anda",
                                                        Toast.LENGTH_LONG).show();
                                                Intent back = new Intent();
                                                setResult(RESULT_OK, back);
                                                finish();
                                            } else {
                                                Toast.makeText(TambahAdmin.this,
                                                        getString(R.string.registration_fail), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    } );
                        } else {
                            Toast.makeText(TambahAdmin.this, task.getException()
                                    .getMessage(), Toast.LENGTH_LONG).show();
                        } progressDialog.dismiss();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                cancel();
                break;
            case R.id.ButtonSave:
                konfir();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Back !!!");
        builder.setMessage("Are you want to leave this page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                cancel();
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

    private void cancel() {
        txtEmailAddress.setText("");
        txtnama.setText("");
        txtPassword.setText("");
        Tphone.setText("");
        txtnim.setText("");
        Intent back = new Intent();
        setResult(RESULT_OK, back);
        finish();
    }
}
