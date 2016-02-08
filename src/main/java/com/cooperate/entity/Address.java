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

    public String getAddr() {
        StringBuilder addr = new StringBuilder();
        addr.append("г.");
        addr.append(this.city);
        addr.append(" ул.");
        addr.append(this.street);
        addr.append(" д.");
        addr.append(this.home);
        if (!this.apartment.isEmpty()) {
            addr.append(" кв.");
            addr.append(this.apartment);
        }
        return addr.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (apartment != null ? !apartment.equals(address.apartment) : address.apartment != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (home != null ? !home.equals(address.home) : address.home != null) return false;
        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (home != null ? home.hashCode() : 0);
        result = 31 * result + (apartment != null ? apartment.hashCode() : 0);
        return result;
    }
}
