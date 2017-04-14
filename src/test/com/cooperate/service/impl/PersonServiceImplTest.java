package com.cooperate.service.impl;

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
import static org.testng.Assert.*;

/**
 * Created by Кирилл on 25.03.2017.
 */
public class PersonServiceImplTest {

    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private PersonService service = new PersonServiceImpl();

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
        service.getPersons();
        verify(personDAO).findAll();
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

    @Test
    public void testGetByFio() throws Exception {
        Person person = new Person();
        person.setLastName("Ivanov");
        person.setName("Ivan");
        person.setFatherName("Ivanovich");
        service.getByFio(person);
        verify(personDAO).findByLastNameAndNameAndFatherName("Ivanov", "Ivan", "Ivanovich");
    }

}