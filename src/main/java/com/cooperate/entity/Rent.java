package com.cooperate.entity;

import javax.persistence.*;

/*Класс суммы взноса*/
@Entity
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_rent", nullable = false)
    private Integer id;

    //Год начисления
    @Column(name = "year_rent", nullable = false, unique = true)
    private int yearRent;

    //Начисление членского взноса
    @Column(name = "contribute_rent")
    private float contributeMax;


    //Начисление аренды земли
    @Column(name = "cont_land_rent")
    private float contLandMax;

    //Начисление целевого взноса
    @Column(name = "cont_target_rent")
    private float contTargetMax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getYearRent() {
        return yearRent;
    }

    public void setYearRent(int yearRent) {
        this.yearRent = yearRent;
    }

    public float getContributeMax() {
        return contributeMax;
    }

    public void setContributeMax(float contributeMax) {
        this.contributeMax = contributeMax;
    }

    public float getContLandMax() {
        return contLandMax;
    }

    public void setContLandMax(float contLandMax) {
        this.contLandMax = contLandMax;
    }

    public float getContTargetMax() {
        return contTargetMax;
    }

    public void setContTargetMax(float contTargetMax) {
        this.contTargetMax = contTargetMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rent)) return false;

        Rent rent = (Rent) o;

        if (Float.compare(rent.contLandMax, contLandMax) != 0) return false;
        if (Float.compare(rent.contTargetMax, contTargetMax) != 0) return false;
        if (Float.compare(rent.contributeMax, contributeMax) != 0) return false;
        if (yearRent != rent.yearRent) return false;
        if (id != null ? !id.equals(rent.id) : rent.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + yearRent;
        result = 31 * result + (contributeMax != +0.0f ? Float.floatToIntBits(contributeMax) : 0);
        result = 31 * result + (contLandMax != +0.0f ? Float.floatToIntBits(contLandMax) : 0);
        result = 31 * result + (contTargetMax != +0.0f ? Float.floatToIntBits(contTargetMax) : 0);
        return result;
    }
}
