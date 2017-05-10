package com.drone.imavis.mvp.data.remote.webodm.model;

/**
 * Created by adigu on 07.05.2017.
 */

public class Authentication {

    private String username;
    private String password;

    public Authentication() {}

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
}
