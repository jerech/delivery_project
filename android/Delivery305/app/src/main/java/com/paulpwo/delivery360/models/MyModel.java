package com.paulpwo.delivery360.models;

/**
 * Created by paulpwo on 10/7/16.
 */

public class MyModel {
    private String time;
    private String address;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MyModel(String time, String address) {
        this.time = time;
        this.address = address;
    }
}
