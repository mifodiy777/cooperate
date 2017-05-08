package com.cooperate.dao;

import com.cooperate.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionDAO extends JpaRepository<Contribution, Integer> {

    /**
     * Получение всех долговых периодов в зависимости от параметра "Вкл/Выкл пени"
     * @param finesOn Вкл/Выкл пени
     * @return список долговых периодов
     */
    List<Contribution> findByFinesOn(Boolean finesOn);

    /**
     * Получение списка долговых периодов
     * @param finesOn Вкл/Выкл пени
     * @param year год периодов
     * @return список долговых периодов
     */
    List<Contribution> findByFinesOnAndYear(Boolean finesOn, Integer year);

    /**
     * Получение долгового периода определенного гаража и года
     * @param garagId Гараж
     * @param year Год
     * @return договой период
     */
    @Query("select c from Garag g inner join g.contributions c where g.id = :garagId and c.year = :year ")
    Contribution getContributionByGaragAndYear(@Param("garagId") Integer garagId,
                                               @Param("year") Integer year);

}
