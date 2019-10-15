package com.example.recyclerviewhoteles.models;

import io.realm.RealmObject;

public class Lugar extends RealmObject {
    private int lugId;
    private String lugNombre;
    private String lugDireccion;
    private String lugTelefono;
    private String lugCorreo;
    private String lugLatitud;
    private String lugLongitud;
    private String lugDescripcion;
    private String tipoLugar;
    private int imagenHotel;

    public Lugar() {
    }

    public Lugar(int lugId, String lugNombre, String lugDireccion, String lugTelefono, String lugCorreo, String lugLatitud, String lugLongitud, String lugDescripcion, String tipoLugar, int imagenHotel) {
        this.lugId = lugId;
        this.lugNombre = lugNombre;
        this.lugDireccion = lugDireccion;
        this.lugTelefono = lugTelefono;
        this.lugCorreo = lugCorreo;
        this.lugLatitud = lugLatitud;
        this.lugLongitud = lugLongitud;
        this.lugDescripcion = lugDescripcion;
        this.tipoLugar = tipoLugar;
        this.imagenHotel = imagenHotel;
    }

    public int getLugId() {
        return lugId;
    }

    public void setLugId(int lugId) {
        this.lugId = lugId;
    }

    public String getLugNombre() {
        return lugNombre;
    }

    public void setLugNombre(String lugNombre) {
        this.lugNombre = lugNombre;
    }

    public String getLugDireccion() {
        return lugDireccion;
    }

    public void setLugDireccion(String lugDireccion) {
        this.lugDireccion = lugDireccion;
    }

    public String getLugTelefono() {
        return lugTelefono;
    }

    public void setLugTelefono(String lugTelefono) {
        this.lugTelefono = lugTelefono;
    }

    public String getLugCorreo() {
        return lugCorreo;
    }

    public void setLugCorreo(String lugCorreo) {
        this.lugCorreo = lugCorreo;
    }

    public String getLugLatitud() {
        return lugLatitud;
    }

    public void setLugLatitud(String lugLatitud) {
        this.lugLatitud = lugLatitud;
    }

    public String getLugLongitud() {
        return lugLongitud;
    }

    public void setLugLongitud(String lugLongitud) {
        this.lugLongitud = lugLongitud;
    }

    public String getLugDescripcion() {
        return lugDescripcion;
    }

    public void setLugDescripcion(String lugDescripcion) {
        this.lugDescripcion = lugDescripcion;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public int getImagenHotel() {
        return imagenHotel;
    }

    public void setImagenHotel(int imagenHotel) {
        this.imagenHotel = imagenHotel;
    }
}