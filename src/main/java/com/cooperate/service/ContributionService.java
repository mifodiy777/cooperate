package com.cooperate.service;

import com.cooperate.entity.Contribution;

import java.util.Calendar;


public interface ContributionService {

    void saveOrUpdate(Contribution contribution);

    Contribution getContributionByGaragAndYear(Integer garagId, Integer year);

    void updateFines();

    void onFines(Calendar cel);

}
