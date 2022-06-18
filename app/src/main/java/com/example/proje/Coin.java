package com.example.proje;

import java.text.DecimalFormat;

public class Coin {
    private String paraAdi;
    private double aFiyat,sFiyat;

    public Coin(String paraAdi,double aFiyat,double sFiyat) {
        this.paraAdi = paraAdi;
        this.aFiyat = aFiyat;
        this.sFiyat=sFiyat;
    }

    public String getParaAdi() {
        return paraAdi;
    }

    public void setParaAdi(String paraAdi) {
        this.paraAdi = paraAdi;
    }

    public double getaFiyat() {
        return aFiyat;
    }

    public void setaFiyat(double fiyat) {
        this.aFiyat = fiyat;
    }

    public double getsFiyat() { return sFiyat; }

    public void setsFiyat(double sFiyat) { this.sFiyat = sFiyat; }
}
