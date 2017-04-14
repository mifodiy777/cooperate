package com.cooperate.service.impl;

import com.cooperate.dao.RentDAO;
import com.cooperate.entity.Rent;
import com.cooperate.service.RentService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertTrue;

/**
 * Created by Кирилл on 25.03.2017.
 */
public class RentServiceImplTest {

    @Mock
    private RentDAO rentDAO;

    @InjectMocks
    private RentService service = new RentServiceImpl();

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        reset(rentDAO);
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        Rent rent = new Rent();
        service.saveOrUpdate(rent);
        verify(rentDAO).save(rent);
    }

    @Test
    public void testCheckRent() throws Exception {
        given(rentDAO.countByYearRent(2010)).willReturn(0);
        assertTrue(service.checkRent(2010));
    }

    @Test
    public void testGetRents() throws Exception {
        service.getRents();
        verify(rentDAO).findAll();
    }

    @Test
    public void testCreateNewPeriod() throws Exception {

    }

    @Test
    public void testFindByYear() throws Exception {
        service.findByYear(2010);
        verify(rentDAO).findByYearRent(2010);
    }

    @Test
    public void testFindAll() throws Exception {
        service.findAll();
        verify(rentDAO).findAll(any(Sort.class));
    }

}