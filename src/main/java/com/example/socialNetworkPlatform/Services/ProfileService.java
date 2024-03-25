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
public class ProfileService implements UserDetailsService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    private ProfileRepository userRepository;

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

    // --------------------------------------Create----------------------------------
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

    public ResponseEntity<String> addPost(PostEntity myNewPost) {
        postRepository.save(myNewPost);
        return ResponseEntity.status(200).body("New Post Added!");
    }

    // --------------------------------------Read----------------------------------
    public List<UserView> getAll() {

        List<UserView> usersDto = new ArrayList<>();

        for (ProfileEntity user : userRepository.findAll()) {

            UserView userDto = new UserView();
            List<PostView> posts = new ArrayList<>();

            if (user.getPosts().size() > 0) {

                for (PostEntity post : user.getPosts()) {
                    PostView postdto = new PostView();
                    postdto.setId_post(post.getId_Post());
                    postdto.setBody(post.getBody());
                    postdto.setHeader(post.getHeader());
                    postdto.setCreationDateTime(post.getCreationDateTime());
                    posts.add(postdto);
                }
            }

            userDto.setId_profile(user.getId_profile());
            userDto.setEmail(user.getEmail());
            userDto.setUsername(user.getUsername());
            userDto.setBio(user.getBio());
            userDto.setPosts(posts);
            usersDto.add(userDto);

            for (ProfileEntity follower : user.getFollowers()) {
                userDto.getFollowers().add(follower.getUsername());
            }

            for (ProfileEntity follower : user.getFollowing()) {
                userDto.getFollowing().add(follower.getUsername());
            }
        }
        return (usersDto);
    }

    // --------------------------------------Update----------------------------------
    public void follow(UUID followerId, UUID followedId) {
        Optional<ProfileEntity> followerOptional = userRepository.findById(followerId);
        Optional<ProfileEntity> followedOptional = userRepository.findById(followedId);

        if (followerOptional.isPresent() && followedOptional.isPresent()) {
            ProfileEntity follower = followerOptional.get();
            ProfileEntity followed = followedOptional.get();

            follower.getFollowing().add(followed);
            followed.getFollowers().add(follower);

            userRepository.save(followed);
            userRepository.save(follower);
        } else {
            // Gestionare il caso in cui uno o entrambi i profili non esistano

        }
    }

    public void unfollow(UUID unfollowerId, UUID unfollowedId) {
        Optional<ProfileEntity> followerOptional = userRepository.findById(unfollowerId);
        Optional<ProfileEntity> followedOptional = userRepository.findById(unfollowedId);

        if (followerOptional.isPresent() && followedOptional.isPresent()) {

            ProfileEntity unfollower = followerOptional.get();
            ProfileEntity unfollowed = followedOptional.get();

            unfollower.getFollowing().remove(unfollowed);
            unfollowed.getFollowers().remove(unfollower);

            userRepository.save(unfollower);
            userRepository.save(unfollowed);
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

    // --------------------------------------Delete----------------------------------
    public ResponseEntity<String> deleteAllProfiles() {
        userRepository.deleteAll();
        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<String> deletePostById(UUID id_post) {
        postRepository.deleteById(id_post);
        return ResponseEntity.status(200).body("Post Deleted Succesfully!");
    }

    public ResponseEntity<?> deleteByIdProfile(UUID id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.status(201).build();
        } catch (Exception ex) {
            return ResponseEntity.status(404).build();
        }
    }

    // public List<PostEntity> getPostForProfile(UUID id) {
    // List<PostEntity> allPosts = postRepository.findAll();
    // List<PostEntity> filtredPosts = new ArrayList<>();
    // for (PostEntity postEntity : allPosts) {
    // if (postEntity.getJoinedProfile() != null &&
    // postEntity.getJoinedProfile().getId_profile().equals(id)) {
    // filtredPosts.add(postEntity);
    // }

    // }
    // return filtredPosts;
    // }

}