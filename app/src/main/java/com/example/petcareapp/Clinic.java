package com.example.petcareapp;

public class Clinic {
    private String clinicId;
    private String clinicName;
    private String email;
    private String ownerId; // This links the clinic to the authenticated user

    // Default constructor required for Firebase Realtime Database
    public Clinic() {}

    // Constructor
    public Clinic(String clinicId, String clinicName, String email, String ownerId) {
        this.clinicId = clinicId;
        this.clinicName = clinicName;
        this.email = email;
        this.ownerId = ownerId; // This can be used to associate a clinic with a user
    }

    // Getters and Setters
    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
