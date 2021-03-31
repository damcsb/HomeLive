package com.example.homelive.recycler;

import com.example.homelive.model.Advert;

public interface OnAdvertClickListener {

    public final int EDIT_AD = 200;
    public final int DEL_AD = 201;

    public void onClick(Advert ad, int cop);

}
