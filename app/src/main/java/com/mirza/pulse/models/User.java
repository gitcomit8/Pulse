package com.mirza.pulse.models;

/**
 * Represents a user in the application.
 * This class stores user information such as unique ID, email, display name, and profile image URL.
 */
public class User {
    private String uid;
    private String email;
    private String displayName;
    private String profileImageUrl;

    // Constructor (empty constructor is needed for Firestore)
    public User() {
    }

    // Constructor with parameters
    public User(String uid, String email, String displayName, String profileImageUrl) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}