package com.cooperate.service;

import com.cooperate.dao.ContributionDAO;
import com.cooperate.dao.CustomDAO;
import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Rent;
import com.cooperate.service.ContributionService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

/**
 * Тестирование сервиса долговых периодов
 * Created by Кирилл on 22.03.2017.
 */
public class ContributionServiceTest {

    @InjectMocks
    private ContributionService contributionService = new ContributionService();

    @Mock
    private ContributionDAO contributionDAO;

    @Mock
    private CustomDAO customDAO;

    @Mock
    private RentDAO rentDAO;


    @BeforeClass
    public void setUp() throws Exception {
        initMocks(this);
    }

    @BeforeMethod
    public void init() {
        reset(contributionDAO);
    }

    /**
     * Тест сохранения долговых периодов
     *
     * @throws Exception
     */
    @Test
    public void testSaveOrUpdate() throws Exception {
        Contribution contribution = new Contribution();
        contributionService.saveOrUpdate(contribution);
        verify(contributionDAO).saveAndFlush(contribution);
    }

    /**
     * Тест получения долгового периода для определенного гаража и определенного года
     *
     * @throws Exception
     */
    @Test
    public void testGetContributionByGaragAndYear() throws Exception {
        contributionService.getContributionByGaragAndYear(1, 2017);
        verify(contributionDAO).getContributionByGaragAndYear(1, 2017);
    }

    /**
     * Тест обнавления пеней при отсутсвии долгов, но включеным режимом начисления пеней
     *
     * @throws Exception
     */
    @Test
    public void testUpdateFinesForNullContribute() throws Exception {
        Contribution contribution = mock(Contribution.class);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);
        given(contributionDAO.findByFinesOn(true)).willReturn(list);
        given(contribution.getSumFixed()).willReturn(0f);
        contributionService.updateFines();
        verify(contribution).setFinesOn(false);
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тест обновления пеней
     * Case: newFines < sumContribute.
     * Устанавливаемые пени будут меньше суммы долга.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateFinesMin() throws Exception {
        Contribution contribution = new Contribution();
        contribution.setContribute(2400f);
        contribution.setContLand(200f);
        contribution.setFinesLastUpdate(DateTime.now().minusDays(21).toGregorianCalendar());
        contribution.setFinesOn(true);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        given(contributionDAO.findByFinesOn(true)).willReturn(list);

        contributionService.updateFines();

        assertEquals(DateTime.now().minusDays(2).getDayOfYear(), contribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        assertEquals(contribution.getFines(), 50);
        assertTrue(contribution.isFinesOn());
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тест обновления пеней
     * Case: newFines < sumContribute.
     * new Fines более чем 50
     * Устанавливаемые пени будут меньше суммы долга.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateFinesLs() throws Exception {
        Contribution contribution = new Contribution();
        contribution.setContribute(2400f);
        contribution.setContLand(200f);
        contribution.setFinesLastUpdate(DateTime.now().minusDays(106).toGregorianCalendar());
        contribution.setFinesOn(true);

        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        given(contributionDAO.findByFinesOn(true)).willReturn(list);

        contributionService.updateFines();

        assertEquals(DateTime.now().minusDays(10).getDayOfYear(), contribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        assertEquals(contribution.getFines(), 250);
        assertTrue(contribution.isFinesOn());
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тест обновления пеней
     * Case: newFines == sumContribute
     * Устанавливаемые пени будут равны сумме долга, после чего пени выключаются.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateFinesEquals() throws Exception {
        Contribution contribution = new Contribution();
        contribution.setContribute(2400f);
        contribution.setContLand(200f);
        contribution.setFinesLastUpdate(DateTime.now().minusDays(21).toGregorianCalendar());
        contribution.setFinesOn(true);
        contribution.setFines(2550);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        given(contributionDAO.findByFinesOn(true)).willReturn(list);

        contributionService.updateFines();

        assertEquals(DateTime.now().minusDays(2).getDayOfYear(), contribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        assertEquals(contribution.getFines(), 2600);
        assertFalse(contribution.isFinesOn());
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тест обновления пеней
     * Case: else
     * Устанавливаемые пени превышают сумму долга.
     * В пенях сумма будет равна сумме долга за текущий период
     *
     * @throws Exception
     */
    @Test
    public void testUpdateFinesElse() throws Exception {
        Contribution contribution = new Contribution();
        contribution.setContribute(2400f);
        contribution.setContLand(200f);
        contribution.setFinesLastUpdate(DateTime.now().minusDays(21).toGregorianCalendar());
        contribution.setFinesOn(true);
        contribution.setFines(2590);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        given(contributionDAO.findByFinesOn(true)).willReturn(list);

        contributionService.updateFines();

        assertEquals(DateTime.now().minusDays(2).getDayOfYear(), contribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        assertEquals(contribution.getFines(), 2590);
        assertFalse(contribution.isFinesOn());
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тестирование метода включения режима начисления пеней после нового года
     *
     * @throws Exception
     */
    @Test
    public void testOnFines() throws Exception {
        Calendar now = Calendar.getInstance();

        Contribution contribution = new Contribution();
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR) - 1)).willReturn(list);
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR))).willReturn(new ArrayList<>());

        contributionService.onFines(now);

        assertTrue(contribution.isFinesOn());
        assertEquals(now.get(Calendar.YEAR), contribution.getFinesLastUpdate().get(Calendar.YEAR));
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тестирование метода включения режима начисления пеней после июля месяца текущего года
     *
     * @throws Exception
     */
    @Test
    public void testOnFinesJulyBenefitFalse() throws Exception {

        Contribution contribution = new Contribution();
        contribution.setMemberBoardOn(false);
        contribution.setBenefitsOn(false);
        contribution.setContribute(2400f);
        contribution.setContLand(200f);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        Calendar now = new GregorianCalendar(2017,10,1);

        Rent rent = new Rent();
        rent.setYearRent(now.get(Calendar.YEAR));
        rent.setContributeMax(2400);
        rent.setContLandMax(200);


        given(rentDAO.findByYearRent(now.get(Calendar.YEAR))).willReturn(rent);
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR) - 1)).willReturn(new ArrayList<>());
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR))).willReturn(list);

        contributionService.onFines(now);

        assertTrue(contribution.isFinesOn());
        assertEquals(now.get(Calendar.YEAR) , contribution.getFinesLastUpdate().get(Calendar.YEAR));
        assertEquals(6 , contribution.getFinesLastUpdate().get(Calendar.MONTH));
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тестирование метода включения режима начисления пеней после июля месяца текущего года
     *
     * @throws Exception
     */
    @Test
    public void testOnFinesJulyBenefitTrue() throws Exception {
        Calendar now = new GregorianCalendar(2017,10,1);

        Contribution contribution = new Contribution();
        contribution.setMemberBoardOn(false);
        contribution.setBenefitsOn(true);
        contribution.setContribute(2400f);
        contribution.setContLand(100f);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        Rent rent = new Rent();
        rent.setYearRent(now.get(Calendar.YEAR));
        rent.setContributeMax(2400);
        rent.setContLandMax(200);


        given(rentDAO.findByYearRent(now.get(Calendar.YEAR))).willReturn(rent);
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR) - 1)).willReturn(new ArrayList<>());
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR))).willReturn(list);

        contributionService.onFines(now);

        assertTrue(contribution.isFinesOn());
        assertEquals(now.get(Calendar.YEAR) , contribution.getFinesLastUpdate().get(Calendar.YEAR));
        assertEquals(6 , contribution.getFinesLastUpdate().get(Calendar.MONTH));
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тестирование метода включения режима начисления пеней после июля месяца текущего года
     *
     * @throws Exception
     */
    @Test
    public void testOnFinesJulysMemberBoardOn() throws Exception {
        Calendar now = new GregorianCalendar(2017,10,1);

        Contribution contribution = new Contribution();
        contribution.setMemberBoardOn(true);
        contribution.setBenefitsOn(false);
        contribution.setContribute(0f);
        contribution.setContLand(200f);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        Rent rent = new Rent();
        rent.setYearRent(now.get(Calendar.YEAR));
        rent.setContributeMax(2400);
        rent.setContLandMax(200);


        given(rentDAO.findByYearRent(now.get(Calendar.YEAR))).willReturn(rent);
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR) - 1)).willReturn(new ArrayList<>());
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR))).willReturn(list);

        contributionService.onFines(now);

        assertTrue(contribution.isFinesOn());
        assertEquals(now.get(Calendar.YEAR) , contribution.getFinesLastUpdate().get(Calendar.YEAR));
        assertEquals(6 , contribution.getFinesLastUpdate().get(Calendar.MONTH));
        verify(contributionDAO).save(contribution);
    }

    /**
     * Тестирование метода включения режима начисления пеней после июля месяца текущего года
     *
     * @throws Exception
     */
    @Test
    public void testOnFinesJulysNotEq() throws Exception {
        Calendar now = Calendar.getInstance();

        Contribution contribution = new Contribution();
        contribution.setContribute(0f);
        contribution.setContLand(200f);
        contribution.setFinesOn(false);
        List<Contribution> list = new ArrayList<>();
        list.add(contribution);

        Rent rent = new Rent();
        rent.setYearRent(now.get(Calendar.YEAR));
        rent.setContributeMax(2400);
        rent.setContLandMax(200);


        given(rentDAO.findByYearRent(now.get(Calendar.YEAR))).willReturn(rent);
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR) - 1)).willReturn(new ArrayList<>());
        given(customDAO.findContributionsByFines(now.get(Calendar.YEAR))).willReturn(list);

        contributionService.onFines(now);

        assertFalse(contribution.isFinesOn());
        assertNull(contribution.getFinesLastUpdate());
    }


}