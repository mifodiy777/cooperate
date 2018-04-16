package com.cooperate.dao;

import com.cooperate.entity.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

/**
 * Класс по работе с базой данных через объект Cost
 */
@Repository
public interface CostDAO extends JpaRepository<Cost, Integer> {

    /**
     * Список расходов в определенный промежуток времени
     * @param start начало периода
     * @param end конец периода
     * @return список расходов
     */
    @Query("select distinct с from Cost с where с.date BETWEEN :start and :end order by date ASC ")
    List<Cost> findByDateBetween(@Param("start") Calendar start, @Param("end") Calendar end);

}
