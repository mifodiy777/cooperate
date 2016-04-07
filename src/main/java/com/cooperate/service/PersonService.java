package com.cooperate.service;

import com.cooperate.entity.Person;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
public interface PersonService {

    Person saveOrUpdate(Person person);

    void delete(Integer id);

    List<Person> getPersons();

    List<Person> getMembers();

    Person getPerson(Integer id);

    List<Person> findByfio(String pattern);

    Person getByFio(Person person);

}
