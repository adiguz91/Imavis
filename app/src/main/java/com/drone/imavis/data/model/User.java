package com.drone.imavis.data.model;

import com.drone.imavis.data.local.BaseEntity;

/**
 * Created by adigu on 06.05.2017.
 */

public class User extends BaseEntity {

    private String firstname;
    private String lastname;
    private String eMail;
    //private String passwordHash;

    public User() {}

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
