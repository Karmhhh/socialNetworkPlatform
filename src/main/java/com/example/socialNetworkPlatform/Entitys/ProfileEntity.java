package com.example.socialNetworkPlatform.Entitys;

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

    @ManyToOne
    @JoinColumn(name = "joinProfileFollower")
    private FollowerEntity id_follower;

    public FollowerEntity getId_follower() {
        return id_follower;
    }

    public void setId_follower(FollowerEntity id_follower) {
        this.id_follower = id_follower;
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

}
