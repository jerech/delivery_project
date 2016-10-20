package com.paulpwo.delivery360.models;

import java.util.ArrayList;

/**
 * Created by paulpwo on 4/7/16.
 */

public class Restaurant {

    public String id;
    public String name;
    public String address;
    public String phone;
    public String email;
    public String api_key;
    public String image_url;
    public String created_at;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", api_key='" + api_key + '\'' +
                ", image_url='" + image_url + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Restaurant> {
    }
}
