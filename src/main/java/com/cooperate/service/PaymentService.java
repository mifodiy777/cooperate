package com.cooperate.service;

import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface PaymentService {

    List<Integer> findYears();

    Payment saveOrUpdate(Payment payment);

    List<Payment> getPayments();

    List<Payment> findByYear(Integer year);
    
    HSSFWorkbook reportPayments(Integer year);

    Payment getPayment(Integer id);

    void delete(Integer id);

    void pay(Payment payment);

    Payment getPaymentOnGarag(Garag garag);

    Integer getMaxNumber();

}
