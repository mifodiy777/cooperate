package com.cooperate.service.impl;

import com.cooperate.dao.CustomDAO;
import com.cooperate.dao.GaragDAO;
import com.cooperate.entity.Garag;
import com.cooperate.service.GaragService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

/**
 * Created by Кирилл on 31.03.2017.
 */
public class GaragServiceImplTest {

    @Mock
    private GaragDAO garagDAO;

    @Mock
    private CustomDAO customDAO;

    @InjectMocks
    private GaragService service = new GaragServiceImpl();


    @BeforeClass
    public void init() throws Exception {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        reset(garagDAO);
    }

    @Test
    public void testSaveOrUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {
        Integer id = 1;
        service.delete(id);
        verify(garagDAO).delete(id);
    }

    @Test
    public void testGetGarags() throws Exception {
        service.getGarags();
        verify(garagDAO).findAll();
    }

    @Test
    public void testGetSeries() throws Exception {
        service.getSeries();
        verify(garagDAO).getSeries();
    }

    @Test
    public void testFindBySeries() throws Exception {
        String series = "1";
        service.findBySeries(series);
        verify(garagDAO).findBySeries(series);
    }

    @Test
    public void testGetGarag() throws Exception {
        Integer id = 1;
        service.getGarag(id);
        verify(garagDAO).findOne(id);
    }

    @Test
    public void testSumContribution() throws Exception {
        Garag garag = new Garag();
        garag.setId(1);
        service.sumContribution(garag);
        verify(customDAO).getSumContribution(garag.getId());
    }
}