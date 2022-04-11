package edu.neu.madcourse.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userList;

    public PostAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.firstName.setText(user.getUsername());
        holder.lastName.setText(user.getPassword());
        holder.age.setText(user.getUsername());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView firstName, lastName, age;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.tvFirstName);
            lastName = itemView.findViewById(R.id.tvLastName);
            age = itemView.findViewById(R.id.tvAge);
        }
    }
}
