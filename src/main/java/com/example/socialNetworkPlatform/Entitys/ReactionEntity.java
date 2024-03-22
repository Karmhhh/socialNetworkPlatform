package com.example.socialNetworkPlatform.Entitys;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class ReactionEntity {

    public enum TypeReaction {
        Like,
        Comment
    };

    private TypeReaction reactionType;
    public TypeReaction getReactionType() {
        return reactionType;
    }

    public void setReactionType(TypeReaction reactionType) {
        this.reactionType = reactionType;
    }

    private String bodyReaction;


    public String getBodyReaction() {
        return bodyReaction;
    }

    public void setBodyReaction(String bodyReaction) {
        this.bodyReaction = bodyReaction;
    }

    @Id
    private UUID id_reaction;

    @ManyToOne
    @JoinColumn(name = "id_profile")
    private ProfileEntity joinedProfile;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private PostEntity joinedPost;

    public ProfileEntity getJoinedProfile() {
        return joinedProfile;
    }

    public void setJoinedProfile(ProfileEntity joinedProfile) {
        this.joinedProfile = joinedProfile;
    }

    public PostEntity getJoinedPost() {
        return joinedPost;
    }

    public void setJoinedPost(PostEntity joinedPost) {
        this.joinedPost = joinedPost;
    }

    public UUID getId_reaction() {
        return id_reaction;
    }

    public void setId_reaction(UUID id_reaction) {
        this.id_reaction = id_reaction;
    }

}
