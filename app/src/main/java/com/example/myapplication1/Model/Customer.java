package com.example.myapplication1.Model;

public class Customer {
    String id, name, adress, description;

    public Customer() {
    }

    public Customer(String name, String adress, String description) {
        this.name = name;
        this.adress = adress;
        this.description = description;
    }

    public Customer(String id, String name, String adress, String description) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
