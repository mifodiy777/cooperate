package com.cooperate.service.impl;

import com.cooperate.dao.PersonDAO;
import com.cooperate.entity.Person;
import com.cooperate.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonDAO personDAO;

    @Override
    @Transactional
    public Person saveOrUpdate(Person person) {
        return personDAO.save(person);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        personDAO.delete(id);
    }

    @Override
    public List<Person> getPersons(String fio) {
        if (fio != null && !fio.isEmpty()) {
            personDAO.findByPersonfio(fio);
        }
        return personDAO.findTop30By();
    }

    @Override
    public List<Person> getMembers() {
        return personDAO.findByMemberBoard(true);
    }

    @Override
    public Person getPerson(Integer id) {
        return personDAO.findOne(id);
    }

    //Список владельцев по ФИО
    @Override
    public List<Person> findByfio(String fio) {
        return personDAO.findByPersonfio(fio);
    }

    @Override
    //Определенный владелец по ФИО
    public Person getByFio(Person person) {
        return personDAO.findByLastNameAndNameAndFatherName(person.getLastName(),
                person.getName(), person.getFatherName());
    }


}

