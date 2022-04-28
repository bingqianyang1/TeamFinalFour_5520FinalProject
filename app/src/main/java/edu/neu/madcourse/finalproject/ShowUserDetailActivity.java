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
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ShowUserDetailActivity extends AppCompatActivity {
    Button logOutBtn;
    String username;
    TextView usernameView;
    TextView viewAll;
    TextView following;
    TextView follower;
    ImageView[] image = new ImageView[5];
    TextView[] titles = new TextView[5];
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_detail);
        username = getIntent().getStringExtra("username");
        logOutBtn=findViewById(R.id.logOutBtn);
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
        usernameView.setText(username);

        following=findViewById(R.id.followingNo);
        follower=findViewById(R.id.followerNo);

        viewAll=findViewById(R.id.viewAll);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowUserDetailActivity.this, ShowAllPostActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                //Toast.makeText(ShowUserDetailActivity.this, "You have no more blogs", Toast.LENGTH_LONG).show();
            }
        });

        reference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                int count = 0;
                if(user.getFollowing()==null){
                    following.setText("0");
                }else{
                    following.setText(Integer.toString(user.getFollowing().keySet().size()));
                }
                if(user.getFollowers()==null){
                    follower.setText("0");
                }else{
                    follower.setText(user.getFollowers());
                }

                if(user.getPosts()==null) return;
                for(String key: user.getPosts().keySet()) {
                    titles[count].setText(key);
                    Map<String,String>blog = (Map)user.getPosts().get(key);
                    String img = blog.get("image");
                    //Glide.with(ShowUserDetailActivity.this).load(img).into(image[count]);
                    Picasso.with(ShowUserDetailActivity.this).load(img).into(image[count]);
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowUserDetailActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                DestroyActivitiesUtil destroyActivityUtil = new DestroyActivitiesUtil();
                destroyActivityUtil.exit();
            }
        });
    }

}