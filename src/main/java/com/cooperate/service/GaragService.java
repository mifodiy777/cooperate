package com.cooperate.service;

import com.cooperate.entity.Garag;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Calendar;
import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
public interface GaragService {

    Garag saveOrUpdate(Garag garag);

    void delete(Integer id);

    List<Garag> getGarags();

    List<String> getSeries();

    public List<Garag> findBySeries(String series);

    List<Garag> getGaragForPersonBenefits();

    Garag getGarag(Integer id);

    Boolean existGarag(Garag garag);

    Float sumContribution(Garag garag);  

    HSSFWorkbook reportAll();

    HSSFWorkbook reportBenefitsPerson();

    HSSFWorkbook reportContribute();

    HSSFWorkbook reportProfit(Calendar start, Calendar end);


}
