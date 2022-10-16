package com.example.esatis.Model;

public class Sepet {

    private String pid;
    private String urunAdi;
    private String miktar;
    private String fiyat;
    private String indirim;


    public Sepet(){

    }


    public Sepet(String pid, String urunAdi, String miktar, String fiyat, String indirim) {
        this.pid = pid;
        this.urunAdi = urunAdi;
        this.miktar = miktar;
        this.fiyat = fiyat;
        this.indirim = indirim;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getMiktar() {
        return miktar;
    }

    public void setMiktar(String miktar) {
        this.miktar = miktar;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getIndirim() {
        return indirim;
    }

    public void setIndirim(String indirim) {
        this.indirim = indirim;
    }


}
