package com.cooperate.dao;

import com.cooperate.entity.Garag;
import org.springframework.stereotype.Repository;

/**
 * Created by Кирилл on 01.04.2017.
 */
public interface CustomDAO {

    Float getSumContribution(Integer id);

    Boolean existGarag(Garag garag);
}
