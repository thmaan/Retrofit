package com.example.retrofit;

import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("auto_increment_id")
    private String auto_increment_id;
    private String name;
    private String email;

    public Customer(String auto_increment_id, String name, String email) {
        this.auto_increment_id = auto_increment_id;
        this.name = name;
        this.email = email;
    }

    public String getAuto_increment_id() {
        return auto_increment_id;
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
}
