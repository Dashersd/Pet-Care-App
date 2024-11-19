package com.example.petcareapp;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String name;
    private String email;
    private Map<String, Map<String, Object>> petInformation; // Map to store each pet's information with a unique ID

    // Default constructor required for Firebase
    public User() {
        this.petInformation = new HashMap<>(); // Initialize the petInformation map
    }

    // Constructor with parameters
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.petInformation = new HashMap<>(); // Initialize the petInformation map
    }

    // Method to add a new pet to the user's pet information
    public void addPet(String petId, String petName, String dob, String gender, String breed, String petType) {
        Map<String, Object> petDetails = new HashMap<>();
        petDetails.put("petName", petName);
        petDetails.put("dob", dob);
        petDetails.put("gender", gender);
        petDetails.put("breed", breed);
        petDetails.put("petType", petType);

        this.petInformation.put(petId, petDetails);
    }

    // Getters and Setters for User fields
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Map<String, Object>> getPetInformation() {
        return petInformation;
    }

    public void setPetInformation(Map<String, Map<String, Object>> petInformation) {
        this.petInformation = petInformation;
    }

    // Method to retrieve a specific pet's information by petId
    public Map<String, Object> getPetDetails(String petId) {
        return petInformation.get(petId);
    }

    // Method to remove a pet from pet information by petId
    public void removePet(String petId) {
        this.petInformation.remove(petId);
    }
}
