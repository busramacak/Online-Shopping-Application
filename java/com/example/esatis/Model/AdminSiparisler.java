package com.example.esatis.Model;

public class AdminSiparisler {

    private String adSoyad, tarih, saat, telNo, teslimatAdresi, il, ilce, toplamFiyat, durum;

    public AdminSiparisler(){

    }

    public AdminSiparisler(String adSoyad, String tarih, String saat, String telNo, String teslimatAdresi, String il, String ilce, String fiyat, String durum) {
        this.adSoyad = adSoyad;
        this.tarih = tarih;
        this.saat = saat;
        this.telNo = telNo;
        this.teslimatAdresi = teslimatAdresi;
        this.il = il;
        this.ilce = ilce;
        this.toplamFiyat = fiyat;
        this.durum = durum;
    }


    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getTeslimatAdresi() {
        return teslimatAdresi;
    }

    public void setTeslimatAdresi(String teslimatAdresi) {
        this.teslimatAdresi = teslimatAdresi;
    }

    public String getIl() {
        return il;
    }

    public void setIl(String il) {
        this.il = il;
    }

    public String getIlce() {
        return ilce;
    }

    public void setIlce(String ilce) {
        this.ilce = ilce;
    }

    public String getToplamFiyat() {
        return toplamFiyat;
    }

    public void setToplamFiyat(String toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
