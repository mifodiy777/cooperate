package com.cooperate.dao;

import com.cooperate.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ContributionDAO extends JpaRepository<Contribution, Integer> {

    List<Contribution> findByFinesOn(Boolean finesOn);

    List<Contribution> findByFinesOnAndYear(Boolean finesOn, Integer year);

}
