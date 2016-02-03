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
    @Column(name = "series", nullable = false)
    private String series;

    //Номер
    @Expose
    @Column(name = "number", nullable = false)
    @OrderBy("number")
    private String number;

    //Владелец
    @Expose
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn()
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Garag)) return false;

        Garag garag = (Garag) o;

        if (contributions != null ? !contributions.equals(garag.contributions) : garag.contributions != null)
            return false;
        if (id != null ? !id.equals(garag.id) : garag.id != null) return false;
        if (number != null ? !number.equals(garag.number) : garag.number != null) return false;
        if (payments != null ? !payments.equals(garag.payments) : garag.payments != null) return false;
        if (person != null ? !person.equals(garag.person) : garag.person != null) return false;
        if (series != null ? !series.equals(garag.series) : garag.series != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (contributions != null ? contributions.hashCode() : 0);
        result = 31 * result + (payments != null ? payments.hashCode() : 0);
        return result;
    }
}
