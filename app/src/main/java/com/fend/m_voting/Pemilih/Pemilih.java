package com.fend.m_voting.Pemilih;

public class Pemilih {

    public String nim, name, email, phone, status;

    public Pemilih() {

    }

    public Pemilih(String nim, String name, String email,  String phone, String status) {
        this.nim = nim;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status= status;
        //this.pin= pin;
    }


    //public String getPin() {
    //  return pin;
    //}

    public String getNIM() {
        return nim;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {

        return status;
    }
}