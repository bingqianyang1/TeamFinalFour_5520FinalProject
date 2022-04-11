package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.common.util.ScopeUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private DatabaseReference database;
    private PostAdapter postAdapter;
    private ArrayList<User> userList;
    private ArrayList<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        recyclerView = findViewById(R.id.postList);
        database = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        userList = new ArrayList<>();
        postAdapter = new PostAdapter(this, userList, postList);
        recyclerView.setAdapter(postAdapter);



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    System.out.println(database);
                    User user = dataSnapshot.getValue(User.class);
                    System.out.println(user);
                    userList.add(user);
                }
                for(User user: userList) {
                    for(Map.Entry<String, Object> entry: user.getPosts().entrySet()) {
                        Map<String, String> map = (Map<String, String>)entry.getValue();
                        String image = map.get("image");
                        String title = map.get("title");
                        String content = map.get("content");
                        String location = map.get("location");
                        String time = map.get("time");
                        int likes = Integer.valueOf(map.get("likes"));
                        postList.add(new Post(image, title, content, location, time, likes));
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


//    /**
//     * Get all the posts from the users
//     * @param postList
//     * @param userList
//     */
//    private void getPostsFromUsers(ArrayList<Post> postList, ArrayList<User> userList) {
//        for(User user: userList) {
//            Map<String, Post> postMap = user.getPosts();
//            for(Map.Entry<String, Post> entry: postMap.entrySet()) {
//                Post post = entry.getValue();
//                postList.add(post);
//            }
//        }
//    }


}