package com.fend.m_voting.Jadwal;


public class JadwalRegistrasi {

    public String tanggalmulai, tanggalakhir;


    public JadwalRegistrasi(String tanggalmulai, String tanggalakhir) {
        this.tanggalmulai = tanggalmulai;
        this.tanggalakhir = tanggalakhir;
    }


    public String getTanggalmulai() {
        return tanggalmulai;
    }

    public String getTanggalakhir() {
        return tanggalakhir;
    }




}