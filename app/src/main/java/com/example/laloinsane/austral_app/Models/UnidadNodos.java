package com.example.laloinsane.austral_app.Models;

import java.util.ArrayList;

public class UnidadNodos {
    private int id_unidad;
    private int id_campus;
    private String nombre_unidad;
    private String descripcion_unidad;
    private double latitud_unidad;
    private double longitud_unidad;
    private ArrayList<Conexion> conexiones;
    private ArrayList<Nodo> nodos;

    public int getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
    }

    public int getId_campus() {
        return id_campus;
    }

    public void setId_campus(int id_campus) {
        this.id_campus = id_campus;
    }

    public String getNombre_unidad() {
        return nombre_unidad;
    }

    public void setNombre_unidad(String nombre_unidad) {
        this.nombre_unidad = nombre_unidad;
    }

    public String getDescripcion_unidad() {
        return descripcion_unidad;
    }

    public void setDescripcion_unidad(String descripcion_unidad) {
        this.descripcion_unidad = descripcion_unidad;
    }

    public double getLatitud_unidad() {
        return latitud_unidad;
    }

    public void setLatitud_unidad(double latitud_unidad) {
        this.latitud_unidad = latitud_unidad;
    }

    public double getLongitud_unidad() {
        return longitud_unidad;
    }

    public void setLongitud_unidad(double longitud_unidad) {
        this.longitud_unidad = longitud_unidad;
    }

    public ArrayList<Conexion> getConexiones() {
        return conexiones;
    }

    public void setConexiones(ArrayList<Conexion> conexiones) {
        this.conexiones = conexiones;
    }

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<Nodo> nodos) {
        this.nodos = nodos;
    }
}
