package com.app.keluhkesah.model;

import java.io.Serializable;

public class KeluhKesah implements Serializable {
    public String uid;
    public String userId;
    public String nama;
    public String konten;
    public String avatar;

    public KeluhKesah(){
    }

    public KeluhKesah(String userId, String nama, String konten, String avatar) {
        this.userId = userId;
        this.nama = nama;
        this.konten = konten;
        this.avatar = avatar;
    }
    public KeluhKesah(String avatar) {
        this.avatar = avatar;
    }
}
