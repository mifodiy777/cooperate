package com.cooperate.dto;

/**
 * Класс сбора информации из отчета по доходам Итоговые данные.
 * Created by Кирилл on 15.05.2017.
 */
public class ResultProfit {

    private String series;

    private int oldSumContribute;

    private int oldContribute;

    private int oldContLand;

    private int oldContTarget;

    private int pastOld;

    private int sumRent;

    private int rentContribute;

    private int rentContLand;

    private int rentContTarget;

    private int sumPayment;

    private int payContribute;

    private int payContLand;

    private int payContTarget;

    private int payAdding;

    private int payPastOld;

    private int payFines;

    private int balance;

    private int sumContribute;

    private int contribute;

    private int contLand;

    private int contTarget;

    private int pastContribute;

    private int fines;

    public ResultProfit(String series) {
        this.series = series;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getOldSumContribute() {
        return oldSumContribute;
    }

    public void setOldSumContribute(int oldSumContribute) {
        this.oldSumContribute = oldSumContribute;
    }

    public int getOldContribute() {
        return oldContribute;
    }

    public void setOldContribute(int oldContribute) {
        this.oldContribute = oldContribute;
    }

    public int getOldContLand() {
        return oldContLand;
    }

    public void setOldContLand(int oldContLand) {
        this.oldContLand = oldContLand;
    }

    public int getOldContTarget() {
        return oldContTarget;
    }

    public void setOldContTarget(int oldContTarget) {
        this.oldContTarget = oldContTarget;
    }

    public int getPastOld() {
        return pastOld;
    }

    public void setPastOld(int pastOld) {
        this.pastOld = pastOld;
    }

    public int getSumRent() {
        return sumRent;
    }

    public void setSumRent(int sumRent) {
        this.sumRent = sumRent;
    }

    public int getRentContribute() {
        return rentContribute;
    }

    public void setRentContribute(int rentContribute) {
        this.rentContribute = rentContribute;
    }

    public int getRentContLand() {
        return rentContLand;
    }

    public void setRentContLand(int rentContLand) {
        this.rentContLand = rentContLand;
    }

    public int getRentContTarget() {
        return rentContTarget;
    }

    public void setRentContTarget(int rentContTarget) {
        this.rentContTarget = rentContTarget;
    }

    public int getSumPayment() {
        return sumPayment;
    }

    public void setSumPayment(int sumPayment) {
        this.sumPayment = sumPayment;
    }

    public int getPayContribute() {
        return payContribute;
    }

    public void setPayContribute(int payContribute) {
        this.payContribute = payContribute;
    }

    public int getPayContLand() {
        return payContLand;
    }

    public void setPayContLand(int payContLand) {
        this.payContLand = payContLand;
    }

    public int getPayContTarget() {
        return payContTarget;
    }

    public void setPayContTarget(int payContTarget) {
        this.payContTarget = payContTarget;
    }

    public int getPayAdding() {
        return payAdding;
    }

    public void setPayAdding(int payAdding) {
        this.payAdding = payAdding;
    }

    public int getPayPastOld() {
        return payPastOld;
    }

    public void setPayPastOld(int payPastOld) {
        this.payPastOld = payPastOld;
    }

    public int getPayFines() {
        return payFines;
    }

    public void setPayFines(int payFines) {
        this.payFines = payFines;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getSumContribute() {
        return sumContribute;
    }

    public void setSumContribute(int sumContribute) {
        this.sumContribute = sumContribute;
    }

    public int getContribute() {
        return contribute;
    }

    public void setContribute(int contribute) {
        this.contribute = contribute;
    }

    public int getContLand() {
        return contLand;
    }

    public void setContLand(int contLand) {
        this.contLand = contLand;
    }

    public int getContTarget() {
        return contTarget;
    }

    public void setContTarget(int contTarget) {
        this.contTarget = contTarget;
    }

    public int getPastContribute() {
        return pastContribute;
    }

    public void setPastContribute(int pastContribute) {
        this.pastContribute = pastContribute;
    }

    public int getFines() {
        return fines;
    }

    public void setFines(int fines) {
        this.fines = fines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultProfit that = (ResultProfit) o;

        return series.equals(that.series);
    }

    @Override
    public int hashCode() {
        return series.hashCode();
    }
}
