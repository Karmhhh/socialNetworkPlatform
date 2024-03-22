package com.example.socialNetworkPlatform.Services.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class UserView {
    
    private UUID userId;
    private String username;
    private List<PostView> posts = new ArrayList<>();

    public List<PostView> getPosts() {
        return posts;
    }

    public void setPosts(List<PostView> posts) {
        this.posts = posts;
    }

    private String bio;
    private String email;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        ;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
