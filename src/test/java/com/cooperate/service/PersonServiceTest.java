package com.cooperate.service;

import com.cooperate.dao.PersonDAO;
import com.cooperate.entity.Person;
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

    /**
     * Сохранение владельца
     * @throws Exception
     */
    @Test
    public void testSaveOrUpdate() throws Exception {
        Person person = new Person();
        service.saveOrUpdate(person);
        verify(personDAO).save(person);
    }

    /**
     * Удаление владельца
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        Integer id = 1;
        service.delete(id);
        verify(personDAO).delete(id);
    }

    /**
     * Получение владельцев по fio
     * case 1: fio пусто - получение 30 любых владельцев
     * case 2: fio заполненно - получение списка владельцев по схожим буквам
     * @throws Exception
     */
    @Test
    public void testGetPersons() throws Exception {
        String fio = null;
        service.getPersons(fio);
        verify(personDAO).findTop30By();
        fio = "fio";
        service.getPersons(fio);
        verify(personDAO).findByPersonfio(fio);
    }

    /**
     * Получение списка членов правления
     * @throws Exception
     */
    @Test
    public void testGetMembers() throws Exception {
        service.getMembers();
        verify(personDAO).findByMemberBoard(true);
    }

    /**
     * Получение владельца по идентификатору
     * @throws Exception
     */
    @Test
    public void testGetPerson() throws Exception {
        Integer id = 1;
        service.getPerson(id);
        verify(personDAO).findOne(id);
    }

    /**
     * Получение списка владельце по части ФИО
     * @throws Exception
     */
    @Test
    public void testFindByfio() throws Exception {
        service.findByfio("fio");
        verify(personDAO).findByPersonfio("fio");
    }

}