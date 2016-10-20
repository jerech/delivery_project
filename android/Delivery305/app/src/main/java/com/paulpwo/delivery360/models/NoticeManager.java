package com.paulpwo.delivery360.models;

import java.util.ArrayList;

/**
 * Created by paulpwo on 13/7/16.
 */

public class NoticeManager {
    Integer id;
    String title;
    Integer id_delivery;
    Integer id_restaurant;
    String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId_delivery() {
        return id_delivery;
    }

    public void setId_delivery(Integer id_delivery) {
        this.id_delivery = id_delivery;
    }

    public Integer getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(Integer id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NoticeManager{" +
                ", id=" + id +
                "title='" + title + '\'' +
                ", id_delivery=" + id_delivery +
                ", id_restaurant=" + id_restaurant +
                ", body='" + body + '\'' +
                '}';
    }

    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<NoticeManager> {
    }
}
