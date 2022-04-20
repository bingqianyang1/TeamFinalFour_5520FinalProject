package edu.neu.madcourse.finalproject;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public String email;
    public String password;
    public Map<String, String> followers;
    public Map<String, Object> posts;

    public User(){};

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, String> followers) {
        this.followers = followers;
    }

    public Map<String, Object> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Object> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", followers=" + followers +
                ", posts=" + posts +
                '}';
    }
}
