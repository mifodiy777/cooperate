package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*Класс платежа*/
@Entity
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_payment", nullable = false)
    private Integer id;

    //Номер платежа
    @Column(name = "number_payment", nullable = false, unique = true)
    private Integer number;

    @Column(name = "year", nullable = false)
    private Integer year;


    /*Дата платежа*/
    @Column(name = "date_payment", nullable = false)
    private Calendar datePayment;

    //К какому гаражу прикреплен платеж
    @ManyToOne
    @JoinColumn(nullable = false)
    private Garag garag;

    //ФИО платильщика
    @Column(name = "person_fio", nullable = false)
    private String fio;


    //Сумма платежа
    @Column(name = "payment")
    private float pay;

    //Платеж в членский взнос
    @Column(name = "contribute_pay")
    private float contributePay;

    //Платеж в аренду земли
    @Column(name = "cont_land_pay")
    private float contLandPay;

    //Платеж в целевой взнос
    @Column(name = "cont_target_pay")
    private float contTargetPay;

    //Платеж в пени
    @Column(name = "fines_pay")
    private int finesPay;

    //Платеж в дополнительные взносы
    @Column(name = "additionally_pay")
    private float additionallyPay;

     //Платеж в долги прошлых лет
    @Column(name = "old_contribute_pay")
    private float oldContributePay;

    //Долг после оплаты(Для повторного печати чека)
    @Column(name = "debt_past_pay")
    private float debtPastPay;

    public Payment(Garag garag) {
        this.garag = garag;
    }

    public Payment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Calendar getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Calendar datePayment) {
        this.datePayment = datePayment;
    }

    public String getDatePay() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        return fmt.format(this.datePayment.getTime());
    }

    public Garag getGarag() {
        return garag;
    }

    public void setGarag(Garag garag) {
        this.garag = garag;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public Float getSumPay() {
        return this.pay + this.contributePay + this.contLandPay + this.contTargetPay + this.additionallyPay + this.finesPay+this.oldContributePay;
    }

    public float getContributePay() {
        return contributePay;
    }

    public void setContributePay(float contributePay) {
        this.contributePay = contributePay;
    }

    public float getContLandPay() {
        return contLandPay;
    }

    public void setContLandPay(float contLandPay) {
        this.contLandPay = contLandPay;
    }

    public float getContTargetPay() {
        return contTargetPay;
    }

    public void setContTargetPay(float contTargetPay) {
        this.contTargetPay = contTargetPay;
    }

    public int getFinesPay() {
        return finesPay;
    }

    public void setFinesPay(int finesPay) {
        this.finesPay = finesPay;
    }

    public float getAdditionallyPay() {
        return additionallyPay;
    }

    public void setAdditionallyPay(float additionallyPay) {
        this.additionallyPay = additionallyPay;
    }

    public float getOldContributePay() {
        return oldContributePay;
    }

    public void setOldContributePay(float oldContributePay) {
        this.oldContributePay = oldContributePay;
    }

    public float getDebtPastPay() {
        return debtPastPay;
    }

    public void setDebtPastPay(float debtPastPay) {
        this.debtPastPay = debtPastPay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;

        Payment payment = (Payment) o;

        if (Float.compare(payment.additionallyPay, additionallyPay) != 0) return false;
        if (Float.compare(payment.contLandPay, contLandPay) != 0) return false;
        if (Float.compare(payment.contTargetPay, contTargetPay) != 0) return false;
        if (Float.compare(payment.contributePay, contributePay) != 0) return false;
        if (Float.compare(payment.debtPastPay, debtPastPay) != 0) return false;
        if (finesPay != payment.finesPay) return false;
        if (Float.compare(payment.oldContributePay, oldContributePay) != 0) return false;
        if (Float.compare(payment.pay, pay) != 0) return false;
        if (datePayment != null ? !datePayment.equals(payment.datePayment) : payment.datePayment != null) return false;
        if (fio != null ? !fio.equals(payment.fio) : payment.fio != null) return false;
        if (garag != null ? !garag.equals(payment.garag) : payment.garag != null) return false;
        if (id != null ? !id.equals(payment.id) : payment.id != null) return false;
        if (number != null ? !number.equals(payment.number) : payment.number != null) return false;
        if (year != null ? !year.equals(payment.year) : payment.year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (datePayment != null ? datePayment.hashCode() : 0);
        result = 31 * result + (garag != null ? garag.hashCode() : 0);
        result = 31 * result + (fio != null ? fio.hashCode() : 0);
        result = 31 * result + (pay != +0.0f ? Float.floatToIntBits(pay) : 0);
        result = 31 * result + (contributePay != +0.0f ? Float.floatToIntBits(contributePay) : 0);
        result = 31 * result + (contLandPay != +0.0f ? Float.floatToIntBits(contLandPay) : 0);
        result = 31 * result + (contTargetPay != +0.0f ? Float.floatToIntBits(contTargetPay) : 0);
        result = 31 * result + finesPay;
        result = 31 * result + (additionallyPay != +0.0f ? Float.floatToIntBits(additionallyPay) : 0);
        result = 31 * result + (oldContributePay != +0.0f ? Float.floatToIntBits(oldContributePay) : 0);
        result = 31 * result + (debtPastPay != +0.0f ? Float.floatToIntBits(debtPastPay) : 0);
        return result;
    }
}
