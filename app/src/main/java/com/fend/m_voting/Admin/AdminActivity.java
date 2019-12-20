package com.fend.m_voting.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
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

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listViewAdmin;
    DatabaseReference databaseAdmin;
    List<Admin> adminList;
    TextView usertampil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        setTitle("Data Master Admin");
        databaseAdmin = FirebaseDatabase.getInstance().getReference("Administrator");
        findViewById(R.id.idAddAdmin).setOnClickListener(this);
        findViewById(R.id.button_back).setOnClickListener(this);
        usertampil= findViewById(R.id.usertampil);
        listViewAdmin = findViewById(R.id.listViewAdmin);
        adminList = new ArrayList<>();
        listViewAdmin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Admin obt = adminList.get(i);
                PilihiUpdateDelete(obt.getNIM(), obt.getPassword(), obt.getName(),
                        obt.getEmail(), obt.getPhone());
                return true;
            }
        });

        listViewAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Admin obt = adminList.get(position);
                Bundle bun= new Bundle();
                Intent in= new Intent(getBaseContext(),LihatAdmin.class);
                bun.putString("nim",obt.getNIM());
                //bun.putString("password",obt.getPassword());
                bun.putString("nama",obt.getName()); bun.putString("email",obt.getEmail());
                bun.putString("telp",obt.getPhone());
                in.putExtras(bun); startActivity(in);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        final String t = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        usertampil.setText(t);
        databaseAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminList.clear();
                for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                    Admin admin = adminSnapshot.getValue(Admin.class);
                    adminList.add(admin);  }
                AdminList adapter = new AdminList(AdminActivity.this, adminList);
                listViewAdmin.setAdapter(adapter);  }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void PilihiUpdateDelete(final String nim, String password, final String nama,
                                   String email, String telp  ) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new
                android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.uadmin, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNIM = dialogView.findViewById(R.id.edit_text_nim);
        final EditText editTextNama = dialogView.findViewById(R.id.edit_text_name);
        final EditText editTextEmail = dialogView.findViewById(R.id.edit_text_email);
        final EditText editTextPhone = dialogView.findViewById(R.id.telp);
        final EditText editTextPassword = dialogView.findViewById(R.id.edit_text_password);

        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdateAdmin);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeleteAdmin);
        editTextNIM.setText(nim); editTextPassword.setText(password);
        editTextNama.setText(nama); editTextEmail.setText(email);
        editTextPhone.setText(telp);

        dialogBuilder.setTitle(email);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ni = editTextNIM.getText().toString().trim();
                String pas = editTextPassword.getText().toString().trim();
                String na = editTextNama.getText().toString().trim();
                String em = editTextEmail.getText().toString().trim();
                String te = editTextPhone.getText().toString().trim();

                if (!TextUtils.isEmpty(nim)) {
                    UbahAdmin(ni, pas, na, em, te); b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        AdminActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Warning !!!");
                builder.setMessage("Are you want deleting data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        //hapusAdmin(nim);
                        //b.dismiss();
                        //final String t = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                        final DatabaseReference dR = FirebaseDatabase.getInstance().
                                getReference("Administrator").child(nim);
                        dR.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String emailuser =dataSnapshot.child("email").getValue().toString();
                                if (emailuser.equals(usertampil.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Admin not deleted", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    dR.removeValue();
                                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                                    b.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
        });
    }

    private boolean UbahAdmin(String x, String y, String z, String a, String b) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(
                "Administrator").child(x);
        Admin artist = new Admin(x, y, z, a, b);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_LONG).show();
        return true;
    }


    private boolean hapusAdmin(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(
                "Administrator").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_LONG).show();
        return true;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                Intent back = new Intent(); setResult(RESULT_OK, back);
                finish();
                break;
            case R.id.idAddAdmin:
                Intent i = new Intent(getApplicationContext(), TambahAdmin.class);
                startActivity(i);
                break;
        }
    }

}

