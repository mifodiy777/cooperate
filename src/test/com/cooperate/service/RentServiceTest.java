package com.cooperate.service;

import com.cooperate.dao.RentDAO;
import com.cooperate.entity.*;
import com.cooperate.service.RentService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Кирилл on 25.03.2017.
 */
public class RentServiceTest {

    @Mock
    private RentDAO rentDAO;

    @Mock
    private GaragService garagService;

    @Mock
    private PaymentService paymentService;


    @InjectMocks
    private RentService service = new RentService();

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        reset(rentDAO);
    }

    /**
     * Сохранение долгового периода
     * @throws Exception
     */
    @Test
    public void testSaveOrUpdate() throws Exception {
        Rent rent = new Rent();
        service.saveOrUpdate(rent);
        verify(rentDAO).save(rent);
    }

    /**
     * Проверка существования определенного периода
     * @throws Exception
     */
    @Test
    public void testCheckRent() throws Exception {
        given(rentDAO.findByYearRent(2010)).willReturn(new Rent());
        assertFalse(service.checkRent(2010));
    }

    /**
     * Получение всех долговых периодов
     * @throws Exception
     */
    @Test
    public void testGetRents() throws Exception {
        service.getRents();
        verify(rentDAO).findAll();
    }

    /**
     * Получение долгового периода определенного года
     * @throws Exception
     */
    @Test
    public void testFindByYear() throws Exception {
        service.findByYear(2010);
        verify(rentDAO).findByYearRent(2010);
    }

    /**
     * Получение всех долговых периодов в отсортированном порядке
     * @throws Exception
     */
    @Test
    public void testFindAll() throws Exception {
        service.findAll();
        verify(rentDAO).findAll(any(Sort.class));
    }

    /**
     * Тестирование метода создания начислений для каждого гаража.
     * Case: Владелец не является членом правления
     *       Не является льготником
     *       Не имелось начислений прошлых периодов
     *
     * @throws Exception
     */
    @Test
    public void testCreateNewPeriod() throws Exception{
        Rent rent = new Rent();
        rent.setYearRent(2017);
        rent.setContributeMax(1000);
        rent.setContLandMax(200);
        rent.setContTargetMax(1000);
        Garag garag = new Garag();
        Person person = new Person();
        person.setBenefits("");
        garag.setPerson(person);
        List<Garag> garagList = new ArrayList<>();
        garagList.add(garag);

        Payment payment = new Payment();
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        given(garagService.getGarags()).willReturn(garagList);
        given(paymentService.getPaymentOnGarag(garag)).willReturn(paymentList);
        service.createNewPeriod(rent);
        Contribution contribution = garag.getContributions().get(0);
        assertEquals(contribution.getYear(),Integer.valueOf(2017));
        assertEquals(contribution.getContribute(),1000f);
        assertEquals(contribution.getContLand(),200f);
        assertEquals(contribution.getContTarget(),1000f);
        assertFalse(contribution.isMemberBoardOn());
        assertFalse(contribution.isBenefitsOn());
        verify(garagService).save(garag);
        verify(paymentService).pay(payment,true,"default");
    }

    /**
     * Тестирование метода создания начислений для каждого гаража.
     * Case: Владелец не является членом правления
     *       Не является льготником
     *       Не имелось начислений прошлых периодов
     *
     * @throws Exception
     */
    @Test
    public void testCreateNewPeriodMemberBenefitsContributionsExist() throws Exception{
        Rent rent = new Rent();
        rent.setYearRent(2017);
        rent.setContributeMax(1000);
        rent.setContLandMax(200);
        rent.setContTargetMax(1000);
        Garag garag = new Garag();
        Person person = new Person();
        person.setBenefits("п/у");
        person.setMemberBoard(true);
        garag.setPerson(person);
        List<Garag> garagList = new ArrayList<>();
        garagList.add(garag);

        List<Contribution> contributionList = new ArrayList<>();
        Contribution contributionOld = new Contribution();
        contributionOld.setYear(2016);
        contributionList.add(contributionOld);
        garag.setContributions(contributionList);

        Payment payment = new Payment();
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        given(garagService.getGarags()).willReturn(garagList);
        given(paymentService.getPaymentOnGarag(garag)).willReturn(paymentList);
        service.createNewPeriod(rent);
        assertEquals(garag.getContributions().size(),2);
        for (Contribution contribution : garag.getContributions()){
            if(contribution.getYear() == 2017){
                assertEquals(contribution.getYear(),Integer.valueOf(2017));
                assertEquals(contribution.getContribute(),0f);
                assertEquals(contribution.getContLand(),100f);
                assertEquals(contribution.getContTarget(),1000f);
                assertTrue(contribution.isMemberBoardOn());
                assertTrue(contribution.isBenefitsOn());
            }
        }
        verify(garagService).save(garag);
    }

}