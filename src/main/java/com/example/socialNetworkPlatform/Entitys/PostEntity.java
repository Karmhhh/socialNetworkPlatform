package com.example.socialNetworkPlatform.Entitys;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class PostEntity {

    @Id
    private UUID id_post;
    private String header;
    private String body;
    private LocalDateTime creationDateTime;

    @ManyToOne
    @JoinColumn(name = "id_Profile")
    private ProfileEntity joinedProfile;

    public UUID getId_Post() {
        return id_post;
    }

    public void setId_Post(UUID id_post) {
        this.id_post = id_post;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public ProfileEntity getJoinedProfile() {
        return joinedProfile;
    }

    public void setJoinedProfile(ProfileEntity joinedProfile) {
        this.joinedProfile = joinedProfile;
    }
}
