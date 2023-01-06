package com.test.demo.service_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.demo.entity.Users;
import com.test.demo.repository.UserRepository;
import com.test.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    
    @Override
    public List<Users> getAllUsers(){

        return (List<Users>) userRepository.findAll();
    }

    @Override
    public Users getUserByID(int ID){
        return userRepository.findById(ID).orElse(null);
    }

    @Override
    public Users addOrUpdateUser(Users user){
        return userRepository.save(user);
    }

    @Override
    public Users deleteUser(int ID) throws Exception{
        Users deleteUser = null;
        try{
            deleteUser = userRepository.findById(ID).orElse(null);
            if(deleteUser==null){
                throw new Exception("user not available");
            }else{
                userRepository.deleteById(ID);
            }
        }
        catch(Exception ex){
            throw ex;
        }
        return deleteUser;
    }
}
