package tiagodccosta.assestcoimbra;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Chooser extends AppCompatActivity {

    private Button btnPartilhar;
    private Button btnBack;
    private EditText descricao;
    private ImageView post;

    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference mref;

    DatabaseReference databaseReference;

    private static int PICK_IMAGE = 123;

    Uri imageUri;

    private ProgressBar mProgressBar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                post.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Chooser.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Chooser.this, "Tens de escolher uma imagem!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        firebaseStorage = FirebaseStorage.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = firebaseStorage.getReference("Feed Posts");

        databaseReference = FirebaseDatabase.getInstance().getReference("Feed Posts");


        btnBack = (Button) findViewById(R.id.btnBack);
        btnPartilhar = (Button) findViewById(R.id.btnPartilhar);
        descricao = (EditText) findViewById(R.id.descrição);
        post = (ImageView) findViewById(R.id.postImage);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);           }
        });


        btnPartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    sendData();
                }
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chooser.this, AdminActivity.class));
            }
        });
    }


    private Boolean validate() {
        Boolean result = false;

        if( imageUri == null || descricao == null) {
            Toast.makeText(Chooser.this, "Tens de preencher todos os detalhes", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
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
                Toast.makeText(Chooser.this, "Partilhado com sucesso!", Toast.LENGTH_SHORT).show();
                
                UploadInfo uploadInfo = new UploadInfo(descricao.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                String uploadId = databaseReference.push().getKey();
                databaseReference.child(uploadId).setValue(uploadInfo);

                startActivity(new Intent(Chooser.this, AdminActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chooser.this, "Falha ao partilhar!", Toast.LENGTH_SHORT).show();
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

