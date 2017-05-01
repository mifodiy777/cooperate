package com.cooperate.service;

import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.cooperate.exception.ExistGaragException;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
public interface GaragService {

    Garag saveOrUpdate(Garag garag) throws ExistGaragException;

    Garag save(Garag garag);

    void delete(Integer id);

    List<Garag> getGarags();

    List<String> getSeries();

    List<Garag> findBySeries(String series);

    Garag getGarag(Integer id);

    Float sumContribution(Garag garag);

    void changePerson(Garag garag, Person person, Boolean searchPerson, Boolean deletePerson, Integer oldPersonId, Boolean oneGarag, String reason);

}
