package com.example.mobilalkfejl_kotprog.models;

public class Team extends BaseModel {
    private String name;
    private String city;
    private String principal;


    public Team() {}

    public Team(String id, String name, String city, String principal) {
        super(id);
        this.name = name;
        this.city = city;
        this.principal = principal;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getPrincipal() {
        return principal;
    }
}
