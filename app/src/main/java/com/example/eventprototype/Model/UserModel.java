package com.example.eventprototype.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    //attributes
    private int id;
    private int isStaff, isInternational;
    private String username;
    private String password;
    private String firstName, lastName;
    private String dob;
    private String degree;
    private String country;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(int isStaff) {
        this.isStaff = isStaff;
    }

    public int getIsInternational() {
        return isInternational;
    }

    public void setIsInternational(int isInternational) {
        this.isInternational = isInternational;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}