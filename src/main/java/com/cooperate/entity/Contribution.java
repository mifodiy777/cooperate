package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
public class Contribution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_count", nullable = false)
    private Integer id;

    //Год платежного периода
    @OrderBy
    @Column(name = "year", nullable = false)
    private Integer year;


    //Членский взнос - долг
    /* При появлении нового начисления и периода данное значения
           становиться равным значением членского взноса класса Rent */
    @Column(name = "contribute")
    private float contribute;

    //Сумм членского взноса - изначально
    @Column(name = "contribute_max", columnDefinition = "float default 0.0")
    private float contributeMax = 0f;

    //Аренда земли - долг
    /* При появлении нового начисления и периода данное значения
           становиться равным значением арендой земли класса Rent */
    @Column(name = "contLand")
    private float contLand;

    //Целевой взнос - долг
    /* При появлении нового начисления и периода данное значения
           становиться равным значением целевого взноса класса Rent */
    @Column(name = "contTarget")
    private float contTarget;

    //Пени - долг
    /* Данное значение будет увеличиваться на 0,1% от суммы начисления (Класс Rent этого же периода )
       с 1 июля данного периода. При частичной оплате в данном периоде счетчик останавливаеться.
       При полной оплате долга за этот период в последующие года счетчик так же остановиться.
       При достижении данного значения суммы взноса за период - счетчик остановиться.
       При полной оплате данное значение становиться 0  */
    @Column(name = "fines")
    private int fines;

    //Включение начисление пеней
    //Включаеться при не полностью погашеном долге в след. году
    //Если true то начисление пеней включено
    @Column(name = "fines_on")
    private boolean finesOn;

    //Дата последнего обновления пеней
    @Column(name = "fines_last_update")
    private Calendar finesLastUpdate;

    //Льготный ли период
    @Column(name = "benefitsOn")
    private boolean benefitsOn;

    //Член правления в этом периоде
    @Column(name = "member_board_on")
    private boolean memberBoardOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getContribute() {
        return contribute;
    }

    public void setContribute(Float contribute) {
        this.contribute = contribute;
    }

    public Float getContLand() {
        return contLand;
    }

    public void setContLand(Float contLand) {
        this.contLand = contLand;
    }

    public Float getContTarget() {
        return contTarget;
    }

    public void setContTarget(Float contTarget) {
        this.contTarget = contTarget;
    }

    public int getFines() {
        return fines;
    }

    public void setFines(int fines) {
        this.fines = fines;
    }

    public boolean isFinesOn() {
        return finesOn;
    }

    public void setFinesOn(boolean finesOn) {
        this.finesOn = finesOn;
    }

    public Calendar getFinesLastUpdate() {
        return finesLastUpdate;
    }

    public void setFinesLastUpdate(Calendar finesLastUpdate) {
        this.finesLastUpdate = finesLastUpdate;
    }

    public boolean isBenefitsOn() {
        return benefitsOn;
    }

    public void setBenefitsOn(boolean benefitsOn) {
        this.benefitsOn = benefitsOn;
    }

    public boolean isMemberBoardOn() {
        return memberBoardOn;
    }

    public void setMemberBoardOn(boolean memberBoardOn) {
        this.memberBoardOn = memberBoardOn;
    }

    public float getContributeMax() {
        return contributeMax;
    }

    public void setContributeMax(float contributeMax) {
        this.contributeMax = contributeMax;
    }

    public Float getSumFixed() {
        return this.contribute + this.contLand + this.contTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contribution that = (Contribution) o;

        if (Float.compare(that.contribute, contribute) != 0) return false;
        if (Float.compare(that.contLand, contLand) != 0) return false;
        if (Float.compare(that.contTarget, contTarget) != 0) return false;
        if (fines != that.fines) return false;
        if (finesOn != that.finesOn) return false;
        if (benefitsOn != that.benefitsOn) return false;
        if (memberBoardOn != that.memberBoardOn) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;
        return finesLastUpdate != null ? finesLastUpdate.equals(that.finesLastUpdate) : that.finesLastUpdate == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (contribute != +0.0f ? Float.floatToIntBits(contribute) : 0);
        result = 31 * result + (contLand != +0.0f ? Float.floatToIntBits(contLand) : 0);
        result = 31 * result + (contTarget != +0.0f ? Float.floatToIntBits(contTarget) : 0);
        result = 31 * result + fines;
        result = 31 * result + (finesOn ? 1 : 0);
        result = 31 * result + (finesLastUpdate != null ? finesLastUpdate.hashCode() : 0);
        result = 31 * result + (benefitsOn ? 1 : 0);
        result = 31 * result + (memberBoardOn ? 1 : 0);
        return result;
    }
}
