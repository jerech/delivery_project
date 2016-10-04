package com.paulpwo.delivery305.models;


import android.support.annotation.Keep;

import java.util.ArrayList;

public class Contributor {
    public String id;
    public String categorias;
    public String nombre;
    public String direccion;
    public String telefonos;
    public String masinfo;
       // ============================================================================================
    // FOR RECYCLER
    // ============================================================================================

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasinfo() {
        return masinfo;
    }

    public void setMasinfo(String masinfo) {
        this.masinfo = masinfo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    @Override
    public String toString() {
        return "ContributorDemo{" +
                "categorias='" + categorias + '\'' +
                ", id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefonos='" + telefonos + '\'' +
                ", masinfo='" + masinfo + '\'' +
                '}';
    }

     // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Contributor> {
    }
}
