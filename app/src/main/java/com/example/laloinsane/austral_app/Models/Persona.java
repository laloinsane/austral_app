package com.example.laloinsane.austral_app.Models;

public class Persona {
    private int id_persona;
    private int id_unidad;
    private String nombre_persona;
    private String primer_nombre_persona;
    private String segundo_nombre_persona;
    private String primer_apellido_persona;
    private String segundo_apellido_persona;
    private String cargo_persona;
    private String correo_persona;
    private String fono_persona;

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public int getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
    }

    public String getNombre_persona() {
        return nombre_persona;
    }

    public void setNombre_persona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }

    public String getPrimer_nombre_persona() {
        return primer_nombre_persona;
    }

    public void setPrimer_nombre_persona(String primer_nombre_persona) {
        this.primer_nombre_persona = primer_nombre_persona;
    }

    public String getSegundo_nombre_persona() {
        return segundo_nombre_persona;
    }

    public void setSegundo_nombre_persona(String segundo_nombre_persona) {
        this.segundo_nombre_persona = segundo_nombre_persona;
    }

    public String getPrimer_apellido_persona() {
        return primer_apellido_persona;
    }

    public void setPrimer_apellido_persona(String primer_apellido_persona) {
        this.primer_apellido_persona = primer_apellido_persona;
    }

    public String getSegundo_apellido_persona() {
        return segundo_apellido_persona;
    }

    public void setSegundo_apellido_persona(String segundo_apellido_persona) {
        this.segundo_apellido_persona = segundo_apellido_persona;
    }

    public String getCargo_persona() {
        return cargo_persona;
    }

    public void setCargo_persona(String cargo_persona) {
        this.cargo_persona = cargo_persona;
    }

    public String getCorreo_persona() {
        return correo_persona;
    }

    public void setCorreo_persona(String correo_persona) {
        this.correo_persona = correo_persona;
    }

    public String getFono_persona() {
        return fono_persona;
    }

    public void setFono_persona(String fono_persona) {
        this.fono_persona = fono_persona;
    }
}
