package com.cooperate.dao;

import com.cooperate.dto.CostDTO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.CostType;
import com.cooperate.entity.Garag;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Кирилл on 01.04.2017.
 */
public interface CustomDAO {

    Float getSumContribution(Integer id);

    Boolean existGarag(Garag garag);

    List<Contribution> findContributionsByFines(Integer year);

    List<CostDTO> findGroupCost(Calendar start, Calendar end);

    void deleteCostType(Integer id);

    boolean existCostType(CostType type);
}
