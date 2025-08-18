package com.s23010467.easy_market;

public class User {
    public String name;
    public String email;
    public String role;

    // Empty constructor required for Firebase
    public User() {
    }

    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
