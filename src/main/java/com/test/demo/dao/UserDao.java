package com.test.demo.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;



//要將資料永久化(database)的話 , 增加DAO(Data Acccess Object)
@Repository
public class UserDao {
    

      //要新增使用者,則在這邊增加
      private final static List<UserDetails> APPLICATION_USERS = Arrays.asList(
        new User(
            "crayhung168@gmail.com",
            "password",
            Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ),
        new User(
            "user.mail@gmail.com",
            "password",
            Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
        )
    );


    public UserDetails findUserByEmail(String email){
        return APPLICATION_USERS
                        .stream()
                        .filter(u-> u.getUsername().equals(email))
                        .findFirst()
                        .orElseThrow(()->new UsernameNotFoundException("No user was Found"))
                        
                        ;
    }


}
