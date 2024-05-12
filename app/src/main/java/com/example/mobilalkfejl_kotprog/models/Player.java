package com.example.mobilalkfejl_kotprog.models;

public class Player extends BaseModel {
    private String name;
    private String role;
    private String birthDate;


    public Player() {}

    public Player(String id, String name, String role, String birthDate) {
        super(id);
        this.name = name;
        this.role = role;
        this.birthDate = birthDate;
    }


    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
