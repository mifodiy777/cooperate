package com.cooperate.service;

import com.cooperate.dao.CustomDAO;
import com.cooperate.dao.GaragDAO;
import com.cooperate.entity.Address;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.cooperate.exception.ExistGaragException;
import com.cooperate.service.GaragService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Created by Кирилл on 31.03.2017.
 */
public class GaragServiceTest {

    @Mock
    private GaragDAO garagDAO;

    @Mock
    private CustomDAO customDAO;

    @Mock
    private PersonService personService;

    @Mock
    private HistoryGaragService historyGaragService;

    @InjectMocks
    private GaragService service = new GaragService();


    @BeforeClass
    public void init() throws Exception {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        reset(garagDAO);
    }

    @Test(expectedExceptions = ExistGaragException.class)
    public void testSaveOrUpdateExcept() throws Exception {
        Garag garag = new Garag();
        garag.setSeries("1");
        garag.setNumber("1");
        given(customDAO.existGarag(garag)).willReturn(Boolean.TRUE);
        service.saveOrUpdate(garag);
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        Garag garag = new Garag();
        garag.setSeries("1");
        garag.setNumber("1");
        given(customDAO.existGarag(garag)).willReturn(Boolean.FALSE);
        service.saveOrUpdate(garag);
        verify(garagDAO).save(garag);
    }

    @Test
    public void testSaveOrUpdateIsNewPersonIsOld() throws Exception {
        Garag garag = spy(new Garag());
        Person person = new Person();
        person.setId(1);
        garag.setPerson(person);
        given(customDAO.existGarag(garag)).willReturn(Boolean.FALSE);
        given(garagDAO.save(garag)).willReturn(garag);
        service.saveOrUpdate(garag);
    }

    @Test
    public void testSaveOrUpdateIsNew() throws Exception {
        Garag garag = spy(new Garag());
        garag.setId(1);
        Person person = new Person();
        person.setId(1);
        garag.setPerson(person);
        given(garagDAO.findOne(garag.getId())).willReturn(garag);
        service.saveOrUpdate(garag);
        verify(garag).setContributions(null);
        verify(garag).setPayments(null);
        verify(garag).setHistoryGarags(null);
    }

    @Test
    public void testSave() throws Exception {
        Garag garag = new Garag();
        service.save(garag);
        verify(garagDAO).save(garag);
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

    /**
     * Case: Поиска владельца из базы не было, Старого владельца не удалять.
     * У Владельца был один гараж
     *
     * @throws Exception
     */
    @Test
    public void testChangePersonOneGaragNotDeleteNotSearch() throws Exception {
        Garag garag = new Garag();
        Person oldPerson = mock(Person.class);
        given(oldPerson.getFIO()).willReturn("Иванов Иван Иваныч");
        garag.setPerson(oldPerson);
        Person newPerson = new Person();
        newPerson.setFatherName("FatherName");
        newPerson.setLastName("LastName");
        newPerson.setName("Name");
        newPerson.setId(1);
        Address address = new Address();
        address.setId(1);
        newPerson.setAddress(address);
        String reason = "Причина";
        service.changePerson(garag, newPerson, false, false, 1, true, reason);
        assertNull(newPerson.getId());
        assertNull(newPerson.getAddress().getId());
        verify(historyGaragService).saveReason(reason, "Иванов Иван Иваныч", garag);
        assertEquals(garag.getPerson(), newPerson);
        verify(garagDAO).save(garag);
    }

    /**
     * Case: Поиска владельца из базы не было, Старого владельца удалять.
     * У Владельца было много гаражей
     *
     * @throws Exception
     */
    @Test
    public void testChangePersonManyGaragDeleteNotSearch() throws Exception {
        Garag garag = new Garag();
        Person person = new Person();
        List<Garag> garagList = new ArrayList<>();
        garagList.add(garag);
        person.setId(100);
        person.setFatherName("FatherName");
        person.setLastName("LastName");
        person.setName("Name");
        person.setGaragList(garagList);
        garag.setPerson(person);
        String reason = "Причина";
        service.changePerson(garag, person, false, true, 1, false, reason);
        verify(historyGaragService).saveReason(reason, "LastName Name FatherName", garag);
        verify(personService).saveOrUpdate(person);
        assertNotNull(person.getId());
        assertNotNull(person.getGaragList());
        for (Garag g : person.getGaragList()) {
            assertEquals(person, g.getPerson());
        }
    }

    /**
     * Case: Поиска владельца из базы, Старого владельца удалять.
     * У Владельца было много гаражей
     *
     * @throws Exception
     */
    @Test
    public void testChangePersonManyGaragDeleteSearch() throws Exception {
        Garag garag = new Garag();

        Person oldPerson = new Person();
        List<Garag> garagList = new ArrayList<>();
        garagList.add(garag);
        oldPerson.setGaragList(garagList);
        oldPerson.setId(100);
        oldPerson.setFatherName("oldFatherName");
        oldPerson.setLastName("oldLastName");
        oldPerson.setName("oldName");
        garag.setPerson(oldPerson);

        Person newPerson = new Person();
        newPerson.setName("newPerson");

        String reason = "Причина";
        Integer oldPersonId = 100;
        given(personService.getPerson(oldPersonId)).willReturn(oldPerson);
        given(personService.saveOrUpdate(newPerson)).willReturn(newPerson);

        service.changePerson(garag, newPerson, true, true, oldPersonId, false, reason);

        verify(personService).getPerson(oldPersonId);
        verify(historyGaragService).saveReason(reason,"oldLastName oldName oldFatherName",garag);
        verify(personService).saveOrUpdate(newPerson);
        verify(garagDAO).save(garag);
        assertEquals(garag.getPerson(),newPerson);
        verify(personService).delete(oldPersonId);

    }
}