package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button post_button;
    private ImageView imageView;
    private boolean image_selected = false;
    private Uri selected_image;
    private String upload_image_name;

    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Button select_image_button = findViewById(R.id.choose_image_button);
        select_image_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        test = findViewById(R.id.test);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        post_button = findViewById(R.id.post_button);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (image_selected){
                    final ProgressDialog progressDialog = new ProgressDialog(AddPostActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    upload_image_name = "images/"+ UUID.randomUUID().toString();
                    StorageReference ref = storageReference.child(upload_image_name);
                    ref.putFile(selected_image)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();


                                    storageReference.child(upload_image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Got the download URL for 'users/me/profile.png'
                                            Uri u = uri;
                                            test.setText(u.toString());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            selected_image = data.getData();
            imageView = findViewById(R.id.add_image);
            imageView.setImageURI(selected_image);
            image_selected = true;
        }
    }
}