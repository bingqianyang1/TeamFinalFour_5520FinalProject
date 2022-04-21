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

    private FollowAdapter adapter;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow);

        System.out.println("hereherehereherehereherehereherehereherehereherehere");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = findViewById(R.id.followReView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();
        adapter = new FollowAdapter(this,list);
        recyclerView.setAdapter(adapter);

        String username = getIntent().getStringExtra("username");
        System.out.println(username+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        reference.child(username).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //FollowModel model = dataSnapshot.getValue(FollowModel.class);
                    /**
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(dataSnapshot.getValue());
                    FollowModel following = dataSnapshot.getValue(FollowModel.class);
                    System.out.println("????????????????????????????????????");
                    for(String name: following.getMap().values()){
                        list.add(name);
                    }**/
                    list.add((String)dataSnapshot.getValue());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}