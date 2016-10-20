package com.paulpwo.delivery360.models;

import java.util.ArrayList;

/**
 * Created by paulpwo on 14/7/16.
 */

public class NoticeDelivery {
    String id_delivery;
    String delivery_address;
    String id_restaurant;
    String restaurant_name;
    String restaurant_address;
    String phone;
    String email;
    String time;

    public String getId_delivery() {
        return id_delivery;
    }

    public void setId_delivery(String id_delivery) {
        this.id_delivery = id_delivery;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(String id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<NoticeDelivery> {
    }
}
