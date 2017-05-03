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

    @Transactional
    public Person saveOrUpdate(Person person) {
        return personDAO.save(person);
    }

    @Transactional
    public void delete(Integer id) {
        personDAO.delete(id);
    }

    public List<Person> getPersons(String fio) {
        if (fio != null && !fio.isEmpty()) {
            return personDAO.findByPersonfio(fio);
        }
        return personDAO.findTop30By();
    }

    public List<Person> getMembers() {
        return personDAO.findByMemberBoard(true);
    }

    public Person getPerson(Integer id) {
        return personDAO.findOne(id);
    }

    //Список владельцев по ФИО
    public List<Person> findByfio(String fio) {
        return personDAO.findByPersonfio(fio);
    }

    //Определенный владелец по ФИО
    public Person getByFio(Person person) {
        return personDAO.findByLastNameAndNameAndFatherName(person.getLastName(),
                person.getName(), person.getFatherName());
    }


}

