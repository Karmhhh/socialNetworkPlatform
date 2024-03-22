package com.example.socialNetworkPlatform.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialNetworkPlatform.Entitys.ProfileEntity;
import com.example.socialNetworkPlatform.Services.UserInfoDetails;
import com.example.socialNetworkPlatform.Services.UserInfoService;
import com.example.socialNetworkPlatform.Services.dto.UserRegistration;
import com.example.socialNetworkPlatform.Services.dto.UserView;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/public")
public class ProfileController {
    private Logger LOG = LoggerFactory.getLogger(getClass());
        @Autowired
    UserInfoService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegistration entity) {
       LOG.info(entity.getBio().toString()+"seiqui");
        userService.registerUser(entity); 
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/getAllProfiles")
    public  List<UserView> getAllProfiles() {
        return  userService.getAll();
    }
    @GetMapping("/sayHello")
    public String helloWorld() {
        return "Hello World";
    }

}
