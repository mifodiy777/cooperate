package com.cooperate.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


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
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_person")
    private Person person;

    @Expose
    @Column(name = "additionalInformation")
    private String additionalInformation;


    //Периоды платежные - долги
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("year ASC")
    private List<Contribution> contributions;

    //Долги прошлых лет не облагаемые пенями
    @Expose
    @Column(name = "old_contribute", nullable = false)
    private float oldContribute;

    //Платежи
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garag")
    @OrderBy("datePayment DESC")
    private List<Payment> payments;

    //История изменений      
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garag")
    @OrderBy("dateRecord DESC")
    private List<HistoryGarag> historyGarags;

    @Expose
    @Column(name = "electric_meter", columnDefinition = "boolean default false")
    private Boolean electricMeter;

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

    /**
     * Долги предыдущих лет
     *
     * @return
     */
    public float getOldContribute() {
        return oldContribute;
    }

    public void setOldContribute(float oldContribute) {
        this.oldContribute = oldContribute;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<HistoryGarag> getHistoryGarags() {
        return historyGarags;
    }

    public void setHistoryGarags(List<HistoryGarag> historyGarags) {
        this.historyGarags = historyGarags;
    }

    public String getName() {
        return this.series + "-" + this.number;
    }

    public String getFullName() {
        return " Ряд: " + this.series + " Номер: " + this.number;
    }

    public Boolean getElectricMeter() {
        return electricMeter;
    }

    public void setElectricMeter(Boolean electricMeter) {
        this.electricMeter = electricMeter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Garag)) return false;

        Garag garag = (Garag) o;

        if (Float.compare(garag.oldContribute, oldContribute) != 0) return false;
        if (additionalInformation != null ? !additionalInformation.equals(garag.additionalInformation) : garag.additionalInformation != null)
            return false;
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
        result = 31 * result + (additionalInformation != null ? additionalInformation.hashCode() : 0);
        result = 31 * result + (contributions != null ? contributions.hashCode() : 0);
        result = 31 * result + (oldContribute != +0.0f ? Float.floatToIntBits(oldContribute) : 0);
        result = 31 * result + (payments != null ? payments.hashCode() : 0);
        return result;
    }
}
