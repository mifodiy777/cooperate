package com.cooperate.service.impl;

import com.cooperate.dao.CustomDAO;
import com.cooperate.dao.GaragDAO;
import com.cooperate.entity.*;
import com.cooperate.service.GaragService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Сервис по работе с гаражами
 * Created by KuzminKA on 17.06.2015.
 */
@Service
public class GaragServiceImpl implements GaragService {

    @Autowired
    private GaragDAO garagDAO;

    @Autowired
    private CustomDAO customDAO;

    /**
     * Метод сохранения гаражей в базе
     *
     * @param garag Гараж с заполненными данными
     * @return Сохраненный гараж с заполненным id
     */
    @Override
    @Transactional
    public Garag saveOrUpdate(Garag garag) {
        if (garag.getId() == null && garag.getPerson() != null) {
            if (garag.getPerson().getId() != null) {
                Person p = garag.getPerson();
                garag.setPerson(null);
                Garag getGarag = garagDAO.save(garag);
                getGarag.setPerson(p);
                return garagDAO.save(getGarag);
            }
        }
        return garagDAO.save(garag);
    }

    /**
     * Метод удаления гаража
     *
     * @param id ID гаража
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        garagDAO.delete(id);
    }

    /**
     * Метод получения всех гаражей
     *
     * @return Коллекция со всеми гаражами
     */
    @Override
    public List<Garag> getGarags() {
        return garagDAO.findAll();
    }

    /**
     * Метод получения всех имеющихся рядов
     *
     * @return ряды гаражного кооператива
     */
    @Override
    public List<String> getSeries() {
        return garagDAO.getSeries();
    }

    /**
     * Получение всех гаражей определенного ряда
     *
     * @param series Ряд
     * @return Коллекция гаражей
     */
    @Override
    public List<Garag> findBySeries(String series) {
        return garagDAO.findBySeries(series);
    }

    /**
     * Получение гаража по идентификатору
     *
     * @param id ID
     * @return Гараж
     */
    @Override
    public Garag getGarag(Integer id) {
        return garagDAO.findOne(id);
    }

    /**
     * Метод вычисления суммы общего долга по определенному гаражу
     *
     * @param garag Гараж
     * @return Сумма долга
     */
    @Override
    public Float sumContribution(Garag garag) {
        return customDAO.getSumContribution(garag.getId());
    }

    /**
     * Метод определения существования гаража в базе
     *
     * @param garag Гараж
     * @return true - гараж существует
     */
    @Override
    //todo менял метод - проверить
    public Boolean existGarag(Garag garag) {
        if (garag.getId() == null) {
            return garagDAO.countBySeriesAndNumber(garag.getSeries(), garag.getNumber()) != 0;
        } else {
            return garagDAO.countBySeriesAndNumberAndIdNot(garag.getSeries(), garag.getNumber(), garag.getId()) != 0;
        }
    }

}

