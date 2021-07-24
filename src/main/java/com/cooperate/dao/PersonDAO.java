package com.cooperate.dao;


import com.cooperate.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */

@Repository
public interface PersonDAO extends JpaRepository<Person, Integer> {

    /**
     * Поиск владельцев по ФИО или части ФИО
     * @param fio ФИО
     * @return Список подходящих владельцев
     */
    @Query("select distinct p from Person p where lower(concat(p.lastName,' ',p.name,' ',p.fatherName)) like lower(concat('%', :fio ,'%'))")
    List<Person> findByPersonfio(@Param("fio") String fio);

    /**
     * Список членов без гаража
     * @return список владельцев
     */
    List<Person> findByGaragListIsNull();

    /**
     * Список членов правления
     * @param memberBoard член  правления - true
     * @return список владельцев
     */
    List<Person> findByMemberBoard(Boolean memberBoard);

    /**
     * Метод получения случайных 30 владельцев
     * @return список владельцев
     */
    List<Person> findTop30By();

}
