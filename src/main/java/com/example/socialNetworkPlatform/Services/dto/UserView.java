package com.example.socialNetworkPlatform.Services.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.socialNetworkPlatform.Entitys.ProfileEntity;

public class UserView {

    private UUID id_profile;
    private String username;
    private String bio;
    private String email;
    private Set<String> following = new HashSet<>();
    private Set<String> followers = new HashSet<>();
    private List<PostView> posts;

    public List<PostView> getPosts() {
        return posts;
    }

    public void setPosts(List<PostView> posts) {
        this.posts = posts;
    }

    public UUID getId_profile() {
        return id_profile;
    }

    public void setId_profile(UUID id_profile) {
        this.id_profile = id_profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Set<String> getFollowing() {
        return following;
    }

    public void setFollowing(Set<String> following) {
        this.following = following;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
    }


}
