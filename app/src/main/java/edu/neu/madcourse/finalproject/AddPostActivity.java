package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button post_button;
    private ImageView imageView;
    private boolean image_selected = false;
    private Uri selected_image;
    private String upload_image_name;
    private String city;
    private CheckBox add_location_cb;
    private String date_String;
    private FirebaseDatabase database;
    private String user_name;
    private TextView title_tx;
    private TextView content_tx;
    private Uri upload_image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        user_name = getIntent().getStringExtra("username");
        Button select_image_button = findViewById(R.id.choose_image_button);
        select_image_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

//        test = findViewById(R.id.test);
        title_tx = findViewById(R.id.title_textview);
        content_tx = findViewById(R.id.content_textview);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        add_location_cb = findViewById(R.id.add_location_cb);
        database = FirebaseDatabase.getInstance();

        post_button = findViewById(R.id.post_button);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (image_selected){

                    if (add_location_cb.isChecked()) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AddPostActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        } else {
                            getCurrentLocation();
                        }
                    }

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
                                            upload_image_uri = uri;
                                            update_database();
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

    private void update_database(){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (isNetworkOnline()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                date_String = formatter.format(date).toString();

                uploadPost(database.getReference());


            }
        } else {
            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
        }
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

    public String getLocationName(double latitude, double longitude){
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adrs: addresses){
                if (adrs != null){
                    String city = adrs.getLocality();
                    if (city != null && !city.equals("")){
                        cityName = city;
                    } else {

                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Deny", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(AddPostActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(AddPostActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0){
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longtitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    city = getLocationName(latitude, longtitude);
                }
            }
        }, Looper.getMainLooper());

    }

    public static boolean isNetworkOnline() {
        boolean isOnline = false;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 3000);
            // socket.connect(new InetSocketAddress("114.114.114.114", 53), 3000);
            isOnline = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isOnline;
    }

    private void uploadPost(DatabaseReference database) {

        database.child("Users").child(user_name).child("posts").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Map<String, Object> aPost = new HashMap<>();
                    aPost.put("title", title_tx.getText().toString());
                    aPost.put("content", content_tx.getText().toString());
                    aPost.put("time", date_String);
                    aPost.put("location", city);
                    aPost.put("image", upload_image_uri.toString());
                    aPost.put("likes", "0");


                    Map<String, Object> posts = new HashMap<>();
                    posts.put(title_tx.getText().toString(), aPost);

                    Map<String, Object> updated = new HashMap<>();
                    updated.put("posts", posts);
                    database.child("Users").child(user_name).updateChildren(updated);
                    AddPostActivity.this.finish();
                }else {

//                user.sent_history.put("History" + user.sent_history.size(),receiver_name + ": " + selected_Sticker_String);

                    Map<String, Object> aPost = new HashMap<>();
                    aPost.put("title", title_tx.getText().toString());
                    aPost.put("content", content_tx.getText().toString());
                    aPost.put("time", date_String);
                    aPost.put("location", city);
                    aPost.put("image", upload_image_uri.toString());
                    aPost.put("likes", "0");

                    Map<String, Object> updated = new HashMap<>();
                    updated.put(title_tx.getText().toString(), aPost);
                    database.child("Users").child(user_name).child("posts").updateChildren(updated);
                    AddPostActivity.this.finish();
                }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}