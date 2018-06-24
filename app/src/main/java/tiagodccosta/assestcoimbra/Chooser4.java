package tiagodccosta.assestcoimbra;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;

public class Chooser4 extends AppCompatActivity {

    private Button btnBack;
    private Button btnGet;

    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference mref;

    DatabaseReference databaseReference;

    private static int PICK_IMAGE = 123;

    Uri imageUri;

    private ProgressBar mProgressBar;

    public static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser4);

        firebaseStorage = FirebaseStorage.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = firebaseStorage.getReference("Galeria");

        databaseReference = FirebaseDatabase.getInstance().getReference("Galeria");


        btnBack = (Button) findViewById(R.id.btnBack);
        btnGet = findViewById(R.id.btnGet);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleciona Fotos"), RESULT_LOAD_IMAGE);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chooser4.this, AdminActivity.class));
            }
        });
    }

    public String getImagExt(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(uri));

    }

    private void sendData() {
        mref = storageReference.child(System.currentTimeMillis()+"."+getImagExt(imageUri));

        UploadTask uploadTask = mref.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Chooser4.this, "Partilhado com sucesso!", Toast.LENGTH_SHORT).show();
                String uploadId = databaseReference.push().getKey();
                UploadInfo uploadInfo = new UploadInfo("photo", taskSnapshot.getDownloadUrl().toString(), uploadId);
                databaseReference.child(uploadId).setValue(uploadInfo);

                startActivity(new Intent(Chooser4.this, AdminActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chooser4.this, "Falha ao partilhar!", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mProgressBar.setProgress((int) progress);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if(data.getClipData() != null) {

                int totalItemsSelected = data.getClipData().getItemCount();

                for(int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = System.currentTimeMillis()+"."+getImagExt(fileUri);

                    mref = storageReference.child(fileName);
                    UploadTask uploadTask = mref.putFile(fileUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Chooser4.this, "Partilhado com sucesso!", Toast.LENGTH_SHORT).show();
                            String uploadId = databaseReference.push().getKey();
                            UploadInfo uploadInfo = new UploadInfo("photo", taskSnapshot.getDownloadUrl().toString(), uploadId);
                            databaseReference.child(uploadId).setValue(uploadInfo);
                            startActivity(new Intent(Chooser4.this, AdminActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Chooser4.this, "Falha ao partilhar!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
                }


            }else if(data.getData() != null) {
                Uri fileUri = data.getData();

                String fileName = System.currentTimeMillis()+"."+getImagExt(fileUri);

                mref = storageReference.child(fileName);
                UploadTask uploadTask = mref.putFile(fileUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Chooser4.this, "Partilhado com sucesso!", Toast.LENGTH_SHORT).show();
                        String uploadId = databaseReference.push().getKey();
                        UploadInfo uploadInfo = new UploadInfo("photo", taskSnapshot.getDownloadUrl().toString(), uploadId);
                        databaseReference.child(uploadId).setValue(uploadInfo);
                        startActivity(new Intent(Chooser4.this, AdminActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Chooser4.this, "Falha ao partilhar!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    }
                });
            }

        }

    }

}

