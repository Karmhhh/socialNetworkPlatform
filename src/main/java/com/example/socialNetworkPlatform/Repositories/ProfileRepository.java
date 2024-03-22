package com.example.socialNetworkPlatform.Repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.socialNetworkPlatform.Entitys.ProfileEntity;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {

  
    Optional<ProfileEntity> findUserByUsername(String username);
}