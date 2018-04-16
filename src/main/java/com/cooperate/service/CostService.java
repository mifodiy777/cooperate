package com.cooperate.service;

import com.cooperate.dao.CostDAO;
import com.cooperate.dao.CostTypeDAO;
import com.cooperate.dao.CustomDAO;
import com.cooperate.dto.CostDTO;
import com.cooperate.entity.Cost;
import com.cooperate.entity.CostType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
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

    @Autowired
    private CustomDAO customDAO;

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
     * Метод получения типа расхода
     *
     * @param id идентифиактор типа расхода
     * @return тип расхода
     */
    public CostType getType(Integer id) {
        return typeDAO.getOne(id);
    }

    /**
     * Метод сохраниения типа расхода
     *
     * @param type Тип расхода
     * @return тип расхода
     */
    @Transactional
    public CostType saveType(CostType type) {
        return typeDAO.save(type);
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

    /**
     * Удаление типа расхода
     *
     * @param id ID типа расхода
     */
    @Transactional
    public void deleteType(Integer id) {
        customDAO.deleteCostType(id);
    }

    /**
     * Получить список объектов для отчета по типу расхода за период
     *
     * @param start начало периода
     * @param end   конец периода
     * @return список CostDTO
     */
    public List<CostDTO> findGroupCost(Calendar start, Calendar end) {
        return customDAO.findGroupCost(start, end);
    }

    /**
     * Проверка на уникальность
     * @param type Тип расхода
     * @return true - не уникально
     */
    public boolean existType(CostType type) {
        return customDAO.existCostType(type);
    }

    ;

}
