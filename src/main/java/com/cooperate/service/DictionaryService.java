package com.cooperate.service;

import com.cooperate.dao.DictionaryDAO;
import com.cooperate.entity.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис по работе со справочниками
 */
@Service
public class DictionaryService {

    @Autowired
    private DictionaryDAO dictionaryDAO;

    /**
     * Метод получения списка справочных элементов
     */
    public List<Dictionary> getDictionary() {
        return dictionaryDAO.findAll();
    }

    /**
     * Метод получения значения справочного элемента
     */
    public String findByName(String name) {
        return dictionaryDAO.findByName(name).getValue();
    }

    /**
     * Метод добавления справочников
     */
    @Transactional
    public Dictionary save(Dictionary dictionary) throws DataAccessException {
        dictionaryDAO.save(dictionary);
        return dictionary;
    }


    public Dictionary getItem(Integer id) {
        return dictionaryDAO.findOne(id);
    }
}
