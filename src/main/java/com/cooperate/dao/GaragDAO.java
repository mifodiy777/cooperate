package com.cooperate.dao;

import com.cooperate.entity.Garag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
@Repository
public interface GaragDAO extends JpaRepository<Garag, Integer> {

    /**
     * Метод получает всех льготников
     * @return список гаражей у которых владельцы имеют льготы
     */
    @Query("select g from Garag g inner join g.person p where length(p.benefits) <> 0 ")
    List<Garag> getGaragForPersonBenefits();

    /**
     * Метод получает все ряды
     * @return список рядов
     */
    @Query("select distinct g.series from Garag g")
    List<String> getSeries();

    /**
     * Метод получает должников
     * @return должники
     */
    @Query("select distinct g from Garag g inner join g.person p inner join g.contributions c where g.oldContribute > 0 or c.contribute > 0 or c.contLand > 0 " +
            "or c.contTarget > 0 or c.fines > 0 ")
    List<Garag> getGaragDebt();

    /**
     * Нахождение гаражей выбранного ряда
     * @param series ряд
     * @return список гаражей
     */
    List<Garag> findBySeries(String series);

    /**
     * Нахождение гаражей выбранного ряда c назначенными владельцами
     * @param series ряд
     * @return список гаражей
     */
    @Query("select distinct g from Garag g inner join g.person p where g.series = :series")
    List<Garag> findBySeriesAndPerson(@Param("series") String series);

}
