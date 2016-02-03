package com.cooperate.dao;

import com.cooperate.entity.JournalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;

@Repository
public interface JournalHistoryDAO extends JpaRepository<JournalHistory, Integer> {

    //Метод возвращает записи в журнале старше выбранной даты
    @Query("select h from JournalHistory h where h.dateEvent <= :dateEvent ")
    Iterable <JournalHistory> getEventMoreTwoYear(@Param("dateEvent") Calendar dateEvent);

}
