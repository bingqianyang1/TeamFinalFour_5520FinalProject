package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ShowPostDetailActivity extends AppCompatActivity {
    private String username;
    private String title;
    private String image;
    private String content;
    private String likes;
    private String location;
    private String time;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    private ImageView postDetailImg;
    private TextView postDetailUsername;
    private TextView postDetailTitle;
    private TextView postDetailContent;
    private TextView postDetailLikes;
    private TextView postDetailLocation;
    private TextView postDetailTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_detail);

        image = getIntent().getStringExtra("image");
        username = getIntent().getStringExtra("username");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        likes = getIntent().getStringExtra("likes");
        location = getIntent().getStringExtra("location");
        time = getIntent().getStringExtra("time");

        postDetailImg = findViewById(R.id.postDetailImg);
        postDetailUsername = findViewById(R.id.postDetailUsername);
        postDetailTitle = findViewById(R.id.postDetailTitle);
        postDetailContent = findViewById(R.id.postDetailContent);
        postDetailLikes = findViewById(R.id.postDetailLikes);
        postDetailLocation = findViewById(R.id.postDetailLocation);
        postDetailTime = findViewById(R.id.postDetailTime);


        reference.child(username).child("posts").child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postDetailUsername.setText(username);
                postDetailTitle.setText(title);
                postDetailContent.setText(content);
                postDetailLikes.setText(likes);
                postDetailLocation.setText(location);
                postDetailTime.setText(time);
                Picasso.with(ShowPostDetailActivity.this).load(image).into(postDetailImg);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}