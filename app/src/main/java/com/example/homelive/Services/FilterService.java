package com.example.homelive.Services;

import com.example.homelive.model.Advert;
import com.example.homelive.recycler.AdapterAdvert;

import java.util.ArrayList;
import java.util.List;

public class FilterService {

    public void filtertitle(String title, List<Advert> adverts, AdapterAdvert adapterAdvert){
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getTittle().contains(title)){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filtercity(String city, List<Advert> adverts, AdapterAdvert adapterAdvert){
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getCity().contains(city)){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filterprice1price2(Integer price1, Integer price2, List<Advert> adverts, AdapterAdvert adapterAdvert){
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getPrice() >= price1 && ad.getPrice() <= price2){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }
    public void filterprice1(Integer price1, List<Advert> adverts, AdapterAdvert adapterAdvert){
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getPrice() >= price1){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filterprice2(Integer price2, List<Advert> adverts, AdapterAdvert adapterAdvert){
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getPrice() <= price2){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filtertitleprice1(String title, Integer price1, List<Advert> adverts, AdapterAdvert adapterAdvert){
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getTittle().contains(title) && ad.getPrice() >= price1){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }
    public void filtertitlecity(String title, String city,List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for(Advert ad:adverts){
            if(ad.getTittle().contains(title) && ad.getCity().contains(city)){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filtercityprice1(String city, Integer price1, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getCity().contains(city) && ad.getPrice() >= price1){
                advertArrayList.add(ad);
            }
            adapterAdvert.filtrar(advertArrayList);
        }
    }
    public void filtercityprice2(String city, Integer price2, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getCity().contains(city) && ad.getPrice() <= price2){
                advertArrayList.add(ad);
            }
            adapterAdvert.filtrar(advertArrayList);
        }
    }

    public void filtertitleprice2(String title, Integer price2, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getTittle().contains(title) && ad.getPrice() <= price2){
                advertArrayList.add(ad);
            }
            adapterAdvert.filtrar(advertArrayList);
        }
    }


    public void filtertitlecitynumber1(String title, String city, Integer number1, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getTittle().contains(title) && ad.getCity().contains(city) && ad.getPrice() >= number1){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filtertitlecitynumber2(String title, String city, Integer number2, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getTittle().contains(title) && ad.getCity().contains(city) && ad.getPrice() <= number2){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filtercitynumber1number2(String city, Integer number1, Integer number2, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getCity().contains(city) && ad.getPrice() >= number1 && ad.getPrice() <= number2){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }

    public void filterall(String title, String city, Integer number1, Integer number2, List<Advert> adverts, AdapterAdvert adapterAdvert) {
        ArrayList<Advert> advertArrayList = new ArrayList<>();
        for (Advert ad : adverts) {
            if(ad.getTittle().contains(title) && ad.getCity().contains(city) && ad.getPrice() >= number1 && ad.getPrice()<= number2){
                advertArrayList.add(ad);
            }
        }
        adapterAdvert.filtrar(advertArrayList);
    }
}
