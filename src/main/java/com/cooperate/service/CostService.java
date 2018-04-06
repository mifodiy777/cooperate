package com.cooperate.service;

import com.cooperate.dao.CostDAO;
import com.cooperate.dao.CostTypeDAO;
import com.cooperate.entity.Cost;
import com.cooperate.entity.CostType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @return Список типов расходов
     */
    public List<CostType> getTypes() {
        return typeDAO.findAll();
    }

    /**
     * Метод добавления расходов
     *
     * @param cost расход
     * @return расход
     */
    @Transactional
    public Cost saveCost(Cost cost) throws DataAccessException {
        if (cost.getType().getId() != null) {
            //detached
            cost.setType(typeDAO.findOne(cost.getType().getId()));
        }
        costDAO.save(cost);
        return cost;
    }

    /**
     * Получить список всех расходов
     *
     * @return расходы
     */
    public List<Cost> getAll() {
        return costDAO.findAll();
    }

    /**
     * Удаление расхода
     *
     * @param id ID расхода
     */
    @Transactional
    public void delete(Integer id) {
        costDAO.delete(id);
    }

}
