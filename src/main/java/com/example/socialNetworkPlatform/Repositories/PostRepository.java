package com.example.socialNetworkPlatform.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.socialNetworkPlatform.Entitys.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,UUID> {
    
}
