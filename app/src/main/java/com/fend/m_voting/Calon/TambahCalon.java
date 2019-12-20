package com.fend.m_voting.Calon;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.fend.m_voting.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class TambahCalon extends AppCompatActivity {
    DatabaseReference databaseCalon;
    EditText no_urut, namacalon, calonangkatan, visicalon, misicalon, deckripsi;
    Button back,save;
    private ImageButton Pilih;
    private ImageView ImageContainer;
    public  String a;
    String no, nama, angkatan, visi, misi, des, foto, suara;

    //Deklarasi Variable StorageReference
    private StorageReference reference;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    //Kode permintaan untuk memilih metode pengambilan gamabr
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    private Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_calon);
        databaseCalon = FirebaseDatabase.getInstance().getReference("Candidate");
        no_urut = findViewById(R.id.no_urut);
        namacalon = findViewById(R.id.name_candidate);
        calonangkatan = findViewById(R.id.angkatan);
        visicalon = findViewById(R.id.visi);
        deckripsi = findViewById(R.id.description);
        misicalon = findViewById(R.id.misi);

        Pilih = findViewById(R.id.select_Image);
        Pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        ImageContainer = findViewById(R.id.imageContainer);
        progressBar = findViewById(R.id.progressBar);

        //Mendapatkan Referensi dari Firebase Storage
        reference = FirebaseStorage.getInstance().getReference("Candidate");
        back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kembali();
            }
        });

        save = findViewById(R.id.ButtonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInput();

            }
        });
        progressDialog = new ProgressDialog(this);

    }
    private String getFileExtension(Uri filePath) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(filePath));
    }

    private void konfir() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Success !!!");
        builder.setMessage("Do you want to save data again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                no_urut.setText("");
                namacalon.setText("");
                calonangkatan.setText("");
                visicalon.setText("");
                misicalon.setText("");
                deckripsi.setText("");
                ImageContainer.setScrollBarSize(0);
                ImageContainer.setImageBitmap(null);
                no_urut.requestFocus();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                kembali();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
        alert.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
    }

    private void kembali() {
        Intent back = new Intent();
        setResult(RESULT_OK, back);
        finish();
    }

    private void getImage(){
        //Method ini digunakan untuk mengambil gambar dari Kamera
        CharSequence[] menu = {"Kamera", "Galeri"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Upload Image")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                //Mengambil gambar dari Kemara ponsel
                                Intent imageIntentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(imageIntentCamera, REQUEST_CODE_CAMERA);
                                break;

                            case 1:
                                //Mengambil gambar dari galeri
                                Intent imageIntentGallery = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        dialog.create();
        dialog.show();
    }

    private void cekInput(){
        no = no_urut.getText().toString().trim();
        nama = namacalon.getText().toString().trim();
        visi = visicalon.getText().toString().trim();
        misi = misicalon.getText().toString().trim();
        angkatan = calonangkatan.getText().toString().trim();
        des = deckripsi.getText().toString().trim();
        suara = "0";

        if (no.isEmpty()) {
            no_urut.setError("Input No urut"); no_urut.requestFocus();
            return;
        }

        if (nama.isEmpty()) {
            namacalon.setError(getString(R.string.input_error_nama)); namacalon.requestFocus();
            return;
        }
        if (angkatan.isEmpty()) {
            calonangkatan.setError("Input Angkatan"); calonangkatan.requestFocus();
            return;
        }
        if (angkatan.length() != 4) {
            calonangkatan.setError("Input Angkatan"); calonangkatan.requestFocus();
            return;
        }

        if (visi.isEmpty()) {
            visicalon.setError("Input Visi"); visicalon.requestFocus();
            return;
        }

        if (misi.isEmpty()) {
            misicalon.setError("Input Misi"); misicalon.requestFocus();
            return;
        }

        if (des.isEmpty()) {
            deckripsi.setError("Input Decryption"); deckripsi.requestFocus();
            return;
        }


        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Candidate");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(no_urut.getText().toString()).exists()){
                    progressDialog.dismiss();
                    Toast.makeText(TambahCalon.this, "No urut already exists",
                            Toast.LENGTH_SHORT).show();
                    no_urut.requestFocus();

                }
                else{
                    progressDialog.dismiss();
                    konfirInput();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void konfirInput() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Warning !!!")
                .setMessage("Do you want Save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        com.fend.m_voting.Calon.TambahCalon.this.saving();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void saving(){
        if (resultUri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Data");
            progressDialog.show();
            final StorageReference storageReferences = storageReference.child(Constants.
                    STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." +
                    getFileExtension(resultUri));
            storageReferences.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReferences.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    foto = String.valueOf(uri);

                                    progressDialog.dismiss();
                                    Toast.makeText(TambahCalon.this,
                                            "Saving Data successfully", Toast.LENGTH_SHORT).show();
                                    //uris = taskSnapshot.getStorage().getDownloadUrl().toString();
                                    Calon upload = new Calon
                                            (no, nama, angkatan, visi, misi, des, foto, suara);
                                    databaseCalon.child(no).setValue(upload);
                                    konfir();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahCalon.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) /
                            taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        } else {
            Toast.makeText(this,"Make sure all data is correct",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    ImageContainer.setVisibility(View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageContainer.setImageBitmap(bitmap);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    ImageContainer.setVisibility(View.VISIBLE);
                    Uri imageUri = data.getData();
                    resultUri = imageUri;
                    ImageContainer.setImageURI(resultUri);
                }
                break;
        }
    }
}