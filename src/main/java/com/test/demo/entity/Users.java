package com.test.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "testtable")
public class Users{

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int ID;

    @Column(name = "Email")
    private String Email;

    //Constructor
    public Users(){
        super();
    }


    public Users(int ID,String Email){
        super();
        this.ID=ID;
        this.Email=Email;
    }


    public int getID(){
        return ID;
    }

    public void setID(int ID){
        this.ID=ID;
    }

    public String getEmail(){
        return Email;
    }

    public void setEmail(String Email){
        this.Email=Email;
    }

    @Override
    public String toString(){
        return "Users [ID=" + ID +",Email=" + Email; 
    }
}