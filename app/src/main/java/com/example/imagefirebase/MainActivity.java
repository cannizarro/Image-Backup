package com.example.imagefirebase;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity {

    Button choose;
    ProgressBar uploadProgress;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ArrayList<Uri> imageUri=new ArrayList<Uri>();
    private static final int RC_PHOTO_PICKER =  2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choose=(Button)findViewById(R.id.choose);
        uploadProgress=(ProgressBar)findViewById(R.id.uploadProgress);
        uploadProgress.setVisibility(View.INVISIBLE);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("User_Photos");


    }

    public void picker(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    public void view(View v)
    {
        Intent intent=new Intent(this,recyclerView.class);
        intent.putExtra("Uri",imageUri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode==RC_PHOTO_PICKER && resultCode==RESULT_OK)
        {
            Uri selectedImage=data.getData();
            final StorageReference imageRef=storageReference.child(selectedImage.getLastPathSegment());;
            uploadProgress.setProgress(0);
            uploadProgress.setVisibility(View.VISIBLE);
            UploadTask uploadTask=imageRef.putFile(selectedImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=uploadProgress.getMax() * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    uploadProgress.setProgress((int)progress);
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Uploaded in Firebase Storage.", Toast.LENGTH_SHORT).show();
                    uploadProgress.setVisibility(View.INVISIBLE);
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUri.add(downloadUri);
                    }
                }
            });
        }
    }
}
