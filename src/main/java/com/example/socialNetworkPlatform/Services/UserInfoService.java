package com.example.socialNetworkPlatform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.socialNetworkPlatform.Services.dto.PostView;
import com.example.socialNetworkPlatform.Services.dto.UserRegistration;
import com.example.socialNetworkPlatform.Services.dto.UserView;
import com.example.socialNetworkPlatform.Entitys.PostEntity;
import com.example.socialNetworkPlatform.Entitys.ProfileEntity;
import com.example.socialNetworkPlatform.Repositories.PostRepository;
import com.example.socialNetworkPlatform.Repositories.ProfileRepository;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private ProfileRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    @Autowired
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfileEntity> userDetail = userRepository.findUserByUsername(username);

        if (userDetail.isEmpty())
            throw new UsernameNotFoundException(username);

        UserInfoDetails userInfoDetails = new UserInfoDetails(userDetail.get());
        return userInfoDetails;

    }

    public ResponseEntity<String> registerUser(UserRegistration user) {
        if (user != null) {
            ProfileEntity newUser = new ProfileEntity();
            newUser.setId_profile(UUID.randomUUID());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(encoder.encode(user.getPassword()));
            newUser.setBio(user.getBio());
            newUser.setEmail(user.getEmail());
            userRepository.save(newUser);
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    public ResponseEntity<String> deleteAll() {
        userRepository.deleteAll();
        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<?> deleteById(UUID id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.status(201).build();
        } catch (Exception ex) {
            return ResponseEntity.status(404).build();
        }
    }

    public List<UserView> getAll() {

        List<UserView> usersDto = new ArrayList<>();
        usersDto.clear();
        List<ProfileEntity> users = userRepository.findAll();

        for (ProfileEntity user : users) {

            UserView userDto = new UserView();
            List<PostView> posts = new ArrayList<>();

            if (getPostForProfile(user.getId_profile()).size() > 0) {
                List<PostEntity> filteredPosts = getPostForProfile(user.getId_profile());
                PostView postdto = new PostView();
                for (PostEntity post : filteredPosts) {
                    postdto.setId_post(post.getId_Post());
                    postdto.setBody(post.getBody());
                    postdto.setHeader(post.getHeader());
                    postdto.setCreationDateTime(post.getCreationDateTime());
                    posts.add(postdto);
                }
            } else {
                posts.clear();
            }

            userDto.setId_profile(user.getId_profile());
            userDto.setEmail(user.getEmail());
            userDto.setUsername(user.getUsername());
            userDto.setBio(user.getBio());
            userDto.setPosts(posts);
            usersDto.add(userDto);

        }
        return (usersDto);

    }

    public List<PostEntity> getPostForProfile(UUID id) {
        List<PostEntity> allPosts = postRepository.findAll();
        List<PostEntity> filtredPosts = new ArrayList<>();
        for (PostEntity postEntity : allPosts) {
            if (postEntity.getJoinedProfile() != null && postEntity.getJoinedProfile().getId_profile().equals(id)) {
                filtredPosts.add(postEntity);
            }

        }
        return filtredPosts;
    }

    public ProfileEntity follow(UUID followerId, UUID followedId) {
        Optional<ProfileEntity> followerOptional = userRepository.findById(followerId);
        Optional<ProfileEntity> followedOptional = userRepository.findById(followedId);

        if (followerOptional.isPresent() && followedOptional.isPresent()) {
            ProfileEntity follower = followerOptional.get();
            ProfileEntity followed = followedOptional.get();

            follower.getFollowing().add(followed);
            followed.getFollowers().add(follower);

            return userRepository.save(follower);
        } else {
            // Gestionare il caso in cui uno o entrambi i profili non esistano
            return null;
        }
    }

    public ResponseEntity<String> changePassword(String username, String oldPass, String pass) {
        ProfileEntity userDetail = userRepository.findUserByUsername(username).get();

        if (encoder.matches(oldPass, userDetail.getPassword())) {
            userDetail.setPassword(encoder.encode(pass));
            userRepository.save(userDetail);

            return ResponseEntity.ok().build();
        } else {

            return ResponseEntity.badRequest().build();
        }
    }

}