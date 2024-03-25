package com.example.socialNetworkPlatform.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialNetworkPlatform.Config.filtri.AuthRequest;
import com.example.socialNetworkPlatform.Entitys.PostEntity;
import com.example.socialNetworkPlatform.Entitys.ProfileEntity;
import com.example.socialNetworkPlatform.Repositories.ProfileRepository;
import com.example.socialNetworkPlatform.Services.JwtService;
import com.example.socialNetworkPlatform.Services.ProfileService;
import com.example.socialNetworkPlatform.Services.dto.ChangePassDto;
import com.example.socialNetworkPlatform.Services.dto.PostView;
import com.example.socialNetworkPlatform.Services.dto.UserRegistration;
import com.example.socialNetworkPlatform.Services.dto.UserView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {
    @Autowired
    ProfileService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    ProfileRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    // testato e funzionante
    @PostMapping("/public/login")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(
                    "Allowed! Signed in successfully.\n Token: " + jwtService.generateToken(authRequest.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Not Allowed! Wrong Credential Error Try Again.");
        }
    }

    // ------------------------------------------------create-----------------------------
    // testato e funzionante
    @PostMapping("/public/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegistration entity) {
        userService.registerUser(entity);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/auth/addPost")
    public ResponseEntity<String> addPost(@RequestHeader("Authorization") String authToken,
            @RequestBody PostView myNewPost) {
        authToken = jwtService.extractToken(authToken);
        ProfileEntity myProfile = userRepo.findUserByUsername(jwtService.extractUsername(authToken)).get();
        PostEntity myNewPostEntity = new PostEntity();
        myNewPostEntity.setBody(myNewPost.getBody());
        myNewPostEntity.setCreationDateTime(LocalDateTime.now());
        myNewPostEntity.setHeader(myNewPost.getHeader());
        myNewPostEntity.setId_Post(UUID.randomUUID());
        myNewPostEntity.setJoinedProfile(myProfile);
        userService.addPost(myNewPostEntity);
        return ResponseEntity.status(200).body("Post Added Succesfully!");
    }

    // ----------------------------------------------Read-----------------------------------
    // testato e funzionante
    @GetMapping("/public/getAllProfiles")
    public List<UserView> getAllProfiles() {
        return userService.getAll();
    }

    // testato e funzionante
    @GetMapping("/auth/myProfile")
    public ResponseEntity<Object> myProfile(@RequestHeader("Authorization") String authToken) {
        authToken = jwtService.extractToken(authToken);
        try {
            String username = jwtService.extractUsername(authToken);
            ProfileEntity myProfile = userRepo.findUserByUsername(username).get();

            UserView userDto = new UserView();
            List<PostView> posts = new ArrayList<>();

            if (username != null) {

                userDto.setId_profile(myProfile.getId_profile());
                userDto.setEmail(myProfile.getEmail());
                userDto.setUsername(myProfile.getUsername());
                userDto.setBio(myProfile.getBio());

                for (ProfileEntity follower : myProfile.getFollowers()) {
                    userDto.getFollowers().add(follower.getUsername());
                }

                for (ProfileEntity follower : myProfile.getFollowing()) {
                    userDto.getFollowing().add(follower.getUsername());
                }

                // Set Dei post dell'utente "username"
                if (myProfile.getPosts().size() > 0) {

                    for (PostEntity post : myProfile.getPosts()) {
                        PostView postdto = new PostView();
                        postdto.setId_post(post.getId_Post());
                        postdto.setBody(post.getBody());
                        postdto.setHeader(post.getHeader());
                        postdto.setCreationDateTime(post.getCreationDateTime());
                        posts.add(postdto);
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
            }

            userDto.setPosts(posts);

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il recupero dell'utente");
        }

    }

    // ----------------------------------------------Upload-----------------------------------
    // testato e funzionante
    @GetMapping("/auth/follow")
    public ResponseEntity<String> follow(@RequestHeader("Authorization") String authToken,
            @RequestParam String followedUsername) {
        authToken = jwtService.extractToken(authToken);
        String username = jwtService.extractUsername(authToken);
        ProfileEntity userLoggedBytoken = userRepo.findUserByUsername(username).get();
        ProfileEntity userFollowed = userRepo.findUserByUsername(followedUsername).get();
        userService.follow(userLoggedBytoken.getId_profile(), userFollowed.getId_profile());
        return ResponseEntity
                .ok(userLoggedBytoken.getUsername() + " started follow " + userFollowed.getUsername() + "!");
    }

    @DeleteMapping("/auth/unfollow")
    public ResponseEntity<String> unfollow(@RequestHeader("Authorization") String authToken,
            @RequestParam String unfollowedUsername) {
        authToken = jwtService.extractToken(authToken);
        String username = jwtService.extractUsername(authToken);
        ProfileEntity userLoggedBytoken = userRepo.findUserByUsername(username).get();
        ProfileEntity userUnfollowed = userRepo.findUserByUsername(unfollowedUsername).get();
        userService.unfollow(userLoggedBytoken.getId_profile(), userUnfollowed.getId_profile());
        return ResponseEntity
                .ok(userLoggedBytoken.getUsername() + " removed follow to " + userUnfollowed.getUsername() + "!");
    }

    // testato e funzionante
    @PostMapping("/auth/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassDto changerequest) {

        return userService.changePassword(changerequest.getUsername(), changerequest.getOldPass(),
                changerequest.getNewPass());
    }

    // ----------------------------------------------Delete-----------------------------------
    // testato e funzionante
    @DeleteMapping("/auth/delateMyAccount")
    public ResponseEntity<String> deleteById(@RequestHeader("Authorization") String authToken) {
        authToken = jwtService.extractToken(authToken);
        ProfileEntity myProfile = userRepo.findUserByUsername(jwtService.extractUsername(authToken)).get();
        userService.deleteByIdProfile(myProfile.getId_profile());
        return ResponseEntity.status(200).body("Your Accaunt was deleted!");
    }
    @DeleteMapping("auth/deletePost")
    public ResponseEntity<String> deletePostById(@RequestHeader("Authorization") String authToken,@RequestParam UUID id_Post) {
        authToken = jwtService.extractToken(authToken);
        ProfileEntity myProfile = userRepo.findUserByUsername(jwtService.extractUsername(authToken)).get();
        userService.deletePostById(id_Post);
        return ResponseEntity.status(200).body(myProfile.getUsername() + ", Your Post with id: " + id_Post + " was Delete Succesfully!");
    }
    // @DeleteMapping("/deleteAll")
    // public ResponseEntity<String> deleteAll() {
    // return userService.deleteAll();
    // }

    @GetMapping("/auth/sayHello")
    public String helloWorld() {
        return "Hello World";
    }

}
