package com.cooperate.service;

import com.cooperate.dao.CostDAO;
import com.cooperate.dao.CostTypeDAO;
import com.cooperate.entity.Cost;
import com.cooperate.entity.CostType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис по работе с расходами
 */
@Service
public class CostService {

    @Autowired
    private CostDAO costDAO;

    @Autowired
    private CostTypeDAO typeDAO;

    /**
     * Метод получения списка типов расходов
     *
     * @param type - начало наименования типа расходов
     * @return Список типов расходов
     */
    public List<CostType> getTypes(String type) {
        return typeDAO.findByNameStartingWith(type);
    }

    /**
     * Метод добавления расходов
     *
     * @param cost расход
     * @return расход
     */
    public Cost saveCost(Cost cost) {
        return costDAO.save(cost);
    }

    /**
     * Получить список всех расходов
     *
     * @return расходы
     */
    public List<Cost> getAll() {
        return costDAO.findAll();
    }
}
