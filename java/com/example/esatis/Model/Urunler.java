package com.example.esatis.Model;

public class Urunler {

    private String urunAdi, cesit, fiyat, fotograf, kategori, pid, tarih, zaman;

    public Urunler(){

    }

    public Urunler(String pname, String cesit, String fiyat, String img, String kategori, String pid, String tarih, String zaman) {
        this.urunAdi = pname;
        this.cesit = cesit;
        this.fiyat = fiyat;
        this.fotograf = img;
        this.kategori = kategori;
        this.pid = pid;
        this.tarih = tarih;
        this.zaman = zaman;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getCesit() {
        return cesit;
    }

    public void setCesit(String cesit) {
        this.cesit = cesit;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getFotograf() {
        return fotograf;
    }

    public void setFotograf(String fotograf) {
        this.fotograf = fotograf;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }
}
