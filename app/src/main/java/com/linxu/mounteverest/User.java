package com.linxu.mounteverest;

import java.io.Serializable;


/**
 * Created by lin xu on 26.01.2017.
 */

public class User implements Serializable{

    public String id;
    public String username;
    public String email;
    public String photo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.tobim.profiltest.User.class)
    }

    public User(String id, String username, String email, String photo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.photo = photo;
    }

    public String getId() { return id; }

    public String getPhoto() {
        return photo;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}

