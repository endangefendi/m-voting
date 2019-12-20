package com.fend.m_voting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fend.m_voting.Admin.AdminActivity;
import com.fend.m_voting.Calon.CalonActivity;
import com.fend.m_voting.Jadwal.AturJadwalRegistrasi;
import com.fend.m_voting.Jadwal.AturPengumuman;
import com.fend.m_voting.Pemilih.PemilihActivity;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Dashboard extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        String t = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        setTitle(" Administrator");
        TextView user = findViewById(R.id.user);
        user.setText(t);
        findViewById(R.id.idcalon).setOnClickListener(this);
        findViewById(R.id.idadmin).setOnClickListener(this);
        findViewById(R.id.idpemilih).setOnClickListener(this);
        findViewById(R.id.set).setOnClickListener(this);
        findViewById(R.id.setJadwalRegis).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.logout);

    }

    private void konfir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Warning !!!");
        builder.setMessage("Do you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                logout();
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
        alert.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));    }
    @Override
    public void onBackPressed() {
        konfir();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idcalon:
                Intent i1 = new Intent(getApplicationContext(), CalonActivity.class);
                startActivity(i1);
                break;
            case R.id.idpemilih:
                Intent i2 = new Intent(getApplicationContext(), PemilihActivity.class);
                startActivity(i2);
                break;
            case R.id.idadmin:
                Intent i3 = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(i3);
                break;
            case R.id.set:
                Intent i4 = new Intent(getApplicationContext(), AturPengumuman.class);
                startActivity(i4);
                break;
            case R.id.setJadwalRegis:
                Intent i5 = new Intent(getApplicationContext(), AturJadwalRegistrasi.class);
                startActivity(i5);
                break;
            case R.id.logout:
                konfir();
                break;
        }
    }

    private void logout(){
        Toast.makeText(Dashboard.this, "Logout Berhasil.", Toast.LENGTH_LONG).show();
        Intent logout = new Intent();
        setResult(RESULT_OK, logout);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            konfir();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}