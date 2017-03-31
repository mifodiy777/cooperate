package com.cooperate.service;

import com.cooperate.entity.Garag;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
public interface GaragService {

    Garag saveOrUpdate(Garag garag);

    void delete(Integer id);

    List<Garag> getGarags();

    List<String> getSeries();

    List<Garag> findBySeries(String series);

    Garag getGarag(Integer id);

    Boolean existGarag(Garag garag);

    Float sumContribution(Garag garag);  

}
