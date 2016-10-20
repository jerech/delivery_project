package com.paulpwo.delivery360.models;

import java.util.ArrayList;

/**
 * Created by paulpwo on 17/7/16.
 */

public class DriverDeliveryAvailable {

    public String id;
    public String address;
    public String note;
    public String time;
    public String time_driver;
    public String status;
    public String id_restaurant;
    public String restaurant;
    public  String phone_restaurant;
    public String restaurant_address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_driver() {
        return time_driver;
    }

    public void setTime_driver(String time_driver) {
        this.time_driver = time_driver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(String id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getPhone_restaurant() {
        return phone_restaurant;
    }

    public void setPhone_restaurant(String phone_restaurant) {
        this.phone_restaurant = phone_restaurant;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }
// ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<DriverDeliveryAvailable> {
    }
}
