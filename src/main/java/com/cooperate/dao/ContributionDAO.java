package com.cooperate.dao;

import com.cooperate.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionDAO extends JpaRepository<Contribution, Integer> {

    List<Contribution> findByFinesOn(Boolean finesOn);

    List<Contribution> findByFinesOnAndYear(Boolean finesOn, Integer year);

    @Query("select c from Garag g inner join g.contributions c where g.id = :garagId and c.year = :year ")
    Contribution getContributionByGaragAndYear(@Param("garagId") Integer garagId,
                                               @Param("year") Integer year);

}
