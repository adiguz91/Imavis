package com.drone.imavis.data.model;

import com.drone.imavis.data.local.BaseEntity;

import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

public class User extends BaseEntity {

    private String firstname;
    private String lastname;
    private String email;
    //private String passwordHash;
    private List<Project> projectList;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
