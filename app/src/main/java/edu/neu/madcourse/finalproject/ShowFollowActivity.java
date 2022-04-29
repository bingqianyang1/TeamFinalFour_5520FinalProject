package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowFollowActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference reference;
    private FollowAdapter adapter;
    private ArrayList<String> list;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = findViewById(R.id.followReView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        username = getIntent().getStringExtra("username");
        list=new ArrayList<>();
        adapter = new FollowAdapter(this,list,username);
        recyclerView.setAdapter(adapter);



        reference.child(username).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String v = (String)dataSnapshot.getValue();
                    if(!list.contains(v)){
                        list.add(v);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        list = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new FollowAdapter(this,list,username);
        recyclerView.setAdapter(adapter);

        reference.child(username).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String v = (String)dataSnapshot.getValue();
                    if(!list.contains(v)){
                        list.add(v);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}