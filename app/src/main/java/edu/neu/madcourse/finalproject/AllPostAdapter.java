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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.MyViewHolder>{
    ArrayList<Post> list;
    Context context;
    public AllPostAdapter(Context context, ArrayList<Post>list){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public AllPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.all_post_itme, parent, false);
        return new AllPostAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPostAdapter.MyViewHolder holder, int position) {
        Post model = list.get(position);
        holder.title.setText(model.getTitle());
        Picasso.with(context).load(model.getImage()).into(holder.image);
        /**
        holder.followItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent.putExtra("username", username);
                //intent.putExtra("bloggerName", model);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        //LinearLayout followItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.api_img);
            title = itemView.findViewById(R.id.api_title);
        }
    }
}
