package com.cooperate.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class Address implements Serializable {
    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_address", nullable = false)
    private Integer id;

    //Город
    @Expose
    @Column(name = "city")
    private String city;

    //Улица
    @Expose
    @Column(name = "street")
    private String street;

    //Дом
    @Expose
    @Column(name = "home")
    private String home;

    //Квартира - необязательный параметр
    @Expose
    @Column(name = "apartment")
    private String apartment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }



}
