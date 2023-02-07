package com.test.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.entity.Users;
import com.test.demo.service.UserService;

// // @CrossOrigin 在controller上面加CORS
@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;


    @GetMapping("/allUsers")
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> users = null;
        try{
            users = userService.getAllUsers();
        }
        catch(Exception ex){
            ex.getMessage();
        }
        return new ResponseEntity<List<Users>>(users , HttpStatus.OK);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Users> getUserByID(@PathVariable("id") int ID){
        Users users = null;
        try{
            users = userService.getUserByID(ID);
        }
        catch(Exception ex){
            ex.getMessage();
        }
        return new ResponseEntity<Users>(users , HttpStatus.OK);
    }


    @PostMapping("/addorUpdate")
    public ResponseEntity<Users> addOrUpdate(@RequestBody Users user){
        Users users = null;
        try{
            users = userService.addOrUpdateUser(user);
        }
        catch(Exception ex){
            ex.getMessage();
        }
        return new ResponseEntity<Users>(users , HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Users> delete(@PathVariable("id") int ID){
        Users users = null;
        try{
            users = userService.deleteUser(ID);
        }
        catch(Exception ex){
            ex.getMessage();
        }
        return new ResponseEntity<Users>(users , HttpStatus.OK );
    }
}
