package com.cooperate.service;


import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;

import static org.mockito.Mockito.*;

import com.cooperate.service.PaymentService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Кирилл on 20.03.2017.
 */
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService = new PaymentService();

    @Mock
    private PaymentDAO paymentDAO;

    @BeforeClass
    public void setUp() {
        initMocks(this);
    }

    @BeforeMethod
    public void init() {
        reset(paymentDAO);
    }

    @Test
    public void testFindYears() throws Exception {
        paymentService.findYears();
        verify(paymentDAO).findYears();
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        paymentService.saveOrUpdate(new Payment());
        verify(paymentDAO).save(any(Payment.class));
    }

    @Test
    public void testFindByYear() throws Exception {
        paymentService.findByYear(2014);
        verify(paymentDAO).findByYear(2014);
    }

    @Test
    public void testGetPayment() throws Exception {
        paymentService.getPayment(100);
        verify(paymentDAO).getOne(100);
    }

    @Test
    public void testDelete() throws Exception {
        paymentService.delete(100);
        verify(paymentDAO).delete(100);
    }

    @Test
    public void testGetPaymentOnGarag() throws Exception {
        Garag garag = new Garag();
        garag.setId(100);
        paymentService.getPaymentOnGarag(garag);
        verify(paymentDAO).getPaymentOnGarag(100);
    }

    @Test
    public void testPay() throws Exception {

    }

    @Test
    public void testReportPayments() throws Exception {

    }

}