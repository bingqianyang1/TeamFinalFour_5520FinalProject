package edu.neu.madcourse.finalproject;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public String password;
    public Map<String,String> followers;

    public Map<String, Object> posts;

    public User(){};

    public User(String username, String password){
        this.username = username;
        this.followers = new HashMap<>();
        this.posts = new HashMap<>();
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
