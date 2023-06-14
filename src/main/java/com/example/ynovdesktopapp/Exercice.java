package com.example.ynovdesktopapp;

public class Exercice {

    private int Id;
    private String Exercice;
    private String URLPhoto;
    private String musclesSolicites;
    private boolean HautDuCorps;
    private boolean BasDuCorps;

    public Exercice(int Id, String Exercice, String URLPhoto, String Description, Boolean hautDuCorps, Boolean basDuCorps) {
        this.Id = Id;
        this.Exercice = Exercice;
        this.URLPhoto = URLPhoto;
        this.musclesSolicites = Description;
        this.HautDuCorps = hautDuCorps;
        this.BasDuCorps = basDuCorps;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getExercice() {
        return Exercice;
    }

    public void setExercice(String exercice) {
        this.Exercice = exercice;
    }

    public String getMusclesSolicites() {
        return musclesSolicites;
    }

    public void setMusclesSolicites(String musclesSolicites) {
        this.musclesSolicites = musclesSolicites;
    }

    public String getURLPhoto() {
        return URLPhoto;
    }

    public void setURLPhoto(String URLPhoto) {
        this.URLPhoto = URLPhoto;
    }

    public boolean isHautDuCorps() {
        return HautDuCorps;
    }

    public void setHautDuCorps(boolean hautDuCorps) {
        HautDuCorps = hautDuCorps;
    }

    public boolean isBasDuCorps() {
        return BasDuCorps;
    }

    public void setBasDuCorps(boolean basDuCorps) {
        BasDuCorps = basDuCorps;
    }
}


