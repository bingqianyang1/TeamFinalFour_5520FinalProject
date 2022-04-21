package edu.neu.madcourse.finalproject;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public String email;
    public String password;
    public String followers;
    public Map<String, String>  following;
    public Map<String, Object> posts;

    public User(){};

    public User(String username, String email, String password){
        this.following=new HashMap<>();
        this.username = username;
        this.email = email;
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

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public Map<String, String> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, String> following) {
        this.following = following;
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
