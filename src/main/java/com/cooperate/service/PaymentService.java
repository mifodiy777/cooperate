package com.cooperate.service;

import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment saveOrUpdate(Payment payment);

    List<Payment> getPayments();

    Payment getPayment(Integer id);

    void delete(Integer id);

    void pay(Payment payment);

    Payment getPaymentOnGarag(Garag garag);

    Integer getMaxNumber();

}
