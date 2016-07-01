package com.cooperate.service;

import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;

import java.util.List;

public interface ContributionService {

    void saveOrUpdate(Contribution contribution);

    Contribution getContributionByGaragAndYear(Integer garagId, Integer year);

    void updateFines();



    void onFines();



}
