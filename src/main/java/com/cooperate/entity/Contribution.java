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

    //Аренда земли - долг
    /* При появлении нового начисления и периода данное значения
           становиться равным значением арендой земли класса Rent */
    @Column(name = "cont_land")
    private float contLand;

    //Целевой взнос - долг
    /* При появлении нового начисления и периода данное значения
           становиться равным значением целевого взноса класса Rent */
    @Column(name = "cont_target")
    private float contTarget;

    //Пени - долг
    /* Данное значение будет увеличиваться на 0,1% от суммы начисления (Класс Rent этого же периода )
       с 1 июля данного периода. При частичной оплате в данном периоде счетчик останавливаеться.
       При полной оплате долга за этот период в последующие года счетчик так же остановиться.
       При достижении данного значения суммы взноса за период - счетчик остановиться.
       При полной оплате данное значение становиться 0  */
    @Column(name = "fines")
    private int fines;

    //Пени - начисленные
    /* Данно значение будет равным fines, но при достижении fines
    максимального значения данный параметр изменяться не будет. Т.е. это сумма начисленного пени за период  */
    @Column(name = "fines_sum")
    private int finesSum;

    //Включение начисление пеней
    //Включаеться при не полностью погашеном долге в след. году
    //Если true то начисление пеней включено
    @Column(name = "fines_on")
    private boolean finesOn;

    //Дата последнего обновления пеней
    @Column(name = "fines_last_update")
    private Calendar finesLastUpdate;

    /*Остаточные деньги при уплате всех долгов, При появлении нового периода деньги перейдут в новый период.
      При этом данное значение становиться равным 0, а если сумма покрывает весь следующий период - то деньги перейдут
      в новый период в параметр balance*/
    @Column(name = "balance")
    private float balance;

    /*Дополнительные начисления: уплаты различного рода*/
    @Column(name = "additionally_cont")
    private float additionallyCont;


    //Льготный ли период
    @Column(name = "benefits_on")
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

    public int getFinesSum() {
        return finesSum;
    }

    public void setFinesSum(int finesSum) {
        this.finesSum = finesSum;
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

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public float getAdditionallyCont() {
        return additionallyCont;
    }

    public void setAdditionallyCont(float additionallyCont) {
        this.additionallyCont = additionallyCont;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contribution)) return false;

        Contribution that = (Contribution) o;

        if (Float.compare(that.additionallyCont, additionallyCont) != 0) return false;
        if (Float.compare(that.balance, balance) != 0) return false;
        if (benefitsOn != that.benefitsOn) return false;
        if (Float.compare(that.contLand, contLand) != 0) return false;
        if (Float.compare(that.contTarget, contTarget) != 0) return false;
        if (Float.compare(that.contribute, contribute) != 0) return false;
        if (fines != that.fines) return false;
        if (finesOn != that.finesOn) return false;
        if (finesSum != that.finesSum) return false;
        if (memberBoardOn != that.memberBoardOn) return false;
        if (finesLastUpdate != null ? !finesLastUpdate.equals(that.finesLastUpdate) : that.finesLastUpdate != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (contribute != +0.0f ? Float.floatToIntBits(contribute) : 0);
        result = 31 * result + (contLand != +0.0f ? Float.floatToIntBits(contLand) : 0);
        result = 31 * result + (contTarget != +0.0f ? Float.floatToIntBits(contTarget) : 0);
        result = 31 * result + fines;
        result = 31 * result + finesSum;
        result = 31 * result + (finesOn ? 1 : 0);
        result = 31 * result + (finesLastUpdate != null ? finesLastUpdate.hashCode() : 0);
        result = 31 * result + (balance != +0.0f ? Float.floatToIntBits(balance) : 0);
        result = 31 * result + (additionallyCont != +0.0f ? Float.floatToIntBits(additionallyCont) : 0);
        result = 31 * result + (benefitsOn ? 1 : 0);
        result = 31 * result + (memberBoardOn ? 1 : 0);
        return result;
    }
}
