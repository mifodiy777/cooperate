package com.cooperate.dao;

import com.cooperate.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Класс по работе с базой данных через объект Dictionary
 */
@Repository
public interface DictionaryDAO extends JpaRepository<Dictionary, Integer> {

    Dictionary findByName(String name);

}
