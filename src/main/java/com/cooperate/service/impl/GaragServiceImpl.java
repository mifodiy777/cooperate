package com.cooperate.service.impl;

import com.cooperate.comparator.GaragComparator;
import com.cooperate.dao.GaragDAO;
import com.cooperate.entity.*;
import com.cooperate.service.ContributionService;
import com.cooperate.service.GaragService;
import com.cooperate.service.RentService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by KuzminKA on 17.06.2015.
 */
@Service
public class GaragServiceImpl implements GaragService {

    @Autowired
    private GaragDAO garagDAO;

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

    @Override
    @Transactional
    public void delete(Integer id) {
        garagDAO.delete(id);
    }

    @Override
    public List<Garag> getGarags() {
        return garagDAO.findAll();
    }

    @Override
    public List<String> getSeries() {
        return garagDAO.getSeries();
    }

    @Override
    public List<Garag> findBySeries(String series) {
        return garagDAO.findBySeries(series);
    }

    @Override
    public Garag getGarag(Integer id) {
        return garagDAO.findOne(id);
    }

    //Общая сумма долга
    @Override
    public Float sumContribution(Garag garag) {
        Float sum = garag.getOldContribute();
        for (Contribution c : garagDAO.getOne(garag.getId()).getContributions()) {
            sum += c.getSumFixed() + c.getFines();
        }
        return sum;
    }

    //Существует ли гараж
    @Override
    public Boolean existGarag(Garag garag) {
        if (garag.getId() == null) {
            return garagDAO.countBySeriesAndNumber(garag.getSeries(), garag.getNumber()) == 0;
        } else {
            return garagDAO.countBySeriesAndNumberAndIdNot(garag.getSeries(), garag.getNumber(), garag.getId()) == 0;
        }
    }

}

