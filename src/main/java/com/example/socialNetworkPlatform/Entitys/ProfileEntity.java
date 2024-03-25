package com.example.socialNetworkPlatform.Entitys;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "id_profile", "id_follower", "username" }))
public class ProfileEntity {

    @Id
    private UUID id_profile;
    private String username;
    private String password;
    private String bio;
    private String email;

    @ManyToMany
    @JoinTable(
        name = "follows",
        joinColumns = @JoinColumn(name = "follower_id"), // Cambia il nome della colonna per i follower
        inverseJoinColumns = @JoinColumn(name = "followed_id") // Cambia il nome della colonna per i seguiti
    )
    private Set<ProfileEntity> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<ProfileEntity> followers = new HashSet<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<ProfileEntity> getFollowing() {
        return following;
    }

    public void setFollowing(Set<ProfileEntity> following) {
        this.following = following;
    }

    public Set<ProfileEntity> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<ProfileEntity> followers) {
        this.followers = followers;
    }

}
