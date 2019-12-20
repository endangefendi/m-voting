package com.fend.m_voting.Calon;

public class Calon {
    public String no_urut,nama, angkatan, visi, misi, suara, foto, des;

    public Calon() {

    }

    public Calon (String no_urut, String nama, String angkatan, String visi, String misi, String des, String foto, String suara) {
        this.no_urut = no_urut;
        this.nama = nama;
        this.angkatan = angkatan;
        this.visi = visi;
        this.misi = misi;
        this.suara = suara;
        this.foto = foto;
        this.des = des;
    }

    public String getNo_urut() {
        return no_urut;
    }
    public String getDes() {
        return des;
    }
    public String getNama() {
        return nama;
    }

    public String getAngkatan() {
        return angkatan;
    }

    public String getVisi() {
        return visi;
    }

    public String getMisi() {
        return misi;
    }

    public String getSuara() {
        return suara;
    }

    public String getFoto() {
        return foto;
    }

    public void setNo_urut(String no_urut) {
        this.no_urut = no_urut;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAngkatan(String angkatan) {
        this.angkatan = angkatan;
    }

    public void setVisi(String visi) {
        this.visi = visi;
    }

    public void setMisi(String misi) {
        this.misi = misi;
    }

    public void setSuara(String suara) {
        this.suara = suara;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setDes(String des) {
        this.des = des;
    }


}