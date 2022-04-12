package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView addButton;
    private DatabaseReference database;
    private PostAdapter postAdapter;
    private ArrayList<User> userList;
    private ArrayList<Post> postList;
    private ArrayList<Post> newPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        recyclerView = findViewById(R.id.postList);
        database = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addButton = findViewById(R.id.add);

        postList = new ArrayList<>();
        userList = new ArrayList<>();
        newPostList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList, newPostList);
        recyclerView.setAdapter(postAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                getPosts(userList, postList, newPostList);
                postAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Get all the posts from the user and update the post list
     * @param userList
     * @param postList
     */
    private void getPosts(ArrayList<User> userList, ArrayList<Post> postList, ArrayList<Post> newPostList) {
        for(User user: userList) {
            for(Map.Entry<String, Object> entry: user.getPosts().entrySet()) {
                Map<String, String> map = (Map<String, String>)entry.getValue();
                String image = map.get("image");
                String title = map.get("title");
                String content = map.get("content");
                String location = map.get("location");
                String time = map.get("time");
                String likes = map.get("likes");
                postList.add(new Post(image, title, content, location, time, likes));
                newPostList.add(new Post(image, title, content, location, time, likes));

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                postAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile) {
            Intent intent = new Intent(MainPageActivity.this, AddPostActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}