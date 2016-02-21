package com.cooperate.service;

import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Calendar;
import java.util.List;

public interface PaymentService {

    List<Integer> findYears();

    Payment saveOrUpdate(Payment payment);

    List<Payment> findByYear(Integer year);
    
    HSSFWorkbook reportPayments(Calendar start, Calendar end);

    Payment getPayment(Integer id);

    void delete(Integer id);

    Payment pay(Payment payment,Boolean isNewPeriod);

    List<Payment> getPaymentOnGarag(Garag garag);

    Integer getMaxNumber();

}
