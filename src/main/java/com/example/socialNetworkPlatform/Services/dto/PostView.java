package com.example.socialNetworkPlatform.Services.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PostView {
    private UUID id_post;
    private String header;
    private String body;
    private LocalDateTime creationDateTime;

    public UUID getId_post() {
        return id_post;
    }

    public void setId_post(UUID id_post) {
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

}
