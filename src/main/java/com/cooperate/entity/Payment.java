package com.cooperate.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Calendar;

/*Класс платежа*/
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    @Column(name = "id_payment", nullable = false)
    private Integer id;

    //Номер платежа
    @Expose
    @Column(name = "number_payment", nullable = false, unique = true)
    private Integer number;


    /*Дата платежа*/
    @Expose
    @Column(name = "date_payment")
    private Calendar datePayment;

    //К какому гаражу прикреплен платеж
    @Expose
    @ManyToOne
    private Garag garag;

    //ФИО платильщика
    @Expose
    @Column(name = "person_fio")
    private String fio;


    //Сумма платежа
    @Expose
    @Column(name = "payment")
    private float pay;

    //Платеж в членский взнос
    @Expose
    @Column(name = "contribute_pay")
    private float contributePay;

    //Платеж в аренду земли
    @Expose
    @Column(name = "cont_land_pay")
    private float contLandPay;

    //Платеж в целевой взнос
    @Expose
    @Column(name = "cont_target_pay")
    private float contTargetPay;

    //Платеж в пени
    @Expose
    @Column(name = "fines_pay")
    private int finesPay;

    //Платеж в дополнительные взносы
    @Expose
    @Column(name = "additionally_pay")
    private float additionallyPay;

    //Долг после оплаты(Для повторного печати чека)
    @Expose
    @Column(name = "debt_past_pay")
    private float debtPastPay;


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

    public Calendar getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Calendar datePayment) {
        this.datePayment = datePayment;
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

    public float getDebtPastPay() {
        return debtPastPay;
    }

    public void setDebtPastPay(float debtPastPay) {
        this.debtPastPay = debtPastPay;
    }
}
