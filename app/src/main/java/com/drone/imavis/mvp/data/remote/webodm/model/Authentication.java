package com.drone.imavis.mvp.data.remote.webodm.model;

/**
 * Created by adigu on 07.05.2017.
 */

public class Authentication {

    private String username;
    private String password;

    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
