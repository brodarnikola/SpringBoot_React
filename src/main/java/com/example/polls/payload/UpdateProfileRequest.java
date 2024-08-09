package com.example.polls.payload;


public class UpdateProfileRequest {

    private String oldUsername;
    private String oldEmail;
    private String oldName;
    private String username;
    private String email;
    private String name;

    /* public UpdateProfileRequest(String oldUsername, String oldEmail, String oldName, String username, String email, String name) {
        this.oldUsername = oldUsername;
        this.oldEmail = oldEmail;
        this.oldName = oldName;
        this.username = username;
        this.email = email;
        this.name = name;
    } */

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
