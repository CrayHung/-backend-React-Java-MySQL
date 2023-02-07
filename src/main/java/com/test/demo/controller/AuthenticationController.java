package com.test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.test.demo.config.SecurityConfig;

import com.test.demo.config.JwtUtils;
import com.test.demo.dao.UserDao;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;

    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    // 路徑 "/auth/authenticate"    來獲得user認證
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenicate(
        @RequestBody AuthenticationRequest request
    ){
        //有新的User時,透過authenticationManager新增此User的認證(authenticate)
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        //讀取USER的EMAIL資料 , 如果有user資料就回傳jwtUtils
        //final UserDetails user = userService.findByEmail(request.getEmail());
        final UserDetails user = userDao.findUserByEmail(request.getEmail());
        if(user!=null){
            return ResponseEntity.ok(jwtUtils.generateToken(user));
        }
        return ResponseEntity.status(400).body("some error happened");
    }
}


