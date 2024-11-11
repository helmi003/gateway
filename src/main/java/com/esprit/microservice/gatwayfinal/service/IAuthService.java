package com.esprit.microservice.gatwayfinal.service;

import com.esprit.microservice.gatwayfinal.dao.ResponseMessage;
import com.esprit.microservice.gatwayfinal.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface IAuthService {
    public ResponseEntity<?> login(String username, String password);
    public ResponseEntity<ResponseMessage> logout(String token);
    Object[] createUser(User userRegistration);
    public void emailVerification(String userId);
    public User getUserById(String userId);
    public ResponseEntity<String> deleteUserById(String userId);
    public List<User> getAllUsers();
    public Object[] updateUser(String id,User userRegistration);
    public ResponseEntity<?> checkUser(Jwt jwtToken);
    public ResponseEntity<?> approveUser(String userId);
    public ResponseEntity<?> blockUser(String userId);
    public ResponseEntity<ResponseMessage> forgotPassword(String username);
    public ResponseEntity<?> updatePassword(String oldPassword,String newPassword,String username);
    public ResponseEntity<?> refreshToken(String token);
    public ResponseEntity<?> addImageToUser(String userId, MultipartFile image);
    public ResponseEntity<Resource> getUserImage(String userId)  throws IOException;
    public void sendEmailToUser(String id);
}
