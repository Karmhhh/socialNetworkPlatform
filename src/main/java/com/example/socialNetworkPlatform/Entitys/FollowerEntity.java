package com.example.socialNetworkPlatform.Entitys;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class FollowerEntity {

    @Id
    private UUID id_follower ;

    private UUID joinProfileFollower;

    @ManyToOne
    @JoinColumn(name = "id_profile")
    private ProfileEntity joinProfileFollowed;

    public UUID getId_follower() {
        return id_follower;
    }

    public void setId_follower(UUID id_follower) {
        this.id_follower = id_follower;
    }

    public ProfileEntity getJoinProfileFollowed() {
        return joinProfileFollowed;
    }

    public void setJoinProfileFollowed(ProfileEntity joinProfileFollowed) {
        this.joinProfileFollowed = joinProfileFollowed;
    }

    public UUID getJoinProfileFollower() {
        return joinProfileFollower;
    }

    public void setJoinProfileFollower(UUID joinProfileFollower) {
        this.joinProfileFollower = joinProfileFollower;
    }

}
