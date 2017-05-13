package com.cooperate.service;


import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.cooperate.entity.Person;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Кирилл on 20.03.2017.
 */
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService = new PaymentService();

    @Mock
    private PaymentDAO paymentDAO;

    @Mock
    private ContributionService contributionService;

    @Mock
    private GaragService garagService;

    @BeforeClass
    public void setUp() {
        initMocks(this);
    }

    @BeforeMethod
    public void init() {
        reset(paymentDAO);
    }

    /**
     * Получение списка периодов для которых существуют платежи
     *
     * @throws Exception
     */
    @Test
    public void testFindYears() throws Exception {
        paymentService.findYears();
        verify(paymentDAO).findYears();
    }

    /**
     * Сохранение платежа
     *
     * @throws Exception
     */
    @Test
    public void testSaveOrUpdate() throws Exception {
        paymentService.saveOrUpdate(new Payment());
        verify(paymentDAO).save(any(Payment.class));
    }

    /**
     * Получение списка платежей определенного года
     *
     * @throws Exception
     */
    @Test
    public void testFindByYear() throws Exception {
        paymentService.findByYear(2014);
        verify(paymentDAO).findByYear(2014);
    }

    /**
     * Получение платежа по идентификатору
     *
     * @throws Exception
     */
    @Test
    public void testGetPayment() throws Exception {
        paymentService.getPayment(100);
        verify(paymentDAO).getOne(100);
    }

    /**
     * Удаление платежа
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        paymentService.delete(100);
        verify(paymentDAO).delete(100);
    }

    /**
     * Возвращает платежи для определенного гаража с остатками денег
     *
     * @throws Exception
     */
    @Test
    public void testGetPaymentOnGarag() throws Exception {
        Garag garag = new Garag();
        garag.setId(100);
        paymentService.getPaymentOnGarag(garag);
        verify(paymentDAO).getPaymentOnGarag(100);
    }

    /**
     * Метод проведения дополнительного платежа
     *
     * @throws Exception
     */
    @Test
    public void testPayAdding() throws Exception {
        Calendar now = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setPay(1000);
        Garag garag = new Garag();
        garag.setId(1);
        Person person = mock(Person.class);
        garag.setPerson(person);
        payment.setGarag(garag);
        String fio = "Иванов Иван Иваныч";
        given(person.getFIO()).willReturn(fio);
        given(garagService.getGarag(garag.getId())).willReturn(garag);
        given(paymentDAO.getMaxValueNumber()).willReturn(1);
        given(garagService.sumContribution(garag)).willReturn(100f);

        paymentService.pay(payment, false, "adding");

        assertEquals(payment.getNumber(), Integer.valueOf(2));
        assertEquals(payment.getYear().intValue(), now.get(Calendar.YEAR));
        assertEquals(payment.getDebtPastPay(), 100f);
        assertEquals(payment.getFio(), fio);
        assertEquals(payment.getAdditionallyPay(), 1000f);
        assertEquals(payment.getPay(), 0f);
        assertNotNull(payment.getDatePayment());

        verify(paymentDAO).getMaxValueNumber();
        verify(paymentDAO).save(payment);
    }

    /**
     * Метод проведения платежа по долгам прощлых лет
     * Сумма долга больше суммы платежа
     *
     * @throws Exception
     */
    @Test
    public void testOldPayMore() throws Exception {
        Calendar now = Calendar.getInstance();
        Payment payment = new Payment();
        Contribution contribution = new Contribution();
        payment.setPay(1000);

        headerPay(payment, now, contribution, "oldPay");

        paymentService.pay(payment, false, "default");

        assertEquals(payment.getOldContributePay(), 800f);
        assertEquals(payment.getGarag().getOldContribute(), 0f);
        assertEquals(payment.getPay(), 200f);
    }

    /**
     * Метод проведения платежа по долгам прощлых лет
     * Сумма долга равна сумме платежа
     *
     * @throws Exception
     */
    @Test
    public void testOldPay() throws Exception {
        Calendar now = Calendar.getInstance();
        Payment payment = new Payment();
        Contribution contribution = new Contribution();
        payment.setPay(800);

        headerPay(payment, now, contribution, "oldPay");

        paymentService.pay(payment, false, "default");

        assertEquals(payment.getOldContributePay(), 800f);
        assertEquals(payment.getGarag().getOldContribute(), 0f);
        assertEquals(payment.getPay(), 0f);
    }


    /**
     * Метод проведения платежа moreDebts
     *
     * @throws Exception
     */
    @Test
    public void testPayMoreDebts() throws Exception {
        Calendar now = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setPay(3000);

        Contribution contribution = new Contribution();
        contribution.setYear(2015);
        contribution.setContribute(1000f);
        contribution.setContLand(200f);
        contribution.setContTarget(1000f);
        contribution.setFines(300);

        headerPay(payment, now, contribution, "default");

        paymentService.pay(payment, false, "default");

        assertEquals(contribution.getContribute(), 0f);
        assertEquals(contribution.getContLand(), 0f);
        assertEquals(contribution.getContTarget(), 0f);
        assertEquals(contribution.getFines(), 0);
        assertFalse(contribution.isFinesOn());

        assertEquals(payment.getContributePay(), 1000f);
        assertEquals(payment.getContLandPay(), 200f);
        assertEquals(payment.getContTargetPay(), 1000f);
        assertEquals(payment.getFinesPay(), 300);
        assertEquals(payment.getPay(), 500f);

        verify(paymentDAO).getMaxValueNumber();
        verify(paymentDAO).save(payment);
        verify(contributionService).saveOrUpdate(contribution);
    }

    /**
     * Метод проведения платежа moreDebts - частичное погашение пеней
     *
     * @throws Exception
     */
    @Test
    public void testPayMoreDebtsPart() throws Exception {
        Calendar now = Calendar.getInstance();

        Payment payment = new Payment();
        payment.setPay(3000);

        Contribution contribution = new Contribution();
        contribution.setYear(2015);
        contribution.setContribute(1000f);
        contribution.setContLand(200f);
        contribution.setContTarget(1000f);
        contribution.setFines(1000);

        headerPay(payment, now, contribution, "default");

        paymentService.pay(payment, false, "default");

        assertEquals(contribution.getContribute(), 0f);
        assertEquals(contribution.getContLand(), 0f);
        assertEquals(contribution.getContTarget(), 0f);
        assertEquals(contribution.getFines(), 200);

        assertEquals(payment.getContributePay(), 1000f);
        assertEquals(payment.getContLandPay(), 200f);
        assertEquals(payment.getContTargetPay(), 1000f);
        assertEquals(payment.getFinesPay(), 800);
        assertEquals(payment.getPay(), 0f);

        verify(paymentDAO).getMaxValueNumber();
        verify(paymentDAO).save(payment);
        verify(contributionService).saveOrUpdate(contribution);
    }

    /**
     * Метод проведения платежа lessDebts - частичное погашение целевого долга
     *
     * @throws Exception
     */
    @Test
    public void testPayLessDebtsTarget() throws Exception {
        Calendar now = Calendar.getInstance();

        Payment payment = new Payment();
        payment.setPay(2000);

        Contribution contribution = new Contribution();
        contribution.setYear(now.get(Calendar.YEAR));
        contribution.setContribute(1000f);
        contribution.setContLand(200f);
        contribution.setContTarget(1000f);
        contribution.setFines(200);

        headerPay(payment, now, contribution, "default");

        paymentService.pay(payment, false, "default");

        assertEquals(contribution.getContribute(), 0f);
        assertEquals(contribution.getContLand(), 0f);
        assertEquals(contribution.getContTarget(), 200f);
        assertEquals(contribution.getFines(), 200);

        assertEquals(payment.getContributePay(), 1000f);
        assertEquals(payment.getContLandPay(), 200f);
        assertEquals(payment.getContTargetPay(), 800f);
        assertEquals(payment.getFinesPay(), 0);
        assertEquals(payment.getPay(), 0f);
    }

    /**
     * Метод проведения платежа lessDebts - частичное погашение аренды земли
     *
     * @throws Exception
     */
    @Test
    public void testPayLessDebtsLand() throws Exception {
        Calendar now = Calendar.getInstance();

        Payment payment = new Payment();
        payment.setPay(1100);

        Contribution contribution = new Contribution();
        contribution.setYear(now.get(Calendar.YEAR));
        contribution.setContribute(1000f);
        contribution.setContLand(200f);
        contribution.setContTarget(1000f);
        contribution.setFines(200);

        headerPay(payment, now, contribution, "default");

        paymentService.pay(payment, false, "default");

        assertEquals(contribution.getContribute(), 0f);
        assertEquals(contribution.getContLand(), 100f);
        assertEquals(contribution.getContTarget(), 1000f);
        assertEquals(contribution.getFines(), 200);

        assertEquals(payment.getContributePay(), 1000f);
        assertEquals(payment.getContLandPay(), 100f);
        assertEquals(payment.getContTargetPay(), 0f);
        assertEquals(payment.getFinesPay(), 0);
        assertEquals(payment.getPay(), 0f);
    }

    /**
     * Метод проведения платежа lessDebts - частичное погашение основного взноса
     *
     * @throws Exception
     */
    @Test
    public void testPayLessDebtsContribute() throws Exception {
        Calendar now = Calendar.getInstance();

        Payment payment = new Payment();
        payment.setPay(800);

        Contribution contribution = new Contribution();
        contribution.setYear(now.get(Calendar.YEAR));
        contribution.setContribute(1000f);
        contribution.setContLand(200f);
        contribution.setContTarget(1000f);
        contribution.setFines(200);

        headerPay(payment, now, contribution, "default");

        paymentService.pay(payment, false, "default");

        assertEquals(contribution.getContribute(), 200f);
        assertEquals(contribution.getContLand(), 200f);
        assertEquals(contribution.getContTarget(), 1000f);
        assertEquals(contribution.getFines(), 200);

        assertEquals(payment.getContributePay(), 800f);
        assertEquals(payment.getContLandPay(), 0f);
        assertEquals(payment.getContTargetPay(), 0f);
        assertEquals(payment.getFinesPay(), 0);
        assertEquals(payment.getPay(), 0f);
    }

    private void headerPay(Payment payment, Calendar now, Contribution contribution, String type) {
        Garag garag = new Garag();
        Person person = mock(Person.class);
        payment.setDatePayment(now);
        garag.setId(1);
        garag.setPerson(person);
        payment.setGarag(garag);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);
        garag.setContributions(list);
        if (type.equals("oldPay")) {
            garag.setOldContribute(800f);
        }
        given(person.getFIO()).willReturn("Иванов Иван Иваныч");
        given(garagService.getGarag(garag.getId())).willReturn(garag);
        given(paymentDAO.getMaxValueNumber()).willReturn(null);
        given(garagService.sumContribution(garag)).willReturn(100f);

    }
}