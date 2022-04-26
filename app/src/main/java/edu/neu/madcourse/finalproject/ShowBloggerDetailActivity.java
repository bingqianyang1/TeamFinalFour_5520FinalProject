package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ShowBloggerDetailActivity extends AppCompatActivity {
    Button followBtn;
    String username;
    String bloggerName;
    TextView usernameView;
    TextView viewAll;
    Boolean following=false;
    Boolean exist=true;
    ImageView[] image = new ImageView[5];
    TextView[] titles = new TextView[5];
    TextView followerNo;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blogger_detail);

        username = getIntent().getStringExtra("username");
        bloggerName = getIntent().getStringExtra("bloggerName");
        followBtn=findViewById(R.id.followBtn);
        titles[0]=findViewById(R.id.title1);
        titles[1]=findViewById(R.id.title2);
        titles[2]=findViewById(R.id.title3);
        titles[3]=findViewById(R.id.title4);
        titles[4]=findViewById(R.id.title5);

        image[0]=findViewById(R.id.img01);
        image[1]=findViewById(R.id.img02);
        image[2]=findViewById(R.id.img03);
        image[3]=findViewById(R.id.img04);
        image[4]=findViewById(R.id.img05);

        usernameView =findViewById(R.id.username);
        usernameView.setText(bloggerName);
        followerNo=findViewById(R.id.followerNo);


        viewAll=findViewById(R.id.viewAll);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowBloggerDetailActivity.this, "They don't have any more blogs", Toast.LENGTH_LONG).show();
            }
        });

        reference.child(bloggerName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user==null){
                    Toast.makeText(ShowBloggerDetailActivity.this, "This user does not exists", Toast.LENGTH_LONG).show();
                    exist=false;
                    return;
                }
                int count = 0;
                for(String key: user.getPosts().keySet()) {
                    titles[count].setText(key);
                    Map<String,String> blog = (Map)user.getPosts().get(key);
                    String img = blog.get("image");
                    Glide.with(ShowBloggerDetailActivity.this).load(img).into(image[count]);

                    if(user.getFollowing()!=null && user.getFollowing().values().contains(username)){
                        followBtn.setText("UNFOLLOW");
                        following = true;
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!exist)return;
                if(!following){

                    reference.child(username).child("following").child(bloggerName).setValue(bloggerName);

                    reference.child(username).child("following").child("paul").setValue("paul");

                    //Integer.parseInt(followerNo.getText().toString())+1;
                    followBtn.setText("UNFOLLOW");
                    following = true;
                    /**
                    reference.child(username).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                     **/
                } else{
                    reference.child(username).child("following").child(bloggerName).removeValue();
                    followBtn.setText("FOLLOW");
                    following = false;
                }
            }
        });
    }
}