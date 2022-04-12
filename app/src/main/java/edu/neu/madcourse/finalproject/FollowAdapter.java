package edu.neu.madcourse.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyViewHolder>{
    ArrayList<FollowModel> list;
    Context context;

    public FollowAdapter(Context context, ArrayList<FollowModel>list){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.follow_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.MyViewHolder holder, int position) {
        FollowModel model = list.get(position);
        holder.followText.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView followText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            followText = itemView.findViewById(R.id.followText);
        }
    }
}
