package com.cooperate.service;

import com.cooperate.dao.PersonDAO;
import com.cooperate.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
@Service
public class PersonService {

    @Autowired
    private PersonDAO personDAO;

    /**
     * Сохранение владельца
     * @param person Владелец
     * @return владелец
     */
    @Transactional
    public Person saveOrUpdate(Person person) {
        return personDAO.save(person);
    }

    /**
     * Удаление владельца
     * @param id Идентификатор владельца
     */
    @Transactional
    public void delete(Integer id) {
        personDAO.delete(id);
    }

    /**
     * Получение списка владельцев из базы по определенному паттерну или любые 30 владельцев
     * @param fio Паттерны - часть ФИО
     * @return Список владельцев
     */
    public List<Person> getPersons(String fio) {
        if (fio != null && !fio.isEmpty()) {
            return personDAO.findByPersonfio(fio);
        }
        return personDAO.findTop30By();
    }

    /**
     * Получение списка членов правления
     * @return список членов правления
     */
    public List<Person> getMembers() {
        return personDAO.findByMemberBoard(true);
    }

    /**
     * Получение владельца по идентификатору
     * @param id идентификатор владельца
     * @return владелец
     */
    public Person getPerson(Integer id) {
        return personDAO.findOne(id);
    }

    /**
     * Получение списка владельце по части ФИО
     * @param fio часть или полностью ФИО
     * @return Список владельцев
     */
    public List<Person> findByfio(String fio) {
        return personDAO.findByPersonfio(fio);
    }



}

