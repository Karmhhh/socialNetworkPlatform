package com.example.socialNetworkPlatform.Repositories;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.socialNetworkPlatform.Entitys.FollowerEntity;

@Repository
public interface FollowerRepository extends JpaRepository<FollowerEntity,UUID> {
    
}
