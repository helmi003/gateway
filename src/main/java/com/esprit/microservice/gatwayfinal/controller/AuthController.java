package com.esprit.microservice.gatwayfinal.controller;

import com.esprit.microservice.gatwayfinal.dao.ResponseMessage;
import com.esprit.microservice.gatwayfinal.entity.User;
import com.esprit.microservice.gatwayfinal.repository.UserRepository;
import com.esprit.microservice.gatwayfinal.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IAuthService authService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    @PreAuthorize("hasRole('Admin')")
    public String hello(){
        return "Hello there!";
    }

    @GetMapping("/hello-2")
    @PreAuthorize("hasRole('Student')")
    public String hello2(){
        return "Hello there! - Student";
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login (@RequestBody Map<String, String> requestBody) {
        return authService.login(requestBody.get("username"),requestBody.get("password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout (@RequestBody Map<String, String> requestBody) {
        return authService.logout(requestBody.get("token"));
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody User userRegistration) {
        Object[] obj = authService.createUser(userRegistration);
        int status = (int) obj[0];
        ResponseMessage message = (ResponseMessage) obj[1];
        return ResponseEntity.status(status).body(message);
    }

    @GetMapping("/user-id/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public User getUser(@PathVariable String userId) {
        return authService.getUserById(userId);
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('Admin')")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @DeleteMapping("delete-user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId) {
        return authService.deleteUserById(userId);
    }


    @GetMapping("/user-details")
    public User getUserDetails(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return authService.getUserById(userId);
    }

    @PutMapping("/emailVerification")
    public ResponseEntity<ResponseMessage> emailVerification(Authentication authentication) {
        ResponseMessage message = new ResponseMessage();
        try {
            Jwt jwtToken = (Jwt) authentication.getPrincipal();
            authService.emailVerification(jwtToken.getClaim("sub"));
            message.setMessage("Email verification sent successfully, check your Inbox");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            message.setMessage("Failed to send email verification");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkValidity(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        return authService.checkUser(jwtToken);
    }


    @PutMapping("/update-user")
    public ResponseEntity<Object> updateUser(Authentication authentication,@RequestBody User requestBody) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        Object[] result = authService.updateUser(userId, requestBody);
        int statusCode = (Integer) result[0];
        if (statusCode == 200) {
            Object data = result[1];
            return ResponseEntity.ok(data);
        } else {
            ResponseMessage errorMessage = (ResponseMessage) result[1];
            return ResponseEntity.status(statusCode).body(errorMessage);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> requestBody) {
        return authService.refreshToken(requestBody.get("token"));
    }

    @PutMapping("/approve-user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> approveUser(@PathVariable String userId) {
        return authService.approveUser(userId);
    }

    @PutMapping("/block-user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> activateUser(@PathVariable String userId) {
       return authService.blockUser(userId);
    }


    @PutMapping("forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody Map<String, String> requestBody) {
        return authService.forgotPassword(requestBody.get("username"));
    }

    @PutMapping("update-password")
    public ResponseEntity<?> updatePassword(Authentication authentication,@RequestBody Map<String, String> requestBody) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String username = jwtToken.getClaim("preferred_username");
        return authService.updatePassword(requestBody.get("oldPassword"),requestBody.get("newPassword"),username);
    }

    @PutMapping("update-image")
    public ResponseEntity<?> addImageToUser(Authentication authentication,@RequestParam MultipartFile image)  throws IOException {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return authService.addImageToUser(userId,image);
    }

    @GetMapping("user-image")
    public ResponseEntity<Resource> getUserImage(Authentication authentication)  throws IOException {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return authService.getUserImage(userId);
    }

    @GetMapping("user-image-admin/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Resource> getUserImageByAdmin(@PathVariable String userId)  throws IOException {
        return authService.getUserImage(userId);
    }

    @GetMapping(value = "generateQRCode/{userId}",produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public void generateQrCodeImage(@PathVariable String userId) {
        authService.sendEmailToUser(userId);
    }

}
