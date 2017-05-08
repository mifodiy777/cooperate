package com.cooperate.service;

import com.cooperate.dao.PersonDAO;
import com.cooperate.entity.Person;
import com.cooperate.service.PersonService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Кирилл on 25.03.2017.
 */
public class PersonServiceTest {

    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private PersonService service = new PersonService();

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        reset(personDAO);
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        Person person = new Person();
        service.saveOrUpdate(person);
        verify(personDAO).save(person);
    }

    @Test
    public void testDelete() throws Exception {
        Integer id = 1;
        service.delete(id);
        verify(personDAO).delete(id);
    }

    @Test
    public void testGetPersons() throws Exception {
        String fio = null;
        service.getPersons(fio);
        verify(personDAO).findTop30By();
        fio = "fio";
        service.getPersons(fio);
        verify(personDAO).findByPersonfio(fio);
    }

    @Test
    public void testGetMembers() throws Exception {
        service.getMembers();
        verify(personDAO).findByMemberBoard(true);
    }

    @Test
    public void testGetPerson() throws Exception {
        Integer id = 1;
        service.getPerson(id);
        verify(personDAO).findOne(id);
    }

    @Test
    public void testFindByfio() throws Exception {
        service.findByfio("fio");
        verify(personDAO).findByPersonfio("fio");
    }

}