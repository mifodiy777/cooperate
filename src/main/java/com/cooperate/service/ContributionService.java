package com.cooperate.service;

import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;

import java.util.List;

public interface ContributionService {

    void saveOrUpdate(Contribution contribution);

    void delete(Integer id);

    List<Contribution> getContributionOnGarag(Garag garag);

    void updateFines();

    void onFines();

}
