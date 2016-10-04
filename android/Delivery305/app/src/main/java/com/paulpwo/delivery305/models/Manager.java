package com.paulpwo.delivery305.models;

import android.support.annotation.Keep;

import java.util.ArrayList;

/**
 * Created by paulpwo on 4/7/16.
 */

public class Manager {
    public String id;
    public String email;
    public String api_key;
    public String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", api_key='" + api_key + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Manager> {
    }
}
