package edu.neu.madcourse.finalproject;

public class Post {
    private String image;
    private String title;
    private String content;
    private String location;
    private String time;
    private int likes;

    public Post(String image, String title, String content, String location, String time, int likes) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.location = location;
        this.time = time;
        this.likes = likes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    @Override
    public String toString() {
        return "Post{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                ", likes=" + likes +
                '}';
    }
}
