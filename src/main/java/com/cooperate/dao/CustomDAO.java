package com.cooperate.dao;

import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;

import java.util.List;

/**
 * Created by Кирилл on 01.04.2017.
 */
public interface CustomDAO {

    Float getSumContribution(Integer id);

    Boolean existGarag(Garag garag);

    List<Contribution> findContributionsByFines(Integer year);
}
