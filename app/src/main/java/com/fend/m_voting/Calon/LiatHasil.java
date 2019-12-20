package com.fend.m_voting.Calon;

public class LiatHasil {
    public int no_urut, suara;

    public LiatHasil (int no_urut, int suara) {
        this.no_urut = no_urut;
        this.suara = suara;
    }

    public int getNo_urut() {
        return no_urut;
    }


    public int getSuara() {
        return suara;
    }
}