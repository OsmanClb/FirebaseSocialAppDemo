package com.example.firebasesocialapp;

import java.io.Serializable;

public class Person implements Serializable {
    private String userId;
    private String displayName;
    private String email;
    private String photoUrl;
    private String authenticationMethod;

    public Person() {
    }

    public Person(String userId, String displayName, String email, String photoUrl, String authenticationMethod){
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.authenticationMethod = authenticationMethod;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getDisplayName(){
        return displayName;
    }
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }

    public String getAuthenticationMethod(){
        return authenticationMethod;
    }
    public void setAuthenticationMethod(String authenticationMethod){
        this.authenticationMethod = authenticationMethod;
    }

}
