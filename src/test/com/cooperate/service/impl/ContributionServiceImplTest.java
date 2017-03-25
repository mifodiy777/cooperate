package com.cooperate.service.impl;

import com.cooperate.dao.ContributionDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.service.ContributionService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

/**
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

    @Test
    public void testSaveOrUpdate() throws Exception {
        Contribution contribution = new Contribution();
        contributionService.saveOrUpdate(contribution);
        verify(contributionDAO).save(contribution);
    }

    @Test
    public void testGetContributionByGaragAndYear() throws Exception {
        contributionService.getContributionByGaragAndYear(1, 2017);
        verify(contributionDAO).getContributionByGaragAndYear(1, 2017);
    }

    @Test
    public void testUpdateFines() throws Exception {

    }

    @Test
    public void testOnFines() throws Exception {

    }

}