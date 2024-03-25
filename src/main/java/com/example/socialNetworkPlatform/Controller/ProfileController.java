package com.example.socialNetworkPlatform.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialNetworkPlatform.Config.filtri.AuthRequest;
import com.example.socialNetworkPlatform.Entitys.PostEntity;
import com.example.socialNetworkPlatform.Entitys.ProfileEntity;
import com.example.socialNetworkPlatform.Repositories.ProfileRepository;
import com.example.socialNetworkPlatform.Services.JwtService;
import com.example.socialNetworkPlatform.Services.UserInfoService;
import com.example.socialNetworkPlatform.Services.dto.ChangePassDto;
import com.example.socialNetworkPlatform.Services.dto.PostView;
import com.example.socialNetworkPlatform.Services.dto.UserRegistration;
import com.example.socialNetworkPlatform.Services.dto.UserView;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {
    @Autowired
    UserInfoService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    ProfileRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    // testato e funzionante
    @PostMapping("/public/generate_token")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Not Allowed! Wrong Credential Error Try Again.");
        }
    }

    // testato e funzionante
    @PostMapping("/public/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegistration entity) {
        userService.registerUser(entity);
        return ResponseEntity.ok("ok");
    }

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

                // Set Dei post dell'utente "username"
                if (userService.getPostForProfile(myProfile.getId_profile()).size() > 0) {
                    List<PostEntity> filteredPosts = userService.getPostForProfile(myProfile.getId_profile());
                    PostView postdto = new PostView();
                    for (PostEntity post : filteredPosts) {
                        postdto.setId_post(post.getId_Post());
                        postdto.setBody(post.getBody());
                        postdto.setHeader(post.getHeader());
                        postdto.setCreationDateTime(post.getCreationDateTime());
                        posts.add(postdto);
                    }
                } else {
                    userDto.setPosts(posts);
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

    // testato e funzionante
    @GetMapping("/auth/follow")
    public ResponseEntity<String> follow(@RequestHeader("Authorization") String authToken, @RequestParam String followedUsername) {
        authToken = jwtService.extractToken(authToken);
        String username = jwtService.extractUsername(authToken);
        ProfileEntity userLoggedBytoken = userRepo.findUserByUsername(username).get();
        ProfileEntity userFollowed = userRepo.findUserByUsername(followedUsername).get();
        userService.follow(userLoggedBytoken.getId_profile(), userFollowed.getId_profile());
        return ResponseEntity.ok(userLoggedBytoken.getUsername() + " started follow " + userFollowed.getUsername()+"!");
    }

    // testato e funzionante
    @PostMapping("/auth/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassDto changerequest) {

        return userService.changePassword(changerequest.getUsername(), changerequest.getOldPass(),
                changerequest.getNewPass());
    }

    // @DeleteMapping("/deleteAll")
    // public ResponseEntity<String> deleteAll() {
    // return userService.deleteAll();
    // }

    // @DeleteMapping("/deleteById{id}")
    // public ResponseEntity<String> deleteById(@PathVariable UUID id) {
    // userService.deleteById(id);
    // return ResponseEntity.status(201).build();
    // }

    @GetMapping("/auth/sayHello")
    public String helloWorld() {
        return "Hello World";
    }

}
