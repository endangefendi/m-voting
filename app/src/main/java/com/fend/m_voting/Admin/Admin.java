package com.fend.m_voting.Admin;


public class Admin {
    public String nim,password, name, email, phone;

    public Admin() {

    }
    //ini untuk control ke db admin sdi tambah text

    public Admin(String nim, String password, String name, String email,  String phone) {
        this.nim = nim;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getNIM() {
        return nim;
    }

    public String getPassword() {
        return password;
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
}
