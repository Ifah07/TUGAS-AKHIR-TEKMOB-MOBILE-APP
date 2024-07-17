package com.app.keluhkesah.model;

import java.io.Serializable;

public class Users implements Serializable {
    public String uid;
    public String profile;
    public String userId;
    public String fullname;
    public String nim;

    public Users(String userId, String fullname, String nim, String profile) {
        this.userId = userId;
        this.fullname = fullname;
        this.nim = nim;
        this.profile = profile;
    }
}
