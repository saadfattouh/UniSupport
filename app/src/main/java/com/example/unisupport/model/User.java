package com.example.unisupport.model;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;
    private String jobCareer;
    private String licensePhoto;
    private String profileImage;

    public User(int id, String firstName, String lastName, String email, String phone, String bio, String jobCareer, String licensePhoto, String profileImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.jobCareer = jobCareer;
        this.licensePhoto = licensePhoto;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getJobCareer() {
        return jobCareer;
    }

    public void setJobCareer(String jobCareer) {
        this.jobCareer = jobCareer;
    }

    public String getLicensePhoto() {
        return licensePhoto;
    }

    public void setLicensePhoto(String licensePhoto) {
        this.licensePhoto = licensePhoto;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
