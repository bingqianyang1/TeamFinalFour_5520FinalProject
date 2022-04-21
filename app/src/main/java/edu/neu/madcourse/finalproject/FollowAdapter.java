package edu.neu.madcourse.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyViewHolder>{
    ArrayList<String> list;
    Context context;
    String username;
    public FollowAdapter(Context context, ArrayList<String>list,String username){
        this.list=list;
        this.context=context;
        this.username = username;
    }

    @NonNull
    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.follow_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.MyViewHolder holder, int position) {
        String model = list.get(position);
        holder.followText.setText(model);
        holder.followItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowBloggerDetailActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("bloggerName", model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView followText;
        LinearLayout followItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            followText = itemView.findViewById(R.id.followText);
            followItem = itemView.findViewById(R.id.followItem);
        }
    }
}
