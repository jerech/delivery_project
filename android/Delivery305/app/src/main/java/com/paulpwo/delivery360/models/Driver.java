package com.paulpwo.delivery360.models;

import java.util.ArrayList;

/**
 * Created by paulpwo on 4/7/16.
 */

public class Driver {
    public String id;
    public String first_name;
    public String last_name;
    public String address;
    public String phone;
    public String email;
    public String api_key;
    public String image_url;
    public Integer manager;
    public String created_At;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getCreated_At() {
        return created_At;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", api_key='" + api_key + '\'' +
                ", image_url='" + image_url + '\'' +
                ", image_url='" + manager + '\'' +
                ", created_At='" + created_At + '\'' +
                '}';
    }
    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Driver> {
    }
}
