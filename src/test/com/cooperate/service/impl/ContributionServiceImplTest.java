package com.cooperate.service.impl;

import com.cooperate.dao.ContributionDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.service.ContributionService;
import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

/**
 * Тестирование сервиса долговых периодов
 * Created by Кирилл on 22.03.2017.
 */
public class ContributionServiceImplTest {

    @InjectMocks
    private ContributionService contributionService = new ContributionServiceImpl();

    @Mock
    private ContributionDAO contributionDAO;


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
        verify(contributionDAO).save(contribution);
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
        Contribution spyContribution = spy(contribution);
        List<Contribution> list = new ArrayList<>();
        list.add(spyContribution);
        given(contributionDAO.findByFinesOn(true)).willReturn(list);
        given(spyContribution.getSumFixed()).willReturn(1000f);
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_YEAR, -60);
        spyContribution.setFinesLastUpdate(calendar);
        contributionService.updateFines();
        Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.roll(Calendar.DAY_OF_YEAR, -10);
        assertEquals(lastUpdate.get(Calendar.DAY_OF_YEAR), spyContribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        verify(spyContribution).setFines(50);
        verify(contributionDAO).save(spyContribution);
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
        Contribution spyContribution = spy(contribution);
        List<Contribution> list = new ArrayList<>();
        list.add(spyContribution);
        given(contributionDAO.findByFinesOn(true)).willReturn(list);
        given(spyContribution.getSumFixed()).willReturn(1000f);
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_YEAR, -60);
        spyContribution.setFinesLastUpdate(calendar);
        spyContribution.setFines(950);
        contributionService.updateFines();
        Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.roll(Calendar.DAY_OF_YEAR, -10);
        assertEquals(lastUpdate.get(Calendar.DAY_OF_YEAR), spyContribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        verify(spyContribution).setFinesOn(false);
        verify(spyContribution).setFines(1000);
        verify(contributionDAO).save(spyContribution);
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
        Contribution spyContribution = spy(contribution);
        List<Contribution> list = new ArrayList<>();
        list.add(spyContribution);
        given(contributionDAO.findByFinesOn(true)).willReturn(list);
        given(spyContribution.getSumFixed()).willReturn(1000f);
        DateTime dt = DateTime.now();
        spyContribution.setFinesLastUpdate( dt.minusDays(1060).toGregorianCalendar());
        spyContribution.setFines(0);
        contributionService.updateFines();
        Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.roll(Calendar.DAY_OF_YEAR, -10);
        assertEquals(lastUpdate.get(Calendar.DAY_OF_YEAR), spyContribution.getFinesLastUpdate().get(Calendar.DAY_OF_YEAR));
        verify(spyContribution).setFinesOn(false);
        verify(spyContribution).setFines(1000);
        verify(contributionDAO).save(spyContribution);
    }

    @Test
    public void testOnFines() throws Exception {

    }

}