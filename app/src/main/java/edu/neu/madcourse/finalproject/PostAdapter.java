package edu.neu.madcourse.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>  implements Filterable {

    private Context context;
    private ArrayList<Post> posts;
    private ArrayList<Post> postsAll;
    private RecyclerViewClickListener listener;
    private DatabaseReference database;

    public PostAdapter(Context context, ArrayList<Post> postList, ArrayList<Post> postListAll, RecyclerViewClickListener listener) {
        this.context = context;
        this.posts = postList;
        this.postsAll = postListAll;
        this.listener = listener;
        database = FirebaseDatabase.getInstance().getReference("Users");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = posts.get(position);
        String username = post.getUsername();
        String title = post.getTitle();
        String likes = post.getLikes();
        holder.title.setText(title.length() <= 20? title: title.substring(0,20) + "...");
        holder.likes.setText(likes);


        // Click the Likes
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference likesThisPost = database.child(username).child("posts").child(title).child("likes");
                likesThisPost.setValue(String.valueOf(Integer.valueOf(likes)+1));
            }
        });

        // Handle Situation when multiple lines here.
        String oneLineContent = trimToOneLine(post.getContent());
        holder.content.setText(oneLineContent.length() <= 20? oneLineContent: oneLineContent.substring(0,20) + "...");
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, likes, content;
        ImageView heart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            likes = itemView.findViewById(R.id.likes);
            content = itemView.findViewById(R.id.content);
            heart = itemView.findViewById(R.id.heart);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }



    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        // background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Post> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()) {
                filteredList.addAll(postsAll);
            } else {
                String pattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();
                // Search for title
                for(Post post: postsAll) {
                    if(post.getTitle().toLowerCase().contains(pattern) || (post.getLocation() != null && post.getLocation().toLowerCase().contains(pattern))) {
                        filteredList.add(post);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        // UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            posts.clear();
            posts.addAll((Collection<? extends Post>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Trim content to one line
     * @param content - the content
     * @return
     */
    private String trimToOneLine(String content) {
        return content.replace("\n", " ");
    }


    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

}
