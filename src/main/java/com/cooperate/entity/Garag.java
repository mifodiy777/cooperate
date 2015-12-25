package com.cooperate.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
public class Garag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    @Column(name = "id_garag", nullable = false)
    private Integer id;

    //Ряд
    @Expose
    @Column(name = "series")
    private String series;

    //Номер
    @Expose
    @Column(name = "number")
    @OrderBy("number")
    private String number;

    //Владелец
    @Expose
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    private Person person;


    //Периоды платежные - долги
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("year ASC")
    private List<Contribution> contributions;

    //Платежи
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garag")
    @OrderBy("datePayment DESC")
    private List<Payment> payments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;

    }

    public List<Contribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
