package com.example.homelive.model;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable {

    private String id;
    private String img;
    private String username;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
