package com.paulpwo.delivery305.models;

import android.support.annotation.Keep;

import java.util.ArrayList;

/**
 * Created by paulpwo on 17/7/16.
 */

public class DriversWorking {
    public String id;
    public String first_name;
    public String last_name;
    public String address;
    public String phone;
    public String email;
    public String date_online;
    public String count_delierys;


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

    public String getDate_online() {
        return date_online;
    }

    public void setDate_online(String date_online) {
        this.date_online = date_online;
    }

    public String getCount_delierys() {
        return count_delierys;
    }

    public void setCount_delierys(String count_delierys) {
        this.count_delierys = count_delierys;
    }


    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<DriversWorking> {
    }
}
