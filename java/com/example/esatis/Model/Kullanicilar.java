package com.example.esatis.Model;

public class Kullanicilar {
    private String kullanici, numara, sifre, image, adres;

    public Kullanicilar(){ }

    public Kullanicilar(String kullanici, String numara, String sifre, String image, String adres) {
        this.kullanici = kullanici;
        this.numara = numara;
        this.sifre = sifre;
        this.image = image;
        this.adres = adres;
    }

    public String getKullanici() {
        return kullanici;
    }

    public void setKullanici(String kullanici) {
        this.kullanici = kullanici;
    }

    public String getNumara() {
        return numara;
    }

    public void setNumara(String numara) {
        this.numara = numara;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
