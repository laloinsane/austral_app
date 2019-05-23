package com.example.laloinsane.austral_app.Models;

import java.util.ArrayList;

public class CampusRespuesta {
    private ArrayList<Campus> campus;
    private int total_campus;

    public int getTotal_campus() {
        return total_campus;
    }

    public void setTotal_campus(int total_campus) {
        this.total_campus = total_campus;
    }

    public ArrayList<Campus> getCampus() {
        return campus;
    }

    public void setCampus(ArrayList<Campus> campus) {
        this.campus = campus;
    }
}
